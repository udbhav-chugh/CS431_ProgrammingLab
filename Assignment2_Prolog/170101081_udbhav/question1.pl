% ==============================
% Parent-Child Relationship Data

parent(jatin,avantika).
parent(jolly,jatin).
parent(jolly,kattappa).
parent(manisha,avantika).
parent(manisha,shivkami).
parent(bahubali,shivkami).

% ==========
% Gender Data

male(jolly).
male(kattappa).
male(bahubali).
female(avantika).
female(shivkami).

% =========================
% Uncle Relationship Rules
% uncle(Person1,Person2) means Person1 is uncle of Person2
% it will be true if Person1 is male, Par is parent of Person2, Person1 and Par are siblings
% sibling(Person1,Person2) means Person1 and Person2 are siblings. It is defined for ease of understanding of uncle query.

sibling(Person1,Person2) :- Person1\==Person2, parent(Par,Person1), parent(Par,Person2).

uncle(Person1, Person2) :- 	male(Person1),
							parent(Par,Person2),
							sibling(Person1,Par).

% =========================
% Half Sister Relationship Rules
% halfsister(Person1,Person2) means Person1 is half sister of Person2
% it will be true if Person1 is female, Person1 and Person2 are not same, and they have exactly one common parent
% oneParentCommon(Person1,Person2) means Person1 and Person2 have exactly one parent common. It is defined for ease of understanding of half sister query.
% CommonPar is the common parent, DiffPar1 and DiffPar2 are different parents for Person1 and Person2 respectively.

oneParentCommon(Person1, Person2):- parent(CommonPar,Person1), parent(CommonPar,Person2),     
						 			parent(DiffPar1,Person1), parent(DiffPar2,Person2),
						 			CommonPar \==DiffPar1, CommonPar\==DiffPar2, DiffPar1\==DiffPar2.

halfsister(Person1, Person2) :-		female(Person1), Person1\==Person2,
									oneParentCommon(Person1,Person2).