# A multi-threaded web-server
A multi-threaded (e.g. file-based) web server with thread-pooling implemented in Java. 

This server only supports GET  operation returning 405 in all the other cases.
## How to build it
Clone the project from github and from that folder execute:

*$mvn clean install*
## How to start the server
*$mvn exec:java -Dexec.mainClass=org.enricogiurin.webserver.Main -Dexec.args="<web-root\> <port\>"*

where:
* <web-root\> - absolute path serving the files
* <port\> - listening port of the web server

Example:
*$mvn exec:java -Dexec.mainClass=org.enricogiurin.webserver.Main -Dexec.args="/tmp 8080"*

## Acknowledgments/Links

* [create-a-simple-http-web-server-in-java](https://medium.com/@ssaurel/create-a-simple-http-web-server-in-java-3fc12b29d5fd) 
* [https://github.com/warchildmd/webserver](https://github.com/warchildmd/webserver) 





