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
name|ashwood
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
name|QueryResponse
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
name|apache
operator|.
name|cayenne
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
name|apache
operator|.
name|cayenne
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
name|apache
operator|.
name|cayenne
operator|.
name|ashwood
operator|.
name|graph
operator|.
name|StrongConnection
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
name|DbAttribute
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbJoin
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
name|DbRelationship
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
name|EntityResolver
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
name|EntitySorter
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
name|ObjEntity
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
name|ObjRelationship
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

begin_comment
comment|/**  * Implements dependency sorting algorithms for ObjEntities, DbEntities and  * DataObjects. Presently it works for acyclic database schemas with possible  * multi-reflexive tables.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|AshwoodEntitySorter
implements|implements
name|EntitySorter
block|{
specifier|protected
name|EntityResolver
name|entityResolver
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|DbEntity
argument_list|,
name|ComponentRecord
argument_list|>
name|components
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|DbEntity
argument_list|,
name|List
argument_list|<
name|DbRelationship
argument_list|>
argument_list|>
name|reflexiveDbEntities
decl_stmt|;
specifier|protected
name|Comparator
argument_list|<
name|DbEntity
argument_list|>
name|dbEntityComparator
decl_stmt|;
specifier|protected
name|Comparator
argument_list|<
name|ObjEntity
argument_list|>
name|objEntityComparator
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|dirty
decl_stmt|;
specifier|public
name|AshwoodEntitySorter
parameter_list|()
block|{
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
name|dirty
operator|=
literal|true
expr_stmt|;
block|}
comment|/** 	 * Reindexes internal sorter in a thread-safe manner. 	 */
specifier|protected
name|void
name|indexSorter
parameter_list|()
block|{
comment|// correct double check locking per Joshua Bloch
comment|// http://java.sun.com/developer/technicalArticles/Interviews/bloch_effective_08_qa.html
comment|// (maybe we should use something like CountDownLatch or a Cyclic
comment|// barrier
comment|// instead?)
name|boolean
name|localDirty
init|=
name|dirty
decl_stmt|;
if|if
condition|(
name|localDirty
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|localDirty
operator|=
name|dirty
expr_stmt|;
if|if
condition|(
name|localDirty
condition|)
block|{
name|doIndexSorter
argument_list|()
expr_stmt|;
name|dirty
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/** 	 * Reindexes internal sorter without synchronization. 	 */
specifier|protected
name|void
name|doIndexSorter
parameter_list|()
block|{
name|Map
argument_list|<
name|DbEntity
argument_list|,
name|List
argument_list|<
name|DbRelationship
argument_list|>
argument_list|>
name|reflexiveDbEntities
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Digraph
argument_list|<
name|DbEntity
argument_list|,
name|List
argument_list|<
name|DbAttribute
argument_list|>
argument_list|>
name|referentialDigraph
init|=
operator|new
name|MapDigraph
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|entityResolver
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|DbEntity
name|entity
range|:
name|entityResolver
operator|.
name|getDbEntities
argument_list|()
control|)
block|{
name|referentialDigraph
operator|.
name|addVertex
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|DbEntity
name|destination
range|:
name|entityResolver
operator|.
name|getDbEntities
argument_list|()
control|)
block|{
for|for
control|(
name|DbRelationship
name|candidate
range|:
name|destination
operator|.
name|getRelationships
argument_list|()
control|)
block|{
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
name|origin
init|=
name|candidate
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
name|boolean
name|newReflexive
init|=
name|destination
operator|.
name|equals
argument_list|(
name|origin
argument_list|)
decl_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|candidate
operator|.
name|getJoins
argument_list|()
control|)
block|{
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
if|if
condition|(
name|newReflexive
condition|)
block|{
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|reflexiveRels
init|=
name|reflexiveDbEntities
operator|.
name|get
argument_list|(
name|destination
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
argument_list|<>
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|reflexiveDbEntities
operator|.
name|put
argument_list|(
name|destination
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
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|fks
init|=
name|referentialDigraph
operator|.
name|getArc
argument_list|(
name|origin
argument_list|,
name|destination
argument_list|)
decl_stmt|;
if|if
condition|(
name|fks
operator|==
literal|null
condition|)
block|{
name|fks
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|referentialDigraph
operator|.
name|putArc
argument_list|(
name|origin
argument_list|,
name|destination
argument_list|,
name|fks
argument_list|)
expr_stmt|;
block|}
name|fks
operator|.
name|add
argument_list|(
name|targetAttribute
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
name|StrongConnection
argument_list|<
name|DbEntity
argument_list|,
name|List
argument_list|<
name|DbAttribute
argument_list|>
argument_list|>
name|contractor
init|=
operator|new
name|StrongConnection
argument_list|<>
argument_list|(
name|referentialDigraph
argument_list|)
decl_stmt|;
name|Digraph
argument_list|<
name|Collection
argument_list|<
name|DbEntity
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|List
argument_list|<
name|DbAttribute
argument_list|>
argument_list|>
argument_list|>
name|contractedReferentialDigraph
init|=
operator|new
name|MapDigraph
argument_list|<>
argument_list|()
decl_stmt|;
name|contractor
operator|.
name|contract
argument_list|(
name|contractedReferentialDigraph
argument_list|)
expr_stmt|;
name|IndegreeTopologicalSort
argument_list|<
name|Collection
argument_list|<
name|DbEntity
argument_list|>
argument_list|>
name|sorter
init|=
operator|new
name|IndegreeTopologicalSort
argument_list|<>
argument_list|(
name|contractedReferentialDigraph
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|DbEntity
argument_list|,
name|ComponentRecord
argument_list|>
name|components
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|contractedReferentialDigraph
operator|.
name|order
argument_list|()
argument_list|)
decl_stmt|;
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
argument_list|<
name|DbEntity
argument_list|>
name|component
init|=
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
name|DbEntity
name|table
range|:
name|component
control|)
block|{
name|components
operator|.
name|put
argument_list|(
name|table
argument_list|,
name|rec
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|reflexiveDbEntities
operator|=
name|reflexiveDbEntities
expr_stmt|;
name|this
operator|.
name|components
operator|=
name|components
expr_stmt|;
block|}
comment|/** 	 * @since 3.1 	 */
annotation|@
name|Override
specifier|public
name|void
name|setEntityResolver
parameter_list|(
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
name|this
operator|.
name|entityResolver
operator|=
name|entityResolver
expr_stmt|;
name|this
operator|.
name|dirty
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|sortDbEntities
parameter_list|(
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|,
name|boolean
name|deleteOrder
parameter_list|)
block|{
name|indexSorter
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
annotation|@
name|Override
specifier|public
name|void
name|sortObjEntities
parameter_list|(
name|List
argument_list|<
name|ObjEntity
argument_list|>
name|objEntities
parameter_list|,
name|boolean
name|deleteOrder
parameter_list|)
block|{
name|indexSorter
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
annotation|@
name|Override
specifier|public
name|void
name|sortObjectsForEntity
parameter_list|(
name|ObjEntity
name|objEntity
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|objects
parameter_list|,
name|boolean
name|deleteOrder
parameter_list|)
block|{
name|indexSorter
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Persistent
argument_list|>
name|persistent
init|=
operator|(
name|List
argument_list|<
name|Persistent
argument_list|>
operator|)
name|objects
decl_stmt|;
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
name|persistent
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
name|persistent
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
argument_list|<
name|DbRelationship
argument_list|>
name|reflexiveRels
init|=
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
argument_list|<
name|Persistent
argument_list|>
name|sorted
init|=
operator|new
name|ArrayList
argument_list|<
name|Persistent
argument_list|>
argument_list|(
name|size
argument_list|)
decl_stmt|;
name|Digraph
argument_list|<
name|Persistent
argument_list|,
name|Boolean
argument_list|>
name|objectDependencyGraph
init|=
operator|new
name|MapDigraph
argument_list|<>
argument_list|()
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
name|Persistent
name|masterCandidate
init|=
name|persistent
operator|.
name|get
argument_list|(
name|j
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|master
range|:
name|masters
control|)
block|{
if|if
condition|(
name|masterCandidate
operator|==
name|master
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
argument_list|<
name|Persistent
argument_list|>
name|sorter
init|=
operator|new
name|IndegreeTopologicalSort
argument_list|<>
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
name|Persistent
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
name|persistent
operator|.
name|clear
argument_list|()
expr_stmt|;
name|persistent
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
name|persistent
argument_list|)
expr_stmt|;
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
comment|// find committed snapshot - so we can't fetch from the context as it
comment|// will return
comment|// dirty snapshot; must go down the stack instead
comment|// how do we handle this for NEW objects correctly? For now bail from
comment|// the method
if|if
condition|(
name|object
operator|.
name|getObjectId
argument_list|()
operator|.
name|isTemporary
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
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
name|QueryResponse
name|response
init|=
name|context
operator|.
name|getChannel
argument_list|()
operator|.
name|onQuery
argument_list|(
literal|null
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|result
init|=
name|response
operator|.
name|firstList
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
operator|||
name|result
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|DataRow
name|snapshot
init|=
operator|(
name|DataRow
operator|)
name|result
operator|.
name|get
argument_list|(
literal|0
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
comment|// not using 'localObject', looking up in context instead, as within the
comment|// sorter
comment|// we only care about objects participating in transaction, so no need
comment|// to create
comment|// hollow objects
return|return
operator|(
name|id
operator|!=
literal|null
operator|)
condition|?
name|context
operator|.
name|getGraphManager
argument_list|()
operator|.
name|getNode
argument_list|(
name|id
argument_list|)
else|:
literal|null
return|;
block|}
specifier|protected
name|Comparator
argument_list|<
name|DbEntity
argument_list|>
name|getDbEntityComparator
parameter_list|(
name|boolean
name|dependantFirst
parameter_list|)
block|{
name|Comparator
argument_list|<
name|DbEntity
argument_list|>
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
argument_list|<
name|ObjEntity
argument_list|>
name|getObjEntityComparator
parameter_list|(
name|boolean
name|dependantFirst
parameter_list|)
block|{
name|Comparator
argument_list|<
name|ObjEntity
argument_list|>
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
name|ObjEntityComparator
implements|implements
name|Comparator
argument_list|<
name|ObjEntity
argument_list|>
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|ObjEntity
name|o1
parameter_list|,
name|ObjEntity
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
name|DbEntity
name|t1
init|=
name|o1
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
name|DbEntity
name|t2
init|=
name|o2
operator|.
name|getDbEntity
argument_list|()
decl_stmt|;
return|return
name|dbEntityComparator
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
name|DbEntityComparator
implements|implements
name|Comparator
argument_list|<
name|DbEntity
argument_list|>
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|DbEntity
name|t1
parameter_list|,
name|DbEntity
name|t2
parameter_list|)
block|{
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
return|return
operator|-
literal|1
return|;
if|else if
condition|(
name|t2
operator|==
literal|null
condition|)
return|return
literal|1
return|;
else|else
block|{
name|ComponentRecord
name|rec1
init|=
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
name|int
name|result
init|=
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
decl_stmt|;
comment|// TODO: is this check really needed?
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
block|{
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
argument_list|<
name|DbEntity
argument_list|>
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
argument_list|<
name|DbEntity
argument_list|>
name|component
decl_stmt|;
block|}
block|}
end_class

end_unit

