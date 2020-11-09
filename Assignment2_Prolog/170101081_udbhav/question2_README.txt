This is the readme file for Question1.

On linux, SWI prolog should be installed.
Use command:
prolog

to open the prolog interface in terminal.

Then type:
['question2.pl'].
to load question 2.

Alternatively without using above two commands from terminal itself you can run code using:
swipl -s question2.pl


Sample queries:

Make sure to enter stop names in single inverted commas and spelling should match as mentioned in the knowledge base of code.

The below queires cover all cases of waiting time, taking bus next day, different paths for optimal cost, time, distance and multiple bus changes.

route('Amingaon', 'Ambari').			Output: Optimal Distance:
												Amingaon,123 -> Paltanbazaar,931 -> Chandmari,672 -> Ambari,xxx
												Distance= 180, Time= 48.6, Cost= 315

												Optimal Time:
												Amingaon,756 -> Panbazaar,931 -> Chandmari,672 -> Ambari,xxx
												Distance= 390, Time= 0.9, Cost= 520

												Optimal Cost:
												Amingaon,123 -> Jalukbari,756 -> Paltanbazaar,931 -> Chandmari,864 -> Sundarpur,864 -> Ambari,xxx
												Distance= 615, Time= 55, Cost= 85
												true.

route('Amingaon','Chandmari').			Output: Optimal Distance:
												Amingaon,123 -> Paltanbazaar,931 -> Chandmari,xxx
												Distance= 110, Time= 26, Cost= 115

												Optimal Time:
												Amingaon,756 -> Panbazaar,931 -> Chandmari,xxx
												Distance= 320, Time= 0.5, Cost= 320

												Optimal Cost:
												Amingaon,123 -> Jalukbari,756 -> Paltanbazaar,931 -> Chandmari,xxx
												Distance= 265, Time= 28, Cost= 30
												true.


route('Jalukbari', 'Malingaon').			Output: Path Doesn't Exist.
													true.

route('Paltanbazaar', 'Ambari').			Output: Optimal Distance:
													Paltanbazaar,931 -> Chandmari,672 -> Ambari,xxx
													Distance= 130, Time= 25.1, Cost= 215

													Optimal Time:
													Paltanbazaar,756 -> Chandmari,672 -> Ambari,xxx
													Distance= 180, Time= 1.1, Cost= 235

													Optimal Cost:
													Paltanbazaar,931 -> Chandmari,864 -> Sundarpur,864 -> Ambari,xxx
													Distance= 410, Time= 29.5, Cost= 70
													true.


============
To install SWI prolog use the following commands:

sudo apt-add-repository ppa:swi-prolog/stable
sudo apt-get update
sudo apt-get install swi-prolog



