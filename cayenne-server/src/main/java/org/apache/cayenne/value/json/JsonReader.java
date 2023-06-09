begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|value
operator|.
name|json
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayDeque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Deque
import|;
end_import

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
comment|/**  * @since 4.2  */
end_comment

begin_class
class|class
name|JsonReader
extends|extends
name|AbstractJsonConsumer
argument_list|<
name|Object
argument_list|>
block|{
specifier|private
specifier|final
name|Deque
argument_list|<
name|Object
argument_list|>
name|objects
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Deque
argument_list|<
name|Object
argument_list|>
name|names
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|JsonReader
parameter_list|(
name|String
name|json
parameter_list|)
block|{
name|super
argument_list|(
name|json
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onArrayStart
parameter_list|()
block|{
name|objects
operator|.
name|addLast
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onObjectStart
parameter_list|()
block|{
name|objects
operator|.
name|addLast
argument_list|(
operator|new
name|HashMap
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onArrayValue
parameter_list|(
name|JsonTokenizer
operator|.
name|JsonToken
name|token
parameter_list|)
block|{
name|onArrayValue
argument_list|(
operator|(
name|Object
operator|)
name|token
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|void
name|onArrayValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Object
name|array
init|=
name|objects
operator|.
name|getLast
argument_list|()
decl_stmt|;
if|if
condition|(
name|array
operator|instanceof
name|List
condition|)
block|{
operator|(
operator|(
name|List
argument_list|<
name|Object
argument_list|>
operator|)
name|array
operator|)
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Expected List got "
operator|+
name|array
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onObjectKey
parameter_list|(
name|JsonTokenizer
operator|.
name|JsonToken
name|token
parameter_list|)
block|{
name|names
operator|.
name|addLast
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onObjectValue
parameter_list|(
name|JsonTokenizer
operator|.
name|JsonToken
name|token
parameter_list|)
block|{
name|onObjectValue
argument_list|(
name|token
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|void
name|onObjectValue
parameter_list|(
name|Object
name|value
parameter_list|,
name|boolean
name|updateState
parameter_list|)
block|{
name|Object
name|name
init|=
name|names
operator|.
name|pollLast
argument_list|()
decl_stmt|;
name|Object
name|map
init|=
name|objects
operator|.
name|getLast
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|instanceof
name|Map
condition|)
block|{
operator|(
operator|(
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
operator|)
name|map
operator|)
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Expected Map got "
operator|+
name|map
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|updateState
condition|)
block|{
name|setState
argument_list|(
name|State
operator|.
name|OBJECT_KEY
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onValue
parameter_list|(
name|JsonTokenizer
operator|.
name|JsonToken
name|token
parameter_list|)
block|{
name|objects
operator|.
name|addLast
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onArrayEnd
parameter_list|()
block|{
name|Object
name|value
init|=
name|objects
operator|.
name|pollLast
argument_list|()
decl_stmt|;
name|onValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onObjectEnd
parameter_list|()
block|{
name|Object
name|value
init|=
name|objects
operator|.
name|pollLast
argument_list|()
decl_stmt|;
name|onValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|void
name|onValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
switch|switch
condition|(
name|currentState
argument_list|()
condition|)
block|{
case|case
name|OBJECT_VALUE
case|:
name|onObjectValue
argument_list|(
name|value
argument_list|,
literal|true
argument_list|)
expr_stmt|;
break|break;
case|case
name|ARRAY
case|:
name|onArrayValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
break|break;
default|default:
name|objects
operator|.
name|addLast
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|output
parameter_list|()
block|{
if|if
condition|(
name|objects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|objects
operator|.
name|getFirst
argument_list|()
return|;
block|}
block|}
end_class

end_unit

