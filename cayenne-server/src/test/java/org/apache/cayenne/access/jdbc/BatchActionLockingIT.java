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
name|access
operator|.
name|jdbc
package|;
end_package

begin_import
import|import
name|com
operator|.
name|mockrunner
operator|.
name|jdbc
operator|.
name|PreparedStatementResultSetHandler
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mockrunner
operator|.
name|mock
operator|.
name|jdbc
operator|.
name|MockConnection
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
name|access
operator|.
name|MockOperationObserver
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
name|OptimisticLockException
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
name|jdbc
operator|.
name|reader
operator|.
name|RowReaderFactory
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
name|DeleteBatchTranslator
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
name|di
operator|.
name|Injector
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
name|EntityResolver
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
name|di
operator|.
name|server
operator|.
name|CayenneProjects
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|LOCKING_PROJECT
argument_list|)
specifier|public
class|class
name|BatchActionLockingIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|Injector
name|injector
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testRunAsIndividualQueriesSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|EntityResolver
name|resolver
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
comment|// test with adapter that supports keys...
name|JdbcAdapter
name|adapter
init|=
name|buildAdapter
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|DbEntity
name|dbEntity
init|=
name|resolver
operator|.
name|getObjEntity
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
argument_list|<
name|DbAttribute
argument_list|>
name|qualifierAttributes
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|dbEntity
operator|.
name|getAttribute
argument_list|(
literal|"LOCKING_TEST_ID"
argument_list|)
argument_list|,
name|dbEntity
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
name|nullAttributeNames
init|=
name|Collections
operator|.
name|singleton
argument_list|(
literal|"NAME"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|qualifierSnapshot
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|qualifierSnapshot
operator|.
name|put
argument_list|(
literal|"LOCKING_TEST_ID"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|DeleteBatchQuery
name|batchQuery
init|=
operator|new
name|DeleteBatchQuery
argument_list|(
name|dbEntity
argument_list|,
name|qualifierAttributes
argument_list|,
name|nullAttributeNames
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|batchQuery
operator|.
name|setUsingOptimisticLocking
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|batchQuery
operator|.
name|add
argument_list|(
name|qualifierSnapshot
argument_list|)
expr_stmt|;
name|DeleteBatchTranslator
name|batchQueryBuilder
init|=
operator|new
name|DeleteBatchTranslator
argument_list|(
name|batchQuery
argument_list|,
name|adapter
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|MockConnection
name|mockConnection
init|=
operator|new
name|MockConnection
argument_list|()
decl_stmt|;
name|PreparedStatementResultSetHandler
name|preparedStatementResultSetHandler
init|=
name|mockConnection
operator|.
name|getPreparedStatementResultSetHandler
argument_list|()
decl_stmt|;
name|preparedStatementResultSetHandler
operator|.
name|setExactMatch
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|preparedStatementResultSetHandler
operator|.
name|setCaseSensitive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|preparedStatementResultSetHandler
operator|.
name|prepareUpdateCount
argument_list|(
literal|"DELETE"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|boolean
name|generatesKeys
init|=
literal|false
decl_stmt|;
name|DataNode
name|node
init|=
operator|new
name|DataNode
argument_list|()
decl_stmt|;
name|node
operator|.
name|setAdapter
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|node
operator|.
name|setEntityResolver
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|node
operator|.
name|setRowReaderFactory
argument_list|(
name|mock
argument_list|(
name|RowReaderFactory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|BatchAction
name|action
init|=
operator|new
name|BatchAction
argument_list|(
name|batchQuery
argument_list|,
name|node
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|action
operator|.
name|runAsIndividualQueries
argument_list|(
name|mockConnection
argument_list|,
name|batchQueryBuilder
argument_list|,
operator|new
name|MockOperationObserver
argument_list|()
argument_list|,
name|generatesKeys
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|mockConnection
operator|.
name|getNumberCommits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|mockConnection
operator|.
name|getNumberRollbacks
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRunAsIndividualQueriesOptimisticLockingFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|EntityResolver
name|resolver
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
comment|// test with adapter that supports keys...
name|JdbcAdapter
name|adapter
init|=
name|buildAdapter
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|DbEntity
name|dbEntity
init|=
name|resolver
operator|.
name|getObjEntity
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
argument_list|<
name|DbAttribute
argument_list|>
name|qualifierAttributes
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|dbEntity
operator|.
name|getAttribute
argument_list|(
literal|"LOCKING_TEST_ID"
argument_list|)
argument_list|,
name|dbEntity
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
name|nullAttributeNames
init|=
name|Collections
operator|.
name|singleton
argument_list|(
literal|"NAME"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|qualifierSnapshot
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|qualifierSnapshot
operator|.
name|put
argument_list|(
literal|"LOCKING_TEST_ID"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|DeleteBatchQuery
name|batchQuery
init|=
operator|new
name|DeleteBatchQuery
argument_list|(
name|dbEntity
argument_list|,
name|qualifierAttributes
argument_list|,
name|nullAttributeNames
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|batchQuery
operator|.
name|setUsingOptimisticLocking
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|batchQuery
operator|.
name|add
argument_list|(
name|qualifierSnapshot
argument_list|)
expr_stmt|;
name|DeleteBatchTranslator
name|batchQueryBuilder
init|=
operator|new
name|DeleteBatchTranslator
argument_list|(
name|batchQuery
argument_list|,
name|adapter
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|MockConnection
name|mockConnection
init|=
operator|new
name|MockConnection
argument_list|()
decl_stmt|;
name|PreparedStatementResultSetHandler
name|preparedStatementResultSetHandler
init|=
name|mockConnection
operator|.
name|getPreparedStatementResultSetHandler
argument_list|()
decl_stmt|;
name|preparedStatementResultSetHandler
operator|.
name|setExactMatch
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|preparedStatementResultSetHandler
operator|.
name|setCaseSensitive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|preparedStatementResultSetHandler
operator|.
name|prepareUpdateCount
argument_list|(
literal|"DELETE"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|boolean
name|generatesKeys
init|=
literal|false
decl_stmt|;
name|DataNode
name|node
init|=
operator|new
name|DataNode
argument_list|()
decl_stmt|;
name|node
operator|.
name|setAdapter
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|node
operator|.
name|setEntityResolver
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|node
operator|.
name|setRowReaderFactory
argument_list|(
name|mock
argument_list|(
name|RowReaderFactory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|BatchAction
name|action
init|=
operator|new
name|BatchAction
argument_list|(
name|batchQuery
argument_list|,
name|node
argument_list|,
literal|false
argument_list|)
decl_stmt|;
try|try
block|{
name|action
operator|.
name|runAsIndividualQueries
argument_list|(
name|mockConnection
argument_list|,
name|batchQueryBuilder
argument_list|,
operator|new
name|MockOperationObserver
argument_list|()
argument_list|,
name|generatesKeys
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"No OptimisticLockingFailureException thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OptimisticLockException
name|e
parameter_list|)
block|{
block|}
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|mockConnection
operator|.
name|getNumberCommits
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|mockConnection
operator|.
name|getNumberRollbacks
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|JdbcAdapter
name|buildAdapter
parameter_list|(
name|boolean
name|supportGeneratedKeys
parameter_list|)
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
name|adapter
operator|.
name|setSupportsGeneratedKeys
argument_list|(
name|supportGeneratedKeys
argument_list|)
expr_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
return|return
name|adapter
return|;
block|}
block|}
end_class

end_unit

