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
name|gen
operator|.
name|xml
package|;
end_package

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
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|gen
operator|.
name|CgenConfiguration
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|CgenSaverDelegateTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testExistingRootOverride
parameter_list|()
throws|throws
name|Exception
block|{
name|CgenConfiguration
name|config
init|=
operator|new
name|CgenConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/tmp/src/main/java"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|URL
name|baseURL
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"/tmp/src/main/resources"
argument_list|)
operator|.
name|toUri
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|CgenSaverDelegate
operator|.
name|resolveOutputDir
argument_list|(
name|baseURL
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/tmp/src/main/resources"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
argument_list|,
name|config
operator|.
name|getRootPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|""
argument_list|)
argument_list|,
name|config
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: do we care about this case?
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExistingRootAndRelPath
parameter_list|()
throws|throws
name|Exception
block|{
name|CgenConfiguration
name|config
init|=
operator|new
name|CgenConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/tmp/src/main/java"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|updateOutputPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|URL
name|baseURL
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"/tmp/src/main/resources"
argument_list|)
operator|.
name|toUri
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|CgenSaverDelegate
operator|.
name|resolveOutputDir
argument_list|(
name|baseURL
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/tmp/src/main/resources"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
argument_list|,
name|config
operator|.
name|getRootPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"../java"
argument_list|)
argument_list|,
name|config
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEmptyRootInMavenTree
parameter_list|()
throws|throws
name|Exception
block|{
name|CgenConfiguration
name|config
init|=
operator|new
name|CgenConfiguration
argument_list|()
decl_stmt|;
name|URL
name|baseURL
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"/tmp/src/main/resources"
argument_list|)
operator|.
name|toUri
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|CgenSaverDelegate
operator|.
name|resolveOutputDir
argument_list|(
name|baseURL
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/tmp/src/main/resources"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
argument_list|,
name|config
operator|.
name|getRootPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"../java"
argument_list|)
argument_list|,
name|config
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEmptyRoot
parameter_list|()
throws|throws
name|Exception
block|{
name|CgenConfiguration
name|config
init|=
operator|new
name|CgenConfiguration
argument_list|()
decl_stmt|;
name|URL
name|baseURL
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"/tmp/somefolder"
argument_list|)
operator|.
name|toUri
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
name|CgenSaverDelegate
operator|.
name|resolveOutputDir
argument_list|(
name|baseURL
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/tmp/somefolder"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
argument_list|,
name|config
operator|.
name|getRootPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|""
argument_list|)
argument_list|,
name|config
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

