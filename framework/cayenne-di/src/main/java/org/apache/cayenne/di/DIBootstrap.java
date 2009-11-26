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
name|di
operator|.
name|spi
operator|.
name|DefaultInjector
import|;
end_import

begin_comment
comment|/**  * A class that bootstraps the Cayenne DI container.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DIBootstrap
block|{
comment|/**      * Creates and returns an injector instance working with the set of provided modules.      */
specifier|public
specifier|static
name|Injector
name|createInjector
parameter_list|(
name|Module
modifier|...
name|modules
parameter_list|)
throws|throws
name|DIException
block|{
return|return
operator|new
name|DefaultInjector
argument_list|(
name|modules
argument_list|)
return|;
block|}
block|}
end_class

end_unit

