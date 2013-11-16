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
name|configuration
operator|.
name|server
package|;
end_package

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
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|DataDomain
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
name|DataNode
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
name|CayenneRuntime
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
name|TransactionManager
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
name|TransactionalOperation
import|;
end_import

begin_comment
comment|/**  * An object representing Cayenne server-stack that connects directly to the  * database via JDBC. This is an entry point for user applications to access  * Cayenne, which encapsulates the dependency injection internals. The term  * "server" is used as opposed to ROP "client" (see {@link ClientRuntime}). Any  * application, desktop, server, etc. that has a direct JDBC connection should  * be using this runtime.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|ServerRuntime
extends|extends
name|CayenneRuntime
block|{
specifier|private
specifier|static
name|Module
name|mainModule
parameter_list|(
name|String
modifier|...
name|configurationLocations
parameter_list|)
block|{
return|return
operator|new
name|ServerModule
argument_list|(
name|configurationLocations
argument_list|)
return|;
block|}
comment|/**      * Creates a server runtime configuring it with a standard set of services      * contained in {@link ServerModule}. CayenneServerModule is created with      * provided 'configurationLocation'. An optional array of extra modules may      * contain service overrides and/or user services.      */
specifier|public
name|ServerRuntime
parameter_list|(
name|String
name|configurationLocation
parameter_list|,
name|Module
modifier|...
name|extraModules
parameter_list|)
block|{
name|super
argument_list|(
name|mergeModules
argument_list|(
name|mainModule
argument_list|(
name|configurationLocation
argument_list|)
argument_list|,
name|extraModules
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a server runtime configuring it with a standard set of services      * contained in {@link ServerModule}. CayenneServerModule is created with      * one or more 'configurationLocations'. An optional array of extra modules      * may contain service overrides and/or user services.      */
specifier|public
name|ServerRuntime
parameter_list|(
name|String
index|[]
name|configurationLocations
parameter_list|,
name|Module
modifier|...
name|extraModules
parameter_list|)
block|{
name|super
argument_list|(
name|mergeModules
argument_list|(
name|mainModule
argument_list|(
name|configurationLocations
argument_list|)
argument_list|,
name|extraModules
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Runs provided operation wrapped in a single transaction. Transaction      * handling delegated to the internal {@link TransactionManager}. Nested      * calls to 'performInTransaction' are safe and attached to the same      * in-progress transaction. TransactionalOperation can be some arbitrary      * user code, which most often than not will consist of multiple Cayenne      * operations.      *       * @since 3.2      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|performInTransaction
parameter_list|(
name|TransactionalOperation
argument_list|<
name|T
argument_list|>
name|op
parameter_list|)
block|{
name|TransactionManager
name|tm
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|TransactionManager
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|tm
operator|.
name|performInTransaction
argument_list|(
name|op
argument_list|)
return|;
block|}
comment|/**      * Returns the main runtime DataDomain. Note that by default the returned      * DataDomain is the same as the main DataChannel returned by      * {@link #getChannel()}. Although users may redefine DataChannel provider      * in the DI registry, for instance to decorate this DataDomain with a      * custom wrapper.      */
specifier|public
name|DataDomain
name|getDataDomain
parameter_list|()
block|{
return|return
name|injector
operator|.
name|getInstance
argument_list|(
name|DataDomain
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Provides access to the JDBC DataSource assigned to a given DataNode. A      * null argument will work if there's only one DataNode configured.      *<p>      * Normally Cayenne applications don't need to access DataSource or any      * other JDBC code directly, however in some unusual conditions it may be      * needed, and this method provides a shortcut to raw JDBC.      */
specifier|public
name|DataSource
name|getDataSource
parameter_list|(
name|String
name|dataNodeName
parameter_list|)
block|{
name|DataDomain
name|domain
init|=
name|getDataDomain
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataNodeName
operator|!=
literal|null
condition|)
block|{
name|DataNode
name|node
init|=
name|domain
operator|.
name|getDataNode
argument_list|(
name|dataNodeName
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown DataNode name: "
operator|+
name|dataNodeName
argument_list|)
throw|;
block|}
return|return
name|node
operator|.
name|getDataSource
argument_list|()
return|;
block|}
else|else
block|{
name|Collection
argument_list|<
name|DataNode
argument_list|>
name|nodes
init|=
name|domain
operator|.
name|getDataNodes
argument_list|()
decl_stmt|;
if|if
condition|(
name|nodes
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"If DataNode name is not specified, DataDomain must have exactly 1 DataNode. Actual node count: "
operator|+
name|nodes
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|nodes
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getDataSource
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

