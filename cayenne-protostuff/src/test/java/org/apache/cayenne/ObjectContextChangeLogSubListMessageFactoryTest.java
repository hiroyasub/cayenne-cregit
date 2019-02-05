begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
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
name|graph
operator|.
name|CompoundDiff
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
name|graph
operator|.
name|NodeCreateOperation
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
name|rop
operator|.
name|ROPSerializationService
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
name|rop
operator|.
name|protostuff
operator|.
name|ProtostuffProperties
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
name|rop
operator|.
name|protostuff
operator|.
name|ProtostuffROPSerializationService
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

begin_class
specifier|public
class|class
name|ObjectContextChangeLogSubListMessageFactoryTest
extends|extends
name|ProtostuffProperties
block|{
specifier|private
name|ROPSerializationService
name|serializationService
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
name|serializationService
operator|=
operator|new
name|ProtostuffROPSerializationService
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetDiffsSerializable
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectContextChangeLog
name|recorder
init|=
operator|new
name|ObjectContextChangeLog
argument_list|()
decl_stmt|;
name|recorder
operator|.
name|addOperation
argument_list|(
operator|new
name|NodeCreateOperation
argument_list|(
name|ObjectId
operator|.
name|of
argument_list|(
literal|"test"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|CompoundDiff
name|diff
init|=
operator|(
name|CompoundDiff
operator|)
name|recorder
operator|.
name|getDiffs
argument_list|()
decl_stmt|;
name|byte
index|[]
name|data
init|=
name|serializationService
operator|.
name|serialize
argument_list|(
name|diff
argument_list|)
decl_stmt|;
name|CompoundDiff
name|diff0
init|=
name|serializationService
operator|.
name|deserialize
argument_list|(
name|data
argument_list|,
name|CompoundDiff
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|diff0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|diff0
operator|.
name|getDiffs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

