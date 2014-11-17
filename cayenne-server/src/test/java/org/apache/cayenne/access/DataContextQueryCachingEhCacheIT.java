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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|cache
operator|.
name|EhCacheQueryCache
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
name|After
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
name|DataContextQueryCachingEhCacheIT
extends|extends
name|DataContextQueryCachingIT
block|{
specifier|protected
name|EhCacheQueryCache
name|domainCache
decl_stmt|;
specifier|protected
name|EhCacheQueryCache
name|contextCache
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|testSetUp
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
name|tPainting
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|setColumns
argument_list|(
literal|"PAINTING_ID"
argument_list|,
literal|"PAINTING_TITLE"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"ESTIMATED_PRICE"
argument_list|)
expr_stmt|;
name|domain
operator|=
name|context
operator|.
name|getParentDataDomain
argument_list|()
expr_stmt|;
name|oldCache
operator|=
name|domain
operator|.
name|getQueryCache
argument_list|()
expr_stmt|;
name|domainCache
operator|=
operator|new
name|EhCacheQueryCache
argument_list|()
expr_stmt|;
name|contextCache
operator|=
operator|new
name|EhCacheQueryCache
argument_list|()
expr_stmt|;
name|domain
operator|.
name|setQueryCache
argument_list|(
name|domainCache
argument_list|)
expr_stmt|;
name|context
operator|.
name|setQueryCache
argument_list|(
name|contextCache
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|testTearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|domainCache
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|contextCache
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

