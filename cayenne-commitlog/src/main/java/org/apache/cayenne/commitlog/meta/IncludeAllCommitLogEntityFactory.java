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
name|commitlog
operator|.
name|meta
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
name|IncludeAllCommitLogEntityFactory
implements|implements
name|CommitLogEntityFactory
block|{
specifier|private
specifier|static
specifier|final
name|CommitLogEntity
name|ALLOWED_ENTITY
init|=
operator|new
name|CommitLogEntity
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isIncluded
parameter_list|(
name|String
name|property
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isConfidential
parameter_list|(
name|String
name|property
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isIncluded
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
annotation|@
name|Override
specifier|public
name|CommitLogEntity
name|getEntity
parameter_list|(
name|ObjectId
name|id
parameter_list|)
block|{
return|return
name|ALLOWED_ENTITY
return|;
block|}
block|}
end_class

end_unit

