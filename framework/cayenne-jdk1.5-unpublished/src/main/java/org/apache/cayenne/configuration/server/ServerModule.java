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
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
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
name|access
operator|.
name|DataDomain
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
name|access
operator|.
name|DefaultObjectMapRetainStrategy
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
name|access
operator|.
name|ObjectMapRetainStrategy
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
name|access
operator|.
name|dbsync
operator|.
name|SchemaUpdateStrategy
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
name|access
operator|.
name|dbsync
operator|.
name|SkipSchemaUpdateStrategy
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
name|access
operator|.
name|jdbc
operator|.
name|BatchQueryBuilderFactory
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
name|access
operator|.
name|jdbc
operator|.
name|DefaultBatchQueryBuilderFactory
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
name|access
operator|.
name|types
operator|.
name|BigDecimalType
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
name|access
operator|.
name|types
operator|.
name|BigIntegerType
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
name|access
operator|.
name|types
operator|.
name|BooleanType
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
name|access
operator|.
name|types
operator|.
name|ByteArrayType
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
name|access
operator|.
name|types
operator|.
name|ByteType
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
name|access
operator|.
name|types
operator|.
name|CalendarType
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
name|access
operator|.
name|types
operator|.
name|CharType
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
name|access
operator|.
name|types
operator|.
name|DateType
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
name|access
operator|.
name|types
operator|.
name|DoubleType
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
name|access
operator|.
name|types
operator|.
name|FloatType
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
name|access
operator|.
name|types
operator|.
name|IntegerType
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
name|access
operator|.
name|types
operator|.
name|LongType
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
name|access
operator|.
name|types
operator|.
name|ShortType
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
name|access
operator|.
name|types
operator|.
name|TimeType
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
name|access
operator|.
name|types
operator|.
name|TimestampType
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
name|access
operator|.
name|types
operator|.
name|UUIDType
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
name|access
operator|.
name|types
operator|.
name|UtilDateType
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
name|access
operator|.
name|types
operator|.
name|VoidType
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
name|ashwood
operator|.
name|AshwoodEntitySorter
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
name|ConfigurationNameMapper
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
name|Constants
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
name|DataChannelDescriptorLoader
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
name|DataChannelDescriptorMerger
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
name|DataMapLoader
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
name|DefaultConfigurationNameMapper
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
name|DefaultDataChannelDescriptorMerger
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
name|DefaultObjectStoreFactory
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
name|ObjectStoreFactory
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
name|configuration
operator|.
name|XMLDataChannelDescriptorLoader
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
name|XMLDataMapLoader
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
name|dba
operator|.
name|db2
operator|.
name|DB2Sniffer
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
name|dba
operator|.
name|derby
operator|.
name|DerbySniffer
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
name|dba
operator|.
name|frontbase
operator|.
name|FrontBaseSniffer
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
name|dba
operator|.
name|h2
operator|.
name|H2Sniffer
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
name|dba
operator|.
name|hsqldb
operator|.
name|HSQLDBSniffer
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
name|dba
operator|.
name|ingres
operator|.
name|IngresSniffer
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
name|dba
operator|.
name|mysql
operator|.
name|MySQLSniffer
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
name|dba
operator|.
name|openbase
operator|.
name|OpenBaseSniffer
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
name|dba
operator|.
name|oracle
operator|.
name|OracleSniffer
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
name|dba
operator|.
name|postgres
operator|.
name|PostgresSniffer
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
name|dba
operator|.
name|sqlite
operator|.
name|SQLiteSniffer
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
name|dba
operator|.
name|sqlserver
operator|.
name|SQLServerSniffer
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
name|dba
operator|.
name|sybase
operator|.
name|SybaseSniffer
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
name|AdhocObjectFactory
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
name|ListBuilder
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
name|di
operator|.
name|spi
operator|.
name|DefaultAdhocObjectFactory
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
name|log
operator|.
name|CommonsJdbcEventLogger
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
name|log
operator|.
name|JdbcEventLogger
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
name|EntitySorter
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
name|resource
operator|.
name|ClassLoaderResourceLocator
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
name|resource
operator|.
name|ResourceLocator
import|;
end_import

