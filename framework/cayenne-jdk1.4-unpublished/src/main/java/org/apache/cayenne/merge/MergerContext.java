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
name|merge
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
name|DataMap
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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_comment
comment|/**  * An object passed as an argument to {@link MergerToken#execute(MergerContext)}s that a  * {@link MergerToken} can do its work.  *   * @author  */
end_comment

begin_interface
specifier|public
interface|interface
name|MergerContext
block|{
specifier|public
name|void
name|executeSql
parameter_list|(
name|String
name|sql
parameter_list|)
function_decl|;
specifier|public
name|DbAdapter
name|getAdapter
parameter_list|()
function_decl|;
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
function_decl|;
specifier|public
name|ValidationResult
name|getValidationResult
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

