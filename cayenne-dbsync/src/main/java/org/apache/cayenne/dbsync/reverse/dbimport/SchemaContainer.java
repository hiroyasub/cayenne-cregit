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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|SchemaContainer
extends|extends
name|FilterContainer
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|Schema
argument_list|>
name|schemaCollection
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|List
argument_list|<
name|Schema
argument_list|>
name|getSchemas
parameter_list|()
block|{
return|return
name|schemaCollection
return|;
block|}
specifier|public
name|void
name|addSchema
parameter_list|(
name|Schema
name|schema
parameter_list|)
block|{
name|this
operator|.
name|schemaCollection
operator|.
name|add
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
specifier|public
name|SchemaContainer
parameter_list|()
block|{
block|}
specifier|public
name|SchemaContainer
parameter_list|(
name|SchemaContainer
name|original
parameter_list|)
block|{
name|super
argument_list|(
name|original
argument_list|)
expr_stmt|;
for|for
control|(
name|Schema
name|schema
range|:
name|original
operator|.
name|getSchemas
argument_list|()
control|)
block|{
name|this
operator|.
name|addSchema
argument_list|(
operator|new
name|Schema
argument_list|(
name|schema
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEmptyContainer
parameter_list|()
block|{
if|if
condition|(
operator|!
name|super
operator|.
name|isEmptyContainer
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|schemaCollection
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
for|for
control|(
name|Schema
name|schema
range|:
name|schemaCollection
control|)
block|{
if|if
condition|(
operator|!
name|schema
operator|.
name|isEmptyContainer
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
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
if|if
condition|(
operator|!
name|isBlank
argument_list|(
name|schemaCollection
argument_list|)
condition|)
block|{
for|for
control|(
name|Schema
name|schema
range|:
name|schemaCollection
control|)
block|{
name|schema
operator|.
name|toString
argument_list|(
name|res
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
block|}
block|}
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
block|}
end_class

end_unit

