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
name|map
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|CayenneRuntimeException
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
name|exp
operator|.
name|Expression
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
name|query
operator|.
name|ObjectSelect
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
name|query
operator|.
name|Ordering
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
name|query
operator|.
name|PrefetchTreeNode
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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|SelectQueryDescriptor
extends|extends
name|QueryDescriptor
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|8798258795351950215L
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DISTINCT_PROPERTY
init|=
literal|"cayenne.SelectQuery.distinct"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|boolean
name|DISTINCT_DEFAULT
init|=
literal|false
decl_stmt|;
specifier|protected
name|Expression
name|qualifier
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Ordering
argument_list|>
name|orderings
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|prefetchesMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|SelectQueryDescriptor
parameter_list|()
block|{
name|super
argument_list|(
name|SELECT_QUERY
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDistinct
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|setProperty
argument_list|(
name|DISTINCT_PROPERTY
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDistinct
parameter_list|()
block|{
name|String
name|distinct
init|=
name|getProperty
argument_list|(
name|DISTINCT_PROPERTY
argument_list|)
decl_stmt|;
return|return
name|distinct
operator|!=
literal|null
condition|?
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|distinct
argument_list|)
else|:
name|DISTINCT_DEFAULT
return|;
block|}
comment|/**      * Returns qualifier of this query.      */
specifier|public
name|Expression
name|getQualifier
parameter_list|()
block|{
return|return
name|qualifier
return|;
block|}
comment|/**      * Sets qualifier for this query.      */
specifier|public
name|void
name|setQualifier
parameter_list|(
name|Expression
name|qualifier
parameter_list|)
block|{
name|this
operator|.
name|qualifier
operator|=
name|qualifier
expr_stmt|;
block|}
comment|/**      * Returns list of orderings for this query.      */
specifier|public
name|List
argument_list|<
name|Ordering
argument_list|>
name|getOrderings
parameter_list|()
block|{
return|return
name|orderings
return|;
block|}
comment|/**      * Sets list of orderings for this query.      */
specifier|public
name|void
name|setOrderings
parameter_list|(
name|List
argument_list|<
name|Ordering
argument_list|>
name|orderings
parameter_list|)
block|{
name|this
operator|.
name|orderings
operator|=
name|orderings
expr_stmt|;
block|}
comment|/**      * Adds single ordering for this query.      */
specifier|public
name|void
name|addOrdering
parameter_list|(
name|Ordering
name|ordering
parameter_list|)
block|{
name|this
operator|.
name|orderings
operator|.
name|add
argument_list|(
name|ordering
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes single ordering from this query.      */
specifier|public
name|void
name|removeOrdering
parameter_list|(
name|Ordering
name|ordering
parameter_list|)
block|{
name|this
operator|.
name|orderings
operator|.
name|remove
argument_list|(
name|ordering
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns map of prefetch paths with semantics for this query.      *      * @since 4.1      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|getPrefetchesMap
parameter_list|()
block|{
return|return
name|prefetchesMap
return|;
block|}
comment|/**      * Returns list of prefetch paths for this query.      *      * @deprecated since 4.1 use {@link #getPrefetchesMap()}.      */
annotation|@
name|Deprecated
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getPrefetches
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|prefetchesMap
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Sets map of prefetch paths with semantics for this query.      *      * @since 4.1      */
specifier|public
name|void
name|setPrefetchesMap
parameter_list|(
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|prefetchesMap
parameter_list|)
block|{
name|this
operator|.
name|prefetchesMap
operator|=
name|prefetchesMap
expr_stmt|;
block|}
comment|/**      * Sets list of prefetch paths for this query.      *      * @deprecated since 4.1 use {@link #setPrefetchesMap(HashMap)}.      */
annotation|@
name|Deprecated
specifier|public
name|void
name|setPrefetches
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|prefetches
parameter_list|)
block|{
for|for
control|(
name|String
name|prefetch
range|:
name|prefetches
control|)
block|{
name|this
operator|.
name|prefetchesMap
operator|.
name|put
argument_list|(
name|prefetch
argument_list|,
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Adds prefetch path with semantics to this query.      *      * @since 4.1      */
specifier|public
name|void
name|addPrefetch
parameter_list|(
name|String
name|prefetchPath
parameter_list|,
name|int
name|semantics
parameter_list|)
block|{
name|this
operator|.
name|prefetchesMap
operator|.
name|put
argument_list|(
name|prefetchPath
argument_list|,
name|semantics
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds single prefetch path to this query.      *      * @deprecated since 4.1 use {@link #addPrefetch(String, int)}      */
annotation|@
name|Deprecated
specifier|public
name|void
name|addPrefetch
parameter_list|(
name|String
name|prefetchPath
parameter_list|)
block|{
name|this
operator|.
name|prefetchesMap
operator|.
name|put
argument_list|(
name|prefetchPath
argument_list|,
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes single prefetch path from this query.      */
specifier|public
name|void
name|removePrefetch
parameter_list|(
name|String
name|prefetchPath
parameter_list|)
block|{
name|this
operator|.
name|prefetchesMap
operator|.
name|remove
argument_list|(
name|prefetchPath
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ObjectSelect
argument_list|<
name|?
argument_list|>
name|buildQuery
parameter_list|()
block|{
comment|// resolve root
name|Object
name|root
init|=
name|getRoot
argument_list|()
decl_stmt|;
name|String
name|rootEntityName
decl_stmt|;
if|if
condition|(
name|root
operator|instanceof
name|ObjEntity
condition|)
block|{
name|rootEntityName
operator|=
operator|(
operator|(
name|ObjEntity
operator|)
name|root
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|root
operator|instanceof
name|String
condition|)
block|{
name|rootEntityName
operator|=
operator|(
name|String
operator|)
name|root
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unexpected root for the SelectQueryDescriptor '%s'."
argument_list|,
name|root
argument_list|)
throw|;
block|}
name|ObjectSelect
argument_list|<
name|?
argument_list|>
name|query
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Object
operator|.
name|class
argument_list|,
name|getQualifier
argument_list|()
argument_list|)
decl_stmt|;
name|query
operator|.
name|entityName
argument_list|(
name|rootEntityName
argument_list|)
expr_stmt|;
name|query
operator|.
name|setRoot
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Ordering
argument_list|>
name|orderings
init|=
name|this
operator|.
name|getOrderings
argument_list|()
decl_stmt|;
if|if
condition|(
name|orderings
operator|!=
literal|null
operator|&&
operator|!
name|orderings
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|query
operator|.
name|orderBy
argument_list|(
name|orderings
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|prefetchesMap
operator|!=
literal|null
condition|)
block|{
name|prefetchesMap
operator|.
name|forEach
argument_list|(
name|query
operator|::
name|prefetch
argument_list|)
expr_stmt|;
block|}
comment|// TODO: apply DISTINCT property
name|query
operator|.
name|initWithProperties
argument_list|(
name|this
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|query
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
literal|"query"
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"name"
argument_list|,
name|getName
argument_list|()
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|String
name|rootString
init|=
literal|null
decl_stmt|;
name|String
name|rootType
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|root
operator|instanceof
name|String
condition|)
block|{
name|rootType
operator|=
name|QueryDescriptor
operator|.
name|OBJ_ENTITY_ROOT
expr_stmt|;
name|rootString
operator|=
name|root
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|root
operator|instanceof
name|ObjEntity
condition|)
block|{
name|rootType
operator|=
name|QueryDescriptor
operator|.
name|OBJ_ENTITY_ROOT
expr_stmt|;
name|rootString
operator|=
operator|(
operator|(
name|ObjEntity
operator|)
name|root
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|root
operator|instanceof
name|DbEntity
condition|)
block|{
name|rootType
operator|=
name|QueryDescriptor
operator|.
name|DB_ENTITY_ROOT
expr_stmt|;
name|rootString
operator|=
operator|(
operator|(
name|DbEntity
operator|)
name|root
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|root
operator|instanceof
name|Procedure
condition|)
block|{
name|rootType
operator|=
name|QueryDescriptor
operator|.
name|PROCEDURE_ROOT
expr_stmt|;
name|rootString
operator|=
operator|(
operator|(
name|Procedure
operator|)
name|root
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|root
operator|instanceof
name|Class
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|rootType
operator|=
name|QueryDescriptor
operator|.
name|JAVA_CLASS_ROOT
expr_stmt|;
name|rootString
operator|=
operator|(
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|root
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|rootType
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|attribute
argument_list|(
literal|"root"
argument_list|,
name|rootType
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"root-name"
argument_list|,
name|rootString
argument_list|)
expr_stmt|;
block|}
comment|// print properties
name|encodeProperties
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
comment|// encode qualifier
if|if
condition|(
name|qualifier
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|start
argument_list|(
literal|"qualifier"
argument_list|)
operator|.
name|nested
argument_list|(
name|qualifier
argument_list|,
name|delegate
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
comment|// encode orderings
name|encoder
operator|.
name|nested
argument_list|(
name|orderings
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
name|PrefetchTreeNode
name|prefetchTree
init|=
operator|new
name|PrefetchTreeNode
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|prefetchPath
range|:
name|prefetchesMap
operator|.
name|keySet
argument_list|()
control|)
block|{
name|PrefetchTreeNode
name|node
init|=
name|prefetchTree
operator|.
name|addPath
argument_list|(
name|prefetchPath
argument_list|)
decl_stmt|;
name|node
operator|.
name|setSemantics
argument_list|(
name|prefetchesMap
operator|.
name|get
argument_list|(
name|prefetchPath
argument_list|)
argument_list|)
expr_stmt|;
name|node
operator|.
name|setPhantom
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|nested
argument_list|(
name|prefetchTree
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|visitQuery
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

