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
name|ConfigurationNameMapper
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
name|DataMapLoader
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
name|util
operator|.
name|Util
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
name|slf4j
operator|.
name|Logger
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
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_comment
comment|/**  * @since 3.1  * @since 4.1 moved from org.apache.cayenne.configuration package  */
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
specifier|final
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
comment|/** 	 * Versions of project XML files that this loader can read. 	 */
specifier|static
specifier|final
name|String
index|[]
name|SUPPORTED_PROJECT_VERSIONS
init|=
block|{
literal|"11"
block|}
decl_stmt|;
static|static
block|{
name|Arrays
operator|.
name|sort
argument_list|(
name|SUPPORTED_PROJECT_VERSIONS
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * @deprecated the caller should use password resolving strategy instead of 	 *             resolving the password on the spot. For one thing this can be 	 *             used in the Modeler and no password may be available. 	 */
annotation|@
name|Deprecated
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
operator|.
name|getMessage
argument_list|()
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
return|return
name|password
return|;
block|}
comment|/** 	 * @deprecated the caller should use password resolving strategy instead of 	 *             resolving the password on the spot. For one thing this can be 	 *             used in the Modeler and no password may be available. 	 */
annotation|@
name|Deprecated
specifier|static
name|String
name|passwordFromInputStream
parameter_list|(
name|InputStream
name|inputStream
parameter_list|)
block|{
name|String
name|password
init|=
literal|null
decl_stmt|;
try|try
init|(
name|BufferedReader
name|bufferedReader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|inputStream
argument_list|)
argument_list|)
init|;
init|)
block|{
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
operator|.
name|getMessage
argument_list|()
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
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
name|ignored
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
name|Provider
argument_list|<
name|XMLReader
argument_list|>
name|xmlReaderProvider
decl_stmt|;
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
annotation|@
name|Inject
specifier|protected
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|HandlerFactory
name|handlerFactory
decl_stmt|;
annotation|@
name|Override
specifier|public
name|ConfigurationTree
argument_list|<
name|DataChannelDescriptor
argument_list|>
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
name|logger
operator|.
name|info
argument_list|(
literal|"Loading XML configuration resource from "
operator|+
name|configurationURL
argument_list|)
expr_stmt|;
specifier|final
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
try|try
init|(
name|InputStream
name|in
init|=
name|configurationURL
operator|.
name|openStream
argument_list|()
init|)
block|{
name|XMLReader
name|parser
init|=
name|xmlReaderProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|LoaderContext
name|loaderContext
init|=
operator|new
name|LoaderContext
argument_list|(
name|parser
argument_list|,
name|handlerFactory
argument_list|)
decl_stmt|;
name|loaderContext
operator|.
name|addDataMapListener
argument_list|(
name|dataMap
lambda|->
name|descriptor
operator|.
name|getDataMaps
argument_list|()
operator|.
name|add
argument_list|(
name|dataMap
argument_list|)
argument_list|)
expr_stmt|;
name|DataChannelHandler
name|rootHandler
init|=
operator|new
name|DataChannelHandler
argument_list|(
name|this
argument_list|,
name|descriptor
argument_list|,
name|loaderContext
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
name|InputSource
name|input
init|=
operator|new
name|InputSource
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|input
operator|.
name|setSystemId
argument_list|(
name|configurationURL
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|loaderContext
operator|.
name|dataChannelLoaded
argument_list|(
name|descriptor
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
comment|// TODO: andrus 03/10/2010 - actually provide load failures here...
return|return
operator|new
name|ConfigurationTree
argument_list|<>
argument_list|(
name|descriptor
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
end_class

end_unit

