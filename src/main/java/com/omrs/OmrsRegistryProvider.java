/*
 * IBM Confidential
 * OCO Source Materials
 * Copyright IBM Corp. 2018
 */

package com.omrs;

import org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file.FileBasedRegistryStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.OMRSCohortRegistryStoreProviderBase;

/**
 * Connection provider for saving the omag registry file into DB2
 *
 */
public class OmrsRegistryProvider extends OMRSCohortRegistryStoreProviderBase {
    static final String connectorTypeGUID = "108b85fe-d7a8-45c3-9f88-742ac4e4fd14";
    static final String connectorTypeName = "DB2 Based Cohort Registry Store Connector";
    static final String connectorTypeDescription = "Connector supports storing of the open metadata cohort registry in DB2.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public OmrsRegistryProvider() {
        
        super.setConnectorClassName(FileBasedRegistryStoreConnector.class.getName()); //OmrsRegistryConnector.class.getName());
        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());
        super.connectorTypeBean = connectorType;
    }
}
