package task3b1;

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

	public JButton[] numKeys = new JButton[12];
	public JButton[] operatorKeys = new JButton[4];
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

		numKeys[11] = new JButton("Clear");
		numKeys[11].setFont(new Font("Gisha", Font.BOLD, 14));
		numKeys[11].setBackground(colorGrey);
		mainWindowFrame.getContentPane().add(numKeys[11]);

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
		numKeys[11].setBounds(350, 315, 110, 50);


		//operator keys
		for(int i = 0;i<4;i++){
			operatorKeys[i] = new JButton(getOperator(i));
			operatorKeys[i].setFont(new Font("Gisha", Font.BOLD, 17));
			operatorKeys[i].setBackground(colorGrey);
			mainWindowFrame.getContentPane().add(operatorKeys[i]);
		}
		xcord = 290;
		for(int i=0;i<4;i++){
			operatorKeys[i].setBounds(xcord,380,50,50);
			xcord+=60;
		}

		//Insturction labels
		lbl = new JLabel("Instrctions:");
		lbl.setBounds(50, 450, 700, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("1. Keep Pressing Enter to select first number");
		lbl.setBounds(50, 465, 700, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("2. Press Space to select operator.");
		lbl.setBounds(50, 480, 700, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("3. In an expression you can have only one operator since this is the case of single operator.");
		lbl.setBounds(50, 495, 700, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("4. After one selection, operator selection stops until input is evaluated or cleared.");
		lbl.setBounds(50, 510, 700, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("5. See folder task3b2 if you want multiple digits and multiple operators.");
		lbl.setBounds(50, 525, 700, 14);
		mainWindowFrame.getContentPane().add(lbl);
		

		lbl = new JLabel("6. Keep Pressing Enter to select second number");
		lbl.setBounds(50, 540, 700, 14);
		mainWindowFrame.getContentPane().add(lbl);

		lbl = new JLabel("7. Press Enter on Clear to clear input and repeat process");
		lbl.setBounds(50, 555, 700, 14);
		mainWindowFrame.getContentPane().add(lbl);


		lbl = new JLabel("8. If expression is invalid, Invalid Expression will be displayed.");
		lbl.setBounds(50, 570, 700, 14);
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
		return null;
	}

	//keypress events
	//curState = 0 -> operator not pressend once
	//curState = 1 -> operator pressed once
	@Override
	public void keyPressed(KeyEvent e) {
		if(displayText.getText().equals("Invalid Expression")){
			displayText.setText("");
		}
		//enter number
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			int temp = numHighlighter.i;
			if(temp == 10){
				String temp1 = ExpressionEvaluation.getExpressionValue(displayText.getText());
				if(temp1==null){
					temp1="Invalid Expression";
					System.out.println(temp1);
				}
				displayText.setText(temp1);
				operatorKeys[operator].setBackground(colorGrey);
				operatorHighlighter = new keyHighlighter(operatorKeys, 1);
				operatorHighlighter.execute();
				curState=0;
			}
			else if(temp == 11){
				displayText.setText("");
				curState = 0;
				operatorKeys[operator].setBackground(colorGrey);
				operatorHighlighter = new keyHighlighter(operatorKeys, 1);
				operatorHighlighter.execute();
			}
			else{
				displayText.setText(displayText.getText() + String.valueOf(temp));
			}
			
		}
		//select operator
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			if(curState==0){
				operator = operatorHighlighter.i;
				displayText.setText(displayText.getText() + getOperator(operator));
				operatorHighlighter.cancel(true);
				curState = 1;
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
