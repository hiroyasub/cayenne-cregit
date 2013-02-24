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
name|trans
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|InsertBatchQuery
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
name|SimpleLockingTestEntity
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
name|InsertBatchQueryBuilderTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
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
specifier|public
name|void
name|testConstructor
parameter_list|()
throws|throws
name|Exception
block|{
name|DbAdapter
name|adapter
init|=
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|DbAdapter
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
name|DeleteBatchQueryBuilder
name|builder
init|=
operator|new
name|DeleteBatchQueryBuilder
argument_list|(
name|adapter
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|adapter
argument_list|,
name|builder
operator|.
name|getAdapter
argument_list|()
argument_list|)
expr_stmt|;
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
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|DbAdapter
name|adapter
init|=
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|DbAdapter
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
name|InsertBatchQuery
name|deleteQuery
init|=
operator|new
name|InsertBatchQuery
argument_list|(
name|entity
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|InsertBatchQueryBuilder
name|builder
init|=
operator|new
name|InsertBatchQueryBuilder
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
name|assertNotNull
argument_list|(
name|generatedSql
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"INSERT INTO "
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|" (DESCRIPTION, LOCKING_TEST_ID, NAME) VALUES (?, ?, ?)"
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
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|SimpleLockingTestEntity
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
name|InsertBatchQuery
name|deleteQuery
init|=
operator|new
name|InsertBatchQuery
argument_list|(
name|entity
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|InsertBatchQueryBuilder
name|builder
init|=
operator|new
name|InsertBatchQueryBuilder
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
literal|"INSERT INTO "
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
literal|" ("
operator|+
name|charStart
operator|+
literal|"DESCRIPTION"
operator|+
name|charEnd
operator|+
literal|", "
operator|+
name|charStart
operator|+
literal|"LOCKING_TEST_ID"
operator|+
name|charEnd
operator|+
literal|", "
operator|+
name|charStart
operator|+
literal|"NAME"
operator|+
name|charEnd
operator|+
literal|") VALUES (?, ?, ?)"
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
block|}
end_class

end_unit

