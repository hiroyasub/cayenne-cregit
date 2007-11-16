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
name|query
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
name|QueryBuilder
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|XMLSerializable
import|;
end_import

begin_comment
comment|/**  * A query that selects persistent objects of a certain type or "raw data" (aka DataRows).  * Supports expression qualifier, multiple orderings and a number of other parameters that  * serve as runtime hints to Cayenne on how to optimize the fetch and result processing.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|SelectQuery
extends|extends
name|QualifiedQuery
implements|implements
name|ParameterizedQuery
implements|,
name|XMLSerializable
block|{
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
name|List
argument_list|<
name|String
argument_list|>
name|customDbAttributes
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Ordering
argument_list|>
name|orderings
decl_stmt|;
specifier|protected
name|boolean
name|distinct
decl_stmt|;
name|SelectQueryMetadata
name|selectInfo
init|=
operator|new
name|SelectQueryMetadata
argument_list|()
decl_stmt|;
comment|/** Creates an empty SelectQuery. */
specifier|public
name|SelectQuery
parameter_list|()
block|{
block|}
comment|/**      * Creates a SelectQuery with null qualifier, for the specifed ObjEntity      *       * @param root the ObjEntity this SelectQuery is for.      */
specifier|public
name|SelectQuery
parameter_list|(
name|ObjEntity
name|root
parameter_list|)
block|{
name|this
argument_list|(
name|root
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a SelectQuery for the specified ObjEntity with the given qualifier      *       * @param root the ObjEntity this SelectQuery is for.      * @param qualifier an Expression indicating which objects should be fetched      */
specifier|public
name|SelectQuery
parameter_list|(
name|ObjEntity
name|root
parameter_list|,
name|Expression
name|qualifier
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|init
argument_list|(
name|root
argument_list|,
name|qualifier
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a SelectQuery that selects all objects of a given persistent class.      *       * @param rootClass the Class of objects fetched by this query.      */
specifier|public
name|SelectQuery
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|rootClass
parameter_list|)
block|{
name|this
argument_list|(
name|rootClass
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a SelectQuery that selects objects of a given persistent class that match      * supplied qualifier.      *       * @param rootClass the Class of objects fetched by this query.      */
specifier|public
name|SelectQuery
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|rootClass
parameter_list|,
name|Expression
name|qualifier
parameter_list|)
block|{
name|init
argument_list|(
name|rootClass
argument_list|,
name|qualifier
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a SelectQuery for the specified DbEntity.      *       * @param root the DbEntity this SelectQuery is for.      * @since 1.1      */
specifier|public
name|SelectQuery
parameter_list|(
name|DbEntity
name|root
parameter_list|)
block|{
name|this
argument_list|(
name|root
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a SelectQuery for the specified DbEntity with the given qualifier.      *       * @param root the DbEntity this SelectQuery is for.      * @param qualifier an Expression indicating which objects should be fetched      * @since 1.1      */
specifier|public
name|SelectQuery
parameter_list|(
name|DbEntity
name|root
parameter_list|,
name|Expression
name|qualifier
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|init
argument_list|(
name|root
argument_list|,
name|qualifier
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates SelectQuery with<code>objEntityName</code> parameter.      */
specifier|public
name|SelectQuery
parameter_list|(
name|String
name|objEntityName
parameter_list|)
block|{
name|this
argument_list|(
name|objEntityName
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates SelectQuery with<code>objEntityName</code> and<code>qualifier</code>      * parameters.      */
specifier|public
name|SelectQuery
parameter_list|(
name|String
name|objEntityName
parameter_list|,
name|Expression
name|qualifier
parameter_list|)
block|{
name|init
argument_list|(
name|objEntityName
argument_list|,
name|qualifier
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|(
name|Object
name|root
parameter_list|,
name|Expression
name|qualifier
parameter_list|)
block|{
name|this
operator|.
name|setRoot
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|this
operator|.
name|setQualifier
argument_list|(
name|qualifier
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|QueryMetadata
name|getMetaData
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|selectInfo
operator|.
name|resolve
argument_list|(
name|root
argument_list|,
name|resolver
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// must force DataRows if custom attributes are fetched
if|if
condition|(
name|isFetchingCustomAttributes
argument_list|()
condition|)
block|{
name|QueryMetadataWrapper
name|wrapper
init|=
operator|new
name|QueryMetadataWrapper
argument_list|(
name|selectInfo
argument_list|)
decl_stmt|;
name|wrapper
operator|.
name|override
argument_list|(
name|QueryMetadata
operator|.
name|FETCHING_DATA_ROWS_PROPERTY
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
name|wrapper
return|;
block|}
else|else
block|{
return|return
name|selectInfo
return|;
block|}
block|}
comment|/**      * Routes itself and if there are any prefetches configured, creates prefetch queries      * and routes them as well.      *       * @since 1.2      */
specifier|public
name|void
name|route
parameter_list|(
name|QueryRouter
name|router
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|,
name|Query
name|substitutedQuery
parameter_list|)
block|{
name|super
operator|.
name|route
argument_list|(
name|router
argument_list|,
name|resolver
argument_list|,
name|substitutedQuery
argument_list|)
expr_stmt|;
name|routePrefetches
argument_list|(
name|router
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates and routes extra disjoint prefetch queries.      *       * @since 1.2      */
name|void
name|routePrefetches
parameter_list|(
name|QueryRouter
name|router
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|)
block|{
operator|new
name|SelectQueryPrefetchRouterAction
argument_list|()
operator|.
name|route
argument_list|(
name|this
argument_list|,
name|router
argument_list|,
name|resolver
argument_list|)
expr_stmt|;
block|}
comment|/**      * Calls "makeSelect" on the visitor.      *       * @since 1.2      */
specifier|public
name|SQLAction
name|createSQLAction
parameter_list|(
name|SQLActionVisitor
name|visitor
parameter_list|)
block|{
return|return
name|visitor
operator|.
name|objectSelectAction
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Initializes query parameters using a set of properties.      *       * @since 1.1      */
specifier|public
name|void
name|initWithProperties
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|properties
parameter_list|)
block|{
comment|// must init defaults even if properties are empty
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
name|properties
operator|=
name|Collections
operator|.
name|EMPTY_MAP
expr_stmt|;
block|}
name|Object
name|distinct
init|=
name|properties
operator|.
name|get
argument_list|(
name|DISTINCT_PROPERTY
argument_list|)
decl_stmt|;
comment|// init ivars from properties
name|this
operator|.
name|distinct
operator|=
operator|(
name|distinct
operator|!=
literal|null
operator|)
condition|?
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|distinct
operator|.
name|toString
argument_list|()
argument_list|)
else|:
name|DISTINCT_DEFAULT
expr_stmt|;
name|selectInfo
operator|.
name|initWithProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
comment|/**      * Prints itself as XML to the provided PrintWriter.      *       * @since 1.1      */
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
literal|"\" factory=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|"org.apache.cayenne.map.SelectQueryBuilder"
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
name|QueryBuilder
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
name|QueryBuilder
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
name|QueryBuilder
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
name|QueryBuilder
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
condition|)
block|{
name|rootType
operator|=
name|QueryBuilder
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
if|if
condition|(
name|distinct
operator|!=
name|DISTINCT_DEFAULT
condition|)
block|{
name|encoder
operator|.
name|printProperty
argument_list|(
name|DISTINCT_PROPERTY
argument_list|,
name|distinct
argument_list|)
expr_stmt|;
block|}
name|selectInfo
operator|.
name|encodeAsXML
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
comment|/**      * A shortcut for {@link #queryWithParameters(Map, boolean)}that prunes parts of      * qualifier that have no parameter value set.      */
specifier|public
name|SelectQuery
name|queryWithParameters
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|parameters
parameter_list|)
block|{
return|return
name|queryWithParameters
argument_list|(
name|parameters
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Returns a query built using this query as a prototype, using a set of parameters to      * build the qualifier.      *       * @see org.apache.cayenne.exp.Expression#expWithParameters(java.util.Map, boolean)      *      parameter substitution.      */
specifier|public
name|SelectQuery
name|queryWithParameters
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|parameters
parameter_list|,
name|boolean
name|pruneMissing
parameter_list|)
block|{
comment|// create a query replica
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|()
decl_stmt|;
name|query
operator|.
name|setDistinct
argument_list|(
name|distinct
argument_list|)
expr_stmt|;
name|query
operator|.
name|selectInfo
operator|.
name|copyFromInfo
argument_list|(
name|this
operator|.
name|selectInfo
argument_list|)
expr_stmt|;
name|query
operator|.
name|setRoot
argument_list|(
name|root
argument_list|)
expr_stmt|;
comment|// The following algorithm is for building the new query name based
comment|// on the original query name and a hashcode of the map of parameters.
comment|// This way the query clone can take advantage of caching. Fixes
comment|// problem reported in CAY-360.
if|if
condition|(
operator|!
name|Util
operator|.
name|isEmptyString
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|parameters
operator|!=
literal|null
operator|&&
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|parameters
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|query
operator|.
name|setName
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|orderings
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|addOrderings
argument_list|(
name|orderings
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|customDbAttributes
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|addCustomDbAttributes
argument_list|(
name|customDbAttributes
argument_list|)
expr_stmt|;
block|}
comment|// substitute qualifier parameters
if|if
condition|(
name|qualifier
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|setQualifier
argument_list|(
name|qualifier
operator|.
name|expWithParameters
argument_list|(
name|parameters
argument_list|,
name|pruneMissing
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|query
return|;
block|}
comment|/**      * Creates and returns a new SelectQuery built using this query as a prototype and      * substituting qualifier parameters with the values from the map.      *       * @since 1.1      */
specifier|public
name|Query
name|createQuery
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|parameters
parameter_list|)
block|{
return|return
name|queryWithParameters
argument_list|(
name|parameters
argument_list|)
return|;
block|}
comment|/**      * Adds ordering specification to this query orderings.      */
specifier|public
name|void
name|addOrdering
parameter_list|(
name|Ordering
name|ordering
parameter_list|)
block|{
name|nonNullOrderings
argument_list|()
operator|.
name|add
argument_list|(
name|ordering
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a list of orderings.      */
specifier|public
name|void
name|addOrderings
parameter_list|(
name|List
argument_list|<
name|Ordering
argument_list|>
name|orderings
parameter_list|)
block|{
name|nonNullOrderings
argument_list|()
operator|.
name|addAll
argument_list|(
name|orderings
argument_list|)
expr_stmt|;
block|}
comment|/** Adds ordering specification to this query orderings. */
specifier|public
name|void
name|addOrdering
parameter_list|(
name|String
name|sortPathSpec
parameter_list|,
name|boolean
name|isAscending
parameter_list|)
block|{
name|addOrdering
argument_list|(
operator|new
name|Ordering
argument_list|(
name|sortPathSpec
argument_list|,
name|isAscending
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** Adds ordering specification to this query orderings. */
specifier|public
name|void
name|addOrdering
parameter_list|(
name|String
name|sortPathSpec
parameter_list|,
name|boolean
name|isAscending
parameter_list|,
name|boolean
name|ignoreCase
parameter_list|)
block|{
name|addOrdering
argument_list|(
operator|new
name|Ordering
argument_list|(
name|sortPathSpec
argument_list|,
name|isAscending
argument_list|,
name|ignoreCase
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes ordering.      *       * @since 1.1      */
specifier|public
name|void
name|removeOrdering
parameter_list|(
name|Ordering
name|ordering
parameter_list|)
block|{
if|if
condition|(
name|orderings
operator|!=
literal|null
condition|)
block|{
name|orderings
operator|.
name|remove
argument_list|(
name|ordering
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns a list of orderings used by this query.      */
specifier|public
name|List
argument_list|<
name|Ordering
argument_list|>
name|getOrderings
parameter_list|()
block|{
return|return
operator|(
name|orderings
operator|!=
literal|null
operator|)
condition|?
name|orderings
else|:
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
comment|/**      * Clears all configured orderings.      */
specifier|public
name|void
name|clearOrderings
parameter_list|()
block|{
name|orderings
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Returns true if this query returns distinct rows.      */
specifier|public
name|boolean
name|isDistinct
parameter_list|()
block|{
return|return
name|distinct
return|;
block|}
comment|/**      * Sets<code>distinct</code> property that determines whether this query returns      * distinct row.      */
specifier|public
name|void
name|setDistinct
parameter_list|(
name|boolean
name|distinct
parameter_list|)
block|{
name|this
operator|.
name|distinct
operator|=
name|distinct
expr_stmt|;
block|}
comment|/**      * Returns a list of attributes that will be included in the results of this query.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getCustomDbAttributes
parameter_list|()
block|{
comment|// if query root is DbEntity, and no custom attributes
comment|// are defined, return DbEntity attributes.
if|if
condition|(
operator|(
name|customDbAttributes
operator|==
literal|null
operator|||
name|customDbAttributes
operator|.
name|isEmpty
argument_list|()
operator|)
operator|&&
operator|(
name|root
operator|instanceof
name|DbEntity
operator|)
condition|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|(
operator|(
name|DbEntity
operator|)
name|root
operator|)
operator|.
name|getAttributeMap
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
return|return
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|names
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|(
name|customDbAttributes
operator|!=
literal|null
operator|)
condition|?
name|customDbAttributes
else|:
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
block|}
comment|/**      * Adds a path to the DbAttribute that should be included in the results of this      * query. Valid paths would look like<code>ARTIST_NAME</code>,      *<code>PAINTING_ARRAY.PAINTING_ID</code>, etc.      */
specifier|public
name|void
name|addCustomDbAttribute
parameter_list|(
name|String
name|attributePath
parameter_list|)
block|{
name|nonNullCustomDbAttributes
argument_list|()
operator|.
name|add
argument_list|(
name|attributePath
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addCustomDbAttributes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|attrPaths
parameter_list|)
block|{
name|nonNullCustomDbAttributes
argument_list|()
operator|.
name|addAll
argument_list|(
name|attrPaths
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if there is at least one custom query attribute      * specified, otherwise returns<code>false</code> for the case when the query      * results will contain only the root entity attributes.      *<p>      * Note that queries that are fetching custom attributes always return data rows      * instead of DataObjects.      *</p>      */
specifier|public
name|boolean
name|isFetchingCustomAttributes
parameter_list|()
block|{
return|return
operator|(
name|root
operator|instanceof
name|DbEntity
operator|)
operator|||
operator|(
name|customDbAttributes
operator|!=
literal|null
operator|&&
operator|!
name|customDbAttributes
operator|.
name|isEmpty
argument_list|()
operator|)
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|PrefetchTreeNode
name|getPrefetchTree
parameter_list|()
block|{
return|return
name|selectInfo
operator|.
name|getPrefetchTree
argument_list|()
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|void
name|setPrefetchTree
parameter_list|(
name|PrefetchTreeNode
name|prefetchTree
parameter_list|)
block|{
name|selectInfo
operator|.
name|setPrefetchTree
argument_list|(
name|prefetchTree
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a prefetch with specified relationship path to the query.      *       * @since 1.2 signature changed to return created PrefetchTreeNode.      */
specifier|public
name|PrefetchTreeNode
name|addPrefetch
parameter_list|(
name|String
name|prefetchPath
parameter_list|)
block|{
return|return
name|selectInfo
operator|.
name|addPrefetch
argument_list|(
name|prefetchPath
argument_list|,
name|PrefetchTreeNode
operator|.
name|UNDEFINED_SEMANTICS
argument_list|)
return|;
block|}
comment|/**      * Clears all stored prefetch paths.      */
specifier|public
name|void
name|clearPrefetches
parameter_list|()
block|{
name|selectInfo
operator|.
name|clearPrefetches
argument_list|()
expr_stmt|;
block|}
comment|/**      * Removes prefetch.      *       * @since 1.1      */
specifier|public
name|void
name|removePrefetch
parameter_list|(
name|String
name|prefetchPath
parameter_list|)
block|{
name|selectInfo
operator|.
name|removePrefetch
argument_list|(
name|prefetchPath
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>true</code> if this query should produce a list of data rows as      * opposed to DataObjects,<code>false</code> for DataObjects. This is a hint to      * QueryEngine executing this query.      */
specifier|public
name|boolean
name|isFetchingDataRows
parameter_list|()
block|{
return|return
name|this
operator|.
name|isFetchingCustomAttributes
argument_list|()
operator|||
name|selectInfo
operator|.
name|isFetchingDataRows
argument_list|()
return|;
block|}
comment|/**      * Sets query result type. If<code>flag</code> parameter is<code>true</code>,      * then results will be in the form of data rows.      *<p>      *<i>Note that if<code>isFetchingCustAttributes()</code> returns<code>true</code>,      * this setting has no effect, and data rows are always fetched.</i>      *</p>      */
specifier|public
name|void
name|setFetchingDataRows
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|selectInfo
operator|.
name|setFetchingDataRows
argument_list|(
name|flag
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns refresh policy of this query. Default is<code>true</code>.      *       * @since 1.1      */
specifier|public
name|boolean
name|isRefreshingObjects
parameter_list|()
block|{
return|return
name|selectInfo
operator|.
name|isRefreshingObjects
argument_list|()
return|;
block|}
comment|/**      * @since 1.1      */
specifier|public
name|void
name|setRefreshingObjects
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|selectInfo
operator|.
name|setRefreshingObjects
argument_list|(
name|flag
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.1      */
specifier|public
name|String
name|getCachePolicy
parameter_list|()
block|{
return|return
name|selectInfo
operator|.
name|getCachePolicy
argument_list|()
return|;
block|}
comment|/**      * @since 1.1      */
specifier|public
name|void
name|setCachePolicy
parameter_list|(
name|String
name|policy
parameter_list|)
block|{
name|this
operator|.
name|selectInfo
operator|.
name|setCachePolicy
argument_list|(
name|policy
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|String
index|[]
name|getCacheGroups
parameter_list|()
block|{
return|return
name|selectInfo
operator|.
name|getCacheGroups
argument_list|()
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|setCacheGroups
parameter_list|(
name|String
index|[]
name|cachGroups
parameter_list|)
block|{
name|this
operator|.
name|selectInfo
operator|.
name|setCacheGroups
argument_list|(
name|cachGroups
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the fetchLimit.      */
specifier|public
name|int
name|getFetchLimit
parameter_list|()
block|{
return|return
name|selectInfo
operator|.
name|getFetchLimit
argument_list|()
return|;
block|}
comment|/**      * Sets the fetchLimit.      */
specifier|public
name|void
name|setFetchLimit
parameter_list|(
name|int
name|fetchLimit
parameter_list|)
block|{
name|this
operator|.
name|selectInfo
operator|.
name|setFetchLimit
argument_list|(
name|fetchLimit
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns<code>pageSize</code> property. Page size is a hint telling Cayenne      * QueryEngine that query result should use paging instead of reading the whole result      * in the memory.      */
specifier|public
name|int
name|getPageSize
parameter_list|()
block|{
return|return
name|selectInfo
operator|.
name|getPageSize
argument_list|()
return|;
block|}
comment|/**      * Sets<code>pageSize</code> property.      *       * @param pageSize The pageSize to set      */
specifier|public
name|void
name|setPageSize
parameter_list|(
name|int
name|pageSize
parameter_list|)
block|{
name|selectInfo
operator|.
name|setPageSize
argument_list|(
name|pageSize
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns true if objects fetched via this query should be fully resolved according      * to the inheritance hierarchy.      *       * @since 1.1      */
specifier|public
name|boolean
name|isResolvingInherited
parameter_list|()
block|{
return|return
name|selectInfo
operator|.
name|isResolvingInherited
argument_list|()
return|;
block|}
comment|/**      * Sets whether the objects fetched via this query should be fully resolved according      * to the inheritance hierarchy.      *       * @since 1.1      */
specifier|public
name|void
name|setResolvingInherited
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|selectInfo
operator|.
name|setResolvingInherited
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a list that internally stores custom db attributes, creating it on demand.      *       * @since 1.2      */
name|List
argument_list|<
name|String
argument_list|>
name|nonNullCustomDbAttributes
parameter_list|()
block|{
if|if
condition|(
name|customDbAttributes
operator|==
literal|null
condition|)
block|{
name|customDbAttributes
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|customDbAttributes
return|;
block|}
comment|/**      * Returns a list that internally stores orderings, creating it on demand.      *       * @since 1.2      */
name|List
argument_list|<
name|Ordering
argument_list|>
name|nonNullOrderings
parameter_list|()
block|{
if|if
condition|(
name|orderings
operator|==
literal|null
condition|)
block|{
name|orderings
operator|=
operator|new
name|ArrayList
argument_list|<
name|Ordering
argument_list|>
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
return|return
name|orderings
return|;
block|}
block|}
end_class

end_unit

