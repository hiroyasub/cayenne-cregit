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
name|MockImplementation1Alt
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
name|MockImplementation1Alt2
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
name|MockImplementation1_ListConfiguration
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
name|MockImplementation1_MapConfiguration
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
name|MockImplementation1_WithInjector
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
name|MockImplementation2Sub1
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
name|MockImplementation2_ConstructorProvider
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
name|MockImplementation2_Named
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
name|MockImplementation4
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
name|MockImplementation4Alt
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
name|MockImplementation5
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
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|mock
operator|.
name|MockInterface4
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
name|MockInterface5
import|;
end_import

begin_class
specifier|public
class|class
name|DefaultInjectorInjectionTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testFieldInjection
parameter_list|()
block|{
name|Module
name|module
init|=
operator|new
name|Module
argument_list|()
block|{
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
name|MockInterface2
name|service
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|MockInterface2
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
literal|"altered_MyName"
argument_list|,
name|service
operator|.
name|getAlteredName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFieldInjection_Named
parameter_list|()
block|{
name|Module
name|module
init|=
operator|new
name|Module
argument_list|()
block|{
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
name|bind
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|,
literal|"one"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation1Alt
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|,
literal|"two"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation1Alt2
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
name|MockImplementation2_Named
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
name|MockInterface2
name|service
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|MockInterface2
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
literal|"altered_alt"
argument_list|,
name|service
operator|.
name|getAlteredName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFieldInjectionSuperclass
parameter_list|()
block|{
name|Module
name|module
init|=
operator|new
name|Module
argument_list|()
block|{
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
name|bind
argument_list|(
name|MockInterface2
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation2Sub1
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
name|MockInterface2
name|service
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|MockInterface2
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
literal|"altered_MyName:XName"
argument_list|,
name|service
operator|.
name|getAlteredName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConstructorInjection
parameter_list|()
block|{
name|Module
name|module
init|=
operator|new
name|Module
argument_list|()
block|{
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
name|bind
argument_list|(
name|MockInterface4
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation4
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
name|MockInterface4
name|service
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|MockInterface4
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
literal|"constructor_MyName"
argument_list|,
name|service
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConstructorInjection_Named
parameter_list|()
block|{
name|Module
name|module
init|=
operator|new
name|Module
argument_list|()
block|{
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
name|bind
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|,
literal|"one"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation1Alt
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|MockInterface1
operator|.
name|class
argument_list|,
literal|"two"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation1Alt2
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|MockInterface4
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation4Alt
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
name|MockInterface4
name|service
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|MockInterface4
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
literal|"constructor_alt2"
argument_list|,
name|service
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProviderInjection_Constructor
parameter_list|()
block|{
name|Module
name|module
init|=
operator|new
name|Module
argument_list|()
block|{
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
name|bind
argument_list|(
name|MockInterface2
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation2_ConstructorProvider
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
name|MockInterface2
name|service
init|=
name|injector
operator|.
name|getInstance
argument_list|(
name|MockInterface2
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"altered_MyName"
argument_list|,
name|service
operator|.
name|getAlteredName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMapInjection
parameter_list|()
block|{
name|Module
name|module
init|=
operator|new
name|Module
argument_list|()
block|{
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
name|MockImplementation1_MapConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bindMap
argument_list|(
literal|"xyz"
argument_list|)
operator|.
name|put
argument_list|(
literal|"x"
argument_list|,
literal|"xvalue"
argument_list|)
operator|.
name|put
argument_list|(
literal|"y"
argument_list|,
literal|"yvalue"
argument_list|)
operator|.
name|put
argument_list|(
literal|"x"
argument_list|,
literal|"xvalue1"
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
literal|";x=xvalue1;y=yvalue"
argument_list|,
name|service
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testListInjection_addValue
parameter_list|()
block|{
name|Module
name|module
init|=
operator|new
name|Module
argument_list|()
block|{
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
name|MockImplementation1_ListConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bindList
argument_list|(
literal|"xyz"
argument_list|)
operator|.
name|add
argument_list|(
literal|"xvalue"
argument_list|)
operator|.
name|add
argument_list|(
literal|"yvalue"
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
literal|";xvalue;yvalue"
argument_list|,
name|service
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testListInjection_addType
parameter_list|()
block|{
name|Module
name|module
init|=
operator|new
name|Module
argument_list|()
block|{
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
name|MockInterface5
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MockImplementation5
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|MockImplementation1_ListConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bindList
argument_list|(
literal|"xyz"
argument_list|)
operator|.
name|add
argument_list|(
name|MockInterface5
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
literal|"yvalue"
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
literal|";xyz;yvalue"
argument_list|,
name|service
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInjectorInjection
parameter_list|()
block|{
name|Module
name|module
init|=
operator|new
name|Module
argument_list|()
block|{
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
name|MockImplementation1_WithInjector
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
literal|"injector_not_null"
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

