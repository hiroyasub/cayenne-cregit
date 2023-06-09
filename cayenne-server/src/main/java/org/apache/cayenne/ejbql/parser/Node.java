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
operator|.
name|parser
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
name|ejbql
operator|.
name|EJBQLExpression
import|;
end_import

begin_comment
comment|/**  * A JJTree-compliant tree node interface.  *   * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|Node
extends|extends
name|EJBQLExpression
block|{
comment|/** 	 * This method is called after the node has been made the current node. It 	 * indicates that child nodes can now be added to it. 	 */
specifier|public
name|void
name|jjtOpen
parameter_list|()
function_decl|;
comment|/** 	 * This method is called after all the child nodes have been added. 	 */
specifier|public
name|void
name|jjtClose
parameter_list|()
function_decl|;
comment|/** 	 * This pair of methods are used to inform the node of its parent. 	 */
specifier|public
name|void
name|jjtSetParent
parameter_list|(
name|Node
name|n
parameter_list|)
function_decl|;
specifier|public
name|Node
name|jjtGetParent
parameter_list|()
function_decl|;
comment|/** 	 * This method tells the node to add its argument to the node's list of 	 * children. 	 */
specifier|public
name|void
name|jjtAddChild
parameter_list|(
name|Node
name|n
parameter_list|,
name|int
name|i
parameter_list|)
function_decl|;
comment|/** 	 * This method returns a child node. The children are numbered from zero, 	 * left to right. 	 */
specifier|public
name|Node
name|jjtGetChild
parameter_list|(
name|int
name|i
parameter_list|)
function_decl|;
comment|/** Return the number of children the node has. */
specifier|public
name|int
name|jjtGetNumChildren
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

