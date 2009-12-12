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
name|project2
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
name|Configurable
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
name|ConfigurationVisitor
import|;
end_import

begin_class
specifier|public
class|class
name|ProjectTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testRootNode
parameter_list|()
block|{
name|Configurable
name|object
init|=
operator|new
name|Configurable
argument_list|()
block|{
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|acceptVisitor
parameter_list|(
name|ConfigurationVisitor
argument_list|<
name|T
argument_list|>
name|visitor
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|Project
argument_list|<
name|Configurable
argument_list|>
name|project
init|=
operator|new
name|Project
argument_list|<
name|Configurable
argument_list|>
argument_list|(
name|object
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|object
argument_list|,
name|project
operator|.
name|getRootNode
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testVersion
parameter_list|()
block|{
name|Configurable
name|object
init|=
operator|new
name|Configurable
argument_list|()
block|{
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|acceptVisitor
parameter_list|(
name|ConfigurationVisitor
argument_list|<
name|T
argument_list|>
name|visitor
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|Project
argument_list|<
name|Configurable
argument_list|>
name|project
init|=
operator|new
name|Project
argument_list|<
name|Configurable
argument_list|>
argument_list|(
name|object
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|project
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|project
operator|.
name|setVersion
argument_list|(
literal|"1.1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.1"
argument_list|,
name|project
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

