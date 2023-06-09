begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *    Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|spi
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
name|di
operator|.
name|DIRuntimeException
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
name|List
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
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

begin_class
specifier|public
class|class
name|DIGraphTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testTopSortNoCycles
parameter_list|()
block|{
name|DIGraph
argument_list|<
name|String
argument_list|>
name|graph
init|=
operator|new
name|DIGraph
argument_list|<>
argument_list|()
decl_stmt|;
name|graph
operator|.
name|add
argument_list|(
literal|"x"
argument_list|,
literal|"y"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|add
argument_list|(
literal|"x"
argument_list|,
literal|"z"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|add
argument_list|(
literal|"z"
argument_list|,
literal|"a"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|sorted
init|=
name|graph
operator|.
name|topSort
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|asList
argument_list|(
literal|"y"
argument_list|,
literal|"a"
argument_list|,
literal|"z"
argument_list|,
literal|"x"
argument_list|)
argument_list|,
name|sorted
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|DIRuntimeException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testTopSortDirectCycle
parameter_list|()
block|{
name|DIGraph
argument_list|<
name|String
argument_list|>
name|graph
init|=
operator|new
name|DIGraph
argument_list|<>
argument_list|()
decl_stmt|;
name|graph
operator|.
name|add
argument_list|(
literal|"x"
argument_list|,
literal|"y"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|add
argument_list|(
literal|"y"
argument_list|,
literal|"x"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|topSort
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTopSortDirectCycleOverride
parameter_list|()
block|{
name|DIGraph
argument_list|<
name|String
argument_list|>
name|graph
init|=
operator|new
name|DIGraph
argument_list|<>
argument_list|()
decl_stmt|;
name|graph
operator|.
name|addWithOverride
argument_list|(
literal|"x"
argument_list|,
literal|"y"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|addWithOverride
argument_list|(
literal|"y"
argument_list|,
literal|"x"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|sorted
init|=
name|graph
operator|.
name|topSort
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|asList
argument_list|(
literal|"x"
argument_list|,
literal|"y"
argument_list|)
argument_list|,
name|sorted
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|DIRuntimeException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testTopSortInDirectCycle
parameter_list|()
block|{
name|DIGraph
argument_list|<
name|String
argument_list|>
name|graph
init|=
operator|new
name|DIGraph
argument_list|<>
argument_list|()
decl_stmt|;
name|graph
operator|.
name|add
argument_list|(
literal|"x"
argument_list|,
literal|"y"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|add
argument_list|(
literal|"y"
argument_list|,
literal|"z"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|add
argument_list|(
literal|"z"
argument_list|,
literal|"x"
argument_list|)
expr_stmt|;
name|graph
operator|.
name|topSort
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

