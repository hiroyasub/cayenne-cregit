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
name|lifecycle
operator|.
name|relationship
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
name|DataChannelQueryFilter
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
name|DataChannelQueryFilterChain
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
name|DataObject
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
name|ObjectContext
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
name|QueryResponse
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
name|annotation
operator|.
name|PostLoad
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
name|annotation
operator|.
name|PostPersist
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
name|annotation
operator|.
name|PostUpdate
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
name|query
operator|.
name|Query
import|;
end_import

begin_comment
comment|/**  * A {@link DataChannelQueryFilter} that implements ObjectId relationships read functionality.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|ObjectIdRelationshipFilter
implements|implements
name|DataChannelQueryFilter
block|{
specifier|private
name|ObjectIdRelationshipFaultingStrategy
name|faultingStrategy
decl_stmt|;
specifier|public
name|ObjectIdRelationshipFilter
parameter_list|()
block|{
name|this
operator|.
name|faultingStrategy
operator|=
name|createFaultingStrategy
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|ObjectIdRelationshipFaultingStrategy
name|createFaultingStrategy
parameter_list|()
block|{
return|return
operator|new
name|ObjectIdRelationshipBatchFaultingStrategy
argument_list|()
return|;
block|}
specifier|public
name|QueryResponse
name|onQuery
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|Query
name|query
parameter_list|,
name|DataChannelQueryFilterChain
name|chain
parameter_list|)
block|{
try|try
block|{
return|return
name|chain
operator|.
name|onQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
return|;
block|}
finally|finally
block|{
name|faultingStrategy
operator|.
name|afterQuery
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|PostUpdate
argument_list|(
name|entityAnnotations
operator|=
name|ObjectIdRelationship
operator|.
name|class
argument_list|)
annotation|@
name|PostPersist
argument_list|(
name|entityAnnotations
operator|=
name|ObjectIdRelationship
operator|.
name|class
argument_list|)
name|void
name|postCommit
parameter_list|(
name|DataObject
name|object
parameter_list|)
block|{
comment|// invalidate after commit to ensure UUID property is re-read...
name|object
operator|.
name|getObjectContext
argument_list|()
operator|.
name|invalidateObjects
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
comment|/**      * A lifecycle callback method that delegates object post load event processing to the      * underlying faulting strategy.      */
annotation|@
name|PostLoad
argument_list|(
name|entityAnnotations
operator|=
name|ObjectIdRelationship
operator|.
name|class
argument_list|)
name|void
name|postLoad
parameter_list|(
name|DataObject
name|object
parameter_list|)
block|{
name|faultingStrategy
operator|.
name|afterObjectLoaded
argument_list|(
name|object
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

