This is the readme file for question 2 of Assignment 3

To run the porgram, haskell must be installed.
To run on linux use the following commands:

ghci
This will open the Prelude prompt. On it type
:l question2.hs

This will load question 2 and you are ready. Same inputs for the operations are given:

Use the following command to generate a random fixtures draw and print it:
fixture "all"

Use the following command to get fixture for a particular team:
fixture "DS"

Use the following command to get the match at a time greater than or equal to give time
nextMatch 1 13.25


Note: 
fixture "all" generates a new random set of fixtures everytime it is called and remaining queries are answered based on the latest fixtures generated. If fixtures are not generated yet, and query is called it will give an error message saying you should call fixture "all" first. Also, if you want to print the current generated fixtures without regenrating them I have added a command:
fixture "current"
This will print current fixtures. 
Use fixture "all" to generate random fixtures and print them.


===================
If haskell is not installed, install it using the following command on ubuntu:
sudo apt-get install haskell-platform