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
comment|/**  * DataChannel is an abstraction used by ObjectContexts to obtain mapping metadata and  * access a persistent store. There is rarely a need to use it directly.  *   * @since 1.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|DataChannel
block|{
comment|/**      * A synchronization type that results in changes from an ObjectContext to be recorded      * in the parent DataChannel. If the parent is itself an ObjectContext, changes are      * NOT propagated any further.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLUSH_NOCASCADE_SYNC
init|=
literal|1
decl_stmt|;
comment|/**      * A synchronization type that results in changes from an ObjectContext to be recorded      * in the parent DataChannel. If the parent is itself an ObjectContext, it is expected      * to send its own sync message to its parent DataChannel to cascade sycnhronization      * all the way down the stack.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLUSH_CASCADE_SYNC
init|=
literal|2
decl_stmt|;
comment|/**      * A synchronization type that results in cascading rollback of changes through the      * DataChannel stack.      */
specifier|public
specifier|static
specifier|final
name|int
name|ROLLBACK_CASCADE_SYNC
init|=
literal|3
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|EventSubject
name|GRAPH_CHANGED_SUBJECT
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|DataChannel
operator|.
name|class
argument_list|,
literal|"graphChanged"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|EventSubject
name|GRAPH_FLUSHED_SUBJECT
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|DataChannel
operator|.
name|class
argument_list|,
literal|"graphFlushed"
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|EventSubject
name|GRAPH_ROLLEDBACK_SUBJECT
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|DataChannel
operator|.
name|class
argument_list|,
literal|"graphRolledback"
argument_list|)
decl_stmt|;
comment|/**      * Returns an EventManager associated with this channel. Channel may return null if      * EventManager is not available for any reason.      */
name|EventManager
name|getEventManager
parameter_list|()
function_decl|;
comment|/**      * Returns an EntityResolver instance that contains runtime mapping information.      */
name|EntityResolver
name|getEntityResolver
parameter_list|()
function_decl|;
comment|/**      * Executes a query, using provided<em>context</em> to register persistent objects      * if query returns any objects.      *       * @param originatingContext an ObjectContext that originated the query, used to      *            register result objects.      * @return a generic response object that encapsulates result of the execution.      */
name|QueryResponse
name|onQuery
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|Query
name|query
parameter_list|)
function_decl|;
comment|/**      * Processes synchronization request from a child ObjectContext, returning a GraphDiff      * that describes changes to objects made on the receiving end as a result of      * syncronization.      * @param originatingContext an ObjectContext that initiated the sync. Can be null.      * @param changes diff from the context that initiated the sync.      * @param syncType One of {@link #FLUSH_NOCASCADE_SYNC}, {@link #FLUSH_CASCADE_SYNC},      *            {@link #ROLLBACK_CASCADE_SYNC}.      */
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
function_decl|;
block|}
end_interface

end_unit

