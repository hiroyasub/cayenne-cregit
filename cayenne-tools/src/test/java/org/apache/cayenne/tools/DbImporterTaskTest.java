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
name|test
operator|.
name|file
operator|.
name|FileUtil
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
name|SQLReader
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
name|resource
operator|.
name|ResourceUtil
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
name|tools
operator|.
name|dbimport
operator|.
name|DbImportConfiguration
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
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|ProjectHelper
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
name|UnknownElement
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
name|util
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|custommonkey
operator|.
name|xmlunit
operator|.
name|DetailedDiff
import|;
end_import

begin_import
import|import
name|org
operator|.
name|custommonkey
operator|.
name|xmlunit
operator|.
name|Diff
import|;
end_import

begin_import
import|import
name|org
operator|.
name|custommonkey
operator|.
name|xmlunit
operator|.
name|Difference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|custommonkey
operator|.
name|xmlunit
operator|.
name|XMLUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|SAXException
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
name|FileReader
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
name|net
operator|.
name|URL
import|;
end_import

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
name|DriverManager
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
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
name|List
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbimport
operator|.
name|ReverseEngineeringUtils
operator|.
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
operator|.
name|isBlank
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_comment
comment|// TODO: we are only testing on Derby. We may need to dynamically switch between DBs
end_comment

begin_comment
comment|// based on "cayenneTestConnection", like we do in cayenne-server, etc.
end_comment

