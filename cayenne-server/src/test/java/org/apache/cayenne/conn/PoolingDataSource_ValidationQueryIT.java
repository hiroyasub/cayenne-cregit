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
name|conn
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
name|PoolingDataSource_ValidationQueryIT
extends|extends
name|BasePoolingDataSourceIT
block|{
annotation|@
name|Override
specifier|protected
name|PoolingDataSourceParameters
name|createParameters
parameter_list|()
block|{
name|PoolingDataSourceParameters
name|params
init|=
name|super
operator|.
name|createParameters
argument_list|()
decl_stmt|;
name|params
operator|.
name|setValidationQuery
argument_list|(
literal|"SELECT count(1) FROM ARTIST"
argument_list|)
expr_stmt|;
return|return
name|params
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConnection_ValidationQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|dataSource
operator|.
name|getCurrentlyInUse
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|dataSource
operator|.
name|getCurrentlyUnused
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: we are not testing much here... we really need to mock
comment|// validation query execution somehow and verify that it is taken into
comment|// account
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
literal|1
argument_list|,
name|dataSource
operator|.
name|getCurrentlyInUse
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dataSource
operator|.
name|getCurrentlyUnused
argument_list|()
argument_list|)
expr_stmt|;
name|c1
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

