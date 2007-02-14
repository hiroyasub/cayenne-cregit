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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|LifecycleListener
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
name|PersistenceState
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
name|access
operator|.
name|event
operator|.
name|DataContextEvent
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
name|access
operator|.
name|event
operator|.
name|DataContextTransactionEventListener
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
name|access
operator|.
name|event
operator|.
name|DataObjectTransactionEventListener
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

begin_comment
comment|/**  * Handles DataContext events on domain flush.  *   * @since 1.2  * @author Andrus Adamchik  * @deprecated since 3.0M1 in favor of {@link LifecycleListener}. Will be removed in  *             later 3.0 milestones.  */
end_comment

begin_class
class|class
name|DataContextFlushEventHandler
implements|implements
name|DataContextTransactionEventListener
block|{
name|List
name|objectsToNotify
decl_stmt|;
name|DataContext
name|originatingContext
decl_stmt|;
name|DataContextFlushEventHandler
parameter_list|(
name|DataContext
name|originatingContext
parameter_list|)
block|{
name|this
operator|.
name|originatingContext
operator|=
name|originatingContext
expr_stmt|;
name|this
operator|.
name|objectsToNotify
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
comment|// remember objects to notify of commit events
name|Iterator
name|it
init|=
name|originatingContext
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getObjectIterator
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
name|Persistent
name|object
init|=
operator|(
name|Persistent
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|DataObjectTransactionEventListener
condition|)
block|{
switch|switch
condition|(
name|object
operator|.
name|getPersistenceState
argument_list|()
condition|)
block|{
case|case
name|PersistenceState
operator|.
name|NEW
case|:
case|case
name|PersistenceState
operator|.
name|MODIFIED
case|:
case|case
name|PersistenceState
operator|.
name|DELETED
case|:
name|this
operator|.
name|objectsToNotify
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
name|void
name|registerForDataContextEvents
parameter_list|()
block|{
name|EventManager
name|eventManager
init|=
name|originatingContext
operator|.
name|getEventManager
argument_list|()
decl_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"dataContextWillCommit"
argument_list|,
name|DataContextEvent
operator|.
name|class
argument_list|,
name|DataContext
operator|.
name|WILL_COMMIT
argument_list|,
name|originatingContext
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"dataContextDidCommit"
argument_list|,
name|DataContextEvent
operator|.
name|class
argument_list|,
name|DataContext
operator|.
name|DID_COMMIT
argument_list|,
name|originatingContext
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|addListener
argument_list|(
name|this
argument_list|,
literal|"dataContextDidRollback"
argument_list|,
name|DataContextEvent
operator|.
name|class
argument_list|,
name|DataContext
operator|.
name|DID_ROLLBACK
argument_list|,
name|originatingContext
argument_list|)
expr_stmt|;
block|}
name|void
name|unregisterFromDataContextEvents
parameter_list|()
block|{
name|EventManager
name|eventManager
init|=
name|originatingContext
operator|.
name|getEventManager
argument_list|()
decl_stmt|;
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|,
name|DataContext
operator|.
name|WILL_COMMIT
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|,
name|DataContext
operator|.
name|DID_COMMIT
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|removeListener
argument_list|(
name|this
argument_list|,
name|DataContext
operator|.
name|DID_ROLLBACK
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dataContextWillCommit
parameter_list|(
name|DataContextEvent
name|event
parameter_list|)
block|{
name|Iterator
name|iter
init|=
name|objectsToNotify
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
operator|(
operator|(
name|DataObjectTransactionEventListener
operator|)
name|iter
operator|.
name|next
argument_list|()
operator|)
operator|.
name|willCommit
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|dataContextDidCommit
parameter_list|(
name|DataContextEvent
name|event
parameter_list|)
block|{
name|Iterator
name|iter
init|=
name|objectsToNotify
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
operator|(
operator|(
name|DataObjectTransactionEventListener
operator|)
name|iter
operator|.
name|next
argument_list|()
operator|)
operator|.
name|didCommit
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|dataContextDidRollback
parameter_list|(
name|DataContextEvent
name|event
parameter_list|)
block|{
comment|// do nothing for now
block|}
block|}
end_class

end_unit

