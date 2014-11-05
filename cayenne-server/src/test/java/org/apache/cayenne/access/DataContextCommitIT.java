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
name|graph
operator|.
name|GraphDiff
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
name|graph
operator|.
name|MockGraphChangeHandler
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
name|NullTestEntity
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
name|assertSame
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
name|DataContextCommitIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PAINTING_INFO"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST_EXHIBIT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST_GROUP"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"NULL_TEST"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFlushToParent_Commit
parameter_list|()
block|{
comment|// commit new object
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
literal|"Test"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|GraphDiff
name|diff
init|=
name|context
operator|.
name|flushToParent
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|diff
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Object
index|[]
name|newIds
init|=
operator|new
name|Object
index|[
literal|1
index|]
decl_stmt|;
name|MockGraphChangeHandler
name|diffChecker
init|=
operator|new
name|MockGraphChangeHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|nodeIdChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|newId
parameter_list|)
block|{
name|super
operator|.
name|nodeIdChanged
argument_list|(
name|nodeId
argument_list|,
name|newId
argument_list|)
expr_stmt|;
name|newIds
index|[
literal|0
index|]
operator|=
name|newId
expr_stmt|;
block|}
block|}
decl_stmt|;
name|diff
operator|.
name|apply
argument_list|(
name|diffChecker
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|diffChecker
operator|.
name|getCallbackCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|newIds
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
comment|// commit a mix of new and modified
name|Painting
name|p
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p
operator|.
name|setPaintingTitle
argument_list|(
literal|"PT"
argument_list|)
expr_stmt|;
name|p
operator|.
name|setToArtist
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
name|a
operator|.
name|getArtistName
argument_list|()
operator|+
literal|"_"
argument_list|)
expr_stmt|;
name|GraphDiff
name|diff2
init|=
name|context
operator|.
name|flushToParent
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|diff2
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Object
index|[]
name|newIds2
init|=
operator|new
name|Object
index|[
literal|1
index|]
decl_stmt|;
name|MockGraphChangeHandler
name|diffChecker2
init|=
operator|new
name|MockGraphChangeHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|nodeIdChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|newId
parameter_list|)
block|{
name|super
operator|.
name|nodeIdChanged
argument_list|(
name|nodeId
argument_list|,
name|newId
argument_list|)
expr_stmt|;
name|newIds2
index|[
literal|0
index|]
operator|=
name|newId
expr_stmt|;
block|}
block|}
decl_stmt|;
name|diff2
operator|.
name|apply
argument_list|(
name|diffChecker2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|diffChecker2
operator|.
name|getCallbackCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|p
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|newIds2
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
comment|// commit new object with uninitialized attributes
name|context
operator|.
name|newObject
argument_list|(
name|NullTestEntity
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
name|GraphDiff
name|diff3
init|=
name|context
operator|.
name|flushToParent
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|diff3
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

