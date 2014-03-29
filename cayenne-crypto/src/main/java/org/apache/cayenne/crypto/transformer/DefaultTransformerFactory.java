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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|jdbc
operator|.
name|ColumnDescriptor
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
name|translator
operator|.
name|batch
operator|.
name|BatchParameterBinding
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
name|CipherFactory
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
name|map
operator|.
name|ColumnMapper
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
name|ValueTransformer
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
name|ValueTransformerFactory
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbAttribute
import|;
end_import

begin_comment
comment|/**  * @since 3.2  */
end_comment

begin_class
specifier|public
class|class
name|DefaultTransformerFactory
implements|implements
name|TransformerFactory
block|{
specifier|private
name|CipherFactory
name|cipherFactory
decl_stmt|;
specifier|private
name|ColumnMapper
name|columnMapper
decl_stmt|;
specifier|private
name|ValueTransformerFactory
name|transformerFactory
decl_stmt|;
specifier|public
name|DefaultTransformerFactory
parameter_list|(
annotation|@
name|Inject
name|ColumnMapper
name|columnMapper
parameter_list|,
annotation|@
name|Inject
name|ValueTransformerFactory
name|transformerFactory
parameter_list|,
annotation|@
name|Inject
name|CipherFactory
name|cipherFactory
parameter_list|)
block|{
name|this
operator|.
name|columnMapper
operator|=
name|columnMapper
expr_stmt|;
name|this
operator|.
name|transformerFactory
operator|=
name|transformerFactory
expr_stmt|;
name|this
operator|.
name|cipherFactory
operator|=
name|cipherFactory
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|MapTransformer
name|decryptor
parameter_list|(
name|ColumnDescriptor
index|[]
name|columns
parameter_list|,
name|Object
name|sampleRow
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|sampleRow
operator|instanceof
name|Map
operator|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|len
init|=
name|columns
operator|.
name|length
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|cryptoColumns
init|=
literal|null
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
name|DbAttribute
name|a
init|=
name|columns
index|[
name|i
index|]
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|a
operator|!=
literal|null
operator|&&
name|columnMapper
operator|.
name|isEncrypted
argument_list|(
name|a
argument_list|)
condition|)
block|{
if|if
condition|(
name|cryptoColumns
operator|==
literal|null
condition|)
block|{
name|cryptoColumns
operator|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|len
operator|-
name|i
argument_list|)
expr_stmt|;
block|}
name|cryptoColumns
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cryptoColumns
operator|!=
literal|null
condition|)
block|{
name|int
name|dlen
init|=
name|cryptoColumns
operator|.
name|size
argument_list|()
decl_stmt|;
name|String
index|[]
name|keys
init|=
operator|new
name|String
index|[
name|dlen
index|]
decl_stmt|;
name|ValueTransformer
index|[]
name|transformers
init|=
operator|new
name|ValueTransformer
index|[
name|dlen
index|]
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
name|dlen
condition|;
name|i
operator|++
control|)
block|{
name|ColumnDescriptor
name|cd
init|=
name|columns
index|[
name|cryptoColumns
operator|.
name|get
argument_list|(
name|i
argument_list|)
index|]
decl_stmt|;
name|keys
index|[
name|i
index|]
operator|=
name|cd
operator|.
name|getDataRowKey
argument_list|()
expr_stmt|;
name|transformers
index|[
name|i
index|]
operator|=
name|transformerFactory
operator|.
name|decryptor
argument_list|(
name|cd
operator|.
name|getAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|DefaultMapTransformer
argument_list|(
name|keys
argument_list|,
name|transformers
argument_list|,
name|cipherFactory
operator|.
name|cipher
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|BindingsTransformer
name|encryptor
parameter_list|(
name|BatchParameterBinding
index|[]
name|bindings
parameter_list|)
block|{
name|int
name|len
init|=
name|bindings
operator|.
name|length
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|cryptoColumns
init|=
literal|null
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
name|DbAttribute
name|a
init|=
name|bindings
index|[
name|i
index|]
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|columnMapper
operator|.
name|isEncrypted
argument_list|(
name|a
argument_list|)
condition|)
block|{
if|if
condition|(
name|cryptoColumns
operator|==
literal|null
condition|)
block|{
name|cryptoColumns
operator|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|(
name|len
operator|-
name|i
argument_list|)
expr_stmt|;
block|}
name|cryptoColumns
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cryptoColumns
operator|!=
literal|null
condition|)
block|{
name|int
name|dlen
init|=
name|cryptoColumns
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
index|[]
name|positions
init|=
operator|new
name|int
index|[
name|dlen
index|]
decl_stmt|;
name|ValueTransformer
index|[]
name|transformers
init|=
operator|new
name|ValueTransformer
index|[
name|dlen
index|]
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
name|dlen
condition|;
name|i
operator|++
control|)
block|{
name|int
name|pos
init|=
name|cryptoColumns
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|BatchParameterBinding
name|b
init|=
name|bindings
index|[
name|pos
index|]
decl_stmt|;
name|positions
index|[
name|i
index|]
operator|=
name|pos
expr_stmt|;
name|transformers
index|[
name|i
index|]
operator|=
name|transformerFactory
operator|.
name|encryptor
argument_list|(
name|b
operator|.
name|getAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|DefaultBindingsTransformer
argument_list|(
name|positions
argument_list|,
name|transformers
argument_list|,
name|cipherFactory
operator|.
name|cipher
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

