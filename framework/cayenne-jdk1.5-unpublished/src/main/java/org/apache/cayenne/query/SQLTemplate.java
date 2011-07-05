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
name|MapLoader
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
name|SQLResult
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
name|IteratorUtils
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
name|Transformer
import|;
end_import

begin_comment
comment|/**  * A query that executes unchanged (except for template preprocessing) "raw" SQL specified  * by the user.<h3>Template Script</h3>  *<p>  * SQLTemplate stores a dynamic template for the SQL query that supports parameters and  * customization using Velocity scripting language. The most straightforward use of  * scripting abilities is to build parameterized queries. For example:  *</p>  *   *<pre>  *                  SELECT ID, NAME FROM SOME_TABLE WHERE NAME LIKE $a  *</pre>  *<p>  *<i>For advanced scripting options see "Scripting SQLTemplate" chapter in the User  * Guide.</i>  *</p>  *<h3>Per-Database Template Customization</h3>  *<p>  * SQLTemplate has a {@link #getDefaultTemplate() default template script}, but also it  * allows to configure multiple templates and switch them dynamically. This way a single  * query can have multiple "dialects" specific to a given database.  *</p>  *<h3>Parameter Sets</h3>  *<p>  * SQLTemplate supports multiple sets of parameters, so a single query can be executed  * multiple times with different parameters. "Scrolling" through parameter list is done by  * calling {@link #parametersIterator()}. This iterator goes over parameter sets,  * returning a Map on each call to "next()"  *</p>  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|SQLTemplate
extends|extends
name|AbstractQuery
implements|implements
name|ParameterizedQuery
implements|,
name|XMLSerializable
block|{
specifier|static
specifier|final
name|String
name|COLUMN_NAME_CAPITALIZATION_PROPERTY
init|=
literal|"cayenne.SQLTemplate.columnNameCapitalization"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Transformer
name|nullMapTransformer
init|=
operator|new
name|Transformer
argument_list|()
block|{
specifier|public
name|Object
name|transform
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
return|return
operator|(
name|input
operator|!=
literal|null
operator|)
condition|?
name|input
else|:
name|Collections
operator|.
name|EMPTY_MAP
return|;
block|}
block|}
decl_stmt|;
specifier|protected
name|String
name|defaultTemplate
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|templates
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
index|[]
name|parameters
decl_stmt|;
specifier|protected
name|CapsStrategy
name|columnNamesCapitalization
decl_stmt|;
specifier|protected
name|SQLResult
name|result
decl_stmt|;
name|SQLTemplateMetadata
name|metaData
init|=
operator|new
name|SQLTemplateMetadata
argument_list|()
decl_stmt|;
comment|/**      * Creates an empty SQLTemplate. Note this constructor does not specify the "root" of      * the query, so a user must call "setRoot" later to make sure SQLTemplate can be      * executed.      *       * @since 1.2      */
specifier|public
name|SQLTemplate
parameter_list|()
block|{
block|}
comment|/**      * @since 1.2      * @deprecated since 3.1, use SQLTemplate(DataMap rootMap, String defaultTemplate,      *             boolean isFetchingDataRows) instead      */
annotation|@
name|Deprecated
specifier|public
name|SQLTemplate
parameter_list|(
name|DataMap
name|rootMap
parameter_list|,
name|String
name|defaultTemplate
parameter_list|)
block|{
name|this
argument_list|(
name|rootMap
argument_list|,
name|defaultTemplate
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.1      */
specifier|public
name|SQLTemplate
parameter_list|(
name|DataMap
name|rootMap
parameter_list|,
name|String
name|defaultTemplate
parameter_list|,
name|boolean
name|isFetchingDataRows
parameter_list|)
block|{
name|setDefaultTemplate
argument_list|(
name|defaultTemplate
argument_list|)
expr_stmt|;
name|setRoot
argument_list|(
name|rootMap
argument_list|)
expr_stmt|;
name|setFetchingDataRows
argument_list|(
name|isFetchingDataRows
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|SQLTemplate
parameter_list|(
name|ObjEntity
name|rootEntity
parameter_list|,
name|String
name|defaultTemplate
parameter_list|)
block|{
name|setDefaultTemplate
argument_list|(
name|defaultTemplate
argument_list|)
expr_stmt|;
name|setRoot
argument_list|(
name|rootEntity
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|SQLTemplate
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|rootClass
parameter_list|,
name|String
name|defaultTemplate
parameter_list|)
block|{
name|setDefaultTemplate
argument_list|(
name|defaultTemplate
argument_list|)
expr_stmt|;
name|setRoot
argument_list|(
name|rootClass
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|SQLTemplate
parameter_list|(
name|DbEntity
name|rootEntity
parameter_list|,
name|String
name|defaultTemplate
parameter_list|)
block|{
name|setDefaultTemplate
argument_list|(
name|defaultTemplate
argument_list|)
expr_stmt|;
name|setRoot
argument_list|(
name|rootEntity
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|SQLTemplate
parameter_list|(
name|String
name|objEntityName
parameter_list|,
name|String
name|defaultTemplate
parameter_list|)
block|{
name|setRoot
argument_list|(
name|objEntityName
argument_list|)
expr_stmt|;
name|setDefaultTemplate
argument_list|(
name|defaultTemplate
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
annotation|@
name|Override
specifier|public
name|QueryMetadata
name|getMetaData
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|metaData
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
return|return
name|metaData
return|;
block|}
comment|/**      * Calls<em>sqlAction(this)</em> on the visitor.      *       * @since 1.2      */
annotation|@
name|Override
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
name|sqlAction
argument_list|(
name|this
argument_list|)
return|;
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
literal|"org.apache.cayenne.map.SQLTemplateBuilder"
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
if|else if
condition|(
name|root
operator|instanceof
name|DataMap
condition|)
block|{
name|rootType
operator|=
name|MapLoader
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
name|metaData
operator|.
name|encodeAsXML
argument_list|(
name|encoder
argument_list|)
expr_stmt|;
if|if
condition|(
name|getColumnNamesCapitalization
argument_list|()
operator|!=
name|CapsStrategy
operator|.
name|DEFAULT
condition|)
block|{
name|encoder
operator|.
name|printProperty
argument_list|(
name|COLUMN_NAME_CAPITALIZATION_PROPERTY
argument_list|,
name|getColumnNamesCapitalization
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// encode default SQL
if|if
condition|(
name|defaultTemplate
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<sql><![CDATA["
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|defaultTemplate
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"]]></sql>"
argument_list|)
expr_stmt|;
block|}
comment|// encode adapter SQL
if|if
condition|(
name|templates
operator|!=
literal|null
operator|&&
operator|!
name|templates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|//sorting entries by adapter name
name|TreeSet
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|templates
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
name|templates
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
name|print
argument_list|(
literal|"<sql adapter-class=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|"\"><![CDATA["
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|sql
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"]]></sql>"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// TODO: support parameter encoding
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
comment|/**      * Initializes query parameters using a set of properties.      *       * @since 1.1      */
specifier|public
name|void
name|initWithProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|properties
parameter_list|)
block|{
comment|// must init defaults even if properties are empty
name|metaData
operator|.
name|initWithProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
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
name|columnNamesCapitalization
init|=
name|properties
operator|.
name|get
argument_list|(
name|COLUMN_NAME_CAPITALIZATION_PROPERTY
argument_list|)
decl_stmt|;
name|this
operator|.
name|columnNamesCapitalization
operator|=
operator|(
name|columnNamesCapitalization
operator|!=
literal|null
operator|)
condition|?
name|CapsStrategy
operator|.
name|valueOf
argument_list|(
name|columnNamesCapitalization
operator|.
name|toString
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
comment|/**      * Returns an iterator over parameter sets. Each element returned from the iterator is      * a java.util.Map.      */
specifier|public
name|Iterator
argument_list|<
name|?
argument_list|>
name|parametersIterator
parameter_list|()
block|{
return|return
operator|(
name|parameters
operator|==
literal|null
operator|||
name|parameters
operator|.
name|length
operator|==
literal|0
operator|)
condition|?
name|IteratorUtils
operator|.
name|emptyIterator
argument_list|()
else|:
name|IteratorUtils
operator|.
name|transformedIterator
argument_list|(
name|IteratorUtils
operator|.
name|arrayIterator
argument_list|(
name|parameters
argument_list|)
argument_list|,
name|nullMapTransformer
argument_list|)
return|;
block|}
comment|/**      * Returns the number of parameter sets.      */
specifier|public
name|int
name|parametersSize
parameter_list|()
block|{
return|return
operator|(
name|parameters
operator|!=
literal|null
operator|)
condition|?
name|parameters
operator|.
name|length
else|:
literal|0
return|;
block|}
comment|/**      * Returns a new query built using this query as a prototype and a new set of      * parameters.      */
specifier|public
name|SQLTemplate
name|queryWithParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
modifier|...
name|parameters
parameter_list|)
block|{
comment|// create a query replica
name|SQLTemplate
name|query
init|=
operator|new
name|SQLTemplate
argument_list|()
decl_stmt|;
name|query
operator|.
name|setRoot
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|query
operator|.
name|setDefaultTemplate
argument_list|(
name|getDefaultTemplate
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|templates
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|templates
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|templates
argument_list|)
expr_stmt|;
block|}
name|query
operator|.
name|metaData
operator|.
name|copyFromInfo
argument_list|(
name|this
operator|.
name|metaData
argument_list|)
expr_stmt|;
name|query
operator|.
name|setParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
name|query
operator|.
name|setColumnNamesCapitalization
argument_list|(
name|this
operator|.
name|getColumnNamesCapitalization
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|query
return|;
block|}
comment|/**      * Creates and returns a new SQLTemplate built using this query as a prototype and      * substituting template parameters with the values from the map.      *       * @since 1.1      */
specifier|public
name|Query
name|createQuery
parameter_list|(
name|Map
argument_list|<
name|String
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
comment|/**      * @since 3.0      */
specifier|public
name|QueryCacheStrategy
name|getCacheStrategy
parameter_list|()
block|{
return|return
name|metaData
operator|.
name|getCacheStrategy
argument_list|()
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|setCacheStrategy
parameter_list|(
name|QueryCacheStrategy
name|strategy
parameter_list|)
block|{
name|metaData
operator|.
name|setCacheStrategy
argument_list|(
name|strategy
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
name|metaData
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
modifier|...
name|cacheGroups
parameter_list|)
block|{
name|this
operator|.
name|metaData
operator|.
name|setCacheGroups
argument_list|(
name|cacheGroups
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getFetchLimit
parameter_list|()
block|{
return|return
name|metaData
operator|.
name|getFetchLimit
argument_list|()
return|;
block|}
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
name|metaData
operator|.
name|setFetchLimit
argument_list|(
name|fetchLimit
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|int
name|getFetchOffset
parameter_list|()
block|{
return|return
name|metaData
operator|.
name|getFetchOffset
argument_list|()
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|void
name|setFetchOffset
parameter_list|(
name|int
name|fetchOffset
parameter_list|)
block|{
name|metaData
operator|.
name|setFetchOffset
argument_list|(
name|fetchOffset
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getPageSize
parameter_list|()
block|{
return|return
name|metaData
operator|.
name|getPageSize
argument_list|()
return|;
block|}
specifier|public
name|void
name|setPageSize
parameter_list|(
name|int
name|pageSize
parameter_list|)
block|{
name|metaData
operator|.
name|setPageSize
argument_list|(
name|pageSize
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setFetchingDataRows
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|metaData
operator|.
name|setFetchingDataRows
argument_list|(
name|flag
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isFetchingDataRows
parameter_list|()
block|{
return|return
name|metaData
operator|.
name|isFetchingDataRows
argument_list|()
return|;
block|}
comment|/**      * Returns default SQL template for this query.      */
specifier|public
name|String
name|getDefaultTemplate
parameter_list|()
block|{
return|return
name|defaultTemplate
return|;
block|}
comment|/**      * Sets default SQL template for this query.      */
specifier|public
name|void
name|setDefaultTemplate
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|defaultTemplate
operator|=
name|string
expr_stmt|;
block|}
comment|/**      * Returns a template for key, or a default template if a template for key is not      * found.      */
specifier|public
specifier|synchronized
name|String
name|getTemplate
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|templates
operator|==
literal|null
condition|)
block|{
return|return
name|defaultTemplate
return|;
block|}
name|String
name|template
init|=
name|templates
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|template
operator|!=
literal|null
operator|)
condition|?
name|template
else|:
name|defaultTemplate
return|;
block|}
comment|/**      * Returns template for key, or null if there is no template configured for this key.      * Unlike {@link #getTemplate(String)}this method does not return a default template      * as a failover strategy, rather it returns null.      */
specifier|public
specifier|synchronized
name|String
name|getCustomTemplate
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
operator|(
name|templates
operator|!=
literal|null
operator|)
condition|?
name|templates
operator|.
name|get
argument_list|(
name|key
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**      * Adds a SQL template string for a given key. Note the the keys understood by Cayenne      * must be fully qualified adapter class names. This way the framework can related      * current DataNode to the right template. E.g.      * "org.apache.cayenne.dba.oracle.OracleAdapter" is a key that should be used to setup      * an Oracle-specific template.      *       * @see #setDefaultTemplate(String)      */
specifier|public
specifier|synchronized
name|void
name|setTemplate
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|template
parameter_list|)
block|{
if|if
condition|(
name|templates
operator|==
literal|null
condition|)
block|{
name|templates
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|templates
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|template
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|removeTemplate
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|templates
operator|!=
literal|null
condition|)
block|{
name|templates
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns a collection of configured template keys.      */
specifier|public
specifier|synchronized
name|Collection
argument_list|<
name|String
argument_list|>
name|getTemplateKeys
parameter_list|()
block|{
return|return
operator|(
name|templates
operator|!=
literal|null
operator|)
condition|?
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|templates
operator|.
name|keySet
argument_list|()
argument_list|)
else|:
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
comment|/**      * Utility method to get the first set of parameters, since most queries will only      * have one.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|getParameters
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|map
init|=
operator|(
name|parameters
operator|!=
literal|null
operator|&&
name|parameters
operator|.
name|length
operator|>
literal|0
operator|)
condition|?
name|parameters
index|[
literal|0
index|]
else|:
literal|null
decl_stmt|;
return|return
operator|(
name|map
operator|!=
literal|null
operator|)
condition|?
name|map
else|:
name|Collections
operator|.
name|EMPTY_MAP
return|;
block|}
comment|/**      * Utility method to initialize query with one or more sets of parameters.      */
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
modifier|...
name|parameters
parameter_list|)
block|{
if|if
condition|(
name|parameters
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|parameters
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
comment|// clone parameters to ensure that we don't have immutable maps that are not
comment|// serializable with Hessian...
name|this
operator|.
name|parameters
operator|=
operator|new
name|Map
index|[
name|parameters
operator|.
name|length
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|parameters
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|this
operator|.
name|parameters
index|[
name|i
index|]
operator|=
name|parameters
index|[
name|i
index|]
operator|!=
literal|null
condition|?
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|parameters
index|[
name|i
index|]
argument_list|)
else|:
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @since 1.2      */
specifier|public
name|PrefetchTreeNode
name|getPrefetchTree
parameter_list|()
block|{
return|return
name|metaData
operator|.
name|getPrefetchTree
argument_list|()
return|;
block|}
comment|/**      * Adds a prefetch.      *       * @since 1.2      */
specifier|public
name|PrefetchTreeNode
name|addPrefetch
parameter_list|(
name|String
name|prefetchPath
parameter_list|)
block|{
comment|// by default use JOINT_PREFETCH_SEMANTICS
return|return
name|metaData
operator|.
name|addPrefetch
argument_list|(
name|prefetchPath
argument_list|,
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|void
name|removePrefetch
parameter_list|(
name|String
name|prefetch
parameter_list|)
block|{
name|metaData
operator|.
name|removePrefetch
argument_list|(
name|prefetch
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds all prefetches from a provided collection.      *       * @since 1.2      */
specifier|public
name|void
name|addPrefetches
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|prefetches
parameter_list|)
block|{
name|metaData
operator|.
name|addPrefetches
argument_list|(
name|prefetches
argument_list|,
name|PrefetchTreeNode
operator|.
name|JOINT_PREFETCH_SEMANTICS
argument_list|)
expr_stmt|;
block|}
comment|/**      * Clears all prefetches.      *       * @since 1.2      */
specifier|public
name|void
name|clearPrefetches
parameter_list|()
block|{
name|metaData
operator|.
name|clearPrefetches
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns a column name capitalization policy applied to selecting queries. This is      * used to simplify mapping of the queries like "SELECT * FROM ...", ensuring that a      * chosen Cayenne column mapping strategy (e.g. all column names in uppercase) is      * portable across database engines that can have varying default capitalization.      * Default (null) value indicates that column names provided in result set are used      * unchanged.      *       * @since 3.0      */
specifier|public
name|CapsStrategy
name|getColumnNamesCapitalization
parameter_list|()
block|{
return|return
name|columnNamesCapitalization
operator|!=
literal|null
condition|?
name|columnNamesCapitalization
else|:
name|CapsStrategy
operator|.
name|DEFAULT
return|;
block|}
comment|/**      * Sets a column name capitalization policy applied to selecting queries. This is used      * to simplify mapping of the queries like "SELECT * FROM ...", ensuring that a chosen      * Cayenne column mapping strategy (e.g. all column names in uppercase) is portable      * across database engines that can have varying default capitalization. Default      * (null) value indicates that column names provided in result set are used unchanged.      *<p/>      * Note that while a non-default setting is useful for queries that do not rely on a      * #result directive to describe columns, it works for all SQLTemplates the same way.      *       * @since 3.0      */
specifier|public
name|void
name|setColumnNamesCapitalization
parameter_list|(
name|CapsStrategy
name|columnNameCapitalization
parameter_list|)
block|{
name|this
operator|.
name|columnNamesCapitalization
operator|=
name|columnNameCapitalization
expr_stmt|;
block|}
comment|/**      * Sets an optional explicit mapping of the result set. If result set mapping is      * specified, the result of SQLTemplate may not be a normal list of Persistent objects      * or DataRows, instead it will follow the {@link SQLResult} rules.      *       * @since 3.0      */
specifier|public
name|void
name|setResult
parameter_list|(
name|SQLResult
name|resultSet
parameter_list|)
block|{
name|this
operator|.
name|result
operator|=
name|resultSet
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|SQLResult
name|getResult
parameter_list|()
block|{
return|return
name|result
return|;
block|}
comment|/**      * Sets statement's fetch size (0 for no default size)      *       * @since 3.0      */
specifier|public
name|void
name|setStatementFetchSize
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|metaData
operator|.
name|setStatementFetchSize
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return statement's fetch size      * @since 3.0      */
specifier|public
name|int
name|getStatementFetchSize
parameter_list|()
block|{
return|return
name|metaData
operator|.
name|getStatementFetchSize
argument_list|()
return|;
block|}
block|}
end_class

end_unit

