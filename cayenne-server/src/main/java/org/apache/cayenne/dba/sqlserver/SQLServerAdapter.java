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
name|dba
operator|.
name|sqlserver
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|SQLTreeProcessor
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
name|ExtendedType
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
name|ExtendedTypeFactory
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
name|Inject
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
name|query
operator|.
name|Query
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
name|query
operator|.
name|SQLAction
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
comment|/**  *<p>  * Cayenne DbAdapter implementation for<a  * href="http://www.microsoft.com/sql/">Microsoft SQL Server</a> engine.  *</p>  *<h3>Microsoft Driver Settings</h3>  *<p>  * Sample connection settings to use with MS SQL Server are shown below:  *  *<pre>  *       sqlserver.jdbc.username = test  *       sqlserver.jdbc.password = secret  *       sqlserver.jdbc.url = jdbc:sqlserver://192.168.0.65;databaseName=cayenne;SelectMethod=cursor  *       sqlserver.jdbc.driver = com.microsoft.sqlserver.jdbc.SQLServerDriver  *</pre>  *<p>  *<i>Note on case-sensitive LIKE: if your application requires case-sensitive  * LIKE support, ask your DBA to configure the database to use a case-senstitive  * collation (one with "CS" in symbolic collation name instead of "CI", e.g.  * "SQL_Latin1_general_CP1_CS_AS").</i>  *</p>  *<h3>jTDS Driver Settings</h3>  *<p>  * jTDS is an open source driver that can be downloaded from<a href=  * "http://jtds.sourceforge.net">http://jtds.sourceforge.net</a>. It supports  * both SQLServer and Sybase. Sample SQLServer settings are the following:  *</p>  *  *<pre>  *       sqlserver.jdbc.username = test  *       sqlserver.jdbc.password = secret  *       sqlserver.jdbc.url = jdbc:jtds:sqlserver://192.168.0.65/cayenne  *       sqlserver.jdbc.driver = net.sourceforge.jtds.jdbc.Driver  *</pre>  *  * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|SQLServerAdapter
extends|extends
name|SybaseAdapter
block|{
comment|/** 	 * @deprecated since 4.2 unused 	 */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|String
name|TRIM_FUNCTION
init|=
literal|"RTRIM"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|SYSTEM_SCHEMAS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"db_accessadmin"
argument_list|,
literal|"db_backupoperator"
argument_list|,
literal|"db_datareader"
argument_list|,
literal|"db_datawriter"
argument_list|,
literal|"db_ddladmin"
argument_list|,
literal|"db_denydatareader"
argument_list|,
literal|"db_denydatawriter"
argument_list|,
literal|"dbo"
argument_list|,
literal|"sys"
argument_list|,
literal|"db_owner"
argument_list|,
literal|"db_securityadmin"
argument_list|,
literal|"guest"
argument_list|,
literal|"INFORMATION_SCHEMA"
argument_list|)
decl_stmt|;
specifier|public
name|SQLServerAdapter
parameter_list|(
annotation|@
name|Inject
name|RuntimeProperties
name|runtimeProperties
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_DEFAULT_TYPES_LIST
argument_list|)
name|List
argument_list|<
name|ExtendedType
argument_list|>
name|defaultExtendedTypes
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_USER_TYPES_LIST
argument_list|)
name|List
argument_list|<
name|ExtendedType
argument_list|>
name|userExtendedTypes
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_TYPE_FACTORIES_LIST
argument_list|)
name|List
argument_list|<
name|ExtendedTypeFactory
argument_list|>
name|extendedTypeFactories
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_RESOURCE_LOCATOR
argument_list|)
name|ResourceLocator
name|resourceLocator
parameter_list|,
annotation|@
name|Inject
name|ValueObjectTypeRegistry
name|valueObjectTypeRegistry
parameter_list|)
block|{
name|super
argument_list|(
name|runtimeProperties
argument_list|,
name|defaultExtendedTypes
argument_list|,
name|userExtendedTypes
argument_list|,
name|extendedTypeFactories
argument_list|,
name|resourceLocator
argument_list|,
name|valueObjectTypeRegistry
argument_list|)
expr_stmt|;
name|this
operator|.
name|setSupportsBatchUpdates
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Not supported, see:<a href="http://microsoft/mssql-jdbc#245">mssql-jdbc #245</a>      */
annotation|@
name|Override
specifier|public
name|boolean
name|supportsGeneratedKeysForBatchInserts
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/** 	 * @since 4.2 	 */
annotation|@
name|Override
specifier|public
name|SQLTreeProcessor
name|getSqlTreeProcessor
parameter_list|()
block|{
name|SQLServerTreeProcessor
name|sqlServerTreeProcessor
init|=
operator|new
name|SQLServerTreeProcessor
argument_list|()
decl_stmt|;
name|sqlServerTreeProcessor
operator|.
name|setVersion
argument_list|(
name|this
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sqlServerTreeProcessor
return|;
block|}
comment|/** 	 * Uses SQLServerActionBuilder to create the right action. 	 * 	 * @since 1.2 	 */
annotation|@
name|Override
specifier|public
name|SQLAction
name|getAction
parameter_list|(
name|Query
name|query
parameter_list|,
name|DataNode
name|node
parameter_list|)
block|{
return|return
name|query
operator|.
name|createSQLAction
argument_list|(
operator|new
name|SQLServerActionBuilder
argument_list|(
name|node
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSystemSchemas
parameter_list|()
block|{
return|return
name|SYSTEM_SCHEMAS
return|;
block|}
block|}
end_class

end_unit

