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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|DataRow
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
name|com
operator|.
name|caucho
operator|.
name|hessian
operator|.
name|io
operator|.
name|AbstractSerializerFactory
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
name|Deserializer
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
name|HessianProtocolException
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
name|JavaDeserializer
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
name|Serializer
import|;
end_import

begin_comment
comment|/**  * An object that manages all custom (de)serializers used on the client.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|ClientSerializerFactory
extends|extends
name|AbstractSerializerFactory
block|{
specifier|private
name|Map
argument_list|<
name|Class
argument_list|,
name|Deserializer
argument_list|>
name|deserializers
decl_stmt|;
specifier|private
name|Deserializer
name|dataRowDeserializer
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Serializer
name|getSerializer
parameter_list|(
name|Class
name|cl
parameter_list|)
throws|throws
name|HessianProtocolException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Deserializer
name|getDeserializer
parameter_list|(
name|Class
name|cl
parameter_list|)
throws|throws
name|HessianProtocolException
block|{
name|Deserializer
name|deserializer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|PersistentObjectList
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cl
argument_list|)
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|deserializers
operator|!=
literal|null
condition|)
block|{
name|deserializer
operator|=
name|deserializers
operator|.
name|get
argument_list|(
name|cl
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|deserializer
operator|==
literal|null
condition|)
block|{
name|deserializer
operator|=
operator|new
name|JavaDeserializer
argument_list|(
name|cl
argument_list|)
expr_stmt|;
if|if
condition|(
name|deserializers
operator|==
literal|null
condition|)
block|{
name|deserializers
operator|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|,
name|Deserializer
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|deserializers
operator|.
name|put
argument_list|(
name|cl
argument_list|,
name|deserializer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|else if
condition|(
name|DataRow
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cl
argument_list|)
condition|)
block|{
if|if
condition|(
name|dataRowDeserializer
operator|==
literal|null
condition|)
block|{
name|dataRowDeserializer
operator|=
operator|new
name|DataRowDeserializer
argument_list|()
expr_stmt|;
block|}
return|return
name|dataRowDeserializer
return|;
block|}
return|return
name|deserializer
return|;
block|}
block|}
end_class

end_unit

