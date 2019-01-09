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
name|io
operator|.
name|IOException
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
name|Expression
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ASTFullObject
extends|extends
name|SimpleNode
block|{
specifier|public
name|ASTFullObject
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|Node
name|node
init|=
name|wrapChild
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|jjtAddChild
argument_list|(
name|node
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|node
operator|.
name|jjtSetParent
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTFullObject
parameter_list|()
block|{
name|this
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|ASTFullObject
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
name|String
name|getExpressionOperator
parameter_list|(
name|int
name|index
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
return|return
name|o
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|appendAsString
parameter_list|(
name|Appendable
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|append
argument_list|(
literal|":FULL_OBJECT:"
argument_list|)
expr_stmt|;
name|super
operator|.
name|appendAsString
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Expression
name|shallowCopy
parameter_list|()
block|{
return|return
operator|new
name|ASTFullObject
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|Expression
operator|.
name|FULL_OBJECT
return|;
block|}
block|}
end_class

end_unit

