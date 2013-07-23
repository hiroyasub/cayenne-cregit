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
name|ObjectContext
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
name|graph
operator|.
name|GraphDiff
import|;
end_import

begin_comment
comment|/**  * A message used for synchronization of the child with parent. It defines a few types of  * synchronization: "flush" (when the child sends its changes without a commit), "commit"  * (cascading flush with ultimate commit to the database), and "rollback" - cascading  * reverting of all uncommitted changes.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|SyncMessage
implements|implements
name|ClientMessage
block|{
specifier|protected
specifier|transient
name|ObjectContext
name|source
decl_stmt|;
specifier|protected
name|int
name|type
decl_stmt|;
specifier|protected
name|GraphDiff
name|senderChanges
decl_stmt|;
comment|// private constructor for Hessian deserialization
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
name|SyncMessage
parameter_list|()
block|{
block|}
specifier|public
name|SyncMessage
parameter_list|(
name|ObjectContext
name|source
parameter_list|,
name|int
name|syncType
parameter_list|,
name|GraphDiff
name|senderChanges
parameter_list|)
block|{
comment|// validate type
if|if
condition|(
name|syncType
operator|!=
name|DataChannel
operator|.
name|FLUSH_NOCASCADE_SYNC
operator|&&
name|syncType
operator|!=
name|DataChannel
operator|.
name|FLUSH_CASCADE_SYNC
operator|&&
name|syncType
operator|!=
name|DataChannel
operator|.
name|ROLLBACK_CASCADE_SYNC
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"'type' is invalid: "
operator|+
name|syncType
argument_list|)
throw|;
block|}
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|syncType
expr_stmt|;
name|this
operator|.
name|senderChanges
operator|=
name|senderChanges
expr_stmt|;
block|}
comment|/**      * Returns a source of SyncMessage.      */
specifier|public
name|ObjectContext
name|getSource
parameter_list|()
block|{
return|return
name|source
return|;
block|}
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|GraphDiff
name|getSenderChanges
parameter_list|()
block|{
return|return
name|senderChanges
return|;
block|}
comment|/**      * Returns a description of the type of message.      * Possibilities are "flush-sync", "flush-cascade-sync", "rollback-cascade-sync" or "unknown-sync".      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|DataChannel
operator|.
name|FLUSH_NOCASCADE_SYNC
case|:
return|return
literal|"flush-sync"
return|;
case|case
name|DataChannel
operator|.
name|FLUSH_CASCADE_SYNC
case|:
return|return
literal|"flush-cascade-sync"
return|;
case|case
name|DataChannel
operator|.
name|ROLLBACK_CASCADE_SYNC
case|:
return|return
literal|"rollback-cascade-sync"
return|;
default|default:
return|return
literal|"unknown-sync"
return|;
block|}
block|}
block|}
end_class

end_unit
