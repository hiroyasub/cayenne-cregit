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
name|jdbc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|ObjectContext
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
name|PersistenceState
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
name|trans
operator|.
name|DeleteBatchQueryBuilder
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
name|JdbcAdapter
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
name|di
operator|.
name|AdhocObjectFactory
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
name|di
operator|.
name|Inject
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
name|exp
operator|.
name|ExpressionFactory
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
name|query
operator|.
name|DeleteBatchQuery
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
name|SQLTemplate
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
name|SelectQuery
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
name|test
operator|.
name|parallel
operator|.
name|ParallelTestContainer
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
name|testdo
operator|.
name|locking
operator|.
name|SoftTest
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
name|unit
operator|.
name|UnitDbAdapter
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|LOCKING_PROJECT
argument_list|)
specifier|public
class|class
name|SoftDeleteBatchQueryBuilderTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DbAdapter
name|adapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|unitAdapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
specifier|private
name|DeleteBatchQueryBuilder
name|createBuilder
parameter_list|()
block|{
name|JdbcAdapter
name|adapter
init|=
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|JdbcAdapter
operator|.
name|class
argument_list|,
name|JdbcAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|createBuilder
argument_list|(
name|adapter
argument_list|)
return|;
block|}
specifier|private
name|DeleteBatchQueryBuilder
name|createBuilder
parameter_list|(
name|JdbcAdapter
name|adapter
parameter_list|)
block|{
return|return
operator|(
name|DeleteBatchQueryBuilder
operator|)
operator|new
name|SoftDeleteQueryBuilderFactory
argument_list|()
operator|.
name|createDeleteQueryBuilder
argument_list|(
name|adapter
argument_list|)
return|;
block|}
specifier|public
name|void
name|testCreateSqlString
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|SoftTest
operator|.
name|class
argument_list|)
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|idAttributes
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|DbAttribute
operator|)
name|entity
operator|.
name|getAttribute
argument_list|(
literal|"SOFT_TEST_ID"
argument_list|)
argument_list|)
decl_stmt|;
name|DeleteBatchQuery
name|deleteQuery
init|=
operator|new
name|DeleteBatchQuery
argument_list|(
name|entity
argument_list|,
name|idAttributes
argument_list|,
literal|null
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|DeleteBatchQueryBuilder
name|builder
init|=
name|createBuilder
argument_list|()
decl_stmt|;
name|String
name|generatedSql
init|=
name|builder
operator|.
name|createSqlString
argument_list|(
name|deleteQuery
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|generatedSql
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"UPDATE "
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|" SET DELETED = ? WHERE SOFT_TEST_ID = ?"
argument_list|,
name|generatedSql
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateSqlStringWithNulls
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|SoftTest
operator|.
name|class
argument_list|)
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|idAttributes
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|DbAttribute
operator|)
name|entity
operator|.
name|getAttribute
argument_list|(
literal|"SOFT_TEST_ID"
argument_list|)
argument_list|,
operator|(
name|DbAttribute
operator|)
name|entity
operator|.
name|getAttribute
argument_list|(
literal|"NAME"
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|nullAttributes
init|=
name|Collections
operator|.
name|singleton
argument_list|(
literal|"NAME"
argument_list|)
decl_stmt|;
name|DeleteBatchQuery
name|deleteQuery
init|=
operator|new
name|DeleteBatchQuery
argument_list|(
name|entity
argument_list|,
name|idAttributes
argument_list|,
name|nullAttributes
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|DeleteBatchQueryBuilder
name|builder
init|=
name|createBuilder
argument_list|()
decl_stmt|;
name|String
name|generatedSql
init|=
name|builder
operator|.
name|createSqlString
argument_list|(
name|deleteQuery
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|generatedSql
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"UPDATE "
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|" SET DELETED = ? WHERE SOFT_TEST_ID = ? AND NAME IS NULL"
argument_list|,
name|generatedSql
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateSqlStringWithIdentifiersQuote
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|SoftTest
operator|.
name|class
argument_list|)
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
try|try
block|{
name|entity
operator|.
name|getDataMap
argument_list|()
operator|.
name|setQuotingSQLIdentifiers
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|idAttributes
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
operator|(
name|DbAttribute
operator|)
name|entity
operator|.
name|getAttribute
argument_list|(
literal|"SOFT_TEST_ID"
argument_list|)
argument_list|)
decl_stmt|;
name|DeleteBatchQuery
name|deleteQuery
init|=
operator|new
name|DeleteBatchQuery
argument_list|(
name|entity
argument_list|,
name|idAttributes
argument_list|,
literal|null
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|JdbcAdapter
name|adapter
init|=
operator|(
name|JdbcAdapter
operator|)
name|this
operator|.
name|adapter
decl_stmt|;
name|DeleteBatchQueryBuilder
name|builder
init|=
name|createBuilder
argument_list|(
name|adapter
argument_list|)
decl_stmt|;
name|String
name|generatedSql
init|=
name|builder
operator|.
name|createSqlString
argument_list|(
name|deleteQuery
argument_list|)
decl_stmt|;
name|String
name|charStart
init|=
name|unitAdapter
operator|.
name|getIdentifiersStartQuote
argument_list|()
decl_stmt|;
name|String
name|charEnd
init|=
name|unitAdapter
operator|.
name|getIdentifiersEndQuote
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|generatedSql
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"UPDATE "
operator|+
name|charStart
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
name|charEnd
operator|+
literal|" SET "
operator|+
name|charStart
operator|+
literal|"DELETED"
operator|+
name|charEnd
operator|+
literal|" = ? WHERE "
operator|+
name|charStart
operator|+
literal|"SOFT_TEST_ID"
operator|+
name|charEnd
operator|+
literal|" = ?"
argument_list|,
name|generatedSql
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|entity
operator|.
name|getDataMap
argument_list|()
operator|.
name|setQuotingSQLIdentifiers
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testUpdate
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DbEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|SoftTest
operator|.
name|class
argument_list|)
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|JdbcAdapter
name|adapter
init|=
operator|(
name|JdbcAdapter
operator|)
name|this
operator|.
name|adapter
decl_stmt|;
name|BatchQueryBuilderFactory
name|oldFactory
init|=
name|adapter
operator|.
name|getBatchQueryBuilderFactory
argument_list|()
decl_stmt|;
try|try
block|{
name|adapter
operator|.
name|setBatchQueryBuilderFactory
argument_list|(
operator|new
name|SoftDeleteQueryBuilderFactory
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|SoftTest
name|test
init|=
name|context
operator|.
name|newObject
argument_list|(
name|SoftTest
operator|.
name|class
argument_list|)
decl_stmt|;
name|test
operator|.
name|setName
argument_list|(
literal|"SoftDeleteBatchQueryBuilderTest"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
specifier|final
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|SoftTest
operator|.
name|class
argument_list|)
decl_stmt|;
operator|new
name|ParallelTestContainer
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|assertResult
parameter_list|()
throws|throws
name|Exception
block|{
name|query
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"name"
argument_list|,
name|test
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|.
name|andQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchDbExp
argument_list|(
literal|"DELETED"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|runTest
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|test
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|test
operator|.
name|getPersistenceState
argument_list|()
argument_list|,
name|PersistenceState
operator|.
name|DELETED
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
operator|new
name|ParallelTestContainer
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|assertResult
parameter_list|()
throws|throws
name|Exception
block|{
name|query
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"name"
argument_list|,
name|test
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SQLTemplate
name|template
init|=
operator|new
name|SQLTemplate
argument_list|(
name|entity
argument_list|,
literal|"SELECT * FROM SOFT_TEST"
argument_list|)
decl_stmt|;
name|template
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|performQuery
argument_list|(
name|template
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|runTest
argument_list|(
literal|200
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|entity
argument_list|,
literal|"DELETE FROM SOFT_TEST"
argument_list|)
argument_list|)
expr_stmt|;
name|adapter
operator|.
name|setBatchQueryBuilderFactory
argument_list|(
name|oldFactory
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
