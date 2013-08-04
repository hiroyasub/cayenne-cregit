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
package|;
end_package

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
name|Map
operator|.
name|Entry
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
name|PersistenceState
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
name|Validating
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
name|graph
operator|.
name|CompoundDiff
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
name|graph
operator|.
name|GraphChangeHandler
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
name|graph
operator|.
name|GraphDiff
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
name|graph
operator|.
name|NodeDiff
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
name|ValidationException
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

begin_comment
comment|/**  * A GraphDiff facade for the ObjectStore changes. Provides a way for the lower  * layers of the access stack to speed up processing of presorted ObjectStore  * diffs.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|ObjectStoreGraphDiff
implements|implements
name|GraphDiff
block|{
specifier|private
name|ObjectStore
name|objectStore
decl_stmt|;
specifier|private
name|GraphDiff
name|resolvedDiff
decl_stmt|;
name|ObjectStoreGraphDiff
parameter_list|(
name|ObjectStore
name|objectStore
parameter_list|)
block|{
name|this
operator|.
name|objectStore
operator|=
name|objectStore
expr_stmt|;
name|preprocess
argument_list|(
name|objectStore
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|Object
argument_list|,
name|ObjectDiff
argument_list|>
name|getChangesByObjectId
parameter_list|()
block|{
return|return
name|objectStore
operator|.
name|getChangesByObjectId
argument_list|()
return|;
block|}
comment|/**      * Requires external synchronization on ObjectStore.      */
name|boolean
name|validateAndCheckNoop
parameter_list|()
block|{
if|if
condition|(
name|getChangesByObjectId
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|boolean
name|noop
init|=
literal|true
decl_stmt|;
comment|// build a new collection for validation as validation methods may
comment|// result in
comment|// ObjectStore modifications
name|Collection
argument_list|<
name|Validating
argument_list|>
name|objectsToValidate
init|=
literal|null
decl_stmt|;
for|for
control|(
specifier|final
name|ObjectDiff
name|diff
range|:
name|getChangesByObjectId
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|diff
operator|.
name|isNoop
argument_list|()
condition|)
block|{
name|noop
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|diff
operator|.
name|getObject
argument_list|()
operator|instanceof
name|Validating
condition|)
block|{
if|if
condition|(
name|objectsToValidate
operator|==
literal|null
condition|)
block|{
name|objectsToValidate
operator|=
operator|new
name|ArrayList
argument_list|<
name|Validating
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|objectsToValidate
operator|.
name|add
argument_list|(
operator|(
name|Validating
operator|)
name|diff
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|objectsToValidate
operator|!=
literal|null
condition|)
block|{
name|ValidationResult
name|result
init|=
operator|new
name|ValidationResult
argument_list|()
decl_stmt|;
for|for
control|(
name|Validating
name|object
range|:
name|objectsToValidate
control|)
block|{
switch|switch
condition|(
operator|(
operator|(
name|Persistent
operator|)
name|object
operator|)
operator|.
name|getPersistenceState
argument_list|()
condition|)
block|{
case|case
name|PersistenceState
operator|.
name|NEW
case|:
name|object
operator|.
name|validateForInsert
argument_list|(
name|result
argument_list|)
expr_stmt|;
break|break;
case|case
name|PersistenceState
operator|.
name|MODIFIED
case|:
name|object
operator|.
name|validateForUpdate
argument_list|(
name|result
argument_list|)
expr_stmt|;
break|break;
case|case
name|PersistenceState
operator|.
name|DELETED
case|:
name|object
operator|.
name|validateForDelete
argument_list|(
name|result
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|result
operator|.
name|hasFailures
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
name|result
argument_list|)
throw|;
block|}
block|}
return|return
name|noop
return|;
block|}
specifier|public
name|boolean
name|isNoop
parameter_list|()
block|{
if|if
condition|(
name|getChangesByObjectId
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
for|for
control|(
name|ObjectDiff
name|diff
range|:
name|getChangesByObjectId
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|diff
operator|.
name|isNoop
argument_list|()
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
specifier|public
name|void
name|apply
parameter_list|(
name|GraphChangeHandler
name|handler
parameter_list|)
block|{
name|resolveDiff
argument_list|()
expr_stmt|;
name|resolvedDiff
operator|.
name|apply
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|undo
parameter_list|(
name|GraphChangeHandler
name|handler
parameter_list|)
block|{
name|resolveDiff
argument_list|()
expr_stmt|;
name|resolvedDiff
operator|.
name|undo
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
comment|/**      * Converts diffs organized by ObjectId in a collection of diffs sorted by      * diffId (same as creation order).      */
specifier|private
name|void
name|resolveDiff
parameter_list|()
block|{
if|if
condition|(
name|resolvedDiff
operator|==
literal|null
condition|)
block|{
name|CompoundDiff
name|diff
init|=
operator|new
name|CompoundDiff
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|ObjectDiff
argument_list|>
name|changes
init|=
name|getChangesByObjectId
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|changes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|NodeDiff
argument_list|>
name|allChanges
init|=
operator|new
name|ArrayList
argument_list|<
name|NodeDiff
argument_list|>
argument_list|(
name|changes
operator|.
name|size
argument_list|()
operator|*
literal|2
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|ObjectDiff
name|objectDiff
range|:
name|changes
operator|.
name|values
argument_list|()
control|)
block|{
name|objectDiff
operator|.
name|appendDiffs
argument_list|(
name|allChanges
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|allChanges
argument_list|)
expr_stmt|;
name|diff
operator|.
name|addAll
argument_list|(
name|allChanges
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|resolvedDiff
operator|=
name|diff
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|preprocess
parameter_list|(
name|ObjectStore
name|objectStore
parameter_list|)
block|{
name|Map
argument_list|<
name|Object
argument_list|,
name|ObjectDiff
argument_list|>
name|changes
init|=
name|getChangesByObjectId
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|changes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|Object
argument_list|,
name|ObjectDiff
argument_list|>
name|entry
range|:
name|changes
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ObjectId
name|id
init|=
operator|(
name|ObjectId
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Persistent
name|object
init|=
operator|(
name|Persistent
operator|)
name|objectStore
operator|.
name|getNode
argument_list|(
name|id
argument_list|)
decl_stmt|;
comment|// address manual id override.
name|ObjectId
name|objectId
init|=
name|object
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|id
operator|.
name|equals
argument_list|(
name|objectId
argument_list|)
condition|)
block|{
if|if
condition|(
name|objectId
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|replacement
init|=
name|id
operator|.
name|getReplacementIdMap
argument_list|()
decl_stmt|;
name|replacement
operator|.
name|clear
argument_list|()
expr_stmt|;
name|replacement
operator|.
name|putAll
argument_list|(
name|objectId
operator|.
name|getIdSnapshot
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|object
operator|.
name|setObjectId
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

