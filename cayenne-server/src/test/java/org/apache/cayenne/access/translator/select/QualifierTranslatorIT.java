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
operator|.
name|translator
operator|.
name|select
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
name|fail
import|;
end_import

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
name|access
operator|.
name|DataNode
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
name|query
operator|.
name|MockQuery
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
name|testdo
operator|.
name|testmap
operator|.
name|Exhibit
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
name|ServerCaseDataSourceFactory
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
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
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
name|QualifierTranslatorIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataNode
name|node
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ServerCaseDataSourceFactory
name|dataSourceFactory
decl_stmt|;
comment|// TODO: not an integration test; extract into *Test
annotation|@
name|Test
specifier|public
name|void
name|testNonQualifiedQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|TstQueryAssembler
name|qa
init|=
operator|new
name|TstQueryAssembler
argument_list|(
operator|new
name|MockQuery
argument_list|()
argument_list|,
name|node
operator|.
name|getAdapter
argument_list|()
argument_list|,
name|node
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
operator|new
name|QualifierTranslator
argument_list|(
name|qa
argument_list|)
operator|.
name|appendPart
argument_list|(
operator|new
name|StringBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassCastException
name|ccex
parameter_list|)
block|{
comment|// exception expected
block|}
block|}
comment|// TODO: not an integration test; extract into *Test
annotation|@
name|Test
specifier|public
name|void
name|testNullQualifier
parameter_list|()
throws|throws
name|Exception
block|{
name|TstQueryAssembler
name|qa
init|=
operator|new
name|TstQueryAssembler
argument_list|(
operator|new
name|SelectQuery
argument_list|<
name|Object
argument_list|>
argument_list|()
argument_list|,
name|node
operator|.
name|getAdapter
argument_list|()
argument_list|,
name|node
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|StringBuilder
name|out
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
operator|new
name|QualifierTranslator
argument_list|(
name|qa
argument_list|)
operator|.
name|appendPart
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|out
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBinary_In1
parameter_list|()
throws|throws
name|Exception
block|{
name|doExpressionTest
argument_list|(
name|Exhibit
operator|.
name|class
argument_list|,
literal|"toGallery.galleryName in ('g1', 'g2', 'g3')"
argument_list|,
literal|"ta.GALLERY_NAME IN (?, ?, ?)"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBinary_In2
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
literal|"toGallery.galleryName"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"g1"
block|,
literal|"g2"
block|,
literal|"g3"
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|doExpressionTest
argument_list|(
name|Exhibit
operator|.
name|class
argument_list|,
name|exp
argument_list|,
literal|"ta.GALLERY_NAME IN (?, ?, ?)"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBinary_In3
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
literal|"toGallery.galleryName"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"g1"
block|,
literal|"g2"
block|,
literal|"g3"
block|}
argument_list|)
decl_stmt|;
name|doExpressionTest
argument_list|(
name|Exhibit
operator|.
name|class
argument_list|,
name|exp
argument_list|,
literal|"ta.GALLERY_NAME IN (?, ?, ?)"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBinary_Like
parameter_list|()
throws|throws
name|Exception
block|{
name|doExpressionTest
argument_list|(
name|Exhibit
operator|.
name|class
argument_list|,
literal|"toGallery.galleryName like 'a%'"
argument_list|,
literal|"ta.GALLERY_NAME LIKE ?"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBinary_LikeIgnoreCase
parameter_list|()
throws|throws
name|Exception
block|{
name|doExpressionTest
argument_list|(
name|Exhibit
operator|.
name|class
argument_list|,
literal|"toGallery.galleryName likeIgnoreCase 'a%'"
argument_list|,
literal|"UPPER(ta.GALLERY_NAME) LIKE UPPER(?)"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBinary_IsNull
parameter_list|()
throws|throws
name|Exception
block|{
name|doExpressionTest
argument_list|(
name|Exhibit
operator|.
name|class
argument_list|,
literal|"toGallery.galleryName = null"
argument_list|,
literal|"ta.GALLERY_NAME IS NULL"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBinary_IsNotNull
parameter_list|()
throws|throws
name|Exception
block|{
name|doExpressionTest
argument_list|(
name|Exhibit
operator|.
name|class
argument_list|,
literal|"toGallery.galleryName != null"
argument_list|,
literal|"ta.GALLERY_NAME IS NOT NULL"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTernary_Between
parameter_list|()
throws|throws
name|Exception
block|{
name|doExpressionTest
argument_list|(
name|Painting
operator|.
name|class
argument_list|,
literal|"estimatedPrice between 3000 and 15000"
argument_list|,
literal|"ta.ESTIMATED_PRICE BETWEEN ? AND ?"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExtras
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|oid1
init|=
operator|new
name|ObjectId
argument_list|(
literal|"Gallery"
argument_list|,
literal|"GALLERY_ID"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|ObjectId
name|oid2
init|=
operator|new
name|ObjectId
argument_list|(
literal|"Gallery"
argument_list|,
literal|"GALLERY_ID"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|Gallery
name|g1
init|=
operator|new
name|Gallery
argument_list|()
decl_stmt|;
name|Gallery
name|g2
init|=
operator|new
name|Gallery
argument_list|()
decl_stmt|;
name|g1
operator|.
name|setObjectId
argument_list|(
name|oid1
argument_list|)
expr_stmt|;
name|g2
operator|.
name|setObjectId
argument_list|(
name|oid2
argument_list|)
expr_stmt|;
name|Expression
name|e1
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"toGallery"
argument_list|,
name|g1
argument_list|)
decl_stmt|;
name|Expression
name|e2
init|=
name|e1
operator|.
name|orExp
argument_list|(
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"toGallery"
argument_list|,
name|g2
argument_list|)
argument_list|)
decl_stmt|;
name|doExpressionTest
argument_list|(
name|Exhibit
operator|.
name|class
argument_list|,
name|e2
argument_list|,
literal|"(ta.GALLERY_ID = ?) OR (ta.GALLERY_ID = ?)"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doExpressionTest
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|queryType
parameter_list|,
name|String
name|qualifier
parameter_list|,
name|String
name|expectedSQL
parameter_list|)
throws|throws
name|Exception
block|{
name|doExpressionTest
argument_list|(
name|queryType
argument_list|,
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|qualifier
argument_list|)
argument_list|,
name|expectedSQL
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doExpressionTest
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|queryType
parameter_list|,
name|Expression
name|qualifier
parameter_list|,
name|String
name|expectedSQL
parameter_list|)
throws|throws
name|Exception
block|{
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|q
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|(
name|queryType
argument_list|)
decl_stmt|;
name|q
operator|.
name|setQualifier
argument_list|(
name|qualifier
argument_list|)
expr_stmt|;
name|TstQueryAssembler
name|qa
init|=
operator|new
name|TstQueryAssembler
argument_list|(
name|q
argument_list|,
name|node
operator|.
name|getAdapter
argument_list|()
argument_list|,
name|node
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|StringBuilder
name|out
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
name|translated
init|=
operator|new
name|QualifierTranslator
argument_list|(
name|qa
argument_list|)
operator|.
name|appendPart
argument_list|(
name|out
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedSQL
argument_list|,
name|translated
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

