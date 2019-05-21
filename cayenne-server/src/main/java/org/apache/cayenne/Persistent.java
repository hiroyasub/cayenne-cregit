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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * Defines minimal API of an object that can be persisted via Cayenne.  *   * @since 1.2  */
end_comment

begin_comment
comment|// TODO: with field-based access there is no need to declare setters...
end_comment

begin_interface
specifier|public
interface|interface
name|Persistent
extends|extends
name|Serializable
block|{
name|ObjectId
name|getObjectId
parameter_list|()
function_decl|;
name|void
name|setObjectId
parameter_list|(
name|ObjectId
name|id
parameter_list|)
function_decl|;
name|int
name|getPersistenceState
parameter_list|()
function_decl|;
name|void
name|setPersistenceState
parameter_list|(
name|int
name|state
parameter_list|)
function_decl|;
name|ObjectContext
name|getObjectContext
parameter_list|()
function_decl|;
name|void
name|setObjectContext
parameter_list|(
name|ObjectContext
name|objectContext
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

