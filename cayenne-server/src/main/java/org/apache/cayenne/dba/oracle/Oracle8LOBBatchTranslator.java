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
name|oracle
package|;
end_package

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
name|CayenneRuntimeException
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
name|translator
operator|.
name|batch
operator|.
name|DefaultBatchTranslator
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
name|dba
operator|.
name|TypesMapping
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
name|BatchQueryRow
import|;
end_import

begin_comment
comment|/**  * Superclass of query builders for the DML operations involving LOBs.  *   */
end_comment

begin_class
specifier|abstract
class|class
name|Oracle8LOBBatchTranslator
extends|extends
name|DefaultBatchTranslator
block|{
specifier|protected
name|String
name|newClobFunction
decl_stmt|;
specifier|protected
name|String
name|newBlobFunction
decl_stmt|;
name|Oracle8LOBBatchTranslator
parameter_list|(
name|BatchQuery
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
specifier|abstract
name|List
argument_list|<
name|Object
argument_list|>
name|getValuesForLOBUpdateParameters
parameter_list|(
name|BatchQueryRow
name|row
parameter_list|)
function_decl|;
specifier|abstract
name|String
name|createSql
parameter_list|(
name|BatchQueryRow
name|row
parameter_list|)
function_decl|;
annotation|@
name|Override
specifier|protected
name|String
name|createSql
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
name|String
name|createLOBSelectString
parameter_list|(
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|selectedLOBAttributes
parameter_list|,
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|qualifierAttributes
parameter_list|)
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
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"SELECT "
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|it
init|=
name|selectedLOBAttributes
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|strategy
operator|.
name|quotedName
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
name|buf
operator|.
name|append
argument_list|(
literal|" FROM "
argument_list|)
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
operator|.
name|append
argument_list|(
literal|" WHERE "
argument_list|)
expr_stmt|;
name|it
operator|=
name|qualifierAttributes
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbAttribute
name|attribute
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|appendDbAttribute
argument_list|(
name|buf
argument_list|,
name|attribute
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" = ?"
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" AND "
argument_list|)
expr_stmt|;
block|}
block|}
name|buf
operator|.
name|append
argument_list|(
literal|" FOR UPDATE"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Appends parameter placeholder for the value of the column being updated.      * If requested, performs special handling on LOB columns.      */
specifier|protected
name|void
name|appendUpdatedParameter
parameter_list|(
name|StringBuilder
name|buf
parameter_list|,
name|DbAttribute
name|dbAttribute
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|int
name|type
init|=
name|dbAttribute
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|isUpdateableColumn
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|CLOB
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|newClobFunction
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|BLOB
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|newBlobFunction
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unknown LOB column type: "
operator|+
name|type
operator|+
literal|"("
operator|+
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|type
argument_list|)
operator|+
literal|"). Query buffer: "
operator|+
name|buf
argument_list|)
throw|;
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
name|dbAttributes
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
name|DbAttribute
name|attribute
init|=
name|b
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
name|int
name|type
init|=
name|attribute
operator|.
name|getType
argument_list|()
decl_stmt|;
comment|// TODO: (Andrus) This works as long as there is no LOBs in
comment|// qualifier
if|if
condition|(
name|isUpdateableColumn
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
condition|)
block|{
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
else|else
block|{
name|b
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
specifier|protected
name|boolean
name|isUpdateableColumn
parameter_list|(
name|Object
name|value
parameter_list|,
name|int
name|type
parameter_list|)
block|{
return|return
name|value
operator|==
literal|null
operator|||
name|type
operator|!=
name|Types
operator|.
name|BLOB
operator|&&
name|type
operator|!=
name|Types
operator|.
name|CLOB
return|;
block|}
name|void
name|setNewBlobFunction
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|newBlobFunction
operator|=
name|string
expr_stmt|;
block|}
name|void
name|setNewClobFunction
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|newClobFunction
operator|=
name|string
expr_stmt|;
block|}
block|}
end_class

end_unit

