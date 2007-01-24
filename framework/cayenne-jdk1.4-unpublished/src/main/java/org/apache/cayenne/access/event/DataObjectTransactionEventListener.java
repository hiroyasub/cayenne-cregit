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
operator|.
name|event
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventListener
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
name|LifecycleEventCallback
import|;
end_import

begin_comment
comment|/**  * This interface declares methods that DataObject classes can implement to be notified  * about transactions of their DataContext. Note: explicit registration with EventManager  * is not necessary, since the events are simply forwarded by ContextCommitObserver;  * stricly speaking these methods are just regular 'callbacks'. The event argument is  * passed along for convenience.  *   * @deprecated since 3.0M1 in favor of {@link LifecycleEventCallback}. Will be removed in  *             later 3.0 milestones.  */
end_comment

begin_interface
specifier|public
interface|interface
name|DataObjectTransactionEventListener
extends|extends
name|EventListener
block|{
specifier|public
name|void
name|willCommit
parameter_list|(
name|DataContextEvent
name|event
parameter_list|)
function_decl|;
specifier|public
name|void
name|didCommit
parameter_list|(
name|DataContextEvent
name|event
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

