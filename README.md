# indexingLogs
A Java project to read the logs from a log file and index them to Elasticsearch.

Java maven project to read the log file. This file contains 4 lines. Each line represents 1 log. 
The format of the log line is as follows:

<date> <timestamp> <logLevel> <className>:<lineNumber> - <message>
 
The program is extracting these fields from each log line as key-value pairs. 
For example, consider the following line :
 
2018-05-02 16:01:45 INFO  Student:12 - This is info message
 
Expected output :

date : 2018-05-02

timestamp : 16:01:45

logLevel : INFO

className : Student

lineNumber : 12

message : This is info message
 

it is storing the key-value pairs extracted from the log lines in Elasticsearch. During indexing, each log line is treated as one ‘document’ in Elasticsearch.
NOTE: Download and install Elasticsearch
