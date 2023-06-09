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
name|junit
operator|.
name|Before
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
name|QueryDescriptorLoaderTest
block|{
specifier|protected
name|QueryDescriptorLoader
name|builder
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|=
operator|new
name|QueryDescriptorLoader
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
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
annotation|@
name|Test
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
name|QueryDescriptor
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
annotation|@
name|Test
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
name|QueryDescriptor
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
annotation|@
name|Test
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
name|QueryDescriptor
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

