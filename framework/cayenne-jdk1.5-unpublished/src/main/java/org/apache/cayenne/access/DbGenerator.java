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
name|access
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
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

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
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
name|Collection
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
name|ListIterator
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
name|javax
operator|.
name|sql
operator|.
name|DataSource
import|;
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
name|PkGenerator
import|;
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
name|TypesMapping
import|;
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
name|map
operator|.
name|DbAttribute
import|;
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
name|DbEntity
import|;
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
name|DbJoin
import|;
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
name|DbRelationship
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|validation
operator|.
name|SimpleValidationFailure
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|validation
operator|.
name|ValidationResult
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
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Utility class that generates database schema based on Cayenne mapping. It is a logical  * counterpart of DbLoader class.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DbGenerator
block|{
specifier|private
name|Log
name|logObj
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DbGenerator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|DbAdapter
name|adapter
decl_stmt|;
specifier|protected
name|DataMap
name|map
decl_stmt|;
comment|// optional DataDomain needed for correct FK generation in cross-db situations
specifier|protected
name|DataDomain
name|domain
decl_stmt|;
comment|// stores generated SQL statements
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|dropTables
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|createTables
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|createConstraints
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|createPK
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|dropPK
decl_stmt|;
comment|/**      * Contains all DbEntities ordered considering their interdependencies.      * DerivedDbEntities are filtered out of this list.      */
specifier|protected
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntitiesInInsertOrder
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntitiesRequiringAutoPK
decl_stmt|;
specifier|protected
name|boolean
name|shouldDropTables
decl_stmt|;
specifier|protected
name|boolean
name|shouldCreateTables
decl_stmt|;
specifier|protected
name|boolean
name|shouldDropPKSupport
decl_stmt|;
specifier|protected
name|boolean
name|shouldCreatePKSupport
decl_stmt|;
specifier|protected
name|boolean
name|shouldCreateFKConstraints
decl_stmt|;
specifier|protected
name|ValidationResult
name|failures
decl_stmt|;
comment|/**      * Creates and initializes new DbGenerator.      */
specifier|public
name|DbGenerator
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|,
name|DataMap
name|map
parameter_list|)
block|{
name|this
argument_list|(
name|adapter
argument_list|,
name|map
argument_list|,
name|Collections
operator|.
expr|<
name|DbEntity
operator|>
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates and initializes new DbGenerator instance.      *       * @param adapter DbAdapter corresponding to the database      * @param map DataMap whose entities will be used in schema generation      * @param excludedEntities entities that should be ignored during schema generation      */
specifier|public
name|DbGenerator
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|excludedEntities
parameter_list|)
block|{
name|this
argument_list|(
name|adapter
argument_list|,
name|map
argument_list|,
name|excludedEntities
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates and initializes new DbGenerator instance.      *       * @param adapter DbAdapter corresponding to the database      * @param map DataMap whose entities will be used in schema generation      * @param excludedEntities entities that should be ignored during schema generation      * @param domain optional DataDomain used to detect cross-database relationships.      * @since 1.2      */
specifier|public
name|DbGenerator
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|excludedEntities
parameter_list|,
name|DataDomain
name|domain
parameter_list|)
block|{
comment|// sanity check
if|if
condition|(
name|adapter
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Adapter must not be null."
argument_list|)
throw|;
block|}
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"DataMap must not be null."
argument_list|)
throw|;
block|}
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
name|prepareDbEntities
argument_list|(
name|excludedEntities
argument_list|)
expr_stmt|;
name|resetToDefaults
argument_list|()
expr_stmt|;
name|buildStatements
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|resetToDefaults
parameter_list|()
block|{
name|this
operator|.
name|shouldDropTables
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|shouldDropPKSupport
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|shouldCreatePKSupport
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|shouldCreateTables
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|shouldCreateFKConstraints
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * Creates and stores internally a set of statements for database schema creation,      * ignoring configured schema creation preferences. Statements are NOT executed in      * this method.      */
specifier|protected
name|void
name|buildStatements
parameter_list|()
block|{
name|dropTables
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|createTables
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|createConstraints
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|DbAdapter
name|adapter
init|=
name|getAdapter
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|DbEntity
name|dbe
range|:
name|this
operator|.
name|dbEntitiesInInsertOrder
control|)
block|{
name|String
name|name
init|=
name|dbe
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// build "DROP TABLE"
name|dropTables
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|adapter
operator|.
name|dropTableStatements
argument_list|(
name|dbe
argument_list|)
argument_list|)
expr_stmt|;
comment|// build "CREATE TABLE"
name|createTables
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|adapter
operator|.
name|createTable
argument_list|(
name|dbe
argument_list|)
argument_list|)
expr_stmt|;
comment|// build constraints
name|createConstraints
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|createConstraintsQueries
argument_list|(
name|dbe
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|PkGenerator
name|pkGenerator
init|=
name|adapter
operator|.
name|getPkGenerator
argument_list|()
decl_stmt|;
name|dropPK
operator|=
name|pkGenerator
operator|.
name|dropAutoPkStatements
argument_list|(
name|dbEntitiesRequiringAutoPK
argument_list|)
expr_stmt|;
name|createPK
operator|=
name|pkGenerator
operator|.
name|createAutoPkStatements
argument_list|(
name|dbEntitiesRequiringAutoPK
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if there is nothing to be done by this generator. If      *<code>respectConfiguredSettings</code> is<code>true</code>, checks are done      * applying currently configured settings, otherwise check is done, assuming that all      * possible generated objects.      */
specifier|public
name|boolean
name|isEmpty
parameter_list|(
name|boolean
name|respectConfiguredSettings
parameter_list|)
block|{
if|if
condition|(
name|dbEntitiesInInsertOrder
operator|.
name|isEmpty
argument_list|()
operator|&&
name|dbEntitiesRequiringAutoPK
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
name|respectConfiguredSettings
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
operator|!
operator|(
name|shouldDropTables
operator|||
name|shouldCreateTables
operator|||
name|shouldCreateFKConstraints
operator|||
name|shouldCreatePKSupport
operator|||
name|shouldDropPKSupport
operator|)
return|;
block|}
comment|/** Returns DbAdapter associated with this DbGenerator. */
specifier|public
name|DbAdapter
name|getAdapter
parameter_list|()
block|{
return|return
name|adapter
return|;
block|}
comment|/**      * Returns a list of all schema statements that should be executed with the current      * configuration.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|configuredStatements
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|shouldDropTables
condition|)
block|{
name|ListIterator
argument_list|<
name|DbEntity
argument_list|>
name|it
init|=
name|dbEntitiesInInsertOrder
operator|.
name|listIterator
argument_list|(
name|dbEntitiesInInsertOrder
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasPrevious
argument_list|()
condition|)
block|{
name|DbEntity
name|ent
init|=
name|it
operator|.
name|previous
argument_list|()
decl_stmt|;
name|list
operator|.
name|addAll
argument_list|(
name|dropTables
operator|.
name|get
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|shouldCreateTables
condition|)
block|{
for|for
control|(
specifier|final
name|DbEntity
name|ent
range|:
name|dbEntitiesInInsertOrder
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|createTables
operator|.
name|get
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|shouldCreateFKConstraints
condition|)
block|{
for|for
control|(
specifier|final
name|DbEntity
name|ent
range|:
name|dbEntitiesInInsertOrder
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|fks
init|=
name|createConstraints
operator|.
name|get
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|list
operator|.
name|addAll
argument_list|(
name|fks
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|shouldDropPKSupport
condition|)
block|{
name|list
operator|.
name|addAll
argument_list|(
name|dropPK
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|shouldCreatePKSupport
condition|)
block|{
name|list
operator|.
name|addAll
argument_list|(
name|createPK
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
comment|/**      * Creates a temporary DataSource out of DataSourceInfo and invokes      *<code>public void runGenerator(DataSource ds)</code>.      */
specifier|public
name|void
name|runGenerator
parameter_list|(
name|DataSourceInfo
name|dsi
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|failures
operator|=
literal|null
expr_stmt|;
comment|// do a pre-check. Maybe there is no need to run anything
comment|// and therefore no need to create a connection
if|if
condition|(
name|isEmpty
argument_list|(
literal|true
argument_list|)
condition|)
block|{
return|return;
block|}
name|Driver
name|driver
init|=
operator|(
name|Driver
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|dsi
operator|.
name|getJdbcDriver
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|DataSource
name|dataSource
init|=
operator|new
name|DriverDataSource
argument_list|(
name|driver
argument_list|,
name|dsi
operator|.
name|getDataSourceUrl
argument_list|()
argument_list|,
name|dsi
operator|.
name|getUserName
argument_list|()
argument_list|,
name|dsi
operator|.
name|getPassword
argument_list|()
argument_list|)
decl_stmt|;
name|runGenerator
argument_list|(
name|dataSource
argument_list|)
expr_stmt|;
block|}
comment|/**      * Executes a set of commands to drop/create database objects. This is the main worker      * method of DbGenerator. Command set is built based on pre-configured generator      * settings.      */
specifier|public
name|void
name|runGenerator
parameter_list|(
name|DataSource
name|ds
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|failures
operator|=
literal|null
expr_stmt|;
name|Connection
name|connection
init|=
name|ds
operator|.
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
comment|// drop tables
if|if
condition|(
name|shouldDropTables
condition|)
block|{
name|ListIterator
argument_list|<
name|DbEntity
argument_list|>
name|it
init|=
name|dbEntitiesInInsertOrder
operator|.
name|listIterator
argument_list|(
name|dbEntitiesInInsertOrder
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasPrevious
argument_list|()
condition|)
block|{
name|DbEntity
name|ent
init|=
name|it
operator|.
name|previous
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|statement
range|:
name|dropTables
operator|.
name|get
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
control|)
block|{
name|safeExecute
argument_list|(
name|connection
argument_list|,
name|statement
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// create tables
name|List
argument_list|<
name|String
argument_list|>
name|createdTables
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|shouldCreateTables
condition|)
block|{
for|for
control|(
specifier|final
name|DbEntity
name|ent
range|:
name|dbEntitiesInInsertOrder
control|)
block|{
comment|// only create missing tables
name|safeExecute
argument_list|(
name|connection
argument_list|,
name|createTables
operator|.
name|get
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|createdTables
operator|.
name|add
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// create FK
if|if
condition|(
name|shouldCreateTables
operator|&&
name|shouldCreateFKConstraints
condition|)
block|{
for|for
control|(
name|DbEntity
name|ent
range|:
name|dbEntitiesInInsertOrder
control|)
block|{
if|if
condition|(
name|createdTables
operator|.
name|contains
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|fks
init|=
name|createConstraints
operator|.
name|get
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|fk
range|:
name|fks
control|)
block|{
name|safeExecute
argument_list|(
name|connection
argument_list|,
name|fk
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// drop PK
if|if
condition|(
name|shouldDropPKSupport
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|dropAutoPKSQL
init|=
name|getAdapter
argument_list|()
operator|.
name|getPkGenerator
argument_list|()
operator|.
name|dropAutoPkStatements
argument_list|(
name|dbEntitiesRequiringAutoPK
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|sql
range|:
name|dropAutoPKSQL
control|)
block|{
name|safeExecute
argument_list|(
name|connection
argument_list|,
name|sql
argument_list|)
expr_stmt|;
block|}
block|}
comment|// create pk
if|if
condition|(
name|shouldCreatePKSupport
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|createAutoPKSQL
init|=
name|getAdapter
argument_list|()
operator|.
name|getPkGenerator
argument_list|()
operator|.
name|createAutoPkStatements
argument_list|(
name|dbEntitiesRequiringAutoPK
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|sql
range|:
name|createAutoPKSQL
control|)
block|{
name|safeExecute
argument_list|(
name|connection
argument_list|,
name|sql
argument_list|)
expr_stmt|;
block|}
block|}
operator|new
name|DbGeneratorPostprocessor
argument_list|()
operator|.
name|execute
argument_list|(
name|connection
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Builds and executes a SQL statement, catching and storing SQL exceptions resulting      * from invalid SQL. Only non-recoverable exceptions are rethrown.      *       * @since 1.1      */
specifier|protected
name|boolean
name|safeExecute
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
name|Statement
name|statement
init|=
name|connection
operator|.
name|createStatement
argument_list|()
decl_stmt|;
try|try
block|{
name|QueryLogger
operator|.
name|logQuery
argument_list|(
name|sql
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|statement
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|ex
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|failures
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|failures
operator|=
operator|new
name|ValidationResult
argument_list|()
expr_stmt|;
block|}
name|failures
operator|.
name|addFailure
argument_list|(
operator|new
name|SimpleValidationFailure
argument_list|(
name|sql
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|QueryLogger
operator|.
name|logQueryError
argument_list|(
name|ex
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
finally|finally
block|{
name|statement
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns an array of queries to create foreign key constraints for a particular      * DbEntity.      *       * @deprecated since 3.0 as this method is used to generate both FK and UNIQUE      *             constraints, use 'createConstraintsQueries' instead.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|createFkConstraintsQueries
parameter_list|(
name|DbEntity
name|table
parameter_list|)
block|{
return|return
name|createConstraintsQueries
argument_list|(
name|table
argument_list|)
return|;
block|}
comment|/**      * Creates FK and UNIQUE constraint statements for a given table.      *       * @since 3.0      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|createConstraintsQueries
parameter_list|(
name|DbEntity
name|table
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|DbRelationship
name|rel
range|:
name|table
operator|.
name|getRelationships
argument_list|()
control|)
block|{
if|if
condition|(
name|rel
operator|.
name|isToMany
argument_list|()
condition|)
block|{
continue|continue;
block|}
comment|// skip FK to a different DB
if|if
condition|(
name|domain
operator|!=
literal|null
condition|)
block|{
name|DataMap
name|srcMap
init|=
name|rel
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
name|DataMap
name|targetMap
init|=
name|rel
operator|.
name|getTargetEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|srcMap
operator|!=
literal|null
operator|&&
name|targetMap
operator|!=
literal|null
operator|&&
name|srcMap
operator|!=
name|targetMap
condition|)
block|{
if|if
condition|(
name|domain
operator|.
name|lookupDataNode
argument_list|(
name|srcMap
argument_list|)
operator|!=
name|domain
operator|.
name|lookupDataNode
argument_list|(
name|targetMap
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
block|}
comment|// create an FK CONSTRAINT only if the relationship is to PK
comment|// and if this is not a dependent PK
comment|// create UNIQUE CONSTRAINT on FK if reverse relationship is to-one
if|if
condition|(
name|rel
operator|.
name|isToPK
argument_list|()
operator|&&
operator|!
name|rel
operator|.
name|isToDependentPK
argument_list|()
condition|)
block|{
if|if
condition|(
name|getAdapter
argument_list|()
operator|.
name|supportsUniqueConstraints
argument_list|()
condition|)
block|{
name|DbRelationship
name|reverse
init|=
name|rel
operator|.
name|getReverseRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|reverse
operator|!=
literal|null
operator|&&
operator|!
name|reverse
operator|.
name|isToMany
argument_list|()
operator|&&
operator|!
name|reverse
operator|.
name|isToPK
argument_list|()
condition|)
block|{
name|String
name|unique
init|=
name|getAdapter
argument_list|()
operator|.
name|createUniqueConstraint
argument_list|(
operator|(
name|DbEntity
operator|)
name|rel
operator|.
name|getSourceEntity
argument_list|()
argument_list|,
name|rel
operator|.
name|getSourceAttributes
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|unique
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|unique
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|String
name|fk
init|=
name|getAdapter
argument_list|()
operator|.
name|createFkConstraint
argument_list|(
name|rel
argument_list|)
decl_stmt|;
if|if
condition|(
name|fk
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|fk
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|list
return|;
block|}
comment|/**      * Returns an object representing a collection of failures that occurred on the last      * "runGenerator" invocation, or null if there were no failures. Failures usually      * indicate problems with generated DDL (such as "create...", "drop...", etc.) and      * usually happen due to the DataMap being out of sync with the database.      *       * @since 1.1      */
specifier|public
name|ValidationResult
name|getFailures
parameter_list|()
block|{
return|return
name|failures
return|;
block|}
comment|/**      * Returns whether DbGenerator is configured to create primary key support for DataMap      * entities.      */
specifier|public
name|boolean
name|shouldCreatePKSupport
parameter_list|()
block|{
return|return
name|shouldCreatePKSupport
return|;
block|}
comment|/**      * Returns whether DbGenerator is configured to create tables for DataMap entities.      */
specifier|public
name|boolean
name|shouldCreateTables
parameter_list|()
block|{
return|return
name|shouldCreateTables
return|;
block|}
specifier|public
name|boolean
name|shouldDropPKSupport
parameter_list|()
block|{
return|return
name|shouldDropPKSupport
return|;
block|}
specifier|public
name|boolean
name|shouldDropTables
parameter_list|()
block|{
return|return
name|shouldDropTables
return|;
block|}
specifier|public
name|boolean
name|shouldCreateFKConstraints
parameter_list|()
block|{
return|return
name|shouldCreateFKConstraints
return|;
block|}
specifier|public
name|void
name|setShouldCreatePKSupport
parameter_list|(
name|boolean
name|shouldCreatePKSupport
parameter_list|)
block|{
name|this
operator|.
name|shouldCreatePKSupport
operator|=
name|shouldCreatePKSupport
expr_stmt|;
block|}
specifier|public
name|void
name|setShouldCreateTables
parameter_list|(
name|boolean
name|shouldCreateTables
parameter_list|)
block|{
name|this
operator|.
name|shouldCreateTables
operator|=
name|shouldCreateTables
expr_stmt|;
block|}
specifier|public
name|void
name|setShouldDropPKSupport
parameter_list|(
name|boolean
name|shouldDropPKSupport
parameter_list|)
block|{
name|this
operator|.
name|shouldDropPKSupport
operator|=
name|shouldDropPKSupport
expr_stmt|;
block|}
specifier|public
name|void
name|setShouldDropTables
parameter_list|(
name|boolean
name|shouldDropTables
parameter_list|)
block|{
name|this
operator|.
name|shouldDropTables
operator|=
name|shouldDropTables
expr_stmt|;
block|}
specifier|public
name|void
name|setShouldCreateFKConstraints
parameter_list|(
name|boolean
name|shouldCreateFKConstraints
parameter_list|)
block|{
name|this
operator|.
name|shouldCreateFKConstraints
operator|=
name|shouldCreateFKConstraints
expr_stmt|;
block|}
comment|/**      * Returns a DataDomain used by the DbGenerator to detect cross-database      * relationships. By default DataDomain is null.      *       * @since 1.2      */
specifier|public
name|DataDomain
name|getDomain
parameter_list|()
block|{
return|return
name|domain
return|;
block|}
comment|/**      * Helper method that orders DbEntities to satisfy referential constraints and returns      * an ordered list. It also filters out DerivedDbEntities.      */
specifier|private
name|void
name|prepareDbEntities
parameter_list|(
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|excludedEntities
parameter_list|)
block|{
if|if
condition|(
name|excludedEntities
operator|==
literal|null
condition|)
block|{
name|excludedEntities
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|DbEntity
argument_list|>
name|tables
init|=
operator|new
name|ArrayList
argument_list|<
name|DbEntity
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DbEntity
argument_list|>
name|tablesWithAutoPk
init|=
operator|new
name|ArrayList
argument_list|<
name|DbEntity
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbEntity
name|nextEntity
range|:
name|map
operator|.
name|getDbEntities
argument_list|()
control|)
block|{
comment|// do sanity checks...
comment|// tables with no columns are not included
if|if
condition|(
name|nextEntity
operator|.
name|getAttributes
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|logObj
operator|.
name|info
argument_list|(
literal|"Skipping entity with no attributes: "
operator|+
name|nextEntity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
comment|// check if this entity is explicitly excluded
if|if
condition|(
name|excludedEntities
operator|.
name|contains
argument_list|(
name|nextEntity
argument_list|)
condition|)
block|{
continue|continue;
block|}
comment|// tables with invalid DbAttributes are not included
name|boolean
name|invalidAttributes
init|=
literal|false
decl_stmt|;
for|for
control|(
specifier|final
name|DbAttribute
name|attr
range|:
name|nextEntity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|attr
operator|.
name|getType
argument_list|()
operator|==
name|TypesMapping
operator|.
name|NOT_DEFINED
condition|)
block|{
name|logObj
operator|.
name|info
argument_list|(
literal|"Skipping entity, attribute type is undefined: "
operator|+
name|nextEntity
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|attr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|invalidAttributes
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|invalidAttributes
condition|)
block|{
continue|continue;
block|}
name|tables
operator|.
name|add
argument_list|(
name|nextEntity
argument_list|)
expr_stmt|;
comment|// check if an automatic PK generation can be potentially supported
comment|// in this entity. For now simply check that the key is not propagated
name|Iterator
argument_list|<
name|DbRelationship
argument_list|>
name|relationships
init|=
name|nextEntity
operator|.
name|getRelationships
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
comment|// create a copy of the original PK list,
comment|// since the list will be modified locally
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|pkAttributes
init|=
operator|new
name|ArrayList
argument_list|<
name|DbAttribute
argument_list|>
argument_list|(
name|nextEntity
operator|.
name|getPrimaryKeys
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|pkAttributes
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|&&
name|relationships
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbRelationship
name|nextRelationship
init|=
name|relationships
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|nextRelationship
operator|.
name|isToMasterPK
argument_list|()
condition|)
block|{
continue|continue;
block|}
comment|// supposedly all source attributes of the relationship
comment|// to master entity must be a part of primary key,
comment|// so
for|for
control|(
name|DbJoin
name|join
range|:
name|nextRelationship
operator|.
name|getJoins
argument_list|()
control|)
block|{
name|pkAttributes
operator|.
name|remove
argument_list|(
name|join
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// primary key is needed only if at least one of the primary key attributes
comment|// is not propagated via relationship
if|if
condition|(
name|pkAttributes
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|tablesWithAutoPk
operator|.
name|add
argument_list|(
name|nextEntity
argument_list|)
expr_stmt|;
block|}
block|}
comment|// sort table list
if|if
condition|(
name|tables
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|DataNode
name|node
init|=
operator|new
name|DataNode
argument_list|(
literal|"temp"
argument_list|)
decl_stmt|;
name|node
operator|.
name|addDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|node
operator|.
name|getEntitySorter
argument_list|()
operator|.
name|sortDbEntities
argument_list|(
name|tables
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|dbEntitiesInInsertOrder
operator|=
name|tables
expr_stmt|;
name|this
operator|.
name|dbEntitiesRequiringAutoPK
operator|=
name|tablesWithAutoPk
expr_stmt|;
block|}
block|}
end_class

end_unit

