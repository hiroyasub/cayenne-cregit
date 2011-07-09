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
name|configuration
operator|.
name|server
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InitialContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NameNotFoundException
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
name|JNDIDataSourceFactory
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
name|unit
operator|.
name|JNDISetup
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
name|com
operator|.
name|mockrunner
operator|.
name|mock
operator|.
name|jdbc
operator|.
name|MockDataSource
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|JNDIDataSourceFactoryTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|Injector
name|injector
decl_stmt|;
specifier|public
name|void
name|testGetDataSource_NameBound
parameter_list|()
throws|throws
name|Exception
block|{
name|DataNodeDescriptor
name|descriptor
init|=
operator|new
name|DataNodeDescriptor
argument_list|()
decl_stmt|;
name|descriptor
operator|.
name|setParameters
argument_list|(
literal|"jdbc/TestDS"
argument_list|)
expr_stmt|;
name|JNDISetup
operator|.
name|doSetup
argument_list|()
expr_stmt|;
name|MockDataSource
name|dataSource
init|=
operator|new
name|MockDataSource
argument_list|()
decl_stmt|;
name|InitialContext
name|context
init|=
operator|new
name|InitialContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|bind
argument_list|(
name|descriptor
operator|.
name|getParameters
argument_list|()
argument_list|,
name|dataSource
argument_list|)
expr_stmt|;
try|try
block|{
name|JNDIDataSourceFactory
name|factory
init|=
operator|new
name|JNDIDataSourceFactory
argument_list|()
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|dataSource
argument_list|,
name|factory
operator|.
name|getDataSource
argument_list|(
name|descriptor
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// since the context is shared, must clear it after the test
name|context
operator|.
name|unbind
argument_list|(
name|descriptor
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testGetDataSource_NameBoundWithPrefix
parameter_list|()
throws|throws
name|Exception
block|{
name|DataNodeDescriptor
name|descriptor
init|=
operator|new
name|DataNodeDescriptor
argument_list|()
decl_stmt|;
name|descriptor
operator|.
name|setParameters
argument_list|(
literal|"jdbc/TestDS"
argument_list|)
expr_stmt|;
name|JNDISetup
operator|.
name|doSetup
argument_list|()
expr_stmt|;
name|MockDataSource
name|dataSource
init|=
operator|new
name|MockDataSource
argument_list|()
decl_stmt|;
name|InitialContext
name|context
init|=
operator|new
name|InitialContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|bind
argument_list|(
literal|"java:comp/env/"
operator|+
name|descriptor
operator|.
name|getParameters
argument_list|()
argument_list|,
name|dataSource
argument_list|)
expr_stmt|;
try|try
block|{
name|JNDIDataSourceFactory
name|factory
init|=
operator|new
name|JNDIDataSourceFactory
argument_list|()
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|factory
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|dataSource
argument_list|,
name|factory
operator|.
name|getDataSource
argument_list|(
name|descriptor
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// since the context is shared, must clear it after the test
name|context
operator|.
name|unbind
argument_list|(
literal|"java:comp/env/"
operator|+
name|descriptor
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testGetDataSource_NameNotBound
parameter_list|()
throws|throws
name|Exception
block|{
name|DataNodeDescriptor
name|descriptor
init|=
operator|new
name|DataNodeDescriptor
argument_list|()
decl_stmt|;
name|descriptor
operator|.
name|setParameters
argument_list|(
literal|"jdbc/TestDS"
argument_list|)
expr_stmt|;
name|JNDISetup
operator|.
name|doSetup
argument_list|()
expr_stmt|;
name|JNDIDataSourceFactory
name|factory
init|=
operator|new
name|JNDIDataSourceFactory
argument_list|()
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|factory
argument_list|)
expr_stmt|;
try|try
block|{
name|factory
operator|.
name|getDataSource
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Didn't throw on unbound name"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NameNotFoundException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
block|}
end_class

end_unit

