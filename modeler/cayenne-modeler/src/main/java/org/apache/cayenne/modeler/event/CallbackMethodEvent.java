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
name|map
operator|.
name|event
operator|.
name|MapEvent
import|;
end_import

begin_comment
comment|/**  * Event for creating/removing/modifying callback methods  *  * @version 1.0 Oct 25, 2007  */
end_comment

begin_class
specifier|public
class|class
name|CallbackMethodEvent
extends|extends
name|MapEvent
block|{
comment|/**      * callbacm method name      */
specifier|private
name|String
name|callbackMethod
decl_stmt|;
comment|/**      * constructor      * @param source event source      * @param prevCallbackMethod previous callback method name (for renaming)      * @param callbackMethod (new) callback method name      * @param id event type id      */
specifier|public
name|CallbackMethodEvent
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|prevCallbackMethod
parameter_list|,
name|String
name|callbackMethod
parameter_list|,
name|int
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|,
name|prevCallbackMethod
argument_list|)
expr_stmt|;
name|this
operator|.
name|callbackMethod
operator|=
name|callbackMethod
expr_stmt|;
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return new callback method name      */
specifier|public
name|String
name|getNewName
parameter_list|()
block|{
return|return
name|callbackMethod
return|;
block|}
block|}
end_class

end_unit

