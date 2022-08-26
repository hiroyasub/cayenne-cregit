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
package|;
end_package

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
name|dba
operator|.
name|TypesMapping
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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|TraversalHelper
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
name|DbRelationship
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
name|util
operator|.
name|CayenneMapEntry
import|;
end_import

begin_comment
comment|/**  * A convenience superclass for {@link ClassDescriptorFactory} implementors.  *   * @since 3.0  */
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
literal|"Unmapped entity: %s"
argument_list|,
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
name|descriptorMap
operator|.
name|getResolver
argument_list|()
operator|.
name|getObjectFactory
argument_list|()
operator|.
name|getJavaClass
argument_list|(
name|entity
operator|.
name|getJavaClassName
argument_list|()
argument_list|)
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
name|ObjAttribute
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
else|else
block|{
name|createAttributeProperty
argument_list|(
name|descriptor
argument_list|,
name|attribute
argument_list|)
expr_stmt|;
block|}
block|}
comment|// only include this entity relationships and skip superclasses...
for|for
control|(
name|ObjRelationship
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
name|relationship
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
name|relationship
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"java.util.Map"
operator|.
name|equals
argument_list|(
name|collectionType
argument_list|)
condition|)
block|{
name|createToManyMapProperty
argument_list|(
name|descriptor
argument_list|,
name|relationship
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"java.util.Set"
operator|.
name|equals
argument_list|(
name|collectionType
argument_list|)
condition|)
block|{
name|createToManySetProperty
argument_list|(
name|descriptor
argument_list|,
name|relationship
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"java.util.Collection"
operator|.
name|equals
argument_list|(
name|collectionType
argument_list|)
condition|)
block|{
name|createToManyCollectionProperty
argument_list|(
name|descriptor
argument_list|,
name|relationship
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
name|getInheritanceTree
argument_list|(
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|descriptor
operator|.
name|setEntityInheritanceTree
argument_list|(
name|inheritanceTree
argument_list|)
expr_stmt|;
name|indexSubclassDescriptors
argument_list|(
name|descriptor
argument_list|,
name|inheritanceTree
argument_list|)
expr_stmt|;
name|indexQualifiers
argument_list|(
name|descriptor
argument_list|,
name|inheritanceTree
argument_list|)
expr_stmt|;
name|appendDeclaredRootDbEntity
argument_list|(
name|descriptor
argument_list|,
name|descriptor
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
name|indexRootDbEntities
argument_list|(
name|descriptor
argument_list|,
name|inheritanceTree
argument_list|)
expr_stmt|;
name|indexSuperclassProperties
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
name|indexAdditionalDbEntities
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
name|descriptor
operator|.
name|sortProperties
argument_list|()
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
name|EmbeddableDescriptor
name|embeddableDescriptor
init|=
name|createEmbeddableDescriptor
argument_list|(
name|embeddedAttribute
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
name|embeddableDescriptor
argument_list|,
name|embeddableName
argument_list|,
name|attribute
operator|.
name|getJavaClass
argument_list|()
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
name|ObjEntity
name|childEntity
init|=
name|child
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|descriptor
operator|.
name|addSubclassDescriptor
argument_list|(
name|childEntity
operator|.
name|getClassName
argument_list|()
argument_list|,
name|descriptorMap
operator|.
name|getDescriptor
argument_list|(
name|childEntity
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
specifier|protected
name|void
name|indexRootDbEntities
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
name|ObjEntity
name|childEntity
init|=
name|child
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|appendDeclaredRootDbEntity
argument_list|(
name|descriptor
argument_list|,
name|childEntity
argument_list|)
expr_stmt|;
name|indexRootDbEntities
argument_list|(
name|descriptor
argument_list|,
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|appendDeclaredRootDbEntity
parameter_list|(
name|PersistentDescriptor
name|descriptor
parameter_list|,
name|ObjEntity
name|entity
parameter_list|)
block|{
name|DbEntity
name|dbEntity
init|=
name|entity
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbEntity
operator|!=
literal|null
condition|)
block|{
comment|// descriptor takes care of weeding off duplicates, which are likely
comment|// in cases
comment|// of non-horizontal inheritance
name|descriptor
operator|.
name|addRootDbEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|indexQualifiers
parameter_list|(
specifier|final
name|PersistentDescriptor
name|descriptor
parameter_list|,
name|EntityInheritanceTree
name|inheritanceTree
parameter_list|)
block|{
name|Expression
name|qualifier
decl_stmt|;
if|if
condition|(
name|inheritanceTree
operator|!=
literal|null
condition|)
block|{
name|qualifier
operator|=
name|inheritanceTree
operator|.
name|qualifierForEntityAndSubclasses
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|qualifier
operator|=
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getDeclaredQualifier
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|qualifier
operator|!=
literal|null
condition|)
block|{
comment|// using map instead of a Set to collect attributes, as
comment|// ObjEntity.getAttribute may return a decorator for attribute on
comment|// each call, resulting in dupes
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ObjAttribute
argument_list|>
name|attributes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
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
name|qualifier
operator|.
name|traverse
argument_list|(
operator|new
name|TraversalHelper
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|startNode
parameter_list|(
name|Expression
name|node
parameter_list|,
name|Expression
name|parentNode
parameter_list|)
block|{
if|if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|DB_PATH
condition|)
block|{
name|String
name|path
init|=
name|node
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
specifier|final
name|DbAttribute
name|attribute
init|=
name|dbEntity
operator|.
name|getAttribute
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
name|ObjAttribute
name|objectAttribute
init|=
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getAttributeForDbAttribute
argument_list|(
name|attribute
argument_list|)
decl_stmt|;
if|if
condition|(
name|objectAttribute
operator|==
literal|null
condition|)
block|{
name|objectAttribute
operator|=
operator|new
name|ObjAttribute
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|DbAttribute
name|getDbAttribute
parameter_list|()
block|{
return|return
name|attribute
return|;
block|}
block|}
expr_stmt|;
comment|// we semi-officially DO NOT support inheritance
comment|// descriptors based on related entities, so
comment|// here we
comment|// assume that DbAttribute is rooted in the root
comment|// DbEntity, and no relationship is involved.
name|objectAttribute
operator|.
name|setDbAttributePath
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|objectAttribute
operator|.
name|setType
argument_list|(
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|attribute
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|attributes
operator|.
name|put
argument_list|(
name|objectAttribute
operator|.
name|getName
argument_list|()
argument_list|,
name|objectAttribute
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|node
operator|.
name|getType
argument_list|()
operator|==
name|Expression
operator|.
name|OBJ_PATH
condition|)
block|{
name|String
name|path
init|=
name|node
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ObjAttribute
name|attribute
init|=
name|descriptor
operator|.
name|getEntity
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|attributes
operator|.
name|put
argument_list|(
name|path
argument_list|,
name|attribute
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|descriptor
operator|.
name|setDiscriminatorColumns
argument_list|(
name|attributes
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|descriptor
operator|.
name|setEntityQualifier
argument_list|(
name|qualifier
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Adds superclass properties to the descriptor, applying proper overrides.      */
specifier|protected
name|void
name|indexSuperclassProperties
parameter_list|(
specifier|final
name|PersistentDescriptor
name|descriptor
parameter_list|)
block|{
name|ClassDescriptor
name|superDescriptor
init|=
name|descriptor
operator|.
name|getSuperclassDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|superDescriptor
operator|!=
literal|null
condition|)
block|{
name|superDescriptor
operator|.
name|visitProperties
argument_list|(
operator|new
name|PropertyVisitor
argument_list|()
block|{
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
comment|// decorate super property to return an overridden attribute
name|descriptor
operator|.
name|addSuperProperty
argument_list|(
operator|new
name|AttributePropertyDecorator
argument_list|(
name|descriptor
argument_list|,
name|property
argument_list|)
argument_list|)
expr_stmt|;
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
name|descriptor
operator|.
name|addSuperProperty
argument_list|(
name|property
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
name|descriptor
operator|.
name|addSuperProperty
argument_list|(
name|property
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|indexAdditionalDbEntities
parameter_list|(
specifier|final
name|PersistentDescriptor
name|descriptor
parameter_list|)
block|{
name|descriptor
operator|.
name|visitProperties
argument_list|(
operator|new
name|PropertyVisitor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
if|if
condition|(
operator|!
name|property
operator|.
name|getAttribute
argument_list|()
operator|.
name|isFlattened
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|it
init|=
name|property
operator|.
name|getAttribute
argument_list|()
operator|.
name|getDbPathIterator
argument_list|()
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
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
name|CayenneMapEntry
name|next
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|DbRelationship
condition|)
block|{
name|DbRelationship
name|rel
init|=
operator|(
name|DbRelationship
operator|)
name|next
decl_stmt|;
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|descriptor
operator|.
name|addAdditionalDbEntity
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|,
name|rel
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
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
name|getRelationship
argument_list|()
operator|.
name|isFlattened
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|dbRelationships
init|=
name|property
operator|.
name|getRelationship
argument_list|()
operator|.
name|getDbRelationships
argument_list|()
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|count
init|=
name|dbRelationships
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|DbRelationship
name|rel
init|=
name|dbRelationships
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|descriptor
operator|.
name|addAdditionalDbEntity
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|,
name|rel
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
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
name|EmbeddableDescriptor
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
comment|/**      * Creates a descriptor of the embedded property.      */
specifier|protected
name|EmbeddableDescriptor
name|createEmbeddableDescriptor
parameter_list|(
name|EmbeddedAttribute
name|embeddedAttribute
parameter_list|)
block|{
comment|// TODO: andrus, 11/19/2007 = avoid creation of descriptor for every property of embeddable;
comment|//       look up reusable descriptor instead.
return|return
operator|new
name|FieldEmbeddableDescriptor
argument_list|(
name|descriptorMap
operator|.
name|getResolver
argument_list|()
operator|.
name|getObjectFactory
argument_list|()
argument_list|,
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

