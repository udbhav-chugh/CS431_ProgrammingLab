This is the readme file for question 3 of Assignment 3

To run the porgram, haskell must be installed.
To run on linux use the following commands:

ghci
This will open the Prelude prompt. On it type
:l question3.hs

This will load question 3 and you are ready. Same inputs for the operations are given:
NOTE: The code takes an average of 4 seconds to run and might take around 18-20 seconds in worst cases.

design (1000,3,2)

Output:
Bedroom: 3 (10 X 10)
Hall: 2 (15 X 10)
Kitchen: 1 (7 X 5)
Bathroom: 4 (4 X 5)
Garden: 1 (13 X 20)
Balcony: 1 (5 X 5)
Unused Space: 0

For each type of room, I look at all possible dimensions that a particular room can take up. To optimze the code, I continously remove configurations that result is same total area. See the report for exact details of algorithm implementation.


===================
If haskell is not installed, install it using the following command on ubuntu:
sudo apt-get install haskell-platform
