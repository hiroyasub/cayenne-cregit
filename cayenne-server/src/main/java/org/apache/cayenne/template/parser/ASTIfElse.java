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
name|template
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
name|cayenne
operator|.
name|template
operator|.
name|Context
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|ASTIfElse
extends|extends
name|SimpleNode
block|{
specifier|public
name|ASTIfElse
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|evaluate
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|ASTExpression
name|condition
init|=
operator|(
name|ASTExpression
operator|)
name|jjtGetChild
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|condition
operator|.
name|evaluateAsBoolean
argument_list|(
name|context
argument_list|)
condition|)
block|{
name|jjtGetChild
argument_list|(
literal|1
argument_list|)
operator|.
name|evaluate
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// else is optional
if|if
condition|(
name|jjtGetNumChildren
argument_list|()
operator|>
literal|2
condition|)
block|{
name|jjtGetChild
argument_list|(
literal|2
argument_list|)
operator|.
name|evaluate
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

