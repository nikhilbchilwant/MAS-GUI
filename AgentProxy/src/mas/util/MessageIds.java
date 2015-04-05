package mas.util;

/**
 * @author Anand Prajapati, Nikhil chilwant
 * Class contains Id's for all message communication between agents.
 * Whenever there is an update in one of the zonedata's, a message with some message id will be sent to all the registered observers
 */

public class MessageIds {

	// Customer message id's here

	// contains job generated by customer 
	public static final String msgnewWorkOrderFromCustomer = "customer_newWorkOrderFromCustomer";

	// jobs under negotiation
	public static final String msgcustomerJobsUnderNegotiation = "customer_jobsUnderNegotiation";

	// accepted jobs by GSA
	public static final String msgcustomerConfirmedJobs = "customer_jobsAccepted";
	
	// canceled orders by customer
	public static final String msgcustomerCanceledOrders = "customer_canceledOrders";

	// orders with changed due dates
	public static final String msgcustomerChangeDDorders = "customer_Changed_duedate_orders";

	//-----------------------------------------------------------------------------------------------------------------

	// global scheduling message id's here

	// final confirmed order coming from customer which has to be accepted
	public static final String msgGSAConfirmedOrder = "NewJobFromCustomer";

	// jobs under negotiation
	public static final String msgGSAjobsUnderNegaotiation = "GSA_jobsUnderNegaotiation"; 

	// job which are confirmed and advertised for machine
	public static final String msgjobForLSA = "GSA_job_for_machine";

	// advertise job for bids from LSA
	public static final String msgaskBidForJobFromLSA = "GSA_ask-for-bid";

	// advertise job for getting waiting time from LSA's
	public static final String msgGetWaitingTime = "GSA_LocalSchedulingwaiting-time";

	//-----------------------------------------------------------------------------------------------------------------

	// Local scheduling agent message ids here

	// update your waiting time for the job here
	public static final String msgWaitingTime = "WaitingTime_LSA";

	// update your bid for job here
	public static final String msgbidForJob = "LSA_BidForJob";

	// update local queue of job of machine
	public static final String msgmachineJobQueue = "LSA_JobQueueForMachine";

	// update the job for machine here
	public static final String msgbatchForMachine = "LSA_batchForMachine";
	
	// 
	public static final String msgbidResultJob = "LSA_ContractNet_job";

	//------------------------------------------------------------------------------------------------------------------

	// Local maintenance agent message ids here

	// update data for preventive maintenance data for machine
	public static final String msgprevMaintData = "Maintenance_preventiveMaintenanceData";

	// update data for corrective maintenance data for machine
	public static final String msgcorrectiveMaintdata = "Maintenance_correctiveMaintenancedata";

	// update preventive maintenance job for machine here
	public static final String msgpreventiveMaintJob = "Maintenance_preventiveMaintJob";

	// update inspection job data for machine here
	public static final String msginspectionJobData = "Maintenance_inspectionData";

	// update inspection job for machine here
	public static final String msginspectionJob = "Maintenance_inspectionJob";

	//------------------------------------------------------------------------------------------------------------------

	// machine simulator message ids here

	//update new job for the machine
	public static final String msgaskJobFromLSA = "_askJobFromLSA";
	
	// update machine's health  here
	public static final String msgmyHealth ="machine_health";

	// update the finished job here 
	public static final String msgfinishedBatch ="machine_finishedJob";

	// update start of maintenance here
	public static final String msgmaintenanceStart ="machine_maintenanceStart";

	// update start of inspection here
	public static final String msginspectionStart ="machine_inspectionStart";

	// update machine's failure here i.e. when machine is failed
	public static final String msgmachineFailures ="machine_machineFailures";

	//---------------------------------------------------------------------------------------------------------------------

	// blackboard agent message id's here

	public static final String RegisterMe = "Register";
	public static final String UpdateParameter = "UpdateParam";
	public static final String SubscribeParameter = "subscribe-parameter";

	public static final String msgGSAQuery = "GSAqueriesToLSA";

	public static final String msgLSAQueryResponse = "ResponseToGSAQuery";

	public static final String msgreqToChangeDueDate = "CallBackJobReq";

	public static final String msgCallBackReqByGSA = "GSAReqForCallBack";

	public static final String msgJobCompletion = "sendNotificationOfCompletedJob";


	//operation n is finished. now send job to GSA for n+1th operation
	public static String msgLSAfinishedJobs="finished_job_fromLSA";

	public static String msgJobFromBatchForMachine = "job_from_Batch";


	//	// Local scheduling Agent id's here
	//	public static final String LSJobForMachine = "LSJobForMachine";
	//	public static final String LSjobFromGS = "LSjobFromGS";
	//	public static final String LSaskForGS = "LSaskForGS";
	//	public static final String LSsendBidToGS = "LSsendBidToGS";
	//	public static final String LSA_JobBid = "LSABidToGSA";
	//	public static final String LSsendWaitingTimeGS = "LSsendWaitingTimeGS";
	//	public static final String LSA_JobWaitingTime = "LSA_WaitingTimeTo_GSA";
	//	public static final String LSsendJobToMachine = "LSsendJobToMachine";
	//	public static final String LSA_JobQueue = "LSA_JobQueue";
	//	public static final String LSgetCompletedJobFromMachine = "LSgetCompletedJobFromMachine";
	//
	//
	//	public static final String AskWaitTime = "GiveWaitingTime";
	//
	//	public static String TotalJobsSent = "jobs-sent-total";
	//
	//	public static String JobsRejected = "jobs-rejected";
	//
	//	public static String JobFromScheduler ="Job-To-Machine";
	//
	//	public static String GSABidForJobFromLSA ="Bid-For-Job";
	//
	//	public static String MachineFailure = "Failure-of-Machine";
	//
	//	public static String WaitTime = "MaxWaitingTimeForJob";
	//
	//	public static String GSANegotiationJobsCustomer = "_GSANegotiationJobsCustomer";
	//
	//	public static String OrderConfirmation = "OrderConfirmation";
	//
	//	public static String GSA_NewWorkOrder_fromCustomer = "GSA_NewWorkOrder_fromCustomer";
	//
	//	public static String SendJob="SendJob";
	//
	//	public static String GSAwaitingTimeFromLSA = "_waitingTimeFromLSA";
	//
	//	public static String GiveJob="GiveJob";
	//
	//	public static String correctiveData = "Corrective-Repair-Data";
	//	public static String maintMachineFailureInfo = "Fail-Start";
	//	public static String machinePrevMaintenanceData = "machineMaintenanceData";
	//	public static String machinePrevMaintenanceStart = "machineMaintenanceStart";
	//	public static String machineInspectionStart = "machineInspectionStart";
	//	public static String machineInspectionData = "machineInspectionStart";
	//	public static String completedJobFromMachine = "completedJobFromMachine";
	//
	//	public static String failEnd = "Fail-End";
	//	public static String machineSimulatorHealth = "Machine-Simualtor-State";
	//	public static String MaintMachineRepaired = "Machine-failure-End";
	//	public static String maintenanceJob = "Maintenance-Job";
	//	public static String maintenanceJobStartData = "maintenanceJobStartData";
	//	public static String inspect_job_data = "IJTime";
}
