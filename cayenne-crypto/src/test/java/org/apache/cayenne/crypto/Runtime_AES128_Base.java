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
name|sql
operator|.
name|SQLException
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
name|table1
decl_stmt|;
specifier|protected
name|TableHelper
name|table2
decl_stmt|;
specifier|protected
name|TableHelper
name|table4
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|(
name|boolean
name|compress
parameter_list|,
name|boolean
name|useHMAC
parameter_list|)
throws|throws
name|Exception
block|{
name|Module
name|crypto
init|=
name|createCryptoModule
argument_list|(
name|compress
argument_list|,
name|useHMAC
argument_list|)
decl_stmt|;
name|this
operator|.
name|runtime
operator|=
name|createRuntime
argument_list|(
name|crypto
argument_list|)
expr_stmt|;
name|setupTestTables
argument_list|(
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
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setupTestTables
parameter_list|(
name|DBHelper
name|dbHelper
parameter_list|)
throws|throws
name|SQLException
block|{
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
argument_list|,
literal|"PLAIN_INT"
argument_list|,
literal|"CRYPTO_INT"
argument_list|)
expr_stmt|;
name|table1
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|this
operator|.
name|table4
operator|=
operator|new
name|TableHelper
argument_list|(
name|dbHelper
argument_list|,
literal|"TABLE4"
argument_list|)
operator|.
name|setColumns
argument_list|(
literal|"ID"
argument_list|,
literal|"PLAIN_STRING"
argument_list|,
literal|"PLAIN_INT"
argument_list|)
expr_stmt|;
name|table4
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|ServerRuntime
name|createRuntime
parameter_list|(
name|Module
name|crypto
parameter_list|)
block|{
return|return
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
name|addModule
argument_list|(
name|crypto
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
specifier|protected
name|Module
name|createCryptoModule
parameter_list|(
name|boolean
name|compress
parameter_list|,
name|boolean
name|useHMAC
parameter_list|)
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
name|CryptoModuleExtender
name|builder
init|=
name|CryptoModule
operator|.
name|extend
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
if|if
condition|(
name|useHMAC
condition|)
block|{
name|builder
operator|.
name|useHMAC
argument_list|()
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|module
argument_list|()
return|;
block|}
block|}
end_class

end_unit

