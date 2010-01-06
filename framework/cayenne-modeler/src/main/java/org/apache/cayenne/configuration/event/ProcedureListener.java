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
operator|.
name|event
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventListener
import|;
end_import

begin_comment
comment|/**   * Listener for Procedure events.  *   */
end_comment

begin_interface
specifier|public
interface|interface
name|ProcedureListener
extends|extends
name|EventListener
block|{
comment|/**       * Procedure changed.       */
specifier|public
name|void
name|procedureChanged
parameter_list|(
name|ProcedureEvent
name|e
parameter_list|)
function_decl|;
comment|/**       * New Procedure has been created.       */
specifier|public
name|void
name|procedureAdded
parameter_list|(
name|ProcedureEvent
name|e
parameter_list|)
function_decl|;
comment|/**       * Procedure has been removed.      */
specifier|public
name|void
name|procedureRemoved
parameter_list|(
name|ProcedureEvent
name|e
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

