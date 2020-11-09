% BUS details database.
% There are total of 5 buses with each input in the following format:
% Bus (Number, Origin, Destination Place, Departure Time, Arrival Time, Distance, Cost)
% =================================================================================

% Sample examples like: 
% route('Amingaon', 'Ambari').
% route('Amingaon','Chandmari').
% route('Paltanbazaar', 'Ambari').
% cover all cases of waiting time, taking bus next day, different paths for optimal cost, time, distance and multiple bus changes.
% One can also try other examples of ones choice
% route('Jalukbari', 'Malingaon'). covers the case of no path exists

% Bus number 123
bus(123,'Amingaon','Jalukbari',13,14.5,100,5).
bus(123,'Amingaon','Paltanbazaar',15,15.6,50,100).
bus(123,'Amingaon','Maligaon',14,16,115,20).
bus(123,'Maligaon','Panbazaar',17.5,19.5,120,25).

% Bus number 756
bus(756,'Jalukbari','Paltanbazaar',15,16,105,10).
bus(756,'Paltanbazaar','Chandmari',14.5,14.6,110,35).
bus(756,'Amingaon','Panbazaar',14.7,14.8,80,110).

% Bus number 931
bus(931,'Amingaon','Maligaon',12,13.5,200,200).
bus(931,'Paltanbazaar','Chandmari',14.5,17,60,15).
bus(931,'Maligaon','Panbazaar',16.5,19,220,205).
bus(931,'Panbazaar','Chandmari',15,15.2,240,210).

% Bus number 864
bus(864,'Chandmari','Sundarpur',15,15.5,180,30).
bus(864,'Sundarpur','Ambari',19,20,170,25).

% Bus number 672
bus(672,'Chandmari','Sundarpur',12,17,360,150).
bus(672,'Sundarpur','Ambari',13.5,21,270,250).
bus(672,'Chandmari','Ambari',15.5,15.6,70,200).

% rules to print the bath and bus number taken to reach from source to destination

printPath([]).
printPath([Stop]) :- write(Stop),write(",xxx").
printPath([Stop,Bus|Tail]) :- write(Stop),write(","),write(Bus),write(" -> "), printPath(Tail).

% rule to update time
% if initial stop take only bus travel time
% if stop is not inital take into account the waiting time. 24 is added in case bus will be taken the next day.
updateTime(Departure, Arrival, CurArrival, Time, Time2):- CurArrival =:= -1, Time is Arrival-Departure+Time2;
														  Departure >= CurArrival, Time is Arrival-CurArrival+Time2;
														  Departure < CurArrival, Time is 24+Arrival-CurArrival+Time2.
% rule to update distance
updateDist(Dist, Dist1, Dist2) :- Dist is Dist1 + Dist2.

% rule to update cost
updateCost(Cost, Cost1, Cost2) :- Cost is Cost1 + Cost2.

% rules to recursively find paths between source and destination and set time, cost and distance for the path
% maintains a visited list to not visit the same node more than once as repeating nodes will never be optimal and might result in infinte loop

path(Stop,Stop,Path,Path,0,0,0,_).
path(Src,Dest,Visited,Path,Time,Dist,Cost,CurArrival) :- 	bus(Number,Src,Intermediate,Departure,Arrival,Dist1,Cost1),
						                                    \+ member(Intermediate,Visited),
						                                    append(Visited,[Number,Intermediate],NewVisited),
						                                    path(Intermediate,Dest,NewVisited,Path,Time2,Dist2,Cost2, Arrival),
						                                    updateTime(Departure, Arrival, CurArrival, Time, Time2),
						                                    updateDist(Dist, Dist1, Dist2),
						                                    updateCost(Cost, Cost1, Cost2).

% rule to find valid path, its time, distance and cost value given source and destination

validPath(Src,Dest,Path,Time,Dist,Cost) :- 	path(Src,Dest,[Src],Path,Time,Dist,Cost,-1).

%find paths from source to destination using backtracking. 
%Sort them based on distance as first parameter and take path with minimum cost.

optimalDistPath(Src,Dest,OptimalDistPath,Time,Dist,Cost):-  findall([D,T,C,Path],validPath(Src,Dest,Path,T,D,C),Paths),
                                                            msort(Paths,SortedPaths),
                                                            SortedPaths = [[Dist,Time,Cost,OptimalDistPath]|_].

%find paths from source to destination using backtracking. 
%Sort them based on time as first parameter and take path with minimum cost.

optimalTimePath(Src,Dest,OptimalTimePath,Time,Dist,Cost):-  findall([T,D,C,Path],validPath(Src,Dest,Path,T,D,C),Paths),
                                                            msort(Paths,SortedPaths),
                                                            SortedPaths = [[Time,Dist,Cost,OptimalTimePath]|_].

%find paths from source to destination using backtracking. 
%Sort them based on cost as first parameter and take path with minimum cost.

optimalCostPath(Src,Dest,OptimalCostPath,Time,Dist,Cost):-  findall([C,T,D,Path],validPath(Src,Dest,Path,T,D,C),Paths),
                                                            msort(Paths,SortedPaths),
                                                            SortedPaths = [[Cost,Time,Dist,OptimalCostPath]|_].

%rounding float X till D decimal places and store in Y

round(X,Y,D) :- Z is X*10^D, round(Z,ZA), Y is ZA/10^D.


% Call functions to find optimal routes based on distance cost and ime
% print the path along with distance, cost and time values for each type of optimal path.

route(Src,Dest):-
    optimalDistPath(Src,Dest,OptimalDistPath,DTime,DDist,DCost),
    round(DTime,DTimeRound,3),
    write("Optimal Distance:"),nl,
    printPath(OptimalDistPath),nl,
    write("Distance= "),write(DDist), write(", Time= "),write(DTimeRound), write(", Cost= "),write(DCost),nl,nl,
    
    optimalTimePath(Src,Dest,OptimalTimePath,TTime,TDist,TCost),
    round(TTime,TTimeRound,3),
    write("Optimal Time:"),nl,
    printPath(OptimalTimePath),nl,
    write("Distance= "),write(TDist), write(", Time= "),write(TTimeRound), write(", Cost= "),write(TCost),nl,nl,
    
    optimalCostPath(Src,Dest,OptimalCostPath,CTime,CDist,CCost),
    round(CTime,CTimeRound,3),
    write("Optimal Cost:"),nl,
    printPath(OptimalCostPath),nl,
    write("Distance= "),write(CDist), write(", Time= "),write(CTimeRound), write(", Cost= "),write(CCost),nl,!;

    write("Path Doesn't Exist."),!.