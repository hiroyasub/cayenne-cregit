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
name|access
operator|.
name|ConnectionLogger
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
name|QueryLogger
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
name|conn
operator|.
name|PoolManager
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
comment|/**  * A {@link DataSourceFactory} that loads JDBC connection information from an XML resource  * associated with the DataNodeDescriptor, returning a DataSource with simple connection  * pooling.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|XMLPoolingDataSourceFactory
implements|implements
name|DataSourceFactory
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|XMLPoolingDataSourceFactory
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
specifier|public
name|DataSource
name|getDataSource
parameter_list|(
name|DataNodeDescriptor
name|nodeDescriptor
parameter_list|)
throws|throws
name|Exception
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"loading driver information from '"
operator|+
name|nodeDescriptor
operator|.
name|getLocation
argument_list|()
operator|+
literal|"'."
argument_list|)
expr_stmt|;
name|Resource
name|resource
init|=
name|nodeDescriptor
operator|.
name|getConfigurationSource
argument_list|()
decl_stmt|;
if|if
condition|(
name|resource
operator|==
literal|null
condition|)
block|{
name|String
name|message
init|=
literal|"Null configuration resource for node descriptor with location '"
operator|+
name|nodeDescriptor
operator|.
name|getLocation
argument_list|()
operator|+
literal|"'"
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ConfigurationException
argument_list|(
name|message
argument_list|)
throw|;
block|}
name|DataSourceInfo
name|dataSourceDescriptor
init|=
operator|new
name|DataSourceInfo
argument_list|()
decl_stmt|;
name|XMLReader
name|parser
init|=
name|Util
operator|.
name|createXmlReader
argument_list|()
decl_stmt|;
name|DriverHandler
name|handler
init|=
operator|new
name|DriverHandler
argument_list|(
name|dataSourceDescriptor
argument_list|,
name|parser
argument_list|)
decl_stmt|;
name|parser
operator|.
name|setContentHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|parser
operator|.
name|setErrorHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|parser
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
name|resource
operator|.
name|getURL
argument_list|()
operator|.
name|openStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|createDataSource
argument_list|(
name|dataSourceDescriptor
argument_list|)
return|;
block|}
comment|/**      * Returns an instance of {@link PoolManager}.      */
specifier|protected
name|DataSource
name|createDataSource
parameter_list|(
name|DataSourceInfo
name|dataSourceDescriptor
parameter_list|)
throws|throws
name|Exception
block|{
name|ConnectionLogger
name|logger
init|=
operator|new
name|ConnectionLogger
argument_list|()
decl_stmt|;
try|try
block|{
return|return
operator|new
name|PoolManager
argument_list|(
name|dataSourceDescriptor
operator|.
name|getJdbcDriver
argument_list|()
argument_list|,
name|dataSourceDescriptor
operator|.
name|getDataSourceUrl
argument_list|()
argument_list|,
name|dataSourceDescriptor
operator|.
name|getMinConnections
argument_list|()
argument_list|,
name|dataSourceDescriptor
operator|.
name|getMaxConnections
argument_list|()
argument_list|,
name|dataSourceDescriptor
operator|.
name|getUserName
argument_list|()
argument_list|,
name|dataSourceDescriptor
operator|.
name|getPassword
argument_list|()
argument_list|,
name|logger
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|QueryLogger
operator|.
name|logConnectFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
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
specifier|private
class|class
name|DriverHandler
extends|extends
name|SAXNestedTagHandler
block|{
specifier|private
name|DataSourceInfo
name|dataSourceDescriptor
decl_stmt|;
name|DriverHandler
parameter_list|(
name|DataSourceInfo
name|dataSourceDescriptor
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
literal|"class"
argument_list|)
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"loading driver "
operator|+
name|className
argument_list|)
expr_stmt|;
name|dataSourceDescriptor
operator|.
name|setJdbcDriver
argument_list|(
name|className
argument_list|)
expr_stmt|;
return|return
operator|new
name|DriverChildrenHandler
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
name|name
argument_list|,
name|attributes
argument_list|)
return|;
block|}
block|}
specifier|private
class|class
name|DriverChildrenHandler
extends|extends
name|SAXNestedTagHandler
block|{
specifier|private
name|DataSourceInfo
name|dataSourceDescriptor
decl_stmt|;
name|DriverChildrenHandler
parameter_list|(
name|XMLReader
name|parser
parameter_list|,
name|DriverHandler
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
name|dataSourceDescriptor
operator|=
name|parentHandler
operator|.
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
name|Collection
argument_list|<
name|Resource
argument_list|>
name|urls
init|=
name|resourceLocator
operator|.
name|findResources
argument_list|(
name|passwordLocation
argument_list|)
decl_stmt|;
if|if
condition|(
name|urls
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|password
operator|=
name|passwordFromURL
argument_list|(
name|urls
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getURL
argument_list|()
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

