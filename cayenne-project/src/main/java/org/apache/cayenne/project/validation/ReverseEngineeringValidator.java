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
name|project
operator|.
name|validation
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
name|dbimport
operator|.
name|ReverseEngineering
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
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ReverseEngineeringValidator
extends|extends
name|ConfigurationNodeValidator
block|{
name|void
name|validate
parameter_list|(
name|ReverseEngineering
name|reverseEngineering
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|validateName
argument_list|(
name|reverseEngineering
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
block|}
name|void
name|validateName
parameter_list|(
name|ReverseEngineering
name|reverseEngineering
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|String
name|name
init|=
name|reverseEngineering
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// Must have name
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|reverseEngineering
argument_list|,
literal|"Unnamed ReverseEngineering"
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
end_class

end_unit

