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
name|access
operator|.
name|jdbc
operator|.
name|ColumnDescriptor
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
name|DbAttributeBinding
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
name|ObjAttribute
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
name|SelectQuery
import|;
end_import

begin_comment
comment|/**  * An abstraction of {@link SelectQuery} translator.  *   * @since 4.0 this is an interface.  */
end_comment

begin_interface
specifier|public
interface|interface
name|SelectTranslator
block|{
name|String
name|getSql
parameter_list|()
throws|throws
name|Exception
function_decl|;
name|DbAttributeBinding
index|[]
name|getBindings
parameter_list|()
function_decl|;
name|Map
argument_list|<
name|ObjAttribute
argument_list|,
name|ColumnDescriptor
argument_list|>
name|getAttributeOverrides
parameter_list|()
function_decl|;
name|ColumnDescriptor
index|[]
name|getResultColumns
parameter_list|()
function_decl|;
name|boolean
name|isSuppressingDistinct
parameter_list|()
function_decl|;
comment|/** 	 * @since 4.0 	 * @return do query has at least one join 	 */
name|boolean
name|hasJoins
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

