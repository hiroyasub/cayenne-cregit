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
name|conf
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
name|SAXException
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
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|helpers
operator|.
name|DefaultHandler
import|;
end_import

begin_comment
comment|/**  * Creates DataSource objects from XML configuration files that describe a JDBC driver.  * Wraps JDBC driver in a generic DataSource implementation.  *   * @author Andrus Adamchik  */
end_comment

begin_comment
comment|// TODO: factory shouldn't contain any state specific to location ("driverInfo" ivar
end_comment

begin_comment
comment|// should go, and probably "parser" too)... Otherwise the API doesn't make sense -
end_comment

begin_comment
comment|// sequential invocations of getDataSource() will have side effects....
end_comment

begin_class
specifier|public
class|class
name|DriverDataSourceFactory
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
name|DriverDataSourceFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|XMLReader
name|parser
decl_stmt|;
specifier|protected
name|DataSourceInfo
name|driverInfo
decl_stmt|;
specifier|protected
name|Configuration
name|parentConfiguration
decl_stmt|;
comment|/**      * Creates new DriverDataSourceFactory.      */
specifier|public
name|DriverDataSourceFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|parser
operator|=
name|Util
operator|.
name|createXmlReader
argument_list|()
expr_stmt|;
block|}
comment|/**      * Stores configuration object internally to use it later for resource loading.      */
specifier|public
name|void
name|initializeWithParentConfiguration
parameter_list|(
name|Configuration
name|parentConfiguration
parameter_list|)
block|{
name|this
operator|.
name|parentConfiguration
operator|=
name|parentConfiguration
expr_stmt|;
block|}
specifier|public
name|DataSource
name|getDataSource
parameter_list|(
name|String
name|location
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|load
argument_list|(
name|location
argument_list|)
expr_stmt|;
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
name|driverInfo
operator|.
name|getJdbcDriver
argument_list|()
argument_list|,
name|driverInfo
operator|.
name|getDataSourceUrl
argument_list|()
argument_list|,
name|driverInfo
operator|.
name|getMinConnections
argument_list|()
argument_list|,
name|driverInfo
operator|.
name|getMaxConnections
argument_list|()
argument_list|,
name|driverInfo
operator|.
name|getUserName
argument_list|()
argument_list|,
name|driverInfo
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
name|ex
parameter_list|)
block|{
name|QueryLogger
operator|.
name|logConnectFailure
argument_list|(
name|ex
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
comment|/**      * Returns DataSourceInfo property.      */
specifier|protected
name|DataSourceInfo
name|getDriverInfo
parameter_list|()
block|{
return|return
name|this
operator|.
name|driverInfo
return|;
block|}
specifier|protected
name|InputStream
name|getInputStream
parameter_list|(
name|String
name|location
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|parentConfiguration
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"No parent Configuration set - cannot continue."
argument_list|)
throw|;
block|}
return|return
name|this
operator|.
name|parentConfiguration
operator|.
name|getResourceLocator
argument_list|()
operator|.
name|findResourceStream
argument_list|(
name|location
argument_list|)
return|;
block|}
comment|/**      * Loads driver information from the file at<code>location</code>. Called      * internally from "getDataSource"      */
specifier|protected
name|void
name|load
parameter_list|(
name|String
name|location
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
name|location
operator|+
literal|"'."
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|this
operator|.
name|getInputStream
argument_list|(
name|location
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Error: location '"
operator|+
name|location
operator|+
literal|"' not found."
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"Can't find DataSource configuration file at "
operator|+
name|location
argument_list|)
throw|;
block|}
name|RootHandler
name|handler
init|=
operator|new
name|RootHandler
argument_list|()
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
name|in
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// SAX handlers start below
comment|/** Handler for the root element. Its only child must be the "driver" element. */
specifier|private
class|class
name|RootHandler
extends|extends
name|DefaultHandler
block|{
comment|/**          * Handles the start of a "driver" element. A driver handler is created and          * initialized with the element name and attributes.          *           * @exception SAXException if the tag given is not<code>"driver"</code>          */
specifier|public
name|void
name|startElement
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
name|atts
parameter_list|)
throws|throws
name|SAXException
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
operator|new
name|DriverHandler
argument_list|(
name|parser
argument_list|,
name|this
argument_list|)
operator|.
name|init
argument_list|(
name|localName
argument_list|,
name|atts
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"<driver> must be the root element.<"
operator|+
name|localName
operator|+
literal|"> is unexpected."
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"Config file is not of expected XML type. '"
operator|+
name|localName
operator|+
literal|"' unexpected."
argument_list|)
throw|;
block|}
block|}
block|}
comment|/** Handler for the "driver" element. */
specifier|private
class|class
name|DriverHandler
extends|extends
name|AbstractHandler
block|{
specifier|public
name|DriverHandler
parameter_list|(
name|XMLReader
name|parser
parameter_list|,
name|ContentHandler
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
block|}
specifier|public
name|void
name|init
parameter_list|(
name|String
name|name
parameter_list|,
name|Attributes
name|attrs
parameter_list|)
block|{
name|String
name|className
init|=
name|attrs
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
name|driverInfo
operator|=
operator|new
name|DataSourceInfo
argument_list|()
expr_stmt|;
name|driverInfo
operator|.
name|setJdbcDriver
argument_list|(
name|className
argument_list|)
expr_stmt|;
block|}
comment|/**          * Handles the start of a driver child element. An appropriate handler is created          * and initialized with the element name and attributes.          *           * @exception SAXException if the tag given is not recognized.          */
specifier|public
name|void
name|startElement
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
name|atts
parameter_list|)
throws|throws
name|SAXException
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
operator|new
name|LoginHandler
argument_list|(
name|this
operator|.
name|parser
argument_list|,
name|this
argument_list|)
operator|.
name|init
argument_list|(
name|localName
argument_list|,
name|atts
argument_list|,
name|driverInfo
argument_list|)
expr_stmt|;
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
operator|new
name|UrlHandler
argument_list|(
name|this
operator|.
name|parser
argument_list|,
name|this
argument_list|)
operator|.
name|init
argument_list|(
name|localName
argument_list|,
name|atts
argument_list|,
name|driverInfo
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
operator|new
name|ConnectionHandler
argument_list|(
name|this
operator|.
name|parser
argument_list|,
name|this
argument_list|)
operator|.
name|init
argument_list|(
name|localName
argument_list|,
name|atts
argument_list|,
name|driverInfo
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"<login, url, connectionPool> are valid.<"
operator|+
name|localName
operator|+
literal|"> is unexpected."
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"Config file is not of expected XML type"
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
class|class
name|UrlHandler
extends|extends
name|AbstractHandler
block|{
comment|/**          * Constructor which just delegates to the superconstructor.          *           * @param parentHandler The handler which should be restored to the parser at the          *            end of the element. Must not be<code>null</code>.          */
specifier|public
name|UrlHandler
parameter_list|(
name|XMLReader
name|parser
parameter_list|,
name|ContentHandler
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
block|}
specifier|public
name|void
name|init
parameter_list|(
name|String
name|name
parameter_list|,
name|Attributes
name|atts
parameter_list|,
name|DataSourceInfo
name|driverInfo
parameter_list|)
throws|throws
name|SAXException
block|{
name|driverInfo
operator|.
name|setDataSourceUrl
argument_list|(
name|atts
operator|.
name|getValue
argument_list|(
literal|"value"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|driverInfo
operator|.
name|getDataSourceUrl
argument_list|()
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"error:<url> has no 'value'."
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"'<url value=' attribute is required."
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
class|class
name|LoginHandler
extends|extends
name|AbstractHandler
block|{
comment|/**          * Constructor which just delegates to the superconstructor.          *           * @param parentHandler The handler which should be restored to the parser at the          *            end of the element. Must not be<code>null</code>.          */
specifier|public
name|LoginHandler
parameter_list|(
name|XMLReader
name|parser
parameter_list|,
name|ContentHandler
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
block|}
specifier|private
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
name|InputStreamReader
name|inputStreamReader
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
name|inputStreamReader
operator|=
operator|new
name|InputStreamReader
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
name|bufferedReader
operator|=
operator|new
name|BufferedReader
argument_list|(
name|inputStreamReader
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
name|exception
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|bufferedReader
operator|.
name|close
argument_list|()
expr_stmt|;
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
name|inputStreamReader
operator|.
name|close
argument_list|()
expr_stmt|;
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
name|exception
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|password
return|;
block|}
specifier|public
name|void
name|init
parameter_list|(
name|String
name|name
parameter_list|,
name|Attributes
name|atts
parameter_list|,
name|DataSourceInfo
name|driverInfo
parameter_list|)
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
name|atts
operator|.
name|getValue
argument_list|(
literal|"encoderClass"
argument_list|)
decl_stmt|;
name|String
name|encoderSalt
init|=
name|atts
operator|.
name|getValue
argument_list|(
literal|"encoderSalt"
argument_list|)
decl_stmt|;
name|String
name|password
init|=
name|atts
operator|.
name|getValue
argument_list|(
literal|"password"
argument_list|)
decl_stmt|;
name|String
name|passwordLocation
init|=
name|atts
operator|.
name|getValue
argument_list|(
literal|"passwordLocation"
argument_list|)
decl_stmt|;
name|String
name|passwordSource
init|=
name|atts
operator|.
name|getValue
argument_list|(
literal|"passwordSource"
argument_list|)
decl_stmt|;
name|String
name|username
init|=
name|atts
operator|.
name|getValue
argument_list|(
literal|"userName"
argument_list|)
decl_stmt|;
name|driverInfo
operator|.
name|setPasswordEncoderClass
argument_list|(
name|encoderClass
argument_list|)
expr_stmt|;
name|driverInfo
operator|.
name|setPasswordEncoderSalt
argument_list|(
name|encoderSalt
argument_list|)
expr_stmt|;
name|driverInfo
operator|.
name|setPasswordLocation
argument_list|(
name|passwordLocation
argument_list|)
expr_stmt|;
name|driverInfo
operator|.
name|setPasswordSource
argument_list|(
name|passwordSource
argument_list|)
expr_stmt|;
name|driverInfo
operator|.
name|setUserName
argument_list|(
name|username
argument_list|)
expr_stmt|;
comment|// Replace {} in passwordSource with encoderSalt -- useful for EXECUTABLE&
comment|// URL options
if|if
condition|(
name|encoderSalt
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
name|encoderSalt
argument_list|)
expr_stmt|;
block|}
name|PasswordEncoding
name|passwordEncoder
init|=
name|driverInfo
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
comment|// New style model (v1.2), process extra
comment|// locations
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
name|URL
name|url
init|=
name|parentConfiguration
operator|.
name|getResourceLocator
argument_list|()
operator|.
name|findResource
argument_list|(
name|passwordSource
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
name|password
operator|=
name|passwordFromURL
argument_list|(
name|url
argument_list|)
expr_stmt|;
else|else
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
name|exception
operator|.
name|printStackTrace
argument_list|()
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
name|exception
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|exception
parameter_list|)
block|{
name|exception
operator|.
name|printStackTrace
argument_list|()
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
name|driverInfo
operator|.
name|setPassword
argument_list|(
name|passwordEncoder
operator|.
name|decodePassword
argument_list|(
name|password
argument_list|,
name|encoderSalt
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
class|class
name|ConnectionHandler
extends|extends
name|AbstractHandler
block|{
comment|/**          * Constructor which just delegates to the superconstructor.          *           * @param parentHandler The handler which should be restored to the parser at the          *            end of the element. Must not be<code>null</code>.          */
specifier|public
name|ConnectionHandler
parameter_list|(
name|XMLReader
name|parser
parameter_list|,
name|ContentHandler
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
block|}
specifier|public
name|void
name|init
parameter_list|(
name|String
name|name
parameter_list|,
name|Attributes
name|atts
parameter_list|,
name|DataSourceInfo
name|driverInfo
parameter_list|)
throws|throws
name|SAXException
block|{
try|try
block|{
name|String
name|min
init|=
name|atts
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
name|driverInfo
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
name|String
name|max
init|=
name|atts
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
name|driverInfo
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
literal|"Error loading numeric attribute"
argument_list|,
name|nfex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SAXException
argument_list|(
literal|"Error reading numeric attribute."
argument_list|,
name|nfex
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

