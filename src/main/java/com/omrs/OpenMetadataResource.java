
package com.omrs;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider;
import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.OMAGServerAdminServices;
import org.odpi.openmetadata.adminservices.OMAGServerAdminStoreServices;
import org.odpi.openmetadata.adminservices.OMAGServerOperationalServices;
import org.odpi.openmetadata.adminservices.configuration.properties.CohortConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenMetadataEventProtocolVersion;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenMetadataExchangeRule;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.EmbeddedConnection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.VirtualConnection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import io.swagger.v3.oas.annotations.Parameter;

@Path("/open-metadata")
public class OpenMetadataResource {
	
	public static final String serverName = "TestOmrsServer";
    public static final String KAFKA_PRODUCER = "producer";
    public static final String KAFKA_CONSUMER = "consumer";

	private static final ConnectorConfigurationFactory factory = new ConnectorConfigurationFactory();
    private static final OMAGServerAdminServices adminAPI = new OMAGServerAdminServices();
    private static final OMAGServerOperationalServices operationalAPI = new OMAGServerOperationalServices();
    private static final Gson gson = (new GsonBuilder()).create();

	public static String metadataCollectionId;
	public static String userId = "admin";

	@Path("/cohorts/{cohort_name}")
	@POST
	@Produces("application/json")
	public Response registerCohort(
			@Parameter(description = "name of the cohort", required = true) @PathParam("cohort_name") String cohortName,
	        @Parameter(description = "name/value pairs used to configure the connection to the event bus connector",
	                   required = false) Map<String, Object> configProperties,
	        @Parameter(description = "common root of the topics used by the open metadata server",
	                   required = false) @QueryParam("topic_url_root") String topicURLRoot) {
		
		createConfigurationStore();
		
        createLocalRepository();
        
        createEventMapper();
        
        adminAPI.setServerUserId(userId, serverName, userId);
        adminAPI.setOrganizationName(userId, serverName, "ABC");
        adminAPI.setServerURLRoot(userId, serverName, "http://localhost:9080/egeria");

        addCohortConfig(cohortName, topicURLRoot, configProperties);

        OMAGServerConfigResponse response = adminAPI.getStoredConfiguration(userId, serverName);
		return Response.ok().entity(response).status(200).build();
	}

	@Path("/instance")
	@POST
	@Produces("application/json")
	public Response activateOmrsServer() {
		
		operationalAPI.activateWithStoredConfig(userId, serverName);
		return Response.status(Response.Status.OK).build();
	}

	@Path("/cohorts")
	@GET
	@Produces("application/json")
	public Response getCohorts() {
		String response = "{\"key\": \"value\"}";
		return Response.ok().entity(response).status(200).build();
	}

	@Path("/crud")
	@GET
	@Produces("application/json")
	public Response performCrudAction(
			@Parameter(description = "create entity, delete entity", required = true) @PathParam("action") String action) throws TypeErrorException {
		if (action.equals("new_term")) {
			TestOmrsRepositoryEventMapper eventMapper = TestOmrsRepositoryEventMapper.getInstance();
			OMRSRepositoryHelper repoHelper = eventMapper.getMetadataCollection().getParentConnector().getRepositoryHelper();
			
	        EntityDetail detail = repoHelper.getSkeletonEntity(serverName, metadataCollectionId, null, "", "GlossaryTerm");
	        detail.setGUID(UUID.randomUUID().toString());
	        detail.setInstanceProvenanceType(InstanceProvenanceType.LOCAL_COHORT);
	        detail.setStatus(InstanceStatus.ACTIVE);
	        detail.setMetadataCollectionName(serverName);
	        
	        InstanceProperties instanceProperties = new InstanceProperties();
	        TypeDef typeDef = repoHelper.getTypeDefByName(metadataCollectionId, "GlossaryTerm");
	        List<TypeDefAttribute> typeDefAttributes = typeDef.getPropertiesDefinition();
	        TypeDefAttribute typeDefAttribute = getTypeDefAttribute(typeDefAttributes, "displayName");   
	        setValueInInstanceProperty(instanceProperties, typeDefAttribute.getAttributeType(), "displayName", "Term Name");
	        detail.setProperties(instanceProperties);
	
	        System.out.println("Sending New EntityDetail :" + detail.toString());
	
			eventMapper.processNewEntity(detail);
		} else {
			throw new BadRequestException("Invalid action:" + action);
		}
        
		return Response.ok().status(200).build();
	}
	
