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

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyChangeListener
import|;
end_import

begin_comment
comment|/**  * An API for a binding child that allows to establish bindings to custom Swing  * components.  *   * @since 1.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|BoundComponent
block|{
comment|/**      * Processes value pushed from parent.      */
name|void
name|bindingUpdated
parameter_list|(
name|String
name|expression
parameter_list|,
name|Object
name|newValue
parameter_list|)
function_decl|;
comment|/**      * Adds a property change listener to be notified of property updates.      */
comment|// TODO: andrus, 04/8/2006 - declaring this method in the interface is redundant...
comment|// property "add*" methods can be discoverd via Bean introspection. See
comment|// BeanActionBinding for details.
name|void
name|addPropertyChangeListener
parameter_list|(
name|String
name|expression
parameter_list|,
name|PropertyChangeListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * Returns bound view component.      */
name|Component
name|getView
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

