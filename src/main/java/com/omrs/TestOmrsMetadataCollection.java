/*
 * IBM Confidential
 * OCO Source Materials
 * Copyright IBM Corp. 2018, 2021
 */
package com.omrs;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityConflictException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.HomeEntityException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.HomeRelationshipException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidEntityException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidRelationshipException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidTypeDefException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipConflictException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RelationshipNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefConflictException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

import com.google.gson.Gson;

/**
 * The TestOmrsMetadataCollection represents a local metadata repository.
 * Requests to this metadata collection are mapped to requests to the local
 * repository.
 */
public class TestOmrsMetadataCollection extends OMRSMetadataCollectionBase {

    TestOmrsMetadataCollection(TestOmrsRepositoryConnector parentConnector, String repositoryName,
        OMRSRepositoryHelper repositoryHelper, OMRSRepositoryValidator repositoryValidator,
        String metadataCollectionId) {

        super(parentConnector, repositoryName, repositoryHelper, repositoryValidator,
            metadataCollectionId);
    }
    
    @Override
    public boolean verifyAttributeTypeDef(String userId, AttributeTypeDef typeDef)
        throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException,
        TypeDefConflictException, InvalidTypeDefException, UserNotAuthorizedException {
        return true;
    }
    
    @Override
    public boolean verifyTypeDef(String userId, TypeDef typeDef) throws InvalidParameterException,
        RepositoryErrorException, TypeDefNotSupportedException, TypeDefConflictException,
        InvalidTypeDefException, UserNotAuthorizedException {
        return true;
    }
    
    @Override
    public EntityDetail isEntityKnown(String userId, String guid)
        throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException {
    	
    	return null;
    }

    @Override
    public void saveEntityReferenceCopy(String userId, EntityDetail entity)
        throws InvalidParameterException, RepositoryErrorException, TypeErrorException,
        PropertyErrorException, HomeEntityException, EntityConflictException,
        InvalidEntityException, UserNotAuthorizedException {

        Gson gson = new Gson();
        System.out.println("userId: " + userId + ", entity: " + gson.toJson(entity));
    }
    
    @Override
    public void purgeEntityReferenceCopy(String serverName, String entityGUID, String typeDefGUID,
        String typeDefName, String homeMetadataCollectionId) throws InvalidParameterException, RepositoryErrorException,
        EntityNotKnownException, HomeEntityException, FunctionNotSupportedException, UserNotAuthorizedException {
        
        System.out.println("deleting the entity: " + entityGUID);
    }

    @Override
    public void saveRelationshipReferenceCopy(String userId, Relationship relationship)
        throws InvalidParameterException, RepositoryErrorException, TypeErrorException, EntityNotKnownException,
        PropertyErrorException, HomeRelationshipException, RelationshipConflictException, InvalidRelationshipException,
        FunctionNotSupportedException, UserNotAuthorizedException {
        
        Gson gson = new Gson();
        System.out.println("userId: " + userId + ", relationship: " + gson.toJson(relationship));
    }
    
    @Override
    public  void purgeRelationshipReferenceCopy(String userId, String relationshipGUID, String typeDefGUID,
            String typeDefName, String   homeMetadataCollectionId) 
            throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException,
            HomeRelationshipException, FunctionNotSupportedException, UserNotAuthorizedException
    {
        System.out.println("deleting the relationship: " + relationshipGUID);

    }
    
    public TestOmrsRepositoryConnector getParentConnector() {
        return (TestOmrsRepositoryConnector)parentConnector;
    }
}