    private TypeDefAttribute getTypeDefAttribute(List<TypeDefAttribute> typeDefAttributes, String name) {
        TypeDefAttribute attribute = null;
        for (TypeDefAttribute typeDefAttribute : typeDefAttributes) {
            if (typeDefAttribute.getAttributeName().equals(name)) {
                attribute = typeDefAttribute;
                break;
            }
        }
        return attribute;
    }
    
    private void setValueInInstanceProperty(InstanceProperties instanceProperties, AttributeTypeDef attributeTypeDef, String fieldName,
            Object fieldValue) {
            String attrTypeGuid = attributeTypeDef.getGUID();
            String attrTypeName = attributeTypeDef.getName();
             
            PrimitiveDef primitiveDef = (PrimitiveDef)attributeTypeDef;
            PrimitiveDefCategory primitiveDefCategory = primitiveDef.getPrimitiveDefCategory();
            PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
            primitivePropertyValue.setPrimitiveDefCategory(primitiveDefCategory);
            primitivePropertyValue.setTypeGUID(attrTypeGuid);
            primitivePropertyValue.setTypeName(attrTypeName);
            primitivePropertyValue.setPrimitiveValue(fieldValue);
            instanceProperties.setProperty(fieldName, primitivePropertyValue);
        }

    private void createConfigurationStore() {

        Connection connection = factory.getServerConfigConnection(serverName);
        (new OMAGServerAdminStoreServices()).setConfigurationStoreConnection(userId, connection);
    }
    
    private void createLocalRepository() {
        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(setConfigurationAsOrigin(ConnectorType.getConnectorTypeType()));
        connectorType.setGUID("1aef36d7-7b95-41eb-8061-62b4c504d7f0");
        connectorType.setQualifiedName("TestOMRSRepository.ConnectorType." + serverName);
        connectorType.setDisplayName("TestOMRSRepository.ConnectorType." + serverName);
        connectorType.setDescription("Test OMRS repository connector type.");
        connectorType.setConnectorProviderClassName(TestOmrsRepositoryConnectorProvider.class.getName());

        Connection connection = new Connection();
        connection.setType(setConfigurationAsOrigin(Connection.getConnectionType()));
        connection.setGUID("8106ab8a-56cd-46a8-b909-1cd227e15371");
        connection.setQualifiedName("TestOMRSRepository.Connection." + serverName);
        connection.setDisplayName("TestOMRSRepository.Connection." + serverName);
        connection.setDescription("Test OMRS repository connection.");
        connection.setConnectorType(connectorType);

        adminAPI.setRepositoryProxyConnection(userId, serverName, connection);
    }

    public void createEventMapper() {

        Endpoint endpoint = new Endpoint();
        endpoint.setType(setConfigurationAsOrigin(Endpoint.getEndpointType()));
        endpoint.setGUID(UUID.randomUUID().toString());
        endpoint.setQualifiedName("TestEventMapper.Endpoint." + serverName);
        endpoint.setDisplayName("TestEventMapper.Endpoint." + serverName);
        endpoint.setDescription("Test event mapper endpoint.");
        endpoint.setAddress("WKC BG repository notifications");

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(setConfigurationAsOrigin(ConnectorType.getConnectorTypeType()));
        connectorType.setGUID(UUID.randomUUID().toString());
        connectorType.setQualifiedName("TestEventMapper.ConnectorType." + serverName);
        connectorType.setDisplayName("TestEventMapper.ConnectorType." + serverName);
        connectorType.setDescription("Test event mapper connector type.");
        connectorType.setConnectorProviderClassName(TestOmrsRepositoryEventMapperProvider.class.getName());

        Connection connection = new Connection();
        connection.setType(setConfigurationAsOrigin(Connection.getConnectionType()));
        connection.setGUID(UUID.randomUUID().toString());
        connection.setQualifiedName("TestEventMapper.Connection." + serverName);
        connection.setDisplayName("TestEventMapper.Connection." + serverName);
        connection.setDescription("Test event mapper connection.");
        connection.setEndpoint(endpoint);
        connection.setConnectorType(connectorType);

        adminAPI.setRepositoryProxyEventMapper(userId, serverName, connection);
    }

