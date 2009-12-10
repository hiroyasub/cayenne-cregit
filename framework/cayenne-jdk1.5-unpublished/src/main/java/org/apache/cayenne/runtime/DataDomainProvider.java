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
name|runtime
package|;
end_package

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
name|DataSourceFactory
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
name|DataSourceFactoryLoader
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
name|DbAdapterFactory
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
name|ConfigurationException
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
name|runtimeName
init|=
name|configurationProperties
operator|.
name|get
argument_list|(
name|RuntimeProperties
operator|.
name|CAYENNE_RUNTIME_NAME
argument_list|)
decl_stmt|;
name|DataChannelDescriptor
name|descriptor
init|=
name|loader
operator|.
name|load
argument_list|(
name|runtimeName
argument_list|)
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
name|getDataNodeDescriptors
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
name|getLocation
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

