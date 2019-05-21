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
name|access
operator|.
name|translator
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
name|types
operator|.
name|ExtendedType
import|;
end_import

begin_comment
comment|/**  * Describes a PreparedStatement parameter generic binding.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ParameterBinding
block|{
specifier|private
specifier|static
specifier|final
name|int
name|EXCLUDED_POSITION
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|Object
name|value
decl_stmt|;
specifier|private
name|int
name|statementPosition
decl_stmt|;
specifier|private
name|ExtendedType
name|extendedType
decl_stmt|;
specifier|private
name|Integer
name|jdbcType
decl_stmt|;
specifier|private
name|int
name|scale
decl_stmt|;
specifier|public
name|ParameterBinding
parameter_list|(
name|Object
name|value
parameter_list|,
name|Integer
name|jdbcType
parameter_list|,
name|int
name|scale
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|jdbcType
operator|=
name|jdbcType
expr_stmt|;
name|this
operator|.
name|scale
operator|=
name|scale
expr_stmt|;
block|}
specifier|public
name|ParameterBinding
parameter_list|()
block|{
name|this
operator|.
name|statementPosition
operator|=
name|EXCLUDED_POSITION
expr_stmt|;
block|}
specifier|public
name|Object
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|int
name|getStatementPosition
parameter_list|()
block|{
return|return
name|statementPosition
return|;
block|}
specifier|public
name|void
name|setStatementPosition
parameter_list|(
name|int
name|statementPosition
parameter_list|)
block|{
name|this
operator|.
name|statementPosition
operator|=
name|statementPosition
expr_stmt|;
block|}
specifier|public
name|boolean
name|isExcluded
parameter_list|()
block|{
return|return
name|statementPosition
operator|==
name|EXCLUDED_POSITION
return|;
block|}
specifier|public
name|ExtendedType
name|getExtendedType
parameter_list|()
block|{
return|return
name|extendedType
return|;
block|}
specifier|public
name|void
name|setExtendedType
parameter_list|(
name|ExtendedType
name|extendedType
parameter_list|)
block|{
name|this
operator|.
name|extendedType
operator|=
name|extendedType
expr_stmt|;
block|}
comment|/** 	 * Marks the binding object as excluded for the current iteration. 	 */
specifier|public
name|void
name|exclude
parameter_list|()
block|{
name|this
operator|.
name|statementPosition
operator|=
name|EXCLUDED_POSITION
expr_stmt|;
name|this
operator|.
name|value
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|extendedType
operator|=
literal|null
expr_stmt|;
block|}
comment|/** 	 * Sets the value of the binding and initializes statement position var, 	 * thus "including" this binding in the current iteration. 	 */
specifier|public
name|void
name|include
parameter_list|(
name|int
name|statementPosition
parameter_list|,
name|Object
name|value
parameter_list|,
name|ExtendedType
name|extendedType
parameter_list|)
block|{
name|this
operator|.
name|statementPosition
operator|=
name|statementPosition
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|extendedType
operator|=
name|extendedType
expr_stmt|;
block|}
specifier|public
name|Integer
name|getJdbcType
parameter_list|()
block|{
return|return
name|jdbcType
return|;
block|}
specifier|public
name|void
name|setJdbcType
parameter_list|(
name|Integer
name|type
parameter_list|)
block|{
name|this
operator|.
name|jdbcType
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|int
name|getScale
parameter_list|()
block|{
return|return
name|scale
return|;
block|}
specifier|public
name|void
name|setScale
parameter_list|(
name|int
name|scale
parameter_list|)
block|{
name|this
operator|.
name|scale
operator|=
name|scale
expr_stmt|;
block|}
block|}
end_class

end_unit

