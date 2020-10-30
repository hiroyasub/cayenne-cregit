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
name|access
operator|.
name|sqlbuilder
operator|.
name|InsertBuilder
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
name|sqlbuilder
operator|.
name|SQLBuilder
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
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|InsertBatchTranslator
extends|extends
name|BaseBatchTranslator
argument_list|<
name|InsertBatchQuery
argument_list|>
implements|implements
name|BatchTranslator
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
name|super
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getSql
parameter_list|()
block|{
name|InsertBatchQuery
name|query
init|=
name|context
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|InsertBuilder
name|insertBuilder
init|=
name|SQLBuilder
operator|.
name|insert
argument_list|(
name|context
operator|.
name|getRootDbEntity
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|query
operator|.
name|getDbAttributes
argument_list|()
control|)
block|{
comment|// skip generated attributes, if needed
if|if
condition|(
name|excludeInBatch
argument_list|(
name|attribute
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|insertBuilder
operator|.
name|column
argument_list|(
name|SQLBuilder
operator|.
name|column
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|attribute
argument_list|(
name|attribute
argument_list|)
argument_list|)
comment|// We can use here any non-null value, to create attribute binding,
comment|// actual value and ExtendedType will be set at updateBindings() call.
operator|.
name|value
argument_list|(
name|SQLBuilder
operator|.
name|value
argument_list|(
literal|1
argument_list|)
operator|.
name|attribute
argument_list|(
name|attribute
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|doTranslate
argument_list|(
name|insertBuilder
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|DbAttributeBinding
index|[]
name|updateBindings
parameter_list|(
name|BatchQueryRow
name|row
parameter_list|)
block|{
name|InsertBatchQuery
name|query
init|=
name|context
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
name|int
name|j
init|=
literal|0
decl_stmt|;
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|query
operator|.
name|getDbAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|excludeInBatch
argument_list|(
name|attribute
argument_list|)
condition|)
block|{
name|i
operator|++
expr_stmt|;
continue|continue;
block|}
name|Object
name|value
init|=
name|row
operator|.
name|getValue
argument_list|(
name|i
operator|++
argument_list|)
decl_stmt|;
name|ExtendedType
argument_list|<
name|?
argument_list|>
name|extendedType
init|=
name|value
operator|!=
literal|null
condition|?
name|context
operator|.
name|getAdapter
argument_list|()
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
name|context
operator|.
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
operator|.
name|getDefaultType
argument_list|()
decl_stmt|;
name|bindings
index|[
name|j
index|]
operator|.
name|include
argument_list|(
operator|++
name|j
argument_list|,
name|value
argument_list|,
name|extendedType
argument_list|)
expr_stmt|;
block|}
return|return
name|bindings
return|;
block|}
specifier|protected
name|boolean
name|excludeInBatch
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
comment|// attribute inclusion rule - one of the rules below must be true:
comment|//  (1) attribute not generated
comment|//  (2) attribute is generated and PK and adapter does not support generated keys
return|return
name|attribute
operator|.
name|isGenerated
argument_list|()
operator|&&
operator|(
operator|!
name|attribute
operator|.
name|isPrimaryKey
argument_list|()
operator|||
name|context
operator|.
name|getAdapter
argument_list|()
operator|.
name|supportsGeneratedKeys
argument_list|()
operator|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isNullAttribute
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

