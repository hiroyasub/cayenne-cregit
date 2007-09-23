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
name|pref
package|;
end_package

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
name|FileFilter
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
name|CayenneRuntimeException
import|;
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
name|ConnectionLogger
import|;
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
name|QueryLogger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|conf
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|conf
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
name|conf
operator|.
name|DefaultConfiguration
import|;
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
name|PoolManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|util
operator|.
name|Version
import|;
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
name|SQLTemplate
import|;
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

begin_comment
comment|/**  * An implementation of preference service that stores the data using embedded HSQL DB  * database with Cayenne.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|HSQLEmbeddedPreferenceService
extends|extends
name|CayennePreferenceService
block|{
specifier|protected
name|File
name|dbDirectory
decl_stmt|;
specifier|protected
name|String
name|baseName
decl_stmt|;
specifier|protected
name|String
name|masterBaseName
decl_stmt|;
specifier|protected
name|String
name|cayenneConfigPackage
decl_stmt|;
specifier|protected
name|Configuration
name|configuration
decl_stmt|;
comment|/**      * Creates a new PreferenceService that stores preferences using Cayenne and embedded      * HSQLDB engine.      *       * @param dbLocation path to an HSQL db.      * @param cayenneConfigPackage a Java package that holds cayenne.xml for preferences      *            access (can be null)      * @param defaultDomain root domain name for this service.      */
specifier|public
name|HSQLEmbeddedPreferenceService
parameter_list|(
name|String
name|dbLocation
parameter_list|,
name|String
name|cayenneConfigPackage
parameter_list|,
name|String
name|defaultDomain
parameter_list|)
block|{
name|super
argument_list|(
name|defaultDomain
argument_list|)
expr_stmt|;
if|if
condition|(
name|dbLocation
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|PreferenceException
argument_list|(
literal|"Null DB location."
argument_list|)
throw|;
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|dbLocation
argument_list|)
decl_stmt|;
name|this
operator|.
name|dbDirectory
operator|=
name|file
operator|.
name|getParentFile
argument_list|()
expr_stmt|;
name|this
operator|.
name|masterBaseName
operator|=
name|file
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|cayenneConfigPackage
operator|=
name|cayenneConfigPackage
expr_stmt|;
block|}
comment|/**      * If true, this service updates a secondary HSQL instance that may need      * synchronization with master.      */
specifier|public
name|boolean
name|isSecondaryDB
parameter_list|()
block|{
return|return
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|masterBaseName
argument_list|,
name|baseName
argument_list|)
return|;
block|}
specifier|public
name|File
name|getMasterLock
parameter_list|()
block|{
return|return
operator|new
name|File
argument_list|(
name|dbDirectory
argument_list|,
name|masterBaseName
operator|+
literal|".lck"
argument_list|)
return|;
block|}
comment|/**      * Creates a separate Cayenne stack used to work with preferences database only, so      * that any other use of Cayenne in the app is not affected.      */
specifier|public
name|void
name|startService
parameter_list|()
block|{
comment|// use custom DataSourceFactory to prepare the DB...
name|HSQLDataSourceFactory
name|dataSourceFactory
init|=
operator|new
name|HSQLDataSourceFactory
argument_list|()
decl_stmt|;
name|DefaultConfiguration
name|configuration
init|=
operator|new
name|DefaultConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setDataSourceFactory
argument_list|(
name|dataSourceFactory
argument_list|)
expr_stmt|;
if|if
condition|(
name|cayenneConfigPackage
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|addClassPath
argument_list|(
name|cayenneConfigPackage
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|configuration
operator|.
name|initialize
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error connecting to preference DB."
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|configuration
operator|.
name|didInitialize
argument_list|()
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|dataContext
operator|=
name|configuration
operator|.
name|getDomain
argument_list|()
operator|.
name|createDataContext
argument_list|()
expr_stmt|;
comment|// create DB if it does not exist...
if|if
condition|(
name|dataSourceFactory
operator|.
name|needSchemaUpdate
operator|&&
operator|!
name|upgradeDB
argument_list|()
condition|)
block|{
name|initSchema
argument_list|()
expr_stmt|;
block|}
comment|// bootstrap our own preferences...
name|initPreferences
argument_list|()
expr_stmt|;
comment|// start save timer...
name|startTimer
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|stopService
parameter_list|()
block|{
if|if
condition|(
name|saveTimer
operator|!=
literal|null
condition|)
block|{
name|saveTimer
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|dataContext
operator|!=
literal|null
condition|)
block|{
comment|// flush changes...
name|savePreferences
argument_list|()
expr_stmt|;
comment|// shutdown HSQL
name|dataContext
operator|.
name|performNonSelectingQuery
argument_list|(
operator|new
name|SQLTemplate
argument_list|(
name|Domain
operator|.
name|class
argument_list|,
literal|"SHUTDOWN"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// shutdown Cayenne
if|if
condition|(
name|configuration
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
comment|// attempt to sync primary DB...
if|if
condition|(
name|isSecondaryDB
argument_list|()
condition|)
block|{
name|File
name|lock
init|=
name|getMasterLock
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|lock
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// TODO: according to JavaDoc this is not reliable enough...
comment|// Investigate HSQL API for a better solution.
try|try
block|{
if|if
condition|(
name|lock
operator|.
name|createNewFile
argument_list|()
condition|)
block|{
try|try
block|{
name|moveDB
argument_list|(
name|baseName
argument_list|,
name|masterBaseName
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
throw|throw
operator|new
name|PreferenceException
argument_list|(
literal|"Error shutting down database. Preferences may be in invalid state."
argument_list|)
throw|;
block|}
block|}
block|}
block|}
comment|/**      * Copies database with older version.      */
name|boolean
name|upgradeDB
parameter_list|()
block|{
name|String
name|versionName
init|=
name|dbDirectory
operator|.
name|getName
argument_list|()
decl_stmt|;
name|File
name|prefsDir
init|=
name|dbDirectory
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
name|String
index|[]
name|prefs
init|=
name|prefsDir
operator|.
name|list
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefs
operator|==
literal|null
operator|||
name|prefs
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// find older version
name|Version
name|currentVersion
init|=
operator|new
name|Version
argument_list|(
name|versionName
argument_list|)
decl_stmt|;
name|Version
name|previousVersion
init|=
operator|new
name|Version
argument_list|(
literal|"0"
argument_list|)
decl_stmt|;
name|File
name|lastDir
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|prefs
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|prefsDir
argument_list|,
name|prefs
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|dir
operator|.
name|isDirectory
argument_list|()
operator|&&
operator|new
name|File
argument_list|(
name|dir
argument_list|,
name|baseName
operator|+
literal|".properties"
argument_list|)
operator|.
name|isFile
argument_list|()
condition|)
block|{
comment|// check that there are DB files
name|Version
name|v
decl_stmt|;
try|try
block|{
name|v
operator|=
operator|new
name|Version
argument_list|(
name|prefs
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfex
parameter_list|)
block|{
comment|// ignore... not a version dir...
continue|continue;
block|}
if|if
condition|(
name|v
operator|.
name|compareTo
argument_list|(
name|currentVersion
argument_list|)
operator|<
literal|0
operator|&&
name|v
operator|.
name|compareTo
argument_list|(
name|previousVersion
argument_list|)
operator|>
literal|0
condition|)
block|{
name|previousVersion
operator|=
name|v
expr_stmt|;
name|lastDir
operator|=
name|dir
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|lastDir
operator|!=
literal|null
condition|)
block|{
name|copyDB
argument_list|(
name|lastDir
argument_list|,
name|baseName
argument_list|,
name|baseName
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Copies one database to another. Caller must provide HSQLDB locks on target for this      * to work reliably.      */
name|void
name|moveDB
parameter_list|(
name|String
name|masterBaseName
parameter_list|,
name|String
name|targetBaseName
parameter_list|)
block|{
name|File
index|[]
name|filesToMove
init|=
name|dbDirectory
operator|.
name|listFiles
argument_list|(
operator|new
name|HSQLDBFileFilter
argument_list|(
name|masterBaseName
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|filesToMove
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|filesToMove
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|ext
init|=
name|Util
operator|.
name|extractFileExtension
argument_list|(
name|filesToMove
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|dbDirectory
argument_list|,
name|targetBaseName
operator|+
literal|"."
operator|+
name|ext
argument_list|)
decl_stmt|;
if|if
condition|(
name|filesToMove
index|[
name|i
index|]
operator|.
name|exists
argument_list|()
condition|)
block|{
name|filesToMove
index|[
name|i
index|]
operator|.
name|renameTo
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|target
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Copies one database to another. Caller must provide HSQLDB locks for this to work      * reliably.      */
name|void
name|copyDB
parameter_list|(
name|String
name|masterBaseName
parameter_list|,
name|String
name|targetBaseName
parameter_list|)
block|{
name|copyDB
argument_list|(
name|dbDirectory
argument_list|,
name|masterBaseName
argument_list|,
name|targetBaseName
argument_list|)
expr_stmt|;
block|}
name|void
name|copyDB
parameter_list|(
name|File
name|sourceDirectory
parameter_list|,
name|String
name|masterBaseName
parameter_list|,
name|String
name|targetBaseName
parameter_list|)
block|{
name|File
index|[]
name|filesToCopy
init|=
name|sourceDirectory
operator|.
name|listFiles
argument_list|(
operator|new
name|HSQLDBFileFilter
argument_list|(
name|masterBaseName
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|filesToCopy
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|filesToCopy
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|ext
init|=
name|Util
operator|.
name|extractFileExtension
argument_list|(
name|filesToCopy
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|dbDirectory
argument_list|,
name|targetBaseName
operator|+
literal|"."
operator|+
name|ext
argument_list|)
decl_stmt|;
if|if
condition|(
name|filesToCopy
index|[
name|i
index|]
operator|.
name|exists
argument_list|()
condition|)
block|{
name|Util
operator|.
name|copy
argument_list|(
name|filesToCopy
index|[
name|i
index|]
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|target
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// filers HSQLDB files
specifier|final
class|class
name|HSQLDBFileFilter
implements|implements
name|FileFilter
block|{
name|String
name|baseName
decl_stmt|;
name|HSQLDBFileFilter
parameter_list|(
name|String
name|baseName
parameter_list|)
block|{
name|this
operator|.
name|baseName
operator|=
name|baseName
expr_stmt|;
block|}
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|pathname
parameter_list|)
block|{
if|if
condition|(
operator|!
name|pathname
operator|.
name|isFile
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|fullName
init|=
name|pathname
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|fullName
operator|.
name|endsWith
argument_list|(
literal|".lck"
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|dot
init|=
name|fullName
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
name|String
name|name
init|=
operator|(
name|dot
operator|>
literal|0
operator|)
condition|?
name|fullName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|dot
argument_list|)
else|:
name|fullName
decl_stmt|;
return|return
name|baseName
operator|.
name|equals
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
comment|// addresses various issues with embedded database...
specifier|final
class|class
name|HSQLDataSourceFactory
implements|implements
name|DataSourceFactory
block|{
name|boolean
name|needSchemaUpdate
decl_stmt|;
name|String
name|url
decl_stmt|;
name|void
name|prepareDB
parameter_list|()
throws|throws
name|IOException
block|{
comment|// try master DB
if|if
condition|(
name|checkMainDB
argument_list|(
name|masterBaseName
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// try last active DB
if|if
condition|(
name|baseName
operator|!=
literal|null
operator|&&
name|checkMainDB
argument_list|(
name|baseName
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// file locked... need to switch to a secondary DB
comment|// arbitrary big but finite number of attempts...
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|200
condition|;
name|i
operator|++
control|)
block|{
name|String
name|name
init|=
name|masterBaseName
operator|+
name|i
decl_stmt|;
name|File
name|lock
init|=
operator|new
name|File
argument_list|(
name|dbDirectory
argument_list|,
name|name
operator|+
literal|".lck"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|lock
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// TODO: according to JavaDoc this is not reliable enough...
comment|// Investigate HSQL API for a better solution.
if|if
condition|(
operator|!
name|lock
operator|.
name|createNewFile
argument_list|()
condition|)
block|{
continue|continue;
block|}
try|try
block|{
name|copyDB
argument_list|(
name|masterBaseName
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
name|needSchemaUpdate
operator|=
literal|false
expr_stmt|;
name|url
operator|=
literal|"jdbc:hsqldb:file:"
operator|+
name|Util
operator|.
name|substBackslashes
argument_list|(
operator|new
name|File
argument_list|(
name|dbDirectory
argument_list|,
name|name
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|baseName
operator|=
name|name
expr_stmt|;
return|return;
block|}
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Can't create preferences DB"
argument_list|)
throw|;
block|}
name|boolean
name|checkMainDB
parameter_list|(
name|String
name|sessionBaseName
parameter_list|)
block|{
name|File
name|dbFile
init|=
operator|new
name|File
argument_list|(
name|dbDirectory
argument_list|,
name|sessionBaseName
operator|+
literal|".properties"
argument_list|)
decl_stmt|;
comment|// no db file exists
if|if
condition|(
operator|!
name|dbFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|needSchemaUpdate
operator|=
literal|true
expr_stmt|;
name|url
operator|=
literal|"jdbc:hsqldb:file:"
operator|+
name|Util
operator|.
name|substBackslashes
argument_list|(
operator|new
name|File
argument_list|(
name|dbDirectory
argument_list|,
name|sessionBaseName
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|baseName
operator|=
name|sessionBaseName
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// no lock exists... continue...
name|File
name|lockFile
init|=
operator|new
name|File
argument_list|(
name|dbDirectory
argument_list|,
name|sessionBaseName
operator|+
literal|".lck"
argument_list|)
decl_stmt|;
comment|// on Windows try deleting the lock... OS locking should prevent
comment|// this operation if another process is running...
if|if
condition|(
name|lockFile
operator|.
name|exists
argument_list|()
operator|&&
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"windows"
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|lockFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|lockFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|needSchemaUpdate
operator|=
literal|false
expr_stmt|;
name|url
operator|=
literal|"jdbc:hsqldb:file:"
operator|+
name|Util
operator|.
name|substBackslashes
argument_list|(
operator|new
name|File
argument_list|(
name|dbDirectory
argument_list|,
name|sessionBaseName
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|baseName
operator|=
name|sessionBaseName
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
specifier|public
name|DataSource
name|getDataSource
parameter_list|(
name|String
name|location
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|prepareDB
argument_list|()
expr_stmt|;
name|PoolManager
name|pm
init|=
operator|new
name|PoolManager
argument_list|(
name|org
operator|.
name|hsqldb
operator|.
name|jdbcDriver
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|url
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|,
literal|"sa"
argument_list|,
literal|null
argument_list|,
operator|new
name|ConnectionLogger
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|pm
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|QueryLogger
operator|.
name|logConnectFailure
argument_list|(
name|th
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|PreferenceException
argument_list|(
literal|"Error connecting to DB"
argument_list|,
name|th
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|initializeWithParentConfiguration
parameter_list|(
name|Configuration
name|conf
parameter_list|)
block|{
block|}
block|}
block|}
end_class

end_unit

