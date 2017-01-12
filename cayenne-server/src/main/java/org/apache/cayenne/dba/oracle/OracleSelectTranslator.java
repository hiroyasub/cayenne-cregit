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
name|oracle
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
name|DataNode
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
comment|/**  * Select translator that implements Oracle-specific optimizations.  *   */
end_comment

begin_class
class|class
name|OracleSelectTranslator
extends|extends
name|DefaultSelectTranslator
block|{
comment|/** 	 * @since 4.0 	 */
specifier|public
name|OracleSelectTranslator
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
name|offset
init|=
name|queryMetadata
operator|.
name|getFetchOffset
argument_list|()
decl_stmt|;
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
operator|||
name|offset
operator|>
literal|0
condition|)
block|{
name|int
name|max
init|=
operator|(
name|limit
operator|<=
literal|0
operator|)
condition|?
name|Integer
operator|.
name|MAX_VALUE
else|:
name|limit
operator|+
name|offset
decl_stmt|;
name|buffer
operator|.
name|insert
argument_list|(
literal|0
argument_list|,
literal|"select * from ( select tid.*, ROWNUM rnum from ("
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|") tid where ROWNUM<="
argument_list|)
operator|.
name|append
argument_list|(
name|max
argument_list|)
operator|.
name|append
argument_list|(
literal|") where rnum> "
argument_list|)
operator|.
name|append
argument_list|(
name|offset
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|appendSelectColumns
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|selectColumnExpList
parameter_list|)
block|{
comment|// we need to add aliases to all columns to make fetch
comment|// limit and offset work properly on Oracle (see CAY-1266)
comment|// append columns (unroll the loop's first element)
name|int
name|columnCount
init|=
name|selectColumnExpList
operator|.
name|size
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|selectColumnExpList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|selectColumnExpList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|contains
argument_list|(
literal|" AS "
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" AS c0"
argument_list|)
expr_stmt|;
block|}
comment|// assume there is at least 1 element
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|columnCount
condition|;
name|i
operator|++
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|selectColumnExpList
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|selectColumnExpList
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|contains
argument_list|(
literal|" AS "
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" AS c"
argument_list|)
operator|.
name|append
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

