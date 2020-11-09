package task2;

import java.io.*;
import java.util.*;
import java.lang.*; 


//Imports for Semaphore Lock
import java.util.concurrent.Semaphore;

public class DistributedSystemModification {

    private final static Scanner scanner = new Scanner(System.in);  // For reading input
    private ArrayList<Student> studentRecords; //to store data read from file
    private ArrayList<UpdateInfo > updateInputs; // To store input queries of updating marks
    private ArrayList<Semaphore> semaphoreLocksList;

    //Constructor
    private DistributedSystemModification() {
        updateInputs = new ArrayList<>();
        studentRecords = new ArrayList<>();
        semaphoreLocksList = new ArrayList<>();
    }

    private int getIndex(String rollNumber){
    	int index = 0;
    	for (Student entry : studentRecords){
    		if(rollNumber.equals(entry.getRollNum())) {
    			return index;
    		}
    		index++;
    	}
    	return -1;
    } 

    //call updateData studens using semaphoreLocks for synchronization
    void updateSyncData(String rollNumber, int marksToUpdate, String updatedBy) {
        int index = getIndex(rollNumber);
        if (index != -1) {
            //synchronisation with semaphore locks
        	try{
            	semaphoreLocksList.get(index).acquire();
	            updateData(index, marksToUpdate, updatedBy);
        	} catch (InterruptedException exc) { 
                System.out.println(exc); 
            } 
	        semaphoreLocksList.get(index).release();
        }
    }

    //update the marks after lock is acquired
    private void updateData(int index, int marksToUpdate, String updatedBy) {
    	Student student = studentRecords.get(index);
        if (student.getLatestEvaluator().equals("CC") && !updatedBy.equals("CC")) {
        	System.out.println(updatedBy + " could not update marks of roll number " + student.getRollNum() + " by " + marksToUpdate + " due to lower prioirty than CC.");
            return;
        }
        int marks = Integer.parseInt(student.getMarks().trim());
        marks = marks + marksToUpdate;
        student.setMarks(String.valueOf(marks));
        student.setLatestEvaluator(updatedBy);
        studentRecords.set(index, student);
    	System.out.println(updatedBy + " successfully updated marks of roll number " + student.getRollNum() + " by " + marksToUpdate);
    }

    //Read the input buffer and Update the marks of the students
    private void UpdateMarks() {

        // Create the threads for updating the marks of the students
        Evaluator ccEvaluator = new Evaluator(this, "CC", Thread.MAX_PRIORITY);
        Evaluator ta1Evaluator = new Evaluator(this, "TA1", Thread.NORM_PRIORITY);
        Evaluator ta2Evaluator = new Evaluator(this, "TA2", Thread.NORM_PRIORITY);

        //add input query in threads for TA1, TA2, CC
        for (UpdateInfo info : updateInputs) {
            String evaluator = info.getEvaluator();
            String rollNumber = info.getRollNum();
            String updateMarks = info.getMarksToUpdate();
            if ("TA1".equals(evaluator)) {
                ta1Evaluator.addInputQueryEvaluator(rollNumber, updateMarks);
            } else if ("TA2".equals(evaluator)) {
                ta2Evaluator.addInputQueryEvaluator(rollNumber, updateMarks);
            } else if ("CC".equals(evaluator)) {
                ccEvaluator.addInputQueryEvaluator(rollNumber, updateMarks);
            }
        }
        // Clear the global input buffer.
        updateInputs.clear();

        // Start the threads
        ccEvaluator.start();
        ta1Evaluator.start();
        ta2Evaluator.start();

        // Wait for the threads to complete
        try{
	        ccEvaluator.join();
	        ta1Evaluator.join();
	        ta2Evaluator.join();
	    } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Write the final results back to the file
        WriteInFile("./Stud_Info.txt", studentRecords);
    }

