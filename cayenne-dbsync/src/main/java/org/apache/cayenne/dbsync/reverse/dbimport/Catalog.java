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
name|dbsync
operator|.
name|reverse
operator|.
name|dbimport
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
name|ConfigurationNodeVisitor
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
name|util
operator|.
name|XMLEncoder
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
name|util
operator|.
name|XMLSerializable
import|;
end_import

begin_comment
comment|/**  * @since 4.0.  */
end_comment

begin_class
specifier|public
class|class
name|Catalog
extends|extends
name|SchemaContainer
implements|implements
name|XMLSerializable
block|{
specifier|public
name|Catalog
parameter_list|()
block|{
block|}
specifier|public
name|Catalog
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Catalog
parameter_list|(
name|Catalog
name|original
parameter_list|)
block|{
name|super
argument_list|(
name|original
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|StringBuilder
name|toString
parameter_list|(
name|StringBuilder
name|res
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|res
operator|.
name|append
argument_list|(
name|prefix
argument_list|)
operator|.
name|append
argument_list|(
literal|"Catalog: "
argument_list|)
operator|.
name|append
argument_list|(
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|toString
argument_list|(
name|res
argument_list|,
name|prefix
operator|+
literal|"  "
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|,
name|ConfigurationNodeVisitor
name|delegate
parameter_list|)
block|{
name|encoder
operator|.
name|start
argument_list|(
literal|"catalog"
argument_list|)
operator|.
name|nested
argument_list|(
name|this
operator|.
name|getIncludeTables
argument_list|()
argument_list|,
name|delegate
argument_list|)
operator|.
name|nested
argument_list|(
name|this
operator|.
name|getExcludeTables
argument_list|()
argument_list|,
name|delegate
argument_list|)
operator|.
name|nested
argument_list|(
name|this
operator|.
name|getIncludeColumns
argument_list|()
argument_list|,
name|delegate
argument_list|)
operator|.
name|nested
argument_list|(
name|this
operator|.
name|getExcludeColumns
argument_list|()
argument_list|,
name|delegate
argument_list|)
operator|.
name|nested
argument_list|(
name|this
operator|.
name|getIncludeProcedures
argument_list|()
argument_list|,
name|delegate
argument_list|)
operator|.
name|nested
argument_list|(
name|this
operator|.
name|getExcludeProcedures
argument_list|()
argument_list|,
name|delegate
argument_list|)
operator|.
name|simpleTag
argument_list|(
literal|"name"
argument_list|,
name|this
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|nested
argument_list|(
name|this
operator|.
name|getSchemas
argument_list|()
argument_list|,
name|delegate
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

