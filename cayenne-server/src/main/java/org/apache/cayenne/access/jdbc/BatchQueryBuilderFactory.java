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
name|jdbc
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
name|trans
operator|.
name|BatchQueryBuilder
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
name|dba
operator|.
name|DbAdapter
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
name|DeleteBatchQuery
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
name|InsertBatchQuery
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
name|UpdateBatchQuery
import|;
end_import

begin_comment
comment|/**  * Factory which creates BatchQueryBuilders for different types of queries,  * which, in their turn, create SQL strings for batch queries.  *   * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|BatchQueryBuilderFactory
block|{
comment|/**      * Creates query builder for INSERT queries      *       * @since 3.2      */
name|BatchQueryBuilder
name|createInsertQueryBuilder
parameter_list|(
name|InsertBatchQuery
name|query
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|)
function_decl|;
comment|/**      * Creates query builder for UPDATE queries      *       * @since 3.2      */
name|BatchQueryBuilder
name|createUpdateQueryBuilder
parameter_list|(
name|UpdateBatchQuery
name|query
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|)
function_decl|;
comment|/**      * Creates query builder for DELETE queries      *       * @since 3.2      */
name|BatchQueryBuilder
name|createDeleteQueryBuilder
parameter_list|(
name|DeleteBatchQuery
name|query
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

