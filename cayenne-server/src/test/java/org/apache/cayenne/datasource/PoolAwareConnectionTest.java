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
name|assertSame
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|anyString
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
name|PreparedStatement
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
name|PoolAwareConnectionTest
block|{
specifier|private
name|UnmanagedPoolingDataSource
name|parentMock
decl_stmt|;
specifier|private
name|Connection
name|connectionMock
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
name|connectionMock
operator|=
name|mock
argument_list|(
name|Connection
operator|.
name|class
argument_list|)
expr_stmt|;
name|parentMock
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
name|parentMock
operator|.
name|createUnwrapped
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|connectionMock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRecover
parameter_list|()
throws|throws
name|SQLException
block|{
name|PoolAwareConnection
name|paConnection
init|=
operator|new
name|PoolAwareConnection
argument_list|(
name|parentMock
argument_list|,
name|connectionMock
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SQLException
name|e
init|=
name|mock
argument_list|(
name|SQLException
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|parentMock
argument_list|,
name|times
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|createUnwrapped
argument_list|()
expr_stmt|;
name|paConnection
operator|.
name|recover
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|parentMock
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|createUnwrapped
argument_list|()
expr_stmt|;
name|assertSame
argument_list|(
name|connectionMock
argument_list|,
name|paConnection
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
name|testPrepareStatement
parameter_list|()
throws|throws
name|SQLException
block|{
name|PreparedStatement
name|firstTry
init|=
name|mock
argument_list|(
name|PreparedStatement
operator|.
name|class
argument_list|)
decl_stmt|;
name|PreparedStatement
name|secondTry
init|=
name|mock
argument_list|(
name|PreparedStatement
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|connectionMock
operator|.
name|prepareStatement
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|firstTry
argument_list|,
name|secondTry
argument_list|)
expr_stmt|;
name|PoolAwareConnection
name|paConnection
init|=
operator|new
name|PoolAwareConnection
argument_list|(
name|parentMock
argument_list|,
name|connectionMock
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|PreparedStatement
name|st
init|=
name|paConnection
operator|.
name|prepareStatement
argument_list|(
literal|"SELECT 1"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|firstTry
argument_list|,
name|st
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrepareStatement_Recover
parameter_list|()
throws|throws
name|SQLException
block|{
name|PreparedStatement
name|secondTry
init|=
name|mock
argument_list|(
name|PreparedStatement
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|connectionMock
operator|.
name|prepareStatement
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenThrow
argument_list|(
operator|new
name|SQLException
argument_list|(
literal|"E1"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|secondTry
argument_list|)
expr_stmt|;
name|PoolAwareConnection
name|paConnection
init|=
operator|new
name|PoolAwareConnection
argument_list|(
name|parentMock
argument_list|,
name|connectionMock
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|PreparedStatement
name|st
init|=
name|paConnection
operator|.
name|prepareStatement
argument_list|(
literal|"SELECT 1"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|secondTry
argument_list|,
name|st
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrepareStatement_Recover_Impossible
parameter_list|()
throws|throws
name|SQLException
block|{
name|SQLException
name|original
init|=
operator|new
name|SQLException
argument_list|(
literal|"E1"
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|connectionMock
operator|.
name|prepareStatement
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenThrow
argument_list|(
name|original
argument_list|)
expr_stmt|;
name|PoolAwareConnection
name|paConnection
init|=
operator|new
name|PoolAwareConnection
argument_list|(
name|parentMock
argument_list|,
name|connectionMock
argument_list|,
literal|null
argument_list|)
decl_stmt|;
try|try
block|{
name|paConnection
operator|.
name|prepareStatement
argument_list|(
literal|"SELECT 1"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|assertSame
argument_list|(
name|original
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

