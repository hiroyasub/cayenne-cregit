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
name|lifecycle
operator|.
name|id
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
name|ObjectId
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|lifecycle
operator|.
name|db
operator|.
name|E1
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

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
name|assertEquals
import|;
end_import

begin_class
specifier|public
class|class
name|IdCoderTest
block|{
specifier|private
name|ServerRuntime
name|runtime
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
name|runtime
operator|=
name|ServerRuntime
operator|.
name|builder
argument_list|()
operator|.
name|addConfig
argument_list|(
literal|"cayenne-lifecycle.xml"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|runtime
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetStringId
parameter_list|()
block|{
name|IdCoder
name|handler
init|=
operator|new
name|IdCoder
argument_list|(
name|runtime
operator|.
name|getChannel
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|E1
name|e1
init|=
operator|new
name|E1
argument_list|()
decl_stmt|;
name|e1
operator|.
name|setObjectId
argument_list|(
name|ObjectId
operator|.
name|of
argument_list|(
literal|"E1"
argument_list|,
literal|"ID"
argument_list|,
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"E1:5"
argument_list|,
name|handler
operator|.
name|getStringId
argument_list|(
name|e1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetStringId_ObjectId
parameter_list|()
block|{
name|IdCoder
name|handler
init|=
operator|new
name|IdCoder
argument_list|(
name|runtime
operator|.
name|getChannel
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"E1:5"
argument_list|,
name|handler
operator|.
name|getStringId
argument_list|(
name|ObjectId
operator|.
name|of
argument_list|(
literal|"E1"
argument_list|,
literal|"ID"
argument_list|,
literal|5
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetStringId_Temp
parameter_list|()
block|{
name|IdCoder
name|handler
init|=
operator|new
name|IdCoder
argument_list|(
name|runtime
operator|.
name|getChannel
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|key
init|=
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|10
block|,
literal|100
block|}
decl_stmt|;
name|E1
name|e1
init|=
operator|new
name|E1
argument_list|()
decl_stmt|;
name|e1
operator|.
name|setObjectId
argument_list|(
name|ObjectId
operator|.
name|of
argument_list|(
literal|"E1"
argument_list|,
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|".E1:01020A64"
argument_list|,
name|handler
operator|.
name|getStringId
argument_list|(
name|e1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetObjectId_Temp
parameter_list|()
block|{
name|IdCoder
name|handler
init|=
operator|new
name|IdCoder
argument_list|(
name|runtime
operator|.
name|getChannel
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|key
init|=
operator|new
name|byte
index|[]
block|{
literal|1
block|,
operator|(
name|byte
operator|)
literal|0xD7
block|,
literal|10
block|,
literal|100
block|}
decl_stmt|;
name|ObjectId
name|decoded
init|=
name|handler
operator|.
name|getObjectId
argument_list|(
literal|".E1:01D70A64"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ObjectId
operator|.
name|of
argument_list|(
literal|"E1"
argument_list|,
name|key
argument_list|)
argument_list|,
name|decoded
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSringId_TempWithReplacement
parameter_list|()
block|{
name|IdCoder
name|handler
init|=
operator|new
name|IdCoder
argument_list|(
name|runtime
operator|.
name|getChannel
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|key
init|=
operator|new
name|byte
index|[]
block|{
literal|5
block|,
literal|2
block|,
literal|11
block|,
literal|99
block|}
decl_stmt|;
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|of
argument_list|(
literal|"E1"
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|id
operator|.
name|getReplacementIdMap
argument_list|()
operator|.
name|put
argument_list|(
literal|"ID"
argument_list|,
literal|6
argument_list|)
expr_stmt|;
name|E1
name|e1
init|=
operator|new
name|E1
argument_list|()
decl_stmt|;
name|e1
operator|.
name|setObjectId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"E1:6"
argument_list|,
name|handler
operator|.
name|getStringId
argument_list|(
name|e1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

