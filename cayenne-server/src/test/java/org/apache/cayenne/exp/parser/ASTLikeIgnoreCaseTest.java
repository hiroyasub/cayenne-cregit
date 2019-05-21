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
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|ASTLikeIgnoreCaseTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testToEJBQL
parameter_list|()
block|{
name|Expression
name|like
init|=
operator|new
name|ASTLikeIgnoreCase
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"a"
argument_list|)
argument_list|,
literal|"%b%"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|like
operator|.
name|toEJBQL
argument_list|(
literal|"p"
argument_list|)
argument_list|,
literal|"upper(p.a) like '%B%'"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluate
parameter_list|()
block|{
name|Expression
name|like
init|=
operator|new
name|ASTLikeIgnoreCase
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"artistName"
argument_list|)
argument_list|,
literal|"aBcD"
argument_list|)
decl_stmt|;
name|Expression
name|notLike
init|=
operator|new
name|ASTNotLikeIgnoreCase
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"artistName"
argument_list|)
argument_list|,
literal|"aBcD"
argument_list|)
decl_stmt|;
name|Artist
name|noMatch1
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|noMatch1
operator|.
name|setArtistName
argument_list|(
literal|"dabc"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|like
operator|.
name|match
argument_list|(
name|noMatch1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|notLike
operator|.
name|match
argument_list|(
name|noMatch1
argument_list|)
argument_list|)
expr_stmt|;
name|Artist
name|match1
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|match1
operator|.
name|setArtistName
argument_list|(
literal|"abcd"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed: "
operator|+
name|like
argument_list|,
name|like
operator|.
name|match
argument_list|(
name|match1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Failed: "
operator|+
name|notLike
argument_list|,
name|notLike
operator|.
name|match
argument_list|(
name|match1
argument_list|)
argument_list|)
expr_stmt|;
name|Artist
name|match2
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|match2
operator|.
name|setArtistName
argument_list|(
literal|"ABcD"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed: "
operator|+
name|like
argument_list|,
name|like
operator|.
name|match
argument_list|(
name|match2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Failed: "
operator|+
name|notLike
argument_list|,
name|notLike
operator|.
name|match
argument_list|(
name|match2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluateWithCollection
parameter_list|()
block|{
name|Expression
name|like
init|=
operator|new
name|ASTLikeIgnoreCase
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"paintingArray.paintingTitle"
argument_list|)
argument_list|,
literal|"aBcD"
argument_list|)
decl_stmt|;
name|Expression
name|notLike
init|=
operator|new
name|ASTNotLikeIgnoreCase
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"paintingArray.paintingTitle"
argument_list|)
argument_list|,
literal|"aBcD"
argument_list|)
decl_stmt|;
name|Artist
name|noMatch1
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|noMatch1
operator|.
name|writePropertyDirectly
argument_list|(
literal|"paintingArray"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|createPainting
argument_list|(
literal|"xyz"
argument_list|)
argument_list|,
name|createPainting
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Failed: "
operator|+
name|like
argument_list|,
name|like
operator|.
name|match
argument_list|(
name|noMatch1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed: "
operator|+
name|like
argument_list|,
name|notLike
operator|.
name|match
argument_list|(
name|noMatch1
argument_list|)
argument_list|)
expr_stmt|;
name|Artist
name|match1
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|match1
operator|.
name|writePropertyDirectly
argument_list|(
literal|"paintingArray"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|createPainting
argument_list|(
literal|"AbCd"
argument_list|)
argument_list|,
name|createPainting
argument_list|(
literal|"abcd"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed: "
operator|+
name|like
argument_list|,
name|like
operator|.
name|match
argument_list|(
name|match1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Failed: "
operator|+
name|like
argument_list|,
name|notLike
operator|.
name|match
argument_list|(
name|match1
argument_list|)
argument_list|)
expr_stmt|;
name|Artist
name|match2
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|match2
operator|.
name|writePropertyDirectly
argument_list|(
literal|"paintingArray"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|createPainting
argument_list|(
literal|"Xzy"
argument_list|)
argument_list|,
name|createPainting
argument_list|(
literal|"abcd"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed: "
operator|+
name|like
argument_list|,
name|like
operator|.
name|match
argument_list|(
name|match2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed: "
operator|+
name|like
argument_list|,
name|notLike
operator|.
name|match
argument_list|(
name|match2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEvaluateUnicode
parameter_list|()
block|{
name|Expression
name|like
init|=
operator|new
name|ASTLikeIgnoreCase
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"artistName"
argument_list|)
argument_list|,
literal|"ÃÐÄÃ%"
argument_list|)
decl_stmt|;
name|Expression
name|notLike
init|=
operator|new
name|ASTNotLikeIgnoreCase
argument_list|(
operator|new
name|ASTObjPath
argument_list|(
literal|"artistName"
argument_list|)
argument_list|,
literal|"ÃÐÄÃ%"
argument_list|)
decl_stmt|;
name|Artist
name|noMatch1
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|noMatch1
operator|.
name|setArtistName
argument_list|(
literal|"Ã bÄÃ¾"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|like
operator|.
name|match
argument_list|(
name|noMatch1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|notLike
operator|.
name|match
argument_list|(
name|noMatch1
argument_list|)
argument_list|)
expr_stmt|;
name|Artist
name|match1
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|match1
operator|.
name|setArtistName
argument_list|(
literal|"Ã Ð±ÄÃ¾d"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed: "
operator|+
name|like
argument_list|,
name|like
operator|.
name|match
argument_list|(
name|match1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Failed: "
operator|+
name|notLike
argument_list|,
name|notLike
operator|.
name|match
argument_list|(
name|match1
argument_list|)
argument_list|)
expr_stmt|;
name|Artist
name|match2
init|=
operator|new
name|Artist
argument_list|()
decl_stmt|;
name|match2
operator|.
name|setArtistName
argument_list|(
literal|"Ã ÐÄÃ"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Failed: "
operator|+
name|like
argument_list|,
name|like
operator|.
name|match
argument_list|(
name|match2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Failed: "
operator|+
name|notLike
argument_list|,
name|notLike
operator|.
name|match
argument_list|(
name|match2
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Painting
name|createPainting
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Painting
name|p
init|=
operator|new
name|Painting
argument_list|()
decl_stmt|;
name|p
operator|.
name|setPaintingTitle
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
block|}
end_class

end_unit

