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
name|mixin
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
name|DataChannel
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
name|DataChannelFilter
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
name|DataChannelFilterChain
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
name|graph
operator|.
name|GraphDiff
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
comment|/**  * A {@link DataChannelFilter} that implements mixin relationships faulting  * functionality.  */
end_comment

begin_class
specifier|public
class|class
name|MixinRelationshipFilter
implements|implements
name|DataChannelFilter
block|{
specifier|private
name|MixinRelationshipFaultingStrategy
name|faultingStrategy
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|init
parameter_list|(
name|DataChannel
name|channel
parameter_list|)
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
name|MixinRelationshipFaultingStrategy
name|createFaultingStrategy
parameter_list|()
block|{
return|return
operator|new
name|MixinRelationshipBatchFaultingStrategy
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|GraphDiff
name|onSync
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|GraphDiff
name|diff
parameter_list|,
name|int
name|syncType
parameter_list|,
name|DataChannelFilterChain
name|chain
parameter_list|)
block|{
comment|// noop for now
return|return
name|chain
operator|.
name|onSync
argument_list|(
name|context
argument_list|,
name|diff
argument_list|,
name|syncType
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|DataChannelFilterChain
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
comment|/** 	 * A lifecycle callback method that delegates object post load event 	 * processing to the underlying faulting strategy. 	 */
annotation|@
name|PostLoad
argument_list|(
name|entityAnnotations
operator|=
name|MixinRelationship
operator|.
name|class
argument_list|)
annotation|@
name|PostPersist
argument_list|(
name|entityAnnotations
operator|=
name|MixinRelationship
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

