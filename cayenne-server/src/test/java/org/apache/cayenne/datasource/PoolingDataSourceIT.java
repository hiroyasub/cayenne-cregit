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
name|datasource
package|;
end_package

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
name|assertTrue
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
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|PoolingDataSourceIT
extends|extends
name|BasePoolingDataSourceIT
block|{
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testGetConnectionWithUserName
parameter_list|()
throws|throws
name|Exception
block|{
name|dataSource
operator|.
name|getConnection
argument_list|(
literal|"user"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConnectionAutoCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|dataSource
operator|.
name|getMaxConnections
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Connection
argument_list|>
name|connections
init|=
operator|new
name|ArrayList
argument_list|<
name|Connection
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|dataSource
operator|.
name|getMaxConnections
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Connection
name|c
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to reset connection state"
argument_list|,
name|c
operator|.
name|getAutoCommit
argument_list|()
argument_list|)
expr_stmt|;
name|connections
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Connection
name|c
range|:
name|connections
control|)
block|{
name|c
operator|.
name|setAutoCommit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|dataSource
operator|.
name|getMaxConnections
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Connection
name|c
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
decl_stmt|;
comment|// presumably this pass through the pool should return existing
comment|// connections
name|assertTrue
argument_list|(
name|connections
operator|.
name|contains
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed to reset connection state for reused connection"
argument_list|,
name|c
operator|.
name|getAutoCommit
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
for|for
control|(
name|Connection
name|c
range|:
name|connections
control|)
block|{
try|try
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
block|}
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|dataSource
operator|.
name|poolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|dataSource
operator|.
name|availableSize
argument_list|()
argument_list|)
expr_stmt|;
name|Connection
name|c1
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|dataSource
operator|.
name|poolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dataSource
operator|.
name|availableSize
argument_list|()
argument_list|)
expr_stmt|;
name|Connection
name|c2
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|dataSource
operator|.
name|poolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|dataSource
operator|.
name|availableSize
argument_list|()
argument_list|)
expr_stmt|;
name|Connection
name|c3
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|dataSource
operator|.
name|poolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|dataSource
operator|.
name|availableSize
argument_list|()
argument_list|)
expr_stmt|;
name|c1
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|dataSource
operator|.
name|poolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dataSource
operator|.
name|availableSize
argument_list|()
argument_list|)
expr_stmt|;
name|c2
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|dataSource
operator|.
name|poolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|dataSource
operator|.
name|availableSize
argument_list|()
argument_list|)
expr_stmt|;
name|c3
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|dataSource
operator|.
name|poolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|dataSource
operator|.
name|availableSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConnection_BeforeScopeEnd
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|dataSource
operator|.
name|poolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|dataSource
operator|.
name|availableSize
argument_list|()
argument_list|)
expr_stmt|;
name|dataSource
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|dataSource
operator|.
name|poolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|dataSource
operator|.
name|availableSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConnection_Concurrent
parameter_list|()
block|{
name|PoolTask
index|[]
name|tasks
init|=
operator|new
name|PoolTask
index|[
literal|2
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|tasks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|tasks
index|[
name|i
index|]
operator|=
operator|new
name|PoolTask
argument_list|()
expr_stmt|;
block|}
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|tasks
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
literal|100
condition|;
name|j
operator|++
control|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|tasks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|executor
operator|.
name|submit
argument_list|(
name|tasks
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
comment|// check for completion or deadlock
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
try|try
block|{
comment|// normally this completes in less than 2 seconds. If it takes 30
comment|// then it failed.
name|boolean
name|didFinish
init|=
name|executor
operator|.
name|awaitTermination
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|didFinish
condition|)
block|{
name|fail
argument_list|(
literal|"Connection pool either deadlocked or contended over the lock too long."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|tasks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|tasks
index|[
name|i
index|]
operator|.
name|i
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
class|class
name|PoolTask
implements|implements
name|Runnable
block|{
name|AtomicInteger
name|i
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|i
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
try|try
block|{
name|Connection
name|c
init|=
name|dataSource
operator|.
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|Statement
name|st
init|=
name|c
operator|.
name|createStatement
argument_list|()
decl_stmt|;
try|try
block|{
name|ResultSet
name|rs
init|=
name|st
operator|.
name|executeQuery
argument_list|(
literal|"SELECT ARTIST_ID FROM ARTIST"
argument_list|)
decl_stmt|;
try|try
block|{
name|rs
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|st
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

