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
name|project
operator|.
name|upgrade
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
name|resource
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|UpgradeUnit
block|{
specifier|private
name|Resource
name|resource
decl_stmt|;
specifier|private
name|Document
name|document
decl_stmt|;
specifier|public
name|UpgradeUnit
parameter_list|(
name|Resource
name|resource
parameter_list|,
name|Document
name|document
parameter_list|)
block|{
name|this
operator|.
name|resource
operator|=
name|resource
expr_stmt|;
name|this
operator|.
name|document
operator|=
name|document
expr_stmt|;
block|}
specifier|public
name|Document
name|getDocument
parameter_list|()
block|{
return|return
name|document
return|;
block|}
specifier|public
name|Resource
name|getResource
parameter_list|()
block|{
return|return
name|resource
return|;
block|}
specifier|public
name|void
name|setDocument
parameter_list|(
name|Document
name|document
parameter_list|)
block|{
name|this
operator|.
name|document
operator|=
name|document
expr_stmt|;
block|}
specifier|public
name|void
name|setResource
parameter_list|(
name|Resource
name|resource
parameter_list|)
block|{
name|this
operator|.
name|resource
operator|=
name|resource
expr_stmt|;
block|}
block|}
end_class

end_unit

