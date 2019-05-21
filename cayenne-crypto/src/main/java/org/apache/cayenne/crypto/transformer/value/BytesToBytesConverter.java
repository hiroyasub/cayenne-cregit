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
operator|.
name|transformer
operator|.
name|value
package|;
end_package

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_comment
comment|// we can't use<byte[]> , so parameterizing this converter to Object
end_comment

begin_class
specifier|public
class|class
name|BytesToBytesConverter
implements|implements
name|BytesConverter
argument_list|<
name|Object
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|BytesConverter
argument_list|<
name|Object
argument_list|>
name|INSTANCE
init|=
operator|new
name|BytesToBytesConverter
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|toBytes
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
operator|(
name|byte
index|[]
operator|)
name|value
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|fromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|bytes
return|;
block|}
block|}
end_class

end_unit

