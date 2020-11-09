package task1;

//Imports for file reading
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

//Imports for data structure
import java.util.ArrayList;
import java.util.List;

//ReentrantLock used for synchronization
import java.util.concurrent.locks.ReentrantLock;

//Import for Random
import java.util.Random;


//Assignment 1 Task1: Sock Matching
public class SockMatchMain {
	
    private int robotArmsCount; // Number of robot arms
    private final List<Integer> socksList;  // socks heap

    // List of RobotArm Threads
    private List<RobotArm> robotArmsList; 
    // Reentrant locks for the socks so that only one robot arm accesses a particualr sock at a time
    private List<ReentrantLock> reentrantLocksList;   

    private MatchPairMachine matchPairMachine;    // Sock matcher
    private ShelfManager shelfManager;  // Shelf manager

    //Constructor
    SockMatchMain(int robotArmsCount, List<Integer> socksList) {
        this.robotArmsCount = robotArmsCount;
        this.socksList = socksList;

        // Create Shelf manager and match pair machine
        shelfManager = new ShelfManager();
        matchPairMachine = new MatchPairMachine(shelfManager);

        // Create robotic arms and associated locks
        robotArmsList = new ArrayList<>();
        for (int i = 0; i < robotArmsCount; i++) {
            RobotArm robotArm = new RobotArm(this, this.matchPairMachine, String.valueOf(i));
            robotArmsList.add(robotArm);
        }
        reentrantLocksList = new ArrayList<>();
        for (int i = 0; i < socksList.size(); i++) {
            ReentrantLock reentrantLock = new ReentrantLock();
            reentrantLocksList.add(reentrantLock);
        }
    }


    //get a sock from heap
    // Display could not acquire lock and retry if tried to access sock which is already being accessed by another robot arm.
    //return -1 if no sock left
    int getSockFromHeap(String threadName) {
        int socksListIndex = -1;
        boolean isSockLeft = true;
        int sockValue;

        // Generate a random number and lock that sock
    	Random rand = new Random();
        synchronized (socksList) {
            if (socksList.size() > 0) {
                socksListIndex = rand.nextInt(socksList.size());
            } else {
                return -1; //No sock left
            }
        }

        // Try to acquire lock of the sock
        boolean isAquired = reentrantLocksList.get(socksListIndex).tryLock();
        
        if (isAquired) {
        	synchronized (socksList) {
        		if(socksListIndex < socksList.size()){
		            sockValue = socksList.get(socksListIndex);
		            socksList.remove(socksListIndex);
        			// Release the lock so that it can be acquired by some other robot arm
            		reentrantLocksList.get(socksListIndex).unlock();
            		return sockValue;
        		}
        		else{
        			// Release the lock so that it can be acquired by some other robot arm
            		reentrantLocksList.get(socksListIndex).unlock();
            		System.out.println("Access denied to thread " + threadName + ". Will try to pick another sock");
        			return getSockFromHeap(threadName);
        		}
	        }
            //return the sockValue
        } else {
            System.out.println("Access denied to thread " + threadName + ". Will try to pick another sock");
            //retry acquiring diffferent sock from heap
            return getSockFromHeap(threadName);
        }
    }

    //Initilaise the robot arms and print total socks paired after completion of the process.
    private void executeSockMatching() throws InterruptedException {
        //Start robot arms
        for(int i=0;i<robotArmsList.size();i++){
        	RobotArm robotArm = robotArmsList.get(i);
            robotArm.start();
        }
        // wait for all robot arms to stop
        for(int i=0;i<robotArmsList.size();i++){
        	RobotArm robotArm = robotArmsList.get(i);
            robotArm.join();
        }
        // Print the collected socks count
        List<Integer> finalSockCount = shelfManager.getFinalSockCount();
            System.out.println("\n");
            System.out.println("Color Code: 0->whiteSock\t1->blackSock\t2->blueSock\t3->greySock\n");
        for(int i=0;i<4;i++){
        	System.out.println("Total socks paired of type " + i + " = " + finalSockCount.get(i));
        }
    }

    //Main function
    public static void main(String[] args) throws IOException, InterruptedException {
        // File to take input from
        File inputFile = new File("./input.txt");
        Scanner scanner = new Scanner(inputFile);

        // Take the number of robots as input
        int robotArmsCount = scanner.nextInt();
        List<Integer> socksList = new ArrayList<>();

        // Take the list of socks as input and check for valid input
        boolean wrongInput = false;
        while (scanner.hasNextInt()) {
            int nextSock = scanner.nextInt();
            if(nextSock <0 || nextSock >3){
                System.out.println("Sock color must be from 0 to 3. Wrong Input. Exiting");
                wrongInput = true;
                break;
            }
            else{
	            socksList.add(nextSock);
            }
        }
        if(wrongInput == false){
            // Create a sock matching machine and execute process
            System.out.println("Color Code: 0->whiteSock\t1->blackSock\t2->blueSock\t3->greySock\n");

            SockMatchMain sockMatchMain = new SockMatchMain(robotArmsCount, socksList);
            sockMatchMain.executeSockMatching();   
        }
    }


}
