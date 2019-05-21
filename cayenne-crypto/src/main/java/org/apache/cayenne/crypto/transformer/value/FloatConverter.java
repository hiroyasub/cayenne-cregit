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
comment|/**  * Converts between float and byte[]  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|FloatConverter
implements|implements
name|BytesConverter
argument_list|<
name|Float
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|BytesConverter
argument_list|<
name|Float
argument_list|>
name|INSTANCE
init|=
operator|new
name|FloatConverter
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|BYTES
init|=
literal|4
decl_stmt|;
specifier|static
name|float
name|getFloat
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
if|if
condition|(
name|bytes
operator|.
name|length
operator|>
name|BYTES
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"byte[] is too large for a single float value: "
operator|+
name|bytes
operator|.
name|length
argument_list|)
throw|;
block|}
return|return
name|Float
operator|.
name|intBitsToFloat
argument_list|(
name|IntegerConverter
operator|.
name|getInt
argument_list|(
name|bytes
argument_list|)
argument_list|)
return|;
block|}
specifier|static
name|byte
index|[]
name|getBytes
parameter_list|(
name|float
name|f
parameter_list|)
block|{
return|return
name|IntegerConverter
operator|.
name|getBytes
argument_list|(
name|Float
operator|.
name|floatToRawIntBits
argument_list|(
name|f
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Float
name|fromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|getFloat
argument_list|(
name|bytes
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|toBytes
parameter_list|(
name|Float
name|value
parameter_list|)
block|{
return|return
name|getBytes
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

