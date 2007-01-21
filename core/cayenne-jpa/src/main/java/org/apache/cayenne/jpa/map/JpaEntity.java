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

begin_comment
comment|/**  * A JPA-compliant entity.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|JpaEntity
extends|extends
name|JpaAbstractEntity
block|{
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|JpaTable
name|table
decl_stmt|;
specifier|protected
name|JpaInheritance
name|inheritance
decl_stmt|;
specifier|protected
name|String
name|discriminatorValue
decl_stmt|;
specifier|protected
name|JpaDiscriminatorColumn
name|discriminatorColumn
decl_stmt|;
specifier|protected
name|JpaSequenceGenerator
name|sequenceGenerator
decl_stmt|;
specifier|protected
name|JpaTableGenerator
name|tableGenerator
decl_stmt|;
specifier|protected
name|JpaSqlResultSetMapping
name|sqlResultSetMapping
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaAttributeOverride
argument_list|>
name|attributeOverrides
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaAssociationOverride
argument_list|>
name|associationOverrides
decl_stmt|;
comment|// TODO: andrus, 7/25/2006 - according to the notes in the JPA spec FR, these
comment|// annotations can be specified on a mapped superclass as well as entity. Check the
comment|// spec test to verify that and move these to superclass.
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
name|JpaSecondaryTable
argument_list|>
name|secondaryTables
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaPrimaryKeyJoinColumn
argument_list|>
name|primaryKeyJoinColumns
decl_stmt|;
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|TreeNodeChild
specifier|public
name|JpaDiscriminatorColumn
name|getDiscriminatorColumn
parameter_list|()
block|{
return|return
name|discriminatorColumn
return|;
block|}
specifier|public
name|void
name|setDiscriminatorColumn
parameter_list|(
name|JpaDiscriminatorColumn
name|discriminatorColumn
parameter_list|)
block|{
name|this
operator|.
name|discriminatorColumn
operator|=
name|discriminatorColumn
expr_stmt|;
block|}
comment|/**      * Returns discriminatorValue property.      *<h3>Specification Documentation</h3>      *<p>      *<b>Description:</b> An optional value that indicates that the row is an entity of      * this entity type.      *</p>      *<p>      *<b>Default:</b> If the DiscriminatorValue annotation is not specified, a      * provider-specific function to generate a value representing the entity type is used      * for the value of the discriminator column. If the DiscriminatorType is STRING, the      * discriminator value default is the entity name.      *</p>      */
specifier|public
name|String
name|getDiscriminatorValue
parameter_list|()
block|{
return|return
name|discriminatorValue
return|;
block|}
specifier|public
name|void
name|setDiscriminatorValue
parameter_list|(
name|String
name|discriminatorValue
parameter_list|)
block|{
name|this
operator|.
name|discriminatorValue
operator|=
name|discriminatorValue
expr_stmt|;
block|}
annotation|@
name|TreeNodeChild
specifier|public
name|JpaInheritance
name|getInheritance
parameter_list|()
block|{
return|return
name|inheritance
return|;
block|}
specifier|public
name|void
name|setInheritance
parameter_list|(
name|JpaInheritance
name|inheritance
parameter_list|)
block|{
name|this
operator|.
name|inheritance
operator|=
name|inheritance
expr_stmt|;
block|}
annotation|@
name|TreeNodeChild
specifier|public
name|JpaSequenceGenerator
name|getSequenceGenerator
parameter_list|()
block|{
return|return
name|sequenceGenerator
return|;
block|}
specifier|public
name|void
name|setSequenceGenerator
parameter_list|(
name|JpaSequenceGenerator
name|sequenceGenerator
parameter_list|)
block|{
name|this
operator|.
name|sequenceGenerator
operator|=
name|sequenceGenerator
expr_stmt|;
block|}
annotation|@
name|TreeNodeChild
specifier|public
name|JpaSqlResultSetMapping
name|getSqlResultSetMapping
parameter_list|()
block|{
return|return
name|sqlResultSetMapping
return|;
block|}
specifier|public
name|void
name|setSqlResultSetMapping
parameter_list|(
name|JpaSqlResultSetMapping
name|sqlResultSetMapping
parameter_list|)
block|{
name|this
operator|.
name|sqlResultSetMapping
operator|=
name|sqlResultSetMapping
expr_stmt|;
block|}
annotation|@
name|TreeNodeChild
specifier|public
name|JpaTable
name|getTable
parameter_list|()
block|{
return|return
name|table
return|;
block|}
specifier|public
name|void
name|setTable
parameter_list|(
name|JpaTable
name|table
parameter_list|)
block|{
name|this
operator|.
name|table
operator|=
name|table
expr_stmt|;
block|}
annotation|@
name|TreeNodeChild
specifier|public
name|JpaTableGenerator
name|getTableGenerator
parameter_list|()
block|{
return|return
name|tableGenerator
return|;
block|}
specifier|public
name|void
name|setTableGenerator
parameter_list|(
name|JpaTableGenerator
name|tableGenerator
parameter_list|)
block|{
name|this
operator|.
name|tableGenerator
operator|=
name|tableGenerator
expr_stmt|;
block|}
comment|/**      * Returns a collection of attribute overrides. Attribute overrides allows to change      * the definition of attributes from a mapped superclass.      */
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaAttributeOverride
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaAttributeOverride
argument_list|>
name|getAttributeOverrides
parameter_list|()
block|{
if|if
condition|(
name|attributeOverrides
operator|==
literal|null
condition|)
block|{
name|attributeOverrides
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaAttributeOverride
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|attributeOverrides
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaAssociationOverride
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaAssociationOverride
argument_list|>
name|getAssociationOverrides
parameter_list|()
block|{
if|if
condition|(
name|associationOverrides
operator|==
literal|null
condition|)
block|{
name|associationOverrides
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaAssociationOverride
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|associationOverrides
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
argument_list|()
expr_stmt|;
block|}
return|return
name|namedQueries
return|;
block|}
comment|/**      * Returns a collection of {@link JpaPrimaryKeyJoinColumn} objects that reference keys      * of a primary table. PK join columns used by subclasses in a      * {@link javax.persistence.InheritanceType#JOINED} mapping scenario.      */
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaPrimaryKeyJoinColumn
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaPrimaryKeyJoinColumn
argument_list|>
name|getPrimaryKeyJoinColumns
parameter_list|()
block|{
if|if
condition|(
name|primaryKeyJoinColumns
operator|==
literal|null
condition|)
block|{
name|primaryKeyJoinColumns
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
block|}
return|return
name|primaryKeyJoinColumns
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaSecondaryTable
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaSecondaryTable
argument_list|>
name|getSecondaryTables
parameter_list|()
block|{
if|if
condition|(
name|secondaryTables
operator|==
literal|null
condition|)
block|{
name|secondaryTables
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
block|}
return|return
name|secondaryTables
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"JpaEntity:"
operator|+
name|name
return|;
block|}
block|}
end_class

end_unit

