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
name|di
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
name|assertNotNull
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
name|DIBootstrapTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCreateInjector_Empty
parameter_list|()
block|{
name|Injector
name|emptyInjector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|emptyInjector
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateInjector_SingleModule
parameter_list|()
block|{
specifier|final
name|boolean
index|[]
name|configureCalled
init|=
operator|new
name|boolean
index|[
literal|1
index|]
decl_stmt|;
name|Module
name|module
init|=
name|binder
lambda|->
name|configureCalled
index|[
literal|0
index|]
operator|=
literal|true
decl_stmt|;
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
name|module
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|injector
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configureCalled
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateInjector_MultiModule
parameter_list|()
block|{
specifier|final
name|boolean
index|[]
name|configureCalled
init|=
operator|new
name|boolean
index|[
literal|2
index|]
decl_stmt|;
name|Module
name|module1
init|=
name|binder
lambda|->
name|configureCalled
index|[
literal|0
index|]
operator|=
literal|true
decl_stmt|;
name|Module
name|module2
init|=
name|binder
lambda|->
name|configureCalled
index|[
literal|1
index|]
operator|=
literal|true
decl_stmt|;
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
name|module1
argument_list|,
name|module2
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|injector
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configureCalled
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configureCalled
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

