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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|Transformer
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
comment|/**  * Superclass of aggregated conditional nodes such as NOT, AND, OR. Performs  * extra checks on parent and child expressions to validate conditions that  * are not addressed in the Cayenne expressions grammar.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AggregateConditionNode
extends|extends
name|SimpleNode
block|{
name|AggregateConditionNode
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
name|boolean
name|pruneNodeForPrunedChild
parameter_list|(
name|Object
name|prunedChild
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|transformExpression
parameter_list|(
name|Transformer
name|transformer
parameter_list|)
block|{
name|Object
name|transformed
init|=
name|super
operator|.
name|transformExpression
argument_list|(
name|transformer
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|transformed
operator|instanceof
name|AggregateConditionNode
operator|)
condition|)
block|{
return|return
name|transformed
return|;
block|}
name|AggregateConditionNode
name|condition
init|=
operator|(
name|AggregateConditionNode
operator|)
name|transformed
decl_stmt|;
comment|// prune itself if the transformation resulted in
comment|// no children or a single child
switch|switch
condition|(
name|condition
operator|.
name|getOperandCount
argument_list|()
condition|)
block|{
case|case
literal|1
case|:
return|return
name|condition
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
return|;
case|case
literal|0
case|:
return|return
name|PRUNED_NODE
return|;
default|default :
return|return
name|condition
return|;
block|}
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
specifier|public
name|void
name|jjtAddChild
parameter_list|(
name|Node
name|n
parameter_list|,
name|int
name|i
parameter_list|)
block|{
comment|// this is a check that we can't handle properly
comment|// in the grammar... do it here...
comment|// only allow conditional nodes...no scalars
if|if
condition|(
operator|!
operator|(
name|n
operator|instanceof
name|ConditionNode
operator|)
operator|&&
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
literal|": invalid child - "
operator|+
name|label
argument_list|)
throw|;
block|}
name|super
operator|.
name|jjtAddChild
argument_list|(
name|n
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

