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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|ProcedureQueryCacheKeyTest
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
specifier|public
name|void
name|testNoCache
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|ProcedureQuery
name|query
init|=
operator|new
name|ProcedureQuery
argument_list|(
literal|"ABC"
argument_list|,
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
specifier|public
name|void
name|testLocalCache
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|ProcedureQuery
name|query
init|=
operator|new
name|ProcedureQuery
argument_list|(
literal|"ABC"
argument_list|,
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
specifier|public
name|void
name|testSharedCache
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|ProcedureQuery
name|query
init|=
operator|new
name|ProcedureQuery
argument_list|(
literal|"ABC"
argument_list|,
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
specifier|public
name|void
name|testNamedQuery
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|ProcedureQuery
name|query
init|=
operator|new
name|ProcedureQuery
argument_list|(
literal|"ABC"
argument_list|,
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
name|assertEquals
argument_list|(
literal|"proc:"
argument_list|,
name|md1
operator|.
name|getCacheKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCacheFetchOffsetAndLimit
parameter_list|()
block|{
name|EntityResolver
name|resolver
init|=
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|ProcedureQuery
name|q1
init|=
operator|new
name|ProcedureQuery
argument_list|(
literal|"ABC"
argument_list|,
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
name|SHARED_CACHE
argument_list|)
expr_stmt|;
name|q1
operator|.
name|setFetchOffset
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|q1
operator|.
name|setFetchLimit
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|ProcedureQuery
name|q2
init|=
operator|new
name|ProcedureQuery
argument_list|(
literal|"ABC"
argument_list|,
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
name|SHARED_CACHE
argument_list|)
expr_stmt|;
name|q2
operator|.
name|setFetchOffset
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|q2
operator|.
name|setFetchLimit
argument_list|(
literal|3
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
block|}
block|}
end_class

end_unit

