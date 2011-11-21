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
name|configuration
operator|.
name|rop
operator|.
name|client
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|DataChannel
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
name|cache
operator|.
name|MapQueryCacheProvider
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
name|cache
operator|.
name|QueryCache
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
name|DefaultRuntimeProperties
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
name|ObjectContextFactory
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
name|RuntimeProperties
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
name|Module
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
name|DefaultEventManager
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
name|EventManager
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
name|remote
operator|.
name|ClientConnection
import|;
end_import

begin_comment
comment|/**  * A DI module containing all Cayenne ROP client runtime configurations.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|ClientModule
implements|implements
name|Module
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ROP_SERVICE_URL
init|=
literal|"cayenne.config.rop.service.url"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROP_SERVICE_USER_NAME
init|=
literal|"cayenne.config.rop.service.username"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROP_SERVICE_PASSWORD
init|=
literal|"cayenne.config.rop.service.password"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROP_SERVICE_SHARED_SESSION
init|=
literal|"cayenne.config.rop.service.shared_session"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROP_SERVICE_TIMEOUT
init|=
literal|"cayenne.config.rop.service.timeout"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CHANNEL_EVENTS
init|=
literal|"cayenne.config.rop.client.channel.events"
decl_stmt|;
comment|// TODO: this property name is exactly the same as CHANNEL_EVENTS... Seems messed up
specifier|public
specifier|static
specifier|final
name|String
name|CHANNEL_REMOTE_EVENTS_OPTIONAL
init|=
literal|"cayenne.config.rop.client.channel.events"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONTEXT_CHANGE_EVENTS
init|=
literal|"cayenne.config.rop.client.context.change_events"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CONTEXT_LIFECYCLE_EVENTS
init|=
literal|"cayenne.config.rop.client.context.lifecycle_events"
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
decl_stmt|;
specifier|public
name|ClientModule
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
block|{
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null 'properties' map"
argument_list|)
throw|;
block|}
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
comment|// expose this module properties to DefaultRuntimeProperties
name|binder
operator|.
expr|<
name|String
operator|>
name|bindMap
argument_list|(
name|DefaultRuntimeProperties
operator|.
name|PROPERTIES_MAP
argument_list|)
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ObjectContextFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|CayenneContextFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ClientConnection
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|HessianConnectionProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|EventManager
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultEventManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|RuntimeProperties
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultRuntimeProperties
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataChannel
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ClientChannelProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|QueryCache
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|MapQueryCacheProvider
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

