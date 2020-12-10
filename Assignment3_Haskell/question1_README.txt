This is the readme file for question 1 of Assignment 3

To run the porgram, haskell must be installed.
To run on linux use the following commands:

ghci
This will open the Prelude prompt. On it type
:l question1.hs

This will load question 1 and you are ready. Same inputs for the operations are given (Give the set inputs as list as shown below):

empty []								Output: True
empty [1,2,3]							Output: False
union [1,2,3] [2,3,4]					Output: [1,2,3,4]
intersection [1,2,3] [2,3,4]			Output: [2,3]
subtraction [1,2,3] [2,3,4]				Output: [1]
addition [1,2,3] [2,3,4]				Output: [3,4,5,6,7]

With each operation, I have also added the check for 
- expected input and output type. Eg: for union input should be 2 lists and output is single list
- The class to which elemets of list can belong to. Example: for union, element type must belong to Eq class (so that they can be compared for equality). For Minkowksi addition, apart from Eq class they must also belong to Num class since addition of char/string etc. is not defined.
- If input lists are not sets, i.e., lists do not have unique elements
Hence if you try to give a wrong input, it will generate error.
Example:
union [1,2,3]     -> Error
intersection 1 2   -> Error
addition ["abc","def"] ["a","v"] -> Error 
subtraction [1,2,2] [1,2,3] -> Error (first list is not a set)

All will give error, since union expects 2 lists but got 1 list. Similarly intersection expects two list but it got 2 integers. Similarly, since addition is not defined for strings, third input gave error. For fourth input first list is not a set(since 2 is repeated) hence it will give error. Like this error will be generated for others if wrong input type is given since that operation is not feasible.



===================
If haskell is not installed, install it using the following command on ubuntu:
sudo apt-get install haskell-platform