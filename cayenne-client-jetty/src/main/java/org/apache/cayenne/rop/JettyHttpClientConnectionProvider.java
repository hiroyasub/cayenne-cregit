begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  ****************************************************************/
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
name|CayenneRuntimeException
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
name|RuntimeProperties
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
name|rop
operator|.
name|client
operator|.
name|ClientConstants
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
name|DIRuntimeException
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
name|remote
operator|.
name|ClientConnection
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
name|http
operator|.
name|JettyHttpROPConnector
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
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|api
operator|.
name|AuthenticationStore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|client
operator|.
name|util
operator|.
name|BasicAuthentication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|util
operator|.
name|ssl
operator|.
name|SslContextFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_comment
comment|/**  * This {@link Provider} initializes HTTP/1.1 {@link ClientConnection} through {@link JettyHttpROPConnector} which uses  * {@link org.eclipse.jetty.client.HttpClient}.  */
end_comment

begin_class
specifier|public
class|class
name|JettyHttpClientConnectionProvider
implements|implements
name|Provider
argument_list|<
name|ClientConnection
argument_list|>
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
name|JettyHttpROPConnector
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|RuntimeProperties
name|runtimeProperties
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ROPSerializationService
name|serializationService
decl_stmt|;
annotation|@
name|Override
specifier|public
name|ClientConnection
name|get
parameter_list|()
throws|throws
name|DIRuntimeException
block|{
name|String
name|sharedSession
init|=
name|runtimeProperties
operator|.
name|get
argument_list|(
name|ClientConstants
operator|.
name|ROP_SERVICE_SHARED_SESSION_PROPERTY
argument_list|)
decl_stmt|;
name|JettyHttpROPConnector
name|ropConnector
init|=
name|createJettyHttpRopConnector
argument_list|()
decl_stmt|;
name|ProxyRemoteService
name|remoteService
init|=
operator|new
name|ProxyRemoteService
argument_list|(
name|serializationService
argument_list|,
name|ropConnector
argument_list|)
decl_stmt|;
name|HttpClientConnection
name|clientConnection
init|=
operator|new
name|HttpClientConnection
argument_list|(
name|remoteService
argument_list|,
name|sharedSession
argument_list|)
decl_stmt|;
name|ropConnector
operator|.
name|setClientConnection
argument_list|(
name|clientConnection
argument_list|)
expr_stmt|;
return|return
name|clientConnection
return|;
block|}
specifier|protected
name|JettyHttpROPConnector
name|createJettyHttpRopConnector
parameter_list|()
block|{
name|String
name|url
init|=
name|runtimeProperties
operator|.
name|get
argument_list|(
name|ClientConstants
operator|.
name|ROP_SERVICE_URL_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"No property defined for '%s', can't initialize connection"
argument_list|,
name|ClientConstants
operator|.
name|ROP_SERVICE_URL_PROPERTY
argument_list|)
throw|;
block|}
name|String
name|username
init|=
name|runtimeProperties
operator|.
name|get
argument_list|(
name|ClientConstants
operator|.
name|ROP_SERVICE_USERNAME_PROPERTY
argument_list|)
decl_stmt|;
name|long
name|readTimeout
init|=
name|runtimeProperties
operator|.
name|getLong
argument_list|(
name|ClientConstants
operator|.
name|ROP_SERVICE_TIMEOUT_PROPERTY
argument_list|,
operator|-
literal|1L
argument_list|)
decl_stmt|;
name|HttpClient
name|httpClient
init|=
name|initJettyHttpClient
argument_list|()
decl_stmt|;
name|addBasicAuthentication
argument_list|(
name|httpClient
argument_list|,
name|url
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|JettyHttpROPConnector
name|result
init|=
operator|new
name|JettyHttpROPConnector
argument_list|(
name|httpClient
argument_list|,
name|url
argument_list|,
name|username
argument_list|)
decl_stmt|;
if|if
condition|(
name|readTimeout
operator|>
literal|0
condition|)
block|{
name|result
operator|.
name|setReadTimeout
argument_list|(
name|readTimeout
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|protected
name|HttpClient
name|initJettyHttpClient
parameter_list|()
block|{
try|try
block|{
name|HttpClient
name|httpClient
init|=
operator|new
name|HttpClient
argument_list|(
operator|new
name|SslContextFactory
argument_list|()
argument_list|)
decl_stmt|;
name|httpClient
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|httpClient
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Exception while starting Jetty HttpClient."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|addBasicAuthentication
parameter_list|(
name|HttpClient
name|httpClient
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|username
parameter_list|)
block|{
name|String
name|password
init|=
name|runtimeProperties
operator|.
name|get
argument_list|(
name|ClientConstants
operator|.
name|ROP_SERVICE_PASSWORD_PROPERTY
argument_list|)
decl_stmt|;
name|String
name|realm
init|=
name|runtimeProperties
operator|.
name|get
argument_list|(
name|ClientConstants
operator|.
name|ROP_SERVICE_REALM_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
operator|&&
name|password
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|realm
operator|==
literal|null
operator|&&
name|logger
operator|.
name|isWarnEnabled
argument_list|()
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"In order to use JettyClient with BASIC Authentication "
operator|+
literal|"you should provide Constants.ROP_SERVICE_REALM_PROPERTY."
argument_list|)
expr_stmt|;
return|return;
block|}
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
literal|"Adding authentication"
operator|+
literal|"\nUser: "
operator|+
name|username
operator|+
literal|"\nRealm: "
operator|+
name|realm
argument_list|)
expr_stmt|;
block|}
name|AuthenticationStore
name|auth
init|=
name|httpClient
operator|.
name|getAuthenticationStore
argument_list|()
decl_stmt|;
name|auth
operator|.
name|addAuthentication
argument_list|(
operator|new
name|BasicAuthentication
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|url
argument_list|)
argument_list|,
name|realm
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

