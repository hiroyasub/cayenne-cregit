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
operator|.
name|persistent
operator|.
name|client
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
name|rop
operator|.
name|client
operator|.
name|ClientConstants
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
name|rop
operator|.
name|client
operator|.
name|ClientRuntime
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
name|ObjectSelect
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
name|List
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|ClientConstants
operator|.
name|ROP_SERVICE_URL_PROPERTY
argument_list|,
literal|"http://localhost:8080/tutorial-rop-server/cayenne-service"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|ClientConstants
operator|.
name|ROP_SERVICE_USERNAME_PROPERTY
argument_list|,
literal|"cayenne-user"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|ClientConstants
operator|.
name|ROP_SERVICE_PASSWORD_PROPERTY
argument_list|,
literal|"secret"
argument_list|)
expr_stmt|;
name|ClientRuntime
name|runtime
init|=
name|ClientRuntime
operator|.
name|builder
argument_list|()
operator|.
name|properties
argument_list|(
name|properties
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
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
name|runtime
operator|.
name|shutdown
argument_list|()
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
comment|// ObjectSelect examples
name|List
argument_list|<
name|Painting
argument_list|>
name|paintings1
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Painting
argument_list|>
name|paintings2
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|Painting
operator|.
name|NAME
operator|.
name|likeIgnoreCase
argument_list|(
literal|"gi%"
argument_list|)
argument_list|)
operator|.
name|select
argument_list|(
name|context
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
comment|// Delete object example
name|Artist
name|picasso
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|where
argument_list|(
name|Artist
operator|.
name|NAME
operator|.
name|eq
argument_list|(
literal|"Pablo Picasso"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
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

