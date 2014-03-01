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
name|parser
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
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
name|Cayenne
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
name|ObjectId
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
name|Persistent
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
name|util
operator|.
name|ConversionUtil
import|;
end_import

begin_comment
comment|/**  * Performs argument conversions for a calling binary expression, so that the  * expression could eval the arguments of the same type.  *   * @since 3.2  */
end_comment

begin_class
specifier|abstract
class|class
name|Evaluator
block|{
specifier|private
specifier|static
specifier|final
name|ConcurrentMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Evaluator
argument_list|>
name|evaluators
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Evaluator
name|NULL_LHS_EVALUATOR
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Evaluator
name|DEFAULT_EVALUATOR
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Evaluator
name|PERSISTENT_EVALUATOR
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Evaluator
name|BIG_DECIMAL_EVALUATOR
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Evaluator
name|COMPAREABLE_EVALUATOR
decl_stmt|;
comment|/**      * A decorator of an evaluator that presumes non-null 'lhs' argument and      * allows for null 'rhs'.      */
specifier|static
class|class
name|NonNullLhsEvaluator
extends|extends
name|Evaluator
block|{
specifier|final
name|Evaluator
name|delegate
decl_stmt|;
name|NonNullLhsEvaluator
parameter_list|(
name|Evaluator
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
annotation|@
name|Override
name|Integer
name|compare
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
block|{
if|if
condition|(
name|rhs
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
else|else
block|{
return|return
name|delegate
operator|.
name|compare
argument_list|(
name|lhs
argument_list|,
name|rhs
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
name|boolean
name|eq
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
block|{
return|return
name|rhs
operator|==
literal|null
condition|?
literal|false
else|:
name|delegate
operator|.
name|eq
argument_list|(
name|lhs
argument_list|,
name|rhs
argument_list|)
return|;
block|}
block|}
static|static
block|{
name|evaluators
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Evaluator
argument_list|>
argument_list|()
expr_stmt|;
name|NULL_LHS_EVALUATOR
operator|=
operator|new
name|Evaluator
argument_list|()
block|{
annotation|@
name|Override
name|Integer
name|compare
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
name|boolean
name|eq
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
block|{
return|return
name|rhs
operator|==
literal|null
return|;
block|}
block|}
expr_stmt|;
name|DEFAULT_EVALUATOR
operator|=
operator|new
name|NonNullLhsEvaluator
argument_list|(
operator|new
name|Evaluator
argument_list|()
block|{
annotation|@
name|Override
name|boolean
name|eq
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
block|{
return|return
name|lhs
operator|.
name|equals
argument_list|(
name|rhs
argument_list|)
return|;
block|}
annotation|@
name|Override
name|Integer
name|compare
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|PERSISTENT_EVALUATOR
operator|=
operator|new
name|NonNullLhsEvaluator
argument_list|(
operator|new
name|Evaluator
argument_list|()
block|{
annotation|@
name|Override
name|Integer
name|compare
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
name|boolean
name|eq
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
block|{
name|Persistent
name|lhsPersistent
init|=
operator|(
name|Persistent
operator|)
name|lhs
decl_stmt|;
if|if
condition|(
name|rhs
operator|instanceof
name|Persistent
condition|)
block|{
return|return
name|lhsPersistent
operator|.
name|getObjectId
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|Persistent
operator|)
name|rhs
operator|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|rhs
operator|instanceof
name|ObjectId
condition|)
block|{
return|return
name|lhsPersistent
operator|.
name|getObjectId
argument_list|()
operator|.
name|equals
argument_list|(
name|rhs
argument_list|)
return|;
block|}
if|if
condition|(
name|rhs
operator|instanceof
name|Map
condition|)
block|{
return|return
name|lhsPersistent
operator|.
name|getObjectId
argument_list|()
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|equals
argument_list|(
name|rhs
argument_list|)
return|;
block|}
if|if
condition|(
name|lhsPersistent
operator|.
name|getObjectId
argument_list|()
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
comment|// the only options left below are for the single key IDs
return|return
literal|false
return|;
block|}
if|if
condition|(
name|rhs
operator|instanceof
name|Number
condition|)
block|{
comment|// only care about whole numbers
if|if
condition|(
name|rhs
operator|instanceof
name|Integer
condition|)
block|{
return|return
name|Cayenne
operator|.
name|longPKForObject
argument_list|(
name|lhsPersistent
argument_list|)
operator|==
operator|(
operator|(
name|Number
operator|)
name|rhs
operator|)
operator|.
name|longValue
argument_list|()
return|;
block|}
if|if
condition|(
name|rhs
operator|instanceof
name|Long
condition|)
block|{
return|return
name|Cayenne
operator|.
name|longPKForObject
argument_list|(
name|lhsPersistent
argument_list|)
operator|==
operator|(
operator|(
name|Number
operator|)
name|rhs
operator|)
operator|.
name|longValue
argument_list|()
return|;
block|}
block|}
return|return
name|Cayenne
operator|.
name|pkForObject
argument_list|(
name|lhsPersistent
argument_list|)
operator|.
name|equals
argument_list|(
name|rhs
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|BIG_DECIMAL_EVALUATOR
operator|=
operator|new
name|NonNullLhsEvaluator
argument_list|(
operator|new
name|Evaluator
argument_list|()
block|{
annotation|@
name|Override
name|Integer
name|compare
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
block|{
return|return
operator|(
operator|(
name|BigDecimal
operator|)
name|lhs
operator|)
operator|.
name|compareTo
argument_list|(
name|ConversionUtil
operator|.
name|toBigDecimal
argument_list|(
name|rhs
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
name|boolean
name|eq
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
block|{
comment|// BigDecimals must be compared using compareTo (
comment|// see CAY-280 and BigDecimal.equals JavaDoc)
name|Integer
name|c
init|=
name|compare
argument_list|(
name|lhs
argument_list|,
name|rhs
argument_list|)
decl_stmt|;
return|return
name|c
operator|!=
literal|null
operator|&&
name|c
operator|==
literal|0
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|COMPAREABLE_EVALUATOR
operator|=
operator|new
name|NonNullLhsEvaluator
argument_list|(
operator|new
name|Evaluator
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
annotation|@
name|Override
name|Integer
name|compare
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
block|{
return|return
operator|(
operator|(
name|Comparable
operator|)
name|lhs
operator|)
operator|.
name|compareTo
argument_list|(
name|ConversionUtil
operator|.
name|toComparable
argument_list|(
name|rhs
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
name|boolean
name|eq
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
block|{
return|return
name|lhs
operator|.
name|equals
argument_list|(
name|rhs
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Evaluator
name|evaluator
parameter_list|(
name|Object
name|lhs
parameter_list|)
block|{
if|if
condition|(
name|lhs
operator|==
literal|null
condition|)
block|{
return|return
name|NULL_LHS_EVALUATOR
return|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|lhsType
init|=
name|lhs
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Evaluator
name|e
init|=
name|evaluators
operator|.
name|get
argument_list|(
name|lhsType
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
name|Evaluator
name|created
init|=
name|compileEvaluator
argument_list|(
name|lhsType
argument_list|)
decl_stmt|;
name|Evaluator
name|existing
init|=
name|evaluators
operator|.
name|putIfAbsent
argument_list|(
name|lhsType
argument_list|,
name|created
argument_list|)
decl_stmt|;
name|e
operator|=
name|existing
operator|!=
literal|null
condition|?
name|existing
else|:
name|created
expr_stmt|;
block|}
return|return
name|e
return|;
block|}
specifier|private
specifier|static
name|Evaluator
name|compileEvaluator
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|lhsType
parameter_list|)
block|{
name|Evaluator
name|ev
init|=
name|findInHierarchy
argument_list|(
name|lhsType
argument_list|)
decl_stmt|;
if|if
condition|(
name|ev
operator|!=
literal|null
condition|)
block|{
return|return
name|ev
return|;
block|}
comment|// check known interfaces
if|if
condition|(
name|Persistent
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|lhsType
argument_list|)
condition|)
block|{
return|return
name|PERSISTENT_EVALUATOR
return|;
block|}
if|if
condition|(
name|BigDecimal
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|lhsType
argument_list|)
condition|)
block|{
return|return
name|BIG_DECIMAL_EVALUATOR
return|;
block|}
if|if
condition|(
name|Comparable
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|lhsType
argument_list|)
condition|)
block|{
return|return
name|COMPAREABLE_EVALUATOR
return|;
block|}
comment|// nothing we recognize... return default
return|return
name|DEFAULT_EVALUATOR
return|;
block|}
specifier|private
specifier|static
name|Evaluator
name|findInHierarchy
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|lhsType
parameter_list|)
block|{
if|if
condition|(
name|Object
operator|.
name|class
operator|.
name|equals
argument_list|(
name|lhsType
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Evaluator
name|ev
init|=
name|evaluators
operator|.
name|get
argument_list|(
name|lhsType
argument_list|)
decl_stmt|;
return|return
operator|(
name|ev
operator|!=
literal|null
operator|)
condition|?
name|ev
else|:
name|findInHierarchy
argument_list|(
name|lhsType
operator|.
name|getSuperclass
argument_list|()
argument_list|)
return|;
block|}
specifier|abstract
name|boolean
name|eq
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
function_decl|;
comment|/**      * Returns NULL if comparison is invalid, otherwise returns positive,      * negative or zero, with the same meaning as      * {@link Comparable#compareTo(Object)}.      */
specifier|abstract
name|Integer
name|compare
parameter_list|(
name|Object
name|lhs
parameter_list|,
name|Object
name|rhs
parameter_list|)
function_decl|;
block|}
end_class

end_unit

