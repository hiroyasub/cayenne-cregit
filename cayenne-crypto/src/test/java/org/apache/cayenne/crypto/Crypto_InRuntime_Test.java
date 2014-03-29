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
name|crypto
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
name|HashMap
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
name|crypto
operator|.
name|db
operator|.
name|Table1
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
name|crypto
operator|.
name|map
operator|.
name|PatternColumnMapper
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
name|crypto
operator|.
name|unit
operator|.
name|Rot13TransformerFactory
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
name|Module
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

begin_class
specifier|public
class|class
name|Crypto_InRuntime_Test
block|{
specifier|private
name|ServerRuntime
name|runtime
decl_stmt|;
specifier|private
name|TableHelper
name|table1
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
name|Module
name|crypto
init|=
operator|new
name|CryptoModuleBuilder
argument_list|()
operator|.
name|valueTransformer
argument_list|(
name|Rot13TransformerFactory
operator|.
name|class
argument_list|)
operator|.
name|columnMapper
argument_list|(
operator|new
name|PatternColumnMapper
argument_list|(
literal|"^CRYPTO_"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|this
operator|.
name|runtime
operator|=
operator|new
name|ServerRuntime
argument_list|(
literal|"cayenne-crypto.xml"
argument_list|,
name|crypto
argument_list|)
expr_stmt|;
name|DBHelper
name|dbHelper
init|=
operator|new
name|DBHelper
argument_list|(
name|runtime
operator|.
name|getDataSource
argument_list|(
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|this
operator|.
name|table1
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"TABLE1"
argument_list|)
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"PLAIN_STRING"
argument_list|,
literal|"CRYPTO_STRING"
argument_list|)
expr_stmt|;
name|table1
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInsert
parameter_list|()
throws|throws
name|SQLException
block|{
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|Table1
name|t1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Table1
operator|.
name|class
argument_list|)
decl_stmt|;
name|t1
operator|.
name|setPlainString
argument_list|(
literal|"plain_1"
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoString
argument_list|(
literal|"crypto_1"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|Object
index|[]
name|data
init|=
name|table1
operator|.
name|select
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"plain_1"
argument_list|,
name|data
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Rot13TransformerFactory
operator|.
name|rotate
argument_list|(
literal|"crypto_1"
argument_list|)
argument_list|,
name|data
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInsert_MultipleObjects
parameter_list|()
throws|throws
name|SQLException
block|{
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|Table1
name|t1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Table1
operator|.
name|class
argument_list|)
decl_stmt|;
name|t1
operator|.
name|setPlainString
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoString
argument_list|(
literal|"crypto_1"
argument_list|)
expr_stmt|;
name|Table1
name|t2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Table1
operator|.
name|class
argument_list|)
decl_stmt|;
name|t2
operator|.
name|setPlainString
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|t2
operator|.
name|setCryptoString
argument_list|(
literal|"crypto_2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|data
init|=
name|table1
operator|.
name|selectAll
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cipherByPlain
init|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
index|[]
name|r
range|:
name|data
control|)
block|{
name|cipherByPlain
operator|.
name|put
argument_list|(
name|r
index|[
literal|1
index|]
argument_list|,
name|r
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|Rot13TransformerFactory
operator|.
name|rotate
argument_list|(
literal|"crypto_1"
argument_list|)
argument_list|,
name|cipherByPlain
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Rot13TransformerFactory
operator|.
name|rotate
argument_list|(
literal|"crypto_2"
argument_list|)
argument_list|,
name|cipherByPlain
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_SelectQuery
parameter_list|()
throws|throws
name|SQLException
block|{
name|table1
operator|.
name|insert
argument_list|(
literal|1
argument_list|,
literal|"plain_1"
argument_list|,
name|Rot13TransformerFactory
operator|.
name|rotate
argument_list|(
literal|"crypto_1"
argument_list|)
argument_list|)
expr_stmt|;
name|table1
operator|.
name|insert
argument_list|(
literal|2
argument_list|,
literal|"plain_2"
argument_list|,
name|Rot13TransformerFactory
operator|.
name|rotate
argument_list|(
literal|"crypto_2"
argument_list|)
argument_list|)
expr_stmt|;
name|table1
operator|.
name|insert
argument_list|(
literal|3
argument_list|,
literal|"plain_3"
argument_list|,
name|Rot13TransformerFactory
operator|.
name|rotate
argument_list|(
literal|"crypto_3"
argument_list|)
argument_list|)
expr_stmt|;
name|SelectQuery
argument_list|<
name|Table1
argument_list|>
name|select
init|=
name|SelectQuery
operator|.
name|query
argument_list|(
name|Table1
operator|.
name|class
argument_list|)
decl_stmt|;
name|select
operator|.
name|addOrdering
argument_list|(
name|Table1
operator|.
name|PLAIN_STRING
operator|.
name|asc
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Table1
argument_list|>
name|result
init|=
name|runtime
operator|.
name|newContext
argument_list|()
operator|.
name|select
argument_list|(
name|select
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"crypto_1"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCryptoString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"crypto_2"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getCryptoString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"crypto_3"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getCryptoString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

