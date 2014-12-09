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
name|tools
operator|.
name|dbimport
operator|.
name|config
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_comment
comment|/**  * @since 4.0.  */
end_comment

begin_class
specifier|public
class|class
name|ReverseEngineering
extends|extends
name|FilterContainer
block|{
specifier|private
name|Collection
argument_list|<
name|Catalog
argument_list|>
name|catalogs
init|=
operator|new
name|LinkedList
argument_list|<
name|Catalog
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|Schema
argument_list|>
name|schemas
init|=
operator|new
name|LinkedList
argument_list|<
name|Schema
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|ReverseEngineering
parameter_list|()
block|{
block|}
specifier|public
name|Collection
argument_list|<
name|Catalog
argument_list|>
name|getCatalogs
parameter_list|()
block|{
return|return
name|catalogs
return|;
block|}
specifier|public
name|void
name|setCatalogs
parameter_list|(
name|Collection
argument_list|<
name|Catalog
argument_list|>
name|catalogs
parameter_list|)
block|{
name|this
operator|.
name|catalogs
operator|=
name|catalogs
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|Schema
argument_list|>
name|getSchemas
parameter_list|()
block|{
return|return
name|schemas
return|;
block|}
specifier|public
name|void
name|setSchemas
parameter_list|(
name|Collection
argument_list|<
name|Schema
argument_list|>
name|schemas
parameter_list|)
block|{
name|this
operator|.
name|schemas
operator|=
name|schemas
expr_stmt|;
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
name|schemas
operator|.
name|add
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addCatalog
parameter_list|(
name|Catalog
name|catalog
parameter_list|)
block|{
name|this
operator|.
name|catalogs
operator|.
name|add
argument_list|(
name|catalog
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

