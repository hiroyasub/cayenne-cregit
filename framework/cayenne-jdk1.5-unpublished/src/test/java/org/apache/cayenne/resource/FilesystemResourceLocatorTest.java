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
name|resource
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
name|io
operator|.
name|FileOutputStream
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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|unit
operator|.
name|CayenneResources
import|;
end_import

begin_class
specifier|public
class|class
name|FilesystemResourceLocatorTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testArrayConstructor
parameter_list|()
block|{
name|FilesystemResourceLocator
name|l1
init|=
operator|new
name|FilesystemResourceLocator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|l1
operator|.
name|roots
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.dir"
argument_list|)
argument_list|,
name|l1
operator|.
name|roots
index|[
literal|0
index|]
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|base
init|=
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getTestDir
argument_list|()
decl_stmt|;
name|File
name|f1
init|=
operator|new
name|File
argument_list|(
name|base
argument_list|,
literal|"f1"
argument_list|)
decl_stmt|;
name|File
name|f2
init|=
operator|new
name|File
argument_list|(
operator|new
name|File
argument_list|(
name|base
argument_list|,
literal|"f2"
argument_list|)
argument_list|,
literal|"f3"
argument_list|)
decl_stmt|;
name|FilesystemResourceLocator
name|l2
init|=
operator|new
name|FilesystemResourceLocator
argument_list|(
name|f1
argument_list|,
name|f2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|l2
operator|.
name|roots
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|base
argument_list|,
name|l2
operator|.
name|roots
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
name|base
argument_list|,
literal|"f2"
argument_list|)
argument_list|,
name|l2
operator|.
name|roots
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCollectionConstructor
parameter_list|()
block|{
name|FilesystemResourceLocator
name|l1
init|=
operator|new
name|FilesystemResourceLocator
argument_list|(
name|Collections
operator|.
expr|<
name|File
operator|>
name|emptyList
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|l1
operator|.
name|roots
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.dir"
argument_list|)
argument_list|,
name|l1
operator|.
name|roots
index|[
literal|0
index|]
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|base
init|=
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getTestDir
argument_list|()
decl_stmt|;
name|File
name|f1
init|=
operator|new
name|File
argument_list|(
name|base
argument_list|,
literal|"f1"
argument_list|)
decl_stmt|;
name|File
name|f2
init|=
operator|new
name|File
argument_list|(
operator|new
name|File
argument_list|(
name|base
argument_list|,
literal|"f2"
argument_list|)
argument_list|,
literal|"f3"
argument_list|)
decl_stmt|;
name|FilesystemResourceLocator
name|l2
init|=
operator|new
name|FilesystemResourceLocator
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|f1
argument_list|,
name|f2
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|l2
operator|.
name|roots
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|base
argument_list|,
name|l2
operator|.
name|roots
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
name|base
argument_list|,
literal|"f2"
argument_list|)
argument_list|,
name|l2
operator|.
name|roots
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFindResources
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|base
init|=
operator|new
name|File
argument_list|(
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getTestDir
argument_list|()
argument_list|,
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|root1
init|=
operator|new
name|File
argument_list|(
name|base
argument_list|,
literal|"r1"
argument_list|)
decl_stmt|;
name|File
name|root2
init|=
operator|new
name|File
argument_list|(
name|base
argument_list|,
literal|"r2"
argument_list|)
decl_stmt|;
name|root1
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|root2
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FilesystemResourceLocator
name|locator
init|=
operator|new
name|FilesystemResourceLocator
argument_list|(
name|root1
argument_list|,
name|root2
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|Resource
argument_list|>
name|resources1
init|=
name|locator
operator|.
name|findResources
argument_list|(
literal|"x.txt"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resources1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|resources1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|f1
init|=
operator|new
name|File
argument_list|(
name|root1
argument_list|,
literal|"x.txt"
argument_list|)
decl_stmt|;
name|touch
argument_list|(
name|f1
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Resource
argument_list|>
name|resources2
init|=
name|locator
operator|.
name|findResources
argument_list|(
literal|"x.txt"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resources2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resources2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|f1
operator|.
name|toURL
argument_list|()
argument_list|,
name|resources2
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getURL
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|f2
init|=
operator|new
name|File
argument_list|(
name|root2
argument_list|,
literal|"x.txt"
argument_list|)
decl_stmt|;
name|touch
argument_list|(
name|f2
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Resource
argument_list|>
name|resources3
init|=
name|locator
operator|.
name|findResources
argument_list|(
literal|"x.txt"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resources3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|resources3
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Resource
index|[]
name|resources3a
init|=
name|resources3
operator|.
name|toArray
argument_list|(
operator|new
name|Resource
index|[
literal|2
index|]
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|f1
operator|.
name|toURL
argument_list|()
argument_list|,
name|resources3a
index|[
literal|0
index|]
operator|.
name|getURL
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|f2
operator|.
name|toURL
argument_list|()
argument_list|,
name|resources3a
index|[
literal|1
index|]
operator|.
name|getURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|touch
parameter_list|(
name|File
name|f
parameter_list|)
throws|throws
name|Exception
block|{
name|FileOutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|'a'
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

