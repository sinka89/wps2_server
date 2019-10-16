**Lightweight WPS 2 server Implementation with ehcache**

Testing purpose only at the moment...

wps2_config.properties -> define server endpoint / nr of threads to use basic props / default props etc

Only supports POST with xml in body

As default the server_url is localhost and port 9001 * to test use non ssl endpoint and post xml like in the examples provided in type_of_requests (ex: http://localhost:9001/wps)

For https generate a keystore like the one /wps2_server.pfx (ex: keytool -genkey -alias wps2_server -keystore wps2_server.pfx -storetype PKCS12 -keyalg RSA -storepass password -validity 730 -keysize 2048), change prop HTTPS_SERVER_JKS_TOKEN_PASS with the password provided and also set IS_SERVER_HTTPS prop to true, now the server will accept only https connections.

**Build & Execution**
To build mvn clean install inside folder then use provided _deploy_server.bat to start.

**What works**:
EhCache is used to manage and store the Results (mandatory config otherwise the server will not start -> ehcache.xml)

 - GetCapabilities type of requests (ex: type_of_requests/executeRequest.xml)
 - DescribeProcess type of requests (ex: type_of_requests/describeProcess.xml)
 - ExecuteProcess type of requests (ex: type_of_requests/executeRequestAsync.xml, type_of_requests/executeRequestSync.xml)
 - GetStatus type of requests (ex: type_of_requests/getStatus.xml)
 - GetResult type of requests (ex: type_of_requests/getResult.xml)
 - Dismiss type of requests (ex: type_of_requests/dismissProcess.xml)
 
Note the difference from sync / async is only the mode attribute. If using async you can query for the status / result of the process using the returned uuid, with the GetStatus.xml or GetResult.xml request. Also on the async requests the option for Dismiss is implemented which terminates the process and silently logs the possible errors (ex: ThreadInterruptEx...)

        * Process *
        The process definition location is define statically in the WpsProcessReflectionUtil and loaded / parsed based on annotations
        Will autoload on startup in memmory the parsed processes found -> if parsing error detected the server will exclude the Process that produced the error
        All processes must implement ProcessImplementation interface and if the process has external resources that take a long time to load
        in the executing stage they must be declared in the listOfCancelableResources and implemented (ex provided for InputStream / HttpURLConnection)
            The method will trigger when a dismiss request is received for a specific started task.
        
        Supports multiple inputs and only one output at the moment
  
  
* xmlExecute: Process modes (JobControlOptions) supported: "sync" "async" **Note that if the process is not specifically defined to support sync in the annotation the async default will be used (executeRequest xml attribute <mode>)
* xmlExecute: Process responses supported: "document" "raw" **The document response type will try to wrap in a xml the raw returns as is to the client (executeRequest xml attribute <response>)

`The executeProcess request will dictate the response type and cannot be changed after execution is completed... the process must be rerun`

* will do: implement test case for every operation.

**What is not working**:
* Multi language support is not yet implemented
* RawData processing is not finished -> supports only one file result
* ComplexData is not implemented yet
