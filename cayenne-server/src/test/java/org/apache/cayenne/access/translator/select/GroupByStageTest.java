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
name|EmptyNode
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
name|GroupByNode
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
name|exp
operator|.
name|property
operator|.
name|PropertyFactory
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
name|GroupByStageTest
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
name|TranslatableQueryWrapper
name|wrapper
init|=
operator|new
name|MockQueryWrapperBuilder
argument_list|()
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
comment|// no result columns
annotation|@
name|Test
specifier|public
name|void
name|emptyContext
parameter_list|()
block|{
name|GroupByStage
name|stage
init|=
operator|new
name|GroupByStage
argument_list|()
decl_stmt|;
name|stage
operator|.
name|perform
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|Node
name|node
init|=
name|context
operator|.
name|getSelectBuilder
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|node
operator|.
name|getChildrenCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// result column but no aggregates
annotation|@
name|Test
specifier|public
name|void
name|noAggregates
parameter_list|()
block|{
name|context
operator|.
name|addResultNode
argument_list|(
operator|new
name|ColumnNode
argument_list|(
literal|"t0"
argument_list|,
literal|"column"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|GroupByStage
name|stage
init|=
operator|new
name|GroupByStage
argument_list|()
decl_stmt|;
name|stage
operator|.
name|perform
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|Node
name|node
init|=
name|context
operator|.
name|getSelectBuilder
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|node
operator|.
name|getChildrenCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// result column + aggregate
annotation|@
name|Test
specifier|public
name|void
name|groupByWithAggregates
parameter_list|()
block|{
name|context
operator|.
name|addResultNode
argument_list|(
operator|new
name|ColumnNode
argument_list|(
literal|"t0"
argument_list|,
literal|"column"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|addResultNode
argument_list|(
operator|new
name|EmptyNode
argument_list|()
argument_list|,
literal|true
argument_list|,
name|PropertyFactory
operator|.
name|COUNT
argument_list|,
literal|"count"
argument_list|)
expr_stmt|;
name|GroupByStage
name|stage
init|=
operator|new
name|GroupByStage
argument_list|()
decl_stmt|;
name|stage
operator|.
name|perform
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|Node
name|node
init|=
name|context
operator|.
name|getSelectBuilder
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|node
operator|.
name|getChildrenCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|node
operator|.
name|getChild
argument_list|(
literal|0
argument_list|)
argument_list|,
name|instanceOf
argument_list|(
name|GroupByNode
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|node
operator|.
name|getChild
argument_list|(
literal|0
argument_list|)
operator|.
name|getChildrenCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|node
operator|.
name|getChild
argument_list|(
literal|0
argument_list|)
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
name|ColumnNode
name|columnNode
init|=
operator|(
name|ColumnNode
operator|)
name|node
operator|.
name|getChild
argument_list|(
literal|0
argument_list|)
operator|.
name|getChild
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"t0"
argument_list|,
name|columnNode
operator|.
name|getTable
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"column"
argument_list|,
name|columnNode
operator|.
name|getColumn
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

