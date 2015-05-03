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
name|velocity
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|MockOperationObserver
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
name|server
operator|.
name|ServerRuntime
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
name|CapsStrategy
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
name|test
operator|.
name|jdbc
operator|.
name|DBHelper
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
name|TableHelper
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
name|testdo
operator|.
name|testmap
operator|.
name|Artist
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|CayenneProjects
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|unit
operator|.
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_comment
comment|/**  * Test for Result directive to check if we could use ResultDirective  * optionally.  */
end_comment

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
specifier|public
class|class
name|ResultDirectiveIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|JdbcAdapter
name|dbAdapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataNode
name|node
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
throws|throws
name|SQLException
block|{
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ARTIST"
argument_list|)
operator|.
name|setColumns
argument_list|(
literal|"ARTIST_ID"
argument_list|,
literal|"ARTIST_NAME"
argument_list|)
operator|.
name|insert
argument_list|(
literal|1l
argument_list|,
literal|"ArtistToTestResult"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithoutResultDirective
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sql
init|=
literal|"SELECT ARTIST_ID, ARTIST_NAME FROM ARTIST"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|selectResult
init|=
name|selectForQuery
argument_list|(
name|sql
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1l
argument_list|,
name|selectResult
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ArtistToTestResult"
argument_list|,
name|selectResult
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithOnlyResultDirective
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sql
init|=
literal|"SELECT #result('ARTIST_ID' 'java.lang.Integer'), #result('ARTIST_NAME' 'java.lang.String')"
operator|+
literal|" FROM ARTIST"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|selectResult
init|=
name|selectForQuery
argument_list|(
name|sql
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1l
argument_list|,
name|selectResult
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ArtistToTestResult"
argument_list|,
name|selectResult
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithMixedDirectiveUse1
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sql
init|=
literal|"SELECT ARTIST_ID, #result('ARTIST_NAME' 'java.lang.String') FROM ARTIST"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|selectResult
init|=
name|selectForQuery
argument_list|(
name|sql
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1l
argument_list|,
name|selectResult
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ArtistToTestResult"
argument_list|,
name|selectResult
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWithMixedDirectiveUse2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sql
init|=
literal|"SELECT #result('ARTIST_ID' 'java.lang.Integer'), ARTIST_NAME FROM ARTIST"
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|selectResult
init|=
name|selectForQuery
argument_list|(
name|sql
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1l
argument_list|,
name|selectResult
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ArtistToTestResult"
argument_list|,
name|selectResult
operator|.
name|get
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|selectForQuery
parameter_list|(
name|String
name|sql
parameter_list|)
block|{
name|SQLTemplate
name|template
init|=
operator|new
name|SQLTemplate
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|sql
argument_list|)
decl_stmt|;
name|template
operator|.
name|setColumnNamesCapitalization
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
expr_stmt|;
name|MockOperationObserver
name|observer
init|=
operator|new
name|MockOperationObserver
argument_list|()
decl_stmt|;
name|runtime
operator|.
name|getDataDomain
argument_list|()
operator|.
name|performQueries
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|template
argument_list|)
argument_list|,
name|observer
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|data
init|=
name|observer
operator|.
name|rowsForQuery
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
name|data
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
return|return
name|row
return|;
block|}
block|}
end_class

end_unit

