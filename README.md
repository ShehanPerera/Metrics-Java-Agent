# Metrics-Java-Agent

<b><h2>STEP 1</h2></b>
Download API Manager 3.0.0 and create an api for use

<b><h2>STEP 2</h2></b>
Download Metrics-Java-Agent

<b><h2>STEP 3</h2></b>
Install the package 
`mvn clean install`

<b><h2>STEP 4</h2></b>
Copy agent-apim-1.0-SNAPSHOT.jar from target to ballerina gateway directory 
(wso2apim-gateway-3.0.0-m13)

<b><h2>STEP 5</h2></b>
Change the ballerina.sh in wso2apim-gateway-3.0.0-m13/bin as follows 

`...
$JAVA_OPTS \
  	-classpath "$BALLERINA_CLASSPATH" \
   	-javaagent:$BALLERINA_HOME/agent-apim-1.0-SNAPSHOT.jar \
 ...`
  
<b><h2>STEP 6</h2></b>
Run the service 

<b><h2>STEP 7</h2></b>
You can see the metrics in JMX 

