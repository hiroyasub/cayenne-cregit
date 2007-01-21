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
name|access
operator|.
name|types
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
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
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
name|CayenneRuntimeException
import|;
end_import

begin_comment
comment|/**  * ExtendedTypeFactory for handling serializable objects. Returned ExtendedType is simply  * an object serialization wrapper on top of byte[] ExtendedType.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|SerializableTypeFactory
implements|implements
name|ExtendedTypeFactory
block|{
specifier|private
name|ExtendedTypeMap
name|map
decl_stmt|;
name|SerializableTypeFactory
parameter_list|(
name|ExtendedTypeMap
name|map
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
specifier|public
name|ExtendedType
name|getType
parameter_list|(
name|Class
name|objectClass
parameter_list|)
block|{
if|if
condition|(
name|Serializable
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|objectClass
argument_list|)
condition|)
block|{
comment|// using a binary stream delegate instead of byte[] may actually speed up
comment|// things in some dbs, but at least byte[] type works consistently across
comment|// adapters...
comment|// note - can't use "getRegisteredType" as it causes infinite recursion
name|ExtendedType
name|bytesType
init|=
name|map
operator|.
name|getExplictlyRegisteredType
argument_list|(
literal|"byte[]"
argument_list|)
decl_stmt|;
comment|// not sure if this type of recursion can occur, still worth checking
if|if
condition|(
name|bytesType
operator|instanceof
name|SerializableType
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Can't create Serializable ExtendedType for "
operator|+
name|objectClass
operator|.
name|getName
argument_list|()
operator|+
literal|": no ExtendedType exists for byte[]"
argument_list|)
throw|;
block|}
return|return
operator|new
name|SerializableType
argument_list|(
name|objectClass
argument_list|,
name|bytesType
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * A serialization wrapper on top of byte[] ExtendedType      */
specifier|final
class|class
name|SerializableType
extends|extends
name|ExtendedTypeDecorator
block|{
specifier|private
name|Class
name|javaClass
decl_stmt|;
name|SerializableType
parameter_list|(
name|Class
name|javaClass
parameter_list|,
name|ExtendedType
name|bytesType
parameter_list|)
block|{
name|super
argument_list|(
name|bytesType
argument_list|)
expr_stmt|;
name|this
operator|.
name|javaClass
operator|=
name|javaClass
expr_stmt|;
block|}
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|javaClass
operator|.
name|getName
argument_list|()
return|;
block|}
name|Object
name|fromJavaObject
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|ByteArrayOutputStream
name|bytes
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
block|{
comment|// avoid unneeded array copy...
specifier|public
specifier|synchronized
name|byte
index|[]
name|toByteArray
parameter_list|()
block|{
return|return
name|buf
return|;
block|}
block|}
decl_stmt|;
try|try
block|{
name|ObjectOutputStream
name|out
init|=
operator|new
name|ObjectOutputStream
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|out
operator|.
name|writeObject
argument_list|(
name|object
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error serializing object"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|bytes
operator|.
name|toByteArray
argument_list|()
return|;
block|}
name|Object
name|toJavaObject
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|byte
index|[]
name|bytes
init|=
operator|(
name|byte
index|[]
operator|)
name|object
decl_stmt|;
try|try
block|{
return|return
name|bytes
operator|!=
literal|null
operator|&&
name|bytes
operator|.
name|length
operator|>
literal|0
condition|?
operator|new
name|ObjectInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
argument_list|)
operator|.
name|readObject
argument_list|()
else|:
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error deserializing object"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

