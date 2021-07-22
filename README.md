# Tutorial to exchange metadata using Open Source ODPI Egeria Open Metadata Repository Services cohort

Refer to https://medium.com/p/9c2979e0145d for the details.

## Prerequisites:
1. Zookeeper and Kafka needs to be installed.
2. Kafka topic needs to be created for the cohort with a name in the folloing format:
`egeria.omag.openmetadata.repositoryservices.cohort.<topic_name>.OMRSTopic`
3. Two web servers need to be installed, one for sending metadata and the other for receiving metadat. The code has been validated with WebSphere Liberty v21.0.0.6.
4. You might also want to install an IDE, like Eclipse to load the code and run the web app in the we server.

The code has been validated by installing the above software in 3 different servers as follows:
1. Server1 with Zookeeper and Kafka
2. Server2 with a web server for sending metadata
3. Server3 with a web server for receiving metadata

## Steps to run the sample 
Execute the following steps on both the web servers to register the server to the OMRS cohort:
1. Create OMRS cohort configuration:
- http://XXXX:XXXX/TestOmrsServer_gradle/abc/open-metadata/cohorts/<cohort_name>
2. Activate OMRS:
- http://XXXX:XXXX/TestOmrsServer_gradle/abc/open-metadata/instance

Send a term from the source server:
- http://localhost:9080/TestOmrsServer_gradle/abc/open-metadata/crud?action=new_term

