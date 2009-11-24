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
name|query
operator|.
name|SQLTemplate
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
name|util
operator|.
name|ThreadedTestHelper
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
name|util
operator|.
name|Cayenne
import|;
end_import

begin_class
specifier|public
class|class
name|ObjectStoreGCTest
extends|extends
name|CayenneCase
block|{
annotation|@
name|Override
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
name|deleteTestData
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testReleaseUnreferenced
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"insert into ARTIST (ARTIST_ID, ARTIST_NAME) values (1, 'aa')"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
comment|// allow for slow GC
operator|new
name|ThreadedTestHelper
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|assertResult
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|assertWithTimeout
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRetainUnreferencedNew
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|a
operator|=
literal|null
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
comment|// allow for slow GC
operator|new
name|ThreadedTestHelper
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|assertResult
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|assertWithTimeout
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
operator|new
name|ThreadedTestHelper
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|assertResult
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|assertWithTimeout
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRetainUnreferencedModified
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
literal|"insert into ARTIST (ARTIST_ID, ARTIST_NAME) values (1, 'aa')"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a
init|=
name|Cayenne
operator|.
name|objectForPK
argument_list|(
name|context
argument_list|,
name|Artist
operator|.
name|class
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"Y"
argument_list|)
expr_stmt|;
name|a
operator|=
literal|null
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
operator|new
name|ThreadedTestHelper
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|assertResult
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|assertWithTimeout
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
operator|new
name|ThreadedTestHelper
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|assertResult
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|registeredObjectsCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|assertWithTimeout
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

