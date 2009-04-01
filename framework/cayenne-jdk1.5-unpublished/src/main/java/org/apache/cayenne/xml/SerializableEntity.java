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
name|xml
package|;
end_package

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
name|Iterator
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
name|reflect
operator|.
name|PropertyUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_comment
comment|/**  * A flyweight wrapper for serializing with XML mapping. This object is NOT thread-safe.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|SerializableEntity
implements|implements
name|XMLSerializable
block|{
name|Element
name|descriptor
decl_stmt|;
name|XMLMappingDescriptor
name|descriptorMap
decl_stmt|;
comment|// same node can store more than one object during encoding
specifier|transient
name|Object
name|object
decl_stmt|;
specifier|public
name|SerializableEntity
parameter_list|(
name|XMLMappingDescriptor
name|descriptorMap
parameter_list|,
name|Element
name|descriptor
parameter_list|)
block|{
name|this
operator|.
name|descriptor
operator|=
name|descriptor
expr_stmt|;
name|this
operator|.
name|descriptorMap
operator|=
name|descriptorMap
expr_stmt|;
block|}
name|String
name|getName
parameter_list|()
block|{
return|return
name|descriptor
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
return|;
block|}
name|Element
name|getDescriptor
parameter_list|()
block|{
return|return
name|descriptor
return|;
block|}
name|void
name|setObject
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|this
operator|.
name|object
operator|=
name|object
expr_stmt|;
block|}
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
name|object
operator|instanceof
name|Collection
condition|)
block|{
name|Collection
argument_list|<
name|?
argument_list|>
name|c
init|=
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
if|if
condition|(
operator|!
name|c
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// push the first node, and create the rest as peers.
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|c
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|encodeObject
argument_list|(
name|encoder
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|encodeObject
argument_list|(
name|encoder
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|// Make sure we pop the node we just pushed -- needed for fix to CAY-597.
name|encoder
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|encodeObject
argument_list|(
name|encoder
argument_list|,
name|this
operator|.
name|object
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// Needed for fix to CAY-597.  This makes sure we get back to the appropriate level in the DOM, rather than constantly re-rooting the tree.
name|encoder
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|decodeFromXML
parameter_list|(
name|XMLDecoder
name|decoder
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Decoding is not supported by this object"
argument_list|)
throw|;
block|}
name|void
name|encodeObject
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|,
name|Object
name|object
parameter_list|,
name|boolean
name|push
parameter_list|)
block|{
name|encoder
operator|.
name|setRoot
argument_list|(
name|descriptor
operator|.
name|getAttribute
argument_list|(
literal|"xmlTag"
argument_list|)
argument_list|,
literal|null
argument_list|,
name|push
argument_list|)
expr_stmt|;
for|for
control|(
name|Element
name|property
range|:
name|XMLUtil
operator|.
name|getChildren
argument_list|(
name|descriptor
argument_list|)
control|)
block|{
name|String
name|xmlTag
init|=
name|property
operator|.
name|getAttribute
argument_list|(
literal|"xmlTag"
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|property
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|object
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|SerializableEntity
name|relatedEntity
init|=
name|descriptorMap
operator|.
name|getEntity
argument_list|(
name|xmlTag
argument_list|)
decl_stmt|;
if|if
condition|(
name|relatedEntity
operator|!=
literal|null
condition|)
block|{
name|relatedEntity
operator|.
name|setObject
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|relatedEntity
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|encoder
operator|.
name|encodeProperty
argument_list|(
name|xmlTag
argument_list|,
name|value
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

