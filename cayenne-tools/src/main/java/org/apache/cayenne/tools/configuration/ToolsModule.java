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
name|tools
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
name|translator
operator|.
name|batch
operator|.
name|DefaultBatchTranslatorFactory
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
name|DbAdapterFactory
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
name|DefaultDbAdapterFactory
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
name|firebird
operator|.
name|FirebirdSniffer
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_comment
comment|/**  * A DI module to bootstrap DI container for Cayenne Ant tasks and Maven  * plugins.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ToolsModule
implements|implements
name|Module
block|{
specifier|private
name|Log
name|logger
decl_stmt|;
specifier|public
name|ToolsModule
parameter_list|(
name|Log
name|logger
parameter_list|)
block|{
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null logger"
argument_list|)
throw|;
block|}
name|this
operator|.
name|logger
operator|=
name|logger
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
name|binder
operator|.
name|bind
argument_list|(
name|Log
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|logger
argument_list|)
expr_stmt|;
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
name|bindList
argument_list|(
name|Constants
operator|.
name|SERVER_DEFAULT_TYPES_LIST
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
name|BatchTranslatorFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultBatchTranslatorFactory
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
name|CommonsJdbcEventLogger
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// TODO: this is cloned from ServerModule... figure out how to reuse
comment|// this list
comment|// a bit ugly - need to bind all sniffers explicitly first before
comment|// placing then in a list
name|binder
operator|.
name|bind
argument_list|(
name|FirebirdSniffer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|FirebirdSniffer
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|OpenBaseSniffer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|OpenBaseSniffer
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|FrontBaseSniffer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|FrontBaseSniffer
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|IngresSniffer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|IngresSniffer
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|SQLiteSniffer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|SQLiteSniffer
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DB2Sniffer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DB2Sniffer
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|H2Sniffer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|H2Sniffer
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|HSQLDBSniffer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|HSQLDBSniffer
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|SybaseSniffer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|SybaseSniffer
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|DerbySniffer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DerbySniffer
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|SQLServerSniffer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|SQLServerSniffer
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|OracleSniffer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|OracleSniffer
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|PostgresSniffer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|PostgresSniffer
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|MySQLSniffer
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MySQLSniffer
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|FirebirdSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|OpenBaseSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|FrontBaseSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|IngresSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|SQLiteSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|DB2Sniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|H2Sniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|HSQLDBSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|SybaseSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|DerbySniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|SQLServerSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|OracleSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|PostgresSniffer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|MySQLSniffer
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|DriverDataSourceFactory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

