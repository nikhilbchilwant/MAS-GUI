package mas.globalSchedulingproxy.plan;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import mas.globalSchedulingproxy.goal.QueryJobGoal;
import mas.globalSchedulingproxy.gui.GSAproxyGUI;
import mas.jobproxy.Batch;
import mas.jobproxy.job;
import mas.util.AgentUtil;
import mas.util.ID;
import mas.util.JobQueryObject;
import mas.util.MessageIds;
import mas.util.ZoneDataUpdate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bdi4jade.core.BeliefBase;
import bdi4jade.plan.PlanBody;
import bdi4jade.plan.PlanInstance;
import bdi4jade.plan.PlanInstance.EndState;

import mas.globalSchedulingproxy.goal.*;

public class QueryFromLSA extends Behaviour implements PlanBody {

	private static final long serialVersionUID = 1L;

	private AID blackboard_AID;
	private int step = 0;
	private MessageTemplate mt;
	private int MachineCount = 0;
	private int repliesCnt=0;
	private Batch queryJob;
	private ACLMessage[] LSAqueryResponse;
	private BeliefBase bfBase;
	private Logger log = LogManager.getLogger();

	@Override
	public EndState getEndState() {
		return (step >= 3 ? EndState.SUCCESSFUL : null);
	}

	@Override
	public void init(PlanInstance PI) {
		
		bfBase = PI.getBeliefBase();
		this.queryJob=	((QueryJobGoal)(PI.getGoal())).getBatchToQuery();
		blackboard_AID = new AID(ID.Blackboard.LocalName, false);
		mt = MessageTemplate.MatchConversationId(MessageIds.msgLSAQueryResponse);
	}

	@Override
	public void action() {

		switch(step) {
		case 0:
			this.MachineCount = (int) bfBase.getBelief(ID.GlobalScheduler.BeliefBaseConst.NoOfMachines).
			getValue();

			if (MachineCount != 0) {
				JobQueryObject queryForm = new JobQueryObject.Builder().
						currentJob(this.queryJob).build();
				LSAqueryResponse=new ACLMessage[MachineCount];
				ZoneDataUpdate QueryRequest = new ZoneDataUpdate.
						Builder(ID.GlobalScheduler.ZoneData.QueryRequest).
						value(queryForm).
						Build();
				log.info("sent query");
				AgentUtil.sendZoneDataUpdate(blackboard_AID, QueryRequest, myAgent);
				step = 1;
			}

		case 1:
			try {
				ACLMessage reply = myAgent.receive(mt);
//				log.info("recieved"+reply);
				if (reply != null) {
					LSAqueryResponse[repliesCnt] = reply;
					repliesCnt++;
					if (repliesCnt == MachineCount) {
						step = 2;
						repliesCnt=0;
//						log.info("got replies");
					}
				}
				else {
					block();
				}
			} catch (Exception e3) {
			}

			break;

		case 2:
			JobQueryObject response = getQueryResponse(LSAqueryResponse);
			log.info(response);
			GSAproxyGUI.showQueryResult(response);
			step = 3;
			break;
		}
	}

	private JobQueryObject getQueryResponse(ACLMessage[] LSAqueryResponse2) {

		JobQueryObject response = null;
		
		for(int i = 0; i < LSAqueryResponse2.length; i++) {
			try {
				JobQueryObject queryResponse = (JobQueryObject) LSAqueryResponse2[i].getContentObject();
				Batch j = (queryResponse).getCurrentJob();
				if(j != null) {
					response = queryResponse;
					log.info(j.getBatchNumber() + " is at " + queryResponse.getCurrentMachine().getLocalName());
					if(queryResponse.isOnMachine()) {
						log.info(j.getBatchNumber() + " is currently under process at " +
								queryResponse.getCurrentMachine().getLocalName());
					}
				}
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
		}
		return response;
	}

	@Override
	public boolean done() {
		return step >= 3;
	}

}
