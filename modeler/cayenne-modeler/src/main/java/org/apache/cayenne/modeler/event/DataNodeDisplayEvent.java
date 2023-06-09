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
name|configuration
operator|.
name|DataChannelDescriptor
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
name|configuration
operator|.
name|DataNodeDescriptor
import|;
end_import

begin_comment
comment|/**  * Represents a display event of a DataNode.  *   */
end_comment

begin_class
specifier|public
class|class
name|DataNodeDisplayEvent
extends|extends
name|DomainDisplayEvent
block|{
specifier|protected
name|DataNodeDescriptor
name|dataNode
decl_stmt|;
comment|/** True if data node is different from the current data node. */
specifier|protected
name|boolean
name|dataNodeChanged
init|=
literal|true
decl_stmt|;
comment|/** Current DataNode changed. */
specifier|public
name|DataNodeDisplayEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|DataChannelDescriptor
name|dataChannelDescriptor
parameter_list|,
name|DataNodeDescriptor
name|node
parameter_list|)
block|{
name|super
argument_list|(
name|src
argument_list|,
name|dataChannelDescriptor
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataNode
operator|=
name|node
expr_stmt|;
name|setDomainChanged
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/** Get data node (data source) associated with this data map. */
specifier|public
name|DataNodeDescriptor
name|getDataNode
parameter_list|()
block|{
return|return
name|dataNode
return|;
block|}
comment|/** Returns true if data node is different from the current data node. */
specifier|public
name|boolean
name|isDataNodeChanged
parameter_list|()
block|{
return|return
name|dataNodeChanged
return|;
block|}
specifier|public
name|void
name|setDataNodeChanged
parameter_list|(
name|boolean
name|temp
parameter_list|)
block|{
name|dataNodeChanged
operator|=
name|temp
expr_stmt|;
block|}
comment|/** 	 * Sets the dataNode. 	 * @param dataNode The dataNode to set 	 */
specifier|public
name|void
name|setDataNode
parameter_list|(
name|DataNodeDescriptor
name|dataNode
parameter_list|)
block|{
name|this
operator|.
name|dataNode
operator|=
name|dataNode
expr_stmt|;
block|}
block|}
end_class

end_unit

