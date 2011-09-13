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
name|di
operator|.
name|spi
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
name|di
operator|.
name|AdhocObjectFactory
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
name|di
operator|.
name|Inject
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
name|di
operator|.
name|Injector
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
name|di
operator|.
name|Provider
import|;
end_import

begin_comment
comment|/**  * A default implementation of {@link AdhocObjectFactory} that creates objects using  * default no-arg constructor and injects dependencies into annotated fields. Note that  * constructor injection is not supported by this factory.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DefaultAdhocObjectFactory
implements|implements
name|AdhocObjectFactory
block|{
annotation|@
name|Inject
specifier|protected
name|Injector
name|injector
decl_stmt|;
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|newInstance
parameter_list|(
name|Class
argument_list|<
name|?
super|super
name|T
argument_list|>
name|superType
parameter_list|,
name|String
name|className
parameter_list|)
block|{
if|if
condition|(
name|superType
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null superType"
argument_list|)
throw|;
block|}
if|if
condition|(
name|className
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null className"
argument_list|)
throw|;
block|}
name|Class
argument_list|<
name|T
argument_list|>
name|type
decl_stmt|;
try|try
block|{
name|type
operator|=
operator|(
name|Class
argument_list|<
name|T
argument_list|>
operator|)
name|getJavaClass
argument_list|(
name|className
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid class %s of type %s"
argument_list|,
name|e
argument_list|,
name|className
argument_list|,
name|superType
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|superType
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Class %s is not assignable to %s"
argument_list|,
name|className
argument_list|,
name|superType
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|T
name|instance
decl_stmt|;
try|try
block|{
name|Provider
argument_list|<
name|T
argument_list|>
name|provider0
init|=
operator|new
name|ConstructorInjectingProvider
argument_list|<
name|T
argument_list|>
argument_list|(
name|type
argument_list|,
operator|(
name|DefaultInjector
operator|)
name|injector
argument_list|)
decl_stmt|;
name|Provider
argument_list|<
name|T
argument_list|>
name|provider1
init|=
operator|new
name|FieldInjectingProvider
argument_list|<
name|T
argument_list|>
argument_list|(
name|provider0
argument_list|,
operator|(
name|DefaultInjector
operator|)
name|injector
argument_list|)
decl_stmt|;
name|instance
operator|=
name|provider1
operator|.
name|get
argument_list|()
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
literal|"Error creating instance of class %s of type %s"
argument_list|,
name|e
argument_list|,
name|className
argument_list|,
name|superType
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|instance
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getJavaClass
parameter_list|(
name|String
name|className
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
comment|// is there a better way to get array class from string name?
if|if
condition|(
name|className
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
literal|"Null class name"
argument_list|)
throw|;
block|}
name|ClassLoader
name|classLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|classLoader
operator|==
literal|null
condition|)
block|{
name|classLoader
operator|=
name|DefaultAdhocObjectFactory
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
expr_stmt|;
block|}
comment|// use custom logic on failure only, assuming primitives and arrays are not that
comment|// common
try|try
block|{
return|return
name|Class
operator|.
name|forName
argument_list|(
name|className
argument_list|,
literal|true
argument_list|,
name|classLoader
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|className
operator|.
name|endsWith
argument_list|(
literal|"[]"
argument_list|)
condition|)
block|{
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
name|TYPE
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
name|TYPE
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
name|TYPE
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
name|TYPE
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
name|TYPE
return|;
block|}
if|else if
condition|(
literal|"long"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
name|Long
operator|.
name|TYPE
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
name|TYPE
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
name|TYPE
return|;
block|}
comment|// try inner class often specified with "." instead of $
else|else
block|{
name|int
name|dot
init|=
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|dot
operator|>
literal|0
operator|&&
name|dot
operator|+
literal|1
operator|<
name|className
operator|.
name|length
argument_list|()
condition|)
block|{
name|className
operator|=
name|className
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|dot
argument_list|)
operator|+
literal|"$"
operator|+
name|className
operator|.
name|substring
argument_list|(
name|dot
operator|+
literal|1
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|Class
operator|.
name|forName
argument_list|(
name|className
argument_list|,
literal|true
argument_list|,
name|classLoader
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|nestedE
parameter_list|)
block|{
comment|// ignore, throw the original exception...
block|}
block|}
block|}
throw|throw
name|e
throw|;
block|}
if|if
condition|(
name|className
operator|.
name|length
argument_list|()
operator|<
literal|3
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid class name: "
operator|+
name|className
argument_list|)
throw|;
block|}
comment|// TODO: support for multi-dim arrays
name|className
operator|=
name|className
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|className
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
expr_stmt|;
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
name|byte
index|[]
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
name|int
index|[]
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
name|short
index|[]
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
name|char
index|[]
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
name|double
index|[]
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
name|float
index|[]
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
name|boolean
index|[]
operator|.
name|class
return|;
block|}
return|return
name|Class
operator|.
name|forName
argument_list|(
literal|"[L"
operator|+
name|className
operator|+
literal|";"
argument_list|,
literal|true
argument_list|,
name|classLoader
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

