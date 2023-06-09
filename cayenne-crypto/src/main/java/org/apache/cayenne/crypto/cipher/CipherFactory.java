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
name|crypto
operator|.
name|cipher
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|Cipher
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|CipherFactory
block|{
comment|/**      * Creates and returns a new {@link Cipher} configured using settings known      * to the factory implementation.      *       * @return a new Cipher that is guaranteed to be unused by other callers or      *         null if the factory does not support cipher-based encryption.      */
name|Cipher
name|cipher
parameter_list|()
function_decl|;
comment|/**      * Returns the block size for the ciphers created by this factory. This      * information is needed for the callers to presize they various arrays      * before a cipher is available.      */
name|int
name|blockSize
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

