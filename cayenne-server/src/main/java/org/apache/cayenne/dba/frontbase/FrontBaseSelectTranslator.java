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
name|dba
operator|.
name|frontbase
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
name|translator
operator|.
name|select
operator|.
name|DefaultSelectTranslator
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
name|dba
operator|.
name|DbAdapter
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
name|map
operator|.
name|EntityResolver
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
name|query
operator|.
name|Query
import|;
end_import

begin_comment
comment|/**  * @since 1.2  */
end_comment

begin_class
class|class
name|FrontBaseSelectTranslator
extends|extends
name|DefaultSelectTranslator
block|{
specifier|static
specifier|final
name|String
name|SELECT_PREFIX
init|=
literal|"SELECT"
decl_stmt|;
comment|/** 	 * @since 4.0 	 */
specifier|public
name|FrontBaseSelectTranslator
parameter_list|(
name|Query
name|query
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
name|super
argument_list|(
name|query
argument_list|,
name|adapter
argument_list|,
name|entityResolver
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|appendLimitAndOffsetClauses
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|)
block|{
name|int
name|limit
init|=
name|queryMetadata
operator|.
name|getFetchLimit
argument_list|()
decl_stmt|;
if|if
condition|(
name|limit
operator|>
literal|0
operator|&&
name|buffer
operator|.
name|length
argument_list|()
operator|>
name|SELECT_PREFIX
operator|.
name|length
argument_list|()
condition|)
block|{
if|if
condition|(
name|SELECT_PREFIX
operator|.
name|equals
argument_list|(
name|buffer
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|SELECT_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|insert
argument_list|(
name|SELECT_PREFIX
operator|.
name|length
argument_list|()
argument_list|,
literal|" TOP "
operator|+
name|limit
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

