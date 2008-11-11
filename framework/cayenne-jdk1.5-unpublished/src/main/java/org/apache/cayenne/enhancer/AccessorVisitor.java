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
name|enhancer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|ClassAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|ClassVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|MethodVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|Type
import|;
end_import

begin_comment
comment|/**  * An enhancer that adds interceptor code to the getters and setters.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AccessorVisitor
extends|extends
name|ClassAdapter
block|{
comment|// duplicated from JpaClassDescriptor.
specifier|private
specifier|static
specifier|final
name|Pattern
name|GETTER_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^(is|get)([A-Z])(.*)$"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|SETTER_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^set([A-Z])(.*)$"
argument_list|)
decl_stmt|;
specifier|private
name|EnhancementHelper
name|helper
decl_stmt|;
specifier|public
specifier|static
name|String
name|propertyNameForGetter
parameter_list|(
name|String
name|getterName
parameter_list|)
block|{
name|Matcher
name|getMatch
init|=
name|GETTER_PATTERN
operator|.
name|matcher
argument_list|(
name|getterName
argument_list|)
decl_stmt|;
if|if
condition|(
name|getMatch
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|getMatch
operator|.
name|group
argument_list|(
literal|2
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|+
name|getMatch
operator|.
name|group
argument_list|(
literal|3
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|String
name|propertyNameForSetter
parameter_list|(
name|String
name|setterName
parameter_list|)
block|{
name|Matcher
name|setMatch
init|=
name|SETTER_PATTERN
operator|.
name|matcher
argument_list|(
name|setterName
argument_list|)
decl_stmt|;
if|if
condition|(
name|setMatch
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|setMatch
operator|.
name|group
argument_list|(
literal|1
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|+
name|setMatch
operator|.
name|group
argument_list|(
literal|2
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|AccessorVisitor
parameter_list|(
name|ClassVisitor
name|cw
parameter_list|)
block|{
name|super
argument_list|(
name|cw
argument_list|)
expr_stmt|;
name|this
operator|.
name|helper
operator|=
operator|new
name|EnhancementHelper
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|boolean
name|isEnhancedProperty
parameter_list|(
name|String
name|property
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|boolean
name|isLazyFaulted
parameter_list|(
name|String
name|property
parameter_list|)
function_decl|;
annotation|@
name|Override
specifier|public
name|void
name|visit
parameter_list|(
name|int
name|version
parameter_list|,
name|int
name|access
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|signature
parameter_list|,
name|String
name|superName
parameter_list|,
name|String
index|[]
name|interfaces
parameter_list|)
block|{
name|helper
operator|.
name|reset
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|super
operator|.
name|visit
argument_list|(
name|version
argument_list|,
name|access
argument_list|,
name|name
argument_list|,
name|signature
argument_list|,
name|superName
argument_list|,
name|interfaces
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|MethodVisitor
name|visitGetter
parameter_list|(
name|MethodVisitor
name|mv
parameter_list|,
name|String
name|property
parameter_list|,
name|Type
name|propertyType
parameter_list|)
block|{
if|if
condition|(
name|isEnhancedProperty
argument_list|(
name|property
argument_list|)
condition|)
block|{
if|if
condition|(
name|isLazyFaulted
argument_list|(
name|property
argument_list|)
condition|)
block|{
return|return
operator|new
name|GetterVisitor
argument_list|(
name|mv
argument_list|,
name|helper
argument_list|,
name|property
argument_list|,
literal|true
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|GetterVisitor
argument_list|(
name|mv
argument_list|,
name|helper
argument_list|,
name|property
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
return|return
name|mv
return|;
block|}
specifier|protected
name|MethodVisitor
name|visitSetter
parameter_list|(
name|MethodVisitor
name|mv
parameter_list|,
name|String
name|property
parameter_list|,
name|Type
name|propertyType
parameter_list|)
block|{
if|if
condition|(
name|isEnhancedProperty
argument_list|(
name|property
argument_list|)
condition|)
block|{
return|return
operator|new
name|SetterVisitor
argument_list|(
name|mv
argument_list|,
name|helper
argument_list|,
name|property
argument_list|,
name|propertyType
argument_list|)
return|;
block|}
return|return
name|mv
return|;
block|}
annotation|@
name|Override
specifier|public
name|MethodVisitor
name|visitMethod
parameter_list|(
name|int
name|access
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|desc
parameter_list|,
name|String
name|signature
parameter_list|,
name|String
index|[]
name|exceptions
parameter_list|)
block|{
name|MethodVisitor
name|mv
init|=
name|super
operator|.
name|visitMethod
argument_list|(
name|access
argument_list|,
name|name
argument_list|,
name|desc
argument_list|,
name|signature
argument_list|,
name|exceptions
argument_list|)
decl_stmt|;
comment|// TODO: andrus, 10/8/2006 - what other signature checks do we need to do?
name|Type
name|returnType
init|=
name|Type
operator|.
name|getReturnType
argument_list|(
name|desc
argument_list|)
decl_stmt|;
name|Type
index|[]
name|args
init|=
name|Type
operator|.
name|getArgumentTypes
argument_list|(
name|desc
argument_list|)
decl_stmt|;
comment|// possible setter
if|if
condition|(
literal|"V"
operator|.
name|equals
argument_list|(
name|returnType
operator|.
name|getDescriptor
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|String
name|setProperty
init|=
name|AccessorVisitor
operator|.
name|propertyNameForSetter
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|setProperty
operator|!=
literal|null
condition|)
block|{
return|return
name|visitSetter
argument_list|(
name|mv
argument_list|,
name|setProperty
argument_list|,
name|args
index|[
literal|0
index|]
argument_list|)
return|;
block|}
block|}
block|}
comment|// possible getter
if|else if
condition|(
name|args
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|String
name|getProperty
init|=
name|AccessorVisitor
operator|.
name|propertyNameForGetter
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|getProperty
operator|!=
literal|null
condition|)
block|{
return|return
name|visitGetter
argument_list|(
name|mv
argument_list|,
name|getProperty
argument_list|,
name|returnType
argument_list|)
return|;
block|}
block|}
return|return
name|mv
return|;
block|}
block|}
end_class

end_unit

