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

begin_comment
comment|/**  * Class for processing listener class selection  *  * @author Vasil Tarasevich  * @version 1.0 Oct 28, 2007  */
end_comment

begin_class
specifier|public
class|class
name|ListenerClassSelectionEvent
extends|extends
name|CayenneEvent
block|{
comment|/**      * selected listener class      */
specifier|private
name|String
name|listenerClass
decl_stmt|;
comment|/**      * constructor      * @param source event source      * @param listenerClass listener class      */
specifier|public
name|ListenerClassSelectionEvent
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|listenerClass
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|this
operator|.
name|listenerClass
operator|=
name|listenerClass
expr_stmt|;
block|}
comment|/**      * @return selected listener class      */
specifier|public
name|String
name|getListenerClass
parameter_list|()
block|{
return|return
name|listenerClass
return|;
block|}
block|}
end_class

end_unit

