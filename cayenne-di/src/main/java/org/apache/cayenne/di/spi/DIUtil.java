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
name|di
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|ParameterizedType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
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
name|di
operator|.
name|Key
import|;
end_import

begin_comment
comment|/**  * A helper class used by Cayenne DI implementation.  *   * @since 3.1  */
end_comment

begin_class
class|class
name|DIUtil
block|{
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|parameterClass
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|ParameterizedType
name|parameterizedType
init|=
operator|(
name|ParameterizedType
operator|)
name|type
decl_stmt|;
name|Type
index|[]
name|parameters
init|=
name|parameterizedType
operator|.
name|getActualTypeArguments
argument_list|()
decl_stmt|;
if|if
condition|(
name|parameters
operator|.
name|length
operator|==
literal|1
condition|)
block|{
return|return
name|typeToClass
argument_list|(
name|parameters
index|[
literal|0
index|]
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|allParametersClass
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|arr
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|ParameterizedType
name|parameterizedType
init|=
operator|(
name|ParameterizedType
operator|)
name|type
decl_stmt|;
name|Type
index|[]
name|parameters
init|=
name|parameterizedType
operator|.
name|getActualTypeArguments
argument_list|()
decl_stmt|;
name|arr
operator|=
operator|new
name|Class
index|[
name|parameters
operator|.
name|length
index|]
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Type
name|next
range|:
name|parameters
control|)
block|{
name|arr
index|[
name|i
operator|++
index|]
operator|=
name|typeToClass
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|arr
return|;
block|}
specifier|static
name|Key
argument_list|<
name|?
argument_list|>
name|getKeyForTypeAndGenericType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|String
name|bindingName
parameter_list|)
block|{
if|if
condition|(
name|List
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|objectClass
init|=
name|parameterClass
argument_list|(
name|genericType
argument_list|)
decl_stmt|;
if|if
condition|(
name|objectClass
operator|==
literal|null
condition|)
block|{
name|objectClass
operator|=
name|Object
operator|.
name|class
expr_stmt|;
block|}
return|return
name|Key
operator|.
name|getListOf
argument_list|(
name|objectClass
argument_list|,
name|bindingName
argument_list|)
return|;
block|}
if|else if
condition|(
name|Map
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|classes
init|=
name|DIUtil
operator|.
name|allParametersClass
argument_list|(
name|genericType
argument_list|)
decl_stmt|;
if|if
condition|(
name|classes
operator|==
literal|null
condition|)
block|{
name|classes
operator|=
operator|new
name|Class
index|[]
block|{
name|Object
operator|.
name|class
block|,
name|Object
operator|.
name|class
block|}
expr_stmt|;
block|}
return|return
name|Key
operator|.
name|getMapOf
argument_list|(
name|classes
index|[
literal|0
index|]
argument_list|,
name|classes
index|[
literal|1
index|]
argument_list|,
name|bindingName
argument_list|)
return|;
block|}
return|return
name|Key
operator|.
name|get
argument_list|(
name|type
argument_list|,
name|bindingName
argument_list|)
return|;
block|}
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|typeToClass
parameter_list|(
name|Type
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|instanceof
name|Class
condition|)
block|{
return|return
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|type
return|;
block|}
if|else if
condition|(
name|type
operator|instanceof
name|ParameterizedType
condition|)
block|{
return|return
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
operator|(
operator|(
name|ParameterizedType
operator|)
name|type
operator|)
operator|.
name|getRawType
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|Object
operator|.
name|class
return|;
block|}
block|}
block|}
end_class

end_unit

