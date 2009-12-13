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
name|resource
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * A descriptor of a {@link DataNode}, normally loaded from XML.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DataNodeDescriptor
implements|implements
name|ConfigurationNode
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
name|location
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
specifier|protected
name|Resource
name|configurationSource
decl_stmt|;
specifier|public
name|DataNodeDescriptor
parameter_list|()
block|{
name|dataMapNames
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
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
specifier|public
name|String
name|getLocation
parameter_list|()
block|{
return|return
name|location
return|;
block|}
specifier|public
name|void
name|setLocation
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|this
operator|.
name|location
operator|=
name|location
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
name|Resource
name|getConfigurationSource
parameter_list|()
block|{
return|return
name|configurationSource
return|;
block|}
specifier|public
name|void
name|setConfigurationSource
parameter_list|(
name|Resource
name|descriptorResource
parameter_list|)
block|{
name|this
operator|.
name|configurationSource
operator|=
name|descriptorResource
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
block|}
end_class

end_unit

