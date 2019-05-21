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
name|modeler
operator|.
name|event
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
name|map
operator|.
name|event
operator|.
name|MapEvent
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
name|assertSame
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ModelerEventTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testConstructor1
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|src
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|MapEvent
name|e
init|=
operator|new
name|TestMapEvent
argument_list|(
name|src
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|src
argument_list|,
name|e
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testId
parameter_list|()
throws|throws
name|Exception
block|{
name|MapEvent
name|e
init|=
operator|new
name|TestMapEvent
argument_list|(
operator|new
name|Object
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|MapEvent
operator|.
name|CHANGE
argument_list|,
name|e
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|setId
argument_list|(
name|MapEvent
operator|.
name|ADD
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MapEvent
operator|.
name|ADD
argument_list|,
name|e
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
class|class
name|TestMapEvent
extends|extends
name|MapEvent
block|{
specifier|public
name|TestMapEvent
parameter_list|(
name|Object
name|source
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
comment|// TODO Auto-generated constructor stub
block|}
specifier|public
name|TestMapEvent
parameter_list|(
name|Object
name|source
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
comment|// TODO Auto-generated constructor stub
block|}
specifier|public
name|String
name|getNewName
parameter_list|()
block|{
return|return
literal|""
return|;
block|}
block|}
block|}
end_class

end_unit

