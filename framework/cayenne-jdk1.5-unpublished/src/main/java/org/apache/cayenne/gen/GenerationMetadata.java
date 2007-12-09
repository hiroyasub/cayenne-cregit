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
name|gen
package|;
end_package

begin_comment
comment|/**  * Represents basic metadata associated with a code generation execution.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_interface
specifier|public
interface|interface
name|GenerationMetadata
block|{
comment|/**      * Returns class name (without a package) of the sub class associated with this      * generator.      */
name|String
name|getSubClassName
parameter_list|()
function_decl|;
comment|/**      * Returns the super class (without a package) of the data object class associated      * with this generator      */
name|String
name|getSuperClassName
parameter_list|()
function_decl|;
comment|/**      * Returns the base class (without a package) of the data object class associated with      * this generator. Class name must not include a package.      */
name|String
name|getBaseClassName
parameter_list|()
function_decl|;
comment|/**      * Returns Java package name of the class associated with this generator.      */
name|String
name|getSubPackageName
parameter_list|()
function_decl|;
comment|/**      * Returns<code>superPackageName</code> property that defines a superclass's      * package name.      */
name|String
name|getSuperPackageName
parameter_list|()
function_decl|;
comment|/**      * Returns<code>basePackageName</code> property that defines a baseclass's      * (superclass superclass) package name.      */
name|String
name|getBasePackageName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

