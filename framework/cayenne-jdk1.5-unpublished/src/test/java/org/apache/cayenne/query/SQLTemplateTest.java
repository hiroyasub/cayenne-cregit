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
name|query
package|;
end_package

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
name|DataRow
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
name|DataContext
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
name|EntityResolver
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
name|remote
operator|.
name|hessian
operator|.
name|service
operator|.
name|HessianUtil
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
name|CayenneCase
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
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|SQLTemplateTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deleteTestData
argument_list|()
expr_stmt|;
name|context
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testBindCHARInWHERE
parameter_list|()
block|{
comment|// add 2 Artists
name|DataMap
name|testDataMap
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"testmap"
argument_list|)
decl_stmt|;
name|SQLTemplate
name|q1
init|=
operator|new
name|SQLTemplate
argument_list|(
name|testDataMap
argument_list|,
literal|"INSERT INTO ARTIST VALUES (1, 'Surikov', null)"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|SQLTemplate
name|q2
init|=
operator|new
name|SQLTemplate
argument_list|(
name|testDataMap
argument_list|,
literal|"INSERT INTO ARTIST VALUES (2, 'Shishkin', null)"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|QueryChain
name|chain
init|=
operator|new
name|QueryChain
argument_list|()
decl_stmt|;
name|chain
operator|.
name|addQuery
argument_list|(
name|q1
argument_list|)
expr_stmt|;
name|chain
operator|.
name|addQuery
argument_list|(
name|q2
argument_list|)
expr_stmt|;
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
name|chain
argument_list|)
expr_stmt|;
comment|// now select one Artist by Name, It's matter that ARTIST_NAME is CHAR not VARCHAR
name|SQLTemplate
name|s1
init|=
operator|new
name|SQLTemplate
argument_list|(
name|testDataMap
argument_list|,
literal|"SELECT * FROM ARTIST WHERE ARTIST_NAME = #bind($ARTIST_NAME)"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|//whitespace after name is for reason
name|s1
operator|.
name|setParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"ARTIST_NAME"
argument_list|,
literal|"Surikov "
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|s1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSQLTemplateForDataMap
parameter_list|()
block|{
name|DataMap
name|testDataMap
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"testmap"
argument_list|)
decl_stmt|;
name|SQLTemplate
name|q1
init|=
operator|new
name|SQLTemplate
argument_list|(
name|testDataMap
argument_list|,
literal|"SELECT * FROM ARTIST"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSQLTemplateForDataMapWithInsert
parameter_list|()
block|{
name|DataMap
name|testDataMap
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"testmap"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
literal|"INSERT INTO ARTIST VALUES (15, 'Surikov', null)"
decl_stmt|;
name|SQLTemplate
name|q1
init|=
operator|new
name|SQLTemplate
argument_list|(
name|testDataMap
argument_list|,
name|sql
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
name|q1
argument_list|)
expr_stmt|;
name|SQLTemplate
name|q2
init|=
operator|new
name|SQLTemplate
argument_list|(
name|testDataMap
argument_list|,
literal|"SELECT * FROM ARTIST"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSQLTemplateForDataMapWithInsertException
parameter_list|()
block|{
name|DataMap
name|testDataMap
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDataMap
argument_list|(
literal|"testmap"
argument_list|)
decl_stmt|;
name|String
name|sql
init|=
literal|"INSERT INTO ARTIST VALUES (15, 'Surikov', null)"
decl_stmt|;
name|SQLTemplate
name|q1
init|=
operator|new
name|SQLTemplate
argument_list|(
name|testDataMap
argument_list|,
name|sql
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|context
operator|.
name|performNonSelectingQuery
argument_list|(
name|q1
argument_list|)
expr_stmt|;
name|SQLTemplate
name|q2
init|=
operator|new
name|SQLTemplate
argument_list|(
name|testDataMap
argument_list|,
literal|"SELECT * FROM ARTIST"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|boolean
name|gotRuntimeException
init|=
literal|false
decl_stmt|;
try|try
block|{
name|context
operator|.
name|performQuery
argument_list|(
name|q2
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|e
parameter_list|)
block|{
name|gotRuntimeException
operator|=
literal|true
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"If fetchingDataRows is false and ObjectEntity not set, shoulb be thrown exception"
argument_list|,
name|gotRuntimeException
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testColumnNameCapitalization
parameter_list|()
block|{
name|SQLTemplate
name|q1
init|=
operator|new
name|SQLTemplate
argument_list|(
literal|"E1"
argument_list|,
literal|"SELECT"
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|CapsStrategy
operator|.
name|DEFAULT
argument_list|,
name|q1
operator|.
name|getColumnNamesCapitalization
argument_list|()
argument_list|)
expr_stmt|;
name|q1
operator|.
name|setColumnNamesCapitalization
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|,
name|q1
operator|.
name|getColumnNamesCapitalization
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testQueryWithParameters
parameter_list|()
block|{
name|SQLTemplate
name|q1
init|=
operator|new
name|SQLTemplate
argument_list|(
literal|"E1"
argument_list|,
literal|"SELECT"
argument_list|)
decl_stmt|;
name|q1
operator|.
name|setName
argument_list|(
literal|"QName"
argument_list|)
expr_stmt|;
name|Query
name|q2
init|=
name|q1
operator|.
name|queryWithParameters
argument_list|(
name|Collections
operator|.
name|EMPTY_MAP
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|q2
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|q1
argument_list|,
name|q2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|q2
operator|instanceof
name|SQLTemplate
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Query
name|q3
init|=
name|q1
operator|.
name|queryWithParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|q3
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|q1
argument_list|,
name|q3
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|q3
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|q1
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|q3
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Query
name|q4
init|=
name|q1
operator|.
name|queryWithParameters
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|q4
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|q3
argument_list|,
name|q4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|q3
operator|.
name|getName
argument_list|()
argument_list|,
name|q4
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSerializability
parameter_list|()
throws|throws
name|Exception
block|{
name|SQLTemplate
name|o
init|=
operator|new
name|SQLTemplate
argument_list|(
literal|"Test"
argument_list|,
literal|"DO SQL"
argument_list|)
decl_stmt|;
name|Object
name|clone
init|=
name|Util
operator|.
name|cloneViaSerialization
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|clone
operator|instanceof
name|SQLTemplate
argument_list|)
expr_stmt|;
name|SQLTemplate
name|c1
init|=
operator|(
name|SQLTemplate
operator|)
name|clone
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|c1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o
operator|.
name|getRoot
argument_list|()
argument_list|,
name|c1
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o
operator|.
name|getDefaultTemplate
argument_list|()
argument_list|,
name|c1
operator|.
name|getDefaultTemplate
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSerializabilityWithHessian
parameter_list|()
throws|throws
name|Exception
block|{
name|SQLTemplate
name|o
init|=
operator|new
name|SQLTemplate
argument_list|(
literal|"Test"
argument_list|,
literal|"DO SQL"
argument_list|)
decl_stmt|;
name|Object
name|clone
init|=
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
name|o
argument_list|,
operator|new
name|EntityResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|clone
operator|instanceof
name|SQLTemplate
argument_list|)
expr_stmt|;
name|SQLTemplate
name|c1
init|=
operator|(
name|SQLTemplate
operator|)
name|clone
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|c1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o
operator|.
name|getRoot
argument_list|()
argument_list|,
name|c1
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|o
operator|.
name|getDefaultTemplate
argument_list|()
argument_list|,
name|c1
operator|.
name|getDefaultTemplate
argument_list|()
argument_list|)
expr_stmt|;
comment|// set immutable parameters ... query must recast them to mutable version
name|Map
index|[]
name|parameters
init|=
operator|new
name|Map
index|[]
block|{
name|Collections
operator|.
name|EMPTY_MAP
block|}
decl_stmt|;
name|o
operator|.
name|setParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
name|HessianUtil
operator|.
name|cloneViaClientServerSerialization
argument_list|(
name|o
argument_list|,
operator|new
name|EntityResolver
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetDefaultTemplate
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|()
decl_stmt|;
name|query
operator|.
name|setDefaultTemplate
argument_list|(
literal|"AAA # BBB"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AAA # BBB"
argument_list|,
name|query
operator|.
name|getDefaultTemplate
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetTemplate
parameter_list|()
block|{
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|()
decl_stmt|;
comment|// no template for key, no default template... must be null
name|assertNull
argument_list|(
name|query
operator|.
name|getTemplate
argument_list|(
literal|"key1"
argument_list|)
argument_list|)
expr_stmt|;
comment|// no template for key, must return default
name|query
operator|.
name|setDefaultTemplate
argument_list|(
literal|"AAA # BBB"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AAA # BBB"
argument_list|,
name|query
operator|.
name|getTemplate
argument_list|(
literal|"key1"
argument_list|)
argument_list|)
expr_stmt|;
comment|// must find template
name|query
operator|.
name|setTemplate
argument_list|(
literal|"key1"
argument_list|,
literal|"XYZ"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"XYZ"
argument_list|,
name|query
operator|.
name|getTemplate
argument_list|(
literal|"key1"
argument_list|)
argument_list|)
expr_stmt|;
comment|// add another template.. still must find
name|query
operator|.
name|setTemplate
argument_list|(
literal|"key2"
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"XYZ"
argument_list|,
name|query
operator|.
name|getTemplate
argument_list|(
literal|"key1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|query
operator|.
name|getTemplate
argument_list|(
literal|"key2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSingleParameterSet
parameter_list|()
throws|throws
name|Exception
block|{
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Map
name|params
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameters
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|params
argument_list|,
name|query
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|query
operator|.
name|parametersIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|params
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameters
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|it
operator|=
name|query
operator|.
name|parametersIterator
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBatchParameterSet
parameter_list|()
throws|throws
name|Exception
block|{
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|Map
name|params1
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|params1
operator|.
name|put
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
name|Map
name|params2
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|params2
operator|.
name|put
argument_list|(
literal|"1"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameters
argument_list|(
operator|new
name|Map
index|[]
block|{
name|params1
block|,
name|params2
block|,
literal|null
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|params1
argument_list|,
name|query
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|query
operator|.
name|parametersIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|params1
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|params2
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|Map
operator|)
name|it
operator|.
name|next
argument_list|()
operator|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameters
argument_list|(
operator|(
name|Map
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|query
operator|.
name|getParameters
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|it
operator|=
name|query
operator|.
name|parametersIterator
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

