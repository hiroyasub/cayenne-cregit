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
operator|.
name|transformer
operator|.
name|value
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
name|key
operator|.
name|KeySource
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
name|DbAttribute
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
name|BeforeClass
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
name|math
operator|.
name|BigInteger
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
name|Date
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
name|assertSame
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

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
specifier|public
class|class
name|DefaultValueTransformerFactoryIT
block|{
specifier|private
specifier|static
name|DbEntity
name|t1
decl_stmt|;
specifier|private
specifier|static
name|DbEntity
name|t2
decl_stmt|;
specifier|private
specifier|static
name|DbEntity
name|t3
decl_stmt|;
specifier|private
specifier|static
name|DbEntity
name|t5
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|dbToBytes
decl_stmt|,
name|objectToBytes
decl_stmt|;
specifier|private
name|DefaultValueTransformerFactory
name|f
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
throws|throws
name|Exception
block|{
name|ServerRuntime
name|runtime
init|=
name|ServerRuntime
operator|.
name|builder
argument_list|()
operator|.
name|addConfig
argument_list|(
literal|"cayenne-crypto.xml"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|t1
operator|=
name|runtime
operator|.
name|getChannel
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDbEntity
argument_list|(
literal|"TABLE1"
argument_list|)
expr_stmt|;
name|t2
operator|=
name|runtime
operator|.
name|getChannel
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDbEntity
argument_list|(
literal|"TABLE2"
argument_list|)
expr_stmt|;
name|t3
operator|=
name|runtime
operator|.
name|getChannel
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDbEntity
argument_list|(
literal|"TABLE3"
argument_list|)
expr_stmt|;
name|t5
operator|=
name|runtime
operator|.
name|getChannel
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|getDbEntity
argument_list|(
literal|"TABLE5"
argument_list|)
expr_stmt|;
name|dbToBytes
operator|=
name|getDefaultDbConverters
argument_list|()
expr_stmt|;
name|objectToBytes
operator|=
name|getDefaultObjectConverters
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
block|{
name|f
operator|=
operator|new
name|DefaultValueTransformerFactory
argument_list|(
name|mock
argument_list|(
name|KeySource
operator|.
name|class
argument_list|)
argument_list|,
name|dbToBytes
argument_list|,
name|objectToBytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetJavaType
parameter_list|()
block|{
name|DbAttribute
name|t1_ct
init|=
name|t1
operator|.
name|getAttribute
argument_list|(
literal|"CRYPTO_STRING"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.String"
argument_list|,
name|f
operator|.
name|getJavaType
argument_list|(
name|t1_ct
argument_list|)
argument_list|)
expr_stmt|;
name|DbAttribute
name|t2_cb
init|=
name|t2
operator|.
name|getAttribute
argument_list|(
literal|"CRYPTO_BYTES"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"byte[]"
argument_list|,
name|f
operator|.
name|getJavaType
argument_list|(
name|t2_cb
argument_list|)
argument_list|)
expr_stmt|;
name|DbEntity
name|fakeEntity
init|=
name|mock
argument_list|(
name|DbEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|fakeEntity
operator|.
name|getDataMap
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|t1
operator|.
name|getDataMap
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|fakeA1
init|=
name|mock
argument_list|(
name|DbAttribute
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|fakeA1
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"fake1"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|fakeA1
operator|.
name|getEntity
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|fakeEntity
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|fakeA1
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Types
operator|.
name|VARBINARY
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"byte[]"
argument_list|,
name|f
operator|.
name|getJavaType
argument_list|(
name|fakeA1
argument_list|)
argument_list|)
expr_stmt|;
name|DbAttribute
name|fakeA2
init|=
name|mock
argument_list|(
name|DbAttribute
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|fakeA2
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"fake2"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|fakeA2
operator|.
name|getEntity
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|fakeEntity
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|fakeA2
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.String"
argument_list|,
name|f
operator|.
name|getJavaType
argument_list|(
name|fakeA2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetAmbiguousJavaType
parameter_list|()
block|{
comment|// this one have two bound ObjAttributes, warn should be in log
name|DbAttribute
name|a1
init|=
name|t5
operator|.
name|getAttribute
argument_list|(
literal|"CRYPTO_INT1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"java.lang.String"
argument_list|,
name|f
operator|.
name|getJavaType
argument_list|(
name|a1
argument_list|)
argument_list|)
expr_stmt|;
comment|// this one doesn't have any bindings, warn should be in log
name|DbAttribute
name|a2
init|=
name|t5
operator|.
name|getAttribute
argument_list|(
literal|"CRYPTO_INT2"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"byte[]"
argument_list|,
name|f
operator|.
name|getJavaType
argument_list|(
name|a2
argument_list|)
argument_list|)
expr_stmt|;
comment|// this one have one binding
name|DbAttribute
name|a3
init|=
name|t5
operator|.
name|getAttribute
argument_list|(
literal|"CRYPTO_INT3"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"int"
argument_list|,
name|f
operator|.
name|getJavaType
argument_list|(
name|a3
argument_list|)
argument_list|)
expr_stmt|;
comment|// this one have two bindings but with the same int type
name|DbAttribute
name|a4
init|=
name|t5
operator|.
name|getAttribute
argument_list|(
literal|"CRYPTO_INT4"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"int"
argument_list|,
name|f
operator|.
name|getJavaType
argument_list|(
name|a4
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateEncryptor
parameter_list|()
block|{
name|DbAttribute
name|t1_ct
init|=
name|t1
operator|.
name|getAttribute
argument_list|(
literal|"CRYPTO_STRING"
argument_list|)
decl_stmt|;
name|ValueEncryptor
name|t1
init|=
name|f
operator|.
name|createEncryptor
argument_list|(
name|t1_ct
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|t1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|t1
operator|instanceof
name|DefaultValueEncryptor
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Utf8StringConverter
operator|.
name|INSTANCE
argument_list|,
operator|(
operator|(
name|DefaultValueEncryptor
operator|)
name|t1
operator|)
operator|.
name|getPreConverter
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|,
operator|(
operator|(
name|DefaultValueEncryptor
operator|)
name|t1
operator|)
operator|.
name|getPostConverter
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|t2_cb
init|=
name|t2
operator|.
name|getAttribute
argument_list|(
literal|"CRYPTO_BYTES"
argument_list|)
decl_stmt|;
name|ValueEncryptor
name|t2
init|=
name|f
operator|.
name|createEncryptor
argument_list|(
name|t2_cb
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|t2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|t2
operator|instanceof
name|DefaultValueEncryptor
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|,
operator|(
operator|(
name|DefaultValueEncryptor
operator|)
name|t2
operator|)
operator|.
name|getPreConverter
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|,
operator|(
operator|(
name|DefaultValueEncryptor
operator|)
name|t2
operator|)
operator|.
name|getPostConverter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateDecryptor
parameter_list|()
block|{
name|DbAttribute
name|t1_ct
init|=
name|t1
operator|.
name|getAttribute
argument_list|(
literal|"CRYPTO_STRING"
argument_list|)
decl_stmt|;
name|ValueDecryptor
name|t1
init|=
name|f
operator|.
name|createDecryptor
argument_list|(
name|t1_ct
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|t1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|t1
operator|instanceof
name|DefaultValueDecryptor
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|,
operator|(
operator|(
name|DefaultValueDecryptor
operator|)
name|t1
operator|)
operator|.
name|getPreConverter
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Utf8StringConverter
operator|.
name|INSTANCE
argument_list|,
operator|(
operator|(
name|DefaultValueDecryptor
operator|)
name|t1
operator|)
operator|.
name|getPostConverter
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|t2_cb
init|=
name|t2
operator|.
name|getAttribute
argument_list|(
literal|"CRYPTO_BYTES"
argument_list|)
decl_stmt|;
name|ValueDecryptor
name|t2
init|=
name|f
operator|.
name|createDecryptor
argument_list|(
name|t2_cb
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|t2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|t2
operator|instanceof
name|DefaultValueDecryptor
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|,
operator|(
operator|(
name|DefaultValueDecryptor
operator|)
name|t2
operator|)
operator|.
name|getPreConverter
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|,
operator|(
operator|(
name|DefaultValueDecryptor
operator|)
name|t2
operator|)
operator|.
name|getPostConverter
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|t3_cb
init|=
name|t3
operator|.
name|getAttribute
argument_list|(
literal|"CRYPTO_BYTES"
argument_list|)
decl_stmt|;
name|ValueDecryptor
name|t3
init|=
name|f
operator|.
name|createDecryptor
argument_list|(
name|t3_cb
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|t3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|t3
operator|instanceof
name|DefaultValueDecryptor
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|,
operator|(
operator|(
name|DefaultValueDecryptor
operator|)
name|t3
operator|)
operator|.
name|getPreConverter
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|Utf8StringConverter
operator|.
name|INSTANCE
argument_list|,
operator|(
operator|(
name|DefaultValueDecryptor
operator|)
name|t3
operator|)
operator|.
name|getPostConverter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncryptor
parameter_list|()
block|{
name|DbAttribute
name|t1_ct
init|=
name|t1
operator|.
name|getAttribute
argument_list|(
literal|"CRYPTO_STRING"
argument_list|)
decl_stmt|;
name|ValueEncryptor
name|t1
init|=
name|f
operator|.
name|encryptor
argument_list|(
name|t1_ct
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|t1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|t1
argument_list|,
name|f
operator|.
name|encryptor
argument_list|(
name|t1_ct
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|t1
argument_list|,
name|f
operator|.
name|encryptor
argument_list|(
name|t1_ct
argument_list|)
argument_list|)
expr_stmt|;
name|DbAttribute
name|t2_cb
init|=
name|t2
operator|.
name|getAttribute
argument_list|(
literal|"CRYPTO_BYTES"
argument_list|)
decl_stmt|;
name|ValueEncryptor
name|t2
init|=
name|f
operator|.
name|encryptor
argument_list|(
name|t2_cb
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|t2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|t2
argument_list|,
name|f
operator|.
name|encryptor
argument_list|(
name|t2_cb
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|t2
argument_list|,
name|f
operator|.
name|encryptor
argument_list|(
name|t2_cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDecryptor
parameter_list|()
block|{
name|DbAttribute
name|t1_ct
init|=
name|t1
operator|.
name|getAttribute
argument_list|(
literal|"CRYPTO_STRING"
argument_list|)
decl_stmt|;
name|ValueDecryptor
name|t1
init|=
name|f
operator|.
name|decryptor
argument_list|(
name|t1_ct
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|t1
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|t1
argument_list|,
name|f
operator|.
name|decryptor
argument_list|(
name|t1_ct
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|t1
argument_list|,
name|f
operator|.
name|decryptor
argument_list|(
name|t1_ct
argument_list|)
argument_list|)
expr_stmt|;
name|DbAttribute
name|t2_cb
init|=
name|t2
operator|.
name|getAttribute
argument_list|(
literal|"CRYPTO_BYTES"
argument_list|)
decl_stmt|;
name|ValueDecryptor
name|t2
init|=
name|f
operator|.
name|decryptor
argument_list|(
name|t2_cb
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|t2
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|t2
argument_list|,
name|f
operator|.
name|decryptor
argument_list|(
name|t2_cb
argument_list|)
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|t2
argument_list|,
name|f
operator|.
name|decryptor
argument_list|(
name|t2_cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|getDefaultDbConverters
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|dbToBytes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|dbToBytes
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|BINARY
argument_list|)
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|dbToBytes
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|BLOB
argument_list|)
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|dbToBytes
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|VARBINARY
argument_list|)
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|dbToBytes
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|LONGVARBINARY
argument_list|)
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|dbToBytes
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|CHAR
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|dbToBytes
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|NCHAR
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|dbToBytes
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|CLOB
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|dbToBytes
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|NCLOB
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|dbToBytes
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|LONGVARCHAR
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|dbToBytes
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|LONGNVARCHAR
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|dbToBytes
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|dbToBytes
operator|.
name|put
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|Types
operator|.
name|NVARCHAR
argument_list|)
argument_list|,
name|Base64StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
return|return
name|dbToBytes
return|;
block|}
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|getDefaultObjectConverters
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|BytesConverter
argument_list|<
name|?
argument_list|>
argument_list|>
name|objectToBytes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
literal|"byte[]"
argument_list|,
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|Utf8StringConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|Double
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|DoubleConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|Double
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|,
name|DoubleConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|Float
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|FloatConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|Float
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|,
name|FloatConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|Long
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|LongConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|Long
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|,
name|LongConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|IntegerConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|,
name|IntegerConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|Short
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ShortConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|Short
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|,
name|ShortConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|Byte
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ByteConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|Byte
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|,
name|ByteConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|Boolean
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|BooleanConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|Boolean
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|,
name|BooleanConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|Date
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|UtilDateConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|BigInteger
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|BigIntegerConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|objectToBytes
operator|.
name|put
argument_list|(
name|BigDecimal
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|BigDecimalConverter
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
return|return
name|objectToBytes
return|;
block|}
block|}
end_class

end_unit

