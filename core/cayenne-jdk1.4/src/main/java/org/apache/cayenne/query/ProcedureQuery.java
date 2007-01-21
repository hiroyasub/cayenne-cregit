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
name|access
operator|.
name|jdbc
operator|.
name|ColumnDescriptor
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
name|jdbc
operator|.
name|RowDescriptor
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
comment|/**  * A query based on Procedure. Can be used as a select query, or as a query of an  * arbitrary complexity, performing data modification, selecting data (possibly with  * multiple result sets per call), returning values via OUT parameters.  *<h3>Execution with DataContext</h3>  *<h4>Reading OUT parameters</h4>  *<p>  * If a ProcedureQuery has OUT parameters, they are wrapped in a separate List in the  * query result. Such list will contain a single Map with OUT parameter values.  *</p>  *<h4>Using ProcedureQuery as a GenericSelectQuery</h4>  *<p>  * Executing ProcedureQuery via  * {@link org.apache.cayenne.access.DataContext#performQuery(Query)} makes sense only if  * the stored procedure returns a single result set (or alternatively returns a result via  * OUT parameters and no other result sets). It is still OK if data modification occurs as  * a side effect. However if the query returns more then one result set, a more generic  * form should be used:  * {@link org.apache.cayenne.access.DataContext#performGenericQuery(Query)}.  *</p>  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ProcedureQuery
extends|extends
name|AbstractQuery
implements|implements
name|ParameterizedQuery
implements|,
name|XMLSerializable
block|{
comment|/**      * @since 1.2      */
specifier|protected
name|String
name|resultEntityName
decl_stmt|;
comment|/**      * @since 1.2      */
specifier|protected
name|Class
name|resultClass
decl_stmt|;
specifier|protected
name|Map
name|parameters
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|ProcedureQueryMetadata
name|metaData
init|=
operator|new
name|ProcedureQueryMetadata
argument_list|()
decl_stmt|;
comment|// TODO: ColumnDescriptor is not XMLSerializable so we can't store
comment|// it in a DataMap
comment|/**      * @since 1.2      */
specifier|protected
name|List
name|resultDescriptors
decl_stmt|;
comment|/**      * Creates an empty procedure query.      */
specifier|public
name|ProcedureQuery
parameter_list|()
block|{
comment|// for backwards compatibility we go against usual default...
name|metaData
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a ProcedureQuery based on a Procedure object.      */
specifier|public
name|ProcedureQuery
parameter_list|(
name|Procedure
name|procedure
parameter_list|)
block|{
comment|// for backwards compatibility we go against usual default...
name|metaData
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setRoot
argument_list|(
name|procedure
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a ProcedureQuery based on a stored procedure.      *<p>      * Performance Note: with current EntityResolver implementation it is preferrable to      * use Procedure object instead of String as a query root. String root can cause      * unneeded EntityResolver reindexing on every call. See this mailing list thread:<a      * href="http://objectstyle.org/cayenne/lists/cayenne-user/2005/01/0109.html">      * http://objectstyle.org/cayenne/lists/cayenne-user/2005/01/0109.html</a>      *</p>      *       * @param procedureName A name of the stored procedure. For this query to work, a      *            procedure with this name must be mapped in Cayenne.      */
specifier|public
name|ProcedureQuery
parameter_list|(
name|String
name|procedureName
parameter_list|)
block|{
comment|// for backwards compatibility we go against usual default...
name|metaData
operator|.
name|setFetchingDataRows
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setRoot
argument_list|(
name|procedureName
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.1      */
specifier|public
name|ProcedureQuery
parameter_list|(
name|Procedure
name|procedure
parameter_list|,
name|Class
name|resultType
parameter_list|)
block|{
name|setRoot
argument_list|(
name|procedure
argument_list|)
expr_stmt|;
name|this
operator|.
name|resultClass
operator|=
name|resultType
expr_stmt|;
block|}
comment|/**      *<p>      * Performance Note: with current EntityResolver implementation it is preferrable to      * use Procedure object instead of String as a query root. String root can cause      * unneeded EntityResolver reindexing on every call. See this mailing list thread:<a      * href="http://objectstyle.org/cayenne/lists/cayenne-user/2005/01/0109.html">      * http://objectstyle.org/cayenne/lists/cayenne-user/2005/01/0109.html</a>      *</p>      *       * @since 1.1      */
specifier|public
name|ProcedureQuery
parameter_list|(
name|String
name|procedureName
parameter_list|,
name|Class
name|resultType
parameter_list|)
block|{
name|setRoot
argument_list|(
name|procedureName
argument_list|)
expr_stmt|;
name|this
operator|.
name|resultClass
operator|=
name|resultType
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
name|metaData
operator|.
name|resolve
argument_list|(
name|root
argument_list|,
name|resultClass
operator|!=
literal|null
condition|?
operator|(
name|Object
operator|)
name|resultClass
else|:
name|resultEntityName
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
comment|/**      * Returns a List of #{@link RowDescriptor} objects describing query ResultSets in      * the order they are returned by the stored procedure.      *<p>      *<i>Note that if a procedure returns ResultSet in an OUT parameter, it is returned      * prior to any other result sets (though in practice database engines usually support      * only one mechanism for returning result sets.</i>      *</p>      *       * @since 1.2      */
specifier|public
name|List
name|getResultDescriptors
parameter_list|()
block|{
return|return
name|resultDescriptors
operator|!=
literal|null
condition|?
name|resultDescriptors
else|:
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
comment|/**      * Adds a descriptor for a single ResultSet. More than one descriptor can be added by      * calling this method multiple times in the order of described ResultSet appearance      * in the procedure results.      *       * @since 1.2      */
specifier|public
specifier|synchronized
name|void
name|addResultDescriptor
parameter_list|(
name|ColumnDescriptor
index|[]
name|descriptor
parameter_list|)
block|{
if|if
condition|(
name|resultDescriptors
operator|==
literal|null
condition|)
block|{
name|resultDescriptors
operator|=
operator|new
name|ArrayList
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|resultDescriptors
operator|.
name|add
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes result descriptor from the list of descriptors.      *       * @since 1.2      */
specifier|public
name|void
name|removeResultDescriptor
parameter_list|(
name|ColumnDescriptor
index|[]
name|descriptor
parameter_list|)
block|{
if|if
condition|(
name|resultDescriptors
operator|!=
literal|null
condition|)
block|{
name|resultDescriptors
operator|.
name|remove
argument_list|(
name|descriptor
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Calls "makeProcedure" on the visitor.      *       * @since 1.2      */
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
name|procedureAction
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
name|metaData
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
literal|"org.apache.cayenne.map.ProcedureQueryBuilder"
argument_list|)
expr_stmt|;
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
name|QueryBuilder
operator|.
name|PROCEDURE_ROOT
argument_list|)
expr_stmt|;
name|String
name|rootString
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
name|Procedure
condition|)
block|{
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
if|if
condition|(
name|rootString
operator|!=
literal|null
condition|)
block|{
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
if|if
condition|(
name|resultEntityName
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"\" result-entity=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|resultEntityName
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
comment|/**      * Creates and returns a new ProcedureQuery built using this query as a prototype and      * substituting template parameters with the values from the map.      *       * @since 1.1      */
specifier|public
name|Query
name|createQuery
parameter_list|(
name|Map
name|parameters
parameter_list|)
block|{
comment|// create a query replica
name|ProcedureQuery
name|query
init|=
operator|new
name|ProcedureQuery
argument_list|()
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|query
operator|.
name|setRoot
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
name|query
operator|.
name|setResultEntityName
argument_list|(
name|resultEntityName
argument_list|)
expr_stmt|;
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
comment|// TODO: implement algorithm for building the name based on the original name and
comment|// the hashcode of the map of parameters. This way query clone can take advantage
comment|// of caching.
return|return
name|query
return|;
block|}
specifier|public
name|String
name|getCachePolicy
parameter_list|()
block|{
return|return
name|metaData
operator|.
name|getCachePolicy
argument_list|()
return|;
block|}
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
name|metaData
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
index|[]
name|cachGroups
parameter_list|)
block|{
name|this
operator|.
name|metaData
operator|.
name|setCacheGroups
argument_list|(
name|cachGroups
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
specifier|public
name|boolean
name|isRefreshingObjects
parameter_list|()
block|{
return|return
name|metaData
operator|.
name|isRefreshingObjects
argument_list|()
return|;
block|}
specifier|public
name|void
name|setRefreshingObjects
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|metaData
operator|.
name|setRefreshingObjects
argument_list|(
name|flag
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isResolvingInherited
parameter_list|()
block|{
return|return
name|metaData
operator|.
name|isResolvingInherited
argument_list|()
return|;
block|}
specifier|public
name|void
name|setResolvingInherited
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|metaData
operator|.
name|setResolvingInherited
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a named parameter to the internal map of parameters.      *       * @since 1.1      */
specifier|public
specifier|synchronized
name|void
name|addParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|parameters
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 1.1      */
specifier|public
specifier|synchronized
name|void
name|removeParameter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|parameters
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a map of procedure parameters.      *       * @since 1.1      */
specifier|public
name|Map
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
comment|/**      * Sets a map of parameters.      *       * @since 1.1      */
specifier|public
specifier|synchronized
name|void
name|setParameters
parameter_list|(
name|Map
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|parameters
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|parameters
operator|.
name|putAll
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Cleans up all configured parameters.      *       * @since 1.1      */
specifier|public
specifier|synchronized
name|void
name|clearParameters
parameter_list|()
block|{
name|this
operator|.
name|parameters
operator|.
name|clear
argument_list|()
expr_stmt|;
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
comment|/**      * @since 1.2      */
specifier|public
name|String
name|getResultEntityName
parameter_list|()
block|{
return|return
name|resultEntityName
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|void
name|setResultEntityName
parameter_list|(
name|String
name|resultEntityName
parameter_list|)
block|{
name|this
operator|.
name|resultEntityName
operator|=
name|resultEntityName
expr_stmt|;
block|}
block|}
end_class

end_unit

