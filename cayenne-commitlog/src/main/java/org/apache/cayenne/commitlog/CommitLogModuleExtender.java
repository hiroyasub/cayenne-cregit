begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|commitlog
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
name|commitlog
operator|.
name|meta
operator|.
name|AnnotationCommitLogEntityFactory
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
name|commitlog
operator|.
name|meta
operator|.
name|CommitLogEntity
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
name|commitlog
operator|.
name|meta
operator|.
name|CommitLogEntityFactory
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
name|commitlog
operator|.
name|meta
operator|.
name|IncludeAllCommitLogEntityFactory
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
name|configuration
operator|.
name|server
operator|.
name|ServerModule
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
name|di
operator|.
name|ListBuilder
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
name|di
operator|.
name|Module
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
name|tx
operator|.
name|TransactionFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|HashSet
import|;
end_import

begin_comment
comment|/**  * A builder of a custom extensions module for {@link CommitLogModule} that customizes its services and installs  * application-specific commit log listeners.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|CommitLogModuleExtender
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CommitLogModuleExtender
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|CommitLogEntityFactory
argument_list|>
name|entityFactoryType
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|CommitLogListener
argument_list|>
argument_list|>
name|listenerTypes
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|CommitLogListener
argument_list|>
name|listenerInstances
decl_stmt|;
specifier|private
name|boolean
name|excludeFromTransaction
decl_stmt|;
name|CommitLogModuleExtender
parameter_list|()
block|{
name|entityFactory
argument_list|(
name|IncludeAllCommitLogEntityFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|listenerTypes
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|listenerInstances
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|CommitLogModuleExtender
name|addListener
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|CommitLogListener
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|listenerTypes
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|CommitLogModuleExtender
name|addListener
parameter_list|(
name|CommitLogListener
name|instance
parameter_list|)
block|{
name|this
operator|.
name|listenerInstances
operator|.
name|add
argument_list|(
name|instance
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * If called, events will be dispatched outside of the main commit      * transaction. By default events are dispatched within the transaction, so      * listeners can commit their code together with the main commit.      */
specifier|public
name|CommitLogModuleExtender
name|excludeFromTransaction
parameter_list|()
block|{
name|this
operator|.
name|excludeFromTransaction
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Installs entity filter that would only include entities annotated with      * {@link CommitLog} on the callbacks. Also {@link CommitLog#confidential()}      * properties will be obfuscated and {@link CommitLog#ignoredProperties()} -      * excluded from the change collection.      */
specifier|public
name|CommitLogModuleExtender
name|commitLogAnnotationEntitiesOnly
parameter_list|()
block|{
return|return
name|entityFactory
argument_list|(
name|AnnotationCommitLogEntityFactory
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Installs a custom factory for {@link CommitLogEntity} objects that      * allows implementors to use their own annotations, etc.      */
specifier|public
name|CommitLogModuleExtender
name|entityFactory
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|CommitLogEntityFactory
argument_list|>
name|entityFactoryType
parameter_list|)
block|{
name|this
operator|.
name|entityFactoryType
operator|=
name|entityFactoryType
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Creates a DI module that would install {@link CommitLogFilter} and its      * listeners in Cayenne.      */
specifier|public
name|Module
name|module
parameter_list|()
block|{
return|return
name|binder
lambda|->
block|{
if|if
condition|(
name|listenerTypes
operator|.
name|isEmpty
argument_list|()
operator|&&
name|listenerInstances
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"No listeners configured. Skipping CommitLogFilter registration"
argument_list|)
expr_stmt|;
return|return;
block|}
name|binder
operator|.
name|bind
argument_list|(
name|CommitLogEntityFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|entityFactoryType
argument_list|)
expr_stmt|;
name|ListBuilder
argument_list|<
name|CommitLogListener
argument_list|>
name|listeners
init|=
name|CommitLogModule
operator|.
name|contributeListeners
argument_list|(
name|binder
argument_list|)
operator|.
name|addAll
argument_list|(
name|listenerInstances
argument_list|)
decl_stmt|;
comment|// types have to be added one-by-one
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|CommitLogListener
argument_list|>
name|type
range|:
name|listenerTypes
control|)
block|{
name|listeners
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|excludeFromTransaction
condition|)
block|{
name|ServerModule
operator|.
name|contributeDomainSyncFilters
argument_list|(
name|binder
argument_list|)
operator|.
name|addAfter
argument_list|(
name|CommitLogFilter
operator|.
name|class
argument_list|,
name|TransactionFilter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ServerModule
operator|.
name|contributeDomainSyncFilters
argument_list|(
name|binder
argument_list|)
operator|.
name|insertBefore
argument_list|(
name|CommitLogFilter
operator|.
name|class
argument_list|,
name|TransactionFilter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

