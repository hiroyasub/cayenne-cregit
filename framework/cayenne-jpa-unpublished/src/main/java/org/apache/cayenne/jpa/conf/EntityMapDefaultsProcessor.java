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
name|conf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Time
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
name|Date
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EnumType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|TemporalType
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
name|jpa
operator|.
name|JpaProviderException
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
name|jpa
operator|.
name|map
operator|.
name|AccessType
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
name|jpa
operator|.
name|map
operator|.
name|JpaAbstractEntity
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
name|jpa
operator|.
name|map
operator|.
name|JpaAttribute
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
name|jpa
operator|.
name|map
operator|.
name|JpaAttributes
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
name|jpa
operator|.
name|map
operator|.
name|JpaBasic
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
name|jpa
operator|.
name|map
operator|.
name|JpaClassDescriptor
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
name|jpa
operator|.
name|map
operator|.
name|JpaColumn
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
name|jpa
operator|.
name|map
operator|.
name|JpaEntity
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
name|jpa
operator|.
name|map
operator|.
name|JpaEntityMap
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
name|jpa
operator|.
name|map
operator|.
name|JpaId
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
name|jpa
operator|.
name|map
operator|.
name|JpaJoinColumn
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
name|jpa
operator|.
name|map
operator|.
name|JpaManagedClass
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
name|jpa
operator|.
name|map
operator|.
name|JpaManyToMany
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
name|jpa
operator|.
name|map
operator|.
name|JpaManyToOne
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
name|jpa
operator|.
name|map
operator|.
name|JpaMappedSuperclass
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
name|jpa
operator|.
name|map
operator|.
name|JpaOneToMany
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
name|jpa
operator|.
name|map
operator|.
name|JpaOneToOne
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
name|jpa
operator|.
name|map
operator|.
name|JpaPropertyDescriptor
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
name|jpa
operator|.
name|map
operator|.
name|JpaRelationship
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
name|jpa
operator|.
name|map
operator|.
name|JpaTable
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
name|jpa
operator|.
name|map
operator|.
name|JpaVersion
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
name|project
operator|.
name|ProjectPath
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
name|BaseTreeVisitor
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
name|HierarchicalTreeVisitor
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
name|TraversalUtil
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
name|Util
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
name|validation
operator|.
name|SimpleValidationFailure
import|;
end_import

