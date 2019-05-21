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
name|access
operator|.
name|jdbc
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
name|unsupported_distinct_types
operator|.
name|Customer
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
name|unsupported_distinct_types
operator|.
name|Product
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
name|Types
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|UNSUPPORTED_DISTINCT_TYPES_PROJECT
argument_list|)
specifier|public
class|class
name|SelectActionWithUnsupportedDistinctTypesIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|protected
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|private
name|TableHelper
name|tProduct
decl_stmt|;
specifier|private
name|TableHelper
name|tComposition
decl_stmt|;
specifier|private
name|TableHelper
name|tCustomer
decl_stmt|;
specifier|private
name|TableHelper
name|tOrders
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
name|tProduct
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"PRODUCT"
argument_list|)
expr_stmt|;
name|tProduct
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"LONGVARCHAR_COL"
argument_list|)
expr_stmt|;
name|tCustomer
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"CUSTOMER"
argument_list|)
expr_stmt|;
name|tCustomer
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"LONGVARCHAR_COL"
argument_list|)
expr_stmt|;
name|tComposition
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"COMPOSITION"
argument_list|)
expr_stmt|;
name|tComposition
operator|.
name|setColumns
argument_list|(
literal|"BASE_ID"
argument_list|,
literal|"CONTAINED_ID"
argument_list|)
expr_stmt|;
name|tOrders
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"ORDERS"
argument_list|)
expr_stmt|;
name|tOrders
operator|.
name|setColumns
argument_list|(
literal|"CUSTOMER_ID"
argument_list|,
literal|"PRODUCT_ID"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createCompositionManyToManyDataSet
parameter_list|()
throws|throws
name|SQLException
block|{
name|tProduct
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"product1"
argument_list|)
expr_stmt|;
name|tProduct
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"product2"
argument_list|)
expr_stmt|;
name|tProduct
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"product3"
argument_list|)
expr_stmt|;
name|tProduct
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|"product4"
argument_list|)
expr_stmt|;
name|tComposition
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tComposition
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tComposition
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|tComposition
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tComposition
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|tComposition
operator|.
name|insert
argument_list|(
literal|4
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createOrdersManyToManyDataSet
parameter_list|()
throws|throws
name|SQLException
block|{
name|tProduct
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"product1"
argument_list|)
expr_stmt|;
name|tProduct
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"product2"
argument_list|)
expr_stmt|;
name|tProduct
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"product3"
argument_list|)
expr_stmt|;
name|tCustomer
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"customer1"
argument_list|)
expr_stmt|;
name|tCustomer
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"customer2"
argument_list|)
expr_stmt|;
name|tCustomer
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"customer3"
argument_list|)
expr_stmt|;
name|tOrders
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tOrders
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tOrders
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|tOrders
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|tOrders
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|tOrders
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCompositionSelectManyToManyQuery
parameter_list|()
throws|throws
name|SQLException
block|{
name|createCompositionManyToManyDataSet
argument_list|()
expr_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|Product
operator|.
name|class
argument_list|)
decl_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
literal|"contained"
argument_list|)
expr_stmt|;
name|query
operator|.
name|addPrefetch
argument_list|(
literal|"base"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Product
argument_list|>
name|result
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
for|for
control|(
name|Product
name|product
range|:
name|result
control|)
block|{
name|List
argument_list|<
name|Product
argument_list|>
name|productsContained
init|=
name|product
operator|.
name|getContained
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|productsContained
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Product
argument_list|>
name|productsBase
init|=
name|product
operator|.
name|getBase
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|productsBase
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|productsContained
operator|.
name|size
argument_list|()
operator|+
name|productsBase
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOrdersSelectManyToManyQuery
parameter_list|()
throws|throws
name|SQLException
block|{
name|createOrdersManyToManyDataSet
argument_list|()
expr_stmt|;
name|List
name|assertSizes
init|=
operator|new
name|ArrayList
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|assertSizes
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|SelectQuery
name|productQuery
init|=
operator|new
name|SelectQuery
argument_list|(
name|Product
operator|.
name|class
argument_list|)
decl_stmt|;
name|productQuery
operator|.
name|addPrefetch
argument_list|(
literal|"orderBy"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Product
argument_list|>
name|productResult
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|productQuery
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|productResult
argument_list|)
expr_stmt|;
name|List
name|orderBySizes
init|=
operator|new
name|ArrayList
argument_list|(
literal|3
argument_list|)
decl_stmt|;
for|for
control|(
name|Product
name|product
range|:
name|productResult
control|)
block|{
name|List
argument_list|<
name|Customer
argument_list|>
name|orderBy
init|=
name|product
operator|.
name|getOrderBy
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|orderBy
argument_list|)
expr_stmt|;
name|orderBySizes
operator|.
name|add
argument_list|(
name|orderBy
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|assertSizes
operator|.
name|containsAll
argument_list|(
name|orderBySizes
argument_list|)
argument_list|)
expr_stmt|;
name|SelectQuery
name|customerQuery
init|=
operator|new
name|SelectQuery
argument_list|(
name|Customer
operator|.
name|class
argument_list|)
decl_stmt|;
name|customerQuery
operator|.
name|addPrefetch
argument_list|(
literal|"order"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Customer
argument_list|>
name|customerResult
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|customerQuery
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|customerResult
argument_list|)
expr_stmt|;
name|List
name|orderSizes
init|=
operator|new
name|ArrayList
argument_list|(
literal|3
argument_list|)
decl_stmt|;
for|for
control|(
name|Customer
name|customer
range|:
name|customerResult
control|)
block|{
name|List
argument_list|<
name|Product
argument_list|>
name|orders
init|=
name|customer
operator|.
name|getOrder
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|orders
argument_list|)
expr_stmt|;
name|orderSizes
operator|.
name|add
argument_list|(
name|orders
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|assertSizes
operator|.
name|containsAll
argument_list|(
name|orderSizes
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

