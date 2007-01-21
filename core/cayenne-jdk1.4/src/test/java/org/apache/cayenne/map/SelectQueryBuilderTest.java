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
name|map
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|query
operator|.
name|Query
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
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|SelectQuery
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|SelectQueryBuilderTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testGetQueryType
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQueryBuilder
name|builder
init|=
operator|new
name|MockupRootQueryBuilder
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|builder
operator|.
name|getQuery
argument_list|()
operator|instanceof
name|SelectQuery
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetQueryName
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQueryBuilder
name|builder
init|=
operator|new
name|MockupRootQueryBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setName
argument_list|(
literal|"xyz"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xyz"
argument_list|,
name|builder
operator|.
name|getQuery
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetQueryRoot
parameter_list|()
throws|throws
name|Exception
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
name|SelectQueryBuilder
name|builder
init|=
operator|new
name|SelectQueryBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setRoot
argument_list|(
name|map
argument_list|,
name|QueryBuilder
operator|.
name|OBJ_ENTITY_ROOT
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|builder
operator|.
name|getQuery
argument_list|()
operator|instanceof
name|SelectQuery
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|entity
argument_list|,
operator|(
operator|(
name|SelectQuery
operator|)
name|builder
operator|.
name|getQuery
argument_list|()
operator|)
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetQueryQualifier
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQueryBuilder
name|builder
init|=
operator|new
name|MockupRootQueryBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setQualifier
argument_list|(
literal|"abc = 5"
argument_list|)
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|(
name|SelectQuery
operator|)
name|builder
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Expression
operator|.
name|fromString
argument_list|(
literal|"abc = 5"
argument_list|)
argument_list|,
name|query
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetQueryProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|SelectQueryBuilder
name|builder
init|=
operator|new
name|MockupRootQueryBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|addProperty
argument_list|(
name|QueryMetadata
operator|.
name|FETCH_LIMIT_PROPERTY
argument_list|,
literal|"5"
argument_list|)
expr_stmt|;
name|Query
name|query
init|=
name|builder
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|query
operator|instanceof
name|SelectQuery
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
operator|(
operator|(
name|SelectQuery
operator|)
name|query
operator|)
operator|.
name|getFetchLimit
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: test other properties...
block|}
class|class
name|MockupRootQueryBuilder
extends|extends
name|SelectQueryBuilder
block|{
specifier|public
name|Object
name|getRoot
parameter_list|()
block|{
return|return
literal|"FakeRoot"
return|;
block|}
block|}
block|}
end_class

end_unit

