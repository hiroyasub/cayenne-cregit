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
name|velocity
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|translator
operator|.
name|ParameterBinding
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|context
operator|.
name|InternalContextAdapter
import|;
end_import

begin_comment
comment|/**  * A custom Velocity directive to create a set of SQL conditions to check unequality of an  * ObjectId of an object. Usage in Velocity template is "WHERE  * #bindObjectNotEqual($object)" or "WHERE #bindObjectNotEqual($object $columns  * $idValues)".  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|BindObjectNotEqualDirective
extends|extends
name|BindObjectEqualDirective
block|{
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"bindObjectNotEqual"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|renderColumn
parameter_list|(
name|InternalContextAdapter
name|context
parameter_list|,
name|Writer
name|writer
parameter_list|,
name|Object
name|columnName
parameter_list|,
name|int
name|columnIndex
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|columnIndex
operator|>
literal|0
condition|)
block|{
name|writer
operator|.
name|write
argument_list|(
literal|" OR "
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|write
argument_list|(
name|columnName
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|render
parameter_list|(
name|InternalContextAdapter
name|context
parameter_list|,
name|Writer
name|writer
parameter_list|,
name|ParameterBinding
name|binding
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|binding
operator|.
name|getValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|bind
argument_list|(
name|context
argument_list|,
name|binding
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"<> ?"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|writer
operator|.
name|write
argument_list|(
literal|"IS NOT NULL"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

