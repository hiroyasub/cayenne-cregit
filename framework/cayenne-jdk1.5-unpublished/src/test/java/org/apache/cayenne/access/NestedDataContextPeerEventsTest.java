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
name|art
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
name|ObjectContext
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
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_class
specifier|public
class|class
name|NestedDataContextPeerEventsTest
extends|extends
name|CayenneCase
block|{
specifier|public
name|void
name|testPeerObjectUpdatedTempOID
parameter_list|()
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|ObjectContext
name|peer1
init|=
name|context
operator|.
name|createChildObjectContext
argument_list|()
decl_stmt|;
name|Artist
name|a1
init|=
name|peer1
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"Y"
argument_list|)
expr_stmt|;
name|ObjectId
name|a1TempId
init|=
name|a1
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|ObjectContext
name|peer2
init|=
name|context
operator|.
name|createChildObjectContext
argument_list|()
decl_stmt|;
name|Artist
name|a2
init|=
operator|(
name|Artist
operator|)
name|peer2
operator|.
name|localObject
argument_list|(
name|a1TempId
argument_list|,
name|a1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|a1TempId
argument_list|,
name|a2
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|peer1
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|a1
operator|.
name|getObjectId
argument_list|()
operator|.
name|isTemporary
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|a2
operator|.
name|getObjectId
argument_list|()
operator|.
name|isTemporary
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a2
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|a1
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPeerObjectUpdatedSimpleProperty
parameter_list|()
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
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
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|peer1
init|=
name|context
operator|.
name|createChildObjectContext
argument_list|()
decl_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
operator|)
name|peer1
operator|.
name|localObject
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|a
argument_list|)
decl_stmt|;
name|ObjectContext
name|peer2
init|=
name|context
operator|.
name|createChildObjectContext
argument_list|()
decl_stmt|;
name|Artist
name|a2
init|=
operator|(
name|Artist
operator|)
name|peer2
operator|.
name|localObject
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|a
argument_list|)
decl_stmt|;
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"Y"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"X"
argument_list|,
name|a2
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|peer1
operator|.
name|commitChangesToParent
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Y"
argument_list|,
name|a2
operator|.
name|getArtistName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Peer data context became dirty on event processing"
argument_list|,
name|peer2
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPeerObjectUpdatedToOneRelationship
parameter_list|()
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
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
name|Artist
name|altA
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
name|setToArtist
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|p
operator|.
name|setPaintingTitle
argument_list|(
literal|"PPP"
argument_list|)
expr_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"X"
argument_list|)
expr_stmt|;
name|altA
operator|.
name|setArtistName
argument_list|(
literal|"Y"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|peer1
init|=
name|context
operator|.
name|createChildObjectContext
argument_list|()
decl_stmt|;
name|Painting
name|p1
init|=
operator|(
name|Painting
operator|)
name|peer1
operator|.
name|localObject
argument_list|(
name|p
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|p
argument_list|)
decl_stmt|;
name|Artist
name|altA1
init|=
operator|(
name|Artist
operator|)
name|peer1
operator|.
name|localObject
argument_list|(
name|altA
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|altA
argument_list|)
decl_stmt|;
name|ObjectContext
name|peer2
init|=
name|context
operator|.
name|createChildObjectContext
argument_list|()
decl_stmt|;
name|Painting
name|p2
init|=
operator|(
name|Painting
operator|)
name|peer2
operator|.
name|localObject
argument_list|(
name|p
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|p
argument_list|)
decl_stmt|;
name|Artist
name|altA2
init|=
operator|(
name|Artist
operator|)
name|peer2
operator|.
name|localObject
argument_list|(
name|altA
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|altA
argument_list|)
decl_stmt|;
name|Artist
name|a2
init|=
operator|(
name|Artist
operator|)
name|peer2
operator|.
name|localObject
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|a
argument_list|)
decl_stmt|;
name|p1
operator|.
name|setToArtist
argument_list|(
name|altA1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|a2
argument_list|,
name|p2
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|peer1
operator|.
name|commitChangesToParent
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|altA2
argument_list|,
name|p2
operator|.
name|getToArtist
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Peer data context became dirty on event processing"
argument_list|,
name|peer2
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPeerObjectUpdatedToManyRelationship
parameter_list|()
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
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
name|Painting
name|px
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
name|px
operator|.
name|setToArtist
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|px
operator|.
name|setPaintingTitle
argument_list|(
literal|"PX"
argument_list|)
expr_stmt|;
name|Painting
name|py
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
name|py
operator|.
name|setPaintingTitle
argument_list|(
literal|"PY"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectContext
name|peer1
init|=
name|context
operator|.
name|createChildObjectContext
argument_list|()
decl_stmt|;
name|Painting
name|py1
init|=
operator|(
name|Painting
operator|)
name|peer1
operator|.
name|localObject
argument_list|(
name|py
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|py
argument_list|)
decl_stmt|;
name|Artist
name|a1
init|=
operator|(
name|Artist
operator|)
name|peer1
operator|.
name|localObject
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|a
argument_list|)
decl_stmt|;
name|ObjectContext
name|peer2
init|=
name|context
operator|.
name|createChildObjectContext
argument_list|()
decl_stmt|;
name|Painting
name|py2
init|=
operator|(
name|Painting
operator|)
name|peer2
operator|.
name|localObject
argument_list|(
name|py
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|py
argument_list|)
decl_stmt|;
name|Artist
name|a2
init|=
operator|(
name|Artist
operator|)
name|peer2
operator|.
name|localObject
argument_list|(
name|a
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|a
argument_list|)
decl_stmt|;
name|a1
operator|.
name|addToPaintingArray
argument_list|(
name|py1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|a2
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|a2
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|contains
argument_list|(
name|py2
argument_list|)
argument_list|)
expr_stmt|;
name|peer1
operator|.
name|commitChangesToParent
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|a2
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|a2
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|contains
argument_list|(
name|py2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Peer data context became dirty on event processing"
argument_list|,
name|peer2
operator|.
name|hasChanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