begin_comment
comment|/**  * A DI module containing all Cayenne server runtime configuration.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|ServerModule
implements|implements
name|Module
block|{
specifier|protected
name|String
index|[]
name|configurationLocations
decl_stmt|;
comment|/**      * Creates a ServerModule with at least one configuration location. For multi-module      * projects additional locations can be specified as well.      */
specifier|public
name|ServerModule
parameter_list|(
name|String
modifier|...
name|configurationLocations
parameter_list|)
block|{
if|if
condition|(
name|configurationLocations
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null configurationLocations"
argument_list|)
throw|;
block|}
if|if
condition|(
name|configurationLocations
operator|.
name|length
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Empty configurationLocations"
argument_list|)
throw|;
block|}
name|this
operator|.
name|configurationLocations
operator|=
name|configurationLocations
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
comment|// configure empty global stack properties
name|binder
operator|.
name|bindMap
argument_list|(
name|Constants
operator|.
name|PROPERTIES_MAP
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|JdbcEventLogger
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|CommonsJdbcEventLogger
operator|.
name|class
argument_list|)
expr_stmt|;
name|AdhocObjectFactory
name|objectFactory
init|=
operator|new
name|DefaultAdhocObjectFactory
argument_list|()
decl_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|AdhocObjectFactory
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|objectFactory
argument_list|)
expr_stmt|;
comment|// configure known DbAdapter detectors in reverse order of popularity. Users can
comment|// add their own to install custom adapters automatically
name|binder
operator|.
name|bindList
argument_list|(
name|Constants
operator|.
name|SERVER_ADAPTER_DETECTORS_LIST
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|OpenBaseSniffer
argument_list|(
name|objectFactory
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|FrontBaseSniffer
argument_list|(
name|objectFactory
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|IngresSniffer
argument_list|(
name|objectFactory
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|SQLiteSniffer
argument_list|(
name|objectFactory
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|DB2Sniffer
argument_list|(
name|objectFactory
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|H2Sniffer
argument_list|(
name|objectFactory
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|HSQLDBSniffer
argument_list|(
name|objectFactory
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|SybaseSniffer
argument_list|(
name|objectFactory
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|DerbySniffer
argument_list|(
name|objectFactory
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|SQLServerSniffer
argument_list|(
name|objectFactory
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|OracleSniffer
argument_list|(
name|objectFactory
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|PostgresSniffer
argument_list|(
name|objectFactory
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|MySQLSniffer
argument_list|(
name|objectFactory
argument_list|)
argument_list|)
expr_stmt|;
comment|// configure an empty filter chain
name|binder
operator|.
name|bindList
argument_list|(
name|Constants
operator|.
name|SERVER_DOMAIN_FILTERS_LIST
argument_list|)
expr_stmt|;
comment|// configure extended types
name|binder
operator|.
name|bindList
argument_list|(
name|Constants
operator|.
name|SERVER_DEFAULT_TYPES_LIST
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|VoidType
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|BigDecimalType
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|BigIntegerType
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|BooleanType
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|ByteArrayType
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|ByteType
argument_list|(
literal|false
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|CharType
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|DateType
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|DoubleType
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|FloatType
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|IntegerType
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|LongType
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|ShortType
argument_list|(
literal|false
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|TimeType
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|TimestampType
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|UtilDateType
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|CalendarType
argument_list|<
name|GregorianCalendar
argument_list|>
argument_list|(
name|GregorianCalendar
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|CalendarType
argument_list|<
name|Calendar
argument_list|>
argument_list|(
name|Calendar
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|UUIDType
argument_list|()
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bindList
argument_list|(
name|Constants
operator|.
name|SERVER_USER_TYPES_LIST
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bindList
argument_list|(
name|Constants
operator|.
name|SERVER_TYPE_FACTORIES_LIST
argument_list|)
expr_stmt|;
comment|// configure explicit configurations
name|ListBuilder
argument_list|<
name|Object
argument_list|>
name|locationsListBuilder
init|=
name|binder
operator|.
name|bindList
argument_list|(
name|Constants
operator|.
name|SERVER_PROJECT_LOCATIONS_LIST
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|location
range|:
name|configurationLocations
control|)
block|{
name|locationsListBuilder
operator|.
name|add
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
name|binder
operator|.
name|bind
argument_list|(
name|ConfigurationNameMapper
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultConfigurationNameMapper
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
comment|// a service to provide the main stack DataDomain
name|binder
operator|.
name|bind
argument_list|(
name|DataDomain
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|DataDomainProvider
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// will return DataDomain for request for a DataChannel
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
name|DomainDataChannelProvider
operator|.
name|class
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
name|DataContextFactory
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// a service to load project XML descriptors
name|binder
operator|.
name|bind
argument_list|(
name|DataChannelDescriptorLoader
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|XMLDataChannelDescriptorLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataChannelDescriptorMerger
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultDataChannelDescriptorMerger
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// a service to load DataMap XML descriptors
name|binder
operator|.
name|bind
argument_list|(
name|DataMapLoader
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|XMLDataMapLoader
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// a locator of resources, such as XML descriptors
name|binder
operator|.
name|bind
argument_list|(
name|ResourceLocator
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ClassLoaderResourceLocator
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// a global properties object
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
comment|// a service to load DataSourceFactories. DelegatingDataSourceFactory will attempt
comment|// to find the actual worker factory dynamically on each call depending on
comment|// DataNodeDescriptor data and the environment
name|binder
operator|.
name|bind
argument_list|(
name|DataSourceFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DelegatingDataSourceFactory
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// a default SchemaUpdateStrategy (used when no explicit strategy is specified in
comment|// XML)
name|binder
operator|.
name|bind
argument_list|(
name|SchemaUpdateStrategy
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|SkipSchemaUpdateStrategy
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// a default DBAdapterFactory used to load custom and automatic DbAdapters
name|binder
operator|.
name|bind
argument_list|(
name|DbAdapterFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultDbAdapterFactory
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// binding AshwoodEntitySorter without scope, as this is a stateful object and is
comment|// configured by the owning domain
name|binder
operator|.
name|bind
argument_list|(
name|EntitySorter
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|AshwoodEntitySorter
operator|.
name|class
argument_list|)
operator|.
name|withoutScope
argument_list|()
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|BatchQueryBuilderFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultBatchQueryBuilderFactory
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// a default ObjectMapRetainStrategy used to create objects map for ObjectStore
name|binder
operator|.
name|bind
argument_list|(
name|ObjectMapRetainStrategy
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultObjectMapRetainStrategy
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// a default ObjectStoreFactory used to create ObjectStores for contexts
name|binder
operator|.
name|bind
argument_list|(
name|ObjectStoreFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultObjectStoreFactory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

