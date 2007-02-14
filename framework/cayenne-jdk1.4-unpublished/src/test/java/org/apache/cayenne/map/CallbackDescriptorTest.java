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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|CallbackDescriptorTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testConstructor
parameter_list|()
block|{
name|CallbackDescriptor
name|m
init|=
operator|new
name|CallbackDescriptor
argument_list|(
name|CallbackMap
operator|.
name|POST_LOAD
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|CallbackMap
operator|.
name|POST_LOAD
argument_list|,
name|m
operator|.
name|getCallbackType
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
operator|new
name|CallbackDescriptor
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Must have thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
specifier|public
name|void
name|testAddCallbackMethod
parameter_list|()
block|{
name|CallbackDescriptor
name|m
init|=
operator|new
name|CallbackDescriptor
argument_list|(
name|CallbackMap
operator|.
name|PRE_PERSIST
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|m
operator|.
name|getCallbackMethods
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|addCallbackMethod
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|m
operator|.
name|getCallbackMethods
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|addCallbackMethod
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|m
operator|.
name|getCallbackMethods
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|addCallbackMethod
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|m
operator|.
name|getCallbackMethods
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

