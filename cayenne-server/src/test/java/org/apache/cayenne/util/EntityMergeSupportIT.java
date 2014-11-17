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
name|util
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
name|DeleteRule
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
name|merge
operator|.
name|MergeCase
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
name|Arrays
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
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
name|EntityMergeSupportIT
extends|extends
name|MergeCase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testMerging
parameter_list|()
block|{
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
name|map
operator|.
name|addObjEntity
argument_list|(
name|objEntity2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|EntityMergeSupport
argument_list|(
name|map
argument_list|)
operator|.
name|synchronizeWithDbEntities
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|objEntity1
argument_list|,
name|objEntity2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|objEntity1
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|objEntity1
operator|.
name|getRelationship
argument_list|(
literal|"rel1To2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|objEntity2
operator|.
name|getRelationship
argument_list|(
literal|"rel2To1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|objEntity1
operator|.
name|getRelationship
argument_list|(
literal|"rel1To2"
argument_list|)
operator|.
name|getDeleteRule
argument_list|()
argument_list|,
name|DeleteRule
operator|.
name|DEFAULT_DELETE_RULE_TO_MANY
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|objEntity2
operator|.
name|getRelationship
argument_list|(
literal|"rel2To1"
argument_list|)
operator|.
name|getDeleteRule
argument_list|()
argument_list|,
name|DeleteRule
operator|.
name|DEFAULT_DELETE_RULE_TO_ONE
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
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

