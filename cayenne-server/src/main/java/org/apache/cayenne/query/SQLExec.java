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
name|query
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
name|ObjectContext
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
name|QueryResponse
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
name|QueryResult
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
name|DataMap
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
name|EntityResolver
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
name|QueryResultBuilder
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
name|Collections
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

begin_comment
comment|/**  * A generic query based on raw SQL and featuring fluent API. While  * {@link SQLExec} can be used to select data (see {@link #execute(ObjectContext)}  * ), it is normally used for updates, DDL operations, etc.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|SQLExec
extends|extends
name|IndirectQuery
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|6533566707148045615L
decl_stmt|;
comment|/**      * Creates a query executing provided SQL run against default database.      */
specifier|public
specifier|static
name|SQLExec
name|query
parameter_list|(
name|String
name|sql
parameter_list|)
block|{
return|return
operator|new
name|SQLExec
argument_list|(
name|sql
argument_list|)
return|;
block|}
comment|/**      * Creates a query executing provided SQL that performs routing based on the      * provided DataMap name.      */
specifier|public
specifier|static
name|SQLExec
name|query
parameter_list|(
name|String
name|dataMapName
parameter_list|,
name|String
name|sql
parameter_list|)
block|{
name|SQLExec
name|query
init|=
operator|new
name|SQLExec
argument_list|(
name|sql
argument_list|)
decl_stmt|;
name|query
operator|.
name|dataMapName
operator|=
name|dataMapName
expr_stmt|;
return|return
name|query
return|;
block|}
specifier|protected
name|String
name|dataMapName
decl_stmt|;
specifier|protected
name|StringBuilder
name|sqlBuffer
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|positionalParams
decl_stmt|;
specifier|public
name|SQLExec
parameter_list|(
name|String
name|sql
parameter_list|)
block|{
name|this
operator|.
name|sqlBuffer
operator|=
name|sql
operator|!=
literal|null
condition|?
operator|new
name|StringBuilder
argument_list|(
name|sql
argument_list|)
else|:
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
block|}
specifier|public
name|String
name|getSql
parameter_list|()
block|{
name|String
name|sql
init|=
name|sqlBuffer
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|sql
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|sql
else|:
literal|null
return|;
block|}
comment|/**      * Appends a piece of SQL to the previously stored SQL template.      */
specifier|public
name|SQLExec
name|append
parameter_list|(
name|String
name|sqlChunk
parameter_list|)
block|{
name|sqlBuffer
operator|.
name|append
argument_list|(
name|sqlChunk
argument_list|)
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SQLExec
name|params
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|params
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
specifier|public
name|SQLExec
name|params
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|params
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|params
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Map
name|bareMap
init|=
name|parameters
decl_stmt|;
name|this
operator|.
name|params
operator|.
name|putAll
argument_list|(
name|bareMap
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
comment|// since named parameters are specified, resetting positional
comment|// parameters
name|this
operator|.
name|positionalParams
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Initializes positional parameters of the query. Parameters are bound in      * the order they are found in the SQL template. If a given parameter name      * is used more than once, only the first occurrence is treated as      * "position", subsequent occurrences are bound with the same value as the      * first one. If template parameters count is different from the array      * parameter count, an exception will be thrown.      *<p>      * Note that calling this method will reset any previously set *named*      * parameters.      */
specifier|public
name|SQLExec
name|paramsArray
parameter_list|(
name|Object
modifier|...
name|params
parameter_list|)
block|{
return|return
name|paramsList
argument_list|(
name|params
operator|!=
literal|null
condition|?
name|Arrays
operator|.
name|asList
argument_list|(
name|params
argument_list|)
else|:
literal|null
argument_list|)
return|;
block|}
comment|/**      * Initializes positional parameters of the query. Parameters are bound in      * the order they are found in the SQL template. If a given parameter name      * is used more than once, only the first occurrence is treated as      * "position", subsequent occurrences are bound with the same value as the      * first one. If template parameters count is different from the list      * parameter count, an exception will be thrown.      *<p>      * Note that calling this method will reset any previously set *named*      * parameters.      */
specifier|public
name|SQLExec
name|paramsList
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|params
parameter_list|)
block|{
comment|// since named parameters are specified, resetting positional parameters
name|this
operator|.
name|params
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|positionalParams
operator|=
name|params
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Returns a potentially immutable map of named parameters that will be      * bound to SQL.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParams
parameter_list|()
block|{
return|return
name|params
operator|!=
literal|null
condition|?
name|params
else|:
name|Collections
operator|.
expr|<
name|String
operator|,
name|Object
operator|>
name|emptyMap
argument_list|()
return|;
block|}
comment|/**      * Returns a potentially immutable list of positional parameters that will      * be bound to SQL.      */
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getPositionalParams
parameter_list|()
block|{
return|return
name|positionalParams
operator|!=
literal|null
condition|?
name|positionalParams
else|:
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|public
name|QueryResult
name|execute
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
comment|// TODO: switch ObjectContext to QueryResult instead of QueryResponse
comment|// and create its own 'exec' method
name|QueryResponse
name|response
init|=
name|context
operator|.
name|performGenericQuery
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|QueryResultBuilder
name|builder
init|=
name|QueryResultBuilder
operator|.
name|builder
argument_list|(
name|response
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|response
operator|.
name|reset
argument_list|()
init|;
name|response
operator|.
name|next
argument_list|()
condition|;
control|)
block|{
if|if
condition|(
name|response
operator|.
name|isList
argument_list|()
condition|)
block|{
name|builder
operator|.
name|addSelectResult
argument_list|(
name|response
operator|.
name|currentList
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|addBatchUpdateResult
argument_list|(
name|response
operator|.
name|currentUpdateCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
specifier|public
name|int
name|update
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
comment|// TODO: create a corresponding method in ObjectContext
name|QueryResult
name|results
init|=
name|execute
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|results
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
literal|"Expected a single update result. Got a total of "
operator|+
name|results
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|results
operator|.
name|firstUpdateCount
argument_list|()
return|;
block|}
specifier|public
name|int
index|[]
name|updateBatch
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
comment|// TODO: create a corresponding method in ObjectContext
name|QueryResult
name|results
init|=
name|execute
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|results
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
literal|"Expected a single update result. Got a total of "
operator|+
name|results
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|results
operator|.
name|firstBatchUpdateCount
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Query
name|createReplacementQuery
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|Object
name|root
decl_stmt|;
if|if
condition|(
name|dataMapName
operator|!=
literal|null
condition|)
block|{
name|DataMap
name|map
init|=
name|resolver
operator|.
name|getDataMap
argument_list|(
name|dataMapName
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid dataMapName '%s'"
argument_list|,
name|dataMapName
argument_list|)
throw|;
block|}
name|root
operator|=
name|map
expr_stmt|;
block|}
else|else
block|{
comment|// will route via default node. TODO: allow explicit node name?
name|root
operator|=
literal|null
expr_stmt|;
block|}
name|SQLTemplate
name|template
init|=
operator|new
name|SQLTemplate
argument_list|()
decl_stmt|;
name|template
operator|.
name|setRoot
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|template
operator|.
name|setDefaultTemplate
argument_list|(
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|positionalParams
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|setParamsList
argument_list|(
name|positionalParams
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|.
name|setParams
argument_list|(
name|params
argument_list|)
expr_stmt|;
block|}
return|return
name|template
return|;
block|}
block|}
end_class

end_unit

