/*
 * IBM Confidential
 * OCO Source Materials
 * Copyright IBM Corp. 2017, 2018
 */

package com.omrs;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

/**
 * The TestOmrsRepositoryConnector is a connector to a local repository. This is the connector
 * used by the EnterpriseOMRSRepositoryConnector to make a direct call to the local repository.
 */
public class TestOmrsRepositoryConnector extends OMRSRepositoryConnector {

	/**
     * Set up the unique Id for this metadata collection.
     *
     * @param metadataCollectionId - String unique Id
     */
    public void setMetadataCollectionId(String metadataCollectionId) {
        this.metadataCollectionId = metadataCollectionId;

        /*
         * Initialize the metadata collection only once the connector is properly set up.
         */
        super.metadataCollection = new TestOmrsMetadataCollection(this, super.serverName,
            repositoryHelper, repositoryValidator, metadataCollectionId);
    }
}
