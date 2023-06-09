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
name|lang
operator|.
name|reflect
operator|.
name|Modifier
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * Defines a generic callback operation executed via reflection on a persistent object.  * Note that the method must be declared in the class itself. Callback will not look up  * the class hierarchy.  *   * @since 3.0  */
end_comment

begin_class
class|class
name|CallbackOnEntity
extends|extends
name|AbstractCallback
block|{
specifier|private
specifier|final
name|Method
name|callbackMethod
decl_stmt|;
name|CallbackOnEntity
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|objectClass
parameter_list|,
name|String
name|methodName
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|this
argument_list|(
name|findMethod
argument_list|(
name|objectClass
argument_list|,
name|methodName
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 4.2      */
name|CallbackOnEntity
parameter_list|(
name|Method
name|method
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
operator|!
name|validateMethod
argument_list|(
name|method
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Class "
operator|+
name|method
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" has no valid callback method '"
operator|+
name|method
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|this
operator|.
name|callbackMethod
operator|=
name|method
expr_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|isAccessible
argument_list|(
name|callbackMethod
argument_list|)
condition|)
block|{
name|callbackMethod
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|performCallback
parameter_list|(
name|Object
name|entity
parameter_list|)
block|{
try|try
block|{
name|callbackMethod
operator|.
name|invoke
argument_list|(
name|entity
argument_list|,
operator|(
name|Object
index|[]
operator|)
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
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error invoking entity callback method "
operator|+
name|callbackMethod
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"callback-entity: "
operator|+
name|callbackMethod
operator|.
name|getDeclaringClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|callbackMethod
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|static
specifier|private
name|boolean
name|validateMethod
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|int
name|modifiers
init|=
name|method
operator|.
name|getModifiers
argument_list|()
decl_stmt|;
comment|// must be non-static, void, with no args
comment|// JPA spec also requires it to be non-final, but we don't care
return|return
operator|!
name|Modifier
operator|.
name|isStatic
argument_list|(
name|modifiers
argument_list|)
operator|&&
name|Void
operator|.
name|TYPE
operator|.
name|isAssignableFrom
argument_list|(
name|method
operator|.
name|getReturnType
argument_list|()
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
return|;
block|}
specifier|static
specifier|private
name|Method
name|findMethod
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|objectClass
parameter_list|,
name|String
name|methodName
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|Method
name|method
decl_stmt|;
try|try
block|{
name|method
operator|=
name|objectClass
operator|.
name|getDeclaredMethod
argument_list|(
name|methodName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Class "
operator|+
name|objectClass
operator|.
name|getName
argument_list|()
operator|+
literal|" has no valid callback method '"
operator|+
name|methodName
operator|+
literal|"'"
argument_list|)
throw|;
block|}
return|return
name|method
return|;
block|}
block|}
end_class

end_unit

