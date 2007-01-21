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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DerivedDbEntityTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|DerivedDbEntity
name|ent
decl_stmt|;
specifier|protected
name|DataMap
name|map
decl_stmt|;
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|ent
operator|=
operator|new
name|DerivedDbEntity
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|map
operator|=
operator|new
name|DataMap
argument_list|()
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testParentEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNull
argument_list|(
name|ent
operator|.
name|getParentEntity
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|ent
argument_list|)
expr_stmt|;
name|DbEntity
name|parent
init|=
operator|new
name|DbEntity
argument_list|(
literal|"xyz"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|ent
operator|.
name|setParentEntity
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|parent
argument_list|,
name|ent
operator|.
name|getParentEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGroupByAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|DerivedDbAttribute
name|at
init|=
operator|new
name|DerivedDbAttribute
argument_list|()
decl_stmt|;
name|at
operator|.
name|setName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|ent
operator|.
name|addAttribute
argument_list|(
name|at
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ent
operator|.
name|getGroupByAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|at
operator|.
name|setGroupBy
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ent
operator|.
name|getGroupByAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFullyQualifiedName1
parameter_list|()
throws|throws
name|Exception
block|{
name|assignParent
argument_list|()
expr_stmt|;
name|ent
operator|.
name|setName
argument_list|(
literal|"derived"
argument_list|)
expr_stmt|;
name|ent
operator|.
name|getParentEntity
argument_list|()
operator|.
name|setName
argument_list|(
literal|"parent"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ent
operator|.
name|getParentEntity
argument_list|()
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|,
name|ent
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFullyQualifiedName2
parameter_list|()
throws|throws
name|Exception
block|{
name|assignParent
argument_list|()
expr_stmt|;
name|ent
operator|.
name|setName
argument_list|(
literal|"derived"
argument_list|)
expr_stmt|;
name|ent
operator|.
name|getParentEntity
argument_list|()
operator|.
name|setName
argument_list|(
literal|"parent"
argument_list|)
expr_stmt|;
name|ent
operator|.
name|getParentEntity
argument_list|()
operator|.
name|setSchema
argument_list|(
literal|"parent_schema"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ent
operator|.
name|getParentEntity
argument_list|()
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|,
name|ent
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResetToParentView1
parameter_list|()
throws|throws
name|Exception
block|{
name|assignParent
argument_list|()
expr_stmt|;
name|DerivedDbAttribute
name|at
init|=
operator|new
name|DerivedDbAttribute
argument_list|()
decl_stmt|;
name|at
operator|.
name|setName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|DbRelationship
name|rel
init|=
operator|new
name|DbRelationship
argument_list|()
decl_stmt|;
name|rel
operator|.
name|setName
argument_list|(
literal|"xyz"
argument_list|)
expr_stmt|;
name|ent
operator|.
name|addAttribute
argument_list|(
name|at
argument_list|)
expr_stmt|;
name|ent
operator|.
name|addRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
name|ent
operator|.
name|resetToParentView
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ent
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ent
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testResetToParentView2
parameter_list|()
throws|throws
name|Exception
block|{
name|assignParent
argument_list|()
expr_stmt|;
name|DbAttribute
name|at
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|at
operator|.
name|setName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|DbRelationship
name|rel
init|=
operator|new
name|DbRelationship
argument_list|()
decl_stmt|;
name|rel
operator|.
name|setName
argument_list|(
literal|"xyz"
argument_list|)
expr_stmt|;
name|ent
operator|.
name|getParentEntity
argument_list|()
operator|.
name|addAttribute
argument_list|(
name|at
argument_list|)
expr_stmt|;
name|ent
operator|.
name|getParentEntity
argument_list|()
operator|.
name|addRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
name|ent
operator|.
name|resetToParentView
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ent
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ent
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assignParent
parameter_list|()
block|{
name|DbEntity
name|parent
init|=
operator|new
name|DbEntity
argument_list|(
literal|"qwerty"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|ent
operator|.
name|setParentEntity
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

