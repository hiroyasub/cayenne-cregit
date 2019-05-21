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
operator|.
name|event
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
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
name|assertNull
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

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|MapEventTest
block|{
annotation|@
name|Test
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
name|MapEventFixture
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
literal|"someName"
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
name|assertFalse
argument_list|(
name|event
operator|.
name|isNameChange
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
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
name|MapEventFixture
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
literal|"someName"
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
annotation|@
name|Test
specifier|public
name|void
name|testOldName
parameter_list|()
throws|throws
name|Exception
block|{
name|MapEvent
name|event
init|=
operator|new
name|MapEventFixture
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|,
literal|"someName"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|event
operator|.
name|getOldName
argument_list|()
argument_list|)
expr_stmt|;
name|event
operator|.
name|setOldName
argument_list|(
literal|"oldName"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"oldName"
argument_list|,
name|event
operator|.
name|getOldName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
class|class
name|MapEventFixture
extends|extends
name|MapEvent
block|{
name|String
name|newName
decl_stmt|;
specifier|public
name|MapEventFixture
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|newName
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|this
operator|.
name|newName
operator|=
name|newName
expr_stmt|;
block|}
specifier|public
name|MapEventFixture
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|newName
parameter_list|,
name|String
name|oldName
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|,
name|oldName
argument_list|)
expr_stmt|;
name|this
operator|.
name|newName
operator|=
name|newName
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getNewName
parameter_list|()
block|{
return|return
name|newName
return|;
block|}
block|}
block|}
end_class

end_unit

