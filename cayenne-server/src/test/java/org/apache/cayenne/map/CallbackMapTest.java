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
name|CallbackMapTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetCallbacks
parameter_list|()
block|{
name|CallbackDescriptor
index|[]
name|callbacks
init|=
operator|new
name|CallbackMap
argument_list|()
operator|.
name|getCallbacks
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|LifecycleEvent
operator|.
name|values
argument_list|()
operator|.
name|length
argument_list|,
name|callbacks
operator|.
name|length
argument_list|)
expr_stmt|;
comment|// assert correct callback ordering
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|callbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|LifecycleEvent
operator|.
name|values
argument_list|()
index|[
name|i
index|]
argument_list|,
name|callbacks
index|[
name|i
index|]
operator|.
name|getCallbackType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

