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
name|rop
operator|.
name|ROPSerializationService
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

begin_class
specifier|public
class|class
name|ProtostuffROPSerializationService
implements|implements
name|ROPSerializationService
block|{
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
name|Schema
argument_list|<
name|Wrapper
argument_list|>
name|schema
init|=
name|RuntimeSchema
operator|.
name|getSchema
argument_list|(
name|Wrapper
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|schema
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
name|Schema
argument_list|<
name|Wrapper
argument_list|>
name|schema
init|=
name|RuntimeSchema
operator|.
name|getSchema
argument_list|(
name|Wrapper
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|schema
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
name|Schema
argument_list|<
name|Wrapper
argument_list|>
name|schema
init|=
name|RuntimeSchema
operator|.
name|getSchema
argument_list|(
name|Wrapper
operator|.
name|class
argument_list|)
decl_stmt|;
name|Wrapper
name|result
init|=
name|schema
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
name|schema
argument_list|,
name|LinkedBuffer
operator|.
name|allocate
argument_list|()
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
name|Schema
argument_list|<
name|Wrapper
argument_list|>
name|schema
init|=
name|RuntimeSchema
operator|.
name|getSchema
argument_list|(
name|Wrapper
operator|.
name|class
argument_list|)
decl_stmt|;
name|Wrapper
name|result
init|=
name|schema
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
name|schema
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
specifier|private
class|class
name|Wrapper
block|{
specifier|public
name|Object
name|data
decl_stmt|;
specifier|public
name|Wrapper
parameter_list|(
name|Object
name|data
parameter_list|)
block|{
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

