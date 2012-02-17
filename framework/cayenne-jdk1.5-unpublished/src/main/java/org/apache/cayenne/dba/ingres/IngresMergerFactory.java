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
name|ingres
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
name|DbJoin
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
name|DbRelationship
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
name|merge
operator|.
name|AddRelationshipToDb
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
name|merge
operator|.
name|DropColumnToDb
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
name|merge
operator|.
name|DropRelationshipToDb
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
name|merge
operator|.
name|MergerFactory
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
name|merge
operator|.
name|MergerToken
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
name|merge
operator|.
name|SetColumnTypeToDb
import|;
end_import

begin_class
specifier|public
class|class
name|IngresMergerFactory
extends|extends
name|MergerFactory
block|{
annotation|@
name|Override
specifier|public
name|MergerToken
name|createSetColumnTypeToDb
parameter_list|(
specifier|final
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|columnOriginal
parameter_list|,
specifier|final
name|DbAttribute
name|columnNew
parameter_list|)
block|{
return|return
operator|new
name|SetColumnTypeToDb
argument_list|(
name|entity
argument_list|,
name|columnOriginal
argument_list|,
name|columnNew
argument_list|)
block|{
annotation|@
name|Override
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
name|entity
argument_list|)
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" ALTER COLUMN "
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
literal|" "
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|MergerToken
name|createDropColumnToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
return|return
operator|new
name|DropColumnToDb
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
block|{
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
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
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
name|buf
operator|.
name|append
argument_list|(
literal|"ALTER TABLE "
argument_list|)
expr_stmt|;
name|buf
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
name|buf
operator|.
name|append
argument_list|(
literal|" DROP COLUMN "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteString
argument_list|(
name|getColumn
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" RESTRICT "
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|MergerToken
name|createAddRelationshipToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
specifier|final
name|DbRelationship
name|rel
parameter_list|)
block|{
return|return
operator|new
name|AddRelationshipToDb
argument_list|(
name|entity
argument_list|,
name|rel
argument_list|)
block|{
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
if|if
condition|(
operator|!
name|rel
operator|.
name|isToMany
argument_list|()
operator|&&
name|rel
operator|.
name|isToPK
argument_list|()
operator|&&
operator|!
name|rel
operator|.
name|isToDependentPK
argument_list|()
condition|)
block|{
name|DbEntity
name|source
init|=
operator|(
name|DbEntity
operator|)
name|rel
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
name|boolean
name|status
init|=
operator|(
name|source
operator|.
name|getDataMap
argument_list|()
operator|!=
literal|null
operator|&&
name|source
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
operator|)
decl_stmt|;
name|QuotingStrategy
name|context
init|=
name|adapter
operator|.
name|getQuotingStrategy
argument_list|(
name|status
argument_list|)
decl_stmt|;
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|StringBuilder
name|refBuf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"ALTER TABLE "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteFullyQualifiedName
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
comment|// requires the ADD CONSTRAINT statement
name|buf
operator|.
name|append
argument_list|(
literal|" ADD CONSTRAINT "
argument_list|)
expr_stmt|;
name|String
name|name
init|=
literal|"U_"
operator|+
name|rel
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"_"
operator|+
operator|(
name|long
operator|)
operator|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
operator|(
name|Math
operator|.
name|random
argument_list|()
operator|*
literal|100000
operator|)
operator|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteString
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" FOREIGN KEY ("
argument_list|)
expr_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|rel
operator|.
name|getJoins
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|first
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|refBuf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
else|else
name|first
operator|=
literal|false
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteString
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|refBuf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteString
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|") REFERENCES "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteFullyQualifiedName
argument_list|(
operator|(
name|DbEntity
operator|)
name|rel
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|refBuf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
name|String
name|fksql
init|=
name|buf
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|fksql
operator|!=
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|fksql
argument_list|)
return|;
block|}
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|MergerToken
name|createDropRelationshipToDb
parameter_list|(
specifier|final
name|DbEntity
name|entity
parameter_list|,
name|DbRelationship
name|rel
parameter_list|)
block|{
return|return
operator|new
name|DropRelationshipToDb
argument_list|(
name|entity
argument_list|,
name|rel
argument_list|)
block|{
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
name|String
name|fkName
init|=
name|getFkName
argument_list|()
decl_stmt|;
if|if
condition|(
name|fkName
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
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
literal|"ALTER TABLE "
argument_list|)
expr_stmt|;
name|buf
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
name|buf
operator|.
name|append
argument_list|(
literal|" DROP CONSTRAINT "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|fkName
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" CASCADE "
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

