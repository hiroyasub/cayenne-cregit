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
name|ejbql
package|;
end_package

begin_comment
comment|/**  * An abstract EJBQL expression interface.  *   * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|EJBQLExpression
block|{
comment|/**      * Accepts a visitor, calling appropriate visitor method. If the visitor method      * returns true, visits all children, otherwise stops.      */
name|void
name|visit
parameter_list|(
name|EJBQLExpressionVisitor
name|visitor
parameter_list|)
function_decl|;
comment|/**      * Returns a number of child operands of this expression node.      */
name|int
name|getChildrenCount
parameter_list|()
function_decl|;
comment|/**      * Returns a child expression node at the specified index.      */
name|EJBQLExpression
name|getChild
parameter_list|(
name|int
name|index
parameter_list|)
function_decl|;
comment|/**      * Returns a text property of the node.      */
name|String
name|getText
parameter_list|()
function_decl|;
comment|/**      * Returns an optional boolean flag that negates the value of the expression.      */
name|boolean
name|isNegated
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

