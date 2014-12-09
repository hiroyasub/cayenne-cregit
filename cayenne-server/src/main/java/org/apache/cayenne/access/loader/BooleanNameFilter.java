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
name|access
operator|.
name|loader
package|;
end_package

begin_comment
comment|/**  * @since 4.0.  */
end_comment

begin_class
specifier|public
class|class
name|BooleanNameFilter
implements|implements
name|NameFilter
block|{
specifier|private
specifier|final
name|boolean
name|isInclude
decl_stmt|;
specifier|public
name|BooleanNameFilter
parameter_list|(
name|boolean
name|isInclude
parameter_list|)
block|{
name|this
operator|.
name|isInclude
operator|=
name|isInclude
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isIncluded
parameter_list|(
name|String
name|string
parameter_list|)
block|{
return|return
name|this
operator|.
name|isInclude
return|;
block|}
block|}
end_class

end_unit

