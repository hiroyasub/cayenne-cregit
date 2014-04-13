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
name|JceksKeySourceTest
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

begin_class
specifier|public
class|class
name|Runtime_AES128_Base
block|{
specifier|protected
name|ServerRuntime
name|runtime
decl_stmt|;
specifier|protected
name|TableHelper
name|table2
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|(
name|boolean
name|compress
parameter_list|)
throws|throws
name|Exception
block|{
name|URL
name|keyStoreUrl
init|=
name|JceksKeySourceTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|JceksKeySourceTest
operator|.
name|KS1_JCEKS
argument_list|)
decl_stmt|;
name|CryptoModuleBuilder
name|builder
init|=
operator|new
name|CryptoModuleBuilder
argument_list|()
operator|.
name|keyStore
argument_list|(
name|keyStoreUrl
argument_list|,
name|JceksKeySourceTest
operator|.
name|TEST_KEY_PASS
argument_list|,
literal|"k3"
argument_list|)
decl_stmt|;
if|if
condition|(
name|compress
condition|)
block|{
name|builder
operator|.
name|compress
argument_list|()
expr_stmt|;
block|}
name|Module
name|crypto
init|=
name|builder
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
name|table2
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"TABLE2"
argument_list|)
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"PLAIN_BYTES"
argument_list|,
literal|"CRYPTO_BYTES"
argument_list|)
expr_stmt|;
name|table2
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

