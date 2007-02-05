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
name|conf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|project
operator|.
name|CayenneUserDir
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
name|collections
operator|.
name|ExtendedProperties
import|;
end_import

begin_comment
comment|/**  * ConnectionProperties handles a set of DataSourceInfo objects using information stored  * in $HOME/.cayenne/connection.properties. As of now this is purely a utility class. Its  * features are not used in deployment.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ConnectionProperties
block|{
specifier|public
specifier|static
specifier|final
name|String
name|EMBEDDED_DATASOURCE
init|=
literal|"internal_embedded_datasource"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EMBEDDED_DATASOURCE_DBADAPTER
init|=
literal|"org.apache.cayenne.dba.hsqldb.HSQLDBAdapter"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EMBEDDED_DATASOURCE_USERNAME
init|=
literal|"sa"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EMBEDDED_DATASOURCE_PASSWORD
init|=
literal|""
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EMBEDDED_DATASOURCE_URL
init|=
literal|"jdbc:hsqldb:mem:aname"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EMBEDDED_DATASOURCE_JDBC_DRIVER
init|=
literal|"org.hsqldb.jdbcDriver"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTIES_FILE
init|=
literal|"connection.properties"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ADAPTER_KEY
init|=
literal|"adapter"
decl_stmt|;
specifier|static
specifier|final
name|String
name|ADAPTER20_KEY
init|=
literal|"cayenne.adapter"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|USER_NAME_KEY
init|=
literal|"jdbc.username"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PASSWORD_KEY
init|=
literal|"jdbc.password"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|URL_KEY
init|=
literal|"jdbc.url"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DRIVER_KEY
init|=
literal|"jdbc.driver"
decl_stmt|;
specifier|protected
specifier|static
name|ConnectionProperties
name|sharedInstance
decl_stmt|;
specifier|protected
name|Map
name|connectionInfos
init|=
name|Collections
operator|.
name|synchronizedMap
argument_list|(
operator|new
name|HashMap
argument_list|()
argument_list|)
decl_stmt|;
static|static
block|{
name|sharedInstance
operator|=
name|loadDefaultProperties
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns ConnectionProperties singleton.      */
specifier|public
specifier|static
name|ConnectionProperties
name|getInstance
parameter_list|()
block|{
return|return
name|sharedInstance
return|;
block|}
comment|/**      * Loads connection properties from $HOME/.cayenne/connection.properties.      */
specifier|protected
specifier|static
name|ConnectionProperties
name|loadDefaultProperties
parameter_list|()
block|{
name|File
name|f
init|=
name|CayenneUserDir
operator|.
name|getInstance
argument_list|()
operator|.
name|resolveFile
argument_list|(
name|PROPERTIES_FILE
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
operator|new
name|ConnectionProperties
argument_list|(
operator|new
name|ExtendedProperties
argument_list|(
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
comment|// lets touch this file so that users would get a clue of what it is
name|createSamplePropertiesFile
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignoring
block|}
return|return
operator|new
name|ConnectionProperties
argument_list|(
operator|new
name|ExtendedProperties
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
specifier|static
name|void
name|createSamplePropertiesFile
parameter_list|(
name|File
name|f
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedWriter
name|out
init|=
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
name|f
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|out
operator|.
name|write
argument_list|(
literal|"# Cayenne named connections configuration file."
argument_list|)
expr_stmt|;
name|out
operator|.
name|newLine
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"#"
argument_list|)
expr_stmt|;
name|out
operator|.
name|newLine
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"# Sample named connections (named 'example1' and 'example2'): "
argument_list|)
expr_stmt|;
name|out
operator|.
name|newLine
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"#"
argument_list|)
expr_stmt|;
name|out
operator|.
name|newLine
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"# example1."
operator|+
name|ADAPTER_KEY
operator|+
literal|" = org.apache.cayenne.dba.mysql.MySQLAdapter"
argument_list|)
expr_stmt|;
name|out
operator|.
name|newLine
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"# example1."
operator|+
name|USER_NAME_KEY
operator|+
literal|" = some_user"
argument_list|)
expr_stmt|;
name|out
operator|.
name|newLine
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"# example1."
operator|+
name|PASSWORD_KEY
operator|+
literal|" = some_passwd"
argument_list|)
expr_stmt|;
name|out
operator|.
name|newLine
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"# example1."
operator|+
name|URL_KEY
operator|+
literal|" = jdbc:mysql://noise/cayenne"
argument_list|)
expr_stmt|;
name|out
operator|.
name|newLine
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"# example1."
operator|+
name|DRIVER_KEY
operator|+
literal|" = org.gjt.mm.mysql.Driver"
argument_list|)
expr_stmt|;
name|out
operator|.
name|newLine
argument_list|()
expr_stmt|;
comment|// example 2
name|out
operator|.
name|write
argument_list|(
literal|"#"
argument_list|)
expr_stmt|;
name|out
operator|.
name|newLine
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"# example2."
operator|+
name|ADAPTER_KEY
operator|+
literal|" = org.apache.cayenne.dba.mysql.MySQLAdapter"
argument_list|)
expr_stmt|;
name|out
operator|.
name|newLine
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"# example2."
operator|+
name|USER_NAME_KEY
operator|+
literal|" = some_user"
argument_list|)
expr_stmt|;
name|out
operator|.
name|newLine
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"# example2."
operator|+
name|PASSWORD_KEY
operator|+
literal|" = some_passwd"
argument_list|)
expr_stmt|;
name|out
operator|.
name|newLine
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"# example2."
operator|+
name|URL_KEY
operator|+
literal|" = jdbc:mysql://noise/cayenne"
argument_list|)
expr_stmt|;
name|out
operator|.
name|newLine
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
literal|"# example2."
operator|+
name|DRIVER_KEY
operator|+
literal|" = org.gjt.mm.mysql.Driver"
argument_list|)
expr_stmt|;
name|out
operator|.
name|newLine
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Constructor for ConnectionProperties.      */
specifier|public
name|ConnectionProperties
parameter_list|(
name|ExtendedProperties
name|props
parameter_list|)
block|{
name|Iterator
name|names
init|=
name|extractNames
argument_list|(
name|props
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|names
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|names
operator|.
name|next
argument_list|()
decl_stmt|;
name|DataSourceInfo
name|dsi
init|=
name|buildDataSourceInfo
argument_list|(
name|props
operator|.
name|subset
argument_list|(
name|name
argument_list|)
argument_list|)
decl_stmt|;
name|connectionInfos
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|dsi
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns DataSourceInfo object for a symbolic name. If name does not match an      * existing object, returns null.      */
specifier|public
name|DataSourceInfo
name|getConnectionInfo
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|EMBEDDED_DATASOURCE
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// Create embedded data source instead
name|DataSourceInfo
name|connectionInfo
init|=
operator|new
name|DataSourceInfo
argument_list|()
decl_stmt|;
name|connectionInfo
operator|.
name|setAdapterClassName
argument_list|(
name|EMBEDDED_DATASOURCE_DBADAPTER
argument_list|)
expr_stmt|;
name|connectionInfo
operator|.
name|setUserName
argument_list|(
name|EMBEDDED_DATASOURCE_USERNAME
argument_list|)
expr_stmt|;
name|connectionInfo
operator|.
name|setPassword
argument_list|(
name|EMBEDDED_DATASOURCE_PASSWORD
argument_list|)
expr_stmt|;
name|connectionInfo
operator|.
name|setDataSourceUrl
argument_list|(
name|EMBEDDED_DATASOURCE_URL
argument_list|)
expr_stmt|;
name|connectionInfo
operator|.
name|setJdbcDriver
argument_list|(
name|EMBEDDED_DATASOURCE_JDBC_DRIVER
argument_list|)
expr_stmt|;
return|return
name|connectionInfo
return|;
block|}
synchronized|synchronized
init|(
name|connectionInfos
init|)
block|{
return|return
operator|(
name|DataSourceInfo
operator|)
name|connectionInfos
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
comment|/**      * Creates a DataSourceInfo object from a set of properties.      */
specifier|protected
name|DataSourceInfo
name|buildDataSourceInfo
parameter_list|(
name|ExtendedProperties
name|props
parameter_list|)
block|{
name|DataSourceInfo
name|dsi
init|=
operator|new
name|DataSourceInfo
argument_list|()
decl_stmt|;
name|String
name|adapter
init|=
name|props
operator|.
name|getString
argument_list|(
name|ADAPTER_KEY
argument_list|)
decl_stmt|;
comment|// try legacy adapter key
if|if
condition|(
name|adapter
operator|==
literal|null
condition|)
block|{
name|adapter
operator|=
name|props
operator|.
name|getString
argument_list|(
name|ADAPTER20_KEY
argument_list|)
expr_stmt|;
block|}
name|dsi
operator|.
name|setAdapterClassName
argument_list|(
name|adapter
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|setUserName
argument_list|(
name|props
operator|.
name|getString
argument_list|(
name|USER_NAME_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|setPassword
argument_list|(
name|props
operator|.
name|getString
argument_list|(
name|PASSWORD_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|setDataSourceUrl
argument_list|(
name|props
operator|.
name|getString
argument_list|(
name|URL_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|dsi
operator|.
name|setJdbcDriver
argument_list|(
name|props
operator|.
name|getString
argument_list|(
name|DRIVER_KEY
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|dsi
return|;
block|}
comment|/**      * Returns a list of connection names configured in the properties object.      */
specifier|protected
name|List
name|extractNames
parameter_list|(
name|ExtendedProperties
name|props
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|props
operator|.
name|getKeys
argument_list|()
decl_stmt|;
name|List
name|list
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|key
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|int
name|dotInd
init|=
name|key
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|dotInd
operator|<=
literal|0
operator|||
name|dotInd
operator|>=
name|key
operator|.
name|length
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|String
name|name
init|=
name|key
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|dotInd
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|list
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|list
return|;
block|}
block|}
end_class

end_unit

