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
package|;
end_package

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
name|ejbql
operator|.
name|parser
operator|.
name|EJBQLDecimalLiteral
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
name|EJBQLFromItem
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
name|EJBQLIntegerLiteral
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
name|EJBQLJoin
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
name|EJBQLPositionalInputParameter
import|;
end_import

begin_comment
comment|/**  * A noop implementation of the EJBQL visitor that returns same preset boolean value from  * all methods. Intended for subclassing.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|EJBQLBaseVisitor
implements|implements
name|EJBQLExpressionVisitor
block|{
specifier|protected
name|boolean
name|continueFlag
decl_stmt|;
specifier|public
name|EJBQLBaseVisitor
parameter_list|()
block|{
name|this
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EJBQLBaseVisitor
parameter_list|(
name|boolean
name|continueFlag
parameter_list|)
block|{
name|this
operator|.
name|continueFlag
operator|=
name|continueFlag
expr_stmt|;
block|}
specifier|public
name|boolean
name|visitAbs
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitAbstractSchemaName
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitAdd
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitAggregate
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitAll
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitAnd
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitAny
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitAscending
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitAverage
parameter_list|(
name|EJBQLAggregateColumn
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitBetween
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitBooleanLiteral
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitClassName
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitConcat
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitConstructor
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitConstructorParameter
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitConstructorParameters
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitCount
parameter_list|(
name|EJBQLAggregateColumn
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitCurrentDate
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitCurrentTime
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitCurrentTimestamp
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitDecimalLiteral
parameter_list|(
name|EJBQLDecimalLiteral
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitDelete
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitDescending
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
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
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitDistinct
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitDivide
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitEquals
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitEscapeCharacter
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitExists
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
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
return|return
name|continueFlag
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
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitGreaterOrEqual
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitGreaterThan
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitGroupBy
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitHaving
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
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
return|return
name|continueFlag
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
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitIn
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitInnerFetchJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitInnerJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitIntegerLiteral
parameter_list|(
name|EJBQLIntegerLiteral
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitIsEmpty
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitIsNull
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitLength
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitLessOrEqual
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitLessThan
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitLike
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitLocate
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitLower
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitMax
parameter_list|(
name|EJBQLAggregateColumn
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitMemberOf
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitMin
parameter_list|(
name|EJBQLAggregateColumn
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitMod
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitMultiply
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitNamedInputParameter
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitNegative
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitNot
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitNotEquals
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitOr
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitOrderBy
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitOrderByItem
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitOuterFetchJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitOuterJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
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
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitPatternValue
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitPositionalInputParameter
parameter_list|(
name|EJBQLPositionalInputParameter
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitSelect
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitSelectClause
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitSelectExpression
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitSelectExpressions
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitSize
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitSqrt
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitStringLiteral
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitSubselect
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitSubstring
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitSubtract
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitSum
parameter_list|(
name|EJBQLAggregateColumn
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitTok
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitTrim
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitTrimBoth
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitTrimCharacter
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitTrimLeading
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitTrimTrailing
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitUpdate
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitUpdateField
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitUpdateItem
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitUpdateValue
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitUpper
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
specifier|public
name|boolean
name|visitWhere
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
block|{
return|return
name|continueFlag
return|;
block|}
block|}
end_class

end_unit

