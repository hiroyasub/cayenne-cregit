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
name|dba
operator|.
name|postgres
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
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
name|access
operator|.
name|trans
operator|.
name|ProcedureTranslator
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
name|JdbcAdapter
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
name|sqlserver
operator|.
name|SQLServerProcedureAction
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
name|EntityResolver
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
name|ProcedureQuery
import|;
end_import

begin_comment
comment|/**  * Current implementation simply relies on SQLServerProcedureAction superclass behavior.  * Namely that CallableStatement.execute() rewinds result set pointer so  * CallableStatement.getMoreResults() shouldn't be invoked until the first result set is  * processed.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|PostgresProcedureAction
extends|extends
name|SQLServerProcedureAction
block|{
name|PostgresProcedureAction
parameter_list|(
name|ProcedureQuery
name|query
parameter_list|,
name|JdbcAdapter
name|adapter
parameter_list|,
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
name|super
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|,
name|entityResolver
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a translator that adds parenthesis to no-param queries.      */
comment|// see CAY-750 for the problem description
annotation|@
name|Override
specifier|protected
name|ProcedureTranslator
name|createTranslator
parameter_list|(
name|Connection
name|connection
parameter_list|)
block|{
name|ProcedureTranslator
name|translator
init|=
operator|new
name|PostgresProcedureTranslator
argument_list|()
decl_stmt|;
name|translator
operator|.
name|setAdapter
argument_list|(
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
name|translator
operator|.
name|setQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|translator
operator|.
name|setEntityResolver
argument_list|(
name|getEntityResolver
argument_list|()
argument_list|)
expr_stmt|;
name|translator
operator|.
name|setConnection
argument_list|(
name|connection
argument_list|)
expr_stmt|;
return|return
name|translator
return|;
block|}
specifier|static
class|class
name|PostgresProcedureTranslator
extends|extends
name|ProcedureTranslator
block|{
annotation|@
name|Override
specifier|protected
name|String
name|createSqlString
parameter_list|()
block|{
name|String
name|sql
init|=
name|super
operator|.
name|createSqlString
argument_list|()
decl_stmt|;
comment|// add empty parameter parenthesis
if|if
condition|(
name|sql
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
operator|&&
operator|!
name|sql
operator|.
name|endsWith
argument_list|(
literal|")}"
argument_list|)
condition|)
block|{
name|sql
operator|=
name|sql
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|sql
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|+
literal|"()}"
expr_stmt|;
block|}
return|return
name|sql
return|;
block|}
block|}
block|}
end_class

end_unit

