import Data.IORef
import System.Random
import System.Directory
import System.IO.Unsafe

-- FixtureTiming stores date and time of a fixture.
data FixtureTiming = FixtureTiming{ year :: Integer, month :: Integer, day :: Integer, hour :: Integer,minute :: Integer, time :: Double } deriving(Show,Read)

availableSlots = [
                  FixtureTiming{year=2020,month=12,day=1,hour=9,minute=30,time=9.5 },FixtureTiming{year=2020,month=12,day=1,hour=19,minute=30,time=19.5 },
                  FixtureTiming{year=2020,month=12,day=2,hour=9,minute=30,time=9.5 },FixtureTiming{year=2020,month=12,day=2,hour=19,minute=30,time=19.5 },
                  FixtureTiming{year=2020,month=12,day=3,hour=9,minute=30,time=9.5 },FixtureTiming{year=2020,month=12,day=3,hour=19,minute=30,time=19.5 }
                 ]

-- FixtureObject stores the two competing teams along with their FixtureTiming of curFixtureObject.
data FixtureObject = FixtureObject{firstTeam :: String, secondTeam :: String, fixtureTime :: FixtureTiming} deriving(Show,Read)

-- codes for teams playing the tournament
teamCodes = ["BS", "CM", "CH", "CV", "CS", "DS", "EE", "HU", "MA", "ME", "PH", "ST"]

-- format output string according to the required format
getOutputString :: FixtureObject -> String
getOutputString curFixtureObject = (firstTeam curFixtureObject) ++ " vs " ++ (secondTeam curFixtureObject) ++ " " 
                                 ++ show (day curFixtureTiming) ++ "-" ++ show (month curFixtureTiming) ++ "-" ++ show (year curFixtureTiming)
                                 ++ " " ++ show (hourValue) ++ ":" ++ show (minute curFixtureTiming) ++ " " ++ ampmValue ++ "\n"
                                 where curFixtureTiming = fixtureTime curFixtureObject
                                       ampmValue | ( (hour curFixtureTiming) >= 12) = "PM"
                                                 | otherwise = "AM"
                                       hourValue | (( hour curFixtureTiming) > 12) = (hour curFixtureTiming)-12 
                                                 | otherwise = (hour curFixtureTiming)
--list to store permutation of fixtures
drawsGlobal :: IORef [FixtureObject]
drawsGlobal = unsafePerformIO (newIORef [])

-- assign fixtures based on the random permuted list generated
assignFixtures :: [String] -> [FixtureTiming] -> [FixtureObject]
assignFixtures [] [] = []
assignFixtures (firstTeam:secondTeam:remTeams) (timingHead:remTimings) = 
    FixtureObject{firstTeam = firstTeam,secondTeam = secondTeam,fixtureTime = timingHead} : (assignFixtures remTeams remTimings)
               
-- convert all fixutres to string for printing              
allDrawsToString :: [FixtureObject] -> String
allDrawsToString [] = ""
allDrawsToString (curFixture:remFixtures) = (getOutputString curFixture) ++ (allDrawsToString remFixtures)

--convert specific draw of particular team to string for printing
drawsToString :: [FixtureObject] -> String -> String
drawsToString (curFixture:remFixtures) teamName | ((firstTeam curFixture)==teamName || (secondTeam curFixture)==teamName) = (getOutputString curFixture)
                                                | otherwise = drawsToString remFixtures teamName

-- check if fixture timing >= input time
compareTiming :: FixtureTiming -> FixtureTiming -> Bool
compareTiming timingHead timeInpObject = if ( (yearComp==GT) || (yearComp==EQ && monthComp==GT) || (yearComp==EQ && monthComp==EQ && dayComp==GT) || (yearComp==EQ && monthComp==EQ && dayComp==EQ && (timeComp==GT || timeComp==EQ) ) )
                                     then True
                                     else False
                                     where  yearComp = compare (year timingHead) (year timeInpObject)
                                            monthComp = compare (month timingHead) (month timeInpObject)
                                            dayComp = compare (day timingHead) (day timeInpObject)
                                            timeComp = compare (time timingHead) (time timeInpObject)

-- traverse all fixture timings to get fixture just greater or equal to input time
fixtureNextToTimeInp :: [FixtureObject] -> FixtureTiming -> FixtureObject
fixtureNextToTimeInp [] timeSlot = error "No fixture is scheduled after the input date and time."
fixtureNextToTimeInp (curFixtureObject:remainingFixtures) timeSlot | compareTiming (fixtureTime curFixtureObject) timeSlot = curFixtureObject
                                                                  | otherwise = fixtureNextToTimeInp remainingFixtures timeSlot

-- find fixture with time greater than or equal to inputTime
findNextFixture :: FixtureTiming -> IO()    
findNextFixture fixtureTime = do 
    fixturesAll <- readCurDraws
    if (null fixturesAll) then do
        error "No Fixture created yet. Use fixture \"all\" to create fixtures."
    else do
        let curFixtureObject = fixtureNextToTimeInp fixturesAll fixtureTime
        putStr (getOutputString curFixtureObject)

-- get the current random draws
readCurDraws :: IO [FixtureObject]
readCurDraws = readIORef drawsGlobal

-- set the random draws generated
writeCurDraws :: [FixtureObject] -> IO ()
writeCurDraws fixtures = writeIORef drawsGlobal fixtures

-- based on random seed, permute team codes to generate random fixtures
permuteTeamCodes :: [a] -> StdGen -> [a]
permuteTeamCodes [] _   = []
permuteTeamCodes list curSeed = curFirstVal : (permuteTeamCodes (take ranValue list ++ drop (ranValue+1) list) nextSeed )
                                where (ranValue,nextSeed) = randomR (0, (length list)-1) curSeed
                                      curFirstVal = list !! ranValue

-- store the permuted fixtures in drawsGlobal
createRandomList :: IO()
createRandomList = do
                curSeed <- newStdGen
                let permuteTeams = permuteTeamCodes teamCodes curSeed
                let fixturesList = assignFixtures permuteTeams availableSlots
                writeCurDraws fixturesList

-- check if input is correct and if yes, call findNextFixture to get next fixture
nextMatch :: Integer -> Double -> IO()
nextMatch dateInp timeInp
    | (floor timeInp)<0 || (floor timeInp)>23 = error "Hour must be from 0 to 23"
    | dateInp<1 || dateInp>31 = error "The date must be from 1 to 31"
    | otherwise = findNextFixture FixtureTiming{ year=2020,month=12,day=dateInp,
      hour=(floor timeInp),minute=(ceiling( (timeInp - ( fromIntegral((floor timeInp)) ) ) * 60 )), 
      time=timeInp}


-- fixture "all" generates new random fixtures and prints them
-- fixture "DS"  (or any other team code) prints fixture for the particular team code.
-- fixture "current" prints the latest generated fixtures without regenrating them
fixture :: String -> IO()    
fixture "all" = do 
    createRandomList
    fixturesAll <- readCurDraws
    putStr (allDrawsToString fixturesAll)

fixture "current" = do 
    fixturesAll <- readCurDraws
    if (null fixturesAll) then do
        error "No Fixture created yet. Use fixture \"all\" to create fixtures."
    else do
        putStr (allDrawsToString fixturesAll)

fixture teamCode = do 
    fixturesAll <- readCurDraws
    if (null fixturesAll) then do
        error "No Fixture created yet. Use fixture \"all\" to create fixtures."
    else if (teamCode `elem` teamCodes) then do
        putStr (drawsToString fixturesAll teamCode)
    else
        error "The input team code is not a valid code."