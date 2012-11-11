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
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|map
operator|.
name|DataMap
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
name|resource
operator|.
name|URLResource
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
name|cayenne
operator|.
name|util
operator|.
name|Util
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
name|util
operator|.
name|XMLEncoder
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
specifier|public
name|void
name|testSaveLoaded
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
name|String
name|packagePath
init|=
name|getClass
argument_list|()
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
name|URL
name|packageUrl
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|packagePath
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|packageUrl
argument_list|)
expr_stmt|;
name|URL
name|outUrl
init|=
operator|new
name|URL
argument_list|(
name|packageUrl
argument_list|,
literal|"dbimport/testSaveLoaded1.map.xml"
argument_list|)
decl_stmt|;
name|File
name|out
init|=
operator|new
name|File
argument_list|(
name|outUrl
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|out
operator|.
name|delete
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|out
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|(
literal|"testSaveLoaded1"
argument_list|)
decl_stmt|;
name|map
operator|.
name|setConfigurationSource
argument_list|(
operator|new
name|URLResource
argument_list|(
name|outUrl
argument_list|)
argument_list|)
expr_stmt|;
name|action
operator|.
name|saveLoaded
argument_list|(
name|map
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|contents
init|=
name|Util
operator|.
name|stringFromFile
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Has no project version saved"
argument_list|,
name|contents
operator|.
name|contains
argument_list|(
literal|"project-version=\""
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateDataMap_New
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
name|URL
name|outUrl
init|=
name|dataMapUrl
argument_list|(
literal|"testCreateDataMap1.map.xml"
argument_list|)
decl_stmt|;
name|File
name|out
init|=
operator|new
name|File
argument_list|(
name|outUrl
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|out
operator|.
name|delete
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|out
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|DbImportParameters
name|parameters
init|=
operator|new
name|DbImportParameters
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|setDataMapFile
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|DataMap
name|dataMap
init|=
name|action
operator|.
name|createDataMap
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"testCreateDataMap1"
argument_list|,
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|outUrl
argument_list|,
name|dataMap
operator|.
name|getConfigurationSource
argument_list|()
operator|.
name|getURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateDataMap_Existing
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|outUrl
init|=
name|dataMapUrl
argument_list|(
literal|"testCreateDataMap2.map.xml"
argument_list|)
decl_stmt|;
name|File
name|out
init|=
operator|new
name|File
argument_list|(
name|outUrl
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|out
operator|.
name|delete
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|out
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|DataMap
name|tempMap
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|tempMap
operator|.
name|addDbEntity
argument_list|(
operator|new
name|DbEntity
argument_list|(
literal|"X"
argument_list|)
argument_list|)
expr_stmt|;
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|tempMap
operator|.
name|encodeAsXML
argument_list|(
operator|new
name|XMLEncoder
argument_list|(
name|writer
argument_list|)
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
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
name|setDataMapFile
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|DataMap
name|dataMap
init|=
name|action
operator|.
name|createDataMap
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"testCreateDataMap2"
argument_list|,
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|outUrl
argument_list|,
name|dataMap
operator|.
name|getConfigurationSource
argument_list|()
operator|.
name|getURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|URL
name|dataMapUrl
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|String
name|packagePath
init|=
name|getClass
argument_list|()
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
name|URL
name|packageUrl
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|packagePath
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|packageUrl
argument_list|)
expr_stmt|;
return|return
operator|new
name|URL
argument_list|(
name|packageUrl
argument_list|,
literal|"dbimport/"
operator|+
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit
