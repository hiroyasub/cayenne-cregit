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
name|crypto
package|;
end_package

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
name|db
operator|.
name|Table2
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
name|Table7
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
name|transformer
operator|.
name|value
operator|.
name|IntegerConverter
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
name|CryptoUnitUtils
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
name|property
operator|.
name|PropertyFactory
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
name|ObjectSelect
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
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
name|assertNull
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
specifier|public
class|class
name|Runtime_AES128_IT
extends|extends
name|Runtime_AES128_Base
block|{
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
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
name|Table2
name|t1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Table2
operator|.
name|class
argument_list|)
decl_stmt|;
name|t1
operator|.
name|setPlainBytes
argument_list|(
literal|"plain_1"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoBytes
argument_list|(
literal|"crypto_1"
operator|.
name|getBytes
argument_list|()
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
name|table2
operator|.
name|select
argument_list|()
decl_stmt|;
name|assertArrayEquals
argument_list|(
literal|"plain_1"
operator|.
name|getBytes
argument_list|()
argument_list|,
operator|(
name|byte
index|[]
operator|)
name|data
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
literal|"crypto_1"
operator|.
name|getBytes
argument_list|()
argument_list|,
name|CryptoUnitUtils
operator|.
name|decrypt_AES_CBC
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|data
index|[
literal|2
index|]
argument_list|,
name|runtime
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInsert_Numeric
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
name|setPlainInt
argument_list|(
literal|59
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoInt
argument_list|(
literal|61
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
literal|59
argument_list|,
name|data
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|61
argument_list|)
argument_list|,
name|IntegerConverter
operator|.
name|INSTANCE
operator|.
name|fromBytes
argument_list|(
name|CryptoUnitUtils
operator|.
name|decrypt_AES_CBC
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|data
index|[
literal|4
index|]
argument_list|,
name|runtime
argument_list|)
argument_list|)
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
name|Table2
name|t1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Table2
operator|.
name|class
argument_list|)
decl_stmt|;
name|t1
operator|.
name|setPlainBytes
argument_list|(
literal|"a"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoBytes
argument_list|(
literal|"crypto_1"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|Table2
name|t2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Table2
operator|.
name|class
argument_list|)
decl_stmt|;
name|t2
operator|.
name|setPlainBytes
argument_list|(
literal|"b"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|t2
operator|.
name|setCryptoBytes
argument_list|(
literal|"crypto_2"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|Table2
name|t3
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Table2
operator|.
name|class
argument_list|)
decl_stmt|;
name|t3
operator|.
name|setPlainBytes
argument_list|(
literal|"c"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|t3
operator|.
name|setCryptoBytes
argument_list|(
literal|null
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
name|table2
operator|.
name|selectAll
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
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
name|byte
index|[]
argument_list|>
name|cipherByPlain
init|=
operator|new
name|HashMap
argument_list|<>
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
operator|new
name|String
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|r
index|[
literal|1
index|]
argument_list|)
argument_list|,
operator|(
name|byte
index|[]
operator|)
name|r
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
name|assertArrayEquals
argument_list|(
literal|"crypto_1"
operator|.
name|getBytes
argument_list|()
argument_list|,
name|CryptoUnitUtils
operator|.
name|decrypt_AES_CBC
argument_list|(
name|cipherByPlain
operator|.
name|get
argument_list|(
literal|"a"
argument_list|)
argument_list|,
name|runtime
argument_list|)
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
literal|"crypto_2"
operator|.
name|getBytes
argument_list|()
argument_list|,
name|CryptoUnitUtils
operator|.
name|decrypt_AES_CBC
argument_list|(
name|cipherByPlain
operator|.
name|get
argument_list|(
literal|"b"
argument_list|)
argument_list|,
name|runtime
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|cipherByPlain
operator|.
name|get
argument_list|(
literal|"c"
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
name|ObjectContext
name|context
init|=
name|runtime
operator|.
name|newContext
argument_list|()
decl_stmt|;
name|Table2
name|t1
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Table2
operator|.
name|class
argument_list|)
decl_stmt|;
name|t1
operator|.
name|setPlainBytes
argument_list|(
literal|"a"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoBytes
argument_list|(
literal|"crypto_1"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|Table2
name|t2
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Table2
operator|.
name|class
argument_list|)
decl_stmt|;
name|t2
operator|.
name|setPlainBytes
argument_list|(
literal|"b"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|t2
operator|.
name|setCryptoBytes
argument_list|(
literal|"crypto_2"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|Table2
name|t3
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Table2
operator|.
name|class
argument_list|)
decl_stmt|;
name|t3
operator|.
name|setPlainBytes
argument_list|(
literal|"c"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|t3
operator|.
name|setCryptoBytes
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|ObjectSelect
argument_list|<
name|Table2
argument_list|>
name|select
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Table2
operator|.
name|class
argument_list|)
operator|.
name|orderBy
argument_list|(
name|Table2
operator|.
name|PLAIN_BYTES
operator|.
name|asc
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Table2
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
name|assertArrayEquals
argument_list|(
literal|"crypto_1"
operator|.
name|getBytes
argument_list|()
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCryptoBytes
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
literal|"crypto_2"
operator|.
name|getBytes
argument_list|()
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getCryptoBytes
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
literal|null
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getCryptoBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_SelectNumeric
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
name|setPlainInt
argument_list|(
literal|59
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoInt
argument_list|(
literal|61
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Table1
argument_list|>
name|result
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Table1
operator|.
name|class
argument_list|)
operator|.
name|select
argument_list|(
name|runtime
operator|.
name|newContext
argument_list|()
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
name|assertEquals
argument_list|(
literal|59
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPlainInt
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|61
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCryptoInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_ColumnQueryObject
parameter_list|()
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
name|setCryptoInt
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoString
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Table1
argument_list|>
name|result
init|=
name|ObjectSelect
operator|.
name|columnQuery
argument_list|(
name|Table1
operator|.
name|class
argument_list|,
name|PropertyFactory
operator|.
name|createSelf
argument_list|(
name|Table1
operator|.
name|class
argument_list|)
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
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCryptoInt
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_ColumnQueryObjectWithPlainScalar
parameter_list|()
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
name|setCryptoInt
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setPlainInt
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoString
argument_list|(
literal|"test"
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
name|result
init|=
name|ObjectSelect
operator|.
name|columnQuery
argument_list|(
name|Table1
operator|.
name|class
argument_list|,
name|PropertyFactory
operator|.
name|createSelf
argument_list|(
name|Table1
operator|.
name|class
argument_list|)
argument_list|,
name|Table1
operator|.
name|PLAIN_INT
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
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
operator|(
operator|(
name|Table1
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|0
index|]
operator|)
operator|.
name|getCryptoInt
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
operator|(
operator|(
name|Table1
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|0
index|]
operator|)
operator|.
name|getCryptoString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_ColumnQueryObjectWithEncryptedScalar
parameter_list|()
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
name|setCryptoInt
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setPlainInt
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoString
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setPlainString
argument_list|(
literal|"Test"
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
name|result
init|=
name|ObjectSelect
operator|.
name|columnQuery
argument_list|(
name|Table1
operator|.
name|class
argument_list|,
name|PropertyFactory
operator|.
name|createSelf
argument_list|(
name|Table1
operator|.
name|class
argument_list|)
argument_list|,
name|Table1
operator|.
name|CRYPTO_INT
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
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
operator|(
operator|(
name|Table1
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|0
index|]
operator|)
operator|.
name|getCryptoInt
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
operator|(
operator|(
name|Table1
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|0
index|]
operator|)
operator|.
name|getCryptoString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testColumnQueryWithRelationshipWithTheSameNames
parameter_list|()
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
name|setCryptoInt
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setPlainInt
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoString
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setPlainString
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|Table7
name|t7
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Table7
operator|.
name|class
argument_list|)
decl_stmt|;
name|t7
operator|.
name|setCryptoInt
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|t7
operator|.
name|setCryptoString
argument_list|(
literal|"string"
argument_list|)
expr_stmt|;
name|t1
operator|.
name|addToTable7s
argument_list|(
name|t7
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
name|result
init|=
name|ObjectSelect
operator|.
name|columnQuery
argument_list|(
name|Table1
operator|.
name|class
argument_list|,
name|Table1
operator|.
name|CRYPTO_INT
argument_list|,
name|PropertyFactory
operator|.
name|createSelf
argument_list|(
name|Table1
operator|.
name|class
argument_list|)
argument_list|,
name|Table1
operator|.
name|CRYPTO_INT
argument_list|,
name|Table1
operator|.
name|TABLE7S
operator|.
name|dot
argument_list|(
name|Table7
operator|.
name|CRYPTO_INT
argument_list|)
argument_list|,
name|Table1
operator|.
name|TABLE7S
operator|.
name|dot
argument_list|(
name|Table7
operator|.
name|CRYPTO_STRING
argument_list|)
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
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
operator|(
operator|(
name|Table1
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|1
index|]
operator|)
operator|.
name|getCryptoInt
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
operator|(
operator|(
name|Table1
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|1
index|]
operator|)
operator|.
name|getCryptoString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"string"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|4
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectWith2Objects
parameter_list|()
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
name|setCryptoInt
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setPlainInt
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoString
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setPlainString
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|Table7
name|t7
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Table7
operator|.
name|class
argument_list|)
decl_stmt|;
name|t7
operator|.
name|setCryptoInt
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|t7
operator|.
name|setCryptoString
argument_list|(
literal|"string"
argument_list|)
expr_stmt|;
name|t1
operator|.
name|addToTable7s
argument_list|(
name|t7
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
name|result
init|=
name|ObjectSelect
operator|.
name|columnQuery
argument_list|(
name|Table1
operator|.
name|class
argument_list|,
name|PropertyFactory
operator|.
name|createSelf
argument_list|(
name|Table1
operator|.
name|class
argument_list|)
argument_list|,
name|Table1
operator|.
name|TABLE7S
operator|.
name|flat
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
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
operator|(
operator|(
name|Table1
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|0
index|]
operator|)
operator|.
name|getCryptoString
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|1
index|]
operator|instanceof
name|Table7
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
operator|(
operator|(
name|Table7
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|1
index|]
operator|)
operator|.
name|getCryptoInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjectSelectWithPrefetch
parameter_list|()
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
name|setCryptoInt
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setPlainInt
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoString
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setPlainString
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|Table7
name|t7
init|=
name|context
operator|.
name|newObject
argument_list|(
name|Table7
operator|.
name|class
argument_list|)
decl_stmt|;
name|t7
operator|.
name|setCryptoInt
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|t7
operator|.
name|setCryptoString
argument_list|(
literal|"string"
argument_list|)
expr_stmt|;
name|t1
operator|.
name|addToTable7s
argument_list|(
name|t7
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Table1
argument_list|>
name|table1s
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Table1
operator|.
name|class
argument_list|)
operator|.
name|prefetch
argument_list|(
name|Table1
operator|.
name|TABLE7S
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
name|table1s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|table1s
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
literal|"string"
argument_list|,
name|table1s
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTable7s
argument_list|()
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_ColumnQuerySingleScalar
parameter_list|()
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
name|setCryptoInt
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoString
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|result
init|=
name|ObjectSelect
operator|.
name|columnQuery
argument_list|(
name|Table1
operator|.
name|class
argument_list|,
name|Table1
operator|.
name|CRYPTO_STRING
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
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|test_ColumnQueryMultipleScalars
parameter_list|()
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
name|setCryptoInt
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setCryptoString
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|t1
operator|.
name|setPlainInt
argument_list|(
literal|2
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
name|result
init|=
name|ObjectSelect
operator|.
name|columnQuery
argument_list|(
name|Table1
operator|.
name|class
argument_list|,
name|Table1
operator|.
name|CRYPTO_STRING
argument_list|,
name|Table1
operator|.
name|CRYPTO_INT
argument_list|,
name|Table1
operator|.
name|PLAIN_INT
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
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

