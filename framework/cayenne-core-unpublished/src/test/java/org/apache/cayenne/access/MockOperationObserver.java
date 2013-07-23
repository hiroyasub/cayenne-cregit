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
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|CayenneRuntimeException
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
name|ResultIterator
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
comment|/**  * Helper class to process test queries results.  */
end_comment

begin_class
specifier|public
class|class
name|MockOperationObserver
implements|implements
name|OperationObserver
block|{
specifier|protected
name|Map
name|resultRows
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|protected
name|Map
name|resultCounts
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|protected
name|Map
name|resultBatch
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|public
name|List
name|rowsForQuery
parameter_list|(
name|Query
name|q
parameter_list|)
block|{
return|return
operator|(
name|List
operator|)
name|resultRows
operator|.
name|get
argument_list|(
name|q
argument_list|)
return|;
block|}
specifier|public
name|int
name|countForQuery
parameter_list|(
name|Query
name|q
parameter_list|)
block|{
name|Integer
name|count
init|=
operator|(
name|Integer
operator|)
name|resultCounts
operator|.
name|get
argument_list|(
name|q
argument_list|)
decl_stmt|;
return|return
operator|(
name|count
operator|!=
literal|null
operator|)
condition|?
name|count
operator|.
name|intValue
argument_list|()
else|:
operator|-
literal|1
return|;
block|}
specifier|public
name|int
index|[]
name|countsForQuery
parameter_list|(
name|Query
name|q
parameter_list|)
block|{
return|return
operator|(
name|int
index|[]
operator|)
name|resultBatch
operator|.
name|get
argument_list|(
name|q
argument_list|)
return|;
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
name|resultCounts
operator|.
name|put
argument_list|(
name|query
argument_list|,
operator|new
name|Integer
argument_list|(
name|resultCount
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|nextRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|dataRows
parameter_list|)
block|{
name|resultRows
operator|.
name|put
argument_list|(
name|query
argument_list|,
name|dataRows
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
name|resultBatch
operator|.
name|put
argument_list|(
name|query
argument_list|,
name|resultCount
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
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
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
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
specifier|public
name|void
name|nextRows
parameter_list|(
name|Query
name|q
parameter_list|,
name|ResultIterator
name|it
parameter_list|)
block|{
block|}
specifier|public
name|void
name|nextGeneratedRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|ResultIterator
name|keysIterator
parameter_list|)
block|{
block|}
specifier|public
name|boolean
name|isIteratedResult
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit
