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
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|security
operator|.
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStoreException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|CertificateException
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
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|Inject
import|;
end_import

begin_comment
comment|/**  * A {@link KeySource} based on a JDK KeyStore. DI properties are used to locate  * the KeyStore and keys within it.  *   * @since 3.2  */
end_comment

begin_class
specifier|public
class|class
name|KeyStoreKeySource
implements|implements
name|KeySource
block|{
specifier|private
name|KeyStore
name|keyStore
decl_stmt|;
specifier|private
name|char
index|[]
name|keyPasswordChars
decl_stmt|;
specifier|public
name|KeyStoreKeySource
parameter_list|(
annotation|@
name|Inject
argument_list|(
name|CryptoConstants
operator|.
name|PROPERTIES_MAP
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
throws|throws
name|KeyStoreException
block|{
name|String
name|keyStoreUrl
init|=
name|properties
operator|.
name|get
argument_list|(
name|CryptoConstants
operator|.
name|KEYSTORE_URL
argument_list|)
decl_stmt|;
if|if
condition|(
name|keyStoreUrl
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneCryptoException
argument_list|(
literal|"KeyStore URL is not set. Property name: "
operator|+
name|CryptoConstants
operator|.
name|KEYSTORE_URL
argument_list|)
throw|;
block|}
name|String
name|keyStorePassword
init|=
name|properties
operator|.
name|get
argument_list|(
name|CryptoConstants
operator|.
name|KEYSTORE_PASSWORD
argument_list|)
decl_stmt|;
comment|// NULL password is valid, though not secure .. so no NULL validation
name|String
name|keyPassword
init|=
name|properties
operator|.
name|get
argument_list|(
name|CryptoConstants
operator|.
name|KEY_PASSWORD
argument_list|)
decl_stmt|;
name|this
operator|.
name|keyPasswordChars
operator|=
name|keyPassword
operator|!=
literal|null
condition|?
name|keyPassword
operator|.
name|toCharArray
argument_list|()
else|:
literal|null
expr_stmt|;
comment|// NULL password is valid, though not secure .. so no NULL validation
try|try
block|{
name|this
operator|.
name|keyStore
operator|=
name|createKeyStore
argument_list|(
name|keyStoreUrl
argument_list|,
name|keyStorePassword
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneCryptoException
argument_list|(
literal|"Error loading keystore at "
operator|+
name|keyStoreUrl
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|KeyStore
name|createKeyStore
parameter_list|(
name|String
name|keyStoreUrl
parameter_list|,
name|String
name|keyStorePassword
parameter_list|)
throws|throws
name|KeyStoreException
throws|,
name|IOException
throws|,
name|NoSuchAlgorithmException
throws|,
name|CertificateException
block|{
name|char
index|[]
name|keyStorePasswordChars
init|=
name|keyStorePassword
operator|!=
literal|null
condition|?
name|keyStorePassword
operator|.
name|toCharArray
argument_list|()
else|:
literal|null
decl_stmt|;
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|KeyStore
operator|.
name|getDefaultType
argument_list|()
argument_list|)
decl_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|keyStoreUrl
argument_list|)
decl_stmt|;
name|InputStream
name|in
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
try|try
block|{
name|keyStore
operator|.
name|load
argument_list|(
name|in
argument_list|,
name|keyStorePasswordChars
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|keyStore
return|;
block|}
annotation|@
name|Override
specifier|public
name|Key
name|getKey
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
try|try
block|{
return|return
name|keyStore
operator|.
name|getKey
argument_list|(
name|alias
argument_list|,
name|keyPasswordChars
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneCryptoException
argument_list|(
literal|"Error accessing key for alias: "
operator|+
name|alias
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

