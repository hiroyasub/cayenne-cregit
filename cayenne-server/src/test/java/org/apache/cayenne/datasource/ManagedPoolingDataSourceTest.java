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
name|assertFalse
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
name|assertNotNull
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
name|assertSame
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
name|mockito
operator|.
name|Mockito
operator|.
name|mock
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
name|times
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
name|verify
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
name|when
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
name|SQLException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
specifier|public
class|class
name|ManagedPoolingDataSourceTest
block|{
specifier|private
name|Connection
index|[]
name|mockConnections
decl_stmt|;
specifier|private
name|UnmanagedPoolingDataSource
name|mockPoolingDataSource
decl_stmt|;
specifier|private
name|ManagedPoolingDataSource
name|dataSource
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
throws|throws
name|SQLException
block|{
name|this
operator|.
name|mockConnections
operator|=
operator|new
name|Connection
index|[
literal|4
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|mockConnections
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|mockConnections
index|[
name|i
index|]
operator|=
name|mock
argument_list|(
name|Connection
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|mockPoolingDataSource
operator|=
name|mock
argument_list|(
name|UnmanagedPoolingDataSource
operator|.
name|class
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|mockPoolingDataSource
operator|.
name|getConnection
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mockConnections
index|[
literal|0
index|]
argument_list|,
name|mockConnections
index|[
literal|1
index|]
argument_list|,
name|mockConnections
index|[
literal|2
index|]
argument_list|,
name|mockConnections
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataSource
operator|=
operator|new
name|ManagedPoolingDataSource
argument_list|(
name|mockPoolingDataSource
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|after
parameter_list|()
block|{
name|dataSource
operator|.
name|beforeScopeEnd
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConnection
parameter_list|()
throws|throws
name|SQLException
block|{
name|assertSame
argument_list|(
name|mockConnections
index|[
literal|0
index|]
argument_list|,
name|dataSource
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|mockConnections
index|[
literal|1
index|]
argument_list|,
name|dataSource
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|mockConnections
index|[
literal|2
index|]
argument_list|,
name|dataSource
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|mockConnections
index|[
literal|3
index|]
argument_list|,
name|dataSource
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClose
parameter_list|()
throws|throws
name|SQLException
throws|,
name|InterruptedException
block|{
name|assertNotNull
argument_list|(
name|dataSource
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
comment|// state before shutdown
name|verify
argument_list|(
name|mockPoolingDataSource
argument_list|,
name|times
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|dataSource
operator|.
name|getDataSourceManager
argument_list|()
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataSource
operator|.
name|getDataSourceManager
argument_list|()
operator|.
name|isAlive
argument_list|()
argument_list|)
expr_stmt|;
name|dataSource
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// state after shutdown
name|verify
argument_list|(
name|mockPoolingDataSource
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|dataSource
operator|.
name|getDataSourceManager
argument_list|()
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
comment|// give the thread some time to process interrupt and die
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|dataSource
operator|.
name|getDataSourceManager
argument_list|()
operator|.
name|isAlive
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|dataSource
operator|.
name|getConnection
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
comment|// expected , DataSource should not give out connections any longer
block|}
block|}
block|}
end_class

end_unit

