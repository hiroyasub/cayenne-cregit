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
name|configuration
operator|.
name|xml
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
name|configuration
operator|.
name|ConfigurationNode
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_comment
comment|/**  *<p>  *     Default implementation of {@link DataChannelMetaData} that stores data in Map.  *</p>  *<p>  *     This implementation is thread safe.  *</p>  *  * @see NoopDataChannelMetaData  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|DefaultDataChannelMetaData
implements|implements
name|DataChannelMetaData
block|{
specifier|private
name|Map
argument_list|<
name|ConfigurationNode
argument_list|,
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
argument_list|>
name|map
decl_stmt|;
specifier|public
name|DefaultDataChannelMetaData
parameter_list|()
block|{
name|map
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
comment|/**      * value.getClass() will be used under the hood to associate data with the key object.      *      * @param key object for which we want to store data      * @param value data to store      */
annotation|@
name|Override
specifier|public
name|void
name|add
parameter_list|(
name|ConfigurationNode
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|key
operator|==
literal|null
operator|||
name|value
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|data
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
name|data
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|old
init|=
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|data
argument_list|)
decl_stmt|;
comment|// extra check in case if someone was fast enough
if|if
condition|(
name|old
operator|!=
literal|null
condition|)
block|{
name|data
operator|.
name|putAll
argument_list|(
name|old
argument_list|)
expr_stmt|;
block|}
block|}
name|data
operator|.
name|put
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * If either key or value is {@code null} then {@code null} will be returned.      *      * @param key object for wich we want meta data      * @param type meta data type class      * @param<T> data type      * @return value or {@code null}      */
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|ConfigurationNode
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|key
operator|==
literal|null
operator|||
name|type
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|data
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|type
operator|.
name|cast
argument_list|(
name|data
operator|.
name|get
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
comment|/**      *      * @param key object for wich we want meta data      * @param type meta data type class      * @param<T> data type      * @return removed value or {@code null}      */
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|remove
parameter_list|(
name|ConfigurationNode
name|key
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|key
operator|==
literal|null
operator|||
name|type
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|data
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|type
operator|.
name|cast
argument_list|(
name|data
operator|.
name|remove
argument_list|(
name|type
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

