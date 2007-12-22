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
name|unit
operator|.
name|BasicCase
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|QueryBuilderTest
extends|extends
name|BasicCase
block|{
specifier|protected
name|QueryLoader
name|builder
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|=
operator|new
name|QueryLoader
argument_list|()
block|{
specifier|public
name|Query
name|getQuery
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
expr_stmt|;
block|}
specifier|public
name|void
name|testSetName
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|.
name|setName
argument_list|(
literal|"aaa"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aaa"
argument_list|,
name|builder
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSetRootInfoDbEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|(
literal|"map"
argument_list|)
decl_stmt|;
name|DbEntity
name|entity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"DB1"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setRoot
argument_list|(
name|map
argument_list|,
name|QueryLoader
operator|.
name|DB_ENTITY_ROOT
argument_list|,
literal|"DB1"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|entity
argument_list|,
name|builder
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSetRootObjEntity
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|(
literal|"map"
argument_list|)
decl_stmt|;
name|ObjEntity
name|entity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"OBJ1"
argument_list|)
decl_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setRoot
argument_list|(
name|map
argument_list|,
name|QueryLoader
operator|.
name|OBJ_ENTITY_ROOT
argument_list|,
literal|"OBJ1"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|entity
argument_list|,
name|builder
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSetRootDataMap
parameter_list|()
throws|throws
name|Exception
block|{
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|(
literal|"map"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setRoot
argument_list|(
name|map
argument_list|,
name|QueryLoader
operator|.
name|DATA_MAP_ROOT
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|map
argument_list|,
name|builder
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

