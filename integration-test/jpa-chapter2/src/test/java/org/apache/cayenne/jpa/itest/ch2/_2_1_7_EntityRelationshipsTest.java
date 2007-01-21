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
name|jpa
operator|.
name|itest
operator|.
name|ch2
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
name|itest
operator|.
name|jpa
operator|.
name|EntityManagerCase
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
name|jpa
operator|.
name|itest
operator|.
name|ch2
operator|.
name|entity
operator|.
name|CollectionFieldPersistenceEntity
import|;
end_import

begin_class
specifier|public
class|class
name|_2_1_7_EntityRelationshipsTest
extends|extends
name|EntityManagerCase
block|{
specifier|public
name|void
name|testEmptyCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"HelperEntity1"
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"CollectionFieldPersistenceEntity"
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"CollectionFieldPersistenceEntity"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
operator|new
name|Integer
argument_list|(
literal|5
argument_list|)
block|}
argument_list|)
expr_stmt|;
name|CollectionFieldPersistenceEntity
name|o1
init|=
name|getEntityManager
argument_list|()
operator|.
name|find
argument_list|(
name|CollectionFieldPersistenceEntity
operator|.
name|class
argument_list|,
operator|new
name|Integer
argument_list|(
literal|5
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|o1
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|o1
operator|.
name|getCollection
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO: andrus 8/10/2006 - fails because of pk handling issues
specifier|public
name|void
name|testNonEmptyCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"HelperEntity1"
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|deleteAll
argument_list|(
literal|"CollectionFieldPersistenceEntity"
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"CollectionFieldPersistenceEntity"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
operator|new
name|Integer
argument_list|(
literal|5
argument_list|)
block|}
argument_list|)
expr_stmt|;
name|getDbHelper
argument_list|()
operator|.
name|insert
argument_list|(
literal|"HelperEntity1"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"entity_id"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
operator|new
name|Integer
argument_list|(
literal|4
argument_list|)
block|,
operator|new
name|Integer
argument_list|(
literal|5
argument_list|)
block|}
argument_list|)
expr_stmt|;
name|CollectionFieldPersistenceEntity
name|o1
init|=
name|getEntityManager
argument_list|()
operator|.
name|find
argument_list|(
name|CollectionFieldPersistenceEntity
operator|.
name|class
argument_list|,
operator|new
name|Integer
argument_list|(
literal|5
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|o1
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|o1
operator|.
name|getCollection
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

