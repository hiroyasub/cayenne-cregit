begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *    Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
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
operator|.
name|spi
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
name|Module
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  *<p>  * Provider of modules used by module auto-loading mechanism to identify and load modules.  *</p>  *<p>  * Multiple providers can be created by inheriting from this interface and using it with {@link ModuleLoader}  *</p>  *  * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|ModuleProvider
block|{
name|Module
name|module
parameter_list|()
function_decl|;
name|Class
argument_list|<
name|?
extends|extends
name|Module
argument_list|>
name|moduleType
parameter_list|()
function_decl|;
comment|/**      * Returns an array of module types this module overrides. Module auto-loading mechanism will ensure module      * load order that respects overriding preferences.      *      * @return a collection of module types this module overrides.      */
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Module
argument_list|>
argument_list|>
name|overrides
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

