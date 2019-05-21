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
name|event
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
name|event
operator|.
name|CayenneEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|modeler
operator|.
name|editor
operator|.
name|CallbackType
import|;
end_import

begin_comment
comment|/**  * Event for callback type selection change  *  * @version 1.0 Oct 28, 2007  */
end_comment

begin_class
specifier|public
class|class
name|CallbackTypeSelectionEvent
extends|extends
name|CayenneEvent
block|{
comment|/**      * selected calback type      */
specifier|private
name|CallbackType
name|callbackType
decl_stmt|;
comment|/**      * constructor      * @param source event source      * @param callbackType selected callback type      */
specifier|public
name|CallbackTypeSelectionEvent
parameter_list|(
name|Object
name|source
parameter_list|,
name|CallbackType
name|callbackType
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|this
operator|.
name|callbackType
operator|=
name|callbackType
expr_stmt|;
block|}
comment|/**      * @return selected callback type      */
specifier|public
name|CallbackType
name|getCallbackType
parameter_list|()
block|{
return|return
name|callbackType
return|;
block|}
block|}
end_class

end_unit

