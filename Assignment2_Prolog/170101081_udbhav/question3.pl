% ======================================================================================================
% directedConnections(src,dest,dist) indicates it is possible to go from src to dest gate in dist distance.
% undirectedConnections(src, dest, dist) is used to indicate path from both src to dest and dest to src.
% Throughout the question undirectedConnections(Src,Dest,Dist) is used since movement in both directions is allowed.
% undirectedConnections(Src,Dest,Dist) returns true if path exists either from Src to Dest or Dest to Src of distance Dist
% ======================================================================================================

directedConnections('G1','G5',4).
directedConnections('G2','G5',6).
directedConnections('G3','G5',8).
directedConnections('G4','G5',9).
directedConnections('G1','G6',10).
directedConnections('G2','G6',9).
directedConnections('G3','G6',3).
directedConnections('G4','G6',5).
directedConnections('G5','G7',3).
directedConnections('G5','G10',4).
directedConnections('G5','G11',6).
directedConnections('G5','G12',7).
directedConnections('G5','G6',7).
directedConnections('G5','G8',9).
directedConnections('G6','G8',2).
directedConnections('G6','G12',3).
directedConnections('G6','G11',5).
directedConnections('G6','G10',9).
directedConnections('G6','G7',10).
directedConnections('G7','G10',2).
directedConnections('G7','G11',5).
directedConnections('G7','G12',7).
directedConnections('G7','G8',10).
directedConnections('G8','G9',3).
directedConnections('G8','G12',3).
directedConnections('G8','G11',4).
directedConnections('G8','G10',8).
directedConnections('G10','G15',5).
directedConnections('G10','G11',2).
directedConnections('G10','G12',5).
directedConnections('G11','G15',4).
directedConnections('G11','G13',5).
directedConnections('G11','G12',4).
directedConnections('G12','G13',7).
directedConnections('G12','G14',8).
directedConnections('G15','G13',3).
directedConnections('G13','G14',4).
directedConnections('G14','G17',5).
directedConnections('G14','G18',4).
directedConnections('G17','G18',8).

% undirectedConnections(Src,Dest,Dist) returns true if path exists either from Src to Dest or Dest to Src of distance Dist

undirectedConnections(Src,Dest,Dist) :- directedConnections(Src,Dest,Dist).
undirectedConnections(Src,Dest,Dist) :- directedConnections(Dest,Src,Dist).

% the 4 gates from where entry is possible. Exit is possible only from 'G17'

begin('G1').
begin('G2').
begin('G3').
begin('G4').

%rules to print a list separated by ->

printSinglePath([]).
printSinglePath([Head]) :-     	write(Head),!.
printSinglePath([Head|Tail]) :-	write(Head),write(' -> '),
                            	printSinglePath(Tail).

% rules to print multiple lists.

printMultiplePaths([]).
printMultiplePaths([Head|Tail]) :-  printSinglePath(Head),nl,
                              		printMultiplePaths(Tail).

% rules to count number of elements in a list.

getListCount([],0).
getListCount([_|Tail],CountPaths) :-	getListCount(Tail, CountTemp),
										CountPaths is CountTemp+1.

% rules to find paths between source and destination along with the distance travelled on this path
% a visited list is maintained to get the paths with no gates repeated. 
% Since if gate repetion is not checked, infinite paths will be generated and any optimal path will never have repeated gates in it

uniqueGatesPath(Gate,Gate,Path,Path,0).
uniqueGatesPath(Src,Dest,Visited,Path,Dist) :- undirectedConnections(Src,Intermediate,Dist1),
	                                          \+ member(Intermediate,Visited),
	                                          append(Visited,[Intermediate],NewVisited),
	                                          uniqueGatesPath(Intermediate,Dest,NewVisited,Path,Dist2),
	                                          Dist is Dist1+Dist2.

% rules to find valid path along with the distance. 
% Valid path means path must begin at one of G1,G2,G3,G4 and must end at G17
% Also a path must exist between these gates

validUniqueGatesPath(Path,Dist) :-  [B|_] = Path, begin(B),
                              		uniqueGatesPath(B,'G17',[B],Path,Dist).

% rules to just check if path is valid without calculating distance
% here repetition of gates is allowed we just need to find if path is valid or not
path([Src,'G17']) :-                    undirectedConnections(Src,'G17',_).
path([Src,Dest,NextDest|PathList]) :-   undirectedConnections(Src,Dest,_),
                                        append([Dest,NextDest],PathList,RemainingList),
                                        path(RemainingList).

% rules to check if the input path is valid without worrying about distance
% begins at G1,G2,G3,G4 and end at G17 and path exists between begin point and end

validPath(Path) :- [B|_] = Path, begin(B),path(Path).

% rules to traverse sorted list till the point distanceof paths is equal to shortest path distance
% to ensure if multiple optimal paths exist, all are printed
allOptimalPaths([],_,[]).
allOptimalPaths([[MinDist,Head]|Tail],MinDist,[Head|OptimalList]) :- allOptimalPaths(Tail,MinDist,OptimalList);!.

% rules to find the the optimal paths from one of the beginning gates(G1,G2,G3,G4) to G17
% the algorithm uses backtracking to find all paths between begin gate and end gate by calling validUniqueGatesPath defined above
% it then sorts the path based on distance and the path with smallest distance is the most optimal path.
% In case multiple optimal paths exist, allOptimalPaths rules will give all such paths and these paths along with optimal distance are then printed

optimal(OptimalPath) :- findall([Dist,Path],validUniqueGatesPath(Path,Dist),ListOfPaths),
                 		msort(ListOfPaths,SortedPaths),
						SortedPaths = [[MinDist, OptimalPath] | _],
						allOptimalPaths(SortedPaths,MinDist,ListOfOptimalPaths),
						write("Optimal Path(s): "),nl,
						printMultiplePaths(ListOfOptimalPaths),
						write("Optimal Distance Value: "),write(MinDist),!.

% simply prints a possible path and can have repeated gates. 
% Since in cases with repeated gates there can be infinite paths, it prints one of them and you can use ; to print next and use . to stop
repeatGatesPath :-  validPath(Path),
                  	printSinglePath(Path),nl.

% finds and prints all possible paths to escape.
% does not repeat gates as otherwise with gate repetion allowed there will be infinite paths

allPaths :- findall(Path,validUniqueGatesPath(Path,_),ListOfPaths),
          	getListCount(ListOfPaths,PathCount),
          	printMultiplePaths(ListOfPaths),nl,
          	write("Total Paths: "),write(PathCount).

% get the optimal Paths and print them along the optimal distnace value

optimal :- optimal(_).

% check if a given path is valid or not

valid(Path) :- validPath(Path),!.
