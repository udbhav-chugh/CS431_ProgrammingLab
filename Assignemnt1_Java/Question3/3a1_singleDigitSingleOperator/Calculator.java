package task3a1;

import java.awt.Color;
import java.awt.EventQueue;

//Swing imports
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//main Calculator class
//implements standard java keylistner interface
public class Calculator implements KeyListener{
	private JFrame mainWindowFrame; //main frame
	private Integer curState = 0, operator; // curState is used to decide the operation to be performed
	private JTextField displayText; //text field
	private JLabel lbl; //label to display instructions

	private Color colorGrey; //key color without highlight

	public JButton[] numKeys = new JButton[10];
	public JButton[] operatorKeys = new JButton[5];
	private keyHighlighter numHighlighter = new keyHighlighter(numKeys, 0);
	private keyHighlighter operatorHighlighter = new keyHighlighter(operatorKeys, 1);

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Calculator window = new Calculator();
					window.mainWindowFrame.setVisible(true);
					window.mainWindowFrame.setFocusable(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public Calculator() {
		initialize();
		//keylistner on the main mainWindowFrame
		mainWindowFrame.addKeyListener(this);
		//start the number highlight thread
		numHighlighter.execute();
	}

	private void initialize() {
		//main frame
		mainWindowFrame = new JFrame();
		mainWindowFrame.setResizable(false);
		mainWindowFrame.setBounds(100, 100, 800, 700);
		mainWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindowFrame.getContentPane().setLayout(null);

		colorGrey = new Color(220, 220, 215);

		displayText = new JTextField();
		displayText.setHorizontalAlignment(SwingConstants.CENTER);
		displayText.setFont(new Font("Gisha", Font.PLAIN, 13));
		displayText.setEditable(false);
		displayText.setBounds(300, 10, 200, 50);
		mainWindowFrame.getContentPane().add(displayText);
		displayText.setColumns(10);

		//number keys
		for(int i = 0;i<10;i++){
			numKeys[i] = new JButton(String.valueOf(i));
			numKeys[i].setFont(new Font("Gisha", Font.BOLD, 17));
			numKeys[i].setBackground(colorGrey);
			mainWindowFrame.getContentPane().add(numKeys[i]);
		}
		int xcord = 310, ycord = 70;
		for(int i=0;i<9;i++){
			numKeys[i].setBounds(xcord,ycord,50,50);
			xcord+=60;
			if(i>0 && i%3==2){
				ycord+=60;
				xcord=310;
			}
		}
		numKeys[9].setBounds(370, 250, 50, 50);
		
		//operator keys
		for(int i = 0;i<5;i++){
			operatorKeys[i] = new JButton(getOperator(i));
			operatorKeys[i].setFont(new Font("Gisha", Font.BOLD, 17));
			operatorKeys[i].setBackground(colorGrey);
			mainWindowFrame.getContentPane().add(operatorKeys[i]);
		}
		xcord = 290;
		for(int i=0;i<4;i++){
			operatorKeys[i].setBounds(xcord,320,50,50);
			xcord+=60;
		}
		operatorKeys[4].setBounds(320, 380, 150, 50);

		//Insturction labels
		lbl = new JLabel("Instrctions:");
		lbl.setBounds(250, 450, 500, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("1. Press Enter to select first number");
		lbl.setBounds(250, 465, 500, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("2. Press Enter to select operator");
		lbl.setBounds(250, 480, 500, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("3. Press Enter to select second number");
		lbl.setBounds(250, 495, 500, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("4. Press Enter on Clear to clear input and repeat process.");
		lbl.setBounds(250, 510, 500, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("5. If expression is invalid, Invalid Expression will be displayed.");
		lbl.setBounds(250, 525, 500, 14);
		mainWindowFrame.getContentPane().add(lbl);

	}
	//method the get the operator corresponding to input number
	private String getOperator(Integer num){
		if(num==0){
			return "+";
		}
		else if(num==1){
			return "-";
		}
		else if(num==2){
			return "*";
		}
		else if(num==3){
			return "/";
		}
		else if(num==4){
			return "Clear";
		}
		return null; //wont arrise
	}

	//keypress events
	//curState = 0->initial number not entered or cleared input
	//curState = 1-> first operand entered, waiting for operator
	//curState = 2-> waiting for second operand
	@Override
	public void keyPressed(KeyEvent e) {
		if(displayText.getText().equals("Invalid Expression")){
			displayText.setText("");
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			if(curState == 0){
				int temp = numHighlighter.i;
				//stop number highlight thread;
				//update text field with number
				displayText.setText(displayText.getText() + String.valueOf(temp));
				numHighlighter.cancel(true);
				numKeys[temp].setBackground(colorGrey);
				operatorHighlighter = new keyHighlighter(operatorKeys, 1);
				operatorHighlighter.execute();
				curState = 1;
			}
			else if(curState == 1){
				operator = operatorHighlighter.i;
				if(operator == 4){
					displayText.setText("");
					curState = 0;
				}
				else{
					displayText.setText(displayText.getText() + getOperator(operator));
					curState = 2;
				}
				//stop operator highlight thread
				operatorHighlighter.cancel(true);
				operatorKeys[operator].setBackground(colorGrey);				
				//new number object needs to be created
				numHighlighter = new keyHighlighter(numKeys, 0);
				numHighlighter.execute();
				
			}
			else if(curState == 2){
				int temp = numHighlighter.i;
				numHighlighter.cancel(true);
				displayText.setText(displayText.getText() + String.valueOf(temp));
				numKeys[temp].setBackground(colorGrey);
				//set text after evaluation of expression
				String temp1 = ExpressionEvaluation.getExpressionValue(displayText.getText());
				if(temp1==null){
					temp1="Invalid Expression";
					displayText.setText(temp1);

					System.out.println(temp1);
					curState = 0;
					numHighlighter = new keyHighlighter(numKeys, 0);
					numHighlighter.execute();
				}
				else{
					displayText.setText(temp1);
					operatorHighlighter = new keyHighlighter(operatorKeys, 1);
					operatorHighlighter.execute();
					curState = 1;
				}

				
			}
		}
	}
	//Nothing to do on key released or typed
	@Override
	public void keyReleased(KeyEvent arg0) {

	}
	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
