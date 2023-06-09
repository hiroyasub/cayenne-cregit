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
name|project
operator|.
name|validation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
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
name|dba
operator|.
name|TypesMapping
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
name|ProcedureParameter
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

begin_class
class|class
name|ProcedureParameterValidator
extends|extends
name|ConfigurationNodeValidator
block|{
name|void
name|validate
parameter_list|(
name|ProcedureParameter
name|parameter
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
comment|// Must have name
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|parameter
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|parameter
argument_list|,
literal|"Unnamed ProcedureParameter"
argument_list|)
expr_stmt|;
block|}
comment|// all attributes must have type
if|if
condition|(
name|parameter
operator|.
name|getType
argument_list|()
operator|==
name|TypesMapping
operator|.
name|NOT_DEFINED
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|parameter
argument_list|,
literal|"ProcedureParameter '%s' has no type"
argument_list|,
name|parameter
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// VARCHAR and CHAR attributes must have max length
if|if
condition|(
name|parameter
operator|.
name|getMaxLength
argument_list|()
operator|<
literal|0
operator|&&
operator|(
name|parameter
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|VARCHAR
operator|||
name|parameter
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|NVARCHAR
operator|||
name|parameter
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|CHAR
operator|||
name|parameter
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|NCHAR
operator|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|parameter
argument_list|,
literal|"Character ProcedureParameter '%s' doesn't have max length"
argument_list|,
name|parameter
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// all attributes must have type
if|if
condition|(
name|parameter
operator|.
name|getDirection
argument_list|()
operator|<=
literal|0
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|parameter
argument_list|,
literal|"ProcedureParameter '%s' has no direction"
argument_list|,
name|parameter
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

