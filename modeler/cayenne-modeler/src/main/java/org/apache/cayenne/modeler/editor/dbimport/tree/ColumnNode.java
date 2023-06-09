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
name|modeler
operator|.
name|editor
operator|.
name|dbimport
operator|.
name|tree
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
name|Collection
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
name|dbimport
operator|.
name|ExcludeColumn
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
name|dbimport
operator|.
name|FilterContainer
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
name|dbimport
operator|.
name|IncludeColumn
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
name|dbimport
operator|.
name|IncludeTable
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
name|dbimport
operator|.
name|ReverseEngineering
import|;
end_import

begin_class
class|class
name|ColumnNode
extends|extends
name|Node
argument_list|<
name|TableNode
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
name|ColumnNode
parameter_list|(
name|String
name|name
parameter_list|,
name|TableNode
argument_list|<
name|?
argument_list|>
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Status
name|getStatus
parameter_list|(
name|ReverseEngineering
name|config
parameter_list|)
block|{
name|Status
name|parentStatus
init|=
name|getParent
argument_list|()
operator|.
name|getStatus
argument_list|(
name|config
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentStatus
operator|!=
name|Status
operator|.
name|INCLUDE
condition|)
block|{
return|return
name|parentStatus
return|;
block|}
name|List
argument_list|<
name|FilterContainer
argument_list|>
name|containers
init|=
name|getParent
argument_list|()
operator|.
name|getContainers
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|IncludeColumn
argument_list|>
name|includeColumns
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ExcludeColumn
argument_list|>
name|excludeColumns
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|FilterContainer
name|container
range|:
name|containers
control|)
block|{
if|if
condition|(
name|container
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|IncludeTable
name|table
init|=
name|getParent
argument_list|()
operator|.
name|getIncludeTable
argument_list|(
name|container
operator|.
name|getIncludeTables
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|table
operator|!=
literal|null
condition|)
block|{
name|includeColumns
operator|.
name|addAll
argument_list|(
name|table
operator|.
name|getIncludeColumns
argument_list|()
argument_list|)
expr_stmt|;
name|excludeColumns
operator|.
name|addAll
argument_list|(
name|table
operator|.
name|getExcludeColumns
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|includeColumns
operator|.
name|addAll
argument_list|(
name|container
operator|.
name|getIncludeColumns
argument_list|()
argument_list|)
expr_stmt|;
name|excludeColumns
operator|.
name|addAll
argument_list|(
name|container
operator|.
name|getExcludeColumns
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|includesColumn
argument_list|(
name|includeColumns
argument_list|,
name|excludeColumns
argument_list|)
return|;
block|}
specifier|private
name|Status
name|includesColumn
parameter_list|(
name|Collection
argument_list|<
name|IncludeColumn
argument_list|>
name|includeColumns
parameter_list|,
name|Collection
argument_list|<
name|ExcludeColumn
argument_list|>
name|excludeColumns
parameter_list|)
block|{
if|if
condition|(
name|includeColumns
operator|.
name|isEmpty
argument_list|()
operator|&&
name|excludeColumns
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Status
operator|.
name|INCLUDE
return|;
block|}
if|if
condition|(
operator|!
name|includeColumns
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|getIncludeColumn
argument_list|(
name|includeColumns
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|Status
operator|.
name|INCLUDE
return|;
block|}
block|}
if|if
condition|(
operator|!
name|excludeColumns
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|getExcludeColumn
argument_list|(
name|excludeColumns
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|Status
operator|.
name|EXCLUDE_EXPLICIT
return|;
block|}
else|else
block|{
return|return
name|includeColumns
operator|.
name|isEmpty
argument_list|()
condition|?
name|Status
operator|.
name|INCLUDE
else|:
name|Status
operator|.
name|EXCLUDE_IMPLICIT
return|;
block|}
block|}
return|return
name|Status
operator|.
name|EXCLUDE_IMPLICIT
return|;
block|}
name|IncludeColumn
name|getIncludeColumn
parameter_list|(
name|Collection
argument_list|<
name|IncludeColumn
argument_list|>
name|includeColumns
parameter_list|)
block|{
for|for
control|(
name|IncludeColumn
name|column
range|:
name|includeColumns
control|)
block|{
if|if
condition|(
name|getName
argument_list|()
operator|.
name|matches
argument_list|(
name|column
operator|.
name|getPattern
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|column
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
name|ExcludeColumn
name|getExcludeColumn
parameter_list|(
name|Collection
argument_list|<
name|ExcludeColumn
argument_list|>
name|excludeColumns
parameter_list|)
block|{
for|for
control|(
name|ExcludeColumn
name|column
range|:
name|excludeColumns
control|)
block|{
if|if
condition|(
name|getName
argument_list|()
operator|.
name|matches
argument_list|(
name|column
operator|.
name|getPattern
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|column
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

