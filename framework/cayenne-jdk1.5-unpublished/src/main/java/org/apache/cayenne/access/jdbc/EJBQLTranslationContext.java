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
name|access
operator|.
name|jdbc
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
name|HashMap
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ejbql
operator|.
name|EJBQLCompiledExpression
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
name|EJBQLException
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
name|DbEntity
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
name|DbRelationship
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
name|query
operator|.
name|SQLResultSetMapping
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
name|SQLTemplate
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
name|ClassDescriptor
import|;
end_import

begin_comment
comment|/**  * A context used for translating of EJBQL to SQL.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|EJBQLTranslationContext
block|{
specifier|private
name|EJBQLCompiledExpression
name|compiledExpression
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|parameters
decl_stmt|;
specifier|private
name|EJBQLTranslatorFactory
name|translatorFactory
decl_stmt|;
specifier|private
name|EntityResolver
name|entityResolver
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|tableAliases
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|boundParameters
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|attributes
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|idAliases
decl_stmt|;
specifier|private
name|int
name|columnAliasPosition
decl_stmt|;
specifier|private
name|boolean
name|usingAliases
decl_stmt|;
specifier|private
name|List
argument_list|<
name|StringBuilder
argument_list|>
name|bufferStack
decl_stmt|;
specifier|private
name|List
argument_list|<
name|StringBuilder
argument_list|>
name|bufferChain
decl_stmt|;
specifier|private
name|StringBuilder
name|stackTop
decl_stmt|;
comment|// a flag indicating whether column expressions should be treated as result columns or
comment|// not.
specifier|private
name|boolean
name|appendingResultColumns
decl_stmt|;
specifier|public
name|EJBQLTranslationContext
parameter_list|(
name|EntityResolver
name|entityResolver
parameter_list|,
name|EJBQLCompiledExpression
name|compiledExpression
parameter_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|EJBQLTranslatorFactory
name|translatorFactory
parameter_list|)
block|{
name|this
operator|.
name|entityResolver
operator|=
name|entityResolver
expr_stmt|;
name|this
operator|.
name|compiledExpression
operator|=
name|compiledExpression
expr_stmt|;
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
name|this
operator|.
name|translatorFactory
operator|=
name|translatorFactory
expr_stmt|;
name|this
operator|.
name|usingAliases
operator|=
literal|true
expr_stmt|;
comment|// buffer stack will hold named buffers during translation in the order they were
comment|// requested
name|this
operator|.
name|bufferStack
operator|=
operator|new
name|ArrayList
argument_list|<
name|StringBuilder
argument_list|>
argument_list|()
expr_stmt|;
comment|// buffer chain will hold named and unnamed buffers in the order they should be
comment|// concatenated
name|this
operator|.
name|bufferChain
operator|=
operator|new
name|ArrayList
argument_list|<
name|StringBuilder
argument_list|>
argument_list|()
expr_stmt|;
name|stackTop
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|bufferChain
operator|.
name|add
argument_list|(
name|stackTop
argument_list|)
expr_stmt|;
name|bufferStack
operator|.
name|add
argument_list|(
name|stackTop
argument_list|)
expr_stmt|;
block|}
name|SQLTemplate
name|getQuery
parameter_list|()
block|{
comment|// concatenate buffers...
name|StringBuilder
name|main
init|=
name|bufferChain
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|bufferChain
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|main
operator|.
name|append
argument_list|(
name|bufferChain
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|sql
init|=
name|main
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|main
operator|.
name|toString
argument_list|()
else|:
literal|null
decl_stmt|;
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|(
name|compiledExpression
operator|.
name|getRootDescriptor
argument_list|()
operator|.
name|getObjectClass
argument_list|()
argument_list|,
name|sql
argument_list|)
decl_stmt|;
name|query
operator|.
name|setParameters
argument_list|(
name|boundParameters
argument_list|)
expr_stmt|;
return|return
name|query
return|;
block|}
specifier|private
name|String
name|resolveId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
name|idAliases
operator|==
literal|null
condition|)
block|{
return|return
name|id
return|;
block|}
name|String
name|resolvedAlias
init|=
name|idAliases
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|resolvedAlias
operator|!=
literal|null
condition|)
block|{
return|return
name|resolvedAlias
return|;
block|}
return|return
name|id
return|;
block|}
name|EJBQLTranslatorFactory
name|getTranslatorFactory
parameter_list|()
block|{
return|return
name|translatorFactory
return|;
block|}
name|EntityResolver
name|getEntityResolver
parameter_list|()
block|{
return|return
name|entityResolver
return|;
block|}
comment|/**      * Looks up entity descriptor for an identifier that can be a compiled expression id      * or one of the aliases.      */
specifier|public
name|ClassDescriptor
name|getEntityDescriptor
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|compiledExpression
operator|.
name|getEntityDescriptor
argument_list|(
name|resolveId
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|getIncomingRelationships
parameter_list|(
name|EJBQLTableId
name|id
parameter_list|)
block|{
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|incoming
init|=
name|compiledExpression
operator|.
name|getIncomingRelationships
argument_list|(
name|resolveId
argument_list|(
name|id
operator|.
name|getEntityId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|// append tail of flattened relationships...
if|if
condition|(
name|id
operator|.
name|getDbPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|DbEntity
name|entity
decl_stmt|;
if|if
condition|(
name|incoming
operator|==
literal|null
operator|||
name|incoming
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|entity
operator|=
name|compiledExpression
operator|.
name|getEntityDescriptor
argument_list|(
name|id
operator|.
name|getEntityId
argument_list|()
argument_list|)
operator|.
name|getEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|DbRelationship
name|last
init|=
name|incoming
operator|.
name|get
argument_list|(
name|incoming
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|entity
operator|=
operator|(
name|DbEntity
operator|)
name|last
operator|.
name|getTargetEntity
argument_list|()
expr_stmt|;
block|}
name|incoming
operator|=
operator|new
name|ArrayList
argument_list|<
name|DbRelationship
argument_list|>
argument_list|(
name|incoming
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|entity
operator|.
name|resolvePathComponents
argument_list|(
name|id
operator|.
name|getDbPath
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|incoming
operator|.
name|add
argument_list|(
operator|(
name|DbRelationship
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|incoming
return|;
block|}
comment|/**      * Creates a previously unused id alias for an entity identified by an id.      */
name|String
name|createIdAlias
parameter_list|(
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
name|idAliases
operator|==
literal|null
condition|)
block|{
name|idAliases
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|String
name|alias
init|=
name|id
operator|+
literal|"_alias"
operator|+
name|i
decl_stmt|;
if|if
condition|(
name|idAliases
operator|.
name|containsKey
argument_list|(
name|alias
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|compiledExpression
operator|.
name|getEntityDescriptor
argument_list|(
name|alias
argument_list|)
operator|!=
literal|null
condition|)
block|{
continue|continue;
block|}
name|idAliases
operator|.
name|put
argument_list|(
name|alias
argument_list|,
name|id
argument_list|)
expr_stmt|;
return|return
name|alias
return|;
block|}
throw|throw
operator|new
name|EJBQLException
argument_list|(
literal|"Failed to create id alias"
argument_list|)
throw|;
block|}
comment|/**      * Inserts a marker in the SQL, mapped to a StringBuilder that can be later filled      * with content.      */
name|void
name|markCurrentPosition
parameter_list|(
name|String
name|marker
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
name|findOrCreateMarkedBuffer
argument_list|(
name|marker
argument_list|)
decl_stmt|;
name|bufferChain
operator|.
name|add
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
comment|// immediately create unmarked buffer after the marked one and replace the bottom
comment|// of the stack with it
name|StringBuilder
name|tailBuffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|bufferChain
operator|.
name|add
argument_list|(
name|tailBuffer
argument_list|)
expr_stmt|;
name|bufferStack
operator|.
name|set
argument_list|(
literal|0
argument_list|,
name|tailBuffer
argument_list|)
expr_stmt|;
name|stackTop
operator|=
name|bufferStack
operator|.
name|get
argument_list|(
name|bufferStack
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
comment|/**      * Switches the current buffer to a marked buffer, pushing the currently used buffer      * on the stack. Note that this can be done even before the marker is inserted in the      * main buffer. If "reset" is true, any previous contents of the marker are cleared.      */
name|void
name|pushMarker
parameter_list|(
name|String
name|marker
parameter_list|,
name|boolean
name|reset
parameter_list|)
block|{
name|stackTop
operator|=
name|findOrCreateMarkedBuffer
argument_list|(
name|marker
argument_list|)
expr_stmt|;
if|if
condition|(
name|reset
condition|)
block|{
name|stackTop
operator|.
name|delete
argument_list|(
literal|0
argument_list|,
name|stackTop
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|bufferStack
operator|.
name|add
argument_list|(
name|stackTop
argument_list|)
expr_stmt|;
block|}
comment|/**      * Pops a marker stack, switching to the previously used marker.      */
name|void
name|popMarker
parameter_list|()
block|{
name|int
name|lastIndex
init|=
name|bufferStack
operator|.
name|size
argument_list|()
operator|-
literal|1
decl_stmt|;
name|bufferStack
operator|.
name|remove
argument_list|(
name|lastIndex
argument_list|)
expr_stmt|;
name|stackTop
operator|=
name|bufferStack
operator|.
name|get
argument_list|(
name|lastIndex
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|private
name|StringBuilder
name|findOrCreateMarkedBuffer
parameter_list|(
name|String
name|marker
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|(
name|StringBuilder
operator|)
name|getAttribute
argument_list|(
name|marker
argument_list|)
decl_stmt|;
if|if
condition|(
name|buffer
operator|==
literal|null
condition|)
block|{
name|buffer
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
comment|// register mapping of internal to external marker
name|setAttribute
argument_list|(
name|marker
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
return|;
block|}
comment|/**      * Returns a context "attribute" stored for the given name. Attributes is a state      * preservation mechanism used by translators and have the same scope as the context.      */
name|Object
name|getAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|attributes
operator|!=
literal|null
condition|?
name|attributes
operator|.
name|get
argument_list|(
name|name
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**      * Sets a context "attribute". Attributes is a state preservation mechanism used by      * translators and have the same scope as the context.      */
name|void
name|setAttribute
parameter_list|(
name|String
name|var
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|attributes
operator|==
literal|null
condition|)
block|{
name|attributes
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|attributes
operator|.
name|put
argument_list|(
name|var
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Appends a piece of SQL to the internal buffer.      */
specifier|public
name|EJBQLTranslationContext
name|append
parameter_list|(
name|String
name|chunk
parameter_list|)
block|{
name|stackTop
operator|.
name|append
argument_list|(
name|chunk
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Appends a piece of SQL to the internal buffer.      */
specifier|public
name|EJBQLTranslationContext
name|append
parameter_list|(
name|char
name|chunk
parameter_list|)
block|{
name|stackTop
operator|.
name|append
argument_list|(
name|chunk
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Deletes a specified number of characters from the end of the current buffer.      */
name|EJBQLTranslationContext
name|trim
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|int
name|len
init|=
name|stackTop
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|len
operator|>=
name|n
condition|)
block|{
name|stackTop
operator|.
name|delete
argument_list|(
name|len
operator|-
name|n
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
name|EJBQLCompiledExpression
name|getCompiledExpression
parameter_list|()
block|{
return|return
name|compiledExpression
return|;
block|}
name|String
name|bindPositionalParameter
parameter_list|(
name|int
name|position
parameter_list|)
block|{
return|return
name|bindParameter
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|position
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
name|String
name|bindNamedParameter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|bindParameter
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates a new parameter variable, binding provided value to it.      */
name|String
name|bindParameter
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|bindParameter
argument_list|(
name|value
argument_list|,
literal|"id"
argument_list|)
return|;
block|}
name|void
name|rebindParameter
parameter_list|(
name|String
name|boundName
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
name|boundParameters
operator|.
name|put
argument_list|(
name|boundName
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new parameter variable with the specified prefix, binding provided value      * to it.      */
name|String
name|bindParameter
parameter_list|(
name|Object
name|value
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
if|if
condition|(
name|boundParameters
operator|==
literal|null
condition|)
block|{
name|boundParameters
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|String
name|var
init|=
name|prefix
operator|+
name|boundParameters
operator|.
name|size
argument_list|()
decl_stmt|;
name|boundParameters
operator|.
name|put
argument_list|(
name|var
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|var
return|;
block|}
name|Object
name|getBoundParameter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|boundParameters
operator|!=
literal|null
condition|?
name|boundParameters
operator|.
name|get
argument_list|(
name|name
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**      * Retrieves a SQL alias for the combination of EJBQL id variable and a table name. If      * such alias hasn't been used, it is created on the fly.      */
specifier|protected
name|String
name|getTableAlias
parameter_list|(
name|String
name|idPath
parameter_list|,
name|String
name|tableName
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isUsingAliases
argument_list|()
condition|)
block|{
return|return
name|tableName
return|;
block|}
name|StringBuilder
name|keyBuffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// per JPA spec, 4.4.2, "Identification variables are case insensitive.", while
comment|// relationship path is case-sensitive
name|int
name|dot
init|=
name|idPath
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|dot
operator|>
literal|0
condition|)
block|{
name|keyBuffer
operator|.
name|append
argument_list|(
name|idPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|dot
argument_list|)
operator|.
name|toLowerCase
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|idPath
operator|.
name|substring
argument_list|(
name|dot
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|keyBuffer
operator|.
name|append
argument_list|(
name|idPath
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|key
init|=
name|keyBuffer
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
operator|.
name|append
argument_list|(
name|tableName
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|alias
decl_stmt|;
if|if
condition|(
name|tableAliases
operator|!=
literal|null
condition|)
block|{
name|alias
operator|=
name|tableAliases
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tableAliases
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|alias
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|alias
operator|==
literal|null
condition|)
block|{
name|alias
operator|=
literal|"t"
operator|+
name|tableAliases
operator|.
name|size
argument_list|()
expr_stmt|;
name|tableAliases
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|alias
argument_list|)
expr_stmt|;
block|}
return|return
name|alias
return|;
block|}
comment|/**      * Returns a positional column alias, incrementing position index on each call.      */
name|String
name|nextColumnAlias
parameter_list|()
block|{
name|SQLResultSetMapping
name|resultSetMapping
init|=
name|compiledExpression
operator|.
name|getResultSetMapping
argument_list|()
decl_stmt|;
if|if
condition|(
name|resultSetMapping
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|EJBQLException
argument_list|(
literal|"No result set mapping exists for expression, can't map column aliases"
argument_list|)
throw|;
block|}
return|return
name|resultSetMapping
operator|.
name|getColumnResults
argument_list|()
operator|.
name|get
argument_list|(
name|columnAliasPosition
operator|++
argument_list|)
return|;
block|}
name|boolean
name|isAppendingResultColumns
parameter_list|()
block|{
return|return
name|appendingResultColumns
return|;
block|}
name|void
name|setAppendingResultColumns
parameter_list|(
name|boolean
name|appendingResultColumns
parameter_list|)
block|{
name|this
operator|.
name|appendingResultColumns
operator|=
name|appendingResultColumns
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUsingAliases
parameter_list|()
block|{
return|return
name|usingAliases
return|;
block|}
specifier|public
name|void
name|setUsingAliases
parameter_list|(
name|boolean
name|useAliases
parameter_list|)
block|{
name|this
operator|.
name|usingAliases
operator|=
name|useAliases
expr_stmt|;
block|}
block|}
end_class

end_unit

