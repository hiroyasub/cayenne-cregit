begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|merge
operator|.
name|builders
operator|.
name|ObjectMother
operator|.
name|dbAttr
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|merge
operator|.
name|builders
operator|.
name|ObjectMother
operator|.
name|dbEntity
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|merge
operator|.
name|builders
operator|.
name|ObjectMother
operator|.
name|objAttr
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|merge
operator|.
name|builders
operator|.
name|ObjectMother
operator|.
name|objEntity
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
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|any
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
name|doNothing
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
name|doThrow
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
name|never
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
name|stub
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
name|io
operator|.
name|File
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
name|util
operator|.
name|LinkedList
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
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|access
operator|.
name|loader
operator|.
name|DbLoaderConfiguration
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
name|map
operator|.
name|MapLoader
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
name|merge
operator|.
name|AddColumnToDb
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
name|merge
operator|.
name|AddRelationshipToDb
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
name|merge
operator|.
name|CreateTableToDb
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
name|merge
operator|.
name|CreateTableToModel
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
name|merge
operator|.
name|DefaultModelMergeDelegate
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
name|merge
operator|.
name|MergerFactory
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
name|merge
operator|.
name|MergerToken
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
name|merge
operator|.
name|builders
operator|.
name|DataMapBuilder
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
name|project
operator|.
name|FileProjectSaver
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
name|project
operator|.
name|Project
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
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_class
specifier|public
class|class
name|DbImportActionTest
block|{
specifier|public
specifier|static
specifier|final
name|File
name|FILE_STUB
init|=
operator|new
name|File
argument_list|(
literal|""
argument_list|)
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|7513956717393115824L
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|exists
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|canRead
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testNewDataMapImport
parameter_list|()
throws|throws
name|Exception
block|{
name|DbLoader
name|dbLoader
init|=
operator|new
name|DbLoader
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|load
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|DbLoaderConfiguration
name|config
parameter_list|)
throws|throws
name|SQLException
block|{
operator|new
name|DataMapBuilder
argument_list|(
name|dataMap
argument_list|)
operator|.
name|withDbEntities
argument_list|(
literal|2
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
index|[]
name|getDefaultTableTypes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|DbImportConfiguration
name|params
init|=
name|mock
argument_list|(
name|DbImportConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|params
operator|.
name|createLoader
argument_list|(
name|any
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|Connection
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|DbLoaderDelegate
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dbLoader
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|params
operator|.
name|createDataMap
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|DataMap
argument_list|(
literal|"testImport"
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|params
operator|.
name|createMergeDelegate
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|DefaultModelMergeDelegate
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|params
operator|.
name|getDbLoaderConfig
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|DbLoaderConfiguration
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|DataMap
name|DATA_MAP
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|params
operator|.
name|initializeDataMap
argument_list|(
name|any
argument_list|(
name|DataMap
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|DATA_MAP
argument_list|)
expr_stmt|;
specifier|final
name|boolean
index|[]
name|haveWeTriedToSave
init|=
block|{
literal|false
block|}
decl_stmt|;
name|DbImportAction
name|action
init|=
name|buildDbImportAction
argument_list|(
operator|new
name|FileProjectSaver
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|save
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
name|haveWeTriedToSave
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
comment|// Validation phase
name|assertEquals
argument_list|(
name|DATA_MAP
argument_list|,
name|project
operator|.
name|getRootNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|action
operator|.
name|execute
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"We should try to save."
argument_list|,
name|haveWeTriedToSave
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImportWithFieldChanged
parameter_list|()
throws|throws
name|Exception
block|{
name|DbLoader
name|dbLoader
init|=
operator|new
name|DbLoader
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|load
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|DbLoaderConfiguration
name|config
parameter_list|)
throws|throws
name|SQLException
block|{
operator|new
name|DataMapBuilder
argument_list|(
name|dataMap
argument_list|)
operator|.
name|with
argument_list|(
name|dbEntity
argument_list|(
literal|"ARTGROUP"
argument_list|)
operator|.
name|attributes
argument_list|(
name|dbAttr
argument_list|(
literal|"GROUP_ID"
argument_list|)
operator|.
name|typeInt
argument_list|()
operator|.
name|primaryKey
argument_list|()
argument_list|,
name|dbAttr
argument_list|(
literal|"NAME"
argument_list|)
operator|.
name|typeVarchar
argument_list|(
literal|100
argument_list|)
operator|.
name|mandatory
argument_list|()
argument_list|,
name|dbAttr
argument_list|(
literal|"NAME_01"
argument_list|)
operator|.
name|typeVarchar
argument_list|(
literal|100
argument_list|)
operator|.
name|mandatory
argument_list|()
argument_list|,
name|dbAttr
argument_list|(
literal|"PARENT_GROUP_ID"
argument_list|)
operator|.
name|typeInt
argument_list|()
argument_list|)
argument_list|)
operator|.
name|with
argument_list|(
name|objEntity
argument_list|(
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"ArtGroup"
argument_list|,
literal|"ARTGROUP"
argument_list|)
operator|.
name|attributes
argument_list|(
name|objAttr
argument_list|(
literal|"name"
argument_list|)
operator|.
name|type
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|dbPath
argument_list|(
literal|"NAME"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
index|[]
name|getDefaultTableTypes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|DbImportConfiguration
name|params
init|=
name|mock
argument_list|(
name|DbImportConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|params
operator|.
name|createLoader
argument_list|(
name|any
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|Connection
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|DbLoaderDelegate
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dbLoader
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|params
operator|.
name|createDataMap
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|DataMap
argument_list|(
literal|"testImport"
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|params
operator|.
name|getDataMapFile
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|FILE_STUB
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|params
operator|.
name|createMergeDelegate
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|DefaultModelMergeDelegate
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|params
operator|.
name|getDbLoaderConfig
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|DbLoaderConfiguration
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|boolean
index|[]
name|haveWeTriedToSave
init|=
block|{
literal|false
block|}
decl_stmt|;
name|DbImportAction
name|action
init|=
name|buildDbImportAction
argument_list|(
operator|new
name|FileProjectSaver
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|save
parameter_list|(
name|Project
name|project
parameter_list|)
block|{
name|haveWeTriedToSave
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
comment|// Validation phase
name|DataMap
name|rootNode
init|=
operator|(
name|DataMap
operator|)
name|project
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rootNode
operator|.
name|getObjEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rootNode
operator|.
name|getDbEntityMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DbEntity
name|entity
init|=
name|rootNode
operator|.
name|getDbEntity
argument_list|(
literal|"ARTGROUP"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|entity
operator|.
name|getAttribute
argument_list|(
literal|"NAME_01"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|,
operator|new
name|MapLoader
argument_list|()
block|{
annotation|@
name|Override
specifier|public
specifier|synchronized
name|DataMap
name|loadDataMap
parameter_list|(
name|InputSource
name|src
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
return|return
operator|new
name|DataMapBuilder
argument_list|()
operator|.
name|with
argument_list|(
name|dbEntity
argument_list|(
literal|"ARTGROUP"
argument_list|)
operator|.
name|attributes
argument_list|(
name|dbAttr
argument_list|(
literal|"GROUP_ID"
argument_list|)
operator|.
name|typeInt
argument_list|()
operator|.
name|primaryKey
argument_list|()
argument_list|,
name|dbAttr
argument_list|(
literal|"NAME"
argument_list|)
operator|.
name|typeVarchar
argument_list|(
literal|100
argument_list|)
operator|.
name|mandatory
argument_list|()
argument_list|,
name|dbAttr
argument_list|(
literal|"PARENT_GROUP_ID"
argument_list|)
operator|.
name|typeInt
argument_list|()
argument_list|)
argument_list|)
operator|.
name|with
argument_list|(
name|objEntity
argument_list|(
literal|"org.apache.cayenne.testdo.testmap"
argument_list|,
literal|"ArtGroup"
argument_list|,
literal|"ARTGROUP"
argument_list|)
operator|.
name|attributes
argument_list|(
name|objAttr
argument_list|(
literal|"name"
argument_list|)
operator|.
name|type
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|dbPath
argument_list|(
literal|"NAME"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|action
operator|.
name|execute
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"We should try to save."
argument_list|,
name|haveWeTriedToSave
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImportWithoutChanges
parameter_list|()
throws|throws
name|Exception
block|{
name|DbLoader
name|dbLoader
init|=
operator|new
name|DbLoader
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|load
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|DbLoaderConfiguration
name|config
parameter_list|)
throws|throws
name|SQLException
block|{
operator|new
name|DataMapBuilder
argument_list|(
name|dataMap
argument_list|)
operator|.
name|with
argument_list|(
name|dbEntity
argument_list|(
literal|"ARTGROUP"
argument_list|)
operator|.
name|attributes
argument_list|(
name|dbAttr
argument_list|(
literal|"NAME"
argument_list|)
operator|.
name|typeVarchar
argument_list|(
literal|100
argument_list|)
operator|.
name|mandatory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
index|[]
name|getDefaultTableTypes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|DbImportConfiguration
name|params
init|=
name|mock
argument_list|(
name|DbImportConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|params
operator|.
name|createLoader
argument_list|(
name|any
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|Connection
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|DbLoaderDelegate
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dbLoader
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|params
operator|.
name|createDataMap
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|DataMap
argument_list|(
literal|"testImport"
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|params
operator|.
name|getDataMapFile
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|FILE_STUB
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|params
operator|.
name|createMergeDelegate
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|DefaultModelMergeDelegate
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|params
operator|.
name|getDbLoaderConfig
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|DbLoaderConfiguration
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
name|when
argument_list|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|log
operator|.
name|isInfoEnabled
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|FileProjectSaver
name|projectSaver
init|=
name|mock
argument_list|(
name|FileProjectSaver
operator|.
name|class
argument_list|)
decl_stmt|;
name|doNothing
argument_list|()
operator|.
name|when
argument_list|(
name|projectSaver
argument_list|)
operator|.
name|save
argument_list|(
name|any
argument_list|(
name|Project
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|MapLoader
name|mapLoader
init|=
name|mock
argument_list|(
name|MapLoader
operator|.
name|class
argument_list|)
decl_stmt|;
name|stub
argument_list|(
name|mapLoader
operator|.
name|loadDataMap
argument_list|(
name|any
argument_list|(
name|InputSource
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|toReturn
argument_list|(
operator|new
name|DataMapBuilder
argument_list|()
operator|.
name|with
argument_list|(
name|dbEntity
argument_list|(
literal|"ARTGROUP"
argument_list|)
operator|.
name|attributes
argument_list|(
name|dbAttr
argument_list|(
literal|"NAME"
argument_list|)
operator|.
name|typeVarchar
argument_list|(
literal|100
argument_list|)
operator|.
name|mandatory
argument_list|()
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|DbImportAction
name|action
init|=
name|buildDbImportAction
argument_list|(
name|log
argument_list|,
name|projectSaver
argument_list|,
name|mapLoader
argument_list|)
decl_stmt|;
name|action
operator|.
name|execute
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|projectSaver
argument_list|,
name|never
argument_list|()
argument_list|)
operator|.
name|save
argument_list|(
name|any
argument_list|(
name|Project
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mapLoader
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|loadDataMap
argument_list|(
name|any
argument_list|(
name|InputSource
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testImportWithDbError
parameter_list|()
throws|throws
name|Exception
block|{
name|DbLoader
name|dbLoader
init|=
name|mock
argument_list|(
name|DbLoader
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|dbLoader
operator|.
name|getDefaultTableTypes
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|doThrow
argument_list|(
operator|new
name|SQLException
argument_list|()
argument_list|)
operator|.
name|when
argument_list|(
name|dbLoader
argument_list|)
operator|.
name|load
argument_list|(
name|any
argument_list|(
name|DataMap
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|DbLoaderConfiguration
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|DbImportConfiguration
name|params
init|=
name|mock
argument_list|(
name|DbImportConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|params
operator|.
name|createLoader
argument_list|(
name|any
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|Connection
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|DbLoaderDelegate
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dbLoader
argument_list|)
expr_stmt|;
name|FileProjectSaver
name|projectSaver
init|=
name|mock
argument_list|(
name|FileProjectSaver
operator|.
name|class
argument_list|)
decl_stmt|;
name|doNothing
argument_list|()
operator|.
name|when
argument_list|(
name|projectSaver
argument_list|)
operator|.
name|save
argument_list|(
name|any
argument_list|(
name|Project
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|MapLoader
name|mapLoader
init|=
name|mock
argument_list|(
name|MapLoader
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|mapLoader
operator|.
name|loadDataMap
argument_list|(
name|any
argument_list|(
name|InputSource
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|DbImportAction
name|action
init|=
name|buildDbImportAction
argument_list|(
name|projectSaver
argument_list|,
name|mapLoader
argument_list|)
decl_stmt|;
try|try
block|{
name|action
operator|.
name|execute
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|verify
argument_list|(
name|projectSaver
argument_list|,
name|never
argument_list|()
argument_list|)
operator|.
name|save
argument_list|(
name|any
argument_list|(
name|Project
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mapLoader
argument_list|,
name|never
argument_list|()
argument_list|)
operator|.
name|loadDataMap
argument_list|(
name|any
argument_list|(
name|InputSource
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DbImportAction
name|buildDbImportAction
parameter_list|(
name|FileProjectSaver
name|projectSaver
parameter_list|,
name|MapLoader
name|mapLoader
parameter_list|)
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
name|when
argument_list|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|log
operator|.
name|isInfoEnabled
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|buildDbImportAction
argument_list|(
name|log
argument_list|,
name|projectSaver
argument_list|,
name|mapLoader
argument_list|)
return|;
block|}
specifier|private
name|DbImportAction
name|buildDbImportAction
parameter_list|(
name|Log
name|log
parameter_list|,
name|FileProjectSaver
name|projectSaver
parameter_list|,
name|MapLoader
name|mapLoader
parameter_list|)
throws|throws
name|Exception
block|{
name|DbAdapter
name|dbAdapter
init|=
name|mock
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|dbAdapter
operator|.
name|mergerFactory
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|MergerFactory
argument_list|()
argument_list|)
expr_stmt|;
name|DbAdapterFactory
name|adapterFactory
init|=
name|mock
argument_list|(
name|DbAdapterFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|adapterFactory
operator|.
name|createAdapter
argument_list|(
name|any
argument_list|(
name|DataNodeDescriptor
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|DataSource
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dbAdapter
argument_list|)
expr_stmt|;
name|DataSourceFactory
name|dataSourceFactory
init|=
name|mock
argument_list|(
name|DataSourceFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|DataSource
name|mock
init|=
name|mock
argument_list|(
name|DataSource
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|dataSourceFactory
operator|.
name|getDataSource
argument_list|(
name|any
argument_list|(
name|DataNodeDescriptor
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|mock
argument_list|)
expr_stmt|;
return|return
operator|new
name|DbImportAction
argument_list|(
name|log
argument_list|,
name|projectSaver
argument_list|,
name|dataSourceFactory
argument_list|,
name|adapterFactory
argument_list|,
name|mapLoader
argument_list|)
return|;
block|}
annotation|@
name|Test
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
annotation|@
name|Test
specifier|public
name|void
name|testMergeTokensSorting
parameter_list|()
block|{
name|LinkedList
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
operator|new
name|LinkedList
argument_list|<
name|MergerToken
argument_list|>
argument_list|()
decl_stmt|;
name|tokens
operator|.
name|add
argument_list|(
operator|new
name|AddColumnToDb
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|add
argument_list|(
operator|new
name|AddRelationshipToDb
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|add
argument_list|(
operator|new
name|CreateTableToDb
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|tokens
operator|.
name|add
argument_list|(
operator|new
name|CreateTableToModel
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|asList
argument_list|(
literal|"AddColumnToDb"
argument_list|,
literal|"CreateTableToDb"
argument_list|,
literal|"CreateTableToModel"
argument_list|,
literal|"AddRelationshipToDb"
argument_list|)
argument_list|,
name|toClasses
argument_list|(
name|DbImportAction
operator|.
name|sort
argument_list|(
name|tokens
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|toClasses
parameter_list|(
name|List
argument_list|<
name|MergerToken
argument_list|>
name|sort
parameter_list|)
block|{
name|LinkedList
argument_list|<
name|String
argument_list|>
name|res
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|MergerToken
name|mergerToken
range|:
name|sort
control|)
block|{
name|res
operator|.
name|add
argument_list|(
name|mergerToken
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|res
return|;
block|}
block|}
end_class

end_unit

