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
name|ArrayList
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
name|map
operator|.
name|DbEntity
import|;
end_import

begin_class
specifier|public
class|class
name|SetPrimaryKeyToDb
extends|extends
name|AbstractToDbToken
operator|.
name|Entity
block|{
specifier|private
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|primaryKeyOriginal
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|primaryKeyNew
decl_stmt|;
specifier|private
name|String
name|detectedPrimaryKeyName
decl_stmt|;
specifier|public
name|SetPrimaryKeyToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|primaryKeyOriginal
parameter_list|,
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|primaryKeyNew
parameter_list|,
name|String
name|detectedPrimaryKeyName
parameter_list|)
block|{
name|super
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|this
operator|.
name|primaryKeyOriginal
operator|=
name|primaryKeyOriginal
expr_stmt|;
name|this
operator|.
name|primaryKeyNew
operator|=
name|primaryKeyNew
expr_stmt|;
name|this
operator|.
name|detectedPrimaryKeyName
operator|=
name|detectedPrimaryKeyName
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
name|List
argument_list|<
name|String
argument_list|>
name|sqls
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|primaryKeyOriginal
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|appendDropOriginalPrimaryKeySQL
argument_list|(
name|adapter
argument_list|,
name|sqls
argument_list|)
expr_stmt|;
block|}
name|appendAddNewPrimaryKeySQL
argument_list|(
name|adapter
argument_list|,
name|sqls
argument_list|)
expr_stmt|;
return|return
name|sqls
return|;
block|}
specifier|protected
name|void
name|appendDropOriginalPrimaryKeySQL
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|sqls
parameter_list|)
block|{
if|if
condition|(
name|detectedPrimaryKeyName
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|sqls
operator|.
name|add
argument_list|(
literal|"ALTER TABLE "
operator|+
name|getQuotingStrategy
argument_list|(
name|adapter
argument_list|)
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|getEntity
argument_list|()
argument_list|)
operator|+
literal|" DROP CONSTRAINT "
operator|+
name|detectedPrimaryKeyName
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|appendAddNewPrimaryKeySQL
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|sqls
parameter_list|)
block|{
name|QuotingStrategy
name|quotingStrategy
init|=
name|getQuotingStrategy
argument_list|(
name|adapter
argument_list|)
decl_stmt|;
name|StringBuilder
name|sql
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sql
operator|.
name|append
argument_list|(
literal|"ALTER TABLE "
argument_list|)
expr_stmt|;
name|sql
operator|.
name|append
argument_list|(
name|quotingStrategy
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sql
operator|.
name|append
argument_list|(
literal|" ADD PRIMARY KEY ("
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|it
init|=
name|primaryKeyNew
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|sql
operator|.
name|append
argument_list|(
name|quotingStrategy
operator|.
name|quotedIdentifier
argument_list|(
name|it
operator|.
name|next
argument_list|()
operator|.
name|getName
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
name|sql
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
name|sql
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
name|sqls
operator|.
name|add
argument_list|(
name|sql
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
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
name|createSetPrimaryKeyToModel
argument_list|(
name|getEntity
argument_list|()
argument_list|,
name|primaryKeyNew
argument_list|,
name|primaryKeyOriginal
argument_list|,
name|detectedPrimaryKeyName
argument_list|)
return|;
block|}
specifier|public
name|String
name|getTokenName
parameter_list|()
block|{
return|return
literal|"Set Primary Key"
return|;
block|}
block|}
end_class

end_unit

