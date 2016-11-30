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
name|dbsync
operator|.
name|merge
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
name|DataNode
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
name|validation
operator|.
name|SimpleValidationFailure
import|;
end_import

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

begin_class
specifier|public
class|class
name|CreateTableToDb
extends|extends
name|AbstractToDbToken
operator|.
name|Entity
block|{
specifier|public
name|CreateTableToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|super
argument_list|(
literal|"Create Table"
argument_list|,
name|entity
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
name|needAutoPkSupport
argument_list|()
condition|)
block|{
name|sqls
operator|.
name|addAll
argument_list|(
name|adapter
operator|.
name|getPkGenerator
argument_list|()
operator|.
name|createAutoPkStatements
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|getEntity
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sqls
operator|.
name|add
argument_list|(
name|adapter
operator|.
name|createTable
argument_list|(
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|sqls
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|MergerContext
name|mergerContext
parameter_list|)
block|{
try|try
block|{
name|DataNode
name|node
init|=
name|mergerContext
operator|.
name|getDataNode
argument_list|()
decl_stmt|;
name|DbAdapter
name|adapter
init|=
name|node
operator|.
name|getAdapter
argument_list|()
decl_stmt|;
if|if
condition|(
name|needAutoPkSupport
argument_list|()
condition|)
block|{
name|adapter
operator|.
name|getPkGenerator
argument_list|()
operator|.
name|createAutoPk
argument_list|(
name|node
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|executeSql
argument_list|(
name|mergerContext
argument_list|,
name|adapter
operator|.
name|createTable
argument_list|(
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|mergerContext
operator|.
name|getValidationResult
argument_list|()
operator|.
name|addFailure
argument_list|(
operator|new
name|SimpleValidationFailure
argument_list|(
name|this
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|needAutoPkSupport
parameter_list|()
block|{
name|DbEntity
name|entity
init|=
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|.
name|getPrimaryKeyGenerator
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|DbAttribute
name|attribute
range|:
name|entity
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
if|if
condition|(
name|attribute
operator|.
name|isGenerated
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
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
name|createDropTableToModel
argument_list|(
name|getEntity
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

