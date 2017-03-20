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
name|dba
operator|.
name|ingres
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
name|access
operator|.
name|translator
operator|.
name|select
operator|.
name|QueryAssembler
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
name|translator
operator|.
name|select
operator|.
name|TrimmingQualifierTranslator
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
name|parser
operator|.
name|ASTExtract
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
name|parser
operator|.
name|ASTFunctionCall
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
name|parser
operator|.
name|Node
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
class|class
name|IngresQualifierTranslator
extends|extends
name|TrimmingQualifierTranslator
block|{
name|IngresQualifierTranslator
parameter_list|(
name|QueryAssembler
name|queryAssembler
parameter_list|)
block|{
name|super
argument_list|(
name|queryAssembler
argument_list|,
name|IngresAdapter
operator|.
name|TRIM_FUNCTION
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|endNode
parameter_list|(
name|Expression
name|node
parameter_list|,
name|Expression
name|parentNode
parameter_list|)
block|{
name|super
operator|.
name|endNode
argument_list|(
name|node
argument_list|,
name|parentNode
argument_list|)
expr_stmt|;
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|FUNCTION_CALL
condition|)
block|{
if|if
condition|(
literal|"LOCATE"
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|ASTFunctionCall
operator|)
name|node
operator|)
operator|.
name|getFunctionName
argument_list|()
argument_list|)
condition|)
block|{
comment|// order of args in ingres version of LOCATE is different, so swap them back
name|swapNodeChildren
argument_list|(
operator|(
name|ASTFunctionCall
operator|)
name|node
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|appendFunction
parameter_list|(
name|ASTFunctionCall
name|functionExpression
parameter_list|)
block|{
if|if
condition|(
literal|"CONCAT"
operator|.
name|equals
argument_list|(
name|functionExpression
operator|.
name|getFunctionName
argument_list|()
argument_list|)
condition|)
block|{
comment|// noop
block|}
if|else if
condition|(
literal|"LOCATE"
operator|.
name|equals
argument_list|(
name|functionExpression
operator|.
name|getFunctionName
argument_list|()
argument_list|)
condition|)
block|{
comment|// order of args in ingres version of LOCATE is different
comment|// LOCATE(substr, str) -> LOCATE(str, substr)
name|out
operator|.
name|append
argument_list|(
literal|"LOCATE"
argument_list|)
expr_stmt|;
name|swapNodeChildren
argument_list|(
name|functionExpression
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"TRIM"
operator|.
name|equals
argument_list|(
name|functionExpression
operator|.
name|getFunctionName
argument_list|()
argument_list|)
condition|)
block|{
comment|// simple TRIM removes only trailing spaces
name|out
operator|.
name|append
argument_list|(
literal|"LTRIM(RTRIM"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|appendFunction
argument_list|(
name|functionExpression
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|appendFunctionArgDivider
parameter_list|(
name|ASTFunctionCall
name|functionExpression
parameter_list|)
block|{
if|if
condition|(
literal|"CONCAT"
operator|.
name|equals
argument_list|(
name|functionExpression
operator|.
name|getFunctionName
argument_list|()
argument_list|)
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|" + "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|appendFunctionArgDivider
argument_list|(
name|functionExpression
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|appendFunctionArg
parameter_list|(
name|Object
name|value
parameter_list|,
name|ASTFunctionCall
name|functionExpression
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
literal|"SUBSTRING"
operator|.
name|equals
argument_list|(
name|functionExpression
operator|.
name|getFunctionName
argument_list|()
argument_list|)
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"CAST("
argument_list|)
expr_stmt|;
name|super
operator|.
name|appendFunctionArg
argument_list|(
name|value
argument_list|,
name|functionExpression
argument_list|)
expr_stmt|;
name|clearLastFunctionArgDivider
argument_list|(
name|functionExpression
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|" AS INTEGER)"
argument_list|)
expr_stmt|;
name|appendFunctionArgDivider
argument_list|(
name|functionExpression
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|appendFunctionArg
argument_list|(
name|value
argument_list|,
name|functionExpression
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|clearLastFunctionArgDivider
parameter_list|(
name|ASTFunctionCall
name|functionExpression
parameter_list|)
block|{
if|if
condition|(
literal|"CONCAT"
operator|.
name|equals
argument_list|(
name|functionExpression
operator|.
name|getFunctionName
argument_list|()
argument_list|)
condition|)
block|{
name|out
operator|.
name|delete
argument_list|(
name|out
operator|.
name|length
argument_list|()
operator|-
literal|" + "
operator|.
name|length
argument_list|()
argument_list|,
name|out
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|clearLastFunctionArgDivider
argument_list|(
name|functionExpression
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"TRIM"
operator|.
name|equals
argument_list|(
name|functionExpression
operator|.
name|getFunctionName
argument_list|()
argument_list|)
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|appendExtractFunction
parameter_list|(
name|ASTExtract
name|functionExpression
parameter_list|)
block|{
switch|switch
condition|(
name|functionExpression
operator|.
name|getPart
argument_list|()
condition|)
block|{
case|case
name|DAY_OF_WEEK
case|:
case|case
name|DAY_OF_MONTH
case|:
case|case
name|DAY_OF_YEAR
case|:
comment|// ingres variants are without '_'
name|out
operator|.
name|append
argument_list|(
name|functionExpression
operator|.
name|getPart
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|replace
argument_list|(
literal|"_"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
break|break;
default|default:
name|appendFunction
argument_list|(
name|functionExpression
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|swapNodeChildren
parameter_list|(
name|Node
name|node
parameter_list|,
name|int
name|i
parameter_list|,
name|int
name|j
parameter_list|)
block|{
name|Node
name|ni
init|=
name|node
operator|.
name|jjtGetChild
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Node
name|nj
init|=
name|node
operator|.
name|jjtGetChild
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|node
operator|.
name|jjtAddChild
argument_list|(
name|ni
argument_list|,
name|j
argument_list|)
expr_stmt|;
name|node
operator|.
name|jjtAddChild
argument_list|(
name|nj
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

