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
name|access
operator|.
name|translator
operator|.
name|DbAttributeBinding
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
name|access
operator|.
name|types
operator|.
name|ExtendedType
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
name|access
operator|.
name|types
operator|.
name|ExtendedTypeMap
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
name|value
operator|.
name|ValueEncryptor
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DefaultBindingsTransformer
implements|implements
name|BindingsTransformer
block|{
specifier|private
name|int
index|[]
name|positions
decl_stmt|;
specifier|private
name|ValueEncryptor
index|[]
name|transformers
decl_stmt|;
specifier|private
name|BytesEncryptor
name|encryptor
decl_stmt|;
specifier|private
name|ExtendedTypeMap
name|extendedTypeMap
decl_stmt|;
specifier|public
name|DefaultBindingsTransformer
parameter_list|(
name|int
index|[]
name|positions
parameter_list|,
name|ValueEncryptor
index|[]
name|transformers
parameter_list|,
name|BytesEncryptor
name|encryptor
parameter_list|,
name|ExtendedTypeMap
name|extendedTypeMap
parameter_list|)
block|{
name|this
operator|.
name|positions
operator|=
name|positions
expr_stmt|;
name|this
operator|.
name|transformers
operator|=
name|transformers
expr_stmt|;
name|this
operator|.
name|encryptor
operator|=
name|encryptor
expr_stmt|;
name|this
operator|.
name|extendedTypeMap
operator|=
name|extendedTypeMap
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|transform
parameter_list|(
name|DbAttributeBinding
index|[]
name|bindings
parameter_list|)
block|{
name|int
name|len
init|=
name|positions
operator|.
name|length
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
name|len
condition|;
name|i
operator|++
control|)
block|{
name|DbAttributeBinding
name|b
init|=
name|bindings
index|[
name|positions
index|[
name|i
index|]
index|]
decl_stmt|;
name|Object
name|transformed
init|=
name|transformers
index|[
name|i
index|]
operator|.
name|encrypt
argument_list|(
name|encryptor
argument_list|,
name|b
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|b
operator|.
name|setValue
argument_list|(
name|transformed
argument_list|)
expr_stmt|;
name|ExtendedType
name|extendedType
init|=
name|transformed
operator|!=
literal|null
condition|?
name|extendedTypeMap
operator|.
name|getRegisteredType
argument_list|(
name|transformed
operator|.
name|getClass
argument_list|()
argument_list|)
else|:
name|extendedTypeMap
operator|.
name|getDefaultType
argument_list|()
decl_stmt|;
name|b
operator|.
name|setExtendedType
argument_list|(
name|extendedType
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

