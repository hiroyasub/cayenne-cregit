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
name|unit
operator|.
name|di
operator|.
name|server
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
name|ObjectContext
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
name|DataContext
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
name|DataNode
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
name|translator
operator|.
name|batch
operator|.
name|BatchTranslatorFactory
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
name|BigIntegerValueType
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
name|DefaultValueObjectTypeRegistry
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
name|LocalDateTimeValueType
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
name|LocalDateValueType
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
name|LocalTimeValueType
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
name|UUIDValueType
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
name|ValueObjectTypeRegistry
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
name|server
operator|.
name|DataSourceFactory
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
name|server
operator|.
name|ServerModule
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
name|server
operator|.
name|ServerRuntime
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
name|xml
operator|.
name|DataChannelMetaData
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
name|xml
operator|.
name|DefaultHandlerFactory
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
name|xml
operator|.
name|HandlerFactory
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
name|xml
operator|.
name|NoopDataChannelMetaData
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
name|xml
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
name|configuration
operator|.
name|xml
operator|.
name|XMLReaderProvider
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
name|conn
operator|.
name|DataSourceInfo
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
name|DbAdapter
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
name|JdbcAdapter
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
name|DB2Adapter
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
name|DerbyAdapter
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
name|firebird
operator|.
name|FirebirdAdapter
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
name|FrontBaseAdapter
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
name|H2Adapter
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
name|HSQLDBAdapter
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
name|IngresAdapter
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
name|MySQLAdapter
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
name|OpenBaseAdapter
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
name|Oracle8Adapter
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
name|OracleAdapter
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
name|PostgresAdapter
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
name|SQLiteAdapter
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
name|SQLServerAdapter
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
name|SybaseAdapter
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
name|ClassLoaderManager
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
name|Key
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
name|di
operator|.
name|spi
operator|.
name|DefaultClassLoaderManager
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
name|DefaultScope
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
name|Slf4jJdbcEventLogger
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
name|EntityResolver
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|unit
operator|.
name|DB2UnitDbAdapter
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
name|unit
operator|.
name|DerbyUnitDbAdapter
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
name|unit
operator|.
name|FirebirdUnitDbAdapter
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
name|unit
operator|.
name|FrontBaseUnitDbAdapter
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
name|unit
operator|.
name|H2UnitDbAdapter
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
name|unit
operator|.
name|HSQLDBUnitDbAdapter
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
name|unit
operator|.
name|IngresUnitDbAdapter
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
name|unit
operator|.
name|MySQLUnitDbAdapter
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
name|unit
operator|.
name|OpenBaseUnitDbAdapter
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
name|unit
operator|.
name|OracleUnitDbAdapter
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
name|unit
operator|.
name|PostgresUnitDbAdapter
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
name|unit
operator|.
name|SQLServerUnitDbAdapter
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
name|unit
operator|.
name|SQLiteUnitDbAdapter
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
name|unit
operator|.
name|SybaseUnitDbAdapter
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
name|unit
operator|.
name|UnitDbAdapter
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
name|unit
operator|.
name|di
operator|.
name|DataChannelInterceptor
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
name|unit
operator|.
name|di
operator|.
name|UnitTestLifecycleManager
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
name|unit
operator|.
name|util
operator|.
name|SQLTemplateCustomizer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|XMLReader
import|;
end_import

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

