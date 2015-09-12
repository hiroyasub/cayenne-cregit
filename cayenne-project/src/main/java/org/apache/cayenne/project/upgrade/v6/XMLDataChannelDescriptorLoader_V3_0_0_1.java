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
name|project
operator|.
name|upgrade
operator|.
name|v6
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

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
name|configuration
operator|.
name|SAXNestedTagHandler
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
name|JNDIDataSourceFactory
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
name|XMLPoolingDataSourceFactory
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
name|Util
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

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
name|XMLReader
import|;
end_import

begin_comment
comment|/**  * A loader of Cayenne projects descriptor for version "3.0.0.1".  */
end_comment

begin_class
class|class
name|XMLDataChannelDescriptorLoader_V3_0_0_1
block|{
specifier|static
specifier|final
name|String
name|DBCP_DATA_SOURCE_FACTORY
init|=
literal|"org.apache.cayenne.configuration.server.DBCPDataSourceFactory"
decl_stmt|;
specifier|private
specifier|static
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|XMLDataChannelDescriptorLoader_V3_0_0_1
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|DOMAINS_TAG
init|=
literal|"domains"
decl_stmt|;
specifier|static
specifier|final
name|String
name|DOMAIN_TAG
init|=
literal|"domain"
decl_stmt|;
specifier|static
specifier|final
name|String
name|MAP_TAG
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
name|MAP_REF_TAG
init|=
literal|"map-ref"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|dataSourceFactoryLegacyNameMapping
decl_stmt|;
static|static
block|{
name|dataSourceFactoryLegacyNameMapping
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
name|dataSourceFactoryLegacyNameMapping
operator|.
name|put
argument_list|(
literal|"org.apache.cayenne.conf.DriverDataSourceFactory"
argument_list|,
name|XMLPoolingDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|dataSourceFactoryLegacyNameMapping
operator|.
name|put
argument_list|(
literal|"org.apache.cayenne.conf.JNDIDataSourceFactory"
argument_list|,
name|JNDIDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|dataSourceFactoryLegacyNameMapping
operator|.
name|put
argument_list|(
literal|"org.apache.cayenne.conf.DBCPDataSourceFactory"
argument_list|,
name|DBCP_DATA_SOURCE_FACTORY
argument_list|)
expr_stmt|;
block|}
comment|// implementation is statically typed and is intentionally not DI-provided
specifier|protected
name|XMLDataMapLoader_V3_0_0_1
name|mapLoader
decl_stmt|;
specifier|protected
name|XMLDataSourceInfoLoader_V3_0_0_1
name|dataSourceInfoLoader
decl_stmt|;
name|XMLDataChannelDescriptorLoader_V3_0_0_1
parameter_list|()
block|{
name|mapLoader
operator|=
operator|new
name|XMLDataMapLoader_V3_0_0_1
argument_list|()
expr_stmt|;
name|dataSourceInfoLoader
operator|=
operator|new
name|XMLDataSourceInfoLoader_V3_0_0_1
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|load
parameter_list|(
name|Resource
name|configurationSource
parameter_list|)
throws|throws
name|ConfigurationException
block|{
if|if
condition|(
name|configurationSource
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null configurationSource"
argument_list|)
throw|;
block|}
name|URL
name|configurationURL
init|=
name|configurationSource
operator|.
name|getURL
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|domains
init|=
operator|new
name|ArrayList
argument_list|<
name|DataChannelDescriptor
argument_list|>
argument_list|()
decl_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|configurationURL
operator|.
name|openStream
argument_list|()
init|;
init|)
block|{
name|XMLReader
name|parser
init|=
name|Util
operator|.
name|createXmlReader
argument_list|()
decl_stmt|;
name|DomainsHandler
name|rootHandler
init|=
operator|new
name|DomainsHandler
argument_list|(
name|configurationSource
argument_list|,
name|domains
argument_list|,
name|parser
argument_list|)
decl_stmt|;
name|parser
operator|.
name|setContentHandler
argument_list|(
name|rootHandler
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setErrorHandler
argument_list|(
name|rootHandler
argument_list|)
expr_stmt|;
name|parser
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
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
literal|"Error loading configuration from %s"
argument_list|,
name|e
argument_list|,
name|configurationURL
argument_list|)
throw|;
block|}
return|return
name|domains
return|;
block|}
comment|/** 	 * Make sure the domain name is only made up of Java-identifier-safe 	 * characters. 	 */
specifier|protected
name|String
name|scrubDomainName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|name
return|;
block|}
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
name|name
operator|.
name|length
argument_list|()
argument_list|)
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
name|name
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|name
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|==
literal|0
operator|&&
operator|!
name|Character
operator|.
name|isJavaIdentifierStart
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'_'
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|i
operator|>
literal|0
operator|&&
operator|!
name|Character
operator|.
name|isJavaIdentifierPart
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'_'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/** 	 * Converts the names of standard Cayenne-supplied DataSourceFactories from 	 * the legacy names to the current names. 	 */
specifier|private
name|String
name|convertDataSourceFactory
parameter_list|(
name|String
name|dataSourceFactory
parameter_list|)
block|{
if|if
condition|(
name|dataSourceFactory
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|converted
init|=
name|dataSourceFactoryLegacyNameMapping
operator|.
name|get
argument_list|(
name|dataSourceFactory
argument_list|)
decl_stmt|;
return|return
name|converted
operator|!=
literal|null
condition|?
name|converted
else|:
name|dataSourceFactory
return|;
block|}
specifier|final
class|class
name|DomainsHandler
extends|extends
name|SAXNestedTagHandler
block|{
specifier|private
name|Collection
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|domains
decl_stmt|;
specifier|private
name|Resource
name|configurationSource
decl_stmt|;
name|DomainsHandler
parameter_list|(
name|Resource
name|configurationSource
parameter_list|,
name|Collection
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|domains
parameter_list|,
name|XMLReader
name|parser
parameter_list|)
block|{
name|super
argument_list|(
name|parser
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|domains
operator|=
name|domains
expr_stmt|;
name|this
operator|.
name|configurationSource
operator|=
name|configurationSource
expr_stmt|;
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
name|qName
parameter_list|,
name|Attributes
name|attributes
parameter_list|)
block|{
if|if
condition|(
name|localName
operator|.
name|equals
argument_list|(
name|DOMAINS_TAG
argument_list|)
condition|)
block|{
return|return
operator|new
name|DomainsChildrenHandler
argument_list|(
name|parser
argument_list|,
name|this
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
name|qName
argument_list|,
name|attributes
argument_list|)
return|;
block|}
block|}
specifier|final
class|class
name|DomainsChildrenHandler
extends|extends
name|SAXNestedTagHandler
block|{
specifier|private
name|Collection
argument_list|<
name|DataChannelDescriptor
argument_list|>
name|domains
decl_stmt|;
specifier|private
name|Resource
name|configurationSource
decl_stmt|;
name|DomainsChildrenHandler
parameter_list|(
name|XMLReader
name|parser
parameter_list|,
name|DomainsHandler
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parser
argument_list|,
name|parent
argument_list|)
expr_stmt|;
name|this
operator|.
name|domains
operator|=
name|parent
operator|.
name|domains
expr_stmt|;
name|this
operator|.
name|configurationSource
operator|=
name|parent
operator|.
name|configurationSource
expr_stmt|;
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
name|localName
operator|.
name|equals
argument_list|(
name|DOMAIN_TAG
argument_list|)
condition|)
block|{
name|String
name|domainName
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
name|DataChannelDescriptor
name|descriptor
init|=
operator|new
name|DataChannelDescriptor
argument_list|()
decl_stmt|;
name|descriptor
operator|.
name|setName
argument_list|(
name|scrubDomainName
argument_list|(
name|domainName
argument_list|)
argument_list|)
expr_stmt|;
name|descriptor
operator|.
name|setConfigurationSource
argument_list|(
name|configurationSource
argument_list|)
expr_stmt|;
name|domains
operator|.
name|add
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
return|return
operator|new
name|DataChannelChildrenHandler
argument_list|(
name|descriptor
argument_list|,
name|parser
argument_list|,
name|this
argument_list|)
return|;
block|}
name|logger
operator|.
name|info
argument_list|(
name|unexpectedTagMessage
argument_list|(
name|localName
argument_list|,
name|DOMAIN_TAG
argument_list|)
argument_list|)
expr_stmt|;
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
block|}
specifier|final
class|class
name|DataChannelChildrenHandler
extends|extends
name|SAXNestedTagHandler
block|{
specifier|private
name|DataChannelDescriptor
name|descriptor
decl_stmt|;
name|DataChannelChildrenHandler
parameter_list|(
name|DataChannelDescriptor
name|descriptor
parameter_list|,
name|XMLReader
name|parser
parameter_list|,
name|DomainsChildrenHandler
name|parentHandler
parameter_list|)
block|{
name|super
argument_list|(
name|parser
argument_list|,
name|parentHandler
argument_list|)
expr_stmt|;
name|this
operator|.
name|descriptor
operator|=
name|descriptor
expr_stmt|;
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
name|localName
operator|.
name|equals
argument_list|(
name|PROPERTY_TAG
argument_list|)
condition|)
block|{
name|String
name|key
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
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
literal|""
argument_list|,
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
if|else if
condition|(
name|localName
operator|.
name|equals
argument_list|(
name|MAP_TAG
argument_list|)
condition|)
block|{
name|String
name|dataMapName
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
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
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
literal|"location"
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
name|DataMap
name|dataMap
init|=
name|mapLoader
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
if|else if
condition|(
name|localName
operator|.
name|equals
argument_list|(
name|NODE_TAG
argument_list|)
condition|)
block|{
name|String
name|nodeName
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
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
comment|// TODO: assign dummy name?
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"Error:<node> without 'name'."
argument_list|)
throw|;
block|}
name|DataNodeDescriptor
name|nodeDescriptor
init|=
operator|new
name|DataNodeDescriptor
argument_list|()
decl_stmt|;
name|nodeDescriptor
operator|.
name|setName
argument_list|(
name|nodeName
argument_list|)
expr_stmt|;
name|String
name|dataSourceFactory
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
literal|"factory"
argument_list|)
decl_stmt|;
name|String
name|dataSourceFactory6
init|=
name|convertDataSourceFactory
argument_list|(
name|dataSourceFactory
argument_list|)
decl_stmt|;
name|nodeDescriptor
operator|.
name|setDataSourceFactoryType
argument_list|(
name|dataSourceFactory6
argument_list|)
expr_stmt|;
comment|// depending on the factory, "datasource" attribute is
comment|// interpreted
comment|// differently
name|String
name|datasource
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
literal|"datasource"
argument_list|)
decl_stmt|;
if|if
condition|(
name|XMLPoolingDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|dataSourceFactory6
argument_list|)
condition|)
block|{
name|Resource
name|baseResource
init|=
name|descriptor
operator|.
name|getConfigurationSource
argument_list|()
decl_stmt|;
name|Resource
name|dataNodeResource
init|=
name|baseResource
operator|.
name|getRelativeResource
argument_list|(
name|datasource
argument_list|)
decl_stmt|;
name|nodeDescriptor
operator|.
name|setConfigurationSource
argument_list|(
name|dataNodeResource
argument_list|)
expr_stmt|;
name|DataSourceInfo
name|dataSourceInfo
init|=
name|dataSourceInfoLoader
operator|.
name|load
argument_list|(
name|dataNodeResource
argument_list|)
decl_stmt|;
name|nodeDescriptor
operator|.
name|setDataSourceDescriptor
argument_list|(
name|dataSourceInfo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|nodeDescriptor
operator|.
name|setParameters
argument_list|(
name|datasource
argument_list|)
expr_stmt|;
block|}
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
name|nodeDescriptor
operator|.
name|setAdapterType
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
literal|"adapter"
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
literal|""
argument_list|,
literal|"schema-update-strategy"
argument_list|)
argument_list|)
expr_stmt|;
return|return
operator|new
name|DataNodeChildrenHandler
argument_list|(
name|parser
argument_list|,
name|this
argument_list|,
name|nodeDescriptor
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
block|}
specifier|final
class|class
name|DataNodeChildrenHandler
extends|extends
name|SAXNestedTagHandler
block|{
specifier|private
name|DataNodeDescriptor
name|nodeDescriptor
decl_stmt|;
name|DataNodeChildrenHandler
parameter_list|(
name|XMLReader
name|parser
parameter_list|,
name|SAXNestedTagHandler
name|parentHandler
parameter_list|,
name|DataNodeDescriptor
name|nodeDescriptor
parameter_list|)
block|{
name|super
argument_list|(
name|parser
argument_list|,
name|parentHandler
argument_list|)
expr_stmt|;
name|this
operator|.
name|nodeDescriptor
operator|=
name|nodeDescriptor
expr_stmt|;
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
name|localName
operator|.
name|equals
argument_list|(
name|MAP_REF_TAG
argument_list|)
condition|)
block|{
name|String
name|mapName
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
literal|"name"
argument_list|)
decl_stmt|;
name|nodeDescriptor
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|add
argument_list|(
name|mapName
argument_list|)
expr_stmt|;
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
block|}
block|}
end_class

end_unit

