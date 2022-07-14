begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|gen
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
name|exp
operator|.
name|ExpressionParameter
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
name|ASTList
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
name|map
operator|.
name|QueryDescriptor
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
name|SelectQueryDescriptor
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
name|Ordering
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
name|LinkedHashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|Matcher
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

begin_comment
comment|/**  * Attributes and Methods for working with Queries.  *  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|DataMapUtils
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|queriesMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/** 	 * Return valid method name based on query name (replace all illegal 	 * characters with underscore '_'). 	 *  	 * @param query descriptor 	 * @return Method name that perform query. 	 */
specifier|public
name|String
name|getQueryMethodName
parameter_list|(
name|QueryDescriptor
name|query
parameter_list|)
block|{
return|return
name|Util
operator|.
name|underscoredToJava
argument_list|(
name|query
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/** 	 * Get all parameter names that used in query qualifier. 	 * 	 * @param query select query descriptor 	 * @return Parameter names. 	 */
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getParameterNames
parameter_list|(
name|SelectQueryDescriptor
name|query
parameter_list|)
block|{
if|if
condition|(
name|query
operator|.
name|getQualifier
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptySet
argument_list|()
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParameters
init|=
name|queriesMap
operator|.
name|get
argument_list|(
name|query
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|queryParameters
operator|==
literal|null
condition|)
block|{
name|queryParameters
operator|=
name|getParameterNames
argument_list|(
name|query
operator|.
name|getQualifier
argument_list|()
argument_list|,
name|query
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|queriesMap
operator|.
name|put
argument_list|(
name|query
operator|.
name|getName
argument_list|()
argument_list|,
name|queryParameters
argument_list|)
expr_stmt|;
block|}
return|return
name|parseQualifier
argument_list|(
name|query
operator|.
name|getQualifier
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Boolean
name|isValidParameterNames
parameter_list|(
name|SelectQueryDescriptor
name|query
parameter_list|)
block|{
if|if
condition|(
name|query
operator|.
name|getQualifier
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParameters
init|=
name|queriesMap
operator|.
name|get
argument_list|(
name|query
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|queryParameters
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|queryParameters
operator|=
name|getParameterNames
argument_list|(
name|query
operator|.
name|getQualifier
argument_list|()
argument_list|,
name|query
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// if we have wrong path in queryParameters return false.
return|return
literal|false
return|;
block|}
block|}
for|for
control|(
name|Ordering
name|ordering
range|:
name|query
operator|.
name|getOrderings
argument_list|()
control|)
block|{
comment|// validate paths in ordering
name|String
name|path
init|=
name|ordering
operator|.
name|getSortSpecString
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|it
init|=
operator|(
operator|(
name|ObjEntity
operator|)
name|query
operator|.
name|getRoot
argument_list|()
operator|)
operator|.
name|resolvePathComponents
argument_list|(
name|path
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
try|try
block|{
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|e
parameter_list|)
block|{
comment|// if we have wrong path in orderings return false.
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/** 	 * Get list of parameter names in the same order as in qualifier. 	 *  	 * @param qualifierString 	 *            to be parsed 	 * @return List of parameter names. 	 */
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|parseQualifier
parameter_list|(
name|String
name|qualifierString
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\$[\\w]+"
argument_list|)
decl_stmt|;
name|Matcher
name|matcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|qualifierString
argument_list|)
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|name
init|=
name|matcher
operator|.
name|group
argument_list|()
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
name|Util
operator|.
name|underscoredToJava
argument_list|(
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|boolean
name|hasParameters
parameter_list|(
name|SelectQueryDescriptor
name|query
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParameters
init|=
name|queriesMap
operator|.
name|get
argument_list|(
name|query
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|queryParameters
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|queryParameters
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
return|;
block|}
comment|/** 	 * Get type of parameter for given name. 	 * 	 * @param query descriptor 	 * @param name parameter name 	 * @return Parameter type. 	 */
specifier|public
name|String
name|getParameterType
parameter_list|(
name|SelectQueryDescriptor
name|query
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|queriesMap
operator|.
name|get
argument_list|(
name|query
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getParameterNames
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|Object
name|root
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|types
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|typeName
init|=
literal|""
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|LinkedList
argument_list|<>
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
name|expression
operator|.
name|getOperandCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|operand
init|=
name|expression
operator|.
name|getOperand
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|operand
operator|instanceof
name|Expression
condition|)
block|{
name|types
operator|.
name|putAll
argument_list|(
name|getParameterNames
argument_list|(
operator|(
name|Expression
operator|)
name|operand
argument_list|,
name|root
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|operand
operator|instanceof
name|ASTObjPath
condition|)
block|{
name|PathComponent
argument_list|<
name|ObjAttribute
argument_list|,
name|ObjRelationship
argument_list|>
name|component
init|=
operator|(
operator|(
name|ObjEntity
operator|)
name|root
operator|)
operator|.
name|lastPathComponent
argument_list|(
operator|(
name|ASTObjPath
operator|)
name|operand
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
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
name|attribute
operator|!=
literal|null
condition|)
block|{
name|typeName
operator|=
name|attribute
operator|.
name|getType
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ObjRelationship
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
name|typeName
operator|=
name|relationship
operator|.
name|getTargetEntity
argument_list|()
operator|.
name|getClassName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|typeName
operator|=
literal|"Object"
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|operand
operator|instanceof
name|ASTList
condition|)
block|{
name|Object
index|[]
name|values
init|=
operator|(
name|Object
index|[]
operator|)
operator|(
operator|(
name|ASTList
operator|)
name|operand
operator|)
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|value
range|:
name|values
control|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|ExpressionParameter
condition|)
block|{
name|names
operator|.
name|add
argument_list|(
operator|(
operator|(
name|ExpressionParameter
operator|)
name|value
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|operand
operator|instanceof
name|ExpressionParameter
condition|)
block|{
name|names
operator|.
name|add
argument_list|(
operator|(
operator|(
name|ExpressionParameter
operator|)
name|operand
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|types
operator|.
name|put
argument_list|(
name|Util
operator|.
name|underscoredToJava
argument_list|(
name|name
argument_list|,
literal|false
argument_list|)
argument_list|,
name|typeName
argument_list|)
expr_stmt|;
block|}
return|return
name|types
return|;
block|}
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
block|}
end_class

end_unit

