begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
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

begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

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
name|DataMap
import|;
end_import

begin_comment
comment|/**  * Represents a display event of a DataMap.  *   */
end_comment

begin_class
specifier|public
class|class
name|DataMapDisplayEvent
extends|extends
name|DataNodeDisplayEvent
block|{
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
comment|/** True if different from current data map. */
specifier|protected
name|boolean
name|dataMapChanged
init|=
literal|true
decl_stmt|;
specifier|public
name|DataMapDisplayEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|DataChannelDescriptor
name|dataChannelDescriptor
parameter_list|)
block|{
name|this
argument_list|(
name|src
argument_list|,
name|map
argument_list|,
name|dataChannelDescriptor
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|DataMapDisplayEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|DataMap
name|map
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
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|map
expr_stmt|;
name|setDataNodeChanged
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/** Get dataMap wrapper. */
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|dataMap
return|;
block|}
comment|/** 	 * Sets the dataMap. 	 * @param dataMap The dataMap to set 	 */
specifier|public
name|void
name|setDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
block|}
comment|/** Returns true if data map is different from the current data map. */
specifier|public
name|boolean
name|isDataMapChanged
parameter_list|()
block|{
return|return
name|dataMapChanged
return|;
block|}
specifier|public
name|void
name|setDataMapChanged
parameter_list|(
name|boolean
name|temp
parameter_list|)
block|{
name|dataMapChanged
operator|=
name|temp
expr_stmt|;
block|}
block|}
end_class

end_unit

