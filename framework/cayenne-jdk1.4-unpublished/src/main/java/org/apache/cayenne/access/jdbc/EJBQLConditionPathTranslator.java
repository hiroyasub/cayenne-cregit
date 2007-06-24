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
name|parser
operator|.
name|EJBQLIdentificationVariable
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
name|parser
operator|.
name|EJBQLIdentifier
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
name|parser
operator|.
name|EJBQLInnerJoin
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
name|parser
operator|.
name|EJBQLPath
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
name|reflect
operator|.
name|ClassDescriptor
import|;
end_import

begin_class
specifier|abstract
class|class
name|EJBQLConditionPathTranslator
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
specifier|private
name|String
name|fullPath
decl_stmt|;
specifier|private
name|EJBQLFromTranslator
name|joinAppender
decl_stmt|;
name|EJBQLConditionPathTranslator
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
specifier|public
name|boolean
name|visitPath
parameter_list|(
name|EJBQLPath
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
name|getCompiledExpression
argument_list|()
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
name|fullPath
operator|=
name|idPath
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitIdentificationVariable
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
comment|// TODO: andrus 6/11/2007 - if the path ends with relationship, the last join will
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
name|EJBQLFromTranslator
name|getJoinAppender
parameter_list|()
block|{
if|if
condition|(
name|joinAppender
operator|==
literal|null
condition|)
block|{
name|joinAppender
operator|=
operator|new
name|EJBQLFromTranslator
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
return|return
name|joinAppender
return|;
block|}
specifier|private
name|void
name|resolveJoin
parameter_list|()
block|{
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
name|context
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
name|currentEntity
operator|.
name|getDbEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// register join
name|EJBQLIdentifier
name|id
init|=
operator|new
name|EJBQLIdentifier
argument_list|(
operator|-
literal|1
argument_list|)
decl_stmt|;
name|id
operator|.
name|setText
argument_list|(
name|idPath
argument_list|)
expr_stmt|;
name|EJBQLIdentificationVariable
name|idVar
init|=
operator|new
name|EJBQLIdentificationVariable
argument_list|(
operator|-
literal|1
argument_list|)
decl_stmt|;
name|idVar
operator|.
name|setText
argument_list|(
name|lastPathComponent
argument_list|)
expr_stmt|;
name|EJBQLPath
name|path
init|=
operator|new
name|EJBQLPath
argument_list|(
operator|-
literal|1
argument_list|)
decl_stmt|;
name|path
operator|.
name|jjtAddChild
argument_list|(
name|id
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|path
operator|.
name|jjtAddChild
argument_list|(
name|idVar
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|EJBQLIdentifier
name|joinId
init|=
operator|new
name|EJBQLIdentifier
argument_list|(
operator|-
literal|1
argument_list|)
decl_stmt|;
name|joinId
operator|.
name|setText
argument_list|(
name|fullPath
argument_list|)
expr_stmt|;
name|EJBQLInnerJoin
name|join
init|=
operator|new
name|EJBQLInnerJoin
argument_list|(
operator|-
literal|1
argument_list|)
decl_stmt|;
name|join
operator|.
name|jjtAddChild
argument_list|(
name|path
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|join
operator|.
name|jjtAddChild
argument_list|(
name|joinId
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|switchToMarker
argument_list|(
name|EJBQLTranslationContext
operator|.
name|FROM_TAIL_MARKER
argument_list|)
expr_stmt|;
name|getJoinAppender
argument_list|()
operator|.
name|visitInnerJoin
argument_list|(
name|join
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|switchToMainBuffer
argument_list|()
expr_stmt|;
name|this
operator|.
name|idPath
operator|=
name|newPath
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
name|currentEntity
operator|.
name|getDbEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|processIntermediatePathComponent
parameter_list|()
block|{
name|ObjRelationship
name|relationship
init|=
operator|(
name|ObjRelationship
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
name|ObjEntity
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
name|ObjAttribute
name|attribute
init|=
operator|(
name|ObjAttribute
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
name|ObjRelationship
name|relationship
init|=
operator|(
name|ObjRelationship
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
name|ObjAttribute
name|attribute
parameter_list|)
block|{
name|DbEntity
name|table
init|=
name|currentEntity
operator|.
name|getDbEntity
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
name|table
operator|.
name|getFullyQualifiedName
argument_list|()
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
name|attribute
operator|.
name|getDbAttributeName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processTerminatingRelationship
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
comment|// check whether we need a join
if|if
condition|(
name|relationship
operator|.
name|isSourceIndependentFromTargetChange
argument_list|()
condition|)
block|{
comment|// TODO: andrus, 6/13/2007 - implement
block|}
else|else
block|{
comment|// match FK against the target object
comment|// TODO: andrus, 6/21/2007 - flattened support
name|DbRelationship
name|dbRelationship
init|=
operator|(
name|DbRelationship
operator|)
name|relationship
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
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
name|table
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
decl_stmt|;
name|List
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
operator|(
name|DbJoin
operator|)
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
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Map
name|multiColumnMatch
init|=
operator|new
name|HashMap
argument_list|(
name|joins
operator|.
name|size
argument_list|()
operator|+
literal|2
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|joins
operator|.
name|iterator
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
name|DbJoin
name|join
init|=
operator|(
name|DbJoin
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|column
init|=
name|alias
operator|+
literal|"."
operator|+
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
block|}
end_class

end_unit

