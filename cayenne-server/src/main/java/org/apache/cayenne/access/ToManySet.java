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
name|io
operator|.
name|Serializable
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
name|util
operator|.
name|PersistentObjectSet
import|;
end_import

begin_class
specifier|public
class|class
name|ToManySet
parameter_list|<
name|E
parameter_list|>
extends|extends
name|PersistentObjectSet
argument_list|<
name|E
argument_list|>
implements|implements
name|Serializable
block|{
specifier|protected
name|ToManySet
parameter_list|(
name|Persistent
name|relationshipOwner
parameter_list|,
name|String
name|relationshipName
parameter_list|)
block|{
name|super
argument_list|(
name|relationshipOwner
argument_list|,
name|relationshipName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|shouldAddToRemovedFromUnresolvedSet
parameter_list|(
name|E
name|object
parameter_list|)
block|{
comment|// No point in adding a new or transient object -- these will never be fetched
comment|// from the database.
if|if
condition|(
name|object
operator|instanceof
name|Persistent
condition|)
block|{
name|Persistent
name|dataObject
init|=
operator|(
name|Persistent
operator|)
name|object
decl_stmt|;
return|return
operator|(
name|dataObject
operator|.
name|getPersistenceState
argument_list|()
operator|!=
name|PersistenceState
operator|.
name|TRANSIENT
operator|)
operator|&&
operator|(
name|dataObject
operator|.
name|getPersistenceState
argument_list|()
operator|!=
name|PersistenceState
operator|.
name|NEW
operator|)
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|postprocessAdd
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|collection
parameter_list|)
block|{
comment|// no need for this operation for DataObjects...
block|}
annotation|@
name|Override
specifier|protected
name|void
name|postprocessRemove
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|E
argument_list|>
name|collection
parameter_list|)
block|{
comment|// no need for this operation for DataObjects...
block|}
annotation|@
name|Override
specifier|protected
name|void
name|postprocessAdd
parameter_list|(
name|E
name|addedObject
parameter_list|)
block|{
comment|// no need for this operation for DataObjects...
block|}
annotation|@
name|Override
specifier|protected
name|void
name|postprocessRemove
parameter_list|(
name|E
name|removedObject
parameter_list|)
block|{
comment|// no need for this operation for DataObjects...
block|}
annotation|@
name|Override
specifier|protected
name|void
name|updateReverse
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|resolved
parameter_list|)
block|{
comment|// no need for this operation for DataObjects...
block|}
block|}
end_class

end_unit

