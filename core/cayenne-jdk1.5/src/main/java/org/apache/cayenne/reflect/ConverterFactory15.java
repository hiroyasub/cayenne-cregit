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
name|reflect
package|;
end_package

begin_comment
comment|/**  * JDK 15 friendly converter factory.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|ConverterFactory15
extends|extends
name|ConverterFactory
block|{
specifier|private
name|EnumConverter
name|enumConveter
init|=
operator|new
name|EnumConverter
argument_list|()
decl_stmt|;
annotation|@
name|Override
name|Converter
name|getConverter
parameter_list|(
name|Class
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null type"
argument_list|)
throw|;
block|}
comment|// check for enum BEFORE super call, as it will return a noop converter
if|if
condition|(
name|type
operator|.
name|isEnum
argument_list|()
condition|)
block|{
return|return
name|enumConveter
return|;
block|}
return|return
name|super
operator|.
name|getConverter
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

