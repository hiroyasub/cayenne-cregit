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
name|sql
operator|.
name|Types
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
name|DeleteBatchQueryBuilder
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
name|BatchQueryRow
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

begin_comment
comment|/**  * Implementation of {@link DeleteBatchQueryBuilder}, which uses 'soft' delete  * (runs UPDATE and sets 'deleted' field to true instead-of running SQL DELETE)  */
end_comment

begin_class
specifier|public
class|class
name|SoftDeleteBatchQueryBuilder
extends|extends
name|DeleteBatchQueryBuilder
block|{
specifier|private
name|String
name|deletedFieldName
decl_stmt|;
specifier|public
name|SoftDeleteBatchQueryBuilder
parameter_list|(
name|DeleteBatchQuery
name|query
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
name|String
name|deletedFieldName
parameter_list|)
block|{
name|super
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|)
expr_stmt|;
name|this
operator|.
name|deletedFieldName
operator|=
name|deletedFieldName
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|createSqlString
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|needSoftDelete
argument_list|()
condition|)
block|{
return|return
name|super
operator|.
name|createSqlString
argument_list|()
return|;
block|}
name|QuotingStrategy
name|strategy
init|=
name|adapter
operator|.
name|getQuotingStrategy
argument_list|()
decl_stmt|;
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"UPDATE "
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|strategy
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|query
operator|.
name|getDbEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|" SET "
argument_list|)
operator|.
name|append
argument_list|(
name|strategy
operator|.
name|quotedIdentifier
argument_list|(
name|query
operator|.
name|getDbEntity
argument_list|()
argument_list|,
name|deletedFieldName
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|" = ?"
argument_list|)
expr_stmt|;
name|applyQualifier
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|int
name|getFirstParameterIndex
parameter_list|()
block|{
return|return
name|needSoftDelete
argument_list|()
condition|?
literal|2
else|:
literal|1
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|bindParameters
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|BatchQueryRow
name|row
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
if|if
condition|(
name|needSoftDelete
argument_list|()
condition|)
block|{
comment|// binding first parameter (which is 'deleted') as true
name|adapter
operator|.
name|bindParameter
argument_list|(
name|statement
argument_list|,
literal|true
argument_list|,
literal|1
argument_list|,
name|Types
operator|.
name|BOOLEAN
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|bindParameters
argument_list|(
name|statement
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return whether 'soft' deletion should be used      */
specifier|protected
name|boolean
name|needSoftDelete
parameter_list|()
block|{
name|DbAttribute
name|attr
init|=
name|query
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|deletedFieldName
argument_list|)
decl_stmt|;
return|return
name|attr
operator|!=
literal|null
operator|&&
name|attr
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|BOOLEAN
return|;
block|}
block|}
end_class

end_unit

