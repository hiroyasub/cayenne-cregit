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
name|exp
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
name|CayenneRuntimeException
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
name|access
operator|.
name|DataContext
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
name|query
operator|.
name|ObjectSelect
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
name|UnitDbAdapter
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
name|assertNotSame
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
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|ExpressionIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|adapter
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testMatch
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|context
operator|instanceof
name|DataContext
argument_list|)
expr_stmt|;
name|DataContext
name|context2
init|=
operator|(
name|DataContext
operator|)
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"Equals"
argument_list|)
expr_stmt|;
name|Painting
name|p1
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
name|p1
operator|.
name|setToArtist
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setPaintingTitle
argument_list|(
literal|"painting1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|assertNotSame
argument_list|(
name|context2
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Painting
argument_list|>
name|objects
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
name|Painting
operator|.
name|TO_ARTIST
operator|.
name|eq
argument_list|(
name|a1
argument_list|)
argument_list|)
operator|.
name|select
argument_list|(
name|context2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// 2 same objects in different contexts
name|assertTrue
argument_list|(
name|Painting
operator|.
name|TO_ARTIST
operator|.
name|eq
argument_list|(
name|a1
argument_list|)
operator|.
name|match
argument_list|(
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// we change one object - so the objects are different now
comment|// (PersistenceState different)
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"newName"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Painting
operator|.
name|TO_ARTIST
operator|.
name|eq
argument_list|(
name|a1
argument_list|)
operator|.
name|match
argument_list|(
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Artist
name|a2
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
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"Equals"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// 2 different objects in different contexts
name|assertFalse
argument_list|(
name|Painting
operator|.
name|TO_ARTIST
operator|.
name|eq
argument_list|(
name|a2
argument_list|)
operator|.
name|match
argument_list|(
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFirst
parameter_list|()
block|{
name|List
argument_list|<
name|Painting
argument_list|>
name|paintingList
init|=
operator|new
name|ArrayList
argument_list|<
name|Painting
argument_list|>
argument_list|()
decl_stmt|;
name|Painting
name|p1
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
name|p1
operator|.
name|setPaintingTitle
argument_list|(
literal|"x1"
argument_list|)
expr_stmt|;
name|paintingList
operator|.
name|add
argument_list|(
name|p1
argument_list|)
expr_stmt|;
name|Painting
name|p2
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
name|p2
operator|.
name|setPaintingTitle
argument_list|(
literal|"x2"
argument_list|)
expr_stmt|;
name|paintingList
operator|.
name|add
argument_list|(
name|p2
argument_list|)
expr_stmt|;
name|Painting
name|p3
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
name|p3
operator|.
name|setPaintingTitle
argument_list|(
literal|"x3"
argument_list|)
expr_stmt|;
name|paintingList
operator|.
name|add
argument_list|(
name|p3
argument_list|)
expr_stmt|;
name|Expression
name|e1
init|=
name|ExpressionFactory
operator|.
name|likeExp
argument_list|(
literal|"paintingTitle"
argument_list|,
literal|"x%"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|p1
argument_list|,
name|e1
operator|.
name|first
argument_list|(
name|paintingList
argument_list|)
argument_list|)
expr_stmt|;
name|Expression
name|e3
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"paintingTitle"
argument_list|,
literal|"x3"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|p3
argument_list|,
name|e3
operator|.
name|first
argument_list|(
name|paintingList
argument_list|)
argument_list|)
expr_stmt|;
name|Expression
name|e4
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"paintingTitle"
argument_list|,
literal|"x4"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|e4
operator|.
name|first
argument_list|(
name|paintingList
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLessThanNull
parameter_list|()
block|{
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"Picasso"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
decl_stmt|;
try|try
block|{
name|artists
operator|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|lt
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|)
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|adapter
operator|.
name|supportsNullComparison
argument_list|()
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
else|else
block|{
return|return;
block|}
block|}
name|assertTrue
argument_list|(
literal|"Less than 'NULL' never matches anything"
argument_list|,
name|artists
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInNull
parameter_list|()
block|{
name|Artist
name|a1
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"Picasso"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
decl_stmt|;
try|try
block|{
name|artists
operator|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|in
argument_list|(
literal|"Picasso"
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|adapter
operator|.
name|supportsNullComparison
argument_list|()
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
else|else
block|{
return|return;
block|}
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

