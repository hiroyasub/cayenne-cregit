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
name|modeler
operator|.
name|editor
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
name|configuration
operator|.
name|ConfigurationNodeVisitor
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

begin_class
specifier|public
class|class
name|ObjCallbackMethod
implements|implements
name|XMLSerializable
implements|,
name|Serializable
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|CallbackType
name|callbackType
decl_stmt|;
specifier|public
name|ObjCallbackMethod
parameter_list|(
name|String
name|name
parameter_list|,
name|CallbackType
name|callbackType
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|callbackType
operator|=
name|callbackType
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
parameter_list|,
name|ConfigurationNodeVisitor
name|delegate
parameter_list|)
block|{
name|encoder
operator|.
name|start
argument_list|(
name|encodeCallbackTypeForXML
argument_list|(
name|callbackType
argument_list|)
argument_list|)
operator|.
name|attribute
argument_list|(
name|name
argument_list|,
name|getName
argument_list|()
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
name|encodeCallbackTypeForXML
parameter_list|(
name|CallbackType
name|type
parameter_list|)
block|{
switch|switch
condition|(
name|type
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|POST_ADD
case|:
return|return
literal|"post-add"
return|;
case|case
name|POST_LOAD
case|:
return|return
literal|"post-load"
return|;
case|case
name|POST_PERSIST
case|:
return|return
literal|"post-persist"
return|;
case|case
name|POST_REMOVE
case|:
return|return
literal|"post-remove"
return|;
case|case
name|POST_UPDATE
case|:
return|return
literal|"post-update"
return|;
case|case
name|PRE_PERSIST
case|:
return|return
literal|"pre-persist"
return|;
case|case
name|PRE_REMOVE
case|:
return|return
literal|"pre-remove"
return|;
default|default:
return|return
literal|"pre-update"
return|;
block|}
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
name|CallbackType
name|getCallbackType
parameter_list|()
block|{
return|return
name|callbackType
return|;
block|}
specifier|public
name|void
name|setCallbackType
parameter_list|(
name|CallbackType
name|callbackType
parameter_list|)
block|{
name|this
operator|.
name|callbackType
operator|=
name|callbackType
expr_stmt|;
block|}
block|}
end_class

end_unit

