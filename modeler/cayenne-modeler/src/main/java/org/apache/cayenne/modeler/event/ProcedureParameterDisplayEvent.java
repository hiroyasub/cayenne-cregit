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
name|modeler
operator|.
name|event
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
name|configuration
operator|.
name|DataChannelDescriptor
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
name|map
operator|.
name|DataMap
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
name|map
operator|.
name|Procedure
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
name|map
operator|.
name|ProcedureParameter
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|ProcedureParameterDisplayEvent
extends|extends
name|ProcedureDisplayEvent
block|{
specifier|protected
name|ProcedureParameter
index|[]
name|procedureParameters
decl_stmt|;
specifier|public
name|ProcedureParameterDisplayEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|ProcedureParameter
name|procedureParameter
parameter_list|,
name|Procedure
name|procedure
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|DataChannelDescriptor
name|domain
parameter_list|)
block|{
name|super
argument_list|(
name|src
argument_list|,
name|procedure
argument_list|,
name|map
argument_list|,
name|domain
argument_list|)
expr_stmt|;
name|this
operator|.
name|procedureParameters
operator|=
operator|new
name|ProcedureParameter
index|[]
block|{
name|procedureParameter
block|}
expr_stmt|;
block|}
specifier|public
name|ProcedureParameterDisplayEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|ProcedureParameter
index|[]
name|procedureParameters
parameter_list|,
name|Procedure
name|procedure
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|DataChannelDescriptor
name|domain
parameter_list|)
block|{
name|super
argument_list|(
name|src
argument_list|,
name|procedure
argument_list|,
name|map
argument_list|,
name|domain
argument_list|)
expr_stmt|;
name|this
operator|.
name|procedureParameters
operator|=
name|procedureParameters
expr_stmt|;
block|}
specifier|public
name|ProcedureParameter
index|[]
name|getProcedureParameters
parameter_list|()
block|{
return|return
name|procedureParameters
return|;
block|}
specifier|public
name|void
name|setProcedureParameters
parameter_list|(
name|ProcedureParameter
index|[]
name|parameters
parameter_list|)
block|{
name|procedureParameters
operator|=
name|parameters
expr_stmt|;
block|}
block|}
end_class

end_unit

