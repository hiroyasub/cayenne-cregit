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
name|modeler
operator|.
name|util
operator|.
name|path
package|;
end_package

begin_class
specifier|public
class|class
name|MaxLengthTrimmer
implements|implements
name|PathTrimmer
block|{
specifier|private
name|int
name|maxLength
decl_stmt|;
specifier|public
name|MaxLengthTrimmer
parameter_list|()
block|{
name|this
argument_list|(
name|DEFAULT_MAX_LENGTH
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MaxLengthTrimmer
parameter_list|(
name|int
name|maxLength
parameter_list|)
block|{
name|this
operator|.
name|maxLength
operator|=
name|maxLength
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|trim
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|.
name|length
argument_list|()
operator|<=
name|maxLength
condition|)
block|{
return|return
name|path
return|;
block|}
return|return
literal|"..."
operator|+
name|path
operator|.
name|substring
argument_list|(
name|path
operator|.
name|length
argument_list|()
operator|-
name|maxLength
argument_list|)
return|;
block|}
block|}
end_class

end_unit

