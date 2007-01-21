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
name|access
package|;
end_package

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
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|art
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
name|DataObject
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
name|DataRow
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
name|MockQueryMetadata
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
name|QueryMetadata
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
name|SelectQuery
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
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DataContextQueryCachingTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|DataContext
name|context
decl_stmt|;
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
name|context
operator|=
name|createDataContextWithSharedCache
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|DataContext
name|createDataContextNoCacheClear
parameter_list|()
block|{
return|return
name|getDomain
argument_list|()
operator|.
name|createDataContext
argument_list|()
return|;
block|}
specifier|public
name|void
name|testLocalCacheDataRowsNoRefresh
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQuery
name|select
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|select
operator|.
name|setName
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|select
operator|.
name|setRefreshingObjects
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|select
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|select
operator|.
name|setCachePolicy
argument_list|(
name|QueryMetadata
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|MockDataNode
name|engine
init|=
name|MockDataNode
operator|.
name|interceptNode
argument_list|(
name|getDomain
argument_list|()
argument_list|,
name|getNode
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|List
name|rows
init|=
name|mockupDataRows
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|engine
operator|.
name|addExpectedResult
argument_list|(
name|select
argument_list|,
name|rows
argument_list|)
expr_stmt|;
comment|// first run, no cache yet
name|List
name|resultRows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rows
argument_list|,
name|resultRows
argument_list|)
expr_stmt|;
name|QueryMetadata
name|cacheKey
init|=
operator|new
name|MockQueryMetadata
argument_list|()
block|{
specifier|public
name|String
name|getCacheKey
parameter_list|()
block|{
return|return
literal|"c"
return|;
block|}
block|}
decl_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rows
argument_list|,
name|context
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
comment|// now the query with the same name must run from cache
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|List
name|cachedResultRows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rows
argument_list|,
name|cachedResultRows
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|engine
operator|.
name|stopInterceptNode
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testLocalCacheDataRowsRefresh
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQuery
name|select
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|select
operator|.
name|setName
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|select
operator|.
name|setRefreshingObjects
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|select
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|select
operator|.
name|setCachePolicy
argument_list|(
name|QueryMetadata
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|MockDataNode
name|engine
init|=
name|MockDataNode
operator|.
name|interceptNode
argument_list|(
name|getDomain
argument_list|()
argument_list|,
name|getNode
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
comment|// first run, no cache yet
name|List
name|rows1
init|=
name|mockupDataRows
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|engine
operator|.
name|addExpectedResult
argument_list|(
name|select
argument_list|,
name|rows1
argument_list|)
expr_stmt|;
name|List
name|resultRows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rows1
argument_list|,
name|resultRows
argument_list|)
expr_stmt|;
name|QueryMetadata
name|cacheKey
init|=
operator|new
name|MockQueryMetadata
argument_list|()
block|{
specifier|public
name|String
name|getCacheKey
parameter_list|()
block|{
return|return
literal|"c"
return|;
block|}
block|}
decl_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rows1
argument_list|,
name|context
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
comment|// second run, must refresh the cache
name|List
name|rows2
init|=
name|mockupDataRows
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|engine
operator|.
name|addExpectedResult
argument_list|(
name|select
argument_list|,
name|rows2
argument_list|)
expr_stmt|;
name|select
operator|.
name|setCachePolicy
argument_list|(
name|QueryMetadata
operator|.
name|LOCAL_CACHE_REFRESH
argument_list|)
expr_stmt|;
name|List
name|freshResultRows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rows2
argument_list|,
name|freshResultRows
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rows2
argument_list|,
name|context
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|engine
operator|.
name|stopInterceptNode
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testSharedCacheDataRowsNoRefresh
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQuery
name|select
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|select
operator|.
name|setName
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|select
operator|.
name|setRefreshingObjects
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|select
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|select
operator|.
name|setCachePolicy
argument_list|(
name|QueryMetadata
operator|.
name|SHARED_CACHE
argument_list|)
expr_stmt|;
name|MockDataNode
name|engine
init|=
name|MockDataNode
operator|.
name|interceptNode
argument_list|(
name|getDomain
argument_list|()
argument_list|,
name|getNode
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|List
name|rows
init|=
name|mockupDataRows
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|engine
operator|.
name|addExpectedResult
argument_list|(
name|select
argument_list|,
name|rows
argument_list|)
expr_stmt|;
comment|// first run, no cache yet
name|List
name|resultRows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rows
argument_list|,
name|resultRows
argument_list|)
expr_stmt|;
name|QueryMetadata
name|cacheKey
init|=
operator|new
name|MockQueryMetadata
argument_list|()
block|{
specifier|public
name|String
name|getCacheKey
parameter_list|()
block|{
return|return
literal|"c"
return|;
block|}
block|}
decl_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rows
argument_list|,
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
comment|// now the query with the same name must run from cache
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|List
name|cachedResultRows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rows
argument_list|,
name|cachedResultRows
argument_list|)
expr_stmt|;
comment|// query from an alt DataContext must run from cache
name|DataContext
name|altContext
init|=
name|createDataContextNoCacheClear
argument_list|()
decl_stmt|;
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|List
name|altResultRows
init|=
name|altContext
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rows
argument_list|,
name|altResultRows
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|engine
operator|.
name|stopInterceptNode
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testSharedCacheDataRowsRefresh
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQuery
name|select
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|select
operator|.
name|setName
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|select
operator|.
name|setRefreshingObjects
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|select
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|select
operator|.
name|setCachePolicy
argument_list|(
name|QueryMetadata
operator|.
name|SHARED_CACHE
argument_list|)
expr_stmt|;
name|MockDataNode
name|engine
init|=
name|MockDataNode
operator|.
name|interceptNode
argument_list|(
name|getDomain
argument_list|()
argument_list|,
name|getNode
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
comment|// first run, no cache yet
name|List
name|rows1
init|=
name|mockupDataRows
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|engine
operator|.
name|addExpectedResult
argument_list|(
name|select
argument_list|,
name|rows1
argument_list|)
expr_stmt|;
name|List
name|resultRows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rows1
argument_list|,
name|resultRows
argument_list|)
expr_stmt|;
name|QueryMetadata
name|cacheKey
init|=
operator|new
name|MockQueryMetadata
argument_list|()
block|{
specifier|public
name|String
name|getCacheKey
parameter_list|()
block|{
return|return
literal|"c"
return|;
block|}
block|}
decl_stmt|;
name|assertEquals
argument_list|(
name|rows1
argument_list|,
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
comment|// second run, must refresh the cache
name|List
name|rows2
init|=
name|mockupDataRows
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|engine
operator|.
name|addExpectedResult
argument_list|(
name|select
argument_list|,
name|rows2
argument_list|)
expr_stmt|;
name|select
operator|.
name|setCachePolicy
argument_list|(
name|QueryMetadata
operator|.
name|SHARED_CACHE_REFRESH
argument_list|)
expr_stmt|;
name|List
name|freshResultRows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rows2
argument_list|,
name|freshResultRows
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rows2
argument_list|,
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|engine
operator|.
name|stopInterceptNode
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testLocalCacheDataObjectsRefresh
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQuery
name|select
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|select
operator|.
name|setName
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|select
operator|.
name|setRefreshingObjects
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|select
operator|.
name|setFetchingDataRows
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|select
operator|.
name|setCachePolicy
argument_list|(
name|QueryMetadata
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|MockDataNode
name|engine
init|=
name|MockDataNode
operator|.
name|interceptNode
argument_list|(
name|getDomain
argument_list|()
argument_list|,
name|getNode
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
comment|// first run, no cache yet
name|List
name|rows1
init|=
name|mockupDataRows
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|engine
operator|.
name|addExpectedResult
argument_list|(
name|select
argument_list|,
name|rows1
argument_list|)
expr_stmt|;
name|List
name|resultRows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|resultRows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultRows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|DataObject
argument_list|)
expr_stmt|;
name|QueryMetadata
name|cacheKey
init|=
operator|new
name|MockQueryMetadata
argument_list|()
block|{
specifier|public
name|String
name|getCacheKey
parameter_list|()
block|{
return|return
literal|"c"
return|;
block|}
block|}
decl_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|resultRows
argument_list|,
name|context
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
comment|// second run, must refresh the cache
name|List
name|rows2
init|=
name|mockupDataRows
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|engine
operator|.
name|addExpectedResult
argument_list|(
name|select
argument_list|,
name|rows2
argument_list|)
expr_stmt|;
name|select
operator|.
name|setCachePolicy
argument_list|(
name|QueryMetadata
operator|.
name|LOCAL_CACHE_REFRESH
argument_list|)
expr_stmt|;
name|List
name|freshResultRows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|freshResultRows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultRows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|DataObject
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|freshResultRows
argument_list|,
name|context
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|engine
operator|.
name|stopInterceptNode
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testLocalCacheDataObjectsNoRefresh
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQuery
name|select
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|select
operator|.
name|setName
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|select
operator|.
name|setRefreshingObjects
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|select
operator|.
name|setFetchingDataRows
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|select
operator|.
name|setCachePolicy
argument_list|(
name|QueryMetadata
operator|.
name|LOCAL_CACHE
argument_list|)
expr_stmt|;
name|MockDataNode
name|engine
init|=
name|MockDataNode
operator|.
name|interceptNode
argument_list|(
name|getDomain
argument_list|()
argument_list|,
name|getNode
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|List
name|rows
init|=
name|mockupDataRows
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|engine
operator|.
name|addExpectedResult
argument_list|(
name|select
argument_list|,
name|rows
argument_list|)
expr_stmt|;
comment|// first run, no cache yet
name|List
name|resultRows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|resultRows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultRows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|DataObject
argument_list|)
expr_stmt|;
name|QueryMetadata
name|cacheKey
init|=
operator|new
name|MockQueryMetadata
argument_list|()
block|{
specifier|public
name|String
name|getCacheKey
parameter_list|()
block|{
return|return
literal|"c"
return|;
block|}
block|}
decl_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|resultRows
argument_list|,
name|context
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
comment|// now the query with the same name must run from cache
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|List
name|cachedResultRows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|resultRows
argument_list|,
name|cachedResultRows
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|engine
operator|.
name|stopInterceptNode
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testSharedCacheDataObjectsNoRefresh
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQuery
name|select
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|select
operator|.
name|setName
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|select
operator|.
name|setRefreshingObjects
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|select
operator|.
name|setFetchingDataRows
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|select
operator|.
name|setCachePolicy
argument_list|(
name|QueryMetadata
operator|.
name|SHARED_CACHE
argument_list|)
expr_stmt|;
name|MockDataNode
name|engine
init|=
name|MockDataNode
operator|.
name|interceptNode
argument_list|(
name|getDomain
argument_list|()
argument_list|,
name|getNode
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|List
name|rows
init|=
name|mockupDataRows
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|engine
operator|.
name|addExpectedResult
argument_list|(
name|select
argument_list|,
name|rows
argument_list|)
expr_stmt|;
comment|// first run, no cache yet
name|List
name|resultRows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|resultRows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|resultRows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|DataObject
argument_list|)
expr_stmt|;
name|QueryMetadata
name|cacheKey
init|=
operator|new
name|MockQueryMetadata
argument_list|()
block|{
specifier|public
name|String
name|getCacheKey
parameter_list|()
block|{
return|return
literal|"c"
return|;
block|}
block|}
decl_stmt|;
name|assertNull
argument_list|(
name|context
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rows
argument_list|,
name|context
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
argument_list|)
expr_stmt|;
comment|// now the query with the same name must run from cache
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|List
name|cachedResultRows
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cachedResultRows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cachedResultRows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|DataObject
argument_list|)
expr_stmt|;
comment|// query from an alt DataContext must run from cache
name|DataContext
name|altContext
init|=
name|createDataContextNoCacheClear
argument_list|()
decl_stmt|;
name|engine
operator|.
name|reset
argument_list|()
expr_stmt|;
name|List
name|altResultRows
init|=
name|altContext
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|engine
operator|.
name|getRunCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|altResultRows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|altResultRows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|DataObject
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|engine
operator|.
name|stopInterceptNode
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testLocalCacheRefreshObjectsRefresh
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteTestData
argument_list|()
expr_stmt|;
name|createTestData
argument_list|(
literal|"testLocalCacheRefreshObjectsRefresh_Insert"
argument_list|)
expr_stmt|;
name|SelectQuery
name|select
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|select
operator|.
name|setName
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|select
operator|.
name|setRefreshingObjects
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|select
operator|.
name|setCachePolicy
argument_list|(
name|QueryMetadata
operator|.
name|LOCAL_CACHE_REFRESH
argument_list|)
expr_stmt|;
comment|// no cache yet...
name|List
name|objects1
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
operator|)
name|objects1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"aaa"
argument_list|,
name|a1
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
comment|// cache, but force refresh
name|createTestData
argument_list|(
literal|"testLocalCacheRefreshObjectsRefresh_Update1"
argument_list|)
expr_stmt|;
name|List
name|objects2
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a2
init|=
operator|(
name|Artist
operator|)
name|objects2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|a2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bbb"
argument_list|,
name|a2
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
comment|// cache, no refresh
name|select
operator|.
name|setRefreshingObjects
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|createTestData
argument_list|(
literal|"testLocalCacheRefreshObjectsRefresh_Update1"
argument_list|)
expr_stmt|;
name|List
name|objects3
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects3
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a3
init|=
operator|(
name|Artist
operator|)
name|objects3
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|a1
argument_list|,
name|a3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bbb"
argument_list|,
name|a3
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|List
name|mockupDataRows
parameter_list|(
name|int
name|len
parameter_list|)
block|{
name|List
name|rows
init|=
operator|new
name|ArrayList
argument_list|(
name|len
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|DataRow
name|a
init|=
operator|new
name|DataRow
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|a
operator|.
name|put
argument_list|(
literal|"ARTIST_ID"
argument_list|,
operator|new
name|Integer
argument_list|(
name|i
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|.
name|put
argument_list|(
literal|"ARTIST_NAME"
argument_list|,
literal|"A-"
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
name|rows
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
return|return
name|rows
return|;
block|}
block|}
end_class

end_unit

