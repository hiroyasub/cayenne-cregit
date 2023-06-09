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

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
class|class
name|JsonFormatter
extends|extends
name|AbstractJsonConsumer
argument_list|<
name|String
argument_list|>
block|{
specifier|private
specifier|final
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|JsonFormatter
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
name|builder
operator|.
name|append
argument_list|(
literal|'['
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
if|if
condition|(
name|builder
operator|.
name|charAt
argument_list|(
name|builder
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|==
literal|' '
condition|)
block|{
name|builder
operator|.
name|delete
argument_list|(
name|builder
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|,
name|builder
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
expr_stmt|;
if|if
condition|(
name|State
operator|.
name|OBJECT_VALUE
operator|.
name|equals
argument_list|(
name|currentState
argument_list|()
argument_list|)
condition|)
block|{
name|setState
argument_list|(
name|State
operator|.
name|OBJECT_KEY
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|onObjectStart
parameter_list|()
block|{
name|builder
operator|.
name|append
argument_list|(
literal|'{'
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
if|if
condition|(
name|builder
operator|.
name|charAt
argument_list|(
name|builder
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|==
literal|' '
condition|)
block|{
name|builder
operator|.
name|delete
argument_list|(
name|builder
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|,
name|builder
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
if|if
condition|(
name|State
operator|.
name|ARRAY
operator|.
name|equals
argument_list|(
name|currentState
argument_list|()
argument_list|)
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
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
name|appendToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
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
name|appendToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|": "
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
name|appendToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
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
name|appendToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|output
parameter_list|()
block|{
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|appendToken
parameter_list|(
name|JsonTokenizer
operator|.
name|JsonToken
name|token
parameter_list|)
block|{
if|if
condition|(
name|token
operator|.
name|type
operator|==
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|STRING
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
name|token
operator|.
name|getData
argument_list|()
argument_list|,
name|token
operator|.
name|from
argument_list|,
name|token
operator|.
name|to
operator|-
name|token
operator|.
name|from
argument_list|)
expr_stmt|;
if|if
condition|(
name|token
operator|.
name|type
operator|==
name|JsonTokenizer
operator|.
name|TokenType
operator|.
name|STRING
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

