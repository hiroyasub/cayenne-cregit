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
name|wocompat
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
name|ExpressionFactory
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
name|Entity
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
name|query
operator|.
name|SortOrder
import|;
end_import

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
comment|/**  * A descriptor of SelectQuery loaded from EOModel. It is an informal  * "decorator" of Cayenne SelectQuery to provide access to the extra information  * of WebObjects EOFetchSpecification.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|EOQuery
parameter_list|<
name|T
parameter_list|>
extends|extends
name|SelectQuery
argument_list|<
name|T
argument_list|>
block|{
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|plistMap
decl_stmt|;
specifier|protected
name|Map
name|bindings
decl_stmt|;
specifier|public
name|EOQuery
parameter_list|(
name|ObjEntity
name|root
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|plistMap
parameter_list|)
block|{
name|super
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|this
operator|.
name|plistMap
operator|=
name|plistMap
expr_stmt|;
name|initFromPlist
argument_list|(
name|plistMap
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|initFromPlist
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|plistMap
parameter_list|)
block|{
name|setDistinct
argument_list|(
literal|"YES"
operator|.
name|equalsIgnoreCase
argument_list|(
operator|(
name|String
operator|)
name|plistMap
operator|.
name|get
argument_list|(
literal|"usesDistinct"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|fetchLimit
init|=
name|plistMap
operator|.
name|get
argument_list|(
literal|"fetchLimit"
argument_list|)
decl_stmt|;
if|if
condition|(
name|fetchLimit
operator|!=
literal|null
condition|)
block|{
try|try
block|{
if|if
condition|(
name|fetchLimit
operator|instanceof
name|Number
condition|)
block|{
name|setFetchLimit
argument_list|(
operator|(
operator|(
name|Number
operator|)
name|fetchLimit
operator|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setFetchLimit
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|fetchLimit
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfex
parameter_list|)
block|{
comment|// ignoring...
block|}
block|}
comment|// sort orderings
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|orderings
init|=
operator|(
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
operator|)
name|plistMap
operator|.
name|get
argument_list|(
literal|"sortOrderings"
argument_list|)
decl_stmt|;
if|if
condition|(
name|orderings
operator|!=
literal|null
operator|&&
operator|!
name|orderings
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ordering
range|:
name|orderings
control|)
block|{
name|boolean
name|asc
init|=
operator|!
literal|"compareDescending:"
operator|.
name|equals
argument_list|(
name|ordering
operator|.
name|get
argument_list|(
literal|"selectorName"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|key
init|=
name|ordering
operator|.
name|get
argument_list|(
literal|"key"
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|addOrdering
argument_list|(
name|key
argument_list|,
name|asc
condition|?
name|SortOrder
operator|.
name|ASCENDING
else|:
name|SortOrder
operator|.
name|DESCENDING
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// qualifiers
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|qualifierMap
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
operator|)
name|plistMap
operator|.
name|get
argument_list|(
literal|"qualifier"
argument_list|)
decl_stmt|;
if|if
condition|(
name|qualifierMap
operator|!=
literal|null
operator|&&
operator|!
name|qualifierMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|setQualifier
argument_list|(
name|makeQualifier
argument_list|(
name|qualifierMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// prefetches
name|List
name|prefetches
init|=
operator|(
name|List
operator|)
name|plistMap
operator|.
name|get
argument_list|(
literal|"prefetchingRelationshipKeyPaths"
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefetches
operator|!=
literal|null
operator|&&
operator|!
name|prefetches
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Iterator
name|it
init|=
name|prefetches
operator|.
name|iterator
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
name|addPrefetch
argument_list|(
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// data rows - note that we do not support fetching individual columns
comment|// in the
comment|// modeler...
if|if
condition|(
name|plistMap
operator|.
name|containsKey
argument_list|(
literal|"rawRowKeyPaths"
argument_list|)
condition|)
block|{
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Collection
name|getBindingNames
parameter_list|()
block|{
if|if
condition|(
name|bindings
operator|==
literal|null
condition|)
block|{
name|initBindings
argument_list|()
expr_stmt|;
block|}
return|return
name|bindings
operator|.
name|keySet
argument_list|()
return|;
block|}
specifier|public
name|String
name|bindingClass
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|bindings
operator|==
literal|null
condition|)
block|{
name|initBindings
argument_list|()
expr_stmt|;
block|}
return|return
operator|(
name|String
operator|)
name|bindings
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|private
specifier|synchronized
name|void
name|initBindings
parameter_list|()
block|{
if|if
condition|(
name|bindings
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
name|bindings
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|root
operator|instanceof
name|Entity
operator|)
condition|)
block|{
return|return;
block|}
name|Map
name|qualifier
init|=
operator|(
name|Map
operator|)
name|plistMap
operator|.
name|get
argument_list|(
literal|"qualifier"
argument_list|)
decl_stmt|;
name|initBindings
argument_list|(
name|bindings
argument_list|,
operator|(
name|Entity
operator|)
name|root
argument_list|,
name|qualifier
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initBindings
parameter_list|(
name|Map
name|bindings
parameter_list|,
name|Entity
name|entity
parameter_list|,
name|Map
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
return|return;
block|}
if|if
condition|(
literal|"EOKeyValueQualifier"
operator|.
name|equals
argument_list|(
name|qualifier
operator|.
name|get
argument_list|(
literal|"class"
argument_list|)
argument_list|)
condition|)
block|{
name|String
name|key
init|=
operator|(
name|String
operator|)
name|qualifier
operator|.
name|get
argument_list|(
literal|"key"
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Object
name|value
init|=
name|qualifier
operator|.
name|get
argument_list|(
literal|"value"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|value
operator|instanceof
name|Map
operator|)
condition|)
block|{
return|return;
block|}
name|Map
name|valueMap
init|=
operator|(
name|Map
operator|)
name|value
decl_stmt|;
if|if
condition|(
operator|!
literal|"EOQualifierVariable"
operator|.
name|equals
argument_list|(
name|valueMap
operator|.
name|get
argument_list|(
literal|"class"
argument_list|)
argument_list|)
operator|||
operator|!
name|valueMap
operator|.
name|containsKey
argument_list|(
literal|"_key"
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|name
init|=
operator|(
name|String
operator|)
name|valueMap
operator|.
name|get
argument_list|(
literal|"_key"
argument_list|)
decl_stmt|;
name|String
name|className
init|=
literal|null
decl_stmt|;
comment|// we don't know whether its obj path or db path, so the expression
comment|// can blow
comment|// ... in fact we can't support DB Path as the key is different from
comment|// external
comment|// name,
comment|// so we will use Object type for all DB path...
try|try
block|{
name|Object
name|lastObject
init|=
operator|new
name|ASTObjPath
argument_list|(
name|key
argument_list|)
operator|.
name|evaluate
argument_list|(
name|entity
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastObject
operator|instanceof
name|ObjAttribute
condition|)
block|{
name|className
operator|=
operator|(
operator|(
name|ObjAttribute
operator|)
name|lastObject
operator|)
operator|.
name|getType
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|lastObject
operator|instanceof
name|ObjRelationship
condition|)
block|{
name|ObjEntity
name|target
init|=
operator|(
operator|(
name|ObjRelationship
operator|)
name|lastObject
operator|)
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
name|className
operator|=
name|target
operator|.
name|getClassName
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|ex
parameter_list|)
block|{
name|className
operator|=
literal|"java.lang.Object"
expr_stmt|;
block|}
if|if
condition|(
name|className
operator|==
literal|null
condition|)
block|{
name|className
operator|=
literal|"java.lang.Object"
expr_stmt|;
block|}
name|bindings
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|className
argument_list|)
expr_stmt|;
return|return;
block|}
name|List
name|children
init|=
operator|(
name|List
operator|)
name|qualifier
operator|.
name|get
argument_list|(
literal|"qualifiers"
argument_list|)
decl_stmt|;
if|if
condition|(
name|children
operator|!=
literal|null
condition|)
block|{
name|Iterator
name|it
init|=
name|children
operator|.
name|iterator
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
name|initBindings
argument_list|(
name|bindings
argument_list|,
name|entity
argument_list|,
operator|(
name|Map
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** 	 * Creates the Expression equivalent of the EOFetchSpecification represented 	 * by the Map. 	 *  	 * @param qualifierMap 	 *            - FetchSpecification to translate 	 * @return Expression equivalent to FetchSpecification 	 */
specifier|public
specifier|synchronized
name|Expression
name|makeQualifier
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|qualifierMap
parameter_list|)
block|{
if|if
condition|(
name|qualifierMap
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|EOFetchSpecificationParser
operator|.
name|makeQualifier
argument_list|(
operator|(
name|EOObjEntity
operator|)
name|getRoot
argument_list|()
argument_list|,
name|qualifierMap
argument_list|)
return|;
block|}
comment|/** 	 * EOFetchSpecificationParser parses EOFetchSpecifications from a 	 * WebObjects-style EOModel. It recursively builds Cayenne Expression 	 * objects and assembles them into the final aggregate Expression. 	 */
specifier|static
class|class
name|EOFetchSpecificationParser
block|{
comment|// Xcode/EOModeler expressions have a colon at the end of the selector
comment|// name
comment|// (just like standard Objective-C syntax). WOLips does not. Add both
comment|// sets to the hash map to handle both types of models.
comment|// Selector strings (Java-base).
specifier|static
specifier|final
name|String
name|IS_EQUAL_TO
init|=
literal|"isEqualTo"
decl_stmt|;
specifier|static
specifier|final
name|String
name|IS_NOT_EQUAL_TO
init|=
literal|"isNotEqualTo"
decl_stmt|;
specifier|static
specifier|final
name|String
name|IS_LIKE
init|=
literal|"isLike"
decl_stmt|;
specifier|static
specifier|final
name|String
name|CASE_INSENSITIVE_LIKE
init|=
literal|"isCaseInsensitiveLike"
decl_stmt|;
specifier|static
specifier|final
name|String
name|IS_LESS_THAN
init|=
literal|"isLessThan"
decl_stmt|;
specifier|static
specifier|final
name|String
name|IS_LESS_THAN_OR_EQUAL_TO
init|=
literal|"isLessThanOrEqualTo"
decl_stmt|;
specifier|static
specifier|final
name|String
name|IS_GREATER_THAN
init|=
literal|"isGreaterThan"
decl_stmt|;
specifier|static
specifier|final
name|String
name|IS_GREATER_THAN_OR_EQUAL_TO
init|=
literal|"isGreaterThanOrEqualTo"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|OBJ_C
init|=
literal|":"
decl_stmt|;
comment|// Objective-C syntax addition.
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|selectorToExpressionBridge
decl_stmt|;
comment|/** 		 * selectorToExpressionBridge is just a mapping of EOModeler's selector 		 * types to Cayenne Expression types. 		 *  		 * @return HashMap of Expression types, keyed by the corresponding 		 *         selector name 		 */
specifier|static
specifier|synchronized
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|selectorToExpressionBridge
parameter_list|()
block|{
comment|// Initialize selectorToExpressionBridge if needed.
if|if
condition|(
literal|null
operator|==
name|selectorToExpressionBridge
condition|)
block|{
name|selectorToExpressionBridge
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|IS_EQUAL_TO
argument_list|,
name|Expression
operator|.
name|EQUAL_TO
argument_list|)
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|IS_EQUAL_TO
operator|+
name|OBJ_C
argument_list|,
name|Expression
operator|.
name|EQUAL_TO
argument_list|)
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|IS_NOT_EQUAL_TO
argument_list|,
name|Expression
operator|.
name|NOT_EQUAL_TO
argument_list|)
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|IS_NOT_EQUAL_TO
operator|+
name|OBJ_C
argument_list|,
name|Expression
operator|.
name|NOT_EQUAL_TO
argument_list|)
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|IS_LIKE
argument_list|,
name|Expression
operator|.
name|LIKE
argument_list|)
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|IS_LIKE
operator|+
name|OBJ_C
argument_list|,
name|Expression
operator|.
name|LIKE
argument_list|)
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|CASE_INSENSITIVE_LIKE
argument_list|,
name|Expression
operator|.
name|LIKE_IGNORE_CASE
argument_list|)
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|CASE_INSENSITIVE_LIKE
operator|+
name|OBJ_C
argument_list|,
name|Expression
operator|.
name|LIKE_IGNORE_CASE
argument_list|)
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|IS_LESS_THAN
argument_list|,
name|Expression
operator|.
name|LESS_THAN
argument_list|)
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|IS_LESS_THAN
operator|+
name|OBJ_C
argument_list|,
name|Expression
operator|.
name|LESS_THAN
argument_list|)
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|IS_LESS_THAN_OR_EQUAL_TO
argument_list|,
name|Expression
operator|.
name|LESS_THAN_EQUAL_TO
argument_list|)
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|IS_LESS_THAN_OR_EQUAL_TO
operator|+
name|OBJ_C
argument_list|,
name|Expression
operator|.
name|LESS_THAN_EQUAL_TO
argument_list|)
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|IS_GREATER_THAN
argument_list|,
name|Expression
operator|.
name|GREATER_THAN
argument_list|)
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|IS_GREATER_THAN
operator|+
name|OBJ_C
argument_list|,
name|Expression
operator|.
name|GREATER_THAN
argument_list|)
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|IS_GREATER_THAN_OR_EQUAL_TO
argument_list|,
name|Expression
operator|.
name|GREATER_THAN_EQUAL_TO
argument_list|)
expr_stmt|;
name|selectorToExpressionBridge
operator|.
name|put
argument_list|(
name|IS_GREATER_THAN_OR_EQUAL_TO
operator|+
name|OBJ_C
argument_list|,
name|Expression
operator|.
name|GREATER_THAN_EQUAL_TO
argument_list|)
expr_stmt|;
block|}
return|return
name|selectorToExpressionBridge
return|;
block|}
comment|/** 		 * isAggregate determines whether a qualifier is "aggregate" -- has 		 * children -- or "simple". 		 *  		 * @param qualifier 		 *            - a Map containing the qualifier settings 		 * @return boolean indicating whether the qualifier is "aggregate" 		 *         qualifier 		 */
specifier|static
name|boolean
name|isAggregate
parameter_list|(
name|Map
name|qualifier
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|true
decl_stmt|;
name|String
name|theClass
init|=
operator|(
name|String
operator|)
name|qualifier
operator|.
name|get
argument_list|(
literal|"class"
argument_list|)
decl_stmt|;
if|if
condition|(
name|theClass
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
comment|// should maybe throw an exception?
block|}
if|if
condition|(
name|theClass
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"EOKeyValueQualifier"
argument_list|)
operator|||
name|theClass
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"EOKeyComparisonQualifier"
argument_list|)
condition|)
block|{
name|result
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/** 		 * expressionTypeForQualifier looks at a qualifier containing the 		 * EOModeler FetchSpecification and returns the equivalent Cayenne 		 * Expression type for its selector. 		 *  		 * @param qualifierMap 		 *            - a Map containing the qualifier settings to examine. 		 * @return int Expression type 		 */
specifier|static
name|int
name|expressionTypeForQualifier
parameter_list|(
name|Map
name|qualifierMap
parameter_list|)
block|{
comment|// get selector
name|String
name|selector
init|=
operator|(
name|String
operator|)
name|qualifierMap
operator|.
name|get
argument_list|(
literal|"selectorName"
argument_list|)
decl_stmt|;
return|return
name|expressionTypeForSelector
argument_list|(
name|selector
argument_list|)
return|;
block|}
comment|/** 		 * expressionTypeForSelector looks at a selector from an EOModeler 		 * FetchSpecification and returns the equivalent Cayenne Expression 		 * type. 		 *  		 * @param selector 		 *            - a String containing the selector name. 		 * @return int Expression type 		 */
specifier|static
name|int
name|expressionTypeForSelector
parameter_list|(
name|String
name|selector
parameter_list|)
block|{
name|Integer
name|expType
init|=
name|selectorToExpressionBridge
argument_list|()
operator|.
name|get
argument_list|(
name|selector
argument_list|)
decl_stmt|;
return|return
operator|(
name|expType
operator|!=
literal|null
condition|?
name|expType
operator|.
name|intValue
argument_list|()
else|:
operator|-
literal|1
operator|)
return|;
block|}
comment|/** 		 * aggregateExpressionClassForQualifier looks at a qualifer and returns 		 * the aggregate type: one of Expression.AND, Expression.OR, or 		 * Expression.NOT 		 *  		 * @param qualifierMap 		 *            - containing the qualifier to examine 		 * @return int aggregate Expression type 		 */
specifier|static
name|int
name|aggregateExpressionClassForQualifier
parameter_list|(
name|Map
name|qualifierMap
parameter_list|)
block|{
name|String
name|qualifierClass
init|=
operator|(
name|String
operator|)
name|qualifierMap
operator|.
name|get
argument_list|(
literal|"class"
argument_list|)
decl_stmt|;
if|if
condition|(
name|qualifierClass
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|qualifierClass
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"EOAndQualifier"
argument_list|)
condition|)
block|{
return|return
name|Expression
operator|.
name|AND
return|;
block|}
if|else if
condition|(
name|qualifierClass
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"EOOrQualifier"
argument_list|)
condition|)
block|{
return|return
name|Expression
operator|.
name|OR
return|;
block|}
if|else if
condition|(
name|qualifierClass
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"EONotQualifier"
argument_list|)
condition|)
block|{
return|return
name|Expression
operator|.
name|NOT
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
comment|// error
block|}
comment|/** 		 * makeQualifier recursively builds an Expression for each condition in 		 * the qualifierMap and assembles from them the complex Expression to 		 * represent the entire EOFetchSpecification. 		 *  		 * @param qualifierMap 		 *            - Map representation of EOFetchSpecification 		 * @return Expression translation of the EOFetchSpecification 		 */
specifier|static
name|Expression
name|makeQualifier
parameter_list|(
name|EOObjEntity
name|entity
parameter_list|,
name|Map
name|qualifierMap
parameter_list|)
block|{
if|if
condition|(
name|isAggregate
argument_list|(
name|qualifierMap
argument_list|)
condition|)
block|{
comment|// the fetch specification has more than one qualifier
name|int
name|aggregateClass
init|=
name|aggregateExpressionClassForQualifier
argument_list|(
name|qualifierMap
argument_list|)
decl_stmt|;
comment|// AND,
comment|// OR,
comment|// NOT
if|if
condition|(
name|aggregateClass
operator|==
name|Expression
operator|.
name|NOT
condition|)
block|{
comment|// NOT qualifiers only have one child, keyed with
comment|// "qualifier"
name|Map
name|child
init|=
operator|(
name|Map
operator|)
name|qualifierMap
operator|.
name|get
argument_list|(
literal|"qualifier"
argument_list|)
decl_stmt|;
comment|// build the child expression
name|Expression
name|childExp
init|=
name|makeQualifier
argument_list|(
name|entity
argument_list|,
name|child
argument_list|)
decl_stmt|;
return|return
name|childExp
operator|.
name|notExp
argument_list|()
return|;
comment|// add the "not" clause and return
comment|// the
comment|// result
block|}
else|else
block|{
comment|// AND, OR qualifiers can have multiple children, keyed with
comment|// "qualifiers"
comment|// get the list of children
name|List
name|children
init|=
operator|(
name|List
operator|)
name|qualifierMap
operator|.
name|get
argument_list|(
literal|"qualifiers"
argument_list|)
decl_stmt|;
if|if
condition|(
name|children
operator|!=
literal|null
condition|)
block|{
name|ArrayList
argument_list|<
name|Expression
argument_list|>
name|childExpressions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// build an Expression for each child
name|Iterator
argument_list|<
name|Map
argument_list|>
name|it
init|=
name|children
operator|.
name|iterator
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
name|Expression
name|childExp
init|=
name|makeQualifier
argument_list|(
name|entity
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|childExpressions
operator|.
name|add
argument_list|(
name|childExp
argument_list|)
expr_stmt|;
block|}
comment|// join the child expressions and return the result
return|return
name|ExpressionFactory
operator|.
name|joinExp
argument_list|(
name|aggregateClass
argument_list|,
name|childExpressions
argument_list|)
return|;
block|}
block|}
block|}
comment|// end if isAggregate(qualifierMap)...
comment|// the query has a single qualifier
comment|// get expression selector type
name|String
name|qualifierClass
init|=
operator|(
name|String
operator|)
name|qualifierMap
operator|.
name|get
argument_list|(
literal|"class"
argument_list|)
decl_stmt|;
comment|// the key or key path we're comparing
name|String
name|key
init|=
literal|null
decl_stmt|;
comment|// the key, keyPath, value, or parameterized value against which
comment|// we're
comment|// comparing the key
name|Object
name|comparisonValue
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|"EOKeyComparisonQualifier"
operator|.
name|equals
argument_list|(
name|qualifierClass
argument_list|)
condition|)
block|{
comment|// Comparing two keys or key paths
name|key
operator|=
operator|(
name|String
operator|)
name|qualifierMap
operator|.
name|get
argument_list|(
literal|"leftValue"
argument_list|)
expr_stmt|;
name|comparisonValue
operator|=
name|qualifierMap
operator|.
name|get
argument_list|(
literal|"rightValue"
argument_list|)
expr_stmt|;
comment|// FIXME: I think EOKeyComparisonQualifier sytle Expressions are
comment|// not
comment|// supported...
return|return
literal|null
return|;
block|}
if|else if
condition|(
literal|"EOKeyValueQualifier"
operator|.
name|equals
argument_list|(
name|qualifierClass
argument_list|)
condition|)
block|{
comment|// Comparing key with a value or parameterized value
name|key
operator|=
operator|(
name|String
operator|)
name|qualifierMap
operator|.
name|get
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|qualifierMap
operator|.
name|get
argument_list|(
literal|"value"
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Map
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|valueMap
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
operator|)
name|value
decl_stmt|;
name|String
name|objClass
init|=
name|valueMap
operator|.
name|get
argument_list|(
literal|"class"
argument_list|)
decl_stmt|;
comment|// can be a
comment|// qualifier class
comment|// or java type
if|if
condition|(
literal|"EOQualifierVariable"
operator|.
name|equals
argument_list|(
name|objClass
argument_list|)
operator|&&
name|valueMap
operator|.
name|containsKey
argument_list|(
literal|"_key"
argument_list|)
condition|)
block|{
comment|// make a parameterized expression
name|String
name|paramName
init|=
name|valueMap
operator|.
name|get
argument_list|(
literal|"_key"
argument_list|)
decl_stmt|;
name|comparisonValue
operator|=
operator|new
name|ExpressionParameter
argument_list|(
name|paramName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Object
name|queryVal
init|=
name|valueMap
operator|.
name|get
argument_list|(
literal|"value"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"NSNumber"
operator|.
name|equals
argument_list|(
name|objClass
argument_list|)
condition|)
block|{
comment|// comparison to NSNumber -- cast
name|comparisonValue
operator|=
name|queryVal
expr_stmt|;
block|}
if|else if
condition|(
literal|"EONull"
operator|.
name|equals
argument_list|(
name|objClass
argument_list|)
condition|)
block|{
comment|// comparison to null
name|comparisonValue
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
comment|// Could there be other types? boolean, date,
comment|// etc.???
comment|// no cast
name|comparisonValue
operator|=
name|queryVal
expr_stmt|;
block|}
block|}
block|}
if|else if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
comment|// value expression
name|comparisonValue
operator|=
name|value
expr_stmt|;
block|}
comment|// end if (value instanceof Map) else...
block|}
comment|// check whether the key is an object path; if at least one
comment|// component is not,
comment|// switch to db path..
name|Expression
name|keyExp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|key
argument_list|)
decl_stmt|;
try|try
block|{
name|entity
operator|.
name|lastPathComponent
argument_list|(
name|keyExp
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|e
parameter_list|)
block|{
try|try
block|{
name|keyExp
operator|=
name|entity
operator|.
name|translateToDbPath
argument_list|(
name|keyExp
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|dbpathEx
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
try|try
block|{
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|expressionTypeForQualifier
argument_list|(
name|qualifierMap
argument_list|)
argument_list|)
decl_stmt|;
name|exp
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
name|keyExp
argument_list|)
expr_stmt|;
name|exp
operator|.
name|setOperand
argument_list|(
literal|1
argument_list|,
name|comparisonValue
argument_list|)
expr_stmt|;
return|return
name|exp
return|;
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

