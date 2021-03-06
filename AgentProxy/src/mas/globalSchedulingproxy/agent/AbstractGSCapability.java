package mas.globalSchedulingproxy.agent;

import jade.core.AID;
import jade.lang.acl.MessageTemplate;

import java.util.HashSet;
import java.util.Set;

import mas.globalSchedulingproxy.database.BatchDataBase;
import mas.globalSchedulingproxy.goal.GSASendNegotitationGoal;
import mas.globalSchedulingproxy.goal.GetNoOfMachinesGoal;
import mas.globalSchedulingproxy.goal.LoadBatchOperationDetailsGoal;
import mas.globalSchedulingproxy.goal.QueryJobGoal;
import mas.globalSchedulingproxy.goal.RegisterAgentToBlackBoardGoal;
import mas.globalSchedulingproxy.goal.RegisterServiceGoal;
import mas.globalSchedulingproxy.gui.WebLafGSA;
import mas.globalSchedulingproxy.plan.AskForWaitingTime;
import mas.globalSchedulingproxy.plan.CallBackChangeDueDatePlan;
import mas.globalSchedulingproxy.plan.GSASendNegotiationJobPlan;
import mas.globalSchedulingproxy.plan.GetNoOfMachinesPlan;
import mas.globalSchedulingproxy.plan.HandleCompletedOrderbyLSAPlan;
import mas.globalSchedulingproxy.plan.LoadBatchOperationDetailsPlan;
import mas.globalSchedulingproxy.plan.NegotiateViaGuiPlan;
import mas.globalSchedulingproxy.plan.QueryFromLSA;
import mas.globalSchedulingproxy.plan.RegisterAgentToBlackboardPlan;
import mas.globalSchedulingproxy.plan.RegisterServicePlan;
import mas.globalSchedulingproxy.plan.TakeOrderAndRaiseBidPlan;
import mas.jobproxy.Batch;
import mas.util.ID;
import mas.util.MessageIds;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import bdi4jade.belief.Belief;
import bdi4jade.belief.TransientBelief;
import bdi4jade.core.BeliefBase;
import bdi4jade.core.Capability;
import bdi4jade.core.PlanLibrary;
import bdi4jade.plan.Plan;
import bdi4jade.util.plan.SimplePlan;
/**
 * Global Scheduling Agent capability. Currently, only one capability exists.
 * @author NikhilChilwant
 *
 */
public abstract class AbstractGSCapability  extends Capability {

	private static final long serialVersionUID = 1L;
	private Logger log;

	public AbstractGSCapability() {
		super(new BeliefBase(getBeliefs()), new PlanLibrary(getPlans()));
	}

	public static Set<Belief<?>> getBeliefs() {
		Set<Belief<?>> beliefs = new HashSet<Belief<?>>();

		Belief<AID> BB_AID = new TransientBelief<AID>(
				ID.GlobalScheduler.BeliefBaseConst.blackboardAgent);		

		//no of machines = no. of LSA		
		Belief<Integer> NoOfMachines = new TransientBelief<Integer>(ID.GlobalScheduler.
				BeliefBaseConst.NoOfMachines);

		//batch abou which query was done
		Belief<Batch> query = new TransientBelief<Batch>(
				ID.GlobalScheduler.BeliefBaseConst.GSAqueryJob);

		//batch under negotaition
		Belief<Batch> underNegotiation = new TransientBelief<Batch>(
				ID.GlobalScheduler.BeliefBaseConst.Current_Negotiation_Batch);

		//stores instance of GUI
		Belief<WebLafGSA> GSA_gui = new TransientBelief<WebLafGSA>(
				ID.GlobalScheduler.BeliefBaseConst.GSA_GUI_instance); 

		Belief<BatchDataBase> dBase = new TransientBelief<BatchDataBase>(
				ID.GlobalScheduler.BeliefBaseConst.batchDatabase);
		
		
		Belief<Integer> batchCount = new TransientBelief<Integer>(
				ID.GlobalScheduler.BeliefBaseConst.batchCount);

		dBase.setValue(null);
		
		underNegotiation.setValue(null);
		NoOfMachines.setValue(0);
		batchCount.setValue(0);
		
		beliefs.add(BB_AID);
		beliefs.add(NoOfMachines);
		
		beliefs.add(query);
		beliefs.add(underNegotiation);
		beliefs.add(GSA_gui);
		beliefs.add(dBase);
		beliefs.add(batchCount);

		return beliefs;
	}

	public static Set<Plan> getPlans() {
		Set<Plan> plans = new HashSet<Plan>();

		//register Global Scheduling Agent service on blackboard
		plans.add(new SimplePlan(RegisterServiceGoal.class, RegisterServicePlan.class));

		//register Global Scheduling Agent on blackboard
		plans.add(new SimplePlan(RegisterAgentToBlackBoardGoal.class,
				RegisterAgentToBlackboardPlan.class));

		//return no. of machines connected to GLobal Scheduling Agent.
		plans.add(new SimplePlan(GetNoOfMachinesGoal.class, GetNoOfMachinesPlan.class));

		//ask for waiting from Local Scheduling Agents
		plans.add(new SimplePlan(MessageTemplate.MatchConversationId(MessageIds.msgnewWorkOrderFromCustomer),
				AskForWaitingTime.class));

		//Negotiation between customer and Global SCheduling Agent
		plans.add(new SimplePlan(MessageTemplate.MatchConversationId(
				MessageIds.msgcustomerJobsUnderNegotiation), NegotiateViaGuiPlan.class));

		//send negotiation to customer
		plans.add(new SimplePlan(GSASendNegotitationGoal.class, GSASendNegotiationJobPlan.class));

		//raise bid from Local Scheduling Agent for confrimed job
		plans.add(new SimplePlan(
				MessageTemplate.MatchConversationId(MessageIds.msgcustomerConfirmedJobs),
				TakeOrderAndRaiseBidPlan.class));

		plans.add(new SimplePlan(
				MessageTemplate.MatchConversationId(MessageIds.msgLSAfinishedJobs),
				HandleCompletedOrderbyLSAPlan.class));

		plans.add(new SimplePlan(MessageTemplate.MatchConversationId(MessageIds.msgreqToChangeDueDate),
				CallBackChangeDueDatePlan.class));

		plans.add(new SimplePlan(QueryJobGoal.class, QueryFromLSA.class));

		plans.add(new SimplePlan(LoadBatchOperationDetailsGoal.class, LoadBatchOperationDetailsPlan.class));

		return plans;
	}	

	@Override
	protected void setup() {
		log = LogManager.getLogger();		

		myAgent.addGoal(new RegisterServiceGoal());
		myAgent.addGoal(new RegisterAgentToBlackBoardGoal());
		myAgent.addGoal(new LoadBatchOperationDetailsGoal());
		myAgent.addGoal(new GetNoOfMachinesGoal());
	}

}
