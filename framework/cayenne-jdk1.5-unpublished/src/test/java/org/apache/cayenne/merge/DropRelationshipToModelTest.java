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
name|merge
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
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
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbJoin
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
name|DbRelationship
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
name|ObjAttribute
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
name|ObjEntity
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
name|ObjRelationship
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|ServerCase
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|DropRelationshipToModelTest
extends|extends
name|MergeCase
block|{
specifier|public
name|void
name|testForreignKey
parameter_list|()
throws|throws
name|Exception
block|{
name|dropTableIfPresent
argument_list|(
literal|"NEW_TABLE"
argument_list|)
expr_stmt|;
name|dropTableIfPresent
argument_list|(
literal|"NEW_TABLE2"
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|DbEntity
name|dbEntity1
init|=
operator|new
name|DbEntity
argument_list|(
literal|"NEW_TABLE"
argument_list|)
decl_stmt|;
name|DbAttribute
name|e1col1
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"ID"
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|,
name|dbEntity1
argument_list|)
decl_stmt|;
name|e1col1
operator|.
name|setMandatory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|e1col1
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dbEntity1
operator|.
name|addAttribute
argument_list|(
name|e1col1
argument_list|)
expr_stmt|;
name|DbAttribute
name|e1col2
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"NAME"
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|dbEntity1
argument_list|)
decl_stmt|;
name|e1col2
operator|.
name|setMaxLength
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|e1col2
operator|.
name|setMandatory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dbEntity1
operator|.
name|addAttribute
argument_list|(
name|e1col2
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbEntity1
argument_list|)
expr_stmt|;
name|DbEntity
name|dbEntity2
init|=
operator|new
name|DbEntity
argument_list|(
literal|"NEW_TABLE2"
argument_list|)
decl_stmt|;
name|DbAttribute
name|e2col1
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"ID"
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|,
name|dbEntity2
argument_list|)
decl_stmt|;
name|e2col1
operator|.
name|setMandatory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|e2col1
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dbEntity2
operator|.
name|addAttribute
argument_list|(
name|e2col1
argument_list|)
expr_stmt|;
name|DbAttribute
name|e2col2
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"FK"
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|,
name|dbEntity2
argument_list|)
decl_stmt|;
name|dbEntity2
operator|.
name|addAttribute
argument_list|(
name|e2col2
argument_list|)
expr_stmt|;
name|DbAttribute
name|e2col3
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"NAME"
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|dbEntity2
argument_list|)
decl_stmt|;
name|e2col3
operator|.
name|setMaxLength
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|dbEntity2
operator|.
name|addAttribute
argument_list|(
name|e2col3
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbEntity2
argument_list|)
expr_stmt|;
comment|// create db relationships
name|DbRelationship
name|rel1To2
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"rel1To2"
argument_list|)
decl_stmt|;
name|rel1To2
operator|.
name|setSourceEntity
argument_list|(
name|dbEntity1
argument_list|)
expr_stmt|;
name|rel1To2
operator|.
name|setTargetEntity
argument_list|(
name|dbEntity2
argument_list|)
expr_stmt|;
name|rel1To2
operator|.
name|setToMany
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|rel1To2
operator|.
name|addJoin
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|rel1To2
argument_list|,
name|e1col1
operator|.
name|getName
argument_list|()
argument_list|,
name|e2col2
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|dbEntity1
operator|.
name|addRelationship
argument_list|(
name|rel1To2
argument_list|)
expr_stmt|;
name|DbRelationship
name|rel2To1
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"rel2To1"
argument_list|)
decl_stmt|;
name|rel2To1
operator|.
name|setSourceEntity
argument_list|(
name|dbEntity2
argument_list|)
expr_stmt|;
name|rel2To1
operator|.
name|setTargetEntity
argument_list|(
name|dbEntity1
argument_list|)
expr_stmt|;
name|rel2To1
operator|.
name|setToMany
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|rel2To1
operator|.
name|addJoin
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|rel2To1
argument_list|,
name|e2col2
operator|.
name|getName
argument_list|()
argument_list|,
name|e1col1
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|dbEntity2
operator|.
name|addRelationship
argument_list|(
name|rel2To1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|rel1To2
argument_list|,
name|rel2To1
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|rel2To1
argument_list|,
name|rel1To2
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|4
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// create ObjEntities
name|ObjEntity
name|objEntity1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"NewTable"
argument_list|)
decl_stmt|;
name|objEntity1
operator|.
name|setDbEntity
argument_list|(
name|dbEntity1
argument_list|)
expr_stmt|;
name|ObjAttribute
name|oatr1
init|=
operator|new
name|ObjAttribute
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|oatr1
operator|.
name|setDbAttributePath
argument_list|(
name|e1col2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|oatr1
operator|.
name|setType
argument_list|(
literal|"java.lang.String"
argument_list|)
expr_stmt|;
name|objEntity1
operator|.
name|addAttribute
argument_list|(
name|oatr1
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|objEntity1
argument_list|)
expr_stmt|;
name|ObjEntity
name|objEntity2
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"NewTable2"
argument_list|)
decl_stmt|;
name|objEntity2
operator|.
name|setDbEntity
argument_list|(
name|dbEntity2
argument_list|)
expr_stmt|;
name|ObjAttribute
name|o2a1
init|=
operator|new
name|ObjAttribute
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|o2a1
operator|.
name|setDbAttributePath
argument_list|(
name|e2col3
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|o2a1
operator|.
name|setType
argument_list|(
literal|"java.lang.String"
argument_list|)
expr_stmt|;
name|objEntity2
operator|.
name|addAttribute
argument_list|(
name|o2a1
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|objEntity2
argument_list|)
expr_stmt|;
comment|// create ObjRelationships
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|objEntity1
operator|.
name|getRelationships
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
name|objEntity2
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjRelationship
name|objRel1To2
init|=
operator|new
name|ObjRelationship
argument_list|(
literal|"objRel1To2"
argument_list|)
decl_stmt|;
name|objRel1To2
operator|.
name|addDbRelationship
argument_list|(
name|rel1To2
argument_list|)
expr_stmt|;
name|objRel1To2
operator|.
name|setSourceEntity
argument_list|(
name|objEntity1
argument_list|)
expr_stmt|;
name|objRel1To2
operator|.
name|setTargetEntity
argument_list|(
name|objEntity2
argument_list|)
expr_stmt|;
name|objEntity1
operator|.
name|addRelationship
argument_list|(
name|objRel1To2
argument_list|)
expr_stmt|;
name|ObjRelationship
name|objRel2To1
init|=
operator|new
name|ObjRelationship
argument_list|(
literal|"objRel2To1"
argument_list|)
decl_stmt|;
name|objRel2To1
operator|.
name|addDbRelationship
argument_list|(
name|rel2To1
argument_list|)
expr_stmt|;
name|objRel2To1
operator|.
name|setSourceEntity
argument_list|(
name|objEntity2
argument_list|)
expr_stmt|;
name|objRel2To1
operator|.
name|setTargetEntity
argument_list|(
name|objEntity1
argument_list|)
expr_stmt|;
name|objEntity2
operator|.
name|addRelationship
argument_list|(
name|objRel2To1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objEntity1
operator|.
name|getRelationships
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
name|objEntity2
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|objRel1To2
argument_list|,
name|objRel2To1
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|objRel2To1
argument_list|,
name|objRel1To2
operator|.
name|getReverseRelationship
argument_list|()
argument_list|)
expr_stmt|;
comment|// remove relationship and fk from model, merge to db and read to model
name|dbEntity2
operator|.
name|removeRelationship
argument_list|(
name|rel2To1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|dbEntity1
operator|.
name|removeRelationship
argument_list|(
name|rel1To2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|dbEntity2
operator|.
name|removeAttribute
argument_list|(
name|e2col2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|MergerToken
argument_list|>
name|tokens
init|=
name|createMergeTokens
argument_list|()
decl_stmt|;
name|assertTokens
argument_list|(
name|tokens
argument_list|,
literal|2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|MergerToken
name|token
range|:
name|tokens
control|)
block|{
if|if
condition|(
name|token
operator|.
name|getDirection
argument_list|()
operator|.
name|isToDb
argument_list|()
condition|)
block|{
name|execute
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|dbEntity2
operator|.
name|addRelationship
argument_list|(
name|rel2To1
argument_list|)
expr_stmt|;
name|dbEntity1
operator|.
name|addRelationship
argument_list|(
name|rel1To2
argument_list|)
expr_stmt|;
name|dbEntity2
operator|.
name|addAttribute
argument_list|(
name|e2col2
argument_list|)
expr_stmt|;
comment|// try do use the merger to remove the relationship in the model
name|tokens
operator|=
name|createMergeTokens
argument_list|()
expr_stmt|;
name|assertTokens
argument_list|(
name|tokens
argument_list|,
literal|2
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// TODO: reversing the following two tokens should also reverse the
comment|// order
name|MergerToken
name|token0
init|=
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|createReverse
argument_list|(
name|mergerFactory
argument_list|()
argument_list|)
decl_stmt|;
name|MergerToken
name|token1
init|=
name|tokens
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|createReverse
argument_list|(
name|mergerFactory
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|token0
operator|instanceof
name|DropColumnToModel
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|token1
operator|instanceof
name|DropRelationshipToModel
argument_list|)
expr_stmt|;
name|execute
argument_list|(
name|token1
argument_list|)
expr_stmt|;
name|execute
argument_list|(
name|token0
argument_list|)
expr_stmt|;
comment|// check after merging
name|assertNull
argument_list|(
name|dbEntity2
operator|.
name|getAttribute
argument_list|(
name|e2col2
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|dbEntity1
operator|.
name|getRelationships
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
name|dbEntity2
operator|.
name|getRelationships
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
name|objEntity1
operator|.
name|getRelationships
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
name|objEntity2
operator|.
name|getRelationships
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// clear up
name|dbEntity1
operator|.
name|removeRelationship
argument_list|(
name|rel1To2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|dbEntity2
operator|.
name|removeRelationship
argument_list|(
name|rel2To1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeObjEntity
argument_list|(
name|objEntity1
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeDbEntity
argument_list|(
name|dbEntity1
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeObjEntity
argument_list|(
name|objEntity2
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeDbEntity
argument_list|(
name|dbEntity2
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|refreshMappingCache
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getObjEntity
argument_list|(
name|objEntity1
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDbEntity
argument_list|(
name|dbEntity1
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getObjEntity
argument_list|(
name|objEntity2
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDbEntity
argument_list|(
name|dbEntity2
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|getDbEntities
argument_list|()
operator|.
name|contains
argument_list|(
name|dbEntity1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|getDbEntities
argument_list|()
operator|.
name|contains
argument_list|(
name|dbEntity2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

