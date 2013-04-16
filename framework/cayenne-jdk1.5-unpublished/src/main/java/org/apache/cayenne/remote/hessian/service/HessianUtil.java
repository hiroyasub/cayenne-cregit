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
name|remote
operator|.
name|hessian
operator|.
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|EntityResolver
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
name|remote
operator|.
name|hessian
operator|.
name|HessianConfig
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
name|remote
operator|.
name|hessian
operator|.
name|HessianConnection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|caucho
operator|.
name|hessian
operator|.
name|io
operator|.
name|HessianInput
import|;
end_import

begin_import
import|import
name|com
operator|.
name|caucho
operator|.
name|hessian
operator|.
name|io
operator|.
name|HessianOutput
import|;
end_import

begin_comment
comment|/**  * Hessian related utilities.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|HessianUtil
block|{
comment|/**      * A utility method that clones an object using Hessian serialization/deserialization      * mechanism, which is different from default Java serialization.      */
specifier|public
specifier|static
name|Object
name|cloneViaClientServerSerialization
parameter_list|(
name|Serializable
name|object
parameter_list|,
name|EntityResolver
name|serverResolver
parameter_list|)
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|bytes
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|HessianOutput
name|out
init|=
operator|new
name|HessianOutput
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|out
operator|.
name|setSerializerFactory
argument_list|(
name|HessianConfig
operator|.
name|createFactory
argument_list|(
name|HessianConnection
operator|.
name|CLIENT_SERIALIZER_FACTORIES
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|byte
index|[]
name|data
init|=
name|bytes
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|HessianInput
name|in
init|=
operator|new
name|HessianInput
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|)
decl_stmt|;
name|in
operator|.
name|setSerializerFactory
argument_list|(
name|HessianConfig
operator|.
name|createFactory
argument_list|(
name|HessianService
operator|.
name|SERVER_SERIALIZER_FACTORIES
argument_list|,
name|serverResolver
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|in
operator|.
name|readObject
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|Object
name|cloneViaServerClientSerialization
parameter_list|(
name|Serializable
name|object
parameter_list|,
name|EntityResolver
name|serverResolver
parameter_list|)
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|bytes
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|HessianOutput
name|out
init|=
operator|new
name|HessianOutput
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|out
operator|.
name|setSerializerFactory
argument_list|(
name|HessianConfig
operator|.
name|createFactory
argument_list|(
name|HessianService
operator|.
name|SERVER_SERIALIZER_FACTORIES
argument_list|,
name|serverResolver
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|byte
index|[]
name|data
init|=
name|bytes
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|HessianInput
name|in
init|=
operator|new
name|HessianInput
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|)
decl_stmt|;
name|in
operator|.
name|setSerializerFactory
argument_list|(
name|HessianConfig
operator|.
name|createFactory
argument_list|(
name|HessianConnection
operator|.
name|CLIENT_SERIALIZER_FACTORIES
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|in
operator|.
name|readObject
argument_list|()
return|;
block|}
specifier|private
name|HessianUtil
parameter_list|()
block|{
block|}
block|}
end_class

end_unit

