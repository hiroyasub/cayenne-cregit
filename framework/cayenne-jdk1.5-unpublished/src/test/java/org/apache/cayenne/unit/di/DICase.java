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
name|Injector
import|;
end_import

begin_comment
comment|/**  * A unit test superclass that supports injection of members based on the standard unit  * test container.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|DICase
extends|extends
name|TestCase
block|{
specifier|protected
specifier|abstract
name|Injector
name|getUnitTestInjector
parameter_list|()
function_decl|;
annotation|@
name|Override
specifier|protected
specifier|final
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|getUnitTestInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|UnitTestLifecycleManager
operator|.
name|class
argument_list|)
operator|.
name|setUp
argument_list|(
name|this
argument_list|)
expr_stmt|;
try|try
block|{
name|setUpAfterInjection
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// must stop the lifecycle manager (do the same thing we'd normally do in
comment|// 'tearDown' ), otherwise following tests will end up in
comment|// a bad state
try|try
block|{
name|getUnitTestInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|UnitTestLifecycleManager
operator|.
name|class
argument_list|)
operator|.
name|tearDown
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|x
parameter_list|)
block|{
comment|// swallow...
block|}
throw|throw
name|e
throw|;
block|}
block|}
annotation|@
name|Override
specifier|protected
specifier|final
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|tearDownBeforeInjection
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|getUnitTestInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|UnitTestLifecycleManager
operator|.
name|class
argument_list|)
operator|.
name|tearDown
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setUpAfterInjection
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
specifier|protected
name|void
name|tearDownBeforeInjection
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

