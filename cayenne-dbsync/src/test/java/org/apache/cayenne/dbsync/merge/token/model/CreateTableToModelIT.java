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
name|dbsync
operator|.
name|merge
operator|.
name|token
operator|.
name|model
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
name|assertFalse
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
name|assertNull
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
name|dbsync
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
name|dbsync
operator|.
name|merge
operator|.
name|token
operator|.
name|MergerToken
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
name|ObjEntity
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
name|CreateTableToModelIT
extends|extends
name|MergeCase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testAddTable
parameter_list|()
throws|throws
name|Exception
block|{
name|dropTableIfPresent
argument_list|(
literal|"NEW_TABLE"
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
name|dbEntity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"NEW_TABLE"
argument_list|)
decl_stmt|;
name|DbAttribute
name|column1
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
name|dbEntity
argument_list|)
decl_stmt|;
name|column1
operator|.
name|setMandatory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|column1
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|addAttribute
argument_list|(
name|column1
argument_list|)
expr_stmt|;
name|DbAttribute
name|column2
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
name|dbEntity
argument_list|)
decl_stmt|;
name|column2
operator|.
name|setMaxLength
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|column2
operator|.
name|setMandatory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|addAttribute
argument_list|(
name|column2
argument_list|)
expr_stmt|;
comment|// for the new entity to the db
name|execute
argument_list|(
name|mergerFactory
argument_list|()
operator|.
name|createCreateTableToDb
argument_list|(
name|dbEntity
argument_list|)
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
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|MergerToken
name|token
init|=
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
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
name|token
operator|=
name|token
operator|.
name|createReverse
argument_list|(
name|mergerFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|token
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|token
operator|instanceof
name|CreateTableToModel
argument_list|)
expr_stmt|;
name|execute
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|ObjEntity
name|objEntity
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ObjEntity
name|candidate
range|:
name|map
operator|.
name|getObjEntities
argument_list|()
control|)
block|{
if|if
condition|(
name|dbEntity
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|candidate
operator|.
name|getDbEntityName
argument_list|()
argument_list|)
condition|)
block|{
name|objEntity
operator|=
name|candidate
expr_stmt|;
break|break;
block|}
block|}
name|assertNotNull
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|objEntity
operator|.
name|getClassName
argument_list|()
argument_list|,
name|map
operator|.
name|getDefaultPackage
argument_list|()
operator|+
literal|"."
operator|+
name|objEntity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|objEntity
operator|.
name|getSuperClassName
argument_list|()
argument_list|,
name|map
operator|.
name|getDefaultSuperclass
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objEntity
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
literal|"java.lang.String"
argument_list|,
name|objEntity
operator|.
name|getAttributes
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
comment|// clear up
comment|// fix psql case issue
name|map
operator|.
name|removeDbEntity
argument_list|(
name|objEntity
operator|.
name|getDbEntity
argument_list|()
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
name|objEntity
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
name|dbEntity
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
name|objEntity
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
name|dbEntity
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
name|dbEntity
argument_list|)
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|1
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

