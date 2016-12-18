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
name|lifecycle
operator|.
name|changeset
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
name|Map
import|;
end_import

begin_comment
comment|/**  * A {@link ChangeSet} implemented as a wrapper on top of {@link GraphDiff} of unspecified  * nature.  *<p>  * Synchronization note: While this class is thread safe, but is not generally intended  * for use in multi-threaded manner. It is common to use it within a single transaction  * thread.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|GenericChangeSet
implements|implements
name|ChangeSet
block|{
specifier|private
name|GraphDiff
name|diff
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|PropertyChange
argument_list|>
argument_list|>
name|changes
decl_stmt|;
specifier|public
name|GenericChangeSet
parameter_list|(
name|GraphDiff
name|diff
parameter_list|)
block|{
name|this
operator|.
name|diff
operator|=
name|diff
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PropertyChange
argument_list|>
name|getChanges
parameter_list|(
name|Persistent
name|object
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PropertyChange
argument_list|>
name|changes
init|=
name|getChanges
argument_list|()
operator|.
name|get
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|changes
operator|!=
literal|null
condition|?
name|changes
else|:
name|Collections
operator|.
name|EMPTY_MAP
return|;
block|}
specifier|private
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|PropertyChange
argument_list|>
argument_list|>
name|getChanges
parameter_list|()
block|{
if|if
condition|(
name|changes
operator|==
literal|null
condition|)
block|{
name|changes
operator|=
name|parseDiff
argument_list|()
expr_stmt|;
block|}
return|return
name|changes
return|;
block|}
specifier|private
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|PropertyChange
argument_list|>
argument_list|>
name|parseDiff
parameter_list|()
block|{
specifier|final
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|PropertyChange
argument_list|>
argument_list|>
name|changes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|diff
operator|.
name|apply
argument_list|(
operator|new
name|GraphChangeHandler
argument_list|()
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|PropertyChange
argument_list|>
name|getChangeMap
parameter_list|(
name|Object
name|id
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PropertyChange
argument_list|>
name|map
init|=
name|changes
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
name|map
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|changes
operator|.
name|put
argument_list|(
operator|(
name|ObjectId
operator|)
name|id
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
name|PropertyChange
name|getChange
parameter_list|(
name|Object
name|id
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|oldValue
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PropertyChange
argument_list|>
name|map
init|=
name|getChangeMap
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|PropertyChange
name|change
init|=
name|map
operator|.
name|get
argument_list|(
name|property
argument_list|)
decl_stmt|;
if|if
condition|(
name|change
operator|==
literal|null
condition|)
block|{
name|change
operator|=
operator|new
name|PropertyChange
argument_list|(
name|property
argument_list|,
name|oldValue
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|property
argument_list|,
name|change
argument_list|)
expr_stmt|;
block|}
return|return
name|change
return|;
block|}
specifier|public
name|void
name|nodeRemoved
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
comment|// noop, don't care, we'll still track the changes for deleted objects.
block|}
specifier|public
name|void
name|nodeCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|)
block|{
comment|// noop (??)
block|}
specifier|public
name|void
name|arcDeleted
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|Object
name|arcId
parameter_list|)
block|{
comment|// record the fact of relationship change... TODO: analyze relationship
comment|// semantics and record changset values
name|getChange
argument_list|(
name|nodeId
argument_list|,
operator|(
name|String
operator|)
name|arcId
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|arcCreated
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|targetNodeId
parameter_list|,
name|Object
name|arcId
parameter_list|)
block|{
comment|// record the fact of relationship change... TODO: analyze relationship
comment|// semantics and record changset values
name|getChange
argument_list|(
name|nodeId
argument_list|,
operator|(
name|String
operator|)
name|arcId
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nodePropertyChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|oldValue
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
name|getChange
argument_list|(
name|nodeId
argument_list|,
name|property
argument_list|,
name|oldValue
argument_list|)
operator|.
name|setNewValue
argument_list|(
name|newValue
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nodeIdChanged
parameter_list|(
name|Object
name|nodeId
parameter_list|,
name|Object
name|newId
parameter_list|)
block|{
comment|// store the same change set under old and new ids to allow lookup before
comment|// and after the commit
name|Map
argument_list|<
name|String
argument_list|,
name|PropertyChange
argument_list|>
name|map
init|=
name|getChangeMap
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
name|changes
operator|.
name|put
argument_list|(
operator|(
name|ObjectId
operator|)
name|newId
argument_list|,
name|map
argument_list|)
expr_stmt|;
comment|// record a change for a special ID "property"
name|getChange
argument_list|(
name|nodeId
argument_list|,
name|OBJECT_ID_PROPERTY_NAME
argument_list|,
name|nodeId
argument_list|)
operator|.
name|setNewValue
argument_list|(
name|newId
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|changes
return|;
block|}
block|}
end_class

end_unit

