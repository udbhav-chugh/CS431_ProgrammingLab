This folder contains the code for single digit input with mutiple operand and operators allowed:

For example: 3+2*5-1
NOTE: the expression is computed by transforming in postfix expression and * and / are given higher priority than + and -
So for above expression: answer will be 3+(2*5)-1 = 12
The above input will return 12

Press enter when 3 is highlighted
Press enter when + is highlighted
Press enter when 2 is highlighted
Press enter when * is highlighted
Press enter when 5 is highlighted
Press enter when - is highlighted
Press enter when 1 is highlighted
Press enter when Evaluate is highlighted


After this answer will be displayed on the screen. 
And from the answer you can continue proceeding further like above or wait for clear to highlight to clear the input and restart.


To compile the code, type:
javac -d . *.java

To run the code after compiling, type:
java task3a2.Calculator

============================================

Note: 
You can also compile using the following command:
make

To run the code after compiling, type:
java task3a2.Calculator

To remove task3a2 folder which is generated after compiling type:
make clean
