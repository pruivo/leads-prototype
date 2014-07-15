# LEADS prototype #

This prototypes uses Client Listeners to process events in the Infinispan nodes and sends back to the coordinator 
small pieces of data needed.

## Requirements ##

* Java 7 
* Maven 3
* Infinispan 7.0 snapshot

## How to compile it? ##

First you need to download and compile the most recent version of Infinispan:

`git clone https://github.com/infinispan/infinispan.git`

`cd infinispan`

`mvn clean install -DskipTests`

Second, you need to download and compile the prototype code:

`git clone https://github.com/pruivo/leads-prototype.git`

`cd leads-prototype`

`mvn clean package`

## How to run it? ##
 
*Note:* All the scripts prints a help message with the argument `--help`

*Note:* The default values are suitable to try it locally.

Execute the following commands in a separate terminal/shell:

* Start the server

`JAVA_HOME=<path to java 7> ./server/bin/run.sh`

* Start the listener

`JAVA_HOME=<path to java 7> ./client/bin/run.sh --listener`

This program will print all the line number in which the words "beer" and "free" exists in the same line.

* Start the client

`JAVA_HOME=<path to java 7> ./client/bin/run.sh --client`

And follow the console instructions. 
