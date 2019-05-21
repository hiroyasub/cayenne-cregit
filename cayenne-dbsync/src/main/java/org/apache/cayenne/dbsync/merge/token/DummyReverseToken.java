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
name|dbsync
operator|.
name|merge
operator|.
name|token
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
name|dbsync
operator|.
name|merge
operator|.
name|context
operator|.
name|MergeDirection
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
name|dbsync
operator|.
name|merge
operator|.
name|context
operator|.
name|MergerContext
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
name|dbsync
operator|.
name|merge
operator|.
name|factory
operator|.
name|MergerTokenFactory
import|;
end_import

begin_comment
comment|/**  * The reverse of a {@link MergerToken} that can not be reversed.. This will not execute  * any thing, but {@link #createReverse(MergerTokenFactory)} will get back the reverse that  * this was made from.  */
end_comment

begin_class
specifier|public
class|class
name|DummyReverseToken
implements|implements
name|MergerToken
block|{
specifier|private
name|MergerToken
name|reverse
decl_stmt|;
specifier|public
name|DummyReverseToken
parameter_list|(
name|MergerToken
name|reverse
parameter_list|)
block|{
name|this
operator|.
name|reverse
operator|=
name|reverse
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|MergerToken
name|createReverse
parameter_list|(
name|MergerTokenFactory
name|factory
parameter_list|)
block|{
return|return
name|reverse
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|MergerContext
name|mergerContext
parameter_list|)
block|{
comment|// can not execute
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|MergeDirection
name|getDirection
parameter_list|()
block|{
return|return
name|reverse
operator|.
name|getDirection
argument_list|()
operator|.
name|reverseDirection
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getTokenName
parameter_list|()
block|{
return|return
literal|"Can not execute the reverse of "
operator|+
name|reverse
operator|.
name|getTokenName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getTokenValue
parameter_list|()
block|{
return|return
name|reverse
operator|.
name|getTokenValue
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getSortingWeight
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|compareTo
parameter_list|(
name|MergerToken
name|o
parameter_list|)
block|{
return|return
operator|-
name|o
operator|.
name|getSortingWeight
argument_list|()
return|;
block|}
block|}
end_class

end_unit

