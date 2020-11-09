package task3a1;

import java.util.EmptyStackException;
import java.util.Stack;

//converts expression to postfixExpression and then evaluates it
public class ExpressionEvaluation {

    public static String getExpressionValue(String inpString){
    	if(inpString.length() == 0){
    		return null;
    	}
        //if initially there is a + or - sign append 0 in beginning
        if(inpString.charAt(0) == '+' || inpString.charAt(0) == '-')
            inpString = "0" + inpString;

        // For operators.
        Stack<Character> operatorsStack = new Stack<Character>();
        
        // For intermediate evaluated expressions.
        Stack<Double> curEvalStack = new Stack<Double>();           
        
        Double val1, val2;
        String postfixExpression = "";
        for(int i=0;i<inpString.length();){
            if(inpString.charAt(i) == ' '){
                i++;
                continue;
            }
            if(inpString.charAt(i) >= '0' && inpString.charAt(i) <= '9'){
                String numTemp ="";
                while(i < inpString.length() && ((inpString.charAt(i) >= '0' && inpString.charAt(i) <= '9') || inpString.charAt(i) == '.'))
                    numTemp += inpString.charAt(i++);
                curEvalStack.push(Double.parseDouble(numTemp));
                postfixExpression += numTemp;
            }
            else if(inpString.charAt(i) == '*' || inpString.charAt(i) == '/'){
                while(operatorsStack.empty() == false && (operatorsStack.peek() == '*' || operatorsStack.peek() == '/')){
                    try{
                        val2 = curEvalStack.pop();
                        val1= curEvalStack.pop();
                    }
                    catch (EmptyStackException e){
                        return null;
                    }
                    if(operatorsStack.peek() == '+'){
                    	curEvalStack.push(val1+val2);
                    }
                    else if(operatorsStack.peek() == '-'){
                    	curEvalStack.push(val1-val2);
                    }
                    else if(operatorsStack.peek() == '*'){
                    	curEvalStack.push(val1*val2);
                    }
                    else if(operatorsStack.peek() == '/'){
                    	if(val2==0) 
                    		return null;
            			curEvalStack.push(Math.round((val1/val2) * 1000)/1000.0);
                    }
                    postfixExpression += operatorsStack.peek();
                    operatorsStack.pop();
                }
                operatorsStack.push(inpString.charAt(i++));
            }
            else if(inpString.charAt(i) == '+' || inpString.charAt(i) == '-'){
                while(operatorsStack.empty() == false)
                {
                    try{
                        val2 = curEvalStack.pop();
                        val1= curEvalStack.pop();
                    }
                    catch (EmptyStackException e){
                        return null;
                    }
                    if(operatorsStack.peek() == '+'){
                    	curEvalStack.push(val1+val2);
                    }
                    else if(operatorsStack.peek() == '-'){
                    	curEvalStack.push(val1-val2);
                    }
                    else if(operatorsStack.peek() == '*'){
                    	curEvalStack.push(val1*val2);
                    }
                    else if(operatorsStack.peek() == '/'){
                    	if(val2==0) 
                    		return null;
            			curEvalStack.push(Math.round((val1/val2) * 1000)/1000.0);
                    }
                    postfixExpression += operatorsStack.peek();
                    operatorsStack.pop();
                }
                operatorsStack.push(inpString.charAt(i++));
            }
        }

        while(operatorsStack.empty() == false){
            try{
                val2 = curEvalStack.pop();
                val1= curEvalStack.pop();
            }
            catch (EmptyStackException e){
                return null;
            }
            if(operatorsStack.peek() == '+'){
            	curEvalStack.push(val1+val2);
            }
            else if(operatorsStack.peek() == '-'){
            	curEvalStack.push(val1-val2);
            }
            else if(operatorsStack.peek() == '*'){
            	curEvalStack.push(val1*val2);
            }
            else if(operatorsStack.peek() == '/'){
            	if(val2==0) 
            		return null;
    			curEvalStack.push(Math.round((val1/val2) * 1000)/1000.0);
            }
            postfixExpression += operatorsStack.peek();
            operatorsStack.pop();
        }
        System.out.println("Expression to be calculated: " + inpString);
        System.out.println("Postfix form of expression: " + postfixExpression);
        try{
            Double out = curEvalStack.pop();
            if(curEvalStack.empty() == false)
                return null;
            return String.valueOf(out);
        }
        catch(EmptyStackException e){
            return null;
        }
    }
}
