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
name|unit
package|;
end_package

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
name|BytesDecryptor
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
name|transformer
operator|.
name|bytes
operator|.
name|BytesEncryptor
import|;
end_import

begin_comment
comment|/**  * A fake "cipher" used for unit tests that does simple bytes swapping.  */
end_comment

begin_class
specifier|public
class|class
name|SwapBytesTransformer
implements|implements
name|BytesEncryptor
implements|,
name|BytesDecryptor
block|{
specifier|private
specifier|static
specifier|final
name|SwapBytesTransformer
name|instance
init|=
operator|new
name|SwapBytesTransformer
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|BytesEncryptor
name|encryptor
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
specifier|public
specifier|static
name|BytesDecryptor
name|decryptor
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
specifier|private
name|SwapBytesTransformer
parameter_list|()
block|{
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|decrypt
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|int
name|inputOffset
parameter_list|,
name|Key
name|key
parameter_list|)
block|{
name|byte
index|[]
name|output
init|=
operator|new
name|byte
index|[
name|input
operator|.
name|length
operator|-
name|inputOffset
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|input
argument_list|,
name|inputOffset
argument_list|,
name|output
argument_list|,
literal|0
argument_list|,
name|input
operator|.
name|length
operator|-
name|inputOffset
argument_list|)
expr_stmt|;
name|swap
argument_list|(
name|output
argument_list|,
literal|0
argument_list|,
name|output
operator|.
name|length
operator|-
literal|1
argument_list|)
expr_stmt|;
return|return
name|output
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|encrypt
parameter_list|(
name|byte
index|[]
name|input
parameter_list|,
name|int
name|outputOffset
parameter_list|,
name|byte
index|[]
name|flags
parameter_list|)
block|{
name|byte
index|[]
name|output
init|=
operator|new
name|byte
index|[
name|input
operator|.
name|length
operator|+
name|outputOffset
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|input
argument_list|,
literal|0
argument_list|,
name|output
argument_list|,
name|outputOffset
argument_list|,
name|input
operator|.
name|length
argument_list|)
expr_stmt|;
name|swap
argument_list|(
name|output
argument_list|,
name|outputOffset
argument_list|,
name|outputOffset
operator|+
name|input
operator|.
name|length
operator|-
literal|1
argument_list|)
expr_stmt|;
return|return
name|output
return|;
block|}
specifier|private
name|void
name|swap
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
block|{
if|if
condition|(
name|start
operator|>=
name|end
condition|)
block|{
return|return;
block|}
name|byte
name|b
init|=
name|buffer
index|[
name|end
index|]
decl_stmt|;
name|buffer
index|[
name|end
index|]
operator|=
name|buffer
index|[
name|start
index|]
expr_stmt|;
name|buffer
index|[
name|start
index|]
operator|=
name|b
expr_stmt|;
name|swap
argument_list|(
name|buffer
argument_list|,
operator|++
name|start
argument_list|,
operator|--
name|end
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

