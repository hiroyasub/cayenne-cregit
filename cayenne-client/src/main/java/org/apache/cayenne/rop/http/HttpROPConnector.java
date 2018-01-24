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
name|rop
operator|.
name|http
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
name|remote
operator|.
name|RemoteSession
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
name|rop
operator|.
name|HttpClientConnection
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
name|rop
operator|.
name|ROPConnector
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
name|rop
operator|.
name|ROPConstants
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
name|rop
operator|.
name|ROPUtil
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
name|OutputStream
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
name|net
operator|.
name|URLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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

begin_class
specifier|public
class|class
name|HttpROPConnector
implements|implements
name|ROPConnector
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
name|HttpROPConnector
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SESSION_COOKIE_NAME
init|=
literal|"JSESSIONID"
decl_stmt|;
specifier|private
name|HttpClientConnection
name|clientConnection
decl_stmt|;
specifier|private
name|String
name|url
decl_stmt|;
specifier|private
name|String
name|username
decl_stmt|;
specifier|private
name|String
name|password
decl_stmt|;
specifier|private
name|Long
name|readTimeout
decl_stmt|;
specifier|public
name|HttpROPConnector
parameter_list|(
name|String
name|url
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
specifier|public
name|void
name|setClientConnection
parameter_list|(
name|HttpClientConnection
name|clientConnection
parameter_list|)
block|{
name|this
operator|.
name|clientConnection
operator|=
name|clientConnection
expr_stmt|;
block|}
specifier|public
name|void
name|setReadTimeout
parameter_list|(
name|Long
name|readTimeout
parameter_list|)
block|{
name|this
operator|.
name|readTimeout
operator|=
name|readTimeout
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|establishSession
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|logger
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
name|ROPUtil
operator|.
name|getLogConnect
argument_list|(
name|url
argument_list|,
name|username
argument_list|,
name|password
operator|!=
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestParams
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|requestParams
operator|.
name|put
argument_list|(
name|ROPConstants
operator|.
name|OPERATION_PARAMETER
argument_list|,
name|ROPConstants
operator|.
name|ESTABLISH_SESSION_OPERATION
argument_list|)
expr_stmt|;
return|return
name|doRequest
argument_list|(
name|requestParams
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|establishSharedSession
parameter_list|(
name|String
name|sharedSessionName
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|logger
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
name|ROPUtil
operator|.
name|getLogConnect
argument_list|(
name|url
argument_list|,
name|username
argument_list|,
name|password
operator|!=
literal|null
argument_list|,
name|sharedSessionName
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|requestParams
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|requestParams
operator|.
name|put
argument_list|(
name|ROPConstants
operator|.
name|OPERATION_PARAMETER
argument_list|,
name|ROPConstants
operator|.
name|ESTABLISH_SHARED_SESSION_OPERATION
argument_list|)
expr_stmt|;
name|requestParams
operator|.
name|put
argument_list|(
name|ROPConstants
operator|.
name|SESSION_NAME_PARAMETER
argument_list|,
name|sharedSessionName
argument_list|)
expr_stmt|;
return|return
name|doRequest
argument_list|(
name|requestParams
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|sendMessage
parameter_list|(
name|byte
index|[]
name|message
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|doRequest
argument_list|(
name|message
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|logger
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
name|ROPUtil
operator|.
name|getLogDisconnect
argument_list|(
name|url
argument_list|,
name|username
argument_list|,
name|password
operator|!=
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|InputStream
name|doRequest
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
throws|throws
name|IOException
block|{
name|URLConnection
name|connection
init|=
operator|new
name|URL
argument_list|(
name|url
argument_list|)
operator|.
name|openConnection
argument_list|()
decl_stmt|;
if|if
condition|(
name|readTimeout
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|setReadTimeout
argument_list|(
name|readTimeout
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|addAuthHeader
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setRequestProperty
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/x-www-form-urlencoded"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setRequestProperty
argument_list|(
literal|"charset"
argument_list|,
literal|"utf-8"
argument_list|)
expr_stmt|;
try|try
init|(
name|OutputStream
name|output
init|=
name|connection
operator|.
name|getOutputStream
argument_list|()
init|)
block|{
name|output
operator|.
name|write
argument_list|(
name|ROPUtil
operator|.
name|getParamsAsString
argument_list|(
name|params
argument_list|)
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|output
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
return|return
name|connection
operator|.
name|getInputStream
argument_list|()
return|;
block|}
specifier|protected
name|InputStream
name|doRequest
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|URLConnection
name|connection
init|=
operator|new
name|URL
argument_list|(
name|url
argument_list|)
operator|.
name|openConnection
argument_list|()
decl_stmt|;
if|if
condition|(
name|readTimeout
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|setReadTimeout
argument_list|(
name|readTimeout
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|addAuthHeader
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|addSessionCookie
argument_list|(
name|connection
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setRequestProperty
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
if|if
condition|(
name|data
operator|!=
literal|null
condition|)
block|{
try|try
init|(
name|OutputStream
name|output
init|=
name|connection
operator|.
name|getOutputStream
argument_list|()
init|)
block|{
name|output
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|output
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|connection
operator|.
name|getInputStream
argument_list|()
return|;
block|}
specifier|protected
name|void
name|addAuthHeader
parameter_list|(
name|URLConnection
name|connection
parameter_list|)
block|{
name|String
name|basicAuth
init|=
name|ROPUtil
operator|.
name|getBasicAuth
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
if|if
condition|(
name|basicAuth
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|addRequestProperty
argument_list|(
literal|"Authorization"
argument_list|,
name|basicAuth
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|addSessionCookie
parameter_list|(
name|URLConnection
name|connection
parameter_list|)
block|{
name|RemoteSession
name|session
init|=
name|clientConnection
operator|.
name|getSession
argument_list|()
decl_stmt|;
if|if
condition|(
name|session
operator|!=
literal|null
operator|&&
name|session
operator|.
name|getSessionId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|addRequestProperty
argument_list|(
literal|"Cookie"
argument_list|,
name|SESSION_COOKIE_NAME
operator|+
literal|"="
operator|+
name|session
operator|.
name|getSessionId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

