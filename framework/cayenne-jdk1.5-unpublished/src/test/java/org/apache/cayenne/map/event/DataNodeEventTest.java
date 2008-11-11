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
name|access
operator|.
name|DataNode
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|DataNodeEventTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testNewName
parameter_list|()
throws|throws
name|Exception
block|{
name|MapEvent
name|event
init|=
operator|new
name|DataNodeEvent
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|DataNode
argument_list|(
literal|"someName"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"someName"
argument_list|,
name|event
operator|.
name|getNewName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNoNameChange
parameter_list|()
throws|throws
name|Exception
block|{
name|MapEvent
name|event
init|=
operator|new
name|DataNodeEvent
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|DataNode
argument_list|(
literal|"someName"
argument_list|)
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|event
operator|.
name|isNameChange
argument_list|()
argument_list|)
expr_stmt|;
name|event
operator|.
name|setOldName
argument_list|(
literal|"someOldName"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|event
operator|.
name|isNameChange
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNameChange
parameter_list|()
throws|throws
name|Exception
block|{
name|MapEvent
name|event
init|=
operator|new
name|DataNodeEvent
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
operator|new
name|DataNode
argument_list|(
literal|"someName"
argument_list|)
argument_list|,
literal|"someOldName"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"someName"
argument_list|,
name|event
operator|.
name|getNewName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|event
operator|.
name|isNameChange
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

