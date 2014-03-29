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
operator|.
name|transformer
operator|.
name|value
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|DatatypeConverter
import|;
end_import

begin_comment
comment|/**  * Generates a Base64-encoded String from a byte array. generated String does  * not contain line breaks that are used for MIME Base64, but are meaningless in  * a DB.  *   * @since 3.2  */
end_comment

begin_class
class|class
name|Base64FromBytesConverter
implements|implements
name|FromBytesConverter
block|{
specifier|static
specifier|final
name|FromBytesConverter
name|INSTANCE
init|=
operator|new
name|Base64FromBytesConverter
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Object
name|fromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|DatatypeConverter
operator|.
name|printBase64Binary
argument_list|(
name|bytes
argument_list|)
return|;
block|}
block|}
end_class

end_unit

