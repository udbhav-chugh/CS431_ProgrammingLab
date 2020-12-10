-- get area occupied by a pair of bedroom and hall
getInitialArea :: (Integer,Integer) ->(Integer,Integer) ->[Integer]->Integer
getInitialArea bedroomDim hallDim countList = ((fst(bedroomDim) * snd(bedroomDim) * (countList!!0)) + (fst(hallDim) * snd(hallDim) * (countList!!1)))

-- get area occupied when another room is added to the current configuration
getArea :: Integer -> (Integer,Integer) -> Integer -> Integer
getArea areaCur roomDim roomCount = (fst(roomDim) * snd(roomDim) * roomCount) + areaCur

-- check if total area of the rooms is less than the required area
checkArea :: Integer -> Integer -> Bool
checkArea areaCur totalSpace = (areaCur <= totalSpace)

-- check if both dimensions of dim1 are less than dimensions of dim2
-- Used for maintaing size of kitchen smaller than hall and bedroom and size of bathroom smaller than size of kitchen 
checkDimension :: (Integer,Integer) -> (Integer,Integer) -> Bool
checkDimension dim1 dim2 = (fst(dim1) <= fst(dim2) && snd(dim1) <= snd(dim2))

-- among all possible configurations, get area which occupies maximum space
getMaxSpaceOccupied :: [((Integer,Integer), (Integer,Integer),(Integer,Integer),(Integer,Integer),(Integer,Integer),(Integer,Integer),Integer)] -> Integer
getMaxSpaceOccupied [] = 0
getMaxSpaceOccupied ((bed, hall, kit, bath, bal, gar, areaCur):remainList) = maximum [areaCur:: Integer, getMaxSpaceOccupied(remainList)]

-- print final string in desired format
getoutputString :: String -> Integer -> (Integer,Integer) ->String
getoutputString roomType roomCount roomDim = roomType ++ ": " ++ show (roomCount) ++ " (" ++ show (fst(roomDim)) ++ " X " ++ show(snd(roomDim))++ ")\n"

-- the design function which takes a tuple input and displays result on terminal
-- Example: design (1000,3,2)
design :: (Integer,Integer,Integer) -> IO()
design (totalArea, bedroomCount, hallCount) = do

  -- get all possible dimensions for bedrooms (10 X 10) to (15 X 15)
  let bedroomDimPossible = [(dimension1, dimension2) | dimension1<- [10..15],dimension2<-[10..15]]

  -- get all possible dimensions for halls (15 X 10) to (20 X 15)
  let hallDimPossible = [(dimension1, dimension2) | dimension1<- [15..20],dimension2<-[10..15]]

  -- get all possible dimensions for kitchens (7 X 5) to (15 X 13)
  let kitchenDimPossible = [(dimension1, dimension2) | dimension1<- [7..15],dimension2<-[5..13]]
  let kitchenCount = ceiling(fromIntegral bedroomCount/3)

  -- get all possible dimensions for bathrooms (4 X 5) to (8 X 9)
  let bathroomDimPossible = [(dimension1, dimension2) | dimension1<- [4..8],dimension2<-[5..9]]
  let bathroomCount = bedroomCount + 1

  -- get all possible dimensions for balcony (5 X 5) to (10 X 10)
  let balconyDimPossible = [(dimension1, dimension2) | dimension1<- [5..10],dimension2<-[5..10]]
  let balconyCount = 1

  -- get all possible dimensions for garden (10 X 10) to (20 X 20)
  let gardenDimPossible = [(dimension1, dimension2) | dimension1<- [10..20],dimension2<-[10..20]]
  let gardenCount = 1

  -- list to store required rooms for each room type
  let countList = [bedroomCount,hallCount,kitchenCount,bathroomCount,balconyCount,gardenCount]

  -- get all possible configurations with bedrooms and halls
  let bedHall = [(firstT,secondT, (getInitialArea firstT secondT countList) ) | firstT <- bedroomDimPossible, secondT <- hallDimPossible]

  -- keep only those configurations of bedrooms and halls which can fit in the total space
  let bedHallPossible = filter (\(bed,hall,area) -> (checkArea area totalArea)) bedHall

  -- get all possible configurations by adding kitchen to existing possible configurations
  let bedHallKit = [(firstT,secondT,thirdT, (getArea areaCur thirdT kitchenCount)) | (firstT,secondT, areaCur) <- bedHallPossible, thirdT <- kitchenDimPossible]
  
  -- keep only those configurations st bedrooms, halls,kitchen fit in total space
  -- also the dimensions of kitchen is less than both bedrooms and halls
  let bedHallKitPossible = filter (\(bed,hall,kit,area) -> (checkArea area totalArea)
                               && (checkDimension kit bed) && (checkDimension kit hall)) bedHallKit   

  -- get all possible configurations by adding bathrooms to existing possible configurations
  let bedHallKitBath = [(firstT, secondT, thirdT, fourthT, (getArea areaCur fourthT bathroomCount)) | (firstT,secondT,thirdT, areaCur) <- bedHallKitPossible, fourthT <- bathroomDimPossible]
  
  -- keep only those configurations st bedrooms, halls,kitchen,bathrooms fit in total space
  -- also the dimensions of bathrooms is less than that of kitchens
  let bedHallKitBathPossible = uniqueAreaBedHallKitBath (filter (\(bed,hall,kit,bath,area) ->
                               (checkArea area totalArea) && (checkDimension bath kit) ) bedHallKitBath) [] 

  -- get all possible configurations by adding balcony to existing possible configurations
  let bedHallKitBathBal = [(firstT, secondT, thirdT, fourthT, fifthT, (getArea areaCur fifthT balconyCount) ) | (firstT, secondT, thirdT, fourthT, areaCur) <- bedHallKitBathPossible, fifthT <- balconyDimPossible]
  
  -- keep only those configurations st bedrooms, halls,kitchen,bathrooms and balcony fit in total space
  let bedHallKitBathBalPossible = uniqueAreaBedHallKitBathBal (filter (\(bed,hall,kit,bath,bal,area) ->
                              (checkArea area totalArea)) bedHallKitBathBal) []    

  -- get all possible configurations by adding garden to existing possible configurations
  let bedHallKitBathBalGar = [(firstT, secondT, thirdT, fourthT, fifthT, sixthT, (getArea areaCur sixthT gardenCount) ) | (firstT, secondT, thirdT, fourthT, fifthT, areaCur) <- bedHallKitBathBalPossible, sixthT <- gardenDimPossible]
 
  -- keep only those configurations st bedrooms, halls,kitchen,bathrooms, balcony and garden fit in total space
  let bedHallKitBathBalGarPossible = uniqueAreaBedHallKitBathBalGar (filter (\(bed,hall,kit,bath,bal,gar,area) -> 
                               (checkArea area totalArea)) bedHallKitBathBalGar) []


  -- get max area occupied among configurations that satsify all above criterias
  let finalAreaOccupied = getMaxSpaceOccupied bedHallKitBathBalGarPossible
  -- get the final configuration (if any)
  let finalConfigs = filter (\(bed,hall,kit,bath,bal,gar, area) -> area == finalAreaOccupied) bedHallKitBathBalGarPossible
  let unoccupiedSpace = totalArea-finalAreaOccupied

  -- a possible configuration is found based on given constraints
  if (length finalConfigs > 0) then do
    let (bed,hall,kit,bath,bal,gar,areaCur) = finalConfigs !! 0
    putStr (getoutputString "Bedroom" bedroomCount bed)
    putStr (getoutputString "Hall" hallCount hall)
    putStr (getoutputString "Kitchen" kitchenCount kit)
    putStr (getoutputString "Bathroom" bathroomCount bath)
    putStr (getoutputString "Garden" gardenCount gar)
    putStr (getoutputString "Balcony" balconyCount bal)
    putStr ("Unused Space: " ++ show (unoccupiedSpace) ++ "\n")
  -- no possible configuration based on given constraints is found
  else
    putStr ("No design possible for the given constraints\n")


