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
name|event
operator|.
name|EventManager
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
name|DataRowStoreTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testDefaultConstructor
parameter_list|()
block|{
name|DataRowStore
name|cache
init|=
operator|new
name|DataRowStore
argument_list|(
literal|"cacheXYZ"
argument_list|,
name|Collections
operator|.
name|EMPTY_MAP
argument_list|,
operator|new
name|EventManager
argument_list|()
argument_list|)
decl_stmt|;
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
name|indexOf
argument_list|(
literal|"cacheXYZ"
argument_list|)
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DataRowStore
operator|.
name|REMOTE_NOTIFICATION_DEFAULT
argument_list|,
name|cache
operator|.
name|isNotifyingRemoteListeners
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConstructorWithProperties
parameter_list|()
block|{
name|Map
name|props
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|DataRowStore
operator|.
name|REMOTE_NOTIFICATION_PROPERTY
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
operator|!
name|DataRowStore
operator|.
name|REMOTE_NOTIFICATION_DEFAULT
argument_list|)
argument_list|)
expr_stmt|;
name|DataRowStore
name|cache
init|=
operator|new
name|DataRowStore
argument_list|(
literal|"cacheXYZ"
argument_list|,
name|props
argument_list|,
operator|new
name|EventManager
argument_list|()
argument_list|)
decl_stmt|;
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
name|assertEquals
argument_list|(
operator|!
name|DataRowStore
operator|.
name|REMOTE_NOTIFICATION_DEFAULT
argument_list|,
name|cache
operator|.
name|isNotifyingRemoteListeners
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNotifyingRemoteListeners
parameter_list|()
block|{
name|DataRowStore
name|cache
init|=
operator|new
name|DataRowStore
argument_list|(
literal|"cacheXYZ"
argument_list|,
name|Collections
operator|.
name|EMPTY_MAP
argument_list|,
operator|new
name|EventManager
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|DataRowStore
operator|.
name|REMOTE_NOTIFICATION_DEFAULT
argument_list|,
name|cache
operator|.
name|isNotifyingRemoteListeners
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|setNotifyingRemoteListeners
argument_list|(
operator|!
name|DataRowStore
operator|.
name|REMOTE_NOTIFICATION_DEFAULT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|!
name|DataRowStore
operator|.
name|REMOTE_NOTIFICATION_DEFAULT
argument_list|,
name|cache
operator|.
name|isNotifyingRemoteListeners
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests LRU cache behavior.      */
specifier|public
name|void
name|testMaxSize
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
name|props
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|DataRowStore
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
name|DataRowStore
name|cache
init|=
operator|new
name|DataRowStore
argument_list|(
literal|"cacheXYZ"
argument_list|,
name|props
argument_list|,
operator|new
name|EventManager
argument_list|()
argument_list|)
decl_stmt|;
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
operator|new
name|ObjectId
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
name|diff1
init|=
operator|new
name|HashMap
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
operator|new
name|ObjectId
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
name|diff2
init|=
operator|new
name|HashMap
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
operator|new
name|ObjectId
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
name|diff3
init|=
operator|new
name|HashMap
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
name|EMPTY_LIST
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
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
name|EMPTY_LIST
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
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
name|EMPTY_LIST
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
argument_list|,
name|Collections
operator|.
name|EMPTY_LIST
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

