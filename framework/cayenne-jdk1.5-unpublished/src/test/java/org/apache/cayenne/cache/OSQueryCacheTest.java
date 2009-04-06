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
name|cache
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
name|java
operator|.
name|util
operator|.
name|Properties
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
name|cache
operator|.
name|OSQueryCache
operator|.
name|RefreshSpecification
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
name|com
operator|.
name|opensymphony
operator|.
name|oscache
operator|.
name|base
operator|.
name|CacheEntry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|oscache
operator|.
name|general
operator|.
name|GeneralCacheAdministrator
import|;
end_import

begin_class
specifier|public
class|class
name|OSQueryCacheTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testDefaults
parameter_list|()
block|{
name|OSQueryCache
name|cache
init|=
operator|new
name|OSQueryCache
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|cache
operator|.
name|refreshSpecifications
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|cache
operator|.
name|defaultRefreshSpecification
operator|.
name|cronExpression
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CacheEntry
operator|.
name|INDEFINITE_EXPIRY
argument_list|,
name|cache
operator|.
name|defaultRefreshSpecification
operator|.
name|refreshPeriod
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDefaultOverrides
parameter_list|()
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|OSQueryCache
operator|.
name|DEFAULT_REFRESH_KEY
argument_list|,
literal|"15"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|OSQueryCache
operator|.
name|DEFAULT_CRON_KEY
argument_list|,
literal|"9 * * * * *"
argument_list|)
expr_stmt|;
name|OSQueryCache
name|cache
init|=
operator|new
name|OSQueryCache
argument_list|(
operator|new
name|GeneralCacheAdministrator
argument_list|()
argument_list|,
name|props
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|cache
operator|.
name|refreshSpecifications
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"9 * * * * *"
argument_list|,
name|cache
operator|.
name|defaultRefreshSpecification
operator|.
name|cronExpression
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|15
argument_list|,
name|cache
operator|.
name|defaultRefreshSpecification
operator|.
name|refreshPeriod
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testQueryOverrides
parameter_list|()
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|OSQueryCache
operator|.
name|GROUP_PREFIX
operator|+
literal|"ABC"
operator|+
name|OSQueryCache
operator|.
name|REFRESH_SUFFIX
argument_list|,
literal|"25"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|OSQueryCache
operator|.
name|GROUP_PREFIX
operator|+
literal|"ABC"
operator|+
name|OSQueryCache
operator|.
name|CRON_SUFFIX
argument_list|,
literal|"12 * * * * *"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|OSQueryCache
operator|.
name|GROUP_PREFIX
operator|+
literal|"XYZ"
operator|+
name|OSQueryCache
operator|.
name|REFRESH_SUFFIX
argument_list|,
literal|"35"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|OSQueryCache
operator|.
name|GROUP_PREFIX
operator|+
literal|"XYZ"
operator|+
name|OSQueryCache
operator|.
name|CRON_SUFFIX
argument_list|,
literal|"24 * * * * *"
argument_list|)
expr_stmt|;
name|OSQueryCache
name|cache
init|=
operator|new
name|OSQueryCache
argument_list|(
operator|new
name|GeneralCacheAdministrator
argument_list|()
argument_list|,
name|props
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cache
operator|.
name|refreshSpecifications
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cache
operator|.
name|refreshSpecifications
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|RefreshSpecification
name|abc
init|=
name|cache
operator|.
name|refreshSpecifications
operator|.
name|get
argument_list|(
literal|"ABC"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|abc
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"12 * * * * *"
argument_list|,
name|abc
operator|.
name|cronExpression
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|25
argument_list|,
name|abc
operator|.
name|refreshPeriod
argument_list|)
expr_stmt|;
name|RefreshSpecification
name|xyz
init|=
name|cache
operator|.
name|refreshSpecifications
operator|.
name|get
argument_list|(
literal|"XYZ"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|xyz
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"24 * * * * *"
argument_list|,
name|xyz
operator|.
name|cronExpression
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|35
argument_list|,
name|xyz
operator|.
name|refreshPeriod
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGroupNames
parameter_list|()
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|OSQueryCache
name|c1
init|=
operator|new
name|OSQueryCache
argument_list|(
operator|new
name|GeneralCacheAdministrator
argument_list|()
argument_list|,
name|props
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|c1
operator|.
name|getGroupNames
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|OSQueryCache
operator|.
name|GROUP_PREFIX
operator|+
literal|"ABC"
operator|+
name|OSQueryCache
operator|.
name|REFRESH_SUFFIX
argument_list|,
literal|"25"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|OSQueryCache
operator|.
name|GROUP_PREFIX
operator|+
literal|"XYZ"
operator|+
name|OSQueryCache
operator|.
name|CRON_SUFFIX
argument_list|,
literal|"24 * * * * *"
argument_list|)
expr_stmt|;
name|OSQueryCache
name|c2
init|=
operator|new
name|OSQueryCache
argument_list|(
operator|new
name|GeneralCacheAdministrator
argument_list|()
argument_list|,
name|props
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|c2
operator|.
name|getGroupNames
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|c2
operator|.
name|getGroupNames
argument_list|()
operator|.
name|contains
argument_list|(
literal|"ABC"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|c2
operator|.
name|getGroupNames
argument_list|()
operator|.
name|contains
argument_list|(
literal|"XYZ"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSize
parameter_list|()
block|{
name|OSQueryCache
name|cache
init|=
operator|new
name|OSQueryCache
argument_list|()
decl_stmt|;
name|List
name|r1
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|QueryMetadata
name|m1
init|=
operator|new
name|MockQueryMetadata
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getCacheKey
parameter_list|()
block|{
return|return
literal|"a"
return|;
block|}
block|}
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|m1
argument_list|,
name|r1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|r2
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|QueryMetadata
name|m2
init|=
operator|new
name|MockQueryMetadata
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getCacheKey
parameter_list|()
block|{
return|return
literal|"b"
return|;
block|}
block|}
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|m2
argument_list|,
name|r2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

