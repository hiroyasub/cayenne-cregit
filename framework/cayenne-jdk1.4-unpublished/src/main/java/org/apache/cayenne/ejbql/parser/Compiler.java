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
name|ejbql
operator|.
name|parser
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
name|EJBQLCompiledExpression
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
name|EJBQLDelegatingVisitor
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
name|EJBQLExpressionVisitor
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
name|EntityResolver
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
name|ArcProperty
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
name|Property
import|;
end_import

begin_comment
comment|/**  * Produces an {@link EJBQLCompiledExpression} out of an EJBQL expression tree.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|Compiler
block|{
specifier|private
name|String
name|rootId
decl_stmt|;
specifier|private
name|EntityResolver
name|resolver
decl_stmt|;
specifier|private
name|Map
name|descriptorsById
decl_stmt|;
specifier|private
name|Map
name|incomingById
decl_stmt|;
specifier|private
name|EJBQLExpressionVisitor
name|fromItemVisitor
decl_stmt|;
specifier|private
name|EJBQLExpressionVisitor
name|joinVisitor
decl_stmt|;
specifier|private
name|EJBQLExpressionVisitor
name|whereClauseVisitor
decl_stmt|;
specifier|private
name|EJBQLExpressionVisitor
name|rootDescriptorVisitor
decl_stmt|;
name|Compiler
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
name|this
operator|.
name|descriptorsById
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|incomingById
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|rootDescriptorVisitor
operator|=
operator|new
name|SelectExpressionVisitor
argument_list|()
expr_stmt|;
name|this
operator|.
name|fromItemVisitor
operator|=
operator|new
name|FromItemVisitor
argument_list|()
expr_stmt|;
name|this
operator|.
name|joinVisitor
operator|=
operator|new
name|JoinVisitor
argument_list|()
expr_stmt|;
name|this
operator|.
name|whereClauseVisitor
operator|=
operator|new
name|WhereClauseVisitor
argument_list|()
expr_stmt|;
block|}
name|CompiledExpression
name|compile
parameter_list|(
name|String
name|source
parameter_list|,
name|EJBQLExpression
name|parsed
parameter_list|)
block|{
name|parsed
operator|.
name|visit
argument_list|(
operator|new
name|CompilationVisitor
argument_list|()
argument_list|)
expr_stmt|;
name|CompiledExpression
name|compiled
init|=
operator|new
name|CompiledExpression
argument_list|()
decl_stmt|;
name|compiled
operator|.
name|setExpression
argument_list|(
name|parsed
argument_list|)
expr_stmt|;
name|compiled
operator|.
name|setSource
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|compiled
operator|.
name|setRootId
argument_list|(
name|rootId
argument_list|)
expr_stmt|;
name|compiled
operator|.
name|setDescriptorsById
argument_list|(
name|descriptorsById
argument_list|)
expr_stmt|;
name|compiled
operator|.
name|setIncomingById
argument_list|(
name|incomingById
argument_list|)
expr_stmt|;
return|return
name|compiled
return|;
block|}
specifier|static
name|String
name|normalizeIdPath
parameter_list|(
name|String
name|idPath
parameter_list|)
block|{
comment|// per JPA spec, 4.4.2, "Identification variables are case insensitive."
name|int
name|pathSeparator
init|=
name|idPath
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
return|return
name|pathSeparator
operator|<
literal|0
condition|?
name|idPath
operator|.
name|toLowerCase
argument_list|()
else|:
name|idPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pathSeparator
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|+
name|idPath
operator|.
name|substring
argument_list|(
name|pathSeparator
argument_list|)
return|;
block|}
class|class
name|CompilationVisitor
extends|extends
name|EJBQLDelegatingVisitor
block|{
name|CompilationVisitor
parameter_list|()
block|{
name|super
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|visitSelectExpression
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|updateSubtreeDelegate
argument_list|(
name|rootDescriptorVisitor
argument_list|,
name|expression
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitFromItem
parameter_list|(
name|EJBQLFromItem
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
name|updateSubtreeDelegate
argument_list|(
name|fromItemVisitor
argument_list|,
name|expression
argument_list|,
name|finishedChildIndex
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitInnerFetchJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
name|updateSubtreeDelegate
argument_list|(
name|joinVisitor
argument_list|,
name|join
argument_list|,
name|finishedChildIndex
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitInnerJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
name|updateSubtreeDelegate
argument_list|(
name|joinVisitor
argument_list|,
name|join
argument_list|,
name|finishedChildIndex
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitOuterFetchJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
name|updateSubtreeDelegate
argument_list|(
name|joinVisitor
argument_list|,
name|join
argument_list|,
name|finishedChildIndex
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitOuterJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
name|updateSubtreeDelegate
argument_list|(
name|joinVisitor
argument_list|,
name|join
argument_list|,
name|finishedChildIndex
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitWhere
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
name|updateSubtreeDelegate
argument_list|(
name|whereClauseVisitor
argument_list|,
name|expression
argument_list|,
name|finishedChildIndex
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|updateSubtreeDelegate
parameter_list|(
name|EJBQLExpressionVisitor
name|delegate
parameter_list|,
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
operator|<
literal|0
condition|)
block|{
name|setDelegate
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|finishedChildIndex
operator|+
literal|1
operator|==
name|expression
operator|.
name|getChildrenCount
argument_list|()
condition|)
block|{
name|setDelegate
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
class|class
name|FromItemVisitor
extends|extends
name|EJBQLBaseVisitor
block|{
specifier|private
name|String
name|entityName
decl_stmt|;
specifier|public
name|boolean
name|visitIdentificationVariable
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|entityName
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
specifier|public
name|boolean
name|visitIdentifier
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
comment|// per JPA spec, 4.4.2, "Identification variables are case insensitive."
name|rootId
operator|=
name|normalizeIdPath
argument_list|(
name|expression
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
comment|// resolve class descriptor
name|ClassDescriptor
name|descriptor
init|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
name|entityName
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
literal|"Unmapped abstract schema name: "
operator|+
name|entityName
argument_list|)
throw|;
block|}
name|ClassDescriptor
name|old
init|=
operator|(
name|ClassDescriptor
operator|)
name|descriptorsById
operator|.
name|put
argument_list|(
name|rootId
argument_list|,
name|descriptor
argument_list|)
decl_stmt|;
if|if
condition|(
name|old
operator|!=
literal|null
operator|&&
name|old
operator|!=
name|descriptor
condition|)
block|{
throw|throw
operator|new
name|EJBQLException
argument_list|(
literal|"Duplicate identification variable definition: "
operator|+
name|rootId
operator|+
literal|", it is already used for "
operator|+
name|old
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
block|}
class|class
name|JoinVisitor
extends|extends
name|EJBQLBaseVisitor
block|{
specifier|private
name|String
name|id
decl_stmt|;
specifier|private
name|ObjRelationship
name|incoming
decl_stmt|;
specifier|private
name|ClassDescriptor
name|descriptor
decl_stmt|;
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
operator|+
literal|1
operator|<
name|expression
operator|.
name|getChildrenCount
argument_list|()
condition|)
block|{
name|this
operator|.
name|id
operator|=
name|expression
operator|.
name|getId
argument_list|()
expr_stmt|;
name|this
operator|.
name|descriptor
operator|=
operator|(
name|ClassDescriptor
operator|)
name|descriptorsById
operator|.
name|get
argument_list|(
name|id
argument_list|)
expr_stmt|;
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
literal|"Unmapped id variable: "
operator|+
name|id
argument_list|)
throw|;
block|}
block|}
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
name|Property
name|property
init|=
name|descriptor
operator|.
name|getProperty
argument_list|(
name|expression
operator|.
name|getText
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|property
operator|instanceof
name|ArcProperty
condition|)
block|{
name|incoming
operator|=
operator|(
operator|(
name|ArcProperty
operator|)
name|property
operator|)
operator|.
name|getRelationship
argument_list|()
expr_stmt|;
name|descriptor
operator|=
operator|(
operator|(
name|ArcProperty
operator|)
name|property
operator|)
operator|.
name|getTargetDescriptor
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|EJBQLException
argument_list|(
literal|"Incorrect relationship path: "
operator|+
name|expression
operator|.
name|getText
argument_list|()
argument_list|)
throw|;
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
if|if
condition|(
name|incoming
operator|!=
literal|null
condition|)
block|{
name|String
name|aliasId
init|=
name|expression
operator|.
name|getText
argument_list|()
decl_stmt|;
comment|// map id variable to class descriptor
name|ClassDescriptor
name|old
init|=
operator|(
name|ClassDescriptor
operator|)
name|descriptorsById
operator|.
name|put
argument_list|(
name|aliasId
argument_list|,
name|descriptor
argument_list|)
decl_stmt|;
if|if
condition|(
name|old
operator|!=
literal|null
operator|&&
name|old
operator|!=
name|descriptor
condition|)
block|{
throw|throw
operator|new
name|EJBQLException
argument_list|(
literal|"Duplicate identification variable definition: "
operator|+
name|aliasId
operator|+
literal|", it is already used for "
operator|+
name|old
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|incomingById
operator|.
name|put
argument_list|(
name|aliasId
argument_list|,
name|incoming
argument_list|)
expr_stmt|;
name|id
operator|=
literal|null
expr_stmt|;
name|descriptor
operator|=
literal|null
expr_stmt|;
name|incoming
operator|=
literal|null
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
class|class
name|WhereClauseVisitor
extends|extends
name|EJBQLBaseVisitor
block|{
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
operator|<
literal|0
condition|)
block|{
name|String
name|id
init|=
name|normalizeIdPath
argument_list|(
name|expression
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|ClassDescriptor
name|descriptor
init|=
operator|(
name|ClassDescriptor
operator|)
name|descriptorsById
operator|.
name|get
argument_list|(
name|id
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
literal|"Unmapped id variable: "
operator|+
name|id
argument_list|)
throw|;
block|}
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|(
name|id
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|expression
operator|.
name|getChildrenCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|pathChunk
init|=
name|expression
operator|.
name|getChild
argument_list|(
name|i
argument_list|)
operator|.
name|getText
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|pathChunk
argument_list|)
expr_stmt|;
name|Property
name|property
init|=
name|descriptor
operator|.
name|getProperty
argument_list|(
name|pathChunk
argument_list|)
decl_stmt|;
if|if
condition|(
name|property
operator|instanceof
name|ArcProperty
condition|)
block|{
name|ObjRelationship
name|incoming
init|=
operator|(
operator|(
name|ArcProperty
operator|)
name|property
operator|)
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
name|descriptor
operator|=
operator|(
operator|(
name|ArcProperty
operator|)
name|property
operator|)
operator|.
name|getTargetDescriptor
argument_list|()
expr_stmt|;
name|String
name|path
init|=
name|buffer
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|buffer
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|descriptorsById
operator|.
name|put
argument_list|(
name|path
argument_list|,
name|descriptor
argument_list|)
expr_stmt|;
name|incomingById
operator|.
name|put
argument_list|(
name|path
argument_list|,
name|incoming
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
class|class
name|SelectExpressionVisitor
extends|extends
name|EJBQLBaseVisitor
block|{
specifier|public
name|boolean
name|visitIdentifier
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|rootId
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
block|}
block|}
end_class

end_unit

