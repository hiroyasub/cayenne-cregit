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
name|di
package|;
end_package

begin_comment
comment|/**  * Creates objects for user-provided String class names, injecting dependencies  * into them.  *   * @since 3.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|AdhocObjectFactory
block|{
comment|/**      * Returns an instance of "className" that implements "superType", injecting      * dependencies from the registry into it.      */
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
function_decl|;
comment|/**      * Returns a Java class loaded using ClassLoader returned from      * {@link ClassLoaderManager#getClassLoader(String)} for a given class name.      *       * @since 4.0      */
name|Class
argument_list|<
name|?
argument_list|>
name|getJavaClass
parameter_list|(
name|String
name|className
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

