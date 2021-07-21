/*
 * IBM Confidential
 * OCO Source Materials
 * Copyright IBM Corp. 2018, 2021
 */
package com.omrs;

import java.util.List;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollectionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
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

import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
//import com.ibm.wdp.bg.api.GlobalConfiguration;
//import com.ibm.wdp.bg.model.util.BgGsonBuilder;
//import com.ibm.wdp.bg.model.util.BgGsonBuilder.CommunicationMode;
//import com.ibm.wdp.bg.openmetadata.adapters.eventmapper.BgOmrsRepositoryEventMapper;

/**
 * The TestOmrsMetadataCollection represents a local metadata repository.
 * Requests to this metadata collection are mapped to requests to the local
 * repository. The metadata collection reads typedefs from WKC BG and will attempt
 * to convert them to OM typedefs - and vice versa. During these conversions it
 * will check the validity of the content of the type defs as far as possible,
 * giving up on a typedef that it cannot verify or convert. This implementation
 * uses lower level classes in WKC BG.
 */
public class TestOmrsMetadataCollection extends OMRSMetadataCollectionBase {

    /*
     * The typeDefsCache is a long-lived TypeDefsByCategory used to remember the
     * TypeDefs from WKC BG (that can be modeled in OM). It is allocated at the
     * start of loadTypeDefs so that it can be refreshed by a fresh call to
     * loadTypeDefs. The typeDefsCache is retained across API calls; unlike
     * typeDefsForAPI which is not.
     */

    /**
     * typeDefsForAPI is a transient TDBC object - it is reallocated at the
     * start of each external API call. It's purpose is to marshall the results
     * for the current API (only), which can then be extracted or turned into a
     * TypeDefGallery (TDG), depending on return type of the API.
     */
//    private TypeDefsByCategory typeDefsForAPI = null;
    /**
     * entityTimeKeeper is being used to find the elapsed time of an entity 
     * to persist. As the clock is being used for specific purpose it is a
     * static at this point of time, can be changed to instance level if the
     * usage progresses.
     */
    private static Stopwatch stopWatch = Stopwatch.createUnstarted();

    // package private
    TestOmrsMetadataCollection(TestOmrsRepositoryConnector parentConnector, String repositoryName,
        OMRSRepositoryHelper repositoryHelper, OMRSRepositoryValidator repositoryValidator,
        String metadataCollectionId) {

        super(parentConnector, repositoryName, repositoryHelper, repositoryValidator,
            metadataCollectionId);
    }
    
    /**
     * setEventMapper  
     * When the eventmapper starts, it calls this method. 
     * 
     */
//    public void setEventMapper(BgOmrsRepositoryEventMapper eventMapper) {
//    }

    @Override
    public boolean verifyAttributeTypeDef(String userId, AttributeTypeDef typeDef)
        throws InvalidParameterException, RepositoryErrorException, TypeDefNotSupportedException,
        TypeDefConflictException, InvalidTypeDefException, UserNotAuthorizedException {
        // TODO: This method still needs to be implemented.  Returning false for now i.e. typedef is not added in local repository
        // so that typedefs from OMRS get loaded in OMRSContentManger::knownTypes.  This Hashmap is checked
        // for typedef validation.  It is needed when sending events to OMRS for instance.
        return true;
    }
    
    @Override
    public boolean verifyTypeDef(String userId, TypeDef typeDef) throws InvalidParameterException,
        RepositoryErrorException, TypeDefNotSupportedException, TypeDefConflictException,
        InvalidTypeDefException, UserNotAuthorizedException {
        // TODO: This method still needs to be implemented.  Returning false for now i.e. typedef is not added in local repository
        // so that typedefs from OMRS get loaded in OMRSContentManger::knownTypes.  This Hashmap is checked
        // for typedef validation.  It is needed when sending events to OMRS for instance.
        return true;
    }
    
    /*
     * ======================================================================
     * Group 1: Confirm the identity of the metadata repository being called.
     */
    
    /**
     * @return the parentConnector
     */
    public TestOmrsRepositoryConnector getParentConnector() {
        return (TestOmrsRepositoryConnector)parentConnector;
    }

