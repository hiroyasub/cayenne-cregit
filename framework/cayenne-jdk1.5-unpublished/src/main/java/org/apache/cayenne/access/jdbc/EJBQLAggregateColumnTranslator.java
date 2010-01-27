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
name|ejbql
operator|.
name|parser
operator|.
name|EJBQLAggregateColumn
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

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
class|class
name|EJBQLAggregateColumnTranslator
extends|extends
name|EJBQLBaseVisitor
block|{
specifier|private
name|EJBQLTranslationContext
name|context
decl_stmt|;
specifier|private
name|String
name|attributeType
decl_stmt|;
name|EJBQLAggregateColumnTranslator
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
annotation|@
name|Override
specifier|public
name|boolean
name|visitCount
parameter_list|(
name|EJBQLAggregateColumn
name|expression
parameter_list|)
block|{
name|visitAggregateColumn
argument_list|(
name|expression
argument_list|,
operator|new
name|CountColumnVisitor
argument_list|()
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
name|visitAverage
parameter_list|(
name|EJBQLAggregateColumn
name|expression
parameter_list|)
block|{
name|visitAggregateColumn
argument_list|(
name|expression
argument_list|,
operator|new
name|FieldPathTranslator
argument_list|()
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
name|visitMax
parameter_list|(
name|EJBQLAggregateColumn
name|expression
parameter_list|)
block|{
name|visitAggregateColumn
argument_list|(
name|expression
argument_list|,
operator|new
name|FieldPathTranslator
argument_list|()
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
name|visitMin
parameter_list|(
name|EJBQLAggregateColumn
name|expression
parameter_list|)
block|{
name|visitAggregateColumn
argument_list|(
name|expression
argument_list|,
operator|new
name|FieldPathTranslator
argument_list|()
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
name|visitSum
parameter_list|(
name|EJBQLAggregateColumn
name|expression
parameter_list|)
block|{
name|visitAggregateColumn
argument_list|(
name|expression
argument_list|,
operator|new
name|FieldPathTranslator
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
specifier|private
name|void
name|visitAggregateColumn
parameter_list|(
name|EJBQLAggregateColumn
name|column
parameter_list|,
name|EJBQLExpressionVisitor
name|pathVisitor
parameter_list|)
block|{
if|if
condition|(
name|context
operator|.
name|isAppendingResultColumns
argument_list|()
condition|)
block|{
name|context
operator|.
name|append
argument_list|(
literal|" #result('"
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
expr_stmt|;
block|}
name|context
operator|.
name|append
argument_list|(
name|column
operator|.
name|getFunction
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
expr_stmt|;
comment|// path visitor must set attributeType ivar
name|column
operator|.
name|visit
argument_list|(
name|pathVisitor
argument_list|)
expr_stmt|;
name|context
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
if|if
condition|(
name|context
operator|.
name|isAppendingResultColumns
argument_list|()
condition|)
block|{
name|context
operator|.
name|append
argument_list|(
literal|"' '"
argument_list|)
operator|.
name|append
argument_list|(
name|column
operator|.
name|getJavaType
argument_list|(
name|attributeType
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"' '"
argument_list|)
operator|.
name|append
argument_list|(
name|context
operator|.
name|nextColumnAlias
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"')"
argument_list|)
expr_stmt|;
block|}
block|}
class|class
name|FieldPathTranslator
extends|extends
name|EJBQLPathTranslator
block|{
name|FieldPathTranslator
parameter_list|()
block|{
name|super
argument_list|(
name|EJBQLAggregateColumnTranslator
operator|.
name|this
operator|.
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitDistinct
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|context
operator|.
name|append
argument_list|(
literal|"DISTINCT "
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|appendMultiColumnPath
parameter_list|(
name|EJBQLMultiColumnOperand
name|operand
parameter_list|)
block|{
throw|throw
operator|new
name|EJBQLException
argument_list|(
literal|"Can't use multi-column paths in column clause"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|processTerminatingAttribute
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
block|{
name|EJBQLAggregateColumnTranslator
operator|.
name|this
operator|.
name|attributeType
operator|=
name|attribute
operator|.
name|getType
argument_list|()
expr_stmt|;
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
annotation|@
name|Override
specifier|protected
name|void
name|processTerminatingRelationship
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|dbAttr
init|=
operator|(
operator|(
name|ObjEntity
operator|)
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|)
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbAttr
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|this
operator|.
name|resolveJoin
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|append
argument_list|(
literal|'*'
argument_list|)
expr_stmt|;
block|}
block|}
class|class
name|CountColumnVisitor
extends|extends
name|EJBQLBaseVisitor
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|visitDistinct
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
name|context
operator|.
name|append
argument_list|(
literal|"DISTINCT "
argument_list|)
expr_stmt|;
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
name|context
operator|.
name|append
argument_list|(
literal|'*'
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
name|visitPath
parameter_list|(
name|EJBQLExpression
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
operator|new
name|FieldPathTranslator
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

