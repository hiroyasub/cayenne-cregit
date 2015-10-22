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
name|lifecycle
operator|.
name|unit
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
class|class
name|FlattenedServerCase
block|{
specifier|protected
name|ServerRuntime
name|runtime
decl_stmt|;
specifier|protected
name|TableHelper
name|e3
decl_stmt|;
specifier|protected
name|TableHelper
name|e4
decl_stmt|;
specifier|protected
name|TableHelper
name|e34
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
name|e3
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"E3"
argument_list|)
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
name|this
operator|.
name|e4
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"E4"
argument_list|)
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|)
expr_stmt|;
name|this
operator|.
name|e34
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"E34"
argument_list|)
operator|.
name|setColumns
argument_list|(
literal|"E3_ID"
argument_list|,
literal|"E4_ID"
argument_list|)
expr_stmt|;
name|this
operator|.
name|e34
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|this
operator|.
name|e3
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|ServerRuntimeBuilder
name|configureCayenne
parameter_list|()
block|{
return|return
name|ServerRuntimeBuilder
operator|.
name|builder
argument_list|()
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

