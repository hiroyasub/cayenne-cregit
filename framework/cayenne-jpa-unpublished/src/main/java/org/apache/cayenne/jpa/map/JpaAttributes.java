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
comment|/**  * An attribute container.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|JpaAttributes
implements|implements
name|XMLSerializable
block|{
specifier|protected
name|Collection
argument_list|<
name|JpaId
argument_list|>
name|ids
decl_stmt|;
specifier|protected
name|JpaEmbeddedId
name|embeddedId
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaBasic
argument_list|>
name|basicAttributes
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaVersion
argument_list|>
name|versionAttributes
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaManyToOne
argument_list|>
name|manyToOneRelationships
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaOneToMany
argument_list|>
name|oneToManyRelationships
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaOneToOne
argument_list|>
name|oneToOneRelationships
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaManyToMany
argument_list|>
name|manyToManyRelationships
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaEmbedded
argument_list|>
name|embeddedAttributes
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|JpaTransient
argument_list|>
name|transientAttributes
decl_stmt|;
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
if|if
condition|(
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|encoder
operator|.
name|println
argument_list|(
literal|"<attributes>"
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
name|ids
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|ids
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|embeddedId
operator|!=
literal|null
condition|)
block|{
name|embeddedId
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|basicAttributes
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|basicAttributes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|versionAttributes
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|versionAttributes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|manyToOneRelationships
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|manyToOneRelationships
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|oneToManyRelationships
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|oneToManyRelationships
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|oneToOneRelationships
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|oneToOneRelationships
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|manyToManyRelationships
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|manyToManyRelationships
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|embeddedAttributes
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|embeddedAttributes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|transientAttributes
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|transientAttributes
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
name|println
argument_list|(
literal|"</attributes>"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JpaAttribute
name|getAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|embeddedId
operator|!=
literal|null
operator|&&
name|name
operator|.
name|equals
argument_list|(
name|embeddedId
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|embeddedId
return|;
block|}
name|JpaAttribute
name|attribute
decl_stmt|;
name|attribute
operator|=
name|getId
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
return|return
name|attribute
return|;
block|}
name|attribute
operator|=
name|getBasicAttribute
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
return|return
name|attribute
return|;
block|}
name|attribute
operator|=
name|getVersionAttribute
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
return|return
name|attribute
return|;
block|}
name|attribute
operator|=
name|getManyToOneRelationship
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
return|return
name|attribute
return|;
block|}
name|attribute
operator|=
name|getOneToManyRelationship
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
return|return
name|attribute
return|;
block|}
name|attribute
operator|=
name|getOneToOneRelationship
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
return|return
name|attribute
return|;
block|}
name|attribute
operator|=
name|getManyToManyRelationship
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
return|return
name|attribute
return|;
block|}
name|attribute
operator|=
name|getTransientAttribute
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
return|return
name|attribute
return|;
block|}
name|attribute
operator|=
name|getEmbeddedAttribute
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
return|return
name|attribute
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns combined count of all attributes and relationships.      */
specifier|public
name|int
name|size
parameter_list|()
block|{
name|int
name|size
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|embeddedId
operator|!=
literal|null
condition|)
block|{
name|size
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|ids
operator|!=
literal|null
condition|)
block|{
name|size
operator|+=
name|ids
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|basicAttributes
operator|!=
literal|null
condition|)
block|{
name|size
operator|+=
name|basicAttributes
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|versionAttributes
operator|!=
literal|null
condition|)
block|{
name|size
operator|+=
name|versionAttributes
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|manyToOneRelationships
operator|!=
literal|null
condition|)
block|{
name|size
operator|+=
name|manyToOneRelationships
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|oneToManyRelationships
operator|!=
literal|null
condition|)
block|{
name|size
operator|+=
name|oneToManyRelationships
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|oneToOneRelationships
operator|!=
literal|null
condition|)
block|{
name|size
operator|+=
name|oneToOneRelationships
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|manyToManyRelationships
operator|!=
literal|null
condition|)
block|{
name|size
operator|+=
name|manyToManyRelationships
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|embeddedAttributes
operator|!=
literal|null
condition|)
block|{
name|size
operator|+=
name|embeddedAttributes
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|transientAttributes
operator|!=
literal|null
condition|)
block|{
name|size
operator|+=
name|transientAttributes
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
return|return
name|size
return|;
block|}
specifier|public
name|JpaId
name|getId
parameter_list|(
name|String
name|idName
parameter_list|)
block|{
if|if
condition|(
name|idName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null id name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ids
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaId
name|id
range|:
name|ids
control|)
block|{
if|if
condition|(
name|idName
operator|.
name|equals
argument_list|(
name|id
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|id
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|JpaId
name|getIdForColumnName
parameter_list|(
name|String
name|idColumnName
parameter_list|)
block|{
if|if
condition|(
name|idColumnName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null id column name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|ids
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaId
name|id
range|:
name|ids
control|)
block|{
if|if
condition|(
name|id
operator|.
name|getColumn
argument_list|()
operator|!=
literal|null
operator|&&
name|idColumnName
operator|.
name|equals
argument_list|(
name|id
operator|.
name|getColumn
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|id
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns a JpaAttribute for a given property name      */
specifier|public
name|JpaBasic
name|getBasicAttribute
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
if|if
condition|(
name|attributeName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null attribute name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|basicAttributes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaBasic
name|attribute
range|:
name|basicAttributes
control|)
block|{
if|if
condition|(
name|attributeName
operator|.
name|equals
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|attribute
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|JpaManyToOne
name|getManyToOneRelationship
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
if|if
condition|(
name|attributeName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null attribute name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|manyToOneRelationships
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaManyToOne
name|attribute
range|:
name|manyToOneRelationships
control|)
block|{
if|if
condition|(
name|attributeName
operator|.
name|equals
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|attribute
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|JpaOneToMany
name|getOneToManyRelationship
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
if|if
condition|(
name|attributeName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null attribute name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|oneToManyRelationships
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaOneToMany
name|attribute
range|:
name|oneToManyRelationships
control|)
block|{
if|if
condition|(
name|attributeName
operator|.
name|equals
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|attribute
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaId
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaId
argument_list|>
name|getIds
parameter_list|()
block|{
if|if
condition|(
name|ids
operator|==
literal|null
condition|)
block|{
name|ids
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaId
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|ids
return|;
block|}
annotation|@
name|TreeNodeChild
specifier|public
name|JpaEmbeddedId
name|getEmbeddedId
parameter_list|()
block|{
return|return
name|embeddedId
return|;
block|}
specifier|public
name|void
name|setEmbeddedId
parameter_list|(
name|JpaEmbeddedId
name|embeddedId
parameter_list|)
block|{
name|this
operator|.
name|embeddedId
operator|=
name|embeddedId
expr_stmt|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaBasic
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaBasic
argument_list|>
name|getBasicAttributes
parameter_list|()
block|{
if|if
condition|(
name|basicAttributes
operator|==
literal|null
condition|)
block|{
name|basicAttributes
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaBasic
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|basicAttributes
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaEmbedded
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaEmbedded
argument_list|>
name|getEmbeddedAttributes
parameter_list|()
block|{
if|if
condition|(
name|embeddedAttributes
operator|==
literal|null
condition|)
block|{
name|embeddedAttributes
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaEmbedded
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|embeddedAttributes
return|;
block|}
specifier|public
name|JpaEmbedded
name|getEmbeddedAttribute
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
if|if
condition|(
name|attributeName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null attribute name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|embeddedAttributes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaEmbedded
name|attribute
range|:
name|embeddedAttributes
control|)
block|{
if|if
condition|(
name|attributeName
operator|.
name|equals
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|attribute
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaManyToMany
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaManyToMany
argument_list|>
name|getManyToManyRelationships
parameter_list|()
block|{
if|if
condition|(
name|manyToManyRelationships
operator|==
literal|null
condition|)
block|{
name|manyToManyRelationships
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaManyToMany
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|manyToManyRelationships
return|;
block|}
specifier|public
name|JpaManyToMany
name|getManyToManyRelationship
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
if|if
condition|(
name|attributeName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null attribute name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|manyToManyRelationships
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaManyToMany
name|attribute
range|:
name|manyToManyRelationships
control|)
block|{
if|if
condition|(
name|attributeName
operator|.
name|equals
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|attribute
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaManyToOne
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaManyToOne
argument_list|>
name|getManyToOneRelationships
parameter_list|()
block|{
if|if
condition|(
name|manyToOneRelationships
operator|==
literal|null
condition|)
block|{
name|manyToOneRelationships
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaManyToOne
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|manyToOneRelationships
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaOneToMany
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaOneToMany
argument_list|>
name|getOneToManyRelationships
parameter_list|()
block|{
if|if
condition|(
name|oneToManyRelationships
operator|==
literal|null
condition|)
block|{
name|oneToManyRelationships
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaOneToMany
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|oneToManyRelationships
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaOneToOne
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaOneToOne
argument_list|>
name|getOneToOneRelationships
parameter_list|()
block|{
if|if
condition|(
name|oneToOneRelationships
operator|==
literal|null
condition|)
block|{
name|oneToOneRelationships
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaOneToOne
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|oneToOneRelationships
return|;
block|}
specifier|public
name|JpaOneToOne
name|getOneToOneRelationship
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
if|if
condition|(
name|attributeName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null attribute name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|oneToOneRelationships
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaOneToOne
name|attribute
range|:
name|oneToOneRelationships
control|)
block|{
if|if
condition|(
name|attributeName
operator|.
name|equals
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|attribute
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaTransient
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaTransient
argument_list|>
name|getTransientAttributes
parameter_list|()
block|{
if|if
condition|(
name|transientAttributes
operator|==
literal|null
condition|)
block|{
name|transientAttributes
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaTransient
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|transientAttributes
return|;
block|}
comment|/**      * Returns a JpaTransient for a given property name      */
specifier|public
name|JpaTransient
name|getTransientAttribute
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
if|if
condition|(
name|attributeName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null attribute name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|transientAttributes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaTransient
name|attribute
range|:
name|transientAttributes
control|)
block|{
if|if
condition|(
name|attributeName
operator|.
name|equals
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|attribute
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaVersion
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaVersion
argument_list|>
name|getVersionAttributes
parameter_list|()
block|{
if|if
condition|(
name|versionAttributes
operator|==
literal|null
condition|)
block|{
name|versionAttributes
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaVersion
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|versionAttributes
return|;
block|}
comment|/**      * Returns a JpaTransient for a given property name      */
specifier|public
name|JpaVersion
name|getVersionAttribute
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
if|if
condition|(
name|attributeName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null attribute name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|versionAttributes
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|JpaVersion
name|attribute
range|:
name|versionAttributes
control|)
block|{
if|if
condition|(
name|attributeName
operator|.
name|equals
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|attribute
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

