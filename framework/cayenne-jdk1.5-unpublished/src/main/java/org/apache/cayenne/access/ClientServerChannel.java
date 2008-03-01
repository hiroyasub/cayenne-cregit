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
name|access
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
name|QueryResponse
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
name|cache
operator|.
name|QueryCache
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
name|EventManager
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|EntityResolver
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
name|query
operator|.
name|Query
import|;
end_import

begin_comment
comment|/**  * A DataChannel that provides a server-side end of the bridge between client and server  * objects in a Remote Object Persistence stack.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ClientServerChannel
implements|implements
name|DataChannel
block|{
specifier|protected
name|DataContext
name|serverContext
decl_stmt|;
specifier|public
name|ClientServerChannel
parameter_list|(
name|DataDomain
name|domain
parameter_list|)
block|{
name|this
argument_list|(
name|domain
operator|.
name|createDataContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a ClientServerChannel that wraps a specified DataContext.      *       * @since 3.0      */
specifier|public
name|ClientServerChannel
parameter_list|(
name|DataContext
name|serverContext
parameter_list|)
block|{
name|this
operator|.
name|serverContext
operator|=
name|serverContext
expr_stmt|;
block|}
comment|/**      * @deprecated since 3.0 as DataChannel events (incorrectly called "lifecycleEvents"      *             in 2.0) are no longer posted by ClientServerChannel.      */
specifier|public
name|ClientServerChannel
parameter_list|(
name|DataDomain
name|domain
parameter_list|,
name|boolean
name|lifecycleEventsEnabled
parameter_list|)
block|{
name|this
argument_list|(
name|domain
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated Since 3.0 - always returns false. This method was a misnomer referring      *             to DataChannel events, not lifecycle events introduced in 3.0.      *             Currently ClientServerChannel posts no channel events.      */
specifier|public
name|boolean
name|isLifecycleEventsEnabled
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**      * @deprecated Since 3.0 - does nothing. This method was a misnomer referring to      *             DataChannel events, not lifecycle events introduced in 3.0. Currently      *             ClientServerChannel posts no channel events.      */
specifier|public
name|void
name|setLifecycleEventsEnabled
parameter_list|(
name|boolean
name|lifecycleEventsEnabled
parameter_list|)
block|{
block|}
specifier|public
name|QueryResponse
name|onQuery
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|Query
name|query
parameter_list|)
block|{
return|return
operator|new
name|ClientServerChannelQueryAction
argument_list|(
name|this
argument_list|,
name|query
argument_list|)
operator|.
name|execute
argument_list|()
return|;
block|}
name|QueryCache
name|getQueryCache
parameter_list|()
block|{
return|return
name|serverContext
operator|.
name|getQueryCache
argument_list|()
return|;
block|}
name|DataChannel
name|getParentChannel
parameter_list|()
block|{
return|return
name|serverContext
return|;
block|}
specifier|public
name|EntityResolver
name|getEntityResolver
parameter_list|()
block|{
return|return
name|serverContext
operator|.
name|getEntityResolver
argument_list|()
return|;
block|}
specifier|public
name|EventManager
name|getEventManager
parameter_list|()
block|{
return|return
name|serverContext
operator|!=
literal|null
condition|?
name|serverContext
operator|.
name|getEventManager
argument_list|()
else|:
literal|null
return|;
block|}
specifier|public
name|GraphDiff
name|onSync
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|GraphDiff
name|changes
parameter_list|,
name|int
name|syncType
parameter_list|)
block|{
name|GraphDiff
name|diff
init|=
name|getParentChannel
argument_list|()
operator|.
name|onSync
argument_list|(
literal|null
argument_list|,
name|changes
argument_list|,
name|syncType
argument_list|)
decl_stmt|;
return|return
operator|new
name|ClientReturnDiffFilter
argument_list|(
name|getEntityResolver
argument_list|()
argument_list|)
operator|.
name|filter
argument_list|(
name|diff
argument_list|)
return|;
block|}
block|}
end_class

end_unit

