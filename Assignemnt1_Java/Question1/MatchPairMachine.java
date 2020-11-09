package task1;

//Imports for data structure
import java.util.ArrayList;
import java.util.List;

// Maintain if any colored sock is present in Matching Machine or not.
// If present, pair it up and send to shelf manager. Else mark that color as present.
class MatchPairMachine {
    private ShelfManager shelfManager;
    private Boolean isSockPresentWhite, isSockPresentBlack, isSockPresentBlue, isSockPresentGrey;
    private Object whiteLock, blackLock, blueLock, greyLock;

    MatchPairMachine(ShelfManager shelfManager) {
        this.shelfManager = shelfManager;
       	isSockPresentWhite = isSockPresentBlack = isSockPresentBlue = isSockPresentGrey = false;
        whiteLock = new Object(); 
        blackLock = new Object();
        blueLock = new Object();
        greyLock = new Object();
    }

    Boolean MatchPairHelper(Boolean isSockPresent, int sockValue){
        //one sock present, so make pair and pass to shelf manager.
    	if(isSockPresent == true){
            System.out.println("Matching Machine matched a pair of color " + sockValue + " and passed it to shelf manager");
           	shelfManager.ManageSockPair(sockValue);
            isSockPresent = false;
    		return isSockPresent;
    	}
        // this color sock not present. Mark it as present.
    	else{
    		isSockPresent = true;
    		return isSockPresent;
    	}
    }

    //Match pair to handle each individual color.
    //Synchronized block is not directly applied on Boolean rather an Object since instance of Boolean cant be controlled
    void MatchPair(int sockValue) {
    	switch(sockValue){
    		case 0: synchronized(whiteLock){
						isSockPresentWhite = MatchPairHelper(isSockPresentWhite, 0);
    				}
    				break;

    		case 1: synchronized(blackLock){
						isSockPresentBlack = MatchPairHelper(isSockPresentBlack, 1);
    				}
    				break;

    		case 2: synchronized(blueLock){
						isSockPresentBlue = MatchPairHelper(isSockPresentBlue, 2);
    				}
    				break;

    		case 3: synchronized(greyLock){
						isSockPresentGrey = MatchPairHelper(isSockPresentGrey, 3);
    				}
    				break;
    	}
    }
}
