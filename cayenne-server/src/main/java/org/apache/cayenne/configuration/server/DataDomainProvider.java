begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|server
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ConfigurationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|DataChannel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|DataChannelQueryFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|DataChannelSyncFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DataDomain
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DataNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DataRowStoreFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|types
operator|.
name|ValueObjectTypeRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|cache
operator|.
name|NestedQueryCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|cache
operator|.
name|QueryCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|ConfigurationTree
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|DataChannelDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|DataChannelDescriptorLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|DataChannelDescriptorMerger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|DataNodeDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|RuntimeProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|AdhocObjectFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|Injector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|event
operator|.
name|EventManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DataMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|EntitySorter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|reflect
operator|.
name|generic
operator|.
name|ValueComparisonStrategyFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|resource
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|resource
operator|.
name|ResourceLocator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * A {@link DataChannel} provider that provides a single instance of DataDomain  * configured per configuration supplied via injected  * {@link DataChannelDescriptorLoader}.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DataDomainProvider
implements|implements
name|Provider
argument_list|<
name|DataDomain
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DataDomainProvider
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ResourceLocator
name|resourceLocator
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DataChannelDescriptorMerger
name|descriptorMerger
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DataChannelDescriptorLoader
name|loader
decl_stmt|;
comment|/** 	 * @since 4.1 	 */
annotation|@
name|Inject
specifier|protected
name|List
argument_list|<
name|DataChannelQueryFilter
argument_list|>
name|queryFilters
decl_stmt|;
comment|/** 	 * @since 4.1 	 */
annotation|@
name|Inject
specifier|protected
name|List
argument_list|<
name|DataChannelSyncFilter
argument_list|>
name|syncFilters
decl_stmt|;
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_DOMAIN_LISTENERS_LIST
argument_list|)
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|listeners
decl_stmt|;
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_PROJECT_LOCATIONS_LIST
argument_list|)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|locations
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|Injector
name|injector
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|QueryCache
name|queryCache
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|RuntimeProperties
name|runtimeProperties
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DataNodeFactory
name|dataNodeFactory
decl_stmt|;
annotation|@
name|Override
specifier|public
name|DataDomain
name|get
parameter_list|()
throws|throws
name|ConfigurationException
block|{
try|try
block|{
return|return
name|createAndInitDataDomain
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|ConfigurationException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|String
name|causeMessage
init|=
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|String
name|message
init|=
name|causeMessage
operator|!=
literal|null
operator|&&
name|causeMessage
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|causeMessage
else|:
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|DataDomainLoadException
argument_list|(
literal|"DataDomain startup failed: %s"
argument_list|,
name|e
argument_list|,
name|message
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|DataDomain
name|createDataDomain
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|DataDomain
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|protected
name|DataDomain
name|createAndInitDataDomain
parameter_list|()
throws|throws
name|Exception
block|{
name|DataChannelDescriptor
name|descriptor
init|=
name|loadDescriptor
argument_list|()
decl_stmt|;
name|DataDomain
name|dataDomain
init|=
name|createDataDomain
argument_list|(
name|descriptor
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|dataDomain
operator|.
name|setMaxIdQualifierSize
argument_list|(
name|runtimeProperties
operator|.
name|getInt
argument_list|(
name|Constants
operator|.
name|SERVER_MAX_ID_QUALIFIER_SIZE_PROPERTY
argument_list|,
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|dataDomain
operator|.
name|setQueryCache
argument_list|(
operator|new
name|NestedQueryCache
argument_list|(
name|queryCache
argument_list|)
argument_list|)
expr_stmt|;
name|dataDomain
operator|.
name|setEntitySorter
argument_list|(
name|injector
operator|.
name|getInstance
argument_list|(
name|EntitySorter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|dataDomain
operator|.
name|setEventManager
argument_list|(
name|injector
operator|.
name|getInstance
argument_list|(
name|EventManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|dataDomain
operator|.
name|setDataRowStoreFactory
argument_list|(
name|injector
operator|.
name|getInstance
argument_list|(
name|DataRowStoreFactory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|dataDomain
operator|.
name|initWithProperties
argument_list|(
name|descriptor
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|DataMap
name|dataMap
range|:
name|descriptor
operator|.
name|getDataMaps
argument_list|()
control|)
block|{
name|dataDomain
operator|.
name|addDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
block|}
name|dataDomain
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|applyDBLayerDefaults
argument_list|()
expr_stmt|;
name|dataDomain
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|setValueObjectTypeRegistry
argument_list|(
name|injector
operator|.
name|getInstance
argument_list|(
name|ValueObjectTypeRegistry
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|dataDomain
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|setValueComparisonStrategyFactory
argument_list|(
name|injector
operator|.
name|getInstance
argument_list|(
name|ValueComparisonStrategyFactory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|dataDomain
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|setObjectFactory
argument_list|(
name|injector
operator|.
name|getInstance
argument_list|(
name|AdhocObjectFactory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|DataNodeDescriptor
name|nodeDescriptor
range|:
name|descriptor
operator|.
name|getNodeDescriptors
argument_list|()
control|)
block|{
name|addDataNode
argument_list|(
name|dataDomain
argument_list|,
name|nodeDescriptor
argument_list|)
expr_stmt|;
block|}
comment|// init default node
name|DataNode
name|defaultNode
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|descriptor
operator|.
name|getDefaultNodeName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|defaultNode
operator|=
name|dataDomain
operator|.
name|getDataNode
argument_list|(
name|descriptor
operator|.
name|getDefaultNodeName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|defaultNode
operator|==
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|DataNode
argument_list|>
name|allNodes
init|=
name|dataDomain
operator|.
name|getDataNodes
argument_list|()
decl_stmt|;
if|if
condition|(
name|allNodes
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|defaultNode
operator|=
name|allNodes
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|defaultNode
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"setting DataNode '"
operator|+
name|defaultNode
operator|.
name|getName
argument_list|()
operator|+
literal|"' as default, used by all unlinked DataMaps"
argument_list|)
expr_stmt|;
name|dataDomain
operator|.
name|setDefaultNode
argument_list|(
name|defaultNode
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|DataChannelQueryFilter
name|filter
range|:
name|queryFilters
control|)
block|{
name|dataDomain
operator|.
name|addQueryFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|DataChannelSyncFilter
name|filter
range|:
name|syncFilters
control|)
block|{
name|dataDomain
operator|.
name|addSyncFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Object
name|listener
range|:
name|listeners
control|)
block|{
name|dataDomain
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
return|return
name|dataDomain
return|;
block|}
comment|/** 	 * @since 4.0      */
specifier|protected
name|DataChannelDescriptor
name|loadDescriptor
parameter_list|()
block|{
name|DataChannelDescriptor
name|descriptor
init|=
name|locations
operator|.
name|isEmpty
argument_list|()
condition|?
operator|new
name|DataChannelDescriptor
argument_list|()
else|:
name|loadDescriptorFromConfigs
argument_list|()
decl_stmt|;
name|String
name|nameOverride
init|=
name|runtimeProperties
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|SERVER_DOMAIN_NAME_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|nameOverride
operator|!=
literal|null
condition|)
block|{
name|descriptor
operator|.
name|setName
argument_list|(
name|nameOverride
argument_list|)
expr_stmt|;
block|}
return|return
name|descriptor
return|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|protected
name|DataNode
name|addDataNode
parameter_list|(
name|DataDomain
name|dataDomain
parameter_list|,
name|DataNodeDescriptor
name|nodeDescriptor
parameter_list|)
throws|throws
name|Exception
block|{
name|DataNode
name|dataNode
init|=
name|dataNodeFactory
operator|.
name|createDataNode
argument_list|(
name|nodeDescriptor
argument_list|)
decl_stmt|;
comment|// DataMaps
for|for
control|(
name|String
name|dataMapName
range|:
name|nodeDescriptor
operator|.
name|getDataMapNames
argument_list|()
control|)
block|{
name|dataNode
operator|.
name|addDataMap
argument_list|(
name|dataDomain
operator|.
name|getDataMap
argument_list|(
name|dataMapName
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|dataDomain
operator|.
name|addNode
argument_list|(
name|dataNode
argument_list|)
expr_stmt|;
return|return
name|dataNode
return|;
block|}
specifier|private
name|DataChannelDescriptor
name|loadDescriptorFromConfigs
parameter_list|()
block|{
name|long
name|t0
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"starting configuration loading: "
operator|+
name|locations
argument_list|)
expr_stmt|;
block|}
name|DataChannelDescriptor
index|[]
name|descriptors
init|=
operator|new
name|DataChannelDescriptor
index|[
name|locations
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|locations
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|location
init|=
name|locations
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|Resource
argument_list|>
name|configurations
init|=
name|resourceLocator
operator|.
name|findResources
argument_list|(
name|location
argument_list|)
decl_stmt|;
if|if
condition|(
name|configurations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DataDomainLoadException
argument_list|(
literal|"Configuration resource \"%s\" is not found."
argument_list|,
name|location
argument_list|)
throw|;
block|}
name|Resource
name|configurationResource
init|=
name|configurations
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// no support for multiple configs yet, but this is not a hard error
if|if
condition|(
name|configurations
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"found "
operator|+
name|configurations
operator|.
name|size
argument_list|()
operator|+
literal|" configurations for "
operator|+
name|location
operator|+
literal|", will use the first one: "
operator|+
name|configurationResource
operator|.
name|getURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ConfigurationTree
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|tree
init|=
name|loader
operator|.
name|load
argument_list|(
name|configurationResource
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|tree
operator|.
name|getLoadFailures
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// TODO: andrus 03/10/2010 - log the errors before throwing?
throw|throw
operator|new
name|DataDomainLoadException
argument_list|(
name|tree
argument_list|,
literal|"Error loading DataChannelDescriptor"
argument_list|)
throw|;
block|}
name|descriptors
index|[
name|i
index|]
operator|=
name|tree
operator|.
name|getRootNode
argument_list|()
expr_stmt|;
block|}
name|long
name|t1
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|logger
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"finished configuration loading in "
operator|+
operator|(
name|t1
operator|-
name|t0
operator|)
operator|+
literal|" ms."
argument_list|)
expr_stmt|;
block|}
return|return
name|descriptorMerger
operator|.
name|merge
argument_list|(
name|descriptors
argument_list|)
return|;
block|}
block|}
end_class

end_unit

