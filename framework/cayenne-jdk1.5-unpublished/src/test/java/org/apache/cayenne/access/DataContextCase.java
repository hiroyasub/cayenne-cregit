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
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
name|art
operator|.
name|ROArtist
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
name|exp
operator|.
name|ExpressionFactory
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|DataContextCase
extends|extends
name|CayenneCase
block|{
specifier|public
specifier|static
specifier|final
name|int
name|artistCount
init|=
literal|25
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|galleryCount
init|=
literal|10
decl_stmt|;
specifier|protected
name|DataContext
name|context
decl_stmt|;
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
name|createTestData
argument_list|(
literal|"testArtists"
argument_list|)
expr_stmt|;
name|context
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|Painting
name|fetchPainting
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|prefetchArtist
parameter_list|)
block|{
name|SelectQuery
name|select
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"paintingTitle"
argument_list|,
name|name
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefetchArtist
condition|)
block|{
name|select
operator|.
name|addPrefetch
argument_list|(
literal|"toArtist"
argument_list|)
expr_stmt|;
block|}
name|List
name|ats
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select
argument_list|)
decl_stmt|;
return|return
operator|(
name|ats
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|)
condition|?
operator|(
name|Painting
operator|)
name|ats
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
literal|null
return|;
block|}
specifier|protected
name|Artist
name|fetchArtist
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|prefetchPaintings
parameter_list|)
block|{
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"artistName"
argument_list|,
name|name
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefetchPaintings
condition|)
block|{
name|q
operator|.
name|addPrefetch
argument_list|(
literal|"paintingArray"
argument_list|)
expr_stmt|;
block|}
name|List
name|ats
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
return|return
operator|(
name|ats
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|)
condition|?
operator|(
name|Artist
operator|)
name|ats
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
literal|null
return|;
block|}
specifier|protected
name|ROArtist
name|fetchROArtist
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|ROArtist
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"artistName"
argument_list|,
name|name
argument_list|)
argument_list|)
decl_stmt|;
name|List
name|ats
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
return|return
operator|(
name|ats
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|)
condition|?
operator|(
name|ROArtist
operator|)
name|ats
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**      * Temporary workaround for current inability to store dates in test       * fixture XML files.      */
specifier|public
name|void
name|populateExhibits
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|insertPaint
init|=
literal|"INSERT INTO EXHIBIT (EXHIBIT_ID, GALLERY_ID, OPENING_DATE, CLOSING_DATE) VALUES (?, ?, ?, ?)"
decl_stmt|;
name|Connection
name|conn
init|=
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|conn
operator|.
name|setAutoCommit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|PreparedStatement
name|stmt
init|=
name|conn
operator|.
name|prepareStatement
argument_list|(
name|insertPaint
argument_list|)
decl_stmt|;
name|Timestamp
name|now
init|=
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|2
condition|;
name|i
operator|++
control|)
block|{
name|stmt
operator|.
name|setInt
argument_list|(
literal|1
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|setInt
argument_list|(
literal|2
argument_list|,
literal|33000
operator|+
name|i
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|setTimestamp
argument_list|(
literal|3
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|setTimestamp
argument_list|(
literal|4
argument_list|,
name|now
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|executeUpdate
argument_list|()
expr_stmt|;
block|}
name|stmt
operator|.
name|close
argument_list|()
expr_stmt|;
name|conn
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|conn
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Helper method that takes one of the artists from the standard      * dataset (always the same one) and creates a new painting for this artist,      * committing it to the database. Both Painting and Artist will be cached in current      * DataContext.      */
specifier|protected
name|Painting
name|insertPaintingInContext
parameter_list|(
name|String
name|paintingName
parameter_list|)
block|{
name|Painting
name|painting
init|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Painting"
argument_list|)
decl_stmt|;
name|painting
operator|.
name|setPaintingTitle
argument_list|(
name|paintingName
argument_list|)
expr_stmt|;
name|painting
operator|.
name|setToArtist
argument_list|(
name|fetchArtist
argument_list|(
literal|"artist2"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
return|return
name|painting
return|;
block|}
block|}
end_class

end_unit

