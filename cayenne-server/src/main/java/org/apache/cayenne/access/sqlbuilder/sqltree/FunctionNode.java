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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
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
name|access
operator|.
name|sqlbuilder
operator|.
name|NodeTreeVisitor
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
name|access
operator|.
name|sqlbuilder
operator|.
name|QuotingAppendable
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|FunctionNode
extends|extends
name|Node
block|{
specifier|private
specifier|final
name|String
name|functionName
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|needParentheses
decl_stmt|;
specifier|private
name|String
name|alias
decl_stmt|;
specifier|public
name|FunctionNode
parameter_list|(
name|String
name|functionName
parameter_list|,
name|String
name|alias
parameter_list|)
block|{
name|this
argument_list|(
name|functionName
argument_list|,
name|alias
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|FunctionNode
parameter_list|(
name|String
name|functionName
parameter_list|,
name|String
name|alias
parameter_list|,
name|boolean
name|needParentheses
parameter_list|)
block|{
name|super
argument_list|(
name|NodeType
operator|.
name|FUNCTION
argument_list|)
expr_stmt|;
name|this
operator|.
name|functionName
operator|=
name|functionName
expr_stmt|;
name|this
operator|.
name|alias
operator|=
name|alias
expr_stmt|;
name|this
operator|.
name|needParentheses
operator|=
name|needParentheses
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|QuotingAppendable
name|append
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
if|if
condition|(
name|skipContent
argument_list|()
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|alias
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|functionName
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|visit
parameter_list|(
name|NodeTreeVisitor
name|visitor
parameter_list|)
block|{
if|if
condition|(
name|skipContent
argument_list|()
condition|)
block|{
name|visitor
operator|.
name|onNodeStart
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|visitor
operator|.
name|onNodeEnd
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return;
block|}
name|super
operator|.
name|visit
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|appendChildrenStart
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
if|if
condition|(
name|skipContent
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|needParentheses
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|appendChildrenEnd
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
if|if
condition|(
name|skipContent
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|needParentheses
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" )"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|alias
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" AS "
argument_list|)
operator|.
name|appendQuoted
argument_list|(
name|alias
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|appendChildrenSeparator
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|,
name|int
name|childIdx
parameter_list|)
block|{
if|if
condition|(
name|skipContent
argument_list|()
condition|)
block|{
return|return;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getFunctionName
parameter_list|()
block|{
return|return
name|functionName
return|;
block|}
specifier|public
name|String
name|getAlias
parameter_list|()
block|{
return|return
name|alias
return|;
block|}
specifier|public
name|void
name|setAlias
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
name|this
operator|.
name|alias
operator|=
name|alias
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Node
name|copy
parameter_list|()
block|{
return|return
operator|new
name|FunctionNode
argument_list|(
name|functionName
argument_list|,
name|alias
argument_list|,
name|needParentheses
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|notInResultNode
parameter_list|()
block|{
comment|// check if parent is of type RESULT
name|Node
name|parent
init|=
name|getParent
argument_list|()
decl_stmt|;
while|while
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|parent
operator|.
name|getType
argument_list|()
operator|==
name|NodeType
operator|.
name|RESULT
condition|)
block|{
return|return
literal|false
return|;
block|}
name|parent
operator|=
name|parent
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|protected
name|boolean
name|skipContent
parameter_list|()
block|{
comment|// has alias and not in result node
return|return
name|alias
operator|!=
literal|null
operator|&&
name|notInResultNode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

