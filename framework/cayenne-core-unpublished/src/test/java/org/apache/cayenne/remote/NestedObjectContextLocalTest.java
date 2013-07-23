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
name|remote
package|;
end_package

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
name|cayenne
operator|.
name|BaseContext
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
name|rop
operator|.
name|client
operator|.
name|ClientRuntime
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
name|query
operator|.
name|QueryCacheStrategy
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
name|testdo
operator|.
name|mt
operator|.
name|ClientMtTable1
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
name|client
operator|.
name|ClientCase
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
name|ClientCase
operator|.
name|MULTI_TIER_PROJECT
argument_list|)
specifier|public
class|class
name|NestedObjectContextLocalTest
extends|extends
name|RemoteCayenneCase
block|{
annotation|@
name|Inject
specifier|private
name|ClientRuntime
name|runtime
decl_stmt|;
specifier|public
name|void
name|testLocalCacheStaysLocal
parameter_list|()
block|{
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|ClientMtTable1
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
name|BaseContext
name|child1
init|=
operator|(
name|BaseContext
operator|)
name|runtime
operator|.
name|newContext
argument_list|(
name|clientContext
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|child1
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|query
operator|.
name|getMetaData
argument_list|(
name|child1
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|clientContext
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|query
operator|.
name|getMetaData
argument_list|(
name|clientContext
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|results
init|=
name|child1
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|results
argument_list|,
name|child1
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|query
operator|.
name|getMetaData
argument_list|(
name|child1
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|clientContext
operator|.
name|getQueryCache
argument_list|()
operator|.
name|get
argument_list|(
name|query
operator|.
name|getMetaData
argument_list|(
name|clientContext
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
