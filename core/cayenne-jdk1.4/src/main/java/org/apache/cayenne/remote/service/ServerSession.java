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
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|DataChannel
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
comment|/**  * An object that stores server side objects for the client session.  *   * @author Andrus Adamchik  * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|ServerSession
implements|implements
name|Serializable
block|{
specifier|protected
name|RemoteSession
name|session
decl_stmt|;
specifier|protected
name|DataChannel
name|channel
decl_stmt|;
specifier|public
name|ServerSession
parameter_list|(
name|RemoteSession
name|session
parameter_list|,
name|DataChannel
name|channel
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|channel
operator|=
name|channel
expr_stmt|;
block|}
specifier|public
name|DataChannel
name|getChannel
parameter_list|()
block|{
return|return
name|channel
return|;
block|}
specifier|public
name|RemoteSession
name|getSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
block|}
end_class

end_unit

