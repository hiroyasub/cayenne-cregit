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
name|Collections
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
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|util
operator|.
name|XMLEncoder
import|;
end_import

begin_comment
comment|/**  * An attribute of the ObjEntity that maps to an embeddable class.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|EmbeddedAttribute
extends|extends
name|ObjAttribute
block|{
specifier|protected
name|SortedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributeOverrides
decl_stmt|;
specifier|public
name|EmbeddedAttribute
parameter_list|()
block|{
name|attributeOverrides
operator|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|EmbeddedAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EmbeddedAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|ObjEntity
name|entity
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
literal|"<embedded-attribute name=\""
operator|+
name|getName
argument_list|()
operator|+
literal|'\"'
argument_list|)
expr_stmt|;
if|if
condition|(
name|getType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" type=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|'\"'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|attributeOverrides
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|encoder
operator|.
name|println
argument_list|(
literal|"/>"
argument_list|)
expr_stmt|;
return|return;
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
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|attributeOverrides
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<embeddable-attribute-override name=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|"\" db-attribute-path=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"\"/>"
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
literal|"</embedded-attribute>"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getAttributeOverrides
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|attributeOverrides
argument_list|)
return|;
block|}
specifier|public
name|Embeddable
name|getEmbeddable
parameter_list|()
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|getNonNullNamespace
argument_list|()
operator|.
name|getEmbeddable
argument_list|(
name|type
argument_list|)
return|;
block|}
specifier|private
name|ObjAttribute
name|makeObjAttribute
parameter_list|(
name|EmbeddableAttribute
name|embeddableAttribute
parameter_list|)
block|{
name|String
name|dbPath
init|=
name|attributeOverrides
operator|.
name|get
argument_list|(
name|embeddableAttribute
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|dbPath
operator|==
literal|null
condition|)
block|{
name|dbPath
operator|=
name|embeddableAttribute
operator|.
name|getDbAttributeName
argument_list|()
expr_stmt|;
block|}
return|return
name|makeObjAttribute
argument_list|(
name|embeddableAttribute
argument_list|,
name|dbPath
argument_list|)
return|;
block|}
specifier|private
name|ObjAttribute
name|makeObjAttribute
parameter_list|(
name|EmbeddableAttribute
name|embeddableAttribute
parameter_list|,
name|String
name|dbPath
parameter_list|)
block|{
name|String
name|fullName
init|=
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|embeddableAttribute
operator|.
name|getName
argument_list|()
decl_stmt|;
name|ObjAttribute
name|oa
init|=
operator|new
name|ObjAttribute
argument_list|(
name|fullName
argument_list|,
name|embeddableAttribute
operator|.
name|getType
argument_list|()
argument_list|,
operator|(
name|ObjEntity
operator|)
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
name|oa
operator|.
name|setDbAttributePath
argument_list|(
name|dbPath
argument_list|)
expr_stmt|;
return|return
name|oa
return|;
block|}
comment|/**      * Returns an ObjAttribute that maps to a given {@link DbAttribute}, or returns null      * if no such attribute exists.      */
specifier|public
name|ObjAttribute
name|getAttributeForDbPath
parameter_list|(
name|String
name|dbPath
parameter_list|)
block|{
name|Embeddable
name|e
init|=
name|getEmbeddable
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|EmbeddableAttribute
name|ea
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|override
range|:
name|attributeOverrides
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|dbPath
operator|.
name|equals
argument_list|(
name|override
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|ea
operator|=
name|e
operator|.
name|getAttribute
argument_list|(
name|override
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|ea
operator|==
literal|null
condition|)
block|{
name|ea
operator|=
name|e
operator|.
name|getAttributeForDbPath
argument_list|(
name|dbPath
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ea
operator|!=
literal|null
condition|)
block|{
return|return
name|makeObjAttribute
argument_list|(
name|ea
argument_list|,
name|dbPath
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns an ObjAttribute for a given name, taking into account column name      * overrides.      */
specifier|public
name|ObjAttribute
name|getAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Embeddable
name|e
init|=
name|getEmbeddable
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|EmbeddableAttribute
name|ea
init|=
name|e
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|ea
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|makeObjAttribute
argument_list|(
name|ea
argument_list|)
return|;
block|}
comment|/**      * Returns a Collection of ObjAttributes of an embedded object taking into account      * column name overrides.      */
specifier|public
name|Collection
argument_list|<
name|ObjAttribute
argument_list|>
name|getAttributes
parameter_list|()
block|{
name|Embeddable
name|e
init|=
name|getEmbeddable
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
name|Collection
argument_list|<
name|EmbeddableAttribute
argument_list|>
name|embeddableAttributes
init|=
name|e
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ObjAttribute
argument_list|>
name|objectAttributes
init|=
operator|new
name|ArrayList
argument_list|<
name|ObjAttribute
argument_list|>
argument_list|(
name|embeddableAttributes
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|EmbeddableAttribute
name|ea
range|:
name|embeddableAttributes
control|)
block|{
name|objectAttributes
operator|.
name|add
argument_list|(
name|makeObjAttribute
argument_list|(
name|ea
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|objectAttributes
return|;
block|}
specifier|public
name|void
name|addAttributeOverride
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|dbAttributeName
parameter_list|)
block|{
name|attributeOverrides
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|dbAttributeName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeAttributeOverride
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|attributeOverrides
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a type of this attribute that must be an {@link Embeddable} object.      */
annotation|@
name|Override
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * Returns Java class of an object property described by this attribute. Wraps any      * thrown exceptions into CayenneRuntimeException.      */
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getJavaClass
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|getType
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|Util
operator|.
name|getJavaClass
argument_list|(
name|getType
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Failed to load class for name '"
operator|+
name|this
operator|.
name|getType
argument_list|()
operator|+
literal|"': "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Sets a type of this attribute that must be an {@link Embeddable} object.      */
annotation|@
name|Override
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * Returns guaranteed non-null MappingNamespace of this relationship. If it happens to      * be null, and exception is thrown. This method is intended for internal use by      * Relationship class.      */
specifier|final
name|MappingNamespace
name|getNonNullNamespace
parameter_list|()
block|{
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
literal|"Embedded attribute '"
operator|+
name|getName
argument_list|()
operator|+
literal|"' has no parent Entity."
argument_list|)
throw|;
block|}
return|return
name|entity
operator|.
name|getNonNullNamespace
argument_list|()
return|;
block|}
block|}
end_class

end_unit
