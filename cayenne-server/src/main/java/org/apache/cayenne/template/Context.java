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
name|template
package|;
end_package

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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|access
operator|.
name|jdbc
operator|.
name|ColumnDescriptor
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
name|access
operator|.
name|translator
operator|.
name|ParameterBinding
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
name|template
operator|.
name|directive
operator|.
name|Bind
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
name|template
operator|.
name|directive
operator|.
name|BindEqual
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
name|template
operator|.
name|directive
operator|.
name|BindNotEqual
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
name|template
operator|.
name|directive
operator|.
name|BindObjectEqual
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
name|template
operator|.
name|directive
operator|.
name|BindObjectNotEqual
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
name|template
operator|.
name|directive
operator|.
name|Directive
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
name|template
operator|.
name|directive
operator|.
name|Result
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|Context
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Directive
argument_list|>
name|directives
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|objects
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parameterAliases
decl_stmt|;
name|List
argument_list|<
name|ParameterBinding
argument_list|>
name|parameterBindings
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ColumnDescriptor
argument_list|>
name|columnDescriptors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|positionalMode
decl_stmt|;
name|int
name|counter
decl_stmt|;
specifier|public
name|Context
parameter_list|()
block|{
name|addDirective
argument_list|(
literal|"result"
argument_list|,
name|Result
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|addDirective
argument_list|(
literal|"bind"
argument_list|,
name|Bind
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|addDirective
argument_list|(
literal|"bindEqual"
argument_list|,
name|BindEqual
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|addDirective
argument_list|(
literal|"bindNotEqual"
argument_list|,
name|BindNotEqual
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|addDirective
argument_list|(
literal|"bindObjectEqual"
argument_list|,
name|BindObjectEqual
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|addDirective
argument_list|(
literal|"bindObjectNotEqual"
argument_list|,
name|BindObjectNotEqual
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|addParameter
argument_list|(
literal|"helper"
argument_list|,
operator|new
name|SQLTemplateRenderingUtils
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Context
parameter_list|(
name|boolean
name|positionalMode
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|positionalMode
operator|=
name|positionalMode
expr_stmt|;
if|if
condition|(
name|positionalMode
condition|)
block|{
name|parameterAliases
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|Directive
name|getDirective
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|directives
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|StringBuilder
name|getBuilder
parameter_list|()
block|{
return|return
name|builder
return|;
block|}
specifier|public
name|String
name|buildTemplate
parameter_list|()
block|{
if|if
condition|(
name|positionalMode
condition|)
block|{
if|if
condition|(
name|counter
operator|<=
name|objects
operator|.
name|size
argument_list|()
operator|-
literal|2
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Too many parameters to bind template: "
operator|+
operator|(
name|objects
operator|.
name|size
argument_list|()
operator|-
literal|1
operator|)
argument_list|)
throw|;
block|}
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|haveObject
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|objects
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Object
name|getObject
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Object
name|object
init|=
name|objects
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|object
operator|!=
literal|null
condition|)
block|{
return|return
name|object
return|;
block|}
if|if
condition|(
name|positionalMode
condition|)
block|{
name|String
name|alias
init|=
name|parameterAliases
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|alias
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|counter
operator|>
name|objects
operator|.
name|size
argument_list|()
operator|-
literal|2
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Too few parameters to bind template: "
operator|+
operator|(
name|objects
operator|.
name|size
argument_list|()
operator|-
literal|1
operator|)
argument_list|)
throw|;
block|}
name|alias
operator|=
name|String
operator|.
name|valueOf
argument_list|(
name|counter
operator|++
argument_list|)
expr_stmt|;
name|parameterAliases
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|alias
argument_list|)
expr_stmt|;
block|}
comment|// give next object on each invocation of method
return|return
name|objects
operator|.
name|get
argument_list|(
name|alias
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|addParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|objects
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|parameters
parameter_list|)
block|{
name|objects
operator|.
name|putAll
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addDirective
parameter_list|(
name|String
name|name
parameter_list|,
name|Directive
name|directive
parameter_list|)
block|{
name|directives
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|directive
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addParameterBinding
parameter_list|(
name|ParameterBinding
name|binding
parameter_list|)
block|{
name|parameterBindings
operator|.
name|add
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addColumnDescriptor
parameter_list|(
name|ColumnDescriptor
name|descriptor
parameter_list|)
block|{
name|columnDescriptors
operator|.
name|add
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ColumnDescriptor
index|[]
name|getColumnDescriptors
parameter_list|()
block|{
return|return
name|columnDescriptors
operator|.
name|toArray
argument_list|(
operator|new
name|ColumnDescriptor
index|[
literal|0
index|]
argument_list|)
return|;
block|}
specifier|public
name|ParameterBinding
index|[]
name|getParameterBindings
parameter_list|()
block|{
return|return
name|parameterBindings
operator|.
name|toArray
argument_list|(
operator|new
name|ParameterBinding
index|[
literal|0
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

