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
name|dbsync
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DataNode
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|BaseSchemaUpdateStrategy
implements|implements
name|SchemaUpdateStrategy
block|{
specifier|protected
name|BaseSchemaUpdateStrategy
name|currentSchema
decl_stmt|;
specifier|protected
specifier|abstract
name|BaseSchemaUpdateStrategy
name|getSchema
parameter_list|()
function_decl|;
comment|/**      * @since 3.0      */
specifier|public
name|void
name|generateUpdateSchema
parameter_list|(
name|DataNode
name|dataNode
parameter_list|)
throws|throws
name|SQLException
block|{
name|getSchema
argument_list|()
operator|.
name|generateUpdateSchema
argument_list|(
name|dataNode
argument_list|)
expr_stmt|;
name|currentSchema
operator|=
operator|new
name|SkipSchemaUpdateStrategy
argument_list|()
expr_stmt|;
block|}
empty_stmt|;
block|}
end_class

end_unit

