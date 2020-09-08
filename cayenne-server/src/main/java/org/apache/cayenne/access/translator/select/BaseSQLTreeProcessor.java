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
name|translator
operator|.
name|select
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
name|sqltree
operator|.
name|ColumnNode
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
name|DistinctNode
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
name|InNode
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
name|LikeNode
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
name|SQLTreeProcessor
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
name|SimpleNodeTreeVisitor
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
name|ValueNode
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|BaseSQLTreeProcessor
extends|extends
name|SimpleNodeTreeVisitor
implements|implements
name|SQLTreeProcessor
block|{
annotation|@
name|Override
specifier|public
name|Node
name|process
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
name|node
operator|.
name|visit
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|node
return|;
block|}
specifier|protected
name|void
name|onValueNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|ValueNode
name|child
parameter_list|,
name|int
name|index
parameter_list|)
block|{
block|}
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
block|}
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
block|}
specifier|protected
name|void
name|onColumnNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|ColumnNode
name|child
parameter_list|,
name|int
name|index
parameter_list|)
block|{
block|}
specifier|protected
name|void
name|onInNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|InNode
name|child
parameter_list|,
name|int
name|index
parameter_list|)
block|{
block|}
specifier|protected
name|void
name|onLikeNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|LikeNode
name|child
parameter_list|,
name|int
name|index
parameter_list|)
block|{
block|}
specifier|protected
name|void
name|onResultNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|Node
name|child
parameter_list|,
name|int
name|index
parameter_list|)
block|{
block|}
specifier|protected
name|void
name|onDistinctNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|DistinctNode
name|child
parameter_list|,
name|int
name|index
parameter_list|)
block|{
block|}
specifier|protected
name|void
name|onUndefinedNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|Node
name|child
parameter_list|,
name|int
name|index
parameter_list|)
block|{
block|}
specifier|protected
specifier|static
name|void
name|replaceChild
parameter_list|(
name|Node
name|parent
parameter_list|,
name|int
name|index
parameter_list|,
name|Node
name|newChild
parameter_list|)
block|{
name|replaceChild
argument_list|(
name|parent
argument_list|,
name|index
argument_list|,
name|newChild
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|void
name|replaceChild
parameter_list|(
name|Node
name|parent
parameter_list|,
name|int
name|index
parameter_list|,
name|Node
name|newChild
parameter_list|,
name|boolean
name|transferChildren
parameter_list|)
block|{
if|if
condition|(
name|transferChildren
condition|)
block|{
name|Node
name|oldChild
init|=
name|parent
operator|.
name|getChild
argument_list|(
name|index
argument_list|)
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
name|oldChild
operator|.
name|getChildrenCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|newChild
operator|.
name|addChild
argument_list|(
name|oldChild
operator|.
name|getChild
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|parent
operator|.
name|replaceChild
argument_list|(
name|index
argument_list|,
name|newChild
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|Node
name|wrapInFunction
parameter_list|(
name|Node
name|node
parameter_list|,
name|String
name|function
parameter_list|)
block|{
name|FunctionNode
name|functionNode
init|=
operator|new
name|FunctionNode
argument_list|(
name|function
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|functionNode
operator|.
name|addChild
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|functionNode
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|onChildNodeStart
parameter_list|(
name|Node
name|parent
parameter_list|,
name|Node
name|child
parameter_list|,
name|int
name|index
parameter_list|,
name|boolean
name|hasMore
parameter_list|)
block|{
switch|switch
condition|(
name|child
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|VALUE
case|:
name|onValueNode
argument_list|(
name|parent
argument_list|,
operator|(
name|ValueNode
operator|)
name|child
argument_list|,
name|index
argument_list|)
expr_stmt|;
break|break;
case|case
name|FUNCTION
case|:
name|onFunctionNode
argument_list|(
name|parent
argument_list|,
operator|(
name|FunctionNode
operator|)
name|child
argument_list|,
name|index
argument_list|)
expr_stmt|;
break|break;
case|case
name|LIMIT_OFFSET
case|:
name|onLimitOffsetNode
argument_list|(
name|parent
argument_list|,
operator|(
name|LimitOffsetNode
operator|)
name|child
argument_list|,
name|index
argument_list|)
expr_stmt|;
break|break;
case|case
name|COLUMN
case|:
name|onColumnNode
argument_list|(
name|parent
argument_list|,
operator|(
name|ColumnNode
operator|)
name|child
argument_list|,
name|index
argument_list|)
expr_stmt|;
break|break;
case|case
name|IN
case|:
name|onInNode
argument_list|(
name|parent
argument_list|,
operator|(
name|InNode
operator|)
name|child
argument_list|,
name|index
argument_list|)
expr_stmt|;
break|break;
case|case
name|LIKE
case|:
name|onLikeNode
argument_list|(
name|parent
argument_list|,
operator|(
name|LikeNode
operator|)
name|child
argument_list|,
name|index
argument_list|)
expr_stmt|;
break|break;
case|case
name|RESULT
case|:
name|onResultNode
argument_list|(
name|parent
argument_list|,
name|child
argument_list|,
name|index
argument_list|)
expr_stmt|;
break|break;
case|case
name|DISTINCT
case|:
name|onDistinctNode
argument_list|(
name|parent
argument_list|,
operator|(
name|DistinctNode
operator|)
name|child
argument_list|,
name|index
argument_list|)
expr_stmt|;
break|break;
default|default:
name|onUndefinedNode
argument_list|(
name|parent
argument_list|,
name|child
argument_list|,
name|index
argument_list|)
expr_stmt|;
break|break;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

