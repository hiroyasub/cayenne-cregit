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
name|dba
operator|.
name|oracle
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
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|DataNode
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
name|OperationObserver
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
name|jdbc
operator|.
name|SQLStatement
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
name|jdbc
operator|.
name|SQLTemplateAction
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
name|SQLTemplate
import|;
end_import

begin_comment
comment|/**  * A SQLTemplateAction that addresses Oracle 8 driver limitations.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|Oracle8SQLTemplateAction
extends|extends
name|SQLTemplateAction
block|{
name|Oracle8SQLTemplateAction
parameter_list|(
name|SQLTemplate
name|query
parameter_list|,
name|DataNode
name|dataNode
parameter_list|)
block|{
name|super
argument_list|(
name|query
argument_list|,
name|dataNode
argument_list|)
expr_stmt|;
block|}
comment|/**      * Overrides super implementation to guess whether the query is selecting or      * not and execute it appropriately. Super implementation relied on generic      * JDBC mechanism, common for selecting and updating statements that does      * not work in Oracle 8.* drivers.      */
annotation|@
name|Override
specifier|protected
name|void
name|execute
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|OperationObserver
name|callback
parameter_list|,
name|SQLStatement
name|compiled
parameter_list|,
name|Collection
name|updateCounts
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
name|String
name|sql
init|=
name|compiled
operator|.
name|getSql
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|boolean
name|select
init|=
name|sql
operator|.
name|length
argument_list|()
operator|>
literal|"SELECT"
operator|.
name|length
argument_list|()
operator|&&
name|sql
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|"SELECT"
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"SELECT"
argument_list|)
decl_stmt|;
name|long
name|t1
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|boolean
name|iteratedResult
init|=
name|callback
operator|.
name|isIteratedResult
argument_list|()
decl_stmt|;
name|PreparedStatement
name|statement
init|=
name|connection
operator|.
name|prepareStatement
argument_list|(
name|compiled
operator|.
name|getSql
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|bind
argument_list|(
name|statement
argument_list|,
name|compiled
operator|.
name|getBindings
argument_list|()
argument_list|)
expr_stmt|;
comment|// start - code different from super
if|if
condition|(
name|select
condition|)
block|{
name|ResultSet
name|resultSet
init|=
name|statement
operator|.
name|executeQuery
argument_list|()
decl_stmt|;
try|try
block|{
name|processSelectResult
argument_list|(
name|compiled
argument_list|,
name|connection
argument_list|,
name|statement
argument_list|,
name|resultSet
argument_list|,
name|callback
argument_list|,
name|t1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|iteratedResult
condition|)
block|{
name|resultSet
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|int
name|updateCount
init|=
name|statement
operator|.
name|executeUpdate
argument_list|()
decl_stmt|;
name|updateCounts
operator|.
name|add
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|updateCount
argument_list|)
argument_list|)
expr_stmt|;
name|dataNode
operator|.
name|getJdbcEventLogger
argument_list|()
operator|.
name|logUpdateCount
argument_list|(
name|updateCount
argument_list|)
expr_stmt|;
block|}
comment|// end - code different from super
block|}
finally|finally
block|{
if|if
condition|(
operator|!
name|iteratedResult
condition|)
block|{
name|statement
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

