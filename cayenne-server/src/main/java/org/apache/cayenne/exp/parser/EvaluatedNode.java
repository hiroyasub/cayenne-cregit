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
name|exp
operator|.
name|parser
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|EvaluatedNode
extends|extends
name|SimpleNode
block|{
specifier|protected
name|EvaluatedNode
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|super
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|evaluateNode
parameter_list|(
name|Object
name|o
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|len
init|=
name|jjtGetNumChildren
argument_list|()
decl_stmt|;
name|int
name|requiredLen
init|=
name|getRequiredChildrenCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|len
operator|<
name|requiredLen
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|requiredLen
operator|==
literal|0
condition|)
block|{
return|return
name|evaluateSubNode
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|final
name|Object
index|[]
name|evaluatedChildren
init|=
operator|new
name|Object
index|[
name|len
index|]
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
name|len
condition|;
name|i
operator|++
control|)
block|{
name|evaluatedChildren
index|[
name|i
index|]
operator|=
name|evaluateChild
argument_list|(
name|i
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
name|Object
name|firstChild
init|=
name|evaluatedChildren
index|[
literal|0
index|]
decl_stmt|;
comment|// convert Map, keep Map keys
if|if
condition|(
name|firstChild
operator|instanceof
name|Map
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|child
init|=
operator|(
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
operator|)
name|firstChild
decl_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|child
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|child
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|evaluateSubNode
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|evaluatedChildren
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|// convert collection
if|if
condition|(
name|firstChild
operator|instanceof
name|Collection
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Collection
argument_list|<
name|Object
argument_list|>
name|child
init|=
operator|(
name|Collection
argument_list|<
name|Object
argument_list|>
operator|)
name|firstChild
decl_stmt|;
name|Collection
argument_list|<
name|Object
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|child
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|c
range|:
name|child
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|evaluateSubNode
argument_list|(
name|c
argument_list|,
name|evaluatedChildren
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|// convert scalar
return|return
name|evaluateSubNode
argument_list|(
name|firstChild
argument_list|,
name|evaluatedChildren
argument_list|)
return|;
block|}
specifier|abstract
specifier|protected
name|int
name|getRequiredChildrenCount
parameter_list|()
function_decl|;
specifier|abstract
specifier|protected
name|Object
name|evaluateSubNode
parameter_list|(
name|Object
name|o
parameter_list|,
name|Object
index|[]
name|evaluatedChildren
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_class

end_unit

