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
name|Attribute
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
name|EmbeddedAttribute
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
name|Relationship
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
name|getObjEntity
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
argument_list|<
name|?
argument_list|>
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
argument_list|<
name|?
argument_list|>
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
for|for
control|(
name|Attribute
name|attribute
range|:
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getDeclaredAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|attribute
operator|instanceof
name|EmbeddedAttribute
condition|)
block|{
name|EmbeddedAttribute
name|embedded
init|=
operator|(
name|EmbeddedAttribute
operator|)
name|attribute
decl_stmt|;
for|for
control|(
name|ObjAttribute
name|objAttribute
range|:
name|embedded
operator|.
name|getAttributes
argument_list|()
control|)
block|{
name|createEmbeddedAttributeProperty
argument_list|(
name|descriptor
argument_list|,
name|embedded
argument_list|,
name|objAttribute
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
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
block|}
comment|// only include this entity relationships and skip superclasses...
for|for
control|(
name|Relationship
name|relationship
range|:
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getDeclaredRelationships
argument_list|()
control|)
block|{
name|ObjRelationship
name|objRelationship
init|=
operator|(
name|ObjRelationship
operator|)
name|relationship
decl_stmt|;
if|if
condition|(
name|relationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|String
name|collectionType
init|=
name|objRelationship
operator|.
name|getCollectionType
argument_list|()
decl_stmt|;
if|if
condition|(
name|collectionType
operator|==
literal|null
operator|||
name|ObjRelationship
operator|.
name|DEFAULT_COLLECTION_TYPE
operator|.
name|equals
argument_list|(
name|collectionType
argument_list|)
condition|)
block|{
name|createToManyListProperty
argument_list|(
name|descriptor
argument_list|,
name|objRelationship
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|collectionType
operator|.
name|equals
argument_list|(
literal|"java.util.Map"
argument_list|)
condition|)
block|{
name|createToManyMapProperty
argument_list|(
name|descriptor
argument_list|,
name|objRelationship
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|collectionType
operator|.
name|equals
argument_list|(
literal|"java.util.Set"
argument_list|)
condition|)
block|{
name|createToManySetProperty
argument_list|(
name|descriptor
argument_list|,
name|objRelationship
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|collectionType
operator|.
name|equals
argument_list|(
literal|"java.util.Collection"
argument_list|)
condition|)
block|{
name|createToManyCollectionProperty
argument_list|(
name|descriptor
argument_list|,
name|objRelationship
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported to-many collection type: "
operator|+
name|collectionType
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|createToOneProperty
argument_list|(
name|descriptor
argument_list|,
name|objRelationship
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
argument_list|<
name|?
argument_list|>
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
name|void
name|createEmbeddedAttributeProperty
parameter_list|(
name|PersistentDescriptor
name|descriptor
parameter_list|,
name|EmbeddedAttribute
name|embeddedAttribute
parameter_list|,
name|ObjAttribute
name|attribute
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|embeddableClass
init|=
name|embeddedAttribute
operator|.
name|getJavaClass
argument_list|()
decl_stmt|;
name|String
name|propertyPath
init|=
name|attribute
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|lastDot
init|=
name|propertyPath
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastDot
operator|<=
literal|0
operator|||
name|lastDot
operator|==
name|propertyPath
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid embeddable path: "
operator|+
name|propertyPath
argument_list|)
throw|;
block|}
name|String
name|embeddableName
init|=
name|propertyPath
operator|.
name|substring
argument_list|(
name|lastDot
operator|+
literal|1
argument_list|)
decl_stmt|;
name|Accessor
name|embeddedAccessor
init|=
name|createAccessor
argument_list|(
name|descriptor
argument_list|,
name|embeddedAttribute
operator|.
name|getName
argument_list|()
argument_list|,
name|embeddableClass
argument_list|)
decl_stmt|;
name|Accessor
name|embeddedableAccessor
init|=
name|createEmbeddableAccessor
argument_list|(
name|embeddableClass
argument_list|,
name|embeddableName
argument_list|,
name|attribute
operator|.
name|getJavaClass
argument_list|()
argument_list|)
decl_stmt|;
name|EmbeddableDescriptor
name|embeddableDescriptor
init|=
name|createEmbeddableDescriptor
argument_list|(
name|embeddedAttribute
argument_list|)
decl_stmt|;
name|Accessor
name|accessor
init|=
operator|new
name|EmbeddedFieldAccessor
argument_list|(
name|embeddableDescriptor
argument_list|,
name|embeddedAccessor
argument_list|,
name|embeddedableAccessor
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
name|createToManySetProperty
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
name|createToManyMapProperty
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
name|createToManyListProperty
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
name|createToManyCollectionProperty
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
for|for
control|(
name|EntityInheritanceTree
name|child
range|:
name|inheritanceTree
operator|.
name|getChildren
argument_list|()
control|)
block|{
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
comment|/**      * Creates an accessor to read a map key for a given relationship.      */
specifier|protected
name|Accessor
name|createMapKeyAccessor
parameter_list|(
name|ObjRelationship
name|relationship
parameter_list|,
name|ClassDescriptor
name|targetDescriptor
parameter_list|)
block|{
name|String
name|mapKey
init|=
name|relationship
operator|.
name|getMapKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|mapKey
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|PropertyAccessor
argument_list|(
name|targetDescriptor
operator|.
name|getProperty
argument_list|(
name|mapKey
argument_list|)
argument_list|)
return|;
block|}
return|return
name|IdMapKeyAccessor
operator|.
name|SHARED_ACCESSOR
return|;
block|}
comment|/**      * Creates an accessor for the property of the embeddable class.      */
specifier|protected
name|Accessor
name|createEmbeddableAccessor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|embeddableClass
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
block|{
return|return
operator|new
name|FieldAccessor
argument_list|(
name|embeddableClass
argument_list|,
name|propertyName
argument_list|,
name|propertyType
argument_list|)
return|;
block|}
specifier|protected
name|EmbeddableDescriptor
name|createEmbeddableDescriptor
parameter_list|(
name|EmbeddedAttribute
name|embeddedAttribute
parameter_list|)
block|{
comment|// TODO: andrus, 11/19/2007 = avoid creation of descriptor for every property of
comment|// embeddable; look up reusable descriptor instead.
return|return
operator|new
name|FieldEmbeddableDescriptor
argument_list|(
name|embeddedAttribute
operator|.
name|getEmbeddable
argument_list|()
argument_list|,
literal|"owner"
argument_list|,
literal|"embeddedProperty"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

