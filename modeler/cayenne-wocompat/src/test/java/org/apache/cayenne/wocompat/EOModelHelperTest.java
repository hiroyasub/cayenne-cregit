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
name|wocompat
package|;
end_package

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
name|assertNull
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
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Map
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

begin_class
specifier|public
class|class
name|EOModelHelperTest
block|{
specifier|protected
name|EOModelHelper
name|helper
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"wotests/art.eomodeld/"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|helper
operator|=
operator|new
name|EOModelHelper
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testModelNames
parameter_list|()
throws|throws
name|Exception
block|{
name|Iterator
name|names
init|=
name|helper
operator|.
name|modelNames
argument_list|()
decl_stmt|;
comment|// collect to list and then analyze
name|List
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
while|while
condition|(
name|names
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|names
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|contains
argument_list|(
literal|"Artist"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|contains
argument_list|(
literal|"Painting"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|contains
argument_list|(
literal|"ExhibitType"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testQueryNames
parameter_list|()
throws|throws
name|Exception
block|{
name|Iterator
name|artistNames
init|=
name|helper
operator|.
name|queryNames
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|artistNames
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|etNames
init|=
name|helper
operator|.
name|queryNames
argument_list|(
literal|"ExhibitType"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|etNames
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
comment|// collect to list and then analyze
name|List
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
while|while
condition|(
name|etNames
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|etNames
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|contains
argument_list|(
literal|"FetchAll"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|list
operator|.
name|contains
argument_list|(
literal|"TestQuery"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testQueryPListMap
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNull
argument_list|(
name|helper
operator|.
name|queryPListMap
argument_list|(
literal|"Artist"
argument_list|,
literal|"AAA"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|helper
operator|.
name|queryPListMap
argument_list|(
literal|"ExhibitType"
argument_list|,
literal|"AAA"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
name|query
init|=
name|helper
operator|.
name|queryPListMap
argument_list|(
literal|"ExhibitType"
argument_list|,
literal|"FetchAll"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|query
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadQueryIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
name|index
init|=
name|helper
operator|.
name|loadQueryIndex
argument_list|(
literal|"ExhibitType"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|index
operator|.
name|containsKey
argument_list|(
literal|"FetchAll"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOpenQueryStream
parameter_list|()
throws|throws
name|Exception
block|{
try|try
init|(
name|InputStream
name|in
init|=
name|helper
operator|.
name|openQueryStream
argument_list|(
literal|"ExhibitType"
argument_list|)
init|;
init|)
block|{
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOpenNonExistentQueryStream
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|helper
operator|.
name|openQueryStream
argument_list|(
literal|"Artist"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Exception expected - artist has no fetch spec."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioex
parameter_list|)
block|{
comment|// expected...
block|}
block|}
block|}
end_class

end_unit

