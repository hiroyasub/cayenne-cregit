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
name|itest
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
name|ResultSet
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
specifier|abstract
class|class
name|ResultSetTemplate
parameter_list|<
name|T
parameter_list|>
block|{
name|ItestDBUtils
name|parent
decl_stmt|;
specifier|public
name|ResultSetTemplate
parameter_list|(
name|ItestDBUtils
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
specifier|abstract
name|T
name|readResultSet
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
function_decl|;
name|T
name|execute
parameter_list|(
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
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
name|ResultSet
name|rs
init|=
name|st
operator|.
name|executeQuery
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|readResultSet
argument_list|(
name|rs
argument_list|,
name|sql
argument_list|)
return|;
block|}
finally|finally
block|{
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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

