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
name|graph
operator|.
name|GraphEvent
import|;
end_import

begin_comment
comment|/**  * A listener of {@link org.apache.cayenne.DataChannel} lifecycle events. Changes  * related to an event are attached as a GraphDiff. If a listener needs to process these  * changes, the easiest way to do that is via GraphChangeHandler "visitor":  *   *<pre>  *   public void graphChanged(GraphEvent event) {  *       GraphChangeHandler handler = ..;  *       event.getDiff().apply(handler);  *   }  *</pre>  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_interface
specifier|public
interface|interface
name|DataChannelListener
block|{
comment|/**      * Notifies implementing object of the changes that were performed to the object graph      * externally, not by one of the channel ObjectContexts.      */
name|void
name|graphChanged
parameter_list|(
name|GraphEvent
name|event
parameter_list|)
function_decl|;
comment|/**      * Notifies implementing object that one of the channel ObjectContexts flushed its      * changes to the channel.      */
name|void
name|graphFlushed
parameter_list|(
name|GraphEvent
name|event
parameter_list|)
function_decl|;
comment|/**      * Notifies implementing object that one of the channel ObjectContexts initiated a      * rollback.      */
name|void
name|graphRolledback
parameter_list|(
name|GraphEvent
name|event
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

