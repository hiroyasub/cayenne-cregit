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
name|BeanInfo
import|;
end_import

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
name|Introspector
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
name|InvocationTargetException
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|Entity
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
comment|/**  * Utility methods to quickly access object properties. This class supports simple and  * nested properties and also conversion of property values to match property type. No  * converter customization is provided yet, so only basic converters for Strings, Numbers  * and primitives are available.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|PropertyUtils
block|{
comment|/**      * Compiles an accessor that can be used for fast access for the nested property of      * the objects of a given class.      *       * @since 3.0      */
specifier|public
specifier|static
name|Accessor
name|createAccessor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|objectClass
parameter_list|,
name|String
name|nestedPropertyName
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
literal|"Null class."
argument_list|)
throw|;
block|}
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|nestedPropertyName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null or empty property name."
argument_list|)
throw|;
block|}
name|StringTokenizer
name|path
init|=
operator|new
name|StringTokenizer
argument_list|(
name|nestedPropertyName
argument_list|,
name|Entity
operator|.
name|PATH_SEPARATOR
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|countTokens
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
operator|new
name|BeanAccessor
argument_list|(
name|objectClass
argument_list|,
name|nestedPropertyName
argument_list|,
literal|null
argument_list|)
return|;
block|}
name|NestedBeanAccessor
name|accessor
init|=
operator|new
name|NestedBeanAccessor
argument_list|(
name|nestedPropertyName
argument_list|)
decl_stmt|;
while|while
condition|(
name|path
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|token
init|=
name|path
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|accessor
operator|.
name|addAccessor
argument_list|(
operator|new
name|BeanAccessor
argument_list|(
name|objectClass
argument_list|,
name|token
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|accessor
return|;
block|}
comment|/**      * Returns object property using JavaBean-compatible introspection with one addition -      * a property can be a dot-separated property name path.      */
specifier|public
specifier|static
name|Object
name|getProperty
parameter_list|(
name|Object
name|object
parameter_list|,
name|String
name|nestedPropertyName
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null object."
argument_list|)
throw|;
block|}
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|nestedPropertyName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null or empty property name."
argument_list|)
throw|;
block|}
name|StringTokenizer
name|path
init|=
operator|new
name|StringTokenizer
argument_list|(
name|nestedPropertyName
argument_list|,
name|Entity
operator|.
name|PATH_SEPARATOR
argument_list|)
decl_stmt|;
name|int
name|len
init|=
name|path
operator|.
name|countTokens
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|object
decl_stmt|;
name|String
name|pathSegment
init|=
literal|null
decl_stmt|;
try|try
block|{
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|len
condition|;
name|i
operator|++
control|)
block|{
name|pathSegment
operator|=
name|path
operator|.
name|nextToken
argument_list|()
expr_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
comment|// null value in the middle....
throw|throw
operator|new
name|UnresolvablePathException
argument_list|(
literal|"Null value in the middle of the path, failed on "
operator|+
name|nestedPropertyName
operator|+
literal|" from "
operator|+
name|object
argument_list|)
throw|;
block|}
name|value
operator|=
name|getSimpleProperty
argument_list|(
name|value
argument_list|,
name|pathSegment
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|String
name|objectType
init|=
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|"<null>"
decl_stmt|;
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error reading property segment '"
operator|+
name|pathSegment
operator|+
literal|"' in path '"
operator|+
name|nestedPropertyName
operator|+
literal|"' for type "
operator|+
name|objectType
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Sets object property using JavaBean-compatible introspection with one addition - a      * property can be a dot-separated property name path. Before setting a value attempts      * to convert it to a type compatible with the object property. Automatic conversion      * is supported between strings and basic types like numbers or primitives.      */
specifier|public
specifier|static
name|void
name|setProperty
parameter_list|(
name|Object
name|object
parameter_list|,
name|String
name|nestedPropertyName
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|CayenneRuntimeException
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null object."
argument_list|)
throw|;
block|}
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|nestedPropertyName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null or invalid property name."
argument_list|)
throw|;
block|}
name|int
name|dot
init|=
name|nestedPropertyName
operator|.
name|lastIndexOf
argument_list|(
name|Entity
operator|.
name|PATH_SEPARATOR
argument_list|)
decl_stmt|;
name|String
name|lastSegment
decl_stmt|;
if|if
condition|(
name|dot
operator|>
literal|0
condition|)
block|{
name|lastSegment
operator|=
name|nestedPropertyName
operator|.
name|substring
argument_list|(
name|dot
operator|+
literal|1
argument_list|)
expr_stmt|;
name|String
name|pathSegment
init|=
name|nestedPropertyName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|dot
argument_list|)
decl_stmt|;
name|object
operator|=
name|getProperty
argument_list|(
name|object
argument_list|,
name|pathSegment
argument_list|)
expr_stmt|;
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null object at the end of the segment '"
operator|+
name|pathSegment
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|lastSegment
operator|=
name|nestedPropertyName
expr_stmt|;
block|}
try|try
block|{
name|setSimpleProperty
argument_list|(
name|object
argument_list|,
name|lastSegment
argument_list|,
name|value
argument_list|)
expr_stmt|;
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
literal|"Error setting property segment '"
operator|+
name|lastSegment
operator|+
literal|"' in path '"
operator|+
name|nestedPropertyName
operator|+
literal|"'"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|static
name|Object
name|getSimpleProperty
parameter_list|(
name|Object
name|object
parameter_list|,
name|String
name|pathSegment
parameter_list|)
throws|throws
name|IntrospectionException
throws|,
name|IllegalArgumentException
throws|,
name|IllegalAccessException
throws|,
name|InvocationTargetException
block|{
name|PropertyDescriptor
name|descriptor
init|=
name|getPropertyDescriptor
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
argument_list|,
name|pathSegment
argument_list|)
decl_stmt|;
if|if
condition|(
name|descriptor
operator|!=
literal|null
condition|)
block|{
name|Method
name|reader
init|=
name|descriptor
operator|.
name|getReadMethod
argument_list|()
decl_stmt|;
if|if
condition|(
name|reader
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IntrospectionException
argument_list|(
literal|"Unreadable property '"
operator|+
name|pathSegment
operator|+
literal|"'"
argument_list|)
throw|;
block|}
return|return
name|reader
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
comment|// note that Map has two traditional bean properties - 'empty' and 'class', so
comment|// do a check here only after descriptor lookup failed.
if|else if
condition|(
name|object
operator|instanceof
name|Map
condition|)
block|{
return|return
operator|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|object
operator|)
operator|.
name|get
argument_list|(
name|pathSegment
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IntrospectionException
argument_list|(
literal|"No property '"
operator|+
name|pathSegment
operator|+
literal|"' found in class "
operator|+
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|static
name|void
name|setSimpleProperty
parameter_list|(
name|Object
name|object
parameter_list|,
name|String
name|pathSegment
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|IntrospectionException
throws|,
name|IllegalArgumentException
throws|,
name|IllegalAccessException
throws|,
name|InvocationTargetException
block|{
name|PropertyDescriptor
name|descriptor
init|=
name|getPropertyDescriptor
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
argument_list|,
name|pathSegment
argument_list|)
decl_stmt|;
if|if
condition|(
name|descriptor
operator|!=
literal|null
condition|)
block|{
name|Method
name|writer
init|=
name|descriptor
operator|.
name|getWriteMethod
argument_list|()
decl_stmt|;
if|if
condition|(
name|writer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IntrospectionException
argument_list|(
literal|"Unwritable property '"
operator|+
name|pathSegment
operator|+
literal|"'"
argument_list|)
throw|;
block|}
comment|// do basic conversions
name|value
operator|=
name|ConverterFactory
operator|.
name|factory
operator|.
name|getConverter
argument_list|(
name|descriptor
operator|.
name|getPropertyType
argument_list|()
argument_list|)
operator|.
name|convert
argument_list|(
name|value
argument_list|,
name|descriptor
operator|.
name|getPropertyType
argument_list|()
argument_list|)
expr_stmt|;
comment|// set
name|writer
operator|.
name|invoke
argument_list|(
name|object
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|// note that Map has two traditional bean properties - 'empty' and 'class', so
comment|// do a check here only after descriptor lookup failed.
if|else if
condition|(
name|object
operator|instanceof
name|Map
condition|)
block|{
operator|(
operator|(
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
operator|)
name|object
operator|)
operator|.
name|put
argument_list|(
name|pathSegment
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IntrospectionException
argument_list|(
literal|"No property '"
operator|+
name|pathSegment
operator|+
literal|"' found in class "
operator|+
name|object
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|static
name|PropertyDescriptor
name|getPropertyDescriptor
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanClass
parameter_list|,
name|String
name|propertyName
parameter_list|)
throws|throws
name|IntrospectionException
block|{
comment|// bean info is cached by introspector, so this should have reasonable
comment|// performance...
name|BeanInfo
name|info
init|=
name|Introspector
operator|.
name|getBeanInfo
argument_list|(
name|beanClass
argument_list|)
decl_stmt|;
name|PropertyDescriptor
index|[]
name|descriptors
init|=
name|info
operator|.
name|getPropertyDescriptors
argument_list|()
decl_stmt|;
for|for
control|(
name|PropertyDescriptor
name|descriptor
range|:
name|descriptors
control|)
block|{
if|if
condition|(
name|propertyName
operator|.
name|equals
argument_list|(
name|descriptor
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|descriptor
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * "Normalizes" passed type, converting primitive types to their object counterparts.      */
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
name|normalizeType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
name|String
name|className
init|=
name|type
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"byte"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
name|Byte
operator|.
name|class
return|;
block|}
if|else if
condition|(
literal|"int"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
name|Integer
operator|.
name|class
return|;
block|}
if|else if
condition|(
literal|"short"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
name|Short
operator|.
name|class
return|;
block|}
if|else if
condition|(
literal|"char"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
name|Character
operator|.
name|class
return|;
block|}
if|else if
condition|(
literal|"double"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
name|Double
operator|.
name|class
return|;
block|}
if|else if
condition|(
literal|"float"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
name|Float
operator|.
name|class
return|;
block|}
if|else if
condition|(
literal|"boolean"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|class
return|;
block|}
block|}
return|return
name|type
return|;
block|}
comment|/**      * Returns default value that should be used for nulls. For non-primitive types, null      * is returned. For primitive types a default such as zero or false is returned.      */
specifier|static
name|Object
name|defaultNullValueForType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|!=
literal|null
operator|&&
name|type
operator|.
name|isPrimitive
argument_list|()
condition|)
block|{
name|String
name|className
init|=
name|type
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"byte"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
name|Byte
operator|.
name|valueOf
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
return|;
block|}
if|else if
condition|(
literal|"int"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
name|Integer
operator|.
name|valueOf
argument_list|(
literal|0
argument_list|)
return|;
block|}
if|else if
condition|(
literal|"short"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
name|Short
operator|.
name|valueOf
argument_list|(
operator|(
name|short
operator|)
literal|0
argument_list|)
return|;
block|}
if|else if
condition|(
literal|"char"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
name|Character
operator|.
name|valueOf
argument_list|(
operator|(
name|char
operator|)
literal|0
argument_list|)
return|;
block|}
if|else if
condition|(
literal|"double"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
operator|new
name|Double
argument_list|(
literal|0.0d
argument_list|)
return|;
block|}
if|else if
condition|(
literal|"float"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
operator|new
name|Float
argument_list|(
literal|0.0f
argument_list|)
return|;
block|}
if|else if
condition|(
literal|"boolean"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|PropertyUtils
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|static
specifier|final
class|class
name|NestedBeanAccessor
implements|implements
name|Accessor
block|{
specifier|private
name|Collection
argument_list|<
name|Accessor
argument_list|>
name|accessors
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
name|NestedBeanAccessor
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|accessors
operator|=
operator|new
name|ArrayList
argument_list|<
name|Accessor
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
name|void
name|addAccessor
parameter_list|(
name|Accessor
name|accessor
parameter_list|)
block|{
name|accessors
operator|.
name|add
argument_list|(
name|accessor
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
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
name|Object
name|value
init|=
name|object
decl_stmt|;
for|for
control|(
name|Accessor
name|accessor
range|:
name|accessors
control|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null object at the end of the segment '"
operator|+
name|accessor
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|value
operator|=
name|accessor
operator|.
name|getValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
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
name|Object
name|value
init|=
name|object
decl_stmt|;
name|Iterator
argument_list|<
name|Accessor
argument_list|>
name|accessors
init|=
name|this
operator|.
name|accessors
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|accessors
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Accessor
name|accessor
init|=
name|accessors
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|accessors
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|value
operator|=
name|accessor
operator|.
name|getValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|accessor
operator|.
name|setValue
argument_list|(
name|value
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

