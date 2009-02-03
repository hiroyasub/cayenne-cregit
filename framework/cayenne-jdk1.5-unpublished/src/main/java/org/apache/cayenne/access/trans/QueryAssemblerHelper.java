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
name|trans
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|parser
operator|.
name|SimpleNode
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
name|DbJoin
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
name|JoinType
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
name|ObjAttribute
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
name|ObjEntity
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
name|ObjRelationship
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
name|PathComponent
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
name|CayenneMapEntry
import|;
end_import

begin_comment
comment|/**  * Translates parts of the query to SQL. Always works in the context of parent Translator.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|QueryAssemblerHelper
block|{
specifier|protected
name|QueryAssembler
name|queryAssembler
decl_stmt|;
specifier|protected
name|Appendable
name|out
decl_stmt|;
specifier|private
name|String
name|identifiersStartQuote
decl_stmt|;
specifier|private
name|String
name|identifiersEndQuote
decl_stmt|;
comment|/**      * Creates QueryAssemblerHelper initializing with parent {@link QueryAssembler} and      * output buffer object.      */
specifier|public
name|QueryAssemblerHelper
parameter_list|(
name|QueryAssembler
name|queryAssembler
parameter_list|)
block|{
name|this
operator|.
name|queryAssembler
operator|=
name|queryAssembler
expr_stmt|;
if|if
condition|(
name|queryAssembler
operator|.
name|getQueryMetadata
argument_list|()
operator|.
name|getDataMap
argument_list|()
operator|!=
literal|null
operator|&&
name|queryAssembler
operator|.
name|getQueryMetadata
argument_list|()
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
condition|)
block|{
name|identifiersStartQuote
operator|=
name|queryAssembler
operator|.
name|getAdapter
argument_list|()
operator|.
name|getIdentifiersStartQuote
argument_list|()
expr_stmt|;
name|identifiersEndQuote
operator|=
name|queryAssembler
operator|.
name|getAdapter
argument_list|()
operator|.
name|getIdentifiersEndQuote
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|identifiersStartQuote
operator|=
literal|""
expr_stmt|;
name|identifiersEndQuote
operator|=
literal|""
expr_stmt|;
block|}
block|}
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|()
block|{
return|return
name|queryAssembler
operator|.
name|getRootEntity
argument_list|()
return|;
block|}
specifier|public
name|DbEntity
name|getDbEntity
parameter_list|()
block|{
return|return
name|queryAssembler
operator|.
name|getRootDbEntity
argument_list|()
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
parameter_list|<
name|T
extends|extends
name|Appendable
parameter_list|>
name|T
name|appendPart
parameter_list|(
name|T
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
name|doAppendPart
argument_list|()
expr_stmt|;
return|return
name|out
return|;
block|}
comment|/**      * @since 3.0      */
specifier|protected
specifier|abstract
name|void
name|doAppendPart
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Processes parts of the OBJ_PATH expression.      */
specifier|protected
name|void
name|appendObjPath
parameter_list|(
name|Expression
name|pathExp
parameter_list|)
throws|throws
name|IOException
block|{
name|queryAssembler
operator|.
name|resetJoinStack
argument_list|()
expr_stmt|;
name|String
name|joinSplitAlias
init|=
literal|null
decl_stmt|;
for|for
control|(
name|PathComponent
argument_list|<
name|ObjAttribute
argument_list|,
name|ObjRelationship
argument_list|>
name|component
range|:
name|getObjEntity
argument_list|()
operator|.
name|resolvePath
argument_list|(
name|pathExp
argument_list|,
name|queryAssembler
operator|.
name|getPathAliases
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|component
operator|.
name|isAlias
argument_list|()
condition|)
block|{
name|joinSplitAlias
operator|=
name|component
operator|.
name|getName
argument_list|()
expr_stmt|;
for|for
control|(
name|PathComponent
argument_list|<
name|ObjAttribute
argument_list|,
name|ObjRelationship
argument_list|>
name|aliasPart
range|:
name|component
operator|.
name|getAliasedPath
argument_list|()
control|)
block|{
name|ObjRelationship
name|relationship
init|=
name|aliasPart
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|relationship
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Non-relationship aliased path part: "
operator|+
name|aliasPart
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|aliasPart
operator|.
name|isLast
argument_list|()
operator|&&
name|component
operator|.
name|isLast
argument_list|()
condition|)
block|{
name|processRelTermination
argument_list|(
name|relationship
argument_list|,
name|aliasPart
operator|.
name|getJoinType
argument_list|()
argument_list|,
name|joinSplitAlias
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// find and add joins ....
for|for
control|(
name|DbRelationship
name|dbRel
range|:
name|relationship
operator|.
name|getDbRelationships
argument_list|()
control|)
block|{
name|queryAssembler
operator|.
name|dbRelationshipAdded
argument_list|(
name|dbRel
argument_list|,
name|aliasPart
operator|.
name|getJoinType
argument_list|()
argument_list|,
name|joinSplitAlias
argument_list|)
expr_stmt|;
block|}
block|}
block|}
continue|continue;
block|}
name|ObjRelationship
name|relationship
init|=
name|component
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
name|ObjAttribute
name|attribute
init|=
name|component
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|relationship
operator|!=
literal|null
condition|)
block|{
comment|// if this is a last relationship in the path,
comment|// it needs special handling
if|if
condition|(
name|component
operator|.
name|isLast
argument_list|()
condition|)
block|{
name|processRelTermination
argument_list|(
name|relationship
argument_list|,
name|component
operator|.
name|getJoinType
argument_list|()
argument_list|,
name|joinSplitAlias
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// find and add joins ....
for|for
control|(
name|DbRelationship
name|dbRel
range|:
name|relationship
operator|.
name|getDbRelationships
argument_list|()
control|)
block|{
name|queryAssembler
operator|.
name|dbRelationshipAdded
argument_list|(
name|dbRel
argument_list|,
name|component
operator|.
name|getJoinType
argument_list|()
argument_list|,
name|joinSplitAlias
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|dbPathIterator
init|=
name|attribute
operator|.
name|getDbPathIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|dbPathIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|pathPart
init|=
name|dbPathIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathPart
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"ObjAttribute has no component: "
operator|+
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
if|else if
condition|(
name|pathPart
operator|instanceof
name|DbRelationship
condition|)
block|{
name|queryAssembler
operator|.
name|dbRelationshipAdded
argument_list|(
operator|(
name|DbRelationship
operator|)
name|pathPart
argument_list|,
name|JoinType
operator|.
name|INNER
argument_list|,
name|joinSplitAlias
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|pathPart
operator|instanceof
name|DbAttribute
condition|)
block|{
name|processColumnWithQuoteSqlIdentifiers
argument_list|(
operator|(
name|DbAttribute
operator|)
name|pathPart
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|appendDbPath
parameter_list|(
name|Expression
name|pathExp
parameter_list|)
throws|throws
name|IOException
block|{
name|queryAssembler
operator|.
name|resetJoinStack
argument_list|()
expr_stmt|;
name|String
name|joinSplitAlias
init|=
literal|null
decl_stmt|;
for|for
control|(
name|PathComponent
argument_list|<
name|DbAttribute
argument_list|,
name|DbRelationship
argument_list|>
name|component
range|:
name|getDbEntity
argument_list|()
operator|.
name|resolvePath
argument_list|(
name|pathExp
argument_list|,
name|queryAssembler
operator|.
name|getPathAliases
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|component
operator|.
name|isAlias
argument_list|()
condition|)
block|{
name|joinSplitAlias
operator|=
name|component
operator|.
name|getName
argument_list|()
expr_stmt|;
for|for
control|(
name|PathComponent
argument_list|<
name|DbAttribute
argument_list|,
name|DbRelationship
argument_list|>
name|aliasPart
range|:
name|component
operator|.
name|getAliasedPath
argument_list|()
control|)
block|{
name|DbRelationship
name|relationship
init|=
name|aliasPart
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|relationship
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Non-relationship aliased path part: "
operator|+
name|aliasPart
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|aliasPart
operator|.
name|isLast
argument_list|()
operator|&&
name|component
operator|.
name|isLast
argument_list|()
condition|)
block|{
name|processRelTermination
argument_list|(
name|relationship
argument_list|,
name|aliasPart
operator|.
name|getJoinType
argument_list|()
argument_list|,
name|joinSplitAlias
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|queryAssembler
operator|.
name|dbRelationshipAdded
argument_list|(
name|relationship
argument_list|,
name|component
operator|.
name|getJoinType
argument_list|()
argument_list|,
name|joinSplitAlias
argument_list|)
expr_stmt|;
block|}
block|}
continue|continue;
block|}
name|DbRelationship
name|relationship
init|=
name|component
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|relationship
operator|!=
literal|null
condition|)
block|{
comment|// if this is a last relationship in the path,
comment|// it needs special handling
if|if
condition|(
name|component
operator|.
name|isLast
argument_list|()
condition|)
block|{
name|processRelTermination
argument_list|(
name|relationship
argument_list|,
name|component
operator|.
name|getJoinType
argument_list|()
argument_list|,
name|joinSplitAlias
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// find and add joins ....
name|queryAssembler
operator|.
name|dbRelationshipAdded
argument_list|(
name|relationship
argument_list|,
name|component
operator|.
name|getJoinType
argument_list|()
argument_list|,
name|joinSplitAlias
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|processColumnWithQuoteSqlIdentifiers
argument_list|(
name|component
operator|.
name|getAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|processColumn
parameter_list|(
name|DbAttribute
name|dbAttr
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|alias
init|=
operator|(
name|queryAssembler
operator|.
name|supportsTableAliases
argument_list|()
operator|)
condition|?
name|queryAssembler
operator|.
name|getCurrentAlias
argument_list|()
else|:
literal|null
decl_stmt|;
name|out
operator|.
name|append
argument_list|(
name|dbAttr
operator|.
name|getAliasedName
argument_list|(
name|alias
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|processColumnWithQuoteSqlIdentifiers
parameter_list|(
name|DbAttribute
name|dbAttr
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|alias
init|=
operator|(
name|queryAssembler
operator|.
name|supportsTableAliases
argument_list|()
operator|)
condition|?
name|queryAssembler
operator|.
name|getCurrentAlias
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|alias
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
name|identifiersStartQuote
argument_list|)
operator|.
name|append
argument_list|(
name|alias
argument_list|)
operator|.
name|append
argument_list|(
name|identifiersEndQuote
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|append
argument_list|(
name|identifiersStartQuote
argument_list|)
operator|.
name|append
argument_list|(
name|dbAttr
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|identifiersEndQuote
argument_list|)
expr_stmt|;
block|}
comment|/**      * Appends SQL code to the query buffer to handle<code>val</code> as a parameter to      * the PreparedStatement being built. Adds<code>val</code> into QueryAssembler      * parameter list.      *<p>      * If<code>val</code> is null, "NULL" is appended to the query.      *</p>      *<p>      * If<code>val</code> is a DataObject, its primary key value is used as a parameter.      *<i>Only objects with a single column primary key can be used.</i>      *       * @param val object that should be appended as a literal to the query. Must be of one      *            of "standard JDBC" types, null or a DataObject.      * @param attr DbAttribute that has information on what type of parameter is being      *            appended.      */
specifier|protected
name|void
name|appendLiteral
parameter_list|(
name|Object
name|val
parameter_list|,
name|DbAttribute
name|attr
parameter_list|,
name|Expression
name|parentExpression
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"NULL"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|val
operator|instanceof
name|Persistent
condition|)
block|{
name|ObjectId
name|id
init|=
operator|(
operator|(
name|Persistent
operator|)
name|val
operator|)
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
comment|// check if this id is acceptable to be a parameter
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't use TRANSIENT object as a query parameter."
argument_list|)
throw|;
block|}
if|if
condition|(
name|id
operator|.
name|isTemporary
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't use NEW object as a query parameter."
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|snap
init|=
name|id
operator|.
name|getIdSnapshot
argument_list|()
decl_stmt|;
if|if
condition|(
name|snap
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"Object must have a single primary key column "
argument_list|)
operator|.
name|append
argument_list|(
literal|"to serve as a query parameter. "
argument_list|)
operator|.
name|append
argument_list|(
literal|"This object has "
argument_list|)
operator|.
name|append
argument_list|(
name|snap
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
operator|.
name|append
argument_list|(
name|snap
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
comment|// checks have been passed, use id value
name|appendLiteralDirect
argument_list|(
name|snap
operator|.
name|get
argument_list|(
name|snap
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
argument_list|,
name|attr
argument_list|,
name|parentExpression
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|appendLiteralDirect
argument_list|(
name|val
argument_list|,
name|attr
argument_list|,
name|parentExpression
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Appends SQL code to the query buffer to handle<code>val</code> as a parameter to      * the PreparedStatement being built. Adds<code>val</code> into QueryAssembler      * parameter list.      *       * @param buf query buffer      * @param val object that should be appended as a literal to the query. Must be of one      *            of "standard JDBC" types. Can not be null.      */
specifier|protected
name|void
name|appendLiteralDirect
parameter_list|(
name|Object
name|val
parameter_list|,
name|DbAttribute
name|attr
parameter_list|,
name|Expression
name|parentExpression
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|append
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
comment|// we are hoping that when processing parameter list,
comment|// the correct type will be
comment|// guessed without looking at DbAttribute...
name|queryAssembler
operator|.
name|addToParamList
argument_list|(
name|attr
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns database type of expression parameters or null if it can not be determined.      */
specifier|protected
name|DbAttribute
name|paramsDbType
parameter_list|(
name|Expression
name|e
parameter_list|)
block|{
name|int
name|len
init|=
name|e
operator|.
name|getOperandCount
argument_list|()
decl_stmt|;
comment|// for unary expressions, find parent binary - this is a hack mainly to support
comment|// ASTList
if|if
condition|(
name|len
operator|<
literal|2
condition|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|SimpleNode
condition|)
block|{
name|Expression
name|parent
init|=
operator|(
name|Expression
operator|)
operator|(
operator|(
name|SimpleNode
operator|)
name|e
operator|)
operator|.
name|jjtGetParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|paramsDbType
argument_list|(
name|parent
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|// naive algorithm:
comment|// if at least one of the sibling operands is a
comment|// OBJ_PATH or DB_PATH expression, use its attribute type as
comment|// a final answer.
comment|// find attribute or relationship matching the value
name|DbAttribute
name|attribute
init|=
literal|null
decl_stmt|;
name|DbRelationship
name|relationship
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|op
init|=
name|e
operator|.
name|getOperand
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|op
operator|instanceof
name|Expression
condition|)
block|{
name|Expression
name|expression
init|=
operator|(
name|Expression
operator|)
name|op
decl_stmt|;
if|if
condition|(
name|expression
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|OBJ_PATH
condition|)
block|{
name|PathComponent
argument_list|<
name|ObjAttribute
argument_list|,
name|ObjRelationship
argument_list|>
name|last
init|=
name|getObjEntity
argument_list|()
operator|.
name|lastPathComponent
argument_list|(
name|expression
argument_list|,
name|queryAssembler
operator|.
name|getPathAliases
argument_list|()
argument_list|)
decl_stmt|;
comment|// TODO: handle EmbeddableAttribute
comment|// if (last instanceof EmbeddableAttribute)
comment|// break;
if|if
condition|(
name|last
operator|.
name|getAttribute
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|attribute
operator|=
name|last
operator|.
name|getAttribute
argument_list|()
operator|.
name|getDbAttribute
argument_list|()
expr_stmt|;
break|break;
block|}
if|else if
condition|(
name|last
operator|.
name|getRelationship
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|dbPath
init|=
name|last
operator|.
name|getRelationship
argument_list|()
operator|.
name|getDbRelationships
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbPath
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|relationship
operator|=
name|dbPath
operator|.
name|get
argument_list|(
name|dbPath
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
if|else if
condition|(
name|expression
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|DB_PATH
condition|)
block|{
name|PathComponent
argument_list|<
name|DbAttribute
argument_list|,
name|DbRelationship
argument_list|>
name|last
init|=
name|getDbEntity
argument_list|()
operator|.
name|lastPathComponent
argument_list|(
name|expression
argument_list|,
name|queryAssembler
operator|.
name|getPathAliases
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|last
operator|.
name|getAttribute
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|attribute
operator|=
name|last
operator|.
name|getAttribute
argument_list|()
expr_stmt|;
break|break;
block|}
if|else if
condition|(
name|last
operator|.
name|getRelationship
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|relationship
operator|=
name|last
operator|.
name|getRelationship
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
return|return
name|attribute
return|;
block|}
if|if
condition|(
name|relationship
operator|!=
literal|null
condition|)
block|{
comment|// Can't properly handle multiple joins....
if|if
condition|(
name|relationship
operator|.
name|getJoins
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|DbJoin
name|join
init|=
name|relationship
operator|.
name|getJoins
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
return|return
name|join
operator|.
name|getSource
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Processes case when an OBJ_PATH expression ends with relationship. If this is a "to      * many" relationship, a join is added and a column expression for the target entity      * primary key. If this is a "to one" relationship, column expression for the source      * foreign key is added.      *       * @since 3.0      */
specifier|protected
name|void
name|processRelTermination
parameter_list|(
name|ObjRelationship
name|rel
parameter_list|,
name|JoinType
name|joinType
parameter_list|,
name|String
name|joinSplitAlias
parameter_list|)
throws|throws
name|IOException
block|{
name|Iterator
argument_list|<
name|DbRelationship
argument_list|>
name|dbRels
init|=
name|rel
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
comment|// scan DbRelationships
while|while
condition|(
name|dbRels
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbRelationship
name|dbRel
init|=
name|dbRels
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// if this is a last relationship in the path,
comment|// it needs special handling
if|if
condition|(
operator|!
name|dbRels
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|processRelTermination
argument_list|(
name|dbRel
argument_list|,
name|joinType
argument_list|,
name|joinSplitAlias
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// find and add joins ....
name|queryAssembler
operator|.
name|dbRelationshipAdded
argument_list|(
name|dbRel
argument_list|,
name|joinType
argument_list|,
name|joinSplitAlias
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Handles case when a DB_NAME expression ends with relationship. If this is a "to      * many" relationship, a join is added and a column expression for the target entity      * primary key. If this is a "to one" relationship, column expression for the source      * foreign key is added.      *       * @since 3.0      */
specifier|protected
name|void
name|processRelTermination
parameter_list|(
name|DbRelationship
name|rel
parameter_list|,
name|JoinType
name|joinType
parameter_list|,
name|String
name|joinSplitAlias
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|rel
operator|.
name|isToMany
argument_list|()
condition|)
block|{
comment|// append joins
name|queryAssembler
operator|.
name|dbRelationshipAdded
argument_list|(
name|rel
argument_list|,
name|joinType
argument_list|,
name|joinSplitAlias
argument_list|)
expr_stmt|;
block|}
comment|// get last DbRelationship on the list
name|List
argument_list|<
name|DbJoin
argument_list|>
name|joins
init|=
name|rel
operator|.
name|getJoins
argument_list|()
decl_stmt|;
if|if
condition|(
name|joins
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"OBJ_PATH expressions are only supported "
argument_list|)
operator|.
name|append
argument_list|(
literal|"for a single-join relationships. "
argument_list|)
operator|.
name|append
argument_list|(
literal|"This relationship has "
argument_list|)
operator|.
name|append
argument_list|(
name|joins
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" joins."
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|DbJoin
name|join
init|=
name|joins
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|DbAttribute
name|attribute
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|rel
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|DbEntity
name|ent
init|=
operator|(
name|DbEntity
operator|)
name|join
operator|.
name|getRelationship
argument_list|()
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|pk
init|=
name|ent
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|pk
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"DB_NAME expressions can only support "
argument_list|)
operator|.
name|append
argument_list|(
literal|"targets with a single column PK. "
argument_list|)
operator|.
name|append
argument_list|(
literal|"This entity has "
argument_list|)
operator|.
name|append
argument_list|(
name|pk
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" columns in primary key."
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|attribute
operator|=
name|pk
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|attribute
operator|=
name|join
operator|.
name|getSource
argument_list|()
expr_stmt|;
block|}
name|processColumn
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

