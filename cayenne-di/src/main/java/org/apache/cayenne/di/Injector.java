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
name|di
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
name|di
operator|.
name|DIRuntimeException
import|;
end_import

begin_comment
comment|/**  * A facade to the Cayenne DI container. To create an injector use {@link DIBootstrap}  * static methods.  *   * @since 3.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|Injector
block|{
comment|/**      * Returns a service instance bound in the container for a specific type. Throws      *{@link DIRuntimeException} if the type is not bound, or an instance can not be      * created.      */
parameter_list|<
name|T
parameter_list|>
name|T
name|getInstance
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|DIRuntimeException
function_decl|;
comment|/**      * Returns a service instance bound in the container for a specific binding key.      * Throws {@link DIRuntimeException} if the key is not bound, or an instance can      * not be created.      */
parameter_list|<
name|T
parameter_list|>
name|T
name|getInstance
parameter_list|(
name|Key
argument_list|<
name|T
argument_list|>
name|key
parameter_list|)
throws|throws
name|DIRuntimeException
function_decl|;
parameter_list|<
name|T
parameter_list|>
name|Provider
argument_list|<
name|T
argument_list|>
name|getProvider
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|DIRuntimeException
function_decl|;
parameter_list|<
name|T
parameter_list|>
name|Provider
argument_list|<
name|T
argument_list|>
name|getProvider
parameter_list|(
name|Key
argument_list|<
name|T
argument_list|>
name|key
parameter_list|)
throws|throws
name|DIRuntimeException
function_decl|;
comment|/**      * Performs field injection on a given object, ignoring constructor injection. Since      * Cayenne DI injector returns fully injected objects, this method is rarely used      * directly.      *<p>      * Note that using this method inside a custom DI {@link Provider} will most likely      * result in double injection, as custom provider is wrapped in a field-injecting      * provider by the DI container. Instead custom providers must initialize object      * properties manually, obtaining dependencies from Injector.      */
name|void
name|injectMembers
parameter_list|(
name|Object
name|object
parameter_list|)
function_decl|;
comment|/**      * A lifecycle method that let's the injector's services to clean up their state and      * release resources. This method would normally generate a scope end event for the      * injector's one and only singleton scope.      */
name|void
name|shutdown
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

