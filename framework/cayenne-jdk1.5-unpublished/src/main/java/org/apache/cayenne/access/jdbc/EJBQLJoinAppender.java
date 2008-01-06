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
name|jdbc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|ejbql
operator|.
name|EJBQLBaseVisitor
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
name|ejbql
operator|.
name|EJBQLException
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
name|ejbql
operator|.
name|EJBQLExpression
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
name|ejbql
operator|.
name|EJBQLParserFactory
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
name|EntityInheritanceTree
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

begin_comment
comment|/**  * Handles appending joins to the content buffer at a marked position.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|EJBQLJoinAppender
block|{
specifier|protected
name|EJBQLTranslationContext
name|context
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|reusableJoins
decl_stmt|;
specifier|static
name|String
name|makeJoinTailMarker
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
literal|"FROM_TAIL"
operator|+
name|id
return|;
block|}
specifier|public
name|EJBQLJoinAppender
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
comment|/**      * Registers a "reusable" join, returning a preexisting ID if the join is already      * registered. Reusable joins are the implicit inner joins that are added as a result      * of processing of path expressions in SELECT or WHERE clauses. Note that if an      * implicit INNER join overlaps with an explicit INNER join, both joins are added to      * the query.      */
specifier|public
name|String
name|registerReusableJoin
parameter_list|(
name|String
name|sourceIdPath
parameter_list|,
name|String
name|relationship
parameter_list|,
name|String
name|targetId
parameter_list|)
block|{
if|if
condition|(
name|reusableJoins
operator|==
literal|null
condition|)
block|{
name|reusableJoins
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|String
name|key
init|=
name|sourceIdPath
operator|+
literal|":"
operator|+
name|relationship
decl_stmt|;
name|String
name|oldId
init|=
name|reusableJoins
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|targetId
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldId
operator|!=
literal|null
condition|)
block|{
comment|// revert back to old id
name|reusableJoins
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|oldId
argument_list|)
expr_stmt|;
return|return
name|oldId
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|appendInnerJoin
parameter_list|(
name|String
name|marker
parameter_list|,
name|EJBQLTableId
name|lhsId
parameter_list|,
name|EJBQLTableId
name|rhsId
parameter_list|)
block|{
name|appendJoin
argument_list|(
name|marker
argument_list|,
name|lhsId
argument_list|,
name|rhsId
argument_list|,
literal|"INNER JOIN"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|appendOuterJoin
parameter_list|(
name|String
name|marker
parameter_list|,
name|EJBQLTableId
name|lhsId
parameter_list|,
name|EJBQLTableId
name|rhsId
parameter_list|)
block|{
name|appendJoin
argument_list|(
name|marker
argument_list|,
name|lhsId
argument_list|,
name|rhsId
argument_list|,
literal|"LEFT OUTER JOIN"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|appendJoin
parameter_list|(
name|String
name|marker
parameter_list|,
name|EJBQLTableId
name|lhsId
parameter_list|,
name|EJBQLTableId
name|rhsId
parameter_list|,
name|String
name|semantics
parameter_list|)
block|{
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|joinRelationships
init|=
name|context
operator|.
name|getIncomingRelationships
argument_list|(
name|rhsId
argument_list|)
decl_stmt|;
if|if
condition|(
name|joinRelationships
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|EJBQLException
argument_list|(
literal|"No join configured for id "
operator|+
name|rhsId
argument_list|)
throw|;
block|}
comment|// TODO: andrus, 4/8/2007 - support for flattened relationships
name|DbRelationship
name|incomingDB
init|=
name|joinRelationships
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// TODO: andrus, 1/6/2008 - move reusable join check here...
name|String
name|sourceAlias
init|=
name|context
operator|.
name|getTableAlias
argument_list|(
name|lhsId
operator|.
name|getEntityId
argument_list|()
argument_list|,
name|incomingDB
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|marker
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|pushMarker
argument_list|(
name|marker
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|context
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|semantics
argument_list|)
expr_stmt|;
name|String
name|targetAlias
init|=
name|appendTable
argument_list|(
name|rhsId
argument_list|)
decl_stmt|;
name|context
operator|.
name|append
argument_list|(
literal|" ON ("
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|DbJoin
argument_list|>
name|it
init|=
name|incomingDB
operator|.
name|getJoins
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbJoin
name|dbJoin
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|context
operator|.
name|append
argument_list|(
name|sourceAlias
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|dbJoin
operator|.
name|getSourceName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" = "
argument_list|)
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
name|dbJoin
operator|.
name|getTargetName
argument_list|()
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|context
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|DbJoin
name|dbJoin
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|context
operator|.
name|append
argument_list|(
name|sourceAlias
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|dbJoin
operator|.
name|getSourceName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" = "
argument_list|)
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
name|dbJoin
operator|.
name|getTargetName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|marker
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|popMarker
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|String
name|appendTable
parameter_list|(
name|EJBQLTableId
name|id
parameter_list|)
block|{
name|String
name|tableName
init|=
name|id
operator|.
name|getDbEntity
argument_list|(
name|context
argument_list|)
operator|.
name|getFullyQualifiedName
argument_list|()
decl_stmt|;
name|String
name|alias
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|isUsingAliases
argument_list|()
condition|)
block|{
comment|// TODO: andrus 1/5/2007 - if the same table is joined more than once, this
comment|// will create an incorrect alias.
name|alias
operator|=
name|context
operator|.
name|getTableAlias
argument_list|(
name|id
operator|.
name|getEntityId
argument_list|()
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
comment|// not using "AS" to separate table name and alias name - OpenBase doesn't
comment|// support
comment|// "AS", and the rest of the databases do not care
name|context
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|tableName
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|alias
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|tableName
argument_list|)
expr_stmt|;
name|alias
operator|=
name|tableName
expr_stmt|;
block|}
comment|// append inheritance qualifier...
if|if
condition|(
name|id
operator|.
name|isPrimaryTable
argument_list|()
condition|)
block|{
comment|// TODO: andrus 1/6/2008 - this is *extremely* inefficient, especially
comment|// 'ejbqlQualifierForEntityAndSubclasses'. Cache inheritance qualifier in
comment|// the ClassDescriptor
name|ObjEntity
name|entity
init|=
name|context
operator|.
name|getEntityDescriptor
argument_list|(
name|id
operator|.
name|getEntityId
argument_list|()
argument_list|)
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|EntityInheritanceTree
name|inheritanceTree
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupInheritanceTree
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|Expression
name|qualifier
init|=
name|inheritanceTree
operator|!=
literal|null
condition|?
name|inheritanceTree
operator|.
name|qualifierForEntityAndSubclasses
argument_list|()
else|:
name|entity
operator|.
name|getDeclaredQualifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|qualifier
operator|!=
literal|null
condition|)
block|{
name|EJBQLExpression
name|ejbqlQualifier
init|=
name|ejbqlQualifierForEntityAndSubclasses
argument_list|(
name|qualifier
argument_list|,
name|id
operator|.
name|getEntityId
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|pushMarker
argument_list|(
name|EJBQLSelectTranslator
operator|.
name|makeWhereMarker
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|append
argument_list|(
literal|" WHERE"
argument_list|)
expr_stmt|;
name|context
operator|.
name|popMarker
argument_list|()
expr_stmt|;
name|context
operator|.
name|pushMarker
argument_list|(
name|EJBQLSelectTranslator
operator|.
name|makeEntityQualifierMarker
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|ejbqlQualifier
operator|.
name|visit
argument_list|(
name|context
operator|.
name|getTranslatorFactory
argument_list|()
operator|.
name|getConditionTranslator
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|popMarker
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|alias
return|;
block|}
specifier|private
name|EJBQLExpression
name|ejbqlQualifierForEntityAndSubclasses
parameter_list|(
name|Expression
name|qualifier
parameter_list|,
name|String
name|entityId
parameter_list|)
block|{
comment|// parser only works on full queries, so prepend a dummy query and then strip it
comment|// out...
name|String
name|ejbqlChunk
init|=
name|qualifier
operator|.
name|toEJBQL
argument_list|(
name|entityId
argument_list|)
decl_stmt|;
name|EJBQLExpression
name|expression
init|=
name|EJBQLParserFactory
operator|.
name|getParser
argument_list|()
operator|.
name|parse
argument_list|(
literal|"DELETE FROM DUMMY WHERE "
operator|+
name|ejbqlChunk
argument_list|)
decl_stmt|;
specifier|final
name|EJBQLExpression
index|[]
name|result
init|=
operator|new
name|EJBQLExpression
index|[
literal|1
index|]
decl_stmt|;
name|expression
operator|.
name|visit
argument_list|(
operator|new
name|EJBQLBaseVisitor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|visitWhere
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|result
index|[
literal|0
index|]
operator|=
name|expression
operator|.
name|getChild
argument_list|(
literal|0
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|result
index|[
literal|0
index|]
return|;
block|}
block|}
end_class

end_unit

