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
name|reflect
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * Defines bean property API used by Cayenne to access object data, do faulting  * and graph maintenance tasks.  *   * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|PropertyDescriptor
block|{
comment|/**      * Returns property name.      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Returns a property value of an object without disturbing the object fault      * status.      */
name|Object
name|readPropertyDirectly
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
function_decl|;
comment|/**      * Returns a property value, inflating unresolved object if need.      */
name|Object
name|readProperty
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
function_decl|;
comment|/**      * Sets a property value of an object without disturbing the object fault      * status. Old value of the property is specified as a hint and can be      * ignored by the property implementor.      */
name|void
name|writePropertyDirectly
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
throws|throws
name|PropertyException
function_decl|;
comment|/**      * Sets a property value, inflating unresolved object if need. Old value of      * the property is specified as a hint and can be ignored by the property      * implementor.      */
name|void
name|writeProperty
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
throws|throws
name|PropertyException
function_decl|;
comment|/**      * A visitor accept method.      *       * @return a status returned by the corresponding callback method of the      *         visitor. It serves as an indication of whether peer properties      *         processing is still needed.      */
name|boolean
name|visit
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
function_decl|;
comment|/**      * If a property is implemented as a ValueHolder, this operation would      * create an unfaulted value holder and inject it into the object, if an      * object doesn't have it set yet.      */
name|void
name|injectValueHolder
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
function_decl|;
comment|/**      * Allows to use special logic to compare values for equality      * as in rare cases it is not sufficient to use the default equals() method.      * Default implementation uses {@link Util#nullSafeEquals(Object, Object)} method.      *      * @param value1 to compare      * @param value2 to compare      * @return true if given values are equal      *      * @since 4.2      */
specifier|default
name|boolean
name|equals
parameter_list|(
name|Object
name|value1
parameter_list|,
name|Object
name|value2
parameter_list|)
block|{
return|return
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|value1
argument_list|,
name|value2
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

