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
name|tutorial
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|tutorial
operator|.
name|persistent
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
name|tutorial
operator|.
name|persistent
operator|.
name|Gallery
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
name|tutorial
operator|.
name|persistent
operator|.
name|Painting
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
comment|// starting Cayenne
name|ServerRuntime
name|cayenneRuntime
init|=
operator|new
name|ServerRuntime
argument_list|(
literal|"cayenne-project.xml"
argument_list|)
decl_stmt|;
comment|// getting a hold of ObjectContext
name|ObjectContext
name|context
init|=
name|cayenneRuntime
operator|.
name|getContext
argument_list|()
decl_stmt|;
name|newObjectsTutorial
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|selectTutorial
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|deleteTutorial
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
specifier|static
name|void
name|newObjectsTutorial
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
comment|// creating new Artist
name|Artist
name|picasso
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
comment|// Creating other objects
name|Gallery
name|metropolitan
init|=
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
comment|// connecting objects together via relationships
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
comment|// saving all the changes above
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
specifier|static
name|void
name|selectTutorial
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
comment|// SelectQuery examples
name|SelectQuery
argument_list|<
name|Painting
argument_list|>
name|select1
init|=
name|SelectQuery
operator|.
name|from
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Painting
argument_list|>
name|paintings1
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select1
argument_list|)
decl_stmt|;
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
argument_list|<
name|Painting
argument_list|>
name|select2
init|=
name|SelectQuery
operator|.
name|from
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|qualifier2
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Painting
argument_list|>
name|paintings2
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|select2
argument_list|)
decl_stmt|;
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
argument_list|<
name|Painting
argument_list|>
name|select3
init|=
name|SelectQuery
operator|.
name|from
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|qualifier3
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Painting
argument_list|>
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
name|deleteTutorial
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
comment|// Delete object examples
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
argument_list|<
name|Artist
argument_list|>
name|selectToDelete
init|=
name|SelectQuery
operator|.
name|from
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
name|Cayenne
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|selectToDelete
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
name|deleteObjects
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

