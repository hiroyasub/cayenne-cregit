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
name|util
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * A utility class to simplify implementation of Object toString methods. This  * implementation is a trimmed version of commons-lang ToStringBuilder.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|ToStringBuilder
block|{
specifier|protected
name|StringBuilder
name|buffer
decl_stmt|;
specifier|protected
name|Object
name|object
decl_stmt|;
specifier|protected
name|int
name|fieldCount
decl_stmt|;
specifier|public
name|ToStringBuilder
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|this
operator|.
name|object
operator|=
name|object
expr_stmt|;
name|this
operator|.
name|buffer
operator|=
operator|new
name|StringBuilder
argument_list|(
literal|128
argument_list|)
expr_stmt|;
name|appendClassName
argument_list|()
expr_stmt|;
name|appendIdentityHashCode
argument_list|()
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|'['
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ToStringBuilder
name|append
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|fieldCount
operator|++
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|fieldName
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"<null>"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|appendDetail
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|protected
name|void
name|appendDetail
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|Map
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|long
index|[]
condition|)
block|{
name|appendArray
argument_list|(
operator|(
name|long
index|[]
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|int
index|[]
condition|)
block|{
name|appendArray
argument_list|(
operator|(
name|int
index|[]
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|short
index|[]
condition|)
block|{
name|appendArray
argument_list|(
operator|(
name|short
index|[]
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|appendArray
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|char
index|[]
condition|)
block|{
name|appendArray
argument_list|(
operator|(
name|char
index|[]
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|double
index|[]
condition|)
block|{
name|appendArray
argument_list|(
operator|(
name|double
index|[]
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|float
index|[]
condition|)
block|{
name|appendArray
argument_list|(
operator|(
name|float
index|[]
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|instanceof
name|boolean
index|[]
condition|)
block|{
name|appendArray
argument_list|(
operator|(
name|boolean
index|[]
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|appendArray
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|appendArray
parameter_list|(
name|short
index|[]
name|array
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|appendArray
parameter_list|(
name|int
index|[]
name|array
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|appendArray
parameter_list|(
name|float
index|[]
name|array
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|appendArray
parameter_list|(
name|long
index|[]
name|array
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|appendArray
parameter_list|(
name|byte
index|[]
name|array
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|appendArray
parameter_list|(
name|double
index|[]
name|array
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|appendArray
parameter_list|(
name|char
index|[]
name|array
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|appendArray
parameter_list|(
name|boolean
index|[]
name|array
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|appendArray
parameter_list|(
name|Object
index|[]
name|array
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|appendDetail
argument_list|(
name|array
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|appendClassName
parameter_list|()
block|{
if|if
condition|(
name|object
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|appendIdentityHashCode
parameter_list|()
block|{
if|if
condition|(
name|object
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|'@'
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toHexString
argument_list|(
name|System
operator|.
name|identityHashCode
argument_list|(
name|object
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns a String built by the earlier invocations.      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

