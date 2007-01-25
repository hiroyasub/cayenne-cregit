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

begin_comment
comment|/**  * A convenience superclass for {@link ClassDescriptorFactory} implementors.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PersistentDescriptorFactory
implements|implements
name|ClassDescriptorFactory
block|{
specifier|protected
name|ClassDescriptorMap
name|descriptorMap
decl_stmt|;
specifier|public
name|PersistentDescriptorFactory
parameter_list|(
name|ClassDescriptorMap
name|descriptorMap
parameter_list|)
block|{
name|this
operator|.
name|descriptorMap
operator|=
name|descriptorMap
expr_stmt|;
block|}
specifier|public
name|ClassDescriptor
name|getDescriptor
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
name|ObjEntity
name|entity
init|=
name|descriptorMap
operator|.
name|getResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unmapped entity: "
operator|+
name|entityName
argument_list|)
throw|;
block|}
name|Class
name|entityClass
init|=
name|entity
operator|.
name|getJavaClass
argument_list|()
decl_stmt|;
return|return
name|getDescriptor
argument_list|(
name|entity
argument_list|,
name|entityClass
argument_list|)
return|;
block|}
specifier|protected
name|ClassDescriptor
name|getDescriptor
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|Class
name|entityClass
parameter_list|)
block|{
name|String
name|superEntityName
init|=
name|entity
operator|.
name|getSuperEntityName
argument_list|()
decl_stmt|;
name|ClassDescriptor
name|superDescriptor
init|=
operator|(
name|superEntityName
operator|!=
literal|null
operator|)
condition|?
name|descriptorMap
operator|.
name|getDescriptor
argument_list|(
name|superEntityName
argument_list|)
else|:
literal|null
decl_stmt|;
name|PersistentDescriptor
name|descriptor
init|=
name|createDescriptor
argument_list|()
decl_stmt|;
name|descriptor
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|descriptor
operator|.
name|setSuperclassDescriptor
argument_list|(
name|superDescriptor
argument_list|)
expr_stmt|;
name|descriptor
operator|.
name|setObjectClass
argument_list|(
name|entityClass
argument_list|)
expr_stmt|;
name|descriptor
operator|.
name|setPersistenceStateAccessor
argument_list|(
operator|new
name|BeanAccessor
argument_list|(
name|entityClass
argument_list|,
literal|"persistenceState"
argument_list|,
name|Integer
operator|.
name|TYPE
argument_list|)
argument_list|)
expr_stmt|;
comment|// only include this entity attributes and skip superclasses...
name|Iterator
name|attributes
init|=
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getDeclaredAttributes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|attributes
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|attribute
init|=
name|attributes
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|attribute
operator|instanceof
name|ObjAttribute
condition|)
block|{
name|createAttributeProperty
argument_list|(
name|descriptor
argument_list|,
operator|(
name|ObjAttribute
operator|)
name|attribute
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// TODO: andrus, 1/25/2007 - EmbeddedAttribute
block|}
block|}
comment|// only include this entity relationships and skip superclasses...
name|Iterator
name|it
init|=
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getDeclaredRelationships
argument_list|()
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
name|ObjRelationship
name|relationship
init|=
operator|(
name|ObjRelationship
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|relationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|createToManyProperty
argument_list|(
name|descriptor
argument_list|,
name|relationship
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|createToOneProperty
argument_list|(
name|descriptor
argument_list|,
name|relationship
argument_list|)
expr_stmt|;
block|}
block|}
name|EntityInheritanceTree
name|inheritanceTree
init|=
name|descriptorMap
operator|.
name|getResolver
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
name|indexSubclassDescriptors
argument_list|(
name|descriptor
argument_list|,
name|inheritanceTree
argument_list|)
expr_stmt|;
return|return
name|descriptor
return|;
block|}
specifier|protected
name|PersistentDescriptor
name|createDescriptor
parameter_list|()
block|{
return|return
operator|new
name|PersistentDescriptor
argument_list|()
return|;
block|}
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
name|Class
name|propertyType
init|=
name|attribute
operator|.
name|getJavaClass
argument_list|()
decl_stmt|;
name|Accessor
name|accessor
init|=
name|createAccessor
argument_list|(
name|descriptor
argument_list|,
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|propertyType
argument_list|)
decl_stmt|;
name|descriptor
operator|.
name|addDeclaredProperty
argument_list|(
operator|new
name|SimpleAttributeProperty
argument_list|(
name|descriptor
argument_list|,
name|accessor
argument_list|,
name|attribute
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|void
name|createToOneProperty
parameter_list|(
name|PersistentDescriptor
name|descriptor
parameter_list|,
name|ObjRelationship
name|relationship
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|void
name|createToManyProperty
parameter_list|(
name|PersistentDescriptor
name|descriptor
parameter_list|,
name|ObjRelationship
name|relationship
parameter_list|)
function_decl|;
specifier|protected
name|void
name|indexSubclassDescriptors
parameter_list|(
name|PersistentDescriptor
name|descriptor
parameter_list|,
name|EntityInheritanceTree
name|inheritanceTree
parameter_list|)
block|{
if|if
condition|(
name|inheritanceTree
operator|!=
literal|null
condition|)
block|{
name|Iterator
name|it
init|=
name|inheritanceTree
operator|.
name|getChildren
argument_list|()
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
name|EntityInheritanceTree
name|child
init|=
operator|(
name|EntityInheritanceTree
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|descriptor
operator|.
name|addSubclassDescriptor
argument_list|(
name|descriptorMap
operator|.
name|getDescriptor
argument_list|(
name|child
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|indexSubclassDescriptors
argument_list|(
name|descriptor
argument_list|,
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Creates an accessor for the property.      */
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
name|propertyType
parameter_list|)
throws|throws
name|PropertyException
block|{
return|return
operator|new
name|FieldAccessor
argument_list|(
name|descriptor
operator|.
name|getObjectClass
argument_list|()
argument_list|,
name|propertyName
argument_list|,
name|propertyType
argument_list|)
return|;
block|}
block|}
end_class

end_unit

