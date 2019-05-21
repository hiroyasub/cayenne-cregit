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
name|dba
operator|.
name|postgres
operator|.
name|sqltree
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
name|access
operator|.
name|sqlbuilder
operator|.
name|QuotingAppendable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|Node
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|PostgresExtractFunctionNode
extends|extends
name|Node
block|{
specifier|private
specifier|final
name|String
name|functionName
decl_stmt|;
specifier|public
name|PostgresExtractFunctionNode
parameter_list|(
name|String
name|functionName
parameter_list|)
block|{
name|this
operator|.
name|functionName
operator|=
name|functionName
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|QuotingAppendable
name|append
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" EXTRACT("
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"DAY_OF_MONTH"
operator|.
name|equals
argument_list|(
name|functionName
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"day"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"DAY_OF_WEEK"
operator|.
name|equals
argument_list|(
name|functionName
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"dow"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"DAY_OF_YEAR"
operator|.
name|equals
argument_list|(
name|functionName
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"doy"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
name|functionName
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|append
argument_list|(
literal|" FROM "
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|appendChildrenEnd
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Node
name|copy
parameter_list|()
block|{
return|return
operator|new
name|PostgresExtractFunctionNode
argument_list|(
name|functionName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

