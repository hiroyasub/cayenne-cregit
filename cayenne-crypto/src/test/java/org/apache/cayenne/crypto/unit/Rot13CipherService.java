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
name|unit
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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
name|cipher
operator|.
name|CryptoHandler
import|;
end_import

begin_class
specifier|public
class|class
name|Rot13CipherService
implements|implements
name|CryptoHandler
block|{
specifier|public
specifier|static
name|String
name|rotate
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|length
init|=
name|value
operator|.
name|length
argument_list|()
decl_stmt|;
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|length
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|value
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// If c is a letter, rotate it by 13. Numbers/symbols are untouched.
if|if
condition|(
operator|(
name|c
operator|>=
literal|'a'
operator|&&
name|c
operator|<=
literal|'m'
operator|)
operator|||
operator|(
name|c
operator|>=
literal|'A'
operator|&&
name|c
operator|<=
literal|'M'
operator|)
condition|)
name|c
operator|+=
literal|13
expr_stmt|;
comment|// The first half of the alphabet goes forward 13
comment|// letters
if|else if
condition|(
operator|(
name|c
operator|>=
literal|'n'
operator|&&
name|c
operator|<=
literal|'z'
operator|)
operator|||
operator|(
name|c
operator|>=
literal|'A'
operator|&&
name|c
operator|<=
literal|'Z'
operator|)
condition|)
name|c
operator|-=
literal|13
expr_stmt|;
comment|// The last half of the alphabet goes backward 13
comment|// letters
name|result
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|byte
index|[]
name|rotate
parameter_list|(
name|byte
index|[]
name|value
parameter_list|)
block|{
try|try
block|{
name|String
name|valueString
init|=
operator|new
name|String
argument_list|(
name|value
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
return|return
name|rotate
argument_list|(
name|valueString
argument_list|)
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Can't convert between bytes and String"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Object
name|decrypt
parameter_list|(
name|Object
name|ciphertext
parameter_list|,
name|int
name|jdbcType
parameter_list|)
block|{
return|return
name|rotate
argument_list|(
name|ciphertext
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|encrypt
parameter_list|(
name|Object
name|plaintext
parameter_list|,
name|int
name|jdbcType
parameter_list|)
block|{
return|return
name|rotate
argument_list|(
name|plaintext
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

