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
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|SQLBuilder
block|{
specifier|protected
name|DBHelper
name|dbHelper
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|Object
argument_list|>
name|bindings
decl_stmt|;
specifier|protected
name|StringBuilder
name|sqlBuffer
decl_stmt|;
specifier|protected
name|SQLBuilder
parameter_list|(
name|DBHelper
name|dbHelper
parameter_list|,
name|StringBuilder
name|sqlBuffer
parameter_list|,
name|Collection
argument_list|<
name|Object
argument_list|>
name|bindings
parameter_list|)
block|{
name|this
operator|.
name|dbHelper
operator|=
name|dbHelper
expr_stmt|;
name|this
operator|.
name|bindings
operator|=
name|bindings
expr_stmt|;
name|this
operator|.
name|sqlBuffer
operator|=
name|sqlBuffer
expr_stmt|;
block|}
specifier|public
name|int
name|execute
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
operator|new
name|UpdateTemplate
argument_list|(
name|dbHelper
argument_list|)
operator|.
name|execute
argument_list|(
name|sqlBuffer
operator|.
name|toString
argument_list|()
argument_list|,
name|bindings
operator|.
name|toArray
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

