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
name|modeler
operator|.
name|editor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
import|;
end_import

begin_comment
comment|/**   * Callback interface for panels of detail views to process existing   * selections. When tab is selected processExistingSelection() is   * called to reset the state if the tab has any rows selected.  * For example, this is useful when we want to reset the state of the   * "Remove" button if the tab has attributes (relationships) already   * selected.   */
end_comment

begin_interface
specifier|public
interface|interface
name|ExistingSelectionProcessor
block|{
comment|/**       * Called when tab is selected. Resets the state there are any rows selected.      * For example, it is useful when we want to reset "Remove" button       * if the tab has attributes (relationships) already selected.       */
specifier|public
name|void
name|processExistingSelection
parameter_list|(
name|EventObject
name|e
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