    /**
     * @param parentConnector the parentConnector to set
     */
    public void setParentConnector(TestOmrsRepositoryConnector parentConnector) {
        this.parentConnector = parentConnector;
    }
    


    /*
     * ============================== Group 2: Working with typedefs
     */

    /**
     * Returns the list of different types of TypeDefs organized by TypeDef
     * Category.
     *
     * @param userId - unique identifier for requesting user.
     * @return TypeDefs - List of different categories of TypeDefs.
     * @throws RepositoryErrorException - there is a problem communicating with
     * the metadata repository.
     * @throws UserNotAuthorizedException - the userId is not permitted to
     * perform this operation.
     */
    public TypeDefGallery getAllTypes(String userId)
        throws RepositoryErrorException, UserNotAuthorizedException, InvalidParameterException {

        final String methodName = "getAllTypes";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);
        
        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        

        /*
         * Perform operation
         */

        /*
         * TODO try { loadWKCTypeDefs(userId); } catch (RepositoryErrorException
         * e) { e.printStackTrace();
         * LOG.error("getAllTypes: caught exception from WKC {}",
         * e.getErrorMessage()); throw e; }
         */

        /*
         * typeDefsForAPI will have been loaded with all discovered TypeDefs. It
         * can be converted into a TDG and can be reset on the next API to be
         * called.
         */

