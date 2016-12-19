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
name|dbsync
operator|.
name|merge
operator|.
name|token
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
name|dbsync
operator|.
name|merge
operator|.
name|context
operator|.
name|MergeDirection
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
name|dbsync
operator|.
name|merge
operator|.
name|context
operator|.
name|MergerContext
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactory
import|;
end_import

begin_comment
comment|/**  * Represents a minimal atomic synchronization operation between database and Cayenne model.  */
end_comment

begin_interface
specifier|public
interface|interface
name|MergerToken
extends|extends
name|Comparable
argument_list|<
name|MergerToken
argument_list|>
block|{
name|String
name|getTokenName
parameter_list|()
function_decl|;
name|String
name|getTokenValue
parameter_list|()
function_decl|;
name|int
name|getSortingWeight
parameter_list|()
function_decl|;
comment|/**      * The direction of this token. One of {@link MergeDirection#TO_DB} or      * {@link MergeDirection#TO_MODEL}      */
name|MergeDirection
name|getDirection
parameter_list|()
function_decl|;
comment|/**      * Create a complimentary token with the reverse direction. AddColumn in one direction becomes      * DropColumn in the other direction.      *<p>      * Not all tokens are reversible.      */
name|MergerToken
name|createReverse
parameter_list|(
name|MergerTokenFactory
name|factory
parameter_list|)
function_decl|;
comment|/**      * Executes synchronization operation.      *      * @param context merge operation context.      */
name|void
name|execute
parameter_list|(
name|MergerContext
name|context
parameter_list|)
function_decl|;
name|boolean
name|isEmpty
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

