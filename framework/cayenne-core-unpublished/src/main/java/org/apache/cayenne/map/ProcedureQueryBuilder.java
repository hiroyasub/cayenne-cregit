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
name|map
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
name|query
operator|.
name|ProcedureQuery
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
name|Query
import|;
end_import

begin_comment
comment|/**  * A QueryBuilder for stored procedure-based queries.  *   * @since 1.1  */
end_comment

begin_class
class|class
name|ProcedureQueryBuilder
extends|extends
name|QueryLoader
block|{
comment|/**      * Returns a ProcedureQuery.      */
annotation|@
name|Override
specifier|public
name|Query
name|getQuery
parameter_list|()
block|{
name|ProcedureQuery
name|query
init|=
operator|new
name|ProcedureQuery
argument_list|()
decl_stmt|;
name|Object
name|root
init|=
name|getRoot
argument_list|()
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|setRoot
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
name|query
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|query
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|query
operator|.
name|setResultEntityName
argument_list|(
name|resultEntity
argument_list|)
expr_stmt|;
name|query
operator|.
name|initWithProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
return|return
name|query
return|;
block|}
block|}
end_class

end_unit
