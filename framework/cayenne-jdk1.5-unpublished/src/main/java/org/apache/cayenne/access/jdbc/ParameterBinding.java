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
name|jdbc
package|;
end_package

begin_comment
comment|/**  * Describes PreparedStatement parameter binding.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|ParameterBinding
block|{
specifier|protected
name|int
name|jdbcType
decl_stmt|;
specifier|protected
name|int
name|precision
decl_stmt|;
specifier|protected
name|Object
name|value
decl_stmt|;
specifier|public
name|ParameterBinding
parameter_list|(
name|Object
name|value
parameter_list|,
name|int
name|jdbcType
parameter_list|,
name|int
name|precision
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
name|precision
operator|=
name|precision
expr_stmt|;
block|}
specifier|public
name|int
name|getJdbcType
parameter_list|()
block|{
return|return
name|jdbcType
return|;
block|}
specifier|public
name|int
name|getPrecision
parameter_list|()
block|{
return|return
name|precision
return|;
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
name|setJdbcType
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|jdbcType
operator|=
name|i
expr_stmt|;
block|}
specifier|public
name|void
name|setPrecision
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|precision
operator|=
name|i
expr_stmt|;
block|}
specifier|public
name|void
name|setValue
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|value
operator|=
name|object
expr_stmt|;
block|}
block|}
end_class

end_unit

