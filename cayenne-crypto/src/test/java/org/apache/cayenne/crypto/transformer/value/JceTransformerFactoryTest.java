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
operator|.
name|transformer
operator|.
name|value
package|;
end_package

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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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

begin_class
specifier|public
class|class
name|JceTransformerFactoryTest
extends|extends
name|TestCase
block|{
specifier|private
name|DbEntity
name|t1
decl_stmt|;
specifier|private
name|DbEntity
name|t2
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
name|ServerRuntime
name|runtime
init|=
operator|new
name|ServerRuntime
argument_list|(
literal|"cayenne-crypto.xml"
argument_list|)
decl_stmt|;
name|this
operator|.
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
name|this
operator|.
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
block|}
specifier|public
name|void
name|testGetJavaType
parameter_list|()
block|{
name|JceTransformerFactory
name|f
init|=
operator|new
name|JceTransformerFactory
argument_list|()
decl_stmt|;
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
specifier|public
name|void
name|testCreateEncryptor
parameter_list|()
block|{
name|JceTransformerFactory
name|f
init|=
operator|new
name|JceTransformerFactory
argument_list|()
decl_stmt|;
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
name|ValueTransformer
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
name|JceValueEncryptor
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|BytesToBytesConverter
operator|.
name|INSTANCE
argument_list|,
operator|(
operator|(
name|JceValueEncryptor
operator|)
name|t1
operator|)
operator|.
name|toBytes
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
name|ValueTransformer
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
name|JceValueEncryptor
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
name|JceValueEncryptor
operator|)
name|t2
operator|)
operator|.
name|toBytes
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

