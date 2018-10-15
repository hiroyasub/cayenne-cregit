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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|DataObject
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

begin_comment
comment|/**  * Defines API for a DataContext "delegate" - an object that is temporarily passed control  * by DataContext at some critical points in the normal flow of execution. A delegate thus  * can modify the flow, abort an operation, modify the objects participating in an  * operation, or perform any other tasks it deems necessary. DataContextDelegate is shared  * by DataContext and its ObjectStore.  *   * @see org.apache.cayenne.access.DataContext  * @since 1.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|DataContextDelegate
block|{
comment|/**      * Invoked before a Query is executed via<em>DataContext.performQuery</em>. The      * delegate may substitute the Query with a different one or may return null to discard      * the query.      *       * @since 1.2      */
name|Query
name|willPerformQuery
parameter_list|(
name|DataContext
name|context
parameter_list|,
name|Query
name|query
parameter_list|)
function_decl|;
comment|/**      * Invoked before a Query is executed via<em>DataContext.performGenericQuery</em>.      * The delegate may substitute the Query with a different one or may return null to      * discard the query.      *       * @since 1.2      */
name|Query
name|willPerformGenericQuery
parameter_list|(
name|DataContext
name|context
parameter_list|,
name|Query
name|query
parameter_list|)
function_decl|;
comment|/**      * Invoked by parent DataContext whenever an object change is detected. This can be a      * change to the object snapshot, or a modification of an "independent" relationship      * not resulting in a snapshot change. In the later case snapshot argument may be      * null. If a delegate returns<code>true</code>, ObjectStore will attempt to merge      * the changes into an object.      */
name|boolean
name|shouldMergeChanges
parameter_list|(
name|DataObject
name|object
parameter_list|,
name|DataRow
name|snapshotInStore
parameter_list|)
function_decl|;
comment|/**      * Called after a successful merging of external changes to an object. If previosly a      * delegate returned<code>false</code> from      * {@link #shouldMergeChanges(DataObject, DataRow)}, this method is not invoked,      * since changes were not merged.      */
name|void
name|finishedMergeChanges
parameter_list|(
name|DataObject
name|object
parameter_list|)
function_decl|;
comment|/**      * Invoked by ObjectStore whenever it is detected that a database row was deleted for      * object. If a delegate returns<code>true</code>, ObjectStore will change      * MODIFIED objects to NEW (resulting in recreating the deleted record on next commit)      * and all other objects - to TRANSIENT. To block this behavior, delegate should      * return<code>false</code>, and possibly do its own processing.      *       * @param object DataObject that was deleted externally and is still present in the      *            ObjectStore associated with the delegate.      */
name|boolean
name|shouldProcessDelete
parameter_list|(
name|DataObject
name|object
parameter_list|)
function_decl|;
comment|/**      * Called after a successful processing of externally deleted object. If previosly a      * delegate returned<code>false</code> from      * {@link #shouldProcessDelete(DataObject)}, this method is not invoked, since no      * processing was done.      */
name|void
name|finishedProcessDelete
parameter_list|(
name|DataObject
name|object
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

