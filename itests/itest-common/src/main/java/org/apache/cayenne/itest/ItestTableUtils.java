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
name|SQLException
import|;
end_import

begin_comment
comment|/**  * JDBC utilities for integration testing that bypass Cayenne for DB access.  *   */
end_comment

begin_class
specifier|public
class|class
name|ItestTableUtils
block|{
specifier|protected
name|String
name|tableName
decl_stmt|;
specifier|protected
name|ItestDBUtils
name|dbUtils
decl_stmt|;
specifier|protected
name|String
index|[]
name|columns
decl_stmt|;
specifier|public
name|ItestTableUtils
parameter_list|(
name|ItestDBUtils
name|dbUtils
parameter_list|,
name|String
name|tableName
parameter_list|)
block|{
name|this
operator|.
name|dbUtils
operator|=
name|dbUtils
expr_stmt|;
name|this
operator|.
name|tableName
operator|=
name|tableName
expr_stmt|;
block|}
specifier|public
name|ItestTableUtils
name|deleteAll
parameter_list|()
throws|throws
name|SQLException
block|{
name|dbUtils
operator|.
name|deleteAll
argument_list|(
name|tableName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ItestTableUtils
name|setColumns
parameter_list|(
name|String
modifier|...
name|columns
parameter_list|)
block|{
name|this
operator|.
name|columns
operator|=
name|columns
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ItestTableUtils
name|insert
parameter_list|(
name|Object
modifier|...
name|values
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|this
operator|.
name|columns
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Call 'setColumns' to prepare insert"
argument_list|)
throw|;
block|}
if|if
condition|(
name|this
operator|.
name|columns
operator|.
name|length
operator|!=
name|values
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Columns and values arrays are of different size"
argument_list|)
throw|;
block|}
name|dbUtils
operator|.
name|insert
argument_list|(
name|tableName
argument_list|,
name|columns
argument_list|,
name|values
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

