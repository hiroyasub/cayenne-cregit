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
import|import
name|java
operator|.
name|sql
operator|.
name|Driver
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
name|CayenneRuntimeException
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
name|conn
operator|.
name|DataSourceInfo
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
name|PoolingDataSource_FailingValidationQueryIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataSourceInfo
name|dataSourceInfo
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|AdhocObjectFactory
name|objectFactory
decl_stmt|;
specifier|protected
name|PoolingDataSourceParameters
name|createParameters
parameter_list|()
block|{
name|PoolingDataSourceParameters
name|poolParameters
init|=
operator|new
name|PoolingDataSourceParameters
argument_list|()
decl_stmt|;
name|poolParameters
operator|.
name|setMinConnections
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|poolParameters
operator|.
name|setMaxConnections
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|poolParameters
operator|.
name|setMaxQueueWaitTime
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|poolParameters
operator|.
name|setValidationQuery
argument_list|(
literal|"SELECT count(1) FROM NO_SUCH_TABLE"
argument_list|)
expr_stmt|;
return|return
name|poolParameters
return|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CayenneRuntimeException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testConstructor
parameter_list|()
throws|throws
name|Exception
block|{
name|Driver
name|driver
init|=
name|objectFactory
operator|.
name|newInstance
argument_list|(
name|Driver
operator|.
name|class
argument_list|,
name|dataSourceInfo
operator|.
name|getJdbcDriver
argument_list|()
argument_list|)
decl_stmt|;
name|DriverDataSource
name|nonPooling
init|=
operator|new
name|DriverDataSource
argument_list|(
name|driver
argument_list|,
name|dataSourceInfo
operator|.
name|getDataSourceUrl
argument_list|()
argument_list|,
name|dataSourceInfo
operator|.
name|getUserName
argument_list|()
argument_list|,
name|dataSourceInfo
operator|.
name|getPassword
argument_list|()
argument_list|)
decl_stmt|;
name|PoolingDataSourceParameters
name|poolParameters
init|=
name|createParameters
argument_list|()
decl_stmt|;
operator|new
name|PoolingDataSource
argument_list|(
name|nonPooling
argument_list|,
name|poolParameters
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

