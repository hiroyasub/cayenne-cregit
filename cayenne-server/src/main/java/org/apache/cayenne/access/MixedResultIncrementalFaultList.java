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
name|access
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
name|Arrays
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
name|ResultIterator
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
name|exp
operator|.
name|ExpressionFactory
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
name|ObjAttribute
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
name|query
operator|.
name|ColumnSelect
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
name|EntityResultSegment
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
name|query
operator|.
name|QueryMetadata
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
comment|/**  * FaultList that is used for paginated {@link ColumnSelect} queries.  * It expects data as Object[] where ids are stored instead of Persistent objects (as raw value for single PK  * or Map for compound PKs).  * Scalar values that were fetched from ColumnSelect not processed in any way,  * if there is no Persistent objects in the result Collection it will be iterated as is, without faulting anything.  *  * @see QueryMetadata#getPageSize()  * @see org.apache.cayenne.query.QueryMetadata  *  * @since 4.0  */
end_comment

begin_class
class|class
name|MixedResultIncrementalFaultList
parameter_list|<
name|E
parameter_list|>
extends|extends
name|IncrementalFaultList
argument_list|<
name|E
argument_list|>
block|{
comment|/**      * Cached positions for entity results in elements array      */
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|ObjEntity
argument_list|>
name|indexToEntity
decl_stmt|;
comment|/**      * Whether result contains only scalars      */
specifier|private
name|boolean
name|scalarResult
decl_stmt|;
comment|/**      * Creates a new IncrementalFaultList using a given DataContext and query.      *      * @param dataContext  DataContext used by IncrementalFaultList to fill itself with      *                     objects.      * @param query        Main query used to retrieve data. Must have "pageSize"      *                     property set to a value greater than zero.      */
name|MixedResultIncrementalFaultList
parameter_list|(
name|DataContext
name|dataContext
parameter_list|,
name|Query
name|query
parameter_list|,
name|int
name|maxFetchSize
parameter_list|)
block|{
name|super
argument_list|(
name|dataContext
argument_list|,
name|query
argument_list|,
name|maxFetchSize
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
name|IncrementalListHelper
name|createHelper
parameter_list|(
name|QueryMetadata
name|metadata
parameter_list|)
block|{
comment|// first compile some meta data about results
name|indexToEntity
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|scalarResult
operator|=
literal|true
expr_stmt|;
for|for
control|(
name|Object
name|next
range|:
name|metadata
operator|.
name|getResultSetMapping
argument_list|()
control|)
block|{
if|if
condition|(
name|next
operator|instanceof
name|EntityResultSegment
condition|)
block|{
name|EntityResultSegment
name|resultSegment
init|=
operator|(
name|EntityResultSegment
operator|)
name|next
decl_stmt|;
name|ObjEntity
name|entity
init|=
name|resultSegment
operator|.
name|getClassDescriptor
argument_list|()
operator|.
name|getEntity
argument_list|()
decl_stmt|;
comment|// store entity's PK position in result
name|indexToEntity
operator|.
name|put
argument_list|(
name|resultSegment
operator|.
name|getColumnOffset
argument_list|()
argument_list|,
name|entity
argument_list|)
expr_stmt|;
name|scalarResult
operator|=
literal|false
expr_stmt|;
block|}
block|}
comment|// if there is no entities in this results,
comment|// than all data is already there and we don't need to resolve any objects
if|if
condition|(
name|indexToEntity
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
operator|new
name|ScalarArrayListHelper
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|MixedArrayListHelper
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|fillIn
parameter_list|(
specifier|final
name|Query
name|query
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|elementsList
parameter_list|)
block|{
name|elementsList
operator|.
name|clear
argument_list|()
expr_stmt|;
try|try
init|(
name|ResultIterator
name|it
init|=
name|dataContext
operator|.
name|performIteratedQuery
argument_list|(
name|query
argument_list|)
init|)
block|{
while|while
condition|(
name|it
operator|.
name|hasNextRow
argument_list|()
condition|)
block|{
name|elementsList
operator|.
name|add
argument_list|(
name|it
operator|.
name|nextRow
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|unfetchedObjects
operator|=
name|elementsList
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|resolveInterval
parameter_list|(
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
parameter_list|)
block|{
if|if
condition|(
name|fromIndex
operator|>=
name|toIndex
operator|||
name|scalarResult
condition|)
block|{
return|return;
block|}
synchronized|synchronized
init|(
name|elements
init|)
block|{
if|if
condition|(
name|elements
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
comment|// perform bound checking
if|if
condition|(
name|fromIndex
operator|<
literal|0
condition|)
block|{
name|fromIndex
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|toIndex
operator|>
name|elements
operator|.
name|size
argument_list|()
condition|)
block|{
name|toIndex
operator|=
name|elements
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|ObjEntity
argument_list|>
name|entry
range|:
name|indexToEntity
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|List
argument_list|<
name|Expression
argument_list|>
name|quals
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|pageSize
argument_list|)
decl_stmt|;
name|int
name|dataIdx
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|fromIndex
init|;
name|i
operator|<
name|toIndex
condition|;
name|i
operator|++
control|)
block|{
name|Object
index|[]
name|object
init|=
operator|(
name|Object
index|[]
operator|)
name|elements
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|getHelper
argument_list|()
operator|.
name|unresolvedSuspect
argument_list|(
name|object
index|[
name|dataIdx
index|]
argument_list|)
condition|)
block|{
name|Expression
name|nextQualifier
init|=
name|buildIdQualifier
argument_list|(
name|dataIdx
argument_list|,
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|nextQualifier
operator|!=
literal|null
condition|)
block|{
name|quals
operator|.
name|add
argument_list|(
name|nextQualifier
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|int
name|qualsSize
init|=
name|quals
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|qualsSize
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
comment|// fetch the range of objects in fetchSize chunks
name|List
argument_list|<
name|Persistent
argument_list|>
name|objects
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|qualsSize
argument_list|)
decl_stmt|;
name|int
name|fetchSize
init|=
name|maxFetchSize
operator|>
literal|0
condition|?
name|maxFetchSize
else|:
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
name|int
name|fetchEnd
init|=
name|Math
operator|.
name|min
argument_list|(
name|qualsSize
argument_list|,
name|fetchSize
argument_list|)
decl_stmt|;
name|int
name|fetchBegin
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|fetchBegin
operator|<
name|qualsSize
condition|)
block|{
name|ObjectSelect
argument_list|<
name|Persistent
argument_list|>
name|query
init|=
name|createSelectQuery
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|quals
operator|.
name|subList
argument_list|(
name|fetchBegin
argument_list|,
name|fetchEnd
argument_list|)
argument_list|)
decl_stmt|;
name|objects
operator|.
name|addAll
argument_list|(
name|query
operator|.
name|select
argument_list|(
name|dataContext
argument_list|)
argument_list|)
expr_stmt|;
name|fetchBegin
operator|=
name|fetchEnd
expr_stmt|;
name|fetchEnd
operator|+=
name|Math
operator|.
name|min
argument_list|(
name|fetchSize
argument_list|,
name|qualsSize
operator|-
name|fetchEnd
argument_list|)
expr_stmt|;
block|}
comment|// replace ids in the list with objects
name|updatePageWithResults
argument_list|(
name|objects
argument_list|,
name|dataIdx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|void
name|updatePageWithResults
parameter_list|(
name|List
argument_list|<
name|Persistent
argument_list|>
name|objects
parameter_list|,
name|int
name|dataIndex
parameter_list|)
block|{
name|MixedArrayListHelper
name|helper
init|=
operator|(
name|MixedArrayListHelper
operator|)
name|getHelper
argument_list|()
decl_stmt|;
for|for
control|(
name|Persistent
name|object
range|:
name|objects
control|)
block|{
name|helper
operator|.
name|updateWithResolvedObject
argument_list|(
name|object
argument_list|,
name|dataIndex
argument_list|)
expr_stmt|;
block|}
block|}
name|ObjectSelect
argument_list|<
name|Persistent
argument_list|>
name|createSelectQuery
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|List
argument_list|<
name|Expression
argument_list|>
name|expressions
parameter_list|)
block|{
name|ObjectSelect
argument_list|<
name|Persistent
argument_list|>
name|query
init|=
name|ObjectSelect
operator|.
name|query
argument_list|(
name|Persistent
operator|.
name|class
argument_list|)
operator|.
name|entityName
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|where
argument_list|(
name|ExpressionFactory
operator|.
name|joinExp
argument_list|(
name|Expression
operator|.
name|OR
argument_list|,
name|expressions
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|.
name|equals
argument_list|(
name|rootEntity
argument_list|)
operator|&&
name|metadata
operator|.
name|getPrefetchTree
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|prefetch
argument_list|(
name|metadata
operator|.
name|getPrefetchTree
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|query
return|;
block|}
name|Expression
name|buildIdQualifier
parameter_list|(
name|int
name|index
parameter_list|,
name|Object
index|[]
name|data
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
decl_stmt|;
if|if
condition|(
name|data
index|[
name|index
index|]
operator|instanceof
name|Map
condition|)
block|{
name|map
operator|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|data
index|[
name|index
index|]
expr_stmt|;
block|}
if|else if
condition|(
name|data
index|[
name|index
index|]
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
name|map
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ObjAttribute
name|attribute
range|:
name|indexToEntity
operator|.
name|get
argument_list|(
name|index
argument_list|)
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|attribute
operator|.
name|getDbAttributeName
argument_list|()
argument_list|,
name|data
index|[
name|index
operator|+
name|i
operator|++
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ExpressionFactory
operator|.
name|matchAllDbExp
argument_list|(
name|map
argument_list|,
name|Expression
operator|.
name|EQUAL_TO
argument_list|)
return|;
block|}
comment|/**      * Helper that operates on Object[] and checks for Persistent objects' presence in it.      */
class|class
name|MixedArrayListHelper
extends|extends
name|IncrementalListHelper
block|{
annotation|@
name|Override
name|boolean
name|unresolvedSuspect
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
operator|!
operator|(
name|object
operator|instanceof
name|Persistent
operator|)
return|;
block|}
annotation|@
name|Override
name|boolean
name|objectsAreEqual
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
name|objectInTheList
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|Object
index|[]
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|Arrays
operator|.
name|equals
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|object
argument_list|,
operator|(
name|Object
index|[]
operator|)
name|objectInTheList
argument_list|)
return|;
block|}
annotation|@
name|Override
name|boolean
name|replacesObject
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
name|objectInTheList
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
name|boolean
name|replacesObject
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|Object
index|[]
name|dataInTheList
parameter_list|,
name|int
name|dataIdx
parameter_list|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
name|object
operator|.
name|getObjectId
argument_list|()
operator|.
name|getIdSnapshot
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataInTheList
index|[
name|dataIdx
index|]
operator|instanceof
name|Map
condition|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|id
init|=
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|dataInTheList
index|[
name|dataIdx
index|]
decl_stmt|;
if|if
condition|(
name|id
operator|.
name|size
argument_list|()
operator|!=
name|map
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|id
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|map
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
if|else if
condition|(
name|dataInTheList
index|[
name|dataIdx
index|]
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
for|for
control|(
name|Object
name|id
range|:
name|map
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|dataInTheList
index|[
name|dataIdx
operator|++
index|]
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
name|void
name|updateWithResolvedObject
parameter_list|(
name|Persistent
name|object
parameter_list|,
name|int
name|dataIdx
parameter_list|)
block|{
synchronized|synchronized
init|(
name|elements
init|)
block|{
for|for
control|(
name|Object
name|element
range|:
name|elements
control|)
block|{
name|Object
index|[]
name|data
init|=
operator|(
name|Object
index|[]
operator|)
name|element
decl_stmt|;
if|if
condition|(
name|replacesObject
argument_list|(
name|object
argument_list|,
name|data
argument_list|,
name|dataIdx
argument_list|)
condition|)
block|{
name|data
index|[
name|dataIdx
index|]
operator|=
name|object
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|/**      * Helper that actually does nothing      */
class|class
name|ScalarArrayListHelper
extends|extends
name|IncrementalListHelper
block|{
annotation|@
name|Override
name|boolean
name|unresolvedSuspect
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
name|boolean
name|objectsAreEqual
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
name|objectInTheList
parameter_list|)
block|{
return|return
name|objectInTheList
operator|.
name|equals
argument_list|(
name|object
argument_list|)
return|;
block|}
annotation|@
name|Override
name|boolean
name|replacesObject
parameter_list|(
name|Object
name|object
parameter_list|,
name|Object
name|objectInTheList
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

