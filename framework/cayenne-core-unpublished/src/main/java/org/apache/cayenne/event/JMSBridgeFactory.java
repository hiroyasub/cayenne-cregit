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
name|event
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
name|Map
import|;
end_import

begin_comment
comment|/**  * Factory to create JMSBridge instances.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|JMSBridgeFactory
implements|implements
name|EventBridgeFactory
block|{
comment|// this is an OpenJMS default for the factory name. Likely it won't work with
comment|// anything else
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_CONNECTION_FACTORY_DEFAULT
init|=
literal|"JmsTopicConnectionFactory"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC_CONNECTION_FACTORY_PROPERTY
init|=
literal|"cayenne.JMSBridge.topic.connection.factory"
decl_stmt|;
comment|/**      * @since 1.2      */
specifier|public
name|EventBridge
name|createEventBridge
parameter_list|(
name|Collection
argument_list|<
name|EventSubject
argument_list|>
name|localSubjects
parameter_list|,
name|String
name|externalSubject
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|JMSBridge
name|bridge
init|=
operator|new
name|JMSBridge
argument_list|(
name|localSubjects
argument_list|,
name|externalSubject
argument_list|)
decl_stmt|;
comment|// configure properties
name|String
name|topicConnectionFactory
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
name|TOPIC_CONNECTION_FACTORY_PROPERTY
argument_list|)
decl_stmt|;
name|bridge
operator|.
name|setTopicConnectionFactoryName
argument_list|(
name|topicConnectionFactory
operator|!=
literal|null
condition|?
name|topicConnectionFactory
else|:
name|TOPIC_CONNECTION_FACTORY_DEFAULT
argument_list|)
expr_stmt|;
return|return
name|bridge
return|;
block|}
block|}
end_class

end_unit
