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
comment|/**  * Defines a property descriptor that is a part of an Entity. Two examples of things that  * are described by attributes are Java class properties and database table columns.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|Attribute
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
name|entity
decl_stmt|;
comment|/**      * Creates an unnamed Attribute.      */
specifier|public
name|Attribute
parameter_list|()
block|{
block|}
comment|/**      * Creates a named Attribute.      */
specifier|public
name|Attribute
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
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|abstract
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
function_decl|;
comment|/**      * Returns parent entity that holds this attribute.      */
specifier|public
name|Entity
name|getEntity
parameter_list|()
block|{
return|return
name|entity
return|;
block|}
comment|/**      * Sets parent entity that holds this attribute.      */
specifier|public
name|void
name|setEntity
parameter_list|(
name|Entity
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
specifier|public
name|Object
name|getParent
parameter_list|()
block|{
return|return
name|getEntity
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
name|setEntity
argument_list|(
operator|(
name|Entity
operator|)
name|parent
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

