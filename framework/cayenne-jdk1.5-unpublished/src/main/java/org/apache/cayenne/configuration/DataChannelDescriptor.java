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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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

begin_comment
comment|/**  * A descriptor of a DataChannel normally loaded from XML configuration.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DataChannelDescriptor
implements|implements
name|ConfigurationNode
implements|,
name|XMLSerializable
block|{
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|dataMaps
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|DataNodeDescriptor
argument_list|>
name|nodeDescriptors
decl_stmt|;
specifier|protected
name|Resource
name|configurationSource
decl_stmt|;
specifier|public
name|DataChannelDescriptor
parameter_list|()
block|{
name|properties
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|dataMaps
operator|=
operator|new
name|ArrayList
argument_list|<
name|DataMap
argument_list|>
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|nodeDescriptors
operator|=
operator|new
name|ArrayList
argument_list|<
name|DataNodeDescriptor
argument_list|>
argument_list|(
literal|3
argument_list|)
expr_stmt|;
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
literal|"<domain"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|printProjectVersion
argument_list|()
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|indent
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|boolean
name|breakNeeded
init|=
literal|false
decl_stmt|;
if|if
condition|(
operator|!
name|properties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|breakNeeded
operator|=
literal|true
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|properties
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|keys
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
name|encoder
operator|.
name|printProperty
argument_list|(
name|key
argument_list|,
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|dataMaps
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|breakNeeded
condition|)
block|{
name|encoder
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|breakNeeded
operator|=
literal|true
expr_stmt|;
block|}
name|List
argument_list|<
name|DataMap
argument_list|>
name|maps
init|=
operator|new
name|ArrayList
argument_list|<
name|DataMap
argument_list|>
argument_list|(
name|this
operator|.
name|dataMaps
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|maps
argument_list|)
expr_stmt|;
for|for
control|(
name|DataMap
name|dataMap
range|:
name|maps
control|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<map"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|printAttribute
argument_list|(
literal|"name"
argument_list|,
name|dataMap
operator|.
name|getName
argument_list|()
operator|.
name|trim
argument_list|()
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
operator|!
name|nodeDescriptors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|breakNeeded
condition|)
block|{
name|encoder
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|breakNeeded
operator|=
literal|true
expr_stmt|;
block|}
name|List
argument_list|<
name|DataNodeDescriptor
argument_list|>
name|nodes
init|=
operator|new
name|ArrayList
argument_list|<
name|DataNodeDescriptor
argument_list|>
argument_list|(
name|nodeDescriptors
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|nodes
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|nodes
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
literal|"</domain>"
argument_list|)
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
name|visitDataChannelDescriptor
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|DataMap
argument_list|>
name|getDataMaps
parameter_list|()
block|{
return|return
name|dataMaps
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|DataNodeDescriptor
argument_list|>
name|getNodeDescriptors
parameter_list|()
block|{
return|return
name|nodeDescriptors
return|;
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
name|configurationSource
parameter_list|)
block|{
name|this
operator|.
name|configurationSource
operator|=
name|configurationSource
expr_stmt|;
block|}
block|}
end_class

end_unit

