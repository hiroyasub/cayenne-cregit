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
package|;
end_package

begin_comment
comment|/**  * Expression visitor interface. Defines callback methods invoked when   * walking the expression using {@link Expression#traverse(TraversalHandler)}.  *   */
end_comment

begin_interface
specifier|public
interface|interface
name|TraversalHandler
block|{
comment|/**       * Called during traversal after a child of expression      * has been visited.       */
specifier|public
name|void
name|finishedChild
parameter_list|(
name|Expression
name|node
parameter_list|,
name|int
name|childIndex
parameter_list|,
name|boolean
name|hasMoreChildren
parameter_list|)
function_decl|;
comment|/**       * Called during the traversal before an expression node children      * processing is started.      *       * @since 1.1      */
specifier|public
name|void
name|startNode
parameter_list|(
name|Expression
name|node
parameter_list|,
name|Expression
name|parentNode
parameter_list|)
function_decl|;
comment|/**       * Called during the traversal after an expression node children      * processing is finished.      *       * @since 1.1      */
specifier|public
name|void
name|endNode
parameter_list|(
name|Expression
name|node
parameter_list|,
name|Expression
name|parentNode
parameter_list|)
function_decl|;
comment|/**       * Called during the traversal when a leaf non-expression node       * is encountered.      */
specifier|public
name|void
name|objectNode
parameter_list|(
name|Object
name|leaf
parameter_list|,
name|Expression
name|parentNode
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

