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
name|List
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
name|GraphManager
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

begin_class
class|class
name|HierarchicalObjectResolverNode
extends|extends
name|ObjectResolver
block|{
specifier|private
name|PrefetchProcessorNode
name|node
decl_stmt|;
specifier|private
name|long
name|txStartRowVersion
decl_stmt|;
name|HierarchicalObjectResolverNode
parameter_list|(
name|PrefetchProcessorNode
name|node
parameter_list|,
name|DataContext
name|context
parameter_list|,
name|ClassDescriptor
name|descriptor
parameter_list|,
name|boolean
name|refresh
parameter_list|,
name|long
name|txStartRowVersion
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|descriptor
argument_list|,
name|refresh
argument_list|)
expr_stmt|;
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
name|this
operator|.
name|txStartRowVersion
operator|=
name|txStartRowVersion
expr_stmt|;
block|}
annotation|@
name|Override
name|List
argument_list|<
name|Persistent
argument_list|>
name|objectsFromDataRows
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|DataRow
argument_list|>
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
name|GraphManager
name|graphManager
init|=
name|context
operator|.
name|getGraphManager
argument_list|()
decl_stmt|;
for|for
control|(
name|DataRow
name|row
range|:
name|rows
control|)
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
comment|// not using DataRow.createObjectId for performance reasons -
comment|// ObjectResolver
comment|// has all needed metadata already cached.
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
comment|// skip processing of objects that were already processed in this
comment|// transaction, either by this node or by some other node...
comment|// added per CAY-1695 ..
comment|// TODO: is it going to have any side effects? It is run from the
comment|// synchronized block, so I guess other threads can't stick their
comment|// versions of this object in here?
comment|// TODO: also this logic implies that main rows are always fetched
comment|// first... I guess this has to stay true if prefetching is
comment|// involved.
name|Persistent
name|object
init|=
operator|(
name|Persistent
operator|)
name|graphManager
operator|.
name|getNode
argument_list|(
name|anId
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|==
literal|null
operator|||
operator|(
operator|(
name|DataObject
operator|)
name|object
operator|)
operator|.
name|getSnapshotVersion
argument_list|()
operator|<
name|txStartRowVersion
condition|)
block|{
name|object
operator|=
name|objectFromDataRow
argument_list|(
name|row
argument_list|,
name|anId
argument_list|,
name|classDescriptor
argument_list|)
expr_stmt|;
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
block|}
comment|// keep the dupe objects (and data rows) around, as there maybe an
comment|// attached
comment|// joint prefetch...
name|results
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|node
operator|.
name|getParentAttachmentStrategy
argument_list|()
operator|.
name|linkToParent
argument_list|(
name|row
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
comment|// now deal with snapshots
comment|// TODO: refactoring: dupes will clutter the lists and cause extra
comment|// processing...
comment|// removal of dupes happens only downstream, as we need the objects
comment|// matching
comment|// fetched rows for joint prefetch resolving... maybe pushback unique
comment|// and
comment|// non-unique lists to the "node", instead of returning a single list
comment|// from this
comment|// method
name|cache
operator|.
name|snapshotsUpdatedForObjects
argument_list|(
name|results
argument_list|,
name|rows
argument_list|,
name|refreshObjects
argument_list|)
expr_stmt|;
return|return
name|results
return|;
block|}
block|}
end_class

end_unit

