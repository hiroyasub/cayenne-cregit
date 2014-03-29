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
import|import
name|javax
operator|.
name|crypto
operator|.
name|Cipher
import|;
end_import

begin_comment
comment|/**  * @since 3.2  */
end_comment

begin_class
specifier|public
class|class
name|JceValueEncryptor
implements|implements
name|ValueTransformer
block|{
specifier|public
name|JceValueEncryptor
parameter_list|(
name|ToBytesConverter
name|toBytes
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|Object
name|transform
parameter_list|(
name|Cipher
name|cipher
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

