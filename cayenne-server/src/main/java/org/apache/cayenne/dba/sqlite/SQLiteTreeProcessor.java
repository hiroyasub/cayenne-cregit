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
name|dba
operator|.
name|sqlite
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
name|QuotingAppendable
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
name|sqltree
operator|.
name|FunctionNode
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
name|sqltree
operator|.
name|LimitOffsetNode
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
name|sqltree
operator|.
name|Node
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
name|sqltree
operator|.
name|OpExpressionNode
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
name|sqltree
operator|.
name|TextNode
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
name|BaseSQLTreeProcessor
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
name|dba
operator|.
name|mysql
operator|.
name|sqltree
operator|.
name|MysqlLimitOffsetNode
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|SQLiteTreeProcessor
extends|extends
name|BaseSQLTreeProcessor
block|{
annotation|@
name|Override
specifier|protected
name|void
name|onLimitOffsetNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|LimitOffsetNode
name|child
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|replaceChild
argument_list|(
name|parent
argument_list|,
name|index
argument_list|,
operator|new
name|MysqlLimitOffsetNode
argument_list|(
name|child
operator|.
name|getLimit
argument_list|()
argument_list|,
name|child
operator|.
name|getOffset
argument_list|()
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onFunctionNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|FunctionNode
name|child
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|String
name|functionName
init|=
name|child
operator|.
name|getFunctionName
argument_list|()
decl_stmt|;
name|Node
name|replacement
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|functionName
condition|)
block|{
case|case
literal|"LOCATE"
case|:
name|replacement
operator|=
operator|new
name|FunctionNode
argument_list|(
literal|"INSTR"
argument_list|,
name|child
operator|.
name|getAlias
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<=
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|replacement
operator|.
name|addChild
argument_list|(
name|child
operator|.
name|getChild
argument_list|(
literal|1
operator|-
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|parent
operator|.
name|replaceChild
argument_list|(
name|index
argument_list|,
name|replacement
argument_list|)
expr_stmt|;
return|return;
case|case
literal|"DAY_OF_YEAR"
case|:
name|replaceExtractFunction
argument_list|(
name|parent
argument_list|,
name|child
argument_list|,
name|index
argument_list|,
literal|"'%j'"
argument_list|)
expr_stmt|;
return|return;
case|case
literal|"DAY_OF_WEEK"
case|:
name|replaceExtractFunction
argument_list|(
name|parent
argument_list|,
name|child
argument_list|,
name|index
argument_list|,
literal|"'%w'"
argument_list|)
expr_stmt|;
return|return;
case|case
literal|"WEEK"
case|:
name|replaceExtractFunction
argument_list|(
name|parent
argument_list|,
name|child
argument_list|,
name|index
argument_list|,
literal|"'%W'"
argument_list|)
expr_stmt|;
return|return;
case|case
literal|"YEAR"
case|:
name|replaceExtractFunction
argument_list|(
name|parent
argument_list|,
name|child
argument_list|,
name|index
argument_list|,
literal|"'%Y'"
argument_list|)
expr_stmt|;
return|return;
case|case
literal|"MONTH"
case|:
name|replaceExtractFunction
argument_list|(
name|parent
argument_list|,
name|child
argument_list|,
name|index
argument_list|,
literal|"'%m'"
argument_list|)
expr_stmt|;
return|return;
case|case
literal|"DAY"
case|:
case|case
literal|"DAY_OF_MONTH"
case|:
name|replaceExtractFunction
argument_list|(
name|parent
argument_list|,
name|child
argument_list|,
name|index
argument_list|,
literal|"'%d'"
argument_list|)
expr_stmt|;
return|return;
case|case
literal|"HOUR"
case|:
name|replaceExtractFunction
argument_list|(
name|parent
argument_list|,
name|child
argument_list|,
name|index
argument_list|,
literal|"'%H'"
argument_list|)
expr_stmt|;
return|return;
case|case
literal|"MINUTE"
case|:
name|replaceExtractFunction
argument_list|(
name|parent
argument_list|,
name|child
argument_list|,
name|index
argument_list|,
literal|"'%M'"
argument_list|)
expr_stmt|;
return|return;
case|case
literal|"SECOND"
case|:
name|replaceExtractFunction
argument_list|(
name|parent
argument_list|,
name|child
argument_list|,
name|index
argument_list|,
literal|"'%S'"
argument_list|)
expr_stmt|;
return|return;
case|case
literal|"SUBSTRING"
case|:
name|replacement
operator|=
operator|new
name|FunctionNode
argument_list|(
literal|"SUBSTR"
argument_list|,
name|child
operator|.
name|getAlias
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"CONCAT"
case|:
name|replacement
operator|=
operator|new
name|OpExpressionNode
argument_list|(
literal|"||"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"MOD"
case|:
name|replacement
operator|=
operator|new
name|OpExpressionNode
argument_list|(
literal|"%"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"CURRENT_DATE"
case|:
case|case
literal|"CURRENT_TIMESTAMP"
case|:
case|case
literal|"CURRENT_TIME"
case|:
name|replacement
operator|=
operator|new
name|FunctionNode
argument_list|(
name|functionName
argument_list|,
name|child
operator|.
name|getAlias
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|replacement
operator|!=
literal|null
condition|)
block|{
name|replaceChild
argument_list|(
name|parent
argument_list|,
name|index
argument_list|,
name|replacement
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|replaceExtractFunction
parameter_list|(
name|Node
name|parent
parameter_list|,
name|FunctionNode
name|original
parameter_list|,
name|int
name|index
parameter_list|,
name|String
name|format
parameter_list|)
block|{
name|Node
name|replacement
init|=
operator|new
name|FunctionNode
argument_list|(
literal|"cast"
argument_list|,
name|original
operator|.
name|getAlias
argument_list|()
argument_list|,
literal|true
argument_list|)
block|{
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
name|buffer
operator|.
name|append
argument_list|(
literal|" as "
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|FunctionNode
name|strftime
init|=
operator|new
name|FunctionNode
argument_list|(
literal|"strftime"
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|strftime
operator|.
name|addChild
argument_list|(
operator|new
name|TextNode
argument_list|(
name|format
argument_list|)
argument_list|)
expr_stmt|;
name|strftime
operator|.
name|addChild
argument_list|(
name|original
operator|.
name|getChild
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|replacement
operator|.
name|addChild
argument_list|(
name|strftime
argument_list|)
expr_stmt|;
name|replacement
operator|.
name|addChild
argument_list|(
operator|new
name|TextNode
argument_list|(
literal|"integer"
argument_list|)
argument_list|)
expr_stmt|;
name|parent
operator|.
name|replaceChild
argument_list|(
name|index
argument_list|,
name|replacement
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

