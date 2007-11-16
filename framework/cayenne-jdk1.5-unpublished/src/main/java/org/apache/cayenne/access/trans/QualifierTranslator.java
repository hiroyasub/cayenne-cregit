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
name|TraversalHandler
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
name|EntityInheritanceTree
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
name|query
operator|.
name|QualifiedQuery
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
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|IteratorUtils
import|;
end_import

begin_comment
comment|/**   * Translates query qualifier to SQL. Used as a helper class by query translators.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|QualifierTranslator
extends|extends
name|QueryAssemblerHelper
implements|implements
name|TraversalHandler
block|{
specifier|protected
name|StringBuffer
name|qualBuf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
specifier|protected
name|DataObjectMatchTranslator
name|objectMatchTranslator
decl_stmt|;
specifier|protected
name|boolean
name|matchingObject
decl_stmt|;
specifier|public
name|QualifierTranslator
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|QualifierTranslator
parameter_list|(
name|QueryAssembler
name|queryAssembler
parameter_list|)
block|{
name|super
argument_list|(
name|queryAssembler
argument_list|)
expr_stmt|;
block|}
comment|/** Translates query qualifier to SQL WHERE clause.       *  Qualifier is obtained from<code>queryAssembler</code> object.       */
specifier|public
name|String
name|doTranslation
parameter_list|()
block|{
name|qualBuf
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|Expression
name|rootNode
init|=
name|extractQualifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|rootNode
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// build SQL where clause string based on expression
comment|// (using '?' for object values)
name|rootNode
operator|.
name|traverse
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|qualBuf
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|qualBuf
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
specifier|protected
name|Expression
name|extractQualifier
parameter_list|()
block|{
name|Query
name|q
init|=
name|queryAssembler
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|Expression
name|qualifier
init|=
operator|(
operator|(
name|QualifiedQuery
operator|)
name|q
operator|)
operator|.
name|getQualifier
argument_list|()
decl_stmt|;
comment|// append Entity qualifiers, taking inheritance into account
name|ObjEntity
name|entity
init|=
name|getObjEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|EntityInheritanceTree
name|tree
init|=
name|queryAssembler
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupInheritanceTree
argument_list|(
name|entity
argument_list|)
decl_stmt|;
name|Expression
name|entityQualifier
init|=
operator|(
name|tree
operator|!=
literal|null
operator|)
condition|?
name|tree
operator|.
name|qualifierForEntityAndSubclasses
argument_list|()
else|:
name|entity
operator|.
name|getDeclaredQualifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|entityQualifier
operator|!=
literal|null
condition|)
block|{
name|qualifier
operator|=
operator|(
name|qualifier
operator|!=
literal|null
operator|)
condition|?
name|qualifier
operator|.
name|andExp
argument_list|(
name|entityQualifier
argument_list|)
else|:
name|entityQualifier
expr_stmt|;
block|}
block|}
return|return
name|qualifier
return|;
block|}
comment|/**      * Called before processing an expression to initialize objectMatchTranslator if      * needed.      */
specifier|protected
name|void
name|detectObjectMatch
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
comment|// On demand initialization of
comment|// objectMatchTranslator is not possible since there may be null
comment|// object values that would not allow to detect the need for
comment|// such translator in the right time (e.g.: null = dbpath)
name|matchingObject
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|exp
operator|.
name|getOperandCount
argument_list|()
operator|!=
literal|2
condition|)
block|{
comment|// only binary expressions are supported
return|return;
block|}
comment|// check if there are DataObjects among direct children of the Expression
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|2
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|op
init|=
name|exp
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
name|Persistent
operator|||
name|op
operator|instanceof
name|ObjectId
condition|)
block|{
name|matchingObject
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|objectMatchTranslator
operator|==
literal|null
condition|)
block|{
name|objectMatchTranslator
operator|=
operator|new
name|DataObjectMatchTranslator
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|objectMatchTranslator
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
break|break;
block|}
block|}
block|}
specifier|protected
name|void
name|appendObjectMatch
parameter_list|()
block|{
if|if
condition|(
operator|!
name|matchingObject
operator|||
name|objectMatchTranslator
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"An invalid attempt to append object match."
argument_list|)
throw|;
block|}
comment|// turn off special handling, so that all the methods behave as a superclass's impl.
name|matchingObject
operator|=
literal|false
expr_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
name|DbRelationship
name|relationship
init|=
name|objectMatchTranslator
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|relationship
operator|.
name|isToMany
argument_list|()
operator|&&
operator|!
name|relationship
operator|.
name|isToPK
argument_list|()
condition|)
block|{
name|queryAssembler
operator|.
name|dbRelationshipAdded
argument_list|(
name|relationship
argument_list|)
expr_stmt|;
block|}
name|Iterator
name|it
init|=
name|objectMatchTranslator
operator|.
name|keys
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
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|qualBuf
operator|.
name|append
argument_list|(
literal|" AND "
argument_list|)
expr_stmt|;
block|}
name|String
name|key
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|DbAttribute
name|attr
init|=
name|objectMatchTranslator
operator|.
name|getAttribute
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|Object
name|val
init|=
name|objectMatchTranslator
operator|.
name|getValue
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|processColumn
argument_list|(
name|qualBuf
argument_list|,
name|attr
argument_list|,
name|relationship
argument_list|)
expr_stmt|;
name|qualBuf
operator|.
name|append
argument_list|(
name|objectMatchTranslator
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|appendLiteral
argument_list|(
name|qualBuf
argument_list|,
name|val
argument_list|,
name|attr
argument_list|,
name|objectMatchTranslator
operator|.
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|objectMatchTranslator
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
comment|/** Opportunity to insert an operation */
specifier|public
name|void
name|finishedChild
parameter_list|(
name|Expression
name|node
parameter_list|,
name|int
name|childIndex
parameter_list|,
name|boolean
name|hasMoreChildren
parameter_list|)
block|{
if|if
condition|(
operator|!
name|hasMoreChildren
condition|)
block|{
return|return;
block|}
name|StringBuffer
name|buf
init|=
operator|(
name|matchingObject
operator|)
condition|?
operator|new
name|StringBuffer
argument_list|()
else|:
name|qualBuf
decl_stmt|;
switch|switch
condition|(
name|node
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|Expression
operator|.
name|AND
case|:
name|buf
operator|.
name|append
argument_list|(
literal|" AND "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|OR
case|:
name|buf
operator|.
name|append
argument_list|(
literal|" OR "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|EQUAL_TO
case|:
comment|// translate NULL as IS NULL
if|if
condition|(
name|childIndex
operator|==
literal|0
operator|&&
name|node
operator|.
name|getOperandCount
argument_list|()
operator|==
literal|2
operator|&&
name|node
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
operator|==
literal|null
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" IS "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" = "
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|Expression
operator|.
name|NOT_EQUAL_TO
case|:
comment|// translate NULL as IS NOT NULL
if|if
condition|(
name|childIndex
operator|==
literal|0
operator|&&
name|node
operator|.
name|getOperandCount
argument_list|()
operator|==
literal|2
operator|&&
name|node
operator|.
name|getOperand
argument_list|(
literal|1
argument_list|)
operator|==
literal|null
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" IS NOT "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"<> "
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|Expression
operator|.
name|LESS_THAN
case|:
name|buf
operator|.
name|append
argument_list|(
literal|"< "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|GREATER_THAN
case|:
name|buf
operator|.
name|append
argument_list|(
literal|"> "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|LESS_THAN_EQUAL_TO
case|:
name|buf
operator|.
name|append
argument_list|(
literal|"<= "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|GREATER_THAN_EQUAL_TO
case|:
name|buf
operator|.
name|append
argument_list|(
literal|">= "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|IN
case|:
name|buf
operator|.
name|append
argument_list|(
literal|" IN "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|NOT_IN
case|:
name|buf
operator|.
name|append
argument_list|(
literal|" NOT IN "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|LIKE
case|:
name|buf
operator|.
name|append
argument_list|(
literal|" LIKE "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|NOT_LIKE
case|:
name|buf
operator|.
name|append
argument_list|(
literal|" NOT LIKE "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|LIKE_IGNORE_CASE
case|:
name|buf
operator|.
name|append
argument_list|(
literal|") LIKE UPPER("
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|NOT_LIKE_IGNORE_CASE
case|:
name|buf
operator|.
name|append
argument_list|(
literal|") NOT LIKE UPPER("
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|ADD
case|:
name|buf
operator|.
name|append
argument_list|(
literal|" + "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|SUBTRACT
case|:
name|buf
operator|.
name|append
argument_list|(
literal|" - "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|MULTIPLY
case|:
name|buf
operator|.
name|append
argument_list|(
literal|" * "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|DIVIDE
case|:
name|buf
operator|.
name|append
argument_list|(
literal|" / "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|BETWEEN
case|:
if|if
condition|(
name|childIndex
operator|==
literal|0
condition|)
name|buf
operator|.
name|append
argument_list|(
literal|" BETWEEN "
argument_list|)
expr_stmt|;
if|else if
condition|(
name|childIndex
operator|==
literal|1
condition|)
name|buf
operator|.
name|append
argument_list|(
literal|" AND "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|NOT_BETWEEN
case|:
if|if
condition|(
name|childIndex
operator|==
literal|0
condition|)
name|buf
operator|.
name|append
argument_list|(
literal|" NOT BETWEEN "
argument_list|)
expr_stmt|;
if|else if
condition|(
name|childIndex
operator|==
literal|1
condition|)
name|buf
operator|.
name|append
argument_list|(
literal|" AND "
argument_list|)
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|matchingObject
condition|)
block|{
name|objectMatchTranslator
operator|.
name|setOperation
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|objectMatchTranslator
operator|.
name|setExpression
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|startNode
parameter_list|(
name|Expression
name|node
parameter_list|,
name|Expression
name|parentNode
parameter_list|)
block|{
name|int
name|count
init|=
name|node
operator|.
name|getOperandCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|count
operator|==
literal|2
condition|)
block|{
comment|// binary nodes are the only ones that currently require this
name|detectObjectMatch
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parenthesisNeeded
argument_list|(
name|node
argument_list|,
name|parentNode
argument_list|)
condition|)
block|{
name|qualBuf
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|count
operator|==
literal|0
condition|)
block|{
comment|// not all databases handle true/false
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|TRUE
condition|)
block|{
name|qualBuf
operator|.
name|append
argument_list|(
literal|"1 = 1"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|FALSE
condition|)
block|{
name|qualBuf
operator|.
name|append
argument_list|(
literal|"1 = 0"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|count
operator|==
literal|1
condition|)
block|{
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|NEGATIVE
condition|)
name|qualBuf
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
expr_stmt|;
comment|// ignore POSITIVE - it is a NOOP
comment|// else if(node.getType() == Expression.POSITIVE)
comment|//     qualBuf.append('+');
if|else if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|NOT
condition|)
name|qualBuf
operator|.
name|append
argument_list|(
literal|"NOT "
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|LIKE_IGNORE_CASE
operator|||
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|NOT_LIKE_IGNORE_CASE
condition|)
block|{
name|qualBuf
operator|.
name|append
argument_list|(
literal|"UPPER("
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @since 1.1      */
specifier|public
name|void
name|endNode
parameter_list|(
name|Expression
name|node
parameter_list|,
name|Expression
name|parentNode
parameter_list|)
block|{
comment|// check if we need to use objectMatchTranslator to finish building the expression
if|if
condition|(
name|node
operator|.
name|getOperandCount
argument_list|()
operator|==
literal|2
operator|&&
name|matchingObject
condition|)
block|{
name|appendObjectMatch
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|parenthesisNeeded
argument_list|(
name|node
argument_list|,
name|parentNode
argument_list|)
condition|)
block|{
name|qualBuf
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|LIKE_IGNORE_CASE
operator|||
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|NOT_LIKE_IGNORE_CASE
condition|)
block|{
name|qualBuf
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|objectNode
parameter_list|(
name|Object
name|leaf
parameter_list|,
name|Expression
name|parentNode
parameter_list|)
block|{
if|if
condition|(
name|parentNode
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|OBJ_PATH
condition|)
block|{
name|appendObjPath
argument_list|(
name|qualBuf
argument_list|,
name|parentNode
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|parentNode
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|DB_PATH
condition|)
block|{
name|appendDbPath
argument_list|(
name|qualBuf
argument_list|,
name|parentNode
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|parentNode
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|LIST
condition|)
block|{
name|appendList
argument_list|(
name|parentNode
argument_list|,
name|paramsDbType
argument_list|(
name|parentNode
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|appendLiteral
argument_list|(
name|qualBuf
argument_list|,
name|leaf
argument_list|,
name|paramsDbType
argument_list|(
name|parentNode
argument_list|)
argument_list|,
name|parentNode
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|boolean
name|parenthesisNeeded
parameter_list|(
name|Expression
name|node
parameter_list|,
name|Expression
name|parentNode
parameter_list|)
block|{
if|if
condition|(
name|parentNode
operator|==
literal|null
condition|)
return|return
literal|false
return|;
comment|// only unary expressions can go w/o parenthesis
if|if
condition|(
name|node
operator|.
name|getOperandCount
argument_list|()
operator|>
literal|1
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|OBJ_PATH
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|DB_PATH
condition|)
return|return
literal|false
return|;
return|return
literal|true
return|;
block|}
specifier|private
specifier|final
name|void
name|appendList
parameter_list|(
name|Expression
name|listExpr
parameter_list|,
name|DbAttribute
name|paramDesc
parameter_list|)
block|{
name|Iterator
name|it
init|=
literal|null
decl_stmt|;
name|Object
name|list
init|=
name|listExpr
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|instanceof
name|List
condition|)
block|{
name|it
operator|=
operator|(
operator|(
name|List
operator|)
name|list
operator|)
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|list
operator|instanceof
name|Object
index|[]
condition|)
block|{
name|it
operator|=
name|IteratorUtils
operator|.
name|arrayIterator
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|list
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|className
init|=
operator|(
name|list
operator|!=
literal|null
operator|)
condition|?
name|list
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|"<null>"
decl_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported type for the list expressions: "
operator|+
name|className
argument_list|)
throw|;
block|}
comment|// process first element outside the loop
comment|// (unroll loop to avoid condition checking
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
name|appendLiteral
argument_list|(
name|qualBuf
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|,
name|paramDesc
argument_list|,
name|listExpr
argument_list|)
expr_stmt|;
else|else
return|return;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|qualBuf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|appendLiteral
argument_list|(
name|qualBuf
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|,
name|paramDesc
argument_list|,
name|listExpr
argument_list|)
expr_stmt|;
block|}
block|}
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
operator|!
name|matchingObject
condition|)
block|{
name|super
operator|.
name|appendLiteral
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
if|else if
condition|(
name|val
operator|==
literal|null
operator|||
operator|(
name|val
operator|instanceof
name|Persistent
operator|)
condition|)
block|{
name|objectMatchTranslator
operator|.
name|setDataObject
argument_list|(
operator|(
name|Persistent
operator|)
name|val
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|val
operator|instanceof
name|ObjectId
condition|)
block|{
name|objectMatchTranslator
operator|.
name|setObjectId
argument_list|(
operator|(
name|ObjectId
operator|)
name|val
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Attempt to use literal other than DataObject during object match."
argument_list|)
throw|;
block|}
block|}
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
operator|!
name|matchingObject
condition|)
block|{
name|super
operator|.
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
name|objectMatchTranslator
operator|.
name|setRelationship
argument_list|(
name|rel
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

