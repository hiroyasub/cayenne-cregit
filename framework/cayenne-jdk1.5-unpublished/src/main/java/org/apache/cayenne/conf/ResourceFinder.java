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
name|conf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
comment|/**  * An object that defines a strategy for locating stream resources.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_interface
specifier|public
interface|interface
name|ResourceFinder
block|{
comment|/**      * Returns a collection of resource URLs with a given name found in the environment      * using some lookup strategy.      */
name|Collection
argument_list|<
name|URL
argument_list|>
name|getResources
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns a single resource matching a given name. If more than one resource matches      * the name, it is implementation specific which one will be returned.      */
name|URL
name|getResource
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

