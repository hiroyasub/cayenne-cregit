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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * A QueryResponse that contains a sublist of the query result. Also contains extra  * information about the full list.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|IncrementalListResponse
extends|extends
name|ListResponse
block|{
specifier|protected
name|int
name|fullSize
decl_stmt|;
specifier|public
name|IncrementalListResponse
parameter_list|(
name|List
name|objectList
parameter_list|,
name|int
name|fullSize
parameter_list|)
block|{
name|super
argument_list|(
name|objectList
argument_list|)
expr_stmt|;
name|this
operator|.
name|fullSize
operator|=
name|fullSize
expr_stmt|;
block|}
specifier|public
name|int
name|getFullSize
parameter_list|()
block|{
return|return
name|fullSize
return|;
block|}
block|}
end_class

end_unit

