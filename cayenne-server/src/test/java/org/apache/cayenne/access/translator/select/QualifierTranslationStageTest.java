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
name|access
operator|.
name|translator
operator|.
name|select
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|ColumnNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|Node
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|OpExpressionNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|ValueNode
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
name|junit
operator|.
name|Before
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
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|instanceOf
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
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

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|QualifierTranslationStageTest
block|{
specifier|private
name|TranslatorContext
name|context
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|prepareContext
parameter_list|()
block|{
name|DbEntity
name|dbEntity
init|=
operator|new
name|DbEntity
argument_list|()
decl_stmt|;
name|dbEntity
operator|.
name|setName
argument_list|(
literal|"mock"
argument_list|)
expr_stmt|;
name|DbAttribute
name|dbAttribute
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|dbAttribute
operator|.
name|setName
argument_list|(
literal|"path"
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|addAttribute
argument_list|(
name|dbAttribute
argument_list|)
expr_stmt|;
name|TranslatableQueryWrapper
name|wrapper
init|=
operator|new
name|MockQueryWrapperBuilder
argument_list|()
operator|.
name|withQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|greaterOrEqualDbExp
argument_list|(
literal|"path"
argument_list|,
literal|10
argument_list|)
argument_list|)
operator|.
name|withMetaData
argument_list|(
operator|new
name|MockQueryMetadataBuilder
argument_list|()
operator|.
name|withDbEntity
argument_list|(
name|dbEntity
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|context
operator|=
operator|new
name|MockTranslatorContext
argument_list|(
name|wrapper
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|perform
parameter_list|()
block|{
name|QualifierTranslationStage
name|stage
init|=
operator|new
name|QualifierTranslationStage
argument_list|()
decl_stmt|;
name|stage
operator|.
name|perform
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getQualifierNode
argument_list|()
argument_list|)
expr_stmt|;
comment|// Content of "Qualifier" node:
comment|//
comment|//   OpExpression
comment|//    /        \
comment|// Column     Value
name|Node
name|op
init|=
name|context
operator|.
name|getQualifierNode
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|op
argument_list|,
name|instanceOf
argument_list|(
name|OpExpressionNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|">="
argument_list|,
operator|(
operator|(
name|OpExpressionNode
operator|)
name|op
operator|)
operator|.
name|getOp
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|op
operator|.
name|getChildrenCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|op
operator|.
name|getChild
argument_list|(
literal|0
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|ColumnNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|op
operator|.
name|getChild
argument_list|(
literal|1
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|ValueNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|ColumnNode
name|columnNode
init|=
operator|(
name|ColumnNode
operator|)
name|op
operator|.
name|getChild
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ValueNode
name|valueNode
init|=
operator|(
name|ValueNode
operator|)
name|op
operator|.
name|getChild
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"path"
argument_list|,
name|columnNode
operator|.
name|getColumn
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|valueNode
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

