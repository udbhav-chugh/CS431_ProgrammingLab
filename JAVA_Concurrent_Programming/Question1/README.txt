This is the README file for task1 of Assignment1: Sock Matching Robot

To compile the code, type the following command in the current folder:
javac -d . *.java

This will create classes corressponding to each Java file.

After compiling, to run the code, type the following command:
java task1.SockMatchMain

Note: The input to the code is provided from the input.txt in the resources folder of task1 and output is displayed on the terminal.
The first number of the file is the robot arms used for sock matching.
After that there are a series of numbers from 0 to 3 representing the sock type.
0-> white
1-> black
2-> blue
3-> grey

Note to compile the code, you can also run the following command to run the makefile:
make
And then to run the code, type:
java task1.SockMatchMain

To delete the "task1" folder which is created after compilation you can run the following command:
make clean
