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
name|jpa
operator|.
name|map
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Member
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|ObjectContext
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
name|enhancer
operator|.
name|EnhancementHelper
import|;
end_import

begin_comment
comment|/**  * Provides information about a class relevant to JPA, such potential persistence fields,  * etc.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|JpaClassDescriptor
block|{
specifier|private
specifier|static
specifier|final
name|Pattern
name|GETTER_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^(is|get)([A-Z])(.*)$"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|SETTER_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^set([A-Z])(.*)$"
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|reservedProperties
decl_stmt|;
static|static
block|{
name|reservedProperties
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|reservedProperties
operator|.
name|add
argument_list|(
name|propertyKey
argument_list|(
literal|"objectId"
argument_list|,
name|ObjectId
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|reservedProperties
operator|.
name|add
argument_list|(
name|propertyKey
argument_list|(
literal|"persistenceState"
argument_list|,
name|Integer
operator|.
name|TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|reservedProperties
operator|.
name|add
argument_list|(
name|propertyKey
argument_list|(
literal|"objectContext"
argument_list|,
name|ObjectContext
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Collection
argument_list|<
name|JpaPropertyDescriptor
argument_list|>
name|fieldDescriptors
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaPropertyDescriptor
argument_list|>
name|propertyDescriptors
decl_stmt|;
specifier|protected
name|Class
argument_list|<
name|?
argument_list|>
name|managedClass
decl_stmt|;
specifier|protected
name|AccessType
name|access
decl_stmt|;
specifier|static
name|String
name|propertyKey
parameter_list|(
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
name|propertyName
operator|+
literal|':'
operator|+
name|propertyType
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|propertyNameForGetter
parameter_list|(
name|String
name|getterName
parameter_list|)
block|{
name|Matcher
name|getMatch
init|=
name|GETTER_PATTERN
operator|.
name|matcher
argument_list|(
name|getterName
argument_list|)
decl_stmt|;
if|if
condition|(
name|getMatch
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|getMatch
operator|.
name|group
argument_list|(
literal|2
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|+
name|getMatch
operator|.
name|group
argument_list|(
literal|3
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|String
name|propertyNameForSetter
parameter_list|(
name|String
name|setterName
parameter_list|)
block|{
name|Matcher
name|setMatch
init|=
name|SETTER_PATTERN
operator|.
name|matcher
argument_list|(
name|setterName
argument_list|)
decl_stmt|;
if|if
condition|(
name|setMatch
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|setMatch
operator|.
name|group
argument_list|(
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|+
name|setMatch
operator|.
name|group
argument_list|(
literal|2
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|JpaClassDescriptor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|managedClass
parameter_list|)
block|{
name|this
operator|.
name|managedClass
operator|=
name|managedClass
expr_stmt|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getManagedClass
parameter_list|()
block|{
return|return
name|managedClass
return|;
block|}
specifier|public
name|AccessType
name|getAccess
parameter_list|()
block|{
return|return
name|access
return|;
block|}
specifier|public
name|void
name|setAccess
parameter_list|(
name|AccessType
name|access
parameter_list|)
block|{
name|this
operator|.
name|access
operator|=
name|access
expr_stmt|;
block|}
comment|/**      * Returns descriptor matching the property name. If the underlying entity map uses      * FIELD access, a descriptor is looked up in the list of class fields, if it uses      * PROPERTY access - descriptor is looked up in the list of class properties.      */
specifier|public
name|JpaPropertyDescriptor
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|getAccess
argument_list|()
operator|==
name|AccessType
operator|.
name|FIELD
condition|)
block|{
for|for
control|(
name|JpaPropertyDescriptor
name|d
range|:
name|getFieldDescriptors
argument_list|()
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|d
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|d
return|;
block|}
block|}
block|}
if|else if
condition|(
name|getAccess
argument_list|()
operator|==
name|AccessType
operator|.
name|PROPERTY
condition|)
block|{
for|for
control|(
name|JpaPropertyDescriptor
name|d
range|:
name|getPropertyDescriptors
argument_list|()
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|d
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|d
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns descriptor matching the property name. Note that entity map access type is      * ignored and instead field vs. property descriptor is determined from the member      * type.      */
specifier|public
name|JpaPropertyDescriptor
name|getPropertyForMember
parameter_list|(
name|Member
name|classMember
parameter_list|)
block|{
if|if
condition|(
name|classMember
operator|instanceof
name|Field
condition|)
block|{
for|for
control|(
name|JpaPropertyDescriptor
name|d
range|:
name|getFieldDescriptors
argument_list|()
control|)
block|{
if|if
condition|(
name|d
operator|.
name|getMember
argument_list|()
operator|.
name|equals
argument_list|(
name|classMember
argument_list|)
condition|)
block|{
return|return
name|d
return|;
block|}
block|}
block|}
if|else if
condition|(
name|classMember
operator|instanceof
name|Method
condition|)
block|{
for|for
control|(
name|JpaPropertyDescriptor
name|d
range|:
name|getPropertyDescriptors
argument_list|()
control|)
block|{
if|if
condition|(
name|d
operator|.
name|getMember
argument_list|()
operator|.
name|equals
argument_list|(
name|classMember
argument_list|)
condition|)
block|{
return|return
name|d
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|JpaPropertyDescriptor
argument_list|>
name|getFieldDescriptors
parameter_list|()
block|{
if|if
condition|(
name|fieldDescriptors
operator|==
literal|null
condition|)
block|{
name|compileFields
argument_list|()
expr_stmt|;
block|}
return|return
name|fieldDescriptors
return|;
block|}
comment|/**      * Returns getters for public and protected methods that look like read/write bean      * properties, as those are potential persistent properties.      */
specifier|public
name|Collection
argument_list|<
name|JpaPropertyDescriptor
argument_list|>
name|getPropertyDescriptors
parameter_list|()
block|{
if|if
condition|(
name|propertyDescriptors
operator|==
literal|null
condition|)
block|{
name|compileProperties
argument_list|()
expr_stmt|;
block|}
return|return
name|propertyDescriptors
return|;
block|}
specifier|protected
name|void
name|compileFields
parameter_list|()
block|{
name|Field
index|[]
name|fields
init|=
name|managedClass
operator|.
name|getDeclaredFields
argument_list|()
decl_stmt|;
name|fieldDescriptors
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaPropertyDescriptor
argument_list|>
argument_list|(
name|fields
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fields
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|modifiers
init|=
name|fields
index|[
name|i
index|]
operator|.
name|getModifiers
argument_list|()
decl_stmt|;
comment|// skip transient fields (in a Java sense)
if|if
condition|(
name|Modifier
operator|.
name|isTransient
argument_list|(
name|modifiers
argument_list|)
condition|)
block|{
continue|continue;
block|}
comment|// skip static fields
if|if
condition|(
name|Modifier
operator|.
name|isStatic
argument_list|(
name|modifiers
argument_list|)
condition|)
block|{
continue|continue;
block|}
comment|// skip fields created by Cayenne enhancer
if|if
condition|(
name|EnhancementHelper
operator|.
name|isGeneratedField
argument_list|(
name|fields
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|fieldDescriptors
operator|.
name|add
argument_list|(
operator|new
name|JpaPropertyDescriptor
argument_list|(
name|fields
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|compileProperties
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PropertyTuple
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PropertyTuple
argument_list|>
argument_list|()
decl_stmt|;
comment|// per JPA spec, 2.1.1:
comment|// The property accessor methods must be public or protected. When
comment|// property-based access is used, the object/relational mapping annotations for
comment|// the entity class annotate the getter property accessors.
comment|// JPA Spec, 2.1.9.3, regarding non-entity superclasses:
comment|// The non-entity superclass serves for inheritance of behavior only. The state of
comment|// a non-entity superclass is not persistent. Any state inherited from non-entity
comment|// superclasses is non-persistent in an inheriting entity class. This
comment|// non-persistent state is not managed by the EntityManager, nor it is
comment|// required to be retained across transactions. Any annotations on such
comment|// superclasses are ignored.
name|Method
index|[]
name|methods
init|=
name|managedClass
operator|.
name|getDeclaredMethods
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
name|methods
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|modifiers
init|=
name|methods
index|[
name|i
index|]
operator|.
name|getModifiers
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Modifier
operator|.
name|isProtected
argument_list|(
name|modifiers
argument_list|)
operator|&&
operator|!
name|Modifier
operator|.
name|isPublic
argument_list|(
name|modifiers
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|String
name|name
init|=
name|methods
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|parameters
init|=
name|methods
index|[
name|i
index|]
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|returnType
init|=
name|methods
index|[
name|i
index|]
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
name|boolean
name|isVoid
init|=
name|Void
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|returnType
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isVoid
operator|&&
name|parameters
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|String
name|propertyName
init|=
name|propertyNameForGetter
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|propertyName
operator|!=
literal|null
condition|)
block|{
name|String
name|key
init|=
name|propertyKey
argument_list|(
name|propertyName
argument_list|,
name|returnType
argument_list|)
decl_stmt|;
if|if
condition|(
name|reservedProperties
operator|.
name|contains
argument_list|(
name|key
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|PropertyTuple
name|t
init|=
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
name|t
operator|=
operator|new
name|PropertyTuple
argument_list|()
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
name|t
operator|.
name|getter
operator|=
name|methods
index|[
name|i
index|]
expr_stmt|;
name|t
operator|.
name|name
operator|=
name|propertyName
expr_stmt|;
continue|continue;
block|}
block|}
if|if
condition|(
name|isVoid
operator|&&
name|parameters
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|String
name|propertyName
init|=
name|propertyNameForSetter
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|propertyName
operator|!=
literal|null
condition|)
block|{
name|String
name|key
init|=
name|propertyName
operator|+
literal|":"
operator|+
name|parameters
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
decl_stmt|;
name|PropertyTuple
name|t
init|=
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
name|t
operator|=
operator|new
name|PropertyTuple
argument_list|()
expr_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
name|t
operator|.
name|setter
operator|=
name|methods
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
block|}
name|this
operator|.
name|propertyDescriptors
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaPropertyDescriptor
argument_list|>
argument_list|(
name|properties
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|PropertyTuple
name|t
range|:
name|properties
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|t
operator|.
name|getter
operator|!=
literal|null
operator|&&
name|t
operator|.
name|setter
operator|!=
literal|null
condition|)
block|{
name|propertyDescriptors
operator|.
name|add
argument_list|(
operator|new
name|JpaPropertyDescriptor
argument_list|(
name|t
operator|.
name|getter
argument_list|,
name|t
operator|.
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|static
specifier|final
class|class
name|PropertyTuple
block|{
name|String
name|name
decl_stmt|;
name|Method
name|getter
decl_stmt|;
name|Method
name|setter
decl_stmt|;
block|}
block|}
end_class

end_unit