-- keep only one configuration of bed hall kitchen bathroom for each distinct area
-- this reduces search space as one configuration per area is enough to explore all possible configurations
uniqueAreaBedHallKitBath :: [((Integer,Integer),(Integer,Integer),(Integer,Integer),(Integer,Integer),Integer)] -> [Integer] -> [((Integer,Integer),(Integer,Integer),(Integer,Integer),(Integer,Integer),Integer)]
uniqueAreaBedHallKitBath [] _= []
uniqueAreaBedHallKitBath ((bed, hall, kit, bath, areaCur):remainList) areaVisited = do
  if ( (areaCur `elem` areaVisited) == True) then
    uniqueAreaBedHallKitBath remainList areaVisited
  else
    (bed, hall, kit, bath, areaCur) : uniqueAreaBedHallKitBath remainList (areaCur:areaVisited)


-- keep only one configuration of bed hall kitchen bathroom and balcony for each distinct area
uniqueAreaBedHallKitBathBal :: [((Integer,Integer),(Integer,Integer),(Integer,Integer),(Integer,Integer),(Integer,Integer),Integer)] -> [Integer] -> [((Integer,Integer),(Integer,Integer),(Integer,Integer),(Integer,Integer),(Integer,Integer), Integer)]
uniqueAreaBedHallKitBathBal [] _= []
uniqueAreaBedHallKitBathBal ((bed, hall, kit, bath, bal, areaCur):remainList) areaVisited = do
  if ( (areaCur `elem` areaVisited) == True) then
    uniqueAreaBedHallKitBathBal remainList areaVisited
  else
    (bed, hall, kit, bath, bal, areaCur) : uniqueAreaBedHallKitBathBal remainList (areaCur:areaVisited)

-- keep only one configuration of bed hall kitchen bathroom balcony and garden for each distinct area
uniqueAreaBedHallKitBathBalGar :: [((Integer,Integer), (Integer,Integer),(Integer,Integer),(Integer,Integer),(Integer,Integer),(Integer,Integer),Integer)] -> [Integer] -> [((Integer,Integer),(Integer,Integer),(Integer,Integer),(Integer,Integer),(Integer,Integer), (Integer,Integer), Integer)]
uniqueAreaBedHallKitBathBalGar [] _= []
uniqueAreaBedHallKitBathBalGar ((bed, hall, kit, bath, bal, gar, areaCur):remainList) areaVisited = do
  if ( (areaCur `elem` areaVisited) == True) then
    uniqueAreaBedHallKitBathBalGar remainList areaVisited
  else
    (bed, hall, kit, bath, bal, gar, areaCur) : uniqueAreaBedHallKitBathBalGar remainList (areaCur:areaVisited)
