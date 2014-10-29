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
name|types
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|CharTypeTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testRightTrim
parameter_list|()
block|{
name|CharType
name|charType
init|=
operator|new
name|CharType
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|trimmedStr
init|=
name|charType
operator|.
name|rtrim
argument_list|(
literal|"  text    "
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Trimmed string is not valid."
argument_list|,
name|trimmedStr
operator|.
name|equals
argument_list|(
literal|"  text"
argument_list|)
argument_list|)
expr_stmt|;
name|trimmedStr
operator|=
name|charType
operator|.
name|rtrim
argument_list|(
literal|" text"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Trimmed string is not valid."
argument_list|,
name|trimmedStr
operator|.
name|equals
argument_list|(
literal|" text"
argument_list|)
argument_list|)
expr_stmt|;
name|trimmedStr
operator|=
name|charType
operator|.
name|rtrim
argument_list|(
literal|"text"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Trimmed string is not valid."
argument_list|,
name|trimmedStr
operator|.
name|equals
argument_list|(
literal|"text"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

