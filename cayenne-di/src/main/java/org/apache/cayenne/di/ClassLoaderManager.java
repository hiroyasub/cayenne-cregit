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

begin_comment
comment|/**  * Maps ClassLoaders to resources. This is a useful abstraction when switching  * between environments. E.g. between JEE with thread/hierarchical classloaders  * and OSGi with per-bundle classloaders.  *   * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|ClassLoaderManager
block|{
comment|/**      * Returns a ClassLoader appropriate for loading a given resource. Resource      * path should be compatible with Class.getResource(..) and such, i.e. the      * path component separator should be slash, not dot.      */
name|ClassLoader
name|getClassLoader
parameter_list|(
name|String
name|resourceName
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

