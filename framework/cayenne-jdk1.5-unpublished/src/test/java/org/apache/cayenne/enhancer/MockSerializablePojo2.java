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
name|enhancer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * A serializable pojo without "serialVersionId" defined.  *   */
end_comment

begin_class
specifier|public
class|class
name|MockSerializablePojo2
implements|implements
name|Serializable
block|{
specifier|protected
name|String
name|attribute1
decl_stmt|;
specifier|public
name|String
name|getAttribute1
parameter_list|()
block|{
return|return
name|attribute1
return|;
block|}
specifier|public
name|void
name|setAttribute1
parameter_list|(
name|String
name|attribute1
parameter_list|)
block|{
name|this
operator|.
name|attribute1
operator|=
name|attribute1
expr_stmt|;
block|}
block|}
end_class

end_unit

