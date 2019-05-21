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
name|map
operator|.
name|Procedure
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
name|ProcedureValidator
extends|extends
name|ConfigurationNodeValidator
block|{
name|void
name|validate
parameter_list|(
name|Procedure
name|procedure
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|validateName
argument_list|(
name|procedure
argument_list|,
name|validationResult
argument_list|)
expr_stmt|;
comment|// check that return value is present
if|if
condition|(
name|procedure
operator|.
name|isReturningValue
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|ProcedureParameter
argument_list|>
name|parameters
init|=
name|procedure
operator|.
name|getCallParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|procedure
argument_list|,
literal|"Procedure '%s' returns a value, but has no parameters"
argument_list|,
name|procedure
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|void
name|validateName
parameter_list|(
name|Procedure
name|procedure
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|String
name|name
init|=
name|procedure
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
name|procedure
argument_list|,
literal|"Unnamed Procedure"
argument_list|)
expr_stmt|;
return|return;
block|}
name|DataMap
name|map
init|=
name|procedure
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// check for duplicate names in the parent context
for|for
control|(
specifier|final
name|Procedure
name|otherProcedure
range|:
name|map
operator|.
name|getProcedures
argument_list|()
control|)
block|{
if|if
condition|(
name|otherProcedure
operator|==
name|procedure
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|otherProcedure
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
name|procedure
argument_list|,
literal|"Duplicate Procedure name: %s"
argument_list|,
name|procedure
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
end_class

end_unit

