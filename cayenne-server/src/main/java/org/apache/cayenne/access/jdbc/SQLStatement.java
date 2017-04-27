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

begin_comment
comment|/**  * A PreparedStatement descriptor containing a String of SQL and an array of parameters.  * SQLStatement is essentially a "compiled" version of any single query.  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|SQLStatement
block|{
specifier|protected
name|String
name|sql
decl_stmt|;
specifier|protected
name|ParameterBinding
index|[]
name|bindings
decl_stmt|;
specifier|protected
name|ColumnDescriptor
index|[]
name|resultColumns
decl_stmt|;
specifier|public
name|SQLStatement
parameter_list|()
block|{
block|}
specifier|public
name|SQLStatement
parameter_list|(
name|String
name|sql
parameter_list|,
name|ParameterBinding
index|[]
name|bindings
parameter_list|)
block|{
name|this
argument_list|(
name|sql
argument_list|,
literal|null
argument_list|,
name|bindings
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|SQLStatement
parameter_list|(
name|String
name|sql
parameter_list|,
name|ColumnDescriptor
index|[]
name|resultColumns
parameter_list|,
name|ParameterBinding
index|[]
name|bindings
parameter_list|)
block|{
name|setSql
argument_list|(
name|sql
argument_list|)
expr_stmt|;
name|setBindings
argument_list|(
name|bindings
argument_list|)
expr_stmt|;
name|setResultColumns
argument_list|(
name|resultColumns
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|ColumnDescriptor
index|[]
name|getResultColumns
parameter_list|()
block|{
return|return
name|resultColumns
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|void
name|setResultColumns
parameter_list|(
name|ColumnDescriptor
index|[]
name|descriptors
parameter_list|)
block|{
name|resultColumns
operator|=
name|descriptors
expr_stmt|;
block|}
specifier|public
name|ParameterBinding
index|[]
name|getBindings
parameter_list|()
block|{
return|return
name|bindings
return|;
block|}
specifier|public
name|String
name|getSql
parameter_list|()
block|{
return|return
name|sql
return|;
block|}
specifier|public
name|void
name|setBindings
parameter_list|(
name|ParameterBinding
index|[]
name|bindings
parameter_list|)
block|{
name|this
operator|.
name|bindings
operator|=
name|bindings
expr_stmt|;
block|}
specifier|public
name|void
name|setSql
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|sql
operator|=
name|string
expr_stmt|;
block|}
block|}
end_class

end_unit

