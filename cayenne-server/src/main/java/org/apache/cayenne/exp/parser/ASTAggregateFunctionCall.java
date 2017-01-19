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
name|exp
operator|.
name|parser
package|;
end_package

begin_comment
comment|/**  * Base class for all aggregation functions expressions  * It's more like marker interface for now.  * @since 4.0  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|ASTAggregateFunctionCall
extends|extends
name|ASTFunctionCall
block|{
name|ASTAggregateFunctionCall
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|functionName
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|,
name|functionName
argument_list|)
expr_stmt|;
block|}
name|ASTAggregateFunctionCall
parameter_list|(
name|int
name|id
parameter_list|,
name|String
name|functionName
parameter_list|,
name|Object
modifier|...
name|nodes
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|,
name|functionName
argument_list|,
name|nodes
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

