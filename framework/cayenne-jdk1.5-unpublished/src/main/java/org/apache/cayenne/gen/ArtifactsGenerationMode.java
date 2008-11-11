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
comment|/**  * Code generator execution mode for a collection of artifacts.  *   * @since 3.0  */
end_comment

begin_enum
specifier|public
enum|enum
name|ArtifactsGenerationMode
block|{
comment|// TODO: andrus 12/9/2007 - label names are old... need to call it something else...
name|DATAMAP
argument_list|(
literal|"datamap"
argument_list|)
block|,
name|ENTITY
argument_list|(
literal|"entity"
argument_list|)
block|,
name|ALL
argument_list|(
literal|"all"
argument_list|)
block|;
specifier|private
name|String
name|label
decl_stmt|;
specifier|private
name|ArtifactsGenerationMode
parameter_list|(
name|String
name|label
parameter_list|)
block|{
name|this
operator|.
name|label
operator|=
name|label
expr_stmt|;
block|}
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
name|label
return|;
block|}
block|}
end_enum

end_unit

