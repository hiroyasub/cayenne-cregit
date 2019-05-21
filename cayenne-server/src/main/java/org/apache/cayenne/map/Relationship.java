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
name|map
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|CayenneMapEntry
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
name|ToStringBuilder
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
comment|/**  * Defines a relationship between two entities. In a DataMap graph relationships represent  * "arcs" connecting entity "nodes". Relationships are directional, i.e. they have a  * notion of source and target entity. This makes DataMap a "digraph".  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|Relationship
implements|implements
name|CayenneMapEntry
implements|,
name|XMLSerializable
implements|,
name|Serializable
block|{
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|Entity
name|sourceEntity
decl_stmt|;
specifier|protected
name|String
name|targetEntityName
decl_stmt|;
specifier|protected
name|boolean
name|toMany
decl_stmt|;
comment|/**      * A flag that specifies whether a Relationship was mapped by the user or added      * dynamically by Cayenne runtime.      *       * @since 3.0      */
specifier|protected
name|boolean
name|runtime
decl_stmt|;
comment|/**      * Creates an unnamed relationship.      */
specifier|public
name|Relationship
parameter_list|()
block|{
block|}
comment|/**      * Creates a named relationship.      */
specifier|public
name|Relationship
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Returns relationship source entity.      */
specifier|public
name|Entity
name|getSourceEntity
parameter_list|()
block|{
return|return
name|sourceEntity
return|;
block|}
comment|/**      * Sets relationship source entity.      */
specifier|public
name|void
name|setSourceEntity
parameter_list|(
name|Entity
name|sourceEntity
parameter_list|)
block|{
name|this
operator|.
name|sourceEntity
operator|=
name|sourceEntity
expr_stmt|;
block|}
comment|/**      * Returns a target entity of the relationship.      */
specifier|public
specifier|abstract
name|Entity
name|getTargetEntity
parameter_list|()
function_decl|;
comment|/**      * Sets relationship target entity. Internally calls<code>setTargetEntityName</code>.      */
specifier|public
name|void
name|setTargetEntityName
parameter_list|(
name|Entity
name|targetEntity
parameter_list|)
block|{
if|if
condition|(
name|targetEntity
operator|!=
literal|null
condition|)
block|{
name|setTargetEntityName
argument_list|(
name|targetEntity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setTargetEntityName
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the name of a target entity.      */
specifier|public
name|String
name|getTargetEntityName
parameter_list|()
block|{
return|return
name|targetEntityName
return|;
block|}
comment|/**      * Sets the name of relationship target entity.      */
specifier|public
name|void
name|setTargetEntityName
parameter_list|(
name|String
name|targetEntityName
parameter_list|)
block|{
name|this
operator|.
name|targetEntityName
operator|=
name|targetEntityName
expr_stmt|;
block|}
comment|/**      * Returns a boolean value that determines relationship multiplicity. This defines      * semantics of the connection between two nodes described by the source and target      * entities. E.g. to-many relationship between two Persistent object classes means      * that a source object would have a collection of target objects. This is a read-only      * property.      */
specifier|public
name|boolean
name|isToMany
parameter_list|()
block|{
return|return
name|toMany
return|;
block|}
specifier|public
name|Object
name|getParent
parameter_list|()
block|{
return|return
name|getSourceEntity
argument_list|()
return|;
block|}
specifier|public
name|void
name|setParent
parameter_list|(
name|Object
name|parent
parameter_list|)
block|{
if|if
condition|(
name|parent
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|parent
operator|instanceof
name|Entity
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected null or Entity, got: "
operator|+
name|parent
argument_list|)
throw|;
block|}
name|setSourceEntity
argument_list|(
operator|(
name|Entity
operator|)
name|parent
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns guaranteed non-null MappingNamespace of this relationship. If it happens to      * be null, and exception is thrown. This method is intended for internal use by      * Relationship class.      */
specifier|final
name|MappingNamespace
name|getNonNullNamespace
parameter_list|()
block|{
name|Entity
name|entity
init|=
name|getSourceEntity
argument_list|()
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
literal|"Relationship '%s' has no parent Entity."
argument_list|,
name|getName
argument_list|()
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
comment|/**      * Overrides Object.toString() to return informative description.      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|new
name|ToStringBuilder
argument_list|(
name|this
argument_list|)
operator|.
name|append
argument_list|(
literal|"name"
argument_list|,
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"toMany"
argument_list|,
name|isToMany
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|boolean
name|isRuntime
parameter_list|()
block|{
return|return
name|runtime
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|setRuntime
parameter_list|(
name|boolean
name|synthetic
parameter_list|)
block|{
name|this
operator|.
name|runtime
operator|=
name|synthetic
expr_stmt|;
block|}
comment|/**      * Returns a "complimentary" relationship going in the opposite direction. Returns      * null if no such relationship is found.      * @since 3.1      */
specifier|public
specifier|abstract
name|Relationship
name|getReverseRelationship
parameter_list|()
function_decl|;
comment|/**      * Returns if relationship is mandatory      * @since 3.1      */
specifier|public
specifier|abstract
name|boolean
name|isMandatory
parameter_list|()
function_decl|;
block|}
end_class

end_unit

