Question Answering System:
------------------------------


Description:
The QA system takes as input a question  along with a set of relevant documents and finally outputs the top ten ranked guesses for the answer.


Environment:

The program is written in a JAVA environment. The dependencies for the project are:

Eclipse  IDE for JAVA Developers.
Version: Luna Service Release 1a (4.4.1)
JDK Version:JDK 8 Update 31.pkg

External JAR:

All these JAR files needs to be included in External Lib and then added to the Build Path.
stanford-corenlp-3.5.1.jar 	
lucene-queryparser-5.1.0.jar
lucene-analyzers-common-5.1.0.jar
lucene-core-5.1.0.jar
edu.mit.jwi_2.3.3.jar

Include the WordNet-3.0 at root folder
WordNet-3.0.tar.gz



INSTRUCTIONS TO RUN THE PROGRAM:

. Import the program into the Eclipse Environment.
. Add all the external Libraries mentioned above.
. Include question.txt in the folder qadata/dev.
. Ready to Run.


Output:
Writes the top 10 ranked guesses for the answer into Answer.txt