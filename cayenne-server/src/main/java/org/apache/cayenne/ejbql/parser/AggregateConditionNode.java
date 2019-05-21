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
name|ejbql
operator|.
name|parser
package|;
end_package

begin_comment
comment|/**  * Superclass of aggregated conditional nodes such as NOT, AND, OR.   * Defines priority of operations, so that SQL can be supplied with brackets as needed  *   * @since 3.0  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AggregateConditionNode
extends|extends
name|SimpleNode
block|{
comment|//defining all priorities here so that it would be easy to compare them visually
specifier|static
specifier|final
name|int
name|NOT_PRIORITY
init|=
literal|10
decl_stmt|;
specifier|static
specifier|final
name|int
name|AND_PRIORITY
init|=
literal|20
decl_stmt|;
specifier|static
specifier|final
name|int
name|OR_PRIORITY
init|=
literal|30
decl_stmt|;
specifier|public
name|AggregateConditionNode
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns priority of conditional operator.       * This is used to decide whether brackets are needed in resulting SQL       */
specifier|public
specifier|abstract
name|int
name|getPriority
parameter_list|()
function_decl|;
block|}
end_class

end_unit

