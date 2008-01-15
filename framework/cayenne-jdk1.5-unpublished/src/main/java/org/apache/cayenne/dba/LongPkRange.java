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
name|dba
package|;
end_package

begin_comment
comment|/**  * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|LongPkRange
block|{
specifier|private
name|long
name|curValue
decl_stmt|;
specifier|private
name|long
name|maxValue
decl_stmt|;
name|LongPkRange
parameter_list|(
name|long
name|curValue
parameter_list|,
name|long
name|maxValue
parameter_list|)
block|{
name|reset
argument_list|(
name|curValue
argument_list|,
name|maxValue
argument_list|)
expr_stmt|;
block|}
name|void
name|reset
parameter_list|(
name|long
name|curValue
parameter_list|,
name|long
name|maxValue
parameter_list|)
block|{
name|this
operator|.
name|curValue
operator|=
name|curValue
expr_stmt|;
name|this
operator|.
name|maxValue
operator|=
name|maxValue
expr_stmt|;
block|}
name|boolean
name|isExhausted
parameter_list|()
block|{
return|return
name|curValue
operator|>
name|maxValue
return|;
block|}
name|long
name|getNextPrimaryKey
parameter_list|()
block|{
comment|// do bound checking
if|if
condition|(
name|isExhausted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"PkRange is exhausted and can not be used anymore."
argument_list|)
throw|;
block|}
return|return
name|curValue
operator|++
return|;
block|}
block|}
end_class

end_unit

