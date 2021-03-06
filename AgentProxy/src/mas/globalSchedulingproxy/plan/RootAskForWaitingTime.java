package mas.globalSchedulingproxy.plan;

import java.awt.TrayIcon.MessageType;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mas.globalSchedulingproxy.database.BatchDataBase;
import mas.globalSchedulingproxy.database.CustomerBatches;
import mas.globalSchedulingproxy.database.UnitBatchInfo;
import mas.globalSchedulingproxy.goal.GetNoOfMachinesGoal;
import mas.globalSchedulingproxy.gui.WebLafGSA;
import mas.jobproxy.Batch;
import mas.util.AgentUtil;
import mas.util.ID;
import mas.util.MessageIds;
import mas.util.ZoneDataUpdate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bdi4jade.core.BDIAgent;
import bdi4jade.core.BeliefBase;
import bdi4jade.message.MessageGoal;
import bdi4jade.plan.PlanBody;
import bdi4jade.plan.PlanInstance;
import bdi4jade.plan.PlanInstance.EndState;

/** Queries for waiting time from all Local scheduling agents.
 *  First it assigns operations to the batch based on its id and customer id from its database
 *  
 * @author Anand Prajapati
 *
 */
public class RootAskForWaitingTime extends Behaviour implements PlanBody {

	private static final long serialVersionUID = 1L;

	private Batch comingBatch;
	private AID blackboard;
	private int NoOfMachines;
	private String msgReplyID;
	private MessageTemplate mt;
	private int step = 0;
	private int MachineCount;
	protected Logger log;
	private ACLMessage[] WaitingTime;

	// The counter of replies from seller agents
	private int repliesCnt = 0; 
	private Batch JobToSend;
	private long CumulativeWaitingTime = 0;
	private BeliefBase bfBase;
	private int batchNumber;

	private WebLafGSA WeblafGSAgui;
	private CustomerBatches customerBatchInfo;

