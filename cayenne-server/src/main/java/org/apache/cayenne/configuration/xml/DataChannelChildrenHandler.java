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
name|xml
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
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|ContentHandler
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|final
class|class
name|DataChannelChildrenHandler
extends|extends
name|SAXNestedTagHandler
block|{
specifier|private
specifier|static
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|XMLDataChannelDescriptorLoader
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|OLD_MAP_TAG
init|=
literal|"map"
decl_stmt|;
specifier|static
specifier|final
name|String
name|NODE_TAG
init|=
literal|"node"
decl_stmt|;
specifier|static
specifier|final
name|String
name|PROPERTY_TAG
init|=
literal|"property"
decl_stmt|;
specifier|static
specifier|final
name|String
name|DATA_MAP_TAG
init|=
literal|"data-map"
decl_stmt|;
specifier|private
name|XMLDataChannelDescriptorLoader
name|xmlDataChannelDescriptorLoader
decl_stmt|;
specifier|private
name|DataChannelDescriptor
name|descriptor
decl_stmt|;
specifier|private
name|DataNodeDescriptor
name|nodeDescriptor
decl_stmt|;
name|DataChannelChildrenHandler
parameter_list|(
name|XMLDataChannelDescriptorLoader
name|xmlDataChannelDescriptorLoader
parameter_list|,
name|DataChannelHandler
name|parentHandler
parameter_list|)
block|{
name|super
argument_list|(
name|parentHandler
argument_list|)
expr_stmt|;
name|this
operator|.
name|xmlDataChannelDescriptorLoader
operator|=
name|xmlDataChannelDescriptorLoader
expr_stmt|;
name|this
operator|.
name|descriptor
operator|=
name|parentHandler
operator|.
name|descriptor
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|processElement
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
block|{
switch|switch
condition|(
name|localName
condition|)
block|{
case|case
name|PROPERTY_TAG
case|:
name|addProperty
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
case|case
name|OLD_MAP_TAG
case|:
name|addMap
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
case|case
name|NODE_TAG
case|:
name|addNode
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|protected
name|ContentHandler
name|createChildTagHandler
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|name
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
block|{
if|if
condition|(
name|NODE_TAG
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
name|nodeDescriptor
operator|=
operator|new
name|DataNodeDescriptor
argument_list|()
expr_stmt|;
return|return
operator|new
name|DataNodeChildrenHandler
argument_list|(
name|xmlDataChannelDescriptorLoader
argument_list|,
name|this
argument_list|,
name|nodeDescriptor
argument_list|)
return|;
block|}
if|if
condition|(
name|DATA_MAP_TAG
operator|.
name|equals
argument_list|(
name|localName
argument_list|)
condition|)
block|{
return|return
operator|new
name|DataMapHandler
argument_list|(
name|loaderContext
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|createChildTagHandler
argument_list|(
name|namespaceURI
argument_list|,
name|localName
argument_list|,
name|name
argument_list|,
name|attributes
argument_list|)
return|;
block|}
specifier|private
name|void
name|addProperty
parameter_list|(
name|Attributes
name|attributes
parameter_list|)
block|{
name|String
name|key
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"value"
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
name|value
operator|!=
literal|null
condition|)
block|{
name|descriptor
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addMap
parameter_list|(
name|Attributes
name|attributes
parameter_list|)
block|{
name|String
name|dataMapName
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|Resource
name|baseResource
init|=
name|descriptor
operator|.
name|getConfigurationSource
argument_list|()
decl_stmt|;
name|String
name|dataMapLocation
init|=
name|xmlDataChannelDescriptorLoader
operator|.
name|nameMapper
operator|.
name|configurationLocation
argument_list|(
name|DataMap
operator|.
name|class
argument_list|,
name|dataMapName
argument_list|)
decl_stmt|;
name|Resource
name|dataMapResource
init|=
name|baseResource
operator|.
name|getRelativeResource
argument_list|(
name|dataMapLocation
argument_list|)
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Loading XML DataMap resource from "
operator|+
name|dataMapResource
operator|.
name|getURL
argument_list|()
argument_list|)
expr_stmt|;
name|DataMap
name|dataMap
init|=
name|xmlDataChannelDescriptorLoader
operator|.
name|dataMapLoader
operator|.
name|load
argument_list|(
name|dataMapResource
argument_list|)
decl_stmt|;
name|dataMap
operator|.
name|setName
argument_list|(
name|dataMapName
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setLocation
argument_list|(
name|dataMapLocation
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setConfigurationSource
argument_list|(
name|dataMapResource
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setDataChannelDescriptor
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
name|descriptor
operator|.
name|getDataMaps
argument_list|()
operator|.
name|add
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addNode
parameter_list|(
name|Attributes
name|attributes
parameter_list|)
block|{
name|String
name|nodeName
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodeName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"Error:<node> without 'name'."
argument_list|)
throw|;
block|}
name|nodeDescriptor
operator|.
name|setConfigurationSource
argument_list|(
name|descriptor
operator|.
name|getConfigurationSource
argument_list|()
argument_list|)
expr_stmt|;
name|nodeDescriptor
operator|.
name|setName
argument_list|(
name|nodeName
argument_list|)
expr_stmt|;
name|nodeDescriptor
operator|.
name|setAdapterType
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"adapter"
argument_list|)
argument_list|)
expr_stmt|;
name|nodeDescriptor
operator|.
name|setParameters
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"parameters"
argument_list|)
argument_list|)
expr_stmt|;
name|nodeDescriptor
operator|.
name|setDataSourceFactoryType
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"factory"
argument_list|)
argument_list|)
expr_stmt|;
name|nodeDescriptor
operator|.
name|setSchemaUpdateStrategyType
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"schema-update-strategy"
argument_list|)
argument_list|)
expr_stmt|;
name|nodeDescriptor
operator|.
name|setDataChannelDescriptor
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
name|descriptor
operator|.
name|getNodeDescriptors
argument_list|()
operator|.
name|add
argument_list|(
name|nodeDescriptor
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

