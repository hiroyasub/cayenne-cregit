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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
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
comment|/**  * A {@link KeySource} based on a Java "jceks" KeyStore. Uses  * {@link CryptoConstants#KEYSTORE_URL} to locate the keystore and  * {@link CryptoConstants#KEY_PASSWORD} to read the secret key.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|JceksKeySource
implements|implements
name|KeySource
block|{
comment|// this is the only standard keystore type that supports storing secret keys
specifier|private
specifier|static
specifier|final
name|String
name|JCEKS_KEYSTORE_TYPE
init|=
literal|"jceks"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Key
name|NULL_KEY
init|=
operator|new
name|Key
argument_list|()
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|4755682444381893880L
decl_stmt|;
annotation|@
name|Override
specifier|public
name|String
name|getFormat
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getEncoded
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getAlgorithm
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
decl_stmt|;
specifier|private
name|KeyStore
name|keyStore
decl_stmt|;
specifier|private
name|char
index|[]
name|keyPassword
decl_stmt|;
specifier|private
name|String
name|defaultKeyAlias
decl_stmt|;
comment|// caching the keys may not be a good idea for security reasons, but
comment|// re-reading the key from KeyStore for every select row creates a huge
comment|// bottleneck... And considering we are caching keystore password, it
comment|// probably doesn't make things that much worse
specifier|private
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|Key
argument_list|>
name|keyCache
decl_stmt|;
specifier|public
name|JceksKeySource
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
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|CryptoConstants
operator|.
name|CREDENTIALS_MAP
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|char
index|[]
argument_list|>
name|credentials
parameter_list|)
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
name|this
operator|.
name|keyPassword
operator|=
name|credentials
operator|.
name|get
argument_list|(
name|CryptoConstants
operator|.
name|KEY_PASSWORD
argument_list|)
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
name|this
operator|.
name|defaultKeyAlias
operator|=
name|properties
operator|.
name|get
argument_list|(
name|CryptoConstants
operator|.
name|ENCRYPTION_KEY_ALIAS
argument_list|)
expr_stmt|;
if|if
condition|(
name|defaultKeyAlias
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneCryptoException
argument_list|(
literal|"Default key alias is not set. Property name: "
operator|+
name|CryptoConstants
operator|.
name|ENCRYPTION_KEY_ALIAS
argument_list|)
throw|;
block|}
name|this
operator|.
name|keyCache
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Key
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|private
name|KeyStore
name|createKeyStore
parameter_list|(
name|String
name|keyStoreUrl
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
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|JCEKS_KEYSTORE_TYPE
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
literal|null
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
name|Key
name|key
init|=
name|keyCache
operator|.
name|get
argument_list|(
name|alias
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
name|Key
name|newKey
init|=
name|createKey
argument_list|(
name|alias
argument_list|)
decl_stmt|;
name|Key
name|oldKey
init|=
name|keyCache
operator|.
name|putIfAbsent
argument_list|(
name|alias
argument_list|,
name|newKey
argument_list|)
decl_stmt|;
name|key
operator|=
name|oldKey
operator|!=
literal|null
condition|?
name|oldKey
else|:
name|newKey
expr_stmt|;
block|}
return|return
name|key
operator|==
name|NULL_KEY
condition|?
literal|null
else|:
name|key
return|;
block|}
specifier|protected
name|Key
name|createKey
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
try|try
block|{
name|Key
name|key
init|=
name|keyStore
operator|.
name|getKey
argument_list|(
name|alias
argument_list|,
name|keyPassword
argument_list|)
decl_stmt|;
return|return
name|key
operator|!=
literal|null
condition|?
name|key
else|:
name|NULL_KEY
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
annotation|@
name|Override
specifier|public
name|String
name|getDefaultKeyAlias
parameter_list|()
block|{
return|return
name|defaultKeyAlias
return|;
block|}
block|}
end_class

end_unit

