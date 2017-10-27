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
name|query
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
name|exp
operator|.
name|ExpressionFactory
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
name|EntityResolver
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
name|testdo
operator|.
name|testmap
operator|.
name|Painting
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
name|ServerCase
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
name|assertNotEquals
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

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|SelectQueryCacheKeyIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|EntityResolver
name|resolver
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testNoCache
parameter_list|()
block|{
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|query
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|QueryMetadata
name|md1
init|=
name|query
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|NO_CACHE
argument_list|,
name|md1
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|md1
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|.
name|setName
argument_list|(
literal|"XYZ"
argument_list|)
expr_stmt|;
name|QueryMetadata
name|md2
init|=
name|query
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|NO_CACHE
argument_list|,
name|md2
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|md2
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLocalCache
parameter_list|()
block|{
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|query
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|QueryMetadata
name|md1
init|=
name|query
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|,
name|md1
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|md1
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUseLocalCache
parameter_list|()
block|{
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q1
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q1
operator|.
name|useLocalCache
argument_list|()
expr_stmt|;
name|QueryMetadata
name|md1
init|=
name|q1
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|,
name|md1
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|md1
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|md1
operator|.
name|getCacheGroup
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q2
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q2
operator|.
name|useLocalCache
argument_list|(
literal|"g1"
argument_list|)
expr_stmt|;
name|QueryMetadata
name|md2
init|=
name|q2
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|,
name|md2
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|md2
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
name|md2
operator|.
name|getCacheGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSharedCache
parameter_list|()
block|{
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|query
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|)
expr_stmt|;
name|QueryMetadata
name|md1
init|=
name|query
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|,
name|md1
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|md1
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUseSharedCache
parameter_list|()
block|{
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q1
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q1
operator|.
name|useSharedCache
argument_list|()
expr_stmt|;
name|QueryMetadata
name|md1
init|=
name|q1
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|,
name|md1
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|md1
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|md1
operator|.
name|getCacheGroup
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q2
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q2
operator|.
name|useSharedCache
argument_list|(
literal|"g1"
argument_list|,
literal|"g2"
argument_list|)
expr_stmt|;
name|QueryMetadata
name|md2
init|=
name|q2
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|,
name|md2
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|md2
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"g1"
argument_list|,
name|md2
operator|.
name|getCacheGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNamedQuery
parameter_list|()
block|{
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|query
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|)
expr_stmt|;
name|query
operator|.
name|setName
argument_list|(
literal|"XYZ"
argument_list|)
expr_stmt|;
name|QueryMetadata
name|md1
init|=
name|query
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|,
name|md1
operator|.
name|getCacheStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"XYZ"
operator|.
name|equals
argument_list|(
name|md1
operator|.
name|getCacheKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUniqueKeyEntity
parameter_list|()
block|{
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q1
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q1
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q2
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q2
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|Painting
argument_list|>
name|q3
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|q3
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|q1
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|q1
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|,
name|q2
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|q1
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|,
name|q3
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUniqueKeyEntityQualifier
parameter_list|()
block|{
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q1
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q1
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|q1
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q2
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q2
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|q2
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q3
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q3
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|q3
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"a"
argument_list|,
literal|"c"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|q1
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|q1
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|,
name|q2
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|q1
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|,
name|q3
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUniqueKeyEntityFetchLimit
parameter_list|()
block|{
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q1
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q1
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|q1
operator|.
name|setFetchLimit
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q2
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q2
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|q2
operator|.
name|setFetchLimit
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q3
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q3
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|q3
operator|.
name|setFetchLimit
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|Artist
argument_list|>
name|q4
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q4
operator|.
name|setCacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|q1
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|q1
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|,
name|q2
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|q1
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|,
name|q3
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotEquals
argument_list|(
name|q1
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|,
name|q4
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

