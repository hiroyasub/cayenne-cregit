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
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
comment|/**  * A factory of property type converters.  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|ConverterFactory
block|{
specifier|private
specifier|static
specifier|final
name|String
name|FACTORY_CLASS_JDK15
init|=
literal|"org.apache.cayenne.reflect.ConverterFactory15"
decl_stmt|;
specifier|static
specifier|final
name|ConverterFactory
name|factory
init|=
name|createFactory
argument_list|()
decl_stmt|;
specifier|static
name|Map
name|converters
decl_stmt|;
specifier|static
specifier|final
name|Converter
name|noopConverter
init|=
operator|new
name|Converter
argument_list|()
block|{
name|Object
name|convert
parameter_list|(
name|Object
name|object
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
return|return
name|object
return|;
block|}
block|}
decl_stmt|;
static|static
block|{
name|Converter
name|stringConverter
init|=
operator|new
name|Converter
argument_list|()
block|{
name|Object
name|convert
parameter_list|(
name|Object
name|object
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
return|return
name|object
operator|!=
literal|null
condition|?
name|object
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
block|}
decl_stmt|;
name|Converter
name|booleanConverter
init|=
operator|new
name|Converter
argument_list|()
block|{
name|Object
name|convert
parameter_list|(
name|Object
name|object
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
name|type
operator|.
name|isPrimitive
argument_list|()
condition|?
name|Boolean
operator|.
name|FALSE
else|:
literal|null
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|Boolean
condition|)
block|{
return|return
name|object
return|;
block|}
return|return
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|object
operator|.
name|toString
argument_list|()
argument_list|)
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
return|;
block|}
block|}
decl_stmt|;
name|Converter
name|intConverter
init|=
operator|new
name|Converter
argument_list|()
block|{
name|Object
name|convert
parameter_list|(
name|Object
name|object
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
name|type
operator|.
name|isPrimitive
argument_list|()
condition|?
operator|new
name|Integer
argument_list|(
literal|0
argument_list|)
else|:
literal|null
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|Integer
condition|)
block|{
return|return
name|object
return|;
block|}
return|return
operator|new
name|Integer
argument_list|(
name|object
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|Converter
name|byteConverter
init|=
operator|new
name|Converter
argument_list|()
block|{
name|Object
name|convert
parameter_list|(
name|Object
name|object
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
name|type
operator|.
name|isPrimitive
argument_list|()
condition|?
operator|new
name|Byte
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
else|:
literal|null
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|Byte
condition|)
block|{
return|return
name|object
return|;
block|}
return|return
operator|new
name|Byte
argument_list|(
name|object
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|Converter
name|shortConverter
init|=
operator|new
name|Converter
argument_list|()
block|{
name|Object
name|convert
parameter_list|(
name|Object
name|object
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
name|type
operator|.
name|isPrimitive
argument_list|()
condition|?
operator|new
name|Short
argument_list|(
operator|(
name|short
operator|)
literal|0
argument_list|)
else|:
literal|null
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|Short
condition|)
block|{
return|return
name|object
return|;
block|}
return|return
operator|new
name|Short
argument_list|(
name|object
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|Converter
name|charConverter
init|=
operator|new
name|Converter
argument_list|()
block|{
name|Object
name|convert
parameter_list|(
name|Object
name|object
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
name|type
operator|.
name|isPrimitive
argument_list|()
condition|?
operator|new
name|Character
argument_list|(
operator|(
name|char
operator|)
literal|0
argument_list|)
else|:
literal|null
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|Character
condition|)
block|{
return|return
name|object
return|;
block|}
name|String
name|string
init|=
name|object
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
operator|new
name|Character
argument_list|(
name|string
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|string
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
else|:
literal|0
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|Converter
name|doubleConverter
init|=
operator|new
name|Converter
argument_list|()
block|{
name|Object
name|convert
parameter_list|(
name|Object
name|object
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
name|type
operator|.
name|isPrimitive
argument_list|()
condition|?
operator|new
name|Double
argument_list|(
literal|0.0d
argument_list|)
else|:
literal|null
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|Double
condition|)
block|{
return|return
name|object
return|;
block|}
return|return
operator|new
name|Double
argument_list|(
name|object
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|Converter
name|floatConverter
init|=
operator|new
name|Converter
argument_list|()
block|{
name|Object
name|convert
parameter_list|(
name|Object
name|object
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return
name|type
operator|.
name|isPrimitive
argument_list|()
condition|?
operator|new
name|Float
argument_list|(
literal|0.0f
argument_list|)
else|:
literal|null
return|;
block|}
if|if
condition|(
name|object
operator|instanceof
name|Float
condition|)
block|{
return|return
name|object
return|;
block|}
return|return
operator|new
name|Float
argument_list|(
name|object
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|Converter
name|bigDecimalConverter
init|=
operator|new
name|Converter
argument_list|()
block|{
name|Object
name|convert
parameter_list|(
name|Object
name|object
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
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
if|if
condition|(
name|object
operator|instanceof
name|BigDecimal
condition|)
block|{
return|return
name|object
return|;
block|}
return|return
operator|new
name|BigDecimal
argument_list|(
name|object
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|Converter
name|bigIntegerConverter
init|=
operator|new
name|Converter
argument_list|()
block|{
name|Object
name|convert
parameter_list|(
name|Object
name|object
parameter_list|,
name|Class
name|type
parameter_list|)
block|{
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
if|if
condition|(
name|object
operator|instanceof
name|BigInteger
condition|)
block|{
return|return
name|object
return|;
block|}
return|return
operator|new
name|BigInteger
argument_list|(
name|object
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
comment|// TODO: byte[] converter...
name|converters
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
name|Boolean
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|booleanConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
literal|"boolean"
argument_list|,
name|booleanConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
name|Short
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|shortConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
literal|"short"
argument_list|,
name|shortConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
name|Byte
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|byteConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
literal|"byte"
argument_list|,
name|byteConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|intConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
literal|"int"
argument_list|,
name|intConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
name|Double
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|doubleConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
literal|"double"
argument_list|,
name|doubleConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
name|Float
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|floatConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
literal|"float"
argument_list|,
name|floatConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
name|Character
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|charConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
literal|"char"
argument_list|,
name|charConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
name|BigDecimal
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|bigDecimalConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
name|BigInteger
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|bigIntegerConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
name|Number
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|bigDecimalConverter
argument_list|)
expr_stmt|;
name|converters
operator|.
name|put
argument_list|(
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|stringConverter
argument_list|)
expr_stmt|;
block|}
specifier|static
name|ConverterFactory
name|createFactory
parameter_list|()
block|{
try|try
block|{
comment|// sniff JDK 1.5
name|Class
operator|.
name|forName
argument_list|(
literal|"java.lang.StringBuilder"
argument_list|)
expr_stmt|;
name|Class
name|factoryClass
init|=
name|Util
operator|.
name|getJavaClass
argument_list|(
name|FACTORY_CLASS_JDK15
argument_list|)
decl_stmt|;
return|return
operator|(
name|ConverterFactory
operator|)
name|factoryClass
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
comment|// .. jdk 1.4
return|return
operator|new
name|ConverterFactory
argument_list|()
return|;
block|}
block|}
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
name|Converter
name|c
init|=
operator|(
name|Converter
operator|)
name|converters
operator|.
name|get
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|c
operator|!=
literal|null
condition|?
name|c
else|:
name|noopConverter
return|;
block|}
block|}
end_class

end_unit

