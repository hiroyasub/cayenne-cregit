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
name|java
operator|.
name|util
operator|.
name|List
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
name|ExpressionNodeBuilder
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
name|JoinNodeBuilder
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
name|NodeBuilder
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
name|map
operator|.
name|DbAttribute
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
name|map
operator|.
name|DbJoin
import|;
end_import

begin_import
import|import static
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
name|SQLBuilder
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
class|class
name|TableTreeStage
implements|implements
name|TranslationStage
block|{
annotation|@
name|Override
specifier|public
name|void
name|perform
parameter_list|(
name|TranslatorContext
name|context
parameter_list|)
block|{
name|context
operator|.
name|getTableTree
argument_list|()
operator|.
name|visit
argument_list|(
name|node
lambda|->
block|{
name|NodeBuilder
name|tableNode
init|=
name|table
argument_list|(
name|node
operator|.
name|getEntity
argument_list|()
argument_list|)
operator|.
name|as
argument_list|(
name|node
operator|.
name|getTableAlias
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|.
name|getRelationship
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|tableNode
operator|=
name|getJoin
argument_list|(
name|node
argument_list|,
name|tableNode
argument_list|)
operator|.
name|on
argument_list|(
name|getJoinExpression
argument_list|(
name|context
argument_list|,
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|getSelectBuilder
argument_list|()
operator|.
name|from
argument_list|(
name|tableNode
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|JoinNodeBuilder
name|getJoin
parameter_list|(
name|TableTreeNode
name|node
parameter_list|,
name|NodeBuilder
name|table
parameter_list|)
block|{
switch|switch
condition|(
name|node
operator|.
name|getJoinType
argument_list|()
condition|)
block|{
case|case
name|INNER
case|:
return|return
name|join
argument_list|(
name|table
argument_list|)
return|;
case|case
name|LEFT_OUTER
case|:
return|return
name|leftJoin
argument_list|(
name|table
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported join type: "
operator|+
name|node
operator|.
name|getJoinType
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|NodeBuilder
name|getJoinExpression
parameter_list|(
name|TranslatorContext
name|context
parameter_list|,
name|TableTreeNode
name|node
parameter_list|)
block|{
name|List
argument_list|<
name|DbJoin
argument_list|>
name|joins
init|=
name|node
operator|.
name|getRelationship
argument_list|()
operator|.
name|getJoins
argument_list|()
decl_stmt|;
name|ExpressionNodeBuilder
name|expressionNodeBuilder
init|=
literal|null
decl_stmt|;
name|String
name|sourceAlias
init|=
name|context
operator|.
name|getTableTree
argument_list|()
operator|.
name|aliasForPath
argument_list|(
name|node
operator|.
name|getAttributePath
argument_list|()
operator|.
name|getParent
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|DbJoin
name|dbJoin
range|:
name|joins
control|)
block|{
name|DbAttribute
name|src
init|=
name|dbJoin
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|DbAttribute
name|dst
init|=
name|dbJoin
operator|.
name|getTarget
argument_list|()
decl_stmt|;
name|ExpressionNodeBuilder
name|joinExp
init|=
name|table
argument_list|(
name|sourceAlias
argument_list|)
operator|.
name|column
argument_list|(
name|src
argument_list|)
operator|.
name|eq
argument_list|(
name|table
argument_list|(
name|node
operator|.
name|getTableAlias
argument_list|()
argument_list|)
operator|.
name|column
argument_list|(
name|dst
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|expressionNodeBuilder
operator|!=
literal|null
condition|)
block|{
name|expressionNodeBuilder
operator|=
name|expressionNodeBuilder
operator|.
name|and
argument_list|(
name|joinExp
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|expressionNodeBuilder
operator|=
name|joinExp
expr_stmt|;
block|}
block|}
return|return
name|expressionNodeBuilder
return|;
block|}
block|}
end_class

end_unit

