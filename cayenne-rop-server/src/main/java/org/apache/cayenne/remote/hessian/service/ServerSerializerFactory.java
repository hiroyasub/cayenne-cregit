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
name|remote
operator|.
name|hessian
operator|.
name|service
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
name|JavaSerializer
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
comment|/**  * An object that manages all custom (de)serializers used on the server.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|ServerSerializerFactory
extends|extends
name|AbstractSerializerFactory
block|{
specifier|private
name|ServerPersistentObjectListSerializer
name|persistentObjectListSerializer
decl_stmt|;
specifier|private
name|ServerDataRowSerializer
name|dataRowSerilaizer
decl_stmt|;
specifier|private
name|Serializer
name|javaSerializer
decl_stmt|;
name|ServerSerializerFactory
parameter_list|()
block|{
name|this
operator|.
name|persistentObjectListSerializer
operator|=
operator|new
name|ServerPersistentObjectListSerializer
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataRowSerilaizer
operator|=
operator|new
name|ServerDataRowSerializer
argument_list|()
expr_stmt|;
block|}
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
return|return
name|persistentObjectListSerializer
return|;
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
return|return
name|dataRowSerilaizer
return|;
block|}
comment|//turns out Hessian uses its own (incorrect) serialization mechanism for maps
if|else if
condition|(
name|PersistentObjectMap
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
name|javaSerializer
operator|==
literal|null
condition|)
block|{
name|javaSerializer
operator|=
operator|new
name|JavaSerializer
argument_list|(
name|cl
argument_list|)
expr_stmt|;
block|}
return|return
name|javaSerializer
return|;
block|}
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
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