begin_class
specifier|public
class|class
name|DbImporterTaskTest
block|{
static|static
block|{
name|XMLUnit
operator|.
name|setIgnoreWhitespace
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|File
name|distDir
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|File
name|distDir
init|=
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|baseTestDirectory
argument_list|()
argument_list|,
literal|"cdbImport"
argument_list|)
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|distDir
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|distDir
operator|=
name|file
operator|.
name|getParentFile
argument_list|()
expr_stmt|;
comment|// prepare destination directory
if|if
condition|(
operator|!
name|distDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|assertTrue
argument_list|(
name|distDir
operator|.
name|mkdirs
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|file
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadCatalog
parameter_list|()
throws|throws
name|Exception
block|{
name|assertCatalog
argument_list|(
name|getCdbImport
argument_list|(
literal|"build-catalog.xml"
argument_list|)
operator|.
name|getReverseEngineering
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSchema
argument_list|(
name|getCdbImport
argument_list|(
literal|"build-schema.xml"
argument_list|)
operator|.
name|getReverseEngineering
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadCatalogAndSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|assertCatalogAndSchema
argument_list|(
name|getCdbImport
argument_list|(
literal|"build-catalog-and-schema.xml"
argument_list|)
operator|.
name|getReverseEngineering
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadFlat
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFlat
argument_list|(
name|getCdbImport
argument_list|(
literal|"build-flat.xml"
argument_list|)
operator|.
name|getReverseEngineering
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSkipRelationshipsLoading
parameter_list|()
throws|throws
name|Exception
block|{
name|assertSkipRelationshipsLoading
argument_list|(
name|getCdbImport
argument_list|(
literal|"build-skip-relationships-loading.xml"
argument_list|)
operator|.
name|getReverseEngineering
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTableTypes
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTableTypes
argument_list|(
name|getCdbImport
argument_list|(
literal|"build-table-types.xml"
argument_list|)
operator|.
name|getReverseEngineering
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIncludeTable
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"build-include-table.xml"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DbImporterTask
name|getCdbImport
parameter_list|(
name|String
name|buildFile
parameter_list|)
block|{
name|Project
name|project
init|=
operator|new
name|Project
argument_list|()
decl_stmt|;
name|File
name|map
init|=
name|distDir
argument_list|(
name|buildFile
argument_list|)
decl_stmt|;
name|ResourceUtil
operator|.
name|copyResourceToFile
argument_list|(
name|getPackagePath
argument_list|()
operator|+
literal|"/"
operator|+
name|buildFile
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|ProjectHelper
operator|.
name|configureProject
argument_list|(
name|project
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|UnknownElement
name|task
init|=
operator|(
name|UnknownElement
operator|)
name|project
operator|.
name|getTargets
argument_list|()
operator|.
name|get
argument_list|(
literal|"dist"
argument_list|)
operator|.
name|getTasks
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|task
operator|.
name|maybeConfigure
argument_list|()
expr_stmt|;
return|return
operator|(
name|DbImporterTask
operator|)
name|task
operator|.
name|getRealThing
argument_list|()
return|;
block|}
specifier|private
name|String
name|getPackagePath
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
return|;
block|}
specifier|private
name|void
name|test
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|DbImporterTask
name|cdbImport
init|=
name|getCdbImport
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|File
name|mapFile
init|=
name|cdbImport
operator|.
name|getMap
argument_list|()
decl_stmt|;
name|URL
name|mapUrlRes
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|mapFile
operator|.
name|getName
argument_list|()
operator|+
literal|"-result"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|mapUrlRes
operator|!=
literal|null
operator|&&
operator|new
name|File
argument_list|(
name|mapUrlRes
operator|.
name|toURI
argument_list|()
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ResourceUtil
operator|.
name|copyResourceToFile
argument_list|(
name|mapUrlRes
argument_list|,
operator|new
name|File
argument_list|(
name|mapFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|mapFile
operator|.
name|getName
argument_list|()
operator|+
literal|"-result"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|mapFileCopy
init|=
name|distDir
argument_list|(
literal|"copy-"
operator|+
name|mapFile
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|mapFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|getFileUtils
argument_list|()
operator|.
name|copyFile
argument_list|(
name|mapFile
argument_list|,
name|mapFileCopy
argument_list|)
expr_stmt|;
name|cdbImport
operator|.
name|setMap
argument_list|(
name|mapFileCopy
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mapFileCopy
operator|=
name|mapFile
expr_stmt|;
block|}
name|prepareDatabase
argument_list|(
name|name
argument_list|,
name|cdbImport
operator|.
name|toParameters
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|cdbImport
operator|.
name|execute
argument_list|()
expr_stmt|;
name|verifyResult
argument_list|(
name|mapFile
argument_list|,
name|mapFileCopy
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|cleanDb
argument_list|(
name|cdbImport
operator|.
name|toParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|cleanDb
parameter_list|(
name|DbImportConfiguration
name|dbImportConfiguration
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|IllegalAccessException
throws|,
name|InstantiationException
throws|,
name|SQLException
block|{
name|Class
operator|.
name|forName
argument_list|(
name|dbImportConfiguration
operator|.
name|getDriver
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
comment|// Get a connection
name|Connection
name|connection
init|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|dbImportConfiguration
operator|.
name|getUrl
argument_list|()
argument_list|)
decl_stmt|;
name|Statement
name|stmt
init|=
name|connection
operator|.
name|createStatement
argument_list|()
decl_stmt|;
name|ResultSet
name|tables
init|=
name|connection
operator|.
name|getMetaData
argument_list|()
operator|.
name|getTables
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"TABLE"
block|}
argument_list|)
decl_stmt|;
while|while
condition|(
name|tables
operator|.
name|next
argument_list|()
condition|)
block|{
name|String
name|schema
init|=
name|tables
operator|.
name|getString
argument_list|(
literal|"TABLE_SCHEM"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"DROP TABLE "
operator|+
operator|(
name|isBlank
argument_list|(
name|schema
argument_list|)
condition|?
literal|""
else|:
name|schema
operator|+
literal|"."
operator|)
operator|+
name|tables
operator|.
name|getString
argument_list|(
literal|"TABLE_NAME"
argument_list|)
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|execute
argument_list|(
literal|"DROP TABLE "
operator|+
operator|(
name|isBlank
argument_list|(
name|schema
argument_list|)
condition|?
literal|""
else|:
name|schema
operator|+
literal|"."
operator|)
operator|+
name|tables
operator|.
name|getString
argument_list|(
literal|"TABLE_NAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ResultSet
name|schemas
init|=
name|connection
operator|.
name|getMetaData
argument_list|()
operator|.
name|getSchemas
argument_list|()
decl_stmt|;
while|while
condition|(
name|schemas
operator|.
name|next
argument_list|()
condition|)
block|{
name|String
name|schem
init|=
name|schemas
operator|.
name|getString
argument_list|(
literal|"TABLE_SCHEM"
argument_list|)
decl_stmt|;
if|if
condition|(
name|schem
operator|.
name|startsWith
argument_list|(
literal|"SCHEMA"
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"DROP SCHEMA "
operator|+
name|schem
argument_list|)
expr_stmt|;
name|stmt
operator|.
name|execute
argument_list|(
literal|"DROP SCHEMA "
operator|+
name|schem
operator|+
literal|" RESTRICT"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|void
name|verifyResult
parameter_list|(
name|File
name|map
parameter_list|,
name|File
name|mapFileCopy
parameter_list|)
block|{
try|try
block|{
name|FileReader
name|control
init|=
operator|new
name|FileReader
argument_list|(
name|map
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"-result"
argument_list|)
decl_stmt|;
name|FileReader
name|test
init|=
operator|new
name|FileReader
argument_list|(
name|mapFileCopy
argument_list|)
decl_stmt|;
name|DetailedDiff
name|diff
init|=
operator|new
name|DetailedDiff
argument_list|(
operator|new
name|Diff
argument_list|(
name|control
argument_list|,
name|test
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|diff
operator|.
name|similar
argument_list|()
condition|)
block|{
for|for
control|(
name|Difference
name|d
range|:
operator|(
operator|(
name|List
argument_list|<
name|Difference
argument_list|>
operator|)
name|diff
operator|.
name|getAllDifferences
argument_list|()
operator|)
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"-------------------------------------------"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|d
operator|.
name|getTestNodeDetail
argument_list|()
operator|.
name|getNode
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|d
operator|.
name|getControlNodeDetail
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|fail
argument_list|(
name|diff
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|fail
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|prepareDatabase
parameter_list|(
name|String
name|sqlFile
parameter_list|,
name|DbImportConfiguration
name|dbImportConfiguration
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|sqlUrl
init|=
name|ResourceUtil
operator|.
name|getResource
argument_list|(
name|getClass
argument_list|()
argument_list|,
name|sqlFile
operator|+
literal|".sql"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|sqlUrl
argument_list|)
expr_stmt|;
name|Class
operator|.
name|forName
argument_list|(
name|dbImportConfiguration
operator|.
name|getDriver
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
try|try
init|(
name|Connection
name|c
init|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|dbImportConfiguration
operator|.
name|getUrl
argument_list|()
argument_list|)
init|;
init|)
block|{
comment|// TODO: move parsing SQL files to a common utility (DBHelper?) .
comment|// ALso see UnitDbApater.executeDDL - this should use the same
comment|// utility
try|try
init|(
name|Statement
name|stmt
init|=
name|c
operator|.
name|createStatement
argument_list|()
init|;
init|)
block|{
for|for
control|(
name|String
name|sql
range|:
name|SQLReader
operator|.
name|statements
argument_list|(
name|sqlUrl
argument_list|,
literal|";"
argument_list|)
control|)
block|{
comment|// skip comments
if|if
condition|(
name|sql
operator|.
name|startsWith
argument_list|(
literal|"-- "
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|stmt
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

