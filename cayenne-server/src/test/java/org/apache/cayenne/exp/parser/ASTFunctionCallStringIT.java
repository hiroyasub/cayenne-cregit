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
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|exp
operator|.
name|Property
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
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

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
name|ASTFunctionCallStringIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
specifier|private
name|Artist
name|createArtist
parameter_list|(
name|String
name|name
parameter_list|)
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
name|a1
operator|.
name|setArtistName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|a1
operator|.
name|setDateOfBirth
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
return|return
name|a1
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTTrimInWhere
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|a1
init|=
name|createArtist
argument_list|(
literal|"  name  "
argument_list|)
decl_stmt|;
name|ASTTrim
name|exp
init|=
operator|new
name|ASTTrim
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|path
argument_list|()
argument_list|)
decl_stmt|;
name|Property
argument_list|<
name|String
argument_list|>
name|trimmedName
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"trimmedName"
argument_list|,
name|exp
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artist
name|a2
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
name|trimmedName
operator|.
name|eq
argument_list|(
literal|"name"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|a1
argument_list|,
name|a2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTUpperInWhere
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|a1
init|=
name|createArtist
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|ASTUpper
name|exp
init|=
operator|new
name|ASTUpper
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|path
argument_list|()
argument_list|)
decl_stmt|;
name|Property
argument_list|<
name|String
argument_list|>
name|upperName
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"upperName"
argument_list|,
name|exp
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artist
name|a2
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
name|upperName
operator|.
name|eq
argument_list|(
literal|"NAME"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|a1
argument_list|,
name|a2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTLowerInWhere
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|a1
init|=
name|createArtist
argument_list|(
literal|"NAME"
argument_list|)
decl_stmt|;
name|ASTLower
name|exp
init|=
operator|new
name|ASTLower
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|path
argument_list|()
argument_list|)
decl_stmt|;
name|Property
argument_list|<
name|String
argument_list|>
name|lowerName
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"lowerName"
argument_list|,
name|exp
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artist
name|a2
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
name|lowerName
operator|.
name|eq
argument_list|(
literal|"name"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|a1
argument_list|,
name|a2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTSubstringInWhere
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|a1
init|=
name|createArtist
argument_list|(
literal|"1234567890xyz"
argument_list|)
decl_stmt|;
name|ASTSubstring
name|exp
init|=
operator|new
name|ASTSubstring
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|path
argument_list|()
argument_list|,
operator|new
name|ASTScalar
argument_list|(
operator|(
name|Integer
operator|)
literal|2
argument_list|)
argument_list|,
operator|new
name|ASTScalar
argument_list|(
operator|(
name|Integer
operator|)
literal|8
argument_list|)
argument_list|)
decl_stmt|;
name|Property
argument_list|<
name|String
argument_list|>
name|substrName
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"substrName"
argument_list|,
name|exp
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artist
name|a2
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
name|substrName
operator|.
name|eq
argument_list|(
literal|"23456789"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|a1
argument_list|,
name|a2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTConcat
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|a1
init|=
name|createArtist
argument_list|(
literal|"Pablo"
argument_list|)
decl_stmt|;
name|ASTScalar
name|scalar1
init|=
operator|new
name|ASTScalar
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
name|ASTScalar
name|scalar2
init|=
operator|new
name|ASTScalar
argument_list|(
literal|"Picasso"
argument_list|)
decl_stmt|;
name|ASTConcat
name|exp
init|=
operator|new
name|ASTConcat
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|path
argument_list|()
argument_list|,
name|scalar1
argument_list|,
name|scalar2
argument_list|)
decl_stmt|;
name|Property
argument_list|<
name|String
argument_list|>
name|concatName
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"concatName"
argument_list|,
name|exp
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artist
name|a2
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
name|concatName
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
name|assertEquals
argument_list|(
name|a1
argument_list|,
name|a2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTLength
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|a1
init|=
name|createArtist
argument_list|(
literal|"123456"
argument_list|)
decl_stmt|;
name|ASTLength
name|exp
init|=
operator|new
name|ASTLength
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|path
argument_list|()
argument_list|)
decl_stmt|;
name|Property
argument_list|<
name|Integer
argument_list|>
name|nameLength
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"nameLength"
argument_list|,
name|exp
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artist
name|a2
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
name|nameLength
operator|.
name|gt
argument_list|(
literal|5
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|a1
argument_list|,
name|a2
argument_list|)
expr_stmt|;
name|Artist
name|a3
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
name|nameLength
operator|.
name|lt
argument_list|(
literal|5
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|a3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTLocate
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|a1
init|=
name|createArtist
argument_list|(
literal|"1267834567890abc"
argument_list|)
decl_stmt|;
name|ASTScalar
name|substr
init|=
operator|new
name|ASTScalar
argument_list|(
literal|"678"
argument_list|)
decl_stmt|;
comment|//        ASTScalar offset = new ASTScalar((Integer)5); // not all DBs support offset parameter, so skip it
name|ASTLocate
name|exp
init|=
operator|new
name|ASTLocate
argument_list|(
name|substr
argument_list|,
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|path
argument_list|()
argument_list|)
decl_stmt|;
name|Property
argument_list|<
name|Integer
argument_list|>
name|nameLoc
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"nameLoc"
argument_list|,
name|exp
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artist
name|a2
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
name|nameLoc
operator|.
name|eq
argument_list|(
literal|3
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|a1
argument_list|,
name|a2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCombinedFunction
parameter_list|()
throws|throws
name|Exception
block|{
name|Artist
name|a1
init|=
name|createArtist
argument_list|(
literal|"absdefghij  klmnopq"
argument_list|)
decl_stmt|;
comment|// substring with length 10 from 3 is "sdefghij  "
name|ASTSubstring
name|substring
init|=
operator|new
name|ASTSubstring
argument_list|(
name|Artist
operator|.
name|ARTIST_NAME
operator|.
name|path
argument_list|()
argument_list|,
operator|new
name|ASTScalar
argument_list|(
operator|(
name|Integer
operator|)
literal|3
argument_list|)
argument_list|,
operator|new
name|ASTScalar
argument_list|(
operator|(
name|Integer
operator|)
literal|10
argument_list|)
argument_list|)
decl_stmt|;
name|ASTTrim
name|trim
init|=
operator|new
name|ASTTrim
argument_list|(
name|substring
argument_list|)
decl_stmt|;
name|ASTUpper
name|upper
init|=
operator|new
name|ASTUpper
argument_list|(
name|trim
argument_list|)
decl_stmt|;
name|ASTConcat
name|concat
init|=
operator|new
name|ASTConcat
argument_list|(
name|upper
argument_list|,
operator|new
name|ASTScalar
argument_list|(
literal|" "
argument_list|)
argument_list|,
operator|new
name|ASTScalar
argument_list|(
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|Property
argument_list|<
name|String
argument_list|>
name|name
init|=
name|Property
operator|.
name|create
argument_list|(
literal|"substrName"
argument_list|,
name|concat
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artist
name|a2
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
name|name
operator|.
name|eq
argument_list|(
literal|"SDEFGHIJ test"
argument_list|)
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|a1
argument_list|,
name|a2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTConcatParse
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"concat('abc', 'def')"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abcdef"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTSubstringParse
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"substring('123456789', 3, 2)"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"34"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTTrimParse
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"trim(' abc ')"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTLowerParse
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"lower('AbC')"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTUpperParse
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"upper('aBc')"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ABC"
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testASTLocateParse
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"locate('Bc', 'aBc')"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testComplexParse
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"locate(upper('Bc'), upper('aBc')) = length(substring(trim(lower(concat('   abc', 'def   '))), 3, 2))"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|exp
operator|.
name|evaluate
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

