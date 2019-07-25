# multi-threaded_web-server
A multi-threaded (e.g. file-based) web server with thread-pooling implemented in Java. 

This server only supports GET  operation returning 405 in all the other cases.
## How to build it
Clone the project from github and from that folder run:
*mvn clean install*
## How to start the server
*mvn exec:java -Dexec.mainClass=org.enricogiurin.webserver.Main -Dexec.args="<web-root> <port\>"*
where:
* <web-root\> - absolute path serving the files
* <port\> - listening port of the web server


