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

begin_comment
comment|/**   * Translates parts of the query to SQL.  * Always works in the context of parent Translator.   *   * @author Andrus Adamchik  */
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
specifier|public
name|QueryAssemblerHelper
parameter_list|()
block|{
block|}
comment|/** Creates QueryAssemblerHelper. Sets queryAssembler property. */
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
block|}
comment|/** Returns parent QueryAssembler that uses this helper. */
specifier|public
name|QueryAssembler
name|getQueryAssembler
parameter_list|()
block|{
return|return
name|queryAssembler
return|;
block|}
specifier|public
name|void
name|setQueryAssembler
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
block|}
comment|/** Translates the part of parent translator's query that is supported       * by this PartTranslator. For example, QualifierTranslator will process       * qualifier expression, OrderingTranslator - ordering of the query.       * In the process of translation parent translator is notified of any       * join tables added (so that it can update its "FROM" clause).       * Also parent translator is consulted about table aliases to use       * when translating columns. */
specifier|public
specifier|abstract
name|String
name|doTranslation
parameter_list|()
function_decl|;
specifier|public
name|ObjEntity
name|getObjEntity
parameter_list|()
block|{
return|return
name|getQueryAssembler
argument_list|()
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
name|getQueryAssembler
argument_list|()
operator|.
name|getRootDbEntity
argument_list|()
return|;
block|}
comment|/** Processes parts of the OBJ_PATH expression. */
specifier|protected
name|void
name|appendObjPath
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|Expression
name|pathExp
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|getObjEntity
argument_list|()
operator|.
name|resolvePathComponents
argument_list|(
name|pathExp
argument_list|)
decl_stmt|;
name|ObjRelationship
name|lastRelationship
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|pathComp
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathComp
operator|instanceof
name|ObjRelationship
condition|)
block|{
name|ObjRelationship
name|rel
init|=
operator|(
name|ObjRelationship
operator|)
name|pathComp
decl_stmt|;
comment|// if this is a last relationship in the path,
comment|// it needs special handling
if|if
condition|(
operator|!
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|processRelTermination
argument_list|(
name|buf
argument_list|,
name|rel
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// find and add joins ....
name|Iterator
name|relit
init|=
name|rel
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|relit
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|queryAssembler
operator|.
name|dbRelationshipAdded
argument_list|(
operator|(
name|DbRelationship
operator|)
name|relit
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|lastRelationship
operator|=
name|rel
expr_stmt|;
block|}
else|else
block|{
name|ObjAttribute
name|objAttr
init|=
operator|(
name|ObjAttribute
operator|)
name|pathComp
decl_stmt|;
if|if
condition|(
name|lastRelationship
operator|!=
literal|null
condition|)
block|{
name|List
name|lastDbRelList
init|=
name|lastRelationship
operator|.
name|getDbRelationships
argument_list|()
decl_stmt|;
name|DbRelationship
name|lastDbRel
init|=
operator|(
name|DbRelationship
operator|)
name|lastDbRelList
operator|.
name|get
argument_list|(
name|lastDbRelList
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|processColumn
argument_list|(
name|buf
argument_list|,
name|objAttr
operator|.
name|getDbAttribute
argument_list|()
argument_list|,
name|lastDbRel
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|processColumn
argument_list|(
name|buf
argument_list|,
name|objAttr
operator|.
name|getDbAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|appendDbPath
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|Expression
name|pathExp
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|getDbEntity
argument_list|()
operator|.
name|resolvePathComponents
argument_list|(
name|pathExp
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
name|Object
name|pathComp
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathComp
operator|instanceof
name|DbRelationship
condition|)
block|{
name|DbRelationship
name|rel
init|=
operator|(
name|DbRelationship
operator|)
name|pathComp
decl_stmt|;
comment|// if this is a last relationship in the path,
comment|// it needs special handling
if|if
condition|(
operator|!
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|processRelTermination
argument_list|(
name|buf
argument_list|,
name|rel
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
name|rel
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|DbAttribute
name|dbAttr
init|=
operator|(
name|DbAttribute
operator|)
name|pathComp
decl_stmt|;
name|processColumn
argument_list|(
name|buf
argument_list|,
name|dbAttr
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** Appends column name of a column in a root entity. */
specifier|protected
name|void
name|processColumn
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|Expression
name|nameExp
parameter_list|)
block|{
if|if
condition|(
name|queryAssembler
operator|.
name|supportsTableAliases
argument_list|()
condition|)
block|{
name|String
name|alias
init|=
name|queryAssembler
operator|.
name|aliasForTable
argument_list|(
name|getDbEntity
argument_list|()
argument_list|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|alias
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
name|nameExp
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|processColumn
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|DbAttribute
name|dbAttr
parameter_list|,
name|DbRelationship
name|relationship
parameter_list|)
block|{
name|String
name|alias
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|queryAssembler
operator|.
name|supportsTableAliases
argument_list|()
condition|)
block|{
if|if
condition|(
name|relationship
operator|!=
literal|null
condition|)
block|{
name|alias
operator|=
name|queryAssembler
operator|.
name|aliasForTable
argument_list|(
operator|(
name|DbEntity
operator|)
name|dbAttr
operator|.
name|getEntity
argument_list|()
argument_list|,
name|relationship
argument_list|)
expr_stmt|;
block|}
comment|// sometimes lookup for relationship fails (any specific case other than
comment|// relationship being null?), so lookup by entity. Note that as CAY-194
comment|// shows, lookup by DbEntity may produce incorrect results for
comment|// reflexive relationships.
if|if
condition|(
name|alias
operator|==
literal|null
condition|)
block|{
name|alias
operator|=
name|queryAssembler
operator|.
name|aliasForTable
argument_list|(
operator|(
name|DbEntity
operator|)
name|dbAttr
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|buf
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
name|processColumn
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|DbAttribute
name|dbAttr
parameter_list|)
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
name|aliasForTable
argument_list|(
operator|(
name|DbEntity
operator|)
name|dbAttr
operator|.
name|getEntity
argument_list|()
argument_list|)
else|:
literal|null
decl_stmt|;
name|buf
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
comment|/**      * Appends SQL code to the query buffer to handle<code>val</code> as a      * parameter to the PreparedStatement being built. Adds<code>val</code>      * into QueryAssembler parameter list.       *       *<p>If<code>val</code> is null, "NULL" is appended to the query.</p>      *       *<p>If<code>val</code> is a DataObject, its  primary key value is       * used as a parameter.<i>Only objects with a single column primary key       * can be used.</i>      *       * @param buf query buffer.      *       * @param val object that should be appended as a literal to the query.      * Must be of one of "standard JDBC" types, null or a DataObject.      *       * @param attr DbAttribute that has information on what type of parameter      * is being appended.      *       */
specifier|protected
name|void
name|appendLiteral
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|Object
name|val
parameter_list|,
name|DbAttribute
name|attr
parameter_list|,
name|Expression
name|parentExpression
parameter_list|)
block|{
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|buf
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
name|StringBuffer
name|msg
init|=
operator|new
name|StringBuffer
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
name|buf
argument_list|,
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
name|buf
argument_list|,
name|val
argument_list|,
name|attr
argument_list|,
name|parentExpression
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Appends SQL code to the query buffer to handle<code>val</code> as a      * parameter to the PreparedStatement being built. Adds<code>val</code>      * into QueryAssembler parameter list.       *       *       * @param buf query buffer      * @param val object that should be appended as a literal to the query.       * Must be of one of "standard JDBC" types. Can not be null.      */
specifier|protected
name|void
name|appendLiteralDirect
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|Object
name|val
parameter_list|,
name|DbAttribute
name|attr
parameter_list|,
name|Expression
name|parentExpression
parameter_list|)
block|{
name|buf
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
comment|/**       * Returns database type of expression parameters or      * null if it can not be determined.      */
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
name|Object
name|last
init|=
name|getObjEntity
argument_list|()
operator|.
name|lastPathComponent
argument_list|(
name|expression
argument_list|)
decl_stmt|;
if|if
condition|(
name|last
operator|instanceof
name|ObjAttribute
condition|)
block|{
name|attribute
operator|=
operator|(
operator|(
name|ObjAttribute
operator|)
name|last
operator|)
operator|.
name|getDbAttribute
argument_list|()
expr_stmt|;
break|break;
block|}
if|else if
condition|(
name|last
operator|instanceof
name|ObjRelationship
condition|)
block|{
name|ObjRelationship
name|objRelationship
init|=
operator|(
name|ObjRelationship
operator|)
name|last
decl_stmt|;
name|List
name|dbPath
init|=
name|objRelationship
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
operator|(
name|DbRelationship
operator|)
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
name|Object
name|last
init|=
name|getDbEntity
argument_list|()
operator|.
name|lastPathComponent
argument_list|(
name|expression
argument_list|)
decl_stmt|;
if|if
condition|(
name|last
operator|instanceof
name|DbAttribute
condition|)
block|{
name|attribute
operator|=
operator|(
name|DbAttribute
operator|)
name|last
expr_stmt|;
break|break;
block|}
if|else if
condition|(
name|last
operator|instanceof
name|DbRelationship
condition|)
block|{
name|relationship
operator|=
operator|(
name|DbRelationship
operator|)
name|last
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
operator|(
name|DbJoin
operator|)
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
comment|/** Processes case when an OBJ_PATH expression ends with relationship.       * If this is a "to many" relationship, a join is added and a column       * expression for the target entity primary key. If this is a "to one"       * relationship, column expresion for the source foreign key is added.       */
specifier|protected
name|void
name|processRelTermination
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|ObjRelationship
name|rel
parameter_list|)
block|{
name|Iterator
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
operator|(
name|DbRelationship
operator|)
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
name|buf
argument_list|,
name|dbRel
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
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**       * Handles case when a DB_NAME expression ends with relationship.      * If this is a "to many" relationship, a join is added and a column      * expression for the target entity primary key. If this is a "to one"      * relationship, column expresion for the source foreign key is added.      */
specifier|protected
name|void
name|processRelTermination
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|DbRelationship
name|rel
parameter_list|)
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
argument_list|)
expr_stmt|;
block|}
comment|// get last DbRelationship on the list
name|List
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
name|StringBuffer
name|msg
init|=
operator|new
name|StringBuffer
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
operator|(
name|DbJoin
operator|)
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
name|buf
argument_list|,
name|attribute
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

