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

begin_comment
comment|/**  * A property representing a map of objects keyed by one of the object properties.  *   * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|ToManyMapProperty
extends|extends
name|ToManyProperty
block|{
comment|/**      * Extracts the map key of the target object.      */
name|Object
name|getMapKey
parameter_list|(
name|Object
name|target
parameter_list|)
throws|throws
name|PropertyException
function_decl|;
block|}
end_interface

end_unit

