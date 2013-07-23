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
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ListIterator
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
name|DataChannelSyncCallbackAction
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
name|ObjectId
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
name|Persistent
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
name|graph
operator|.
name|CompoundDiff
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
name|graph
operator|.
name|GraphDiffCompressor
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
name|GraphEvent
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
name|EntityResultSegment
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
name|QueryMetadata
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
name|DeepMergeOperation
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
comment|/**  * A {@link org.apache.cayenne.DataChannel} implementation that accesses a remote server  * via a ClientConnection.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|ClientChannel
implements|implements
name|DataChannel
block|{
specifier|protected
name|ClientConnection
name|connection
decl_stmt|;
specifier|protected
name|EventManager
name|eventManager
decl_stmt|;
specifier|protected
name|EntityResolver
name|entityResolver
decl_stmt|;
specifier|protected
name|boolean
name|channelEventsEnabled
decl_stmt|;
specifier|protected
name|GraphDiffCompressor
name|diffCompressor
decl_stmt|;
name|EventBridge
name|remoteChannelListener
decl_stmt|;
comment|/**      * @param remoteEventsOptional if true, failure to start an EventBridge will not      *            result in an exception.      * @since 3.0      */
specifier|public
name|ClientChannel
parameter_list|(
name|ClientConnection
name|connection
parameter_list|,
name|boolean
name|channelEventsEnabled
parameter_list|,
name|EventManager
name|eventManager
parameter_list|,
name|boolean
name|remoteEventsOptional
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
name|this
operator|.
name|diffCompressor
operator|=
operator|new
name|GraphDiffCompressor
argument_list|()
expr_stmt|;
name|this
operator|.
name|eventManager
operator|=
name|eventManager
expr_stmt|;
name|this
operator|.
name|channelEventsEnabled
operator|=
name|eventManager
operator|!=
literal|null
operator|&&
name|channelEventsEnabled
expr_stmt|;
if|if
condition|(
operator|!
name|remoteEventsOptional
condition|)
block|{
name|setupRemoteChannelListener
argument_list|()
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|setupRemoteChannelListener
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
block|}
block|}
block|}
comment|/**      * @since 3.1      */
specifier|public
name|ClientConnection
name|getConnection
parameter_list|()
block|{
return|return
name|connection
return|;
block|}
comment|/**      * @since 3.1      */
specifier|public
name|boolean
name|isChannelEventsEnabled
parameter_list|()
block|{
return|return
name|channelEventsEnabled
return|;
block|}
specifier|public
name|EventManager
name|getEventManager
parameter_list|()
block|{
return|return
name|eventManager
return|;
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
name|QueryResponse
name|response
init|=
operator|(
name|QueryResponse
operator|)
name|send
argument_list|(
operator|new
name|QueryMessage
argument_list|(
name|query
argument_list|)
argument_list|,
name|QueryResponse
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// if needed, register objects in provided context, rewriting the response
comment|// (assuming all lists are mutable)
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|EntityResolver
name|resolver
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|QueryMetadata
name|info
init|=
name|query
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|info
operator|.
name|isFetchingDataRows
argument_list|()
condition|)
block|{
name|response
operator|.
name|reset
argument_list|()
expr_stmt|;
while|while
condition|(
name|response
operator|.
name|next
argument_list|()
condition|)
block|{
if|if
condition|(
name|response
operator|.
name|isList
argument_list|()
condition|)
block|{
name|List
name|objects
init|=
name|response
operator|.
name|currentList
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|objects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|DeepMergeOperation
name|merger
init|=
operator|new
name|DeepMergeOperation
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|rsMapping
init|=
name|info
operator|.
name|getResultSetMapping
argument_list|()
decl_stmt|;
if|if
condition|(
name|rsMapping
operator|==
literal|null
condition|)
block|{
name|convertSingleObjects
argument_list|(
name|resolver
argument_list|,
name|objects
argument_list|,
name|merger
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|rsMapping
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
if|if
condition|(
name|rsMapping
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|EntityResultSegment
condition|)
block|{
name|convertSingleObjects
argument_list|(
name|resolver
argument_list|,
name|objects
argument_list|,
name|merger
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|processMixedResult
argument_list|(
name|resolver
argument_list|,
name|objects
argument_list|,
name|merger
argument_list|,
name|rsMapping
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
block|}
return|return
name|response
return|;
block|}
specifier|private
name|void
name|processMixedResult
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|,
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|objects
parameter_list|,
name|DeepMergeOperation
name|merger
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|rsMapping
parameter_list|)
block|{
name|int
name|width
init|=
name|rsMapping
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|width
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|rsMapping
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|instanceof
name|EntityResultSegment
condition|)
block|{
for|for
control|(
name|Object
index|[]
name|object
range|:
name|objects
control|)
block|{
name|object
index|[
name|i
index|]
operator|=
name|convertObject
argument_list|(
name|resolver
argument_list|,
name|merger
argument_list|,
operator|(
name|Persistent
operator|)
name|object
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|convertSingleObjects
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|,
name|List
name|objects
parameter_list|,
name|DeepMergeOperation
name|merger
parameter_list|)
block|{
name|ListIterator
name|it
init|=
name|objects
operator|.
name|listIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|next
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|it
operator|.
name|set
argument_list|(
name|convertObject
argument_list|(
name|resolver
argument_list|,
name|merger
argument_list|,
operator|(
name|Persistent
operator|)
name|next
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Object
name|convertObject
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|,
name|DeepMergeOperation
name|merger
parameter_list|,
name|Persistent
name|object
parameter_list|)
block|{
name|ObjectId
name|id
init|=
name|object
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
comment|// sanity check
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Server returned an object without an id: "
operator|+
name|object
argument_list|)
throw|;
block|}
return|return
name|merger
operator|.
name|merge
argument_list|(
name|object
argument_list|)
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
name|DataChannelSyncCallbackAction
name|callbackAction
init|=
name|DataChannelSyncCallbackAction
operator|.
name|getCallbackAction
argument_list|(
name|getEntityResolver
argument_list|()
operator|.
name|getCallbackRegistry
argument_list|()
argument_list|,
name|originatingContext
operator|.
name|getGraphManager
argument_list|()
argument_list|,
name|changes
argument_list|,
name|syncType
argument_list|)
decl_stmt|;
name|callbackAction
operator|.
name|applyPreCommit
argument_list|()
expr_stmt|;
name|changes
operator|=
name|diffCompressor
operator|.
name|compress
argument_list|(
name|changes
argument_list|)
expr_stmt|;
name|GraphDiff
name|replyDiff
init|=
operator|(
name|GraphDiff
operator|)
name|send
argument_list|(
operator|new
name|SyncMessage
argument_list|(
name|originatingContext
argument_list|,
name|syncType
argument_list|,
name|changes
argument_list|)
argument_list|,
name|GraphDiff
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|channelEventsEnabled
condition|)
block|{
name|EventSubject
name|subject
decl_stmt|;
switch|switch
condition|(
name|syncType
condition|)
block|{
case|case
name|DataChannel
operator|.
name|ROLLBACK_CASCADE_SYNC
case|:
name|subject
operator|=
name|DataChannel
operator|.
name|GRAPH_ROLLEDBACK_SUBJECT
expr_stmt|;
break|break;
case|case
name|DataChannel
operator|.
name|FLUSH_NOCASCADE_SYNC
case|:
name|subject
operator|=
name|DataChannel
operator|.
name|GRAPH_CHANGED_SUBJECT
expr_stmt|;
break|break;
case|case
name|DataChannel
operator|.
name|FLUSH_CASCADE_SYNC
case|:
name|subject
operator|=
name|DataChannel
operator|.
name|GRAPH_FLUSHED_SUBJECT
expr_stmt|;
break|break;
default|default:
name|subject
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
comment|// combine message sender changes and message receiver changes into a
comment|// single event
name|boolean
name|sentNoop
init|=
name|changes
operator|==
literal|null
operator|||
name|changes
operator|.
name|isNoop
argument_list|()
decl_stmt|;
name|boolean
name|receivedNoop
init|=
name|replyDiff
operator|==
literal|null
operator|||
name|replyDiff
operator|.
name|isNoop
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|sentNoop
operator|||
operator|!
name|receivedNoop
condition|)
block|{
name|CompoundDiff
name|notification
init|=
operator|new
name|CompoundDiff
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|sentNoop
condition|)
block|{
name|notification
operator|.
name|add
argument_list|(
name|changes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|receivedNoop
condition|)
block|{
name|notification
operator|.
name|add
argument_list|(
name|replyDiff
argument_list|)
expr_stmt|;
block|}
name|Object
name|postedBy
init|=
operator|(
name|originatingContext
operator|!=
literal|null
operator|)
condition|?
name|originatingContext
else|:
name|this
decl_stmt|;
name|GraphEvent
name|e
init|=
operator|new
name|GraphEvent
argument_list|(
name|this
argument_list|,
name|postedBy
argument_list|,
name|notification
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|postEvent
argument_list|(
name|e
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|callbackAction
operator|.
name|applyPostCommit
argument_list|()
expr_stmt|;
return|return
name|replyDiff
return|;
block|}
comment|/**      * Returns EntityResolver obtained from the server. On first access, this method sends      * a message to the server to retrieve the EntityResolver. On subsequent calls locally      * cached resolver is used.      */
specifier|public
name|EntityResolver
name|getEntityResolver
parameter_list|()
block|{
if|if
condition|(
name|entityResolver
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|entityResolver
operator|==
literal|null
condition|)
block|{
name|entityResolver
operator|=
operator|(
name|EntityResolver
operator|)
name|send
argument_list|(
operator|new
name|BootstrapMessage
argument_list|()
argument_list|,
name|EntityResolver
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|entityResolver
return|;
block|}
comment|/**      * Starts up an EventBridge to listen for remote updates. Returns true if the listener      * was setup, false if not. False can be returned if the underlying connection doesn't      * support events of if there is no EventManager available.      */
specifier|protected
name|boolean
name|setupRemoteChannelListener
parameter_list|()
throws|throws
name|CayenneRuntimeException
block|{
if|if
condition|(
name|eventManager
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|EventBridge
name|bridge
init|=
name|connection
operator|.
name|getServerEventBridge
argument_list|()
decl_stmt|;
if|if
condition|(
name|bridge
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
try|try
block|{
comment|// make sure events are sent on behalf of this channel...and received from all
name|bridge
operator|.
name|startup
argument_list|(
name|eventManager
argument_list|,
name|EventBridge
operator|.
name|RECEIVE_LOCAL_EXTERNAL
argument_list|,
literal|null
argument_list|,
name|this
argument_list|)
expr_stmt|;
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
literal|"Error starting EventBridge "
operator|+
name|bridge
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|this
operator|.
name|remoteChannelListener
operator|=
name|bridge
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/**      * Sends a message via connector, getting a result as an instance of a specific class.      *       * @throws org.apache.cayenne.CayenneRuntimeException if an underlying connector      *             exception occurred, or a result is not of expected type.      */
specifier|protected
name|Object
name|send
parameter_list|(
name|ClientMessage
name|message
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultClass
parameter_list|)
block|{
name|Object
name|result
init|=
name|connection
operator|.
name|sendMessage
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
operator|&&
operator|!
name|resultClass
operator|.
name|isInstance
argument_list|(
name|result
argument_list|)
condition|)
block|{
name|String
name|resultString
init|=
operator|new
name|ToStringBuilder
argument_list|(
name|result
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Expected result type: "
operator|+
name|resultClass
operator|.
name|getName
argument_list|()
operator|+
literal|", actual: "
operator|+
name|resultString
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit
