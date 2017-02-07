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
name|configuration
operator|.
name|DataNodeDescriptor
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
name|pref
operator|.
name|ProjectStatePreferences
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
name|query
operator|.
name|Query
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

begin_class
specifier|public
specifier|abstract
class|class
name|DisplayEventType
block|{
specifier|protected
name|ProjectController
name|controller
decl_stmt|;
specifier|protected
name|ProjectStatePreferences
name|preferences
decl_stmt|;
specifier|public
name|DisplayEventType
parameter_list|(
name|ProjectController
name|controller
parameter_list|)
block|{
name|this
operator|.
name|controller
operator|=
name|controller
expr_stmt|;
name|this
operator|.
name|preferences
operator|=
name|controller
operator|.
name|getProjectStatePreferences
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|abstract
name|void
name|fireLastDisplayEvent
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
name|void
name|saveLastDisplayEvent
parameter_list|()
function_decl|;
specifier|protected
name|String
name|getObjectName
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|CayenneMapEntry
condition|)
block|{
return|return
operator|(
operator|(
name|CayenneMapEntry
operator|)
name|object
operator|)
operator|.
name|getName
argument_list|()
return|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|DataChannelDescriptor
condition|)
block|{
return|return
operator|(
operator|(
name|DataChannelDescriptor
operator|)
name|object
operator|)
operator|.
name|getName
argument_list|()
return|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|DataNodeDescriptor
condition|)
block|{
return|return
operator|(
operator|(
name|DataNodeDescriptor
operator|)
name|object
operator|)
operator|.
name|getName
argument_list|()
return|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|DataMap
condition|)
block|{
return|return
operator|(
operator|(
name|DataMap
operator|)
name|object
operator|)
operator|.
name|getName
argument_list|()
return|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|Embeddable
condition|)
block|{
return|return
operator|(
operator|(
name|Embeddable
operator|)
name|object
operator|)
operator|.
name|getClassName
argument_list|()
return|;
block|}
if|else if
condition|(
name|object
operator|instanceof
name|Query
condition|)
block|{
return|return
operator|(
operator|(
name|Query
operator|)
name|object
operator|)
operator|.
name|getName
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
specifier|protected
name|String
name|parseToString
parameter_list|(
name|CayenneMapEntry
index|[]
name|array
parameter_list|)
block|{
if|if
condition|(
name|array
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|CayenneMapEntry
name|entry
range|:
name|array
control|)
block|{
if|if
condition|(
name|entry
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|sb
operator|.
name|append
argument_list|(
name|entry
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
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

