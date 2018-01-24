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
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
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
name|event
operator|.
name|EventBridge
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
name|event
operator|.
name|EventBridgeFactory
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
name|event
operator|.
name|EventSubject
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
name|HashCodeBuilder
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
name|ToStringBuilder
import|;
end_import

begin_comment
comment|/**  * A descriptor used by default service implementation to pass session parameters to the  * client. It provides the client with details on how to invoke the service and how to  * listen for the server events.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|RemoteSession
implements|implements
name|Serializable
block|{
specifier|static
specifier|final
name|Collection
argument_list|<
name|EventSubject
argument_list|>
name|SUBJECTS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|DataChannel
operator|.
name|GRAPH_CHANGED_SUBJECT
argument_list|,
name|DataChannel
operator|.
name|GRAPH_FLUSHED_SUBJECT
argument_list|,
name|DataChannel
operator|.
name|GRAPH_ROLLEDBACK_SUBJECT
argument_list|)
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|String
name|sessionId
decl_stmt|;
specifier|protected
name|String
name|eventBridgeFactory
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|eventBridgeParameters
decl_stmt|;
comment|// private constructor used by hessian deserialization mechanism
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
name|RemoteSession
parameter_list|()
block|{
block|}
comment|/**      * Creates a HessianServiceDescriptor without server events support.      */
specifier|public
name|RemoteSession
parameter_list|(
name|String
name|sessionId
parameter_list|)
block|{
name|this
argument_list|(
name|sessionId
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a HessianServiceDescriptor. If<code>eventBridgeFactory</code> argument      * is not null, session will support server events.      */
specifier|public
name|RemoteSession
parameter_list|(
name|String
name|sessionId
parameter_list|,
name|String
name|eventBridgeFactory
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|eventBridgeParameters
parameter_list|)
block|{
if|if
condition|(
name|sessionId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null sessionId"
argument_list|)
throw|;
block|}
name|this
operator|.
name|sessionId
operator|=
name|sessionId
expr_stmt|;
name|this
operator|.
name|eventBridgeFactory
operator|=
name|eventBridgeFactory
expr_stmt|;
name|this
operator|.
name|eventBridgeParameters
operator|=
name|eventBridgeParameters
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|sessionId
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
return|return
literal|false
return|;
name|RemoteSession
name|that
init|=
operator|(
name|RemoteSession
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|sessionId
argument_list|,
name|that
operator|.
name|sessionId
argument_list|)
return|;
block|}
comment|/**      * Returns server session id. This is often the same as HttpSession id.      */
specifier|public
name|String
name|getSessionId
parameter_list|()
block|{
return|return
name|sessionId
return|;
block|}
comment|/**      * Returns session group name. Group name is used for shared sessions.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|boolean
name|isServerEventsEnabled
parameter_list|()
block|{
return|return
name|eventBridgeFactory
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|ToStringBuilder
name|builder
init|=
operator|new
name|ToStringBuilder
argument_list|(
name|this
argument_list|)
operator|.
name|append
argument_list|(
literal|"sessionId"
argument_list|,
name|sessionId
argument_list|)
decl_stmt|;
if|if
condition|(
name|eventBridgeFactory
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"eventBridgeFactory"
argument_list|,
name|eventBridgeFactory
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|Collection
argument_list|<
name|EventSubject
argument_list|>
name|getSubjects
parameter_list|()
block|{
return|return
name|SUBJECTS
return|;
block|}
comment|/**      * @since 4.0      */
specifier|public
name|String
name|getEventBridgeFactory
parameter_list|()
block|{
return|return
name|eventBridgeFactory
return|;
block|}
comment|/**      * @since 4.0      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getEventBridgeParameters
parameter_list|()
block|{
return|return
name|eventBridgeParameters
operator|!=
literal|null
condition|?
name|eventBridgeParameters
else|:
name|Collections
operator|.
expr|<
name|String
operator|,
name|String
operator|>
name|emptyMap
argument_list|()
return|;
block|}
block|}
end_class

end_unit

