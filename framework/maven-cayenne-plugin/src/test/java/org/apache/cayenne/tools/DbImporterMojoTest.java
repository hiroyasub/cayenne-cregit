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
name|tools
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|configuration
operator|.
name|ToolModule
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
name|dba
operator|.
name|AutoAdapter
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
name|dba
operator|.
name|DbAdapter
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
name|dba
operator|.
name|mysql
operator|.
name|MySQLAdapter
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

begin_class
specifier|public
class|class
name|DbImporterMojoTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testGetAdapter
parameter_list|()
throws|throws
name|Exception
block|{
name|DbImporterMojo
name|mojo
init|=
operator|new
name|DbImporterMojo
argument_list|()
decl_stmt|;
name|DataSource
name|ds
init|=
name|mock
argument_list|(
name|DataSource
operator|.
name|class
argument_list|)
decl_stmt|;
name|Injector
name|injector
init|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
operator|new
name|ToolModule
argument_list|()
argument_list|)
decl_stmt|;
name|DbAdapter
name|adapter
init|=
name|mojo
operator|.
name|getAdapter
argument_list|(
name|injector
argument_list|,
name|ds
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|adapter
operator|instanceof
name|AutoAdapter
argument_list|)
expr_stmt|;
name|Field
name|adapterField
init|=
name|mojo
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"adapter"
argument_list|)
decl_stmt|;
name|adapterField
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|adapterField
operator|.
name|set
argument_list|(
name|mojo
argument_list|,
name|MySQLAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|DbAdapter
name|adapter2
init|=
name|mojo
operator|.
name|getAdapter
argument_list|(
name|injector
argument_list|,
name|ds
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|adapter2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|adapter2
operator|instanceof
name|MySQLAdapter
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

