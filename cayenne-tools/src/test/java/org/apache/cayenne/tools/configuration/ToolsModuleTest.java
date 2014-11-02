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
name|tools
operator|.
name|configuration
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
name|DataNodeDescriptor
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
name|DataSourceFactory
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
name|DbAdapterFactory
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
name|DefaultDbAdapterFactory
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
name|AutoAdapter
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
name|DIBootstrap
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
name|di
operator|.
name|spi
operator|.
name|DefaultAdhocObjectFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
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
name|javax
operator|.
name|sql
operator|.
name|DataSource
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

begin_class
specifier|public
class|class
name|ToolsModuleTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testModuleContents
parameter_list|()
block|{
name|Log
name|log
init|=
name|mock
argument_list|(
name|Log
operator|.
name|class
argument_list|)
decl_stmt|;
name|Injector
name|i
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
operator|new
name|ToolsModule
argument_list|(
name|log
argument_list|)
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|log
argument_list|,
name|i
operator|.
name|getInstance
argument_list|(
name|Log
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|i
operator|.
name|getInstance
argument_list|(
name|DataSourceFactory
operator|.
name|class
argument_list|)
operator|instanceof
name|DriverDataSourceFactory
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|i
operator|.
name|getInstance
argument_list|(
name|AdhocObjectFactory
operator|.
name|class
argument_list|)
operator|instanceof
name|DefaultAdhocObjectFactory
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|i
operator|.
name|getInstance
argument_list|(
name|DbAdapterFactory
operator|.
name|class
argument_list|)
operator|instanceof
name|DefaultDbAdapterFactory
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDbApdater
parameter_list|()
throws|throws
name|Exception
block|{
name|Log
name|log
init|=
name|mock
argument_list|(
name|Log
operator|.
name|class
argument_list|)
decl_stmt|;
name|Injector
name|i
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
operator|new
name|ToolsModule
argument_list|(
name|log
argument_list|)
argument_list|)
decl_stmt|;
name|DbAdapterFactory
name|factory
init|=
name|i
operator|.
name|getInstance
argument_list|(
name|DbAdapterFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|DataNodeDescriptor
name|nodeDescriptor
init|=
name|mock
argument_list|(
name|DataNodeDescriptor
operator|.
name|class
argument_list|)
decl_stmt|;
name|DataSource
name|dataSource
init|=
name|mock
argument_list|(
name|DataSource
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|factory
operator|.
name|createAdapter
argument_list|(
name|nodeDescriptor
argument_list|,
name|dataSource
argument_list|)
operator|instanceof
name|AutoAdapter
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

