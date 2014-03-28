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
name|crypto
package|;
end_package

begin_comment
comment|/**  * @since 3.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|CryptoConstants
block|{
comment|/**      * An injection key for the map of the crypto properties.      */
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTIES_MAP
init|=
literal|"cayenne.crypto.properties"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CIPHER_ALGORITHM
init|=
literal|"cayenne.crypto.cipher.algorithm"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CIPHER_MODE
init|=
literal|"cayenne.crypto.cipher.mode"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CIPHER_PADDING
init|=
literal|"cayenne.crypto.cipher.padding"
decl_stmt|;
block|}
end_interface

end_unit

