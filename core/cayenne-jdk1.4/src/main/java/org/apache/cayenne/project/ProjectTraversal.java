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
name|project
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|DataDomain
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
name|DataNode
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
name|Attribute
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
name|Entity
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
name|Procedure
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
name|Relationship
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
name|Query
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
name|CayenneMapEntry
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
name|Util
import|;
end_import

begin_comment
comment|/**  * ProjectTraversal allows to traverse Cayenne project tree in a "depth-first" order  * starting from an arbitrary level to its children.  *<p>  *<i>Current implementation is not very efficient and would actually first read the whole  * tree, before returning the first element from the iterator.</i>  *</p>  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ProjectTraversal
block|{
specifier|protected
specifier|static
specifier|final
name|Comparator
name|mapObjectComparator
init|=
operator|new
name|MapObjectComparator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|Comparator
name|dataMapComparator
init|=
operator|new
name|DataMapComparator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|Comparator
name|dataDomainComparator
init|=
operator|new
name|DataDomainComparator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|Comparator
name|dataNodeComparator
init|=
operator|new
name|DataNodeComparator
argument_list|()
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|Comparator
name|queryComparator
init|=
operator|new
name|QueryComparator
argument_list|()
decl_stmt|;
specifier|protected
name|ProjectTraversalHandler
name|handler
decl_stmt|;
specifier|protected
name|boolean
name|sort
decl_stmt|;
specifier|public
name|ProjectTraversal
parameter_list|(
name|ProjectTraversalHandler
name|handler
parameter_list|)
block|{
name|this
argument_list|(
name|handler
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates ProjectTraversal instance with a given handler and sort policy. If      *<code>sort</code> is true, children of each node will be sorted using a      * predefined Comparator for a given type of child nodes.      */
specifier|public
name|ProjectTraversal
parameter_list|(
name|ProjectTraversalHandler
name|handler
parameter_list|,
name|boolean
name|sort
parameter_list|)
block|{
name|this
operator|.
name|handler
operator|=
name|handler
expr_stmt|;
name|this
operator|.
name|sort
operator|=
name|sort
expr_stmt|;
block|}
comment|/**      * Performs traversal starting from the root node. Root node can be of any type      * supported in Cayenne projects (Configuration, DataMap, DataNode, etc...)      */
specifier|public
name|void
name|traverse
parameter_list|(
name|Object
name|rootNode
parameter_list|)
block|{
name|this
operator|.
name|traverse
argument_list|(
name|rootNode
argument_list|,
operator|new
name|ProjectPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|traverse
parameter_list|(
name|Object
name|rootNode
parameter_list|,
name|ProjectPath
name|path
parameter_list|)
block|{
if|if
condition|(
name|rootNode
operator|instanceof
name|Project
condition|)
block|{
name|this
operator|.
name|traverseProject
argument_list|(
operator|(
name|Project
operator|)
name|rootNode
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|rootNode
operator|instanceof
name|DataDomain
condition|)
block|{
name|this
operator|.
name|traverseDomains
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|rootNode
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|rootNode
operator|instanceof
name|DataMap
condition|)
block|{
name|this
operator|.
name|traverseMaps
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|rootNode
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|rootNode
operator|instanceof
name|Entity
condition|)
block|{
name|this
operator|.
name|traverseEntities
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|rootNode
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|rootNode
operator|instanceof
name|Attribute
condition|)
block|{
name|this
operator|.
name|traverseAttributes
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|rootNode
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|rootNode
operator|instanceof
name|Relationship
condition|)
block|{
name|this
operator|.
name|traverseRelationships
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|rootNode
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|rootNode
operator|instanceof
name|DataNode
condition|)
block|{
name|this
operator|.
name|traverseNodes
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|rootNode
argument_list|)
operator|.
name|iterator
argument_list|()
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|nodeClass
init|=
operator|(
name|rootNode
operator|!=
literal|null
operator|)
condition|?
name|rootNode
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|"(null)"
decl_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported root node: "
operator|+
name|nodeClass
argument_list|)
throw|;
block|}
block|}
comment|/**      * Performs traversal starting from the Project and down to its children.      */
specifier|public
name|void
name|traverseProject
parameter_list|(
name|Project
name|project
parameter_list|,
name|ProjectPath
name|path
parameter_list|)
block|{
name|ProjectPath
name|projectPath
init|=
name|path
operator|.
name|appendToPath
argument_list|(
name|project
argument_list|)
decl_stmt|;
name|handler
operator|.
name|projectNode
argument_list|(
name|projectPath
argument_list|)
expr_stmt|;
if|if
condition|(
name|handler
operator|.
name|shouldReadChildren
argument_list|(
name|project
argument_list|,
name|path
argument_list|)
condition|)
block|{
name|Iterator
name|it
init|=
name|project
operator|.
name|getChildren
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|this
operator|.
name|traverse
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|,
name|projectPath
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Performs traversal starting from a list of domains.      */
specifier|public
name|void
name|traverseDomains
parameter_list|(
name|Iterator
name|domains
parameter_list|,
name|ProjectPath
name|path
parameter_list|)
block|{
if|if
condition|(
name|sort
condition|)
block|{
name|domains
operator|=
name|Util
operator|.
name|sortedIterator
argument_list|(
name|domains
argument_list|,
name|ProjectTraversal
operator|.
name|dataDomainComparator
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|domains
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DataDomain
name|domain
init|=
operator|(
name|DataDomain
operator|)
name|domains
operator|.
name|next
argument_list|()
decl_stmt|;
name|ProjectPath
name|domainPath
init|=
name|path
operator|.
name|appendToPath
argument_list|(
name|domain
argument_list|)
decl_stmt|;
name|handler
operator|.
name|projectNode
argument_list|(
name|domainPath
argument_list|)
expr_stmt|;
if|if
condition|(
name|handler
operator|.
name|shouldReadChildren
argument_list|(
name|domain
argument_list|,
name|path
argument_list|)
condition|)
block|{
name|this
operator|.
name|traverseMaps
argument_list|(
name|domain
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|,
name|domainPath
argument_list|)
expr_stmt|;
name|this
operator|.
name|traverseNodes
argument_list|(
name|domain
operator|.
name|getDataNodes
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|,
name|domainPath
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|traverseNodes
parameter_list|(
name|Iterator
name|nodes
parameter_list|,
name|ProjectPath
name|path
parameter_list|)
block|{
if|if
condition|(
name|sort
condition|)
block|{
name|nodes
operator|=
name|Util
operator|.
name|sortedIterator
argument_list|(
name|nodes
argument_list|,
name|ProjectTraversal
operator|.
name|dataNodeComparator
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|nodes
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DataNode
name|node
init|=
operator|(
name|DataNode
operator|)
name|nodes
operator|.
name|next
argument_list|()
decl_stmt|;
name|ProjectPath
name|nodePath
init|=
name|path
operator|.
name|appendToPath
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|handler
operator|.
name|projectNode
argument_list|(
name|nodePath
argument_list|)
expr_stmt|;
if|if
condition|(
name|handler
operator|.
name|shouldReadChildren
argument_list|(
name|node
argument_list|,
name|path
argument_list|)
condition|)
block|{
name|this
operator|.
name|traverseMaps
argument_list|(
name|node
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|,
name|nodePath
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|traverseMaps
parameter_list|(
name|Iterator
name|maps
parameter_list|,
name|ProjectPath
name|path
parameter_list|)
block|{
if|if
condition|(
name|sort
condition|)
block|{
name|maps
operator|=
name|Util
operator|.
name|sortedIterator
argument_list|(
name|maps
argument_list|,
name|ProjectTraversal
operator|.
name|dataMapComparator
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|maps
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|maps
operator|.
name|next
argument_list|()
decl_stmt|;
name|ProjectPath
name|mapPath
init|=
name|path
operator|.
name|appendToPath
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|handler
operator|.
name|projectNode
argument_list|(
name|mapPath
argument_list|)
expr_stmt|;
if|if
condition|(
name|handler
operator|.
name|shouldReadChildren
argument_list|(
name|map
argument_list|,
name|path
argument_list|)
condition|)
block|{
name|this
operator|.
name|traverseEntities
argument_list|(
name|map
operator|.
name|getObjEntities
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|,
name|mapPath
argument_list|)
expr_stmt|;
name|this
operator|.
name|traverseEntities
argument_list|(
name|map
operator|.
name|getDbEntities
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|,
name|mapPath
argument_list|)
expr_stmt|;
name|this
operator|.
name|traverseProcedures
argument_list|(
name|map
operator|.
name|getProcedures
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|,
name|mapPath
argument_list|)
expr_stmt|;
name|this
operator|.
name|traverseQueries
argument_list|(
name|map
operator|.
name|getQueries
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|,
name|mapPath
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Performs recursive traversal of an Iterator of Cayenne Query objects.      */
specifier|public
name|void
name|traverseQueries
parameter_list|(
name|Iterator
name|queries
parameter_list|,
name|ProjectPath
name|path
parameter_list|)
block|{
if|if
condition|(
name|sort
condition|)
block|{
name|queries
operator|=
name|Util
operator|.
name|sortedIterator
argument_list|(
name|queries
argument_list|,
name|ProjectTraversal
operator|.
name|queryComparator
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|queries
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Query
name|query
init|=
operator|(
name|Query
operator|)
name|queries
operator|.
name|next
argument_list|()
decl_stmt|;
name|ProjectPath
name|queryPath
init|=
name|path
operator|.
name|appendToPath
argument_list|(
name|query
argument_list|)
decl_stmt|;
name|handler
operator|.
name|projectNode
argument_list|(
name|queryPath
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Performs recusrive traversal of an Iterator of Cayenne Procedure objects.      */
specifier|public
name|void
name|traverseProcedures
parameter_list|(
name|Iterator
name|procedures
parameter_list|,
name|ProjectPath
name|path
parameter_list|)
block|{
if|if
condition|(
name|sort
condition|)
block|{
name|procedures
operator|=
name|Util
operator|.
name|sortedIterator
argument_list|(
name|procedures
argument_list|,
name|ProjectTraversal
operator|.
name|mapObjectComparator
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|procedures
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Procedure
name|procedure
init|=
operator|(
name|Procedure
operator|)
name|procedures
operator|.
name|next
argument_list|()
decl_stmt|;
name|ProjectPath
name|procedurePath
init|=
name|path
operator|.
name|appendToPath
argument_list|(
name|procedure
argument_list|)
decl_stmt|;
name|handler
operator|.
name|projectNode
argument_list|(
name|procedurePath
argument_list|)
expr_stmt|;
if|if
condition|(
name|handler
operator|.
name|shouldReadChildren
argument_list|(
name|procedure
argument_list|,
name|path
argument_list|)
condition|)
block|{
name|this
operator|.
name|traverseProcedureParameters
argument_list|(
name|procedure
operator|.
name|getCallParameters
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|,
name|procedurePath
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|traverseEntities
parameter_list|(
name|Iterator
name|entities
parameter_list|,
name|ProjectPath
name|path
parameter_list|)
block|{
if|if
condition|(
name|sort
condition|)
block|{
name|entities
operator|=
name|Util
operator|.
name|sortedIterator
argument_list|(
name|entities
argument_list|,
name|ProjectTraversal
operator|.
name|mapObjectComparator
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|entities
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Entity
name|ent
init|=
operator|(
name|Entity
operator|)
name|entities
operator|.
name|next
argument_list|()
decl_stmt|;
name|ProjectPath
name|entPath
init|=
name|path
operator|.
name|appendToPath
argument_list|(
name|ent
argument_list|)
decl_stmt|;
name|handler
operator|.
name|projectNode
argument_list|(
name|entPath
argument_list|)
expr_stmt|;
if|if
condition|(
name|handler
operator|.
name|shouldReadChildren
argument_list|(
name|ent
argument_list|,
name|path
argument_list|)
condition|)
block|{
name|this
operator|.
name|traverseAttributes
argument_list|(
name|ent
operator|.
name|getAttributes
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|,
name|entPath
argument_list|)
expr_stmt|;
name|this
operator|.
name|traverseRelationships
argument_list|(
name|ent
operator|.
name|getRelationships
argument_list|()
operator|.
name|iterator
argument_list|()
argument_list|,
name|entPath
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|traverseAttributes
parameter_list|(
name|Iterator
name|attributes
parameter_list|,
name|ProjectPath
name|path
parameter_list|)
block|{
if|if
condition|(
name|sort
condition|)
block|{
name|attributes
operator|=
name|Util
operator|.
name|sortedIterator
argument_list|(
name|attributes
argument_list|,
name|ProjectTraversal
operator|.
name|mapObjectComparator
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|attributes
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|handler
operator|.
name|projectNode
argument_list|(
name|path
operator|.
name|appendToPath
argument_list|(
name|attributes
operator|.
name|next
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|traverseRelationships
parameter_list|(
name|Iterator
name|relationships
parameter_list|,
name|ProjectPath
name|path
parameter_list|)
block|{
if|if
condition|(
name|sort
condition|)
block|{
name|relationships
operator|=
name|Util
operator|.
name|sortedIterator
argument_list|(
name|relationships
argument_list|,
name|ProjectTraversal
operator|.
name|mapObjectComparator
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|relationships
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|handler
operator|.
name|projectNode
argument_list|(
name|path
operator|.
name|appendToPath
argument_list|(
name|relationships
operator|.
name|next
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|traverseProcedureParameters
parameter_list|(
name|Iterator
name|parameters
parameter_list|,
name|ProjectPath
name|path
parameter_list|)
block|{
comment|// Note: !! do not try to sort parameters - they are positional by definition
while|while
condition|(
name|parameters
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|handler
operator|.
name|projectNode
argument_list|(
name|path
operator|.
name|appendToPath
argument_list|(
name|parameters
operator|.
name|next
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|static
class|class
name|QueryComparator
implements|implements
name|Comparator
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|String
name|name1
init|=
operator|(
operator|(
name|Query
operator|)
name|o1
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|name2
init|=
operator|(
operator|(
name|Query
operator|)
name|o2
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name1
operator|==
literal|null
condition|)
block|{
return|return
operator|(
name|name2
operator|!=
literal|null
operator|)
condition|?
operator|-
literal|1
else|:
literal|0
return|;
block|}
if|else if
condition|(
name|name2
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
else|else
block|{
return|return
name|name1
operator|.
name|compareTo
argument_list|(
name|name2
argument_list|)
return|;
block|}
block|}
block|}
specifier|static
class|class
name|MapObjectComparator
implements|implements
name|Comparator
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|String
name|name1
init|=
operator|(
operator|(
name|CayenneMapEntry
operator|)
name|o1
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|name2
init|=
operator|(
operator|(
name|CayenneMapEntry
operator|)
name|o2
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name1
operator|==
literal|null
condition|)
block|{
return|return
operator|(
name|name2
operator|!=
literal|null
operator|)
condition|?
operator|-
literal|1
else|:
literal|0
return|;
block|}
if|else if
condition|(
name|name2
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
else|else
block|{
return|return
name|name1
operator|.
name|compareTo
argument_list|(
name|name2
argument_list|)
return|;
block|}
block|}
block|}
specifier|static
class|class
name|DataMapComparator
implements|implements
name|Comparator
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|String
name|name1
init|=
operator|(
operator|(
name|DataMap
operator|)
name|o1
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|name2
init|=
operator|(
operator|(
name|DataMap
operator|)
name|o2
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name1
operator|==
literal|null
condition|)
block|{
return|return
operator|(
name|name2
operator|!=
literal|null
operator|)
condition|?
operator|-
literal|1
else|:
literal|0
return|;
block|}
if|else if
condition|(
name|name2
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
else|else
block|{
return|return
name|name1
operator|.
name|compareTo
argument_list|(
name|name2
argument_list|)
return|;
block|}
block|}
block|}
specifier|static
class|class
name|DataDomainComparator
implements|implements
name|Comparator
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|String
name|name1
init|=
operator|(
operator|(
name|DataDomain
operator|)
name|o1
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|name2
init|=
operator|(
operator|(
name|DataDomain
operator|)
name|o2
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name1
operator|==
literal|null
condition|)
block|{
return|return
operator|(
name|name2
operator|!=
literal|null
operator|)
condition|?
operator|-
literal|1
else|:
literal|0
return|;
block|}
if|else if
condition|(
name|name2
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
else|else
block|{
return|return
name|name1
operator|.
name|compareTo
argument_list|(
name|name2
argument_list|)
return|;
block|}
block|}
block|}
specifier|static
class|class
name|DataNodeComparator
implements|implements
name|Comparator
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|String
name|name1
init|=
operator|(
operator|(
name|DataNode
operator|)
name|o1
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|name2
init|=
operator|(
operator|(
name|DataNode
operator|)
name|o2
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name1
operator|==
literal|null
condition|)
block|{
return|return
operator|(
name|name2
operator|!=
literal|null
operator|)
condition|?
operator|-
literal|1
else|:
literal|0
return|;
block|}
if|else if
condition|(
name|name2
operator|==
literal|null
condition|)
block|{
return|return
literal|1
return|;
block|}
else|else
block|{
return|return
name|name1
operator|.
name|compareTo
argument_list|(
name|name2
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

