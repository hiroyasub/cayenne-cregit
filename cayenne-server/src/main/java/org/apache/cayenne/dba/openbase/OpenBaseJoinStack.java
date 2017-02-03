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
name|openbase
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
name|translator
operator|.
name|select
operator|.
name|JoinStack
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
name|JoinTreeNode
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
name|dba
operator|.
name|DbAdapter
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
name|DataMap
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
name|DbEntity
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
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbRelationship
import|;
end_import

begin_comment
comment|/**  * OpenBase does not support standard JOIN keyword and have strange syntax for  * defining inner/outer joins  *   * @see http ://www.openbase.com/help/KnowledgeBase/  *      400_OpenBaseSQL/401_SelectStatements.html  * @since 3.0  */
end_comment

begin_class
class|class
name|OpenBaseJoinStack
extends|extends
name|JoinStack
block|{
specifier|protected
name|OpenBaseJoinStack
parameter_list|(
name|DbAdapter
name|dbAdapter
parameter_list|,
name|QueryAssembler
name|assembler
parameter_list|)
block|{
name|super
argument_list|(
name|dbAdapter
argument_list|,
name|assembler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|appendJoinSubtree
parameter_list|(
name|StringBuilder
name|out
parameter_list|,
name|JoinTreeNode
name|node
parameter_list|)
block|{
name|DbRelationship
name|relationship
init|=
name|node
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|relationship
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|DbEntity
name|targetEntity
init|=
name|relationship
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
name|String
name|targetAlias
init|=
name|node
operator|.
name|getTargetTableAlias
argument_list|()
decl_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|targetEntity
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|targetAlias
argument_list|)
expr_stmt|;
for|for
control|(
name|JoinTreeNode
name|child
range|:
name|node
operator|.
name|getChildren
argument_list|()
control|)
block|{
name|appendJoinSubtree
argument_list|(
name|out
argument_list|,
name|child
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|appendQualifier
parameter_list|(
name|StringBuilder
name|out
parameter_list|,
name|boolean
name|firstQualifierElement
parameter_list|)
block|{
name|boolean
name|first
init|=
name|firstQualifierElement
decl_stmt|;
for|for
control|(
name|JoinTreeNode
name|node
range|:
name|rootNode
operator|.
name|getChildren
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|first
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|" AND "
argument_list|)
expr_stmt|;
block|}
name|appendQualifierSubtree
argument_list|(
name|out
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|appendQualifierSubtree
parameter_list|(
name|StringBuilder
name|out
parameter_list|,
name|JoinTreeNode
name|node
parameter_list|)
block|{
name|DbRelationship
name|relationship
init|=
name|node
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
name|String
name|srcAlias
init|=
name|node
operator|.
name|getSourceTableAlias
argument_list|()
decl_stmt|;
name|String
name|targetAlias
init|=
name|node
operator|.
name|getTargetTableAlias
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DbJoin
argument_list|>
name|joins
init|=
name|relationship
operator|.
name|getJoins
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|joins
operator|.
name|size
argument_list|()
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
name|len
condition|;
name|i
operator|++
control|)
block|{
name|DbJoin
name|join
init|=
name|joins
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|" AND "
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|append
argument_list|(
name|srcAlias
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
expr_stmt|;
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
name|out
operator|.
name|append
argument_list|(
literal|" = "
argument_list|)
expr_stmt|;
break|break;
case|case
name|LEFT_OUTER
case|:
name|out
operator|.
name|append
argument_list|(
literal|" * "
argument_list|)
expr_stmt|;
break|break;
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
name|out
operator|.
name|append
argument_list|(
name|targetAlias
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|JoinTreeNode
name|child
range|:
name|node
operator|.
name|getChildren
argument_list|()
control|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|" AND "
argument_list|)
expr_stmt|;
name|appendQualifierSubtree
argument_list|(
name|out
argument_list|,
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

