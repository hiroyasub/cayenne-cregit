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
name|util
operator|.
name|ArrayDeque
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
name|Deque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|Set
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
name|access
operator|.
name|sqlbuilder
operator|.
name|ExpressionNodeBuilder
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
name|sqlbuilder
operator|.
name|ValueNodeBuilder
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|BetweenNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|BitwiseNotNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|EmptyNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|EqualNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|FunctionNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|InNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|LikeNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|Node
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|NotEqualNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|NotNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|OpExpressionNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|TextNode
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
name|ASTFullObject
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
name|ASTSubquery
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
name|exp
operator|.
name|property
operator|.
name|BaseProperty
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
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|sqlbuilder
operator|.
name|SQLBuilder
operator|.
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|Expression
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
class|class
name|QualifierTranslator
implements|implements
name|TraversalHandler
block|{
specifier|private
specifier|final
name|TranslatorContext
name|context
decl_stmt|;
specifier|private
specifier|final
name|PathTranslator
name|pathTranslator
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|Object
argument_list|>
name|expressionsToSkip
decl_stmt|;
specifier|private
specifier|final
name|Deque
argument_list|<
name|Node
argument_list|>
name|nodeStack
decl_stmt|;
specifier|private
name|Node
name|currentNode
decl_stmt|;
name|QualifierTranslator
parameter_list|(
name|TranslatorContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|pathTranslator
operator|=
name|context
operator|.
name|getPathTranslator
argument_list|()
expr_stmt|;
name|this
operator|.
name|expressionsToSkip
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|nodeStack
operator|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|Node
name|translate
parameter_list|(
name|BaseProperty
argument_list|<
name|?
argument_list|>
name|property
parameter_list|)
block|{
if|if
condition|(
name|property
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Node
name|result
init|=
name|translate
argument_list|(
name|property
operator|.
name|getExpression
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|property
operator|.
name|getAlias
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|aliased
argument_list|(
name|result
argument_list|,
name|property
operator|.
name|getAlias
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
return|return
name|result
return|;
block|}
name|Node
name|translate
parameter_list|(
name|Expression
name|qualifier
parameter_list|)
block|{
if|if
condition|(
name|qualifier
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Node
name|rootNode
init|=
operator|new
name|EmptyNode
argument_list|()
decl_stmt|;
name|expressionsToSkip
operator|.
name|clear
argument_list|()
expr_stmt|;
name|boolean
name|hasCurrentNode
init|=
name|currentNode
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
name|hasCurrentNode
condition|)
block|{
name|nodeStack
operator|.
name|push
argument_list|(
name|currentNode
argument_list|)
expr_stmt|;
block|}
name|currentNode
operator|=
name|rootNode
expr_stmt|;
name|qualifier
operator|.
name|traverse
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasCurrentNode
condition|)
block|{
name|currentNode
operator|=
name|nodeStack
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|currentNode
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|rootNode
operator|.
name|getChildrenCount
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// trim empty node
name|Node
name|child
init|=
name|rootNode
operator|.
name|getChild
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|child
operator|.
name|setParent
argument_list|(
literal|null
argument_list|)
expr_stmt|;
return|return
name|child
return|;
block|}
return|return
name|rootNode
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
if|if
condition|(
name|expressionsToSkip
operator|.
name|contains
argument_list|(
name|node
argument_list|)
operator|||
name|expressionsToSkip
operator|.
name|contains
argument_list|(
name|parentNode
argument_list|)
condition|)
block|{
return|return;
block|}
name|Node
name|nextNode
init|=
name|expressionNodeToSqlNode
argument_list|(
name|node
argument_list|,
name|parentNode
argument_list|)
decl_stmt|;
if|if
condition|(
name|nextNode
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|currentNode
operator|.
name|addChild
argument_list|(
name|nextNode
argument_list|)
expr_stmt|;
name|nextNode
operator|.
name|setParent
argument_list|(
name|currentNode
argument_list|)
expr_stmt|;
name|currentNode
operator|=
name|nextNode
expr_stmt|;
block|}
specifier|private
name|Node
name|expressionNodeToSqlNode
parameter_list|(
name|Expression
name|node
parameter_list|,
name|Expression
name|parentNode
parameter_list|)
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
name|NOT_IN
case|:
return|return
operator|new
name|InNode
argument_list|(
literal|true
argument_list|)
return|;
case|case
name|IN
case|:
return|return
operator|new
name|InNode
argument_list|(
literal|false
argument_list|)
return|;
case|case
name|NOT_BETWEEN
case|:
case|case
name|BETWEEN
case|:
return|return
operator|new
name|BetweenNode
argument_list|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|NOT_BETWEEN
argument_list|)
return|;
case|case
name|NOT
case|:
return|return
operator|new
name|NotNode
argument_list|()
return|;
case|case
name|BITWISE_NOT
case|:
return|return
operator|new
name|BitwiseNotNode
argument_list|()
return|;
case|case
name|EQUAL_TO
case|:
return|return
operator|new
name|EqualNode
argument_list|()
return|;
case|case
name|NOT_EQUAL_TO
case|:
return|return
operator|new
name|NotEqualNode
argument_list|()
return|;
case|case
name|LIKE
case|:
case|case
name|NOT_LIKE
case|:
case|case
name|LIKE_IGNORE_CASE
case|:
case|case
name|NOT_LIKE_IGNORE_CASE
case|:
name|PatternMatchNode
name|patternMatchNode
init|=
operator|(
name|PatternMatchNode
operator|)
name|node
decl_stmt|;
name|boolean
name|not
init|=
name|node
operator|.
name|getType
argument_list|()
operator|==
name|NOT_LIKE
operator|||
name|node
operator|.
name|getType
argument_list|()
operator|==
name|NOT_LIKE_IGNORE_CASE
decl_stmt|;
return|return
operator|new
name|LikeNode
argument_list|(
name|patternMatchNode
operator|.
name|isIgnoringCase
argument_list|()
argument_list|,
name|not
argument_list|,
name|patternMatchNode
operator|.
name|getEscapeChar
argument_list|()
argument_list|)
return|;
case|case
name|OBJ_PATH
case|:
name|String
name|path
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PathTranslationResult
name|result
init|=
name|pathTranslator
operator|.
name|translatePath
argument_list|(
name|context
operator|.
name|getMetadata
argument_list|()
operator|.
name|getObjEntity
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
return|return
name|processPathTranslationResult
argument_list|(
name|node
argument_list|,
name|parentNode
argument_list|,
name|result
argument_list|)
return|;
case|case
name|DB_PATH
case|:
name|String
name|dbPath
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PathTranslationResult
name|dbResult
init|=
name|pathTranslator
operator|.
name|translatePath
argument_list|(
name|context
operator|.
name|getMetadata
argument_list|()
operator|.
name|getDbEntity
argument_list|()
argument_list|,
name|dbPath
argument_list|)
decl_stmt|;
return|return
name|processPathTranslationResult
argument_list|(
name|node
argument_list|,
name|parentNode
argument_list|,
name|dbResult
argument_list|)
return|;
case|case
name|FUNCTION_CALL
case|:
name|ASTFunctionCall
name|functionCall
init|=
operator|(
name|ASTFunctionCall
operator|)
name|node
decl_stmt|;
return|return
name|function
argument_list|(
name|functionCall
operator|.
name|getFunctionName
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
case|case
name|ADD
case|:
case|case
name|SUBTRACT
case|:
case|case
name|MULTIPLY
case|:
case|case
name|DIVIDE
case|:
case|case
name|NEGATIVE
case|:
case|case
name|BITWISE_AND
case|:
case|case
name|BITWISE_LEFT_SHIFT
case|:
case|case
name|BITWISE_OR
case|:
case|case
name|BITWISE_RIGHT_SHIFT
case|:
case|case
name|BITWISE_XOR
case|:
case|case
name|OR
case|:
case|case
name|AND
case|:
case|case
name|LESS_THAN
case|:
case|case
name|LESS_THAN_EQUAL_TO
case|:
case|case
name|GREATER_THAN
case|:
case|case
name|GREATER_THAN_EQUAL_TO
case|:
return|return
operator|new
name|OpExpressionNode
argument_list|(
name|expToStr
argument_list|(
name|node
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
return|;
case|case
name|TRUE
case|:
case|case
name|FALSE
case|:
case|case
name|ASTERISK
case|:
return|return
operator|new
name|TextNode
argument_list|(
literal|' '
operator|+
name|expToStr
argument_list|(
name|node
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
return|;
case|case
name|EXISTS
case|:
return|return
operator|new
name|FunctionNode
argument_list|(
literal|"EXISTS"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
return|;
case|case
name|SUBQUERY
case|:
name|ASTSubquery
name|subquery
init|=
operator|(
name|ASTSubquery
operator|)
name|node
decl_stmt|;
name|DefaultSelectTranslator
name|translator
init|=
operator|new
name|DefaultSelectTranslator
argument_list|(
name|subquery
operator|.
name|getQuery
argument_list|()
argument_list|,
name|context
argument_list|)
decl_stmt|;
name|translator
operator|.
name|translate
argument_list|()
expr_stmt|;
return|return
name|translator
operator|.
name|getContext
argument_list|()
operator|.
name|getSelectBuilder
argument_list|()
operator|.
name|build
argument_list|()
return|;
case|case
name|ENCLOSING_OBJECT
case|:
comment|// Translate via parent context's translator
name|Expression
name|expression
init|=
operator|(
name|Expression
operator|)
name|node
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|getParentContext
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unable to translate qualifier, no parent context to use for expression "
operator|+
name|node
argument_list|)
throw|;
block|}
name|expressionsToSkip
operator|.
name|add
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|context
operator|.
name|getParentContext
argument_list|()
operator|.
name|getQualifierTranslator
argument_list|()
operator|.
name|translate
argument_list|(
name|expression
argument_list|)
return|;
case|case
name|FULL_OBJECT
case|:
name|ASTFullObject
name|fullObject
init|=
operator|(
name|ASTFullObject
operator|)
name|node
decl_stmt|;
if|if
condition|(
name|fullObject
operator|.
name|getOperandCount
argument_list|()
operator|==
literal|0
condition|)
block|{
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|dbAttributes
init|=
name|context
operator|.
name|getMetadata
argument_list|()
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbAttributes
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unable to translate reference on entity with more than one PK."
argument_list|)
throw|;
block|}
name|DbAttribute
name|attribute
init|=
name|dbAttributes
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|alias
init|=
name|context
operator|.
name|getTableTree
argument_list|()
operator|.
name|aliasForAttributePath
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|table
argument_list|(
name|alias
argument_list|)
operator|.
name|column
argument_list|(
name|attribute
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Node
name|processPathTranslationResult
parameter_list|(
name|Expression
name|node
parameter_list|,
name|Expression
name|parentNode
parameter_list|,
name|PathTranslationResult
name|result
parameter_list|)
block|{
if|if
condition|(
name|result
operator|.
name|getDbRelationship
argument_list|()
operator|.
name|isPresent
argument_list|()
operator|&&
name|result
operator|.
name|getDbAttributes
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
operator|&&
name|result
operator|.
name|getDbRelationship
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|getTargetEntity
argument_list|()
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
return|return
name|createMultiAttributeMatch
argument_list|(
name|node
argument_list|,
name|parentNode
argument_list|,
name|result
argument_list|)
return|;
block|}
if|else if
condition|(
name|result
operator|.
name|getDbAttributes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
operator|new
name|EmptyNode
argument_list|()
return|;
block|}
else|else
block|{
name|String
name|alias
init|=
name|context
operator|.
name|getTableTree
argument_list|()
operator|.
name|aliasForPath
argument_list|(
name|result
operator|.
name|getLastAttributePath
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|table
argument_list|(
name|alias
argument_list|)
operator|.
name|column
argument_list|(
name|result
operator|.
name|getLastAttribute
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
specifier|private
name|Node
name|createMultiAttributeMatch
parameter_list|(
name|Expression
name|node
parameter_list|,
name|Expression
name|parentNode
parameter_list|,
name|PathTranslationResult
name|result
parameter_list|)
block|{
name|ObjectId
name|objectId
init|=
literal|null
decl_stmt|;
name|expressionsToSkip
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|expressionsToSkip
operator|.
name|add
argument_list|(
name|parentNode
argument_list|)
expr_stmt|;
name|int
name|siblings
init|=
name|parentNode
operator|.
name|getOperandCount
argument_list|()
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
name|siblings
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|operand
init|=
name|parentNode
operator|.
name|getOperand
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|==
name|operand
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|operand
operator|instanceof
name|Persistent
condition|)
block|{
name|objectId
operator|=
operator|(
operator|(
name|Persistent
operator|)
name|operand
operator|)
operator|.
name|getObjectId
argument_list|()
expr_stmt|;
break|break;
block|}
if|else if
condition|(
name|operand
operator|instanceof
name|ObjectId
condition|)
block|{
name|objectId
operator|=
operator|(
name|ObjectId
operator|)
name|operand
expr_stmt|;
break|break;
block|}
if|else if
condition|(
name|operand
operator|instanceof
name|ASTObjPath
condition|)
block|{
comment|// TODO: support comparision of multi attribute ObjPath with other multi attribute ObjPath
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Comparision of multiple attributes not supported for ObjPath"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|objectId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Multi attribute ObjPath isn't matched with valid value. "
operator|+
literal|"List or Persistent object required."
argument_list|)
throw|;
block|}
name|ExpressionNodeBuilder
name|expressionNodeBuilder
init|=
literal|null
decl_stmt|;
name|ExpressionNodeBuilder
name|eq
decl_stmt|;
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|pkIt
init|=
name|result
operator|.
name|getDbRelationship
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|getTargetEntity
argument_list|()
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|int
name|count
init|=
name|result
operator|.
name|getDbAttributes
argument_list|()
operator|.
name|size
argument_list|()
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
name|count
condition|;
name|i
operator|++
control|)
block|{
name|String
name|path
init|=
name|result
operator|.
name|getAttributePaths
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|DbAttribute
name|attribute
init|=
name|result
operator|.
name|getDbAttributes
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|DbAttribute
name|pk
init|=
name|pkIt
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|alias
init|=
name|context
operator|.
name|getTableTree
argument_list|()
operator|.
name|aliasForPath
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Object
name|nextValue
init|=
name|objectId
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|get
argument_list|(
name|pk
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|eq
operator|=
name|table
argument_list|(
name|alias
argument_list|)
operator|.
name|column
argument_list|(
name|attribute
argument_list|)
operator|.
name|eq
argument_list|(
name|value
argument_list|(
name|nextValue
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|expressionNodeBuilder
operator|==
literal|null
condition|)
block|{
name|expressionNodeBuilder
operator|=
name|eq
expr_stmt|;
block|}
else|else
block|{
name|expressionNodeBuilder
operator|=
name|expressionNodeBuilder
operator|.
name|and
argument_list|(
name|eq
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|expressionNodeBuilder
operator|.
name|build
argument_list|()
return|;
block|}
specifier|private
name|boolean
name|nodeProcessed
parameter_list|(
name|Expression
name|node
parameter_list|)
block|{
comment|// must be in sync with expressionNodeToSqlNode() method
switch|switch
condition|(
name|node
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|NOT_IN
case|:
case|case
name|IN
case|:
case|case
name|NOT_BETWEEN
case|:
case|case
name|BETWEEN
case|:
case|case
name|NOT
case|:
case|case
name|BITWISE_NOT
case|:
case|case
name|EQUAL_TO
case|:
case|case
name|NOT_EQUAL_TO
case|:
case|case
name|LIKE
case|:
case|case
name|NOT_LIKE
case|:
case|case
name|LIKE_IGNORE_CASE
case|:
case|case
name|NOT_LIKE_IGNORE_CASE
case|:
case|case
name|OBJ_PATH
case|:
case|case
name|DB_PATH
case|:
case|case
name|FUNCTION_CALL
case|:
case|case
name|ADD
case|:
case|case
name|SUBTRACT
case|:
case|case
name|MULTIPLY
case|:
case|case
name|DIVIDE
case|:
case|case
name|NEGATIVE
case|:
case|case
name|BITWISE_AND
case|:
case|case
name|BITWISE_LEFT_SHIFT
case|:
case|case
name|BITWISE_OR
case|:
case|case
name|BITWISE_RIGHT_SHIFT
case|:
case|case
name|BITWISE_XOR
case|:
case|case
name|OR
case|:
case|case
name|AND
case|:
case|case
name|LESS_THAN
case|:
case|case
name|LESS_THAN_EQUAL_TO
case|:
case|case
name|GREATER_THAN
case|:
case|case
name|GREATER_THAN_EQUAL_TO
case|:
case|case
name|TRUE
case|:
case|case
name|FALSE
case|:
case|case
name|ASTERISK
case|:
case|case
name|EXISTS
case|:
case|case
name|SUBQUERY
case|:
case|case
name|ENCLOSING_OBJECT
case|:
case|case
name|FULL_OBJECT
case|:
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
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
if|if
condition|(
name|expressionsToSkip
operator|.
name|contains
argument_list|(
name|node
argument_list|)
operator|||
name|expressionsToSkip
operator|.
name|contains
argument_list|(
name|parentNode
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|nodeProcessed
argument_list|(
name|node
argument_list|)
condition|)
block|{
if|if
condition|(
name|currentNode
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|currentNode
operator|=
name|currentNode
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
name|expressionsToSkip
operator|.
name|contains
argument_list|(
name|parentNode
argument_list|)
condition|)
block|{
return|return;
block|}
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
operator|||
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
return|return;
block|}
name|ValueNodeBuilder
name|valueNodeBuilder
init|=
name|value
argument_list|(
name|leaf
argument_list|)
operator|.
name|attribute
argument_list|(
name|findDbAttribute
argument_list|(
name|parentNode
argument_list|)
argument_list|)
decl_stmt|;
if|if
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
name|valueNodeBuilder
operator|.
name|array
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|Node
name|nextNode
init|=
name|valueNodeBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
name|currentNode
operator|.
name|addChild
argument_list|(
name|nextNode
argument_list|)
expr_stmt|;
name|nextNode
operator|.
name|setParent
argument_list|(
name|currentNode
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|DbAttribute
name|findDbAttribute
parameter_list|(
name|Expression
name|node
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
name|LIST
condition|)
block|{
if|if
condition|(
name|node
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
name|node
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
name|node
operator|=
name|parent
expr_stmt|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
name|PathTranslationResult
name|result
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
name|node
operator|.
name|getOperandCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|op
init|=
name|node
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
name|ASTObjPath
condition|)
block|{
name|result
operator|=
name|pathTranslator
operator|.
name|translatePath
argument_list|(
name|context
operator|.
name|getMetadata
argument_list|()
operator|.
name|getObjEntity
argument_list|()
argument_list|,
operator|(
operator|(
name|ASTObjPath
operator|)
name|op
operator|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
if|else if
condition|(
name|op
operator|instanceof
name|ASTDbPath
condition|)
block|{
name|result
operator|=
name|pathTranslator
operator|.
name|translatePath
argument_list|(
name|context
operator|.
name|getMetadata
argument_list|()
operator|.
name|getDbEntity
argument_list|()
argument_list|,
operator|(
operator|(
name|ASTDbPath
operator|)
name|op
operator|)
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|result
operator|.
name|getLastAttribute
argument_list|()
return|;
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
block|}
specifier|private
name|String
name|expToStr
parameter_list|(
name|int
name|type
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|AND
case|:
return|return
literal|"AND"
return|;
case|case
name|OR
case|:
return|return
literal|"OR"
return|;
case|case
name|LESS_THAN
case|:
return|return
literal|"<"
return|;
case|case
name|LESS_THAN_EQUAL_TO
case|:
return|return
literal|"<="
return|;
case|case
name|GREATER_THAN
case|:
return|return
literal|">"
return|;
case|case
name|GREATER_THAN_EQUAL_TO
case|:
return|return
literal|">="
return|;
case|case
name|ADD
case|:
return|return
literal|"+"
return|;
case|case
name|SUBTRACT
case|:
return|return
literal|"-"
return|;
case|case
name|MULTIPLY
case|:
return|return
literal|"*"
return|;
case|case
name|DIVIDE
case|:
return|return
literal|"/"
return|;
case|case
name|NEGATIVE
case|:
return|return
literal|"-"
return|;
case|case
name|BITWISE_AND
case|:
return|return
literal|"&"
return|;
case|case
name|BITWISE_OR
case|:
return|return
literal|"|"
return|;
case|case
name|BITWISE_XOR
case|:
return|return
literal|"^"
return|;
case|case
name|BITWISE_NOT
case|:
return|return
literal|"!"
return|;
case|case
name|BITWISE_LEFT_SHIFT
case|:
return|return
literal|"<<"
return|;
case|case
name|BITWISE_RIGHT_SHIFT
case|:
return|return
literal|">>"
return|;
case|case
name|TRUE
case|:
return|return
literal|"1=1"
return|;
case|case
name|FALSE
case|:
return|return
literal|"1=0"
return|;
case|case
name|ASTERISK
case|:
return|return
literal|"*"
return|;
default|default:
return|return
literal|"{other}"
return|;
block|}
block|}
block|}
end_class

end_unit

