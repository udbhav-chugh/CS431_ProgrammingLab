package task3b2;

import java.awt.Color;
import java.awt.EventQueue;

//Swing imports
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;


import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.SwingConstants;
import javax.swing.JLabel;

//main Calculator class
//implements standard java keylistner interface
public class Calculator implements KeyListener{
	private JFrame mainWindowFrame; //main frame
	private Integer curState = 0, operator; // curState is used to decide the operation to be performed
	private JTextField displayText; //text field
	private JLabel lbl; //label to display instructions

	private Color colorGrey; //key color without highlight

	public JButton[] numKeys = new JButton[11];
	public JButton[] operatorKeys = new JButton[5];
	private keyHighlighter numHighlighter = new keyHighlighter(numKeys, 0);
	private keyHighlighter operatorHighlighter = new keyHighlighter(operatorKeys, 1);


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//create a new object on start
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
		//keylistner is added on the main mainWindowFrame
		mainWindowFrame.addKeyListener(this);
		//start the number highlight thread
		numHighlighter.execute();
		operatorHighlighter.execute();
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
		numKeys[10] = new JButton("Evaluate");
		numKeys[10].setFont(new Font("Gisha", Font.BOLD, 14));
		numKeys[10].setBackground(colorGrey);
		mainWindowFrame.getContentPane().add(numKeys[10]);

		int xcord = 310, ycord = 70;
		for(int i=0;i<9;i++){
			numKeys[i].setBounds(xcord,ycord,50,50);
			xcord+=60;
			if(i>0 && i%3==2){
				ycord+=60;
				xcord=310;
			}
		}
		numKeys[9].setBounds(310, 250, 50, 50);
		numKeys[10].setBounds(370, 250, 110, 50);

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
		operatorKeys[4].setBounds(350, 380, 110, 50);

		//Insturction labels
		lbl = new JLabel("Instrctions:");
		lbl.setBounds(150, 450, 600, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("1. Keep Pressing Enter to select number");
		lbl.setBounds(150, 465, 600, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("2. Press Space to select operator");
		lbl.setBounds(150, 480, 600, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("3. Repeat this process as many times as needed");
		lbl.setBounds(150, 495, 600, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("4. Press Enter on Evaluate to get answer");
		lbl.setBounds(150, 510, 600, 14);
		mainWindowFrame.getContentPane().add(lbl);
		
		lbl = new JLabel("5. Press Space on Clear to clear input");
		lbl.setBounds(150, 525, 600, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("6. In an expression, * and / are given higher priority than + and -.");
		lbl.setBounds(150, 540, 600, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("7. If expression is invalid, Invalid Expression will be displayed.");
		lbl.setBounds(150, 555, 600, 14);
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
		return null;
	}

	//only 1 state curState=0, since any number of operands and operators can be input
	@Override
	public void keyPressed(KeyEvent e) {
		if(displayText.getText().equals("Invalid Expression")){
			displayText.setText("");
		}
		//enter number
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			if(curState == 0){
				int temp = numHighlighter.i;
				if(temp == 10){
					String temp1 = ExpressionEvaluation.getExpressionValue(displayText.getText());
					if(temp1==null){
						temp1="Invalid Expression";
						System.out.println(temp1);
					}
					displayText.setText(temp1);
				}
				else{
					displayText.setText(displayText.getText() + String.valueOf(temp));
				}
			}
			
		}
		//select operator
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			if(curState==0){
				operator = operatorHighlighter.i;
				if(operator==4){
					displayText.setText("");
				}
				else{
					displayText.setText(displayText.getText() + getOperator(operator));
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
