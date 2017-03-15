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
comment|/**  * A binding builder for list configurations.  *   * @param<T> A type of list values.  * @since 3.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|ListBuilder
parameter_list|<
name|T
parameter_list|>
extends|extends
name|UnorderedListBuilder
argument_list|<
name|T
argument_list|>
block|{
comment|/**      * @since 4.0      */
name|UnorderedListBuilder
argument_list|<
name|T
argument_list|>
name|after
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * @since 4.0      */
name|UnorderedListBuilder
argument_list|<
name|T
argument_list|>
name|after
parameter_list|(
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|key
parameter_list|)
function_decl|;
comment|/**      * @since 4.0      */
name|UnorderedListBuilder
argument_list|<
name|T
argument_list|>
name|before
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * @since 4.0      */
name|UnorderedListBuilder
argument_list|<
name|T
argument_list|>
name|before
parameter_list|(
name|Key
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|key
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

