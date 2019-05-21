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
name|ejbql
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|ObjAttribute
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
name|cayenne
operator|.
name|map
operator|.
name|ObjRelationship
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
name|Relationship
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
name|reflect
operator|.
name|ClassDescriptor
import|;
end_import

begin_comment
comment|/**  * A translator that walks the relationship/attribute path, appending joins to  * the query.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|EJBQLPathTranslator
extends|extends
name|EJBQLBaseVisitor
block|{
specifier|private
name|EJBQLTranslationContext
name|context
decl_stmt|;
specifier|protected
name|ObjEntity
name|currentEntity
decl_stmt|;
specifier|protected
name|String
name|lastPathComponent
decl_stmt|;
specifier|protected
name|boolean
name|innerJoin
decl_stmt|;
specifier|protected
name|String
name|lastAlias
decl_stmt|;
specifier|protected
name|String
name|idPath
decl_stmt|;
specifier|protected
name|String
name|joinMarker
decl_stmt|;
specifier|protected
name|String
name|fullPath
decl_stmt|;
specifier|private
name|boolean
name|usingAliases
decl_stmt|;
specifier|public
name|EJBQLPathTranslator
parameter_list|(
name|EJBQLTranslationContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|usingAliases
operator|=
literal|true
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|void
name|appendMultiColumnPath
parameter_list|(
name|EJBQLMultiColumnOperand
name|operand
parameter_list|)
function_decl|;
annotation|@
name|Override
specifier|public
name|boolean
name|visitPath
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
if|if
condition|(
name|finishedChildIndex
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|finishedChildIndex
operator|+
literal|1
operator|<
name|expression
operator|.
name|getChildrenCount
argument_list|()
condition|)
block|{
name|processIntermediatePathComponent
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|processLastPathComponent
argument_list|()
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitIdentifier
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|ClassDescriptor
name|descriptor
init|=
name|context
operator|.
name|getEntityDescriptor
argument_list|(
name|expression
operator|.
name|getText
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|descriptor
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|EJBQLException
argument_list|(
literal|"Invalid identification variable: "
operator|+
name|expression
operator|.
name|getText
argument_list|()
argument_list|)
throw|;
block|}
name|this
operator|.
name|currentEntity
operator|=
name|descriptor
operator|.
name|getEntity
argument_list|()
expr_stmt|;
name|this
operator|.
name|idPath
operator|=
name|expression
operator|.
name|getText
argument_list|()
expr_stmt|;
name|this
operator|.
name|joinMarker
operator|=
name|EJBQLJoinAppender
operator|.
name|makeJoinTailMarker
argument_list|(
name|idPath
argument_list|)
expr_stmt|;
name|this
operator|.
name|fullPath
operator|=
name|idPath
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitIdentificationVariable
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
comment|// TODO: andrus 6/11/2007 - if the path ends with relationship, the last
comment|// join will
comment|// get lost...
if|if
condition|(
name|lastPathComponent
operator|!=
literal|null
condition|)
block|{
name|resolveJoin
argument_list|()
expr_stmt|;
block|}
name|resolveLastPathComponent
argument_list|(
name|expression
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|protected
name|void
name|resolveLastPathComponent
parameter_list|(
name|String
name|pathComponent
parameter_list|)
block|{
if|if
condition|(
name|pathComponent
operator|.
name|endsWith
argument_list|(
name|Entity
operator|.
name|OUTER_JOIN_INDICATOR
argument_list|)
condition|)
block|{
name|this
operator|.
name|lastPathComponent
operator|=
name|pathComponent
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pathComponent
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|this
operator|.
name|innerJoin
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|lastPathComponent
operator|=
name|pathComponent
expr_stmt|;
name|this
operator|.
name|innerJoin
operator|=
literal|true
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|resolveJoin
parameter_list|()
block|{
name|EJBQLJoinAppender
name|joinAppender
init|=
name|context
operator|.
name|getTranslatorFactory
argument_list|()
operator|.
name|getJoinAppender
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|String
name|newPath
init|=
name|idPath
operator|+
literal|'.'
operator|+
name|lastPathComponent
decl_stmt|;
name|String
name|oldPath
init|=
name|joinAppender
operator|.
name|registerReusableJoin
argument_list|(
name|idPath
argument_list|,
name|lastPathComponent
argument_list|,
name|newPath
argument_list|)
decl_stmt|;
name|this
operator|.
name|fullPath
operator|=
name|fullPath
operator|+
literal|'.'
operator|+
name|lastPathComponent
expr_stmt|;
if|if
condition|(
name|oldPath
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|idPath
operator|=
name|oldPath
expr_stmt|;
name|Relationship
name|lastRelationship
init|=
name|currentEntity
operator|.
name|getRelationship
argument_list|(
name|lastPathComponent
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastRelationship
operator|!=
literal|null
condition|)
block|{
name|ObjEntity
name|targetEntity
init|=
operator|(
name|ObjEntity
operator|)
name|lastRelationship
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
name|this
operator|.
name|lastAlias
operator|=
name|context
operator|.
name|getTableAlias
argument_list|(
name|fullPath
argument_list|,
name|context
operator|.
name|getQuotingStrategy
argument_list|()
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|targetEntity
operator|.
name|getDbEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|tableName
init|=
name|context
operator|.
name|getQuotingStrategy
argument_list|()
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|currentEntity
operator|.
name|getDbEntity
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|lastAlias
operator|=
name|context
operator|.
name|getTableAlias
argument_list|(
name|oldPath
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Relationship
name|lastRelationship
init|=
name|currentEntity
operator|.
name|getRelationship
argument_list|(
name|lastPathComponent
argument_list|)
decl_stmt|;
name|ObjEntity
name|targetEntity
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|lastRelationship
operator|!=
literal|null
condition|)
block|{
name|targetEntity
operator|=
operator|(
name|ObjEntity
operator|)
name|lastRelationship
operator|.
name|getTargetEntity
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|targetEntity
operator|=
name|currentEntity
expr_stmt|;
block|}
comment|// register join
if|if
condition|(
name|innerJoin
condition|)
block|{
name|joinAppender
operator|.
name|appendInnerJoin
argument_list|(
name|joinMarker
argument_list|,
operator|new
name|EJBQLTableId
argument_list|(
name|idPath
argument_list|)
argument_list|,
operator|new
name|EJBQLTableId
argument_list|(
name|fullPath
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|joinAppender
operator|.
name|appendOuterJoin
argument_list|(
name|joinMarker
argument_list|,
operator|new
name|EJBQLTableId
argument_list|(
name|idPath
argument_list|)
argument_list|,
operator|new
name|EJBQLTableId
argument_list|(
name|fullPath
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|lastAlias
operator|=
name|context
operator|.
name|getTableAlias
argument_list|(
name|fullPath
argument_list|,
name|context
operator|.
name|getQuotingStrategy
argument_list|()
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|targetEntity
operator|.
name|getDbEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|idPath
operator|=
name|newPath
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|processIntermediatePathComponent
parameter_list|()
block|{
name|ObjRelationship
name|relationship
init|=
name|currentEntity
operator|.
name|getRelationship
argument_list|(
name|lastPathComponent
argument_list|)
decl_stmt|;
if|if
condition|(
name|relationship
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|EJBQLException
argument_list|(
literal|"Unknown relationship '"
operator|+
name|lastPathComponent
operator|+
literal|"' for entity '"
operator|+
name|currentEntity
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|this
operator|.
name|currentEntity
operator|=
operator|(
name|ObjEntity
operator|)
name|relationship
operator|.
name|getTargetEntity
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|processLastPathComponent
parameter_list|()
block|{
name|ObjAttribute
name|attribute
init|=
name|currentEntity
operator|.
name|getAttribute
argument_list|(
name|lastPathComponent
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
name|processTerminatingAttribute
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
return|return;
block|}
name|ObjRelationship
name|relationship
init|=
name|currentEntity
operator|.
name|getRelationship
argument_list|(
name|lastPathComponent
argument_list|)
decl_stmt|;
if|if
condition|(
name|relationship
operator|!=
literal|null
condition|)
block|{
name|processTerminatingRelationship
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
return|return;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Invalid path component: "
operator|+
name|lastPathComponent
argument_list|)
throw|;
block|}
specifier|protected
name|void
name|processTerminatingAttribute
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
block|{
name|DbEntity
name|table
init|=
literal|null
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|attribute
operator|.
name|getDbPathIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|pathComponent
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathComponent
operator|instanceof
name|DbAttribute
condition|)
block|{
name|table
operator|=
operator|(
name|DbEntity
operator|)
operator|(
operator|(
name|DbAttribute
operator|)
name|pathComponent
operator|)
operator|.
name|getEntity
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|isUsingAliases
argument_list|()
condition|)
block|{
name|String
name|alias
init|=
name|this
operator|.
name|lastAlias
operator|!=
literal|null
condition|?
name|lastAlias
else|:
name|context
operator|.
name|getTableAlias
argument_list|(
name|idPath
argument_list|,
name|context
operator|.
name|getQuotingStrategy
argument_list|()
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|table
argument_list|)
argument_list|)
decl_stmt|;
name|context
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
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|context
operator|.
name|getQuotingStrategy
argument_list|()
operator|.
name|quotedName
argument_list|(
name|attribute
operator|.
name|getDbAttribute
argument_list|()
argument_list|)
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
name|context
operator|.
name|getQuotingStrategy
argument_list|()
operator|.
name|quotedName
argument_list|(
name|attribute
operator|.
name|getDbAttribute
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|processTerminatingRelationship
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
if|if
condition|(
name|relationship
operator|.
name|isSourceIndependentFromTargetChange
argument_list|()
condition|)
block|{
comment|// (andrus) use an outer join for to-many matches.. This is somewhat
comment|// different
comment|// from traditional Cayenne SelectQuery, as EJBQL spec does not
comment|// allow regular
comment|// path matches done against to-many relationships, and instead
comment|// provides
comment|// MEMBER OF and IS EMPTY operators. Outer join is needed for IS
comment|// EMPTY... I
comment|// guess MEMBER OF could've been done with an inner join though..
name|this
operator|.
name|innerJoin
operator|=
literal|false
expr_stmt|;
name|resolveJoin
argument_list|()
expr_stmt|;
name|DbRelationship
name|dbRelationship
init|=
name|chooseDbRelationship
argument_list|(
name|relationship
argument_list|)
decl_stmt|;
name|DbEntity
name|table
init|=
operator|(
name|DbEntity
operator|)
name|dbRelationship
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
name|String
name|alias
init|=
name|this
operator|.
name|lastAlias
operator|!=
literal|null
condition|?
name|lastAlias
else|:
name|context
operator|.
name|getTableAlias
argument_list|(
name|idPath
argument_list|,
name|context
operator|.
name|getQuotingStrategy
argument_list|()
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|table
argument_list|)
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|pks
init|=
name|table
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|pks
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|DbAttribute
name|pk
init|=
name|pks
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|context
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
if|if
condition|(
name|isUsingAliases
argument_list|()
condition|)
block|{
name|context
operator|.
name|append
argument_list|(
name|alias
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|append
argument_list|(
name|context
operator|.
name|getQuotingStrategy
argument_list|()
operator|.
name|quotedName
argument_list|(
name|pk
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|EJBQLException
argument_list|(
literal|"Multi-column PK to-many matches are not yet supported."
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// match FK against the target object
name|DbRelationship
name|dbRelationship
init|=
name|chooseDbRelationship
argument_list|(
name|relationship
argument_list|)
decl_stmt|;
name|DbEntity
name|table
init|=
operator|(
name|DbEntity
operator|)
name|dbRelationship
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
name|String
name|alias
init|=
name|this
operator|.
name|lastAlias
operator|!=
literal|null
condition|?
name|lastAlias
else|:
name|context
operator|.
name|getTableAlias
argument_list|(
name|idPath
argument_list|,
name|context
operator|.
name|getQuotingStrategy
argument_list|()
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|table
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbJoin
argument_list|>
name|joins
init|=
name|dbRelationship
operator|.
name|getJoins
argument_list|()
decl_stmt|;
if|if
condition|(
name|joins
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|DbJoin
name|join
init|=
name|joins
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|context
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
if|if
condition|(
name|isUsingAliases
argument_list|()
condition|)
block|{
name|context
operator|.
name|append
argument_list|(
name|alias
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|append
argument_list|(
name|context
operator|.
name|getQuotingStrategy
argument_list|()
operator|.
name|quotedName
argument_list|(
name|join
operator|.
name|getSource
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|multiColumnMatch
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|joins
operator|.
name|size
argument_list|()
operator|+
literal|2
argument_list|)
decl_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|joins
control|)
block|{
name|String
name|column
init|=
name|isUsingAliases
argument_list|()
condition|?
name|alias
operator|+
literal|"."
operator|+
name|join
operator|.
name|getSourceName
argument_list|()
else|:
name|join
operator|.
name|getSourceName
argument_list|()
decl_stmt|;
name|multiColumnMatch
operator|.
name|put
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|,
name|column
argument_list|)
expr_stmt|;
block|}
name|appendMultiColumnPath
argument_list|(
name|EJBQLMultiColumnOperand
operator|.
name|getPathOperand
argument_list|(
name|context
argument_list|,
name|multiColumnMatch
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** 	 * Checks if the object relationship is flattened and then chooses the 	 * corresponding db relationship. The last in idPath if isFlattened and the 	 * first in list otherwise. 	 *  	 * @param relationship 	 *            the object relationship 	 *  	 * @return {@link DbRelationship} 	 */
specifier|protected
name|DbRelationship
name|chooseDbRelationship
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|dbRelationships
init|=
name|relationship
operator|.
name|getDbRelationships
argument_list|()
decl_stmt|;
name|String
name|dbRelationshipPath
init|=
name|relationship
operator|.
name|getDbRelationshipPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbRelationshipPath
operator|.
name|contains
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
name|String
name|dbRelName
init|=
name|dbRelationshipPath
operator|.
name|substring
argument_list|(
name|dbRelationshipPath
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|DbRelationship
name|dbR
range|:
name|dbRelationships
control|)
block|{
if|if
condition|(
name|dbR
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|dbRelName
argument_list|)
condition|)
block|{
return|return
name|dbR
return|;
block|}
block|}
block|}
return|return
name|relationship
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isUsingAliases
parameter_list|()
block|{
return|return
name|usingAliases
return|;
block|}
specifier|public
name|void
name|setUsingAliases
parameter_list|(
name|boolean
name|usingAliases
parameter_list|)
block|{
name|this
operator|.
name|usingAliases
operator|=
name|usingAliases
expr_stmt|;
block|}
block|}
end_class

end_unit

