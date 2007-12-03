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
name|ArrayList
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

begin_comment
comment|/**  * Translator of InsertBatchQueries.  *   * @author Andriy Shapochka  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|InsertBatchQueryBuilder
extends|extends
name|BatchQueryBuilder
block|{
specifier|public
name|InsertBatchQueryBuilder
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|super
operator|.
name|setAdapter
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
block|}
comment|/**      * Binds parameters for the current batch iteration to the PreparedStatement. Performs      * filtering of attributes based on column generation rules.      *       * @since 1.2      */
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
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|dbAttributes
init|=
name|query
operator|.
name|getDbAttributes
argument_list|()
decl_stmt|;
name|int
name|attributeCount
init|=
name|dbAttributes
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// must use an independent counter "j" for prepared statement index
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|j
init|=
literal|0
init|;
name|i
operator|<
name|attributeCount
condition|;
name|i
operator|++
control|)
block|{
name|DbAttribute
name|attribute
init|=
name|dbAttributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|includeInBatch
argument_list|(
name|attribute
argument_list|)
condition|)
block|{
name|j
operator|++
expr_stmt|;
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
name|adapter
operator|.
name|bindParameter
argument_list|(
name|statement
argument_list|,
name|value
argument_list|,
name|j
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
comment|/**      * Returns a list of values for the current batch iteration. Performs filtering of      * attributes based on column generation rules. Used primarily for logging.      *       * @since 1.2      */
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getParameterValues
parameter_list|(
name|BatchQuery
name|query
parameter_list|)
block|{
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|attributes
init|=
name|query
operator|.
name|getDbAttributes
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|attributes
operator|.
name|size
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|(
name|len
argument_list|)
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
name|DbAttribute
name|attribute
init|=
name|attributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|includeInBatch
argument_list|(
name|attribute
argument_list|)
condition|)
block|{
name|values
operator|.
name|add
argument_list|(
name|query
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|values
return|;
block|}
specifier|public
name|String
name|createSqlString
parameter_list|(
name|BatchQuery
name|batch
parameter_list|)
block|{
name|String
name|table
init|=
name|batch
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getFullyQualifiedName
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|dbAttributes
init|=
name|batch
operator|.
name|getDbAttributes
argument_list|()
decl_stmt|;
name|StringBuffer
name|query
init|=
operator|new
name|StringBuffer
argument_list|(
literal|"INSERT INTO "
argument_list|)
decl_stmt|;
name|query
operator|.
name|append
argument_list|(
name|table
argument_list|)
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
expr_stmt|;
name|int
name|columnCount
init|=
literal|0
decl_stmt|;
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|dbAttributes
control|)
block|{
comment|// attribute inclusion rule - one of the rules below must be true:
comment|// (1) attribute not generated
comment|// (2) attribute is generated and PK and adapter does not support generated
comment|// keys
if|if
condition|(
name|includeInBatch
argument_list|(
name|attribute
argument_list|)
condition|)
block|{
if|if
condition|(
name|columnCount
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
name|query
operator|.
name|append
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|columnCount
operator|++
expr_stmt|;
block|}
block|}
name|query
operator|.
name|append
argument_list|(
literal|") VALUES ("
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|columnCount
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
name|query
operator|.
name|append
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
block|}
name|query
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
return|return
name|query
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns true if an attribute should be included in the batch.      *       * @since 1.2      */
specifier|protected
name|boolean
name|includeInBatch
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
comment|// attribute inclusion rule - one of the rules below must be true:
comment|// (1) attribute not generated
comment|// (2) attribute is generated and PK and adapter does not support generated
comment|// keys
return|return
operator|!
name|attribute
operator|.
name|isGenerated
argument_list|()
operator|||
operator|(
name|attribute
operator|.
name|isPrimaryKey
argument_list|()
operator|&&
operator|!
name|adapter
operator|.
name|supportsGeneratedKeys
argument_list|()
operator|)
return|;
block|}
block|}
end_class

end_unit

