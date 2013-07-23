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
name|BeforeScopeEnd
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
name|DefaultUnitTestLifecycleManager
implements|implements
name|UnitTestLifecycleManager
block|{
annotation|@
name|Inject
specifier|protected
name|Injector
name|injector
decl_stmt|;
specifier|protected
name|DefaultScope
name|scope
decl_stmt|;
specifier|public
name|DefaultUnitTestLifecycleManager
parameter_list|(
name|DefaultScope
name|scope
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
block|}
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
name|injector
operator|.
name|injectMembers
argument_list|(
name|testCase
argument_list|)
expr_stmt|;
block|}
annotation|@
name|BeforeScopeEnd
specifier|public
parameter_list|<
name|T
extends|extends
name|TestCase
parameter_list|>
name|void
name|tearDown
parameter_list|(
name|T
name|testCase
parameter_list|)
block|{
name|scope
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit
