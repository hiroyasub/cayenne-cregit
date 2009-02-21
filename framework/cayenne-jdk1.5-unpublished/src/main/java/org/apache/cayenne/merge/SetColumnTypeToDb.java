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
name|merge
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|map
operator|.
name|DbEntity
import|;
end_import

begin_comment
comment|/**  * An {@link MergerToken} to use to set type, length and precision.  */
end_comment

begin_class
specifier|public
class|class
name|SetColumnTypeToDb
extends|extends
name|AbstractToDbToken
operator|.
name|Entity
block|{
specifier|private
name|DbAttribute
name|columnOriginal
decl_stmt|;
specifier|private
name|DbAttribute
name|columnNew
decl_stmt|;
specifier|public
name|SetColumnTypeToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|columnOriginal
parameter_list|,
name|DbAttribute
name|columnNew
parameter_list|)
block|{
name|super
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|this
operator|.
name|columnOriginal
operator|=
name|columnOriginal
expr_stmt|;
name|this
operator|.
name|columnNew
operator|=
name|columnNew
expr_stmt|;
block|}
comment|/**      * append the part of the token before the actual column data type      * @param context       */
specifier|protected
name|void
name|appendPrefix
parameter_list|(
name|StringBuffer
name|sqlBuffer
parameter_list|,
name|QuotingStrategy
name|context
parameter_list|)
block|{
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|"ALTER TABLE "
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteFullyQualifiedName
argument_list|(
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" ALTER "
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteString
argument_list|(
name|columnNew
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" TYPE "
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|createSql
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|StringBuffer
name|sqlBuffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|QuotingStrategy
name|context
init|=
name|adapter
operator|.
name|getQuotingStrategy
argument_list|(
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
argument_list|)
decl_stmt|;
name|appendPrefix
argument_list|(
name|sqlBuffer
argument_list|,
name|context
argument_list|)
expr_stmt|;
comment|// copied from JdbcAdapter.createTableAppendColumn
name|String
index|[]
name|types
init|=
name|adapter
operator|.
name|externalTypesForJdbcType
argument_list|(
name|columnNew
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|types
operator|==
literal|null
operator|||
name|types
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|String
name|entityName
init|=
name|columnNew
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
condition|?
operator|(
operator|(
name|DbEntity
operator|)
name|columnNew
operator|.
name|getEntity
argument_list|()
operator|)
operator|.
name|getFullyQualifiedName
argument_list|()
else|:
literal|"<null>"
decl_stmt|;
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Undefined type for attribute '"
operator|+
name|entityName
operator|+
literal|"."
operator|+
name|columnNew
operator|.
name|getName
argument_list|()
operator|+
literal|"': "
operator|+
name|columnNew
operator|.
name|getType
argument_list|()
argument_list|)
throw|;
block|}
name|String
name|type
init|=
name|types
index|[
literal|0
index|]
decl_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
name|type
argument_list|)
expr_stmt|;
comment|// append size and precision (if applicable)
if|if
condition|(
name|TypesMapping
operator|.
name|supportsLength
argument_list|(
name|columnNew
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|int
name|len
init|=
name|columnNew
operator|.
name|getMaxLength
argument_list|()
decl_stmt|;
name|int
name|scale
init|=
name|TypesMapping
operator|.
name|isDecimal
argument_list|(
name|columnNew
operator|.
name|getType
argument_list|()
argument_list|)
condition|?
name|columnNew
operator|.
name|getScale
argument_list|()
else|:
operator|-
literal|1
decl_stmt|;
comment|// sanity check
if|if
condition|(
name|scale
operator|>
name|len
condition|)
block|{
name|scale
operator|=
operator|-
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|len
operator|>
literal|0
condition|)
block|{
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
operator|.
name|append
argument_list|(
name|len
argument_list|)
expr_stmt|;
if|if
condition|(
name|scale
operator|>=
literal|0
condition|)
block|{
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|scale
argument_list|)
expr_stmt|;
block|}
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|sqlBuffer
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|getTokenName
parameter_list|()
block|{
return|return
literal|"Set Column Type"
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getTokenValue
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|columnNew
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|columnOriginal
operator|.
name|getType
argument_list|()
operator|!=
name|columnNew
operator|.
name|getType
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" type: "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|columnOriginal
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" -> "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|columnNew
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|columnOriginal
operator|.
name|getMaxLength
argument_list|()
operator|!=
name|columnNew
operator|.
name|getMaxLength
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" maxLength: "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|columnOriginal
operator|.
name|getMaxLength
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" -> "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|columnNew
operator|.
name|getMaxLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|columnOriginal
operator|.
name|getAttributePrecision
argument_list|()
operator|!=
name|columnNew
operator|.
name|getAttributePrecision
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" precision: "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|columnOriginal
operator|.
name|getAttributePrecision
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" -> "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|columnNew
operator|.
name|getAttributePrecision
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|columnOriginal
operator|.
name|getScale
argument_list|()
operator|!=
name|columnNew
operator|.
name|getScale
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" scale: "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|columnOriginal
operator|.
name|getScale
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|" -> "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|columnNew
operator|.
name|getScale
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|MergerToken
name|createReverse
parameter_list|(
name|MergerFactory
name|factory
parameter_list|)
block|{
return|return
name|factory
operator|.
name|createSetColumnTypeToModel
argument_list|(
name|getEntity
argument_list|()
argument_list|,
name|columnNew
argument_list|,
name|columnOriginal
argument_list|)
return|;
block|}
specifier|public
name|DbAttribute
name|getColumnOriginal
parameter_list|()
block|{
return|return
name|columnOriginal
return|;
block|}
specifier|public
name|DbAttribute
name|getColumnNew
parameter_list|()
block|{
return|return
name|columnNew
return|;
block|}
block|}
end_class

end_unit

