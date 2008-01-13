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
name|map
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
name|DataMap
import|;
end_import

begin_comment
comment|/**   * An events describing a DataMap change.  */
end_comment

begin_class
specifier|public
class|class
name|DataMapEvent
extends|extends
name|MapEvent
block|{
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
comment|/** Creates a DataMap change event. */
specifier|public
name|DataMapEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|DataMap
name|dataMap
parameter_list|)
block|{
name|super
argument_list|(
name|src
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
block|}
comment|/** Creates a DataMap event of a specified type. */
specifier|public
name|DataMapEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|DataMap
name|dataMap
parameter_list|,
name|int
name|id
parameter_list|)
block|{
name|this
argument_list|(
name|src
argument_list|,
name|dataMap
argument_list|)
expr_stmt|;
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
comment|/** Creates a DataMap name change event.*/
specifier|public
name|DataMapEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|DataMap
name|dataMap
parameter_list|,
name|String
name|oldName
parameter_list|)
block|{
name|this
argument_list|(
name|src
argument_list|,
name|dataMap
argument_list|)
expr_stmt|;
name|setOldName
argument_list|(
name|oldName
argument_list|)
expr_stmt|;
block|}
comment|/**  	 * Returns DataMap associated with this event.  	 */
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|dataMap
return|;
block|}
comment|/** 	 * Sets DataMap associated with this event. 	 *  	 * @param dataMap The dataMap to set 	 */
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
annotation|@
name|Override
specifier|public
name|String
name|getNewName
parameter_list|()
block|{
return|return
operator|(
name|dataMap
operator|!=
literal|null
operator|)
condition|?
name|dataMap
operator|.
name|getName
argument_list|()
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

