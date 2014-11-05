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
name|Cayenne
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
name|PersistenceState
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
name|DeleteRule
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
name|ObjRelationship
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
name|Iterator
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
name|assertNull
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
name|DeleteObjectIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|protected
name|TableHelper
name|tArtist
decl_stmt|;
specifier|protected
name|TableHelper
name|tPainting
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
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createHollowDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"artist1"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"painting1"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createObjectDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"artist1"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createObjectsDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"artist1"
argument_list|)
expr_stmt|;
name|tArtist
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"artist2"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createObjectsRelationshipCollectionDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|tArtist
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"artist1"
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"painting1"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"painting2"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tPainting
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"painting3"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteObject
parameter_list|()
throws|throws
name|Exception
block|{
name|createObjectDataSet
argument_list|()
expr_stmt|;
name|Artist
name|artist
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
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|artist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObject
argument_list|(
name|artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|artist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|artist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|artist
operator|.
name|getObjectContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteObjects1
parameter_list|()
throws|throws
name|Exception
block|{
name|createObjectsDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
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
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Artist
name|object
range|:
name|artists
control|)
block|{
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|deleteObjects
argument_list|(
name|artists
argument_list|)
expr_stmt|;
for|for
control|(
name|Artist
name|object
range|:
name|artists
control|)
block|{
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Similar to testDeleteObjects2, but extract ObjectContext instead of
comment|// DataContext.
annotation|@
name|Test
specifier|public
name|void
name|testDeleteObjects2
parameter_list|()
throws|throws
name|Exception
block|{
name|createObjectsDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
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
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Artist
name|object
range|:
name|artists
control|)
block|{
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getObjectContext
argument_list|()
operator|.
name|deleteObjects
argument_list|(
name|artists
argument_list|)
expr_stmt|;
for|for
control|(
name|Artist
name|object
range|:
name|artists
control|)
block|{
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
for|for
control|(
name|Artist
name|object
range|:
name|artists
control|)
block|{
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteObjectsRelationshipCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|createObjectsRelationshipCollectionDataSet
argument_list|()
expr_stmt|;
name|Artist
name|artist
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
name|List
argument_list|<
name|Painting
argument_list|>
name|paintings
init|=
name|artist
operator|.
name|getPaintingArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|paintings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// create a clone to be able to inspect paintings after deletion
name|List
argument_list|<
name|Painting
argument_list|>
name|paintingsClone
init|=
operator|new
name|ArrayList
argument_list|<
name|Painting
argument_list|>
argument_list|(
name|paintings
argument_list|)
decl_stmt|;
for|for
control|(
name|Painting
name|object
range|:
name|paintingsClone
control|)
block|{
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|deleteObjects
argument_list|(
name|paintings
argument_list|)
expr_stmt|;
comment|// as Painting -> Artist has Nullify rule, relationship list has to be
comment|// cleaned up,
comment|// and no exceptions thrown on concurrent modification...
name|ObjRelationship
name|r
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
operator|.
name|getRelationship
argument_list|(
literal|"toArtist"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|DeleteRule
operator|.
name|NULLIFY
argument_list|,
name|r
operator|.
name|getDeleteRule
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|paintings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Painting
name|object
range|:
name|paintingsClone
control|)
block|{
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteObjectInIterator
parameter_list|()
throws|throws
name|Exception
block|{
name|createObjectsRelationshipCollectionDataSet
argument_list|()
expr_stmt|;
name|Artist
name|artist
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
name|List
argument_list|<
name|Painting
argument_list|>
name|paintings
init|=
name|artist
operator|.
name|getPaintingArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|paintings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// create a clone to be able to inspect paintings after deletion
name|List
argument_list|<
name|Painting
argument_list|>
name|paintingsClone
init|=
operator|new
name|ArrayList
argument_list|<
name|Painting
argument_list|>
argument_list|(
name|paintings
argument_list|)
decl_stmt|;
for|for
control|(
name|Painting
name|object
range|:
name|paintingsClone
control|)
block|{
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|Painting
argument_list|>
name|deleteIt
init|=
name|paintings
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|deleteIt
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Painting
name|object
init|=
name|deleteIt
operator|.
name|next
argument_list|()
decl_stmt|;
name|deleteIt
operator|.
name|remove
argument_list|()
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
comment|// relationship list has to be cleaned up,
comment|// and no exceptions thrown on concurrent modification...
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|paintings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Painting
name|object
range|:
name|paintingsClone
control|)
block|{
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|object
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteHollow
parameter_list|()
throws|throws
name|Exception
block|{
name|createHollowDataSet
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Painting
argument_list|>
name|paintings
init|=
name|context
operator|.
name|performQuery
argument_list|(
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|Painting
name|p
init|=
name|paintings
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Artist
name|a
init|=
name|p
operator|.
name|getToArtist
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|,
name|a
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|DELETED
argument_list|,
name|a
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDeleteNew
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|artist
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
name|artist
operator|.
name|setArtistName
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|NEW
argument_list|,
name|artist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|deleteObjects
argument_list|(
name|artist
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|artist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|rollbackChanges
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|,
name|artist
operator|.
name|getPersistenceState
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

