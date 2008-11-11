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
name|project
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
name|List
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
name|DataDomain
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
name|DataNode
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
name|conf
operator|.
name|Configuration
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
name|conf
operator|.
name|DriverDataSourceFactory
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
name|ObjEntity
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
name|CayenneCase
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ApplicationProjectTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|ApplicationProject
name|p
decl_stmt|;
specifier|protected
name|File
name|f
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|f
operator|=
operator|new
name|File
argument_list|(
name|Configuration
operator|.
name|DEFAULT_DOMAIN_FILE
argument_list|)
expr_stmt|;
name|p
operator|=
operator|new
name|ApplicationProject
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProjectFileForObject
parameter_list|()
throws|throws
name|Exception
block|{
name|DataNode
name|node
init|=
operator|new
name|DataNode
argument_list|(
literal|"dn"
argument_list|)
decl_stmt|;
name|DataDomain
name|dm
init|=
operator|new
name|DataDomain
argument_list|(
literal|"dd"
argument_list|)
decl_stmt|;
name|dm
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|p
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addDomain
argument_list|(
name|dm
argument_list|)
expr_stmt|;
name|ProjectFile
name|pf
init|=
name|p
operator|.
name|projectFileForObject
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|pf
argument_list|)
expr_stmt|;
name|node
operator|.
name|setDataSourceFactory
argument_list|(
name|DriverDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ProjectFile
name|pf1
init|=
name|p
operator|.
name|projectFileForObject
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|pf1
operator|instanceof
name|DataNodeFile
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|node
argument_list|,
name|pf1
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|p
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConstructor
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|f
operator|.
name|getCanonicalFile
argument_list|()
argument_list|,
name|p
operator|.
name|getMainFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|p
operator|.
name|projectFileForObject
argument_list|(
name|p
argument_list|)
operator|instanceof
name|ApplicationProjectFile
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|p
operator|.
name|getChildren
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|p
operator|.
name|getChildren
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBuildFileList
parameter_list|()
throws|throws
name|Exception
block|{
comment|// build a test project tree
name|DataDomain
name|d1
init|=
operator|new
name|DataDomain
argument_list|(
literal|"d1"
argument_list|)
decl_stmt|;
name|DataMap
name|m1
init|=
operator|new
name|DataMap
argument_list|(
literal|"m1"
argument_list|)
decl_stmt|;
name|DataNode
name|n1
init|=
operator|new
name|DataNode
argument_list|(
literal|"n1"
argument_list|)
decl_stmt|;
name|n1
operator|.
name|setDataSourceFactory
argument_list|(
name|DriverDataSourceFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|d1
operator|.
name|addMap
argument_list|(
name|m1
argument_list|)
expr_stmt|;
name|d1
operator|.
name|addNode
argument_list|(
name|n1
argument_list|)
expr_stmt|;
name|ObjEntity
name|oe1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"oe1"
argument_list|)
decl_stmt|;
name|m1
operator|.
name|addObjEntity
argument_list|(
name|oe1
argument_list|)
expr_stmt|;
name|n1
operator|.
name|addDataMap
argument_list|(
name|m1
argument_list|)
expr_stmt|;
comment|// initialize project
name|p
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addDomain
argument_list|(
name|d1
argument_list|)
expr_stmt|;
comment|// make assertions
name|List
name|files
init|=
name|p
operator|.
name|buildFileList
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|files
argument_list|)
expr_stmt|;
comment|// list must have 3 files total
name|assertEquals
argument_list|(
literal|"Unexpected number of files: "
operator|+
name|files
argument_list|,
literal|3
argument_list|,
name|files
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

