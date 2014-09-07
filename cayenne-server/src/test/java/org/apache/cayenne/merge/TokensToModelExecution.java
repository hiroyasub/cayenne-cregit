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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DataNode
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
name|DataMap
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|merge
operator|.
name|builders
operator|.
name|ObjectMother
operator|.
name|dataMap
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|merge
operator|.
name|builders
operator|.
name|ObjectMother
operator|.
name|dbAttr
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|merge
operator|.
name|builders
operator|.
name|ObjectMother
operator|.
name|dbEntity
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * @since 3.2.  */
end_comment

begin_class
specifier|public
class|class
name|TokensToModelExecution
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCreateAndDropTable
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|entity
init|=
name|dbEntity
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|DataMap
name|dataMap
init|=
name|dataMap
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|dataMap
operator|.
name|getDbEntityMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataMap
operator|.
name|getObjEntityMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|MergerContext
name|context
init|=
operator|new
name|ExecutingMergerContext
argument_list|(
name|dataMap
argument_list|,
operator|new
name|DataNode
argument_list|()
argument_list|)
decl_stmt|;
name|factory
argument_list|()
operator|.
name|createCreateTableToModel
argument_list|(
name|entity
argument_list|)
operator|.
name|execute
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dataMap
operator|.
name|getDbEntityMap
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
name|dataMap
operator|.
name|getObjEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|entity
argument_list|,
name|dataMap
operator|.
name|getDbEntity
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|factory
argument_list|()
operator|.
name|createDropTableToModel
argument_list|(
name|entity
argument_list|)
operator|.
name|execute
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataMap
operator|.
name|getDbEntityMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataMap
operator|.
name|getObjEntityMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateAndDropColumn
parameter_list|()
throws|throws
name|Exception
block|{
name|DbAttribute
name|attr
init|=
name|dbAttr
argument_list|(
literal|"attr"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|DbEntity
name|entity
init|=
name|dbEntity
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|DataMap
name|dataMap
init|=
name|dataMap
argument_list|()
operator|.
name|with
argument_list|(
name|entity
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dataMap
operator|.
name|getDbEntityMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataMap
operator|.
name|getObjEntityMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|MergerContext
name|context
init|=
operator|new
name|ExecutingMergerContext
argument_list|(
name|dataMap
argument_list|,
operator|new
name|DataNode
argument_list|()
argument_list|)
decl_stmt|;
name|factory
argument_list|()
operator|.
name|createAddColumnToModel
argument_list|(
name|entity
argument_list|,
name|attr
argument_list|)
operator|.
name|execute
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dataMap
operator|.
name|getDbEntityMap
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
name|entity
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
name|attr
argument_list|,
name|entity
operator|.
name|getAttribute
argument_list|(
name|attr
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|factory
argument_list|()
operator|.
name|createDropColumnToModel
argument_list|(
name|entity
argument_list|,
name|attr
argument_list|)
operator|.
name|execute
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|dataMap
operator|.
name|getDbEntityMap
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataMap
operator|.
name|getObjEntityMap
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|MergerFactory
name|factory
parameter_list|()
block|{
return|return
operator|new
name|MergerFactory
argument_list|()
return|;
block|}
block|}
end_class

end_unit

