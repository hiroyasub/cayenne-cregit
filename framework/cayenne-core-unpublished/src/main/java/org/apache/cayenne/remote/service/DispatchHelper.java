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
name|BootstrapMessage
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
name|ClientMessage
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
name|QueryMessage
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
name|SyncMessage
import|;
end_import

begin_comment
comment|/**  * A helper class to match message types with DataChannel methods.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|DispatchHelper
block|{
specifier|static
name|Object
name|dispatch
parameter_list|(
name|DataChannel
name|channel
parameter_list|,
name|ClientMessage
name|message
parameter_list|)
block|{
comment|// do most common messages first...
if|if
condition|(
name|message
operator|instanceof
name|QueryMessage
condition|)
block|{
return|return
name|channel
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
operator|(
operator|(
name|QueryMessage
operator|)
name|message
operator|)
operator|.
name|getQuery
argument_list|()
argument_list|)
return|;
block|}
if|else if
condition|(
name|message
operator|instanceof
name|SyncMessage
condition|)
block|{
name|SyncMessage
name|sync
init|=
operator|(
name|SyncMessage
operator|)
name|message
decl_stmt|;
return|return
name|channel
operator|.
name|onSync
argument_list|(
literal|null
argument_list|,
name|sync
operator|.
name|getSenderChanges
argument_list|()
argument_list|,
name|sync
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
if|else if
condition|(
name|message
operator|instanceof
name|BootstrapMessage
condition|)
block|{
return|return
name|channel
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClientEntityResolver
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Message dispatch error. Unsupported message: "
operator|+
name|message
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit
