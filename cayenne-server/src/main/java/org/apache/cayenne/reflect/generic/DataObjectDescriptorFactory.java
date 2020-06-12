begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|generic
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
name|map
operator|.
name|ObjAttribute
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
name|FaultFactory
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
name|PropertyException
import|;
end_import

begin_comment
comment|/**  * A {@link ClassDescriptorFactory} that creates descriptors for classes implementing  * {@link DataObject}.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|DataObjectDescriptorFactory
extends|extends
name|PersistentDescriptorFactory
block|{
specifier|protected
name|FaultFactory
name|faultFactory
decl_stmt|;
specifier|protected
name|ValueComparisionStrategyFactory
name|valueComparisionStrategyFactory
decl_stmt|;
specifier|public
name|DataObjectDescriptorFactory
parameter_list|(
name|ClassDescriptorMap
name|descriptorMap
parameter_list|,
name|FaultFactory
name|faultFactory
parameter_list|,
name|ValueComparisionStrategyFactory
name|valueComparisionStrategyFactory
parameter_list|)
block|{
name|super
argument_list|(
name|descriptorMap
argument_list|)
expr_stmt|;
name|this
operator|.
name|faultFactory
operator|=
name|faultFactory
expr_stmt|;
name|this
operator|.
name|valueComparisionStrategyFactory
operator|=
name|valueComparisionStrategyFactory
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|ClassDescriptor
name|getDescriptor
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|entityClass
parameter_list|)
block|{
if|if
condition|(
operator|!
name|DataObject
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|entityClass
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|super
operator|.
name|getDescriptor
argument_list|(
name|entity
argument_list|,
name|entityClass
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|PersistentDescriptor
name|createDescriptor
parameter_list|()
block|{
return|return
operator|new
name|DataObjectDescriptor
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|createAttributeProperty
parameter_list|(
name|PersistentDescriptor
name|descriptor
parameter_list|,
name|ObjAttribute
name|attribute
parameter_list|)
block|{
name|DataObjectAttributeProperty
name|property
init|=
operator|new
name|DataObjectAttributeProperty
argument_list|(
name|attribute
argument_list|,
name|valueComparisionStrategyFactory
operator|.
name|getStrategy
argument_list|(
name|attribute
argument_list|)
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
name|descriptor
operator|.
name|addDeclaredProperty
argument_list|(
operator|new
name|DataObjectToManyProperty
argument_list|(
name|relationship
argument_list|,
name|targetDescriptor
argument_list|,
name|faultFactory
operator|.
name|getListFault
argument_list|()
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
name|descriptor
operator|.
name|addDeclaredProperty
argument_list|(
operator|new
name|DataObjectToManyMapProperty
argument_list|(
name|relationship
argument_list|,
name|targetDescriptor
argument_list|,
name|faultFactory
operator|.
name|getMapFault
argument_list|(
name|mapKeyAccessor
argument_list|)
argument_list|,
name|mapKeyAccessor
argument_list|)
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
name|descriptor
operator|.
name|addDeclaredProperty
argument_list|(
operator|new
name|DataObjectToManyProperty
argument_list|(
name|relationship
argument_list|,
name|targetDescriptor
argument_list|,
name|faultFactory
operator|.
name|getSetFault
argument_list|()
argument_list|)
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
name|descriptor
operator|.
name|addDeclaredProperty
argument_list|(
operator|new
name|DataObjectToManyProperty
argument_list|(
name|relationship
argument_list|,
name|targetDescriptor
argument_list|,
name|faultFactory
operator|.
name|getCollectionFault
argument_list|()
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
name|descriptor
operator|.
name|addDeclaredProperty
argument_list|(
operator|new
name|DataObjectToOneProperty
argument_list|(
name|relationship
argument_list|,
name|targetDescriptor
argument_list|,
name|faultFactory
operator|.
name|getToOneFault
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Accessor
name|createAccessor
parameter_list|(
name|PersistentDescriptor
name|descriptor
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|propertyType
parameter_list|)
throws|throws
name|PropertyException
block|{
return|return
operator|new
name|DataObjectAccessor
argument_list|(
name|propertyName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

