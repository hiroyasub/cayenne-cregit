begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *<p/>  * https://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  ****************************************************************/
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
name|map
operator|.
name|ProcedureParameter
import|;
end_import

begin_comment
comment|/**  * Describes a PreparedStatement parameter binding mapped to a DbAttribute.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ProcedureParameterBinding
extends|extends
name|ParameterBinding
block|{
specifier|private
specifier|final
name|ProcedureParameter
name|parameter
decl_stmt|;
specifier|public
name|ProcedureParameterBinding
parameter_list|(
name|ProcedureParameter
name|procedureParameter
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|parameter
operator|=
name|procedureParameter
expr_stmt|;
block|}
specifier|public
name|ProcedureParameter
name|getParameter
parameter_list|()
block|{
return|return
name|parameter
return|;
block|}
annotation|@
name|Override
specifier|public
name|Integer
name|getJdbcType
parameter_list|()
block|{
return|return
name|parameter
operator|.
name|getType
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getScale
parameter_list|()
block|{
return|return
name|parameter
operator|.
name|getPrecision
argument_list|()
return|;
block|}
block|}
end_class

end_unit

