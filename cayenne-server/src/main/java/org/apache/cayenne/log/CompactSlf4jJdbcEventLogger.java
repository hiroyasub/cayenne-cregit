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
name|log
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
name|access
operator|.
name|translator
operator|.
name|DbAttributeBinding
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
name|configuration
operator|.
name|RuntimeProperties
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
name|di
operator|.
name|Inject
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
name|Arrays
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
name|List
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
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|CompactSlf4jJdbcEventLogger
extends|extends
name|Slf4jJdbcEventLogger
block|{
specifier|private
specifier|static
specifier|final
name|String
name|UNION
init|=
literal|"UNION"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SELECT
init|=
literal|"SELECT"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FROM
init|=
literal|"FROM"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
name|SPACE
init|=
literal|' '
decl_stmt|;
specifier|public
name|CompactSlf4jJdbcEventLogger
parameter_list|(
annotation|@
name|Inject
name|RuntimeProperties
name|runtimeProperties
parameter_list|)
block|{
name|super
argument_list|(
name|runtimeProperties
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|logQuery
parameter_list|(
name|String
name|sql
parameter_list|,
name|ParameterBinding
index|[]
name|bindings
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isLoggable
argument_list|()
condition|)
block|{
return|return;
block|}
name|String
name|str
decl_stmt|;
if|if
condition|(
name|sql
operator|.
name|toUpperCase
argument_list|()
operator|.
name|contains
argument_list|(
name|UNION
argument_list|)
condition|)
block|{
name|str
operator|=
name|processUnionSql
argument_list|(
name|sql
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|str
operator|=
name|trimSqlSelectColumns
argument_list|(
name|sql
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|logQuery
argument_list|(
name|str
argument_list|,
name|bindings
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|processUnionSql
parameter_list|(
name|String
name|sql
parameter_list|)
block|{
name|String
name|modified
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|UNION
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
operator|.
name|matcher
argument_list|(
name|sql
argument_list|)
operator|.
name|replaceAll
argument_list|(
name|UNION
argument_list|)
decl_stmt|;
name|String
index|[]
name|queries
init|=
name|modified
operator|.
name|split
argument_list|(
name|UNION
argument_list|)
decl_stmt|;
return|return
name|Arrays
operator|.
name|stream
argument_list|(
name|queries
argument_list|)
operator|.
name|map
argument_list|(
name|this
operator|::
name|trimSqlSelectColumns
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
name|SPACE
operator|+
name|UNION
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|String
name|trimSqlSelectColumns
parameter_list|(
name|String
name|sql
parameter_list|)
block|{
name|int
name|selectIndex
init|=
name|sql
operator|.
name|toUpperCase
argument_list|()
operator|.
name|indexOf
argument_list|(
name|SELECT
argument_list|)
decl_stmt|;
if|if
condition|(
name|selectIndex
operator|==
operator|-
literal|1
condition|)
block|{
return|return
name|sql
return|;
block|}
name|selectIndex
operator|+=
name|SELECT
operator|.
name|length
argument_list|()
expr_stmt|;
name|int
name|fromIndex
init|=
name|sql
operator|.
name|toUpperCase
argument_list|()
operator|.
name|indexOf
argument_list|(
name|FROM
argument_list|)
decl_stmt|;
name|String
name|columns
init|=
name|sql
operator|.
name|substring
argument_list|(
name|selectIndex
argument_list|,
name|fromIndex
argument_list|)
decl_stmt|;
name|String
index|[]
name|columnsArray
init|=
name|columns
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|columnsArray
operator|.
name|length
operator|<=
literal|3
condition|)
block|{
return|return
name|sql
return|;
block|}
name|columns
operator|=
literal|"("
operator|+
name|columnsArray
operator|.
name|length
operator|+
literal|" columns)"
expr_stmt|;
return|return
operator|new
name|StringBuilder
argument_list|(
name|sql
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|selectIndex
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|SPACE
argument_list|)
operator|.
name|append
argument_list|(
name|columns
argument_list|)
operator|.
name|append
argument_list|(
name|SPACE
argument_list|)
operator|.
name|append
argument_list|(
name|sql
argument_list|,
name|fromIndex
argument_list|,
name|sql
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|appendParameters
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|String
name|label
parameter_list|,
name|ParameterBinding
index|[]
name|bindings
parameter_list|)
block|{
name|int
name|bindingLength
init|=
name|bindings
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|bindingLength
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|buildBinding
argument_list|(
name|buffer
argument_list|,
name|label
argument_list|,
name|collectBindings
argument_list|(
name|bindings
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|collectBindings
parameter_list|(
name|ParameterBinding
index|[]
name|bindings
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|bindingsMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|key
init|=
literal|null
decl_stmt|;
name|String
name|value
decl_stmt|;
for|for
control|(
name|ParameterBinding
name|b
range|:
name|bindings
control|)
block|{
if|if
condition|(
name|b
operator|.
name|isExcluded
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|b
operator|instanceof
name|DbAttributeBinding
condition|)
block|{
name|DbAttribute
name|attribute
init|=
operator|(
operator|(
name|DbAttributeBinding
operator|)
name|b
operator|)
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
name|key
operator|=
name|attribute
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|b
operator|.
name|getExtendedType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|b
operator|.
name|getExtendedType
argument_list|()
operator|.
name|toString
argument_list|(
name|b
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|b
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
name|value
operator|=
literal|"NULL"
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
name|b
operator|.
name|getValue
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"@"
operator|+
name|System
operator|.
name|identityHashCode
argument_list|(
name|b
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|objects
init|=
name|bindingsMap
operator|.
name|computeIfAbsent
argument_list|(
name|key
argument_list|,
name|k
lambda|->
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
decl_stmt|;
name|objects
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|bindingsMap
return|;
block|}
specifier|private
name|void
name|buildBinding
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|String
name|label
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|bindingsMap
parameter_list|)
block|{
name|int
name|j
init|=
literal|1
decl_stmt|;
name|boolean
name|hasIncluded
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|k
range|:
name|bindingsMap
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|hasIncluded
condition|)
block|{
name|hasIncluded
operator|=
literal|true
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"["
argument_list|)
operator|.
name|append
argument_list|(
name|label
argument_list|)
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|j
argument_list|)
operator|.
name|append
argument_list|(
literal|"->"
argument_list|)
operator|.
name|append
argument_list|(
name|k
argument_list|)
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|bindingsList
init|=
name|bindingsMap
operator|.
name|get
argument_list|(
name|k
argument_list|)
decl_stmt|;
if|if
condition|(
name|bindingsList
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|bindingsList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"{"
argument_list|)
expr_stmt|;
name|boolean
name|wasAdded
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Object
name|val
range|:
name|bindingsList
control|)
block|{
if|if
condition|(
name|wasAdded
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|wasAdded
operator|=
literal|true
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
block|}
name|j
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|hasIncluded
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|logBeginTransaction
parameter_list|(
name|String
name|transactionLabel
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|logCommitTransaction
parameter_list|(
name|String
name|transactionLabel
parameter_list|)
block|{
block|}
block|}
end_class

end_unit

