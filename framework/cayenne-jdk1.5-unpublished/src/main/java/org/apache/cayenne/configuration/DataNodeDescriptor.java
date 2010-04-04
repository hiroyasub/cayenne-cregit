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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Collections
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
name|configuration
operator|.
name|server
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
name|conn
operator|.
name|DataSourceInfo
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
name|util
operator|.
name|XMLEncoder
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
name|util
operator|.
name|XMLSerializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * A descriptor of {@link DataNode} configuration.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DataNodeDescriptor
implements|implements
name|ConfigurationNode
implements|,
name|XMLSerializable
implements|,
name|Serializable
implements|,
name|Comparable
argument_list|<
name|DataNodeDescriptor
argument_list|>
block|{
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|String
argument_list|>
name|dataMapNames
decl_stmt|;
specifier|protected
name|String
name|parameters
decl_stmt|;
specifier|protected
name|String
name|adapterType
decl_stmt|;
specifier|protected
name|String
name|dataSourceFactoryType
decl_stmt|;
specifier|protected
name|String
name|schemaUpdateStrategyType
decl_stmt|;
comment|// TODO: andrus, 12.13.2009: replace funky DataSourceInfo with a cleaner new class
comment|// (DataSourceDescriptor?)
specifier|protected
name|DataSourceInfo
name|dataSourceDescriptor
decl_stmt|;
specifier|protected
name|Resource
name|configurationSource
decl_stmt|;
comment|/**      * @since 3.1      */
specifier|protected
name|DataChannelDescriptor
name|dataChannelDescriptor
decl_stmt|;
specifier|public
name|DataNodeDescriptor
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DataNodeDescriptor
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|dataMapNames
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * @since 3.1      */
specifier|public
name|DataChannelDescriptor
name|getDataChannelDescriptor
parameter_list|()
block|{
return|return
name|dataChannelDescriptor
return|;
block|}
comment|/**      * @since 3.1      */
specifier|public
name|void
name|setDataChannelDescriptor
parameter_list|(
name|DataChannelDescriptor
name|dataChannelDescriptor
parameter_list|)
block|{
name|this
operator|.
name|dataChannelDescriptor
operator|=
name|dataChannelDescriptor
expr_stmt|;
block|}
specifier|public
name|int
name|compareTo
parameter_list|(
name|DataNodeDescriptor
name|o
parameter_list|)
block|{
name|String
name|o1
init|=
name|getName
argument_list|()
decl_stmt|;
name|String
name|o2
init|=
name|o
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|o1
operator|==
literal|null
condition|)
block|{
return|return
operator|(
name|o2
operator|!=
literal|null
operator|)
condition|?
operator|-
literal|1
else|:
literal|0
return|;
block|}
if|else if
condition|(
name|o2
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
else|else
block|{
return|return
name|o1
operator|.
name|compareTo
argument_list|(
name|o2
argument_list|)
return|;
block|}
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|acceptVisitor
parameter_list|(
name|ConfigurationNodeVisitor
argument_list|<
name|T
argument_list|>
name|visitor
parameter_list|)
block|{
return|return
name|visitor
operator|.
name|visitDataNodeDescriptor
argument_list|(
name|this
argument_list|)
return|;
block|}
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<node"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|printlnAttribute
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|indent
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|printlnAttribute
argument_list|(
literal|"adapter"
argument_list|,
name|adapterType
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|printlnAttribute
argument_list|(
literal|"factory"
argument_list|,
name|dataSourceFactoryType
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|printlnAttribute
argument_list|(
literal|"parameters"
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|printlnAttribute
argument_list|(
literal|"schema-update-strategy"
argument_list|,
name|schemaUpdateStrategyType
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dataMapNames
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|dataMapNames
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|names
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|mapName
range|:
name|names
control|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<map-ref"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|printAttribute
argument_list|(
literal|"name"
argument_list|,
name|mapName
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"/>"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|dataSourceDescriptor
operator|!=
literal|null
condition|)
block|{
name|dataSourceDescriptor
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|indent
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"</node>"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getDataMapNames
parameter_list|()
block|{
return|return
name|dataMapNames
return|;
block|}
comment|/**      * Returns extra DataNodeDescriptor parameters. This property is often used by custom      * {@link DataSourceFactory} to configure a DataSource. E.g. JNDIDataSoirceFactory may      * treat parameters String as a JNDI location of the DataSource, etc.      */
specifier|public
name|String
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
comment|/**      * Sets extra DataNodeDescriptor parameters. This property is often used by custom      * {@link DataSourceFactory} to configure a DataSource. E.g. JNDIDataSoirceFactory may      * treat parameters String as a JNDI location of the DataSource, etc.      */
specifier|public
name|void
name|setParameters
parameter_list|(
name|String
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
specifier|public
name|String
name|getAdapterType
parameter_list|()
block|{
return|return
name|adapterType
return|;
block|}
specifier|public
name|void
name|setAdapterType
parameter_list|(
name|String
name|adapter
parameter_list|)
block|{
name|this
operator|.
name|adapterType
operator|=
name|adapter
expr_stmt|;
block|}
specifier|public
name|String
name|getDataSourceFactoryType
parameter_list|()
block|{
return|return
name|dataSourceFactoryType
return|;
block|}
specifier|public
name|void
name|setDataSourceFactoryType
parameter_list|(
name|String
name|dataSourceFactory
parameter_list|)
block|{
name|this
operator|.
name|dataSourceFactoryType
operator|=
name|dataSourceFactory
expr_stmt|;
block|}
specifier|public
name|String
name|getSchemaUpdateStrategyType
parameter_list|()
block|{
return|return
name|schemaUpdateStrategyType
return|;
block|}
specifier|public
name|void
name|setSchemaUpdateStrategyType
parameter_list|(
name|String
name|schemaUpdateStrategyClass
parameter_list|)
block|{
name|this
operator|.
name|schemaUpdateStrategyType
operator|=
name|schemaUpdateStrategyClass
expr_stmt|;
block|}
specifier|public
name|DataSourceInfo
name|getDataSourceDescriptor
parameter_list|()
block|{
return|return
name|dataSourceDescriptor
return|;
block|}
specifier|public
name|void
name|setDataSourceDescriptor
parameter_list|(
name|DataSourceInfo
name|dataSourceDescriptor
parameter_list|)
block|{
name|this
operator|.
name|dataSourceDescriptor
operator|=
name|dataSourceDescriptor
expr_stmt|;
block|}
comment|/**      * Returns configuration resource for this descriptor. Configuration is usually shared      * with the parent {@link DataChannelDescriptor}.      */
specifier|public
name|Resource
name|getConfigurationSource
parameter_list|()
block|{
return|return
name|configurationSource
return|;
block|}
comment|/**      * Sets configuration resource for this descriptor. Configuration is usually shared      * with the parent {@link DataChannelDescriptor} and has to be synchronized when it      * changes in the parent.      */
specifier|public
name|void
name|setConfigurationSource
parameter_list|(
name|Resource
name|configurationResource
parameter_list|)
block|{
name|this
operator|.
name|configurationSource
operator|=
name|configurationResource
expr_stmt|;
block|}
block|}
end_class

end_unit

