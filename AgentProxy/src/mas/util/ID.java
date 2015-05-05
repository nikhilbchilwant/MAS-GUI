package mas.util;

import jade.util.leap.Serializable;

/**
 * ID class for all ID-constants used while defining beliefs or messageIds or zonedata 
 */

public class ID implements Serializable{

	private static final long serialVersionUID = 1L;

	//It is recommended to keep each String in ID.java to be kept unique in order to avoid confusion and debugging purpose
	public class MAS {
		public static final String main_container_ipaddress = "127.0.0.1";
	}

	public class Blackboard {
		public static final String Service = "blackboard";
		public static final String LocalName = "blackboard";

		public class BeliefBaseConst{
//			public static final String NoOfMachines = "NoOfMachines";
			public static final String serviceDiary = "servicesOfferedByOtherAgents";
		}

		public class ZoneData {

		}
	}

	public class Customer {
		public static final String Service = "customer";
		public static final String LocalName = "customer";

		public class BeliefBaseConst {
			public static final String JobList = "customerBeliefBase_JobList";
			public static final String JOB_GENERATOR = "customerBeliefBase_JOB-GENERATOR";
			public static final String blackboardAgent = "customerBeliefBase_blackboard-agent";
			public static final String CURRENT_JOB2SEND = "customerBeliefBase_Current-job";
			public static final String CURRENT_NEGOTIATION_BATCH = "customerBeliefBase_Current_negotitaion_job";
			public static final String CURRENT_CONFIRMED_JOB = "customerBeliefBase_Current_confirmed_job";
			public static final String CANCEL_ORDER = "customerBeliefBase_Cancel_job";
			public static final String CHANGE_DUEDATE_JOB = "customerBeliefBase_Change_DueDate_job";
			public static final String CUSTOMER_GUI = "customerGUI";
		}

		public class ZoneData {

			// contains job generated by customer 
			public static final String newWorkOrderFromCustomer = "customer_newWorkOrderFromCustomer";

			// jobs under negotiation
			public static final String customerJobsUnderNegotiation = "customer_jobsUnderNegotiation";

			// accepted jobs by GSA
			public static final String customerConfirmedJobs = "customer_jobsAccepted";
			
			// order cancelled by customer
			public static final String customerCanceledOrders = "customer_cancelledOrders";
			
			// order for which due date is changed
			public static final String customerChangeDDorders = "customer_Change_due_date_orders";
		}
	}

	public class LocalScheduler {
		public static final String Service = "LSA_machine-simulator-schedule";
		public static final String LocalName = "Local_Scheduling_Agent";

		public class BeliefBaseConst {
			public static final String blackboardAgent = "LSABeliefBase_blackboard-agent";
			public static final String machine = "LSABeliefBase_machine";
			public static final String batchQueue = "LSABeliefBase_job-list";
			public static final String maintAgent = "LSABeliefBase_maintenanceAgent";
			public static final String globalSchAgent = "LSABeliefBase_gsAgent";
			public static final String dataTracker = "LSABeliefBase_data-tracker";
			public static final String ProcessingCost = "ProcessingCost";
			public static final String supportedOperations = "supportedOperations";
			public static final String regretThreshold = "regret_threshold";
			public static final String currentBatchOnMachine="jobCurrentlyProcessingOnMachine";
			public static final String operationDatabase = "operation_database";
			public static final String doneBatchFromMachine = "done_batchFromMachine";
			public static final String currentBatch = "current_batchOnMachine";
			public static final String actionOnCompletedBatch = "action_on_completed_Batch";
			public static final String gui_machine = "machine_gui";
			public static final String preventiveJobsQueue = "LSA_prevMaintJobs_Queue";
			public static final String currentMaintJob = "LSA_Current_MaintJobOnMachine";
			public static final String currentJobOnMachine =  "LSA_CurrentJobOnMachine";
			public static final String DueDateCalcMethod = "DueDateCalculationMethod";
			public static final String schedulingInterval = "LSA_schedulingInterval";
		}

		public class ZoneData {
			// update your waiting time for the job here
			public static final String WaitingTime = "LSA_WaitingTime";

			// update your bid for job here
			public static final String bidForJob = "LSA_BidForJob";

			// update local queue of job of machine
			public static final String machineJobQueue = "LSA_JobQueueForMachine";

			// update the job for machine here
			public static final String batchForMachine = "LSA_jobForMachine";

			public static final String finishedBatch="LSA_finishedJob";

			public static final String QueryResponse = "ResponseToGSAQuery";

			public static final String gui_machine = "guiMachine";

			public static final String maintenanceJobForMachine = "LSA_MaintJobForMachine";

			public static final String MaintConfirmationLSA = "LSA_maintCOnfirmation";
		}
		
