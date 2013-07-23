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
name|beans
operator|.
name|IntrospectionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|beans
operator|.
name|PropertyDescriptor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_comment
comment|/**  * A property accessor that uses set/get methods following JavaBean naming conventions.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|BeanAccessor
implements|implements
name|Accessor
block|{
specifier|protected
name|String
name|propertyName
decl_stmt|;
specifier|protected
name|Method
name|readMethod
decl_stmt|;
specifier|protected
name|Method
name|writeMethod
decl_stmt|;
specifier|protected
name|Object
name|nullValue
decl_stmt|;
specifier|public
name|BeanAccessor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|objectClass
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|propertyType
parameter_list|)
block|{
if|if
condition|(
name|objectClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null objectClass"
argument_list|)
throw|;
block|}
if|if
condition|(
name|propertyName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null propertyName"
argument_list|)
throw|;
block|}
name|this
operator|.
name|propertyName
operator|=
name|propertyName
expr_stmt|;
name|this
operator|.
name|nullValue
operator|=
name|PropertyUtils
operator|.
name|defaultNullValueForType
argument_list|(
name|propertyType
argument_list|)
expr_stmt|;
try|try
block|{
name|PropertyDescriptor
name|descriptor
init|=
operator|new
name|PropertyDescriptor
argument_list|(
name|propertyName
argument_list|,
name|objectClass
argument_list|)
decl_stmt|;
name|this
operator|.
name|readMethod
operator|=
name|descriptor
operator|.
name|getReadMethod
argument_list|()
expr_stmt|;
name|this
operator|.
name|writeMethod
operator|=
name|descriptor
operator|.
name|getWriteMethod
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IntrospectionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|PropertyException
argument_list|(
literal|"Invalid bean property: "
operator|+
name|propertyName
argument_list|,
name|this
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|propertyName
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|Object
name|getValue
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|PropertyException
block|{
if|if
condition|(
name|readMethod
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|PropertyException
argument_list|(
literal|"Property '"
operator|+
name|propertyName
operator|+
literal|"' is not readable"
argument_list|,
name|this
argument_list|,
name|object
argument_list|)
throw|;
block|}
try|try
block|{
return|return
name|readMethod
operator|.
name|invoke
argument_list|(
name|object
argument_list|,
operator|(
name|Object
index|[]
operator|)
literal|null
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
throw|throw
operator|new
name|PropertyException
argument_list|(
literal|"Error reading property: "
operator|+
name|propertyName
argument_list|,
name|this
argument_list|,
name|object
argument_list|,
name|th
argument_list|)
throw|;
block|}
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|setValue
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
name|newValue
parameter_list|)
throws|throws
name|PropertyException
block|{
if|if
condition|(
name|writeMethod
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|PropertyException
argument_list|(
literal|"Property '"
operator|+
name|propertyName
operator|+
literal|"' is not writable"
argument_list|,
name|this
argument_list|,
name|object
argument_list|)
throw|;
block|}
comment|// this will take care of primitives.
if|if
condition|(
name|newValue
operator|==
literal|null
condition|)
block|{
name|newValue
operator|=
name|this
operator|.
name|nullValue
expr_stmt|;
block|}
try|try
block|{
name|writeMethod
operator|.
name|invoke
argument_list|(
name|object
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
throw|throw
operator|new
name|PropertyException
argument_list|(
literal|"Error reading property: "
operator|+
name|propertyName
argument_list|,
name|this
argument_list|,
name|object
argument_list|,
name|th
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit
