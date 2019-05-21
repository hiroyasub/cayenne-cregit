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
name|ObjectId
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
name|Constants
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
name|DefaultRuntimeProperties
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
name|After
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
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_comment
comment|/**  * We pass null as EventManager parameter, as having it not null will start  * really heavy EventBridge (JavaGroupsBridge implementation) inside DataRowStore  * and this behaviour is not anyhow tested here nor it affects existing tests.  */
end_comment

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
name|DataRowStoreIT
extends|extends
name|ServerCase
block|{
specifier|private
name|DataRowStore
name|cache
decl_stmt|;
annotation|@
name|After
specifier|public
name|void
name|cleanDataStore
parameter_list|()
block|{
if|if
condition|(
name|cache
operator|!=
literal|null
condition|)
block|{
name|cache
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|cache
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefaultConstructor
parameter_list|()
block|{
name|cache
operator|=
operator|new
name|DataRowStore
argument_list|(
literal|"cacheXYZ"
argument_list|,
operator|new
name|DefaultRuntimeProperties
argument_list|(
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|String
operator|>
name|emptyMap
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"cacheXYZ"
argument_list|,
name|cache
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cache
operator|.
name|getSnapshotEventSubject
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cache
operator|.
name|getSnapshotEventSubject
argument_list|()
operator|.
name|getSubjectName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"cacheXYZ"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests LRU cache behavior.      */
annotation|@
name|Test
specifier|public
name|void
name|testMaxSize
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|SNAPSHOT_CACHE_SIZE_PROPERTY
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|cache
operator|=
operator|new
name|DataRowStore
argument_list|(
literal|"cacheXYZ"
argument_list|,
operator|new
name|DefaultRuntimeProperties
argument_list|(
name|props
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cache
operator|.
name|maximumSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectId
name|key1
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|DataRow
argument_list|>
name|diff1
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|diff1
operator|.
name|put
argument_list|(
name|key1
argument_list|,
operator|new
name|DataRow
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|ObjectId
name|key2
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|DataRow
argument_list|>
name|diff2
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|diff2
operator|.
name|put
argument_list|(
name|key2
argument_list|,
operator|new
name|DataRow
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|ObjectId
name|key3
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"Artist"
argument_list|,
name|Artist
operator|.
name|ARTIST_ID_PK_COLUMN
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|DataRow
argument_list|>
name|diff3
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|diff3
operator|.
name|put
argument_list|(
name|key3
argument_list|,
operator|new
name|DataRow
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|cache
operator|.
name|processSnapshotChanges
argument_list|(
name|this
argument_list|,
name|diff1
argument_list|,
name|Collections
operator|.
expr|<
name|ObjectId
operator|>
name|emptyList
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|ObjectId
operator|>
name|emptyList
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|ObjectId
operator|>
name|emptyList
argument_list|()
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
name|cache
operator|.
name|processSnapshotChanges
argument_list|(
name|this
argument_list|,
name|diff2
argument_list|,
name|Collections
operator|.
expr|<
name|ObjectId
operator|>
name|emptyList
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|ObjectId
operator|>
name|emptyList
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|ObjectId
operator|>
name|emptyList
argument_list|()
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
comment|// this addition must overflow the cache, and throw out the first item
name|cache
operator|.
name|processSnapshotChanges
argument_list|(
name|this
argument_list|,
name|diff3
argument_list|,
name|Collections
operator|.
expr|<
name|ObjectId
operator|>
name|emptyList
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|ObjectId
operator|>
name|emptyList
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|ObjectId
operator|>
name|emptyList
argument_list|()
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
name|assertNotNull
argument_list|(
name|cache
operator|.
name|getCachedSnapshot
argument_list|(
name|key2
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|cache
operator|.
name|getCachedSnapshot
argument_list|(
name|key3
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|cache
operator|.
name|getCachedSnapshot
argument_list|(
name|key1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

