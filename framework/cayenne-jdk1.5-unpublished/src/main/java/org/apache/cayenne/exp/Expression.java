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
name|exp
package|;
end_package

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
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|exp
operator|.
name|parser
operator|.
name|ASTScalar
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
name|ExpressionParser
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
name|ExpressionParserTokenManager
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
name|JavaCharStream
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
name|ParseException
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
name|ConversionUtil
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
name|XMLEncoder
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
name|XMLSerializable
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
comment|/**  * Superclass of Cayenne expressions that defines basic API for expressions use.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|Expression
implements|implements
name|Serializable
implements|,
name|XMLSerializable
block|{
comment|/**      * A value that a Transformer might return to indicate that a node has to be pruned      * from the expression during the transformation.      *       * @since 1.2      */
specifier|public
specifier|final
specifier|static
name|Object
name|PRUNED_NODE
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|AND
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|OR
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NOT
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|EQUAL_TO
init|=
literal|3
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NOT_EQUAL_TO
init|=
literal|4
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|LESS_THAN
init|=
literal|5
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|GREATER_THAN
init|=
literal|6
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|LESS_THAN_EQUAL_TO
init|=
literal|7
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|GREATER_THAN_EQUAL_TO
init|=
literal|8
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|BETWEEN
init|=
literal|9
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|IN
init|=
literal|10
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|LIKE
init|=
literal|11
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|LIKE_IGNORE_CASE
init|=
literal|12
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ADD
init|=
literal|16
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|SUBTRACT
init|=
literal|17
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|MULTIPLY
init|=
literal|18
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|DIVIDE
init|=
literal|19
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NEGATIVE
init|=
literal|20
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|TRUE
init|=
literal|21
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|FALSE
init|=
literal|22
decl_stmt|;
comment|/**      * Expression describes a path relative to an ObjEntity. OBJ_PATH expression is      * resolved relative to some root ObjEntity. Path expression components are separated      * by "." (dot). Path can point to either one of these:      *<ul>      *<li><i>An attribute of root ObjEntity.</i> For entity Gallery OBJ_PATH expression      * "galleryName" will point to ObjAttribute "galleryName"      *<li><i>Another ObjEntity related to root ObjEntity via a chain of relationships.</i>      * For entity Gallery OBJ_PATH expression "paintingArray.toArtist" will point to      * ObjEntity "Artist"      *<li><i>ObjAttribute of another ObjEntity related to root ObjEntity via a chain of      * relationships.</i> For entity Gallery OBJ_PATH expression      * "paintingArray.toArtist.artistName" will point to ObjAttribute "artistName"      *</ul>      */
specifier|public
specifier|static
specifier|final
name|int
name|OBJ_PATH
init|=
literal|26
decl_stmt|;
comment|/**      * Expression describes a path relative to a DbEntity. DB_PATH expression is resolved      * relative to some root DbEntity. Path expression components are separated by "."      * (dot). Path can point to either one of these:      *<ul>      *<li><i>An attribute of root DbEntity.</i> For entity GALLERY, DB_PATH expression      * "GALLERY_NAME" will point to a DbAttribute "GALLERY_NAME".</li>      *<li><i>Another DbEntity related to root DbEntity via a chain of relationships.</i>      * For entity GALLERY DB_PATH expression "paintingArray.toArtist" will point to      * DbEntity "ARTIST".</li>      *<li><i>DbAttribute of another ObjEntity related to root DbEntity via a chain of      * relationships.</i> For entity GALLERY DB_PATH expression      * "paintingArray.toArtist.ARTIST_NAME" will point to DbAttribute "ARTIST_NAME".</li>      *</ul>      */
specifier|public
specifier|static
specifier|final
name|int
name|DB_PATH
init|=
literal|27
decl_stmt|;
comment|/**      * Interpreted as a comma-separated list of literals.      */
specifier|public
specifier|static
specifier|final
name|int
name|LIST
init|=
literal|28
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NOT_BETWEEN
init|=
literal|35
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NOT_IN
init|=
literal|36
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NOT_LIKE
init|=
literal|37
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NOT_LIKE_IGNORE_CASE
init|=
literal|38
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|PARSE_BUFFER_MAX_SIZE
init|=
literal|4096
decl_stmt|;
specifier|protected
name|int
name|type
decl_stmt|;
comment|/**      * Parses string, converting it to Expression. If string does not represent a      * semantically correct expression, an ExpressionException is thrown.      *       * @since 1.1      */
comment|// TODO: cache expression strings, since this operation is pretty slow
specifier|public
specifier|static
name|Expression
name|fromString
parameter_list|(
name|String
name|expressionString
parameter_list|)
block|{
if|if
condition|(
name|expressionString
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null expression string."
argument_list|)
throw|;
block|}
comment|// optimizing parser buffers per CAY-1667...
name|int
name|bufferSize
init|=
name|expressionString
operator|.
name|length
argument_list|()
operator|>
name|PARSE_BUFFER_MAX_SIZE
condition|?
name|PARSE_BUFFER_MAX_SIZE
else|:
name|expressionString
operator|.
name|length
argument_list|()
decl_stmt|;
name|Reader
name|reader
init|=
operator|new
name|StringReader
argument_list|(
name|expressionString
argument_list|)
decl_stmt|;
name|JavaCharStream
name|stream
init|=
operator|new
name|JavaCharStream
argument_list|(
name|reader
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
name|bufferSize
argument_list|)
decl_stmt|;
name|ExpressionParserTokenManager
name|tm
init|=
operator|new
name|ExpressionParserTokenManager
argument_list|(
name|stream
argument_list|)
decl_stmt|;
name|ExpressionParser
name|parser
init|=
operator|new
name|ExpressionParser
argument_list|(
name|tm
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|parser
operator|.
name|expression
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ExpressionException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
comment|// another common error is TokenManagerError
throw|throw
operator|new
name|ExpressionException
argument_list|(
name|th
operator|.
name|getMessage
argument_list|()
argument_list|,
name|th
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns a map of path aliases for this expression. It returns a non-empty map only      * if this is a path expression and the aliases are known at the expression creation      * time. Otherwise an empty map is returned.      *       * @since 3.0      */
specifier|public
specifier|abstract
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getPathAliases
parameter_list|()
function_decl|;
comment|/**      * Returns String label for this expression. Used for debugging.      */
specifier|public
name|String
name|expName
parameter_list|()
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
name|NOT
case|:
return|return
literal|"NOT"
return|;
case|case
name|EQUAL_TO
case|:
return|return
literal|"="
return|;
case|case
name|NOT_EQUAL_TO
case|:
return|return
literal|"<>"
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
name|BETWEEN
case|:
return|return
literal|"BETWEEN"
return|;
case|case
name|IN
case|:
return|return
literal|"IN"
return|;
case|case
name|LIKE
case|:
return|return
literal|"LIKE"
return|;
case|case
name|LIKE_IGNORE_CASE
case|:
return|return
literal|"LIKE_IGNORE_CASE"
return|;
case|case
name|OBJ_PATH
case|:
return|return
literal|"OBJ_PATH"
return|;
case|case
name|DB_PATH
case|:
return|return
literal|"DB_PATH"
return|;
case|case
name|LIST
case|:
return|return
literal|"LIST"
return|;
case|case
name|NOT_BETWEEN
case|:
return|return
literal|"NOT BETWEEN"
return|;
case|case
name|NOT_IN
case|:
return|return
literal|"NOT IN"
return|;
case|case
name|NOT_LIKE
case|:
return|return
literal|"NOT LIKE"
return|;
case|case
name|NOT_LIKE_IGNORE_CASE
case|:
return|return
literal|"NOT LIKE IGNORE CASE"
return|;
default|default:
return|return
literal|"other"
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|Expression
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Expression
name|e
init|=
operator|(
name|Expression
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getType
argument_list|()
operator|!=
name|getType
argument_list|()
operator|||
name|e
operator|.
name|getOperandCount
argument_list|()
operator|!=
name|getOperandCount
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// compare operands
name|int
name|len
init|=
name|e
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
name|len
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|e
operator|.
name|getOperand
argument_list|(
name|i
argument_list|)
argument_list|,
name|getOperand
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Returns a type of expression. Most common types are defined as public static fields      * of this interface.      */
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|int
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * A shortcut for<code>expWithParams(params, true)</code>.      */
specifier|public
name|Expression
name|expWithParameters
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
return|return
name|expWithParameters
argument_list|(
name|parameters
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Creates and returns a new Expression instance using this expression as a prototype.      * All ExpressionParam operands are substituted with the values in the      *<code>params</code> map.      *<p>      *<i>Null values in the<code>params</code> map should be explicitly created in the      * map for the corresponding key.</i>      *</p>      *       * @param parameters a map of parameters, with each key being a string name of an      *            expression parameter, and value being the value that should be used in      *            the final expression.      * @param pruneMissing If<code>true</code>, subexpressions that rely on missing      *            parameters will be pruned from the resulting tree. If<code>false</code>,      *            any missing values will generate an exception.      * @return Expression resulting from the substitution of parameters with real values,      *         or null if the whole expression was pruned, due to the missing parameters.      */
specifier|public
name|Expression
name|expWithParameters
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|parameters
parameter_list|,
specifier|final
name|boolean
name|pruneMissing
parameter_list|)
block|{
comment|// create transformer for named parameters
name|Transformer
name|transformer
init|=
operator|new
name|Transformer
argument_list|()
block|{
specifier|public
name|Object
name|transform
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|ExpressionParameter
operator|)
condition|)
block|{
return|return
name|object
return|;
block|}
name|String
name|name
init|=
operator|(
operator|(
name|ExpressionParameter
operator|)
name|object
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|parameters
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
name|pruneMissing
condition|)
block|{
return|return
name|PRUNED_NODE
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|ExpressionException
argument_list|(
literal|"Missing required parameter: $"
operator|+
name|name
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|Object
name|value
init|=
name|parameters
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// wrap lists (for now); also support null parameters
comment|// TODO: andrus 8/14/2007 - shouldn't we also wrap non-null object
comment|// values in ASTScalars?
return|return
operator|(
name|value
operator|!=
literal|null
operator|)
condition|?
name|ExpressionFactory
operator|.
name|wrapPathOperand
argument_list|(
name|value
argument_list|)
else|:
operator|new
name|ASTScalar
argument_list|(
literal|null
argument_list|)
return|;
block|}
block|}
block|}
decl_stmt|;
return|return
name|transform
argument_list|(
name|transformer
argument_list|)
return|;
block|}
comment|/**      * Creates a new expression that joins this object with another expression, using      * specified join type. It is very useful for incrementally building chained      * expressions, like long AND or OR statements.      */
specifier|public
name|Expression
name|joinExp
parameter_list|(
name|int
name|type
parameter_list|,
name|Expression
name|exp
parameter_list|)
block|{
name|Expression
name|join
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|join
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|join
operator|.
name|setOperand
argument_list|(
literal|1
argument_list|,
name|exp
argument_list|)
expr_stmt|;
name|join
operator|.
name|flattenTree
argument_list|()
expr_stmt|;
return|return
name|join
return|;
block|}
comment|/**      * Chains this expression with another expression using "and".      */
specifier|public
name|Expression
name|andExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
name|joinExp
argument_list|(
name|Expression
operator|.
name|AND
argument_list|,
name|exp
argument_list|)
return|;
block|}
comment|/**      * Chains this expression with another expression using "or".      */
specifier|public
name|Expression
name|orExp
parameter_list|(
name|Expression
name|exp
parameter_list|)
block|{
return|return
name|joinExp
argument_list|(
name|Expression
operator|.
name|OR
argument_list|,
name|exp
argument_list|)
return|;
block|}
comment|/**      * Returns a logical NOT of current expression.      *       * @since 1.0.6      */
specifier|public
specifier|abstract
name|Expression
name|notExp
parameter_list|()
function_decl|;
comment|/**      * Returns a count of operands of this expression. In real life there are unary (count ==      * 1), binary (count == 2) and ternary (count == 3) expressions.      */
specifier|public
specifier|abstract
name|int
name|getOperandCount
parameter_list|()
function_decl|;
comment|/**      * Returns a value of operand at<code>index</code>. Operand indexing starts at 0.      */
specifier|public
specifier|abstract
name|Object
name|getOperand
parameter_list|(
name|int
name|index
parameter_list|)
function_decl|;
comment|/**      * Sets a value of operand at<code>index</code>. Operand indexing starts at 0.      */
specifier|public
specifier|abstract
name|void
name|setOperand
parameter_list|(
name|int
name|index
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**      * Calculates expression value with object as a context for path expressions.      *       * @since 1.1      */
specifier|public
specifier|abstract
name|Object
name|evaluate
parameter_list|(
name|Object
name|o
parameter_list|)
function_decl|;
comment|/**      * Calculates expression boolean value with object as a context for path expressions.      *       * @since 1.1      */
specifier|public
name|boolean
name|match
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|ConversionUtil
operator|.
name|toBoolean
argument_list|(
name|evaluate
argument_list|(
name|o
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns a list of objects that match the expression.      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|filterObjects
parameter_list|(
name|List
argument_list|<
name|T
argument_list|>
name|objects
parameter_list|)
block|{
if|if
condition|(
name|objects
operator|==
literal|null
operator|||
name|objects
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
return|return
operator|(
name|List
argument_list|<
name|T
argument_list|>
operator|)
name|filter
argument_list|(
name|objects
argument_list|,
operator|new
name|LinkedList
argument_list|<
name|T
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Adds objects matching this expression from the source collection to the target      * collection.      *       * @since 1.1      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Collection
argument_list|<
name|?
argument_list|>
name|filter
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|source
parameter_list|,
name|Collection
argument_list|<
name|T
argument_list|>
name|target
parameter_list|)
block|{
for|for
control|(
name|T
name|o
range|:
name|source
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|o
argument_list|)
condition|)
block|{
name|target
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|target
return|;
block|}
comment|/**      * Clones this expression.      *       * @since 1.1      */
specifier|public
name|Expression
name|deepCopy
parameter_list|()
block|{
return|return
name|transform
argument_list|(
literal|null
argument_list|)
return|;
block|}
comment|/**      * Creates a copy of this expression node, without copying children.      *       * @since 1.1      */
specifier|public
specifier|abstract
name|Expression
name|shallowCopy
parameter_list|()
function_decl|;
comment|/**      * Returns true if this node should be pruned from expression tree in the event a      * child is removed.      *       * @since 1.1      */
specifier|protected
specifier|abstract
name|boolean
name|pruneNodeForPrunedChild
parameter_list|(
name|Object
name|prunedChild
parameter_list|)
function_decl|;
comment|/**      * Restructures expression to make sure that there are no children of the same type as      * this expression.      *       * @since 1.1      */
specifier|protected
specifier|abstract
name|void
name|flattenTree
parameter_list|()
function_decl|;
comment|/**      * Traverses itself and child expressions, notifying visitor via callback methods as      * it goes. This is an Expression-specific implementation of the "Visitor" design      * pattern.      *       * @since 1.1      */
specifier|public
name|void
name|traverse
parameter_list|(
name|TraversalHandler
name|visitor
parameter_list|)
block|{
if|if
condition|(
name|visitor
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null Visitor."
argument_list|)
throw|;
block|}
name|traverse
argument_list|(
literal|null
argument_list|,
name|visitor
argument_list|)
expr_stmt|;
block|}
comment|/**      * Traverses itself and child expressions, notifying visitor via callback methods as      * it goes.      *       * @since 1.1      */
specifier|protected
name|void
name|traverse
parameter_list|(
name|Expression
name|parentExp
parameter_list|,
name|TraversalHandler
name|visitor
parameter_list|)
block|{
name|visitor
operator|.
name|startNode
argument_list|(
name|this
argument_list|,
name|parentExp
argument_list|)
expr_stmt|;
comment|// recursively traverse each child
name|int
name|count
init|=
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
name|count
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|child
init|=
name|getOperand
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|instanceof
name|Expression
condition|)
block|{
name|Expression
name|childExp
init|=
operator|(
name|Expression
operator|)
name|child
decl_stmt|;
name|childExp
operator|.
name|traverse
argument_list|(
name|this
argument_list|,
name|visitor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|visitor
operator|.
name|objectNode
argument_list|(
name|child
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
name|visitor
operator|.
name|finishedChild
argument_list|(
name|this
argument_list|,
name|i
argument_list|,
name|i
operator|<
name|count
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|visitor
operator|.
name|endNode
argument_list|(
name|this
argument_list|,
name|parentExp
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a transformed copy of this expression, applying transformation provided by      * Transformer to all its nodes. Null transformer will result in an identical deep      * copy of this expression.      *<p>      * To force a node and its children to be pruned from the copy, Transformer should      * return Expression.PRUNED_NODE. Otherwise an expectation is that if a node is an      * Expression it must be transformed to null or another Expression. Any other object      * type would result in a ExpressionException.      *       * @since 1.1      */
specifier|public
name|Expression
name|transform
parameter_list|(
name|Transformer
name|transformer
parameter_list|)
block|{
name|Object
name|transformed
init|=
name|transformExpression
argument_list|(
name|transformer
argument_list|)
decl_stmt|;
if|if
condition|(
name|transformed
operator|==
name|PRUNED_NODE
operator|||
name|transformed
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|else if
condition|(
name|transformed
operator|instanceof
name|Expression
condition|)
block|{
return|return
operator|(
name|Expression
operator|)
name|transformed
return|;
block|}
throw|throw
operator|new
name|ExpressionException
argument_list|(
literal|"Invalid transformed expression: "
operator|+
name|transformed
argument_list|)
throw|;
block|}
comment|/**      * A recursive method called from "transform" to do the actual transformation.      *       * @return null, Expression.PRUNED_NODE or transformed expression.      * @since 1.2      */
specifier|protected
name|Object
name|transformExpression
parameter_list|(
name|Transformer
name|transformer
parameter_list|)
block|{
name|Expression
name|copy
init|=
name|shallowCopy
argument_list|()
decl_stmt|;
name|int
name|count
init|=
name|getOperandCount
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|j
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
name|Object
name|operand
init|=
name|getOperand
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Object
name|transformedChild
decl_stmt|;
if|if
condition|(
name|operand
operator|instanceof
name|Expression
condition|)
block|{
name|transformedChild
operator|=
operator|(
operator|(
name|Expression
operator|)
name|operand
operator|)
operator|.
name|transformExpression
argument_list|(
name|transformer
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|transformer
operator|!=
literal|null
condition|)
block|{
name|transformedChild
operator|=
name|transformer
operator|.
name|transform
argument_list|(
name|operand
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|transformedChild
operator|=
name|operand
expr_stmt|;
block|}
comment|// prune null children only if there is a transformer and it indicated so
name|boolean
name|prune
init|=
name|transformer
operator|!=
literal|null
operator|&&
name|transformedChild
operator|==
name|PRUNED_NODE
decl_stmt|;
if|if
condition|(
operator|!
name|prune
condition|)
block|{
name|copy
operator|.
name|setOperand
argument_list|(
name|j
argument_list|,
name|transformedChild
argument_list|)
expr_stmt|;
name|j
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|prune
operator|&&
name|pruneNodeForPrunedChild
argument_list|(
name|operand
argument_list|)
condition|)
block|{
comment|// bail out early...
return|return
name|PRUNED_NODE
return|;
block|}
block|}
comment|// all the children are processed, only now transform this copy
return|return
operator|(
name|transformer
operator|!=
literal|null
operator|)
condition|?
operator|(
name|Expression
operator|)
name|transformer
operator|.
name|transform
argument_list|(
name|copy
argument_list|)
else|:
name|copy
return|;
block|}
comment|/**      * Encodes itself, wrapping the string into XML CDATA section.      *       * @since 1.1      */
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<![CDATA["
argument_list|)
expr_stmt|;
name|encodeAsString
argument_list|(
name|encoder
operator|.
name|getPrintWriter
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|"]]>"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Stores a String representation of Expression using a provided PrintWriter.      *       * @since 1.1      */
specifier|public
specifier|abstract
name|void
name|encodeAsString
parameter_list|(
name|PrintWriter
name|pw
parameter_list|)
function_decl|;
comment|/**      * Stores a String representation of Expression as EJBQL using a provided PrintWriter.      * DB path expressions produce non-standard EJBQL path expressions.      *       * @since 3.0      */
specifier|public
specifier|abstract
name|void
name|encodeAsEJBQL
parameter_list|(
name|PrintWriter
name|pw
parameter_list|,
name|String
name|rootId
parameter_list|)
function_decl|;
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|encodeAsString
argument_list|(
name|pw
argument_list|)
expr_stmt|;
name|pw
operator|.
name|close
argument_list|()
expr_stmt|;
name|buffer
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|String
name|toEJBQL
parameter_list|(
name|String
name|rootId
parameter_list|)
block|{
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|encodeAsEJBQL
argument_list|(
name|pw
argument_list|,
name|rootId
argument_list|)
expr_stmt|;
name|pw
operator|.
name|close
argument_list|()
expr_stmt|;
name|buffer
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

