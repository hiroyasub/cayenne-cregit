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
name|access
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

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
name|DataRow
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
name|SQLAction
import|;
end_import

begin_comment
comment|/**  * A helper that executes a sequence of queries, providing correct mapping of the results  * to the original query. Note that this class is not thread-safe as it stores current  * query execution state.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|DataNodeQueryAction
implements|implements
name|OperationObserver
block|{
name|OperationObserver
name|observer
decl_stmt|;
name|DataNode
name|node
decl_stmt|;
specifier|private
name|Query
name|currentQuery
decl_stmt|;
specifier|public
name|DataNodeQueryAction
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|OperationObserver
name|observer
parameter_list|)
block|{
name|this
operator|.
name|observer
operator|=
name|observer
expr_stmt|;
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
block|}
specifier|public
name|void
name|runQuery
parameter_list|(
name|Connection
name|connection
parameter_list|,
name|Query
name|query
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
comment|// remember root query ... it will be used to map the results, even if SQLAction
comment|// uses query substitute...
name|this
operator|.
name|currentQuery
operator|=
name|query
expr_stmt|;
name|SQLAction
name|action
init|=
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|getAction
argument_list|(
name|query
argument_list|,
name|node
argument_list|)
decl_stmt|;
name|action
operator|.
name|performAction
argument_list|(
name|connection
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nextBatchCount
parameter_list|(
name|Query
name|query
parameter_list|,
name|int
index|[]
name|resultCount
parameter_list|)
block|{
name|observer
operator|.
name|nextBatchCount
argument_list|(
name|currentQuery
argument_list|,
name|resultCount
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nextCount
parameter_list|(
name|Query
name|query
parameter_list|,
name|int
name|resultCount
parameter_list|)
block|{
name|observer
operator|.
name|nextCount
argument_list|(
name|currentQuery
argument_list|,
name|resultCount
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nextDataRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|List
argument_list|<
name|DataRow
argument_list|>
name|dataRows
parameter_list|)
block|{
name|observer
operator|.
name|nextDataRows
argument_list|(
name|currentQuery
argument_list|,
name|dataRows
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nextDataRows
parameter_list|(
name|Query
name|q
parameter_list|,
name|ResultIterator
name|it
parameter_list|)
block|{
name|observer
operator|.
name|nextDataRows
argument_list|(
name|currentQuery
argument_list|,
name|it
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nextGeneratedDataRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|ResultIterator
name|keysIterator
parameter_list|)
block|{
name|observer
operator|.
name|nextGeneratedDataRows
argument_list|(
name|currentQuery
argument_list|,
name|keysIterator
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nextGlobalException
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|observer
operator|.
name|nextGlobalException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nextQueryException
parameter_list|(
name|Query
name|query
parameter_list|,
name|Exception
name|ex
parameter_list|)
block|{
name|observer
operator|.
name|nextQueryException
argument_list|(
name|currentQuery
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isIteratedResult
parameter_list|()
block|{
return|return
name|observer
operator|.
name|isIteratedResult
argument_list|()
return|;
block|}
block|}
end_class

end_unit

