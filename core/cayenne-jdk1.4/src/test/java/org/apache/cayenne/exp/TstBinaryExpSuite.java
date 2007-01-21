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
package|;
end_package

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

begin_comment
comment|/**  * A suite of binary expression tests.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|TstBinaryExpSuite
extends|extends
name|TstExpressionSuite
block|{
specifier|private
specifier|static
specifier|final
name|TstExpressionCase
name|like1
init|=
name|buildLike1
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|TstExpressionCase
name|likeic1
init|=
name|buildLikeIgnoreCase1
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|TstExpressionCase
name|in1
init|=
name|buildIn1
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|TstExpressionCase
name|in2
init|=
name|buildIn2
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|TstExpressionCase
name|isNull
init|=
name|buildIsNull
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|TstExpressionCase
name|isNotNull
init|=
name|buildIsNotNull
argument_list|()
decl_stmt|;
comment|/** Cayenne syntax: "toGallery.galleryName in ('g1', 'g2', g3')" */
specifier|private
specifier|static
name|TstExpressionCase
name|buildIn1
parameter_list|()
block|{
name|List
name|in
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|in
operator|.
name|add
argument_list|(
literal|"g1"
argument_list|)
expr_stmt|;
name|in
operator|.
name|add
argument_list|(
literal|"g2"
argument_list|)
expr_stmt|;
name|in
operator|.
name|add
argument_list|(
literal|"g3"
argument_list|)
expr_stmt|;
name|Expression
name|e1
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|IN
argument_list|)
decl_stmt|;
name|Expression
name|e10
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|)
decl_stmt|;
name|e10
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
literal|"toGallery.galleryName"
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
name|e10
argument_list|)
expr_stmt|;
name|Expression
name|e11
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|LIST
argument_list|)
decl_stmt|;
name|e11
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setOperand
argument_list|(
literal|1
argument_list|,
name|e11
argument_list|)
expr_stmt|;
return|return
operator|new
name|TstExpressionCase
argument_list|(
literal|"Exhibit"
argument_list|,
name|e1
argument_list|,
literal|"ta.GALLERY_NAME IN (?, ?, ?)"
argument_list|,
literal|3
argument_list|,
literal|2
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|TstExpressionCase
name|buildIsNull
parameter_list|()
block|{
name|Expression
name|e1
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|EQUAL_TO
argument_list|)
decl_stmt|;
name|Expression
name|e10
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|)
decl_stmt|;
name|e10
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
literal|"toGallery.galleryName"
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
name|e10
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setOperand
argument_list|(
literal|1
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
operator|new
name|TstExpressionCase
argument_list|(
literal|"Exhibit"
argument_list|,
name|e1
argument_list|,
literal|"ta.GALLERY_NAME IS NULL"
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|TstExpressionCase
name|buildIsNotNull
parameter_list|()
block|{
name|Expression
name|e1
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|NOT_EQUAL_TO
argument_list|)
decl_stmt|;
name|Expression
name|e10
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|)
decl_stmt|;
name|e10
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
literal|"toGallery.galleryName"
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
name|e10
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setOperand
argument_list|(
literal|1
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
operator|new
name|TstExpressionCase
argument_list|(
literal|"Exhibit"
argument_list|,
name|e1
argument_list|,
literal|"ta.GALLERY_NAME IS NOT NULL"
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|)
return|;
block|}
comment|/** Cayenne syntax: "toGallery.galleryName in ('g1', 'g2', g3')" */
specifier|private
specifier|static
name|TstExpressionCase
name|buildIn2
parameter_list|()
block|{
comment|// test Object[]
name|Object
index|[]
name|in
init|=
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
decl_stmt|;
name|Expression
name|e1
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|IN
argument_list|)
decl_stmt|;
name|Expression
name|e10
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|)
decl_stmt|;
name|e10
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
literal|"toGallery.galleryName"
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
name|e10
argument_list|)
expr_stmt|;
name|Expression
name|e11
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|LIST
argument_list|)
decl_stmt|;
name|e11
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setOperand
argument_list|(
literal|1
argument_list|,
name|e11
argument_list|)
expr_stmt|;
return|return
operator|new
name|TstExpressionCase
argument_list|(
literal|"Exhibit"
argument_list|,
name|e1
argument_list|,
literal|"ta.GALLERY_NAME IN (?, ?, ?)"
argument_list|,
literal|3
argument_list|,
literal|2
argument_list|)
return|;
block|}
comment|/** Cayenne syntax: "toGallery.galleryName like 'a%'" */
specifier|private
specifier|static
name|TstExpressionCase
name|buildLike1
parameter_list|()
block|{
name|Expression
name|e1
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|LIKE
argument_list|)
decl_stmt|;
name|Expression
name|e10
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|)
decl_stmt|;
name|e10
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
literal|"toGallery.galleryName"
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
name|e10
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setOperand
argument_list|(
literal|1
argument_list|,
literal|"a%"
argument_list|)
expr_stmt|;
return|return
operator|new
name|TstExpressionCase
argument_list|(
literal|"Exhibit"
argument_list|,
name|e1
argument_list|,
literal|"ta.GALLERY_NAME LIKE ?"
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|)
return|;
block|}
comment|/** Cayenne syntax: "toGallery.galleryName likeIgnoreCase 'a%'" */
specifier|private
specifier|static
name|TstExpressionCase
name|buildLikeIgnoreCase1
parameter_list|()
block|{
name|Expression
name|e1
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|LIKE_IGNORE_CASE
argument_list|)
decl_stmt|;
name|Expression
name|e10
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|OBJ_PATH
argument_list|)
decl_stmt|;
name|e10
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
literal|"toGallery.galleryName"
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
name|e10
argument_list|)
expr_stmt|;
name|e1
operator|.
name|setOperand
argument_list|(
literal|1
argument_list|,
literal|"a%"
argument_list|)
expr_stmt|;
return|return
operator|new
name|TstExpressionCase
argument_list|(
literal|"Exhibit"
argument_list|,
name|e1
argument_list|,
literal|"UPPER(ta.GALLERY_NAME) LIKE UPPER(?)"
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|)
return|;
block|}
specifier|public
name|TstBinaryExpSuite
parameter_list|()
block|{
name|addCase
argument_list|(
name|like1
argument_list|)
expr_stmt|;
name|addCase
argument_list|(
name|likeic1
argument_list|)
expr_stmt|;
name|addCase
argument_list|(
name|in1
argument_list|)
expr_stmt|;
name|addCase
argument_list|(
name|in2
argument_list|)
expr_stmt|;
name|addCase
argument_list|(
name|isNull
argument_list|)
expr_stmt|;
name|addCase
argument_list|(
name|isNotNull
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

