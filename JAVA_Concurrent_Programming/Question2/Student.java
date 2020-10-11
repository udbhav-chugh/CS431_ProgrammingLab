package task2;

import java.util.*;

//Student class to store each record of file
class Student{
	private String rollNum;
	private String name;
	private String email;
	private String marks;
	private String latestEvaluator;

	Student(String rollNum, String name, String email, String marks, String latestEvaluator){
		this.rollNum = rollNum;
		this.name = name;
		this.email = email;
		this.marks = marks;
		this.latestEvaluator = latestEvaluator;
	}

	public String getRollNum(){  
		return rollNum;  
	}
	public String getName(){  
		return name; 	   
	}
	public String getEmail(){
		return email;
	}
	public String getMarks(){  
		return marks; 	   
	}
	public String getLatestEvaluator(){  
		return latestEvaluator; 	   
	}

	public void setMarks(String marks){
		this.marks = marks;
	}
	public void setLatestEvaluator (String latestEvaluator){
		this.latestEvaluator = latestEvaluator;
	}
}

//comaprator class to sort by roll number
class rollnumberCompare implements Comparator<Student> 
{
	@Override
	public int compare(Student student1,Student student2)
	{
		return (student1.getRollNum()).compareTo(student2.getRollNum());
	}
}
//comaprator class to sort by name
class nameCompare implements Comparator<Student> 
{
	@Override
	public int compare(Student student1,Student student2)
	{
		return (student1.getName()).compareTo(student2.getName());
	}
}