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
name|reader
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
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
name|jdbc
operator|.
name|RowDescriptor
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
name|reader
operator|.
name|RowReader
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
name|reader
operator|.
name|RowReaderFactory
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
name|TransformerFactory
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
name|MapTransformer
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
name|dba
operator|.
name|DbAdapter
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
name|ObjAttribute
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
name|query
operator|.
name|QueryMetadata
import|;
end_import

begin_class
specifier|public
class|class
name|CryptoRowReaderFactoryDecorator
implements|implements
name|RowReaderFactory
block|{
specifier|private
name|RowReaderFactory
name|delegate
decl_stmt|;
specifier|private
name|TransformerFactory
name|cryptoFactory
decl_stmt|;
specifier|public
name|CryptoRowReaderFactoryDecorator
parameter_list|(
annotation|@
name|Inject
name|RowReaderFactory
name|delegate
parameter_list|,
annotation|@
name|Inject
name|TransformerFactory
name|cryptoFactory
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|cryptoFactory
operator|=
name|cryptoFactory
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|RowReader
argument_list|<
name|?
argument_list|>
name|rowReader
parameter_list|(
specifier|final
name|RowDescriptor
name|descriptor
parameter_list|,
name|QueryMetadata
name|queryMetadata
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
name|Map
argument_list|<
name|ObjAttribute
argument_list|,
name|ColumnDescriptor
argument_list|>
name|attributeOverrides
parameter_list|)
block|{
specifier|final
name|RowReader
argument_list|<
name|?
argument_list|>
name|delegateReader
init|=
name|delegate
operator|.
name|rowReader
argument_list|(
name|descriptor
argument_list|,
name|queryMetadata
argument_list|,
name|adapter
argument_list|,
name|attributeOverrides
argument_list|)
decl_stmt|;
return|return
operator|new
name|RowReader
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|private
name|boolean
name|decryptorCompiled
decl_stmt|;
specifier|private
name|MapTransformer
name|decryptor
decl_stmt|;
specifier|private
name|void
name|ensureDecryptorCompiled
parameter_list|(
name|Object
name|row
parameter_list|)
block|{
if|if
condition|(
operator|!
name|decryptorCompiled
condition|)
block|{
name|decryptor
operator|=
name|cryptoFactory
operator|.
name|decryptor
argument_list|(
name|descriptor
operator|.
name|getColumns
argument_list|()
argument_list|,
name|row
argument_list|)
expr_stmt|;
name|decryptorCompiled
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Object
name|readRow
parameter_list|(
name|ResultSet
name|resultSet
parameter_list|)
block|{
name|Object
name|row
init|=
name|delegateReader
operator|.
name|readRow
argument_list|(
name|resultSet
argument_list|)
decl_stmt|;
name|ensureDecryptorCompiled
argument_list|(
name|row
argument_list|)
expr_stmt|;
if|if
condition|(
name|decryptor
operator|!=
literal|null
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|(
name|Map
operator|)
name|row
decl_stmt|;
name|decryptor
operator|.
name|transform
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
return|return
name|row
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

