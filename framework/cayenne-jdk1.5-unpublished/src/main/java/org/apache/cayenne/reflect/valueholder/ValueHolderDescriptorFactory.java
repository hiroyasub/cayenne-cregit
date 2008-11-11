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
name|reflect
operator|.
name|valueholder
package|;
end_package

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
name|Set
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
name|ValueHolder
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
name|reflect
operator|.
name|Accessor
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
name|ClassDescriptorFactory
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
name|ClassDescriptorMap
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
name|PersistentDescriptor
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
name|PersistentDescriptorFactory
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
name|Property
import|;
end_import

begin_comment
comment|/**  * A {@link ClassDescriptorFactory} for Persistent objects that implement relationship  * faulting via {@link ValueHolder}.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|ValueHolderDescriptorFactory
extends|extends
name|PersistentDescriptorFactory
block|{
specifier|public
name|ValueHolderDescriptorFactory
parameter_list|(
name|ClassDescriptorMap
name|descriptorMap
parameter_list|)
block|{
name|super
argument_list|(
name|descriptorMap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|createToManyCollectionProperty
parameter_list|(
name|PersistentDescriptor
name|descriptor
parameter_list|,
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|ClassDescriptor
name|targetDescriptor
init|=
name|descriptorMap
operator|.
name|getDescriptor
argument_list|(
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|reverseName
init|=
name|relationship
operator|.
name|getReverseRelationshipName
argument_list|()
decl_stmt|;
name|Accessor
name|accessor
init|=
name|createAccessor
argument_list|(
name|descriptor
argument_list|,
name|relationship
operator|.
name|getName
argument_list|()
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|descriptor
operator|.
name|addDeclaredProperty
argument_list|(
operator|new
name|ValueHolderListProperty
argument_list|(
name|descriptor
argument_list|,
name|targetDescriptor
argument_list|,
name|accessor
argument_list|,
name|reverseName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|createToManyListProperty
parameter_list|(
name|PersistentDescriptor
name|descriptor
parameter_list|,
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|ClassDescriptor
name|targetDescriptor
init|=
name|descriptorMap
operator|.
name|getDescriptor
argument_list|(
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|reverseName
init|=
name|relationship
operator|.
name|getReverseRelationshipName
argument_list|()
decl_stmt|;
name|Accessor
name|accessor
init|=
name|createAccessor
argument_list|(
name|descriptor
argument_list|,
name|relationship
operator|.
name|getName
argument_list|()
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|descriptor
operator|.
name|addDeclaredProperty
argument_list|(
operator|new
name|ValueHolderListProperty
argument_list|(
name|descriptor
argument_list|,
name|targetDescriptor
argument_list|,
name|accessor
argument_list|,
name|reverseName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|createToManyMapProperty
parameter_list|(
name|PersistentDescriptor
name|descriptor
parameter_list|,
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|ClassDescriptor
name|targetDescriptor
init|=
name|descriptorMap
operator|.
name|getDescriptor
argument_list|(
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|reverseName
init|=
name|relationship
operator|.
name|getReverseRelationshipName
argument_list|()
decl_stmt|;
name|Accessor
name|accessor
init|=
name|createAccessor
argument_list|(
name|descriptor
argument_list|,
name|relationship
operator|.
name|getName
argument_list|()
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|Accessor
name|mapKeyAccessor
init|=
name|createMapKeyAccessor
argument_list|(
name|relationship
argument_list|,
name|targetDescriptor
argument_list|)
decl_stmt|;
name|Property
name|property
init|=
operator|new
name|ValueHolderMapProperty
argument_list|(
name|descriptor
argument_list|,
name|targetDescriptor
argument_list|,
name|accessor
argument_list|,
name|reverseName
argument_list|,
name|mapKeyAccessor
argument_list|)
decl_stmt|;
name|descriptor
operator|.
name|addDeclaredProperty
argument_list|(
name|property
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|createToManySetProperty
parameter_list|(
name|PersistentDescriptor
name|descriptor
parameter_list|,
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|ClassDescriptor
name|targetDescriptor
init|=
name|descriptorMap
operator|.
name|getDescriptor
argument_list|(
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|reverseName
init|=
name|relationship
operator|.
name|getReverseRelationshipName
argument_list|()
decl_stmt|;
name|Accessor
name|accessor
init|=
name|createAccessor
argument_list|(
name|descriptor
argument_list|,
name|relationship
operator|.
name|getName
argument_list|()
argument_list|,
name|Set
operator|.
name|class
argument_list|)
decl_stmt|;
name|descriptor
operator|.
name|addDeclaredProperty
argument_list|(
operator|new
name|ValueHolderSetProperty
argument_list|(
name|descriptor
argument_list|,
name|targetDescriptor
argument_list|,
name|accessor
argument_list|,
name|reverseName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|createToOneProperty
parameter_list|(
name|PersistentDescriptor
name|descriptor
parameter_list|,
name|ObjRelationship
name|relationship
parameter_list|)
block|{
name|ClassDescriptor
name|targetDescriptor
init|=
name|descriptorMap
operator|.
name|getDescriptor
argument_list|(
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|reverseName
init|=
name|relationship
operator|.
name|getReverseRelationshipName
argument_list|()
decl_stmt|;
name|Accessor
name|accessor
init|=
name|createAccessor
argument_list|(
name|descriptor
argument_list|,
name|relationship
operator|.
name|getName
argument_list|()
argument_list|,
name|ValueHolder
operator|.
name|class
argument_list|)
decl_stmt|;
name|Property
name|property
init|=
operator|new
name|ValueHolderProperty
argument_list|(
name|descriptor
argument_list|,
name|targetDescriptor
argument_list|,
name|accessor
argument_list|,
name|reverseName
argument_list|)
decl_stmt|;
name|descriptor
operator|.
name|addDeclaredProperty
argument_list|(
name|property
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

