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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * Provides a level of indirection for property value access, most often used for deferred  * faulting of to-one relationships. A ValueHolder abstracts how a property value is  * obtained (fetched from DB, etc.), thus simplifying design of an object that uses it.  *<p>  * Here is an example of a bean property implemented using ValueHolder:  *</p>  *   *<pre>  * protected ValueHolder someProperty;  *   * public SomeClass getSomeProperty() {  *     return (SomeClass) somePropertyHolder.getValue(SomeClass.class);  * }  *   * public void setSomeProperty(SomeClass newValue) {  *     somePropertyHolder.setValue(SomeClass.class, newValue);  * }  *</pre>  *   * @since 1.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|ValueHolder
parameter_list|<
name|E
parameter_list|>
extends|extends
name|Serializable
block|{
comment|/**      * Returns an object stored by this ValueHolder.      */
name|E
name|getValue
parameter_list|()
throws|throws
name|CayenneRuntimeException
function_decl|;
comment|/**      * Retrieves ValueHolder value without triggering fault resolution.      */
name|E
name|getValueDirectly
parameter_list|()
throws|throws
name|CayenneRuntimeException
function_decl|;
comment|/**      * Sets an object stored by this ValueHolder.      *       * @param value a new value of the ValueHolder.      * @return a previous value saved in the ValueHolder.      */
name|E
name|setValue
parameter_list|(
name|E
name|value
parameter_list|)
throws|throws
name|CayenneRuntimeException
function_decl|;
comment|/**      * Sets ValueHolder vaue without triggering fault resolution.      */
name|E
name|setValueDirectly
parameter_list|(
name|E
name|value
parameter_list|)
throws|throws
name|CayenneRuntimeException
function_decl|;
comment|/**      * Returns true if the internal value is not yet resolved.      */
name|boolean
name|isFault
parameter_list|()
function_decl|;
comment|/**      * Turns a ValueHolder into a fault.      */
name|void
name|invalidate
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

