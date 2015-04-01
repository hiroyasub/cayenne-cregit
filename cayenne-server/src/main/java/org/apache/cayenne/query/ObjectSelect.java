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
name|ResultIteratorCallback
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
name|java
operator|.
name|sql
operator|.
name|Statement
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
name|Arrays
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
name|List
import|;
end_import

begin_comment
comment|/**  * A selecting query providing chainable API. Can be viewed as an alternative to  * {@link SelectQuery}.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ObjectSelect
parameter_list|<
name|T
parameter_list|>
extends|extends
name|IndirectQuery
implements|implements
name|Select
argument_list|<
name|T
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|156124021150949227L
decl_stmt|;
specifier|private
name|boolean
name|fetchingDataRows
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|entityType
decl_stmt|;
specifier|private
name|String
name|entityName
decl_stmt|;
specifier|private
name|String
name|dbEntityName
decl_stmt|;
specifier|private
name|Expression
name|where
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|Ordering
argument_list|>
name|orderings
decl_stmt|;
specifier|private
name|PrefetchTreeNode
name|prefetches
decl_stmt|;
specifier|private
name|int
name|limit
decl_stmt|;
specifier|private
name|int
name|offset
decl_stmt|;
specifier|private
name|int
name|pageSize
decl_stmt|;
specifier|private
name|int
name|statementFetchSize
decl_stmt|;
specifier|private
name|QueryCacheStrategy
name|cacheStrategy
decl_stmt|;
specifier|private
name|String
index|[]
name|cacheGroups
decl_stmt|;
comment|/** 	 * Creates a ObjectSelect that selects objects of a given persistent class. 	 */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|query
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|entityType
parameter_list|)
block|{
return|return
operator|new
name|ObjectSelect
argument_list|<
name|T
argument_list|>
argument_list|()
operator|.
name|entityType
argument_list|(
name|entityType
argument_list|)
return|;
block|}
comment|/** 	 * Creates a ObjectSelect that selects objects of a given persistent class 	 * and uses provided expression for its qualifier. 	 */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|query
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|entityType
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
return|return
operator|new
name|ObjectSelect
argument_list|<
name|T
argument_list|>
argument_list|()
operator|.
name|entityType
argument_list|(
name|entityType
argument_list|)
operator|.
name|where
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/** 	 * Creates a ObjectSelect that selects objects of a given persistent class 	 * and uses provided expression for its qualifier. 	 */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|query
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|entityType
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|List
argument_list|<
name|Ordering
argument_list|>
name|orderings
parameter_list|)
block|{
return|return
operator|new
name|ObjectSelect
argument_list|<
name|T
argument_list|>
argument_list|()
operator|.
name|entityType
argument_list|(
name|entityType
argument_list|)
operator|.
name|where
argument_list|(
name|expression
argument_list|)
operator|.
name|orderBy
argument_list|(
name|orderings
argument_list|)
return|;
block|}
comment|/** 	 * Creates a ObjectSelect that fetches data for an {@link ObjEntity} 	 * determined from a provided class. 	 */
specifier|public
specifier|static
name|ObjectSelect
argument_list|<
name|DataRow
argument_list|>
name|dataRowQuery
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityType
parameter_list|)
block|{
return|return
name|query
argument_list|(
name|entityType
argument_list|)
operator|.
name|fetchDataRows
argument_list|()
return|;
block|}
comment|/** 	 * Creates a ObjectSelect that fetches data for an {@link ObjEntity} 	 * determined from a provided class and uses provided expression for its 	 * qualifier. 	 */
specifier|public
specifier|static
name|ObjectSelect
argument_list|<
name|DataRow
argument_list|>
name|dataRowQuery
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityType
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
return|return
name|query
argument_list|(
name|entityType
argument_list|)
operator|.
name|fetchDataRows
argument_list|()
operator|.
name|where
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/** 	 * Creates a ObjectSelect that fetches data for {@link ObjEntity} determined 	 * from provided "entityName", but fetches the result of a provided type. 	 * This factory method is most often used with generic classes that by 	 * themselves are not enough to resolve the entity to fetch. 	 */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|query
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|resultType
parameter_list|,
name|String
name|entityName
parameter_list|)
block|{
return|return
operator|new
name|ObjectSelect
argument_list|<
name|T
argument_list|>
argument_list|()
operator|.
name|entityName
argument_list|(
name|entityName
argument_list|)
return|;
block|}
comment|/** 	 * Creates a ObjectSelect that fetches DataRows for a {@link DbEntity} 	 * determined from provided "dbEntityName". 	 */
specifier|public
specifier|static
name|ObjectSelect
argument_list|<
name|DataRow
argument_list|>
name|dbQuery
parameter_list|(
name|String
name|dbEntityName
parameter_list|)
block|{
return|return
operator|new
name|ObjectSelect
argument_list|<
name|Object
argument_list|>
argument_list|()
operator|.
name|fetchDataRows
argument_list|()
operator|.
name|dbEntityName
argument_list|(
name|dbEntityName
argument_list|)
return|;
block|}
comment|/** 	 * Creates a ObjectSelect that fetches DataRows for a {@link DbEntity} 	 * determined from provided "dbEntityName" and uses provided expression for 	 * its qualifier. 	 *  	 * @return this object 	 */
specifier|public
specifier|static
name|ObjectSelect
argument_list|<
name|DataRow
argument_list|>
name|dbQuery
parameter_list|(
name|String
name|dbEntityName
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
return|return
operator|new
name|ObjectSelect
argument_list|<
name|Object
argument_list|>
argument_list|()
operator|.
name|fetchDataRows
argument_list|()
operator|.
name|dbEntityName
argument_list|(
name|dbEntityName
argument_list|)
operator|.
name|where
argument_list|(
name|expression
argument_list|)
return|;
block|}
specifier|protected
name|ObjectSelect
parameter_list|()
block|{
block|}
comment|/** 	 * Translates self to a SelectQuery. 	 */
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"deprecation"
block|,
literal|"unchecked"
block|}
argument_list|)
annotation|@
name|Override
specifier|protected
name|Query
name|createReplacementQuery
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
name|SelectQuery
name|replacement
init|=
operator|new
name|SelectQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|entityType
operator|!=
literal|null
condition|)
block|{
name|replacement
operator|.
name|setRoot
argument_list|(
name|entityType
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|entityName
operator|!=
literal|null
condition|)
block|{
name|ObjEntity
name|entity
init|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unrecognized ObjEntity name: "
operator|+
name|entityName
argument_list|)
throw|;
block|}
name|replacement
operator|.
name|setRoot
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|dbEntityName
operator|!=
literal|null
condition|)
block|{
name|DbEntity
name|entity
init|=
name|resolver
operator|.
name|getDbEntity
argument_list|(
name|dbEntityName
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unrecognized DbEntity name: "
operator|+
name|dbEntityName
argument_list|)
throw|;
block|}
name|replacement
operator|.
name|setRoot
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Undefined root entity of the query"
argument_list|)
throw|;
block|}
name|replacement
operator|.
name|setFetchingDataRows
argument_list|(
name|fetchingDataRows
argument_list|)
expr_stmt|;
name|replacement
operator|.
name|setQualifier
argument_list|(
name|where
argument_list|)
expr_stmt|;
name|replacement
operator|.
name|addOrderings
argument_list|(
name|orderings
argument_list|)
expr_stmt|;
name|replacement
operator|.
name|setPrefetchTree
argument_list|(
name|prefetches
argument_list|)
expr_stmt|;
name|replacement
operator|.
name|setCacheStrategy
argument_list|(
name|cacheStrategy
argument_list|)
expr_stmt|;
name|replacement
operator|.
name|setCacheGroups
argument_list|(
name|cacheGroups
argument_list|)
expr_stmt|;
name|replacement
operator|.
name|setFetchLimit
argument_list|(
name|limit
argument_list|)
expr_stmt|;
name|replacement
operator|.
name|setFetchOffset
argument_list|(
name|offset
argument_list|)
expr_stmt|;
name|replacement
operator|.
name|setPageSize
argument_list|(
name|pageSize
argument_list|)
expr_stmt|;
name|replacement
operator|.
name|setStatementFetchSize
argument_list|(
name|statementFetchSize
argument_list|)
expr_stmt|;
return|return
name|replacement
return|;
block|}
comment|/** 	 * Sets the type of the entity to fetch without changing the return type of 	 * the query. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|entityType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityType
parameter_list|)
block|{
return|return
name|resetEntity
argument_list|(
name|entityType
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/** 	 * Sets the {@link ObjEntity} name to fetch without changing the return type 	 * of the query. This form is most often used for generic entities that 	 * don't map to a distinct class. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|entityName
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
return|return
name|resetEntity
argument_list|(
literal|null
argument_list|,
name|entityName
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/** 	 * Sets the {@link DbEntity} name to fetch without changing the return type 	 * of the query. This form is most often used for generic entities that 	 * don't map to a distinct class. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|dbEntityName
parameter_list|(
name|String
name|dbEntityName
parameter_list|)
block|{
return|return
name|resetEntity
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
name|dbEntityName
argument_list|)
return|;
block|}
specifier|private
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|resetEntity
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityType
parameter_list|,
name|String
name|entityName
parameter_list|,
name|String
name|dbEntityName
parameter_list|)
block|{
name|this
operator|.
name|entityType
operator|=
name|entityType
expr_stmt|;
name|this
operator|.
name|entityName
operator|=
name|entityName
expr_stmt|;
name|this
operator|.
name|dbEntityName
operator|=
name|dbEntityName
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * Forces query to fetch DataRows. This automatically changes whatever 	 * result type was set previously to "DataRow". 	 *  	 * @return this object 	 */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|ObjectSelect
argument_list|<
name|DataRow
argument_list|>
name|fetchDataRows
parameter_list|()
block|{
name|this
operator|.
name|fetchingDataRows
operator|=
literal|true
expr_stmt|;
return|return
operator|(
name|ObjectSelect
argument_list|<
name|DataRow
argument_list|>
operator|)
name|this
return|;
block|}
comment|/** 	 * Initializes or resets a qualifier expression of this query. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|where
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|where
operator|=
name|expression
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * Initializes or resets a qualifier expression of this query, using 	 * provided expression String and an array of position parameters. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|where
parameter_list|(
name|String
name|expressionString
parameter_list|,
name|Object
modifier|...
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|where
operator|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|expressionString
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * AND's provided expressions to the existing WHERE clause expression. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|and
parameter_list|(
name|Expression
modifier|...
name|expressions
parameter_list|)
block|{
if|if
condition|(
name|expressions
operator|==
literal|null
operator|||
name|expressions
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
return|return
name|and
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|expressions
argument_list|)
argument_list|)
return|;
block|}
comment|/** 	 * AND's provided expressions to the existing WHERE clause expression. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|and
parameter_list|(
name|Collection
argument_list|<
name|Expression
argument_list|>
name|expressions
parameter_list|)
block|{
if|if
condition|(
name|expressions
operator|==
literal|null
operator|||
name|expressions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|this
return|;
block|}
name|Collection
argument_list|<
name|Expression
argument_list|>
name|all
decl_stmt|;
if|if
condition|(
name|where
operator|!=
literal|null
condition|)
block|{
name|all
operator|=
operator|new
name|ArrayList
argument_list|<
name|Expression
argument_list|>
argument_list|(
name|expressions
operator|.
name|size
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|all
operator|.
name|add
argument_list|(
name|where
argument_list|)
expr_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|expressions
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|all
operator|=
name|expressions
expr_stmt|;
block|}
name|where
operator|=
name|ExpressionFactory
operator|.
name|and
argument_list|(
name|all
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * OR's provided expressions to the existing WHERE clause expression. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|or
parameter_list|(
name|Expression
modifier|...
name|expressions
parameter_list|)
block|{
if|if
condition|(
name|expressions
operator|==
literal|null
operator|||
name|expressions
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|this
return|;
block|}
return|return
name|or
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|expressions
argument_list|)
argument_list|)
return|;
block|}
comment|/** 	 * OR's provided expressions to the existing WHERE clause expression. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|or
parameter_list|(
name|Collection
argument_list|<
name|Expression
argument_list|>
name|expressions
parameter_list|)
block|{
if|if
condition|(
name|expressions
operator|==
literal|null
operator|||
name|expressions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|this
return|;
block|}
name|Collection
argument_list|<
name|Expression
argument_list|>
name|all
decl_stmt|;
if|if
condition|(
name|where
operator|!=
literal|null
condition|)
block|{
name|all
operator|=
operator|new
name|ArrayList
argument_list|<
name|Expression
argument_list|>
argument_list|(
name|expressions
operator|.
name|size
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|all
operator|.
name|add
argument_list|(
name|where
argument_list|)
expr_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|expressions
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|all
operator|=
name|expressions
expr_stmt|;
block|}
name|where
operator|=
name|ExpressionFactory
operator|.
name|or
argument_list|(
name|all
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * Initializes ordering clause of this query with a single ascending 	 * ordering on a given property. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|orderBy
parameter_list|(
name|String
name|property
parameter_list|)
block|{
return|return
name|orderBy
argument_list|(
operator|new
name|Ordering
argument_list|(
name|property
argument_list|)
argument_list|)
return|;
block|}
comment|/** 	 * Initializes ordering clause of this query with a single ordering on a 	 * given property. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|orderBy
parameter_list|(
name|String
name|property
parameter_list|,
name|SortOrder
name|sortOrder
parameter_list|)
block|{
return|return
name|orderBy
argument_list|(
operator|new
name|Ordering
argument_list|(
name|property
argument_list|,
name|sortOrder
argument_list|)
argument_list|)
return|;
block|}
comment|/** 	 * Initializes or resets a list of orderings of this query. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|orderBy
parameter_list|(
name|Ordering
modifier|...
name|orderings
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|orderings
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|orderings
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
return|return
name|addOrderBy
argument_list|(
name|orderings
argument_list|)
return|;
block|}
comment|/** 	 * Initializes or resets a list of orderings of this query. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|orderBy
parameter_list|(
name|Collection
argument_list|<
name|Ordering
argument_list|>
name|orderings
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|orderings
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|orderings
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
return|return
name|addOrderBy
argument_list|(
name|orderings
argument_list|)
return|;
block|}
comment|/** 	 * Adds a single ascending ordering on a given property to the existing 	 * ordering clause of this query. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|addOrderBy
parameter_list|(
name|String
name|property
parameter_list|)
block|{
return|return
name|addOrderBy
argument_list|(
operator|new
name|Ordering
argument_list|(
name|property
argument_list|)
argument_list|)
return|;
block|}
comment|/** 	 * Adds a single ordering on a given property to the existing ordering 	 * clause of this query. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|addOrderBy
parameter_list|(
name|String
name|property
parameter_list|,
name|SortOrder
name|sortOrder
parameter_list|)
block|{
return|return
name|addOrderBy
argument_list|(
operator|new
name|Ordering
argument_list|(
name|property
argument_list|,
name|sortOrder
argument_list|)
argument_list|)
return|;
block|}
comment|/** 	 * Adds new orderings to the list of the existing orderings. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|addOrderBy
parameter_list|(
name|Ordering
modifier|...
name|orderings
parameter_list|)
block|{
if|if
condition|(
name|orderings
operator|==
literal|null
operator|||
name|orderings
operator|==
literal|null
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|this
operator|.
name|orderings
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|orderings
operator|=
operator|new
name|ArrayList
argument_list|<
name|Ordering
argument_list|>
argument_list|(
name|orderings
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Ordering
name|o
range|:
name|orderings
control|)
block|{
name|this
operator|.
name|orderings
operator|.
name|add
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/** 	 * Adds new orderings to the list of orderings. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|addOrderBy
parameter_list|(
name|Collection
argument_list|<
name|Ordering
argument_list|>
name|orderings
parameter_list|)
block|{
if|if
condition|(
name|orderings
operator|==
literal|null
operator|||
name|orderings
operator|==
literal|null
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|this
operator|.
name|orderings
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|orderings
operator|=
operator|new
name|ArrayList
argument_list|<
name|Ordering
argument_list|>
argument_list|(
name|orderings
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|orderings
operator|.
name|addAll
argument_list|(
name|orderings
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * Resets internal prefetches to the new value, which is a single prefetch 	 * with specified semantics. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|prefetch
parameter_list|(
name|String
name|path
parameter_list|,
name|int
name|semantics
parameter_list|)
block|{
name|this
operator|.
name|prefetches
operator|=
name|PrefetchTreeNode
operator|.
name|withPath
argument_list|(
name|path
argument_list|,
name|semantics
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * Resets internal prefetches to the new value. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|prefetch
parameter_list|(
name|PrefetchTreeNode
name|prefetch
parameter_list|)
block|{
name|this
operator|.
name|prefetches
operator|=
name|prefetch
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * Merges prefetch into the query prefetch tree. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|addPrefetch
parameter_list|(
name|PrefetchTreeNode
name|prefetch
parameter_list|)
block|{
if|if
condition|(
name|prefetch
operator|==
literal|null
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|prefetches
operator|==
literal|null
condition|)
block|{
name|prefetches
operator|=
operator|new
name|PrefetchTreeNode
argument_list|()
expr_stmt|;
block|}
name|prefetches
operator|.
name|merge
argument_list|(
name|prefetch
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * Merges a prefetch path with specified semantics into the query prefetch 	 * tree. 	 *  	 * @return this object 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|addPrefetch
parameter_list|(
name|String
name|path
parameter_list|,
name|int
name|semantics
parameter_list|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|prefetches
operator|==
literal|null
condition|)
block|{
name|prefetches
operator|=
operator|new
name|PrefetchTreeNode
argument_list|()
expr_stmt|;
block|}
name|prefetches
operator|.
name|addPath
argument_list|(
name|path
argument_list|)
operator|.
name|setSemantics
argument_list|(
name|semantics
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** 	 * Resets query fetch limit - a parameter that defines max number of objects 	 * that should be ever be fetched from the database. 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|limit
parameter_list|(
name|int
name|fetchLimit
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|limit
operator|!=
name|fetchLimit
condition|)
block|{
name|this
operator|.
name|limit
operator|=
name|fetchLimit
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/** 	 * Resets query fetch offset - a parameter that defines how many objects 	 * should be skipped when reading data from the database. 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|offset
parameter_list|(
name|int
name|fetchOffset
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|offset
operator|!=
name|fetchOffset
condition|)
block|{
name|this
operator|.
name|offset
operator|=
name|fetchOffset
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/** 	 * Resets query page size. A non-negative page size enables query result 	 * pagination that saves memory and processing time for large lists if only 	 * parts of the result are ever going to be accessed. 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|pageSize
parameter_list|(
name|int
name|pageSize
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|pageSize
operator|!=
name|pageSize
condition|)
block|{
name|this
operator|.
name|pageSize
operator|=
name|pageSize
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/** 	 * Sets fetch size of the PreparedStatement generated for this query. Only 	 * non-negative values would change the default size. 	 *  	 * @see Statement#setFetchSize(int) 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|statementFetchSize
parameter_list|(
name|int
name|size
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|statementFetchSize
operator|!=
name|size
condition|)
block|{
name|this
operator|.
name|statementFetchSize
operator|=
name|size
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|cacheStrategy
parameter_list|(
name|QueryCacheStrategy
name|strategy
parameter_list|,
name|String
modifier|...
name|cacheGroups
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|cacheStrategy
operator|!=
name|strategy
condition|)
block|{
name|this
operator|.
name|cacheStrategy
operator|=
name|strategy
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|cacheGroups
argument_list|(
name|cacheGroups
argument_list|)
return|;
block|}
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|cacheGroups
parameter_list|(
name|String
modifier|...
name|cacheGroups
parameter_list|)
block|{
name|this
operator|.
name|cacheGroups
operator|=
name|cacheGroups
operator|!=
literal|null
operator|&&
name|cacheGroups
operator|.
name|length
operator|>
literal|0
condition|?
name|cacheGroups
else|:
literal|null
expr_stmt|;
name|this
operator|.
name|replacementQuery
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|cacheGroups
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|cacheGroups
parameter_list|)
block|{
if|if
condition|(
name|cacheGroups
operator|==
literal|null
condition|)
block|{
return|return
name|cacheGroups
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|)
return|;
block|}
name|String
index|[]
name|array
init|=
operator|new
name|String
index|[
name|cacheGroups
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
return|return
name|cacheGroups
argument_list|(
name|cacheGroups
operator|.
name|toArray
argument_list|(
name|array
argument_list|)
argument_list|)
return|;
block|}
comment|/** 	 * Instructs Cayenne to look for query results in the "local" cache when 	 * running the query. This is a short-hand notation for: 	 *  	 *<pre> 	 * query.cacheStrategy(QueryCacheStrategy.LOCAL_CACHE, cacheGroups); 	 *</pre> 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|localCache
parameter_list|(
name|String
modifier|...
name|cacheGroups
parameter_list|)
block|{
return|return
name|cacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
argument_list|,
name|cacheGroups
argument_list|)
return|;
block|}
comment|/** 	 * Instructs Cayenne to look for query results in the "shared" cache when 	 * running the query. This is a short-hand notation for: 	 *  	 *<pre> 	 * query.cacheStrategy(QueryCacheStrategy.SHARED_CACHE, cacheGroups); 	 *</pre> 	 */
specifier|public
name|ObjectSelect
argument_list|<
name|T
argument_list|>
name|sharedCache
parameter_list|(
name|String
modifier|...
name|cacheGroups
parameter_list|)
block|{
return|return
name|cacheStrategy
argument_list|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
argument_list|,
name|cacheGroups
argument_list|)
return|;
block|}
specifier|public
name|String
index|[]
name|getCacheGroups
parameter_list|()
block|{
return|return
name|cacheGroups
return|;
block|}
specifier|public
name|QueryCacheStrategy
name|getCacheStrategy
parameter_list|()
block|{
return|return
name|cacheStrategy
return|;
block|}
specifier|public
name|int
name|getStatementFetchSize
parameter_list|()
block|{
return|return
name|statementFetchSize
return|;
block|}
specifier|public
name|int
name|getPageSize
parameter_list|()
block|{
return|return
name|pageSize
return|;
block|}
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
specifier|public
name|int
name|getOffset
parameter_list|()
block|{
return|return
name|offset
return|;
block|}
specifier|public
name|boolean
name|isFetchingDataRows
parameter_list|()
block|{
return|return
name|fetchingDataRows
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getEntityType
parameter_list|()
block|{
return|return
name|entityType
return|;
block|}
specifier|public
name|String
name|getEntityName
parameter_list|()
block|{
return|return
name|entityName
return|;
block|}
specifier|public
name|String
name|getDbEntityName
parameter_list|()
block|{
return|return
name|dbEntityName
return|;
block|}
comment|/** 	 * Returns a WHERE clause Expression of this query. 	 */
specifier|public
name|Expression
name|getWhere
parameter_list|()
block|{
return|return
name|where
return|;
block|}
specifier|public
name|Collection
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
specifier|public
name|PrefetchTreeNode
name|getPrefetches
parameter_list|()
block|{
return|return
name|prefetches
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|select
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
return|return
name|context
operator|.
name|select
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|T
name|selectOne
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
return|return
name|context
operator|.
name|selectOne
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|iterate
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|ResultIteratorCallback
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|)
block|{
name|context
operator|.
name|iterate
argument_list|(
operator|(
name|Select
argument_list|<
name|T
argument_list|>
operator|)
name|this
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ResultIterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
return|return
name|context
operator|.
name|iterator
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/** 	 * Selects a single object using provided context. The query itself can 	 * match any number of objects, but will return only the first one. It 	 * returns null if no objects were matched. 	 *<p> 	 * If it matched more than one object, the first object from the list is 	 * returned. This makes 'selectFirst' different from 	 * {@link #selectOne(ObjectContext)}, which would throw in this situation. 	 * 'selectFirst' is useful e.g. when the query is ordered and we only want 	 * to see the first object (e.g. "most recent news article"), etc. 	 *<p> 	 * This method is equivalent to calling "limit(1).selectOne(context)". 	 */
specifier|public
name|T
name|selectFirst
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
return|return
name|limit
argument_list|(
literal|1
argument_list|)
operator|.
name|selectOne
argument_list|(
name|context
argument_list|)
return|;
block|}
block|}
end_class

end_unit

