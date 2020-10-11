package task2;
import java.util.ArrayList;

//Evaluator class (uses threads)
public class Evaluator extends Thread {
    private DistributedSystemModification distributedSystemModification;
    private ArrayList<UpdateInfo> updateInputs;

    //Constructor
    Evaluator(DistributedSystemModification distributedSystemModification, String Name, int priority) {
        this.distributedSystemModification = distributedSystemModification;
        setName(Name);
        setPriority(priority);
        updateInputs = new ArrayList<>();
    }

    // Update marks till updateInputs is empty
    @Override
    public void run() {
        while (updateInputs.size() > 0) {
        	String rollNum = updateInputs.get(0).getRollNum();
        	int marksToUpdate = Integer.parseInt(updateInputs.get(0).getMarksToUpdate());
            distributedSystemModification.updateSyncData(rollNum, marksToUpdate, getName());
            updateInputs.remove(0);
        }
    }

    //Add update marks input to updateInputs to execute later when execute is requested by user
    void addInputQueryEvaluator(String rollNumber, String updateMarks) {
        UpdateInfo newInput = new UpdateInfo(getName(), rollNumber, updateMarks);
        updateInputs.add(newInput);
    }

}
