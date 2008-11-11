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
name|dialog
operator|.
name|datadomain
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
name|access
operator|.
name|DataRowStore
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
name|event
operator|.
name|JMSBridgeFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|scopemvc
operator|.
name|core
operator|.
name|Selector
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|JMSConfigModel
extends|extends
name|CacheSyncConfigModel
block|{
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|storedProperties
init|=
operator|new
name|String
index|[]
block|{
name|DataRowStore
operator|.
name|EVENT_BRIDGE_FACTORY_PROPERTY
block|,
name|JMSBridgeFactory
operator|.
name|TOPIC_CONNECTION_FACTORY_PROPERTY
block|}
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Selector
name|TOPIC_FACTORY_SELECTOR
init|=
name|Selector
operator|.
name|fromString
argument_list|(
literal|"topicFactory"
argument_list|)
decl_stmt|;
specifier|public
name|String
index|[]
name|supportedProperties
parameter_list|()
block|{
return|return
name|storedProperties
return|;
block|}
specifier|public
name|Selector
name|selectorForKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
operator|(
name|JMSBridgeFactory
operator|.
name|TOPIC_CONNECTION_FACTORY_PROPERTY
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|)
condition|?
name|TOPIC_FACTORY_SELECTOR
else|:
literal|null
return|;
block|}
specifier|public
name|String
name|defaultForKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
operator|(
name|JMSBridgeFactory
operator|.
name|TOPIC_CONNECTION_FACTORY_PROPERTY
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|)
condition|?
name|JMSBridgeFactory
operator|.
name|TOPIC_CONNECTION_FACTORY_DEFAULT
else|:
literal|null
return|;
block|}
specifier|public
name|String
name|getTopicFactory
parameter_list|()
block|{
return|return
name|getProperty
argument_list|(
name|JMSBridgeFactory
operator|.
name|TOPIC_CONNECTION_FACTORY_PROPERTY
argument_list|)
return|;
block|}
specifier|public
name|void
name|setTopicFactory
parameter_list|(
name|String
name|topicFactory
parameter_list|)
block|{
name|setProperty
argument_list|(
name|JMSBridgeFactory
operator|.
name|TOPIC_CONNECTION_FACTORY_PROPERTY
argument_list|,
name|topicFactory
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

