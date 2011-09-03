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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
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
name|query
operator|.
name|Ordering
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
name|query
operator|.
name|SortOrder
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
name|jdbc
operator|.
name|DBHelper
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
name|jdbc
operator|.
name|TableHelper
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
name|RelLockingTestEntity
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
name|OptimisticLockingTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|tSimpleLockingTest
decl_stmt|;
specifier|protected
name|TableHelper
name|tRelLockingTest
decl_stmt|;
specifier|protected
name|TableHelper
name|tLockingHelper
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"LOCKING_HELPER"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"REL_LOCKING_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"SIMPLE_LOCKING_TEST"
argument_list|)
expr_stmt|;
name|tSimpleLockingTest
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"SIMPLE_LOCKING_TEST"
argument_list|)
expr_stmt|;
name|tSimpleLockingTest
operator|.
name|setColumns
argument_list|(
literal|"LOCKING_TEST_ID"
argument_list|,
literal|"NAME"
argument_list|,
literal|"DESCRIPTION"
argument_list|)
operator|.
name|setColumnTypes
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|)
expr_stmt|;
name|tRelLockingTest
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"REL_LOCKING_TEST"
argument_list|)
expr_stmt|;
name|tRelLockingTest
operator|.
name|setColumns
argument_list|(
literal|"REL_LOCKING_TEST_ID"
argument_list|,
literal|"SIMPLE_LOCKING_TEST_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
name|tLockingHelper
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"LOCKING_HELPER"
argument_list|)
expr_stmt|;
name|tLockingHelper
operator|.
name|setColumns
argument_list|(
literal|"LOCKING_HELPER_ID"
argument_list|,
literal|"REL_LOCKING_TEST_ID"
argument_list|,
literal|"NAME"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createSimpleLockingDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tLockingHelper
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
name|tRelLockingTest
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
name|tSimpleLockingTest
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
name|tSimpleLockingTest
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"LockTest1"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createLockingOnNullDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tLockingHelper
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
name|tRelLockingTest
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
name|tSimpleLockingTest
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
name|tSimpleLockingTest
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createLockingOnMixedDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tLockingHelper
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
name|tRelLockingTest
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
name|tSimpleLockingTest
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
name|tSimpleLockingTest
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tSimpleLockingTest
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"LockTest2"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tSimpleLockingTest
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"LockTest3"
argument_list|,
literal|"Another Lock Test"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createLockingOnToOneDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tLockingHelper
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
name|tRelLockingTest
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
name|tSimpleLockingTest
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
name|tSimpleLockingTest
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"LockTest1"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|tRelLockingTest
operator|.
name|insert
argument_list|(
literal|5
argument_list|,
literal|1
argument_list|,
literal|"Rel Test 1"
argument_list|)
expr_stmt|;
name|tLockingHelper
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|5
argument_list|,
literal|"Locking Helper 1"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createSimpleLockUpdate
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tSimpleLockingTest
operator|.
name|update
argument_list|()
operator|.
name|set
argument_list|(
literal|"NAME"
argument_list|,
literal|"LockTest1Updated"
argument_list|)
operator|.
name|where
argument_list|(
literal|"LOCKING_TEST_ID"
argument_list|,
literal|1
argument_list|)
operator|.
name|execute
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createRelLockUpdate
parameter_list|()
throws|throws
name|Exception
block|{
name|tRelLockingTest
operator|.
name|update
argument_list|()
operator|.
name|set
argument_list|(
literal|"SIMPLE_LOCKING_TEST_ID"
argument_list|,
literal|1
argument_list|)
operator|.
name|where
argument_list|(
literal|"REL_LOCKING_TEST_ID"
argument_list|,
literal|5
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|createSimpleLockDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|tSimpleLockingTest
operator|.
name|delete
argument_list|()
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessSimpleLockingOnDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|createSimpleLockingDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// change description and save... no optimistic lock failure expected
name|object
operator|.
name|setDescription
argument_list|(
literal|"first update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessSimpleLockingOnDeleteFollowedByInvalidate
parameter_list|()
throws|throws
name|Exception
block|{
name|createSimpleLockingDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// change description and save... no optimistic lock failure expected
name|object
operator|.
name|setDescription
argument_list|(
literal|"first update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessSimpleLockingOnDeleteFollowedByForgetSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|createSimpleLockingDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// change description and save... no optimistic lock failure expected
name|object
operator|.
name|setDescription
argument_list|(
literal|"first update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getDataRowCache
argument_list|()
operator|.
name|forgetSnapshot
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessSimpleLockingOnDeletePrecededByInvalidate
parameter_list|()
throws|throws
name|Exception
block|{
name|createSimpleLockingDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// change description and save... no optimistic lock failure expected
name|object
operator|.
name|setDescription
argument_list|(
literal|"first update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessSimpleLockingOnDeletePrecededByForgetSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|createSimpleLockingDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// change description and save... no optimistic lock failure expected
name|object
operator|.
name|setDescription
argument_list|(
literal|"first update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getDataRowCache
argument_list|()
operator|.
name|forgetSnapshot
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testFailSimpleLockingOnDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|createSimpleLockingDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// change description and save... no optimistic lock failure expected
name|object
operator|.
name|setDescription
argument_list|(
literal|"second update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// change row underneath, delete and save... optimistic lock failure expected
name|createSimpleLockUpdate
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Optimistic lock failure expected."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OptimisticLockException
name|ex
parameter_list|)
block|{
comment|// optimistic lock failure expected...
block|}
block|}
specifier|public
name|void
name|testSuccessSimpleLockingOnUpdate
parameter_list|()
throws|throws
name|Exception
block|{
name|createSimpleLockingDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// change description and save... no optimistic lock failure expected
name|object
operator|.
name|setDescription
argument_list|(
literal|"first update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|object
operator|.
name|setDescription
argument_list|(
literal|"second update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessSimpleLockingOnUpdateFollowedByInvalidate
parameter_list|()
throws|throws
name|Exception
block|{
name|createSimpleLockingDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// change description and save... no optimistic lock failure expected
name|object
operator|.
name|setDescription
argument_list|(
literal|"first update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|object
operator|.
name|setDescription
argument_list|(
literal|"second update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessSimpleLockingOnUpdatePrecededByInvalidate
parameter_list|()
throws|throws
name|Exception
block|{
name|createSimpleLockingDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// change description and save... no optimistic lock failure expected
name|object
operator|.
name|setDescription
argument_list|(
literal|"first update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|invalidateObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|object
operator|.
name|setDescription
argument_list|(
literal|"second update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessSimpleLockingOnUpdateFollowedByForgetSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|createSimpleLockingDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// change description and save... no optimistic lock failure expected
name|object
operator|.
name|setDescription
argument_list|(
literal|"first update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|object
operator|.
name|setDescription
argument_list|(
literal|"second update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getDataRowCache
argument_list|()
operator|.
name|forgetSnapshot
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessSimpleLockingOnUpdatePrecededByForgetSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|createSimpleLockingDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// change description and save... no optimistic lock failure expected
name|object
operator|.
name|setDescription
argument_list|(
literal|"first update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getDataRowCache
argument_list|()
operator|.
name|forgetSnapshot
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|object
operator|.
name|setDescription
argument_list|(
literal|"second update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testFailSimpleLocking
parameter_list|()
throws|throws
name|Exception
block|{
name|createSimpleLockingDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// change description and save... no optimistic lock failure expected
name|object
operator|.
name|setDescription
argument_list|(
literal|"first update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// change row underneath, change description and save... optimistic lock failure
comment|// expected
name|createSimpleLockUpdate
argument_list|()
expr_stmt|;
name|object
operator|.
name|setDescription
argument_list|(
literal|"second update"
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Optimistic lock failure expected."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OptimisticLockException
name|ex
parameter_list|)
block|{
comment|// optimistic lock failure expected...
block|}
block|}
specifier|public
name|void
name|testFailLockingOnNull
parameter_list|()
throws|throws
name|Exception
block|{
name|createLockingOnNullDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// change description and save... no optimistic lock failure expected...
name|object
operator|.
name|setDescription
argument_list|(
literal|"first update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// change row underneath, change description and save... optimistic lock failure
comment|// expected
name|createSimpleLockUpdate
argument_list|()
expr_stmt|;
name|object
operator|.
name|setDescription
argument_list|(
literal|"second update"
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Optimistic lock failure expected."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OptimisticLockException
name|ex
parameter_list|)
block|{
comment|// optimistic lock failure expected...
block|}
block|}
specifier|public
name|void
name|testSuccessLockingOnMixed
parameter_list|()
throws|throws
name|Exception
block|{
name|createLockingOnMixedDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addOrdering
argument_list|(
operator|new
name|Ordering
argument_list|(
literal|"db:LOCKING_TEST_ID"
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object1
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|SimpleLockingTestEntity
name|object2
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|SimpleLockingTestEntity
name|object3
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|// change description and save... no optimistic lock failure expected...
name|object1
operator|.
name|setDescription
argument_list|(
literal|"first update for object1"
argument_list|)
expr_stmt|;
name|object2
operator|.
name|setDescription
argument_list|(
literal|"first update for object2"
argument_list|)
expr_stmt|;
name|object3
operator|.
name|setName
argument_list|(
literal|"object3 - new name"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// TODO: it would be nice to pick inside DataContext to see that 3 batches where
comment|// generated...
comment|// this requires refactoring of ContextCommit.
block|}
specifier|public
name|void
name|testFailLockingOnToOne
parameter_list|()
throws|throws
name|Exception
block|{
name|createLockingOnToOneDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|RelLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|RelLockingTestEntity
name|object
init|=
operator|(
name|RelLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// change name and save... no optimistic lock failure expected
name|object
operator|.
name|setName
argument_list|(
literal|"first update"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// change relationship and save... no optimistic lock failure expected
name|SimpleLockingTestEntity
name|object1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|object
operator|.
name|setToSimpleLockingTest
argument_list|(
name|object1
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// change row underneath, change description and save... optimistic lock failure
comment|// expected
name|createRelLockUpdate
argument_list|()
expr_stmt|;
name|object
operator|.
name|setName
argument_list|(
literal|"third update"
argument_list|)
expr_stmt|;
try|try
block|{
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Optimistic lock failure expected."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OptimisticLockException
name|ex
parameter_list|)
block|{
comment|// optimistic lock failure expected...
block|}
block|}
specifier|public
name|void
name|testFailRetrieveRow
parameter_list|()
throws|throws
name|Exception
block|{
name|createSimpleLockingDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|object
operator|.
name|setDescription
argument_list|(
literal|"first update"
argument_list|)
expr_stmt|;
comment|// change row underneath, change description and save... optimistic lock failure
comment|// expected
name|createSimpleLockUpdate
argument_list|()
expr_stmt|;
try|try
block|{
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Optimistic lock failure expected."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OptimisticLockException
name|ex
parameter_list|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|freshFailedRow
init|=
name|ex
operator|.
name|getFreshSnapshot
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|freshFailedRow
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"LockTest1Updated"
argument_list|,
name|freshFailedRow
operator|.
name|get
argument_list|(
literal|"NAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testFailRetrieveDeletedRow
parameter_list|()
throws|throws
name|Exception
block|{
name|createSimpleLockingDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|allObjects
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|SimpleLockingTestEntity
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|allObjects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SimpleLockingTestEntity
name|object
init|=
operator|(
name|SimpleLockingTestEntity
operator|)
name|allObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|object
operator|.
name|setDescription
argument_list|(
literal|"first update"
argument_list|)
expr_stmt|;
comment|// delete row underneath, change description and save... optimistic lock failure
comment|// expected
name|createSimpleLockDelete
argument_list|()
expr_stmt|;
try|try
block|{
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Optimistic lock failure expected."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OptimisticLockException
name|ex
parameter_list|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|freshFailedRow
init|=
name|ex
operator|.
name|getFreshSnapshot
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|""
operator|+
name|freshFailedRow
argument_list|,
name|freshFailedRow
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

