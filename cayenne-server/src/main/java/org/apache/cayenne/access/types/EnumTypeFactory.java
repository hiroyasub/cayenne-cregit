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
name|access
operator|.
name|types
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
name|ExtendedEnumeration
import|;
end_import

begin_comment
comment|/**  * ExtendedTypeFactory for handling Enum types.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|EnumTypeFactory
implements|implements
name|ExtendedTypeFactory
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|ExtendedType
name|getType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|objectClass
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
name|objectClass
argument_list|)
condition|)
block|{
return|return
operator|new
name|ExtendedEnumType
argument_list|(
name|objectClass
argument_list|)
return|;
block|}
if|else if
condition|(
name|objectClass
operator|.
name|isEnum
argument_list|()
condition|)
block|{
return|return
operator|new
name|EnumType
argument_list|(
name|objectClass
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

