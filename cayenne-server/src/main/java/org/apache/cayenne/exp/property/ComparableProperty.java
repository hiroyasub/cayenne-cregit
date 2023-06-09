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
name|exp
operator|.
name|property
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
name|CayenneRuntimeException
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
name|ExpressionFactory
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
name|FunctionExpressionFactory
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
name|ColumnSelect
import|;
end_import

begin_comment
comment|/**  * Interface (or "Trait") that provides basic functionality for comparable properties.  *  * @see org.apache.cayenne.exp.property  * @since 4.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|ComparableProperty
parameter_list|<
name|E
parameter_list|>
extends|extends
name|Property
argument_list|<
name|E
argument_list|>
block|{
comment|/**      * @param lower The lower bound.      * @param upper The upper bound.      * @return An expression checking for objects between a lower and upper bound inclusive      */
specifier|default
name|Expression
name|between
parameter_list|(
name|E
name|lower
parameter_list|,
name|E
name|upper
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|betweenExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|lower
argument_list|,
name|upper
argument_list|)
return|;
block|}
comment|/**      * @param lower The lower bound.      * @param upper The upper bound.      * @return An expression checking for objects between a lower and upper bound inclusive      */
specifier|default
name|Expression
name|between
parameter_list|(
name|ComparableProperty
argument_list|<
name|?
argument_list|>
name|lower
parameter_list|,
name|ComparableProperty
argument_list|<
name|?
argument_list|>
name|upper
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|betweenExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|lower
operator|.
name|getExpression
argument_list|()
argument_list|,
name|upper
operator|.
name|getExpression
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return A greater than Expression.      */
specifier|default
name|Expression
name|gt
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|greaterExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * @return Represents a greater than relationship between two attributes      * (columns).      */
specifier|default
name|Expression
name|gt
parameter_list|(
name|ComparableProperty
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|greaterExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
operator|.
name|getExpression
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return A greater than or equal to Expression.      */
specifier|default
name|Expression
name|gte
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|greaterOrEqualExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * @return Represents a greater than or equal relationship between two      * attributes (columns).      */
specifier|default
name|Expression
name|gte
parameter_list|(
name|ComparableProperty
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|greaterOrEqualExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
operator|.
name|getExpression
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return A less than Expression.      */
specifier|default
name|Expression
name|lt
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|lessExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * @return Represents a less than relationship between two attributes      * (columns).      */
specifier|default
name|Expression
name|lt
parameter_list|(
name|ComparableProperty
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|lessExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
operator|.
name|getExpression
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return A less than or equal to Expression.      */
specifier|default
name|Expression
name|lte
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|lessOrEqualExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * @return Represents a less than or equal relationship between two      * attributes (columns).      */
specifier|default
name|Expression
name|lte
parameter_list|(
name|ComparableProperty
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|lessOrEqualExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
operator|.
name|getExpression
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @see FunctionExpressionFactory#maxExp(Expression)      */
specifier|default
name|BaseProperty
argument_list|<
name|E
argument_list|>
name|max
parameter_list|()
block|{
return|return
name|PropertyFactory
operator|.
name|createBase
argument_list|(
name|FunctionExpressionFactory
operator|.
name|maxExp
argument_list|(
name|getExpression
argument_list|()
argument_list|)
argument_list|,
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @see FunctionExpressionFactory#minExp(Expression)      */
specifier|default
name|BaseProperty
argument_list|<
name|E
argument_list|>
name|min
parameter_list|()
block|{
return|return
name|PropertyFactory
operator|.
name|createBase
argument_list|(
name|FunctionExpressionFactory
operator|.
name|minExp
argument_list|(
name|getExpression
argument_list|()
argument_list|)
argument_list|,
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @param subquery to use, must be a single column query.      * @return {@link Expression} that translates to a "&lt; ALL (subquery)" SQL      * @since 5.0      */
specifier|default
name|Expression
name|ltAll
parameter_list|(
name|ColumnSelect
argument_list|<
name|E
argument_list|>
name|subquery
parameter_list|)
block|{
name|assertSubqueryIsValidForComparison
argument_list|(
name|subquery
argument_list|)
expr_stmt|;
return|return
name|ExpressionFactory
operator|.
name|lessExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|ExpressionFactory
operator|.
name|all
argument_list|(
name|subquery
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param subquery to use, must be a single column query.      * @return {@link Expression} that translates to a "&lt;= ALL (subquery)" SQL      * @since 5.0      */
specifier|default
name|Expression
name|lteAll
parameter_list|(
name|ColumnSelect
argument_list|<
name|E
argument_list|>
name|subquery
parameter_list|)
block|{
name|assertSubqueryIsValidForComparison
argument_list|(
name|subquery
argument_list|)
expr_stmt|;
return|return
name|ExpressionFactory
operator|.
name|lessOrEqualExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|ExpressionFactory
operator|.
name|all
argument_list|(
name|subquery
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param subquery to use, must be a single column query.      * @return {@link Expression} that translates to a "&gt; ALL (subquery)" SQL      * @since 5.0      */
specifier|default
name|Expression
name|gtAll
parameter_list|(
name|ColumnSelect
argument_list|<
name|E
argument_list|>
name|subquery
parameter_list|)
block|{
name|assertSubqueryIsValidForComparison
argument_list|(
name|subquery
argument_list|)
expr_stmt|;
return|return
name|ExpressionFactory
operator|.
name|greaterExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|ExpressionFactory
operator|.
name|all
argument_list|(
name|subquery
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param subquery to use, must be a single column query.      * @return {@link Expression} that translates to a "&gt;= ALL (subquery)" SQL      * @since 5.0      */
specifier|default
name|Expression
name|gteAll
parameter_list|(
name|ColumnSelect
argument_list|<
name|E
argument_list|>
name|subquery
parameter_list|)
block|{
name|assertSubqueryIsValidForComparison
argument_list|(
name|subquery
argument_list|)
expr_stmt|;
return|return
name|ExpressionFactory
operator|.
name|greaterOrEqualExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|ExpressionFactory
operator|.
name|all
argument_list|(
name|subquery
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param subquery to use, must be a single column query.      * @return {@link Expression} that translates to a "&lt; ANY (subquery)" SQL      * @since 5.0      */
specifier|default
name|Expression
name|ltAny
parameter_list|(
name|ColumnSelect
argument_list|<
name|E
argument_list|>
name|subquery
parameter_list|)
block|{
name|assertSubqueryIsValidForComparison
argument_list|(
name|subquery
argument_list|)
expr_stmt|;
return|return
name|ExpressionFactory
operator|.
name|lessExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|ExpressionFactory
operator|.
name|any
argument_list|(
name|subquery
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param subquery to use, must be a single column query.      * @return {@link Expression} that translates to a "&lt;= ANY (subquery)" SQL      * @since 5.0      */
specifier|default
name|Expression
name|lteAny
parameter_list|(
name|ColumnSelect
argument_list|<
name|E
argument_list|>
name|subquery
parameter_list|)
block|{
name|assertSubqueryIsValidForComparison
argument_list|(
name|subquery
argument_list|)
expr_stmt|;
return|return
name|ExpressionFactory
operator|.
name|lessOrEqualExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|ExpressionFactory
operator|.
name|any
argument_list|(
name|subquery
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param subquery to use, must be a single column query.      * @return {@link Expression} that translates to a "&gt; ANY (subquery)" SQL      * @since 5.0      */
specifier|default
name|Expression
name|gtAny
parameter_list|(
name|ColumnSelect
argument_list|<
name|E
argument_list|>
name|subquery
parameter_list|)
block|{
name|assertSubqueryIsValidForComparison
argument_list|(
name|subquery
argument_list|)
expr_stmt|;
return|return
name|ExpressionFactory
operator|.
name|greaterExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|ExpressionFactory
operator|.
name|any
argument_list|(
name|subquery
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param subquery to use, must be a single column query.      * @return {@link Expression} that translates to a "&gt;= ANY (subquery)" SQL      * @since 5.0      */
specifier|default
name|Expression
name|gteAny
parameter_list|(
name|ColumnSelect
argument_list|<
name|E
argument_list|>
name|subquery
parameter_list|)
block|{
name|assertSubqueryIsValidForComparison
argument_list|(
name|subquery
argument_list|)
expr_stmt|;
return|return
name|ExpressionFactory
operator|.
name|greaterOrEqualExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|ExpressionFactory
operator|.
name|any
argument_list|(
name|subquery
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|void
name|assertSubqueryIsValidForComparison
parameter_list|(
name|ColumnSelect
argument_list|<
name|E
argument_list|>
name|subquery
parameter_list|)
block|{
if|if
condition|(
name|subquery
operator|.
name|getColumns
argument_list|()
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Only single-column query could be used in the comparison."
argument_list|)
throw|;
block|}
block|}
block|}
end_interface

end_unit

