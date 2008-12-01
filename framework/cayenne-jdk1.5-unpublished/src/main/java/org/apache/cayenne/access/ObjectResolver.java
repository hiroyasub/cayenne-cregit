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
name|DataObject
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
name|DataRow
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
name|map
operator|.
name|DbAttribute
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
name|DbEntity
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
name|EntityInheritanceTree
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
name|reflect
operator|.
name|ClassDescriptor
import|;
end_import

begin_comment
comment|/**  * DataRows-to-objects converter for a specific ObjEntity.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|ObjectResolver
block|{
name|DataContext
name|context
decl_stmt|;
name|ClassDescriptor
name|descriptor
decl_stmt|;
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|primaryKey
decl_stmt|;
name|DataRowStore
name|cache
decl_stmt|;
name|DescriptorResolutionStrategy
name|descriptorResolutionStrategy
decl_stmt|;
name|ObjectResolver
parameter_list|(
name|DataContext
name|context
parameter_list|,
name|ClassDescriptor
name|descriptor
parameter_list|)
block|{
name|init
argument_list|(
name|context
argument_list|,
name|descriptor
argument_list|)
expr_stmt|;
block|}
name|void
name|init
parameter_list|(
name|DataContext
name|context
parameter_list|,
name|ClassDescriptor
name|descriptor
parameter_list|)
block|{
comment|// sanity check
name|DbEntity
name|dbEntity
init|=
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbEntity
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"ObjEntity '"
operator|+
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"' has no DbEntity."
argument_list|)
throw|;
block|}
name|this
operator|.
name|primaryKey
operator|=
name|dbEntity
operator|.
name|getPrimaryKeys
argument_list|()
expr_stmt|;
if|if
condition|(
name|primaryKey
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Won't be able to create ObjectId for '"
operator|+
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"'. Reason: DbEntity '"
operator|+
name|dbEntity
operator|.
name|getName
argument_list|()
operator|+
literal|"' has no Primary Key defined."
argument_list|)
throw|;
block|}
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getDataRowCache
argument_list|()
expr_stmt|;
name|this
operator|.
name|descriptor
operator|=
name|descriptor
expr_stmt|;
name|EntityInheritanceTree
name|inheritanceTree
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupInheritanceTree
argument_list|(
name|descriptor
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|descriptorResolutionStrategy
operator|=
name|inheritanceTree
operator|!=
literal|null
condition|?
operator|new
name|InheritanceStrategy
argument_list|()
else|:
operator|new
name|NoInheritanceStrategy
argument_list|()
expr_stmt|;
block|}
comment|/**      * Properly synchronized version of 'objectsFromDataRows'.      */
name|List
argument_list|<
name|Persistent
argument_list|>
name|synchronizedObjectsFromDataRows
parameter_list|(
name|List
name|rows
parameter_list|)
block|{
synchronized|synchronized
init|(
name|context
operator|.
name|getObjectStore
argument_list|()
init|)
block|{
return|return
name|objectsFromDataRows
argument_list|(
name|rows
argument_list|)
return|;
block|}
block|}
comment|/**      * Converts rows to objects.      *<p>      * Synchronization note. This method requires EXTERNAL synchronization on ObjectStore      * and DataRowStore.      *</p>      */
name|List
argument_list|<
name|Persistent
argument_list|>
name|objectsFromDataRows
parameter_list|(
name|List
name|rows
parameter_list|)
block|{
if|if
condition|(
name|rows
operator|==
literal|null
operator|||
name|rows
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|Persistent
argument_list|>
argument_list|(
literal|1
argument_list|)
return|;
block|}
name|List
argument_list|<
name|Persistent
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|Persistent
argument_list|>
argument_list|(
name|rows
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|rows
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
name|DataRow
name|row
init|=
operator|(
name|DataRow
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Persistent
name|object
init|=
name|objectFromDataRow
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't build Object from row: "
operator|+
name|row
argument_list|)
throw|;
block|}
name|results
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
comment|// now deal with snapshots
name|cache
operator|.
name|snapshotsUpdatedForObjects
argument_list|(
name|results
argument_list|,
name|rows
argument_list|)
expr_stmt|;
return|return
name|results
return|;
block|}
comment|/**      * Processes a list of rows for a result set that has objects related to a set of      * parent objects via some relationship defined in PrefetchProcessorNode parameter.      * Relationships are linked in this method, assuming that parent PK columns are      * included in each row and are prefixed with DB relationship name.      *<p>      * Synchronization note. This method requires EXTERNAL synchronization on ObjectStore      * and DataRowStore.      *</p>      */
name|List
argument_list|<
name|Persistent
argument_list|>
name|relatedObjectsFromDataRows
parameter_list|(
name|List
name|rows
parameter_list|,
name|PrefetchProcessorNode
name|node
parameter_list|)
block|{
if|if
condition|(
name|rows
operator|==
literal|null
operator|||
name|rows
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|Persistent
argument_list|>
argument_list|(
literal|1
argument_list|)
return|;
block|}
name|ObjEntity
name|sourceObjEntity
init|=
operator|(
name|ObjEntity
operator|)
name|node
operator|.
name|getIncoming
argument_list|()
operator|.
name|getRelationship
argument_list|()
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
name|String
name|relatedIdPrefix
init|=
name|node
operator|.
name|getIncoming
argument_list|()
operator|.
name|getRelationship
argument_list|()
operator|.
name|getReverseDbRelationshipPath
argument_list|()
operator|+
literal|"."
decl_stmt|;
name|List
argument_list|<
name|Persistent
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|Persistent
argument_list|>
argument_list|(
name|rows
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|rows
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
name|DataRow
name|row
init|=
operator|(
name|DataRow
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Persistent
name|object
init|=
name|objectFromDataRow
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't build Object from row: "
operator|+
name|row
argument_list|)
throw|;
block|}
name|results
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
comment|// link with parent
comment|// The algorithm below of building an ID doesn't take inheritance into
comment|// account, so there maybe a miss...
name|ObjectId
name|id
init|=
name|createObjectId
argument_list|(
name|row
argument_list|,
name|sourceObjEntity
argument_list|,
name|relatedIdPrefix
argument_list|)
decl_stmt|;
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
literal|"Can't build ObjectId from row: "
operator|+
name|row
operator|+
literal|", entity: "
operator|+
name|sourceObjEntity
operator|.
name|getName
argument_list|()
operator|+
literal|", prefix: "
operator|+
name|relatedIdPrefix
argument_list|)
throw|;
block|}
name|Persistent
name|parentObject
init|=
operator|(
name|Persistent
operator|)
name|context
operator|.
name|getObjectStore
argument_list|()
operator|.
name|getNode
argument_list|(
name|id
argument_list|)
decl_stmt|;
comment|// don't attach to hollow objects
if|if
condition|(
name|parentObject
operator|!=
literal|null
operator|&&
name|parentObject
operator|.
name|getPersistenceState
argument_list|()
operator|!=
name|PersistenceState
operator|.
name|HOLLOW
condition|)
block|{
name|node
operator|.
name|linkToParent
argument_list|(
name|object
argument_list|,
name|parentObject
argument_list|)
expr_stmt|;
block|}
block|}
comment|// now deal with snapshots
name|cache
operator|.
name|snapshotsUpdatedForObjects
argument_list|(
name|results
argument_list|,
name|rows
argument_list|)
expr_stmt|;
return|return
name|results
return|;
block|}
comment|/**      * Processes a single row. This method does not synchronize on ObjectStore and doesn't      * send snapshot updates. These are responsibilities of the caller.      */
name|Persistent
name|objectFromDataRow
parameter_list|(
name|DataRow
name|row
parameter_list|)
block|{
comment|// determine entity to use
name|ClassDescriptor
name|classDescriptor
init|=
name|descriptorResolutionStrategy
operator|.
name|descriptorForRow
argument_list|(
name|row
argument_list|)
decl_stmt|;
comment|// not using DataRow.createObjectId for performance reasons - ObjectResolver has
comment|// all needed metadata already cached.
name|ObjectId
name|anId
init|=
name|createObjectId
argument_list|(
name|row
argument_list|,
name|classDescriptor
operator|.
name|getEntity
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// this condition is valid - see comments on 'createObjectId' for details
if|if
condition|(
name|anId
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// this will create a HOLLOW object if it is not registered yet
name|Persistent
name|object
init|=
name|context
operator|.
name|localObject
argument_list|(
name|anId
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// deal with object state
name|int
name|state
init|=
name|object
operator|.
name|getPersistenceState
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|state
condition|)
block|{
case|case
name|PersistenceState
operator|.
name|COMMITTED
case|:
case|case
name|PersistenceState
operator|.
name|MODIFIED
case|:
case|case
name|PersistenceState
operator|.
name|DELETED
case|:
name|DataRowUtils
operator|.
name|mergeObjectWithSnapshot
argument_list|(
name|context
argument_list|,
name|classDescriptor
argument_list|,
name|object
argument_list|,
name|row
argument_list|)
expr_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|DataObject
condition|)
block|{
operator|(
operator|(
name|DataObject
operator|)
name|object
operator|)
operator|.
name|setSnapshotVersion
argument_list|(
name|row
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|PersistenceState
operator|.
name|HOLLOW
case|:
name|DataRowUtils
operator|.
name|mergeObjectWithSnapshot
argument_list|(
name|context
argument_list|,
name|classDescriptor
argument_list|,
name|object
argument_list|,
name|row
argument_list|)
expr_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|DataObject
condition|)
block|{
operator|(
operator|(
name|DataObject
operator|)
name|object
operator|)
operator|.
name|setSnapshotVersion
argument_list|(
name|row
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
break|break;
block|}
return|return
name|object
return|;
block|}
name|ObjEntity
name|getEntity
parameter_list|()
block|{
return|return
name|descriptor
operator|.
name|getEntity
argument_list|()
return|;
block|}
name|ClassDescriptor
name|getDescriptor
parameter_list|()
block|{
return|return
name|descriptor
return|;
block|}
name|ObjectId
name|createObjectId
parameter_list|(
name|DataRow
name|dataRow
parameter_list|,
name|ObjEntity
name|objEntity
parameter_list|,
name|String
name|namePrefix
parameter_list|)
block|{
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|pk
init|=
name|objEntity
operator|==
name|this
operator|.
name|descriptor
operator|.
name|getEntity
argument_list|()
condition|?
name|this
operator|.
name|primaryKey
else|:
name|objEntity
operator|.
name|getDbEntity
argument_list|()
operator|.
name|getPrimaryKeys
argument_list|()
decl_stmt|;
name|boolean
name|prefix
init|=
name|namePrefix
operator|!=
literal|null
operator|&&
name|namePrefix
operator|.
name|length
argument_list|()
operator|>
literal|0
decl_stmt|;
comment|// ... handle special case - PK.size == 1
comment|// use some not-so-significant optimizations...
if|if
condition|(
name|pk
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|DbAttribute
name|attribute
init|=
name|pk
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|key
init|=
operator|(
name|prefix
operator|)
condition|?
name|namePrefix
operator|+
name|attribute
operator|.
name|getName
argument_list|()
else|:
name|attribute
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Object
name|val
init|=
name|dataRow
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
comment|// this is possible when processing left outer joint prefetches
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// PUT without a prefix
return|return
operator|new
name|ObjectId
argument_list|(
name|objEntity
operator|.
name|getName
argument_list|()
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|val
argument_list|)
return|;
block|}
comment|// ... handle generic case - PK.size> 1
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|idMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|pk
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
name|DbAttribute
name|attribute
range|:
name|pk
control|)
block|{
name|String
name|key
init|=
operator|(
name|prefix
operator|)
condition|?
name|namePrefix
operator|+
name|attribute
operator|.
name|getName
argument_list|()
else|:
name|attribute
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Object
name|val
init|=
name|dataRow
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
comment|// this is possible when processing left outer joint prefetches
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// PUT without a prefix
name|idMap
operator|.
name|put
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ObjectId
argument_list|(
name|objEntity
operator|.
name|getName
argument_list|()
argument_list|,
name|idMap
argument_list|)
return|;
block|}
interface|interface
name|DescriptorResolutionStrategy
block|{
name|ClassDescriptor
name|descriptorForRow
parameter_list|(
name|DataRow
name|row
parameter_list|)
function_decl|;
block|}
class|class
name|NoInheritanceStrategy
implements|implements
name|DescriptorResolutionStrategy
block|{
specifier|public
specifier|final
name|ClassDescriptor
name|descriptorForRow
parameter_list|(
name|DataRow
name|row
parameter_list|)
block|{
return|return
name|descriptor
return|;
block|}
block|}
class|class
name|InheritanceStrategy
implements|implements
name|DescriptorResolutionStrategy
block|{
specifier|public
specifier|final
name|ClassDescriptor
name|descriptorForRow
parameter_list|(
name|DataRow
name|row
parameter_list|)
block|{
name|String
name|entityName
init|=
name|row
operator|.
name|getEntityName
argument_list|()
decl_stmt|;
comment|// null probably means that inheritance qualifiers are messed up
return|return
operator|(
name|entityName
operator|!=
literal|null
operator|)
condition|?
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getClassDescriptor
argument_list|(
name|entityName
argument_list|)
else|:
name|descriptor
return|;
block|}
block|}
block|}
end_class

end_unit

