begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/* Generated By:JJTree: Do not edit this line. SimpleNode.java */
end_comment

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
name|exp
operator|.
name|parser
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
name|io
operator|.
name|PrintWriter
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
name|ExpressionException
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
name|Util
import|;
end_import

begin_comment
comment|/**  * Superclass of AST* expressions that implements Node interface defined by  * JavaCC framework.  *<p>  * Some parts of the parser are based on OGNL parser, copyright (c) 2002, Drew  * Davidson and Luke Blanshard.  *</p>  *   * @since 1.1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|SimpleNode
extends|extends
name|Expression
implements|implements
name|Node
block|{
specifier|protected
name|Node
name|parent
decl_stmt|;
specifier|protected
name|Node
index|[]
name|children
decl_stmt|;
specifier|protected
name|int
name|id
decl_stmt|;
comment|/**      *<p>This is a utility method that can represent the supplied scalar as either an EJBQL literal into the      * supplied {@link java.io.PrintWriter} or is able to add the scalar to the parameters and to instead      * write a positional parameter to the EJBQL written to the {@link java.io.PrintWriter}.  If the parameters      * are null and the scalar object is not able to be represented as an EJBQL literal then the method will      * throw a runtime exception to indicate that it has failed to produce valid EJBQL.</p>      */
specifier|protected
specifier|static
name|void
name|encodeScalarAsEJBQL
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|parameterAccumulator
parameter_list|,
name|Appendable
name|out
parameter_list|,
name|Object
name|scalar
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
literal|null
operator|==
name|scalar
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"null"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|scalar
operator|instanceof
name|Boolean
condition|)
block|{
if|if
condition|(
operator|(
name|Boolean
operator|)
name|scalar
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|append
argument_list|(
literal|"false"
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
if|if
condition|(
literal|null
operator|!=
name|parameterAccumulator
condition|)
block|{
name|parameterAccumulator
operator|.
name|add
argument_list|(
name|scalar
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|parameterAccumulator
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// parameters start at 1
return|return;
block|}
if|if
condition|(
name|scalar
operator|instanceof
name|Integer
operator|||
name|scalar
operator|instanceof
name|Long
operator|||
name|scalar
operator|instanceof
name|Float
operator|||
name|scalar
operator|instanceof
name|Double
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
name|scalar
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|scalar
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
name|scalar
operator|)
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|Object
name|encode
init|=
operator|(
name|id
operator|!=
literal|null
operator|)
condition|?
name|id
else|:
name|scalar
decl_stmt|;
name|appendAsEscapedString
argument_list|(
name|out
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|encode
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|scalar
operator|instanceof
name|Enum
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|Enum
argument_list|<
name|?
argument_list|>
name|e
init|=
operator|(
name|Enum
argument_list|<
name|?
argument_list|>
operator|)
name|scalar
decl_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|"enum:"
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|e
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|scalar
operator|instanceof
name|String
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|appendAsEscapedString
argument_list|(
name|out
argument_list|,
name|scalar
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
return|return;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"the scalar type '"
operator|+
name|scalar
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"' is not supported as a scalar type in EJBQL"
argument_list|)
throw|;
block|}
comment|/**      * Utility method that encodes an object that is not an expression Node to      * String.      */
specifier|protected
specifier|static
name|void
name|appendScalarAsString
parameter_list|(
name|Appendable
name|out
parameter_list|,
name|Object
name|scalar
parameter_list|,
name|char
name|quoteChar
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|quote
init|=
name|scalar
operator|instanceof
name|String
decl_stmt|;
if|if
condition|(
name|quote
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
name|quoteChar
argument_list|)
expr_stmt|;
block|}
comment|// encode only ObjectId for Persistent, ensure that the order of keys is
comment|// predictable....
comment|// TODO: should we use UUID here?
if|if
condition|(
name|scalar
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
name|scalar
operator|)
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|Object
name|encode
init|=
operator|(
name|id
operator|!=
literal|null
operator|)
condition|?
name|id
else|:
name|scalar
decl_stmt|;
name|appendAsEscapedString
argument_list|(
name|out
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|encode
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|scalar
operator|instanceof
name|Enum
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|Enum
argument_list|<
name|?
argument_list|>
name|e
init|=
operator|(
name|Enum
argument_list|<
name|?
argument_list|>
operator|)
name|scalar
decl_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|"enum:"
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|e
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|appendAsEscapedString
argument_list|(
name|out
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|scalar
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|quote
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
name|quoteChar
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Utility method that prints a string to the provided Appendable, escaping      * special characters.      */
specifier|protected
specifier|static
name|void
name|appendAsEscapedString
parameter_list|(
name|Appendable
name|out
parameter_list|,
name|String
name|source
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|len
init|=
name|source
operator|.
name|length
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
name|len
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|source
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|c
condition|)
block|{
case|case
literal|'\n'
case|:
name|out
operator|.
name|append
argument_list|(
literal|"\\n"
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|'\r'
case|:
name|out
operator|.
name|append
argument_list|(
literal|"\\r"
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|'\t'
case|:
name|out
operator|.
name|append
argument_list|(
literal|"\\t"
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|'\b'
case|:
name|out
operator|.
name|append
argument_list|(
literal|"\\b"
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|'\f'
case|:
name|out
operator|.
name|append
argument_list|(
literal|"\\f"
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|'\\'
case|:
name|out
operator|.
name|append
argument_list|(
literal|"\\\\"
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|'\''
case|:
name|out
operator|.
name|append
argument_list|(
literal|"\\'"
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|'\"'
case|:
name|out
operator|.
name|append
argument_list|(
literal|"\\\""
argument_list|)
expr_stmt|;
continue|continue;
default|default:
name|out
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|SimpleNode
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|id
operator|=
name|i
expr_stmt|;
block|}
comment|/**      * Always returns empty map.      *       * @since 3.0      */
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getPathAliases
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
specifier|protected
specifier|abstract
name|String
name|getExpressionOperator
parameter_list|(
name|int
name|index
parameter_list|)
function_decl|;
comment|/**      * Returns operator for ebjql statements, which can differ for Cayenne      * expression operator      */
specifier|protected
name|String
name|getEJBQLExpressionOperator
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|getExpressionOperator
argument_list|(
name|index
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|pruneNodeForPrunedChild
parameter_list|(
name|Object
name|prunedChild
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Implemented for backwards compatibility with exp package.      */
annotation|@
name|Override
specifier|public
name|String
name|expName
parameter_list|()
block|{
return|return
name|ExpressionParserTreeConstants
operator|.
name|jjtNodeName
index|[
name|id
index|]
return|;
block|}
comment|/**      * Flattens the tree under this node by eliminating any children that are of      * the same class as this node and copying their children to this node.      */
annotation|@
name|Override
specifier|protected
name|void
name|flattenTree
parameter_list|()
block|{
name|boolean
name|shouldFlatten
init|=
literal|false
decl_stmt|;
name|int
name|newSize
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Node
name|child
range|:
name|children
control|)
block|{
if|if
condition|(
name|child
operator|.
name|getClass
argument_list|()
operator|==
name|getClass
argument_list|()
condition|)
block|{
name|shouldFlatten
operator|=
literal|true
expr_stmt|;
name|newSize
operator|+=
name|child
operator|.
name|jjtGetNumChildren
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|newSize
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|shouldFlatten
condition|)
block|{
name|Node
index|[]
name|newChildren
init|=
operator|new
name|Node
index|[
name|newSize
index|]
decl_stmt|;
name|int
name|j
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Node
name|c
range|:
name|children
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getClass
argument_list|()
operator|==
name|getClass
argument_list|()
condition|)
block|{
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|c
operator|.
name|jjtGetNumChildren
argument_list|()
condition|;
operator|++
name|k
control|)
block|{
name|newChildren
index|[
name|j
operator|++
index|]
operator|=
name|c
operator|.
name|jjtGetChild
argument_list|(
name|k
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|newChildren
index|[
name|j
operator|++
index|]
operator|=
name|c
expr_stmt|;
block|}
block|}
if|if
condition|(
name|j
operator|!=
name|newSize
condition|)
block|{
throw|throw
operator|new
name|ExpressionException
argument_list|(
literal|"Assertion error: "
operator|+
name|j
operator|+
literal|" != "
operator|+
name|newSize
argument_list|)
throw|;
block|}
name|this
operator|.
name|children
operator|=
name|newChildren
expr_stmt|;
block|}
block|}
comment|/**      * @since 4.0      */
annotation|@
name|Override
specifier|public
name|void
name|appendAsString
parameter_list|(
name|Appendable
name|out
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|parent
operator|!=
literal|null
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
if|if
condition|(
operator|(
name|children
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|children
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|children
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|getExpressionOperator
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|children
index|[
name|i
index|]
operator|==
literal|null
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"null"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
operator|(
operator|(
name|SimpleNode
operator|)
name|children
index|[
name|i
index|]
operator|)
operator|.
name|appendAsString
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|parent
operator|!=
literal|null
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
block|}
comment|/**      * @deprecated since 4.0 use {@link #appendAsString(Appendable)}.      */
annotation|@
name|Override
annotation|@
name|Deprecated
specifier|public
name|void
name|encodeAsString
parameter_list|(
name|PrintWriter
name|pw
parameter_list|)
block|{
try|try
block|{
name|appendAsString
argument_list|(
name|pw
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unexpected IO exception appending to PrintWriter"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getOperand
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|Node
name|child
init|=
name|jjtGetChild
argument_list|(
name|index
argument_list|)
decl_stmt|;
comment|// unwrap ASTScalar nodes - this is likely a temporary thing to keep it
comment|// compatible
comment|// with QualifierTranslator. In the future we might want to keep scalar
comment|// nodes
comment|// for the purpose of expression evaluation.
return|return
name|unwrapChild
argument_list|(
name|child
argument_list|)
return|;
block|}
specifier|protected
name|Node
name|wrapChild
parameter_list|(
name|Object
name|child
parameter_list|)
block|{
comment|// when child is null, there's no way of telling whether this is a
comment|// scalar or
comment|// not... fuzzy... maybe we should stop using this method - it is too
comment|// generic
return|return
operator|(
name|child
operator|instanceof
name|Node
operator|||
name|child
operator|==
literal|null
operator|)
condition|?
operator|(
name|Node
operator|)
name|child
else|:
operator|new
name|ASTScalar
argument_list|(
name|child
argument_list|)
return|;
block|}
specifier|protected
name|Object
name|unwrapChild
parameter_list|(
name|Node
name|child
parameter_list|)
block|{
return|return
operator|(
name|child
operator|instanceof
name|ASTScalar
operator|)
condition|?
operator|(
operator|(
name|ASTScalar
operator|)
name|child
operator|)
operator|.
name|getValue
argument_list|()
else|:
name|child
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getOperandCount
parameter_list|()
block|{
return|return
name|jjtGetNumChildren
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setOperand
parameter_list|(
name|int
name|index
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Node
name|node
init|=
operator|(
name|value
operator|==
literal|null
operator|||
name|value
operator|instanceof
name|Node
operator|)
condition|?
operator|(
name|Node
operator|)
name|value
else|:
operator|new
name|ASTScalar
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|jjtAddChild
argument_list|(
name|node
argument_list|,
name|index
argument_list|)
expr_stmt|;
comment|// set the parent, as jjtAddChild doesn't do it...
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|node
operator|.
name|jjtSetParent
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|jjtOpen
parameter_list|()
block|{
block|}
specifier|public
name|void
name|jjtClose
parameter_list|()
block|{
block|}
specifier|public
name|void
name|jjtSetParent
parameter_list|(
name|Node
name|n
parameter_list|)
block|{
name|parent
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|Node
name|jjtGetParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
specifier|public
name|void
name|jjtAddChild
parameter_list|(
name|Node
name|n
parameter_list|,
name|int
name|i
parameter_list|)
block|{
if|if
condition|(
name|children
operator|==
literal|null
condition|)
block|{
name|children
operator|=
operator|new
name|Node
index|[
name|i
operator|+
literal|1
index|]
expr_stmt|;
block|}
if|else if
condition|(
name|i
operator|>=
name|children
operator|.
name|length
condition|)
block|{
name|Node
name|c
index|[]
init|=
operator|new
name|Node
index|[
name|i
operator|+
literal|1
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|children
argument_list|,
literal|0
argument_list|,
name|c
argument_list|,
literal|0
argument_list|,
name|children
operator|.
name|length
argument_list|)
expr_stmt|;
name|children
operator|=
name|c
expr_stmt|;
block|}
name|children
index|[
name|i
index|]
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|Node
name|jjtGetChild
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|children
index|[
name|i
index|]
return|;
block|}
specifier|public
specifier|final
name|int
name|jjtGetNumChildren
parameter_list|()
block|{
return|return
operator|(
name|children
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|children
operator|.
name|length
return|;
block|}
comment|/**      * Evaluates itself with object, pushing result on the stack.      */
specifier|protected
specifier|abstract
name|Object
name|evaluateNode
parameter_list|(
name|Object
name|o
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Sets the parent to this for all children.      *       * @since 3.0      */
specifier|protected
name|void
name|connectChildren
parameter_list|()
block|{
if|if
condition|(
name|children
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Node
name|child
range|:
name|children
control|)
block|{
comment|// although nulls are expected to be wrapped in scalar, still
comment|// doing a
comment|// check here to make it more robust
if|if
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
name|child
operator|.
name|jjtSetParent
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|Object
name|evaluateChild
parameter_list|(
name|int
name|index
parameter_list|,
name|Object
name|o
parameter_list|)
throws|throws
name|Exception
block|{
name|SimpleNode
name|node
init|=
operator|(
name|SimpleNode
operator|)
name|jjtGetChild
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
name|node
operator|!=
literal|null
condition|?
name|node
operator|.
name|evaluate
argument_list|(
name|o
argument_list|)
else|:
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Expression
name|notExp
parameter_list|()
block|{
return|return
operator|new
name|ASTNot
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
comment|// wrap in try/catch to provide unified exception processing
try|try
block|{
return|return
name|evaluateNode
argument_list|(
name|o
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|String
name|string
init|=
name|this
operator|.
name|toString
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|ExpressionException
argument_list|(
literal|"Error evaluating expression '"
operator|+
name|string
operator|+
literal|"'"
argument_list|,
name|string
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|th
argument_list|)
argument_list|)
throw|;
block|}
block|}
comment|/**      * @since 3.0      * @deprecated since 4.0 use {@link #appendAsEJBQL(Appendable, String)}.      */
annotation|@
name|Override
annotation|@
name|Deprecated
specifier|public
name|void
name|encodeAsEJBQL
parameter_list|(
name|PrintWriter
name|pw
parameter_list|,
name|String
name|rootId
parameter_list|)
block|{
try|try
block|{
name|appendAsEJBQL
argument_list|(
name|pw
argument_list|,
name|rootId
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unexpected IO exception appending to PrintWriter"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * @since 4.0      */
specifier|public
name|void
name|appendAsEJBQL
parameter_list|(
name|Appendable
name|out
parameter_list|,
name|String
name|rootId
parameter_list|)
throws|throws
name|IOException
block|{
name|appendAsEJBQL
argument_list|(
literal|null
argument_list|,
name|out
argument_list|,
name|rootId
argument_list|)
expr_stmt|;
block|}
comment|/**          * @since 4.0          */
annotation|@
name|Override
specifier|public
name|void
name|appendAsEJBQL
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|parameterAccumulator
parameter_list|,
name|Appendable
name|out
parameter_list|,
name|String
name|rootId
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|parent
operator|!=
literal|null
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
if|if
condition|(
operator|(
name|children
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|children
operator|.
name|length
operator|>
literal|0
operator|)
condition|)
block|{
name|appendChildrenAsEJBQL
argument_list|(
name|parameterAccumulator
argument_list|,
name|out
argument_list|,
name|rootId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parent
operator|!=
literal|null
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
block|}
comment|/**      * Encodes child of this node with specified index to EJBQL      */
specifier|protected
name|void
name|appendChildrenAsEJBQL
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|parameterAccumulator
parameter_list|,
name|Appendable
name|out
parameter_list|,
name|String
name|rootId
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|children
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|getEJBQLExpressionOperator
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|children
index|[
name|i
index|]
operator|==
literal|null
condition|)
block|{
name|out
operator|.
name|append
argument_list|(
literal|"null"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
operator|(
operator|(
name|SimpleNode
operator|)
name|children
index|[
name|i
index|]
operator|)
operator|.
name|appendAsEJBQL
argument_list|(
name|parameterAccumulator
argument_list|,
name|out
argument_list|,
name|rootId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

