begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *    Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
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

begin_comment
comment|/**  * Defines the names of runtime properties and named collections used in DI modules related to ROP client.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ClientConstants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ROP_SERVICE_URL_PROPERTY
init|=
literal|"cayenne.rop.service_url"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROP_SERVICE_USERNAME_PROPERTY
init|=
literal|"cayenne.rop.service_username"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROP_SERVICE_PASSWORD_PROPERTY
init|=
literal|"cayenne.rop.service_password"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROP_SERVICE_REALM_PROPERTY
init|=
literal|"cayenne.rop.service_realm"
decl_stmt|;
comment|/**      * A boolean property that defines whether ALPN should be used. Possible values are "true" or "false".      */
specifier|public
specifier|static
specifier|final
name|String
name|ROP_SERVICE_USE_ALPN_PROPERTY
init|=
literal|"cayenne.rop.service_use_alpn"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROP_SERVICE_SHARED_SESSION_PROPERTY
init|=
literal|"cayenne.rop.shared_session_name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROP_SERVICE_TIMEOUT_PROPERTY
init|=
literal|"cayenne.rop.service_timeout"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROP_CHANNEL_EVENTS_PROPERTY
init|=
literal|"cayenne.rop.channel_events"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROP_CONTEXT_CHANGE_EVENTS_PROPERTY
init|=
literal|"cayenne.rop.context_change_events"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ROP_CONTEXT_LIFECYCLE_EVENTS_PROPERTY
init|=
literal|"cayenne.rop.context_lifecycle_events"
decl_stmt|;
block|}
end_class

end_unit

