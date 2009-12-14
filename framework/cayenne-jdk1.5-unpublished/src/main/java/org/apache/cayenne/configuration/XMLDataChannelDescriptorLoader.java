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
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|HashMap
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
name|conf
operator|.
name|PasswordEncoding
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
comment|/**  * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|XMLDataChannelDescriptorLoader
implements|implements
name|DataChannelDescriptorLoader
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
name|XMLDataChannelDescriptorLoader
operator|.
name|class
argument_list|)
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
specifier|static
specifier|final
name|String
name|DATA_SOURCE_TAG
init|=
literal|"data-source"
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
name|DBCPDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated the caller should use password resolving strategy instead of resolving      *             the password on the spot. For one thing this can be used in the Modeler      *             and no password may be available.      */
specifier|private
specifier|static
name|String
name|passwordFromURL
parameter_list|(
name|URL
name|url
parameter_list|)
block|{
name|InputStream
name|inputStream
init|=
literal|null
decl_stmt|;
name|String
name|password
init|=
literal|null
decl_stmt|;
try|try
block|{
name|inputStream
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|password
operator|=
name|passwordFromInputStream
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
comment|// Log the error while trying to open the stream. A null
comment|// password will be returned as a result.
name|logger
operator|.
name|warn
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
return|return
name|password
return|;
block|}
comment|/**      * @deprecated the caller should use password resolving strategy instead of resolving      *             the password on the spot. For one thing this can be used in the Modeler      *             and no password may be available.      */
specifier|private
specifier|static
name|String
name|passwordFromInputStream
parameter_list|(
name|InputStream
name|inputStream
parameter_list|)
block|{
name|BufferedReader
name|bufferedReader
init|=
literal|null
decl_stmt|;
name|String
name|password
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bufferedReader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|inputStream
argument_list|)
argument_list|)
expr_stmt|;
name|password
operator|=
name|bufferedReader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
if|if
condition|(
name|bufferedReader
operator|!=
literal|null
condition|)
block|{
name|bufferedReader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
block|}
try|try
block|{
name|inputStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
block|}
block|}
return|return
name|password
return|;
block|}
annotation|@
name|Inject
specifier|protected
name|DataMapLoader
name|dataMapLoader
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ConfigurationNameMapper
name|nameMapper
decl_stmt|;
specifier|public
name|DataChannelDescriptor
name|load
parameter_list|(
name|Resource
name|configurationResource
parameter_list|)
throws|throws
name|ConfigurationException
block|{
if|if
condition|(
name|configurationResource
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null configurationResource"
argument_list|)
throw|;
block|}
name|URL
name|configurationURL
init|=
name|configurationResource
operator|.
name|getURL
argument_list|()
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
name|setConfigurationSource
argument_list|(
name|configurationResource
argument_list|)
expr_stmt|;
name|descriptor
operator|.
name|setName
argument_list|(
name|nameMapper
operator|.
name|configurationNodeName
argument_list|(
name|DataChannelDescriptor
operator|.
name|class
argument_list|,
name|configurationResource
argument_list|)
argument_list|)
expr_stmt|;
name|DataChannelHandler
name|rootHandler
decl_stmt|;
name|InputStream
name|in
init|=
literal|null
decl_stmt|;
try|try
block|{
name|in
operator|=
name|configurationURL
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|XMLReader
name|parser
init|=
name|Util
operator|.
name|createXmlReader
argument_list|()
decl_stmt|;
name|rootHandler
operator|=
operator|new
name|DataChannelHandler
argument_list|(
name|descriptor
argument_list|,
name|parser
argument_list|)
expr_stmt|;
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
finally|finally
block|{
try|try
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioex
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"failure closing input stream for "
operator|+
name|configurationURL
operator|+
literal|", ignoring"
argument_list|,
name|ioex
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|descriptor
return|;
block|}
comment|/**      * Converts the names of standard Cayenne-supplied DataSourceFactories from the legacy      * names to the current names.      */
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
name|DataChannelHandler
extends|extends
name|SAXNestedTagHandler
block|{
specifier|private
name|DataChannelDescriptor
name|descriptor
decl_stmt|;
name|DataChannelHandler
parameter_list|(
name|DataChannelDescriptor
name|dataChannelDescriptor
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
name|descriptor
operator|=
name|dataChannelDescriptor
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
name|version
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
literal|"project-version"
argument_list|)
decl_stmt|;
name|descriptor
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
return|return
operator|new
name|DataChannelChildrenHandler
argument_list|(
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
name|XMLReader
name|parser
parameter_list|,
name|DataChannelHandler
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
name|parentHandler
operator|.
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
name|DataMap
name|dataMap
init|=
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
name|setConfigurationSource
argument_list|(
name|descriptor
operator|.
name|getConfigurationSource
argument_list|()
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
literal|""
argument_list|,
literal|"adapter"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|parameters
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|""
argument_list|,
literal|"parameters"
argument_list|)
decl_stmt|;
name|nodeDescriptor
operator|.
name|setParameters
argument_list|(
name|parameters
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
name|nodeDescriptor
operator|.
name|setDataSourceFactoryType
argument_list|(
name|convertDataSourceFactory
argument_list|(
name|dataSourceFactory
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
if|else if
condition|(
name|localName
operator|.
name|equals
argument_list|(
name|DATA_SOURCE_TAG
argument_list|)
condition|)
block|{
name|DataSourceInfo
name|dataSourceDescriptor
init|=
operator|new
name|DataSourceInfo
argument_list|()
decl_stmt|;
name|nodeDescriptor
operator|.
name|setDataSourceDescriptor
argument_list|(
name|dataSourceDescriptor
argument_list|)
expr_stmt|;
return|return
operator|new
name|DataSourceChildrenHandler
argument_list|(
name|parser
argument_list|,
name|this
argument_list|,
name|dataSourceDescriptor
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
class|class
name|DataSourceChildrenHandler
extends|extends
name|SAXNestedTagHandler
block|{
specifier|private
name|DataSourceInfo
name|dataSourceDescriptor
decl_stmt|;
name|DataSourceChildrenHandler
parameter_list|(
name|XMLReader
name|parser
parameter_list|,
name|DataNodeChildrenHandler
name|parentHandler
parameter_list|,
name|DataSourceInfo
name|dataSourceDescriptor
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
name|dataSourceDescriptor
operator|=
name|dataSourceDescriptor
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
literal|"driver"
argument_list|)
condition|)
block|{
name|String
name|className
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
name|dataSourceDescriptor
operator|.
name|setJdbcDriver
argument_list|(
name|className
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|localName
operator|.
name|equals
argument_list|(
literal|"login"
argument_list|)
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"loading user name and password."
argument_list|)
expr_stmt|;
name|String
name|encoderClass
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"encoderClass"
argument_list|)
decl_stmt|;
name|String
name|encoderKey
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"encoderKey"
argument_list|)
decl_stmt|;
if|if
condition|(
name|encoderKey
operator|==
literal|null
condition|)
block|{
name|encoderKey
operator|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"encoderSalt"
argument_list|)
expr_stmt|;
block|}
name|String
name|password
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"password"
argument_list|)
decl_stmt|;
name|String
name|passwordLocation
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"passwordLocation"
argument_list|)
decl_stmt|;
name|String
name|passwordSource
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"passwordSource"
argument_list|)
decl_stmt|;
if|if
condition|(
name|passwordSource
operator|==
literal|null
condition|)
block|{
name|passwordSource
operator|=
name|DataSourceInfo
operator|.
name|PASSWORD_LOCATION_MODEL
expr_stmt|;
block|}
name|String
name|username
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"userName"
argument_list|)
decl_stmt|;
name|dataSourceDescriptor
operator|.
name|setPasswordEncoderClass
argument_list|(
name|encoderClass
argument_list|)
expr_stmt|;
name|dataSourceDescriptor
operator|.
name|setPasswordEncoderKey
argument_list|(
name|encoderKey
argument_list|)
expr_stmt|;
name|dataSourceDescriptor
operator|.
name|setPasswordLocation
argument_list|(
name|passwordLocation
argument_list|)
expr_stmt|;
name|dataSourceDescriptor
operator|.
name|setPasswordSource
argument_list|(
name|passwordSource
argument_list|)
expr_stmt|;
name|dataSourceDescriptor
operator|.
name|setUserName
argument_list|(
name|username
argument_list|)
expr_stmt|;
comment|// Replace {} in passwordSource with encoderSalt -- useful for EXECUTABLE
comment|//& URL options
if|if
condition|(
name|encoderKey
operator|!=
literal|null
condition|)
block|{
name|passwordSource
operator|=
name|passwordSource
operator|.
name|replaceAll
argument_list|(
literal|"\\{\\}"
argument_list|,
name|encoderKey
argument_list|)
expr_stmt|;
block|}
name|PasswordEncoding
name|passwordEncoder
init|=
name|dataSourceDescriptor
operator|.
name|getPasswordEncoder
argument_list|()
decl_stmt|;
if|if
condition|(
name|passwordLocation
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|passwordLocation
operator|.
name|equals
argument_list|(
name|DataSourceInfo
operator|.
name|PASSWORD_LOCATION_CLASSPATH
argument_list|)
condition|)
block|{
name|ClassLoader
name|classLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|URL
name|url
init|=
name|classLoader
operator|.
name|getResource
argument_list|(
name|username
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|password
operator|=
name|passwordFromURL
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Could not find resource in CLASSPATH: "
operator|+
name|passwordSource
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|passwordLocation
operator|.
name|equals
argument_list|(
name|DataSourceInfo
operator|.
name|PASSWORD_LOCATION_URL
argument_list|)
condition|)
block|{
try|try
block|{
name|password
operator|=
name|passwordFromURL
argument_list|(
operator|new
name|URL
argument_list|(
name|passwordSource
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|exception
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|passwordLocation
operator|.
name|equals
argument_list|(
name|DataSourceInfo
operator|.
name|PASSWORD_LOCATION_EXECUTABLE
argument_list|)
condition|)
block|{
if|if
condition|(
name|passwordSource
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Process
name|process
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
name|passwordSource
argument_list|)
decl_stmt|;
name|password
operator|=
name|passwordFromInputStream
argument_list|(
name|process
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|process
operator|.
name|waitFor
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|exception
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|password
operator|!=
literal|null
operator|&&
name|passwordEncoder
operator|!=
literal|null
condition|)
block|{
name|dataSourceDescriptor
operator|.
name|setPassword
argument_list|(
name|passwordEncoder
operator|.
name|decodePassword
argument_list|(
name|password
argument_list|,
name|encoderKey
argument_list|)
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
literal|"url"
argument_list|)
condition|)
block|{
name|dataSourceDescriptor
operator|.
name|setDataSourceUrl
argument_list|(
name|attributes
operator|.
name|getValue
argument_list|(
literal|"value"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|localName
operator|.
name|equals
argument_list|(
literal|"connectionPool"
argument_list|)
condition|)
block|{
name|String
name|min
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"min"
argument_list|)
decl_stmt|;
if|if
condition|(
name|min
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|dataSourceDescriptor
operator|.
name|setMinConnections
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|min
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfex
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Non-numeric 'min' attribute"
argument_list|,
name|nfex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"Non-numeric 'min' attribute '%s'"
argument_list|,
name|nfex
argument_list|,
name|min
argument_list|)
throw|;
block|}
block|}
name|String
name|max
init|=
name|attributes
operator|.
name|getValue
argument_list|(
literal|"max"
argument_list|)
decl_stmt|;
if|if
condition|(
name|max
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|dataSourceDescriptor
operator|.
name|setMaxConnections
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|max
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfex
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Non-numeric 'max' attribute"
argument_list|,
name|nfex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"Non-numeric 'max' attribute '%s'"
argument_list|,
name|nfex
argument_list|,
name|max
argument_list|)
throw|;
block|}
block|}
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

