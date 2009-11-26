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
name|di
package|;
end_package

begin_comment
comment|/**  * An object passed to a {@link Module} by the DI container during initialization, that  * provides the API for the module to bind its services to the container.  *   * @since 3.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|Binder
block|{
comment|/**      * Starts a binding of a specific interface. Binding should continue using returned      * BindingBuilder.      */
parameter_list|<
name|T
parameter_list|>
name|BindingBuilder
argument_list|<
name|T
argument_list|>
name|bind
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|)
function_decl|;
comment|/**      * Starts a binding of a map "configuration" that will be injected into an      * implementation class for the specified "interfaceType" parameter. Configurations      * can only be injected via a constructor. An object can take at most one      * configuration object via a constructor.      */
parameter_list|<
name|T
parameter_list|>
name|MapBuilder
argument_list|<
name|T
argument_list|>
name|bindMap
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|)
function_decl|;
comment|/**      * Starts a binding of a list "configuration" that will be injected into an      * implementation class for the specified "interfaceType" parameter. Configurations      * can only be injected via a constructor. An object can take at most one      * configuration object via a constructor.      */
parameter_list|<
name|T
parameter_list|>
name|ListBuilder
argument_list|<
name|T
argument_list|>
name|bindList
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

