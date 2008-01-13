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
name|Serializer
import|;
end_import

begin_comment
comment|/**  * A Hessian SerializerFactory extension that supports serializing Enums.  *<p>  *<i>Requires Java 1.5 or newer</i>  *</p>  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|EnumSerializerFactory
extends|extends
name|AbstractSerializerFactory
block|{
specifier|private
specifier|final
name|EnumSerializer
name|enumSerializer
init|=
operator|new
name|EnumSerializer
argument_list|()
decl_stmt|;
specifier|private
name|HashMap
argument_list|<
name|Class
argument_list|,
name|Deserializer
argument_list|>
name|cachedDeserializerMap
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
operator|(
name|cl
operator|.
name|isEnum
argument_list|()
operator|)
condition|?
name|enumSerializer
else|:
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
if|if
condition|(
name|cl
operator|.
name|isEnum
argument_list|()
condition|)
block|{
name|Deserializer
name|deserializer
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|cachedDeserializerMap
operator|!=
literal|null
condition|)
block|{
name|deserializer
operator|=
name|cachedDeserializerMap
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
name|EnumDeserializer
argument_list|(
name|cl
argument_list|)
expr_stmt|;
if|if
condition|(
name|cachedDeserializerMap
operator|==
literal|null
condition|)
block|{
name|cachedDeserializerMap
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
name|cachedDeserializerMap
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
return|return
name|deserializer
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

