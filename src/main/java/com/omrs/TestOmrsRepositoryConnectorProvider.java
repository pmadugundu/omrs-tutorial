/*
 * IBM Confidential
 * OCO Source Materials
 * Copyright IBM Corp. 2018
 */
package com.omrs;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

/**
 * In the Open Connector Framework (OCF), a ConnectorProvider is a factory for a specific type of connector.
 * The BgOmrsRepositoryConnectorProvider is the connector provider for the BgOmrsRepositoryConnector.
 * It extends OMRSRepositoryConnectorProviderBase which in turn extends the OCF ConnectorProviderBase.
 * ConnectorProviderBase supports the creation of connector instances.
 *
 * The TestOmrsRepositoryConnectorProvider must initialize ConnectorProviderBase with the Java class
 * name of the OMRS Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class TestOmrsRepositoryConnectorProvider extends OMRSRepositoryConnectorProviderBase {
    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public TestOmrsRepositoryConnectorProvider() {
        super.setConnectorClassName(TestOmrsRepositoryConnector.class.getName());
    }
}
