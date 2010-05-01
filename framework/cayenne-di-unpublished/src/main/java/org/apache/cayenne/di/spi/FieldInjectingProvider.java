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
name|reflect
operator|.
name|Field
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
name|ConfigurationException
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
name|FieldInjectingProvider
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
name|Key
argument_list|<
name|?
argument_list|>
name|bindingKey
decl_stmt|;
specifier|private
name|DefaultInjector
name|injector
decl_stmt|;
specifier|private
name|Provider
argument_list|<
name|T
argument_list|>
name|delegate
decl_stmt|;
name|FieldInjectingProvider
parameter_list|(
name|Provider
argument_list|<
name|T
argument_list|>
name|delegate
parameter_list|,
name|DefaultInjector
name|injector
parameter_list|,
name|Key
argument_list|<
name|?
argument_list|>
name|bindingKey
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|injector
operator|=
name|injector
expr_stmt|;
name|this
operator|.
name|bindingKey
operator|=
name|bindingKey
expr_stmt|;
block|}
specifier|public
name|T
name|get
parameter_list|()
throws|throws
name|ConfigurationException
block|{
name|T
name|object
init|=
name|delegate
operator|.
name|get
argument_list|()
decl_stmt|;
name|injectMembers
argument_list|(
name|object
argument_list|,
name|object
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|object
return|;
block|}
specifier|private
name|void
name|injectMembers
parameter_list|(
name|T
name|object
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
comment|// bail on recursion stop condition
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Field
name|field
range|:
name|type
operator|.
name|getDeclaredFields
argument_list|()
control|)
block|{
name|Inject
name|inject
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|Inject
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|inject
operator|!=
literal|null
condition|)
block|{
name|injectMember
argument_list|(
name|object
argument_list|,
name|field
argument_list|,
name|inject
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|injectMembers
argument_list|(
name|object
argument_list|,
name|type
operator|.
name|getSuperclass
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|injectMember
parameter_list|(
name|Object
name|object
parameter_list|,
name|Field
name|field
parameter_list|,
name|String
name|bindingName
parameter_list|)
block|{
name|InjectionStack
name|stack
init|=
name|injector
operator|.
name|getInjectionStack
argument_list|()
decl_stmt|;
name|Object
name|value
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|fieldType
init|=
name|field
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|Provider
operator|.
name|class
operator|.
name|equals
argument_list|(
name|fieldType
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
name|field
operator|.
name|getGenericType
argument_list|()
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
name|ConfigurationException
argument_list|(
literal|"Provider field %s.%s of type %s must be "
operator|+
literal|"parameterized to be usable for injection"
argument_list|,
name|field
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|field
operator|.
name|getName
argument_list|()
argument_list|,
name|fieldType
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|value
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
name|bindingName
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stack
operator|.
name|push
argument_list|(
name|bindingKey
argument_list|)
expr_stmt|;
try|try
block|{
name|value
operator|=
name|injector
operator|.
name|getInstance
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|fieldType
argument_list|,
name|bindingName
argument_list|)
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
name|field
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|field
operator|.
name|set
argument_list|(
name|object
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
name|String
name|message
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Error injecting into field %s.%s of type %s"
argument_list|,
name|field
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|field
operator|.
name|getName
argument_list|()
argument_list|,
name|fieldType
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ConfigurationException
argument_list|(
name|message
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

