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
name|access
operator|.
name|translator
operator|.
name|batch
operator|.
name|legacy
package|;
end_package

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
name|InsertBatchQuery
import|;
end_import

begin_comment
comment|/**  * Translator of InsertBatchQueries.  * @deprecated since 4.2  */
end_comment

begin_class
annotation|@
name|Deprecated
specifier|public
class|class
name|InsertBatchTranslator
extends|extends
name|DefaultBatchTranslator
block|{
specifier|public
name|InsertBatchTranslator
parameter_list|(
name|InsertBatchQuery
name|query
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|)
block|{
comment|// no trimming is needed here, so passing hardcoded NULL for trim
comment|// function
name|super
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|,
literal|null
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
literal|"INSERT INTO "
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
comment|// (2) attribute is generated and PK and adapter does not support
comment|// generated
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
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|strategy
operator|.
name|quotedName
argument_list|(
name|attribute
argument_list|)
argument_list|)
expr_stmt|;
name|columnCount
operator|++
expr_stmt|;
block|}
block|}
name|buffer
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
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|')'
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
name|DbAttributeBinding
index|[]
name|createBindings
parameter_list|()
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
name|DbAttribute
name|a
init|=
name|attributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|bindings
index|[
name|i
index|]
operator|=
operator|new
name|DbAttributeBinding
argument_list|(
name|a
argument_list|)
expr_stmt|;
comment|// include/exclude state depends on DbAttribute only and can be
comment|// precompiled here
if|if
condition|(
name|includeInBatch
argument_list|(
name|a
argument_list|)
condition|)
block|{
comment|// setting fake position here... all we care about is that it is
comment|//> -1
name|bindings
index|[
name|i
index|]
operator|.
name|include
argument_list|(
literal|1
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bindings
index|[
name|i
index|]
operator|.
name|exclude
argument_list|()
expr_stmt|;
block|}
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
comment|// exclusions are permanent
if|if
condition|(
operator|!
name|b
operator|.
name|isExcluded
argument_list|()
condition|)
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
comment|// (2) attribute is generated and PK and adapter does not support
comment|// generated
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

