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
name|project
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|DataNodeFileTest
extends|extends
name|TestCase
block|{
specifier|protected
name|DataNodeFile
name|dnf
decl_stmt|;
specifier|protected
name|DataNode
name|node
decl_stmt|;
specifier|protected
name|Project
name|pr
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|pr
operator|=
operator|new
name|TstProject
argument_list|(
operator|new
name|File
argument_list|(
literal|"xyz"
argument_list|)
argument_list|)
expr_stmt|;
name|node
operator|=
operator|new
name|DataNode
argument_list|(
literal|"n1"
argument_list|)
expr_stmt|;
name|dnf
operator|=
operator|new
name|DataNodeFile
argument_list|(
name|pr
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetObject
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSame
argument_list|(
name|node
argument_list|,
name|dnf
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetObjectName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|node
operator|.
name|getName
argument_list|()
argument_list|,
name|dnf
operator|.
name|getObjectName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetFileName
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|node
operator|.
name|getName
argument_list|()
operator|+
literal|".driver.xml"
argument_list|,
name|dnf
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

