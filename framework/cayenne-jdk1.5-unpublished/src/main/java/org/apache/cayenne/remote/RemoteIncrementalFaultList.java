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
name|remote
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
name|ListIterator
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
name|NoSuchElementException
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
name|IDUtil
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
name|IncrementalListResponse
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
comment|/**  * A list that serves as a container of Persistent objects. It is usually returned by an  * ObjectContext when a paginated query is performed. Initially only the first "page" of  * objects is fully resolved. Pages following the first page are resolved on demand. When  * a list element is accessed, the list would ensure that this element as well as all its  * siblings on the same page are fully resolved.  *<p>  * The list can hold DataRows or Persistent objects. Attempts to add any other object  * types will result in an exception.  *</p>  *<p>  * Certain operations like<code>toArray</code> would trigger full list fetch.  *</p>  *<p>  * Synchronization Note: this list is not synchronized. All access to it should follow  * synchronization rules applicable for ArrayList.  *</p>  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|RemoteIncrementalFaultList
implements|implements
name|List
block|{
specifier|static
specifier|final
name|Object
name|PLACEHOLDER
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
specifier|protected
name|List
name|elements
decl_stmt|;
specifier|protected
name|String
name|cacheKey
decl_stmt|;
specifier|protected
name|int
name|pageSize
decl_stmt|;
specifier|protected
name|int
name|unfetchedObjects
decl_stmt|;
specifier|protected
name|Query
name|paginatedQuery
decl_stmt|;
specifier|protected
specifier|transient
name|ObjectContext
name|context
decl_stmt|;
comment|/**      * Stores a hint allowing to distinguish data rows from unfetched ids when the query      * fetches data rows.      */
specifier|protected
name|int
name|rowWidth
decl_stmt|;
specifier|private
name|ListHelper
name|helper
decl_stmt|;
specifier|public
name|RemoteIncrementalFaultList
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|Query
name|paginatedQuery
parameter_list|)
block|{
name|QueryMetadata
name|metadata
init|=
name|paginatedQuery
operator|.
name|getMetaData
argument_list|(
name|context
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|metadata
operator|.
name|getPageSize
argument_list|()
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Page size must be positive: "
operator|+
name|metadata
operator|.
name|getPageSize
argument_list|()
argument_list|)
throw|;
block|}
name|this
operator|.
name|pageSize
operator|=
name|metadata
operator|.
name|getPageSize
argument_list|()
expr_stmt|;
name|this
operator|.
name|helper
operator|=
operator|(
name|metadata
operator|.
name|isFetchingDataRows
argument_list|()
operator|)
condition|?
operator|new
name|DataRowListHelper
argument_list|()
else|:
operator|new
name|PersistentListHelper
argument_list|()
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
comment|// use provided cache key if possible; this would allow clients to
comment|// address the same server-side list from multiple queries.
name|this
operator|.
name|cacheKey
operator|=
name|metadata
operator|.
name|getCacheKey
argument_list|()
expr_stmt|;
if|if
condition|(
name|cacheKey
operator|==
literal|null
condition|)
block|{
name|cacheKey
operator|=
name|generateCacheKey
argument_list|()
expr_stmt|;
block|}
name|Query
name|query
init|=
name|paginatedQuery
decl_stmt|;
comment|// always wrap a query in a Incremental*Query, to ensure cache key is
comment|// client-generated (e.g. see CAY-1003 - client and server can be in different
comment|// timezones, so the key can be messed up)
comment|// there are some serious pagination optimizations for SelectQuery on the
comment|// server-side, so use a special wrapper that is itself a subclass of
comment|// SelectQuery
if|if
condition|(
name|query
operator|instanceof
name|SelectQuery
condition|)
block|{
name|query
operator|=
operator|new
name|IncrementalSelectQuery
argument_list|(
operator|(
name|SelectQuery
operator|)
name|paginatedQuery
argument_list|,
name|cacheKey
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|query
operator|=
operator|new
name|IncrementalQuery
argument_list|(
name|paginatedQuery
argument_list|,
name|cacheKey
argument_list|)
expr_stmt|;
block|}
comment|// ensure that originating query is wrapped to include the right cache key....
name|this
operator|.
name|paginatedQuery
operator|=
name|query
expr_stmt|;
comment|// select directly from the channel, bypassing the context. Otherwise our query
comment|// wrapper can be intercepted incorrectly
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
name|context
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|List
name|firstPage
init|=
name|response
operator|.
name|firstList
argument_list|()
decl_stmt|;
comment|// sanity check
if|if
condition|(
name|firstPage
operator|.
name|size
argument_list|()
operator|>
name|pageSize
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Returned page size ("
operator|+
name|firstPage
operator|.
name|size
argument_list|()
operator|+
literal|") exceeds requested page size ("
operator|+
name|pageSize
operator|+
literal|")"
argument_list|)
throw|;
block|}
comment|// result is smaller than a page
if|else if
condition|(
name|firstPage
operator|.
name|size
argument_list|()
operator|<
name|pageSize
condition|)
block|{
name|this
operator|.
name|elements
operator|=
operator|new
name|ArrayList
argument_list|(
name|firstPage
argument_list|)
expr_stmt|;
name|unfetchedObjects
operator|=
literal|0
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|response
operator|instanceof
name|IncrementalListResponse
condition|)
block|{
name|int
name|fullListSize
init|=
operator|(
operator|(
name|IncrementalListResponse
operator|)
name|response
operator|)
operator|.
name|getFullSize
argument_list|()
decl_stmt|;
name|this
operator|.
name|unfetchedObjects
operator|=
name|fullListSize
operator|-
name|firstPage
operator|.
name|size
argument_list|()
expr_stmt|;
name|this
operator|.
name|elements
operator|=
operator|new
name|ArrayList
argument_list|(
name|fullListSize
argument_list|)
expr_stmt|;
name|elements
operator|.
name|addAll
argument_list|(
name|firstPage
argument_list|)
expr_stmt|;
comment|// fill the rest with placeholder...
for|for
control|(
name|int
name|i
init|=
name|pageSize
init|;
name|i
operator|<
name|fullListSize
condition|;
name|i
operator|++
control|)
block|{
name|elements
operator|.
name|add
argument_list|(
name|PLACEHOLDER
argument_list|)
expr_stmt|;
block|}
block|}
comment|// this happens when full size equals page size
else|else
block|{
name|this
operator|.
name|elements
operator|=
operator|new
name|ArrayList
argument_list|(
name|firstPage
argument_list|)
expr_stmt|;
name|unfetchedObjects
operator|=
literal|0
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|generateCacheKey
parameter_list|()
block|{
name|byte
index|[]
name|bytes
init|=
name|IDUtil
operator|.
name|pseudoUniqueByteSequence8
argument_list|()
decl_stmt|;
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|(
literal|17
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"I"
argument_list|)
expr_stmt|;
for|for
control|(
name|byte
name|aByte
range|:
name|bytes
control|)
block|{
name|IDUtil
operator|.
name|appendFormattedByte
argument_list|(
name|buffer
argument_list|,
name|aByte
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Will resolve all unread objects.      */
specifier|public
name|void
name|resolveAll
parameter_list|()
block|{
name|resolveInterval
argument_list|(
literal|0
argument_list|,
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param object      * @return<code>true</code> if the object corresponds to an unresolved state and      *         does require a fetch before being returned to the user.      */
specifier|private
name|boolean
name|isUnresolved
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
return|return
name|object
operator|==
name|PLACEHOLDER
return|;
block|}
comment|/**      * Resolves a sublist of objects starting at<code>fromIndex</code> up to but not      * including<code>toIndex</code>. Internally performs bound checking and trims      * indexes accordingly.      */
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
name|elements
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No ObjectContext set, can't resolve objects."
argument_list|)
throw|;
block|}
comment|// bounds checking
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
comment|// find disjoint ranges and resolve them individually...
name|int
name|fromPage
init|=
name|pageIndex
argument_list|(
name|fromIndex
argument_list|)
decl_stmt|;
name|int
name|toPage
init|=
name|pageIndex
argument_list|(
name|toIndex
operator|-
literal|1
argument_list|)
decl_stmt|;
name|int
name|rangeStartIndex
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|fromPage
init|;
name|i
operator|<=
name|toPage
condition|;
name|i
operator|++
control|)
block|{
name|int
name|pageStartIndex
init|=
name|i
operator|*
name|pageSize
decl_stmt|;
name|Object
name|firstPageObject
init|=
name|elements
operator|.
name|get
argument_list|(
name|pageStartIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|isUnresolved
argument_list|(
name|firstPageObject
argument_list|)
condition|)
block|{
comment|// start range
if|if
condition|(
name|rangeStartIndex
operator|<
literal|0
condition|)
block|{
name|rangeStartIndex
operator|=
name|pageStartIndex
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// finish range...
if|if
condition|(
name|rangeStartIndex
operator|>=
literal|0
condition|)
block|{
name|forceResolveInterval
argument_list|(
name|rangeStartIndex
argument_list|,
name|pageStartIndex
argument_list|)
expr_stmt|;
name|rangeStartIndex
operator|=
operator|-
literal|1
expr_stmt|;
block|}
block|}
block|}
comment|// load last page
if|if
condition|(
name|rangeStartIndex
operator|>=
literal|0
condition|)
block|{
name|forceResolveInterval
argument_list|(
name|rangeStartIndex
argument_list|,
name|toIndex
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|forceResolveInterval
parameter_list|(
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
parameter_list|)
block|{
name|int
name|pastEnd
init|=
name|toIndex
operator|-
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|pastEnd
operator|>
literal|0
condition|)
block|{
name|toIndex
operator|=
name|size
argument_list|()
expr_stmt|;
block|}
name|int
name|fetchLimit
init|=
name|toIndex
operator|-
name|fromIndex
decl_stmt|;
name|RangeQuery
name|query
init|=
operator|new
name|RangeQuery
argument_list|(
name|cacheKey
argument_list|,
name|fromIndex
argument_list|,
name|fetchLimit
argument_list|,
name|paginatedQuery
argument_list|)
decl_stmt|;
name|List
name|sublist
init|=
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
decl_stmt|;
comment|// sanity check
if|if
condition|(
name|sublist
operator|.
name|size
argument_list|()
operator|!=
name|fetchLimit
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Resolved range size '"
operator|+
name|sublist
operator|.
name|size
argument_list|()
operator|+
literal|"' is not the same as expected: "
operator|+
name|fetchLimit
argument_list|)
throw|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fetchLimit
condition|;
name|i
operator|++
control|)
block|{
name|elements
operator|.
name|set
argument_list|(
name|fromIndex
operator|+
name|i
argument_list|,
name|sublist
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|unfetchedObjects
operator|-=
name|sublist
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns zero-based index of the virtual "page" for a given array element index.      */
name|int
name|pageIndex
parameter_list|(
name|int
name|elementIndex
parameter_list|)
block|{
if|if
condition|(
name|elementIndex
operator|<
literal|0
operator|||
name|elementIndex
operator|>
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"Index: "
operator|+
name|elementIndex
argument_list|)
throw|;
block|}
if|if
condition|(
name|pageSize
operator|<=
literal|0
operator|||
name|elementIndex
operator|<
literal|0
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|elementIndex
operator|/
name|pageSize
return|;
block|}
comment|/**      * Returns ObjectContext associated with this list.      */
specifier|public
name|ObjectContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
comment|/**      * Returns the pageSize.      *       * @return int      */
specifier|public
name|int
name|getPageSize
parameter_list|()
block|{
return|return
name|pageSize
return|;
block|}
comment|/**      * Returns a list iterator for this list. DataObjects are resolved a page (according      * to getPageSize()) at a time as necessary - when retrieved with next() or      * previous().      */
specifier|public
name|ListIterator
name|listIterator
parameter_list|()
block|{
return|return
operator|new
name|ListIteratorHelper
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|/**      * Returns a list iterator of the elements in this list (in proper sequence), starting      * at the specified position in this list. The specified index indicates the first      * element that would be returned by an initial call to the next method. An initial      * call to the previous method would return the element with the specified index minus      * one. DataObjects are resolved a page at a time (according to getPageSize()) as      * necessary - when retrieved with next() or previous().      */
specifier|public
name|ListIterator
name|listIterator
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|index
operator|<
literal|0
operator|||
name|index
operator|>
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IndexOutOfBoundsException
argument_list|(
literal|"Index: "
operator|+
name|index
argument_list|)
throw|;
block|}
return|return
operator|new
name|ListIteratorHelper
argument_list|(
name|index
argument_list|)
return|;
block|}
comment|/**      * Return an iterator for this list. DataObjects are resolved a page (according to      * getPageSize()) at a time as necessary - when retrieved with next().      */
specifier|public
name|Iterator
name|iterator
parameter_list|()
block|{
comment|// by virtue of get(index)'s implementation, resolution of ids into
comment|// objects will occur on pageSize boundaries as necessary.
return|return
operator|new
name|Iterator
argument_list|()
block|{
name|int
name|listIndex
init|=
literal|0
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
operator|(
name|listIndex
operator|<
name|elements
operator|.
name|size
argument_list|()
operator|)
return|;
block|}
specifier|public
name|Object
name|next
parameter_list|()
block|{
if|if
condition|(
name|listIndex
operator|>=
name|elements
operator|.
name|size
argument_list|()
condition|)
throw|throw
operator|new
name|NoSuchElementException
argument_list|(
literal|"no more elements"
argument_list|)
throw|;
return|return
name|get
argument_list|(
name|listIndex
operator|++
argument_list|)
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"remove not supported."
argument_list|)
throw|;
block|}
block|}
return|;
block|}
comment|/**      * @see java.util.List#add(int, Object)      */
specifier|public
name|void
name|add
parameter_list|(
name|int
name|index
parameter_list|,
name|Object
name|element
parameter_list|)
block|{
name|helper
operator|.
name|validateListObject
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|elements
operator|.
name|add
argument_list|(
name|index
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see java.util.Collection#add(Object)      */
specifier|public
name|boolean
name|add
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|helper
operator|.
name|validateListObject
argument_list|(
name|o
argument_list|)
expr_stmt|;
return|return
name|elements
operator|.
name|add
argument_list|(
name|o
argument_list|)
return|;
block|}
comment|/**      * @see java.util.Collection#addAll(Collection)      */
specifier|public
name|boolean
name|addAll
parameter_list|(
name|Collection
name|c
parameter_list|)
block|{
return|return
name|elements
operator|.
name|addAll
argument_list|(
name|c
argument_list|)
return|;
block|}
comment|/**      * @see java.util.List#addAll(int, Collection)      */
specifier|public
name|boolean
name|addAll
parameter_list|(
name|int
name|index
parameter_list|,
name|Collection
name|c
parameter_list|)
block|{
return|return
name|elements
operator|.
name|addAll
argument_list|(
name|index
argument_list|,
name|c
argument_list|)
return|;
block|}
comment|/**      * @see java.util.Collection#clear()      */
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|elements
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * @see java.util.Collection#contains(Object)      */
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|indexOf
argument_list|(
name|o
argument_list|)
operator|>=
literal|0
return|;
block|}
comment|/**      * @see java.util.Collection#containsAll(Collection)      */
specifier|public
name|boolean
name|containsAll
parameter_list|(
name|Collection
name|c
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|c
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
if|if
condition|(
operator|!
name|contains
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * @see java.util.List#get(int)      */
specifier|public
name|Object
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|Object
name|o
init|=
name|elements
operator|.
name|get
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|isUnresolved
argument_list|(
name|o
argument_list|)
condition|)
block|{
comment|// read this page
name|int
name|pageStart
init|=
name|pageIndex
argument_list|(
name|index
argument_list|)
operator|*
name|pageSize
decl_stmt|;
name|resolveInterval
argument_list|(
name|pageStart
argument_list|,
name|pageStart
operator|+
name|pageSize
argument_list|)
expr_stmt|;
return|return
name|elements
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|o
return|;
block|}
block|}
comment|/**      * @see java.util.List#indexOf(Object)      */
specifier|public
name|int
name|indexOf
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|helper
operator|.
name|indexOfObject
argument_list|(
name|o
argument_list|)
return|;
block|}
comment|/**      * @see java.util.Collection#isEmpty()      */
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|elements
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**      * @see java.util.List#lastIndexOf(Object)      */
specifier|public
name|int
name|lastIndexOf
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|helper
operator|.
name|lastIndexOfObject
argument_list|(
name|o
argument_list|)
return|;
block|}
comment|/**      * @see java.util.List#remove(int)      */
specifier|public
name|Object
name|remove
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|elements
operator|.
name|remove
argument_list|(
name|index
argument_list|)
return|;
block|}
comment|/**      * @see java.util.Collection#remove(Object)      */
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|elements
operator|.
name|remove
argument_list|(
name|o
argument_list|)
return|;
block|}
comment|/**      * @see java.util.Collection#removeAll(Collection)      */
specifier|public
name|boolean
name|removeAll
parameter_list|(
name|Collection
name|c
parameter_list|)
block|{
return|return
name|elements
operator|.
name|removeAll
argument_list|(
name|c
argument_list|)
return|;
block|}
comment|/**      * @see java.util.Collection#retainAll(Collection)      */
specifier|public
name|boolean
name|retainAll
parameter_list|(
name|Collection
name|c
parameter_list|)
block|{
return|return
name|elements
operator|.
name|retainAll
argument_list|(
name|c
argument_list|)
return|;
block|}
comment|/**      * @see java.util.List#set(int, Object)      */
specifier|public
name|Object
name|set
parameter_list|(
name|int
name|index
parameter_list|,
name|Object
name|element
parameter_list|)
block|{
name|helper
operator|.
name|validateListObject
argument_list|(
name|element
argument_list|)
expr_stmt|;
return|return
name|elements
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|element
argument_list|)
return|;
block|}
comment|/**      * @see java.util.Collection#size()      */
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|elements
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|List
name|subList
parameter_list|(
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
parameter_list|)
block|{
name|resolveInterval
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|)
expr_stmt|;
return|return
name|elements
operator|.
name|subList
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|)
return|;
block|}
specifier|public
name|Object
index|[]
name|toArray
parameter_list|()
block|{
name|resolveAll
argument_list|()
expr_stmt|;
return|return
name|elements
operator|.
name|toArray
argument_list|()
return|;
block|}
comment|/**      * @see java.util.Collection#toArray(Object[])      */
specifier|public
name|Object
index|[]
name|toArray
parameter_list|(
name|Object
index|[]
name|a
parameter_list|)
block|{
name|resolveAll
argument_list|()
expr_stmt|;
return|return
name|elements
operator|.
name|toArray
argument_list|(
name|a
argument_list|)
return|;
block|}
comment|/**      * Returns a total number of objects that are not resolved yet.      */
specifier|public
name|int
name|getUnfetchedObjects
parameter_list|()
block|{
return|return
name|unfetchedObjects
return|;
block|}
specifier|abstract
class|class
name|ListHelper
block|{
name|int
name|indexOfObject
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|incorrectObjectType
argument_list|(
name|object
argument_list|)
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|elements
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|object
argument_list|,
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
name|int
name|lastIndexOfObject
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|incorrectObjectType
argument_list|(
name|object
argument_list|)
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
for|for
control|(
name|int
name|i
init|=
name|elements
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
if|if
condition|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|object
argument_list|,
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
specifier|abstract
name|boolean
name|incorrectObjectType
parameter_list|(
name|Object
name|object
parameter_list|)
function_decl|;
name|void
name|validateListObject
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|incorrectObjectType
argument_list|(
name|object
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can't store this object: "
operator|+
name|object
argument_list|)
throw|;
block|}
block|}
block|}
class|class
name|PersistentListHelper
extends|extends
name|ListHelper
block|{
annotation|@
name|Override
name|boolean
name|incorrectObjectType
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|Persistent
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Persistent
name|persistent
init|=
operator|(
name|Persistent
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|persistent
operator|.
name|getObjectContext
argument_list|()
operator|!=
name|context
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
class|class
name|DataRowListHelper
extends|extends
name|ListHelper
block|{
annotation|@
name|Override
name|boolean
name|incorrectObjectType
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|Map
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Map
name|map
init|=
operator|(
name|Map
operator|)
name|object
decl_stmt|;
return|return
name|map
operator|.
name|size
argument_list|()
operator|!=
name|rowWidth
return|;
block|}
block|}
class|class
name|ListIteratorHelper
implements|implements
name|ListIterator
block|{
comment|// by virtue of get(index)'s implementation, resolution of ids into
comment|// objects will occur on pageSize boundaries as necessary.
name|int
name|listIndex
decl_stmt|;
specifier|public
name|ListIteratorHelper
parameter_list|(
name|int
name|startIndex
parameter_list|)
block|{
name|this
operator|.
name|listIndex
operator|=
name|startIndex
expr_stmt|;
block|}
specifier|public
name|void
name|add
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"add operation not supported"
argument_list|)
throw|;
block|}
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
operator|(
name|listIndex
operator|<
name|elements
operator|.
name|size
argument_list|()
operator|)
return|;
block|}
specifier|public
name|boolean
name|hasPrevious
parameter_list|()
block|{
return|return
operator|(
name|listIndex
operator|>
literal|0
operator|)
return|;
block|}
specifier|public
name|Object
name|next
parameter_list|()
block|{
if|if
condition|(
name|listIndex
operator|>=
name|elements
operator|.
name|size
argument_list|()
condition|)
throw|throw
operator|new
name|NoSuchElementException
argument_list|(
literal|"at the end of the list"
argument_list|)
throw|;
return|return
name|get
argument_list|(
name|listIndex
operator|++
argument_list|)
return|;
block|}
specifier|public
name|int
name|nextIndex
parameter_list|()
block|{
return|return
name|listIndex
return|;
block|}
specifier|public
name|Object
name|previous
parameter_list|()
block|{
if|if
condition|(
name|listIndex
operator|<
literal|1
condition|)
throw|throw
operator|new
name|NoSuchElementException
argument_list|(
literal|"at the beginning of the list"
argument_list|)
throw|;
return|return
name|get
argument_list|(
operator|--
name|listIndex
argument_list|)
return|;
block|}
specifier|public
name|int
name|previousIndex
parameter_list|()
block|{
return|return
operator|(
name|listIndex
operator|-
literal|1
operator|)
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"remove operation not supported"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|set
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"set operation not supported"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

