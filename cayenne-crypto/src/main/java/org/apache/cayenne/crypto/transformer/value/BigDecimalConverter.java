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

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_comment
comment|/**  * Converts between {@link java.math.BigDecimal} and byte[]  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|BigDecimalConverter
implements|implements
name|BytesConverter
argument_list|<
name|BigDecimal
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|BytesConverter
argument_list|<
name|BigDecimal
argument_list|>
name|INSTANCE
init|=
operator|new
name|BigDecimalConverter
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|INTEGER_BYTES
init|=
name|Integer
operator|.
name|SIZE
operator|/
name|Byte
operator|.
name|SIZE
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|MIN_BYTES
init|=
name|INTEGER_BYTES
operator|+
literal|1
decl_stmt|;
specifier|static
name|BigDecimal
name|getBigDecimal
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|byte
index|[]
name|unscaledBytes
decl_stmt|,
name|scaleBytes
decl_stmt|;
if|if
condition|(
name|bytes
operator|.
name|length
operator|<
name|MIN_BYTES
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"byte[] is too small for a BigDecimal value: "
operator|+
name|bytes
operator|.
name|length
argument_list|)
throw|;
block|}
name|scaleBytes
operator|=
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|INTEGER_BYTES
argument_list|)
expr_stmt|;
name|unscaledBytes
operator|=
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|bytes
argument_list|,
name|INTEGER_BYTES
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
operator|new
name|BigDecimal
argument_list|(
operator|new
name|BigInteger
argument_list|(
name|unscaledBytes
argument_list|)
argument_list|,
name|IntegerConverter
operator|.
name|getInt
argument_list|(
name|scaleBytes
argument_list|)
argument_list|)
return|;
block|}
specifier|static
name|byte
index|[]
name|getBytes
parameter_list|(
name|BigDecimal
name|bigDecimal
parameter_list|)
block|{
name|byte
index|[]
name|result
decl_stmt|,
name|unscaledBytes
decl_stmt|,
name|scaleBytes
decl_stmt|;
name|unscaledBytes
operator|=
name|bigDecimal
operator|.
name|unscaledValue
argument_list|()
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
name|scaleBytes
operator|=
name|IntegerConverter
operator|.
name|getBytes
argument_list|(
name|bigDecimal
operator|.
name|scale
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|=
operator|new
name|byte
index|[
name|INTEGER_BYTES
operator|+
name|unscaledBytes
operator|.
name|length
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|scaleBytes
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|INTEGER_BYTES
operator|-
name|scaleBytes
operator|.
name|length
argument_list|,
name|scaleBytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|unscaledBytes
argument_list|,
literal|0
argument_list|,
name|result
argument_list|,
name|INTEGER_BYTES
argument_list|,
name|unscaledBytes
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|BigDecimal
name|fromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|getBigDecimal
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
name|BigDecimal
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

