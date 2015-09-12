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
name|key
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
name|assertNull
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Key
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
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|crypto
operator|.
name|CayenneCryptoException
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
name|CryptoConstants
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
name|JceksKeySourceTest
block|{
specifier|public
specifier|static
specifier|final
name|char
index|[]
name|TEST_KEY_PASS
init|=
literal|"testkeypass"
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|KS1_JCEKS
init|=
literal|"ks1.jceks"
decl_stmt|;
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CayenneCryptoException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testConstructor_NoUrl
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|char
index|[]
argument_list|>
name|creds
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
operator|new
name|JceksKeySource
argument_list|(
name|props
argument_list|,
name|creds
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetKey_JCEKS_DES
parameter_list|()
block|{
name|URL
name|url
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
name|KS1_JCEKS
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|CryptoConstants
operator|.
name|KEYSTORE_URL
argument_list|,
name|url
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|CryptoConstants
operator|.
name|ENCRYPTION_KEY_ALIAS
argument_list|,
literal|"k2"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|char
index|[]
argument_list|>
name|creds
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|creds
operator|.
name|put
argument_list|(
name|CryptoConstants
operator|.
name|KEY_PASSWORD
argument_list|,
name|TEST_KEY_PASS
argument_list|)
expr_stmt|;
name|JceksKeySource
name|ks
init|=
operator|new
name|JceksKeySource
argument_list|(
name|props
argument_list|,
name|creds
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|ks
operator|.
name|getKey
argument_list|(
literal|"no-such-key"
argument_list|)
argument_list|)
expr_stmt|;
name|Key
name|k1
init|=
name|ks
operator|.
name|getKey
argument_list|(
literal|"k1"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|k1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DES"
argument_list|,
name|k1
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|Key
name|k2
init|=
name|ks
operator|.
name|getKey
argument_list|(
literal|"k2"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|k2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DES"
argument_list|,
name|k2
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
name|Key
name|k3
init|=
name|ks
operator|.
name|getKey
argument_list|(
literal|"k3"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|k3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"AES"
argument_list|,
name|k3
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

