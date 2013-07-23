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

begin_class
specifier|public
specifier|abstract
class|class
name|EJBQLDbPathTranslator
extends|extends
name|EJBQLBaseVisitor
block|{
specifier|private
name|EJBQLTranslationContext
name|context
decl_stmt|;
specifier|protected
name|DbEntity
name|currentEntity
decl_stmt|;
specifier|private
name|String
name|lastPathComponent
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
specifier|private
name|String
name|fullPath
decl_stmt|;
specifier|private
name|boolean
name|usingAliases
decl_stmt|;
specifier|public
name|EJBQLDbPathTranslator
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
name|visitDbPath
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
comment|// expression id is always rooted in an ObjEntity, even for DbPath...
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
operator|.
name|getDbEntity
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
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|lastPathComponent
operator|=
name|expression
operator|.
name|getText
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|resolveJoin
parameter_list|(
name|boolean
name|inner
parameter_list|)
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
comment|// TODO: andrus 1/6/2007 - conflict with object path naming... maybe
comment|// 'registerReusableJoin' should normalize everything to a db path?
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
name|context
operator|.
name|getQuotingStrategy
argument_list|()
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|currentEntity
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// register join
if|if
condition|(
name|inner
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
name|currentEntity
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
name|DbEntity
name|targetEntity
init|=
operator|(
name|DbEntity
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
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|idPath
operator|=
name|newPath
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|processIntermediatePathComponent
parameter_list|()
block|{
name|DbRelationship
name|relationship
init|=
operator|(
name|DbRelationship
operator|)
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
name|DbEntity
operator|)
name|relationship
operator|.
name|getTargetEntity
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|processLastPathComponent
parameter_list|()
block|{
name|DbAttribute
name|attribute
init|=
operator|(
name|DbAttribute
operator|)
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
name|DbRelationship
name|relationship
init|=
operator|(
name|DbRelationship
operator|)
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
name|DbAttribute
name|attribute
parameter_list|)
block|{
name|DbEntity
name|table
init|=
operator|(
name|DbEntity
operator|)
name|attribute
operator|.
name|getEntity
argument_list|()
decl_stmt|;
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
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|processTerminatingRelationship
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|)
block|{
if|if
condition|(
name|relationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
comment|// use an outer join for to-many matches
name|resolveJoin
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|DbEntity
name|table
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
name|DbEntity
name|table
init|=
operator|(
name|DbEntity
operator|)
name|relationship
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
name|relationship
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
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
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
