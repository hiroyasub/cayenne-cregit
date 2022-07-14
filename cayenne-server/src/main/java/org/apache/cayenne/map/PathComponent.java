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
name|map
package|;
end_package

begin_comment
comment|/**  * A component in a path chain.  *   * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|PathComponent
parameter_list|<
name|A
extends|extends
name|Attribute
parameter_list|,
name|R
extends|extends
name|Relationship
parameter_list|>
block|{
name|A
name|getAttribute
parameter_list|()
function_decl|;
name|R
name|getRelationship
parameter_list|()
function_decl|;
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Returns a joint type of this path component in the expression. Attributes always      * return undefined type, while relationships may be outer or inner joins.      */
name|JoinType
name|getJoinType
parameter_list|()
function_decl|;
name|boolean
name|isLast
parameter_list|()
function_decl|;
comment|/**      * Returns true if this component is an alias for a different path. Only the first      * path component can be an alias. Aliased path can be obtained by calling      * {@link #getAliasedPath()}.      */
name|boolean
name|isAlias
parameter_list|()
function_decl|;
comment|/**      * Returns an aliased path or null if this component is not an alias.      */
name|Iterable
argument_list|<
name|PathComponent
argument_list|<
name|A
argument_list|,
name|R
argument_list|>
argument_list|>
name|getAliasedPath
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

