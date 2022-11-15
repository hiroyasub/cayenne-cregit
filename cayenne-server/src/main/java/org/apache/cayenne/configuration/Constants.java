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
name|configuration
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
name|types
operator|.
name|ExtendedType
import|;
end_import

begin_comment
comment|/**  * Defines the names of runtime properties and named collections used in DI modules.  *  * @since 3.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|Constants
block|{
comment|// DI "collections"
comment|/**      * A DI container key for the Map&lt;String, String&gt; storing properties      * used by built-in Cayenne service.      *      * @see org.apache.cayenne.configuration.server.ServerModuleExtender#setProperty(String, Object)      */
name|String
name|PROPERTIES_MAP
init|=
literal|"cayenne.properties"
decl_stmt|;
comment|/**      * A DI container key for the List&lt;DbAdapterDetector&gt; that contains      * objects that can discover the type of current database and install the      * correct DbAdapter in runtime.      */
name|String
name|SERVER_ADAPTER_DETECTORS_LIST
init|=
literal|"cayenne.server.adapter_detectors"
decl_stmt|;
comment|/**      * A DI container key for the List&lt;Object&gt; storing lifecycle events listeners.      *      * @see org.apache.cayenne.configuration.server.ServerModuleExtender#addListener(Object)       */
name|String
name|SERVER_DOMAIN_LISTENERS_LIST
init|=
literal|"cayenne.server.domain_listeners"
decl_stmt|;
comment|/**      * A DI container key for the List&lt;String&gt; storing locations of the      * one of more project configuration files.      */
name|String
name|SERVER_PROJECT_LOCATIONS_LIST
init|=
literal|"cayenne.server.project_locations"
decl_stmt|;
comment|/**      * A DI container key for the List&lt;ExtendedType&gt; storing default      * adapter-agnostic ExtendedTypes.      *      * @see org.apache.cayenne.configuration.server.ServerModuleExtender#addDefaultExtendedType(ExtendedType)       */
name|String
name|SERVER_DEFAULT_TYPES_LIST
init|=
literal|"cayenne.server.default_types"
decl_stmt|;
comment|/**      * A DI container key for the List&lt;ExtendedType&gt; storing a      * user-provided ExtendedTypes.      *      * @see org.apache.cayenne.configuration.server.ServerModuleExtender#addUserExtendedType(ExtendedType)       */
name|String
name|SERVER_USER_TYPES_LIST
init|=
literal|"cayenne.server.user_types"
decl_stmt|;
comment|/**      * A DI container key for the List&lt;ExtendedTypeFactory&gt; storing      * default and user-provided ExtendedTypeFactories.      *      * @see org.apache.cayenne.configuration.server.ServerModuleExtender#addExtendedTypeFactory(Class)       */
name|String
name|SERVER_TYPE_FACTORIES_LIST
init|=
literal|"cayenne.server.type_factories"
decl_stmt|;
comment|/**      * A server-side DI container key for binding {@link org.apache.cayenne.resource.ResourceLocator}      */
name|String
name|SERVER_RESOURCE_LOCATOR
init|=
literal|"cayenne.server.resource_locator"
decl_stmt|;
comment|// Runtime properties
name|String
name|JDBC_DRIVER_PROPERTY
init|=
literal|"cayenne.jdbc.driver"
decl_stmt|;
name|String
name|JDBC_URL_PROPERTY
init|=
literal|"cayenne.jdbc.url"
decl_stmt|;
name|String
name|JDBC_USERNAME_PROPERTY
init|=
literal|"cayenne.jdbc.username"
decl_stmt|;
name|String
name|JDBC_PASSWORD_PROPERTY
init|=
literal|"cayenne.jdbc.password"
decl_stmt|;
name|String
name|JDBC_MIN_CONNECTIONS_PROPERTY
init|=
literal|"cayenne.jdbc.min_connections"
decl_stmt|;
name|String
name|JDBC_MAX_CONNECTIONS_PROPERTY
init|=
literal|"cayenne.jdbc.max_connections"
decl_stmt|;
comment|/**      * Defines a maximum time in milliseconds that a connection request could      * wait in the connection queue. After this period expires, an exception      * will be thrown in the calling method. A value of zero will make the      * thread wait until a connection is available with no time out. Defaults to      * 20 seconds.      *      * @since 4.0      */
name|String
name|JDBC_MAX_QUEUE_WAIT_TIME
init|=
literal|"cayenne.jdbc.max_wait"
decl_stmt|;
comment|/**      * @since 4.0      */
name|String
name|JDBC_VALIDATION_QUERY_PROPERTY
init|=
literal|"cayenne.jdbc.validation_query"
decl_stmt|;
comment|/**      * An integer property defining the maximum number of entries in the query      * cache. Note that not all QueryCache providers may respect this property.      * MapQueryCache uses it, but the rest would use alternative configuration      * methods.      */
name|String
name|QUERY_CACHE_SIZE_PROPERTY
init|=
literal|"cayenne.querycache.size"
decl_stmt|;
comment|/**      * An optional name of the runtime DataDomain. If not specified (which is      * normally the case), the name is inferred from the configuration name.      *      * @since 4.0      */
name|String
name|SERVER_DOMAIN_NAME_PROPERTY
init|=
literal|"cayenne.server.domain.name"
decl_stmt|;
comment|/**      * A boolean property defining whether cross-contexts synchronization is      * enabled. Possible values are "true" or "false".      */
name|String
name|SERVER_CONTEXTS_SYNC_PROPERTY
init|=
literal|"cayenne.server.contexts_sync_strategy"
decl_stmt|;
comment|/**      * A String property that defines how ObjectContexts should retain cached      * committed objects. Possible values are "weak", "soft", "hard".      */
name|String
name|SERVER_OBJECT_RETAIN_STRATEGY_PROPERTY
init|=
literal|"cayenne.server.object_retain_strategy"
decl_stmt|;
comment|/**      * A boolean property that defines whether runtime should use external      * transactions. Possible values are "true" or "false".      */
name|String
name|SERVER_EXTERNAL_TX_PROPERTY
init|=
literal|"cayenne.server.external_tx"
decl_stmt|;
comment|/**      * A property that defines a maximum number of ID qualifiers in where clause      * of queries that are generated for example in      * {@link org.apache.cayenne.access.IncrementalFaultList} or in      * DISJOINT_BY_ID prefetch processing. This is needed to avoid where clause      * size limitations and memory usage efficiency.      */
name|String
name|SERVER_MAX_ID_QUALIFIER_SIZE_PROPERTY
init|=
literal|"cayenne.server.max_id_qualifier_size"
decl_stmt|;
comment|/**      * Defines if database uses case-insensitive collation      */
name|String
name|CI_PROPERTY
init|=
literal|"cayenne.runtime.db.collation.assume.ci"
decl_stmt|;
comment|/**      * A integer property that enables logging for just long running queries      * (rather than all queries). The value is the minimum number of      * milliseconds a query must run before is logged. A value less than or      * equal to zero (the default) disables this feature.      *      * @since 4.0      */
name|String
name|QUERY_EXECUTION_TIME_LOGGING_THRESHOLD_PROPERTY
init|=
literal|"cayenne.server.query_execution_time_logging_threshold"
decl_stmt|;
comment|/**      * Snapshot cache max size      *      * @see org.apache.cayenne.configuration.server.ServerModuleExtender#snapshotCacheSize(int)       * @since 4.0      */
name|String
name|SNAPSHOT_CACHE_SIZE_PROPERTY
init|=
literal|"cayenne.DataRowStore.snapshot.size"
decl_stmt|;
block|}
end_interface

end_unit

