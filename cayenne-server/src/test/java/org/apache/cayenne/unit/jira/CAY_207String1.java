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
name|unit
operator|.
name|jira
package|;
end_package

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|CAY_207String1
block|{
specifier|protected
name|String
name|string
decl_stmt|;
specifier|public
name|CAY_207String1
parameter_list|(
name|String
name|string
parameter_list|)
block|{
comment|// mock deserialization behavior... if the raw data is invalid, an exception
comment|// should be thrown
if|if
condition|(
name|string
operator|!=
literal|null
operator|&&
operator|!
name|string
operator|.
name|startsWith
argument_list|(
literal|"T1"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|string
argument_list|)
throw|;
block|}
name|this
operator|.
name|string
operator|=
name|string
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
name|string
return|;
block|}
block|}
end_class

end_unit

