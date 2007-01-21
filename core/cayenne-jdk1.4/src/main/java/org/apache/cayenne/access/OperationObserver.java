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
name|query
operator|.
name|Query
import|;
end_import

begin_comment
comment|/**  * Defines a set of callback methods that allow QueryEngine to pass back query results and  * notify caller about exceptions.  *   * @see org.apache.cayenne.access.QueryEngine  * @author Andrus Adamchik  */
end_comment

begin_comment
comment|// TODO: need a name that better reflects the functionality,
end_comment

begin_comment
comment|// e.g. OperationContext or QueryContext
end_comment

begin_interface
specifier|public
interface|interface
name|OperationObserver
extends|extends
name|OperationHints
block|{
comment|/**      * Callback method invoked after an updating query is executed.      */
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
function_decl|;
comment|/**      * Callback method invoked after a batch update is executed.      */
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
function_decl|;
comment|/**      * Callback method invoked for each processed ResultSet.      */
specifier|public
name|void
name|nextDataRows
parameter_list|(
name|Query
name|query
parameter_list|,
name|List
name|dataRows
parameter_list|)
function_decl|;
comment|/**      * Callback method invoked for each opened ResultIterator. If this observer requested      * results to be returned as a ResultIterator, this method is invoked instead of      * "nextDataRows(Query,List)". OperationObserver is responsible for closing the      * ResultIterators passed via this method.      */
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
function_decl|;
comment|/**      * Callback method invoked after each batch of generated values is read durring an      * update.      *       * @since 1.2      */
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
function_decl|;
comment|/**      * Callback method invoked on exceptions that happen during an execution of a specific      * query.      */
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
function_decl|;
comment|/**      * Callback method invoked on exceptions that are not tied to a specific query      * execution, such as JDBC connection exceptions, etc.      */
specifier|public
name|void
name|nextGlobalException
parameter_list|(
name|Exception
name|ex
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

