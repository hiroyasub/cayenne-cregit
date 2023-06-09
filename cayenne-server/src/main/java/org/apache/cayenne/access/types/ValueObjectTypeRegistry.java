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
comment|/**  *<p>Registry of user-defined descriptors of custom value objects' classes.</p>  *<p>Used to convert this objects into values that can be used directly with JDBC.</p>  *<p>Part of replacement for the {@link ExtendedType ExtendedTypes API} for the end-user.</p>  *  * @see ValueObjectType  * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|ValueObjectTypeRegistry
block|{
comment|/**      * Lookup descriptor in this registry.      *      * @param valueClass class of the custom value object      * @return {@link ValueObjectType} descriptor      */
parameter_list|<
name|T
parameter_list|>
name|ValueObjectType
argument_list|<
name|T
argument_list|,
name|?
argument_list|>
name|getValueType
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|valueClass
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

