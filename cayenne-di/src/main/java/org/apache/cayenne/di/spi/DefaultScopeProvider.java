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
name|Provider
import|;
end_import

begin_comment
comment|/**  * A provider that provides scoping for other providers.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DefaultScopeProvider
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Provider
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|Provider
argument_list|<
name|T
argument_list|>
name|delegate
decl_stmt|;
specifier|private
name|DefaultScope
name|scope
decl_stmt|;
comment|// presumably "volatile" works in Java 5 and newer to prevent double-checked locking
specifier|private
specifier|volatile
name|T
name|instance
decl_stmt|;
specifier|public
name|DefaultScopeProvider
parameter_list|(
name|DefaultScope
name|scope
parameter_list|,
name|Provider
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|scope
operator|.
name|addScopeEventListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|T
name|get
parameter_list|()
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
name|delegate
operator|.
name|get
argument_list|()
expr_stmt|;
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DIRuntimeException
argument_list|(
literal|"Underlying provider (%s) returned NULL instance"
argument_list|,
name|delegate
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|scope
operator|.
name|addScopeEventListener
argument_list|(
name|instance
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|instance
return|;
block|}
annotation|@
name|AfterScopeEnd
specifier|public
name|void
name|afterScopeEnd
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|localInstance
init|=
name|instance
decl_stmt|;
if|if
condition|(
name|localInstance
operator|!=
literal|null
condition|)
block|{
name|instance
operator|=
literal|null
expr_stmt|;
name|scope
operator|.
name|removeScopeEventListener
argument_list|(
name|localInstance
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

