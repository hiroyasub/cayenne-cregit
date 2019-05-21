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
name|configuration
package|;
end_package

begin_comment
comment|/**  * Password encoders are used to translate the text of the database password, on loading  * and on saving, from one form to another. It can facilitate the obscuring of the  * password text to make database connection information less obvious to someone who  * stumbles onto the password. Cayenne only includes facilities to obscure, not encrypt,  * the database password. The mechanism is user-extensible, though, so should stronger  * security features be required, they can be added and integrated into both the modeler  * and framework.  *   * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|PasswordEncoding
block|{
specifier|final
name|String
index|[]
name|standardEncoders
init|=
operator|new
name|String
index|[]
block|{
name|PlainTextPasswordEncoder
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|Rot13PasswordEncoder
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|Rot47PasswordEncoder
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
decl_stmt|;
comment|/**      * Decodes an encoded database password.      *       * @param encodedPassword - The encoded password to be decoded      * @param key - An optional data element which can be used to unlock the password.      *            Some encoders may require the key.      * @return The decoded normal/plain password.      */
name|String
name|decodePassword
parameter_list|(
name|String
name|encodedPassword
parameter_list|,
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Encodes a normal/plain database password.      *       * @param normalPassword - The normal/plain password to be encoded      * @param key - An optional data element which can be used to lock the password. Some      *            encoders may require the key.      * @return The encoded password.      */
name|String
name|encodePassword
parameter_list|(
name|String
name|normalPassword
parameter_list|,
name|String
name|key
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

