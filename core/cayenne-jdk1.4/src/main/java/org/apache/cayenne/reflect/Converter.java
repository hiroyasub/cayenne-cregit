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
name|reflect
package|;
end_package

begin_comment
comment|/**  * A helper class to do property type conversions.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_comment
comment|// keeping non-public for now, but this can also be used in expressions and such...
end_comment

begin_class
specifier|abstract
class|class
name|Converter
block|{
comment|/**      * Converts object to supported class without doing any type checking.      */
specifier|abstract
name|Object
name|convert
parameter_list|(
name|Object
name|value
parameter_list|,
name|Class
name|type
parameter_list|)
function_decl|;
block|}
end_class

end_unit

