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
name|reflect
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneRuntimeException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ExtendedEnumeration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * @since 1.2  */
end_comment

begin_class
class|class
name|EnumConverter
parameter_list|<
name|T
extends|extends
name|Enum
parameter_list|<
name|T
parameter_list|>
operator|&
name|ExtendedEnumeration
parameter_list|>
implements|implements
name|Converter
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|T
name|convert
parameter_list|(
name|Object
name|object
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|ExtendedEnumeration
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|ExtendedEnumeration
index|[]
name|values
decl_stmt|;
try|try
block|{
name|values
operator|=
operator|(
name|ExtendedEnumeration
index|[]
operator|)
name|type
operator|.
name|getMethod
argument_list|(
literal|"values"
argument_list|)
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// unexpected, all enums should have values
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
for|for
control|(
name|ExtendedEnumeration
name|en
range|:
name|values
control|)
block|{
if|if
condition|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|en
operator|.
name|getDatabaseValue
argument_list|()
argument_list|,
name|object
argument_list|)
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|en
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
name|T
operator|)
name|Enum
operator|.
name|valueOf
argument_list|(
name|type
argument_list|,
name|object
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

