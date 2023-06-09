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

begin_comment
comment|/**  * A JDBC template for reading a single row from the database.  */
end_comment

begin_class
specifier|abstract
class|class
name|RowTemplate
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ResultSetTemplate
argument_list|<
name|T
argument_list|>
block|{
specifier|public
name|RowTemplate
parameter_list|(
name|DBHelper
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
specifier|abstract
name|T
name|readRow
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
annotation|@
name|Override
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
block|{
if|if
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|T
name|row
init|=
name|readRow
argument_list|(
name|rs
argument_list|,
name|sql
argument_list|)
decl_stmt|;
if|if
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"More than one result for sql: "
operator|+
name|sql
argument_list|)
throw|;
block|}
return|return
name|row
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"No results for sql: "
operator|+
name|sql
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