begin_comment
comment|/**  * Initializes JPA specification compatible mapping defaults.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|EntityMapDefaultsProcessor
block|{
specifier|protected
name|HierarchicalTreeVisitor
name|visitor
decl_stmt|;
specifier|protected
specifier|transient
name|EntityMapLoaderContext
name|context
decl_stmt|;
specifier|public
name|void
name|applyDefaults
parameter_list|(
name|EntityMapLoaderContext
name|context
parameter_list|)
throws|throws
name|JpaProviderException
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
if|if
condition|(
name|visitor
operator|==
literal|null
condition|)
block|{
name|visitor
operator|=
name|createVisitor
argument_list|()
expr_stmt|;
block|}
name|TraversalUtil
operator|.
name|traverse
argument_list|(
name|context
operator|.
name|getEntityMap
argument_list|()
argument_list|,
name|visitor
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a stateless instance of the JpaEntityMap traversal visitor. This method is      * lazily invoked and cached by this object.      */
specifier|protected
name|HierarchicalTreeVisitor
name|createVisitor
parameter_list|()
block|{
return|return
operator|new
name|EntityMapVisitor
argument_list|()
return|;
block|}
specifier|abstract
class|class
name|AbstractEntityVisitor
extends|extends
name|BaseTreeVisitor
block|{
name|AbstractEntityVisitor
parameter_list|()
block|{
name|BaseTreeVisitor
name|attributeVisitor
init|=
operator|new
name|BaseTreeVisitor
argument_list|()
decl_stmt|;
name|attributeVisitor
operator|.
name|addChildVisitor
argument_list|(
name|JpaId
operator|.
name|class
argument_list|,
operator|new
name|IdVisitor
argument_list|()
argument_list|)
expr_stmt|;
name|attributeVisitor
operator|.
name|addChildVisitor
argument_list|(
name|JpaBasic
operator|.
name|class
argument_list|,
operator|new
name|BasicVisitor
argument_list|()
argument_list|)
expr_stmt|;
name|attributeVisitor
operator|.
name|addChildVisitor
argument_list|(
name|JpaVersion
operator|.
name|class
argument_list|,
operator|new
name|VersionVisitor
argument_list|()
argument_list|)
expr_stmt|;
name|attributeVisitor
operator|.
name|addChildVisitor
argument_list|(
name|JpaManyToOne
operator|.
name|class
argument_list|,
operator|new
name|ManyToOneVisitor
argument_list|()
argument_list|)
expr_stmt|;
name|attributeVisitor
operator|.
name|addChildVisitor
argument_list|(
name|JpaOneToOne
operator|.
name|class
argument_list|,
operator|new
name|OneToOneVisitor
argument_list|()
argument_list|)
expr_stmt|;
name|attributeVisitor
operator|.
name|addChildVisitor
argument_list|(
name|JpaOneToMany
operator|.
name|class
argument_list|,
operator|new
name|RelationshipVisitor
argument_list|()
argument_list|)
expr_stmt|;
name|attributeVisitor
operator|.
name|addChildVisitor
argument_list|(
name|JpaManyToMany
operator|.
name|class
argument_list|,
operator|new
name|RelationshipVisitor
argument_list|()
argument_list|)
expr_stmt|;
name|addChildVisitor
argument_list|(
name|JpaAttributes
operator|.
name|class
argument_list|,
name|attributeVisitor
argument_list|)
expr_stmt|;
name|addChildVisitor
argument_list|(
name|JpaId
operator|.
name|class
argument_list|,
operator|new
name|IdVisitor
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|onStartNode
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
block|{
name|JpaAbstractEntity
name|abstractEntity
init|=
operator|(
name|JpaAbstractEntity
operator|)
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
comment|// * entity name
if|if
condition|(
name|abstractEntity
operator|.
name|getClassName
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|abstractEntity
operator|instanceof
name|JpaEntity
condition|)
block|{
name|JpaEntity
name|entity
init|=
operator|(
name|JpaEntity
operator|)
name|abstractEntity
decl_stmt|;
if|if
condition|(
name|entity
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// use unqualified class name
name|String
name|fqName
init|=
name|abstractEntity
operator|.
name|getClassName
argument_list|()
decl_stmt|;
name|int
name|split
init|=
name|fqName
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setName
argument_list|(
name|split
operator|>
literal|0
condition|?
name|fqName
operator|.
name|substring
argument_list|(
name|split
operator|+
literal|1
argument_list|)
else|:
name|fqName
argument_list|)
expr_stmt|;
block|}
comment|// * default table (see @Table annotation defaults, JPA spec 9.1.1)
if|if
condition|(
name|entity
operator|.
name|getTable
argument_list|()
operator|==
literal|null
condition|)
block|{
name|JpaTable
name|table
init|=
operator|new
name|JpaTable
argument_list|(
name|AnnotationPrototypes
operator|.
name|getTable
argument_list|()
argument_list|)
decl_stmt|;
comment|// unclear whether we need to apply any other name transformations ...
comment|// or even if we need to uppercase the name. Per default examples
comment|// looks
comment|// like we need. table.setName(entity.getName().toUpperCase());
name|table
operator|.
name|setName
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setTable
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|abstractEntity
operator|.
name|getAttributes
argument_list|()
operator|==
literal|null
condition|)
block|{
name|abstractEntity
operator|.
name|setAttributes
argument_list|(
operator|new
name|JpaAttributes
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// * default persistent fields
name|JpaClassDescriptor
name|descriptor
init|=
name|abstractEntity
operator|.
name|getClassDescriptor
argument_list|()
decl_stmt|;
name|AccessType
name|access
init|=
name|abstractEntity
operator|.
name|getAccess
argument_list|()
decl_stmt|;
if|if
condition|(
name|access
operator|==
literal|null
condition|)
block|{
name|access
operator|=
operator|(
operator|(
name|JpaEntityMap
operator|)
name|path
operator|.
name|getRoot
argument_list|()
operator|)
operator|.
name|getAccess
argument_list|()
expr_stmt|;
name|abstractEntity
operator|.
name|setAccess
argument_list|(
name|access
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|access
operator|==
name|AccessType
operator|.
name|PROPERTY
condition|)
block|{
for|for
control|(
name|JpaPropertyDescriptor
name|candidate
range|:
name|descriptor
operator|.
name|getPropertyDescriptors
argument_list|()
control|)
block|{
name|processProperty
argument_list|(
name|abstractEntity
argument_list|,
name|descriptor
argument_list|,
name|candidate
argument_list|)
expr_stmt|;
block|}
block|}
comment|// field is default...
else|else
block|{
for|for
control|(
name|JpaPropertyDescriptor
name|candidate
range|:
name|descriptor
operator|.
name|getFieldDescriptors
argument_list|()
control|)
block|{
name|processProperty
argument_list|(
name|abstractEntity
argument_list|,
name|descriptor
argument_list|,
name|candidate
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
name|void
name|processProperty
parameter_list|(
name|JpaAbstractEntity
name|entity
parameter_list|,
name|JpaClassDescriptor
name|descriptor
parameter_list|,
name|JpaPropertyDescriptor
name|property
parameter_list|)
block|{
name|JpaAttributes
name|attributes
init|=
name|entity
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|attributes
operator|.
name|getAttribute
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|property
operator|.
name|isDefaultNonRelationalType
argument_list|()
condition|)
block|{
name|JpaBasic
name|attribute
init|=
operator|new
name|JpaBasic
argument_list|()
decl_stmt|;
name|attribute
operator|.
name|setPropertyDescriptor
argument_list|(
name|property
argument_list|)
expr_stmt|;
name|attribute
operator|.
name|setName
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|getBasicAttributes
argument_list|()
operator|.
name|add
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|path
init|=
name|descriptor
operator|.
name|getManagedClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|property
operator|.
name|getName
argument_list|()
decl_stmt|;
name|context
operator|.
name|recordConflict
argument_list|(
operator|new
name|SimpleValidationFailure
argument_list|(
name|property
operator|.
name|getMember
argument_list|()
argument_list|,
literal|"Undefined property persistence status: "
operator|+
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
class|class
name|BasicVisitor
extends|extends
name|BaseTreeVisitor
block|{
name|BasicVisitor
parameter_list|()
block|{
name|addChildVisitor
argument_list|(
name|JpaColumn
operator|.
name|class
argument_list|,
operator|new
name|ColumnVisitor
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|onStartNode
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
block|{
name|JpaBasic
name|jpaBasic
init|=
operator|(
name|JpaBasic
operator|)
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|jpaBasic
operator|.
name|getColumn
argument_list|()
operator|==
literal|null
condition|)
block|{
name|JpaColumn
name|column
init|=
operator|new
name|JpaColumn
argument_list|(
name|AnnotationPrototypes
operator|.
name|getColumn
argument_list|()
argument_list|)
decl_stmt|;
name|column
operator|.
name|setName
argument_list|(
name|jpaBasic
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|column
operator|.
name|setNullable
argument_list|(
name|jpaBasic
operator|.
name|isOptional
argument_list|()
argument_list|)
expr_stmt|;
name|jpaBasic
operator|.
name|setColumn
argument_list|(
name|column
argument_list|)
expr_stmt|;
block|}
name|JpaAbstractEntity
name|entity
init|=
operator|(
name|JpaAbstractEntity
operator|)
name|path
operator|.
name|firstInstanceOf
argument_list|(
name|JpaAbstractEntity
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// process temporal type defaults
if|if
condition|(
name|jpaBasic
operator|.
name|getTemporal
argument_list|()
operator|==
literal|null
operator|&&
name|jpaBasic
operator|.
name|getEnumerated
argument_list|()
operator|==
literal|null
condition|)
block|{
name|JpaClassDescriptor
name|descriptor
init|=
name|entity
operator|.
name|getClassDescriptor
argument_list|()
decl_stmt|;
name|JpaPropertyDescriptor
name|property
init|=
name|descriptor
operator|.
name|getProperty
argument_list|(
name|jpaBasic
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// sanity check
if|if
condition|(
name|property
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No class property found for name: "
operator|+
name|jpaBasic
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|java
operator|.
name|sql
operator|.
name|Date
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|property
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|jpaBasic
operator|.
name|setTemporal
argument_list|(
name|TemporalType
operator|.
name|DATE
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|Time
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|property
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|jpaBasic
operator|.
name|setTemporal
argument_list|(
name|TemporalType
operator|.
name|TIME
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|Timestamp
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|property
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|jpaBasic
operator|.
name|setTemporal
argument_list|(
name|TemporalType
operator|.
name|TIMESTAMP
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|Date
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|property
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|jpaBasic
operator|.
name|setTemporal
argument_list|(
name|TemporalType
operator|.
name|TIMESTAMP
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|property
operator|.
name|getType
argument_list|()
operator|.
name|isEnum
argument_list|()
condition|)
block|{
name|jpaBasic
operator|.
name|setEnumerated
argument_list|(
name|EnumType
operator|.
name|ORDINAL
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
class|class
name|VersionVisitor
extends|extends
name|BasicVisitor
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|onStartNode
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
block|{
name|JpaVersion
name|jpaBasic
init|=
operator|(
name|JpaVersion
operator|)
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|jpaBasic
operator|.
name|getColumn
argument_list|()
operator|==
literal|null
condition|)
block|{
name|JpaColumn
name|column
init|=
operator|new
name|JpaColumn
argument_list|(
name|AnnotationPrototypes
operator|.
name|getColumn
argument_list|()
argument_list|)
decl_stmt|;
name|column
operator|.
name|setName
argument_list|(
name|jpaBasic
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jpaBasic
operator|.
name|setColumn
argument_list|(
name|column
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jpaBasic
operator|.
name|getTemporal
argument_list|()
operator|==
literal|null
condition|)
block|{
name|JpaEntity
name|entity
init|=
operator|(
name|JpaEntity
operator|)
name|path
operator|.
name|firstInstanceOf
argument_list|(
name|JpaEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|JpaClassDescriptor
name|descriptor
init|=
name|entity
operator|.
name|getClassDescriptor
argument_list|()
decl_stmt|;
name|JpaPropertyDescriptor
name|property
init|=
name|descriptor
operator|.
name|getProperty
argument_list|(
name|jpaBasic
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|Date
operator|.
name|class
operator|.
name|equals
argument_list|(
name|property
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|jpaBasic
operator|.
name|setTemporal
argument_list|(
name|TemporalType
operator|.
name|TIMESTAMP
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
specifier|final
class|class
name|ColumnVisitor
extends|extends
name|BaseTreeVisitor
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|onStartNode
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
block|{
name|JpaColumn
name|column
init|=
operator|(
name|JpaColumn
operator|)
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|JpaAttribute
name|parent
init|=
name|path
operator|.
name|firstInstanceOf
argument_list|(
name|JpaAttribute
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|column
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
name|column
operator|.
name|setName
argument_list|(
name|parent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|column
operator|.
name|getTable
argument_list|()
operator|==
literal|null
condition|)
block|{
name|JpaEntity
name|entity
init|=
name|path
operator|.
name|firstInstanceOf
argument_list|(
name|JpaEntity
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// parent can be a mapped superclass
if|if
condition|(
name|entity
operator|!=
literal|null
condition|)
block|{
name|column
operator|.
name|setTable
argument_list|(
name|entity
operator|.
name|getTable
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|parent
operator|.
name|getPropertyDescriptor
argument_list|()
operator|.
name|isStringType
argument_list|()
condition|)
block|{
if|if
condition|(
name|column
operator|.
name|getLength
argument_list|()
operator|<=
literal|0
condition|)
block|{
name|column
operator|.
name|setLength
argument_list|(
name|JpaColumn
operator|.
name|DEFAULT_LENGTH
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// length for non-string types should be ignored...
name|column
operator|.
name|setLength
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
specifier|final
class|class
name|EntityMapVisitor
extends|extends
name|BaseTreeVisitor
block|{
name|EntityMapVisitor
parameter_list|()
block|{
name|addChildVisitor
argument_list|(
name|JpaEntity
operator|.
name|class
argument_list|,
operator|new
name|EntityVisitor
argument_list|()
argument_list|)
expr_stmt|;
name|addChildVisitor
argument_list|(
name|JpaMappedSuperclass
operator|.
name|class
argument_list|,
operator|new
name|MappedSuperclassVisitor
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|onStartNode
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
block|{
name|JpaEntityMap
name|entityMap
init|=
operator|(
name|JpaEntityMap
operator|)
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
comment|// TODO: andrus, 4/28/2006 - actually we need to analyze preloaded classes and
comment|// see how they were annotated to choose the right access type...
name|entityMap
operator|.
name|setAccess
argument_list|(
name|AccessType
operator|.
name|FIELD
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
specifier|final
class|class
name|EntityVisitor
extends|extends
name|AbstractEntityVisitor
block|{      }
specifier|final
class|class
name|IdVisitor
extends|extends
name|BaseTreeVisitor
block|{
name|IdVisitor
parameter_list|()
block|{
name|addChildVisitor
argument_list|(
name|JpaColumn
operator|.
name|class
argument_list|,
operator|new
name|ColumnVisitor
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|onStartNode
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
block|{
name|JpaId
name|id
init|=
operator|(
name|JpaId
operator|)
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|.
name|getColumn
argument_list|()
operator|==
literal|null
condition|)
block|{
name|JpaColumn
name|column
init|=
operator|new
name|JpaColumn
argument_list|(
name|AnnotationPrototypes
operator|.
name|getColumn
argument_list|()
argument_list|)
decl_stmt|;
name|column
operator|.
name|setName
argument_list|(
name|id
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|JpaEntity
name|entity
init|=
operator|(
name|JpaEntity
operator|)
name|path
operator|.
name|firstInstanceOf
argument_list|(
name|JpaEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|column
operator|.
name|setTable
argument_list|(
name|entity
operator|.
name|getTable
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|id
operator|.
name|setColumn
argument_list|(
name|column
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
specifier|final
class|class
name|JoinColumnVisitor
extends|extends
name|BaseTreeVisitor
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|onStartNode
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
block|{
name|JpaRelationship
name|relationship
init|=
operator|(
name|JpaRelationship
operator|)
name|path
operator|.
name|getObjectParent
argument_list|()
decl_stmt|;
name|JpaJoinColumn
name|column
init|=
operator|(
name|JpaJoinColumn
operator|)
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|column
operator|.
name|getTable
argument_list|()
operator|==
literal|null
condition|)
block|{
name|JpaEntity
name|entity
init|=
name|path
operator|.
name|firstInstanceOf
argument_list|(
name|JpaEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|column
operator|.
name|setTable
argument_list|(
name|entity
operator|.
name|getTable
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// JPA Spec, 2.1.8.2 (same for all relationship owners):
comment|// The following mapping defaults apply: [...]
comment|// Table A contains a foreign key to table B. The foreign key column
comment|// name is formed as the concatenation of the following: the name of
comment|// the relationship property or field of entityA; "_" ; the name of
comment|// the primary key column in table B. The foreign key column has the
comment|// same type as the primary key of table B.
name|JpaEntityMap
name|map
init|=
name|path
operator|.
name|firstInstanceOf
argument_list|(
name|JpaEntityMap
operator|.
name|class
argument_list|)
decl_stmt|;
name|JpaEntity
name|target
init|=
name|map
operator|.
name|entityForClass
argument_list|(
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|target
operator|==
literal|null
condition|)
block|{
name|context
operator|.
name|recordConflict
argument_list|(
operator|new
name|SimpleValidationFailure
argument_list|(
name|relationship
argument_list|,
literal|"Invalid relationship target "
operator|+
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|target
operator|.
name|getAttributes
argument_list|()
operator|==
literal|null
operator|||
name|target
operator|.
name|getAttributes
argument_list|()
operator|.
name|getIds
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|context
operator|.
name|recordConflict
argument_list|(
operator|new
name|SimpleValidationFailure
argument_list|(
name|target
argument_list|,
literal|"Relationship target has no PK defined: "
operator|+
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|target
operator|.
name|getAttributes
argument_list|()
operator|.
name|getIds
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
comment|// TODO: andrus, 4/30/2006 implement this; note that instead of
comment|// checking for "attribute.getJoinColumns().isEmpty()" above,
comment|// we'll have to match individual columns
name|context
operator|.
name|recordConflict
argument_list|(
operator|new
name|SimpleValidationFailure
argument_list|(
name|relationship
argument_list|,
literal|"Defaults for compound FK are not implemented."
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|JpaId
name|id
init|=
name|target
operator|.
name|getAttributes
argument_list|()
operator|.
name|getIds
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|pkName
init|=
name|id
operator|.
name|getColumn
argument_list|()
operator|!=
literal|null
condition|?
name|id
operator|.
name|getColumn
argument_list|()
operator|.
name|getName
argument_list|()
else|:
name|id
operator|.
name|getName
argument_list|()
decl_stmt|;
name|column
operator|.
name|setName
argument_list|(
name|relationship
operator|.
name|getName
argument_list|()
operator|+
literal|'_'
operator|+
name|pkName
argument_list|)
expr_stmt|;
name|column
operator|.
name|setReferencedColumnName
argument_list|(
name|id
operator|.
name|getColumn
argument_list|()
operator|!=
literal|null
condition|?
name|id
operator|.
name|getColumn
argument_list|()
operator|.
name|getName
argument_list|()
else|:
name|id
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
specifier|final
class|class
name|MappedSuperclassVisitor
extends|extends
name|AbstractEntityVisitor
block|{      }
specifier|final
class|class
name|ManyToOneVisitor
extends|extends
name|RelationshipVisitor
block|{
name|ManyToOneVisitor
parameter_list|()
block|{
name|addChildVisitor
argument_list|(
name|JpaJoinColumn
operator|.
name|class
argument_list|,
operator|new
name|JoinColumnVisitor
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|onStartNode
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
block|{
if|if
condition|(
operator|!
name|super
operator|.
name|onStartNode
argument_list|(
name|path
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|JpaManyToOne
name|manyToOne
init|=
operator|(
name|JpaManyToOne
operator|)
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|JpaJoinColumn
argument_list|>
name|joinColumns
init|=
name|manyToOne
operator|.
name|getJoinColumns
argument_list|()
decl_stmt|;
if|if
condition|(
name|joinColumns
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|joinColumns
operator|.
name|add
argument_list|(
operator|new
name|JpaJoinColumn
argument_list|(
name|AnnotationPrototypes
operator|.
name|getJoinColumn
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
specifier|final
class|class
name|OneToOneVisitor
extends|extends
name|RelationshipVisitor
block|{
name|OneToOneVisitor
parameter_list|()
block|{
name|addChildVisitor
argument_list|(
name|JpaJoinColumn
operator|.
name|class
argument_list|,
operator|new
name|JoinColumnVisitor
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|onStartNode
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
block|{
if|if
condition|(
operator|!
name|super
operator|.
name|onStartNode
argument_list|(
name|path
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|JpaOneToOne
name|oneToOne
init|=
operator|(
name|JpaOneToOne
operator|)
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|JpaJoinColumn
argument_list|>
name|joinColumns
init|=
name|oneToOne
operator|.
name|getJoinColumns
argument_list|()
decl_stmt|;
name|String
name|mappedBy
init|=
name|oneToOne
operator|.
name|getMappedBy
argument_list|()
decl_stmt|;
if|if
condition|(
name|joinColumns
operator|.
name|isEmpty
argument_list|()
operator|&&
name|mappedBy
operator|==
literal|null
condition|)
block|{
name|joinColumns
operator|.
name|add
argument_list|(
operator|new
name|JpaJoinColumn
argument_list|(
name|AnnotationPrototypes
operator|.
name|getJoinColumn
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
class|class
name|RelationshipVisitor
extends|extends
name|BaseTreeVisitor
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|onStartNode
parameter_list|(
name|ProjectPath
name|path
parameter_list|)
block|{
name|JpaRelationship
name|relationship
init|=
operator|(
name|JpaRelationship
operator|)
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|relationship
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
condition|)
block|{
name|JpaManagedClass
name|relationshipOwner
init|=
operator|(
name|JpaManagedClass
operator|)
name|path
operator|.
name|firstInstanceOf
argument_list|(
name|JpaManagedClass
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|relationship
operator|.
name|getName
argument_list|()
decl_stmt|;
name|JpaClassDescriptor
name|srcDescriptor
init|=
name|relationshipOwner
operator|.
name|getClassDescriptor
argument_list|()
decl_stmt|;
name|JpaPropertyDescriptor
name|property
init|=
name|srcDescriptor
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|targetEntityType
init|=
name|property
operator|.
name|getTargetEntityType
argument_list|()
decl_stmt|;
if|if
condition|(
name|targetEntityType
operator|==
literal|null
condition|)
block|{
name|context
operator|.
name|recordConflict
argument_list|(
operator|new
name|SimpleValidationFailure
argument_list|(
name|property
operator|.
name|getMember
argument_list|()
argument_list|,
literal|"Undefined target entity type: "
operator|+
name|name
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
else|else
block|{
name|relationship
operator|.
name|setTargetEntityName
argument_list|(
name|targetEntityType
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

