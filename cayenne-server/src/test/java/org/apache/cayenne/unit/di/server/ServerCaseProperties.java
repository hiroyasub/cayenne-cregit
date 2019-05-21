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
name|unit
operator|.
name|di
operator|.
name|server
package|;
end_package

begin_comment
comment|/**  * Stores various unit test properties. This object should be bound in a unit test scope.  */
end_comment

begin_class
specifier|public
class|class
name|ServerCaseProperties
block|{
specifier|protected
name|String
name|configurationLocation
decl_stmt|;
specifier|public
name|String
name|getConfigurationLocation
parameter_list|()
block|{
return|return
name|configurationLocation
return|;
block|}
specifier|public
name|void
name|setConfigurationLocation
parameter_list|(
name|String
name|configurationLocation
parameter_list|)
block|{
name|this
operator|.
name|configurationLocation
operator|=
name|configurationLocation
expr_stmt|;
block|}
block|}
end_class

end_unit

