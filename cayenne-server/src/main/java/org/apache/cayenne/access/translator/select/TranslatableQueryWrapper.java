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
name|translator
operator|.
name|select
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|property
operator|.
name|BaseProperty
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
name|Ordering
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
name|QueryMetadata
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
name|Select
import|;
end_import

begin_comment
comment|/**  * This interface allows transparently use different queries (namely SelectQuery, ObjectSelect and ColumnSelect)  * in translator and as subqueries.  *  * @since 4.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|TranslatableQueryWrapper
block|{
name|boolean
name|isDistinct
parameter_list|()
function_decl|;
name|QueryMetadata
name|getMetaData
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
function_decl|;
name|Expression
name|getQualifier
parameter_list|()
function_decl|;
name|Collection
argument_list|<
name|Ordering
argument_list|>
name|getOrderings
parameter_list|()
function_decl|;
name|Collection
argument_list|<
name|BaseProperty
argument_list|<
name|?
argument_list|>
argument_list|>
name|getColumns
parameter_list|()
function_decl|;
name|Expression
name|getHavingQualifier
parameter_list|()
function_decl|;
name|Select
argument_list|<
name|?
argument_list|>
name|unwrap
parameter_list|()
function_decl|;
specifier|default
name|boolean
name|needsResultSetMapping
parameter_list|()
block|{
return|return
name|getColumns
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|getColumns
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
end_interface

end_unit

