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
name|trans
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

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
name|dba
operator|.
name|QuotingStrategy
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
name|DbAttribute
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
name|BatchQuery
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
comment|/**  * A translator for UpdateBatchQueries that produces parameterized SQL.  */
end_comment

begin_class
specifier|public
class|class
name|UpdateBatchQueryBuilder
extends|extends
name|BatchQueryBuilder
block|{
specifier|public
name|UpdateBatchQueryBuilder
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|super
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|createSqlString
parameter_list|(
name|BatchQuery
name|batch
parameter_list|)
throws|throws
name|IOException
block|{
name|UpdateBatchQuery
name|updateBatch
init|=
operator|(
name|UpdateBatchQuery
operator|)
name|batch
decl_stmt|;
name|boolean
name|status
decl_stmt|;
if|if
condition|(
name|batch
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
operator|!=
literal|null
operator|&&
name|batch
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
condition|)
block|{
name|status
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|status
operator|=
literal|false
expr_stmt|;
block|}
name|QuotingStrategy
name|strategy
init|=
name|getAdapter
argument_list|()
operator|.
name|getQuotingStrategy
argument_list|(
name|status
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|qualifierAttributes
init|=
name|updateBatch
operator|.
name|getQualifierAttributes
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|updatedDbAttributes
init|=
name|updateBatch
operator|.
name|getUpdatedAttributes
argument_list|()
decl_stmt|;
name|StringBuffer
name|query
init|=
operator|new
name|StringBuffer
argument_list|(
literal|"UPDATE "
argument_list|)
decl_stmt|;
name|query
operator|.
name|append
argument_list|(
name|strategy
operator|.
name|quoteFullyQualifiedName
argument_list|(
name|batch
operator|.
name|getDbEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|.
name|append
argument_list|(
literal|" SET "
argument_list|)
expr_stmt|;
name|int
name|len
init|=
name|updatedDbAttributes
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|query
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|DbAttribute
name|attribute
init|=
name|updatedDbAttributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|query
operator|.
name|append
argument_list|(
name|strategy
operator|.
name|quoteString
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|.
name|append
argument_list|(
literal|" = ?"
argument_list|)
expr_stmt|;
block|}
name|query
operator|.
name|append
argument_list|(
literal|" WHERE "
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|i
init|=
name|qualifierAttributes
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbAttribute
name|attribute
init|=
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|appendDbAttribute
argument_list|(
name|query
argument_list|,
name|attribute
argument_list|)
expr_stmt|;
name|query
operator|.
name|append
argument_list|(
name|updateBatch
operator|.
name|isNull
argument_list|(
name|attribute
argument_list|)
condition|?
literal|" IS NULL"
else|:
literal|" = ?"
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|query
operator|.
name|append
argument_list|(
literal|" AND "
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|query
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Binds BatchQuery parameters to the PreparedStatement.      */
annotation|@
name|Override
specifier|public
name|void
name|bindParameters
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|BatchQuery
name|query
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
name|UpdateBatchQuery
name|updateBatch
init|=
operator|(
name|UpdateBatchQuery
operator|)
name|query
decl_stmt|;
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|qualifierAttributes
init|=
name|updateBatch
operator|.
name|getQualifierAttributes
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|updatedDbAttributes
init|=
name|updateBatch
operator|.
name|getUpdatedAttributes
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|updatedDbAttributes
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|parameterIndex
init|=
literal|1
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|value
init|=
name|query
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|DbAttribute
name|attribute
init|=
name|updatedDbAttributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|adapter
operator|.
name|bindParameter
argument_list|(
name|statement
argument_list|,
name|value
argument_list|,
name|parameterIndex
operator|++
argument_list|,
name|attribute
operator|.
name|getType
argument_list|()
argument_list|,
name|attribute
operator|.
name|getScale
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|qualifierAttributes
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|value
init|=
name|query
operator|.
name|getValue
argument_list|(
name|len
operator|+
name|i
argument_list|)
decl_stmt|;
name|DbAttribute
name|attribute
init|=
name|qualifierAttributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// skip null attributes... they are translated as "IS NULL"
if|if
condition|(
name|updateBatch
operator|.
name|isNull
argument_list|(
name|attribute
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|adapter
operator|.
name|bindParameter
argument_list|(
name|statement
argument_list|,
name|value
argument_list|,
name|parameterIndex
operator|++
argument_list|,
name|attribute
operator|.
name|getType
argument_list|()
argument_list|,
name|attribute
operator|.
name|getScale
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

