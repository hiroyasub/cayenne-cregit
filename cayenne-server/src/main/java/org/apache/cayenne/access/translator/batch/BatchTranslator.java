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
name|access
operator|.
name|translator
operator|.
name|batch
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
name|access
operator|.
name|translator
operator|.
name|ParameterBinding
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
name|BatchQueryRow
import|;
end_import

begin_comment
comment|/**  * Superclass of batch query translators.  *   * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|BatchTranslator
block|{
comment|/**      * Returns SQL String that can be used to init a PreparedStatement.      */
name|String
name|getSql
parameter_list|()
function_decl|;
comment|/**      * Returns the widest possible array of bindings for this query.      */
name|ParameterBinding
index|[]
name|getBindings
parameter_list|()
function_decl|;
comment|/**      * Updates internal bindings to be used with a given row, returning updated      * bindings array. This method guarantees that the returned array contains      * the same bindings in the same order as in the array returned from      * {@link #getBindings()} (but in a state corresponding to the 'row'      * parameter). Usually the returned array is actually the same object reused      * for every iteration, only with changed object state.      */
name|ParameterBinding
index|[]
name|updateBindings
parameter_list|(
name|BatchQueryRow
name|row
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

