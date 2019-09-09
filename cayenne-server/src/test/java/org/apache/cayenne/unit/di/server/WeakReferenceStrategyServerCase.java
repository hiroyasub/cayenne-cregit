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

begin_class
specifier|public
class|class
name|WeakReferenceStrategyServerCase
extends|extends
name|ServerCase
block|{
specifier|private
specifier|static
specifier|final
name|Injector
name|injector
decl_stmt|;
static|static
block|{
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
name|WeakReferenceStrategyServerCaseModule
argument_list|(
name|testScope
argument_list|)
argument_list|)
expr_stmt|;
name|injector
operator|.
name|getInstance
argument_list|(
name|SchemaBuilder
operator|.
name|class
argument_list|)
operator|.
name|rebuildSchema
argument_list|()
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

