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
name|List
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
name|query
operator|.
name|UpdateBatchQuery
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
name|quotemap
operator|.
name|QuoteAdress
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
name|quotemap
operator|.
name|Quote_Person
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
name|AccessStack
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
name|unit
operator|.
name|CayenneResources
import|;
end_import

begin_class
specifier|public
class|class
name|DataContexQuoteTest
extends|extends
name|CayenneCase
block|{
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|AccessStack
name|buildAccessStack
parameter_list|()
block|{
return|return
name|CayenneResources
operator|.
name|getResources
argument_list|()
operator|.
name|getAccessStack
argument_list|(
name|QUOTEMAP_ACCESS_STACK
argument_list|)
return|;
block|}
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
name|testPrefetchQuote
parameter_list|()
throws|throws
name|Exception
block|{
comment|// work with tables QuoteAdress and Quote_Person.
comment|// In this table parameter quoteSqlIdentifiers = true.
name|QuoteAdress
name|quoteAdress
init|=
operator|(
name|QuoteAdress
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"QuoteAdress"
argument_list|)
decl_stmt|;
name|quoteAdress
operator|.
name|setCity
argument_list|(
literal|"city"
argument_list|)
expr_stmt|;
name|Quote_Person
name|quote_Person
init|=
operator|(
name|Quote_Person
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Quote_Person"
argument_list|)
decl_stmt|;
name|quote_Person
operator|.
name|setSalary
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
name|quote_Person
operator|.
name|setName
argument_list|(
literal|"Arcadi"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|SelectQuery
name|q
init|=
operator|new
name|SelectQuery
argument_list|(
name|QuoteAdress
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
name|objects
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQuery
name|qQuote_Person
init|=
operator|new
name|SelectQuery
argument_list|(
name|Quote_Person
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
name|objects2
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|qQuote_Person
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|QuoteAdress
name|quoteAdress2
init|=
operator|(
name|QuoteAdress
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"QuoteAdress"
argument_list|)
decl_stmt|;
name|quoteAdress2
operator|.
name|setCity
argument_list|(
literal|"city2"
argument_list|)
expr_stmt|;
name|Quote_Person
name|quote_Person2
init|=
operator|(
name|Quote_Person
operator|)
name|context
operator|.
name|newObject
argument_list|(
literal|"Quote_Person"
argument_list|)
decl_stmt|;
name|quote_Person2
operator|.
name|setSalary
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|quote_Person2
operator|.
name|setName
argument_list|(
literal|"Name"
argument_list|)
expr_stmt|;
name|quote_Person2
operator|.
name|setDAte
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|DbEntity
name|entity
init|=
name|getDomain
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|QuoteAdress
operator|.
name|class
argument_list|)
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|List
name|idAttributes
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|entity
operator|.
name|getAttribute
argument_list|(
literal|"City"
argument_list|)
argument_list|)
decl_stmt|;
name|List
name|updatedAttributes
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|entity
operator|.
name|getAttribute
argument_list|(
literal|"City"
argument_list|)
argument_list|)
decl_stmt|;
name|UpdateBatchQuery
name|updateQuery
init|=
operator|new
name|UpdateBatchQuery
argument_list|(
name|entity
argument_list|,
name|idAttributes
argument_list|,
name|updatedAttributes
argument_list|,
literal|null
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|List
name|objects3
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|updateQuery
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|objects3
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQuery
name|qQuote_Person2
init|=
operator|new
name|SelectQuery
argument_list|(
name|Quote_Person
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
name|objects4
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|qQuote_Person
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|objects4
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

