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
name|sqlbuilder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|DistinctNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|FromNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|GroupByNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|HavingNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|LimitOffsetNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|Node
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|OrderByNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|SelectNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|SelectResultNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|TopNode
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|WhereNode
import|;
end_import

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|SelectBuilder
implements|implements
name|NodeBuilder
block|{
specifier|private
specifier|static
specifier|final
name|int
name|SELECT_NODE
init|=
literal|0
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FROM_NODE
init|=
literal|1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|WHERE_NODE
init|=
literal|2
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|GROUPBY_NODE
init|=
literal|3
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|HAVING_NODE
init|=
literal|4
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|UNION_NODE
init|=
literal|5
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|ORDERBY_NODE
init|=
literal|6
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|LIMIT_NODE
init|=
literal|7
decl_stmt|;
comment|/**      * Main root of this query      */
specifier|private
name|Node
name|root
decl_stmt|;
comment|/*      * Following nodes are all children of root,      * but we keep them here for quick access.      */
specifier|private
name|Node
index|[]
name|nodes
init|=
operator|new
name|Node
index|[
name|LIMIT_NODE
operator|+
literal|1
index|]
decl_stmt|;
name|SelectBuilder
parameter_list|(
name|NodeBuilder
modifier|...
name|selectExpressions
parameter_list|)
block|{
name|root
operator|=
operator|new
name|SelectNode
argument_list|()
expr_stmt|;
for|for
control|(
name|NodeBuilder
name|exp
range|:
name|selectExpressions
control|)
block|{
name|node
argument_list|(
name|SELECT_NODE
argument_list|,
name|SelectResultNode
operator|::
operator|new
argument_list|)
operator|.
name|addChild
argument_list|(
name|exp
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|SelectBuilder
name|distinct
parameter_list|()
block|{
name|root
operator|.
name|addChild
argument_list|(
operator|new
name|DistinctNode
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SelectBuilder
name|top
parameter_list|(
name|int
name|count
parameter_list|)
block|{
name|root
operator|.
name|addChild
argument_list|(
operator|new
name|TopNode
argument_list|(
name|count
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SelectBuilder
name|result
parameter_list|(
name|NodeBuilder
name|selectExpression
parameter_list|)
block|{
name|node
argument_list|(
name|SELECT_NODE
argument_list|,
name|SelectResultNode
operator|::
operator|new
argument_list|)
operator|.
name|addChild
argument_list|(
name|selectExpression
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SelectBuilder
name|from
parameter_list|(
name|NodeBuilder
name|table
parameter_list|)
block|{
name|node
argument_list|(
name|FROM_NODE
argument_list|,
name|FromNode
operator|::
operator|new
argument_list|)
operator|.
name|addChild
argument_list|(
name|table
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SelectBuilder
name|from
parameter_list|(
name|NodeBuilder
modifier|...
name|tables
parameter_list|)
block|{
for|for
control|(
name|NodeBuilder
name|next
range|:
name|tables
control|)
block|{
name|node
argument_list|(
name|FROM_NODE
argument_list|,
name|FromNode
operator|::
operator|new
argument_list|)
operator|.
name|addChild
argument_list|(
name|next
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|SelectBuilder
name|where
parameter_list|(
name|NodeBuilder
modifier|...
name|params
parameter_list|)
block|{
for|for
control|(
name|NodeBuilder
name|next
range|:
name|params
control|)
block|{
name|node
argument_list|(
name|WHERE_NODE
argument_list|,
name|WhereNode
operator|::
operator|new
argument_list|)
operator|.
name|addChild
argument_list|(
name|next
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|SelectBuilder
name|where
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
name|node
argument_list|(
name|WHERE_NODE
argument_list|,
name|WhereNode
operator|::
operator|new
argument_list|)
operator|.
name|addChild
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SelectBuilder
name|orderBy
parameter_list|(
name|NodeBuilder
modifier|...
name|params
parameter_list|)
block|{
for|for
control|(
name|NodeBuilder
name|next
range|:
name|params
control|)
block|{
name|node
argument_list|(
name|ORDERBY_NODE
argument_list|,
name|OrderByNode
operator|::
operator|new
argument_list|)
operator|.
name|addChild
argument_list|(
name|next
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|SelectBuilder
name|orderBy
parameter_list|(
name|NodeBuilder
name|param
parameter_list|)
block|{
name|node
argument_list|(
name|ORDERBY_NODE
argument_list|,
name|OrderByNode
operator|::
operator|new
argument_list|)
operator|.
name|addChild
argument_list|(
name|param
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SelectBuilder
name|groupBy
parameter_list|(
name|NodeBuilder
modifier|...
name|params
parameter_list|)
block|{
for|for
control|(
name|NodeBuilder
name|next
range|:
name|params
control|)
block|{
name|node
argument_list|(
name|GROUPBY_NODE
argument_list|,
name|GroupByNode
operator|::
operator|new
argument_list|)
operator|.
name|addChild
argument_list|(
name|next
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|SelectBuilder
name|groupBy
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
name|node
argument_list|(
name|GROUPBY_NODE
argument_list|,
name|GroupByNode
operator|::
operator|new
argument_list|)
operator|.
name|addChild
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SelectBuilder
name|having
parameter_list|(
name|NodeBuilder
modifier|...
name|params
parameter_list|)
block|{
for|for
control|(
name|NodeBuilder
name|next
range|:
name|params
control|)
block|{
name|node
argument_list|(
name|HAVING_NODE
argument_list|,
name|HavingNode
operator|::
operator|new
argument_list|)
operator|.
name|addChild
argument_list|(
name|next
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|SelectBuilder
name|having
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
name|node
argument_list|(
name|HAVING_NODE
argument_list|,
name|HavingNode
operator|::
operator|new
argument_list|)
operator|.
name|addChild
argument_list|(
name|node
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|SelectBuilder
name|limitOffset
parameter_list|(
name|int
name|limit
parameter_list|,
name|int
name|offset
parameter_list|)
block|{
name|nodes
index|[
name|LIMIT_NODE
index|]
operator|=
operator|new
name|LimitOffsetNode
argument_list|(
name|limit
argument_list|,
name|offset
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
specifier|public
name|Node
name|build
parameter_list|()
block|{
for|for
control|(
name|Node
name|next
range|:
name|nodes
control|)
block|{
if|if
condition|(
name|next
operator|!=
literal|null
condition|)
block|{
name|root
operator|.
name|addChild
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|root
return|;
block|}
specifier|public
name|Node
name|getRoot
parameter_list|()
block|{
return|return
name|root
return|;
block|}
specifier|private
name|Node
name|node
parameter_list|(
name|int
name|idx
parameter_list|,
name|Supplier
argument_list|<
name|Node
argument_list|>
name|nodeSupplier
parameter_list|)
block|{
if|if
condition|(
name|nodes
index|[
name|idx
index|]
operator|==
literal|null
condition|)
block|{
name|nodes
index|[
name|idx
index|]
operator|=
name|nodeSupplier
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
return|return
name|nodes
index|[
name|idx
index|]
return|;
block|}
block|}
end_class

end_unit

