package mas.customerproxy.agent;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import mas.jobproxy.Batch;
import mas.jobproxy.job;
import mas.jobproxy.jobOperation;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Jobloader {

	// processing time is input as seconds. Convert it into milliseconds
	private long timeUnitConversion = 1000;

	private int NumJobs;
	private ArrayList<XSSFSheet> sheets;

	private String jobFilePath;
	private ArrayList<String> jobIdList;
	private ArrayList<Double> jobCPNs;
	private ArrayList<Long> jobDueDates;
	private ArrayList<Integer> jobQuantity;
	private ArrayList<Double> jobPenaltyRate;
	private ArrayList<ArrayList<jobOperation> > jobOperations;
	int countJob = 1;
	private String[] tableHeaders = {"Job ID" , "Operations",
			"CPN" , "Penalty Rate"};

	public Jobloader() {
		this.jobIdList = new ArrayList<String>();
		this.jobQuantity = new ArrayList<Integer>();
		this.jobCPNs = new ArrayList<Double>();
		this.jobDueDates = new ArrayList<Long>();
		this.jobPenaltyRate = new ArrayList<Double>();
		this.sheets = new ArrayList<XSSFSheet>();
		this.jobOperations = new ArrayList<ArrayList<jobOperation> >();
		this.jobFilePath = System.getProperty("user.dir");
	}

	public Vector<Batch> getjobVector() {
		Vector<Batch> jobs = new Vector<Batch>();

		for(int index = 0 ; index < jobIdList.size() ; index ++){

			job j = new job.Builder(jobIdList.get(index))
			.jobOperation(this.jobOperations.get(index))
			.jobDueDateTime(this.jobDueDates.get(index))
			.build() ;

			j.setJobNo(countJob++);

			Batch batch = new Batch(jobIdList.get(index));
			batch.addJobToBatch(j);
			batch.setCPN(this.jobCPNs.get(index));
			batch.setPenaltyRate(this.jobPenaltyRate.get(index));
			batch.setDueDateByCustomer(new Date(this.jobDueDates.get(index)) );
			jobs.add(batch);
		}
		return jobs;
	}

	public Vector<String> getJobHeaders(){
		Vector<String> headers = new Vector<String>();

		for(int index = 0 ; index < tableHeaders.length ; index ++){
			headers.add(tableHeaders[index]);
		}

		return headers;
	}

	public Vector<String> getAcceptedJobTableHeader() {
		Vector<String> headers = new Vector<String>();

		for(int index = 0 ; index < tableHeaders.length ; index ++){
			headers.add(tableHeaders[index]);
		}

		headers.add("Due Date");
		return headers;
	}

	public Vector<String> getCompleteJobTableHeader() {
		Vector<String> headers = new Vector<String>();

		for(int index = 0 ; index < tableHeaders.length ; index ++){
			headers.add(tableHeaders[index]);
		}

		headers.add("Due Date");
		return headers;
	}

	public void readFile() {
		XSSFWorkbook wb;
		try{
			FileInputStream file=new FileInputStream(this.jobFilePath +
					"\\jobdata.xlsx");	
			wb = new XSSFWorkbook(file);

			this.NumJobs = wb.getNumberOfSheets();

			XSSFSheet localSheet;
			for(int i = 0 ; i < NumJobs ; i++) {
				localSheet = wb.getSheetAt(i);
				sheets.add(localSheet);
				readSheet(localSheet);
			}

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private void readSheet(XSSFSheet currSheet) {

		Iterator<Row> rows = currSheet.rowIterator();
		XSSFRow row = (XSSFRow) rows.next();

		// first read the second row of job file
		// skip the first header line
		row = (XSSFRow) rows.next();

		Iterator<Cell> cells = row.cellIterator();

		int count = 0; 
		while(cells.hasNext()) {
			XSSFCell cell = (XSSFCell) cells.next();

			switch(count) {
			case 0:
				jobIdList.add(cell.getStringCellValue());
				break;
			case 1:
				jobQuantity.add((int) cell.getNumericCellValue());
				break;
			case 2:
				jobCPNs.add(cell.getNumericCellValue());
				break;
			case 3:
				jobDueDates.add((long) (cell.getNumericCellValue()*timeUnitConversion));
				//				log.info((long) (cell.getNumericCellValue()*timeUnitConversion));
				break;
			case 4:
				jobPenaltyRate.add(cell.getNumericCellValue());
				break;
			}
			count ++;
		}

		ArrayList<jobOperation> opList = new ArrayList<jobOperation>();
		// Now read operations for the job
		// Skip the header row for operations
		row = (XSSFRow) rows.next();

		while( rows.hasNext() ) {

			row = (XSSFRow) rows.next();
			cells = row.cellIterator();

			jobOperation currOperation = new jobOperation();
			count = 0; 
			while(cells.hasNext()) {
				XSSFCell cell = (XSSFCell) cells.next();

				switch(count) {
				case 0:
					// Operation type for the job
					String op = cell.getStringCellValue();
					currOperation.setJobOperationType(op);
					break;

					//				case 1:
					//					// Processing time for this operation
					//					currOperation.
					//					setProcessingTime((long) cell.getNumericCellValue()*timeUnitConversion);
					//					break;
					//
					//				case 2:
					//					// Dimensions for this operation
					//					//					log.info(cell.getCellType());
					//					cell.setCellType(1);
					//					String s = cell.getStringCellValue();
					//					String temp[] = s.split(",");
					//					//			            		  System.out.println("length="+temp.length);
					//					ArrayList<jobDimension> tempDimList = new ArrayList<jobDimension>();
					//					jobDimension tempDim = new jobDimension();
					//					for(int i=0; i < temp.length; i++){
					//						tempDim.setTargetDimension(Double.parseDouble(temp[i]));
					//						tempDimList.add(tempDim );
					//					}
					//					currOperation.setjDims(tempDimList);
					//					break;
					//
					//				case 3:
					//					// Attributes for this operation
					//					String Attr=cell.getStringCellValue();
					//					String tempAttr[]=Attr.split(",");
					//
					//					ArrayList<String> tempAttrList = new ArrayList<String>();
					//
					//					for(int i=0; i < tempAttr.length; i++){
					//						tempAttrList.add(tempAttr[i] );
					//					}
					//					//					currOperation.getjDims().get(0).setAttribute(tempAttrList);
					//
					//					break;
				}
				count++;
			}
			opList.add(currOperation);
		}
		this.jobOperations.add(opList);
	}
}
