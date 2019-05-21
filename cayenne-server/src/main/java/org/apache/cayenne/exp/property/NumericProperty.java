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
name|exp
operator|.
name|parser
operator|.
name|ASTAdd
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
name|parser
operator|.
name|ASTDivide
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
name|parser
operator|.
name|ASTMultiply
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
name|parser
operator|.
name|ASTNegate
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
name|parser
operator|.
name|ASTSubtract
import|;
end_import

begin_comment
comment|/**  * Property that represents attributes mapped on numeric types  *<p>  * Numeric type is an any type inherited from {@link Number}.  *<p>  * Provides basic math functions like {@link #mod(Number)}, {@link #abs()} and {@link #sqrt()}.  * It is also implements {@link ComparableProperty} interface.  *  * @see org.apache.cayenne.exp.property  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|NumericProperty
parameter_list|<
name|E
extends|extends
name|Number
parameter_list|>
extends|extends
name|BaseProperty
argument_list|<
name|E
argument_list|>
implements|implements
name|ComparableProperty
argument_list|<
name|E
argument_list|>
block|{
comment|/**      * Constructs a new property with the given name and expression      *      * @param name       of the property (will be used as alias for the expression)      * @param expression expression for property      * @param type       of the property      * @see PropertyFactory#createNumeric(String, Expression, Class)      */
specifier|protected
name|NumericProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|E
argument_list|>
name|type
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|expression
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see FunctionExpressionFactory#avgExp(Expression)      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|avg
parameter_list|()
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
name|FunctionExpressionFactory
operator|.
name|avgExp
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
comment|/**      * @see FunctionExpressionFactory#sumExp(Expression)      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|sum
parameter_list|()
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
name|FunctionExpressionFactory
operator|.
name|sumExp
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
comment|/**      * {@inheritDoc}      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|max
parameter_list|()
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
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
comment|/**      * {@inheritDoc}      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|min
parameter_list|()
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
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
comment|/**      * @see FunctionExpressionFactory#modExp(Expression, Number)      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|mod
parameter_list|(
name|Number
name|number
parameter_list|)
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
name|FunctionExpressionFactory
operator|.
name|modExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|number
argument_list|)
argument_list|,
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @see FunctionExpressionFactory#modExp(Expression, Number)      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|mod
parameter_list|(
name|NumericProperty
argument_list|<
name|?
argument_list|>
name|number
parameter_list|)
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
name|FunctionExpressionFactory
operator|.
name|modExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|number
operator|.
name|getExpression
argument_list|()
argument_list|)
argument_list|,
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @see FunctionExpressionFactory#absExp(Expression)      *      * @return new property that represents abs() function call with current property as argument      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|abs
parameter_list|()
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
name|FunctionExpressionFactory
operator|.
name|absExp
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
comment|/**      * @see FunctionExpressionFactory#sqrtExp(Expression)      *      * @return new property that represents sqrt() function call with current property as argument      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|sqrt
parameter_list|()
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
name|FunctionExpressionFactory
operator|.
name|sqrtExp
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
comment|/**      * @return new property that represents '+' operator with current property as argument      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|add
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
operator|new
name|ASTAdd
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
argument_list|)
argument_list|,
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return new property that represents '+' operator with current property as argument      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|add
parameter_list|(
name|NumericProperty
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
operator|new
name|ASTAdd
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
operator|.
name|getExpression
argument_list|()
argument_list|)
argument_list|,
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return new property that represents '-' operator with current property as argument      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|sub
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
operator|new
name|ASTSubtract
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
argument_list|)
argument_list|,
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return new property that represents '-' operator with current property as argument      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|sub
parameter_list|(
name|NumericProperty
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
operator|new
name|ASTSubtract
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
operator|.
name|getExpression
argument_list|()
argument_list|)
argument_list|,
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return new property that represents '/' operator with current property as argument      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|div
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
operator|new
name|ASTDivide
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
argument_list|)
argument_list|,
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return new property that represents '/' operator with current property as argument      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|div
parameter_list|(
name|NumericProperty
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
operator|new
name|ASTDivide
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
operator|.
name|getExpression
argument_list|()
argument_list|)
argument_list|,
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return new property that represents '*' operator with current property as argument      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|mul
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
operator|new
name|ASTMultiply
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
argument_list|)
argument_list|,
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return new property that represents '*' operator with current property as argument      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|mul
parameter_list|(
name|NumericProperty
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
operator|new
name|ASTMultiply
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
operator|.
name|getExpression
argument_list|()
argument_list|)
argument_list|,
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return new property that represents negative value of current property      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|neg
parameter_list|()
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
operator|new
name|ASTNegate
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
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|alias
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
name|alias
argument_list|,
name|this
operator|.
name|getExpression
argument_list|()
argument_list|,
name|this
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return property that will be translated relative to parent query      */
specifier|public
name|NumericProperty
argument_list|<
name|E
argument_list|>
name|enclosing
parameter_list|()
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
name|ExpressionFactory
operator|.
name|enclosingObjectExp
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
block|}
end_class

end_unit

