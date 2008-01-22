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
name|ArrayList
import|;
end_import

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
name|query
operator|.
name|SQLResultSetMapping
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
comment|// a flag indicating whether column expressions should be treated as result columns or
comment|// not.
specifier|private
name|boolean
name|appendingResultColumns
decl_stmt|;
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
argument_list|<
name|String
argument_list|,
name|ClassDescriptor
argument_list|>
name|descriptorsById
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ObjRelationship
argument_list|>
name|incomingById
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|EJBQLPath
argument_list|>
name|paths
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
name|pathVisitor
decl_stmt|;
specifier|private
name|EJBQLExpressionVisitor
name|rootDescriptorVisitor
decl_stmt|;
specifier|private
name|SQLResultSetMapping
name|resultSetMapping
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
argument_list|<
name|String
argument_list|,
name|ClassDescriptor
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|incomingById
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|ObjRelationship
argument_list|>
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
name|pathVisitor
operator|=
operator|new
name|PathVisitor
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
comment|// postprocess paths, now that all id vars are resolved
if|if
condition|(
name|paths
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|EJBQLPath
name|path
range|:
name|paths
control|)
block|{
name|String
name|id
init|=
name|normalizeIdPath
argument_list|(
name|path
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|ClassDescriptor
name|descriptor
init|=
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
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
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
name|path
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
name|path
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
name|pathString
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
name|pathString
argument_list|,
name|descriptor
argument_list|)
expr_stmt|;
name|incomingById
operator|.
name|put
argument_list|(
name|pathString
argument_list|,
name|incoming
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
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
name|compiled
operator|.
name|setResultSetMapping
argument_list|(
name|resultSetMapping
argument_list|)
expr_stmt|;
return|return
name|compiled
return|;
block|}
specifier|private
name|void
name|addPath
parameter_list|(
name|EJBQLPath
name|path
parameter_list|)
block|{
if|if
condition|(
name|paths
operator|==
literal|null
condition|)
block|{
name|paths
operator|=
operator|new
name|ArrayList
argument_list|<
name|EJBQLPath
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|paths
operator|.
name|add
argument_list|(
name|path
argument_list|)
expr_stmt|;
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
name|EJBQLBaseVisitor
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|visitSelect
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|appendingResultColumns
operator|=
literal|true
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitFrom
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
name|appendingResultColumns
operator|=
literal|false
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitSelectExpression
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|expression
operator|.
name|visit
argument_list|(
name|rootDescriptorVisitor
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
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
name|expression
operator|.
name|visit
argument_list|(
name|fromItemVisitor
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitInnerFetchJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
block|{
name|join
operator|.
name|visit
argument_list|(
name|joinVisitor
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitInnerJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
block|{
name|join
operator|.
name|visit
argument_list|(
name|joinVisitor
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitOuterFetchJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
block|{
name|join
operator|.
name|visit
argument_list|(
name|joinVisitor
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitOuterJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
block|{
name|join
operator|.
name|visit
argument_list|(
name|joinVisitor
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
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
name|expression
operator|.
name|visit
argument_list|(
name|pathVisitor
argument_list|)
expr_stmt|;
comment|// continue with children as there may be subselects with their own id
comment|// variable declarations
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitOrderBy
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|expression
operator|.
name|visit
argument_list|(
name|pathVisitor
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitSubselect
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|super
operator|.
name|visitSubselect
argument_list|(
name|expression
argument_list|)
return|;
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
annotation|@
name|Override
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
if|if
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
comment|// per JPA spec, 4.4.2, "Identification variables are case insensitive."
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
name|old
init|=
name|descriptorsById
operator|.
name|put
argument_list|(
name|id
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
name|id
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
comment|// if root wasn't detected in the Select Clause, use the first id var as
comment|// root
if|if
condition|(
name|Compiler
operator|.
name|this
operator|.
name|rootId
operator|==
literal|null
condition|)
block|{
name|Compiler
operator|.
name|this
operator|.
name|rootId
operator|=
name|id
expr_stmt|;
block|}
name|this
operator|.
name|entityName
operator|=
literal|null
expr_stmt|;
block|}
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
operator|(
operator|(
name|EJBQLPath
operator|)
name|expression
operator|)
operator|.
name|getId
argument_list|()
expr_stmt|;
name|this
operator|.
name|descriptor
operator|=
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
name|PathVisitor
extends|extends
name|EJBQLBaseVisitor
block|{
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
name|addPath
argument_list|(
operator|(
name|EJBQLPath
operator|)
name|expression
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
class|class
name|SelectExpressionVisitor
extends|extends
name|EJBQLBaseVisitor
block|{
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
if|if
condition|(
name|appendingResultColumns
condition|)
block|{
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
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitAggregate
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|addResultSetColumn
argument_list|()
expr_stmt|;
return|return
literal|false
return|;
block|}
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
name|addPath
argument_list|(
operator|(
name|EJBQLPath
operator|)
name|expression
argument_list|)
expr_stmt|;
name|addResultSetColumn
argument_list|()
expr_stmt|;
return|return
literal|false
return|;
block|}
specifier|private
name|void
name|addResultSetColumn
parameter_list|()
block|{
if|if
condition|(
name|appendingResultColumns
condition|)
block|{
if|if
condition|(
name|resultSetMapping
operator|==
literal|null
condition|)
block|{
name|resultSetMapping
operator|=
operator|new
name|SQLResultSetMapping
argument_list|()
expr_stmt|;
block|}
name|String
name|column
init|=
literal|"sc"
operator|+
name|resultSetMapping
operator|.
name|getColumnResults
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|resultSetMapping
operator|.
name|addColumnResult
argument_list|(
name|column
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

