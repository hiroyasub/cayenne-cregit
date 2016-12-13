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
name|modeler
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|DefaultListCellRenderer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JList
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|ListCellRenderer
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

begin_class
specifier|public
specifier|final
class|class
name|DbAdapterInfo
block|{
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|DEFAULT_ADAPTER_LABELS
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|standardAdapters
init|=
operator|new
name|String
index|[]
block|{
name|JdbcAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|MySQLAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|OracleAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|SybaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|PostgresAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|H2Adapter
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|HSQLDBAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|DB2Adapter
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|SQLServerAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|FrontBaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|FirebirdAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|OpenBaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|DerbyAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|IngresAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|SQLiteAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|IMMUTABLE_LABELS
init|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|DEFAULT_ADAPTER_LABELS
argument_list|)
decl_stmt|;
static|static
block|{
name|DEFAULT_ADAPTER_LABELS
operator|.
name|put
argument_list|(
name|JdbcAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"Generic JDBC Adapter"
argument_list|)
expr_stmt|;
name|DEFAULT_ADAPTER_LABELS
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
literal|"Oracle Adapter"
argument_list|)
expr_stmt|;
name|DEFAULT_ADAPTER_LABELS
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
literal|"MySQL Adapter"
argument_list|)
expr_stmt|;
name|DEFAULT_ADAPTER_LABELS
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
literal|"Sybase Adapter"
argument_list|)
expr_stmt|;
name|DEFAULT_ADAPTER_LABELS
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
literal|"PostgreSQL Adapter"
argument_list|)
expr_stmt|;
name|DEFAULT_ADAPTER_LABELS
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
literal|"HypersonicDB Adapter"
argument_list|)
expr_stmt|;
name|DEFAULT_ADAPTER_LABELS
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
literal|" H2 Database Adapter"
argument_list|)
expr_stmt|;
name|DEFAULT_ADAPTER_LABELS
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
literal|"DB2 Adapter"
argument_list|)
expr_stmt|;
name|DEFAULT_ADAPTER_LABELS
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
literal|"MS SQLServer Adapter"
argument_list|)
expr_stmt|;
name|DEFAULT_ADAPTER_LABELS
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
literal|"FrontBase Adapter"
argument_list|)
expr_stmt|;
name|DEFAULT_ADAPTER_LABELS
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
literal|"Firebird Adapter"
argument_list|)
expr_stmt|;
name|DEFAULT_ADAPTER_LABELS
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
literal|"OpenBase Adapter"
argument_list|)
expr_stmt|;
name|DEFAULT_ADAPTER_LABELS
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
literal|"Derby Adapter"
argument_list|)
expr_stmt|;
name|DEFAULT_ADAPTER_LABELS
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
literal|"Ingres Adapter"
argument_list|)
expr_stmt|;
name|DEFAULT_ADAPTER_LABELS
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
literal|"SQLite Adapter"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|Map
name|getStandardAdapterLabels
parameter_list|()
block|{
return|return
name|IMMUTABLE_LABELS
return|;
block|}
specifier|public
specifier|static
name|ListCellRenderer
name|getListRenderer
parameter_list|()
block|{
return|return
operator|new
name|DbAdapterListRenderer
argument_list|(
name|DEFAULT_ADAPTER_LABELS
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
index|[]
name|getStandardAdapters
parameter_list|()
block|{
return|return
name|standardAdapters
return|;
block|}
specifier|static
specifier|final
class|class
name|DbAdapterListRenderer
extends|extends
name|DefaultListCellRenderer
block|{
name|Map
name|adapterLabels
decl_stmt|;
name|DbAdapterListRenderer
parameter_list|(
name|Map
name|adapterLabels
parameter_list|)
block|{
name|this
operator|.
name|adapterLabels
operator|=
operator|(
name|adapterLabels
operator|!=
literal|null
operator|)
condition|?
name|adapterLabels
else|:
name|Collections
operator|.
name|EMPTY_MAP
expr_stmt|;
block|}
specifier|public
name|Component
name|getListCellRendererComponent
parameter_list|(
name|JList
name|list
parameter_list|,
name|Object
name|object
parameter_list|,
name|int
name|index
parameter_list|,
name|boolean
name|arg3
parameter_list|,
name|boolean
name|arg4
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|Class
condition|)
block|{
name|object
operator|=
operator|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|object
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|Object
name|label
init|=
name|adapterLabels
operator|.
name|get
argument_list|(
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|label
operator|==
literal|null
condition|)
block|{
name|label
operator|=
name|object
expr_stmt|;
block|}
return|return
name|super
operator|.
name|getListCellRendererComponent
argument_list|(
name|list
argument_list|,
name|label
argument_list|,
name|index
argument_list|,
name|arg3
argument_list|,
name|arg4
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

