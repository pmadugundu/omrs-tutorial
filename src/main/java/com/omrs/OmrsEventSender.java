package com.omrs;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventProcessor;

/**
 * Sends events to metadata highway
 */
public interface OmrsEventSender {

    /**
     * Sends out a deleted entity event
     * 
     * @see OMRSRepositoryEventProcessor#processDeletedEntityEvent(String, String, String, String, String, EntityDetail)
     * 
     * @param repositoryEventMapperName
     * @param localMetadataCollectionId
     * @param localServerName
     * @param localServerType
     * @param localOrganizationName
     * @param entityDetail
     */
    void sendDeletedEntityEvent(String repositoryEventMapperName, String localMetadataCollectionId,
            String localServerName, String localServerType, String localOrganizationName, EntityDetail entityDetail);

    /**
     * Sends out an updated entity event
     * 
     * @see OMRSRepositoryEventProcessor#processUpdatedEntityEvent(String, String, String, String, String, EntityDetail, EntityDetail)
     * 
     * @param repositoryEventMapperName
     * @param localMetadataCollectionId
     * @param localServerName
     * @param localServerType
     * @param localOrganizationName
     * @param updatedEntity
     */
    void sendUpdatedEntityEvent(String repositoryEventMapperName, String localMetadataCollectionId,
            String localServerName, String localServerType, String localOrganizationName, EntityDetail updatedEntity);

    /**
     * Sends out an entity reclassified event
     * 
     * @see OMRSRepositoryEventProcessor#processReclassifiedEntityEvent(String, String, String, String, String, EntityDetail)
     * 
     * @param repositoryEventMapperName
     * @param localMetadataCollectionId
     * @param localServerName
     * @param localServerType
     * @param localOrganizationName
     * @param entityDetail
     */
    void sendReclassifiedEntityEvent(String repositoryEventMapperName, String localMetadataCollectionId,
            String localServerName, String localServerType, String localOrganizationName, EntityDetail entityDetail);

    /**
     * Sends out a declassified entity event
     * 
     * @see OMRSRepositoryEventProcessor#processDeclassifiedEntityEvent(String, String, String, String, String, EntityDetail)
     * 
     * @param repositoryEventMapperName
     * @param localMetadataCollectionId
     * @param localServerName
     * @param localServerType
     * @param localOrganizationName
     * @param entityDetail
     */
    void sendDeclassifiedEntityEvent(String repositoryEventMapperName, String localMetadataCollectionId,
            String localServerName, String localServerType, String localOrganizationName, EntityDetail entityDetail);

    /**
     * Sends out a new relationship event
     * 
     * @see OMRSRepositoryEventProcessor#processNewRelationshipEvent(String, String, String, String, String, Relationship)
     * 
     * @param repositoryEventMapperName
     * @param localMetadataCollectionId
     * @param localServerName
     * @param localServerType
     * @param localOrganizationName
     * @param rel
     */
    void sendNewRelationshipEvent(String repositoryEventMapperName, String localMetadataCollectionId,
            String localServerName, String localServerType, String localOrganizationName, Relationship rel);

    
    /**
     * Sends out an updated relationship event
     * 
     * @see OMRSRepositoryEventProcessor#processUpdatedRelationshipEvent(String, String, String, String, String, Relationship, Relationship)
     * 
     * @param repositoryEventMapperName
     * @param localMetadataCollectionId
     * @param localServerName
     * @param localServerType
     * @param localOrganizationName
     * @param rel
     */
    void sendUpdatedRelationshipEvent(String repositoryEventMapperName, String localMetadataCollectionId,
            String localServerName, String localServerType, String localOrganizationName, Relationship rel);
    /**
     * Sends out a purged relationship event
     * 
     * @see OMRSRepositoryEventProcessor#processPurgedRelationshipEvent(String, String, String, String, String, String, String, String)
     * 
     * @param repositoryEventMapperName
     * @param localMetadataCollectionId
     * @param localServerName
     * @param localServerType
     * @param localOrganizationName
     * @param typeDefGUID
     * @param typeDefName
     * @param instanceGUID
     */
    void sendPurgedRelationshipEvent(String repositoryEventMapperName, String localMetadataCollectionId,
            String localServerName, String localServerType, String localOrganizationName, String typeDefGUID,
            String typeDefName, String instanceGUID);
    
    /**
     * Sends out a classified entity event
     * 
     * @see OMRSRepositoryEventProcessor#processClassifiedEntityEvent(String, String, String, String, String, EntityDetail)
     * 
     * @param repositoryEventMapperName
     * @param localMetadataCollectionId
     * @param localServerName
     * @param localServerType
     * @param localOrganizationName
     * @param entityDetail
     */
    void sendClassifiedEntityEvent(String repositoryEventMapperName, String localMetadataCollectionId,
            String localServerName, String localServerType, String localOrganizationName, EntityDetail entityDetail);
    
    /**
     * Sends out an new entity event
     * 
     * @see OMRSRepositoryEventProcessor#processNewEntityEvent(String, String, String, String, String, EntityDetail)
     * 
     * @param repositoryEventMapperName
     * @param localMetadataCollectionId
     * @param localServerName
     * @param localServerType
     * @param localOrganizationName
     * @param entityDetail
     */
    void sendNewEntityEvent(String repositoryEventMapperName, String localMetadataCollectionId,
            String localServerName, String localServerType, String localOrganizationName, EntityDetail entityDetail);
    
    
    /**
     * Sends out a purged entity event
     * 
     * @see OMRSRepositoryEventProcessor#processPurgedEntityEvent(String, String, String, String, String, String, String, String)
     * 
     * @param repositoryEventMapperName
     * @param localMetadataCollectionId
     * @param localServerName
     * @param localServerType
     * @param localOrganizationName
     * @param typeDefGUID
     * @param typeDefName
     * @param guid
     */
    void sendPurgedEntityEvent(String repositoryEventMapperName, String localMetadataCollectionId,
            String localServerName, String localServerType, String localOrganizationName, String typeDefGUID,
            String typeDefName, String guid);

    /**
     * Sends out a refresh entity event
     * 
     * @see OMRSRepositoryEventProcessor#processRefreshEntityEvent(String, String, String, String, String, EntityDetail)
     * @param repositoryEventMapperName
     * @param localMetadataCollectionId
     * @param localServerName
     * @param localServerType
     * @param localOrganizationName
     * @param entityDetail
     */
    void sendRefreshEntityEvent(String repositoryEventMapperName, String localMetadataCollectionId,
            String localServerName, String localServerType, String localOrganizationName, EntityDetail entityDetail);
}
