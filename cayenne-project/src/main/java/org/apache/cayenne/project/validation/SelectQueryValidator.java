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
name|validation
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
name|query
operator|.
name|*
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_class
class|class
name|SelectQueryValidator
extends|extends
name|ConfigurationNodeValidator
block|{
name|void
name|validate
parameter_list|(
name|SelectQueryDescriptor
name|query
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|validateName
argument_list|(
name|query
argument_list|,
name|validationResult
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
name|validationResult
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
name|validationResult
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
name|query
argument_list|,
name|root
argument_list|,
name|ordering
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|query
operator|.
name|getPrefetches
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|prefetchPath
range|:
name|query
operator|.
name|getPrefetches
argument_list|()
control|)
block|{
name|validatePrefetch
argument_list|(
name|root
argument_list|,
name|prefetchPath
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|void
name|validatePrefetch
parameter_list|(
name|Entity
name|root
parameter_list|,
name|String
name|path
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
comment|// TODO: andrus 03/10/2010 - should this be implemented?
block|}
name|void
name|validateOrdering
parameter_list|(
name|QueryDescriptor
name|query
parameter_list|,
name|Entity
name|root
parameter_list|,
name|Ordering
name|ordering
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
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
name|root
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
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|query
argument_list|,
literal|"Invalid ordering path: '%s'"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|void
name|validateQualifier
parameter_list|(
name|Entity
name|root
parameter_list|,
name|Expression
name|qualifier
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
comment|// TODO: andrus 03/10/2010 - should this be implemented?
block|}
name|Entity
name|validateRoot
parameter_list|(
name|QueryDescriptor
name|query
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|DataMap
name|map
init|=
name|query
operator|.
name|getDataMap
argument_list|()
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
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|query
argument_list|,
literal|"Query '%s' has no root"
argument_list|,
name|query
operator|.
name|getName
argument_list|()
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
argument_list|<
name|?
argument_list|>
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
name|DataMap
name|parent
init|=
name|query
operator|.
name|getDataMap
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
name|parent
operator|.
name|getNamespace
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
name|void
name|validateName
parameter_list|(
name|QueryDescriptor
name|query
parameter_list|,
name|ValidationResult
name|validationResult
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
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|query
argument_list|,
literal|"Unnamed SelectQuery"
argument_list|)
expr_stmt|;
return|return;
block|}
name|DataMap
name|map
init|=
name|query
operator|.
name|getDataMap
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
name|QueryDescriptor
name|otherQuery
range|:
name|map
operator|.
name|getQueryDescriptors
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
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|query
argument_list|,
literal|"Duplicate query name: %s"
argument_list|,
name|name
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
end_class

end_unit

