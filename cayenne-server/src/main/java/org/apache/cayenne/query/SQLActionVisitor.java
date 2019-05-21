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
name|query
package|;
end_package

begin_comment
comment|/**  * A factory interface to create standard SQLActions for a set of standard queries.  * Instances of SQLActionVisitor are passed by Cayenne to a Query in  * {@link org.apache.cayenne.query.Query#createSQLAction(SQLActionVisitor)}, allowing  * query to choose the action type and convert itself to a "standard" query if needed.  * Individual DbAdapters would provide special visitors, thus allowing for DB-dependent  * execution algorithms.  *   * @see org.apache.cayenne.query.Query#createSQLAction(SQLActionVisitor)  * @since 1.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|SQLActionVisitor
block|{
comment|/**      * Creates an action to execute a batch update query.      */
name|SQLAction
name|batchAction
parameter_list|(
name|BatchQuery
name|query
parameter_list|)
function_decl|;
comment|/**      * Creates an action to execute a SelectQuery.      */
parameter_list|<
name|T
parameter_list|>
name|SQLAction
name|objectSelectAction
parameter_list|(
name|SelectQuery
argument_list|<
name|T
argument_list|>
name|query
parameter_list|)
function_decl|;
comment|/**      * Creates an action to execute a FluentSelect.      * @since 4.2      */
parameter_list|<
name|T
parameter_list|>
name|SQLAction
name|objectSelectAction
parameter_list|(
name|FluentSelect
argument_list|<
name|T
argument_list|>
name|query
parameter_list|)
function_decl|;
comment|/**      * Creates an action to execute a SQLTemplate.      */
name|SQLAction
name|sqlAction
parameter_list|(
name|SQLTemplate
name|query
parameter_list|)
function_decl|;
comment|/**      * Creates an action to execute a ProcedureQuery.      */
name|SQLAction
name|procedureAction
parameter_list|(
name|ProcedureQuery
name|query
parameter_list|)
function_decl|;
comment|/**      * Creates an action to execute EJBQL query.      */
name|SQLAction
name|ejbqlAction
parameter_list|(
name|EJBQLQuery
name|query
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

