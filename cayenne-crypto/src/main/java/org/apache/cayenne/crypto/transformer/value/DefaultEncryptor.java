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
name|bytes
operator|.
name|BytesEncryptor
import|;
end_import

begin_comment
comment|/**  * @since 3.2  */
end_comment

begin_class
class|class
name|DefaultEncryptor
implements|implements
name|ValueEncryptor
block|{
specifier|private
name|BytesConverter
name|preConverter
decl_stmt|;
specifier|private
name|BytesConverter
name|postConverter
decl_stmt|;
specifier|public
name|DefaultEncryptor
parameter_list|(
name|BytesConverter
name|preConverter
parameter_list|,
name|BytesConverter
name|postConverter
parameter_list|)
block|{
name|this
operator|.
name|preConverter
operator|=
name|preConverter
expr_stmt|;
name|this
operator|.
name|postConverter
operator|=
name|postConverter
expr_stmt|;
block|}
name|BytesConverter
name|getPreConverter
parameter_list|()
block|{
return|return
name|preConverter
return|;
block|}
name|BytesConverter
name|getPostConverter
parameter_list|()
block|{
return|return
name|postConverter
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|encrypt
parameter_list|(
name|BytesEncryptor
name|encryptor
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
comment|// TODO: should we encrypt nulls as well to hide NULL from attackers?
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
name|byte
index|[]
name|bytes
init|=
name|preConverter
operator|.
name|toBytes
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|byte
index|[]
name|transformed
init|=
name|encryptor
operator|.
name|encrypt
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|)
decl_stmt|;
return|return
name|postConverter
operator|.
name|fromBytes
argument_list|(
name|transformed
argument_list|)
return|;
block|}
block|}
end_class

end_unit