    //Read the file initially
    private void readFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("./Stud_Info.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            // use comma as separator
            String[] student = line.split(",");
            Student newEntry = new Student(student[0],student[1],student[2],student[3],student[4]);
            studentRecords.add(newEntry);
            Semaphore semaphoreLock = new Semaphore(1);
            semaphoreLocksList.add(semaphoreLock);
        }
    }

    //Add received input query from user to update marks
    private void addInputQuery() {

        String evaluator = GetEvaluator();
        String rollNumber = GetRoll();
        String marksUpdate = GetMarksToUpdate();

        // Add to the updateInputs
        UpdateInfo newInput = new UpdateInfo(evaluator, rollNumber, marksUpdate);
        updateInputs.add(newInput);
    }

    //Get the evaluator name: TA1, TA2, CC
    private String GetEvaluator() {
        System.out.println("Enter evaluator's name(CC/TA1/TA2): ");
        String evaluator = scanner.next();
        while(evaluator.equals("CC") == false && evaluator.equals("TA1") ==false && evaluator.equals("TA2") == false){
            System.out.println("Invalid evaluator name. Enter again(CC/TA1/TA2): ");
        	evaluator = scanner.next();
        }
        return evaluator;
    }

    //Get the roll number of the student whose marks have to be changed
    private String GetRoll() {
        System.out.println("Enter Roll Number : ");
        return scanner.next();
    }

    //Get marks to be updated(increase or decrease and by how much)
    private String GetMarksToUpdate() {
        System.out.println("Choose one of the following(1/2)\n" +
                "   1) Increase marks\n" +
                "   2) Decrease marks");

        int option = scanner.nextInt();
        int marks;
        switch (option){
        	case 1: System.out.println("Increase marks value : ");
            		marks = scanner.nextInt();
            		return String.valueOf(marks);
            case 2: System.out.println("Decrease marks value : ");
            		marks = scanner.nextInt();
            		return String.valueOf(-marks);
            default: System.out.println("Invalid Option.");
            		return GetMarksToUpdate();
        }
    }

    //Write date from memory to file
    private void WriteInFile(String fileName, ArrayList<Student> dataToWrite) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert writer != null;
        for (Student student : dataToWrite) {
            try {
                    writer.append(student.getRollNum());
                    writer.append(',');
                    writer.append(student.getName());
                    writer.append(',');
                    writer.append(student.getEmail());
                    writer.append(',');
                    writer.append(student.getMarks());
                    writer.append(',');
                    writer.append(student.getLatestEvaluator());
                    writer.append(',');
                	writer.append('\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void GenerateRollNumberSortedFile(){
    	ArrayList<Student > dataSortedRollNumber = new ArrayList<Student>();
    	for (int i=0;i<studentRecords.size();i++){
    		dataSortedRollNumber.add(studentRecords.get(i));
    	}
    	Collections.sort(dataSortedRollNumber,new rollnumberCompare());
    	WriteInFile("./Sorted_Roll.txt", dataSortedRollNumber);    
    	System.out.println("Successfully generated Sorted_Roll.txt");	
    }

    private void GenerateNameSortedFile(){
    	ArrayList<Student > dataSortedName = new ArrayList<Student>();
    	for (int i=0;i<studentRecords.size();i++){
    		dataSortedName.add(studentRecords.get(i));
    	}
    	Collections.sort(dataSortedName,new nameCompare());
    	WriteInFile("./Sorted_Name.txt", dataSortedName);  
    	System.out.println("Successfully generated Sorted_Name.txt");	  	
    }

    public static void main(String[] args) throws IOException {

        DistributedSystemModification distributedSystemModification = new DistributedSystemModification();

        // Read the current data from the file.
        distributedSystemModification.readFile();
        System.out.println("Note: Use option 1 multiple times to add all changes one wishes to check synchronization on.");
        System.out.println("Note: Once added use option 2 to execute the marks updates synchrounously. Changes in the Stud_Info.txt file will be seen after using option 2");

        //menu to be displayed
        while (true) {
            int choice;
            System.out.println("Choose one option(1/2/3/4/5)\n" +
                    "1) Update student marks (Effect of all updations is seen after using option 2)\n" +
                    "2) Execute all update marks inputs uptil this point synchrounously\n" +
                    "3) Generate roll number wise sorted list\n"+
                    "4) Generate name wise sorted list\n"+
                    "5) Exit\n");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    distributedSystemModification.addInputQuery();
                    break;
                case 2:
                    distributedSystemModification.UpdateMarks();
                    break;
                case 3:
                	distributedSystemModification.GenerateRollNumberSortedFile();
                	break;
               	case 4:
               		distributedSystemModification.GenerateNameSortedFile();
                	break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid Option!");
                    break;
            }
        }
    }
}

