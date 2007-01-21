begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|cayenne
operator|.
name|tutorial
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
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
name|GregorianCalendar
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
name|cayenne
operator|.
name|CayenneContext
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
name|DataChannel
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
name|DataObjectUtils
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
name|exp
operator|.
name|Expression
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
name|NamedQuery
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
name|QueryChain
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
name|remote
operator|.
name|ClientChannel
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
name|remote
operator|.
name|ClientConnection
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
name|remote
operator|.
name|hessian
operator|.
name|HessianConnection
import|;
end_import

begin_class
specifier|public
class|class
name|Main
block|{
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|ClientConnection
name|connection
init|=
operator|new
name|HessianConnection
argument_list|(
literal|"http://localhost:8080/cayenne-rop-server-tutorial/cayenne-service"
argument_list|,
literal|"cayenne-user"
argument_list|,
literal|"secret"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|DataChannel
name|channel
init|=
operator|new
name|ClientChannel
argument_list|(
name|connection
argument_list|)
decl_stmt|;
name|ObjectContext
name|context
init|=
operator|new
name|CayenneContext
argument_list|(
name|channel
argument_list|)
decl_stmt|;
comment|// cleans up all data, so that we start with empty database on each
comment|// tutorial run
name|mappingQueriesChapter
argument_list|(
name|context
argument_list|)
expr_stmt|;
comment|// persists an artist, a gallery and a few paintings
name|dataObjectsChapter
argument_list|(
name|context
argument_list|)
expr_stmt|;
comment|// selects previously saved data
name|selectQueryChapter
argument_list|(
name|context
argument_list|)
expr_stmt|;
comment|// deletes objects
name|deleteChapter
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
specifier|static
name|void
name|dataObjectsChapter
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
name|Artist
name|picasso
init|=
operator|(
name|Artist
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|picasso
operator|.
name|setName
argument_list|(
literal|"Pablo Picasso"
argument_list|)
expr_stmt|;
name|picasso
operator|.
name|setDateOfBirthString
argument_list|(
literal|"18811025"
argument_list|)
expr_stmt|;
name|Gallery
name|metropolitan
init|=
operator|(
name|Gallery
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Gallery
operator|.
name|class
argument_list|)
decl_stmt|;
name|metropolitan
operator|.
name|setName
argument_list|(
literal|"Metropolitan Museum of Art"
argument_list|)
expr_stmt|;
name|Painting
name|girl
init|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|girl
operator|.
name|setName
argument_list|(
literal|"Girl Reading at a Table"
argument_list|)
expr_stmt|;
name|Painting
name|stein
init|=
operator|(
name|Painting
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|stein
operator|.
name|setName
argument_list|(
literal|"Gertrude Stein"
argument_list|)
expr_stmt|;
name|picasso
operator|.
name|addToPaintings
argument_list|(
name|girl
argument_list|)
expr_stmt|;
name|picasso
operator|.
name|addToPaintings
argument_list|(
name|stein
argument_list|)
expr_stmt|;
name|girl
operator|.
name|setGallery
argument_list|(
name|metropolitan
argument_list|)
expr_stmt|;
name|stein
operator|.
name|setGallery
argument_list|(
name|metropolitan
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|static
name|void
name|mappingQueriesChapter
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
name|QueryChain
name|chain
init|=
operator|new
name|QueryChain
argument_list|()
decl_stmt|;
name|chain
operator|.
name|addQuery
argument_list|(
operator|new
name|NamedQuery
argument_list|(
literal|"DeleteAll"
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"table"
argument_list|,
literal|"PAINTING"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|chain
operator|.
name|addQuery
argument_list|(
operator|new
name|NamedQuery
argument_list|(
literal|"DeleteAll"
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"table"
argument_list|,
literal|"ARTIST"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|chain
operator|.
name|addQuery
argument_list|(
operator|new
name|NamedQuery
argument_list|(
literal|"DeleteAll"
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"table"
argument_list|,
literal|"GALLERY"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|performGenericQuery
argument_list|(
name|chain
argument_list|)
expr_stmt|;
block|}
specifier|static
name|void
name|selectQueryChapter
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
comment|// select all paintings
name|SelectQuery
name|select1
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
name|paintings1
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select1
argument_list|)
decl_stmt|;
comment|// select paintings that start with "Gi*"
name|Expression
name|qualifier2
init|=
name|ExpressionFactory
operator|.
name|likeIgnoreCaseExp
argument_list|(
name|Painting
operator|.
name|NAME_PROPERTY
argument_list|,
literal|"gi%"
argument_list|)
decl_stmt|;
name|SelectQuery
name|select2
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|qualifier2
argument_list|)
decl_stmt|;
name|List
name|paintings2
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select2
argument_list|)
decl_stmt|;
comment|// select all paintings done by artists who were born more than a 100
comment|// years ago
name|Calendar
name|c
init|=
operator|new
name|GregorianCalendar
argument_list|()
decl_stmt|;
name|c
operator|.
name|set
argument_list|(
name|c
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|YEAR
argument_list|)
operator|-
literal|100
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|Expression
name|qualifier3
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"artist.dateOfBirth< $date"
argument_list|)
decl_stmt|;
name|qualifier3
operator|=
name|qualifier3
operator|.
name|expWithParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"date"
argument_list|,
name|c
operator|.
name|getTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|SelectQuery
name|select3
init|=
operator|new
name|SelectQuery
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|qualifier3
argument_list|)
decl_stmt|;
name|List
name|paintings3
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select3
argument_list|)
decl_stmt|;
block|}
specifier|static
name|void
name|deleteChapter
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
name|Expression
name|qualifier
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Artist
operator|.
name|NAME_PROPERTY
argument_list|,
literal|"Pablo Picasso"
argument_list|)
decl_stmt|;
name|SelectQuery
name|select
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|qualifier
argument_list|)
decl_stmt|;
name|Artist
name|picasso
init|=
operator|(
name|Artist
operator|)
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|select
argument_list|)
decl_stmt|;
if|if
condition|(
name|picasso
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|deleteObject
argument_list|(
name|picasso
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

