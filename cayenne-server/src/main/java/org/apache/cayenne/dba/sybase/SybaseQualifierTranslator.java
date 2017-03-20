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
name|sybase
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
name|translator
operator|.
name|select
operator|.
name|QualifierTranslator
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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|SybaseQualifierTranslator
extends|extends
name|QualifierTranslator
block|{
specifier|public
name|SybaseQualifierTranslator
parameter_list|(
name|QueryAssembler
name|queryAssembler
parameter_list|)
block|{
name|super
argument_list|(
name|queryAssembler
argument_list|)
expr_stmt|;
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
switch|switch
condition|(
name|functionExpression
operator|.
name|getFunctionName
argument_list|()
condition|)
block|{
case|case
literal|"MOD"
case|:
case|case
literal|"CONCAT"
case|:
comment|// noop
break|break;
case|case
literal|"LENGTH"
case|:
name|out
operator|.
name|append
argument_list|(
literal|"LEN"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"LOCATE"
case|:
name|out
operator|.
name|append
argument_list|(
literal|"CHARINDEX"
argument_list|)
expr_stmt|;
break|break;
default|default:
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
switch|switch
condition|(
name|functionExpression
operator|.
name|getFunctionName
argument_list|()
condition|)
block|{
case|case
literal|"MOD"
case|:
name|out
operator|.
name|append
argument_list|(
literal|" % "
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"CONCAT"
case|:
name|out
operator|.
name|append
argument_list|(
literal|" + "
argument_list|)
expr_stmt|;
break|break;
default|default:
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
name|clearLastFunctionArgDivider
parameter_list|(
name|ASTFunctionCall
name|functionExpression
parameter_list|)
block|{
switch|switch
condition|(
name|functionExpression
operator|.
name|getFunctionName
argument_list|()
condition|)
block|{
case|case
literal|"MOD"
case|:
case|case
literal|"CONCAT"
case|:
name|out
operator|.
name|delete
argument_list|(
name|out
operator|.
name|length
argument_list|()
operator|-
literal|3
argument_list|,
name|out
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
name|super
operator|.
name|clearLastFunctionArgDivider
argument_list|(
name|functionExpression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|functionExpression
operator|instanceof
name|ASTExtract
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
annotation|@
name|Override
specifier|protected
name|boolean
name|parenthesisNeeded
parameter_list|(
name|Expression
name|node
parameter_list|,
name|Expression
name|parentNode
parameter_list|)
block|{
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
name|node
operator|instanceof
name|ASTExtract
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
name|super
operator|.
name|parenthesisNeeded
argument_list|(
name|node
argument_list|,
name|parentNode
argument_list|)
return|;
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
name|out
operator|.
name|append
argument_list|(
literal|"datepart("
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|functionExpression
operator|.
name|getPart
argument_list|()
condition|)
block|{
case|case
name|DAY_OF_MONTH
case|:
name|out
operator|.
name|append
argument_list|(
literal|"day"
argument_list|)
expr_stmt|;
break|break;
case|case
name|DAY_OF_WEEK
case|:
name|out
operator|.
name|append
argument_list|(
literal|"weekday"
argument_list|)
expr_stmt|;
break|break;
case|case
name|DAY_OF_YEAR
case|:
name|out
operator|.
name|append
argument_list|(
literal|"dayofyear"
argument_list|)
expr_stmt|;
break|break;
default|default:
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
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|append
argument_list|(
literal|" , "
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

