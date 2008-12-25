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
name|HashSet
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
name|commons
operator|.
name|collections
operator|.
name|IteratorUtils
import|;
end_import

begin_comment
comment|/**  * A default ClassDescriptor implementation for persistent objects.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|PersistentDescriptor
implements|implements
name|ClassDescriptor
block|{
specifier|static
specifier|final
name|Integer
name|TRANSIENT_STATE
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|PersistenceState
operator|.
name|TRANSIENT
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|Integer
name|HOLLOW_STATE
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|PersistenceState
operator|.
name|HOLLOW
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|Integer
name|COMMITTED_STATE
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|PersistenceState
operator|.
name|COMMITTED
argument_list|)
decl_stmt|;
specifier|protected
name|ClassDescriptor
name|superclassDescriptor
decl_stmt|;
comment|// compiled properties ...
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|objectClass
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Property
argument_list|>
name|declaredProperties
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Property
argument_list|>
name|superProperties
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|ClassDescriptor
argument_list|>
name|subclassDescriptors
decl_stmt|;
specifier|protected
name|Accessor
name|persistenceStateAccessor
decl_stmt|;
specifier|protected
name|ObjEntity
name|entity
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|rootDbEntities
decl_stmt|;
specifier|protected
name|EntityInheritanceTree
name|entityInheritanceTree
decl_stmt|;
comment|// combines declared and super properties
specifier|protected
name|Collection
argument_list|<
name|Property
argument_list|>
name|idProperties
decl_stmt|;
comment|// combines declared and super properties
specifier|protected
name|Collection
argument_list|<
name|ArcProperty
argument_list|>
name|mapArcProperties
decl_stmt|;
comment|// inheritance information
specifier|protected
name|Collection
argument_list|<
name|ObjAttribute
argument_list|>
name|allDiscriminatorColumns
decl_stmt|;
specifier|protected
name|Expression
name|entityQualifier
decl_stmt|;
comment|/**      * Creates a PersistentDescriptor.      */
specifier|public
name|PersistentDescriptor
parameter_list|()
block|{
name|this
operator|.
name|declaredProperties
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Property
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|superProperties
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Property
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|subclassDescriptors
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|ClassDescriptor
argument_list|>
argument_list|()
expr_stmt|;
comment|// must be a set as duplicate addition attempts are expected...
name|this
operator|.
name|rootDbEntities
operator|=
operator|new
name|HashSet
argument_list|<
name|DbEntity
argument_list|>
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDiscriminatorColumns
parameter_list|(
name|Collection
argument_list|<
name|ObjAttribute
argument_list|>
name|columns
parameter_list|)
block|{
if|if
condition|(
name|columns
operator|==
literal|null
operator|||
name|columns
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|allDiscriminatorColumns
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|allDiscriminatorColumns
operator|=
operator|new
name|ArrayList
argument_list|<
name|ObjAttribute
argument_list|>
argument_list|(
name|columns
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Registers a superclass property.      */
specifier|public
name|void
name|addSuperProperty
parameter_list|(
name|Property
name|property
parameter_list|)
block|{
name|superProperties
operator|.
name|put
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|,
name|property
argument_list|)
expr_stmt|;
name|indexAddedProperty
argument_list|(
name|property
argument_list|)
expr_stmt|;
block|}
comment|/**      * Registers a property. This method is useful to customize default ClassDescriptor      * generated from ObjEntity by adding new properties or overriding the standard ones.      */
specifier|public
name|void
name|addDeclaredProperty
parameter_list|(
name|Property
name|property
parameter_list|)
block|{
name|declaredProperties
operator|.
name|put
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|,
name|property
argument_list|)
expr_stmt|;
name|indexAddedProperty
argument_list|(
name|property
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a root DbEntity to the list of roots, filtering duplicates.      */
specifier|public
name|void
name|addRootDbEntity
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|)
block|{
name|this
operator|.
name|rootDbEntities
operator|.
name|add
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
block|}
name|void
name|indexAddedProperty
parameter_list|(
name|Property
name|property
parameter_list|)
block|{
if|if
condition|(
name|property
operator|instanceof
name|AttributeProperty
condition|)
block|{
name|ObjAttribute
name|attribute
init|=
operator|(
operator|(
name|AttributeProperty
operator|)
name|property
operator|)
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|attribute
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
if|if
condition|(
name|idProperties
operator|==
literal|null
condition|)
block|{
name|idProperties
operator|=
operator|new
name|ArrayList
argument_list|<
name|Property
argument_list|>
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|idProperties
operator|.
name|add
argument_list|(
name|property
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|property
operator|instanceof
name|ArcProperty
condition|)
block|{
name|ObjRelationship
name|relationship
init|=
operator|(
operator|(
name|ArcProperty
operator|)
name|property
operator|)
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
name|ObjRelationship
name|reverseRelationship
init|=
name|relationship
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|reverseRelationship
operator|!=
literal|null
operator|&&
literal|"java.util.Map"
operator|.
name|equals
argument_list|(
name|reverseRelationship
operator|.
name|getCollectionType
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|mapArcProperties
operator|==
literal|null
condition|)
block|{
name|mapArcProperties
operator|=
operator|new
name|ArrayList
argument_list|<
name|ArcProperty
argument_list|>
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|mapArcProperties
operator|.
name|add
argument_list|(
operator|(
name|ArcProperty
operator|)
name|property
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Removes declared property. This method can be used to customize default      * ClassDescriptor generated from ObjEntity.      */
specifier|public
name|void
name|removeDeclaredProperty
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|Object
name|removed
init|=
name|declaredProperties
operator|.
name|remove
argument_list|(
name|propertyName
argument_list|)
decl_stmt|;
if|if
condition|(
name|removed
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|idProperties
operator|!=
literal|null
condition|)
block|{
name|idProperties
operator|.
name|remove
argument_list|(
name|removed
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mapArcProperties
operator|!=
literal|null
condition|)
block|{
name|mapArcProperties
operator|.
name|remove
argument_list|(
name|removed
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Adds a subclass descriptor that maps to a given class name.      */
specifier|public
name|void
name|addSubclassDescriptor
parameter_list|(
name|String
name|className
parameter_list|,
name|ClassDescriptor
name|subclassDescriptor
parameter_list|)
block|{
comment|// note that 'className' should be used instead of
comment|// "subclassDescriptor.getEntity().getClassName()", as this method is called in
comment|// the early phases of descriptor initialization and we do not want to trigger
comment|// subclassDescriptor resolution just yet to prevent stack overflow.
name|subclassDescriptors
operator|.
name|put
argument_list|(
name|className
argument_list|,
name|subclassDescriptor
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ObjEntity
name|getEntity
parameter_list|()
block|{
return|return
name|entity
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|getRootDbEntities
parameter_list|()
block|{
return|return
name|rootDbEntities
return|;
block|}
specifier|public
name|boolean
name|isFault
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|superclassDescriptor
operator|!=
literal|null
condition|)
block|{
return|return
name|superclassDescriptor
operator|.
name|isFault
argument_list|(
name|object
argument_list|)
return|;
block|}
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|HOLLOW_STATE
operator|.
name|equals
argument_list|(
name|persistenceStateAccessor
operator|.
name|getValue
argument_list|(
name|object
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getObjectClass
parameter_list|()
block|{
return|return
name|objectClass
return|;
block|}
name|void
name|setObjectClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|objectClass
parameter_list|)
block|{
name|this
operator|.
name|objectClass
operator|=
name|objectClass
expr_stmt|;
block|}
specifier|public
name|ClassDescriptor
name|getSubclassDescriptor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|objectClass
parameter_list|)
block|{
if|if
condition|(
name|objectClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null objectClass"
argument_list|)
throw|;
block|}
if|if
condition|(
name|subclassDescriptors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|this
return|;
block|}
name|ClassDescriptor
name|subclassDescriptor
init|=
name|subclassDescriptors
operator|.
name|get
argument_list|(
name|objectClass
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// ascend via the class hierarchy (only doing it if there are multiple choices)
if|if
condition|(
name|subclassDescriptor
operator|==
literal|null
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|currentClass
init|=
name|objectClass
decl_stmt|;
while|while
condition|(
name|subclassDescriptor
operator|==
literal|null
operator|&&
operator|(
name|currentClass
operator|=
name|currentClass
operator|.
name|getSuperclass
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|subclassDescriptor
operator|=
name|subclassDescriptors
operator|.
name|get
argument_list|(
name|currentClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|subclassDescriptor
operator|!=
literal|null
condition|?
name|subclassDescriptor
else|:
name|this
return|;
block|}
comment|/**      * @deprecated since 3.0. Use {@link #visitProperties(PropertyVisitor)} method      *             instead.      */
specifier|public
name|Iterator
argument_list|<
name|Property
argument_list|>
name|getProperties
parameter_list|()
block|{
name|Iterator
argument_list|<
name|Property
argument_list|>
name|declaredIt
init|=
name|IteratorUtils
operator|.
name|unmodifiableIterator
argument_list|(
name|declaredProperties
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|getSuperclassDescriptor
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|declaredIt
return|;
block|}
else|else
block|{
return|return
name|IteratorUtils
operator|.
name|chainedIterator
argument_list|(
name|superclassDescriptor
operator|.
name|getProperties
argument_list|()
argument_list|,
name|declaredIt
argument_list|)
return|;
block|}
block|}
specifier|public
name|Iterator
argument_list|<
name|ObjAttribute
argument_list|>
name|getDiscriminatorColumns
parameter_list|()
block|{
return|return
name|allDiscriminatorColumns
operator|!=
literal|null
condition|?
name|allDiscriminatorColumns
operator|.
name|iterator
argument_list|()
else|:
name|IteratorUtils
operator|.
name|EMPTY_ITERATOR
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|Property
argument_list|>
name|getIdProperties
parameter_list|()
block|{
if|if
condition|(
name|idProperties
operator|!=
literal|null
condition|)
block|{
return|return
name|idProperties
operator|.
name|iterator
argument_list|()
return|;
block|}
return|return
name|IteratorUtils
operator|.
name|EMPTY_ITERATOR
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|ArcProperty
argument_list|>
name|getMapArcProperties
parameter_list|()
block|{
if|if
condition|(
name|mapArcProperties
operator|!=
literal|null
condition|)
block|{
return|return
name|mapArcProperties
operator|.
name|iterator
argument_list|()
return|;
block|}
return|return
name|IteratorUtils
operator|.
name|EMPTY_ITERATOR
return|;
block|}
comment|/**      * Recursively looks up property descriptor in this class descriptor and all      * superclass descriptors.      */
specifier|public
name|Property
name|getProperty
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|Property
name|property
init|=
name|getDeclaredProperty
argument_list|(
name|propertyName
argument_list|)
decl_stmt|;
if|if
condition|(
name|property
operator|==
literal|null
operator|&&
name|superclassDescriptor
operator|!=
literal|null
condition|)
block|{
name|property
operator|=
name|superclassDescriptor
operator|.
name|getProperty
argument_list|(
name|propertyName
argument_list|)
expr_stmt|;
block|}
return|return
name|property
return|;
block|}
specifier|public
name|Property
name|getDeclaredProperty
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
return|return
name|declaredProperties
operator|.
name|get
argument_list|(
name|propertyName
argument_list|)
return|;
block|}
comment|/**      * Returns a descriptor of the mapped superclass or null if the descriptor's entity      * sits at the top of inheritance hierarchy.      */
specifier|public
name|ClassDescriptor
name|getSuperclassDescriptor
parameter_list|()
block|{
return|return
name|superclassDescriptor
return|;
block|}
comment|/**      * Creates a new instance of a class described by this object.      */
specifier|public
name|Object
name|createObject
parameter_list|()
block|{
if|if
condition|(
name|objectClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null objectClass. Descriptor wasn't initialized properly."
argument_list|)
throw|;
block|}
try|try
block|{
return|return
name|objectClass
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error creating object of class '"
operator|+
name|objectClass
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Invokes 'prepareForAccess' of a super descriptor and then invokes      * 'prepareForAccess' of each declared property.      */
specifier|public
name|void
name|injectValueHolders
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
block|{
comment|// do super first
if|if
condition|(
name|getSuperclassDescriptor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getSuperclassDescriptor
argument_list|()
operator|.
name|injectValueHolders
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Property
name|property
range|:
name|declaredProperties
operator|.
name|values
argument_list|()
control|)
block|{
name|property
operator|.
name|injectValueHolder
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Copies object properties from one object to another. Invokes 'shallowCopy' of a      * super descriptor and then invokes 'shallowCopy' of each declared property.      */
specifier|public
name|void
name|shallowMerge
parameter_list|(
specifier|final
name|Object
name|from
parameter_list|,
specifier|final
name|Object
name|to
parameter_list|)
throws|throws
name|PropertyException
block|{
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
name|property
operator|.
name|writePropertyDirectly
argument_list|(
name|to
argument_list|,
name|property
operator|.
name|readPropertyDirectly
argument_list|(
name|to
argument_list|)
argument_list|,
name|property
operator|.
name|readPropertyDirectly
argument_list|(
name|from
argument_list|)
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
name|property
operator|.
name|invalidate
argument_list|(
name|to
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
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
name|boolean
name|visitSuperProperties
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
block|{
for|for
control|(
name|Property
name|next
range|:
name|superProperties
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|next
operator|.
name|visit
argument_list|(
name|visitor
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|boolean
name|visitDeclaredProperties
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
block|{
for|for
control|(
name|Property
name|next
range|:
name|declaredProperties
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|next
operator|.
name|visit
argument_list|(
name|visitor
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|boolean
name|visitAllProperties
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
block|{
if|if
condition|(
operator|!
name|visitProperties
argument_list|(
name|visitor
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|subclassDescriptors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|ClassDescriptor
name|next
range|:
name|subclassDescriptors
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|next
operator|.
name|visitDeclaredProperties
argument_list|(
name|visitor
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitProperties
parameter_list|(
name|PropertyVisitor
name|visitor
parameter_list|)
block|{
if|if
condition|(
operator|!
name|visitSuperProperties
argument_list|(
name|visitor
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|visitDeclaredProperties
argument_list|(
name|visitor
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPersistenceStateAccessor
parameter_list|(
name|Accessor
name|persistenceStateAccessor
parameter_list|)
block|{
name|this
operator|.
name|persistenceStateAccessor
operator|=
name|persistenceStateAccessor
expr_stmt|;
block|}
specifier|public
name|void
name|setEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|)
block|{
name|this
operator|.
name|entity
operator|=
name|entity
expr_stmt|;
block|}
specifier|public
name|void
name|setSuperclassDescriptor
parameter_list|(
name|ClassDescriptor
name|superclassDescriptor
parameter_list|)
block|{
name|this
operator|.
name|superclassDescriptor
operator|=
name|superclassDescriptor
expr_stmt|;
block|}
specifier|public
name|Expression
name|getEntityQualifier
parameter_list|()
block|{
return|return
name|entityQualifier
return|;
block|}
specifier|public
name|void
name|setEntityQualifier
parameter_list|(
name|Expression
name|entityQualifier
parameter_list|)
block|{
name|this
operator|.
name|entityQualifier
operator|=
name|entityQualifier
expr_stmt|;
block|}
specifier|public
name|EntityInheritanceTree
name|getEntityInheritanceTree
parameter_list|()
block|{
return|return
name|entityInheritanceTree
return|;
block|}
specifier|public
name|void
name|setEntityInheritanceTree
parameter_list|(
name|EntityInheritanceTree
name|entityInheritanceTree
parameter_list|)
block|{
name|this
operator|.
name|entityInheritanceTree
operator|=
name|entityInheritanceTree
expr_stmt|;
block|}
block|}
end_class

end_unit

