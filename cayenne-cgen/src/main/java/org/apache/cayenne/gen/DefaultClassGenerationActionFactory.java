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
name|gen
package|;
end_package

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|DefaultClassGenerationActionFactory
implements|implements
name|ClassGenerationActionFactory
block|{
annotation|@
name|Override
specifier|public
name|ClassGenerationAction
name|createAction
parameter_list|(
name|CgenConfiguration
name|cgenConfiguration
parameter_list|)
block|{
name|ClassGenerationAction
name|classGenerationAction
init|=
name|cgenConfiguration
operator|.
name|isClient
argument_list|()
condition|?
operator|new
name|ClientClassGenerationAction
argument_list|()
else|:
operator|new
name|ClassGenerationAction
argument_list|()
decl_stmt|;
name|classGenerationAction
operator|.
name|setCgenConfiguration
argument_list|(
name|cgenConfiguration
argument_list|)
expr_stmt|;
return|return
name|classGenerationAction
return|;
block|}
block|}
end_class

end_unit

