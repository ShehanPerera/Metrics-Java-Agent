# Metrics-Java-Agent

<b><h2>STEP 1</h2></b>
Download Wso2-API Manager 3.0.0 and create an api for use.
Follow the link for more details.
https://docs.wso2.com/display/AM300/

<b><h2>STEP 2</h2></b>
Download Metrics-Java-Agent 

 `git clone https://github.com/ShehanPerera/Metrics-Java-Agent.git`

<b><h2>STEP 3</h2></b>
Install the package 

`mvn clean install`

<b><h2>STEP 4</h2></b>
Copy agent-apim-1.0-SNAPSHOT.jar from target to ballerina gateway directory 
(wso2apim-gateway-3.0.0-m19 is the last update I did)

<b><h2>STEP 5</h2></b>
Change the ballerina.sh in wso2apim-gateway-3.0.0-m13/bin as follows 

`...
$JAVA_OPTS \
  	-classpath "$BALLERINA_CLASSPATH" \
   	-javaagent:$BALLERINA_HOME/agent-apim-1.0-SNAPSHOT.jar \
 ...`
  
<b><h2>STEP 6</h2></b>
Run the API Manager service including the ballerina gateway   

<b><h2>STEP 7</h2></b>
You can see the metrics in JMX 
1.run JMX repoter by 
  `jconsole`
2.In ballerina process we can see Metrics for API Manager 
![screenshot from 2018-03-06 15-38-35](https://user-images.githubusercontent.com/29086284/37028949-c5a3a6fc-215b-11e8-8761-43521c9534cc.png)


