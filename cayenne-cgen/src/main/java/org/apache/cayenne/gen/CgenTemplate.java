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
name|gen
package|;
end_package

begin_comment
comment|/**  * @since 5.0  */
end_comment

begin_class
specifier|public
class|class
name|CgenTemplate
block|{
specifier|private
specifier|final
name|String
name|data
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|isFile
decl_stmt|;
specifier|private
name|TemplateType
name|type
decl_stmt|;
specifier|public
name|CgenTemplate
parameter_list|(
name|String
name|data
parameter_list|,
name|Boolean
name|isFile
parameter_list|,
name|TemplateType
name|type
parameter_list|)
block|{
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
name|this
operator|.
name|isFile
operator|=
name|isFile
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|String
name|getData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
specifier|public
name|boolean
name|isFile
parameter_list|()
block|{
return|return
name|isFile
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
if|if
condition|(
name|isFile
condition|)
block|{
return|return
name|getData
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|type
operator|.
name|name
argument_list|()
return|;
block|}
block|}
specifier|public
name|TemplateType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
block|}
end_class

end_unit

