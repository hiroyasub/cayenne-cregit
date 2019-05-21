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
name|swing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
import|;
end_import

begin_comment
comment|/**  * Defines API of a binding sitting between a Swing widget and domain model, synchronizing  * the values between the two. Parent part of the binding is called "context"as it is used  * as a context of binding expressions. Child of the binding is a bound component that is  * being synchronized with the context.  *   */
end_comment

begin_interface
specifier|public
interface|interface
name|ObjectBinding
block|{
name|Component
name|getView
parameter_list|()
function_decl|;
name|Object
name|getContext
parameter_list|()
function_decl|;
name|void
name|setContext
parameter_list|(
name|Object
name|object
parameter_list|)
function_decl|;
name|void
name|updateView
parameter_list|()
function_decl|;
name|BindingDelegate
name|getDelegate
parameter_list|()
function_decl|;
name|void
name|setDelegate
parameter_list|(
name|BindingDelegate
name|delegate
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

