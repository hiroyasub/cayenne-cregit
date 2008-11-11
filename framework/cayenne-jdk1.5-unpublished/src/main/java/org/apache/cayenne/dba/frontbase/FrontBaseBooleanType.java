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
name|dba
operator|.
name|frontbase
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

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
name|access
operator|.
name|types
operator|.
name|BooleanType
import|;
end_import

begin_comment
comment|/**  * Overrides default BooleanType behavior to bind BOOLEAN type to PreparedStatements via  * "setObject", as binding via "setBoolean" only works for BIT.  *   * @since 1.2  */
end_comment

begin_class
class|class
name|FrontBaseBooleanType
extends|extends
name|BooleanType
block|{
annotation|@
name|Override
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|st
parameter_list|,
name|Object
name|val
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|precision
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|st
operator|.
name|setNull
argument_list|(
name|pos
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|BIT
condition|)
block|{
name|boolean
name|flag
init|=
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|val
argument_list|)
decl_stmt|;
name|st
operator|.
name|setBoolean
argument_list|(
name|pos
argument_list|,
name|flag
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|st
operator|.
name|setObject
argument_list|(
name|pos
argument_list|,
name|val
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

