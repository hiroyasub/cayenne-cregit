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
name|access
operator|.
name|trans
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
name|dba
operator|.
name|QuotingStrategy
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
name|ASTDbPath
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
name|ASTObjPath
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
name|SimpleNode
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
name|Entity
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
name|JoinType
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
name|ObjEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|Transformer
import|;
end_import

begin_comment
comment|/**  * Encapsulates join reuse/split logic used in SelectQuery processing. All  * expression path's that exist in the query (in the qualifier, etc.) are  * processed to produce a combined join tree.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|JoinStack
block|{
specifier|protected
name|JoinTreeNode
name|rootNode
decl_stmt|;
specifier|protected
name|JoinTreeNode
name|topNode
decl_stmt|;
specifier|private
name|QuotingStrategy
name|quotingStrategy
decl_stmt|;
specifier|private
name|int
name|aliasCounter
decl_stmt|;
comment|/**      * Helper class to process DbEntity qualifiers      */
specifier|private
name|QualifierTranslator
name|qualifierTranslator
decl_stmt|;
specifier|protected
name|JoinStack
parameter_list|(
name|DbAdapter
name|dbAdapter
parameter_list|,
name|DataMap
name|dataMap
parameter_list|,
name|QueryAssembler
name|assembler
parameter_list|)
block|{
name|this
operator|.
name|rootNode
operator|=
operator|new
name|JoinTreeNode
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|rootNode
operator|.
name|setTargetTableAlias
argument_list|(
name|newAlias
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|quotingStrategy
operator|=
name|dbAdapter
operator|.
name|getQuotingStrategy
argument_list|()
expr_stmt|;
name|this
operator|.
name|qualifierTranslator
operator|=
name|dbAdapter
operator|.
name|getQualifierTranslator
argument_list|(
name|assembler
argument_list|)
expr_stmt|;
name|resetStack
argument_list|()
expr_stmt|;
block|}
name|String
name|getCurrentAlias
parameter_list|()
block|{
return|return
name|topNode
operator|.
name|getTargetTableAlias
argument_list|()
return|;
block|}
comment|/**      * Returns the number of configured joins.      */
specifier|protected
name|int
name|size
parameter_list|()
block|{
comment|// do not count root as a join
return|return
name|rootNode
operator|.
name|size
argument_list|()
operator|-
literal|1
return|;
block|}
name|void
name|appendRootWithQuoteSqlIdentifiers
parameter_list|(
name|Appendable
name|out
parameter_list|,
name|DbEntity
name|rootEntity
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|append
argument_list|(
name|quotingStrategy
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|rootEntity
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|quotingStrategy
operator|.
name|quotedIdentifier
argument_list|(
name|rootEntity
argument_list|,
name|rootNode
operator|.
name|getTargetTableAlias
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Appends all configured joins to the provided output object.      */
specifier|protected
name|void
name|appendJoins
parameter_list|(
name|Appendable
name|out
parameter_list|)
throws|throws
name|IOException
block|{
comment|// skip root, recursively append its children
for|for
control|(
name|JoinTreeNode
name|child
range|:
name|rootNode
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
specifier|protected
name|void
name|appendJoinSubtree
parameter_list|(
name|Appendable
name|out
parameter_list|,
name|JoinTreeNode
name|node
parameter_list|)
throws|throws
name|IOException
block|{
name|DbRelationship
name|relationship
init|=
name|node
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
name|DbEntity
name|targetEntity
init|=
operator|(
name|DbEntity
operator|)
name|relationship
operator|.
name|getTargetEntity
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
literal|" JOIN"
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
literal|" LEFT JOIN"
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
literal|' '
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|quotingStrategy
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|targetEntity
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|quotingStrategy
operator|.
name|quotedIdentifier
argument_list|(
name|targetEntity
argument_list|,
name|targetAlias
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|" ON ("
argument_list|)
expr_stmt|;
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
name|quotingStrategy
operator|.
name|quotedIdentifier
argument_list|(
name|relationship
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|srcAlias
argument_list|,
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|" = "
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|quotingStrategy
operator|.
name|quotedIdentifier
argument_list|(
name|targetEntity
argument_list|,
name|targetAlias
argument_list|,
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**          * Attaching root Db entity's qualifier          */
name|Expression
name|dbQualifier
init|=
name|targetEntity
operator|.
name|getQualifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbQualifier
operator|!=
literal|null
condition|)
block|{
name|dbQualifier
operator|=
name|dbQualifier
operator|.
name|transform
argument_list|(
operator|new
name|JoinedDbEntityQualifierTransformer
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|len
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
name|qualifierTranslator
operator|.
name|setOut
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|qualifierTranslator
operator|.
name|doAppendPart
argument_list|(
name|dbQualifier
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|append
argument_list|(
literal|')'
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
comment|/**      * Append join information to the qualifier - the part after "WHERE".      */
specifier|protected
name|void
name|appendQualifier
parameter_list|(
name|Appendable
name|out
parameter_list|,
name|boolean
name|firstQualifierElement
parameter_list|)
throws|throws
name|IOException
block|{
comment|// nothing as standard join is performed before "WHERE"
block|}
comment|/**      * Pops the stack all the way to the root node.      */
name|void
name|resetStack
parameter_list|()
block|{
name|topNode
operator|=
name|rootNode
expr_stmt|;
block|}
comment|/**      * Finds or creates a JoinTreeNode for the given arguments and sets it as      * the next current join.      */
name|void
name|pushJoin
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|,
name|JoinType
name|joinType
parameter_list|,
name|String
name|alias
parameter_list|)
block|{
name|topNode
operator|=
name|topNode
operator|.
name|findOrCreateChild
argument_list|(
name|relationship
argument_list|,
name|joinType
argument_list|,
name|alias
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|newAlias
parameter_list|()
block|{
return|return
literal|"t"
operator|+
name|aliasCounter
operator|++
return|;
block|}
comment|/**      * Class to translate *joined* DB Entity qualifiers annotation to *current*      * Obj-entity qualifiers annotation This is done by changing all Obj-paths      * to concatenated Db-paths to root entity and rejecting all original      * Db-paths      */
class|class
name|JoinedDbEntityQualifierTransformer
implements|implements
name|Transformer
block|{
name|StringBuilder
name|pathToRoot
decl_stmt|;
name|JoinedDbEntityQualifierTransformer
parameter_list|(
name|JoinTreeNode
name|node
parameter_list|)
block|{
name|pathToRoot
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
while|while
condition|(
name|node
operator|!=
literal|null
operator|&&
name|node
operator|.
name|getRelationship
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|relName
init|=
name|node
operator|.
name|getRelationship
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**                  * We must be in the same join as 'node', otherwise incorrect                  * join statement like JOIN t1 ... ON (t0.id=t1.id AND                  * t2.qualifier=0) could be generated                  */
if|if
condition|(
name|node
operator|.
name|getJoinType
argument_list|()
operator|==
name|JoinType
operator|.
name|LEFT_OUTER
condition|)
block|{
name|relName
operator|+=
name|Entity
operator|.
name|OUTER_JOIN_INDICATOR
expr_stmt|;
block|}
name|relName
operator|+=
name|ObjEntity
operator|.
name|PATH_SEPARATOR
expr_stmt|;
name|pathToRoot
operator|.
name|insert
argument_list|(
literal|0
argument_list|,
name|relName
argument_list|)
expr_stmt|;
name|node
operator|=
name|node
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|Object
name|transform
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
if|if
condition|(
name|input
operator|instanceof
name|ASTObjPath
condition|)
block|{
return|return
operator|new
name|ASTDbPath
argument_list|(
name|pathToRoot
operator|.
name|toString
argument_list|()
operator|+
operator|(
operator|(
name|SimpleNode
operator|)
name|input
operator|)
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
return|return
name|input
return|;
block|}
block|}
block|}
end_class

end_unit
