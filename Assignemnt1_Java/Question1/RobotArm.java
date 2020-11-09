package task1;

//Each robot arm is a separate thread.
//It picks a sock from heap and gives it to matching machine.
class RobotArm extends Thread {
    private SockMatchMain sockMatchMain;  // To acces the socks heap and pick one sock
    private MatchPairMachine matchPairMachine;    // To pass the picked sock the sock matcher

    //Constructor
    RobotArm(SockMatchMain sockMatchMain, MatchPairMachine matchPairMachine, String name) {
        this.sockMatchMain = sockMatchMain;
        this.matchPairMachine = matchPairMachine;
        super.setName(name);
    }

    @Override
    public void run() {
    	boolean workLeft = true;
        while (workLeft == true) {
            // Pick a sock from the heap
            int sockValue = sockMatchMain.getSockFromHeap(getName());
            // If no sock left in the heap
            if (sockValue == -1){   
                workLeft = false; //robot arm work is done
		System.out.println("Robot arm " + getName()  + " is stopping as no more socks left to pick!");
            }
            else{
                // Pass the picked sock the sock matcher
                System.out.println("Robot arm " + getName()  + " picked the sock of color " + sockValue + " and gave to matching machine");
                matchPairMachine.MatchPair(sockValue);
            }
        }
    }
}
