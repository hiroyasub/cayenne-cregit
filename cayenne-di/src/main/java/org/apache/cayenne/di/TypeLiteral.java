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
name|Arrays
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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
class|class
name|TypeLiteral
parameter_list|<
name|T
parameter_list|>
block|{
specifier|final
name|Class
argument_list|<
name|?
super|super
name|T
argument_list|>
name|type
decl_stmt|;
specifier|final
name|String
name|typeName
decl_stmt|;
specifier|final
name|String
index|[]
name|argumentsType
decl_stmt|;
specifier|static
parameter_list|<
name|T
parameter_list|>
name|TypeLiteral
argument_list|<
name|T
argument_list|>
name|of
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|TypeLiteral
argument_list|<>
argument_list|(
name|type
argument_list|)
return|;
block|}
specifier|static
parameter_list|<
name|T
parameter_list|>
name|TypeLiteral
argument_list|<
name|List
argument_list|<
name|T
argument_list|>
argument_list|>
name|listOf
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|TypeLiteral
argument_list|<>
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|type
argument_list|)
return|;
block|}
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|TypeLiteral
argument_list|<
name|Map
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|mapOf
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|K
argument_list|>
name|keyType
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|V
argument_list|>
name|valueType
parameter_list|)
block|{
return|return
operator|new
name|TypeLiteral
argument_list|<>
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|keyType
argument_list|,
name|valueType
argument_list|)
return|;
block|}
name|TypeLiteral
parameter_list|(
name|Class
argument_list|<
name|?
super|super
name|T
argument_list|>
name|type
parameter_list|,
name|Type
modifier|...
name|argumentsType
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|typeName
operator|=
name|type
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|argumentsType
operator|=
operator|new
name|String
index|[
name|argumentsType
operator|.
name|length
index|]
expr_stmt|;
comment|//argumentsType;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|argumentsType
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|argumentsType
index|[
name|i
index|]
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|argumentsType
index|[
name|i
index|]
operator|=
operator|(
operator|(
name|ParameterizedType
operator|)
name|argumentsType
index|[
name|i
index|]
operator|)
operator|.
name|getRawType
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|argumentsType
index|[
name|i
index|]
operator|instanceof
name|Class
condition|)
block|{
name|this
operator|.
name|argumentsType
index|[
name|i
index|]
operator|=
operator|(
operator|(
name|Class
operator|)
name|argumentsType
index|[
name|i
index|]
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|argumentsType
index|[
name|i
index|]
operator|=
name|argumentsType
index|[
name|i
index|]
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|Class
argument_list|<
name|?
super|super
name|T
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
return|return
literal|false
return|;
name|TypeLiteral
argument_list|<
name|?
argument_list|>
name|that
init|=
operator|(
name|TypeLiteral
argument_list|<
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|typeName
operator|.
name|equals
argument_list|(
name|that
operator|.
name|typeName
argument_list|)
condition|)
return|return
literal|false
return|;
return|return
name|Arrays
operator|.
name|equals
argument_list|(
name|argumentsType
argument_list|,
name|that
operator|.
name|argumentsType
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|typeName
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|Arrays
operator|.
name|hashCode
argument_list|(
name|argumentsType
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|result
init|=
name|typeName
decl_stmt|;
if|if
condition|(
name|argumentsType
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|result
operator|+=
name|Arrays
operator|.
name|toString
argument_list|(
name|argumentsType
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

