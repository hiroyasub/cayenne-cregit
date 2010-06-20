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
name|util
operator|.
name|Collection
import|;
end_import

begin_class
specifier|public
class|class
name|WhereBuilder
extends|extends
name|SQLBuilder
block|{
specifier|protected
name|int
name|whereCount
decl_stmt|;
specifier|protected
name|WhereBuilder
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
parameter_list|,
name|Collection
argument_list|<
name|Integer
argument_list|>
name|bindingTypes
parameter_list|)
block|{
name|super
argument_list|(
name|dbHelper
argument_list|,
name|sqlBuffer
argument_list|,
name|bindings
argument_list|,
name|bindingTypes
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" where "
argument_list|)
expr_stmt|;
block|}
specifier|public
name|WhereBuilder
name|and
parameter_list|(
name|String
name|column
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|and
argument_list|(
name|column
argument_list|,
name|value
argument_list|,
name|NO_TYPE
argument_list|)
return|;
block|}
specifier|public
name|WhereBuilder
name|and
parameter_list|(
name|String
name|column
parameter_list|,
name|Object
name|value
parameter_list|,
name|int
name|valueType
parameter_list|)
block|{
if|if
condition|(
name|whereCount
operator|++
operator|>
literal|0
condition|)
block|{
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" and "
argument_list|)
expr_stmt|;
block|}
name|sqlBuffer
operator|.
name|append
argument_list|(
name|dbHelper
operator|.
name|quote
argument_list|(
name|column
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|" = ?"
argument_list|)
expr_stmt|;
name|initBinding
argument_list|(
name|value
argument_list|,
name|valueType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|WhereBuilder
name|or
parameter_list|(
name|String
name|column
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|or
argument_list|(
name|column
argument_list|,
name|value
argument_list|,
name|NO_TYPE
argument_list|)
return|;
block|}
specifier|public
name|WhereBuilder
name|or
parameter_list|(
name|String
name|column
parameter_list|,
name|Object
name|value
parameter_list|,
name|int
name|valueType
parameter_list|)
block|{
if|if
condition|(
name|whereCount
operator|++
operator|>
literal|0
condition|)
block|{
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" or "
argument_list|)
expr_stmt|;
block|}
name|sqlBuffer
operator|.
name|append
argument_list|(
name|dbHelper
operator|.
name|quote
argument_list|(
name|column
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|" = ?"
argument_list|)
expr_stmt|;
name|initBinding
argument_list|(
name|value
argument_list|,
name|valueType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

