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
comment|/**  * A SchemaUpdateStrategy that does nothing. This is usually the default strategy, as in  * most cases DB schema management is outside the scope of Cayenne.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|SkipSchemaUpdateStrategy
extends|extends
name|BaseSchemaUpdateStrategy
block|{
comment|/**      * @since 3.0      */
specifier|public
name|void
name|updateSchema
parameter_list|(
name|DataNode
name|dataNode
parameter_list|)
block|{
comment|// does nothing
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|public
name|void
name|generateUpdateSchema
parameter_list|(
name|DataNode
name|dataNode
parameter_list|)
block|{
comment|// does nothing
block|}
annotation|@
name|Override
specifier|protected
name|BaseSchemaUpdateStrategy
name|getSchema
parameter_list|()
block|{
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

