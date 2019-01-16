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
name|exp
operator|.
name|property
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|exp
operator|.
name|parser
operator|.
name|ASTPath
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
name|Ordering
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
name|Orderings
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
name|SortOrder
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
name|PropertyUtils
import|;
end_import

begin_comment
comment|/**  * Property that represents generic attribute.  *<p>  * Provides equality checks and sorting API along with some utility methods.  *  * @see org.apache.cayenne.exp.property  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|BaseProperty
parameter_list|<
name|E
parameter_list|>
implements|implements
name|Property
argument_list|<
name|E
argument_list|>
block|{
comment|/**      * Name of the property in the object      */
specifier|protected
specifier|final
name|String
name|name
decl_stmt|;
comment|/**      * Expression provider for the property      */
specifier|protected
specifier|final
name|Supplier
argument_list|<
name|Expression
argument_list|>
name|expressionSupplier
decl_stmt|;
comment|/**      * Explicit type of the property      */
specifier|protected
specifier|final
name|Class
argument_list|<
name|E
argument_list|>
name|type
decl_stmt|;
comment|/**      * Constructs a new property with the given name and expression      *      * @param name of the property (will be used as alias for the expression)      * @param expression expression for property      * @param type of the property      *      * @see PropertyFactory#createBase(String, Expression, Class)      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|BaseProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|?
super|super
name|E
argument_list|>
name|type
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
if|if
condition|(
name|expression
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|expressionSupplier
operator|=
parameter_list|()
lambda|->
name|ExpressionFactory
operator|.
name|pathExp
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|expressionSupplier
operator|=
name|expression
operator|::
name|deepCopy
expr_stmt|;
block|}
name|this
operator|.
name|type
operator|=
operator|(
name|Class
argument_list|<
name|E
argument_list|>
operator|)
name|type
expr_stmt|;
block|}
comment|/**      * @return Name of the property in the object.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * @return alias for this property      */
specifier|public
name|String
name|getAlias
parameter_list|()
block|{
if|if
condition|(
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// check if default name for Path expression is overridden
name|Expression
name|exp
init|=
name|getExpression
argument_list|()
decl_stmt|;
if|if
condition|(
name|exp
operator|instanceof
name|ASTPath
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|ASTPath
operator|)
name|exp
operator|)
operator|.
name|getPath
argument_list|()
operator|.
name|equals
argument_list|(
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
return|return
name|getName
argument_list|()
return|;
block|}
comment|/**      * This method returns fresh copy of the expression for each call.      * @return expression that represents this Property      */
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
return|return
name|expressionSupplier
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|name
operator|!=
literal|null
condition|?
name|name
operator|.
name|hashCode
argument_list|()
else|:
name|expressionSupplier
operator|.
name|get
argument_list|()
operator|.
name|hashCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|type
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|BaseProperty
argument_list|<
name|?
argument_list|>
name|property
init|=
operator|(
name|BaseProperty
argument_list|<
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|?
operator|!
name|name
operator|.
name|equals
argument_list|(
name|property
operator|.
name|name
argument_list|)
else|:
name|property
operator|.
name|name
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
operator|&&
operator|!
name|expressionSupplier
operator|.
name|get
argument_list|()
operator|.
name|equals
argument_list|(
name|property
operator|.
name|expressionSupplier
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
operator|(
name|type
operator|==
literal|null
condition|?
name|property
operator|.
name|type
operator|==
literal|null
else|:
name|type
operator|.
name|equals
argument_list|(
name|property
operator|.
name|type
argument_list|)
operator|)
return|;
block|}
comment|/**      * Converts this property to a path expression.      * This method is equivalent of getExpression() which is preferred as more generic.      *      * @return a newly created expression.      * @see BaseProperty#getExpression()      * @deprecated since 4.2, use {@link #getExpression()} method instead      */
annotation|@
name|Deprecated
specifier|public
name|Expression
name|path
parameter_list|()
block|{
return|return
name|getExpression
argument_list|()
return|;
block|}
comment|/**      * @return An expression representing null.      */
specifier|public
name|Expression
name|isNull
parameter_list|()
block|{
return|return
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * @return An expression representing a non-null value.      */
specifier|public
name|Expression
name|isNotNull
parameter_list|()
block|{
return|return
name|ExpressionFactory
operator|.
name|noMatchExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * @return Ascending sort orderings on this property.      */
specifier|public
name|Ordering
name|asc
parameter_list|()
block|{
return|return
operator|new
name|Ordering
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|SortOrder
operator|.
name|ASCENDING
argument_list|)
return|;
block|}
comment|/**      * @return Ascending sort orderings on this property.      */
specifier|public
name|Orderings
name|ascs
parameter_list|()
block|{
return|return
operator|new
name|Orderings
argument_list|(
name|asc
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return Ascending case insensitive sort orderings on this property.      */
specifier|public
name|Ordering
name|ascInsensitive
parameter_list|()
block|{
return|return
operator|new
name|Ordering
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|SortOrder
operator|.
name|ASCENDING_INSENSITIVE
argument_list|)
return|;
block|}
comment|/**      * @return Ascending case insensitive sort orderings on this property.      */
specifier|public
name|Orderings
name|ascInsensitives
parameter_list|()
block|{
return|return
operator|new
name|Orderings
argument_list|(
name|ascInsensitive
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return Descending sort orderings on this property.      */
specifier|public
name|Ordering
name|desc
parameter_list|()
block|{
return|return
operator|new
name|Ordering
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
return|;
block|}
comment|/**      * @return Descending sort orderings on this property.      */
specifier|public
name|Orderings
name|descs
parameter_list|()
block|{
return|return
operator|new
name|Orderings
argument_list|(
name|desc
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @return Descending case insensitive sort orderings on this property.      */
specifier|public
name|Ordering
name|descInsensitive
parameter_list|()
block|{
return|return
operator|new
name|Ordering
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|SortOrder
operator|.
name|DESCENDING_INSENSITIVE
argument_list|)
return|;
block|}
comment|/**      * @return Descending case insensitive sort orderings on this property.      */
specifier|public
name|Orderings
name|descInsensitives
parameter_list|()
block|{
return|return
operator|new
name|Orderings
argument_list|(
name|descInsensitive
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Extracts property value from an object using JavaBean-compatible      * introspection with one addition - a property can be a dot-separated      * property name path.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|E
name|getFrom
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
return|return
operator|(
name|E
operator|)
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|bean
argument_list|,
name|getName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Extracts property value from a collection of objects using      * JavaBean-compatible introspection with one addition - a property can be a      * dot-separated property name path.      */
specifier|public
name|List
argument_list|<
name|E
argument_list|>
name|getFromAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|beans
parameter_list|)
block|{
name|List
argument_list|<
name|E
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|beans
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|bean
range|:
name|beans
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|getFrom
argument_list|(
name|bean
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Sets a property value in 'obj' using JavaBean-compatible introspection      * with one addition - a property can be a dot-separated property name path.      */
specifier|public
name|void
name|setIn
parameter_list|(
name|Object
name|bean
parameter_list|,
name|E
name|value
parameter_list|)
block|{
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|bean
argument_list|,
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets a property value in a collection of objects using      * JavaBean-compatible introspection with one addition - a property can be a      * dot-separated property name path.      */
specifier|public
name|void
name|setInAll
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|beans
parameter_list|,
name|E
name|value
parameter_list|)
block|{
for|for
control|(
name|Object
name|bean
range|:
name|beans
control|)
block|{
name|setIn
argument_list|(
name|bean
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @see FunctionExpressionFactory#countExp(Expression)      */
specifier|public
name|NumericProperty
argument_list|<
name|Long
argument_list|>
name|count
parameter_list|()
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
name|FunctionExpressionFactory
operator|.
name|countExp
argument_list|(
name|getExpression
argument_list|()
argument_list|)
argument_list|,
name|Long
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * @see FunctionExpressionFactory#countDistinctExp(Expression)      */
specifier|public
name|NumericProperty
argument_list|<
name|Long
argument_list|>
name|countDistinct
parameter_list|()
block|{
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
name|FunctionExpressionFactory
operator|.
name|countDistinctExp
argument_list|(
name|getExpression
argument_list|()
argument_list|)
argument_list|,
name|Long
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Creates alias with different name for this property      */
specifier|public
name|BaseProperty
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
name|createBase
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
comment|/**      * @return type of entity attribute described by this property      */
specifier|public
name|Class
argument_list|<
name|E
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * @return An expression representing equality to TRUE.      */
specifier|public
name|Expression
name|isTrue
parameter_list|()
block|{
return|return
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
return|;
block|}
comment|/**      * @return An expression representing equality to FALSE.      */
specifier|public
name|Expression
name|isFalse
parameter_list|()
block|{
return|return
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
return|;
block|}
comment|/**      * @return An expression representing equality to a value.      */
specifier|public
name|Expression
name|eq
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * @return An expression representing equality between two attributes      * (columns).      */
specifier|public
name|Expression
name|eq
parameter_list|(
name|BaseProperty
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|matchExp
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
comment|/**      * @return An expression representing inequality to a value.      */
specifier|public
name|Expression
name|ne
parameter_list|(
name|E
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|noMatchExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * @return An expression representing inequality between two attributes      * (columns).      */
specifier|public
name|Expression
name|ne
parameter_list|(
name|BaseProperty
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|noMatchExp
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
comment|/**      * @return An expression for finding objects with values in the given set.      */
specifier|public
name|Expression
name|in
parameter_list|(
name|E
name|firstValue
parameter_list|,
name|E
modifier|...
name|moreValues
parameter_list|)
block|{
name|int
name|moreValuesLength
init|=
name|moreValues
operator|!=
literal|null
condition|?
name|moreValues
operator|.
name|length
else|:
literal|0
decl_stmt|;
name|Object
index|[]
name|values
init|=
operator|new
name|Object
index|[
name|moreValuesLength
operator|+
literal|1
index|]
decl_stmt|;
name|values
index|[
literal|0
index|]
operator|=
name|firstValue
expr_stmt|;
if|if
condition|(
name|moreValuesLength
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|moreValues
argument_list|,
literal|0
argument_list|,
name|values
argument_list|,
literal|1
argument_list|,
name|moreValuesLength
argument_list|)
expr_stmt|;
block|}
return|return
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|values
argument_list|)
return|;
block|}
comment|/**      * @return An expression for finding objects with values not in the given      * set.      */
specifier|public
name|Expression
name|nin
parameter_list|(
name|E
name|firstValue
parameter_list|,
name|E
modifier|...
name|moreValues
parameter_list|)
block|{
name|int
name|moreValuesLength
init|=
name|moreValues
operator|!=
literal|null
condition|?
name|moreValues
operator|.
name|length
else|:
literal|0
decl_stmt|;
name|Object
index|[]
name|values
init|=
operator|new
name|Object
index|[
name|moreValuesLength
operator|+
literal|1
index|]
decl_stmt|;
name|values
index|[
literal|0
index|]
operator|=
name|firstValue
expr_stmt|;
if|if
condition|(
name|moreValuesLength
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|moreValues
argument_list|,
literal|0
argument_list|,
name|values
argument_list|,
literal|1
argument_list|,
name|moreValuesLength
argument_list|)
expr_stmt|;
block|}
return|return
name|ExpressionFactory
operator|.
name|notInExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|values
argument_list|)
return|;
block|}
comment|/**      * @return An expression for finding objects with values in the given set.      */
specifier|public
name|Expression
name|in
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|values
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|values
argument_list|)
return|;
block|}
comment|/**      * @return An expression for finding objects with values not in the given      * set.      */
specifier|public
name|Expression
name|nin
parameter_list|(
name|Collection
argument_list|<
name|E
argument_list|>
name|values
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|notInExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|values
argument_list|)
return|;
block|}
comment|/**      * @return An expression for finding objects with values in the given subquery      */
specifier|public
name|Expression
name|in
parameter_list|(
name|ColumnSelect
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|subquery
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|inExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|subquery
argument_list|)
return|;
block|}
comment|/**      * @return An expression for finding objects with values not in the given subquery      */
specifier|public
name|Expression
name|nin
parameter_list|(
name|ColumnSelect
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|subquery
parameter_list|)
block|{
return|return
name|ExpressionFactory
operator|.
name|notInExp
argument_list|(
name|getExpression
argument_list|()
argument_list|,
name|subquery
argument_list|)
return|;
block|}
comment|/**      * @return property that will be translated relative to parent query      */
specifier|public
name|BaseProperty
argument_list|<
name|E
argument_list|>
name|enclosing
parameter_list|()
block|{
return|return
name|PropertyFactory
operator|.
name|createBase
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
comment|/**      * @return An expression for calling functionName with first argument equals to<b>this</b> property      *      and provided additional arguments      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|BaseProperty
argument_list|<
name|T
argument_list|>
name|function
parameter_list|(
name|String
name|functionName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|returnType
parameter_list|,
name|BaseProperty
argument_list|<
name|?
argument_list|>
modifier|...
name|arguments
parameter_list|)
block|{
name|Object
index|[]
name|expressions
init|=
operator|new
name|Expression
index|[
name|arguments
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
name|expressions
index|[
literal|0
index|]
operator|=
name|getExpression
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|arguments
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|expressions
index|[
name|i
index|]
operator|=
name|arguments
index|[
name|i
index|]
operator|.
name|getExpression
argument_list|()
expr_stmt|;
block|}
return|return
name|PropertyFactory
operator|.
name|createBase
argument_list|(
name|FunctionExpressionFactory
operator|.
name|functionCall
argument_list|(
name|functionName
argument_list|,
name|expressions
argument_list|)
argument_list|,
name|returnType
argument_list|)
return|;
block|}
comment|/**      * @return An expression for calling functionName with first argument equals to<b>this</b> property      *      and provided additional arguments      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|BaseProperty
argument_list|<
name|T
argument_list|>
name|function
parameter_list|(
name|String
name|functionName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|returnType
parameter_list|,
name|Object
modifier|...
name|arguments
parameter_list|)
block|{
name|Object
index|[]
name|expressions
init|=
operator|new
name|Object
index|[
name|arguments
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
name|expressions
index|[
literal|0
index|]
operator|=
name|getExpression
argument_list|()
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|arguments
argument_list|,
literal|0
argument_list|,
name|expressions
argument_list|,
literal|1
argument_list|,
name|arguments
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|PropertyFactory
operator|.
name|createBase
argument_list|(
name|FunctionExpressionFactory
operator|.
name|functionCall
argument_list|(
name|functionName
argument_list|,
name|expressions
argument_list|)
argument_list|,
name|returnType
argument_list|)
return|;
block|}
block|}
end_class

end_unit

