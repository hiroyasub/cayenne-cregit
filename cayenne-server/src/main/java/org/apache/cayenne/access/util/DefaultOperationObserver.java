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
name|access
operator|.
name|util
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
name|access
operator|.
name|OperationObserver
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
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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

begin_comment
comment|/**  * Simple implementation of OperationObserver interface. Useful as a superclass  * of other implementations of OperationObserver. This implementation only  * tracks transaction events and exceptions.  *<p>  *<i>This operation observer is unsafe to use in application, since it doesn't  * rethrow the exceptions immediately, and may cause the database to hang.</i>  *</p>  *   */
end_comment

begin_class
specifier|public
class|class
name|DefaultOperationObserver
implements|implements
name|OperationObserver
block|{
specifier|protected
name|List
argument_list|<
name|Throwable
argument_list|>
name|globalExceptions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|Query
argument_list|,
name|Throwable
argument_list|>
name|queryExceptions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * Prints the information about query and global exceptions.      */
specifier|public
name|void
name|printExceptions
parameter_list|(
name|PrintWriter
name|out
parameter_list|)
block|{
if|if
condition|(
name|globalExceptions
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|globalExceptions
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"Global Exception:"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
literal|"Global Exceptions:"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
specifier|final
name|Throwable
name|th
range|:
name|globalExceptions
control|)
block|{
name|th
operator|.
name|printStackTrace
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|queryExceptions
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|queryExceptions
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|out
operator|.
name|println
argument_list|(
literal|"Query Exception:"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|println
argument_list|(
literal|"Query Exceptions:"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
specifier|final
name|Query
name|query
range|:
name|queryExceptions
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Throwable
name|th
init|=
name|queryExceptions
operator|.
name|get
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|th
operator|.
name|printStackTrace
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Returns a list of global exceptions that occured during data operation      * run.      */
specifier|public
name|List
argument_list|<
name|Throwable
argument_list|>
name|getGlobalExceptions
parameter_list|()
block|{
return|return
name|globalExceptions
return|;
block|}
comment|/**      * Returns a list of exceptions that occured during data operation run by      * query.      */
specifier|public
name|Map
argument_list|<
name|Query
argument_list|,
name|Throwable
argument_list|>
name|getQueryExceptions
parameter_list|()
block|{
return|return
name|queryExceptions
return|;
block|}
comment|/**      * Returns<code>true</code> if at least one exception was registered during      * query execution.      */
specifier|public
name|boolean
name|hasExceptions
parameter_list|()
block|{
return|return
name|globalExceptions
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|||
name|queryExceptions
operator|.
name|size
argument_list|()
operator|>
literal|0
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
comment|// noop
block|}
comment|/**      * Closes ResultIterator without reading its data. If you implement a custom      * subclass, only call super if closing the iterator is what you need.      */
specifier|public
name|void
name|nextRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|ResultIterator
name|it
parameter_list|)
block|{
if|if
condition|(
name|it
operator|!=
literal|null
condition|)
block|{
name|it
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Closes ResultIterator without reading its data. If you implement a custom      * subclass, only call super if closing the iterator is what you need.      *       * @since 4.0      */
specifier|public
name|void
name|nextGeneratedRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|ResultIterator
argument_list|<
name|?
argument_list|>
name|keys
parameter_list|,
name|List
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ObjectId
argument_list|>
name|idsToUpdate
parameter_list|)
block|{
if|if
condition|(
name|keys
operator|!=
literal|null
condition|)
block|{
name|keys
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
name|queryExceptions
operator|.
name|put
argument_list|(
name|query
argument_list|,
name|Util
operator|.
name|unwindException
argument_list|(
name|ex
argument_list|)
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
name|globalExceptions
operator|.
name|add
argument_list|(
name|Util
operator|.
name|unwindException
argument_list|(
name|ex
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>false</code>.      */
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

