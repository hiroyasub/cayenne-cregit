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
name|cache
operator|.
name|invalidation
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
name|configuration
operator|.
name|server
operator|.
name|ServerRuntime
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
name|server
operator|.
name|ServerRuntimeBuilder
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
name|di
operator|.
name|Module
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|test
operator|.
name|jdbc
operator|.
name|TableHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|CacheInvalidationCase
block|{
specifier|protected
name|ServerRuntime
name|runtime
decl_stmt|;
specifier|protected
name|TableHelper
name|e1
decl_stmt|;
specifier|protected
name|TableHelper
name|e2
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|startCayenne
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runtime
operator|=
name|configureCayenne
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
name|DBHelper
name|dbHelper
init|=
operator|new
name|DBHelper
argument_list|(
name|runtime
operator|.
name|getDataSource
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|e1
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"E1"
argument_list|)
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
name|this
operator|.
name|e1
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|this
operator|.
name|e2
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"E2"
argument_list|)
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
name|this
operator|.
name|e2
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|extend
parameter_list|(
name|CacheInvalidationModuleExtender
name|e
parameter_list|)
block|{
comment|// do nothing by default, subclasses can override
block|}
specifier|protected
name|Module
name|buildCustomModule
parameter_list|()
block|{
return|return
name|binder
lambda|->
block|{
block|}
return|;
block|}
specifier|protected
name|ServerRuntimeBuilder
name|configureCayenne
parameter_list|()
block|{
return|return
name|ServerRuntime
operator|.
name|builder
argument_list|()
operator|.
name|addModule
argument_list|(
name|b
lambda|->
name|extend
argument_list|(
name|CacheInvalidationModule
operator|.
name|extend
argument_list|(
name|b
argument_list|)
argument_list|)
argument_list|)
operator|.
name|addModule
argument_list|(
name|buildCustomModule
argument_list|()
argument_list|)
operator|.
name|addConfig
argument_list|(
literal|"cayenne-lifecycle.xml"
argument_list|)
return|;
block|}
annotation|@
name|After
specifier|public
name|void
name|shutdownCayenne
parameter_list|()
block|{
if|if
condition|(
name|runtime
operator|!=
literal|null
condition|)
block|{
name|runtime
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

