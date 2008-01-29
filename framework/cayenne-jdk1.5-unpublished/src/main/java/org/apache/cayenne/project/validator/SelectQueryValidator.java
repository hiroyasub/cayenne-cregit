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
name|project
operator|.
name|validator
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|QueryEngine
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
name|exp
operator|.
name|TraversalHelper
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
name|DataMap
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
name|project
operator|.
name|ProjectPath
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
name|query
operator|.
name|PrefetchTreeNode
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * Validator for SelectQueries.  *   * @author Andrus Adamchik  * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|SelectQueryValidator
extends|extends
name|TreeNodeValidator
block|{
annotation|@
name|Override
specifier|public
name|void
name|validateObject
parameter_list|(
name|ProjectPath
name|treeNodePath
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
name|SelectQuery
name|query
init|=
operator|(
name|SelectQuery
operator|)
name|treeNodePath
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|validateName
argument_list|(
name|query
argument_list|,
name|treeNodePath
argument_list|,
name|validator
argument_list|)
expr_stmt|;
comment|// Resolve root to Entity for further validation
name|Entity
name|root
init|=
name|validateRoot
argument_list|(
name|query
argument_list|,
name|treeNodePath
argument_list|,
name|validator
argument_list|)
decl_stmt|;
comment|// validate path-based parts
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|validateQualifier
argument_list|(
name|root
argument_list|,
name|query
operator|.
name|getQualifier
argument_list|()
argument_list|,
name|treeNodePath
argument_list|,
name|validator
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|Ordering
name|ordering
range|:
name|query
operator|.
name|getOrderings
argument_list|()
control|)
block|{
name|validateOrdering
argument_list|(
name|root
argument_list|,
name|ordering
argument_list|,
name|treeNodePath
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|query
operator|.
name|getPrefetchTree
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|PrefetchTreeNode
argument_list|>
name|prefetches
init|=
name|query
operator|.
name|getPrefetchTree
argument_list|()
operator|.
name|nonPhantomNodes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|prefetches
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|validatePrefetch
argument_list|(
name|root
argument_list|,
operator|(
name|prefetches
operator|.
name|next
argument_list|()
operator|)
operator|.
name|getPath
argument_list|()
argument_list|,
name|treeNodePath
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|Entity
name|validateRoot
parameter_list|(
name|SelectQuery
name|query
parameter_list|,
name|ProjectPath
name|path
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
name|DataMap
name|map
init|=
name|path
operator|.
name|firstInstanceOf
argument_list|(
name|DataMap
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|query
operator|.
name|getRoot
argument_list|()
operator|==
literal|null
operator|&&
name|map
operator|!=
literal|null
condition|)
block|{
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"Query has no root"
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
if|if
condition|(
name|query
operator|.
name|getRoot
argument_list|()
operator|==
name|map
condition|)
block|{
comment|// map-level query... everything is clean
return|return
literal|null
return|;
block|}
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
comment|// maybe standalone entity, otherwise bail...
return|return
operator|(
name|query
operator|.
name|getRoot
argument_list|()
operator|instanceof
name|Entity
operator|)
condition|?
operator|(
name|Entity
operator|)
name|query
operator|.
name|getRoot
argument_list|()
else|:
literal|null
return|;
block|}
if|if
condition|(
name|query
operator|.
name|getRoot
argument_list|()
operator|instanceof
name|Entity
condition|)
block|{
return|return
operator|(
name|Entity
operator|)
name|query
operator|.
name|getRoot
argument_list|()
return|;
block|}
comment|// can't validate Class root - it is likely not accessible from here...
if|if
condition|(
name|query
operator|.
name|getRoot
argument_list|()
operator|instanceof
name|Class
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// resolve entity
if|if
condition|(
name|query
operator|.
name|getRoot
argument_list|()
operator|instanceof
name|String
condition|)
block|{
name|QueryEngine
name|parent
init|=
name|path
operator|.
name|firstInstanceOf
argument_list|(
name|QueryEngine
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|parent
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
argument_list|(
operator|(
name|String
operator|)
name|query
operator|.
name|getRoot
argument_list|()
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|validateName
parameter_list|(
name|Query
name|query
parameter_list|,
name|ProjectPath
name|path
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
name|String
name|name
init|=
name|query
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// Must have name
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|validator
operator|.
name|registerError
argument_list|(
literal|"Unnamed SelectQuery."
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return;
block|}
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|path
operator|.
name|getObjectParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// check for duplicate names in the parent context
for|for
control|(
specifier|final
name|Query
name|otherQuery
range|:
name|map
operator|.
name|getQueries
argument_list|()
control|)
block|{
if|if
condition|(
name|otherQuery
operator|==
name|query
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|otherQuery
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|validator
operator|.
name|registerError
argument_list|(
literal|"Duplicate Query name: "
operator|+
name|name
operator|+
literal|"."
argument_list|,
name|path
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
specifier|protected
name|void
name|validateQualifier
parameter_list|(
name|Entity
name|entity
parameter_list|,
name|Expression
name|qualifier
parameter_list|,
name|ProjectPath
name|path
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
try|try
block|{
name|testExpression
argument_list|(
name|entity
argument_list|,
name|qualifier
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|e
parameter_list|)
block|{
name|validator
operator|.
name|registerWarning
argument_list|(
name|buildValidationMessage
argument_list|(
name|e
argument_list|,
literal|"Invalid path in qualifier"
argument_list|)
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|validateOrdering
parameter_list|(
name|Entity
name|entity
parameter_list|,
name|Ordering
name|ordering
parameter_list|,
name|ProjectPath
name|path
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
if|if
condition|(
name|ordering
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|testExpression
argument_list|(
name|entity
argument_list|,
name|ordering
operator|.
name|getSortSpec
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
name|validator
operator|.
name|registerWarning
argument_list|(
name|buildValidationMessage
argument_list|(
name|e
argument_list|,
literal|"Invalid ordering"
argument_list|)
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|validatePrefetch
parameter_list|(
name|Entity
name|entity
parameter_list|,
name|String
name|prefetch
parameter_list|,
name|ProjectPath
name|path
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
if|if
condition|(
name|prefetch
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|testExpression
argument_list|(
name|entity
argument_list|,
name|Expression
operator|.
name|fromString
argument_list|(
name|prefetch
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpressionException
name|e
parameter_list|)
block|{
name|validator
operator|.
name|registerWarning
argument_list|(
name|buildValidationMessage
argument_list|(
name|e
argument_list|,
literal|"Invalid prefetch"
argument_list|)
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|testExpression
parameter_list|(
name|Entity
name|rootEntity
parameter_list|,
name|Expression
name|exp
parameter_list|)
throws|throws
name|ExpressionException
block|{
if|if
condition|(
name|exp
operator|!=
literal|null
condition|)
block|{
name|exp
operator|.
name|traverse
argument_list|(
operator|new
name|EntityExpressionValidator
argument_list|(
name|rootEntity
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|buildValidationMessage
parameter_list|(
name|ExpressionException
name|e
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getExpressionString
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|": '"
argument_list|)
operator|.
name|append
argument_list|(
name|e
operator|.
name|getExpressionString
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|final
class|class
name|EntityExpressionValidator
extends|extends
name|TraversalHelper
block|{
name|Entity
name|rootEntity
decl_stmt|;
name|EntityExpressionValidator
parameter_list|(
name|Entity
name|rootEntity
parameter_list|)
block|{
name|this
operator|.
name|rootEntity
operator|=
name|rootEntity
expr_stmt|;
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
comment|// check if path nodes are compatibe with root entity
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
comment|// this will throw an exception if the path is invalid
name|node
operator|.
name|evaluate
argument_list|(
name|rootEntity
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

