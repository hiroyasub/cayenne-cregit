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
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|DIException
import|;
end_import

begin_comment
comment|/**  * A helper object that tracks the injection stack to prevent circular dependencies.  *   * @since 3.1  */
end_comment

begin_class
class|class
name|InjectionStack
block|{
specifier|private
name|ThreadLocal
argument_list|<
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|>
name|stack
decl_stmt|;
name|InjectionStack
parameter_list|()
block|{
name|this
operator|.
name|stack
operator|=
operator|new
name|ThreadLocal
argument_list|<
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|void
name|reset
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|localStack
init|=
name|stack
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|localStack
operator|!=
literal|null
condition|)
block|{
name|localStack
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
name|void
name|push
parameter_list|(
name|String
name|bindingKey
parameter_list|)
throws|throws
name|DIException
block|{
name|LinkedList
argument_list|<
name|String
argument_list|>
name|localStack
init|=
name|stack
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|localStack
operator|==
literal|null
condition|)
block|{
name|localStack
operator|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|stack
operator|.
name|set
argument_list|(
name|localStack
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|localStack
operator|.
name|contains
argument_list|(
name|bindingKey
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|DIException
argument_list|(
literal|"Circular dependency detected when binding a key \"%s\". Nested keys: %s"
operator|+
literal|". To resolve it, you should inject a Provider instead of an object."
argument_list|,
name|bindingKey
argument_list|,
name|localStack
argument_list|)
throw|;
block|}
name|localStack
operator|.
name|add
argument_list|(
name|bindingKey
argument_list|)
expr_stmt|;
block|}
name|void
name|pop
parameter_list|()
block|{
name|LinkedList
argument_list|<
name|String
argument_list|>
name|localStack
init|=
name|stack
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|localStack
operator|!=
literal|null
condition|)
block|{
name|localStack
operator|.
name|removeLast
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"0"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

