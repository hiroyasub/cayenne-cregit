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
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|LOBInsertBatchQueryBuilder
extends|extends
name|LOBBatchQueryBuilder
block|{
specifier|public
name|LOBInsertBatchQueryBuilder
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
name|List
name|getValuesForLOBUpdateParameters
parameter_list|(
name|BatchQuery
name|query
parameter_list|)
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
name|len
init|=
name|dbAttributes
operator|.
name|size
argument_list|()
decl_stmt|;
name|List
name|values
init|=
operator|new
name|ArrayList
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
name|dbAttributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|isUpdateableColumn
argument_list|(
name|value
argument_list|,
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|values
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|values
return|;
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
for|for
control|(
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|i
init|=
name|dbAttributes
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|DbAttribute
name|attribute
init|=
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
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
literal|", "
argument_list|)
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
name|dbAttributes
operator|.
name|size
argument_list|()
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
name|appendUpdatedParameter
argument_list|(
name|query
argument_list|,
name|dbAttributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|batch
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
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
block|}
end_class

end_unit

