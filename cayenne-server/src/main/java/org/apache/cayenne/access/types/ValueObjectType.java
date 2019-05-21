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
name|access
operator|.
name|types
package|;
end_package

begin_comment
comment|/**  * Descriptor and serialization helper for custom value objects that can be safely stored in the DB.  * Lightweight alternative for the {@link ExtendedType}.  *  * @param<V> type of user's custom object.  * @param<T> type that custom object will be serialized to/from  *            should be backed by appropriate {@link ExtendedType}.  *  * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|ValueObjectType
parameter_list|<
name|V
parameter_list|,
name|T
parameter_list|>
block|{
comment|/**      * @return base type used to serialize<b>V</b> objects to.      */
name|Class
argument_list|<
name|T
argument_list|>
name|getTargetType
parameter_list|()
function_decl|;
comment|/**      * @return type of Objects described by this ValueObjectType.      */
name|Class
argument_list|<
name|V
argument_list|>
name|getValueType
parameter_list|()
function_decl|;
comment|/**      * @param value of type T      * @return java object      */
name|V
name|toJavaObject
parameter_list|(
name|T
name|value
parameter_list|)
function_decl|;
comment|/**      * @param object java object      * @return value of type T      */
name|T
name|fromJavaObject
parameter_list|(
name|V
name|object
parameter_list|)
function_decl|;
comment|/**      * Returned value should be same for objects that is logically equal.      *      * @return String representation usable for cache.      */
name|String
name|toCacheKey
parameter_list|(
name|V
name|object
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

