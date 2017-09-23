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
operator|.
name|spi
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
name|di
operator|.
name|DIRuntimeException
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
name|di
operator|.
name|mock
operator|.
name|MockImplementation1_DepOn2
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
name|mock
operator|.
name|MockImplementation1_DepOn2Constructor
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
name|mock
operator|.
name|MockImplementation1_DepOn2Provider
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
name|mock
operator|.
name|MockImplementation2
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
name|mock
operator|.
name|MockImplementation2_Constructor
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
name|mock
operator|.
name|MockImplementation2_I3Dependency
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
name|mock
operator|.
name|MockImplementation3
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
name|mock
operator|.
name|MockInterface1
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
name|mock
operator|.
name|MockInterface2
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
name|mock
operator|.
name|MockInterface3
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
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|DefaultInjectorCircularInjectionTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testFieldInjection_CircularDependency
parameter_list|()
block|{
name|Module
name|module
init|=
name|binder
lambda|->
block|{
name|binder
operator|.
name|bind
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation1_DepOn2
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|MockInterface2
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation2
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
decl_stmt|;
name|DefaultInjector
name|injector
init|=
operator|new
name|DefaultInjector
argument_list|(
name|module
argument_list|)
decl_stmt|;
try|try
block|{
name|injector
operator|.
name|getInstance
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Circular dependency is not detected."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DIRuntimeException
name|e
parameter_list|)
block|{
comment|// expected
block|}
catch|catch
parameter_list|(
name|StackOverflowError
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Circular dependency is not detected, causing stack overflow"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProviderInjection_CircularDependency
parameter_list|()
block|{
name|Module
name|module
init|=
name|binder
lambda|->
block|{
name|binder
operator|.
name|bind
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation1_DepOn2Provider
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|MockInterface2
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation2
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
decl_stmt|;
name|DefaultInjector
name|injector
init|=
operator|new
name|DefaultInjector
argument_list|(
name|module
argument_list|)
decl_stmt|;
name|MockInterface1
name|service
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MockImplementation2Name"
argument_list|,
name|service
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConstructorInjection_CircularDependency
parameter_list|()
block|{
name|Module
name|module
init|=
name|binder
lambda|->
block|{
name|binder
operator|.
name|bind
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation1_DepOn2Constructor
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|MockInterface2
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation2_Constructor
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
decl_stmt|;
name|DefaultInjector
name|injector
init|=
operator|new
name|DefaultInjector
argument_list|(
name|module
argument_list|)
decl_stmt|;
try|try
block|{
name|injector
operator|.
name|getInstance
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Circular dependency is not detected."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DIRuntimeException
name|e
parameter_list|)
block|{
comment|// expected
block|}
catch|catch
parameter_list|(
name|StackOverflowError
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Circular dependency is not detected, causing stack overflow"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConstructorInjection_WithFieldInjectionDeps
parameter_list|()
block|{
name|Module
name|module
init|=
name|binder
lambda|->
block|{
name|binder
operator|.
name|bind
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation1_DepOn2Constructor
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|MockInterface2
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation2_I3Dependency
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|MockInterface3
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation3
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
decl_stmt|;
name|DefaultInjector
name|injector
init|=
operator|new
name|DefaultInjector
argument_list|(
name|module
argument_list|)
decl_stmt|;
try|try
block|{
name|injector
operator|.
name|getInstance
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DIRuntimeException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Circular dependency is detected incorrectly: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

