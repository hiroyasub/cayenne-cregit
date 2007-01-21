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
name|util
operator|.
name|HashMap
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

begin_comment
comment|/**  * Contains mappings for guessing defaults for various adapter and JDBC settings.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|AdapterMapping
block|{
specifier|protected
name|Map
name|adapterToJDBCDriverMap
decl_stmt|;
specifier|protected
name|Map
name|adapterToJDBCURLMap
decl_stmt|;
specifier|protected
name|Map
name|jdbcDriverToAdapterMap
decl_stmt|;
specifier|protected
name|Map
name|eofPluginToAdapterMap
decl_stmt|;
specifier|public
name|AdapterMapping
parameter_list|()
block|{
name|this
operator|.
name|adapterToJDBCDriverMap
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|adapterToJDBCURLMap
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|jdbcDriverToAdapterMap
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|eofPluginToAdapterMap
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|initDefaults
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|initDefaults
parameter_list|()
block|{
comment|// TODO: make configuration external...
comment|// drivers
name|jdbcDriverToAdapterMap
operator|.
name|put
argument_list|(
literal|"oracle.jdbc.driver.OracleDriver"
argument_list|,
name|OracleAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jdbcDriverToAdapterMap
operator|.
name|put
argument_list|(
literal|"com.sybase.jdbc2.jdbc.SybDriver"
argument_list|,
name|SybaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jdbcDriverToAdapterMap
operator|.
name|put
argument_list|(
literal|"com.mysql.jdbc.Driver"
argument_list|,
name|MySQLAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jdbcDriverToAdapterMap
operator|.
name|put
argument_list|(
literal|"com.ibm.db2.jcc.DB2Driver"
argument_list|,
name|DB2Adapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jdbcDriverToAdapterMap
operator|.
name|put
argument_list|(
literal|"org.hsqldb.jdbcDriver"
argument_list|,
name|HSQLDBAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jdbcDriverToAdapterMap
operator|.
name|put
argument_list|(
literal|"org.postgresql.Driver"
argument_list|,
name|PostgresAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jdbcDriverToAdapterMap
operator|.
name|put
argument_list|(
literal|"com.openbase.jdbc.ObDriver"
argument_list|,
name|OpenBaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jdbcDriverToAdapterMap
operator|.
name|put
argument_list|(
literal|"com.microsoft.jdbc.sqlserver.SQLServerDriver"
argument_list|,
name|SQLServerAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jdbcDriverToAdapterMap
operator|.
name|put
argument_list|(
literal|"org.apache.derby.jdbc.EmbeddedDriver"
argument_list|,
name|DerbyAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jdbcDriverToAdapterMap
operator|.
name|put
argument_list|(
literal|"jdbc.FrontBase.FBJDriver"
argument_list|,
name|FrontBaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jdbcDriverToAdapterMap
operator|.
name|put
argument_list|(
literal|"com.ingres.jdbc.IngresDriver"
argument_list|,
name|IngresAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// urls
name|adapterToJDBCURLMap
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
literal|"jdbc:oracle:thin:@localhost:1521:database"
argument_list|)
expr_stmt|;
name|adapterToJDBCURLMap
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
literal|"jdbc:sybase:Tds:localhost:port/database"
argument_list|)
expr_stmt|;
name|adapterToJDBCURLMap
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
literal|"jdbc:mysql://localhost/database"
argument_list|)
expr_stmt|;
name|adapterToJDBCURLMap
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
literal|"jdbc:db2://localhost:port/database"
argument_list|)
expr_stmt|;
name|adapterToJDBCURLMap
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
literal|"jdbc:hsqldb:hsql://localhost/database"
argument_list|)
expr_stmt|;
name|adapterToJDBCURLMap
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
literal|"jdbc:postgresql://localhost:5432/database"
argument_list|)
expr_stmt|;
name|adapterToJDBCURLMap
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
literal|"jdbc:openbase://localhost/database"
argument_list|)
expr_stmt|;
name|adapterToJDBCURLMap
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
literal|"jdbc:microsoft:sqlserver://host;databaseName=database;SelectMethod=cursor"
argument_list|)
expr_stmt|;
comment|// TODO: embedded Derby Mode... change to client-server once we figure it out
name|adapterToJDBCURLMap
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
literal|"jdbc:derby:testdb;create=true"
argument_list|)
expr_stmt|;
name|adapterToJDBCURLMap
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
literal|"jdbc:FrontBase://localhost/database"
argument_list|)
expr_stmt|;
name|adapterToJDBCURLMap
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
literal|"jdbc:ingres://127.0.0.1:II7/database"
argument_list|)
expr_stmt|;
comment|// adapters
name|adapterToJDBCDriverMap
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
literal|"oracle.jdbc.driver.OracleDriver"
argument_list|)
expr_stmt|;
name|adapterToJDBCDriverMap
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
literal|"com.sybase.jdbc2.jdbc.SybDriver"
argument_list|)
expr_stmt|;
name|adapterToJDBCDriverMap
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
literal|"com.mysql.jdbc.Driver"
argument_list|)
expr_stmt|;
name|adapterToJDBCDriverMap
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
literal|"com.ibm.db2.jcc.DB2Driver"
argument_list|)
expr_stmt|;
name|adapterToJDBCDriverMap
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
literal|"org.hsqldb.jdbcDriver"
argument_list|)
expr_stmt|;
name|adapterToJDBCDriverMap
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
literal|"org.postgresql.Driver"
argument_list|)
expr_stmt|;
name|adapterToJDBCDriverMap
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
literal|"com.openbase.jdbc.ObDriver"
argument_list|)
expr_stmt|;
name|adapterToJDBCDriverMap
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
literal|"com.microsoft.jdbc.sqlserver.SQLServerDriver"
argument_list|)
expr_stmt|;
name|adapterToJDBCDriverMap
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
literal|"org.apache.derby.jdbc.EmbeddedDriver"
argument_list|)
expr_stmt|;
name|adapterToJDBCDriverMap
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
literal|"jdbc.FrontBase.FBJDriver"
argument_list|)
expr_stmt|;
name|adapterToJDBCDriverMap
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
literal|"com.ingres.jdbc.IngresDriver"
argument_list|)
expr_stmt|;
comment|// EOF plugins...
name|eofPluginToAdapterMap
operator|.
name|put
argument_list|(
literal|"com.webobjects.jdbcadaptor.SybasePlugIn"
argument_list|,
name|SybaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|eofPluginToAdapterMap
operator|.
name|put
argument_list|(
literal|"com.webobjects.jdbcadaptor.MerantPlugIn"
argument_list|,
name|SQLServerAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|eofPluginToAdapterMap
operator|.
name|put
argument_list|(
literal|"com.webobjects.jdbcadaptor.MicrosoftPlugIn"
argument_list|,
name|SQLServerAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|eofPluginToAdapterMap
operator|.
name|put
argument_list|(
literal|"com.webobjects.jdbcadaptor.MySQLPlugIn"
argument_list|,
name|MySQLAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|eofPluginToAdapterMap
operator|.
name|put
argument_list|(
literal|"com.webobjects.jdbcadaptor.OraclePlugIn"
argument_list|,
name|OracleAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|eofPluginToAdapterMap
operator|.
name|put
argument_list|(
literal|"com.webobjects.jdbcadaptor.FrontbasePlugIn"
argument_list|,
name|FrontBaseAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|eofPluginToAdapterMap
operator|.
name|put
argument_list|(
literal|"PostgresqlPlugIn"
argument_list|,
name|PostgresAdapter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|jdbcURLForAdapter
parameter_list|(
name|String
name|adapterClass
parameter_list|)
block|{
return|return
operator|(
name|String
operator|)
name|adapterToJDBCURLMap
operator|.
name|get
argument_list|(
name|adapterClass
argument_list|)
return|;
block|}
specifier|public
name|String
name|jdbcDriverForAdapter
parameter_list|(
name|String
name|adapterClass
parameter_list|)
block|{
return|return
operator|(
name|String
operator|)
name|adapterToJDBCDriverMap
operator|.
name|get
argument_list|(
name|adapterClass
argument_list|)
return|;
block|}
specifier|public
name|String
name|adapterForJDBCDriver
parameter_list|(
name|String
name|driverClass
parameter_list|)
block|{
return|return
operator|(
name|String
operator|)
name|jdbcDriverToAdapterMap
operator|.
name|get
argument_list|(
name|driverClass
argument_list|)
return|;
block|}
specifier|public
name|String
name|adapterForEOFPlugin
parameter_list|(
name|String
name|eofPlugin
parameter_list|)
block|{
return|return
operator|(
name|String
operator|)
name|eofPluginToAdapterMap
operator|.
name|get
argument_list|(
name|eofPlugin
argument_list|)
return|;
block|}
specifier|public
name|String
name|adapterForEOFPluginOrDriver
parameter_list|(
name|String
name|eofPlugin
parameter_list|,
name|String
name|jdbcDriver
parameter_list|)
block|{
name|String
name|adapter
init|=
name|adapterForEOFPlugin
argument_list|(
name|eofPlugin
argument_list|)
decl_stmt|;
return|return
operator|(
name|adapter
operator|!=
literal|null
operator|)
condition|?
name|adapter
else|:
name|adapterForJDBCDriver
argument_list|(
name|jdbcDriver
argument_list|)
return|;
block|}
block|}
end_class

end_unit

