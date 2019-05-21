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
name|template
operator|.
name|directive
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
name|Iterator
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
name|access
operator|.
name|translator
operator|.
name|ParameterBinding
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
name|dba
operator|.
name|TypesMapping
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
name|template
operator|.
name|Context
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
name|template
operator|.
name|parser
operator|.
name|ASTExpression
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|Bind
implements|implements
name|Directive
block|{
specifier|public
specifier|static
specifier|final
name|Bind
name|INSTANCE
init|=
operator|new
name|Bind
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|apply
parameter_list|(
name|Context
name|context
parameter_list|,
name|ASTExpression
modifier|...
name|expressions
parameter_list|)
block|{
if|if
condition|(
name|expressions
operator|.
name|length
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
name|Object
name|value
init|=
name|expressions
index|[
literal|0
index|]
operator|.
name|evaluateAsObject
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|String
name|jdbcTypeName
init|=
name|expressions
operator|.
name|length
operator|<
literal|2
condition|?
literal|null
else|:
name|expressions
index|[
literal|1
index|]
operator|.
name|evaluateAsString
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|int
name|scale
init|=
name|expressions
operator|.
name|length
operator|<
literal|3
condition|?
operator|-
literal|1
else|:
operator|(
name|int
operator|)
name|expressions
index|[
literal|2
index|]
operator|.
name|evaluateAsLong
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
operator|(
operator|(
name|Collection
operator|)
name|value
operator|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|bindValue
argument_list|(
name|context
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|,
name|jdbcTypeName
argument_list|,
name|scale
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|context
operator|.
name|getBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|bindValue
argument_list|(
name|context
argument_list|,
name|value
argument_list|,
name|jdbcTypeName
argument_list|,
name|scale
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|bindValue
parameter_list|(
name|Context
name|context
parameter_list|,
name|Object
name|value
parameter_list|,
name|String
name|jdbcTypeName
parameter_list|,
name|int
name|scale
parameter_list|)
block|{
name|int
name|jdbcType
decl_stmt|;
if|if
condition|(
name|jdbcTypeName
operator|!=
literal|null
condition|)
block|{
name|jdbcType
operator|=
name|TypesMapping
operator|.
name|getSqlTypeByName
argument_list|(
name|jdbcTypeName
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|jdbcType
operator|=
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|jdbcType
operator|=
name|TypesMapping
operator|.
name|getSqlTypeByName
argument_list|(
name|TypesMapping
operator|.
name|SQL_NULL
argument_list|)
expr_stmt|;
block|}
name|processBinding
argument_list|(
name|context
argument_list|,
operator|new
name|ParameterBinding
argument_list|(
name|value
argument_list|,
name|jdbcType
argument_list|,
name|scale
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|processBinding
parameter_list|(
name|Context
name|context
parameter_list|,
name|ParameterBinding
name|binding
parameter_list|)
block|{
name|context
operator|.
name|addParameterBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|context
operator|.
name|getBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

