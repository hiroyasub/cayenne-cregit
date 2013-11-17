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
package|;
end_package

begin_comment
comment|/**  * The plain text password encoder passes the text of the database password  * straight-through without any alteration. This is identical to the behavior of pre-3.0  * versions of Cayenne, where the password was stored in the XML model in clear text.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|PlainTextPasswordEncoder
implements|implements
name|PasswordEncoding
block|{
specifier|public
name|String
name|decodePassword
parameter_list|(
name|String
name|encodedPassword
parameter_list|,
name|String
name|key
parameter_list|)
block|{
return|return
name|encodedPassword
return|;
block|}
specifier|public
name|String
name|encodePassword
parameter_list|(
name|String
name|normalPassword
parameter_list|,
name|String
name|key
parameter_list|)
block|{
return|return
name|normalPassword
return|;
block|}
block|}
end_class

end_unit

