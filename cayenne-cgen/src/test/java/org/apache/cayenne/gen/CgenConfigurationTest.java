begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|validation
operator|.
name|ValidationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assume
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|junit
operator|.
name|experimental
operator|.
name|runners
operator|.
name|Enclosed
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
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
name|InvalidPathException
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
name|Path
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
name|java
operator|.
name|util
operator|.
name|Locale
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|Enclosed
operator|.
name|class
argument_list|)
specifier|public
class|class
name|CgenConfigurationTest
block|{
specifier|public
specifier|static
class|class
name|CgenWindowsConfigurationTest
block|{
name|CgenConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|configuration
operator|=
operator|new
name|CgenConfiguration
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|checkPlatform
parameter_list|()
block|{
name|Assume
operator|.
name|assumeTrue
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ROOT
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"win"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|equalRootsEqualDirectories
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"C:\\test1\\test2\\test3"
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|relPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"C:\\test1\\test2\\test3"
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
name|relPath
operator|.
name|toString
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
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|relPath
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|equalRootsNotEqualDirectories
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"C:\\test1\\test2\\test3"
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|relPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"C:\\test1\\test2\\testAnother"
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
name|relPath
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"..\\testAnother"
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|relPath
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|equalRootsEmptyDirectories
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"C:\\"
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|relPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"C:\\"
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
name|relPath
operator|.
name|toString
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
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|relPath
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|notEqualRootsEqualDirectories
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"C:\\test1\\test2\\test3"
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|relPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"E:\\test1\\test2\\test3"
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
name|relPath
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"E:\\test1\\test2\\test3"
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|relPath
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|notEqualRootsNotEqualDirectories
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"C:\\test1\\test2\\test3"
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|relPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"E:\\test1\\test2\\testAnother"
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
name|relPath
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"E:\\test1\\test2\\testAnother"
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|relPath
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|notEqualRootsEmptyDirectories
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"C:\\"
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|relPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"E:\\"
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
name|relPath
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"E:\\"
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|relPath
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ValidationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|emptyRootNotEmptyRelPath
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|relPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"E:\\"
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
name|relPath
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"E:\\"
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|relPath
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|notEmptyRootEmptyRelPath
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"E:\\"
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|relPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
name|relPath
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|relPath
argument_list|,
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"E:\\"
argument_list|)
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|InvalidPathException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|invalidRelPath
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"C:\\test1\\test2\\test3"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
literal|"invalidRoot:\\test"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|InvalidPathException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|invalidRootPath
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"invalidRoot:\\test"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
literal|"C:\\test1\\test2\\test3"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|nullRootPath
parameter_list|()
block|{
name|configuration
operator|.
name|setRelPath
argument_list|(
literal|"C:\\test1\\test2\\test3"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"C:\\test1\\test2\\test3"
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"C:\\test1\\test2\\test3"
argument_list|)
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
class|class
name|CgenUnixConfigurationTest
block|{
name|CgenConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|configuration
operator|=
operator|new
name|CgenConfiguration
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|checkPlatform
parameter_list|()
block|{
name|Assume
operator|.
name|assumeFalse
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ROOT
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"win"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|equalRootsEqualDirectories
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/test1/test2/test3"
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|relPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"/test1/test2/test3"
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
name|relPath
operator|.
name|toString
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
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|relPath
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|equalRootsNotEqualDirectories
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/test1/test2/test3"
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|relPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"/test1/test2/testAnother"
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
name|relPath
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"../testAnother"
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|relPath
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|equalRootsEmptyDirectories
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|relPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
name|relPath
operator|.
name|toString
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
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|relPath
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|concatCorrectRootPathAndRelPath
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/test1/test2/test3"
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|relPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"test1/test2/test3"
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
name|relPath
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"test1/test2/test3"
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/test1/test2/test3/test1/test2/test3"
argument_list|)
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ValidationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|emptyRootNotEmptyRelPath
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|relPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
name|relPath
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/"
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|relPath
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|notEmptyRootEmptyRelPath
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
literal|""
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
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/"
argument_list|)
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ValidationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|invalidRootPath
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"invalidRoot:/test"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
literal|"/test1/test2/test3"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ValidationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|concatInvalidRootPathAndRelPath
parameter_list|()
block|{
name|configuration
operator|.
name|setRootPath
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"invalidRoot:/test"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRelPath
argument_list|(
literal|"test1/test2/test3"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|nullRootPath
parameter_list|()
block|{
name|configuration
operator|.
name|setRelPath
argument_list|(
literal|"/test1/test2/test3"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/test1/test2/test3"
argument_list|)
argument_list|,
name|configuration
operator|.
name|getRelPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"/test1/test2/test3"
argument_list|)
argument_list|,
name|configuration
operator|.
name|buildPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

