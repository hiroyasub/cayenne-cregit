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
name|DbGenerator
import|;
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
name|DriverDataSource
import|;
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
name|map
operator|.
name|DataMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|BuildException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Driver
import|;
end_import

begin_comment
comment|/**  * An Ant Task that is a frontend to Cayenne DbGenerator allowing schema generation from  * DataMap using Ant.  *   * @since 1.2  */
end_comment

begin_comment
comment|// TODO: support classpath attribute for loading the driver
end_comment

begin_class
specifier|public
class|class
name|DbGeneratorTask
extends|extends
name|CayenneTask
block|{
comment|// DbGenerator options... setup defaults similar to DbGenerator itself:
comment|// all DROP set to false, all CREATE - to true
specifier|protected
name|boolean
name|dropTables
decl_stmt|;
specifier|protected
name|boolean
name|dropPK
decl_stmt|;
specifier|protected
name|boolean
name|createTables
init|=
literal|true
decl_stmt|;
specifier|protected
name|boolean
name|createPK
init|=
literal|true
decl_stmt|;
specifier|protected
name|boolean
name|createFK
init|=
literal|true
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|()
block|{
comment|// prepare defaults
if|if
condition|(
name|adapter
operator|==
literal|null
condition|)
block|{
name|adapter
operator|=
operator|new
name|JdbcAdapter
argument_list|()
expr_stmt|;
block|}
name|log
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"connection settings - [driver: %s, url: %s, username: %s]"
argument_list|,
name|driver
argument_list|,
name|url
argument_list|,
name|userName
argument_list|)
argument_list|,
name|Project
operator|.
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
name|log
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"generator options - [dropTables: %s, dropPK: %s, createTables: %s, createPK: %s, createFK: %s]"
argument_list|,
name|dropTables
argument_list|,
name|dropPK
argument_list|,
name|createTables
argument_list|,
name|createPK
argument_list|,
name|createFK
argument_list|)
argument_list|,
name|Project
operator|.
name|MSG_VERBOSE
argument_list|)
expr_stmt|;
name|validateAttributes
argument_list|()
expr_stmt|;
name|ClassLoader
name|loader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|loader
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|DbGeneratorTask
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
comment|// Load the data map and run the db generator.
name|DataMap
name|dataMap
init|=
name|loadDataMap
argument_list|()
decl_stmt|;
name|DbGenerator
name|generator
init|=
operator|new
name|DbGenerator
argument_list|(
name|adapter
argument_list|,
name|dataMap
argument_list|)
decl_stmt|;
name|generator
operator|.
name|setShouldCreateFKConstraints
argument_list|(
name|createFK
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldCreatePKSupport
argument_list|(
name|createPK
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldCreateTables
argument_list|(
name|createTables
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldDropPKSupport
argument_list|(
name|dropPK
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setShouldDropTables
argument_list|(
name|dropTables
argument_list|)
expr_stmt|;
comment|// load driver taking custom CLASSPATH into account...
name|DriverDataSource
name|dataSource
init|=
operator|new
name|DriverDataSource
argument_list|(
operator|(
name|Driver
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|driver
argument_list|)
operator|.
name|newInstance
argument_list|()
argument_list|,
name|url
argument_list|,
name|userName
argument_list|,
name|password
argument_list|)
decl_stmt|;
name|generator
operator|.
name|runGenerator
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Throwable
name|th
init|=
name|Util
operator|.
name|unwindException
argument_list|(
name|ex
argument_list|)
decl_stmt|;
name|String
name|message
init|=
literal|"Error generating database"
decl_stmt|;
if|if
condition|(
name|th
operator|.
name|getLocalizedMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|message
operator|+=
literal|": "
operator|+
name|th
operator|.
name|getLocalizedMessage
argument_list|()
expr_stmt|;
block|}
name|log
argument_list|(
name|message
argument_list|,
name|Project
operator|.
name|MSG_ERR
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BuildException
argument_list|(
name|message
argument_list|,
name|th
argument_list|)
throw|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Validates atttributes that are not related to internal DefaultClassGenerator.      * Throws BuildException if attributes are invalid.      */
specifier|protected
name|void
name|validateAttributes
parameter_list|()
throws|throws
name|BuildException
block|{
name|StringBuilder
name|error
init|=
operator|new
name|StringBuilder
argument_list|(
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
name|error
operator|.
name|append
argument_list|(
literal|"The 'map' attribute must be set.\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|driver
operator|==
literal|null
condition|)
block|{
name|error
operator|.
name|append
argument_list|(
literal|"The 'driver' attribute must be set.\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|error
operator|.
name|append
argument_list|(
literal|"The 'adapter' attribute must be set.\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|error
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
throw|throw
operator|new
name|BuildException
argument_list|(
name|error
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setCreateFK
parameter_list|(
name|boolean
name|createFK
parameter_list|)
block|{
name|this
operator|.
name|createFK
operator|=
name|createFK
expr_stmt|;
block|}
specifier|public
name|void
name|setCreatePK
parameter_list|(
name|boolean
name|createPK
parameter_list|)
block|{
name|this
operator|.
name|createPK
operator|=
name|createPK
expr_stmt|;
block|}
specifier|public
name|void
name|setCreateTables
parameter_list|(
name|boolean
name|createTables
parameter_list|)
block|{
name|this
operator|.
name|createTables
operator|=
name|createTables
expr_stmt|;
block|}
specifier|public
name|void
name|setDropPK
parameter_list|(
name|boolean
name|dropPK
parameter_list|)
block|{
name|this
operator|.
name|dropPK
operator|=
name|dropPK
expr_stmt|;
block|}
specifier|public
name|void
name|setDropTables
parameter_list|(
name|boolean
name|dropTables
parameter_list|)
block|{
name|this
operator|.
name|dropTables
operator|=
name|dropTables
expr_stmt|;
block|}
block|}
end_class

end_unit

