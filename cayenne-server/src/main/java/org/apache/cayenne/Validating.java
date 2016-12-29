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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_comment
comment|/**  * Defines a number of callback methods that allow an object to be validated before safe.  * Entity class can implement this interface and its methods will be called automatically.  *  * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|Validating
block|{
comment|/**      * Performs property validation of the NEW object, appending any validation failures      * to the provided validationResult object. This method is invoked by ObjectContext      * before committing a NEW object to the database.      */
name|void
name|validateForInsert
parameter_list|(
name|ValidationResult
name|validationResult
parameter_list|)
function_decl|;
comment|/**      * Performs property validation of the MODIFIED object, appending any validation      * failures to the provided validationResult object. This method is invoked by      * ObjectContext before committing a MODIFIED object to the database.      */
name|void
name|validateForUpdate
parameter_list|(
name|ValidationResult
name|validationResult
parameter_list|)
function_decl|;
comment|/**      * Performs property validation of the DELETED object, appending any validation      * failures to the provided validationResult object. This method is invoked by      * ObjectContext before committing a DELETED object to the database.      */
name|void
name|validateForDelete
parameter_list|(
name|ValidationResult
name|validationResult
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

