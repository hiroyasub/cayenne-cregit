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
name|Property
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
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * A selecting query providing chainable API. This is an alternative to  * {@link SelectQuery} when you want to use a fluent API. For example, the following  * is a convenient way to return a record:  *<pre>  * {@code  * Artist a = ObjectSelect  * .query(Artist.class)  * .where(Artist.NAME.eq("Picasso"))  * .selectOne(context);  * }  *</pre>  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|ObjectSelect
parameter_list|<
name|T
parameter_list|>
extends|extends
name|FluentSelect
argument_list|<
name|T
argument_list|,
name|ObjectSelect
argument_list|<
name|T
argument_list|>
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
specifier|protected
name|boolean
name|fetchingDataRows
decl_stmt|;
comment|/**      * Creates a ObjectSelect that selects objects of a given persistent class.      */
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
comment|/**      * Creates a ObjectSelect that selects objects of a given persistent class      * and uses provided expression for its qualifier.      */
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
comment|/**      * Creates a ObjectSelect that selects objects of a given persistent class      * and uses provided expression for its qualifier.      */
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
comment|/**      * Creates a ObjectSelect that fetches data for an {@link ObjEntity}      * determined from a provided class.      */
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
comment|/**      * Creates a ObjectSelect that fetches data for an {@link ObjEntity}      * determined from a provided class and uses provided expression for its      * qualifier.      */
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
comment|/**      * Creates a ObjectSelect that fetches data for {@link ObjEntity} determined      * from provided "entityName", but fetches the result of a provided type.      * This factory method is most often used with generic classes that by      * themselves are not enough to resolve the entity to fetch.      */
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
comment|/**      * Creates a ObjectSelect that fetches DataRows for a {@link DbEntity}      * determined from provided "dbEntityName".      */
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
name|DataRow
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
comment|/**      * Creates a ObjectSelect that fetches DataRows for a {@link DbEntity}      * determined from provided "dbEntityName" and uses provided expression for      * its qualifier.      *      * @return this object      */
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
name|DataRow
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
comment|/**      * Translates self to a SelectQuery.      */
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
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|replacement
init|=
operator|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
operator|)
name|super
operator|.
name|createReplacementQuery
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|replacement
operator|.
name|setFetchingDataRows
argument_list|(
name|fetchingDataRows
argument_list|)
expr_stmt|;
return|return
name|replacement
return|;
block|}
comment|/**      * Forces query to fetch DataRows. This automatically changes whatever      * result type was set previously to "DataRow".      *      * @return this object      */
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
comment|/**      *<p>Select only specific properties.</p>      *<p>Can be any properties that can be resolved against root entity type      * (root entity properties, function call expressions, properties of relationships, etc).</p>      *<p>      *<pre>      * List&lt;Object[]&gt; columns = ColumnSelect.query(Artist.class)      *                                    .columns(Artist.ARTIST_NAME, Artist.DATE_OF_BIRTH)      *                                    .select(context);      *</pre>      *      * @param properties array of properties to select      * @see ColumnSelect#column(Property)      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|ColumnSelect
argument_list|<
name|Object
index|[]
argument_list|>
name|columns
parameter_list|(
name|Property
argument_list|<
name|?
argument_list|>
modifier|...
name|properties
parameter_list|)
block|{
return|return
operator|new
name|ColumnSelect
argument_list|<>
argument_list|(
name|this
argument_list|)
operator|.
name|columns
argument_list|(
name|properties
argument_list|)
return|;
block|}
comment|/**      *<p>Select one specific property.</p>      *<p>Can be any property that can be resolved against root entity type      * (root entity property, function call expression, property of relationships, etc)</p>      *<p>If you need several columns use {@link ColumnSelect#columns(Property[])} method as subsequent      * call to this method will override previous columns set via this or      * {@link ColumnSelect#columns(Property[])} method.</p>      *<p>      *<pre>      * List&lt;String&gt; names = ObjectSelect.query(Artist.class).column(Artist.ARTIST_NAME).select(context);      *</pre>      *      * @param property single property to select      * @see ColumnSelect#columns(Property[])      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
parameter_list|<
name|E
parameter_list|>
name|ColumnSelect
argument_list|<
name|E
argument_list|>
name|column
parameter_list|(
name|Property
argument_list|<
name|E
argument_list|>
name|property
parameter_list|)
block|{
return|return
operator|new
name|ColumnSelect
argument_list|<>
argument_list|(
name|this
argument_list|)
operator|.
name|column
argument_list|(
name|property
argument_list|)
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
block|}
end_class

end_unit

