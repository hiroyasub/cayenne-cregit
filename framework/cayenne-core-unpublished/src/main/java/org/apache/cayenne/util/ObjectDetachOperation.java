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
name|PropertyDescriptor
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
comment|/**  * An operation that creates a subgraph of detached objects, using the PrefetchTree to  * delineate the graph boundaries. Target objects can be described by a different set of  * descriptors, thus allowing server-to-client conversion to happen in the process.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|ObjectDetachOperation
block|{
specifier|protected
name|EntityResolver
name|targetResolver
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|Persistent
argument_list|>
name|seen
decl_stmt|;
specifier|public
name|ObjectDetachOperation
parameter_list|(
name|EntityResolver
name|targetResolver
parameter_list|)
block|{
name|this
operator|.
name|targetResolver
operator|=
name|targetResolver
expr_stmt|;
name|this
operator|.
name|seen
operator|=
operator|new
name|HashMap
argument_list|<
name|ObjectId
argument_list|,
name|Persistent
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|seen
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * "Detaches" an object from its context by creating an unattached copy. The copy is      * created using target descriptor of this operation that may be different from the      * object descriptor passed to this method.      */
specifier|public
name|Object
name|detach
parameter_list|(
name|Object
name|object
parameter_list|,
name|ClassDescriptor
name|descriptor
parameter_list|,
specifier|final
name|PrefetchTreeNode
name|prefetchTree
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
specifier|final
name|Persistent
name|source
init|=
operator|(
name|Persistent
operator|)
name|object
decl_stmt|;
name|ObjectId
name|id
init|=
name|source
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
name|source
argument_list|)
throw|;
block|}
name|Object
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
name|seenTarget
return|;
block|}
name|descriptor
operator|=
name|descriptor
operator|.
name|getSubclassDescriptor
argument_list|(
name|source
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
comment|// presumably id's entity name should be of the right subclass.
specifier|final
name|ClassDescriptor
name|targetDescriptor
init|=
name|targetResolver
operator|.
name|getClassDescriptor
argument_list|(
name|id
operator|.
name|getEntityName
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Persistent
name|target
init|=
operator|(
name|Persistent
operator|)
name|targetDescriptor
operator|.
name|createObject
argument_list|()
decl_stmt|;
name|target
operator|.
name|setObjectId
argument_list|(
name|id
argument_list|)
expr_stmt|;
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
name|prefetchTree
operator|!=
literal|null
condition|)
block|{
name|PrefetchTreeNode
name|child
init|=
name|prefetchTree
operator|.
name|getNode
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
name|Object
name|destinationSource
init|=
name|property
operator|.
name|readProperty
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|Object
name|destinationTarget
init|=
name|destinationSource
operator|!=
literal|null
condition|?
name|detach
argument_list|(
name|destinationSource
argument_list|,
name|property
operator|.
name|getTargetDescriptor
argument_list|()
argument_list|,
name|child
argument_list|)
else|:
literal|null
decl_stmt|;
name|ToOneProperty
name|targetProperty
init|=
operator|(
name|ToOneProperty
operator|)
name|targetDescriptor
operator|.
name|getProperty
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|oldTarget
init|=
name|targetProperty
operator|.
name|isFault
argument_list|(
name|target
argument_list|)
condition|?
literal|null
else|:
name|targetProperty
operator|.
name|readProperty
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|targetProperty
operator|.
name|writeProperty
argument_list|(
name|target
argument_list|,
name|oldTarget
argument_list|,
name|destinationTarget
argument_list|)
expr_stmt|;
block|}
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
name|prefetchTree
operator|!=
literal|null
condition|)
block|{
name|PrefetchTreeNode
name|child
init|=
name|prefetchTree
operator|.
name|getNode
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
name|Object
name|value
init|=
name|property
operator|.
name|readProperty
argument_list|(
name|source
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
name|detach
argument_list|(
name|destinationSource
argument_list|,
name|property
operator|.
name|getTargetDescriptor
argument_list|()
argument_list|,
name|child
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
name|detach
argument_list|(
name|destinationSource
argument_list|,
name|property
operator|.
name|getTargetDescriptor
argument_list|()
argument_list|,
name|child
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
name|ToManyProperty
name|targetProperty
init|=
operator|(
name|ToManyProperty
operator|)
name|targetDescriptor
operator|.
name|getProperty
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|targetProperty
operator|.
name|writeProperty
argument_list|(
name|target
argument_list|,
literal|null
argument_list|,
name|targetValue
argument_list|)
expr_stmt|;
block|}
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
name|PropertyDescriptor
name|targetProperty
init|=
name|targetDescriptor
operator|.
name|getProperty
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|targetProperty
operator|.
name|writeProperty
argument_list|(
name|target
argument_list|,
literal|null
argument_list|,
name|property
operator|.
name|readProperty
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
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
