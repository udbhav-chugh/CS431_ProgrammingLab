This is the readme file for Question3.

Run the file question3.pl in SWI prolog.

On linux, SWI prolog should be installed.
Use command:
prolog

to open the prolog interface in terminal.

Then type:
['question3.pl'].
to load question 3.

Alternatively without using above two commands from terminal itself you can run code using:
swipl -s question3.pl


Sample queries:

allPaths.		Output: Print all paths to escape and total count of paths

optimal.		Output: Optimal Path(s): 
						G3 -> G6 -> G12 -> G14 -> G17
						Optimal Distance Value: 19

valid(['G1', 'G6', 'G8', 'G9', 'G8', 'G7', 'G10', 'G15', 'G13', 'G14', 'G18', 'G17']).		Output: true.
valid(['G1', 'G6', 'G8', 'G9', 'G8', 'G7', 'G10', 'G15', 'G13', 'G14', 'G18']).				Output: false.


Make sure to put gates in inverted commas in the valid query.


Explanation and purpose of queries:

allPaths.
This will print all possible paths to escape jail along with the count of paths. Note that this doesnt allow repeated gates as otherwise with repeated gates there can be infinite paths. You can use 
repeatGatesPath.
in case you want paths with repeated sequence allowed. This prints only one path and you can use ; to print next path and so on. repeatGatesPath rule allows repeated gates(which means infinite paths), hence it will always print path one by one on pressing ; Hence use . to end query

optimal.
This will get the optimal Paths to escape jail and print them along the optimal distnace value.
You can also use 
optimal(X). 		Output: Optimal Path(s): 
							G3 -> G6 -> G12 -> G14 -> G17
							Optimal Distance Value: 19
							X = ['G3', 'G6', 'G12', 'G14', 'G17'].

Here additionally to printing, it will assign the most optimal path to X.

valid(['G1', 'G6', 'G8', 'G9', 'G8', 'G7', 'G10', 'G15', 'G13', 'G14', 'G18', 'G17']).
This will check if a given path is valid or not.

If path is valid, it prints
true.
If path is invalid, it prints
false.




============
To install SWI prolog use the following commands:

sudo apt-add-repository ppa:swi-prolog/stable
sudo apt-get update
sudo apt-get install swi-prolog
