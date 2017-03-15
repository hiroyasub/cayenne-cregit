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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|Binder
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
name|di
operator|.
name|MapBuilder
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
name|di
operator|.
name|Module
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|JMSModule
implements|implements
name|Module
block|{
comment|/**      * A DI container key for the Map&lt;String, String&gt; storing      * {@link org.apache.cayenne.event.JMSBridge} properties      *      * @since 4.0      */
specifier|public
specifier|static
specifier|final
name|String
name|JMS_BRIDGE_PROPERTIES_MAP
init|=
literal|"cayenne.server.jms_bridge"
decl_stmt|;
specifier|public
specifier|static
name|void
name|contributeTopicConnectionFactory
parameter_list|(
name|Binder
name|binder
parameter_list|,
name|String
name|factory
parameter_list|)
block|{
name|contributeProperties
argument_list|(
name|binder
argument_list|)
operator|.
name|put
argument_list|(
name|JMSBridge
operator|.
name|TOPIC_CONNECTION_FACTORY_PROPERTY
argument_list|,
name|factory
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|MapBuilder
argument_list|<
name|String
argument_list|>
name|contributeProperties
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
return|return
name|binder
operator|.
name|bindMap
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|JMS_BRIDGE_PROPERTIES_MAP
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
comment|// init properties' defaults
name|contributeTopicConnectionFactory
argument_list|(
name|binder
argument_list|,
name|JMSBridge
operator|.
name|TOPIC_CONNECTION_FACTORY_DEFAULT
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|EventBridge
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|JMSBridgeProvider
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

