begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|ProcedureResult
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
name|access
operator|.
name|jdbc
operator|.
name|ColumnDescriptor
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
name|log
operator|.
name|JdbcEventLogger
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
name|testdo
operator|.
name|testmap
operator|.
name|Painting
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
name|tx
operator|.
name|BaseTransaction
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
name|tx
operator|.
name|ExternalTransaction
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
name|UnitDbAdapter
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
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
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
name|ProcedureCallIT
extends|extends
name|ServerCase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|UPDATE_STORED_PROCEDURE
init|=
literal|"cayenne_tst_upd_proc"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UPDATE_STORED_PROCEDURE_NOPARAM
init|=
literal|"cayenne_tst_upd_proc2"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SELECT_STORED_PROCEDURE
init|=
literal|"cayenne_tst_select_proc"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OUT_STORED_PROCEDURE
init|=
literal|"cayenne_tst_out_proc"
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|accessStackAdapter
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|JdbcEventLogger
name|jdbcEventLogger
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testUpdate
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsStoredProcedures
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// create an artist with painting in the database
name|createArtist
argument_list|(
literal|1000.0
argument_list|)
expr_stmt|;
name|runProcedureSelect
argument_list|(
name|ProcedureCall
operator|.
name|query
argument_list|(
name|UPDATE_STORED_PROCEDURE
argument_list|)
operator|.
name|param
argument_list|(
literal|"paintingPrice"
argument_list|,
literal|3000
argument_list|)
argument_list|)
expr_stmt|;
comment|// check that price have doubled
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|prefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|disjoint
argument_list|()
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a
init|=
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Painting
name|p
init|=
name|a
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2000
argument_list|,
name|p
operator|.
name|getEstimatedPrice
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUpdateNoParam
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsStoredProcedures
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// create an artist with painting in the database
name|createArtist
argument_list|(
literal|1000.0
argument_list|)
expr_stmt|;
name|runProcedureSelect
argument_list|(
name|ProcedureCall
operator|.
name|query
argument_list|(
name|UPDATE_STORED_PROCEDURE_NOPARAM
argument_list|)
argument_list|)
expr_stmt|;
comment|// check that price have doubled
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|prefetch
argument_list|(
name|Artist
operator|.
name|PAINTING_ARRAY
operator|.
name|disjoint
argument_list|()
argument_list|)
operator|.
name|select
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a
init|=
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Painting
name|p
init|=
name|a
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2000
argument_list|,
name|p
operator|.
name|getEstimatedPrice
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelect
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsStoredProcedures
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// create an artist with painting in the database
name|createArtist
argument_list|(
literal|1000.0
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|artists
init|=
name|runProcedureSelect
argument_list|(
name|ProcedureCall
operator|.
name|query
argument_list|(
name|SELECT_STORED_PROCEDURE
argument_list|)
operator|.
name|param
argument_list|(
literal|"aName"
argument_list|,
literal|"An Artist"
argument_list|)
operator|.
name|param
argument_list|(
literal|"paintingPrice"
argument_list|,
literal|3000
argument_list|)
argument_list|)
operator|.
name|firstList
argument_list|()
decl_stmt|;
comment|// check the results
name|assertNotNull
argument_list|(
literal|"Null result from StoredProcedure."
argument_list|,
name|artists
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataRow
name|artistRow
init|=
operator|(
name|DataRow
operator|)
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Artist
name|a
init|=
name|context
operator|.
name|objectFromDataRow
argument_list|(
name|Artist
operator|.
name|class
argument_list|,
name|uppercaseConverter
argument_list|(
name|artistRow
argument_list|)
argument_list|)
decl_stmt|;
name|Painting
name|p
init|=
name|a
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// invalidate painting, it may have been updated in the proc
name|context
operator|.
name|invalidateObjects
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2000
argument_list|,
name|p
operator|.
name|getEstimatedPrice
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFetchLimit
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsStoredProcedures
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// create an artist with painting in the database
name|createArtist
argument_list|(
literal|1000.0
argument_list|)
expr_stmt|;
name|createArtist
argument_list|(
literal|2000.0
argument_list|)
expr_stmt|;
name|createArtist
argument_list|(
literal|3000.0
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|artists
init|=
name|runProcedureSelect
argument_list|(
name|ProcedureCall
operator|.
name|query
argument_list|(
name|SELECT_STORED_PROCEDURE
argument_list|)
operator|.
name|param
argument_list|(
literal|"aName"
argument_list|,
literal|"An Artist"
argument_list|)
operator|.
name|param
argument_list|(
literal|"paintingPrice"
argument_list|,
literal|3000
argument_list|)
operator|.
name|limit
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|firstList
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFetchOffset
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsStoredProcedures
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// create an artist with painting in the database
name|createArtist
argument_list|(
literal|1000.0
argument_list|)
expr_stmt|;
name|createArtist
argument_list|(
literal|2000.0
argument_list|)
expr_stmt|;
name|createArtist
argument_list|(
literal|3000.0
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|artists
init|=
name|runProcedureSelect
argument_list|(
name|ProcedureCall
operator|.
name|query
argument_list|(
name|SELECT_STORED_PROCEDURE
argument_list|)
operator|.
name|param
argument_list|(
literal|"aName"
argument_list|,
literal|"An Artist"
argument_list|)
operator|.
name|param
argument_list|(
literal|"paintingPrice"
argument_list|,
literal|3000
argument_list|)
operator|.
name|offset
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|firstList
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testColumnNameCapitalization
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsStoredProcedures
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// create an artist with painting in the database
name|createArtist
argument_list|(
literal|1000.0
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|artists
init|=
name|runProcedureSelect
argument_list|(
name|ProcedureCall
operator|.
name|query
argument_list|(
name|SELECT_STORED_PROCEDURE
argument_list|)
operator|.
name|param
argument_list|(
literal|"aName"
argument_list|,
literal|"An Artist"
argument_list|)
operator|.
name|capsStrategy
argument_list|(
name|CapsStrategy
operator|.
name|LOWER
argument_list|)
argument_list|)
operator|.
name|firstList
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|DataRow
argument_list|>
name|artists1
init|=
name|runProcedureSelect
argument_list|(
name|ProcedureCall
operator|.
name|query
argument_list|(
name|SELECT_STORED_PROCEDURE
argument_list|)
operator|.
name|param
argument_list|(
literal|"aName"
argument_list|,
literal|"An Artist"
argument_list|)
operator|.
name|capsStrategy
argument_list|(
name|CapsStrategy
operator|.
name|UPPER
argument_list|)
argument_list|)
operator|.
name|firstList
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|containsKey
argument_list|(
literal|"date_of_birth"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|containsKey
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|artists1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|containsKey
argument_list|(
literal|"date_of_birth"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|artists1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|containsKey
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOutParams
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsStoredProcedures
argument_list|()
condition|)
block|{
return|return;
block|}
name|ProcedureResult
name|result
init|=
name|runProcedureSelect
argument_list|(
name|ProcedureCall
operator|.
name|query
argument_list|(
name|OUT_STORED_PROCEDURE
argument_list|)
operator|.
name|param
argument_list|(
literal|"in_param"
argument_list|,
literal|20
argument_list|)
argument_list|)
decl_stmt|;
name|Number
name|price
init|=
operator|(
name|Number
operator|)
name|result
operator|.
name|getOutParam
argument_list|(
literal|"out_param"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|40
argument_list|,
name|price
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectDataObject
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsStoredProcedures
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|canMakeObjectsOutOfProcedures
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// create an artist with painting in the database
name|createArtist
argument_list|(
literal|1101.01
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Artist
argument_list|>
name|artists
init|=
name|runProcedureSelect
argument_list|(
name|ProcedureCall
operator|.
name|query
argument_list|(
name|SELECT_STORED_PROCEDURE
argument_list|,
name|Artist
operator|.
name|class
argument_list|)
operator|.
name|param
argument_list|(
literal|"aName"
argument_list|,
literal|"An Artist"
argument_list|)
argument_list|)
operator|.
name|firstList
argument_list|()
decl_stmt|;
comment|// check the results
name|assertNotNull
argument_list|(
literal|"Null result from StoredProcedure."
argument_list|,
name|artists
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artists
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Artist
name|a
init|=
operator|(
name|Artist
operator|)
name|artists
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Painting
name|p
init|=
name|a
operator|.
name|getPaintingArray
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// invalidate painting, it may have been updated in the proc
name|context
operator|.
name|invalidateObjects
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1101.01
argument_list|,
name|p
operator|.
name|getEstimatedPrice
argument_list|()
operator|.
name|doubleValue
argument_list|()
argument_list|,
literal|0.02
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectWithRowDescriptor
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|accessStackAdapter
operator|.
name|supportsStoredProcedures
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// create an artist with painting in the database
name|createArtist
argument_list|(
literal|1000.0
argument_list|)
expr_stmt|;
comment|// TESTING THIS ***
comment|// A.ARTIST_ID, A.DATE_OF_BIRTH, A.ARTIST_NAME
name|ColumnDescriptor
index|[]
name|columns
init|=
operator|new
name|ColumnDescriptor
index|[
literal|3
index|]
decl_stmt|;
comment|// read ID as Long, and everything else as default types
name|columns
index|[
literal|0
index|]
operator|=
operator|new
name|ColumnDescriptor
argument_list|(
literal|"ARTIST_ID"
argument_list|,
name|Types
operator|.
name|BIGINT
argument_list|)
expr_stmt|;
name|columns
index|[
literal|1
index|]
operator|=
operator|new
name|ColumnDescriptor
argument_list|(
literal|"ARTIST_NAME"
argument_list|,
name|Types
operator|.
name|CHAR
argument_list|)
expr_stmt|;
name|columns
index|[
literal|2
index|]
operator|=
operator|new
name|ColumnDescriptor
argument_list|(
literal|"DATE_OF_BIRTH"
argument_list|,
name|Types
operator|.
name|DATE
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|rows
init|=
name|runProcedureSelect
argument_list|(
name|ProcedureCall
operator|.
name|dataRowQuery
argument_list|(
name|SELECT_STORED_PROCEDURE
argument_list|)
operator|.
name|param
argument_list|(
literal|"aName"
argument_list|,
literal|"An Artist"
argument_list|)
operator|.
name|param
argument_list|(
literal|"paintingPrice"
argument_list|,
literal|3000
argument_list|)
operator|.
name|resultDescriptor
argument_list|(
name|columns
argument_list|)
argument_list|)
operator|.
name|firstList
argument_list|()
decl_stmt|;
comment|// check the results
name|assertNotNull
argument_list|(
literal|"Null result from StoredProcedure."
argument_list|,
name|rows
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DataRow
name|artistRow
init|=
operator|(
name|DataRow
operator|)
name|rows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|artistRow
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|artistRow
operator|=
name|uppercaseConverter
argument_list|(
name|artistRow
argument_list|)
expr_stmt|;
name|Object
name|id
init|=
name|artistRow
operator|.
name|get
argument_list|(
literal|"ARTIST_ID"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expected Long, got: "
operator|+
name|id
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|id
operator|instanceof
name|Long
argument_list|)
expr_stmt|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|ProcedureResult
argument_list|<
name|T
argument_list|>
name|runProcedureSelect
parameter_list|(
name|ProcedureCall
argument_list|<
name|T
argument_list|>
name|q
parameter_list|)
block|{
comment|// Sybase blows whenever a transaction wraps a SP, so turn off
comment|// transactions
comment|// TODO: it is quite the opposite with PostgreSQL. If an SP returns an
comment|// open refcursor, it actually expects a TX in progress, so while we
comment|// don't have refcursor unit tests, this is something to keep in mind
comment|// e.g.
comment|// http://stackoverflow.com/questions/16921942/porting-apache-cayenne-from-oracle-to-postgresql
name|BaseTransaction
name|t
init|=
operator|new
name|ExternalTransaction
argument_list|(
name|jdbcEventLogger
argument_list|)
decl_stmt|;
name|BaseTransaction
operator|.
name|bindThreadTransaction
argument_list|(
name|t
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|q
operator|.
name|call
argument_list|(
name|context
argument_list|)
return|;
block|}
finally|finally
block|{
name|BaseTransaction
operator|.
name|bindThreadTransaction
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|t
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|createArtist
parameter_list|(
name|double
name|paintingPrice
parameter_list|)
block|{
name|Artist
name|a
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|a
operator|.
name|setArtistName
argument_list|(
literal|"An Artist"
argument_list|)
expr_stmt|;
name|Painting
name|p
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Painting
operator|.
name|class
argument_list|)
decl_stmt|;
name|p
operator|.
name|setPaintingTitle
argument_list|(
literal|"A Painting"
argument_list|)
expr_stmt|;
comment|// converting double to string prevents rounding weirdness...
name|p
operator|.
name|setEstimatedPrice
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|""
operator|+
name|paintingPrice
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|.
name|addToPaintingArray
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
comment|/**      * An ugly hack - converting row keys to uppercase ... Tracked via CAY-148.      */
specifier|private
name|DataRow
name|uppercaseConverter
parameter_list|(
name|DataRow
name|row
parameter_list|)
block|{
name|DataRow
name|converted
init|=
operator|new
name|DataRow
argument_list|(
name|row
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|row
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|converted
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|converted
return|;
block|}
block|}
end_class

end_unit

