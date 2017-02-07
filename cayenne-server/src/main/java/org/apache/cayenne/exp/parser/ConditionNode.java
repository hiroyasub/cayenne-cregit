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
name|Collection
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|ExpressionException
import|;
end_import

begin_comment
comment|/**  * Superclass of conditional expressions.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|ConditionNode
extends|extends
name|SimpleNode
block|{
specifier|public
name|ConditionNode
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
specifier|public
name|void
name|jjtSetParent
parameter_list|(
name|Node
name|n
parameter_list|)
block|{
comment|// this is a check that we can't handle properly
comment|// in the grammar... do it here...
comment|// disallow non-aggregated condition parents...
if|if
condition|(
operator|!
operator|(
name|n
operator|instanceof
name|AggregateConditionNode
operator|)
condition|)
block|{
name|String
name|label
init|=
operator|(
name|n
operator|instanceof
name|SimpleNode
operator|)
condition|?
operator|(
operator|(
name|SimpleNode
operator|)
name|n
operator|)
operator|.
name|expName
argument_list|()
else|:
name|String
operator|.
name|valueOf
argument_list|(
name|n
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ExpressionException
argument_list|(
name|expName
argument_list|()
operator|+
literal|": invalid parent - "
operator|+
name|label
argument_list|)
throw|;
block|}
name|super
operator|.
name|jjtSetParent
argument_list|(
name|n
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
operator|!=
name|requiredLen
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
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
name|Object
index|[]
name|evaluatedChildren
init|=
operator|new
name|Object
index|[
name|requiredLen
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
name|requiredLen
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
comment|// don't care here for keys
if|if
condition|(
name|firstChild
operator|instanceof
name|Map
condition|)
block|{
name|firstChild
operator|=
operator|(
operator|(
name|Map
operator|)
name|firstChild
operator|)
operator|.
name|values
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|firstChild
operator|instanceof
name|Collection
condition|)
block|{
for|for
control|(
name|Object
name|c
range|:
operator|(
name|Collection
operator|)
name|firstChild
control|)
block|{
if|if
condition|(
name|evaluateSubNode
argument_list|(
name|c
argument_list|,
name|evaluatedChildren
argument_list|)
operator|==
name|Boolean
operator|.
name|TRUE
condition|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
block|}
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
else|else
block|{
return|return
name|evaluateSubNode
argument_list|(
name|firstChild
argument_list|,
name|evaluatedChildren
argument_list|)
return|;
block|}
block|}
specifier|abstract
specifier|protected
name|int
name|getRequiredChildrenCount
parameter_list|()
function_decl|;
specifier|abstract
specifier|protected
name|Boolean
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

