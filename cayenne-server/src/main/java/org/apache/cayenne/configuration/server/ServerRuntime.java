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
name|Binder
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
name|TransactionListener
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
name|Collection
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
import|;
end_import

begin_comment
comment|/**  * Object representing Cayenne stack. Serves as an entry point to Cayenne for user applications and a factory of ObjectContexts.  * Implementation is a thin wrapper of the dependency injection container.  *<p>The "Server" prefix in the name is in contrast to ROP "client" (that is started via ClientRuntime). So  * ServerRuntime is the default Cayenne stack that you should be using in all apps with the exception of client-side ROP.</p>  *  * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|ServerRuntime
extends|extends
name|CayenneRuntime
block|{
comment|/**      * Creates a builder of ServerRuntime.      *      * @return a builder of ServerRuntime.      * @since 4.0      */
specifier|public
specifier|static
name|ServerRuntimeBuilder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|ServerRuntimeBuilder
argument_list|()
return|;
block|}
comment|/**      * Creates a builder of ServerRuntime.      *      * @param name optional symbolic name of the created runtime.      * @return a named builder of ServerRuntime.      */
specifier|public
specifier|static
name|ServerRuntimeBuilder
name|builder
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|ServerRuntimeBuilder
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
specifier|private
specifier|static
name|Collection
argument_list|<
name|Module
argument_list|>
name|collectModules
parameter_list|(
specifier|final
name|String
index|[]
name|configurationLocations
parameter_list|,
name|Module
modifier|...
name|extraModules
parameter_list|)
block|{
name|Collection
argument_list|<
name|Module
argument_list|>
name|modules
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|ServerModule
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|configurationLocations
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|modules
operator|.
name|add
argument_list|(
operator|new
name|Module
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|ListBuilder
argument_list|<
name|String
argument_list|>
name|locationsBinder
init|=
name|ServerModule
operator|.
name|contributeProjectLocations
argument_list|(
name|binder
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|c
range|:
name|configurationLocations
control|)
block|{
name|locationsBinder
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|extraModules
operator|!=
literal|null
condition|)
block|{
name|modules
operator|.
name|addAll
argument_list|(
name|asList
argument_list|(
name|extraModules
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|modules
return|;
block|}
comment|/**      * Creates a server runtime configuring it with a standard set of services      * contained in {@link ServerModule}. CayenneServerModule is created with      * provided 'configurationLocation'. An optional array of extra modules may      * contain service overrides and/or user services.      *      * @deprecated since 4.0 use {@link ServerRuntime#builder()}.      */
annotation|@
name|Deprecated
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
name|this
argument_list|(
name|collectModules
argument_list|(
operator|new
name|String
index|[]
block|{
name|configurationLocation
block|}
argument_list|,
name|extraModules
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a server runtime configuring it with a standard set of services      * contained in {@link ServerModule}. CayenneServerModule is created with      * one or more 'configurationLocations'. An optional array of extra modules      * may contain service overrides and/or user services.      *      * @deprecated since 4.0 use {@link ServerRuntime#builder()}.      */
annotation|@
name|Deprecated
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
name|this
argument_list|(
name|collectModules
argument_list|(
name|configurationLocations
argument_list|,
name|extraModules
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a server runtime configuring it with a standard set of services      * contained in {@link ServerModule}. CayenneServerModule is created with      * one or more 'configurationLocations'. An optional array of extra modules      * may contain service overrides and/or user services.      *      * @since 4.0      */
specifier|protected
name|ServerRuntime
parameter_list|(
name|Collection
argument_list|<
name|Module
argument_list|>
name|modules
parameter_list|)
block|{
name|super
argument_list|(
name|modules
argument_list|)
expr_stmt|;
block|}
comment|/**      * Runs provided operation wrapped in a single transaction. Transaction      * handling delegated to the internal {@link TransactionManager}. Nested      * calls to 'performInTransaction' are safe and attached to the same      * in-progress transaction. TransactionalOperation can be some arbitrary      * user code, which most often than not will consist of multiple Cayenne      * operations.      *      * @since 4.0      */
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
comment|/**      * Runs provided operation wrapped in a single transaction. Transaction      * handling delegated to the internal {@link TransactionManager}. Nested      * calls to 'performInTransaction' are safe and attached to the same      * in-progress transaction. TransactionalOperation can be some arbitrary      * user code, which most often than not will consist of multiple Cayenne      * operations.      *      * @since 4.0      */
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
parameter_list|,
name|TransactionListener
name|callback
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
argument_list|,
name|callback
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
comment|/**      * Returns a default DataSource for this runtime. If no default DataSource      * exists, an exception is thrown.      *      * @since 4.0      */
specifier|public
name|DataSource
name|getDataSource
parameter_list|()
block|{
name|DataDomain
name|domain
init|=
name|getDataDomain
argument_list|()
decl_stmt|;
name|DataNode
name|defaultNode
init|=
name|domain
operator|.
name|getDefaultNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|defaultNode
operator|==
literal|null
condition|)
block|{
name|int
name|s
init|=
name|domain
operator|.
name|getDataNodes
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No DataSources configured"
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No default DataSource configured. You can get explicitly named DataSource by using 'getDataSource(String)'"
argument_list|)
throw|;
block|}
block|}
return|return
name|defaultNode
operator|.
name|getDataSource
argument_list|()
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
operator|==
literal|null
condition|)
block|{
return|return
name|getDataSource
argument_list|()
return|;
block|}
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
block|}
end_class

end_unit

