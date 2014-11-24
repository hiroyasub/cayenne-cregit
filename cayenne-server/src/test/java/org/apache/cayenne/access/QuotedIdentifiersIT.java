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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ObjectContext
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
name|ObjectId
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
name|exp
operator|.
name|ExpressionFactory
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
name|EJBQLQuery
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
name|ObjectIdQuery
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
name|RelationshipQuery
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|QUOTED_IDENTIFIERS_PROJECT
argument_list|)
specifier|public
class|class
name|QuotedIdentifiersIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|ObjectContext
name|context
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|QuoteAdress
name|quoteAdress
init|=
name|context
operator|.
name|newObject
argument_list|(
name|QuoteAdress
operator|.
name|class
argument_list|)
decl_stmt|;
name|quoteAdress
operator|.
name|setCity
argument_list|(
literal|"city"
argument_list|)
expr_stmt|;
name|quoteAdress
operator|.
name|setGroup
argument_list|(
literal|"324"
argument_list|)
expr_stmt|;
name|Quote_Person
name|quote_Person
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Quote_Person
operator|.
name|class
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
name|quote_Person
operator|.
name|setGroup
argument_list|(
literal|"107324"
argument_list|)
expr_stmt|;
name|quote_Person
operator|.
name|setAddress_Rel
argument_list|(
name|quoteAdress
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
name|context
operator|.
name|newObject
argument_list|(
name|QuoteAdress
operator|.
name|class
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
name|context
operator|.
name|newObject
argument_list|(
name|Quote_Person
operator|.
name|class
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
name|setGroup
argument_list|(
literal|"1111"
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
name|quote_Person2
operator|.
name|setAddress_Rel
argument_list|(
name|quoteAdress2
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrefetchQuote
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getObjEntity
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
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptySet
argument_list|()
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
name|qQuote_Person2
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
name|SelectQuery
name|qQuote_Person3
init|=
operator|new
name|SelectQuery
argument_list|(
name|Quote_Person
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"salary"
argument_list|,
literal|100
argument_list|)
argument_list|)
decl_stmt|;
name|List
name|objects5
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|qQuote_Person3
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects5
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQuery
name|qQuote_Person4
init|=
operator|new
name|SelectQuery
argument_list|(
name|Quote_Person
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"group"
argument_list|,
literal|"107324"
argument_list|)
argument_list|)
decl_stmt|;
name|List
name|objects6
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|qQuote_Person4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects6
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQuery
name|quoteAdress1
init|=
operator|new
name|SelectQuery
argument_list|(
name|QuoteAdress
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"group"
argument_list|,
literal|"324"
argument_list|)
argument_list|)
decl_stmt|;
name|List
name|objects7
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|quoteAdress1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects7
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectIdQuery
name|queryObjectId
init|=
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"QuoteAdress"
argument_list|,
name|QuoteAdress
operator|.
name|GROUP_PROPERTY
argument_list|,
literal|"324"
argument_list|)
argument_list|)
decl_stmt|;
name|List
name|objects8
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|queryObjectId
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects8
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectIdQuery
name|queryObjectId2
init|=
operator|new
name|ObjectIdQuery
argument_list|(
operator|new
name|ObjectId
argument_list|(
literal|"Quote_Person"
argument_list|,
literal|"GROUP"
argument_list|,
literal|"1111"
argument_list|)
argument_list|)
decl_stmt|;
name|List
name|objects9
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|queryObjectId2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects9
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SelectQuery
name|person2Query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Quote_Person
operator|.
name|class
argument_list|,
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
literal|"name"
argument_list|,
literal|"Name"
argument_list|)
argument_list|)
decl_stmt|;
name|Quote_Person
name|quote_Person2
init|=
operator|(
name|Quote_Person
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|person2Query
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|RelationshipQuery
name|relationshipQuery
init|=
operator|new
name|RelationshipQuery
argument_list|(
name|quote_Person2
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|"address_Rel"
argument_list|)
decl_stmt|;
name|List
name|objects10
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|relationshipQuery
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects10
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
name|testQuotedEJBQLQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ejbql
init|=
literal|"select a from QuoteAdress a where a.group = '324'"
decl_stmt|;
name|EJBQLQuery
name|queryEJBQL
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
name|objects11
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|queryEJBQL
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|objects11
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
name|testQuotedEJBQLQueryWithJoin
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ejbql
init|=
literal|"select p from Quote_Person p join p.address_Rel a where p.name = 'Arcadi'"
decl_stmt|;
name|EJBQLQuery
name|queryEJBQL
init|=
operator|new
name|EJBQLQuery
argument_list|(
name|ejbql
argument_list|)
decl_stmt|;
name|List
name|resultList
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|queryEJBQL
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resultList
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
name|testQuotedEJBQLQueryWithOrderBy
parameter_list|()
throws|throws
name|Exception
block|{
name|EJBQLQuery
name|query
init|=
operator|new
name|EJBQLQuery
argument_list|(
literal|"select p from Quote_Person p order by p.name"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Quote_Person
argument_list|>
name|resultList
init|=
operator|(
name|List
argument_list|<
name|Quote_Person
argument_list|>
operator|)
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|resultList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Arcadi"
argument_list|,
name|resultList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Name"
argument_list|,
name|resultList
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

