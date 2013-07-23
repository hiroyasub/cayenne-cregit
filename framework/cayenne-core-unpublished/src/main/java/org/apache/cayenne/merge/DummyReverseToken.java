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
name|merge
package|;
end_package

begin_comment
comment|/**  * The reverse of a {@link MergerToken} that can not be reversed.. This will not execute  * any thing, but {@link #createReverse(MergerFactory)} will get back the reverse that  * this was made from.  */
end_comment

begin_class
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
specifier|public
name|MergerToken
name|createReverse
parameter_list|(
name|MergerFactory
name|factory
parameter_list|)
block|{
return|return
name|reverse
return|;
block|}
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
specifier|public
name|boolean
name|isReversible
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit
