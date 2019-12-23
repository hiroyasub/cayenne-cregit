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
name|exp
operator|.
name|property
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
name|exp
operator|.
name|Expression
import|;
end_import

begin_comment
comment|/**  * Base interface for all types of properties  * @since 4.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|Property
parameter_list|<
name|E
parameter_list|>
block|{
comment|/**      * @return name of this property, can be null      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * @return alias of this property, can be null      */
name|String
name|getAlias
parameter_list|()
function_decl|;
comment|/**      * @return expression that defines this property, not null      */
name|Expression
name|getExpression
parameter_list|()
function_decl|;
comment|/**      * @return java type of this property, can be null      */
name|Class
argument_list|<
name|E
argument_list|>
name|getType
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

