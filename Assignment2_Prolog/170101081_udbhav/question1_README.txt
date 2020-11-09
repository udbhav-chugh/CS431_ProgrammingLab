This is the readme file for Question1.

Both the parts of question 1 (uncle and half sister) are done in the same file.

Run the file question1.pl in SWI prolog.

On linux, SWI prolog should be installed.
Use command:
prolog

to open the prolog interface in terminal.

Then type:
['question1.pl'].
to load question 1.

Alternatively without using above two commands from terminal itself you can run code using:
swipl -s question1.pl



In queries where variable like A or B are passed, keep pressing semi colon till values of A and B are displayed. When all values of A are displayed false will be shown.

For queries where true is displayed, press full stop to end query result and go to next query.

Sample queries to execute (Ensure spelling matches as that mentioned in the knowledge base of question1.pl):

uncle(kattappa,avantika).			Output:	true  (Press full stop)

uncle(avantika,manisha).			Output: false.

uncle(kattappa,A).					Output: A = avantika (Press semi colon)
											false.

uncle(jatin, avantika).				Output: false.

uncle(A, B).						Output: A = kattappa,
											B = avantika (Press semi colon)
											false.


halfsister(avantika, shivkami).		Output:	true  (Press full stop)

halfsister(A, shivkami).			Output: A = avantika ;
											false.

halfsister(A, B).					Output: A = avantika,
									B = shivkami (Press semi colon)
									A = shivkami,
									B = avantika (Press semi colon)
									false.


============
To install SWI prolog use the following commands:

sudo apt-add-repository ppa:swi-prolog/stable
sudo apt-get update
sudo apt-get install swi-prolog
