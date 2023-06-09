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
name|dbsync
operator|.
name|merge
operator|.
name|token
operator|.
name|db
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|token
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
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|SetGeneratedFlagToDb
extends|extends
name|AbstractToDbToken
operator|.
name|EntityAndColumn
block|{
specifier|private
specifier|final
name|boolean
name|isGenerated
decl_stmt|;
specifier|public
name|SetGeneratedFlagToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbAttribute
name|column
parameter_list|,
name|boolean
name|isGenerated
parameter_list|)
block|{
comment|// drop generated attribute must go first
name|super
argument_list|(
literal|"Set Is Generated"
argument_list|,
name|isGenerated
condition|?
literal|111
else|:
literal|109
argument_list|,
name|entity
argument_list|,
name|column
argument_list|)
expr_stmt|;
name|this
operator|.
name|isGenerated
operator|=
name|isGenerated
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|MergerToken
name|createReverse
parameter_list|(
name|MergerTokenFactory
name|factory
parameter_list|)
block|{
return|return
name|factory
operator|.
name|createSetGeneratedFlagToModel
argument_list|(
name|getEntity
argument_list|()
argument_list|,
name|getColumn
argument_list|()
argument_list|,
operator|!
name|isGenerated
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|protected
name|void
name|appendAutoIncrement
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|,
name|StringBuffer
name|builder
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported on generic DB"
argument_list|)
throw|;
block|}
specifier|protected
name|void
name|appendDropAutoIncrement
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|,
name|StringBuffer
name|builder
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported on generic DB"
argument_list|)
throw|;
block|}
specifier|protected
name|void
name|appendAlterColumnClause
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|,
name|StringBuffer
name|builder
parameter_list|)
block|{
name|QuotingStrategy
name|context
init|=
name|adapter
operator|.
name|getQuotingStrategy
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|" ALTER COLUMN "
argument_list|)
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedName
argument_list|(
name|getColumn
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|adapter
operator|.
name|supportsGeneratedKeys
argument_list|()
condition|)
block|{
return|return
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
name|QuotingStrategy
name|context
init|=
name|adapter
operator|.
name|getQuotingStrategy
argument_list|()
decl_stmt|;
name|StringBuffer
name|builder
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"ALTER TABLE "
argument_list|)
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|appendAlterColumnClause
argument_list|(
name|adapter
argument_list|,
name|builder
argument_list|)
expr_stmt|;
if|if
condition|(
name|isGenerated
condition|)
block|{
name|appendAutoIncrement
argument_list|(
name|adapter
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|appendDropAutoIncrement
argument_list|(
name|adapter
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|builder
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