		public class OtherConst{
			public static final String LocalDueDate="Local";
			public static final String GlobalDueDate = "Global";
		}
	}


	public class GlobalScheduler {
		public static final String Service ="global-scheduling-agent";
		public static final String LocalName ="GlobalSchedulingAgent";

		public class ZoneData {
			// final confirmed order coming from customer which has to be accepted
			public static final String GSAConfirmedOrder = "NewJobFromCustomer";

			// jobs under negotiation
			public static final String GSAjobsUnderNegaotiation = "GSA_jobsUnderNegaotiation"; 

			// job which are confirmed after bidding and sent to winner-machine
			public static final String jobForLSA = "GSA_job-for-machine";

			// advertise job for bids from LSA
			public static final String askBidForJobFromLSA = "GSA_ask-for-bid";

			// advertise job for getting waiting time from LSA's
			public static final String GetWaitingTime = "GSA_LocalSchedulingwaiting-time";
			//query job information from LSA
			public static final String QueryRequest = "QueryRequestAbtLSAfromGSA";

			public static final String CallBackJobs = "CallBackJobs";

			public static final String completedJobByGSA = "completedJobs";

			public static final String Current_Negotiation_Job = "GSA_currentNegJob";

			public static final String dueDateChangeBatches = "GSAbatchForDueDateChangeReq";

			public static final String rejectedOrders = "rejected_orders"; 
		}

		public class BeliefBaseConst {
			public static final String blackboardAgent = "GSA_blackboard-agent";
			public static final String NoOfMachines = "NoOfMachines";
//			public static final String DueDateCalcMethod = "DueDateCalc";
			public static final String Current_Negotiation_Batch = "GSA_negotiatin_job";
			public static final String GSAqueryJob = "GSA_query_job";
			public static final String GSA_GUI_instance = "GUIinstanceOfGSA";
			public static final String batchDatabase = "GSA_batchDatabase";
			public static final String batchCount = "GSA_batchCount";
		}
		
		

		public class requestType {
			//enum is most suitable (http://docs.oracle.com/javase/1.5.0/docs/guide/language/enums.html)
			//but it can't be defined in inner class :(
			public static final String currentStatus="currentStatus";
			public static final String cancelBatch="cancelBatch";
			public static final String changeDueDate="changeBatch";
		}
	}

	public class Maintenance {
		public static final String Service = "machine-simulator-maint";
		public static final String LocalName ="machine-simulator-maint";
		public static final String maintJobPrefix = "maint_";

		public class ZoneData {
			// update data for preventive maintenance data for machine
			public static final String prevMaintData = "Maintenance_preventiveMaintenanceData";

			// update data for corrective maintenance data for machine
			public static final String correctiveMaintdata = "Maintenance_correctiveMaintenancedata";

			// update preventive maintenance job for machine here
			public static final String preventiveMaintJob = "Maintenance_preventiveMaintJob";

			// update inspection job data for machine here
			public static final String inspectionJobData = "Maintenance_inspectionData";

			// update inspection job for machine here
			public static final String inspectionJob = "Maintenance_inspectionJob";

			// update status of machine here as in critical when the preventive
			// maintenance is getting delayed
			public static final String machineStatus =  "Maintenance_machineStatus";

		}

		public class BeliefBaseConst {
			public static final String blackboardAgentAID = "Maintenance_blackboard-agent";
			public static final String machineHealth = "Maintenance_machine";
			public static final String globalSchAgentAID = "Maintenance_gsAgent";
			public static final String dataTracker = "Maintenance_data-tracker";
			public static final String preventiveMaintJob = "Maintenance_machine-maintenance-tracker";
			public static final String correctiveRepairData = "Maintenance_Corrective_Repair_Data" ;
			public static final String gui_maintenance = "gui_maintenance";
			public static final String prevMaintFromMachine = "prevMaint_Status_From_Machine";
			public static final String maintenancePeriod = "Maintenance_maintPeriod";
			public static final String maintWarningPeriod = "Maintenance_maintWarningPeriod";
		}
	}

	public class Machine{
		public static final String Service="machine_service";
		public static final String LocalName ="machine_simulator";

		public class ZoneData {

			//update new job for the machine
			public static final String askJobFromLSA = "_askJobFromLSA";

			// update machine's health  here
			public static final String myHealth ="machine_health";

			// update the finished job here 
			public static final String finishedBatch ="machine_finishedJob";

			// update start of maintenance here
			public static final String maintenanceStart ="machine_maintenanceStart";

			// update start of inspection here
			public static final String inspectionStart ="machine_inspectionStart";

			// update machine's failure here i.e. when machine is failed
			public static final String machineFailures ="machine_machineFailures";

			public static final String prevMaintConfirmation = "machine_prevMaintConfirmation";

			public static final String currentJobOnMachine = "machine_currentJob";
		}
	}
}
