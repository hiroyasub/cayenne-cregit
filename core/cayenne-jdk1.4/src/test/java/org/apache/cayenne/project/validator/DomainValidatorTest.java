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
name|validator
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
name|access
operator|.
name|DataDomain
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
name|project
operator|.
name|ProjectPath
import|;
end_import

begin_comment
comment|/**  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|DomainValidatorTest
extends|extends
name|ValidatorTestBase
block|{
specifier|public
name|void
name|testValidateDomains
parameter_list|()
throws|throws
name|Exception
block|{
comment|// should succeed
name|validator
operator|.
name|reset
argument_list|()
expr_stmt|;
name|DataDomain
name|d1
init|=
operator|new
name|DataDomain
argument_list|(
literal|"abc"
argument_list|)
decl_stmt|;
operator|new
name|DomainValidator
argument_list|()
operator|.
name|validateObject
argument_list|(
operator|new
name|ProjectPath
argument_list|(
operator|new
name|Object
index|[]
block|{
name|project
block|,
name|d1
block|}
argument_list|)
argument_list|,
name|validator
argument_list|)
expr_stmt|;
name|assertValidator
argument_list|(
name|ValidationInfo
operator|.
name|VALID
argument_list|)
expr_stmt|;
comment|// should complain about duplicate name
name|DataDomain
name|d3
init|=
operator|new
name|DataDomain
argument_list|(
literal|"xyz"
argument_list|)
decl_stmt|;
name|project
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addDomain
argument_list|(
name|d3
argument_list|)
expr_stmt|;
name|project
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addDomain
argument_list|(
name|d1
argument_list|)
expr_stmt|;
name|d3
operator|.
name|setName
argument_list|(
name|d1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|validator
operator|.
name|reset
argument_list|()
expr_stmt|;
operator|new
name|DomainValidator
argument_list|()
operator|.
name|validateObject
argument_list|(
operator|new
name|ProjectPath
argument_list|(
operator|new
name|Object
index|[]
block|{
name|project
block|,
name|d3
block|}
argument_list|)
argument_list|,
name|validator
argument_list|)
expr_stmt|;
name|assertValidator
argument_list|(
name|ValidationInfo
operator|.
name|ERROR
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

