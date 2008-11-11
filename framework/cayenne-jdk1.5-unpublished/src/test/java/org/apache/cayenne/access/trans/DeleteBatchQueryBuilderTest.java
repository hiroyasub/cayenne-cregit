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
name|LockingCase
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DeleteBatchQueryBuilderTest
extends|extends
name|LockingCase
block|{
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
operator|new
name|JdbcAdapter
argument_list|()
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
name|getDomain
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
name|List
name|idAttributes
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|entity
operator|.
name|getAttribute
argument_list|(
literal|"LOCKING_TEST_ID"
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
operator|new
name|DeleteBatchQueryBuilder
argument_list|(
operator|new
name|JdbcAdapter
argument_list|()
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
literal|"DELETE FROM "
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|" WHERE LOCKING_TEST_ID = ?"
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
name|getDomain
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
name|List
name|idAttributes
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|entity
operator|.
name|getAttribute
argument_list|(
literal|"LOCKING_TEST_ID"
argument_list|)
argument_list|,
name|entity
operator|.
name|getAttribute
argument_list|(
literal|"NAME"
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
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
operator|new
name|DeleteBatchQueryBuilder
argument_list|(
operator|new
name|JdbcAdapter
argument_list|()
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
literal|"DELETE FROM "
operator|+
name|entity
operator|.
name|getName
argument_list|()
operator|+
literal|" WHERE LOCKING_TEST_ID = ? AND NAME IS NULL"
argument_list|,
name|generatedSql
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

