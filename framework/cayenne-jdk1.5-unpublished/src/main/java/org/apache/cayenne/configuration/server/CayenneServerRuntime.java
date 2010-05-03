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

begin_comment
comment|/**  * A runtime representing Cayenne server-stack that connects directly to the database via  * JDBC. The term "server" is used as opposed to ROP "client". Any application, desktop,  * server, etc. that has a direct JDBC connection should be using this runtime.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|CayenneServerRuntime
extends|extends
name|CayenneRuntime
block|{
comment|/**      * Creates a server runtime object based on XML configuration file specified via      * 'configurationLocation' parameter. Configures the runtime with a standard set of      * services contained in {@link CayenneServerModule}.      */
specifier|public
name|CayenneServerRuntime
parameter_list|(
name|String
name|configurationLocation
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|CayenneServerModule
argument_list|(
name|configurationLocation
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a server runtime object based on an array of custom DI modules. When      * implementing custom modules, refer to {@link CayenneServerModule} for the minimal      * set of services expected by Cayenne. The easiest way to do it is to pass      * {@link CayenneServerModule} or its subclass as one of the modules to this      * constructor.      */
specifier|public
name|CayenneServerRuntime
parameter_list|(
name|Module
modifier|...
name|modules
parameter_list|)
block|{
name|super
argument_list|(
name|modules
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a server runtime object based on a collection of DI modules. When      * implementing custom modules, refer to {@link CayenneServerModule} for the minimal      * set of services expected by Cayenne. The easiest way to do it is to pass      * {@link CayenneServerModule} or its subclass as one of the modules to this      * constructor.      */
specifier|public
name|CayenneServerRuntime
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
comment|/**      * Returns the main runtime DataDomain. Note that by default the returned DataDomain      * is the same as the main DataChannel returned by {@link #getDataChannel()}. Although      * users may redefine DataChannel provider in the DI registry, for instance to      * decorate this DataDomain with a custom wrapper.      */
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
block|}
end_class

end_unit

