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
name|util
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
name|ObjectContext
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
name|map
operator|.
name|EntityResolver
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
name|AttributeProperty
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
name|cayenne
operator|.
name|reflect
operator|.
name|PropertyVisitor
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
name|ToManyMapProperty
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
name|ToManyProperty
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
name|ToOneProperty
import|;
end_import

begin_comment
comment|/**  * An operation that merges changes from an object graph, whose objects are registered in  * some ObjectContext, to peer objects in an ObjectConext that is a child of that context.  * The merge terminates at hollow nodes in the parent context to avoid tripping over  * unresolved relationships.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|DeepMergeOperation
block|{
specifier|private
specifier|final
name|EntityResolver
name|entityResolver
decl_stmt|;
specifier|private
specifier|final
name|ShallowMergeOperation
name|shallowMergeOperation
decl_stmt|;
specifier|public
name|DeepMergeOperation
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|entityResolver
operator|=
name|context
operator|.
name|getEntityResolver
argument_list|()
expr_stmt|;
name|this
operator|.
name|shallowMergeOperation
operator|=
operator|new
name|ShallowMergeOperation
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated since 3.1 - unused as the object is now stateless      */
specifier|public
name|void
name|reset
parameter_list|()
block|{
comment|// noop
block|}
comment|/**      * @deprecated since 3.1 use {@link #merge(Persistent)}.      */
specifier|public
name|Object
name|merge
parameter_list|(
name|Object
name|object
parameter_list|,
name|ClassDescriptor
name|descriptor
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|Persistent
operator|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Expected Persistent, got: "
operator|+
name|object
argument_list|)
throw|;
block|}
return|return
name|merge
argument_list|(
operator|(
name|Persistent
operator|)
name|object
argument_list|)
return|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|Persistent
parameter_list|>
name|T
name|merge
parameter_list|(
name|T
name|peerInParentContext
parameter_list|)
block|{
name|ClassDescriptor
name|descriptor
init|=
name|entityResolver
operator|.
name|getClassDescriptor
argument_list|(
name|peerInParentContext
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|merge
argument_list|(
name|peerInParentContext
argument_list|,
name|descriptor
argument_list|,
operator|new
name|HashMap
argument_list|<
name|ObjectId
argument_list|,
name|Persistent
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
specifier|private
parameter_list|<
name|T
extends|extends
name|Persistent
parameter_list|>
name|T
name|merge
parameter_list|(
specifier|final
name|T
name|peerInParentContext
parameter_list|,
name|ClassDescriptor
name|descriptor
parameter_list|,
specifier|final
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|Persistent
argument_list|>
name|seen
parameter_list|)
block|{
name|ObjectId
name|id
init|=
name|peerInParentContext
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
comment|// sanity check
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Server returned an object without an id: "
operator|+
name|peerInParentContext
argument_list|)
throw|;
block|}
name|Persistent
name|seenTarget
init|=
name|seen
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|seenTarget
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|seenTarget
return|;
block|}
specifier|final
name|T
name|target
init|=
name|shallowMergeOperation
operator|.
name|merge
argument_list|(
name|peerInParentContext
argument_list|)
decl_stmt|;
name|seen
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|target
argument_list|)
expr_stmt|;
name|descriptor
operator|=
name|descriptor
operator|.
name|getSubclassDescriptor
argument_list|(
name|peerInParentContext
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|descriptor
operator|.
name|visitProperties
argument_list|(
operator|new
name|PropertyVisitor
argument_list|()
block|{
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
if|if
condition|(
operator|!
name|property
operator|.
name|isFault
argument_list|(
name|peerInParentContext
argument_list|)
condition|)
block|{
name|Persistent
name|destinationSource
init|=
operator|(
name|Persistent
operator|)
name|property
operator|.
name|readProperty
argument_list|(
name|peerInParentContext
argument_list|)
decl_stmt|;
name|Object
name|destinationTarget
init|=
name|destinationSource
operator|!=
literal|null
condition|?
name|merge
argument_list|(
name|destinationSource
argument_list|,
name|property
operator|.
name|getTargetDescriptor
argument_list|()
argument_list|,
name|seen
argument_list|)
else|:
literal|null
decl_stmt|;
name|Object
name|oldTarget
init|=
name|property
operator|.
name|isFault
argument_list|(
name|target
argument_list|)
condition|?
literal|null
else|:
name|property
operator|.
name|readProperty
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|property
operator|.
name|writePropertyDirectly
argument_list|(
name|target
argument_list|,
name|oldTarget
argument_list|,
name|destinationTarget
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
if|if
condition|(
operator|!
name|property
operator|.
name|isFault
argument_list|(
name|peerInParentContext
argument_list|)
condition|)
block|{
name|Object
name|value
init|=
name|property
operator|.
name|readProperty
argument_list|(
name|peerInParentContext
argument_list|)
decl_stmt|;
name|Object
name|targetValue
decl_stmt|;
if|if
condition|(
name|property
operator|instanceof
name|ToManyMapProperty
condition|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
operator|(
name|Map
operator|)
name|value
decl_stmt|;
name|Map
name|targetMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|destinationSource
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Object
name|destinationTarget
init|=
name|destinationSource
operator|!=
literal|null
condition|?
name|merge
argument_list|(
operator|(
name|Persistent
operator|)
name|destinationSource
argument_list|,
name|property
operator|.
name|getTargetDescriptor
argument_list|()
argument_list|,
name|seen
argument_list|)
else|:
literal|null
decl_stmt|;
name|targetMap
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|destinationTarget
argument_list|)
expr_stmt|;
block|}
name|targetValue
operator|=
name|targetMap
expr_stmt|;
block|}
else|else
block|{
name|Collection
name|collection
init|=
operator|(
name|Collection
operator|)
name|value
decl_stmt|;
name|Collection
name|targetCollection
init|=
operator|new
name|ArrayList
argument_list|(
name|collection
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|destinationSource
range|:
name|collection
control|)
block|{
name|Object
name|destinationTarget
init|=
name|destinationSource
operator|!=
literal|null
condition|?
name|merge
argument_list|(
operator|(
name|Persistent
operator|)
name|destinationSource
argument_list|,
name|property
operator|.
name|getTargetDescriptor
argument_list|()
argument_list|,
name|seen
argument_list|)
else|:
literal|null
decl_stmt|;
name|targetCollection
operator|.
name|add
argument_list|(
name|destinationTarget
argument_list|)
expr_stmt|;
block|}
name|targetValue
operator|=
name|targetCollection
expr_stmt|;
block|}
name|property
operator|.
name|writePropertyDirectly
argument_list|(
name|target
argument_list|,
literal|null
argument_list|,
name|targetValue
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|target
return|;
block|}
block|}
end_class

end_unit

