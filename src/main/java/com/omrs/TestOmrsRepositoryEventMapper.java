package com.omrs;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventMapperBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventMapperConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventProcessor;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

/**
 * TestOmrsRepositoryEventMapper provides an implementation of a repository event mapper.
 */
public class TestOmrsRepositoryEventMapper extends OMRSRepositoryEventMapperBase {

	private static TestOmrsRepositoryEventMapper instance = null;
	
    private TestOmrsMetadataCollection metadataCollection = null;   
    private OMRSRepositoryEventProcessor eventProcessor = null;
    
    /**
     * Default constructor
     */
    public TestOmrsRepositoryEventMapper() throws RepositoryErrorException {
    }
    
    public static TestOmrsRepositoryEventMapper getInstance() {
    	return instance;
    }
    
    /**
     * Indicates that the BgOmrsRespoitoryEventMapper connector is completely 
     * configured and can begin processing.
     *
     * @throws ConnectorCheckedException
     */
    @Override
    @SuppressWarnings("deprecation")
    public void start() throws ConnectorCheckedException  {

        super.start();

        // Get and verify the metadataCollection...
        boolean metadataCollectionOK = false;
        try {
            metadataCollection = (TestOmrsMetadataCollection) this.repositoryConnector.getMetadataCollection();
	        if (metadataCollection != null) {
	            // Check that the metadataCollection is responding...
                String id = metadataCollection.getMetadataCollectionId(null);
                if (id.equals(localMetadataCollectionId)) {
                    metadataCollectionOK = true;
                }
	        }
        }
        catch (RepositoryErrorException e) {
        	e.printStackTrace();
        }
        if (!metadataCollectionOK) {
            throw new ConnectorCheckedException(400, this.getClass().getName(), "start",
                        "Could not start the " + localMetadataCollectionId + " for the WKC repository",
                        "The connector is unable to proceed", "Check the system logs and diagnose or report the problem.");
        }
        
        instance = this;
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException
     */
    public void disconnect() throws ConnectorCheckedException {
        super.disconnect();
    }

    /**
     * Wrap {@link OMRSRepositoryEventProcessor} set by egeria with {@link OmrsEventSender} implementation.
     * 
     * @see OMRSRepositoryEventMapperConnector#setRepositoryEventProcessor(OMRSRepositoryEventProcessor)
     */
    @Override
    public void setRepositoryEventProcessor(OMRSRepositoryEventProcessor repositoryEventProcessor) {
        super.setRepositoryEventProcessor(repositoryEventProcessor);
        eventProcessor = repositoryEventProcessor;
    }

    public OMRSRepositoryConnector getConnector() {
        return repositoryConnector;
    }
    
    public void processNewEntity(EntityDetail entityDetail) {
        System.out.println("processNewEntity EntityDetail is " + entityDetail.toString());
        eventProcessor.processNewEntityEvent(repositoryEventMapperName, localMetadataCollectionId, localServerName,
                localServerType, localOrganizationName, entityDetail);
    }

    public void processPurgedEntity(EntityDetail entityDetail) {
        System.out.println("processPurgedEntity EntityDetail is " + entityDetail.toString());
        eventProcessor.processPurgedEntityEvent(repositoryEventMapperName, localMetadataCollectionId, localServerName,
                localServerType, localOrganizationName, entityDetail.getType().getTypeDefGUID(),
                entityDetail.getType().getTypeDefName(), entityDetail.getGUID());
    }

    public void processNewRelationship(Relationship rel) {
        System.out.println("sendNewRelationshipEvent Relationship = " + rel.getType().getTypeDefName());
        eventProcessor.processNewRelationshipEvent(repositoryEventMapperName, localMetadataCollectionId, localServerName,
                localServerType, localOrganizationName, rel);
    }
    
    public void processPurgeRelationships(Relationship rel) {
        System.out.println("processPurgeRelationship Relationship = " + rel.getType().getTypeDefName());    
        String typeDefGUID = rel.getType().getTypeDefGUID();
        String typeDefName = rel.getType().getTypeDefName();
        String instanceGUID = rel.getGUID();
        eventProcessor.processPurgedRelationshipEvent(repositoryEventMapperName, localMetadataCollectionId, localServerName,
                localServerType, localOrganizationName, typeDefGUID, typeDefName, instanceGUID);
    }
    
    public TestOmrsMetadataCollection getMetadataCollection() {
    	return metadataCollection;
    }
}
