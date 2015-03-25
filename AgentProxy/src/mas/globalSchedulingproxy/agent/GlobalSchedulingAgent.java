package mas.globalSchedulingproxy.agent;

import jade.core.AID;
import javax.swing.SwingUtilities;
import mas.globalSchedulingproxy.gui.GSAproxyGUI;
import mas.job.job;
import mas.util.AgentUtil;
import mas.util.ID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import bdi4jade.core.Capability;

public class GlobalSchedulingAgent extends AbstractGlobalSchedulingAgent{

	private static final long serialVersionUID = 1L;
	private static GSAproxyGUI mygui;
	private Logger log;

	public static void addConfirmedJob(job j) {
		if(mygui != null) {
			mygui.addAcceptedJobToList(j);
		}
	}

	public static void addCompletedJob(job j) {
		if(mygui != null) {
			mygui.addCompletedJob(j);
		}
	}

	public void queryJob(job j) {
		if(mygui != null) {
			mygui.showQueryResult();
		}
	}

	@Override
	protected void init() {
		super.init();		

		log = LogManager.getLogger();

		Capability bCap=  new BasicCapability();
		addCapability(bCap);

		AID bba = AgentUtil.findBlackboardAgent(this);
		bCap.getBeliefBase().updateBelief(
				ID.GlobalScheduler.BeliefBaseConst.blackboardAgent, bba);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				mygui = new GSAproxyGUI(GlobalSchedulingAgent.this);
			}
		});

	}
}
