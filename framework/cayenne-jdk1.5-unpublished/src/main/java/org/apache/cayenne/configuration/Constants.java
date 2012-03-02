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
package|;
end_package

begin_comment
comment|/**  * Defines the names of runtime properties and DI collections used in DI modules used to  * configure server and client runtime.  *   * @since 3.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|Constants
block|{
comment|// DI "collections"
comment|/**      * A DI container key for the properties map used to configure either ROP or server      * tiers.      */
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTIES_MAP
init|=
literal|"cayenne.properties"
decl_stmt|;
comment|/**      * A DI container key for the List<DbAdapterDetector> that contains objects that can      * discover the type of current database and install the correct DbAdapter in runtime.      */
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_ADAPTER_DETECTORS_LIST
init|=
literal|"cayenne.server.adapter_detectors"
decl_stmt|;
comment|/**      * A DI container key for the list storing DataDomain filters.      */
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_DOMAIN_FILTERS_LIST
init|=
literal|"cayenne.server.domain_filters"
decl_stmt|;
comment|/**      * A DI container key for the list storing locations of the one of more project      * configuration files.      */
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_PROJECT_LOCATIONS_LIST
init|=
literal|"cayenne.server.project_locations"
decl_stmt|;
comment|/**      * A DI container key for the List<ExtendedType> storing default adapter-agnostic      * ExtendedTypes.      */
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_DEFAULT_TYPES_LIST
init|=
literal|"cayenne.server.default_types"
decl_stmt|;
comment|/**      * A DI container key for the List<ExtendedType> storing a user-provided      * ExtendedTypes.      */
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_USER_TYPES_LIST
init|=
literal|"cayenne.server.user_types"
decl_stmt|;
comment|/**      * A DI container key for the List<ExtendedTypeFactory> storing default and      * user-provided ExtendedTypeFactories.      */
specifier|public
specifier|static
specifier|final
name|String
name|SERVER_TYPE_FACTORIES_LIST
init|=
literal|"cayenne.server.type_factories"
decl_stmt|;
block|}
end_interface

end_unit

