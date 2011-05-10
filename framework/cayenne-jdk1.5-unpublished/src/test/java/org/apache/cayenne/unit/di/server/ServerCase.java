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
name|unit
operator|.
name|di
operator|.
name|server
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
name|spi
operator|.
name|DefaultScope
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
name|unit
operator|.
name|CayenneResources
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
name|unit
operator|.
name|di
operator|.
name|DICase
import|;
end_import

begin_class
specifier|public
class|class
name|ServerCase
extends|extends
name|DICase
block|{
comment|// known runtimes... unit tests may reuse these with @UseServerRuntime annotation or
comment|// can define their own on the fly (TODO: how would that work with the global schema
comment|// setup?)
specifier|public
specifier|static
specifier|final
name|String
name|INHERTITANCE_SINGLE_TABLE1_PROJECT
init|=
literal|"cayenne-inheritance-single-table1.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INHERTITANCE_VERTICAL_PROJECT
init|=
literal|"cayenne-inheritance-vertical.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LOCKING_PROJECT
init|=
literal|"cayenne-locking.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|QUOTED_IDENTIFIERS_PROJECT
init|=
literal|"cayenne-quoted-identifiers.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PEOPLE_PROJECT
init|=
literal|"cayenne-people.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RELATIONSHIPS_PROJECT
init|=
literal|"cayenne-relationships.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TESTMAP_PROJECT
init|=
literal|"cayenne-testmap.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_PROJECT
init|=
literal|"cayenne-default.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Injector
name|injector
decl_stmt|;
static|static
block|{
name|CayenneResources
name|resources
init|=
name|CayenneResources
operator|.
name|getResources
argument_list|()
decl_stmt|;
name|DefaultScope
name|testScope
init|=
operator|new
name|DefaultScope
argument_list|()
decl_stmt|;
name|injector
operator|=
name|DIBootstrap
operator|.
name|createInjector
argument_list|(
operator|new
name|ServerCaseModule
argument_list|(
name|resources
argument_list|,
name|testScope
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Injector
name|getUnitTestInjector
parameter_list|()
block|{
return|return
name|injector
return|;
block|}
block|}
end_class

end_unit

