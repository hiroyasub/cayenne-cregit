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
name|modeler
operator|.
name|event
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
name|DataChannelDescriptor
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DomainDisplayEventTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testDomain
parameter_list|()
throws|throws
name|Exception
block|{
name|DataChannelDescriptor
name|d
init|=
operator|new
name|DataChannelDescriptor
argument_list|()
decl_stmt|;
name|DomainDisplayEvent
name|e
init|=
operator|new
name|DomainDisplayEvent
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
name|d
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|d
argument_list|,
name|e
operator|.
name|getDomain
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

