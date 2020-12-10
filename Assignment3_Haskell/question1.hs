-- checks if input is a list and that list a set,i.e., has no duplicate elements
-- if returns false it means input list has duplicate elements
checkSet :: Eq a => [a] -> Bool
checkSet [] = True
checkSet (head: remainingSet)   | head `elem` remainingSet = False
                                | otherwise = checkSet remainingSet

-- 	check if set is empty
{- 	to check if input is valid, we ensure that input is a list and set (unique eleemnts)
	The list can be empty (Output is True) or non Empty (Output is False)
-}
--getEmpty assumes input will be a set while empty checks first if input is a set then calls getEmpty
getEmpty :: [a] -> Bool
getEmpty []  = True
getEmpty set = False

empty :: Eq a => [a] -> Bool
empty set = if ((checkSet set) == False) then do
                error "First List is not a set as it has duplicate elements."
            else getEmpty set


-- 	union of two sets
-- 	results in set of elements present in either of sets.
{- 	to check if input is valid, we use Eq a which ensures only lists
	with values belong to Eq class, i.e, can be compared for equality are used as input
	If input are not lists or elements not belong to Eq class it will raise error
-}
--getUnion assumes input will be two sets while union checks first if inputs are sets then calls getUnion

getUnion :: Eq a => [a] -> [a] -> [a]
getUnion firstSet [] = firstSet
getUnion firstSet (head:secondSet)     | head `elem` firstSet = getUnion firstSet secondSet
                                       | otherwise = getUnion (firstSet++[head]) secondSet

union :: Eq a => [a] -> [a] -> [a]
union firstSet secondSet = if ((checkSet firstSet) == False) then do
                             error "First List is not a set as it has duplicate elements."
                           else if((checkSet secondSet) == False) then do
                             error "Second List is not a set as it has duplicate elements."
                           else do
                             getUnion firstSet secondSet

-- 	intersection of two sets
-- 	Results in set of elements present in both the sets.
{- 	to check if input is valid, we use Eq a which ensures only lists
	with values belong to Eq class, i.e, can be compared for equality are used as input
	If input are not lists or elements not belong to Eq class it will raise error
-}
--getIntersection assumes input will be two sets while intersection checks first if inputs are sets then calls getIntersection
getIntersection :: Eq a => [a] -> [a] -> [a]
getIntersection [] secondSet = []
getIntersection (head:firstSet) secondSet  | head `elem` secondSet = (head:(getIntersection firstSet secondSet))
                                           | otherwise = getIntersection firstSet secondSet

intersection :: Eq a => [a] -> [a] -> [a]
intersection firstSet secondSet = if ((checkSet firstSet) == False) then do
                                    error "First List is not a set as it has duplicate elements."
                                  else if((checkSet secondSet) == False) then do
                                    error "Second List is not a set as it has duplicate elements."
                                  else do
                                    getIntersection firstSet secondSet

-- 	subtraction: first set - second set. 
-- 	Results in set of elements in first set not present in second set
{- 	to check if input is valid, we use Eq a which ensures only lists
	with values belong to Eq class, i.e, can be compared for equality are used as input
	If input are not lists (with unique elements) or elements not belong to Eq class it will raise error
-}
--getSubtraction assumes input will be two sets while subtraction checks first if inputs are sets then calls getSubtraction
getSubtraction :: Eq a => [a] -> [a] -> [a]
getSubtraction [] secondSet = []
getSubtraction (head:firstSet) secondSet   | head `elem` secondSet = getSubtraction firstSet secondSet
                                           | otherwise = (head:(getSubtraction firstSet secondSet))

subtraction :: Eq a => [a] -> [a] -> [a]
subtraction firstSet secondSet = if ((checkSet firstSet) == False) then do
                                   error "First List is not a set as it has duplicate elements."
                                 else if((checkSet secondSet) == False) then do
                                   error "Second List is not a set as it has duplicate elements."
                                 else do
                                   getSubtraction firstSet secondSet


-- 	Minkowksi sum (addition)
{- 	to check if input is valid, we use Eq and Num a which ensures only lists
	with values belong to Eq class and Num class, i.e, can be compared for equality 
	and addition can be perfromed on them are used as input
	If input are not lists or elements not belong to Eq class or Num class it will raise error
-}
--getAddition assumes input will be two sets while addition checks first if inputs are sets then calls getAddition
getAddition :: (Eq a, Num a) => [a] -> [a] -> [a]
getAddition [] [] = []
getAddition [] secondSet = []
getAddition firstSet [] = []
getAddition (head:firstSet) secondSet = getUnion ([(head+secondSetElement) | secondSetElement <- secondSet]) (getAddition firstSet secondSet)

addition :: (Eq a, Num a) => [a] -> [a] -> [a]
addition firstSet secondSet = if ((checkSet firstSet) == False) then do
                                error "First List is not a set as it has duplicate elements."
                              else if((checkSet secondSet) == False) then do
                                error "Second List is not a set as it has duplicate elements."
                              else do
                                getAddition firstSet secondSet