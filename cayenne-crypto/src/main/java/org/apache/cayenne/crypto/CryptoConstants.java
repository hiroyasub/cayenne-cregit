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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|CryptoConstants
block|{
comment|/**      * An injection key for the Map<String, String> of the crypto properties.      */
name|String
name|PROPERTIES_MAP
init|=
literal|"cayenne.crypto.properties"
decl_stmt|;
comment|/**      * An injection key for the Map<String, char[]> of credentials.      */
name|String
name|CREDENTIALS_MAP
init|=
literal|"cayenne.crypto.properties"
decl_stmt|;
name|String
name|CIPHER_ALGORITHM
init|=
literal|"cayenne.crypto.cipher.algorithm"
decl_stmt|;
name|String
name|CIPHER_MODE
init|=
literal|"cayenne.crypto.cipher.mode"
decl_stmt|;
name|String
name|CIPHER_PADDING
init|=
literal|"cayenne.crypto.cipher.padding"
decl_stmt|;
comment|/**      * Defines a URL of a KeyStore. The actual format depends on the      * {@link KeySource} implementation that will be reading it. E.g. it can be      * a "jceks" Java key store.      */
name|String
name|KEYSTORE_URL
init|=
literal|"cayenne.crypto.keystore.url"
decl_stmt|;
comment|/**      * A password to access all secret keys within the keystore.      */
name|String
name|KEY_PASSWORD
init|=
literal|"cayenne.crypto.key.password"
decl_stmt|;
comment|/**      * A symbolic name of the default encryption key in the keystore.      */
name|String
name|ENCRYPTION_KEY_ALIAS
init|=
literal|"cayenne.crypto.key.enc.alias"
decl_stmt|;
comment|/**      * A property that defines whether compression is enabled. Should be "true"      * or "false". "False" is the default.      */
name|String
name|COMPRESSION
init|=
literal|"cayenne.crypto.compression"
decl_stmt|;
comment|/**      * A property that defines whether HMAC is enabled.      * Should be "true" or "false". "False" is the default.      */
name|String
name|USE_HMAC
init|=
literal|"cayenne.crypto.use_hmac"
decl_stmt|;
block|}
end_interface

end_unit

