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
name|modeler
package|;
end_package

begin_comment
comment|/**  * An interface defining a service for locating external Java resources.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ClassLoadingService
block|{
comment|/**      * Returns Java ClassLoader that knows how to load all classes configured for this      * service.      */
name|ClassLoader
name|getClassLoader
parameter_list|()
function_decl|;
comment|/**      * Returns a class for given class name.      */
parameter_list|<
name|T
parameter_list|>
name|Class
argument_list|<
name|T
argument_list|>
name|loadClass
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|,
name|String
name|className
parameter_list|)
throws|throws
name|ClassNotFoundException
function_decl|;
block|}
end_interface

end_unit

