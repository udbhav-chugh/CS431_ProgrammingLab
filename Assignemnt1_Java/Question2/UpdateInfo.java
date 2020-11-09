package task2;

import java.util.*;

//UpdateInfo class to store inputs provided by evaluators to execute
class UpdateInfo{
	private String evaluator;
	private String rollNum;
	private String marksToUpdate;

	UpdateInfo(String evaluator, String rollNum, String marksToUpdate){
		this.evaluator = evaluator;
		this.rollNum = rollNum;
		this.marksToUpdate = marksToUpdate;
	}

	public String getEvaluator(){  
		return evaluator;  
	}
	public String getRollNum(){  
		return rollNum; 	   
	}
	public String getMarksToUpdate(){
		return marksToUpdate;
	}
}