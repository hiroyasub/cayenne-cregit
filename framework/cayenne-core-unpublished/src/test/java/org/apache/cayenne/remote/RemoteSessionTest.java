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
name|remote
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
name|remote
operator|.
name|RemoteSession
import|;
end_import

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
name|RemoteSessionTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testConstructor1
parameter_list|()
block|{
name|RemoteSession
name|descriptor
init|=
operator|new
name|RemoteSession
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|descriptor
operator|.
name|getSessionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|descriptor
operator|.
name|isServerEventsEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConstructor2
parameter_list|()
block|{
name|RemoteSession
name|descriptor
init|=
operator|new
name|RemoteSession
argument_list|(
literal|"abc"
argument_list|,
literal|"factory"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|descriptor
operator|.
name|getSessionId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|descriptor
operator|.
name|isServerEventsEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testHashCode
parameter_list|()
block|{
name|RemoteSession
name|d1
init|=
operator|new
name|RemoteSession
argument_list|(
literal|"1"
argument_list|)
decl_stmt|;
name|RemoteSession
name|d2
init|=
operator|new
name|RemoteSession
argument_list|(
literal|"1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|d1
operator|.
name|hashCode
argument_list|()
argument_list|,
name|d1
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|d1
operator|.
name|hashCode
argument_list|()
argument_list|,
name|d2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|d2
operator|.
name|setName
argument_list|(
literal|"some name"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|d1
operator|.
name|hashCode
argument_list|()
argument_list|,
name|d2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|RemoteSession
name|d3
init|=
operator|new
name|RemoteSession
argument_list|(
literal|"2"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|d1
operator|.
name|hashCode
argument_list|()
operator|==
name|d3
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
