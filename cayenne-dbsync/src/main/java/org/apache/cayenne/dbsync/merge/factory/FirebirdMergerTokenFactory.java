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
operator|.
name|factory
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
name|token
operator|.
name|AddColumnToDb
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
name|dbsync
operator|.
name|merge
operator|.
name|token
operator|.
name|SetAllowNullToDb
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
name|SetNotNullToDb
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
name|FirebirdMergerTokenFactory
extends|extends
name|DefaultMergerTokenFactory
block|{
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
name|QuotingStrategy
name|quoting
init|=
name|adapter
operator|.
name|getQuotingStrategy
argument_list|()
decl_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"ALTER TABLE "
operator|+
name|quoting
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|getEntity
argument_list|()
argument_list|)
operator|+
literal|" DROP "
operator|+
name|quoting
operator|.
name|quotedName
argument_list|(
name|getColumn
argument_list|()
argument_list|)
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
name|createSetNotNullToDb
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
name|SetNotNullToDb
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
block|{
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
name|QuotingStrategy
name|context
init|=
name|adapter
operator|.
name|getQuotingStrategy
argument_list|()
decl_stmt|;
name|String
name|entityName
init|=
name|context
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|columnName
init|=
name|context
operator|.
name|quotedName
argument_list|(
name|getColumn
argument_list|()
argument_list|)
decl_stmt|;
comment|// Firebird doesn't support ALTER TABLE table_name ALTER column_name SET NOT NULL
comment|// but this might be achived by modyfication of system tables
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"UPDATE RDB$RELATION_FIELDS SET RDB$NULL_FLAG = 1 "
operator|+
literal|"WHERE RDB$FIELD_NAME = '%s' AND RDB$RELATION_NAME = '%s'"
argument_list|,
name|columnName
argument_list|,
name|entityName
argument_list|)
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
name|createSetAllowNullToDb
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
name|SetAllowNullToDb
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
block|{
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
name|QuotingStrategy
name|context
init|=
name|adapter
operator|.
name|getQuotingStrategy
argument_list|()
decl_stmt|;
name|String
name|entityName
init|=
name|context
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|columnName
init|=
name|context
operator|.
name|quotedName
argument_list|(
name|getColumn
argument_list|()
argument_list|)
decl_stmt|;
comment|// Firebird doesn't support ALTER TABLE table_name ALTER column_name DROP NOT NULL
comment|// but this might be achived by modyfication system tables
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"UPDATE RDB$RELATION_FIELDS SET RDB$NULL_FLAG = NULL "
operator|+
literal|" WHERE RDB$FIELD_NAME = '%s' AND RDB$RELATION_NAME = '%s'"
argument_list|,
name|columnName
argument_list|,
name|entityName
argument_list|)
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
name|createAddColumnToDb
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
name|AddColumnToDb
argument_list|(
name|entity
argument_list|,
name|column
argument_list|)
block|{
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
name|quotedFullyQualifiedName
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
literal|" ADD "
argument_list|)
expr_stmt|;
name|sqlBuffer
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
block|}
end_class

end_unit

