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
name|Method
import|;
end_import

begin_comment
comment|/**  * A property accessor that uses set/get methods following JavaBean naming  * conventions.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|BeanAccessor
implements|implements
name|Accessor
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|606253801447018099L
decl_stmt|;
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
name|this
argument_list|(
name|objectClass
argument_list|,
name|propertyName
argument_list|,
name|propertyType
argument_list|,
name|defaultBooleanGetterName
argument_list|(
name|propertyName
argument_list|)
argument_list|,
name|defaultGetterName
argument_list|(
name|propertyName
argument_list|)
argument_list|,
name|defaultSetterName
argument_list|(
name|propertyName
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
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
parameter_list|,
name|String
name|booleanGetterName
parameter_list|,
name|String
name|getterName
parameter_list|,
name|String
name|setterName
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
name|checkPropertyName
argument_list|(
name|propertyName
argument_list|)
expr_stmt|;
if|if
condition|(
name|booleanGetterName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null booleanGetterName"
argument_list|)
throw|;
block|}
if|if
condition|(
name|getterName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null getterName"
argument_list|)
throw|;
block|}
if|if
condition|(
name|setterName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null setterName"
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
name|Method
index|[]
name|publicMethods
init|=
name|objectClass
operator|.
name|getMethods
argument_list|()
decl_stmt|;
name|Method
name|getter
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|publicMethods
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|returnType
init|=
name|method
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
comment|// following Java Bean naming conventions, "is" methods are preferred over "get" methods
if|if
condition|(
name|method
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|booleanGetterName
argument_list|)
operator|&&
name|returnType
operator|.
name|equals
argument_list|(
name|Boolean
operator|.
name|TYPE
argument_list|)
operator|&&
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|getter
operator|=
name|method
expr_stmt|;
break|break;
block|}
comment|// Find the method with the most specific return type.
comment|// This is the same behavior as Class.getMethod(String, Class...) except that
comment|// Class.getMethod prefers synthetic methods generated for interfaces
comment|// over methods with more specific return types in a super class.
if|if
condition|(
name|method
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|getterName
argument_list|)
operator|&&
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|returnType
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
name|getter
operator|=
name|returnType
operator|.
name|equals
argument_list|(
name|Void
operator|.
name|TYPE
argument_list|)
condition|?
literal|null
else|:
name|method
expr_stmt|;
if|if
condition|(
name|returnType
operator|.
name|equals
argument_list|(
name|Boolean
operator|.
name|TYPE
argument_list|)
condition|)
block|{
comment|// keep looking for the "is" method
continue|continue;
block|}
else|else
block|{
comment|// nothing more specific than a primitive, so stop here
break|break;
block|}
block|}
if|if
condition|(
name|getter
operator|==
literal|null
operator|||
name|getter
operator|.
name|getReturnType
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|returnType
argument_list|)
condition|)
block|{
name|getter
operator|=
name|method
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|getter
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Property '"
operator|+
name|propertyName
operator|+
literal|"' is not readable"
argument_list|)
throw|;
block|}
name|this
operator|.
name|readMethod
operator|=
name|getter
expr_stmt|;
comment|// TODO: compare 'propertyType' arg with readMethod.getReturnType()
for|for
control|(
name|Method
name|method
range|:
name|publicMethods
control|)
block|{
if|if
condition|(
operator|!
name|method
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|setterName
argument_list|)
operator|||
operator|!
name|method
operator|.
name|getReturnType
argument_list|()
operator|.
name|equals
argument_list|(
name|Void
operator|.
name|TYPE
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|parameterTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|parameterTypes
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|getter
operator|.
name|getReturnType
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|parameterTypes
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|this
operator|.
name|writeMethod
operator|=
name|method
expr_stmt|;
break|break;
block|}
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
comment|/** 	 * @since 3.0 	 */
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
comment|/** 	 * @since 3.0 	 */
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|,
literal|"unchecked"
block|}
argument_list|)
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
name|Class
name|type
init|=
name|writeMethod
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
decl_stmt|;
name|Converter
argument_list|<
name|?
argument_list|>
name|converter
init|=
name|ConverterFactory
operator|.
name|factory
operator|.
name|getConverter
argument_list|(
name|type
argument_list|)
decl_stmt|;
try|try
block|{
name|newValue
operator|=
operator|(
name|converter
operator|!=
literal|null
operator|)
condition|?
name|converter
operator|.
name|convert
argument_list|(
name|newValue
argument_list|,
name|type
argument_list|)
else|:
name|newValue
expr_stmt|;
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
literal|"Error writing property: "
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
specifier|private
specifier|static
name|String
name|defaultSetterName
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|checkPropertyName
argument_list|(
name|propertyName
argument_list|)
expr_stmt|;
specifier|final
name|String
name|capitalized
init|=
name|Character
operator|.
name|toUpperCase
argument_list|(
name|propertyName
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|+
name|propertyName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
decl_stmt|;
return|return
literal|"set"
operator|+
name|capitalized
return|;
block|}
specifier|private
specifier|static
name|String
name|defaultGetterName
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|checkPropertyName
argument_list|(
name|propertyName
argument_list|)
expr_stmt|;
specifier|final
name|String
name|capitalized
init|=
name|Character
operator|.
name|toUpperCase
argument_list|(
name|propertyName
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|+
name|propertyName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
decl_stmt|;
return|return
literal|"get"
operator|+
name|capitalized
return|;
block|}
specifier|private
specifier|static
name|String
name|defaultBooleanGetterName
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
name|checkPropertyName
argument_list|(
name|propertyName
argument_list|)
expr_stmt|;
specifier|final
name|String
name|capitalized
init|=
name|Character
operator|.
name|toUpperCase
argument_list|(
name|propertyName
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|+
name|propertyName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
decl_stmt|;
return|return
literal|"is"
operator|+
name|capitalized
return|;
block|}
specifier|private
specifier|static
name|void
name|checkPropertyName
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
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
if|if
condition|(
name|propertyName
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Empty propertyName"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

