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
name|Iterator
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
name|cayenne
operator|.
name|tools
operator|.
name|dbimport
operator|.
name|config
operator|.
name|Catalog
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
name|config
operator|.
name|IncludeTable
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
name|config
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|testing
operator|.
name|AbstractMojoTestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
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
name|XMLUnit
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

begin_class
specifier|public
class|class
name|DbImporterMojoTest
extends|extends
name|AbstractMojoTestCase
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
specifier|public
name|void
name|testToParameters_MeaningfulPk
parameter_list|()
throws|throws
name|Exception
block|{
name|DbImportConfiguration
name|parameters1
init|=
name|getCdbImport
argument_list|(
literal|"dbimporter-pom1.xml"
argument_list|)
operator|.
name|toParameters
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|parameters1
operator|.
name|getMeaningfulPkTables
argument_list|()
argument_list|)
expr_stmt|;
name|assertPathEquals
argument_list|(
literal|"target/test/org/apache/cayenne/tools/dbimporter-map1.map.xml"
argument_list|,
name|parameters1
operator|.
name|getDataMapFile
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"x,b*"
argument_list|,
name|getCdbImport
argument_list|(
literal|"dbimporter-pom2.xml"
argument_list|)
operator|.
name|toParameters
argument_list|()
operator|.
name|getMeaningfulPkTables
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"*"
argument_list|,
name|getCdbImport
argument_list|(
literal|"dbimporter-pom3.xml"
argument_list|)
operator|.
name|toParameters
argument_list|()
operator|.
name|getMeaningfulPkTables
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testToParameters_Map
parameter_list|()
throws|throws
name|Exception
block|{
name|DbImportConfiguration
name|parameters1
init|=
name|getCdbImport
argument_list|(
literal|"dbimporter-pom1.xml"
argument_list|)
operator|.
name|toParameters
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|parameters1
operator|.
name|getDataMapFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertPathEquals
argument_list|(
literal|"target/test/org/apache/cayenne/tools/dbimporter-map1.map.xml"
argument_list|,
name|parameters1
operator|.
name|getDataMapFile
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|getCdbImport
argument_list|(
literal|"dbimporter-pom2.xml"
argument_list|)
operator|.
name|toParameters
argument_list|()
operator|.
name|getDataMapFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DbImporterMojo
name|getCdbImport
parameter_list|(
name|String
name|pomFileName
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|(
name|DbImporterMojo
operator|)
name|lookupMojo
argument_list|(
literal|"cdbimport"
argument_list|,
name|getTestFile
argument_list|(
literal|"src/test/resources/org/apache/cayenne/tools/"
operator|+
name|pomFileName
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|void
name|assertPathEquals
parameter_list|(
name|String
name|expectedPath
parameter_list|,
name|String
name|actualPath
parameter_list|)
block|{
name|assertEquals
argument_list|(
operator|new
name|File
argument_list|(
name|expectedPath
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|actualPath
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testImportNewDataMap
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"testImportNewDataMap"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testImportWithoutChanges
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"testImportWithoutChanges"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testImportAddTableAndColumn
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"testImportAddTableAndColumn"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSimpleFiltering
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"testSimpleFiltering"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFilteringWithSchema
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"testFilteringWithSchema"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSchemasAndTableExclude
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"testSchemasAndTableExclude"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testViewsExclude
parameter_list|()
throws|throws
name|Exception
block|{
name|test
argument_list|(
literal|"testViewsExclude"
argument_list|)
expr_stmt|;
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
name|DbImporterMojo
name|cdbImport
init|=
name|getCdbImport
argument_list|(
literal|"dbimport/"
operator|+
name|name
operator|+
literal|"-pom.xml"
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
name|File
name|mapFileCopy
init|=
operator|new
name|File
argument_list|(
name|mapFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
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
name|views
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
literal|"VIEW"
block|}
argument_list|)
decl_stmt|;
while|while
condition|(
name|views
operator|.
name|next
argument_list|()
condition|)
block|{
name|String
name|schema
init|=
name|views
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
literal|"DROP VIEW "
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
name|views
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
literal|"DROP VIEW "
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
name|views
operator|.
name|getString
argument_list|(
literal|"TABLE_NAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|">>>> "
operator|+
name|map
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"-result"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|">>>> "
operator|+
name|mapFileCopy
argument_list|)
expr_stmt|;
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
specifier|public
name|void
name|testFilteringConfig
parameter_list|()
throws|throws
name|Exception
block|{
name|DbImporterMojo
name|cdbImport
init|=
name|getCdbImport
argument_list|(
literal|"config/pom-01.xml"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cdbImport
operator|.
name|getReverseEngineering
argument_list|()
operator|.
name|getCatalogs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Catalog
argument_list|>
name|iterator
init|=
name|cdbImport
operator|.
name|getReverseEngineering
argument_list|()
operator|.
name|getCatalogs
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"catalog-name-01"
argument_list|,
name|iterator
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Catalog
name|catalog
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"catalog-name-02"
argument_list|,
name|catalog
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|Schema
argument_list|>
name|schemaIterator
init|=
name|catalog
operator|.
name|getSchemas
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"schema-name-01"
argument_list|,
name|schemaIterator
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Schema
name|schema
init|=
name|schemaIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"schema-name-02"
argument_list|,
name|schema
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|IncludeTable
argument_list|>
name|includeTableIterator
init|=
name|schema
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"incTable-01"
argument_list|,
name|includeTableIterator
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|IncludeTable
name|includeTable
init|=
name|includeTableIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"incTable-02"
argument_list|,
name|includeTable
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-01"
argument_list|,
name|includeTable
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-01"
argument_list|,
name|includeTable
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-02"
argument_list|,
name|schema
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-02"
argument_list|,
name|schema
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-03"
argument_list|,
name|catalog
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-03"
argument_list|,
name|catalog
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|schemaIterator
operator|=
name|cdbImport
operator|.
name|getReverseEngineering
argument_list|()
operator|.
name|getSchemas
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|schema
operator|=
name|schemaIterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"schema-name-03"
argument_list|,
name|schema
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|schema
operator|=
name|schemaIterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"schema-name-04"
argument_list|,
name|schema
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|includeTableIterator
operator|=
name|schema
operator|.
name|getIncludeTables
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"incTable-04"
argument_list|,
name|includeTableIterator
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excTable-04"
argument_list|,
name|schema
operator|.
name|getExcludeTables
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|includeTable
operator|=
name|includeTableIterator
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"incTable-05"
argument_list|,
name|includeTable
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-04"
argument_list|,
name|includeTable
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-04"
argument_list|,
name|includeTable
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-04"
argument_list|,
name|schema
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-04"
argument_list|,
name|schema
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"includeColumn-03"
argument_list|,
name|catalog
operator|.
name|getIncludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"excludeColumn-03"
argument_list|,
name|catalog
operator|.
name|getExcludeColumns
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"dbimport/"
operator|+
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
comment|// Get a connection
name|Statement
name|stmt
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
operator|.
name|createStatement
argument_list|()
decl_stmt|;
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
end_class

end_unit

