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
name|graph
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
name|NodeDiffTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetNodeId
parameter_list|()
block|{
name|Object
name|id
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|NodeDiff
name|diff
init|=
operator|new
name|ConcreteNodeDiff
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|id
argument_list|,
name|diff
operator|.
name|getNodeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompareTo
parameter_list|()
block|{
name|NodeDiff
name|d1
init|=
operator|new
name|ConcreteNodeDiff
argument_list|(
literal|"x"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|NodeDiff
name|d2
init|=
operator|new
name|ConcreteNodeDiff
argument_list|(
literal|"y"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|NodeDiff
name|d3
init|=
operator|new
name|ConcreteNodeDiff
argument_list|(
literal|"z"
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|NodeDiff
name|d4
init|=
operator|new
name|ConcreteNodeDiff
argument_list|(
literal|"a"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|d1
operator|.
name|compareTo
argument_list|(
name|d2
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|d2
operator|.
name|compareTo
argument_list|(
name|d1
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|d1
operator|.
name|compareTo
argument_list|(
name|d3
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|d2
operator|.
name|compareTo
argument_list|(
name|d4
argument_list|)
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|d2
operator|.
name|compareTo
argument_list|(
name|d3
argument_list|)
operator|<
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
class|class
name|ConcreteNodeDiff
extends|extends
name|NodeDiff
block|{
specifier|public
name|ConcreteNodeDiff
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
name|super
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ConcreteNodeDiff
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|int
name|diffId
parameter_list|)
block|{
name|super
argument_list|(
name|nodeId
argument_list|,
name|diffId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|apply
parameter_list|(
name|GraphChangeHandler
name|tracker
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|undo
parameter_list|(
name|GraphChangeHandler
name|tracker
parameter_list|)
block|{
block|}
block|}
block|}
end_class

end_unit

