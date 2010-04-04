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
name|runtime
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
comment|/**  * A superclass of possible Cayenne runtime objects. A CayenneRuntime is the main access  * point to a given Cayenne stack. It provides a default Cayenne configuration as well as  * a way to customize this configuration via a built in dependency injection container.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|CayenneRuntime
block|{
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|Injector
name|injector
decl_stmt|;
specifier|protected
name|Module
index|[]
name|modules
decl_stmt|;
comment|/**      * Creates a CayenneRuntime with configuration based on supplied array of DI modules.      */
specifier|public
name|CayenneRuntime
parameter_list|(
name|String
name|name
parameter_list|,
name|Module
modifier|...
name|modules
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null runtime name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|modules
operator|==
literal|null
condition|)
block|{
name|modules
operator|=
operator|new
name|Module
index|[
literal|0
index|]
expr_stmt|;
block|}
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|modules
operator|=
name|modules
expr_stmt|;
name|this
operator|.
name|injector
operator|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
name|modules
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns runtime name. By default a name of Cayenne project XML file contains a      * runtime name in it in the form "cayenne-<name>.xml".      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Returns an array of modules used to initialize this runtime.      */
specifier|public
name|Module
index|[]
name|getModules
parameter_list|()
block|{
return|return
name|modules
return|;
block|}
comment|/**      * Returns the runtime {@link DataChannel}.      */
specifier|public
name|DataChannel
name|getDataChannel
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
comment|/**      * Creates and returns an ObjectContext based on the runtime DataChannel.      */
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
name|ObjectContext
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

