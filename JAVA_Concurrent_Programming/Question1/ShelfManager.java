package task1;

//Imports for data structure
import java.util.ArrayList;
import java.util.List;

//Shelf Manager recieves a sock pair and puts it in the right shelf
class ShelfManager {
    // Count of socks represents the different shelves
    private Integer sockCountWhite, sockCountBlack, sockCountBlue, sockCountGrey;
    private Object whiteCountLock, blackCountLock, blueCountLock, greyCountLock;

    //Initialise Sock count of each color with 0;
    ShelfManager() {
        sockCountWhite = sockCountBlack = sockCountBlue = sockCountGrey = 0;
        whiteCountLock = new Object(); 
        blackCountLock = new Object();
        blueCountLock = new Object();
        greyCountLock = new Object();
    }

    //Incrementing count for received pair of socks
    //Synchronized block is not directly applied on Integer rather an Object since instance of Integer cant be controlled
    void ManageSockPair(int sockValue) {
        switch(sockValue){
    		case 0: synchronized(whiteCountLock){
    					sockCountWhite += 2;
        				System.out.println("Shelf Manager put a pair of color " + sockValue + " socks in corresponding shelf");
    				} 
    				break;

    		case 1: synchronized(blackCountLock){
    					sockCountBlack += 2;
        				System.out.println("Shelf Manager put a pair of color " + sockValue + " socks in corresponding shelf");
    				} 
    				break;

    		case 2: synchronized(blueCountLock){
    					sockCountBlue += 2;
        				System.out.println("Shelf Manager put a pair of color " + sockValue + " socks in corresponding shelf");
    				} 
    				break;

    		case 3: synchronized(greyCountLock){
    					sockCountGrey += 2;
        				System.out.println("Shelf Manager put a pair of color " + sockValue + " socks in corresponding shelf");
    				} 
    				break;
    	}
    }

    // Return the collected socks count for each sock.
    List<Integer> getFinalSockCount() {
        List<Integer> sockCount = new ArrayList<>();
        sockCount.add(sockCountWhite);
        sockCount.add(sockCountBlack);
        sockCount.add(sockCountBlue);
        sockCount.add(sockCountGrey);
        return sockCount;
    }
}
