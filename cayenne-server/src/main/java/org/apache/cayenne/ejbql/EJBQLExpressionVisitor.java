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
comment|/**  * A visitor interface to inspect the EJBQL expression tree. Visit methods return  * booleans, indicating whether the children of a given node should be visited.  *   * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|EJBQLExpressionVisitor
block|{
name|boolean
name|visitAbs
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitAbstractSchemaName
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
comment|/**      * Called on visiting "add" expression and also after visiting every expression child.      *       * @param expression an "add" node being visited.      * @param finishedChildIndex "-1" when the expression node is visited for the first      *            time, before its children; otherwise this is an index of a child just      *            visited.      */
name|boolean
name|visitAdd
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitAggregate
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitAll
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
comment|/**      * Called on visiting "and" expression and also after visiting every expression child.      *       * @param expression an "and" node being visited.      * @param finishedChildIndex "-1" when the expression node is visited for the first      *            time, before its children; otherwise this is an index of a child just      *            visited.      */
name|boolean
name|visitAnd
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitAny
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitAscending
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitAverage
parameter_list|(
name|EJBQLAggregateColumn
name|expression
parameter_list|)
function_decl|;
comment|/**      * Called on visiting "between" expression and also after visiting every expression      * child.      *       * @param expression an "between" node being visited.      * @param finishedChildIndex "-1" when the expression node is visited for the first      *            time, before its children; otherwise this is an index of a child just      *            visited.      */
name|boolean
name|visitBetween
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitBooleanLiteral
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitClassName
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitConcat
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitConstructor
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitConstructorParameter
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitConstructorParameters
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitCount
parameter_list|(
name|EJBQLAggregateColumn
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitCurrentDate
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitCurrentTime
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitCurrentTimestamp
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitDecimalLiteral
parameter_list|(
name|EJBQLDecimalLiteral
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitDelete
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitDescending
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitDistinct
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
comment|/**      * Called on visiting "divide" expression and also after visiting every expression      * child.      *       * @param expression an "divide" node being visited.      * @param finishedChildIndex "-1" when the expression node is visited for the first      *            time, before its children; otherwise this is an index of a child just      *            visited.      */
name|boolean
name|visitDivide
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
comment|/**      * Called on visiting "equals" expression and also after visiting every expression      * child.      *       * @param expression an "equals" node being visited.      * @param finishedChildIndex "-1" when the expression node is visited for the first      *            time, before its children; otherwise this is an index of a child just      *            visited.      */
name|boolean
name|visitEquals
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitEscapeCharacter
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitExists
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitFrom
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitFromItem
parameter_list|(
name|EJBQLFromItem
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
comment|/**      * Called on visiting "&gt;=" expression and also after visiting every expression child.      *       * @param expression an "&gt;=" node being visited.      * @param finishedChildIndex "-1" when the expression node is visited for the first      *            time, before its children; otherwise this is an index of a child just      *            visited.      */
name|boolean
name|visitGreaterOrEqual
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
comment|/**      * Called on visiting "&gt;=" expression and also after visiting every expression child.      *       * @param expression an "&gt;=" node being visited.      * @param finishedChildIndex "-1" when the expression node is visited for the first      *            time, before its children; otherwise this is an index of a child just      *            visited.      */
name|boolean
name|visitGreaterThan
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitGroupBy
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitHaving
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitIdentificationVariable
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitIdentifier
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitIn
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitInnerFetchJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
function_decl|;
name|boolean
name|visitInnerJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
function_decl|;
name|boolean
name|visitIntegerLiteral
parameter_list|(
name|EJBQLIntegerLiteral
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitIsEmpty
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitIsNull
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitLength
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
comment|/**      * Called on visiting "&lt;=" expression and also after visiting every expression child.      *       * @param expression an "&lt;=" node being visited.      * @param finishedChildIndex "-1" when the expression node is visited for the first      *            time, before its children; otherwise this is an index of a child just      *            visited.      */
name|boolean
name|visitLessOrEqual
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
comment|/**      * Called on visiting "&lt;" expression and also after visiting every expression child.      *       * @param expression an "&lt;" node being visited.      * @param finishedChildIndex "-1" when the expression node is visited for the first      *            time, before its children; otherwise this is an index of a child just      *            visited.      */
name|boolean
name|visitLessThan
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
comment|/**      * Called on visiting "LIKE" expression and also after visiting every expression      * child.      *       * @param expression an "LIKE" node being visited.      * @param finishedChildIndex "-1" when the expression node is visited for the first      *            time, before its children; otherwise this is an index of a child just      *            visited.      */
name|boolean
name|visitLike
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitLocate
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitLower
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitMax
parameter_list|(
name|EJBQLAggregateColumn
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitMemberOf
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitMin
parameter_list|(
name|EJBQLAggregateColumn
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitMod
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
comment|/**      * Called on visiting "*" expression and also after visiting every expression child.      *       * @param expression an "*" node being visited.      * @param finishedChildIndex "-1" when the expression node is visited for the first      *            time, before its children; otherwise this is an index of a child just      *            visited.      */
name|boolean
name|visitMultiply
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitNamedInputParameter
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitNegative
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitNot
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
comment|/**      * Called on visiting "!=" expression and also after visiting every expression child.      *       * @param expression an "!=" node being visited.      * @param finishedChildIndex "-1" when the expression node is visited for the first      *            time, before its children; otherwise this is an index of a child just      *            visited.      */
name|boolean
name|visitNotEquals
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
comment|/**      * Called on visiting "or" expression and also after visiting every expression child.      *       * @param expression an "or" node being visited.      * @param finishedChildIndex "-1" when the expression node is visited for the first      *            time, before its children; otherwise this is an index of a child just      *            visited.      */
name|boolean
name|visitOr
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitOrderBy
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitOrderByItem
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitOuterFetchJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
function_decl|;
name|boolean
name|visitOuterJoin
parameter_list|(
name|EJBQLJoin
name|join
parameter_list|)
function_decl|;
comment|/**      * Called on visiting "path" expression and also after visiting every expression      * child.      *       * @param expression a "path" node being visited.      * @param finishedChildIndex "-1" when the expression node is visited for the first      *            time, before its children; otherwise this is an index of a child just      *            visited.      */
name|boolean
name|visitPath
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitDbPath
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitPatternValue
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitPositionalInputParameter
parameter_list|(
name|EJBQLPositionalInputParameter
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitSelect
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitSelectClause
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitSelectExpression
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitSelectExpressions
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitSize
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitSqrt
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitStringLiteral
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitSubselect
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitSubstring
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
comment|/**      * Called on visiting "subtract" expression and also after visiting every expression      * child.      *       * @param expression an "subtract" node being visited.      * @param finishedChildIndex "-1" when the expression node is visited for the first      *            time, before its children; otherwise this is an index of a child just      *            visited.      */
name|boolean
name|visitSubtract
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitSum
parameter_list|(
name|EJBQLAggregateColumn
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitTok
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitTrim
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitTrimBoth
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitTrimCharacter
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitTrimLeading
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitTrimTrailing
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitUpdate
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitUpdateField
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitUpdateItem
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitUpdateValue
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
name|boolean
name|visitUpper
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|,
name|int
name|finishedChildIndex
parameter_list|)
function_decl|;
name|boolean
name|visitWhere
parameter_list|(
name|EJBQLExpression
name|expression
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

