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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|art
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
name|art
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
name|map
operator|.
name|Procedure
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
name|ProcedureQuery
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
name|SelectQuery
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

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DataContextProcedureQueryTest
extends|extends
name|CayenneCase
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
specifier|protected
name|DataContext
name|ctxt
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
if|if
condition|(
operator|!
name|getAccessStackAdapter
argument_list|()
operator|.
name|supportsStoredProcedures
argument_list|()
condition|)
block|{
return|return;
block|}
name|deleteTestData
argument_list|()
expr_stmt|;
name|ctxt
operator|=
name|createDataContext
argument_list|()
expr_stmt|;
block|}
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
name|getAccessStackAdapter
argument_list|()
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
name|ProcedureQuery
name|q
init|=
operator|new
name|ProcedureQuery
argument_list|(
name|UPDATE_STORED_PROCEDURE
argument_list|)
decl_stmt|;
name|q
operator|.
name|addParameter
argument_list|(
literal|"paintingPrice"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|3000
argument_list|)
argument_list|)
expr_stmt|;
comment|// since stored procedure commits its stuff, we must use an explicit
comment|// non-committing transaction
name|Transaction
name|t
init|=
name|Transaction
operator|.
name|externalTransaction
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|Transaction
operator|.
name|bindThreadTransaction
argument_list|(
name|t
argument_list|)
expr_stmt|;
try|try
block|{
name|ctxt
operator|.
name|performGenericQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Transaction
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
comment|// check that price have doubled
name|SelectQuery
name|select
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|select
operator|.
name|addPrefetch
argument_list|(
literal|"paintingArray"
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|ctxt
operator|.
name|performQuery
argument_list|(
name|select
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
name|getAccessStackAdapter
argument_list|()
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
name|ProcedureQuery
name|q
init|=
operator|new
name|ProcedureQuery
argument_list|(
name|UPDATE_STORED_PROCEDURE_NOPARAM
argument_list|)
decl_stmt|;
comment|// since stored procedure commits its stuff, we must use an explicit
comment|// non-committing transaction
name|Transaction
name|t
init|=
name|Transaction
operator|.
name|externalTransaction
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|Transaction
operator|.
name|bindThreadTransaction
argument_list|(
name|t
argument_list|)
expr_stmt|;
try|try
block|{
name|ctxt
operator|.
name|performGenericQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Transaction
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
comment|// check that price have doubled
name|SelectQuery
name|select
init|=
operator|new
name|SelectQuery
argument_list|(
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|select
operator|.
name|addPrefetch
argument_list|(
literal|"paintingArray"
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|ctxt
operator|.
name|performQuery
argument_list|(
name|select
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
specifier|public
name|void
name|testSelect1
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|getAccessStackAdapter
argument_list|()
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
name|ProcedureQuery
name|q
init|=
operator|new
name|ProcedureQuery
argument_list|(
name|SELECT_STORED_PROCEDURE
argument_list|)
decl_stmt|;
name|q
operator|.
name|addParameter
argument_list|(
literal|"aName"
argument_list|,
literal|"An Artist"
argument_list|)
expr_stmt|;
name|q
operator|.
name|addParameter
argument_list|(
literal|"paintingPrice"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|3000
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|runProcedureSelect
argument_list|(
name|q
argument_list|)
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
operator|(
name|Artist
operator|)
name|ctxt
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
argument_list|,
literal|false
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
name|ctxt
operator|.
name|invalidateObjects
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|p
argument_list|)
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
specifier|public
name|void
name|testSelect2
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|getAccessStackAdapter
argument_list|()
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
name|ProcedureQuery
name|q
init|=
operator|new
name|ProcedureQuery
argument_list|(
name|SELECT_STORED_PROCEDURE
argument_list|)
decl_stmt|;
name|q
operator|.
name|addParameter
argument_list|(
literal|"aName"
argument_list|,
literal|"An Artist"
argument_list|)
expr_stmt|;
name|q
operator|.
name|addParameter
argument_list|(
literal|"paintingPrice"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|3000
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|runProcedureSelect
argument_list|(
name|q
argument_list|)
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
operator|(
name|Artist
operator|)
name|ctxt
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
argument_list|,
literal|false
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
name|ctxt
operator|.
name|invalidateObjects
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|p
argument_list|)
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
specifier|public
name|void
name|testSelect3
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|getAccessStackAdapter
argument_list|()
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
comment|// test ProcedureQuery with Procedure as root
name|Procedure
name|proc
init|=
name|ctxt
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getProcedure
argument_list|(
name|SELECT_STORED_PROCEDURE
argument_list|)
decl_stmt|;
name|ProcedureQuery
name|q
init|=
operator|new
name|ProcedureQuery
argument_list|(
name|proc
argument_list|)
decl_stmt|;
name|q
operator|.
name|addParameter
argument_list|(
literal|"aName"
argument_list|,
literal|"An Artist"
argument_list|)
expr_stmt|;
name|q
operator|.
name|addParameter
argument_list|(
literal|"paintingPrice"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|3000
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|runProcedureSelect
argument_list|(
name|q
argument_list|)
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
operator|(
name|Artist
operator|)
name|ctxt
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
argument_list|,
literal|false
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
name|ctxt
operator|.
name|invalidateObjects
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|p
argument_list|)
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
name|getAccessStackAdapter
argument_list|()
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
name|ProcedureQuery
name|q
init|=
operator|new
name|ProcedureQuery
argument_list|(
name|SELECT_STORED_PROCEDURE
argument_list|)
decl_stmt|;
name|q
operator|.
name|addParameter
argument_list|(
literal|"aName"
argument_list|,
literal|"An Artist"
argument_list|)
expr_stmt|;
name|q
operator|.
name|addParameter
argument_list|(
literal|"paintingPrice"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|3000
argument_list|)
argument_list|)
expr_stmt|;
name|q
operator|.
name|setFetchLimit
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|runProcedureSelect
argument_list|(
name|q
argument_list|)
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
name|getAccessStackAdapter
argument_list|()
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
name|ProcedureQuery
name|q
init|=
operator|new
name|ProcedureQuery
argument_list|(
name|SELECT_STORED_PROCEDURE
argument_list|)
decl_stmt|;
name|q
operator|.
name|addParameter
argument_list|(
literal|"aName"
argument_list|,
literal|"An Artist"
argument_list|)
expr_stmt|;
name|q
operator|.
name|addParameter
argument_list|(
literal|"paintingPrice"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|3000
argument_list|)
argument_list|)
expr_stmt|;
name|q
operator|.
name|setFetchOffset
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|runProcedureSelect
argument_list|(
name|q
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
block|}
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
name|getAccessStackAdapter
argument_list|()
operator|.
name|supportsStoredProcedures
argument_list|()
condition|)
block|{
return|return;
block|}
name|ProcedureQuery
name|q
init|=
operator|new
name|ProcedureQuery
argument_list|(
name|OUT_STORED_PROCEDURE
argument_list|)
decl_stmt|;
name|q
operator|.
name|addParameter
argument_list|(
literal|"in_param"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|20
argument_list|)
argument_list|)
expr_stmt|;
name|List
name|rows
init|=
name|runProcedureSelect
argument_list|(
name|q
argument_list|)
decl_stmt|;
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
name|Object
name|row
init|=
name|rows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected row class: "
operator|+
name|row
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|row
operator|instanceof
name|Map
argument_list|)
expr_stmt|;
name|Map
name|outParams
init|=
operator|(
name|Map
operator|)
name|row
decl_stmt|;
name|Number
name|price
init|=
operator|(
name|Number
operator|)
name|outParams
operator|.
name|get
argument_list|(
literal|"out_param"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Null result... row content: "
operator|+
name|row
argument_list|,
name|price
argument_list|)
expr_stmt|;
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
name|getAccessStackAdapter
argument_list|()
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
name|getAccessStackAdapter
argument_list|()
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
name|ProcedureQuery
name|q
init|=
operator|new
name|ProcedureQuery
argument_list|(
name|SELECT_STORED_PROCEDURE
argument_list|,
name|Artist
operator|.
name|class
argument_list|)
decl_stmt|;
name|q
operator|.
name|addParameter
argument_list|(
literal|"aName"
argument_list|,
literal|"An Artist"
argument_list|)
expr_stmt|;
name|List
name|artists
init|=
name|runProcedureSelect
argument_list|(
name|q
argument_list|)
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
name|ctxt
operator|.
name|invalidateObjects
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|p
argument_list|)
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
specifier|public
name|void
name|testSelectWithRowDescriptor
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Don't run this on MySQL
if|if
condition|(
operator|!
name|getAccessStackAdapter
argument_list|()
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
comment|// test ProcedureQuery with Procedure as root
name|Procedure
name|proc
init|=
name|ctxt
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getProcedure
argument_list|(
name|SELECT_STORED_PROCEDURE
argument_list|)
decl_stmt|;
name|ProcedureQuery
name|q
init|=
operator|new
name|ProcedureQuery
argument_list|(
name|proc
argument_list|)
decl_stmt|;
name|q
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|q
operator|.
name|addParameter
argument_list|(
literal|"aName"
argument_list|,
literal|"An Artist"
argument_list|)
expr_stmt|;
name|q
operator|.
name|addParameter
argument_list|(
literal|"paintingPrice"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|3000
argument_list|)
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
name|INTEGER
argument_list|,
name|Long
operator|.
name|class
operator|.
name|getName
argument_list|()
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
argument_list|,
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
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
argument_list|,
name|Date
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|q
operator|.
name|addResultDescriptor
argument_list|(
name|columns
argument_list|)
expr_stmt|;
name|List
name|rows
init|=
name|runProcedureSelect
argument_list|(
name|q
argument_list|)
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
specifier|protected
name|List
name|runProcedureSelect
parameter_list|(
name|ProcedureQuery
name|q
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Sybase blows whenever a transaction wraps a SP, so turn off transactions
name|boolean
name|transactionsFlag
init|=
name|ctxt
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|isUsingExternalTransactions
argument_list|()
decl_stmt|;
name|ctxt
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|setUsingExternalTransactions
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|ctxt
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
return|;
block|}
finally|finally
block|{
name|ctxt
operator|.
name|getParentDataDomain
argument_list|()
operator|.
name|setUsingExternalTransactions
argument_list|(
name|transactionsFlag
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
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
name|ctxt
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
name|ctxt
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
name|ctxt
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
comment|/**      * An ugly hack - converting row keys to uppercase ... Tracked via CAY-148.      */
specifier|protected
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
name|Iterator
name|it
init|=
name|row
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
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
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|converted
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
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

