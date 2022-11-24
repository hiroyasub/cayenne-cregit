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
name|gen
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/**  * Class stores set of CgenConfigurations using for classes generation  *  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|CgenConfigList
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_CONFIG_NAME
init|=
literal|"Default"
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|CgenConfiguration
argument_list|>
name|configurations
decl_stmt|;
specifier|public
name|CgenConfigList
parameter_list|()
block|{
name|this
operator|.
name|configurations
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|CgenConfiguration
name|getByName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|configurations
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|config
lambda|->
name|config
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
argument_list|)
operator|.
name|findFirst
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
return|;
block|}
specifier|public
name|void
name|removeByName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|configurations
operator|.
name|removeIf
argument_list|(
name|configuration
lambda|->
name|configuration
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|add
parameter_list|(
name|CgenConfiguration
name|configuration
parameter_list|)
block|{
name|configurations
operator|.
name|add
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getNames
parameter_list|()
block|{
return|return
name|configurations
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|CgenConfiguration
operator|::
name|getName
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|CgenConfiguration
argument_list|>
name|getAll
parameter_list|()
block|{
return|return
name|configurations
return|;
block|}
specifier|public
name|boolean
name|isExist
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|configurations
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|c
lambda|->
name|name
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

