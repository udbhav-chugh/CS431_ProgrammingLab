package task3a2;

import java.awt.Color;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.JButton;

//class to change highlighted key periodically in background
public class keyHighlighter extends SwingWorker<Integer, Integer> {

  //there are two types of object highlight(numbers and operators)
  private JButton[] numOrOpButton;
  public Integer typeOfKey;
  private Color colorGrey; //key color without highlight
  private Color colorBlue; //number color with highlight
  private Color colorRed; //operator color with highlight
  public Integer i = 0;
  //constructor
  public keyHighlighter(JButton[] numOrOpButton, Integer typeOfKey) {
      this.numOrOpButton = numOrOpButton;
      this.typeOfKey = typeOfKey;
      colorGrey = new Color(220, 220, 215);
      colorBlue = new Color(0, 100, 220);
      colorRed = new Color(220, 50, 0);
      if(typeOfKey==0){
        i=9;
      }
      else{
        i=5;
      }
  }
  //process() receives data chunks from the publish method asynchronously on the Event Dispatch Thread
  //and uses it to change the color of the highlighted cell accordingly.
  protected void process(final List<Integer> chunks) {
    for (Integer chunkNumber : chunks) {
    	if(typeOfKey == 0){
    		numOrOpButton[chunkNumber].setBackground(colorBlue);
    		if(chunkNumber == 0){
          numOrOpButton[9].setBackground(colorGrey);
        }
        else{
          numOrOpButton[chunkNumber-1].setBackground(colorGrey);
        }
    	}
    	else{
    		numOrOpButton[chunkNumber].setBackground(colorRed);
    		if(chunkNumber == 0){
          numOrOpButton[5].setBackground(colorGrey);
        }
        else{
          numOrOpButton[chunkNumber-1].setBackground(colorGrey);
        }
    	}
    }
  }

  @Override
  //the thread increments the value of i, and them sleeps for 1.2sec
  protected Integer doInBackground() throws Exception {
      while(true){
        if(typeOfKey == 0) //for numbers
          i = (i+1)%10;
        else if(typeOfKey == 1) //for operators, evaluate and clear
          i = (i+1)%6;
        publish(i);
        Thread.sleep(1200);
      }
  }
}
