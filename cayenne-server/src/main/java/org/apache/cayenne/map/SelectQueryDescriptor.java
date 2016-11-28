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
name|map
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
name|query
operator|.
name|SelectQuery
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|List
argument_list|<
name|String
argument_list|>
name|prefetches
init|=
operator|new
name|ArrayList
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
name|SelectQuery
operator|.
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
name|SelectQuery
operator|.
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
name|valueOf
argument_list|(
name|distinct
argument_list|)
else|:
literal|false
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
comment|/**      * Returns list of prefetch paths for this query.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getPrefetches
parameter_list|()
block|{
return|return
name|prefetches
return|;
block|}
comment|/**      * Sets list of prefetch paths for this query.      */
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
name|this
operator|.
name|prefetches
operator|=
name|prefetches
expr_stmt|;
block|}
comment|/**      * Adds single prefetch path to this query.      */
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
name|prefetches
operator|.
name|add
argument_list|(
name|prefetchPath
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
name|prefetches
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
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|buildQuery
parameter_list|()
block|{
name|SelectQuery
argument_list|<
name|Object
argument_list|>
name|selectQuery
init|=
operator|new
name|SelectQuery
argument_list|<>
argument_list|()
decl_stmt|;
name|selectQuery
operator|.
name|setRoot
argument_list|(
name|this
operator|.
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
name|selectQuery
operator|.
name|setName
argument_list|(
name|this
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|selectQuery
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|selectQuery
operator|.
name|setQualifier
argument_list|(
name|this
operator|.
name|getQualifier
argument_list|()
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
name|selectQuery
operator|.
name|addOrderings
argument_list|(
name|orderings
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|prefetches
init|=
name|this
operator|.
name|getPrefetches
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefetches
operator|!=
literal|null
operator|&&
operator|!
name|prefetches
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|String
name|prefetch
range|:
name|prefetches
control|)
block|{
name|selectQuery
operator|.
name|addPrefetch
argument_list|(
name|prefetch
argument_list|)
expr_stmt|;
block|}
block|}
comment|// init properties
name|selectQuery
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
name|selectQuery
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
parameter_list|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<query name=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|"\" type=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
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
name|MapLoader
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
name|MapLoader
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
name|MapLoader
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
name|MapLoader
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
name|MapLoader
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
name|print
argument_list|(
literal|"\" root=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|rootType
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|"\" root-name=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|rootString
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|println
argument_list|(
literal|"\">"
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|indent
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
name|print
argument_list|(
literal|"<qualifier>"
argument_list|)
expr_stmt|;
name|qualifier
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"</qualifier>"
argument_list|)
expr_stmt|;
block|}
comment|// encode orderings
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
for|for
control|(
name|Ordering
name|ordering
range|:
name|orderings
control|)
block|{
name|ordering
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
block|}
block|}
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
name|prefetches
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
name|PrefetchTreeNode
operator|.
name|UNDEFINED_SEMANTICS
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
name|prefetchTree
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|indent
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"</query>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