    private void addCohortConfig(String cohortName, String topicURLRoot, Map<String, Object> configProperties)  {
    	
        if (configProperties.size() != 2 || !configProperties.containsKey(KAFKA_PRODUCER)
                || !configProperties.containsKey(KAFKA_CONSUMER)) {
            System.out.println("Request body is missing " + KAFKA_CONSUMER + ", " + KAFKA_PRODUCER);
        }

        OMAGServerConfig omagServerConfig = adminAPI.getStoredConfiguration(userId, serverName).getOMAGServerConfig();
        metadataCollectionId = omagServerConfig.getRepositoryServicesConfig().getLocalRepositoryConfig().getMetadataCollectionId();
        userId = metadataCollectionId + "_admin";
        String localServerId = omagServerConfig.getLocalServerId();
        configProperties.put("local.server.id", localServerId);
        CohortConfig cohortConfig = createCohortConfig(cohortName, topicURLRoot, configProperties);
        adminAPI.setCohortConfig(userId, serverName, cohortName, cohortConfig);
    }
    
    public JsonObject createJSONObjectFromPrimitiveVal(String object) {
        return gson.fromJson(object, JsonObject.class);
    }


    private CohortConfig createCohortConfig(String cohortName, String topicURLRoot,
        Map<String, Object> additionalProperties) {
        
        CohortConfig cohortConfig = new CohortConfig();
        cohortConfig.setCohortName(cohortName);
        cohortConfig.setCohortOMRSTopicProtocolVersion(OpenMetadataEventProtocolVersion.V1);
        cohortConfig.setEventsToProcessRule(OpenMetadataExchangeRule.ALL);
        cohortConfig.setCohortRegistryConnection(createRegistryConnection(metadataCollectionId, cohortName, topicURLRoot));
        cohortConfig.setCohortOMRSTopicConnection(
        		createCohortOMRSTopicConnection(cohortName, topicURLRoot, additionalProperties));
        return cohortConfig;
    }

    private Connection createRegistryConnection(String repoId, String cohortName, String topicURLRoot) {
        Connection connection = new Connection();
        ConnectorType connectorType = createConnectorType(cohortName, topicURLRoot, "DefaultCohortRegistry.ConnectorType.");
        connectorType.setDescription("OMRS default cohort registry connector type.");
        connectorType.setConnectorProviderClassName(OmrsRegistryProvider.class.getName());
        connection.setConnectorType(connectorType);
        
        Endpoint endPoint = createEndpoint(cohortName, topicURLRoot, "DefaultCohortRegistry.Endpoint.");
        
        StringBuilder address = new StringBuilder();
        address.append(repoId + "_omag_");

        if (!StringUtils.isEmpty(cohortName)) {
            address.append(cohortName + "_");
        }

        address.append("registrystore");
        endPoint.setAddress(address.toString());
        connection.setEndpoint(endPoint);
        
        StringBuilder name = new StringBuilder();
        name.append("DefaultCohortRegistry.Connection");
        if (!StringUtils.isEmpty(cohortName)) {
            name.append("." + cohortName);
        }
        if (!StringUtils.isEmpty(topicURLRoot)) {
            name.append("." + topicURLRoot);
        }
        connection.setQualifiedName(name.toString());
        connection.setDisplayName(name.toString());
        connection.setDescription("OMRS default cohort registry connection.");
        connection.setGUID(UUID.randomUUID().toString());
        connection.setType(setConfigurationAsOrigin(Connection.getConnectionType())); // createTypeForConnection());
        
        return connection;
    }

    private static VirtualConnection createCohortOMRSTopicConnection(String cohortName,
        String topicURLRoot, Map<String, Object> additionalProperties) {
        
        VirtualConnection virtualConnection = new VirtualConnection();
        ConnectorType connectorType = createConnectorType(cohortName, topicURLRoot, "DefaultCohortTopic.ConnectorType.");
        connectorType.setDescription("OMRS default cohort topic connector type.");
        connectorType.setConnectorProviderClassName("org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicProvider");
        virtualConnection.setConnectorType(connectorType);
            
        StringBuilder name = new StringBuilder();
        name.append("TopicConnector.Cohort");
        if (!StringUtils.isEmpty(cohortName)) {
            name.append("." + cohortName);
        }
        if (!StringUtils.isEmpty(topicURLRoot)) {
            name.append("." + topicURLRoot);
        }
        virtualConnection.setQualifiedName(name.toString());
        virtualConnection.setDisplayName(name.toString());
        virtualConnection.setDescription("OMRS default cohort topic connection.");
        virtualConnection.setGUID(UUID.randomUUID().toString());
        virtualConnection.setType(setConfigurationAsOrigin(VirtualConnection.getVirtualConnectionType())); //createTypeForVirtualConnection());
        
        EmbeddedConnection embeddedConnection = new EmbeddedConnection();
        embeddedConnection.setEmbeddedConnection(createEmbeddedConnection(cohortName, topicURLRoot, additionalProperties));
        embeddedConnection.setDisplayName(cohortName + " OMRS Topic");
        
        virtualConnection.setEmbeddedConnections(Arrays.asList(embeddedConnection));
        
        return virtualConnection;
    }

