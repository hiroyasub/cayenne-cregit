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
name|flush
operator|.
name|operation
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
name|ObjectId
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
name|Persistent
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
name|DbEntity
import|;
end_import

begin_comment
comment|/**  * Object that represents some change on DB level.  * Common cases are insert/update/delete of single DB row.  *  * @since 4.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|DbRowOp
block|{
parameter_list|<
name|T
parameter_list|>
name|T
name|accept
parameter_list|(
name|DbRowOpVisitor
argument_list|<
name|T
argument_list|>
name|visitor
parameter_list|)
function_decl|;
name|DbEntity
name|getEntity
parameter_list|()
function_decl|;
name|ObjectId
name|getChangeId
parameter_list|()
function_decl|;
name|Persistent
name|getObject
parameter_list|()
function_decl|;
comment|/**      * @param rowOp to check      * @return is this and rowOp operations belong to same sql batch      */
name|boolean
name|isSameBatch
parameter_list|(
name|DbRowOp
name|rowOp
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

