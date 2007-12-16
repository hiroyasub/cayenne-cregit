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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|AnnotatedElement
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
name|Arrays
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
name|Comparator
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
name|LinkedList
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
name|javax
operator|.
name|persistence
operator|.
name|AssociationOverride
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|AssociationOverrides
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|AttributeOverride
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|AttributeOverrides
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Basic
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Column
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Embeddable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Embedded
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EmbeddedId
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|EntityListeners
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Enumerated
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|GeneratedValue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Id
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Lob
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|ManyToMany
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|ManyToOne
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|MapKey
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|MappedSuperclass
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|NamedNativeQueries
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|NamedNativeQuery
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|NamedQueries
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|NamedQuery
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|OneToMany
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|OneToOne
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|OrderBy
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|SequenceGenerator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|SqlResultSetMapping
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|TableGenerator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Temporal
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Transient
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Version
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
comment|/**  * {@link org.apache.cayenne.jpa.map.JpaEntityMap} loader that reads mapping information  * from the class annotations per JPA specification.  *<h3>Specification Documentation, persistence_1_0.xsd, "class" element.</h3>  *<p>  * [Each managed class] should be annotated with either \@Entity, \@Embeddable or  * \@MappedSuperclass  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|EntityMapAnnotationLoader
block|{
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|TYPE_ANNOTATION_ORDERING_WEIGHTS
decl_stmt|;
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
decl_stmt|;
static|static
block|{
name|TYPE_ANNOTATION_ORDERING_WEIGHTS
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
comment|// annotations that are top-level only
name|TYPE_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|Entity
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|TYPE_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|Embeddable
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|TYPE_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|MappedSuperclass
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
comment|// annotations that can be a part of Entity or EntityMap
name|TYPE_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|SequenceGenerator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|TYPE_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|NamedNativeQueries
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|TYPE_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|NamedNativeQuery
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|TYPE_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|NamedQueries
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|TYPE_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|NamedQuery
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|TYPE_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|SqlResultSetMapping
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|TYPE_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|TableGenerator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|TYPE_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|EntityListeners
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
comment|// first level of member annotations - annotations representing different types of
comment|// attributes
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|Id
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|Basic
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|EmbeddedId
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|Version
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|ManyToOne
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|OneToMany
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|OneToOne
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|ManyToMany
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|Embedded
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|Transient
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|AssociationOverride
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|AssociationOverrides
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
comment|// second level - attribute overrides (can belong to Embedded or can be a part of
comment|// the entity )
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|AttributeOverride
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|AttributeOverrides
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
comment|// third level of member annotations - details implying one of the attributes
comment|// above
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|GeneratedValue
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|Temporal
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|TableGenerator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|SequenceGenerator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|Lob
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|Temporal
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|Enumerated
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|MapKey
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|OrderBy
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
operator|.
name|put
argument_list|(
name|Column
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|EntityMapLoaderContext
name|context
decl_stmt|;
specifier|protected
name|Comparator
argument_list|<
name|Annotation
argument_list|>
name|typeAnnotationsSorter
decl_stmt|;
specifier|protected
name|Comparator
argument_list|<
name|Annotation
argument_list|>
name|memberAnnotationsSorter
decl_stmt|;
specifier|protected
name|AnnotationProcessorFactory
name|classProcessorFactory
decl_stmt|;
specifier|protected
name|AnnotationProcessorFactory
name|memberProcessorFactory
decl_stmt|;
specifier|protected
name|AnnotationProcessorFactory
name|callbackProcessorFactory
decl_stmt|;
specifier|public
name|EntityMapAnnotationLoader
parameter_list|(
name|EntityMapLoaderContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|typeAnnotationsSorter
operator|=
operator|new
name|AnnotationSorter
argument_list|(
name|TYPE_ANNOTATION_ORDERING_WEIGHTS
argument_list|)
expr_stmt|;
name|this
operator|.
name|memberAnnotationsSorter
operator|=
operator|new
name|AnnotationSorter
argument_list|(
name|MEMBER_ANNOTATION_ORDERING_WEIGHTS
argument_list|)
expr_stmt|;
name|this
operator|.
name|classProcessorFactory
operator|=
operator|new
name|ClassAnnotationProcessorFactory
argument_list|()
expr_stmt|;
name|this
operator|.
name|memberProcessorFactory
operator|=
operator|new
name|MemberAnnotationProcessorFactory
argument_list|()
expr_stmt|;
name|this
operator|.
name|callbackProcessorFactory
operator|=
operator|new
name|EntityCallbackAnnotationProcessorFactory
argument_list|()
expr_stmt|;
block|}
comment|/**      * Processes annotations of a single Java class, loading ORM mapping information to      * the provided entity map.      */
specifier|public
name|void
name|loadClassMapping
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|managedClass
parameter_list|)
throws|throws
name|JpaProviderException
block|{
comment|// avoid duplicates loaded from annotations per CAY-756
if|if
condition|(
name|context
operator|.
name|getEntityMap
argument_list|()
operator|.
name|containsManagedClass
argument_list|(
name|managedClass
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|context
operator|.
name|recordConflict
argument_list|(
operator|new
name|SimpleValidationFailure
argument_list|(
name|managedClass
operator|.
name|getName
argument_list|()
argument_list|,
literal|"Duplicate managed class declaration "
operator|+
name|managedClass
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|Annotation
index|[]
name|classAnnotations
init|=
name|managedClass
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
comment|// per 'getAnnotations' docs, array is returned by copy, so we can modify it...
name|Arrays
operator|.
name|sort
argument_list|(
name|classAnnotations
argument_list|,
name|typeAnnotationsSorter
argument_list|)
expr_stmt|;
name|JpaClassDescriptor
name|descriptor
init|=
operator|new
name|JpaClassDescriptor
argument_list|(
name|managedClass
argument_list|)
decl_stmt|;
comment|// initially set access to the map level access - may be overriden below
name|descriptor
operator|.
name|setAccess
argument_list|(
name|context
operator|.
name|getEntityMap
argument_list|()
operator|.
name|getAccess
argument_list|()
argument_list|)
expr_stmt|;
name|AnnotationContext
name|stack
init|=
operator|new
name|AnnotationContext
argument_list|(
name|descriptor
argument_list|)
decl_stmt|;
name|stack
operator|.
name|push
argument_list|(
name|context
operator|.
name|getEntityMap
argument_list|()
argument_list|)
expr_stmt|;
comment|// === push class-level stuff
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|classAnnotations
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|AnnotationProcessor
name|processor
init|=
name|classProcessorFactory
operator|.
name|getProcessor
argument_list|(
name|classAnnotations
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|onStartElement
argument_list|(
name|managedClass
argument_list|,
name|stack
argument_list|)
expr_stmt|;
block|}
block|}
comment|// if class is not properly annotated, bail early
if|if
condition|(
name|stack
operator|.
name|depth
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return;
block|}
comment|// apply entity callbacks ...
if|if
condition|(
name|stack
operator|.
name|peek
argument_list|()
operator|instanceof
name|JpaAbstractEntity
condition|)
block|{
for|for
control|(
name|Method
name|callback
range|:
name|getEntityCallbacks
argument_list|(
name|managedClass
argument_list|)
control|)
block|{
name|applyEntityCallbackAnnotations
argument_list|(
name|callback
argument_list|,
name|stack
argument_list|)
expr_stmt|;
block|}
block|}
comment|// per JPA spec, 2.1.1, regarding access type:
comment|// When annotations are used, the placement of the mapping annotations on either
comment|// the persistent fields or persistent properties of the entity class specifies
comment|// the access type as being either field- or property-based access respectively.
comment|// Question (andrus) - if no annotations are placed at the field or method level,
comment|// we still must determine the access type to apply default mappping rules. How?
comment|// (using FIELD access for now).
name|boolean
name|fieldAccess
init|=
literal|false
decl_stmt|;
for|for
control|(
name|JpaPropertyDescriptor
name|property
range|:
name|descriptor
operator|.
name|getFieldDescriptors
argument_list|()
control|)
block|{
name|stack
operator|.
name|setPropertyDescriptor
argument_list|(
name|property
argument_list|)
expr_stmt|;
if|if
condition|(
name|applyMemberAnnotations
argument_list|(
name|property
argument_list|,
name|stack
argument_list|)
condition|)
block|{
name|fieldAccess
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|boolean
name|propertyAccess
init|=
literal|false
decl_stmt|;
for|for
control|(
name|JpaPropertyDescriptor
name|property
range|:
name|descriptor
operator|.
name|getPropertyDescriptors
argument_list|()
control|)
block|{
name|stack
operator|.
name|setPropertyDescriptor
argument_list|(
name|property
argument_list|)
expr_stmt|;
if|if
condition|(
name|applyMemberAnnotations
argument_list|(
name|property
argument_list|,
name|stack
argument_list|)
condition|)
block|{
name|propertyAccess
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|stack
operator|.
name|peek
argument_list|()
operator|instanceof
name|JpaManagedClass
condition|)
block|{
name|JpaManagedClass
name|entity
init|=
operator|(
name|JpaManagedClass
operator|)
name|stack
operator|.
name|peek
argument_list|()
decl_stmt|;
comment|// sanity check
if|if
condition|(
name|fieldAccess
operator|&&
name|propertyAccess
condition|)
block|{
throw|throw
operator|new
name|JpaProviderException
argument_list|(
literal|"Entity '"
operator|+
name|entity
operator|.
name|getClassName
argument_list|()
operator|+
literal|"' has both property and field annotations."
argument_list|)
throw|;
block|}
comment|// TODO: andrus - 11/29/2006 - clean this redundancy - access field should be
comment|// stored either in the entity or the descriptor.
if|if
condition|(
name|fieldAccess
condition|)
block|{
name|descriptor
operator|.
name|setAccess
argument_list|(
name|AccessType
operator|.
name|FIELD
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setAccess
argument_list|(
name|AccessType
operator|.
name|FIELD
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|propertyAccess
condition|)
block|{
name|descriptor
operator|.
name|setAccess
argument_list|(
name|AccessType
operator|.
name|PROPERTY
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setAccess
argument_list|(
name|AccessType
operator|.
name|PROPERTY
argument_list|)
expr_stmt|;
block|}
block|}
comment|// === pop class-level stuff
for|for
control|(
name|int
name|i
init|=
name|classAnnotations
operator|.
name|length
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|AnnotationProcessor
name|processor
init|=
name|classProcessorFactory
operator|.
name|getProcessor
argument_list|(
name|classAnnotations
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|onFinishElement
argument_list|(
name|managedClass
argument_list|,
name|stack
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Processes member annotations, returning true if at least one JPA annotation was      * found.      */
specifier|protected
name|boolean
name|applyMemberAnnotations
parameter_list|(
name|JpaPropertyDescriptor
name|property
parameter_list|,
name|AnnotationProcessorStack
name|stack
parameter_list|)
block|{
name|AnnotatedElement
name|member
init|=
name|property
operator|.
name|getMember
argument_list|()
decl_stmt|;
name|Annotation
index|[]
name|annotations
init|=
name|member
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
comment|// per 'getAnnotations' docs, array is returned by copy, so we can modify it...
name|Arrays
operator|.
name|sort
argument_list|(
name|annotations
argument_list|,
name|memberAnnotationsSorter
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|annotations
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|AnnotationProcessor
name|memberProcessor
init|=
name|memberProcessorFactory
operator|.
name|getProcessor
argument_list|(
name|annotations
index|[
name|j
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|memberProcessor
operator|!=
literal|null
condition|)
block|{
name|memberProcessor
operator|.
name|onStartElement
argument_list|(
name|member
argument_list|,
name|stack
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|int
name|j
init|=
name|annotations
operator|.
name|length
operator|-
literal|1
init|;
name|j
operator|>=
literal|0
condition|;
name|j
operator|--
control|)
block|{
name|AnnotationProcessor
name|memberProcessor
init|=
name|memberProcessorFactory
operator|.
name|getProcessor
argument_list|(
name|annotations
index|[
name|j
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|memberProcessor
operator|!=
literal|null
condition|)
block|{
name|memberProcessor
operator|.
name|onFinishElement
argument_list|(
name|member
argument_list|,
name|stack
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|annotations
operator|.
name|length
operator|>
literal|0
return|;
block|}
specifier|protected
name|void
name|applyEntityCallbackAnnotations
parameter_list|(
name|Method
name|method
parameter_list|,
name|AnnotationProcessorStack
name|stack
parameter_list|)
block|{
name|Annotation
index|[]
name|annotations
init|=
name|method
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|annotations
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|AnnotationProcessor
name|callbackProcessor
init|=
name|callbackProcessorFactory
operator|.
name|getProcessor
argument_list|(
name|annotations
index|[
name|j
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|callbackProcessor
operator|!=
literal|null
condition|)
block|{
name|callbackProcessor
operator|.
name|onStartElement
argument_list|(
name|method
argument_list|,
name|stack
argument_list|)
expr_stmt|;
block|}
block|}
comment|// don't call 'onFinishElement' as there is no nesting within callback
comment|// annotations...
block|}
comment|/**      * Returns a collection of methods that match an 'entity callback" pattern, i.e. "void      *<METHOD>()".      */
specifier|protected
name|Collection
argument_list|<
name|Method
argument_list|>
name|getEntityCallbacks
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|managedClass
parameter_list|)
block|{
name|Collection
argument_list|<
name|Method
argument_list|>
name|callbacks
init|=
operator|new
name|ArrayList
argument_list|<
name|Method
argument_list|>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
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
name|Modifier
operator|.
name|isStatic
argument_list|(
name|modifiers
argument_list|)
operator|||
name|Modifier
operator|.
name|isFinal
argument_list|(
name|modifiers
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
operator|!
name|Void
operator|.
name|TYPE
operator|.
name|equals
argument_list|(
name|methods
index|[
name|i
index|]
operator|.
name|getReturnType
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|params
init|=
name|methods
index|[
name|i
index|]
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|params
operator|.
name|length
operator|!=
literal|0
condition|)
block|{
continue|continue;
block|}
name|callbacks
operator|.
name|add
argument_list|(
name|methods
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|callbacks
return|;
block|}
comment|/**      * Comparator for TYPE level JPA annotations that first returns top-level annotations      * that define what kind of managed persistent class is being annotated.      */
specifier|final
class|class
name|AnnotationSorter
implements|implements
name|Comparator
argument_list|<
name|Annotation
argument_list|>
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|weights
decl_stmt|;
name|AnnotationSorter
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|weights
parameter_list|)
block|{
name|this
operator|.
name|weights
operator|=
name|weights
expr_stmt|;
block|}
specifier|public
name|int
name|compare
parameter_list|(
name|Annotation
name|o1
parameter_list|,
name|Annotation
name|o2
parameter_list|)
block|{
name|Integer
name|w1
init|=
name|weights
operator|.
name|get
argument_list|(
name|o1
operator|.
name|annotationType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Integer
name|w2
init|=
name|weights
operator|.
name|get
argument_list|(
name|o2
operator|.
name|annotationType
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// nulls go last as all non-top annotations are not explicitly mentioned
comment|// mapped to sorting weight
return|return
name|Util
operator|.
name|nullSafeCompare
argument_list|(
literal|false
argument_list|,
name|w1
argument_list|,
name|w2
argument_list|)
return|;
block|}
block|}
specifier|final
class|class
name|AnnotationContext
implements|implements
name|AnnotationProcessorStack
block|{
name|LinkedList
argument_list|<
name|Object
argument_list|>
name|stack
init|=
operator|new
name|LinkedList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|JpaClassDescriptor
name|classDescriptor
decl_stmt|;
name|JpaPropertyDescriptor
name|propertyDescriptor
decl_stmt|;
name|AnnotationContext
parameter_list|(
name|JpaClassDescriptor
name|classDescriptor
parameter_list|)
block|{
name|this
operator|.
name|classDescriptor
operator|=
name|classDescriptor
expr_stmt|;
block|}
name|void
name|setPropertyDescriptor
parameter_list|(
name|JpaPropertyDescriptor
name|propertyDescriptor
parameter_list|)
block|{
name|this
operator|.
name|propertyDescriptor
operator|=
name|propertyDescriptor
expr_stmt|;
block|}
specifier|public
name|int
name|depth
parameter_list|()
block|{
return|return
name|stack
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|Object
name|peek
parameter_list|()
block|{
return|return
name|stack
operator|.
name|peek
argument_list|()
return|;
block|}
specifier|public
name|Object
name|pop
parameter_list|()
block|{
return|return
name|stack
operator|.
name|removeFirst
argument_list|()
return|;
block|}
specifier|public
name|void
name|push
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
comment|// do descriptor injection...
if|if
condition|(
name|object
operator|instanceof
name|JpaAttribute
condition|)
block|{
name|JpaAttribute
name|attribute
init|=
operator|(
name|JpaAttribute
operator|)
name|object
decl_stmt|;
name|attribute
operator|.
name|setName
argument_list|(
name|propertyDescriptor
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|attribute
operator|.
name|setPropertyDescriptor
argument_list|(
name|propertyDescriptor
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|JpaManagedClass
condition|)
block|{
operator|(
operator|(
name|JpaManagedClass
operator|)
name|object
operator|)
operator|.
name|setClassDescriptor
argument_list|(
name|classDescriptor
argument_list|)
expr_stmt|;
block|}
name|stack
operator|.
name|addFirst
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|recordConflict
parameter_list|(
name|AnnotatedElement
name|element
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|annotatedType
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"Problem processing annotation: "
argument_list|)
operator|.
name|append
argument_list|(
name|annotatedType
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|", annotated element: "
argument_list|)
operator|.
name|append
argument_list|(
name|element
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", details: "
argument_list|)
operator|.
name|append
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|recordConflict
argument_list|(
operator|new
name|SimpleValidationFailure
argument_list|(
name|peek
argument_list|()
argument_list|,
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

