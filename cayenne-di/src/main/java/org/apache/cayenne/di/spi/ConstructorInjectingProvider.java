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
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|Constructor
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
name|Type
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
name|DIRuntimeException
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
name|Key
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
comment|/**  * @since 3.1  */
end_comment

begin_class
class|class
name|ConstructorInjectingProvider
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Provider
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|Constructor
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|constructor
decl_stmt|;
specifier|private
name|DefaultInjector
name|injector
decl_stmt|;
specifier|private
name|String
index|[]
name|bindingNames
decl_stmt|;
name|ConstructorInjectingProvider
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|implementation
parameter_list|,
name|DefaultInjector
name|injector
parameter_list|)
block|{
name|initConstructor
argument_list|(
name|implementation
argument_list|)
expr_stmt|;
if|if
condition|(
name|constructor
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DIRuntimeException
argument_list|(
literal|"Can't find approprate constructor for implementation class '%s'"
argument_list|,
name|implementation
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|this
operator|.
name|constructor
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|injector
operator|=
name|injector
expr_stmt|;
block|}
specifier|private
name|void
name|initConstructor
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|implementation
parameter_list|)
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
index|[]
name|constructors
init|=
name|implementation
operator|.
name|getDeclaredConstructors
argument_list|()
decl_stmt|;
name|Constructor
argument_list|<
name|?
argument_list|>
name|lastMatch
init|=
literal|null
decl_stmt|;
name|int
name|lastSize
init|=
operator|-
literal|1
decl_stmt|;
comment|// pick the first constructor with all injection-annotated parameters, or the
comment|// default constructor; constructor with the longest parameter list is preferred
comment|// if multiple matches are found
for|for
control|(
name|Constructor
argument_list|<
name|?
argument_list|>
name|constructor
range|:
name|constructors
control|)
block|{
name|int
name|size
init|=
name|constructor
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|size
operator|<=
name|lastSize
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
name|lastSize
operator|=
literal|0
expr_stmt|;
name|lastMatch
operator|=
name|constructor
expr_stmt|;
continue|continue;
block|}
name|boolean
name|injectable
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Annotation
index|[]
name|annotations
range|:
name|constructor
operator|.
name|getParameterAnnotations
argument_list|()
control|)
block|{
name|boolean
name|parameterInjectable
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Annotation
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|annotation
operator|.
name|annotationType
argument_list|()
operator|.
name|equals
argument_list|(
name|Inject
operator|.
name|class
argument_list|)
condition|)
block|{
name|parameterInjectable
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|parameterInjectable
condition|)
block|{
name|injectable
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|injectable
condition|)
block|{
name|lastSize
operator|=
name|size
expr_stmt|;
name|lastMatch
operator|=
name|constructor
expr_stmt|;
block|}
block|}
if|if
condition|(
name|lastMatch
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DIRuntimeException
argument_list|(
literal|"No applicable constructor is found for constructor injection in class '%s'"
argument_list|,
name|implementation
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
comment|// the cast is lame, but Class.getDeclaredConstructors() is not using
comment|// generics in Java 5 and using<?> in Java 6, creating compilation problems.
name|this
operator|.
name|constructor
operator|=
operator|(
name|Constructor
argument_list|<
name|?
extends|extends
name|T
argument_list|>
operator|)
name|lastMatch
expr_stmt|;
name|Annotation
index|[]
index|[]
name|annotations
init|=
name|lastMatch
operator|.
name|getParameterAnnotations
argument_list|()
decl_stmt|;
name|this
operator|.
name|bindingNames
operator|=
operator|new
name|String
index|[
name|annotations
operator|.
name|length
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|annotations
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Annotation
index|[]
name|parameterAnnotations
init|=
name|annotations
index|[
name|i
index|]
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|parameterAnnotations
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|Annotation
name|annotation
init|=
name|parameterAnnotations
index|[
name|j
index|]
decl_stmt|;
if|if
condition|(
name|annotation
operator|.
name|annotationType
argument_list|()
operator|.
name|equals
argument_list|(
name|Inject
operator|.
name|class
argument_list|)
condition|)
block|{
name|Inject
name|inject
init|=
operator|(
name|Inject
operator|)
name|annotation
decl_stmt|;
name|bindingNames
index|[
name|i
index|]
operator|=
name|inject
operator|.
name|value
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
specifier|public
name|T
name|get
parameter_list|()
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|constructorParameters
init|=
name|constructor
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
name|Type
index|[]
name|genericTypes
init|=
name|constructor
operator|.
name|getGenericParameterTypes
argument_list|()
decl_stmt|;
name|Object
index|[]
name|args
init|=
operator|new
name|Object
index|[
name|constructorParameters
operator|.
name|length
index|]
decl_stmt|;
name|InjectionStack
name|stack
init|=
name|injector
operator|.
name|getInjectionStack
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|constructorParameters
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|parameter
init|=
name|constructorParameters
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|Provider
operator|.
name|class
operator|.
name|equals
argument_list|(
name|parameter
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|objectClass
init|=
name|DIUtil
operator|.
name|parameterClass
argument_list|(
name|genericTypes
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|objectClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|DIRuntimeException
argument_list|(
literal|"Constructor provider parameter %s must be "
operator|+
literal|"parameterized to be usable for injection"
argument_list|,
name|parameter
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|args
index|[
name|i
index|]
operator|=
name|injector
operator|.
name|getProvider
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|objectClass
argument_list|,
name|bindingNames
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Key
argument_list|<
name|?
argument_list|>
name|key
init|=
name|Key
operator|.
name|get
argument_list|(
name|parameter
argument_list|,
name|bindingNames
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|stack
operator|.
name|push
argument_list|(
name|key
argument_list|)
expr_stmt|;
try|try
block|{
name|args
index|[
name|i
index|]
operator|=
name|injector
operator|.
name|getInstance
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|stack
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
try|try
block|{
return|return
name|constructor
operator|.
name|newInstance
argument_list|(
name|args
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
name|DIRuntimeException
argument_list|(
literal|"Error instantiating class '%s'"
argument_list|,
name|e
argument_list|,
name|constructor
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

