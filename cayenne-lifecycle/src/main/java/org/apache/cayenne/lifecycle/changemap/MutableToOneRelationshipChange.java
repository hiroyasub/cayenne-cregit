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
name|lifecycle
operator|.
name|changemap
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
name|ObjectId
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|MutableToOneRelationshipChange
implements|implements
name|ToOneRelationshipChange
block|{
specifier|private
name|ObjectId
name|oldValue
decl_stmt|;
specifier|private
name|ObjectId
name|newValue
decl_stmt|;
annotation|@
name|Override
specifier|public
name|ObjectId
name|getOldValue
parameter_list|()
block|{
return|return
name|oldValue
return|;
block|}
annotation|@
name|Override
specifier|public
name|ObjectId
name|getNewValue
parameter_list|()
block|{
return|return
name|newValue
return|;
block|}
specifier|public
name|void
name|connected
parameter_list|(
name|ObjectId
name|o
parameter_list|)
block|{
name|this
operator|.
name|newValue
operator|=
name|o
expr_stmt|;
block|}
specifier|public
name|void
name|disconnected
parameter_list|(
name|ObjectId
name|o
parameter_list|)
block|{
name|this
operator|.
name|oldValue
operator|=
name|o
expr_stmt|;
block|}
block|}
end_class

end_unit

