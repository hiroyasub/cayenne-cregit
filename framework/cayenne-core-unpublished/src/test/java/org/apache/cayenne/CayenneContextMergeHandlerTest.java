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
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
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
name|di
operator|.
name|Inject
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
name|GraphEvent
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
name|testdo
operator|.
name|mt
operator|.
name|ClientMtTable1
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
name|client
operator|.
name|ClientCase
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
name|ClientCase
operator|.
name|MULTI_TIER_PROJECT
argument_list|)
specifier|public
class|class
name|CayenneContextMergeHandlerTest
extends|extends
name|ClientCase
block|{
annotation|@
name|Inject
specifier|protected
name|CayenneContext
name|context
decl_stmt|;
specifier|public
name|void
name|testShouldProcessEvent
parameter_list|()
block|{
name|CayenneContextMergeHandler
name|handler
init|=
operator|new
name|CayenneContextMergeHandler
argument_list|(
name|context
argument_list|)
decl_stmt|;
comment|// 1. Our context initiated the sync:
comment|// src == channel&& postedBy == context
name|GraphEvent
name|e1
init|=
operator|new
name|GraphEvent
argument_list|(
name|context
operator|.
name|getChannel
argument_list|()
argument_list|,
name|context
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|handler
operator|.
name|shouldProcessEvent
argument_list|(
name|e1
argument_list|)
argument_list|)
expr_stmt|;
comment|// 2. Another context initiated the sync:
comment|// postedBy != context&& source == channel
name|GraphEvent
name|e2
init|=
operator|new
name|GraphEvent
argument_list|(
name|context
operator|.
name|getChannel
argument_list|()
argument_list|,
name|mock
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|handler
operator|.
name|shouldProcessEvent
argument_list|(
name|e2
argument_list|)
argument_list|)
expr_stmt|;
comment|// 2.1 Another object initiated the sync:
comment|// postedBy != context&& source == channel
name|GraphEvent
name|e21
init|=
operator|new
name|GraphEvent
argument_list|(
name|context
operator|.
name|getChannel
argument_list|()
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|handler
operator|.
name|shouldProcessEvent
argument_list|(
name|e21
argument_list|)
argument_list|)
expr_stmt|;
comment|// 3. Another channel initiated the sync:
comment|// postedBy == ?&& source != channel
name|GraphEvent
name|e3
init|=
operator|new
name|GraphEvent
argument_list|(
operator|new
name|MockDataChannel
argument_list|()
argument_list|,
operator|new
name|Object
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|handler
operator|.
name|shouldProcessEvent
argument_list|(
name|e3
argument_list|)
argument_list|)
expr_stmt|;
comment|// 4. inactive
name|GraphEvent
name|e4
init|=
operator|new
name|GraphEvent
argument_list|(
name|context
operator|.
name|getChannel
argument_list|()
argument_list|,
name|mock
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|handler
operator|.
name|active
operator|=
literal|false
expr_stmt|;
name|assertFalse
argument_list|(
name|handler
operator|.
name|shouldProcessEvent
argument_list|(
name|e4
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNodePropertyChanged
parameter_list|()
block|{
name|ClientMtTable1
name|o1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|ClientMtTable1
operator|.
name|class
argument_list|)
decl_stmt|;
name|CayenneContextMergeHandler
name|handler
init|=
operator|new
name|CayenneContextMergeHandler
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|o1
operator|.
name|getGlobalAttribute1Direct
argument_list|()
argument_list|)
expr_stmt|;
name|handler
operator|.
name|nodePropertyChanged
argument_list|(
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1_PROPERTY
argument_list|,
literal|null
argument_list|,
literal|"abc"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|o1
operator|.
name|getGlobalAttribute1Direct
argument_list|()
argument_list|)
expr_stmt|;
name|handler
operator|.
name|nodePropertyChanged
argument_list|(
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1_PROPERTY
argument_list|,
literal|"abc"
argument_list|,
literal|"xyz"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xyz"
argument_list|,
name|o1
operator|.
name|getGlobalAttribute1Direct
argument_list|()
argument_list|)
expr_stmt|;
comment|// block if old value is different
name|handler
operator|.
name|nodePropertyChanged
argument_list|(
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1_PROPERTY
argument_list|,
literal|"123"
argument_list|,
literal|"mnk"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xyz"
argument_list|,
name|o1
operator|.
name|getGlobalAttribute1Direct
argument_list|()
argument_list|)
expr_stmt|;
name|handler
operator|.
name|nodePropertyChanged
argument_list|(
name|o1
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|ClientMtTable1
operator|.
name|GLOBAL_ATTRIBUTE1_PROPERTY
argument_list|,
literal|"xyz"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|o1
operator|.
name|getGlobalAttribute1Direct
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
