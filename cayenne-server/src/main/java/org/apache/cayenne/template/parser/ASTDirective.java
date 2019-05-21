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
name|directive
operator|.
name|Directive
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|ASTDirective
extends|extends
name|IdentifierNode
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ASTDirective
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|ASTDirective
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
name|Directive
name|directive
init|=
name|context
operator|.
name|getDirective
argument_list|(
name|getIdentifier
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|directive
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Unknown directive #{}"
argument_list|,
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|ASTExpression
index|[]
name|expressions
init|=
operator|new
name|ASTExpression
index|[
name|children
operator|.
name|length
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
name|children
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|expressions
index|[
name|i
index|]
operator|=
operator|(
name|ASTExpression
operator|)
name|children
index|[
name|i
index|]
expr_stmt|;
block|}
name|directive
operator|.
name|apply
argument_list|(
name|context
argument_list|,
name|expressions
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

