begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|dbsync
operator|.
name|SchemaUpdateStrategy
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
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A {@link DataChannel} provider that provides a single instance of DataDomain configured  * per configuration supplied via injected {@link DataChannelDescriptorLoader}.  *   * @since 3.1  */
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
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
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
name|DataChannelDescriptorLoader
name|loader
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|RuntimeProperties
name|configurationProperties
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|SchemaUpdateStrategy
name|defaultSchemaUpdateStrategy
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DbAdapterFactory
name|adapterFactory
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DataSourceFactoryLoader
name|dataSourceFactoryLoader
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|Injector
name|injector
decl_stmt|;
specifier|protected
specifier|volatile
name|DataDomain
name|dataDomain
decl_stmt|;
specifier|public
name|DataDomain
name|get
parameter_list|()
throws|throws
name|ConfigurationException
block|{
if|if
condition|(
name|dataDomain
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|dataDomain
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|createDataChannel
argument_list|()
expr_stmt|;
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
throw|throw
operator|new
name|DataDomainLoadException
argument_list|(
literal|"Error loading DataChannel: '%s'"
argument_list|,
name|e
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
return|return
name|dataDomain
return|;
block|}
specifier|protected
name|void
name|createDataChannel
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|configurationLocation
init|=
name|configurationProperties
operator|.
name|get
argument_list|(
name|RuntimeProperties
operator|.
name|CONFIGURATION_LOCATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|configurationLocation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DataDomainLoadException
argument_list|(
literal|"No configuration location available. "
operator|+
literal|"You can specify when creating Cayenne runtime "
operator|+
literal|"or via a system property '%s'"
argument_list|,
name|RuntimeProperties
operator|.
name|CONFIGURATION_LOCATION
argument_list|)
throw|;
block|}
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
name|configurationLocation
argument_list|)
expr_stmt|;
block|}
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
name|configurationLocation
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
literal|"Configuration file \"%s\" is not found."
argument_list|,
name|configurationLocation
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
literal|" configurations, will use the first one: "
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
literal|"finished configuration loading: "
operator|+
name|configurationLocation
operator|+
literal|" in "
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
name|DataChannelDescriptor
name|descriptor
init|=
name|tree
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
name|DataDomain
name|dataDomain
init|=
operator|new
name|DataDomain
argument_list|(
name|descriptor
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|dataDomain
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
name|addMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
block|}
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
name|DataNode
name|dataNode
init|=
operator|new
name|DataNode
argument_list|(
name|nodeDescriptor
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|dataNode
operator|.
name|setDataSourceLocation
argument_list|(
name|nodeDescriptor
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|DataSourceFactory
name|dataSourceFactory
init|=
name|dataSourceFactoryLoader
operator|.
name|getDataSourceFactory
argument_list|(
name|nodeDescriptor
argument_list|)
decl_stmt|;
name|DataSource
name|dataSource
init|=
name|dataSourceFactory
operator|.
name|getDataSource
argument_list|(
name|nodeDescriptor
argument_list|)
decl_stmt|;
name|dataNode
operator|.
name|setDataSourceFactory
argument_list|(
name|nodeDescriptor
operator|.
name|getDataSourceFactoryType
argument_list|()
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|setDataSource
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
comment|// schema update strategy
name|String
name|schemaUpdateStrategyType
init|=
name|nodeDescriptor
operator|.
name|getSchemaUpdateStrategyType
argument_list|()
decl_stmt|;
if|if
condition|(
name|schemaUpdateStrategyType
operator|==
literal|null
condition|)
block|{
name|dataNode
operator|.
name|setSchemaUpdateStrategy
argument_list|(
name|defaultSchemaUpdateStrategy
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|setSchemaUpdateStrategyName
argument_list|(
name|defaultSchemaUpdateStrategy
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SchemaUpdateStrategy
name|strategy
init|=
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|SchemaUpdateStrategy
operator|.
name|class
argument_list|,
name|schemaUpdateStrategyType
argument_list|)
decl_stmt|;
name|dataNode
operator|.
name|setSchemaUpdateStrategyName
argument_list|(
name|schemaUpdateStrategyType
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|setSchemaUpdateStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
comment|// DbAdapter
name|dataNode
operator|.
name|setAdapter
argument_list|(
name|adapterFactory
operator|.
name|createAdapter
argument_list|(
name|nodeDescriptor
argument_list|,
name|dataSource
argument_list|)
argument_list|)
expr_stmt|;
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
name|getMap
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
block|}
name|this
operator|.
name|dataDomain
operator|=
name|dataDomain
expr_stmt|;
block|}
block|}
end_class

end_unit