    private static Connection createEmbeddedConnection(String cohortName, String topicURLRoot,
        Map<String, Object> configurationProperties) {
        
        Connection connection = new Connection();
        ConnectorType connectorType = createConnectorType(null, null, "Kafka Open Metadata Topic Connector");
        connectorType.setDescription("Kafka Open Metadata Topic Connector supports string based events over an Apache Kafka event bus.");
        connectorType.setConnectorProviderClassName(KafkaOpenMetadataTopicProvider.class.getName()); //"org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider");
        connectorType.setRecognizedAdditionalProperties(Arrays.asList("producer", "consumer", "local.server.id"));
        connection.setConnectorType(connectorType);
        
        String prefix = "openmetadata.repositoryservices.cohort.";
        Endpoint endPoint = createEndpoint(cohortName, topicURLRoot, prefix);
        StringBuilder address = buildOMRSTopicAddress(topicURLRoot, prefix, cohortName);
        endPoint.setAddress(address.toString());
        connection.setEndpoint(endPoint);
        
        connection.setQualifiedName(cohortName + " OMRS Topic");
        connection.setDisplayName(cohortName + " OMRS Topic");
        connection.setDescription(cohortName + " OMRS Topic");
        connection.setGUID(UUID.randomUUID().toString());
        connection.setType(setConfigurationAsOrigin(Connection.getConnectionType())); //createTypeForConnection());
        connection.setConfigurationProperties(configurationProperties);
                
        return connection;
    }
    
    private static StringBuilder buildOMRSTopicAddress(String topicURLRoot,String prefix, String cohortName) {
        StringBuilder address = new StringBuilder();
        if (!StringUtils.isEmpty(topicURLRoot)) {
            address.append(topicURLRoot + ".");
        }
        if (!StringUtils.isEmpty(prefix)) {
            address.append(prefix);
        }
        if (!StringUtils.isEmpty(cohortName)) {
            address.append(cohortName + ".");
        }
        address.append("OMRSTopic");
        return address;
    }
    
    private static ElementType setConfigurationAsOrigin(ElementType type) {
    	type.setElementOrigin(ElementOrigin.CONFIGURATION);
    	return type;
    }

    private static ConnectorType createConnectorType(String cohortName, String topicURLRoot, String prefix) {
        ConnectorType connectorType = new ConnectorType();
        StringBuilder name = new StringBuilder();
        if (!StringUtils.isEmpty(prefix)) {
            name.append(prefix);
        }   
        if (!StringUtils.isEmpty(cohortName)) {
            name.append(cohortName);
        }
        if (!StringUtils.isEmpty(topicURLRoot)) {
            name.append("." + topicURLRoot);
        }
        String completeName = name.toString();
        connectorType.setDisplayName(completeName);
        connectorType.setQualifiedName(completeName);
        connectorType.setGUID(UUID.randomUUID().toString());
        connectorType.setType(setConfigurationAsOrigin(ConnectorType.getConnectorTypeType())); //createTypeForConnectorType());
        return connectorType;
    }
    
    private static Endpoint createEndpoint(String cohortName, String topicURLRoot, String prefix) {
            
            Endpoint endPoint = new Endpoint();
            StringBuilder name = new StringBuilder();
            if (!StringUtils.isEmpty(prefix)) {
                name.append(prefix);
            }       
            if (!StringUtils.isEmpty(cohortName)) {
                name.append(cohortName);
            }
            if (!StringUtils.isEmpty(topicURLRoot)) {
                name.append("." + topicURLRoot);
            }
            endPoint.setDisplayName(name.toString());
            endPoint.setQualifiedName(name.toString());
            endPoint.setGUID(UUID.randomUUID().toString());
            endPoint.setType(setConfigurationAsOrigin(Endpoint.getEndpointType())); //createTypeForEndpoint());
            return endPoint;
        }
}
