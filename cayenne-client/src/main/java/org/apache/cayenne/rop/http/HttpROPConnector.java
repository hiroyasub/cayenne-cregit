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
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|HttpROPConnector
operator|.
name|class
argument_list|)
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
name|logConnect
argument_list|(
literal|null
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
name|name
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
name|logConnect
argument_list|(
name|name
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
name|name
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
name|StringBuilder
name|urlParams
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|params
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|urlParams
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|urlParams
operator|.
name|append
argument_list|(
literal|'&'
argument_list|)
expr_stmt|;
block|}
name|urlParams
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|urlParams
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
expr_stmt|;
name|urlParams
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|urlParams
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
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
name|connection
operator|.
name|setDoOutput
argument_list|(
literal|true
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
specifier|public
name|String
name|getBasicAuth
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|password
parameter_list|)
block|{
if|if
condition|(
name|user
operator|!=
literal|null
operator|&&
name|password
operator|!=
literal|null
condition|)
block|{
return|return
literal|"Basic "
operator|+
name|base64
argument_list|(
name|user
operator|+
literal|":"
operator|+
name|password
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Creates the Base64 value.      */
specifier|private
name|String
name|base64
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|StringBuffer
name|cb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
init|;
name|i
operator|+
literal|2
operator|<
name|value
operator|.
name|length
argument_list|()
condition|;
name|i
operator|+=
literal|3
control|)
block|{
name|long
name|chunk
init|=
operator|(
name|int
operator|)
name|value
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|chunk
operator|=
operator|(
name|chunk
operator|<<
literal|8
operator|)
operator|+
operator|(
name|int
operator|)
name|value
operator|.
name|charAt
argument_list|(
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
name|chunk
operator|=
operator|(
name|chunk
operator|<<
literal|8
operator|)
operator|+
operator|(
name|int
operator|)
name|value
operator|.
name|charAt
argument_list|(
name|i
operator|+
literal|2
argument_list|)
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
name|encode
argument_list|(
name|chunk
operator|>>
literal|18
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
name|encode
argument_list|(
name|chunk
operator|>>
literal|12
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
name|encode
argument_list|(
name|chunk
operator|>>
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
name|encode
argument_list|(
name|chunk
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|+
literal|1
operator|<
name|value
operator|.
name|length
argument_list|()
condition|)
block|{
name|long
name|chunk
init|=
operator|(
name|int
operator|)
name|value
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|chunk
operator|=
operator|(
name|chunk
operator|<<
literal|8
operator|)
operator|+
operator|(
name|int
operator|)
name|value
operator|.
name|charAt
argument_list|(
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
name|chunk
operator|<<=
literal|8
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
name|encode
argument_list|(
name|chunk
operator|>>
literal|18
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
name|encode
argument_list|(
name|chunk
operator|>>
literal|12
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
name|encode
argument_list|(
name|chunk
operator|>>
literal|6
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|i
operator|<
name|value
operator|.
name|length
argument_list|()
condition|)
block|{
name|long
name|chunk
init|=
operator|(
name|int
operator|)
name|value
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|chunk
operator|<<=
literal|16
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
name|encode
argument_list|(
name|chunk
operator|>>
literal|18
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
name|encode
argument_list|(
name|chunk
operator|>>
literal|12
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
expr_stmt|;
name|cb
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
expr_stmt|;
block|}
return|return
name|cb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|char
name|encode
parameter_list|(
name|long
name|d
parameter_list|)
block|{
name|d
operator|&=
literal|0x3f
expr_stmt|;
if|if
condition|(
name|d
operator|<
literal|26
condition|)
return|return
operator|(
name|char
operator|)
operator|(
name|d
operator|+
literal|'A'
operator|)
return|;
if|else if
condition|(
name|d
operator|<
literal|52
condition|)
return|return
operator|(
name|char
operator|)
operator|(
name|d
operator|+
literal|'a'
operator|-
literal|26
operator|)
return|;
if|else if
condition|(
name|d
operator|<
literal|62
condition|)
return|return
operator|(
name|char
operator|)
operator|(
name|d
operator|+
literal|'0'
operator|-
literal|52
operator|)
return|;
if|else if
condition|(
name|d
operator|==
literal|62
condition|)
return|return
literal|'+'
return|;
else|else
return|return
literal|'/'
return|;
block|}
specifier|private
name|void
name|logConnect
parameter_list|(
name|String
name|sharedSessionName
parameter_list|)
block|{
name|StringBuilder
name|log
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Connecting to ["
argument_list|)
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|append
argument_list|(
name|username
argument_list|)
expr_stmt|;
if|if
condition|(
name|password
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|append
argument_list|(
literal|":*******"
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|append
argument_list|(
literal|"@"
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|append
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|log
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
if|if
condition|(
name|sharedSessionName
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|append
argument_list|(
literal|" - shared session '"
argument_list|)
operator|.
name|append
argument_list|(
name|sharedSessionName
argument_list|)
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|append
argument_list|(
literal|" - dedicated session."
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
name|log
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

