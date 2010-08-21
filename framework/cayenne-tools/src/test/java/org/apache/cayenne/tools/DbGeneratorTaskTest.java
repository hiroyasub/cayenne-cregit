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
name|tools
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
name|dba
operator|.
name|sqlserver
operator|.
name|SQLServerAdapter
import|;
end_import

begin_class
specifier|public
class|class
name|DbGeneratorTaskTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testSetUserName
parameter_list|()
throws|throws
name|Exception
block|{
name|DbGeneratorTask
name|task
init|=
operator|new
name|DbGeneratorTask
argument_list|()
decl_stmt|;
name|task
operator|.
name|setUserName
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"abc"
argument_list|,
name|task
operator|.
name|userName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSetPassword
parameter_list|()
throws|throws
name|Exception
block|{
name|DbGeneratorTask
name|task
init|=
operator|new
name|DbGeneratorTask
argument_list|()
decl_stmt|;
name|task
operator|.
name|setPassword
argument_list|(
literal|"xyz"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xyz"
argument_list|,
name|task
operator|.
name|password
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSetAdapter
parameter_list|()
throws|throws
name|Exception
block|{
name|DbGeneratorTask
name|task
init|=
operator|new
name|DbGeneratorTask
argument_list|()
decl_stmt|;
name|task
operator|.
name|setAdapter
argument_list|(
name|SQLServerAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|task
operator|.
name|adapter
operator|instanceof
name|SQLServerAdapter
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSetUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|DbGeneratorTask
name|task
init|=
operator|new
name|DbGeneratorTask
argument_list|()
decl_stmt|;
name|task
operator|.
name|setUrl
argument_list|(
literal|"jdbc:///"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jdbc:///"
argument_list|,
name|task
operator|.
name|url
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

