Here I have developed four different types of calculator covering the following cases:

1. 3a1_singleDigitSingleOperand contains answer of part a of ques3 with single operator input at a time and single digit operands.(Eg: 2+3). 

2. 3a2_singleDigitMultipleOperands contains an answer of part a of ques3 with multiple operators input in a single expression and single digit operands. (Eg: 3+2*5-1)

3. 3b1_multipleDigitSingleOperand contains answer of part b of ques3 with single operator input at a time but multi digit operands.(Eg: 324+271)

4. 3b2_multipleDigitMultipleOperand contains answer of part b of ques3 with multiple operator input in a single expression and multi digit operands.(Eg: 32+24-11)

The steps to run each individual code are provied inside each folder.

Note for the case 3a2 and 3b2 (with multiple operators), while calculating the value, * and / are given more precdence than and + and -. And between same precedence, left to right evaluation is done.

Also in case the expression is invalid (Eg: 3*/4 or 7/0), Invalid Expression is displayed on the screen.
