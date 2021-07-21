package com.omrs;

import java.util.UUID;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnectorProviderBase;

/**
 * In the Egeria Open Connector Framework (OCF), a ConnectorProvider is a factory for a specific type of connector.
 * The BgOmrsRepositoryEventMapperProvider is the connector provider for the BgOmrsRepositoryEventMapper.
 * It extends OMRSRepositoryEventMapperProviderBase which in turn extends the OCF ConnectorProviderBase.
 * ConnectorProviderBase supports the creation of connector instances.
 *
 * The TestOmrsRepositoryEventMapperProvider must initialize ConnectorProviderBase with the Java class
 * name of the OMRS Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class TestOmrsRepositoryEventMapperProvider extends OMRSRepositoryConnectorProviderBase {

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * OMRS Connector implementation.
     */
    public TestOmrsRepositoryEventMapperProvider()
    {

        super.setConnectorClassName(TestOmrsRepositoryEventMapper.class.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(UUID.randomUUID().toString());
        connectorType.setQualifiedName("Test OMRS Event Mapper Connector");
        connectorType.setDisplayName("Test OMRS Event Mapper Connector");
        connectorType.setDescription("Test OMRS Event Mapper Connector that processes events.");
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }

}