        // Convert the typeDefsForAPI to a gallery
        TypeDefGallery tdg = null;
//        if (typeDefsForAPI != null) {
//            tdg = typeDefsForAPI.convertTypeDefsToGallery();
//        }
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("<== {} TypeDefGallery={}", methodName, tdg);
//        }
        return tdg;
    }

    /**
     * Returns a list of type definitions that have the specified name. Type
     * names should be unique. This method allows wildcard character to be
     * included in the name. These are * (asterisk) for an arbitrary string of
     * characters and ampersand for an arbitrary character.
     *
     * @param userId - unique identifier for requesting user.
     * @param name - name of the TypeDefs to return (including wildcard
     * characters).
     * @return TypeDefGallery - List of different categories of type
     * definitions.
     * @throws InvalidParameterException - the name of the TypeDef is null.
     * @throws RepositoryErrorException - there is a problem communicating with
     * the metadata repository.
     * @throws UserNotAuthorizedException - the userId is not permitted to
     * perform this operation.
     */
    public TypeDefGallery findTypesByName(String userId, String name)
        throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException {

        final String methodName = "findTypesByName";
        @SuppressWarnings("unused")
        final String sourceName = metadataCollectionId;
        final String nameParameterName = "name";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeName(repositoryName, nameParameterName, name, methodName);

        /*
         * Perform operation
         */

        // Clear the per-API records of TypeDefs
//        typeDefsForAPI = new TypeDefsByCategory();

        /*
         * Parse the WKC TypesDef For each list try to convert each element
         * (i.e. each type def) to a corresponding OM type def. If a problem is
         * encountered within a typedef - for example we encounter a reference
         * attribute or anything else that is not supported in OM - then we skip
         * (silently) over the WKC type def. i.e. The metadatacollection will
         * convert the things that it understands, and will silently ignore
         * anything that it doesn't understand (e.g. structDefs) or anything
         * that contains something that it does not understand (e.g. a reference
         * attribute or a collection that contains anything other than
         * primitives).
         */

        // TODO: Populate the typeDefsForAPI object.

        // Convert the typeDefsForAPI to a gallery
        TypeDefGallery tdg = null;
//        if (typeDefsForAPI != null) {
//            tdg = typeDefsForAPI.convertTypeDefsToGallery();
//        }
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("<== findTypesByName(userId={}, name={}): tdg={}", userId, name, tdg);
//        }
        return tdg;
    }

    /**
     * Returns all of the TypeDefs for a specific category.
     *
     * @param userId - unique identifier for requesting user.
     * @param category - enum value for the category of TypeDef to return.
     * @return TypeDefs list.
     * @throws InvalidParameterException - the TypeDefCategory is null.
     * @throws RepositoryErrorException - there is a problem communicating with
     * the metadata repository.
     * @throws UserNotAuthorizedException - the userId is not permitted to
     * perform this operation.
     */
    public List<TypeDef> findTypeDefsByCategory(String userId, TypeDefCategory category)
        throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException {

        final String methodName = "findTypeDefsByCategory";
        final String categoryParameterName = "category";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateTypeDefCategory(repositoryName, categoryParameterName, category,
            methodName);

        /*
         * Perform operation
         */

        // Clear the per-API records of TypeDefs
//        typeDefsForAPI = new TypeDefsByCategory();

        List<TypeDef> retList = null;
        return retList;
    }

    /**
     * Returns all of the AttributeTypeDefs for a specific category.
     *
     * @param userId - unique identifier for requesting user.
     * @param category - enum value for the category of an AttributeTypeDef to
     * return.
     * @return AttributeTypeDefs list.
     * @throws InvalidParameterException - the TypeDefCategory is null.
     * @throws RepositoryErrorException - there is a problem communicating with
     * the metadata repository.
     * @throws UserNotAuthorizedException - the userId is not permitted to
     * perform this operation.
     */
    public List<AttributeTypeDef> findAttributeTypeDefsByCategory(String userId,
        AttributeTypeDefCategory category)
        throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException {

        final String methodName = "findAttributeTypeDefsByCategory";
        final String categoryParameterName = "category";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateAttributeTypeDefCategory(repositoryName, categoryParameterName,
            category, methodName);

        /*
         * Perform operation
         */

        // Clear the per-API records of TypeDefs
//        typeDefsForAPI = new TypeDefsByCategory();

        List<AttributeTypeDef> retList = null;

        return retList;

    }

    /**
     * Returns a boolean indicating if the entity is stored in the metadata
     * collection.
     *
     * @param userId - unique identifier for requesting user.
     * @param guid - String unique identifier for the entity.
     * @return entity details if the entity is found in the metadata collection;
     * otherwise return null.
     * @throws InvalidParameterException - the guid is null.
     * @throws RepositoryErrorException - there is a problem communicating with
     * the metadata repository where the metadata collection is stored.
     * @throws UserNotAuthorizedException - the userId is not permitted to
     * perform this operation.
     */
    public EntityDetail isEntityKnown(String userId, String guid)
        throws InvalidParameterException, RepositoryErrorException, UserNotAuthorizedException {

        final String methodName = "isEntityKnown";
        final String guidParameterName = "guid";

        /*
         * Validate parameters
         */
        this.validateRepositoryConnector(methodName);
        parentConnector.validateRepositoryIsActive(methodName);

        repositoryValidator.validateUserId(repositoryName, userId, methodName);
        repositoryValidator.validateGUID(repositoryName, guidParameterName, guid, methodName);

        /*
         * Perform operation
         */

        EntityDetail entityDetail;
        try {
            entityDetail = getEntityDetail(userId, guid);
        } catch (EntityNotKnownException e) {
            System.out.println(
                "isEntityKnown: caught EntityNotKnownException exception from getEntityDetail - exception swallowed, returning null");
            return null;
        } catch (RepositoryErrorException e) {
            System.out.println(
                "isEntityKnown: caught RepositoryErrorException exception from getEntityDetail - rethrowing");
            throw e;
        } catch (UserNotAuthorizedException e) {
            System.out.println(
                "isEntityKnown: caught UserNotAuthorizedException exception from getEntityDetail - rethrowing");
            throw e;
        }

        return entityDetail;
    }

    /**
     * Return the header and classifications for a specific entity.
     *
     * @param userId - unique identifier for requesting user.
     * @param guid - String unique identifier for the entity.
     * @return EntitySummary structure
     * @throws InvalidParameterException - the guid is null.
     * @throws RepositoryErrorException - there is a problem communicating with
     * the metadata repository where the metadata collection is stored.
     * @throws EntityNotKnownException - the requested entity instance is not
     * known in the metadata collection.
     * @throws UserNotAuthorizedException - the userId is not permitted to
     * perform this operation.
     */
    public EntitySummary getEntitySummary(String userId, String guid)
        throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException,
        UserNotAuthorizedException {

        return null;
    }

    /**
     * Return the header, classifications and properties of a specific entity.
     *
     * @param userId - unique identifier for requesting user.
     * @param guid - String unique identifier for the entity.
     * @return EntityDetail structure.
     * @throws InvalidParameterException - the guid is null.
     * @throws RepositoryErrorException - there is a problem communicating with
     * the metadata repository where the metadata collection is stored.
     * @throws EntityNotKnownException - the requested entity instance is not
     * known in the metadata collection.
     * @throws UserNotAuthorizedException - the userId is not permitted to
     * perform this operation.
     */
    public EntityDetail getEntityDetail(String userId, String guid)
        throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException,
        UserNotAuthorizedException {
        
        return null;
    }

    /**
     * Save the entity as a reference copy. The id of the home metadata
     * collection is already set up in the entity.
     *
     * @param userId - unique identifier for requesting user.
     * @param entity - details of the entity to save.
     * @throws InvalidParameterException - the entity is null.
     * @throws RepositoryErrorException - there is a problem communicating with
     * the metadata repository where the metadata collection is stored.
     * @throws TypeErrorException - the requested type is not known, or not
     * supported in the metadata repository hosting the metadata collection.
     * @throws PropertyErrorException - one or more of the requested properties
     * are not defined, or have different characteristics in the TypeDef for
     * this entity's type.
     * @throws HomeEntityException - the entity belongs to the local repository
     * so creating a reference copy would be invalid.
     * @throws EntityConflictException - the new entity conflicts with an
     * existing entity.
     * @throws InvalidEntityException - the new entity has invalid contents.
     * @throws UserNotAuthorizedException - the userId is not permitted to
     * perform this operation.
     */
    public void saveEntityReferenceCopy(String userId, EntityDetail entity)
        throws InvalidParameterException, RepositoryErrorException, TypeErrorException,
        PropertyErrorException, HomeEntityException, EntityConflictException,
        InvalidEntityException, UserNotAuthorizedException {

        Gson gson = new Gson();
        System.out.println("userId: " + userId + ", entity: " + gson.toJson(entity));
//            GlossaryServiceOmrsProxy.saveEntityReferenceCopy(entity);
    }
    
    /**
     * purge the entity reference
     */
    @Override
    public void purgeEntityReferenceCopy(String serverName, String entityGUID, String typeDefGUID,
        String typeDefName, String homeMetadataCollectionId) throws InvalidParameterException, RepositoryErrorException,
        EntityNotKnownException, HomeEntityException, FunctionNotSupportedException, UserNotAuthorizedException {
        
        System.out.println("deleting the entity: " + entityGUID);
        
//        GlossaryServiceOmrsProxy.purgeEntityReferenceCopy(entityGUID, typeDefName, homeMetadataCollectionId);
        
    }

    /**
     * save relationship reference
     */
    @Override
    public void saveRelationshipReferenceCopy(String userId, Relationship relationship)
        throws InvalidParameterException, RepositoryErrorException, TypeErrorException, EntityNotKnownException,
        PropertyErrorException, HomeRelationshipException, RelationshipConflictException, InvalidRelationshipException,
        FunctionNotSupportedException, UserNotAuthorizedException {
        
        Gson gson = new Gson();
        System.out.println("userId: " + userId + ", relationship: " + gson.toJson(relationship));
//            GlossaryServiceOmrsProxy.saveRelationshipReferenceCopy(relationship);
    }
    
    /**
     * Purge the relationship
     */
    @Override
    public  void purgeRelationshipReferenceCopy(String userId, String relationshipGUID, String typeDefGUID,
            String typeDefName, String   homeMetadataCollectionId) 
            throws InvalidParameterException, RepositoryErrorException, RelationshipNotKnownException,
            HomeRelationshipException, FunctionNotSupportedException, UserNotAuthorizedException
    {
        System.out.println("deleting the relationship: " + relationshipGUID);
//        GlossaryServiceOmrsProxy.purgeRelationshipReferenceCopy(relationshipGUID, typeDefName, homeMetadataCollectionId);

    }
}
