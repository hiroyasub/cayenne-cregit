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
name|model
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

begin_import
import|import
name|groovy
operator|.
name|lang
operator|.
name|Closure
import|;
end_import

begin_import
import|import
name|org
operator|.
name|gradle
operator|.
name|util
operator|.
name|ConfigureUtil
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|FilterContainer
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|IncludeTable
argument_list|>
name|includeTables
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|PatternParam
argument_list|>
name|excludeTables
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|PatternParam
argument_list|>
name|includeColumns
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|PatternParam
argument_list|>
name|excludeColumns
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|PatternParam
argument_list|>
name|includeProcedures
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|PatternParam
argument_list|>
name|excludeProcedures
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|FilterContainer
parameter_list|()
block|{
block|}
name|FilterContainer
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|void
name|name
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|void
name|includeTable
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|includeTables
operator|.
name|add
argument_list|(
operator|new
name|IncludeTable
argument_list|(
name|pattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|includeTable
parameter_list|(
name|Closure
argument_list|<
name|?
argument_list|>
name|closure
parameter_list|)
block|{
name|includeTables
operator|.
name|add
argument_list|(
name|ConfigureUtil
operator|.
name|configure
argument_list|(
name|closure
argument_list|,
operator|new
name|IncludeTable
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|includeTable
parameter_list|(
name|String
name|pattern
parameter_list|,
name|Closure
argument_list|<
name|?
argument_list|>
name|closure
parameter_list|)
block|{
name|includeTables
operator|.
name|add
argument_list|(
name|ConfigureUtil
operator|.
name|configure
argument_list|(
name|closure
argument_list|,
operator|new
name|IncludeTable
argument_list|(
name|pattern
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|includeTables
parameter_list|(
name|String
modifier|...
name|patterns
parameter_list|)
block|{
for|for
control|(
name|String
name|pattern
range|:
name|patterns
control|)
block|{
name|includeTable
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|excludeTable
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|addToCollection
argument_list|(
name|excludeTables
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|excludeTables
parameter_list|(
name|String
modifier|...
name|patterns
parameter_list|)
block|{
for|for
control|(
name|String
name|pattern
range|:
name|patterns
control|)
block|{
name|excludeTable
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|includeColumn
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|addToCollection
argument_list|(
name|includeColumns
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|includeColumns
parameter_list|(
name|String
modifier|...
name|patterns
parameter_list|)
block|{
for|for
control|(
name|String
name|pattern
range|:
name|patterns
control|)
block|{
name|includeColumn
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|excludeColumn
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|addToCollection
argument_list|(
name|excludeColumns
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|excludeColumns
parameter_list|(
name|String
modifier|...
name|patterns
parameter_list|)
block|{
for|for
control|(
name|String
name|pattern
range|:
name|patterns
control|)
block|{
name|excludeColumn
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|includeProcedure
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|addToCollection
argument_list|(
name|includeProcedures
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|includeProcedures
parameter_list|(
name|String
modifier|...
name|patterns
parameter_list|)
block|{
for|for
control|(
name|String
name|pattern
range|:
name|patterns
control|)
block|{
name|includeProcedure
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|excludeProcedure
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|addToCollection
argument_list|(
name|excludeProcedures
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|excludeProcedures
parameter_list|(
name|String
modifier|...
name|patterns
parameter_list|)
block|{
for|for
control|(
name|String
name|pattern
range|:
name|patterns
control|)
block|{
name|excludeProcedure
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|addToCollection
parameter_list|(
name|Collection
argument_list|<
name|PatternParam
argument_list|>
name|collection
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|collection
operator|.
name|add
argument_list|(
operator|new
name|PatternParam
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
parameter_list|<
name|C
extends|extends
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
parameter_list|>
name|C
name|fillContainer
parameter_list|(
specifier|final
name|C
name|container
parameter_list|)
block|{
name|container
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
for|for
control|(
name|IncludeTable
name|table
range|:
name|includeTables
control|)
block|{
name|container
operator|.
name|addIncludeTable
argument_list|(
name|table
operator|.
name|toIncludeTable
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|PatternParam
name|table
range|:
name|excludeTables
control|)
block|{
name|container
operator|.
name|addExcludeTable
argument_list|(
name|table
operator|.
name|toExcludeTable
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|PatternParam
name|column
range|:
name|includeColumns
control|)
block|{
name|container
operator|.
name|addIncludeColumn
argument_list|(
name|column
operator|.
name|toIncludeColumn
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|PatternParam
name|column
range|:
name|excludeColumns
control|)
block|{
name|container
operator|.
name|addExcludeColumn
argument_list|(
name|column
operator|.
name|toExcludeColumn
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|PatternParam
name|procedure
range|:
name|includeProcedures
control|)
block|{
name|container
operator|.
name|addIncludeProcedure
argument_list|(
name|procedure
operator|.
name|toIncludeProcedure
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|PatternParam
name|procedure
range|:
name|excludeProcedures
control|)
block|{
name|container
operator|.
name|addExcludeProcedure
argument_list|(
name|procedure
operator|.
name|toExcludeProcedure
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|container
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
block|}
end_class

end_unit

