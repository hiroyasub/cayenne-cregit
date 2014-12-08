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
name|di
operator|.
name|BeforeScopeEnd
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
name|DIBootstrap
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
name|Injector
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

begin_comment
comment|/**  * A superclass of various Cayenne runtime stacks. A Runtime is the main access  * point to Cayenne for a user application. It provides a default Cayenne  * configuration as well as a way to customize this configuration via a built-in  * dependency injection (DI) container. In fact implementation-wise, Runtime  * object is just a convenience thin wrapper around a DI {@link Injector}.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|CayenneRuntime
block|{
comment|/** 	 * A holder of an Injector bound to the current thread. Used mainly to allow 	 * serializable contexts to attach to correct Cayenne stack on 	 * deserialization. 	 *  	 * @since 3.1 	 */
specifier|protected
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|Injector
argument_list|>
name|threadInjector
init|=
operator|new
name|ThreadLocal
argument_list|<
name|Injector
argument_list|>
argument_list|()
decl_stmt|;
comment|/** 	 * Binds a DI {@link Injector} bound to the current thread. It is primarily 	 * intended for deserialization of ObjectContexts. 	 *  	 * @since 3.1 	 */
specifier|public
specifier|static
name|void
name|bindThreadInjector
parameter_list|(
name|Injector
name|injector
parameter_list|)
block|{
name|threadInjector
operator|.
name|set
argument_list|(
name|injector
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Returns the {@link Injector} bound to the current thread. Will return 	 * null if none is bound. 	 *  	 * @since 3.1 	 */
specifier|public
specifier|static
name|Injector
name|getThreadInjector
parameter_list|()
block|{
return|return
name|threadInjector
operator|.
name|get
argument_list|()
return|;
block|}
specifier|protected
name|Injector
name|injector
decl_stmt|;
specifier|protected
name|Module
name|module
decl_stmt|;
comment|/** 	 * Creates a CayenneRuntime with configuration based on the supplied array 	 * of DI modules. 	 */
specifier|public
name|CayenneRuntime
parameter_list|(
name|Module
name|module
parameter_list|)
block|{
name|this
operator|.
name|module
operator|=
name|module
expr_stmt|;
name|this
operator|.
name|injector
operator|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
name|module
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Returns an array of modules used to initialize this runtime. 	 *  	 * @deprecated since 4.0. We only keep one module now, so use 	 *             {@link #getModule()}. 	 */
annotation|@
name|Deprecated
specifier|public
name|Module
index|[]
name|getModules
parameter_list|()
block|{
return|return
operator|new
name|Module
index|[]
block|{
name|module
block|}
return|;
block|}
comment|/** 	 *  	 * Returns the module used to initialize this runtime. 	 *  	 * @since 4.0 	 */
specifier|public
name|Module
name|getModule
parameter_list|()
block|{
return|return
name|module
return|;
block|}
comment|/** 	 * Returns DI injector used by this runtime. 	 */
specifier|public
name|Injector
name|getInjector
parameter_list|()
block|{
return|return
name|injector
return|;
block|}
comment|/** 	 * Shuts down the DI injector of this runtime, giving all services that need 	 * to release some resources a chance to do that. 	 */
comment|// the following annotation is for environments that manage CayenneRuntimes
comment|// within
comment|// another DI registry (e.g. unit tests)
annotation|@
name|BeforeScopeEnd
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|injector
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * Returns the runtime {@link DataChannel}. 	 */
specifier|public
name|DataChannel
name|getChannel
parameter_list|()
block|{
return|return
name|injector
operator|.
name|getInstance
argument_list|(
name|DataChannel
operator|.
name|class
argument_list|)
return|;
block|}
comment|/** 	 * Returns a new ObjectContext instance based on the runtime's main 	 * DataChannel. 	 *  	 * @since 4.0 	 */
specifier|public
name|ObjectContext
name|newContext
parameter_list|()
block|{
return|return
name|injector
operator|.
name|getInstance
argument_list|(
name|ObjectContextFactory
operator|.
name|class
argument_list|)
operator|.
name|createContext
argument_list|()
return|;
block|}
comment|/** 	 * Returns a new ObjectContext which is a child of the specified 	 * DataChannel. This method is used for creation of nested ObjectContexts, 	 * with parent ObjectContext passed as an argument. 	 *  	 * @since 4.0 	 */
specifier|public
name|ObjectContext
name|newContext
parameter_list|(
name|DataChannel
name|parentChannel
parameter_list|)
block|{
return|return
name|injector
operator|.
name|getInstance
argument_list|(
name|ObjectContextFactory
operator|.
name|class
argument_list|)
operator|.
name|createContext
argument_list|(
name|parentChannel
argument_list|)
return|;
block|}
comment|/** 	 * @deprecated since 3.1 use better named {@link #newContext()} instead. 	 */
annotation|@
name|Deprecated
specifier|public
name|ObjectContext
name|getContext
parameter_list|()
block|{
return|return
name|newContext
argument_list|()
return|;
block|}
comment|/** 	 * @deprecated since 3.1 use better named {@link #newContext(DataChannel)} 	 *             instead. 	 */
annotation|@
name|Deprecated
specifier|public
name|ObjectContext
name|getContext
parameter_list|(
name|DataChannel
name|parentChannel
parameter_list|)
block|{
return|return
name|newContext
argument_list|(
name|parentChannel
argument_list|)
return|;
block|}
block|}
end_class

end_unit

