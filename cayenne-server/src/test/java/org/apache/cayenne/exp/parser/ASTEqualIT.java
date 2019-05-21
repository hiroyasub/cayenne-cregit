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
operator|.
name|parser
package|;
end_package

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
name|assertTrue
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
name|ASTEqualIT
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
name|Test
specifier|public
name|void
name|testEvaluate_DataObject
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"a2"
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setPaintingTitle
argument_list|(
literal|"p1"
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setPaintingTitle
argument_list|(
literal|"p2"
argument_list|)
expr_stmt|;
name|p3
operator|.
name|setPaintingTitle
argument_list|(
literal|"p3"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|p1
operator|.
name|setToArtist
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setToArtist
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|Expression
name|e
init|=
operator|new
name|ASTEqual
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"toArtist"
argument_list|)
argument_list|,
name|a1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|match
argument_list|(
name|p1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|e
operator|.
name|match
argument_list|(
name|p2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|e
operator|.
name|match
argument_list|(
name|p3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluate_TempId
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
name|p1
operator|.
name|setToArtist
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setToArtist
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|Expression
name|e
init|=
operator|new
name|ASTEqual
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"toArtist"
argument_list|)
argument_list|,
name|a1
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|match
argument_list|(
name|p1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|e
operator|.
name|match
argument_list|(
name|p2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|e
operator|.
name|match
argument_list|(
name|p3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluate_Id
parameter_list|()
throws|throws
name|Exception
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|a2
operator|.
name|setArtistName
argument_list|(
literal|"a2"
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setPaintingTitle
argument_list|(
literal|"p1"
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setPaintingTitle
argument_list|(
literal|"p2"
argument_list|)
expr_stmt|;
name|p3
operator|.
name|setPaintingTitle
argument_list|(
literal|"p3"
argument_list|)
expr_stmt|;
name|p1
operator|.
name|setToArtist
argument_list|(
name|a1
argument_list|)
expr_stmt|;
name|p2
operator|.
name|setToArtist
argument_list|(
name|a2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Expression
name|e
init|=
operator|new
name|ASTEqual
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"toArtist"
argument_list|)
argument_list|,
name|Cayenne
operator|.
name|longPKForObject
argument_list|(
name|a1
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|match
argument_list|(
name|p1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|e
operator|.
name|match
argument_list|(
name|p2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|e
operator|.
name|match
argument_list|(
name|p3
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

