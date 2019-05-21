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
name|jcache
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
name|ObjectContext
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
name|cache
operator|.
name|NestedQueryCache
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
name|ServerRuntime
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
name|Inject
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
name|jcache
operator|.
name|unit
operator|.
name|JCacheCase
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
name|query
operator|.
name|ObjectSelect
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|test
operator|.
name|jdbc
operator|.
name|TableHelper
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
name|apache
operator|.
name|cayenne
operator|.
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|CayenneProjects
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
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
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
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|CayenneJCacheModuleIT
extends|extends
name|JCacheCase
block|{
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Inject
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
name|ServerRuntime
name|runtime
decl_stmt|;
specifier|private
name|TableHelper
name|tArtist
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUpTableHelper
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTIST"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|setColumns
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|"ARTIST_NAME"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCachedQueries
parameter_list|()
throws|throws
name|Exception
block|{
comment|// make sure that we have JCacheQueryCache
name|assertEquals
argument_list|(
name|JCacheQueryCache
operator|.
name|class
argument_list|,
operator|(
operator|(
name|NestedQueryCache
operator|)
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getQueryCache
argument_list|()
operator|)
operator|.
name|getDelegate
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectSelect
argument_list|<
name|Artist
argument_list|>
name|g1
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|localCache
argument_list|(
literal|"g1"
argument_list|)
decl_stmt|;
name|ObjectSelect
argument_list|<
name|Artist
argument_list|>
name|g2
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|localCache
argument_list|(
literal|"g2"
argument_list|)
decl_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"artist1"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|g1
operator|.
name|select
argument_list|(
name|context
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// we are still cached, must not see the new changes
name|tArtist
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"artist3"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|"artist4"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|g1
operator|.
name|select
argument_list|(
name|context
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// different cache group - must see the changes
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|g2
operator|.
name|select
argument_list|(
name|context
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// refresh the cache, so that "g1" could see the changes
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getQueryCache
argument_list|()
operator|.
name|removeGroup
argument_list|(
literal|"g1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|g1
operator|.
name|select
argument_list|(
name|context
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|g2
operator|.
name|select
argument_list|(
name|context
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

