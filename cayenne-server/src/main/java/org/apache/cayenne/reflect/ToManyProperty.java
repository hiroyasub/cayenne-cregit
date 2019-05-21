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
comment|/**  * A property representing a collection of objects.  *   * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|ToManyProperty
extends|extends
name|ArcProperty
block|{
name|void
name|addTarget
parameter_list|(
name|Object
name|source
parameter_list|,
name|Object
name|target
parameter_list|,
name|boolean
name|setReverse
parameter_list|)
throws|throws
name|PropertyException
function_decl|;
name|void
name|removeTarget
parameter_list|(
name|Object
name|source
parameter_list|,
name|Object
name|target
parameter_list|,
name|boolean
name|setReverse
parameter_list|)
throws|throws
name|PropertyException
function_decl|;
comment|/**      * Adds value to collection, without triggering changing events      * This method is mostly for internal use      * @since 3.1      */
name|void
name|addTargetDirectly
parameter_list|(
name|Object
name|source
parameter_list|,
name|Object
name|target
parameter_list|)
throws|throws
name|PropertyException
function_decl|;
comment|/**      * Removes value from collection, without triggering changing events      * This method is mostly for internal use      * @since 3.1      */
name|void
name|removeTargetDirectly
parameter_list|(
name|Object
name|source
parameter_list|,
name|Object
name|target
parameter_list|)
throws|throws
name|PropertyException
function_decl|;
block|}
end_interface

end_unit

