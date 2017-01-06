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
name|translator
operator|.
name|select
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
name|exp
operator|.
name|parser
operator|.
name|ASTDbPath
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
name|ASTFunctionCall
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
name|ASTObjPath
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
name|PatternMatchNode
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
name|Query
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
name|SelectQuery
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
name|Transformer
import|;
end_import

begin_comment
comment|/**  * Translates query qualifier to SQL. Used as a helper class by query  * translators.  */
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
name|DataObjectMatchTranslator
name|objectMatchTranslator
decl_stmt|;
specifier|protected
name|boolean
name|matchingObject
decl_stmt|;
specifier|protected
name|boolean
name|caseInsensitive
decl_stmt|;
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
name|caseInsensitive
operator|=
literal|false
expr_stmt|;
block|}
comment|/** 	 * Translates query qualifier to SQL WHERE clause. Qualifier is obtained 	 * from the parent queryAssembler. 	 *  	 * @since 3.0 	 */
annotation|@
name|Override
specifier|protected
name|void
name|doAppendPart
parameter_list|()
block|{
name|doAppendPart
argument_list|(
name|extractQualifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setCaseInsensitive
parameter_list|(
name|boolean
name|caseInsensitive
parameter_list|)
block|{
name|this
operator|.
name|caseInsensitive
operator|=
name|caseInsensitive
expr_stmt|;
block|}
comment|/** 	 * Translates query qualifier to SQL WHERE clause. Qualifier is a method 	 * parameter. 	 *  	 * @since 3.0 	 */
specifier|protected
name|void
name|doAppendPart
parameter_list|(
name|Expression
name|rootNode
parameter_list|)
block|{
if|if
condition|(
name|rootNode
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|rootNode
operator|.
name|traverse
argument_list|(
name|this
argument_list|)
expr_stmt|;
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
name|SelectQuery
argument_list|<
name|?
argument_list|>
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
name|ClassDescriptor
name|descriptor
init|=
name|queryAssembler
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Expression
name|entityQualifier
init|=
name|descriptor
operator|.
name|getEntityInheritanceTree
argument_list|()
operator|.
name|qualifierForEntityAndSubclasses
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
comment|/** 		 * Attaching root Db entity's qualifier 		 */
if|if
condition|(
name|getDbEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Expression
name|dbQualifier
init|=
name|getDbEntity
argument_list|()
operator|.
name|getQualifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbQualifier
operator|!=
literal|null
condition|)
block|{
name|dbQualifier
operator|=
name|dbQualifier
operator|.
name|transform
argument_list|(
operator|new
name|DbEntityQualifierTransformer
argument_list|()
argument_list|)
expr_stmt|;
name|qualifier
operator|=
name|qualifier
operator|==
literal|null
condition|?
name|dbQualifier
else|:
name|qualifier
operator|.
name|andExp
argument_list|(
name|dbQualifier
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|qualifier
return|;
block|}
comment|/** 	 * Called before processing an expression to initialize 	 * objectMatchTranslator if needed. 	 */
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
comment|// check if there are DataObjects among direct children of the
comment|// Expression
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
throws|throws
name|IOException
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
comment|// turn off special handling, so that all the methods behave as a
comment|// superclass's
comment|// impl.
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
argument_list|,
name|JoinType
operator|.
name|INNER
argument_list|,
name|objectMatchTranslator
operator|.
name|getJoinSplitAlias
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|String
argument_list|>
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
name|out
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
name|attr
argument_list|)
expr_stmt|;
name|out
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
annotation|@
name|Override
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
name|Appendable
name|out
init|=
operator|(
name|matchingObject
operator|)
condition|?
operator|new
name|StringBuilder
argument_list|()
else|:
name|this
operator|.
name|out
decl_stmt|;
try|try
block|{
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
name|out
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
name|out
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
name|out
operator|.
name|append
argument_list|(
literal|" IS "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
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
name|out
operator|.
name|append
argument_list|(
literal|" IS NOT "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
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
name|out
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
name|out
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
name|out
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
name|out
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
name|out
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
name|out
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
name|out
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
name|out
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
if|if
condition|(
name|caseInsensitive
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|" LIKE "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|append
argument_list|(
literal|") LIKE UPPER("
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|Expression
operator|.
name|NOT_LIKE_IGNORE_CASE
case|:
if|if
condition|(
name|caseInsensitive
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|" NOT LIKE "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|append
argument_list|(
literal|") NOT LIKE UPPER("
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|Expression
operator|.
name|ADD
case|:
name|out
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
name|out
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
name|out
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
name|out
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
name|out
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
name|out
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
name|out
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
name|out
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
name|BITWISE_OR
case|:
name|out
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|operandForBitwiseOr
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|BITWISE_AND
case|:
name|out
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|operandForBitwiseAnd
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|BITWISE_XOR
case|:
name|out
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|operandForBitwiseXor
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|BITWISE_LEFT_SHIFT
case|:
name|out
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|operandForBitwiseLeftShift
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|BITWISE_RIGHT_SHIFT
case|:
name|out
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|operandForBitwiseRightShift
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|""
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error appending content"
argument_list|,
name|ioex
argument_list|)
throw|;
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
name|out
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
comment|/** 	 * @since 3.1 	 */
specifier|protected
name|String
name|operandForBitwiseNot
parameter_list|()
block|{
return|return
literal|"~"
return|;
block|}
comment|/** 	 * @since 3.1 	 */
specifier|protected
name|String
name|operandForBitwiseOr
parameter_list|()
block|{
return|return
literal|"|"
return|;
block|}
comment|/** 	 * @since 3.1 	 */
specifier|protected
name|String
name|operandForBitwiseAnd
parameter_list|()
block|{
return|return
literal|"&"
return|;
block|}
comment|/** 	 * @since 3.1 	 */
specifier|protected
name|String
name|operandForBitwiseXor
parameter_list|()
block|{
return|return
literal|"^"
return|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|protected
name|String
name|operandForBitwiseLeftShift
parameter_list|()
block|{
return|return
literal|"<<"
return|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|protected
name|String
name|operandForBitwiseRightShift
parameter_list|()
block|{
return|return
literal|">>"
return|;
block|}
annotation|@
name|Override
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
name|boolean
name|parenthesisNeeded
init|=
name|parenthesisNeeded
argument_list|(
name|node
argument_list|,
name|parentNode
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|FUNCTION_CALL
condition|)
block|{
name|appendFunction
argument_list|(
operator|(
name|ASTFunctionCall
operator|)
name|node
argument_list|)
expr_stmt|;
if|if
condition|(
name|parenthesisNeeded
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
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
condition|)
block|{
name|out
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
name|out
operator|.
name|append
argument_list|(
literal|"1 = 1"
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
name|FALSE
condition|)
block|{
name|out
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
block|{
name|out
operator|.
name|append
argument_list|(
literal|'-'
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
name|NOT
condition|)
block|{
name|out
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
name|BITWISE_NOT
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
name|operandForBitwiseNot
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
operator|(
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
operator|)
operator|&&
operator|!
name|caseInsensitive
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"UPPER("
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * @since 1.1 	 */
annotation|@
name|Override
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
try|try
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
name|boolean
name|parenthesisNeeded
init|=
name|parenthesisNeeded
argument_list|(
name|node
argument_list|,
name|parentNode
argument_list|)
decl_stmt|;
name|boolean
name|likeIgnoreCase
init|=
operator|(
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
operator|)
decl_stmt|;
name|boolean
name|isPatternMatchNode
init|=
name|PatternMatchNode
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|node
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
comment|// closing UPPER parenthesis
if|if
condition|(
name|likeIgnoreCase
operator|&&
operator|!
name|caseInsensitive
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isPatternMatchNode
condition|)
block|{
name|appendLikeEscapeCharacter
argument_list|(
operator|(
name|PatternMatchNode
operator|)
name|node
argument_list|)
expr_stmt|;
block|}
comment|// clean up trailing comma in function argument list
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|FUNCTION_CALL
condition|)
block|{
name|clearLastFunctionArgDivider
argument_list|(
operator|(
name|ASTFunctionCall
operator|)
name|node
argument_list|)
expr_stmt|;
block|}
comment|// closing LIKE parenthesis
if|if
condition|(
name|parenthesisNeeded
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
comment|// if inside function call, put comma between arguments
if|if
condition|(
name|parentNode
operator|!=
literal|null
operator|&&
name|parentNode
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|FUNCTION_CALL
condition|)
block|{
name|appendFunctionArgDivider
argument_list|(
operator|(
name|ASTFunctionCall
operator|)
name|parentNode
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ioex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error appending content"
argument_list|,
name|ioex
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
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
try|try
block|{
switch|switch
condition|(
name|parentNode
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|Expression
operator|.
name|OBJ_PATH
case|:
name|appendObjPath
argument_list|(
name|parentNode
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|DB_PATH
case|:
name|appendDbPath
argument_list|(
name|parentNode
argument_list|)
expr_stmt|;
break|break;
case|case
name|Expression
operator|.
name|LIST
case|:
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
break|break;
case|case
name|Expression
operator|.
name|FUNCTION_CALL
case|:
name|appendFunctionArg
argument_list|(
name|leaf
argument_list|,
operator|(
name|ASTFunctionCall
operator|)
name|parentNode
argument_list|)
expr_stmt|;
break|break;
default|default:
name|appendLiteral
argument_list|(
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
catch|catch
parameter_list|(
name|IOException
name|ioex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error appending content"
argument_list|,
name|ioex
argument_list|)
throw|;
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
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|FUNCTION_CALL
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|parentNode
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
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
block|{
return|return
literal|true
return|;
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
name|OBJ_PATH
operator|||
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|DB_PATH
condition|)
block|{
return|return
literal|false
return|;
block|}
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
throws|throws
name|IOException
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
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
argument_list|<
name|?
argument_list|>
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
block|{
name|appendLiteral
argument_list|(
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
else|else
block|{
return|return;
block|}
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|appendLiteral
argument_list|(
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
annotation|@
name|Override
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
operator|!
name|matchingObject
condition|)
block|{
name|super
operator|.
name|appendLiteral
argument_list|(
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
annotation|@
name|Override
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
name|rel
argument_list|,
name|joinType
argument_list|,
name|joinSplitAlias
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
argument_list|,
name|joinType
argument_list|,
name|joinSplitAlias
argument_list|)
expr_stmt|;
block|}
name|objectMatchTranslator
operator|.
name|setRelationship
argument_list|(
name|rel
argument_list|,
name|joinSplitAlias
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Append function name to result SQL 	 * Override this method to rename or skip function if generic name isn't supported on target DB. 	 * @since 4.0 	 */
specifier|protected
name|void
name|appendFunction
parameter_list|(
name|ASTFunctionCall
name|functionExpression
parameter_list|)
block|{
name|out
operator|.
name|append
argument_list|(
name|functionExpression
operator|.
name|getFunctionName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Append scalar argument of a function call 	 * Used only for values stored in ASTScalar other 	 * expressions appended in objectNode() method 	 * 	 * @since 4.0 	 */
specifier|protected
name|void
name|appendFunctionArg
parameter_list|(
name|Object
name|value
parameter_list|,
name|ASTFunctionCall
name|functionExpression
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Create fake DbAttribute to pass argument info down to bind it to SQL prepared statement
name|DbAttribute
name|dbAttrForArg
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|dbAttrForArg
operator|.
name|setType
argument_list|(
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|appendLiteral
argument_list|(
name|value
argument_list|,
name|dbAttrForArg
argument_list|,
name|functionExpression
argument_list|)
expr_stmt|;
name|appendFunctionArgDivider
argument_list|(
name|functionExpression
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Append divider between function arguments. 	 * In overriding methods can be replaced e.g. for " || " for CONCAT operation 	 * @since 4.0 	 */
specifier|protected
name|void
name|appendFunctionArgDivider
parameter_list|(
name|ASTFunctionCall
name|functionExpression
parameter_list|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Clear last divider as we currently don't now position of argument until parent element is ended. 	 * @since 4.0 	 */
specifier|protected
name|void
name|clearLastFunctionArgDivider
parameter_list|(
name|ASTFunctionCall
name|functionExpression
parameter_list|)
block|{
name|out
operator|.
name|delete
argument_list|(
name|out
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|,
name|out
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Class to translate DB Entity qualifiers annotation to Obj-entity 	 * qualifiers annotation This is done by changing all Obj-paths to Db-paths 	 * and rejecting all original Db-paths 	 */
class|class
name|DbEntityQualifierTransformer
implements|implements
name|Transformer
block|{
specifier|public
name|Object
name|transform
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
if|if
condition|(
name|input
operator|instanceof
name|ASTObjPath
condition|)
block|{
return|return
operator|new
name|ASTDbPath
argument_list|(
operator|(
operator|(
name|SimpleNode
operator|)
name|input
operator|)
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
return|return
name|input
return|;
block|}
block|}
block|}
end_class

end_unit

