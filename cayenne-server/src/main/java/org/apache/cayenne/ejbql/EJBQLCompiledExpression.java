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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbRelationship
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|SQLResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|PrefetchTreeNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|reflect
operator|.
name|ClassDescriptor
import|;
end_import

begin_comment
comment|/**  * Represents an EJB QL expression "compiled" in the context of a certain mapping.  *   * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|EJBQLCompiledExpression
block|{
comment|/**      * Returns a tree representation of an EJBQL expression.      */
name|EJBQLExpression
name|getExpression
parameter_list|()
function_decl|;
comment|/**      * Returns a descriptor of the root of this expression such as entity being fetched or      * updated.      */
name|ClassDescriptor
name|getRootDescriptor
parameter_list|()
function_decl|;
comment|/**      * Returns a ClassDescriptor for the id variable.      */
name|ClassDescriptor
name|getEntityDescriptor
parameter_list|(
name|String
name|identifier
parameter_list|)
function_decl|;
comment|/**      * Returns a collection of relationships that joins identifier with a parent entity.      * Returns null if the identifier corresponds to one of the query roots.      */
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|getIncomingRelationships
parameter_list|(
name|String
name|identifier
parameter_list|)
function_decl|;
comment|/**      * Returns EJB QL source of the compiled expression if available.      */
name|String
name|getSource
parameter_list|()
function_decl|;
comment|/**      * Returns a mapping of the result set columns, or null if this is not a select      * expression.      */
name|SQLResult
name|getResult
parameter_list|()
function_decl|;
comment|/**      * Returns prefetched columns tree for fetch joins.      */
name|PrefetchTreeNode
name|getPrefetchTree
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

