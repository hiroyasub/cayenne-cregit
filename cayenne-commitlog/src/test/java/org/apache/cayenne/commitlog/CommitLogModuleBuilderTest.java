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
name|commitlog
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
name|ObjectContext
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
name|commitlog
operator|.
name|model
operator|.
name|ChangeMap
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
name|DIBootstrap
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
name|Injector
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
name|Key
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|CommitLogModuleBuilderTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testListener_Object
parameter_list|()
block|{
name|L
name|listener
init|=
operator|new
name|L
argument_list|()
decl_stmt|;
name|Module
name|m
init|=
name|CommitLogModule
operator|.
name|extend
argument_list|()
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
operator|.
name|module
argument_list|()
decl_stmt|;
name|Injector
name|i
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|CommitLogListener
argument_list|>
name|listeners
init|=
name|i
operator|.
name|getInstance
argument_list|(
name|Key
operator|.
name|getListOf
argument_list|(
name|CommitLogListener
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|listeners
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|listeners
operator|.
name|contains
argument_list|(
name|listener
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testListener_Class
parameter_list|()
block|{
name|Module
name|m
init|=
name|CommitLogModule
operator|.
name|extend
argument_list|()
operator|.
name|addListener
argument_list|(
name|L
operator|.
name|class
argument_list|)
operator|.
name|module
argument_list|()
decl_stmt|;
name|Injector
name|i
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
name|m
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|CommitLogListener
argument_list|>
name|listeners
init|=
name|i
operator|.
name|getInstance
argument_list|(
name|Key
operator|.
name|getListOf
argument_list|(
name|CommitLogListener
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|listeners
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|listeners
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|L
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|L
implements|implements
name|CommitLogListener
block|{
annotation|@
name|Override
specifier|public
name|void
name|onPostCommit
parameter_list|(
name|ObjectContext
name|originatingContext
parameter_list|,
name|ChangeMap
name|changes
parameter_list|)
block|{
comment|// do nothing.
block|}
block|}
block|}
end_class

end_unit

