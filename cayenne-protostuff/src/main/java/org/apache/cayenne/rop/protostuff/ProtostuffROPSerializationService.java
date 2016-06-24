begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *<p>  * http://www.apache.org/licenses/LICENSE-2.0  *<p>  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|rop
operator|.
name|protostuff
package|;
end_package

begin_import
import|import
name|io
operator|.
name|protostuff
operator|.
name|GraphIOUtil
import|;
end_import

begin_import
import|import
name|io
operator|.
name|protostuff
operator|.
name|LinkedBuffer
import|;
end_import

begin_import
import|import
name|io
operator|.
name|protostuff
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|io
operator|.
name|protostuff
operator|.
name|runtime
operator|.
name|DefaultIdStrategy
import|;
end_import

begin_import
import|import
name|io
operator|.
name|protostuff
operator|.
name|runtime
operator|.
name|RuntimeEnv
import|;
end_import

begin_import
import|import
name|io
operator|.
name|protostuff
operator|.
name|runtime
operator|.
name|RuntimeSchema
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
name|ObjectContextChangeLogSubListMessageFactory
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
name|ToManyList
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
name|PrefetchTreeNode
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
name|PrefetchTreeNodeSchema
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
name|rop
operator|.
name|ROPSerializationService
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
name|util
operator|.
name|PersistentObjectList
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
name|util
operator|.
name|PersistentObjectMap
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
name|util
operator|.
name|PersistentObjectSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_comment
comment|/**  * This {@link ROPSerializationService} implementation uses Protostuff {@link GraphIOUtil} to (de)serialize  * Cayenne object graph.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ProtostuffROPSerializationService
implements|implements
name|ROPSerializationService
block|{
specifier|protected
name|Schema
argument_list|<
name|Wrapper
argument_list|>
name|wrapperSchema
decl_stmt|;
specifier|protected
name|DefaultIdStrategy
name|strategy
decl_stmt|;
specifier|public
name|ProtostuffROPSerializationService
parameter_list|()
block|{
name|this
operator|.
name|strategy
operator|=
operator|(
name|DefaultIdStrategy
operator|)
name|RuntimeEnv
operator|.
name|ID_STRATEGY
expr_stmt|;
name|register
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|register
parameter_list|()
block|{
name|this
operator|.
name|wrapperSchema
operator|=
name|RuntimeSchema
operator|.
name|getSchema
argument_list|(
name|Wrapper
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|strategy
operator|.
name|registerCollection
argument_list|(
operator|new
name|ObjectContextChangeLogSubListMessageFactory
argument_list|()
argument_list|)
expr_stmt|;
name|RuntimeSchema
operator|.
name|register
argument_list|(
name|PrefetchTreeNode
operator|.
name|class
argument_list|,
operator|new
name|PrefetchTreeNodeSchema
argument_list|()
argument_list|)
expr_stmt|;
name|RuntimeSchema
operator|.
name|register
argument_list|(
name|PersistentObjectList
operator|.
name|class
argument_list|)
expr_stmt|;
name|RuntimeSchema
operator|.
name|register
argument_list|(
name|PersistentObjectMap
operator|.
name|class
argument_list|)
expr_stmt|;
name|RuntimeSchema
operator|.
name|register
argument_list|(
name|PersistentObjectSet
operator|.
name|class
argument_list|)
expr_stmt|;
name|RuntimeSchema
operator|.
name|register
argument_list|(
name|ToManyList
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|serialize
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|GraphIOUtil
operator|.
name|toByteArray
argument_list|(
operator|new
name|Wrapper
argument_list|(
name|object
argument_list|)
argument_list|,
name|wrapperSchema
argument_list|,
name|LinkedBuffer
operator|.
name|allocate
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|serialize
parameter_list|(
name|Object
name|object
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|IOException
block|{
name|GraphIOUtil
operator|.
name|writeTo
argument_list|(
name|outputStream
argument_list|,
operator|new
name|Wrapper
argument_list|(
name|object
argument_list|)
argument_list|,
name|wrapperSchema
argument_list|,
name|LinkedBuffer
operator|.
name|allocate
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|deserialize
parameter_list|(
name|InputStream
name|inputStream
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|objectClass
parameter_list|)
throws|throws
name|IOException
block|{
name|Wrapper
name|result
init|=
name|wrapperSchema
operator|.
name|newMessage
argument_list|()
decl_stmt|;
name|GraphIOUtil
operator|.
name|mergeFrom
argument_list|(
name|inputStream
argument_list|,
name|result
argument_list|,
name|wrapperSchema
argument_list|)
expr_stmt|;
return|return
name|objectClass
operator|.
name|cast
argument_list|(
name|result
operator|.
name|data
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|deserialize
parameter_list|(
name|byte
index|[]
name|serializedObject
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|objectClass
parameter_list|)
throws|throws
name|IOException
block|{
name|Wrapper
name|result
init|=
name|wrapperSchema
operator|.
name|newMessage
argument_list|()
decl_stmt|;
name|GraphIOUtil
operator|.
name|mergeFrom
argument_list|(
name|serializedObject
argument_list|,
name|result
argument_list|,
name|wrapperSchema
argument_list|)
expr_stmt|;
return|return
name|objectClass
operator|.
name|cast
argument_list|(
name|result
operator|.
name|data
argument_list|)
return|;
block|}
block|}
end_class

end_unit

