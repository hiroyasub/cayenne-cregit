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
name|modeler
operator|.
name|util
operator|.
name|state
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|DataChannelDescriptor
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
name|map
operator|.
name|DataMap
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
name|map
operator|.
name|Embeddable
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
name|map
operator|.
name|EmbeddableAttribute
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
name|modeler
operator|.
name|ProjectController
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
name|modeler
operator|.
name|event
operator|.
name|EmbeddableAttributeDisplayEvent
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
name|modeler
operator|.
name|event
operator|.
name|EmbeddableDisplayEvent
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
name|List
import|;
end_import

begin_class
class|class
name|EmbeddableAttributeDisplayEventType
extends|extends
name|EmbeddableDisplayEventType
block|{
specifier|public
name|EmbeddableAttributeDisplayEventType
parameter_list|(
name|ProjectController
name|controller
parameter_list|)
block|{
name|super
argument_list|(
name|controller
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|fireLastDisplayEvent
parameter_list|()
block|{
name|DataChannelDescriptor
name|dataChannel
init|=
operator|(
name|DataChannelDescriptor
operator|)
name|controller
operator|.
name|getProject
argument_list|()
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|dataChannel
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|preferences
operator|.
name|getDomain
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|DataMap
name|dataMap
init|=
name|dataChannel
operator|.
name|getDataMap
argument_list|(
name|preferences
operator|.
name|getDataMap
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataMap
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Embeddable
name|embeddable
init|=
name|dataMap
operator|.
name|getEmbeddable
argument_list|(
name|preferences
operator|.
name|getEmbeddable
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|embeddable
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EmbeddableDisplayEvent
name|embeddableDisplayEvent
init|=
operator|new
name|EmbeddableDisplayEvent
argument_list|(
name|this
argument_list|,
name|embeddable
argument_list|,
name|dataMap
argument_list|,
name|dataChannel
argument_list|)
decl_stmt|;
name|controller
operator|.
name|fireEmbeddableDisplayEvent
argument_list|(
name|embeddableDisplayEvent
argument_list|)
expr_stmt|;
name|EmbeddableAttribute
index|[]
name|embeddableAttributes
init|=
name|getLastEmbeddableAttributes
argument_list|(
name|embeddable
argument_list|)
decl_stmt|;
name|EmbeddableAttributeDisplayEvent
name|attributeDisplayEvent
init|=
operator|new
name|EmbeddableAttributeDisplayEvent
argument_list|(
name|this
argument_list|,
name|embeddable
argument_list|,
name|embeddableAttributes
argument_list|,
name|dataMap
argument_list|,
name|dataChannel
argument_list|)
decl_stmt|;
name|controller
operator|.
name|fireEmbeddableAttributeDisplayEvent
argument_list|(
name|attributeDisplayEvent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|saveLastDisplayEvent
parameter_list|()
block|{
name|preferences
operator|.
name|setEvent
argument_list|(
name|EmbeddableAttributeDisplayEvent
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|preferences
operator|.
name|setDomain
argument_list|(
name|controller
operator|.
name|getCurrentDataChanel
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|preferences
operator|.
name|setDataMap
argument_list|(
name|controller
operator|.
name|getCurrentDataMap
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|preferences
operator|.
name|setEmbeddable
argument_list|(
name|controller
operator|.
name|getCurrentEmbeddable
argument_list|()
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|EmbeddableAttribute
index|[]
name|currentEmbAttributes
init|=
name|controller
operator|.
name|getCurrentEmbAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentEmbAttributes
operator|==
literal|null
condition|)
block|{
name|preferences
operator|.
name|setEmbAttrs
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|EmbeddableAttribute
name|embeddableAttribute
range|:
name|currentEmbAttributes
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|embeddableAttribute
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|preferences
operator|.
name|setEmbAttrs
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|EmbeddableAttribute
index|[]
name|getLastEmbeddableAttributes
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|)
block|{
name|List
argument_list|<
name|EmbeddableAttribute
argument_list|>
name|embeddableAttributeList
init|=
operator|new
name|ArrayList
argument_list|<
name|EmbeddableAttribute
argument_list|>
argument_list|()
decl_stmt|;
name|EmbeddableAttribute
index|[]
name|attributes
init|=
operator|new
name|EmbeddableAttribute
index|[
literal|0
index|]
decl_stmt|;
name|String
name|embAttrs
init|=
name|preferences
operator|.
name|getEmbAttrs
argument_list|()
decl_stmt|;
if|if
condition|(
name|embAttrs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|embeddableAttributeList
operator|.
name|toArray
argument_list|(
name|attributes
argument_list|)
return|;
block|}
for|for
control|(
name|String
name|embAttrName
range|:
name|embAttrs
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|embeddableAttributeList
operator|.
name|add
argument_list|(
name|embeddable
operator|.
name|getAttribute
argument_list|(
name|embAttrName
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|embeddableAttributeList
operator|.
name|toArray
argument_list|(
name|attributes
argument_list|)
return|;
block|}
block|}
end_class

end_unit

