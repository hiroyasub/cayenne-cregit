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
name|access
operator|.
name|translator
operator|.
name|DbAttributeBinding
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
name|types
operator|.
name|ExtendedType
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
comment|/**  * Translator for delete BatchQueries. Creates parameterized DELETE SQL  * statements.  */
end_comment

begin_class
specifier|public
class|class
name|DeleteBatchTranslator
extends|extends
name|DefaultBatchTranslator
block|{
specifier|public
name|DeleteBatchTranslator
parameter_list|(
name|DeleteBatchQuery
name|query
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
name|String
name|trimFunction
parameter_list|)
block|{
name|super
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|,
name|trimFunction
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|createSql
parameter_list|()
block|{
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
literal|"DELETE FROM "
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
comment|/**      * Appends WHERE clause to SQL string      */
specifier|protected
name|void
name|applyQualifier
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" WHERE "
argument_list|)
expr_stmt|;
name|DeleteBatchQuery
name|deleteBatch
init|=
operator|(
name|DeleteBatchQuery
operator|)
name|query
decl_stmt|;
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|i
init|=
name|deleteBatch
operator|.
name|getDbAttributes
argument_list|()
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
name|buffer
argument_list|,
name|attribute
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|deleteBatch
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
name|buffer
operator|.
name|append
argument_list|(
literal|" AND "
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|DbAttributeBinding
index|[]
name|createBindings
parameter_list|()
block|{
name|DeleteBatchQuery
name|deleteBatch
init|=
operator|(
name|DeleteBatchQuery
operator|)
name|query
decl_stmt|;
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|attributes
init|=
name|deleteBatch
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
name|DbAttributeBinding
index|[]
name|bindings
init|=
operator|new
name|DbAttributeBinding
index|[
name|len
index|]
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
name|bindings
index|[
name|i
index|]
operator|=
operator|new
name|DbAttributeBinding
argument_list|(
name|attributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|bindings
return|;
block|}
annotation|@
name|Override
specifier|protected
name|DbAttributeBinding
index|[]
name|doUpdateBindings
parameter_list|(
name|BatchQueryRow
name|row
parameter_list|)
block|{
name|int
name|len
init|=
name|bindings
operator|.
name|length
decl_stmt|;
name|DeleteBatchQuery
name|deleteBatch
init|=
operator|(
name|DeleteBatchQuery
operator|)
name|query
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|j
init|=
literal|1
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|DbAttributeBinding
name|b
init|=
name|bindings
index|[
name|i
index|]
decl_stmt|;
comment|// skip null attributes... they are translated as "IS NULL"
if|if
condition|(
name|deleteBatch
operator|.
name|isNull
argument_list|(
name|b
operator|.
name|getAttribute
argument_list|()
argument_list|)
condition|)
block|{
name|b
operator|.
name|exclude
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Object
name|value
init|=
name|row
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|ExtendedType
name|extendedType
init|=
name|value
operator|!=
literal|null
condition|?
name|adapter
operator|.
name|getExtendedTypes
argument_list|()
operator|.
name|getRegisteredType
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
else|:
name|adapter
operator|.
name|getExtendedTypes
argument_list|()
operator|.
name|getDefaultType
argument_list|()
decl_stmt|;
name|b
operator|.
name|include
argument_list|(
name|j
operator|++
argument_list|,
name|value
argument_list|,
name|extendedType
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|bindings
return|;
block|}
block|}
end_class

end_unit