	@Override
	public void init(PlanInstance PI) {
		log = LogManager.getLogger();
		bfBase = PI.getBeliefBase();

		batchNumber = (int) bfBase.
				getBelief(ID.GlobalScheduler.BeliefBaseConst.batchCount).
				getValue();
		try {
			comingBatch = (Batch)((MessageGoal)PI.getGoal()).getMessage().getContentObject();
			
			if(comingBatch.getBatchNumber() == -1) {
				comingBatch.setBatchNumber(++ batchNumber);
				bfBase.updateBelief(ID.GlobalScheduler.BeliefBaseConst.batchCount, batchNumber);
			}
			
			log.info("batch no: " + batchNumber);
			msgReplyID = Integer.toString(comingBatch.getBatchNumber());

		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		blackboard = (AID) bfBase.
				getBelief(ID.GlobalScheduler.BeliefBaseConst.blackboardAgent).
				getValue();

		WeblafGSAgui = (WebLafGSA)bfBase.
				getBelief(ID.GlobalScheduler.BeliefBaseConst.GSA_GUI_instance).
				getValue();

		mt = MessageTemplate.and(
				MessageTemplate.MatchConversationId(MessageIds.msgWaitingTime),
				MessageTemplate.MatchReplyWith(msgReplyID));

		BatchDataBase batchDb = (BatchDataBase) bfBase.
				getBelief(ID.GlobalScheduler.BeliefBaseConst.batchDatabase).
				getValue();

		if(batchDb != null) {
			customerBatchInfo = batchDb.getBatchesInfo(comingBatch.getCustomerId());

			if(customerBatchInfo != null) {
				UnitBatchInfo bInfo = customerBatchInfo.getBatchInfo(comingBatch.getBatchId());
				comingBatch.setOperations(bInfo.getOperations());
				log.info("operations for batch : '" + comingBatch.getBatchId()+"' : " + bInfo);
			}
			else {
				step = 5;
				log.info("Database for " + comingBatch.getCustomerId() + " not found !!" );
				log.info("Rejecting the batch");
			}
		} else {
			log.debug("Customer database is missing");
		}
	}

	@Override
	public void action() {
		switch (step) {
		case 0:

			this.MachineCount = (int) bfBase.
			getBelief(ID.GlobalScheduler.BeliefBaseConst.NoOfMachines).
			getValue();

			if(MachineCount != 0) {
				step = 1;
			}
			else{
				((BDIAgent)myAgent).addGoal(new GetNoOfMachinesGoal());
			}
			break;

		case 1:

			ZoneDataUpdate update = new ZoneDataUpdate.Builder(ID.GlobalScheduler.ZoneData.GetWaitingTime)
			.value(comingBatch).setReplyWith(msgReplyID).Build();
			AgentUtil.sendZoneDataUpdate(blackboard, update, myAgent);
			WaitingTime = new ACLMessage[MachineCount];

			step = 2;
			break;

		case 2:
			try {
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					WaitingTime[repliesCnt]=reply;
					repliesCnt++;
					//					log.info("got waiting time from "+ reply.getSender().getLocalName());

					if (repliesCnt == MachineCount) {				
						step = 3; 
						repliesCnt = 0;
					}
				}
				else {
					block();
				}
			}
			catch (Exception e3) {
			}
			break;
		case 3:
			try {
				ACLMessage max = getWorstWaitingTime(WaitingTime);
				
				CumulativeWaitingTime = CumulativeWaitingTime +
						((Batch)max.getContentObject()).getExpectedDueDate();
				log.info("CumulativeWaitingTime="+CumulativeWaitingTime+max.getSender().getLocalName());
				JobToSend = (Batch)(max.getContentObject());
				comingBatch.IncrementOperationNumber();

				if(comingBatch.getCurrentOperationNumber() < 
						comingBatch.getFirstJob().getOperations().size()) {

					step = 1;
				}
				else {
					step = 4;
				}

			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			break;

		case 4:
			JobToSend.resetCurrentOperationNumber();
			JobToSend.setExpectedDueDate(CumulativeWaitingTime + System.currentTimeMillis());

			if(CumulativeWaitingTime < 0) {
				log.info("cannot process Batch no " + JobToSend.getBatchNumber());

				ZoneDataUpdate rejectionUpdate = new ZoneDataUpdate.Builder(
						ID.GlobalScheduler.ZoneData.rejectedOrders).
						value(JobToSend).Build();

				AgentUtil.sendZoneDataUpdate(blackboard, rejectionUpdate, myAgent);	
				String message = "Batch with ID " + JobToSend.getBatchId() + " is Rejected";
				WebLafGSA.showNotification("Batch Rejected", message, MessageType.INFO);
			}
			else{
				log.info("sending waiting time: " + CumulativeWaitingTime + " ms" + " : " 
						+ JobToSend.getCurrentOperationNumber() );

				ZoneDataUpdate NegotiationUpdate = new ZoneDataUpdate.
						Builder(ID.GlobalScheduler.ZoneData.GSAjobsUnderNegaotiation).
						value(JobToSend).
						setReplyWith(msgReplyID).
						Build();

				AgentUtil.sendZoneDataUpdate(blackboard, NegotiationUpdate, myAgent);

				WeblafGSAgui.showNotification("Order", JobToSend.getCustomerId()+
						" placed order for batch ID "+JobToSend.getBatchId(), MessageType.INFO);

			}
			step = 5;
			break;
		}   
	}
/**
 * 
 * @param WaitingTime reply messages from local schedulign agent with waiting time
 * @return message with maximum waiting time
 */
	public ACLMessage getWorstWaitingTime(ACLMessage[] WaitingTime ) {
		ACLMessage MaxwaitingTimeMsg = WaitingTime[0]; 
		for(int i = 0; i < WaitingTime.length; i++){

			try {
				if(((Batch)(WaitingTime[i].getContentObject())).
						getExpectedDueDate() > ((Batch)(MaxwaitingTimeMsg.
								getContentObject())).getExpectedDueDate()){
					MaxwaitingTimeMsg = WaitingTime[i];
				}
			} catch (UnreadableException e) {
				e.printStackTrace();
			}

		}
		//return maximum of all waiting times received from LSAs
		return MaxwaitingTimeMsg; 
	}

	@Override
	public boolean done() {
		return (step >= 5);
	}

	@Override
	public EndState getEndState() {
		return (step >= 5) ? EndState.SUCCESSFUL : null;
	}
}