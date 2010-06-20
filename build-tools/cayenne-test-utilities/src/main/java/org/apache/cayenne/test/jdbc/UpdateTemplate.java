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
name|test
operator|.
name|jdbc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

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
name|SQLException
import|;
end_import

begin_class
class|class
name|UpdateTemplate
block|{
name|DBHelper
name|parent
decl_stmt|;
specifier|public
name|UpdateTemplate
parameter_list|(
name|DBHelper
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
specifier|protected
name|void
name|bindParameters
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|Object
modifier|...
name|bindings
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|bindings
operator|!=
literal|null
operator|&&
name|bindings
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|bindings
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|statement
operator|.
name|setObject
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|bindings
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|int
name|execute
parameter_list|(
name|String
name|sql
parameter_list|,
name|Object
modifier|...
name|bindings
parameter_list|)
throws|throws
name|SQLException
block|{
name|UtilityLogger
operator|.
name|log
argument_list|(
name|sql
argument_list|)
expr_stmt|;
name|Connection
name|c
init|=
name|parent
operator|.
name|getConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|PreparedStatement
name|st
init|=
name|c
operator|.
name|prepareStatement
argument_list|(
name|sql
argument_list|)
decl_stmt|;
try|try
block|{
name|bindParameters
argument_list|(
name|st
argument_list|,
name|bindings
argument_list|)
expr_stmt|;
return|return
name|st
operator|.
name|executeUpdate
argument_list|()
return|;
block|}
finally|finally
block|{
name|st
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

