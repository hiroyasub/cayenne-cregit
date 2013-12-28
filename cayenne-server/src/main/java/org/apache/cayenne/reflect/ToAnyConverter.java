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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
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
name|CayenneRuntimeException
import|;
end_import

begin_comment
comment|/**  * Can convert to any class that has a constructor that takes a   * single Object or a single String parameter.  */
end_comment

begin_class
specifier|public
class|class
name|ToAnyConverter
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Converter
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|Override
specifier|protected
name|T
name|convert
parameter_list|(
name|Object
name|value
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
name|value
operator|==
literal|null
condition|)
return|return
literal|null
return|;
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
return|return
operator|(
name|T
operator|)
name|value
return|;
comment|// no conversion needed
try|try
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
name|constructor
decl_stmt|;
try|try
block|{
name|constructor
operator|=
name|type
operator|.
name|getConstructor
argument_list|(
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
name|constructor
operator|=
name|type
operator|.
name|getConstructor
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|value
operator|=
name|value
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
operator|(
name|T
operator|)
name|constructor
operator|.
name|newInstance
argument_list|(
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

