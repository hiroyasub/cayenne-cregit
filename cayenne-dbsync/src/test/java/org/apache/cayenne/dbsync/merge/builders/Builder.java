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
name|dbsync
operator|.
name|merge
operator|.
name|builders
package|;
end_package

begin_comment
comment|/**  * Base interface for all domain builders  *  * @since 4.0.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Builder
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Build valid object. If some required data omitted it will be filled with random data.      * */
name|T
name|build
parameter_list|()
function_decl|;
comment|/**      * Build valid object and add some optional fields randomly.      * */
name|T
name|random
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

