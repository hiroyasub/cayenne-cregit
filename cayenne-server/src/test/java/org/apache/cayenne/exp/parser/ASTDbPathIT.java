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
name|map
operator|.
name|DbAttribute
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
name|DbEntity
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
name|ObjEntity
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
name|ASTDbPathIT
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
name|a1
operator|.
name|setArtistName
argument_list|(
literal|"a1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Expression
name|idExp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"db:ARTIST_ID"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Cayenne
operator|.
name|longPKForObject
argument_list|(
name|a1
argument_list|)
argument_list|,
name|idExp
operator|.
name|evaluate
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
name|Expression
name|columnExp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"db:ARTIST_NAME"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"a1"
argument_list|,
name|columnExp
operator|.
name|evaluate
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluate_DbEntity
parameter_list|()
block|{
name|Expression
name|e
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"db:paintingArray.PAINTING_TITLE"
argument_list|)
decl_stmt|;
name|ObjEntity
name|ae
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|DbEntity
name|ade
init|=
name|ae
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|Object
name|objTarget
init|=
name|e
operator|.
name|evaluate
argument_list|(
name|ae
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|objTarget
operator|instanceof
name|DbAttribute
argument_list|)
expr_stmt|;
name|Object
name|dbTarget
init|=
name|e
operator|.
name|evaluate
argument_list|(
name|ade
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|dbTarget
operator|instanceof
name|DbAttribute
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluate_Related_DataObject
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
name|attributeOnlyPath
init|=
operator|new
name|ASTDbPath
argument_list|(
literal|"PAINTING_TITLE"
argument_list|)
decl_stmt|;
name|Expression
name|singleStepPath
init|=
operator|new
name|ASTDbPath
argument_list|(
literal|"toArtist.ARTIST_NAME"
argument_list|)
decl_stmt|;
name|Expression
name|multiStepPath
init|=
operator|new
name|ASTDbPath
argument_list|(
literal|"toArtist.paintingArray.PAINTING_TITLE"
argument_list|)
decl_stmt|;
comment|// attribute only path
name|assertEquals
argument_list|(
name|p1
operator|.
name|getPaintingTitle
argument_list|()
argument_list|,
name|attributeOnlyPath
operator|.
name|evaluate
argument_list|(
name|p1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|p2
operator|.
name|getPaintingTitle
argument_list|()
argument_list|,
name|attributeOnlyPath
operator|.
name|evaluate
argument_list|(
name|p2
argument_list|)
argument_list|)
expr_stmt|;
comment|// attribute only path - not in cache
name|p1
operator|.
name|getObjectContext
argument_list|()
operator|.
name|invalidateObjects
argument_list|(
name|p1
argument_list|,
name|p2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|p1
operator|.
name|getPaintingTitle
argument_list|()
argument_list|,
name|attributeOnlyPath
operator|.
name|evaluate
argument_list|(
name|p1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|p2
operator|.
name|getPaintingTitle
argument_list|()
argument_list|,
name|attributeOnlyPath
operator|.
name|evaluate
argument_list|(
name|p2
argument_list|)
argument_list|)
expr_stmt|;
comment|// single step relationship path
name|assertEquals
argument_list|(
name|a1
operator|.
name|getArtistName
argument_list|()
argument_list|,
name|singleStepPath
operator|.
name|evaluate
argument_list|(
name|p1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|a2
operator|.
name|getArtistName
argument_list|()
argument_list|,
name|singleStepPath
operator|.
name|evaluate
argument_list|(
name|p2
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|singleStepPath
operator|.
name|evaluate
argument_list|(
name|p3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|p1
operator|.
name|getPaintingTitle
argument_list|()
argument_list|,
name|multiStepPath
operator|.
name|evaluate
argument_list|(
name|p1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|p2
operator|.
name|getPaintingTitle
argument_list|()
argument_list|,
name|multiStepPath
operator|.
name|evaluate
argument_list|(
name|p2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

