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
name|dbimport
package|;
end_package

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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|DbLoader
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
name|DbLoaderDelegate
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
name|tools
operator|.
name|configuration
operator|.
name|ToolsModule
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

begin_class
specifier|public
class|class
name|DbImportActionTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testCreateLoader
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
argument_list|,
operator|new
name|DbImportModule
argument_list|()
argument_list|)
decl_stmt|;
name|DbImportAction
name|action
init|=
name|i
operator|.
name|getInstance
argument_list|(
name|DbImportAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|DbImportParameters
name|parameters
init|=
operator|new
name|DbImportParameters
argument_list|()
decl_stmt|;
name|Connection
name|connection
init|=
name|mock
argument_list|(
name|Connection
operator|.
name|class
argument_list|)
decl_stmt|;
name|DbLoader
name|loader
init|=
name|action
operator|.
name|createLoader
argument_list|(
name|parameters
argument_list|,
name|mock
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|)
argument_list|,
name|connection
argument_list|,
name|mock
argument_list|(
name|DbLoaderDelegate
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|loader
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|connection
argument_list|,
name|loader
operator|.
name|getConnection
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|loader
operator|.
name|includeTableName
argument_list|(
literal|"dummy"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateLoader_IncludeExclude
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
argument_list|,
operator|new
name|DbImportModule
argument_list|()
argument_list|)
decl_stmt|;
name|DbImportAction
name|action
init|=
name|i
operator|.
name|getInstance
argument_list|(
name|DbImportAction
operator|.
name|class
argument_list|)
decl_stmt|;
name|DbImportParameters
name|parameters
init|=
operator|new
name|DbImportParameters
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|setIncludeTables
argument_list|(
literal|"a,b,c*"
argument_list|)
expr_stmt|;
name|DbLoader
name|loader1
init|=
name|action
operator|.
name|createLoader
argument_list|(
name|parameters
argument_list|,
name|mock
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|)
argument_list|,
name|mock
argument_list|(
name|Connection
operator|.
name|class
argument_list|)
argument_list|,
name|mock
argument_list|(
name|DbLoaderDelegate
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|loader1
operator|.
name|includeTableName
argument_list|(
literal|"dummy"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|loader1
operator|.
name|includeTableName
argument_list|(
literal|"ab"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|loader1
operator|.
name|includeTableName
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|loader1
operator|.
name|includeTableName
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|loader1
operator|.
name|includeTableName
argument_list|(
literal|"cd"
argument_list|)
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setExcludeTables
argument_list|(
literal|"cd"
argument_list|)
expr_stmt|;
name|DbLoader
name|loader2
init|=
name|action
operator|.
name|createLoader
argument_list|(
name|parameters
argument_list|,
name|mock
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|)
argument_list|,
name|mock
argument_list|(
name|Connection
operator|.
name|class
argument_list|)
argument_list|,
name|mock
argument_list|(
name|DbLoaderDelegate
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|loader2
operator|.
name|includeTableName
argument_list|(
literal|"dummy"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|loader2
operator|.
name|includeTableName
argument_list|(
literal|"ab"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|loader2
operator|.
name|includeTableName
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|loader2
operator|.
name|includeTableName
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|loader2
operator|.
name|includeTableName
argument_list|(
literal|"cd"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|loader2
operator|.
name|includeTableName
argument_list|(
literal|"cx"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

