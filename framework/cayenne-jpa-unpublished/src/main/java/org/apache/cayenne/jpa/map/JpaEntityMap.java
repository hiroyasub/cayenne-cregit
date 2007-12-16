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
name|util
operator|.
name|TreeNodeChild
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
name|XMLEncoder
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
name|XMLSerializable
import|;
end_import

begin_comment
comment|/**  * An object that stores JPA mapping information. This is a root object in the hierarchy  * defined in the<em>orm_1_0.xsd</em> schema.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|JpaEntityMap
implements|implements
name|XMLSerializable
block|{
comment|// mapped properties
specifier|protected
name|String
name|version
decl_stmt|;
specifier|protected
name|String
name|description
decl_stmt|;
specifier|protected
name|String
name|packageName
decl_stmt|;
specifier|protected
name|String
name|catalog
decl_stmt|;
specifier|protected
name|String
name|schema
decl_stmt|;
specifier|protected
name|AccessType
name|access
decl_stmt|;
specifier|protected
name|JpaPersistenceUnitMetadata
name|persistenceUnitMetadata
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaEntity
argument_list|>
name|entities
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaEmbeddable
argument_list|>
name|embeddables
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaMappedSuperclass
argument_list|>
name|mappedSuperclasses
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaNamedQuery
argument_list|>
name|namedQueries
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaNamedNativeQuery
argument_list|>
name|namedNativeQueries
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaSqlResultSetMapping
argument_list|>
name|sqlResultSetMappings
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaSequenceGenerator
argument_list|>
name|sequenceGenerators
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaTableGenerator
argument_list|>
name|tableGenerators
decl_stmt|;
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<entity-mappings"
argument_list|)
expr_stmt|;
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" version=\""
operator|+
name|version
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|println
argument_list|(
literal|'>'
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|indent
argument_list|(
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|println
argument_list|(
literal|"<description>"
operator|+
name|description
operator|+
literal|"</description>"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|persistenceUnitMetadata
operator|!=
literal|null
condition|)
block|{
name|persistenceUnitMetadata
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|packageName
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|println
argument_list|(
literal|"<package>"
operator|+
name|packageName
operator|+
literal|"</package>"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|println
argument_list|(
literal|"<schema>"
operator|+
name|schema
operator|+
literal|"</schema>"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|catalog
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|println
argument_list|(
literal|"<catalog>"
operator|+
name|catalog
operator|+
literal|"</catalog>"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|access
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|println
argument_list|(
literal|"<access>"
operator|+
name|access
operator|.
name|name
argument_list|()
operator|+
literal|"</access>"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sequenceGenerators
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|sequenceGenerators
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tableGenerators
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|tableGenerators
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|namedQueries
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|namedQueries
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|namedNativeQueries
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|namedNativeQueries
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sqlResultSetMappings
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|sqlResultSetMappings
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mappedSuperclasses
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|mappedSuperclasses
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|entities
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|entities
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|embeddables
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|embeddables
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|indent
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|"</entity-mappings>"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns true if a given managed class is already loaded.      */
specifier|public
name|boolean
name|containsManagedClass
parameter_list|(
name|String
name|className
parameter_list|)
block|{
if|if
condition|(
name|className
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null class name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|mappedSuperclasses
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaMappedSuperclass
name|object
range|:
name|mappedSuperclasses
control|)
block|{
if|if
condition|(
name|className
operator|.
name|equals
argument_list|(
name|object
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
if|if
condition|(
name|entities
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaEntity
name|object
range|:
name|entities
control|)
block|{
if|if
condition|(
name|className
operator|.
name|equals
argument_list|(
name|object
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
if|if
condition|(
name|embeddables
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaEmbeddable
name|object
range|:
name|embeddables
control|)
block|{
if|if
condition|(
name|className
operator|.
name|equals
argument_list|(
name|object
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Compiles and returns a map of managed class descriptors that includes descriptors      * for entities, managed superclasses and embeddables. Note that class name key in the      * map uses slashes, not dots, to separate package components.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|JpaClassDescriptor
argument_list|>
name|getMangedClasses
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|JpaClassDescriptor
argument_list|>
name|managedClasses
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|JpaClassDescriptor
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|mappedSuperclasses
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaMappedSuperclass
name|object
range|:
name|mappedSuperclasses
control|)
block|{
name|managedClasses
operator|.
name|put
argument_list|(
name|object
operator|.
name|getClassName
argument_list|()
argument_list|,
name|object
operator|.
name|getClassDescriptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|entities
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaEntity
name|object
range|:
name|entities
control|)
block|{
name|managedClasses
operator|.
name|put
argument_list|(
name|object
operator|.
name|getClassName
argument_list|()
argument_list|,
name|object
operator|.
name|getClassDescriptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|embeddables
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaEmbeddable
name|object
range|:
name|embeddables
control|)
block|{
name|managedClasses
operator|.
name|put
argument_list|(
name|object
operator|.
name|getClassName
argument_list|()
argument_list|,
name|object
operator|.
name|getClassDescriptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|managedClasses
return|;
block|}
comment|/**      * Returns a JpaEntity describing a given persistent class.      */
specifier|public
name|JpaEntity
name|entityForClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityClass
parameter_list|)
block|{
if|if
condition|(
name|entityClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null entity class"
argument_list|)
throw|;
block|}
return|return
name|entityForClass
argument_list|(
name|entityClass
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns a JpaEntity describing a given persistent class.      */
specifier|public
name|JpaEntity
name|entityForClass
parameter_list|(
name|String
name|entityClassName
parameter_list|)
block|{
if|if
condition|(
name|entityClassName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null entity class name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|entities
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
for|for
control|(
name|JpaEntity
name|entity
range|:
name|entities
control|)
block|{
if|if
condition|(
name|entityClassName
operator|.
name|equals
argument_list|(
name|entity
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|entity
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns a JpaEmbeddable describing a given embeddable class.      */
specifier|public
name|JpaEmbeddable
name|embeddableForClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|embeddableClass
parameter_list|)
block|{
if|if
condition|(
name|embeddableClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null embeddable class"
argument_list|)
throw|;
block|}
return|return
name|embeddableForClass
argument_list|(
name|embeddableClass
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns a JpaEmbeddable describing a given embeddable class.      */
specifier|public
name|JpaEmbeddable
name|embeddableForClass
parameter_list|(
name|String
name|embeddableClassName
parameter_list|)
block|{
if|if
condition|(
name|embeddableClassName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null embeddable class name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|embeddables
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
for|for
control|(
name|JpaEmbeddable
name|embeddable
range|:
name|embeddables
control|)
block|{
if|if
condition|(
name|embeddableClassName
operator|.
name|equals
argument_list|(
name|embeddable
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|embeddable
return|;
block|}
block|}
return|return
literal|null
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
specifier|public
name|String
name|getCatalog
parameter_list|()
block|{
return|return
name|catalog
return|;
block|}
specifier|public
name|void
name|setCatalog
parameter_list|(
name|String
name|catalog
parameter_list|)
block|{
name|this
operator|.
name|catalog
operator|=
name|catalog
expr_stmt|;
block|}
specifier|public
name|String
name|getPackageName
parameter_list|()
block|{
return|return
name|packageName
return|;
block|}
specifier|public
name|void
name|setPackageName
parameter_list|(
name|String
name|packageProperty
parameter_list|)
block|{
name|this
operator|.
name|packageName
operator|=
name|packageProperty
expr_stmt|;
block|}
specifier|public
name|String
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
specifier|public
name|void
name|setSchema
parameter_list|(
name|String
name|schema
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaEmbeddable
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaEmbeddable
argument_list|>
name|getEmbeddables
parameter_list|()
block|{
if|if
condition|(
name|embeddables
operator|==
literal|null
condition|)
block|{
name|embeddables
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaEmbeddable
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|embeddables
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaEntity
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaEntity
argument_list|>
name|getEntities
parameter_list|()
block|{
if|if
condition|(
name|entities
operator|==
literal|null
condition|)
block|{
name|entities
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaEntity
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|entities
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaMappedSuperclass
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaMappedSuperclass
argument_list|>
name|getMappedSuperclasses
parameter_list|()
block|{
if|if
condition|(
name|mappedSuperclasses
operator|==
literal|null
condition|)
block|{
name|mappedSuperclasses
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaMappedSuperclass
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|mappedSuperclasses
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaNamedNativeQuery
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaNamedNativeQuery
argument_list|>
name|getNamedNativeQueries
parameter_list|()
block|{
if|if
condition|(
name|namedNativeQueries
operator|==
literal|null
condition|)
block|{
name|namedNativeQueries
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaNamedNativeQuery
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|namedNativeQueries
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaNamedQuery
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaNamedQuery
argument_list|>
name|getNamedQueries
parameter_list|()
block|{
if|if
condition|(
name|namedQueries
operator|==
literal|null
condition|)
block|{
name|namedQueries
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaNamedQuery
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|namedQueries
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaSequenceGenerator
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaSequenceGenerator
argument_list|>
name|getSequenceGenerators
parameter_list|()
block|{
if|if
condition|(
name|sequenceGenerators
operator|==
literal|null
condition|)
block|{
name|sequenceGenerators
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaSequenceGenerator
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|sequenceGenerators
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaSqlResultSetMapping
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaSqlResultSetMapping
argument_list|>
name|getSqlResultSetMappings
parameter_list|()
block|{
if|if
condition|(
name|sqlResultSetMappings
operator|==
literal|null
condition|)
block|{
name|sqlResultSetMappings
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaSqlResultSetMapping
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|sqlResultSetMappings
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaTableGenerator
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaTableGenerator
argument_list|>
name|getTableGenerators
parameter_list|()
block|{
if|if
condition|(
name|tableGenerators
operator|==
literal|null
condition|)
block|{
name|tableGenerators
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaTableGenerator
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|tableGenerators
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
annotation|@
name|TreeNodeChild
specifier|public
name|JpaPersistenceUnitMetadata
name|getPersistenceUnitMetadata
parameter_list|()
block|{
return|return
name|persistenceUnitMetadata
return|;
block|}
specifier|public
name|void
name|setPersistenceUnitMetadata
parameter_list|(
name|JpaPersistenceUnitMetadata
name|persistenceUnitMetadata
parameter_list|)
block|{
name|this
operator|.
name|persistenceUnitMetadata
operator|=
name|persistenceUnitMetadata
expr_stmt|;
block|}
block|}
end_class

end_unit

