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
name|dbsync
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactoryProvider
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|DB2MergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|DefaultMergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|DerbyMergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|FirebirdMergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|H2MergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|HSQLMergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|IngresMergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|MySQLMergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|OracleMergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|PostgresMergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|SQLServerMergerTokenFactory
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|SybaseMergerTokenFactory
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
name|DbSyncModule
implements|implements
name|Module
block|{
comment|/**      * A DI container key for the Map&lt;String, String&gt; storing properties      * used by built-in Cayenne service.      */
specifier|public
specifier|static
specifier|final
name|String
name|MERGER_FACTORIES_MAP
init|=
literal|"cayenne.dbsync.mergerfactories"
decl_stmt|;
specifier|public
specifier|static
name|MapBuilder
argument_list|<
name|MergerTokenFactory
argument_list|>
name|contributeMergerTokenFactories
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
name|MergerTokenFactory
operator|.
name|class
argument_list|,
name|MERGER_FACTORIES_MAP
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
comment|// default and per adapter merger factories...
name|binder
operator|.
name|bind
argument_list|(
name|MergerTokenFactory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DefaultMergerTokenFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|contributeMergerTokenFactories
argument_list|(
name|binder
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
name|DB2MergerTokenFactory
operator|.
name|class
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
name|DerbyMergerTokenFactory
operator|.
name|class
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
name|FirebirdMergerTokenFactory
operator|.
name|class
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
name|H2MergerTokenFactory
operator|.
name|class
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
name|HSQLMergerTokenFactory
operator|.
name|class
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
name|IngresMergerTokenFactory
operator|.
name|class
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
name|MySQLMergerTokenFactory
operator|.
name|class
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
name|OracleMergerTokenFactory
operator|.
name|class
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
name|OracleMergerTokenFactory
operator|.
name|class
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
name|PostgresMergerTokenFactory
operator|.
name|class
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
name|SQLServerMergerTokenFactory
operator|.
name|class
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
name|SybaseMergerTokenFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|binder
operator|.
name|bind
argument_list|(
name|MergerTokenFactoryProvider
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|MergerTokenFactoryProvider
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

