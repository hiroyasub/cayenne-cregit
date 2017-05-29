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
name|Objects
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
name|gradle
operator|.
name|testkit
operator|.
name|runner
operator|.
name|BuildResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|testkit
operator|.
name|runner
operator|.
name|GradleRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|testkit
operator|.
name|runner
operator|.
name|TaskOutcome
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DbImportIT
extends|extends
name|BaseTaskIT
block|{
annotation|@
name|Test
specifier|public
name|void
name|notConfiguredTaskFailure
parameter_list|()
throws|throws
name|IOException
block|{
name|GradleRunner
name|runner
init|=
name|createRunner
argument_list|(
literal|"dbimport_failure"
argument_list|,
literal|"cdbimport"
argument_list|,
literal|"--info"
argument_list|)
decl_stmt|;
name|BuildResult
name|result
init|=
name|runner
operator|.
name|buildAndFail
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|task
argument_list|(
literal|":cdbimport"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TaskOutcome
operator|.
name|FAILED
argument_list|,
name|result
operator|.
name|task
argument_list|(
literal|":cdbimport"
argument_list|)
operator|.
name|getOutcome
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getOutput
argument_list|()
operator|.
name|contains
argument_list|(
literal|"No datamap configured in task or in cayenne.defaultDataMap"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|emptyDbTaskSuccess
parameter_list|()
throws|throws
name|IOException
block|{
name|GradleRunner
name|runner
init|=
name|createRunner
argument_list|(
literal|"dbimport_empty_db"
argument_list|,
literal|"cdbimport"
argument_list|,
literal|"--info"
argument_list|)
decl_stmt|;
name|BuildResult
name|result
init|=
name|runner
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|task
argument_list|(
literal|":cdbimport"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TaskOutcome
operator|.
name|SUCCESS
argument_list|,
name|result
operator|.
name|task
argument_list|(
literal|":cdbimport"
argument_list|)
operator|.
name|getOutcome
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|dataMap
init|=
operator|new
name|File
argument_list|(
name|projectDir
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/datamap.map.xml"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|dataMap
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getOutput
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Detected changes: No changes to import."
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|simpleDbTaskSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|dbUrl
init|=
name|prepareDerbyDatabase
argument_list|(
literal|"test_map_db"
argument_list|)
decl_stmt|;
name|GradleRunner
name|runner
init|=
name|createRunner
argument_list|(
literal|"dbimport_simple_db"
argument_list|,
literal|"cdbimport"
argument_list|,
literal|"--info"
argument_list|,
literal|"-PdbUrl="
operator|+
name|dbUrl
argument_list|)
decl_stmt|;
name|BuildResult
name|result
init|=
name|runner
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|task
argument_list|(
literal|":cdbimport"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TaskOutcome
operator|.
name|SUCCESS
argument_list|,
name|result
operator|.
name|task
argument_list|(
literal|":cdbimport"
argument_list|)
operator|.
name|getOutcome
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|dataMap
init|=
operator|new
name|File
argument_list|(
name|projectDir
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/datamap.map.xml"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|dataMap
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// Check few lines from reverse engineering output
name|assertTrue
argument_list|(
name|result
operator|.
name|getOutput
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Table: APP.PAINTING"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getOutput
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Db Relationship : toOne  (EXHIBIT.GALLERY_ID, GALLERY.GALLERY_ID)"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getOutput
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Db Relationship : toMany (GALLERY.GALLERY_ID, PAINTING.GALLERY_ID)"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getOutput
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Create Table         ARTIST"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|result
operator|.
name|getOutput
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Create Table         PAINTING1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getOutput
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Skip relation: '.APP.ARTIST.ARTIST_ID<- .APP.PAINTING1.ARTIST_ID # 1'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getOutput
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Migration Complete Successfully."
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|prepareDerbyDatabase
parameter_list|(
name|String
name|sqlFile
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|sqlUrl
init|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
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
argument_list|)
decl_stmt|;
name|String
name|dbUrl
init|=
literal|"jdbc:derby:"
operator|+
name|projectDir
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/build/"
operator|+
name|sqlFile
decl_stmt|;
try|try
init|(
name|Connection
name|connection
init|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|dbUrl
operator|+
literal|";create=true"
argument_list|)
init|)
block|{
try|try
init|(
name|Statement
name|stmt
init|=
name|connection
operator|.
name|createStatement
argument_list|()
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
comment|// shutdown Derby DB, so it can be used by test build later
try|try
init|(
name|Connection
name|connection
init|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|dbUrl
operator|+
literal|";shutdown=true"
argument_list|)
init|)
block|{
block|}
catch|catch
parameter_list|(
name|SQLException
name|ignored
parameter_list|)
block|{
comment|// should be thrown according to the Derby docs...
block|}
return|return
name|dbUrl
operator|+
literal|";create=true"
return|;
block|}
block|}
end_class

end_unit

