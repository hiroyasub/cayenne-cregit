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
name|configuration
package|;
end_package

begin_comment
comment|/**  * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|DefaultDataChannelDescriptorMerger
implements|implements
name|DataChannelDescriptorMerger
block|{
specifier|public
name|DataChannelDescriptor
name|merge
parameter_list|(
name|DataChannelDescriptor
modifier|...
name|descriptors
parameter_list|)
block|{
if|if
condition|(
name|descriptors
operator|==
literal|null
operator|||
name|descriptors
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null or empty descriptors"
argument_list|)
throw|;
block|}
if|if
condition|(
name|descriptors
operator|.
name|length
operator|==
literal|1
condition|)
block|{
return|return
name|descriptors
index|[
literal|0
index|]
return|;
block|}
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Merging multiple descriptors is not yet implemented"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

