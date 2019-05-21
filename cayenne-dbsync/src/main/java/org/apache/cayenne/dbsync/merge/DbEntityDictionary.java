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
name|merge
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
name|dbsync
operator|.
name|reverse
operator|.
name|filters
operator|.
name|FiltersConfig
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
name|dbsync
operator|.
name|reverse
operator|.
name|filters
operator|.
name|TableFilter
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
name|DbEntity
import|;
end_import

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

begin_class
class|class
name|DbEntityDictionary
extends|extends
name|MergerDictionary
argument_list|<
name|DbEntity
argument_list|>
block|{
specifier|private
specifier|final
name|DataMap
name|container
decl_stmt|;
specifier|private
specifier|final
name|FiltersConfig
name|filtersConfig
decl_stmt|;
name|DbEntityDictionary
parameter_list|(
name|DataMap
name|container
parameter_list|,
name|FiltersConfig
name|filtersConfig
parameter_list|)
block|{
name|this
operator|.
name|container
operator|=
name|container
expr_stmt|;
name|this
operator|.
name|filtersConfig
operator|=
name|filtersConfig
expr_stmt|;
block|}
annotation|@
name|Override
name|String
name|getName
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
comment|//        return entity.getFullyQualifiedName();
return|return
name|entity
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|getAll
parameter_list|()
block|{
return|return
name|filter
argument_list|()
return|;
block|}
specifier|private
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|filter
parameter_list|()
block|{
if|if
condition|(
name|filtersConfig
operator|==
literal|null
condition|)
block|{
return|return
name|container
operator|.
name|getDbEntities
argument_list|()
return|;
block|}
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|existingFiltered
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbEntity
name|entity
range|:
name|container
operator|.
name|getDbEntities
argument_list|()
control|)
block|{
name|TableFilter
name|tableFilter
init|=
name|filtersConfig
operator|.
name|tableFilter
argument_list|(
name|entity
operator|.
name|getCatalog
argument_list|()
argument_list|,
name|entity
operator|.
name|getSchema
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tableFilter
operator|!=
literal|null
operator|&&
name|tableFilter
operator|.
name|isIncludeTable
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|existingFiltered
operator|.
name|add
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|existingFiltered
return|;
block|}
block|}
end_class

end_unit

