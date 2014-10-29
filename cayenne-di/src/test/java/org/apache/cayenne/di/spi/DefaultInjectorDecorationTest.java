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
name|Binder
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
name|MockImplementation1
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
name|MockInterface1_Decorator1
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
name|MockInterface1_Decorator2
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
name|MockInterface1_Decorator3
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
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|DefaultInjectorDecorationTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSingleDecorator_After
parameter_list|()
block|{
name|Module
name|module
init|=
operator|new
name|Module
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
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
name|MockImplementation1
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|decorate
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|)
operator|.
name|after
argument_list|(
name|MockInterface1_Decorator1
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[MyName]"
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
name|testSingleDecorator_Before
parameter_list|()
block|{
name|Module
name|module
init|=
operator|new
name|Module
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
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
name|MockImplementation1
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|decorate
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|)
operator|.
name|before
argument_list|(
name|MockInterface1_Decorator1
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[MyName]"
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
name|testDecoratorChain
parameter_list|()
block|{
name|Module
name|module
init|=
operator|new
name|Module
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
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
name|MockImplementation1
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|decorate
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|)
operator|.
name|before
argument_list|(
name|MockInterface1_Decorator1
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|decorate
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|)
operator|.
name|before
argument_list|(
name|MockInterface1_Decorator2
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|decorate
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|)
operator|.
name|after
argument_list|(
name|MockInterface1_Decorator3
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
name|assertNotNull
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<[{MyName}]>"
argument_list|,
name|service
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

