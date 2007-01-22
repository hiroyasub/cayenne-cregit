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
name|HashMap
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
name|DataObjectUtils
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
name|DataRow
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
name|ObjectContext
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
name|ObjectId
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
name|Persistent
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
name|ObjectIdQuery
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
name|reflect
operator|.
name|ClassDescriptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|comparators
operator|.
name|ReverseComparator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectstyle
operator|.
name|ashwood
operator|.
name|dbutil
operator|.
name|DbUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectstyle
operator|.
name|ashwood
operator|.
name|dbutil
operator|.
name|ForeignKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectstyle
operator|.
name|ashwood
operator|.
name|dbutil
operator|.
name|Table
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectstyle
operator|.
name|ashwood
operator|.
name|graph
operator|.
name|CollectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectstyle
operator|.
name|ashwood
operator|.
name|graph
operator|.
name|Digraph
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectstyle
operator|.
name|ashwood
operator|.
name|graph
operator|.
name|IndegreeTopologicalSort
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectstyle
operator|.
name|ashwood
operator|.
name|graph
operator|.
name|MapDigraph
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectstyle
operator|.
name|ashwood
operator|.
name|graph
operator|.
name|StrongConnection
import|;
end_import

begin_comment
comment|/**  * Implements dependency sorting algorithms for ObjEntities, DbEntities and DataObjects.  * Presently it works for acyclic database schemas with possible multi-reflexive tables.  * The class uses topological sorting from the<a  * href="http://objectstyle.org/ashwood/">Ashwood library</a>.  *   * @author Andriy Shapochka, Andrus Adamchik  * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|AshwoodEntitySorter
implements|implements
name|EntitySorter
block|{
specifier|protected
name|Collection
name|dataMaps
decl_stmt|;
specifier|protected
name|Map
name|dbEntityToTableMap
decl_stmt|;
specifier|protected
name|Digraph
name|referentialDigraph
decl_stmt|;
specifier|protected
name|Digraph
name|contractedReferentialDigraph
decl_stmt|;
specifier|protected
name|Map
name|components
decl_stmt|;
specifier|protected
name|Map
name|reflexiveDbEntities
decl_stmt|;
specifier|protected
name|TableComparator
name|tableComparator
decl_stmt|;
specifier|protected
name|DbEntityComparator
name|dbEntityComparator
decl_stmt|;
specifier|protected
name|ObjEntityComparator
name|objEntityComparator
decl_stmt|;
comment|// used for lazy initialization
specifier|protected
name|boolean
name|dirty
decl_stmt|;
specifier|public
name|AshwoodEntitySorter
parameter_list|(
name|Collection
name|dataMaps
parameter_list|)
block|{
name|tableComparator
operator|=
operator|new
name|TableComparator
argument_list|()
expr_stmt|;
name|dbEntityComparator
operator|=
operator|new
name|DbEntityComparator
argument_list|()
expr_stmt|;
name|objEntityComparator
operator|=
operator|new
name|ObjEntityComparator
argument_list|()
expr_stmt|;
name|setDataMaps
argument_list|(
name|dataMaps
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reindexes internal sorter.      */
specifier|protected
specifier|synchronized
name|void
name|_indexSorter
parameter_list|()
block|{
if|if
condition|(
operator|!
name|dirty
condition|)
block|{
return|return;
block|}
name|Collection
name|tables
init|=
operator|new
name|ArrayList
argument_list|(
literal|64
argument_list|)
decl_stmt|;
name|dbEntityToTableMap
operator|=
operator|new
name|HashMap
argument_list|(
literal|64
argument_list|)
expr_stmt|;
name|reflexiveDbEntities
operator|=
operator|new
name|HashMap
argument_list|(
literal|32
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|dataMaps
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|Iterator
name|entitiesToConvert
init|=
name|map
operator|.
name|getDbEntities
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|entitiesToConvert
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbEntity
name|entity
init|=
operator|(
name|DbEntity
operator|)
name|entitiesToConvert
operator|.
name|next
argument_list|()
decl_stmt|;
name|Table
name|table
init|=
operator|new
name|Table
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
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|fillInMetadata
argument_list|(
name|table
argument_list|,
name|entity
argument_list|)
expr_stmt|;
name|dbEntityToTableMap
operator|.
name|put
argument_list|(
name|entity
argument_list|,
name|table
argument_list|)
expr_stmt|;
name|tables
operator|.
name|add
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
block|}
name|referentialDigraph
operator|=
operator|new
name|MapDigraph
argument_list|(
name|MapDigraph
operator|.
name|HASHMAP_FACTORY
argument_list|)
expr_stmt|;
name|DbUtils
operator|.
name|buildReferentialDigraph
argument_list|(
name|referentialDigraph
argument_list|,
name|tables
argument_list|)
expr_stmt|;
name|StrongConnection
name|contractor
init|=
operator|new
name|StrongConnection
argument_list|(
name|referentialDigraph
argument_list|,
name|CollectionFactory
operator|.
name|ARRAYLIST_FACTORY
argument_list|)
decl_stmt|;
name|contractedReferentialDigraph
operator|=
operator|new
name|MapDigraph
argument_list|(
name|MapDigraph
operator|.
name|HASHMAP_FACTORY
argument_list|)
expr_stmt|;
name|contractor
operator|.
name|contract
argument_list|(
name|contractedReferentialDigraph
argument_list|,
name|CollectionFactory
operator|.
name|ARRAYLIST_FACTORY
argument_list|)
expr_stmt|;
name|IndegreeTopologicalSort
name|sorter
init|=
operator|new
name|IndegreeTopologicalSort
argument_list|(
name|contractedReferentialDigraph
argument_list|)
decl_stmt|;
name|components
operator|=
operator|new
name|HashMap
argument_list|(
name|contractedReferentialDigraph
operator|.
name|order
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|componentIndex
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|sorter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Collection
name|component
init|=
operator|(
name|Collection
operator|)
name|sorter
operator|.
name|next
argument_list|()
decl_stmt|;
name|ComponentRecord
name|rec
init|=
operator|new
name|ComponentRecord
argument_list|(
name|componentIndex
operator|++
argument_list|,
name|component
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|component
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|components
operator|.
name|put
argument_list|(
name|i
operator|.
name|next
argument_list|()
argument_list|,
name|rec
argument_list|)
expr_stmt|;
block|}
block|}
comment|// clear dirty flag
name|this
operator|.
name|dirty
operator|=
literal|false
expr_stmt|;
block|}
comment|/**      * @since 1.1      */
specifier|public
specifier|synchronized
name|void
name|setDataMaps
parameter_list|(
name|Collection
name|dataMaps
parameter_list|)
block|{
name|this
operator|.
name|dirty
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|dataMaps
operator|=
name|dataMaps
operator|!=
literal|null
condition|?
name|dataMaps
else|:
name|Collections
operator|.
name|EMPTY_LIST
expr_stmt|;
block|}
specifier|public
name|void
name|sortDbEntities
parameter_list|(
name|List
name|dbEntities
parameter_list|,
name|boolean
name|deleteOrder
parameter_list|)
block|{
name|_indexSorter
argument_list|()
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|dbEntities
argument_list|,
name|getDbEntityComparator
argument_list|(
name|deleteOrder
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|sortObjEntities
parameter_list|(
name|List
name|objEntities
parameter_list|,
name|boolean
name|deleteOrder
parameter_list|)
block|{
name|_indexSorter
argument_list|()
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|objEntities
argument_list|,
name|getObjEntityComparator
argument_list|(
name|deleteOrder
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|sortObjectsForEntity
parameter_list|(
name|ObjEntity
name|objEntity
parameter_list|,
name|List
name|objects
parameter_list|,
name|boolean
name|deleteOrder
parameter_list|)
block|{
comment|// don't forget to index the sorter
name|_indexSorter
argument_list|()
expr_stmt|;
name|DbEntity
name|dbEntity
init|=
name|objEntity
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
comment|// if no sorting is required
if|if
condition|(
operator|!
name|isReflexive
argument_list|(
name|dbEntity
argument_list|)
condition|)
block|{
return|return;
block|}
name|int
name|size
init|=
name|objects
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|EntityResolver
name|resolver
init|=
operator|(
operator|(
name|Persistent
operator|)
name|objects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getObjectContext
argument_list|()
operator|.
name|getEntityResolver
argument_list|()
decl_stmt|;
name|ClassDescriptor
name|descriptor
init|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
name|objEntity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|List
name|reflexiveRels
init|=
operator|(
name|List
operator|)
name|reflexiveDbEntities
operator|.
name|get
argument_list|(
name|dbEntity
argument_list|)
decl_stmt|;
name|String
index|[]
name|reflexiveRelNames
init|=
operator|new
name|String
index|[
name|reflexiveRels
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|reflexiveRelNames
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|DbRelationship
name|dbRel
init|=
operator|(
name|DbRelationship
operator|)
name|reflexiveRels
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|ObjRelationship
name|objRel
init|=
operator|(
name|dbRel
operator|!=
literal|null
condition|?
name|objEntity
operator|.
name|getRelationshipForDbRelationship
argument_list|(
name|dbRel
argument_list|)
else|:
literal|null
operator|)
decl_stmt|;
name|reflexiveRelNames
index|[
name|i
index|]
operator|=
operator|(
name|objRel
operator|!=
literal|null
condition|?
name|objRel
operator|.
name|getName
argument_list|()
else|:
literal|null
operator|)
expr_stmt|;
block|}
name|List
name|sorted
init|=
operator|new
name|ArrayList
argument_list|(
name|size
argument_list|)
decl_stmt|;
name|Digraph
name|objectDependencyGraph
init|=
operator|new
name|MapDigraph
argument_list|(
name|MapDigraph
operator|.
name|HASHMAP_FACTORY
argument_list|)
decl_stmt|;
name|Object
index|[]
name|masters
init|=
operator|new
name|Object
index|[
name|reflexiveRelNames
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Persistent
name|current
init|=
operator|(
name|Persistent
operator|)
name|objects
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|objectDependencyGraph
operator|.
name|addVertex
argument_list|(
name|current
argument_list|)
expr_stmt|;
name|int
name|actualMasterCount
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|reflexiveRelNames
operator|.
name|length
condition|;
name|k
operator|++
control|)
block|{
name|String
name|reflexiveRelName
init|=
name|reflexiveRelNames
index|[
name|k
index|]
decl_stmt|;
if|if
condition|(
name|reflexiveRelName
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|masters
index|[
name|k
index|]
operator|=
operator|(
name|reflexiveRelName
operator|!=
literal|null
operator|)
condition|?
name|descriptor
operator|.
name|getProperty
argument_list|(
name|reflexiveRelName
argument_list|)
operator|.
name|readProperty
argument_list|(
name|current
argument_list|)
else|:
literal|null
expr_stmt|;
if|if
condition|(
name|masters
index|[
name|k
index|]
operator|==
literal|null
condition|)
block|{
name|masters
index|[
name|k
index|]
operator|=
name|findReflexiveMaster
argument_list|(
name|current
argument_list|,
operator|(
name|ObjRelationship
operator|)
name|objEntity
operator|.
name|getRelationship
argument_list|(
name|reflexiveRelName
argument_list|)
argument_list|,
name|current
operator|.
name|getObjectId
argument_list|()
operator|.
name|getEntityName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|masters
index|[
name|k
index|]
operator|!=
literal|null
condition|)
block|{
name|actualMasterCount
operator|++
expr_stmt|;
block|}
block|}
name|int
name|mastersFound
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|size
operator|&&
name|mastersFound
operator|<
name|actualMasterCount
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|==
name|j
condition|)
block|{
continue|continue;
block|}
name|Object
name|masterCandidate
init|=
name|objects
operator|.
name|get
argument_list|(
name|j
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|masters
operator|.
name|length
condition|;
name|k
operator|++
control|)
block|{
if|if
condition|(
name|masterCandidate
operator|.
name|equals
argument_list|(
name|masters
index|[
name|k
index|]
argument_list|)
condition|)
block|{
name|objectDependencyGraph
operator|.
name|putArc
argument_list|(
name|masterCandidate
argument_list|,
name|current
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|mastersFound
operator|++
expr_stmt|;
block|}
block|}
block|}
block|}
name|IndegreeTopologicalSort
name|sorter
init|=
operator|new
name|IndegreeTopologicalSort
argument_list|(
name|objectDependencyGraph
argument_list|)
decl_stmt|;
while|while
condition|(
name|sorter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|o
init|=
name|sorter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|o
operator|==
literal|null
condition|)
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Sorting objects for "
operator|+
name|objEntity
operator|.
name|getClassName
argument_list|()
operator|+
literal|" failed. Cycles found."
argument_list|)
throw|;
name|sorted
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
comment|// since API requires sorting within the same array,
comment|// simply replace all objects with objects in the right order...
comment|// may come up with something cleaner later
name|objects
operator|.
name|clear
argument_list|()
expr_stmt|;
name|objects
operator|.
name|addAll
argument_list|(
name|sorted
argument_list|)
expr_stmt|;
if|if
condition|(
name|deleteOrder
condition|)
block|{
name|Collections
operator|.
name|reverse
argument_list|(
name|objects
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|fillInMetadata
parameter_list|(
name|Table
name|table
parameter_list|,
name|DbEntity
name|entity
parameter_list|)
block|{
comment|// in this case quite a dummy
name|short
name|keySequence
init|=
literal|1
decl_stmt|;
name|Iterator
name|i
init|=
name|entity
operator|.
name|getRelationshipMap
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbRelationship
name|candidate
init|=
operator|(
name|DbRelationship
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
operator|!
name|candidate
operator|.
name|isToMany
argument_list|()
operator|&&
operator|!
name|candidate
operator|.
name|isToDependentPK
argument_list|()
operator|)
operator|||
name|candidate
operator|.
name|isToMasterPK
argument_list|()
condition|)
block|{
name|DbEntity
name|target
init|=
operator|(
name|DbEntity
operator|)
name|candidate
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
name|boolean
name|newReflexive
init|=
name|entity
operator|.
name|equals
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|Iterator
name|j
init|=
name|candidate
operator|.
name|getJoins
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|j
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbJoin
name|join
init|=
operator|(
name|DbJoin
operator|)
name|j
operator|.
name|next
argument_list|()
decl_stmt|;
name|DbAttribute
name|targetAttribute
init|=
name|join
operator|.
name|getTarget
argument_list|()
decl_stmt|;
if|if
condition|(
name|targetAttribute
operator|.
name|isPrimaryKey
argument_list|()
condition|)
block|{
name|ForeignKey
name|fk
init|=
operator|new
name|ForeignKey
argument_list|()
decl_stmt|;
name|fk
operator|.
name|setPkTableCatalog
argument_list|(
name|target
operator|.
name|getCatalog
argument_list|()
argument_list|)
expr_stmt|;
name|fk
operator|.
name|setPkTableSchema
argument_list|(
name|target
operator|.
name|getSchema
argument_list|()
argument_list|)
expr_stmt|;
name|fk
operator|.
name|setPkTableName
argument_list|(
name|target
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|fk
operator|.
name|setPkColumnName
argument_list|(
name|targetAttribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|fk
operator|.
name|setColumnName
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
expr_stmt|;
name|fk
operator|.
name|setKeySequence
argument_list|(
name|keySequence
operator|++
argument_list|)
expr_stmt|;
name|table
operator|.
name|addForeignKey
argument_list|(
name|fk
argument_list|)
expr_stmt|;
if|if
condition|(
name|newReflexive
condition|)
block|{
name|List
name|reflexiveRels
init|=
operator|(
name|List
operator|)
name|reflexiveDbEntities
operator|.
name|get
argument_list|(
name|entity
argument_list|)
decl_stmt|;
if|if
condition|(
name|reflexiveRels
operator|==
literal|null
condition|)
block|{
name|reflexiveRels
operator|=
operator|new
name|ArrayList
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|reflexiveDbEntities
operator|.
name|put
argument_list|(
name|entity
argument_list|,
name|reflexiveRels
argument_list|)
expr_stmt|;
block|}
name|reflexiveRels
operator|.
name|add
argument_list|(
name|candidate
argument_list|)
expr_stmt|;
name|newReflexive
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
specifier|protected
name|Object
name|findReflexiveMaster
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|ObjRelationship
name|toOneRel
parameter_list|,
name|String
name|targetEntityName
parameter_list|)
block|{
name|DbRelationship
name|finalRel
init|=
operator|(
name|DbRelationship
operator|)
name|toOneRel
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|ObjectContext
name|context
init|=
name|object
operator|.
name|getObjectContext
argument_list|()
decl_stmt|;
comment|// find snapshot
name|ObjectIdQuery
name|query
init|=
operator|new
name|ObjectIdQuery
argument_list|(
name|object
operator|.
name|getObjectId
argument_list|()
argument_list|,
literal|true
argument_list|,
name|ObjectIdQuery
operator|.
name|CACHE
argument_list|)
decl_stmt|;
name|DataRow
name|snapshot
init|=
operator|(
name|DataRow
operator|)
name|DataObjectUtils
operator|.
name|objectForQuery
argument_list|(
name|context
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|ObjectId
name|id
init|=
name|snapshot
operator|.
name|createTargetObjectId
argument_list|(
name|targetEntityName
argument_list|,
name|finalRel
argument_list|)
decl_stmt|;
return|return
operator|(
name|id
operator|!=
literal|null
operator|)
condition|?
name|context
operator|.
name|localObject
argument_list|(
name|id
argument_list|,
literal|null
argument_list|)
else|:
literal|null
return|;
block|}
specifier|protected
name|Comparator
name|getDbEntityComparator
parameter_list|(
name|boolean
name|dependantFirst
parameter_list|)
block|{
name|Comparator
name|c
init|=
name|dbEntityComparator
decl_stmt|;
if|if
condition|(
name|dependantFirst
condition|)
block|{
name|c
operator|=
operator|new
name|ReverseComparator
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
specifier|protected
name|Comparator
name|getObjEntityComparator
parameter_list|(
name|boolean
name|dependantFirst
parameter_list|)
block|{
name|Comparator
name|c
init|=
name|objEntityComparator
decl_stmt|;
if|if
condition|(
name|dependantFirst
condition|)
block|{
name|c
operator|=
operator|new
name|ReverseComparator
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
specifier|protected
name|Table
name|getTable
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|)
block|{
return|return
operator|(
name|dbEntity
operator|!=
literal|null
operator|)
condition|?
operator|(
name|Table
operator|)
name|dbEntityToTableMap
operator|.
name|get
argument_list|(
name|dbEntity
argument_list|)
else|:
literal|null
return|;
block|}
specifier|protected
name|Table
name|getTable
parameter_list|(
name|ObjEntity
name|objEntity
parameter_list|)
block|{
return|return
name|getTable
argument_list|(
name|objEntity
operator|.
name|getDbEntity
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isReflexive
parameter_list|(
name|DbEntity
name|metadata
parameter_list|)
block|{
return|return
name|reflexiveDbEntities
operator|.
name|containsKey
argument_list|(
name|metadata
argument_list|)
return|;
block|}
specifier|private
specifier|final
class|class
name|DbEntityComparator
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
if|if
condition|(
name|o1
operator|==
name|o2
condition|)
return|return
literal|0
return|;
name|Table
name|t1
init|=
name|getTable
argument_list|(
operator|(
name|DbEntity
operator|)
name|o1
argument_list|)
decl_stmt|;
name|Table
name|t2
init|=
name|getTable
argument_list|(
operator|(
name|DbEntity
operator|)
name|o2
argument_list|)
decl_stmt|;
return|return
name|tableComparator
operator|.
name|compare
argument_list|(
name|t1
argument_list|,
name|t2
argument_list|)
return|;
block|}
block|}
specifier|private
specifier|final
class|class
name|ObjEntityComparator
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
if|if
condition|(
name|o1
operator|==
name|o2
condition|)
return|return
literal|0
return|;
name|Table
name|t1
init|=
name|getTable
argument_list|(
operator|(
name|ObjEntity
operator|)
name|o1
argument_list|)
decl_stmt|;
name|Table
name|t2
init|=
name|getTable
argument_list|(
operator|(
name|ObjEntity
operator|)
name|o2
argument_list|)
decl_stmt|;
return|return
name|tableComparator
operator|.
name|compare
argument_list|(
name|t1
argument_list|,
name|t2
argument_list|)
return|;
block|}
block|}
specifier|private
specifier|final
class|class
name|TableComparator
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
name|int
name|result
init|=
literal|0
decl_stmt|;
name|Table
name|t1
init|=
operator|(
name|Table
operator|)
name|o1
decl_stmt|;
name|Table
name|t2
init|=
operator|(
name|Table
operator|)
name|o2
decl_stmt|;
if|if
condition|(
name|t1
operator|==
name|t2
condition|)
return|return
literal|0
return|;
if|if
condition|(
name|t1
operator|==
literal|null
condition|)
name|result
operator|=
operator|-
literal|1
expr_stmt|;
if|else if
condition|(
name|t2
operator|==
literal|null
condition|)
name|result
operator|=
literal|1
expr_stmt|;
else|else
block|{
name|ComponentRecord
name|rec1
init|=
operator|(
name|ComponentRecord
operator|)
name|components
operator|.
name|get
argument_list|(
name|t1
argument_list|)
decl_stmt|;
name|ComponentRecord
name|rec2
init|=
operator|(
name|ComponentRecord
operator|)
name|components
operator|.
name|get
argument_list|(
name|t2
argument_list|)
decl_stmt|;
name|int
name|index1
init|=
name|rec1
operator|.
name|index
decl_stmt|;
name|int
name|index2
init|=
name|rec2
operator|.
name|index
decl_stmt|;
name|result
operator|=
operator|(
name|index1
operator|>
name|index2
condition|?
literal|1
else|:
operator|(
name|index1
operator|<
name|index2
condition|?
operator|-
literal|1
else|:
literal|0
operator|)
operator|)
expr_stmt|;
if|if
condition|(
name|result
operator|!=
literal|0
operator|&&
name|rec1
operator|.
name|component
operator|==
name|rec2
operator|.
name|component
condition|)
name|result
operator|=
literal|0
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
specifier|private
specifier|final
specifier|static
class|class
name|ComponentRecord
block|{
name|ComponentRecord
parameter_list|(
name|int
name|index
parameter_list|,
name|Collection
name|component
parameter_list|)
block|{
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
name|int
name|index
decl_stmt|;
name|Collection
name|component
decl_stmt|;
block|}
block|}
end_class

end_unit

