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
name|configuration
package|;
end_package

begin_comment
comment|/**  * Represents a properties map for a given {@link CayenneRuntime}.  *   * @since 3.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|RuntimeProperties
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CAYENNE_RUNTIME_NAME
init|=
literal|"cayenne.runtime.name"
decl_stmt|;
comment|/**      * Returns property value for a given key.      */
name|String
name|get
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

