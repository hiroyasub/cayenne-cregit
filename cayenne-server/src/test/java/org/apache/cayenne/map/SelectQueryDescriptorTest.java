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
name|map
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
name|query
operator|.
name|QueryMetadata
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|SelectQueryDescriptorTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetQueryType
parameter_list|()
block|{
name|SelectQueryDescriptor
name|builder
init|=
name|QueryDescriptor
operator|.
name|selectQueryDescriptor
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setRoot
argument_list|(
literal|"FakeRoot"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|builder
operator|.
name|buildQuery
argument_list|()
operator|instanceof
name|ObjectSelect
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetQueryRoot
parameter_list|()
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"A"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|SelectQueryDescriptor
name|builder
init|=
name|QueryDescriptor
operator|.
name|selectQueryDescriptor
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setRoot
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|builder
operator|.
name|buildQuery
argument_list|()
operator|instanceof
name|ObjectSelect
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|entity
argument_list|,
name|builder
operator|.
name|buildQuery
argument_list|()
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetQueryQualifier
parameter_list|()
block|{
name|SelectQueryDescriptor
name|builder
init|=
name|QueryDescriptor
operator|.
name|selectQueryDescriptor
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setRoot
argument_list|(
literal|"FakeRoot"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setQualifier
argument_list|(
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"abc = 5"
argument_list|)
argument_list|)
expr_stmt|;
name|ObjectSelect
argument_list|<
name|?
argument_list|>
name|query
init|=
name|builder
operator|.
name|buildQuery
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|ExpressionFactory
operator|.
name|exp
argument_list|(
literal|"abc = 5"
argument_list|)
argument_list|,
name|query
operator|.
name|getWhere
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetQueryProperties
parameter_list|()
block|{
name|SelectQueryDescriptor
name|builder
init|=
name|QueryDescriptor
operator|.
name|selectQueryDescriptor
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setRoot
argument_list|(
literal|"FakeRoot"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setProperty
argument_list|(
name|QueryMetadata
operator|.
name|FETCH_LIMIT_PROPERTY
argument_list|,
literal|"5"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setProperty
argument_list|(
name|QueryMetadata
operator|.
name|STATEMENT_FETCH_SIZE_PROPERTY
argument_list|,
literal|"6"
argument_list|)
expr_stmt|;
name|ObjectSelect
argument_list|<
name|?
argument_list|>
name|query
init|=
name|builder
operator|.
name|buildQuery
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|query
operator|instanceof
name|ObjectSelect
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|query
operator|.
name|getLimit
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|query
operator|.
name|getStatementFetchSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: test other properties...
block|}
block|}
end_class

end_unit

