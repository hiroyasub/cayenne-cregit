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

begin_comment
comment|/**  * A {@link ValueTransformerFactory} that creates encryptors/decryptors that are  * taking advantage of the JCE (Java Cryptography Extension) ciphers.  *   * @since 3.2  */
end_comment

begin_class
specifier|public
class|class
name|JceTransformerFactory
implements|implements
name|ValueTransformerFactory
block|{
specifier|public
name|JceTransformerFactory
parameter_list|()
block|{
comment|// TODO Auto-generated constructor stub
block|}
annotation|@
name|Override
specifier|public
name|ValueTransformer
name|decryptor
parameter_list|(
name|int
name|jdbcType
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ValueTransformer
name|encryptor
parameter_list|(
name|int
name|jdbcType
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"TODO"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

