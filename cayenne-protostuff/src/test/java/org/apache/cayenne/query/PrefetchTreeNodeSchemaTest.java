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
operator|.
name|query
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
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|PrefetchTreeNodeSchemaTest
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
name|testPrefetchTreeNodeSchema
parameter_list|()
throws|throws
name|IOException
block|{
name|PrefetchTreeNode
name|parent
init|=
operator|new
name|PrefetchTreeNode
argument_list|(
literal|null
argument_list|,
literal|"parent"
argument_list|)
decl_stmt|;
name|PrefetchTreeNode
name|child
init|=
operator|new
name|PrefetchTreeNode
argument_list|(
name|parent
argument_list|,
literal|"child"
argument_list|)
decl_stmt|;
name|parent
operator|.
name|addChild
argument_list|(
name|child
argument_list|)
expr_stmt|;
name|byte
index|[]
name|data
init|=
name|serializationService
operator|.
name|serialize
argument_list|(
name|parent
argument_list|)
decl_stmt|;
name|PrefetchTreeNode
name|parent0
init|=
name|serializationService
operator|.
name|deserialize
argument_list|(
name|data
argument_list|,
name|PrefetchTreeNode
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|parent0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|parent0
operator|.
name|hasChildren
argument_list|()
argument_list|)
expr_stmt|;
name|PrefetchTreeNode
name|child0
init|=
name|parent0
operator|.
name|getChild
argument_list|(
literal|"child"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|child0
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|child0
operator|.
name|parent
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|child0
operator|.
name|parent
argument_list|,
name|parent0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

