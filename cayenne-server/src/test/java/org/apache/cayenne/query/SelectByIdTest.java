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
name|query
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
name|assertNotNull
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
name|testdo
operator|.
name|testmap
operator|.
name|Artist
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
name|SelectByIdTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testPrefetch
parameter_list|()
block|{
name|PrefetchTreeNode
name|root
init|=
name|PrefetchTreeNode
operator|.
name|withPath
argument_list|(
literal|"a.b"
argument_list|,
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
decl_stmt|;
name|SelectById
argument_list|<
name|Artist
argument_list|>
name|q
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|6
argument_list|)
decl_stmt|;
name|q
operator|.
name|prefetch
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|PrefetchTreeNode
name|prefetch
init|=
name|q
operator|.
name|getPrefetches
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|prefetch
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|prefetch
operator|.
name|getNode
argument_list|(
literal|"a.b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrefetch_Path
parameter_list|()
block|{
name|SelectById
argument_list|<
name|Artist
argument_list|>
name|q
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|7
argument_list|)
decl_stmt|;
name|q
operator|.
name|prefetch
argument_list|(
literal|"a.b"
argument_list|,
name|PrefetchTreeNode
operator|.
name|DISJOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|PrefetchTreeNode
name|prefetch
init|=
name|q
operator|.
name|getPrefetches
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|prefetch
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|prefetch
operator|.
name|getNode
argument_list|(
literal|"a.b"
argument_list|)
argument_list|)
expr_stmt|;
name|q
operator|.
name|prefetch
argument_list|(
literal|"a.c"
argument_list|,
name|PrefetchTreeNode
operator|.
name|DISJOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
name|prefetch
operator|=
name|q
operator|.
name|getPrefetches
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|prefetch
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|prefetch
operator|.
name|getNode
argument_list|(
literal|"a.c"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|prefetch
operator|.
name|getNode
argument_list|(
literal|"a.b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrefetch_Subroot
parameter_list|()
block|{
name|PrefetchTreeNode
name|root
init|=
name|PrefetchTreeNode
operator|.
name|withPath
argument_list|(
literal|"a.b"
argument_list|,
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
decl_stmt|;
name|SelectById
argument_list|<
name|Artist
argument_list|>
name|q
init|=
name|SelectById
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|8
argument_list|)
decl_stmt|;
name|q
operator|.
name|prefetch
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|PrefetchTreeNode
name|prefetch
init|=
name|q
operator|.
name|getPrefetches
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|prefetch
operator|.
name|getNode
argument_list|(
literal|"a.b"
argument_list|)
argument_list|)
expr_stmt|;
name|PrefetchTreeNode
name|subRoot
init|=
name|PrefetchTreeNode
operator|.
name|withPath
argument_list|(
literal|"a.b.c"
argument_list|,
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
decl_stmt|;
name|q
operator|.
name|prefetch
argument_list|(
name|subRoot
argument_list|)
expr_stmt|;
name|prefetch
operator|=
name|q
operator|.
name|getPrefetches
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|prefetch
operator|.
name|getNode
argument_list|(
literal|"a.b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|prefetch
operator|.
name|getNode
argument_list|(
literal|"a.b.c"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

