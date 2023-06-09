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
name|configuration
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
name|QueryDescriptor
import|;
end_import

begin_comment
comment|/**  * An event generated when a Query object is added to a DataMap,   * removed from a DataMap, or changed within a DataMap.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|QueryEvent
extends|extends
name|MapEvent
block|{
specifier|protected
name|QueryDescriptor
name|query
decl_stmt|;
comment|/**      * Data map containing the query      */
specifier|protected
name|DataMap
name|map
decl_stmt|;
specifier|public
name|QueryEvent
parameter_list|(
name|Object
name|source
parameter_list|,
name|QueryDescriptor
name|query
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|setQuery
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
specifier|public
name|QueryEvent
parameter_list|(
name|Object
name|source
parameter_list|,
name|QueryDescriptor
name|query
parameter_list|,
name|String
name|oldName
parameter_list|)
block|{
name|this
argument_list|(
name|source
argument_list|,
name|query
argument_list|)
expr_stmt|;
name|setOldName
argument_list|(
name|oldName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|QueryEvent
parameter_list|(
name|Object
name|source
parameter_list|,
name|QueryDescriptor
name|query
parameter_list|,
name|int
name|type
parameter_list|)
block|{
name|this
argument_list|(
name|source
argument_list|,
name|query
argument_list|)
expr_stmt|;
name|setId
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a query event, specifying DataMap, containing the query      */
specifier|public
name|QueryEvent
parameter_list|(
name|Object
name|source
parameter_list|,
name|QueryDescriptor
name|query
parameter_list|,
name|DataMap
name|map
parameter_list|)
block|{
name|this
argument_list|(
name|source
argument_list|,
name|query
argument_list|)
expr_stmt|;
name|setDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a query event, specifying DataMap, containing the query      */
specifier|public
name|QueryEvent
parameter_list|(
name|Object
name|source
parameter_list|,
name|QueryDescriptor
name|query
parameter_list|,
name|String
name|oldName
parameter_list|,
name|DataMap
name|map
parameter_list|)
block|{
name|this
argument_list|(
name|source
argument_list|,
name|query
argument_list|,
name|oldName
argument_list|)
expr_stmt|;
name|setDataMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a query event, specifying DataMap, containing the query      */
specifier|public
name|QueryEvent
parameter_list|(
name|Object
name|source
parameter_list|,
name|QueryDescriptor
name|query
parameter_list|,
name|int
name|type
parameter_list|,
name|DataMap
name|map
parameter_list|)
block|{
name|this
argument_list|(
name|source
argument_list|,
name|query
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|setDataMap
argument_list|(
name|map
argument_list|)
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
name|query
operator|!=
literal|null
operator|)
condition|?
name|query
operator|.
name|getName
argument_list|()
else|:
literal|null
return|;
block|}
specifier|public
name|QueryDescriptor
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
specifier|public
name|void
name|setQuery
parameter_list|(
name|QueryDescriptor
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
comment|/**      * @return DataMap, containing the query      */
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|map
return|;
block|}
comment|/**      * Sets DataMap, containing the query      */
specifier|public
name|void
name|setDataMap
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
block|}
block|}
end_class

end_unit

