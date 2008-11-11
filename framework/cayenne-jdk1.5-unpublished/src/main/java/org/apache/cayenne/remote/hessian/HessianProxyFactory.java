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
name|remote
operator|.
name|hessian
package|;
end_package

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

begin_comment
comment|/**  * A proxy factory that handles HTTP sessions.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|HessianProxyFactory
extends|extends
name|com
operator|.
name|caucho
operator|.
name|hessian
operator|.
name|client
operator|.
name|HessianProxyFactory
block|{
specifier|static
specifier|final
name|String
name|SESSION_COOKIE_NAME
init|=
literal|"JSESSIONID"
decl_stmt|;
specifier|private
name|HessianConnection
name|clientConnection
decl_stmt|;
name|HessianProxyFactory
parameter_list|(
name|HessianConnection
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
annotation|@
name|Override
specifier|protected
name|URLConnection
name|openConnection
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
name|URLConnection
name|connection
init|=
name|super
operator|.
name|openConnection
argument_list|(
name|url
argument_list|)
decl_stmt|;
comment|// unfortunately we can't read response cookies without completely reimplementing
comment|// 'HessianProxy.invoke()'. Currently (3.0.13) it doesn't allow to cleanly
comment|// intercept response... so extract session id from the RemoteSession....
comment|// add session cookie
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
name|setRequestProperty
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
return|return
name|connection
return|;
block|}
block|}
end_class

end_unit

