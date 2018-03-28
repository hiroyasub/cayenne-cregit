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
name|SQLTemplate
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|SQLTemplateDescriptor
extends|extends
name|QueryDescriptor
block|{
specifier|protected
name|String
name|sql
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
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|adapterSql
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|SQLTemplateDescriptor
parameter_list|()
block|{
name|super
argument_list|(
name|SQL_TEMPLATE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns default SQL statement for this query.      */
specifier|public
name|String
name|getSql
parameter_list|()
block|{
return|return
name|sql
return|;
block|}
comment|/**      * Sets default SQL statement for this query.      */
specifier|public
name|void
name|setSql
parameter_list|(
name|String
name|sql
parameter_list|)
block|{
name|this
operator|.
name|sql
operator|=
name|sql
expr_stmt|;
block|}
comment|/**      * Returns map of db adapter specific SQL statements.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getAdapterSql
parameter_list|()
block|{
return|return
name|adapterSql
return|;
block|}
comment|/**      * Sets a map db adapter specific SQL statements for this query.      */
specifier|public
name|void
name|setAdapterSql
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|adapterSql
parameter_list|)
block|{
name|this
operator|.
name|adapterSql
operator|=
name|adapterSql
expr_stmt|;
block|}
comment|/**      * Returns map of prefetch paths with semantics for this query.      * @since 4.1      */
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
comment|/**      * Sets map of prefetch paths with semantics for this query.      * @since 4.1      */
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
comment|/**      * Adds single prefetch path with semantics to this query.      * @since 4.1      */
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
comment|/**      * Removes single prefetch path from this query.      * @since 4.1      */
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
name|SQLTemplate
name|buildQuery
parameter_list|()
block|{
name|SQLTemplate
name|template
init|=
operator|new
name|SQLTemplate
argument_list|()
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|setRoot
argument_list|(
name|root
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
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|prefetchesMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|template
operator|.
name|addPrefetch
argument_list|(
name|PrefetchTreeNode
operator|.
name|withPath
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|template
operator|.
name|initWithProperties
argument_list|(
name|this
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
comment|// init SQL
name|template
operator|.
name|setDefaultTemplate
argument_list|(
name|this
operator|.
name|getSql
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|adapterSql
init|=
name|this
operator|.
name|getAdapterSql
argument_list|()
decl_stmt|;
if|if
condition|(
name|adapterSql
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|adapterSql
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
name|value
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|setTemplate
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|template
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
if|else if
condition|(
name|root
operator|instanceof
name|DataMap
condition|)
block|{
name|rootType
operator|=
name|QueryDescriptor
operator|.
name|DATA_MAP_ROOT
expr_stmt|;
name|rootString
operator|=
operator|(
operator|(
name|DataMap
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
comment|// encode default SQL
if|if
condition|(
name|sql
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|start
argument_list|(
literal|"sql"
argument_list|)
operator|.
name|cdata
argument_list|(
name|sql
argument_list|,
literal|true
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
comment|// encode adapter SQL
if|if
condition|(
name|adapterSql
operator|!=
literal|null
operator|&&
operator|!
name|adapterSql
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// sorting entries by adapter name
name|TreeSet
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|adapterSql
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|keys
control|)
block|{
name|String
name|value
init|=
name|adapterSql
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
name|value
operator|!=
literal|null
condition|)
block|{
name|String
name|sql
init|=
name|value
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|sql
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|encoder
operator|.
name|start
argument_list|(
literal|"sql"
argument_list|)
operator|.
name|attribute
argument_list|(
literal|"adapter-class"
argument_list|,
name|key
argument_list|)
operator|.
name|cdata
argument_list|(
name|sql
argument_list|,
literal|true
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
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
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|prefetchesMap
operator|.
name|entrySet
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
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|node
operator|.
name|setSemantics
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
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