begin_class
specifier|public
class|class
name|ServerCaseModule
implements|implements
name|Module
block|{
specifier|protected
name|DefaultScope
name|testScope
decl_stmt|;
specifier|public
name|ServerCaseModule
parameter_list|(
name|DefaultScope
name|testScope
parameter_list|)
block|{
name|this
operator|.
name|testScope
operator|=
name|testScope
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
comment|// these are the objects injectable in unit tests that subclass from
comment|// ServerCase. Note that ServerRuntimeProvider creates ServerRuntime
comment|// instances complete with their own DI injectors, independent from the
comment|// unit test injector. ServerRuntime injector contents are customized
comment|// inside ServerRuntimeProvider.
name|binder
operator|.
name|bindMap
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|UnitDbAdapterProvider
operator|.
name|TEST_ADAPTERS_MAP
argument_list|)
operator|.
name|put
argument_list|(
name|FirebirdAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|FirebirdUnitDbAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|OracleAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|OracleUnitDbAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|DerbyAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|DerbyUnitDbAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|Oracle8Adapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|OracleUnitDbAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|SybaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|SybaseUnitDbAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|MySQLAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|MySQLUnitDbAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|PostgresAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|PostgresUnitDbAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|OpenBaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|OpenBaseUnitDbAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|SQLServerAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|SQLServerUnitDbAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|DB2Adapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|DB2UnitDbAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|HSQLDBAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|HSQLDBUnitDbAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|H2Adapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|H2UnitDbAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|FrontBaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|FrontBaseUnitDbAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|IngresAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|IngresUnitDbAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|SQLiteAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|SQLiteUnitDbAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ServerModule
operator|.
name|contributeProperties
argument_list|(
name|binder
argument_list|)
expr_stmt|;
comment|// configure extended types
name|ServerModule
operator|.
name|contributeDefaultTypes
argument_list|(
name|binder
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
argument_list|<>
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
argument_list|<>
argument_list|(
name|Calendar
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|ServerModule
operator|.
name|contributeUserTypes
argument_list|(
name|binder
argument_list|)
expr_stmt|;
name|ServerModule
operator|.
name|contributeTypeFactories
argument_list|(
name|binder
argument_list|)
expr_stmt|;
name|ServerModule
operator|.
name|contributeValueObjectTypes
argument_list|(
name|binder
argument_list|)
operator|.
name|add
argument_list|(
name|BigIntegerValueType
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|UUIDValueType
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|LocalDateValueType
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|LocalTimeValueType
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|LocalDateTimeValueType
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ValueObjectTypeRegistry
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultValueObjectTypeRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|SchemaBuilder
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|SchemaBuilder
operator|.
name|class
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
name|Slf4jJdbcEventLogger
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
comment|// singleton objects
name|binder
operator|.
name|bind
argument_list|(
name|UnitTestLifecycleManager
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|ServerCaseLifecycleManager
argument_list|(
name|testScope
argument_list|)
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataSourceInfo
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ServerCaseDataSourceInfoProvider
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|ServerCaseSharedDataSourceFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DbAdapter
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ServerCaseDbAdapterProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|JdbcAdapter
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ServerCaseDbAdapterProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|UnitDbAdapter
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|UnitDbAdapterProvider
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// this factory is a hack that allows to inject to DbAdapters loaded outside of
comment|// server runtime... BatchQueryBuilderFactory is hardcoded and whatever is placed
comment|// in the ServerModule is ignored
name|binder
operator|.
name|bind
argument_list|(
name|BatchTranslatorFactory
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ServerCaseBatchQueryBuilderFactoryProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataChannelInterceptor
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ServerCaseDataChannelInterceptor
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|SQLTemplateCustomizer
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|SQLTemplateCustomizerProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ServerCaseDataSourceFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ServerCaseDataSourceFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ClassLoaderManager
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultClassLoaderManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|AdhocObjectFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultAdhocObjectFactory
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|binder
operator|.
name|bind
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|ResourceLocator
operator|.
name|class
argument_list|,
name|Constants
operator|.
name|SERVER_RESOURCE_LOCATOR
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|ClassLoaderResourceLocator
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|HandlerFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultHandlerFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataChannelMetaData
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|NoopDataChannelMetaData
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|XMLReader
operator|.
name|class
argument_list|)
operator|.
name|toProviderInstance
argument_list|(
operator|new
name|XMLReaderProvider
argument_list|(
literal|false
argument_list|)
argument_list|)
operator|.
name|withoutScope
argument_list|()
expr_stmt|;
comment|// test-scoped objects
name|binder
operator|.
name|bind
argument_list|(
name|EntityResolver
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ServerCaseEntityResolverProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|testScope
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DataNode
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ServerCaseDataNodeProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|testScope
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ServerCaseProperties
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ServerCaseProperties
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|testScope
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ServerRuntime
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ServerRuntimeProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|testScope
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|ObjectContext
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ServerCaseObjectContextProvider
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
name|DataContext
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|ServerCaseDataContextProvider
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
name|DBHelper
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|FlavoredDBHelperProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|testScope
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DBCleaner
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|DBCleanerProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|testScope
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

