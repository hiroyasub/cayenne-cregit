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
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|di
operator|.
name|Inject
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
name|Provider
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
name|di
operator|.
name|server
operator|.
name|ServerCaseLifecycleManager
import|;
end_import

begin_class
specifier|public
class|class
name|ClientCaseLifecycleManager
extends|extends
name|ServerCaseLifecycleManager
block|{
annotation|@
name|Inject
specifier|protected
name|Provider
argument_list|<
name|ClientCaseProperties
argument_list|>
name|propertiesProvider
decl_stmt|;
specifier|public
name|ClientCaseLifecycleManager
parameter_list|(
name|DefaultScope
name|scope
parameter_list|)
block|{
name|super
argument_list|(
name|scope
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
extends|extends
name|TestCase
parameter_list|>
name|void
name|setUp
parameter_list|(
name|T
name|testCase
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ClientRuntimeProperty
name|properties
init|=
name|testCase
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|ClientRuntimeProperty
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|pairs
init|=
name|properties
operator|.
name|value
argument_list|()
decl_stmt|;
if|if
condition|(
name|pairs
operator|!=
literal|null
operator|&&
name|pairs
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|String
name|key
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|pairs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|%
literal|2
operator|==
literal|0
condition|)
block|{
name|key
operator|=
name|pairs
index|[
name|i
index|]
expr_stmt|;
block|}
else|else
block|{
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|pairs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|propertiesProvider
operator|.
name|get
argument_list|()
operator|.
name|setRuntimeProperties
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|(
name|testCase
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
