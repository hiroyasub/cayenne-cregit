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
name|project2
package|;
end_package

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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|ConfigurationNameMapper
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
name|ConfigurationTree
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
name|DataChannelDescriptor
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
name|DefaultConfigurationNameMapper
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
name|Binder
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
name|Module
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
name|project2
operator|.
name|unit
operator|.
name|Project2Case
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
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_class
specifier|public
class|class
name|FileProjectSaverTest
extends|extends
name|Project2Case
block|{
specifier|public
name|void
name|testSaveAs_Sorted
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|testFolder
init|=
name|setupTestDirectory
argument_list|(
literal|"testSaveAs_Sorted"
argument_list|)
decl_stmt|;
name|Module
name|testModule
init|=
operator|new
name|Module
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|ConfigurationNameMapper
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultConfigurationNameMapper
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|FileProjectSaver
name|saver
init|=
operator|new
name|FileProjectSaver
argument_list|()
decl_stmt|;
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
name|testModule
argument_list|)
decl_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|saver
argument_list|)
expr_stmt|;
name|DataChannelDescriptor
name|rootNode
init|=
operator|new
name|DataChannelDescriptor
argument_list|()
decl_stmt|;
name|rootNode
operator|.
name|setName
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
comment|// add maps and nodes in reverse alpha order. Check that they are saved in alpha
comment|// order
name|rootNode
operator|.
name|getDataMaps
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|DataMap
argument_list|(
literal|"C"
argument_list|)
argument_list|)
expr_stmt|;
name|rootNode
operator|.
name|getDataMaps
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|DataMap
argument_list|(
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
name|rootNode
operator|.
name|getDataMaps
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|DataMap
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|DataNodeDescriptor
index|[]
name|nodes
init|=
operator|new
name|DataNodeDescriptor
index|[
literal|3
index|]
decl_stmt|;
name|nodes
index|[
literal|0
index|]
operator|=
operator|new
name|DataNodeDescriptor
argument_list|(
literal|"Z"
argument_list|)
expr_stmt|;
name|nodes
index|[
literal|1
index|]
operator|=
operator|new
name|DataNodeDescriptor
argument_list|(
literal|"Y"
argument_list|)
expr_stmt|;
name|nodes
index|[
literal|2
index|]
operator|=
operator|new
name|DataNodeDescriptor
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|nodes
index|[
literal|0
index|]
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|add
argument_list|(
literal|"C"
argument_list|)
expr_stmt|;
name|nodes
index|[
literal|0
index|]
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|add
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|nodes
index|[
literal|0
index|]
operator|.
name|getDataMapNames
argument_list|()
operator|.
name|add
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|rootNode
operator|.
name|getNodeDescriptors
argument_list|()
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|nodes
argument_list|)
argument_list|)
expr_stmt|;
name|Project
name|project
init|=
operator|new
name|Project
argument_list|(
operator|new
name|ConfigurationTree
argument_list|<
name|DataChannelDescriptor
argument_list|>
argument_list|(
name|rootNode
argument_list|)
argument_list|)
decl_stmt|;
name|saver
operator|.
name|saveAs
argument_list|(
name|project
argument_list|,
operator|new
name|URLResource
argument_list|(
name|testFolder
operator|.
name|toURL
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|testFolder
argument_list|,
literal|"cayenne-test.xml"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|target
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertSaveAs_Sorted
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertSaveAs_Sorted
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|Exception
block|{
name|Document
name|document
init|=
name|toDOMTree
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|XPath
name|xpath
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newXPath
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"/domain/@name"
argument_list|,
name|document
argument_list|)
argument_list|)
expr_stmt|;
name|NodeList
name|maps
init|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"/domain/map"
argument_list|,
name|document
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|maps
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"@name"
argument_list|,
name|maps
operator|.
name|item
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"@name"
argument_list|,
name|maps
operator|.
name|item
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"C"
argument_list|,
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"@name"
argument_list|,
name|maps
operator|.
name|item
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|NodeList
name|nodes
init|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"/domain/node"
argument_list|,
name|document
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|nodes
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"X"
argument_list|,
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"@name"
argument_list|,
name|nodes
operator|.
name|item
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Y"
argument_list|,
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"@name"
argument_list|,
name|nodes
operator|.
name|item
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Z"
argument_list|,
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"@name"
argument_list|,
name|nodes
operator|.
name|item
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|NodeList
name|mapRefs
init|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"map-ref"
argument_list|,
name|nodes
operator|.
name|item
argument_list|(
literal|2
argument_list|)
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|mapRefs
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"@name"
argument_list|,
name|mapRefs
operator|.
name|item
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"@name"
argument_list|,
name|mapRefs
operator|.
name|item
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"C"
argument_list|,
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"@name"
argument_list|,
name|mapRefs
operator|.
name|item
argument_list|(
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

