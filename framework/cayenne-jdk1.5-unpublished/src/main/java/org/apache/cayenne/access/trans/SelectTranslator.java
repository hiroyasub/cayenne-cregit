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
name|access
operator|.
name|trans
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
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
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|JoinType
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
name|map
operator|.
name|PathComponent
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
name|PrefetchSelectQuery
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
name|PrefetchTreeNode
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
name|reflect
operator|.
name|ArcProperty
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
name|AttributeProperty
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
name|cayenne
operator|.
name|reflect
operator|.
name|PropertyVisitor
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
name|ToManyProperty
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
name|ToOneProperty
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

begin_comment
comment|/**  * A builder of JDBC PreparedStatements based on Cayenne SelectQueries. Translates  * SelectQuery to parameterized SQL string and wraps it in a PreparedStatement.  * SelectTranslator is stateful and thread-unsafe.  */
end_comment

begin_class
specifier|public
class|class
name|SelectTranslator
extends|extends
name|QueryAssembler
block|{
specifier|protected
specifier|static
specifier|final
name|int
index|[]
name|UNSUPPORTED_DISTINCT_TYPES
init|=
operator|new
name|int
index|[]
block|{
name|Types
operator|.
name|BLOB
block|,
name|Types
operator|.
name|CLOB
block|,
name|Types
operator|.
name|LONGVARBINARY
block|,
name|Types
operator|.
name|LONGVARCHAR
block|}
decl_stmt|;
specifier|protected
specifier|static
name|boolean
name|isUnsupportedForDistinct
parameter_list|(
name|int
name|type
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|UNSUPPORTED_DISTINCT_TYPES
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|UNSUPPORTED_DISTINCT_TYPES
index|[
name|i
index|]
operator|==
name|type
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
name|JoinStack
name|joinStack
init|=
name|createJoinStack
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ColumnDescriptor
argument_list|>
name|resultColumns
decl_stmt|;
name|Map
argument_list|<
name|ObjAttribute
argument_list|,
name|ColumnDescriptor
argument_list|>
name|attributeOverrides
decl_stmt|;
name|Map
argument_list|<
name|ColumnDescriptor
argument_list|,
name|ObjAttribute
argument_list|>
name|defaultAttributesByColumn
decl_stmt|;
name|boolean
name|suppressingDistinct
decl_stmt|;
comment|/**      * If set to<code>true</code>, indicates that distinct select query is required no      * matter what the original query settings where. This flag can be set when joins are      * created using "to-many" relationships.      */
name|boolean
name|forcingDistinct
decl_stmt|;
specifier|protected
name|JoinStack
name|createJoinStack
parameter_list|()
block|{
return|return
operator|new
name|JoinStack
argument_list|()
return|;
block|}
comment|/**      * Returns query translated to SQL. This is a main work method of the      * SelectTranslator.      */
annotation|@
name|Override
specifier|public
name|String
name|createSqlString
parameter_list|()
throws|throws
name|Exception
block|{
name|forcingDistinct
operator|=
literal|false
expr_stmt|;
comment|// build column list
name|this
operator|.
name|resultColumns
operator|=
name|buildResultColumns
argument_list|()
expr_stmt|;
comment|// build qualifier
name|StringBuilder
name|qualifierBuffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|adapter
operator|.
name|getQualifierTranslator
argument_list|(
name|this
argument_list|)
operator|.
name|appendPart
argument_list|(
name|qualifierBuffer
argument_list|)
expr_stmt|;
comment|// build ORDER BY
name|OrderingTranslator
name|orderingTranslator
init|=
operator|new
name|OrderingTranslator
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|StringBuilder
name|orderingBuffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|orderingTranslator
operator|.
name|appendPart
argument_list|(
name|orderingBuffer
argument_list|)
expr_stmt|;
comment|// assemble
name|StringBuilder
name|queryBuf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|queryBuf
operator|.
name|append
argument_list|(
literal|"SELECT "
argument_list|)
expr_stmt|;
comment|// check if DISTINCT is appropriate
comment|// side effect: "suppressingDistinct" flag may end up being flipped here
if|if
condition|(
name|forcingDistinct
operator|||
name|getSelectQuery
argument_list|()
operator|.
name|isDistinct
argument_list|()
condition|)
block|{
name|suppressingDistinct
operator|=
literal|false
expr_stmt|;
for|for
control|(
name|ColumnDescriptor
name|column
range|:
name|resultColumns
control|)
block|{
if|if
condition|(
name|isUnsupportedForDistinct
argument_list|(
name|column
operator|.
name|getJdbcType
argument_list|()
argument_list|)
condition|)
block|{
name|suppressingDistinct
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|suppressingDistinct
condition|)
block|{
name|queryBuf
operator|.
name|append
argument_list|(
literal|"DISTINCT "
argument_list|)
expr_stmt|;
block|}
block|}
comment|// convert ColumnDescriptors to column names
name|List
argument_list|<
name|String
argument_list|>
name|selectColumnExpList
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ColumnDescriptor
name|column
range|:
name|resultColumns
control|)
block|{
name|selectColumnExpList
operator|.
name|add
argument_list|(
name|column
operator|.
name|getQualifiedColumnName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// append any column expressions used in the order by if this query
comment|// uses the DISTINCT modifier
if|if
condition|(
name|forcingDistinct
operator|||
name|getSelectQuery
argument_list|()
operator|.
name|isDistinct
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|orderByColumnList
init|=
name|orderingTranslator
operator|.
name|getOrderByColumnList
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|orderByColumnExp
range|:
name|orderByColumnList
control|)
block|{
comment|// Convert to ColumnDescriptors??
if|if
condition|(
operator|!
name|selectColumnExpList
operator|.
name|contains
argument_list|(
name|orderByColumnExp
argument_list|)
condition|)
block|{
name|selectColumnExpList
operator|.
name|add
argument_list|(
name|orderByColumnExp
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// append columns (unroll the loop's first element)
name|int
name|columnCount
init|=
name|selectColumnExpList
operator|.
name|size
argument_list|()
decl_stmt|;
name|queryBuf
operator|.
name|append
argument_list|(
name|selectColumnExpList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// assume there is at least 1 element
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|columnCount
condition|;
name|i
operator|++
control|)
block|{
name|queryBuf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|queryBuf
operator|.
name|append
argument_list|(
name|selectColumnExpList
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// append from clause
name|queryBuf
operator|.
name|append
argument_list|(
literal|" FROM "
argument_list|)
expr_stmt|;
comment|// append tables and joins
name|joinStack
operator|.
name|appendRoot
argument_list|(
name|queryBuf
argument_list|,
name|getRootDbEntity
argument_list|()
argument_list|)
expr_stmt|;
name|joinStack
operator|.
name|appendJoins
argument_list|(
name|queryBuf
argument_list|)
expr_stmt|;
name|joinStack
operator|.
name|appendQualifier
argument_list|(
name|qualifierBuffer
argument_list|,
name|qualifierBuffer
operator|.
name|length
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
comment|// append qualifier
if|if
condition|(
name|qualifierBuffer
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|queryBuf
operator|.
name|append
argument_list|(
literal|" WHERE "
argument_list|)
expr_stmt|;
name|queryBuf
operator|.
name|append
argument_list|(
name|qualifierBuffer
argument_list|)
expr_stmt|;
block|}
comment|// append prebuilt ordering
if|if
condition|(
name|orderingBuffer
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|queryBuf
operator|.
name|append
argument_list|(
literal|" ORDER BY "
argument_list|)
operator|.
name|append
argument_list|(
name|orderingBuffer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|isSuppressingDistinct
argument_list|()
condition|)
block|{
name|appendLimitAndOffsetClauses
argument_list|(
name|queryBuf
argument_list|)
expr_stmt|;
block|}
return|return
name|queryBuf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Handles appending optional limit and offset clauses. This implementation does      * nothing, deferring to subclasses to define the LIMIT/OFFSET clause syntax.      *       * @since 3.0      */
specifier|protected
name|void
name|appendLimitAndOffsetClauses
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|String
name|getCurrentAlias
parameter_list|()
block|{
return|return
name|joinStack
operator|.
name|getCurrentAlias
argument_list|()
return|;
block|}
comment|/**      * Returns a list of ColumnDescriptors for the query columns.      *       * @since 1.2      */
specifier|public
name|ColumnDescriptor
index|[]
name|getResultColumns
parameter_list|()
block|{
if|if
condition|(
name|resultColumns
operator|==
literal|null
operator|||
name|resultColumns
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
operator|new
name|ColumnDescriptor
index|[
literal|0
index|]
return|;
block|}
return|return
name|resultColumns
operator|.
name|toArray
argument_list|(
operator|new
name|ColumnDescriptor
index|[
name|resultColumns
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
comment|/**      * Returns a map of ColumnDescriptors keyed by ObjAttribute for columns that may need      * to be reprocessed manually due to incompatible mappings along the inheritance      * hierarchy.      *       * @since 1.2      */
specifier|public
name|Map
argument_list|<
name|ObjAttribute
argument_list|,
name|ColumnDescriptor
argument_list|>
name|getAttributeOverrides
parameter_list|()
block|{
if|if
condition|(
name|attributeOverrides
operator|!=
literal|null
condition|)
block|{
return|return
name|attributeOverrides
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
block|}
comment|/**      * Returns true if SelectTranslator determined that a query requiring DISTINCT can't      * be run with DISTINCT keyword for internal reasons. If this method returns true,      * DataNode may need to do in-memory distinct filtering.      *       * @since 1.1      */
specifier|public
name|boolean
name|isSuppressingDistinct
parameter_list|()
block|{
return|return
name|suppressingDistinct
return|;
block|}
specifier|private
name|SelectQuery
name|getSelectQuery
parameter_list|()
block|{
return|return
operator|(
name|SelectQuery
operator|)
name|getQuery
argument_list|()
return|;
block|}
name|List
argument_list|<
name|ColumnDescriptor
argument_list|>
name|buildResultColumns
parameter_list|()
block|{
name|this
operator|.
name|defaultAttributesByColumn
operator|=
operator|new
name|HashMap
argument_list|<
name|ColumnDescriptor
argument_list|,
name|ObjAttribute
argument_list|>
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ColumnDescriptor
argument_list|>
name|columns
init|=
operator|new
name|ArrayList
argument_list|<
name|ColumnDescriptor
argument_list|>
argument_list|()
decl_stmt|;
name|SelectQuery
name|query
init|=
name|getSelectQuery
argument_list|()
decl_stmt|;
comment|// for query with custom attributes use a different strategy
if|if
condition|(
name|query
operator|.
name|isFetchingCustomAttributes
argument_list|()
condition|)
block|{
name|appendCustomColumns
argument_list|(
name|columns
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|appendQueryColumns
argument_list|(
name|columns
argument_list|,
name|query
argument_list|)
expr_stmt|;
block|}
return|return
name|columns
return|;
block|}
comment|/**      * Appends columns needed for object SelectQuery to the provided columns list.      */
comment|// TODO: this whole method screams REFACTORING!!!
name|List
argument_list|<
name|ColumnDescriptor
argument_list|>
name|appendQueryColumns
parameter_list|(
specifier|final
name|List
argument_list|<
name|ColumnDescriptor
argument_list|>
name|columns
parameter_list|,
name|SelectQuery
name|query
parameter_list|)
block|{
specifier|final
name|Set
argument_list|<
name|DbAttribute
argument_list|>
name|attributes
init|=
operator|new
name|HashSet
argument_list|<
name|DbAttribute
argument_list|>
argument_list|()
decl_stmt|;
comment|// fetched attributes include attributes that are either:
comment|//
comment|// * class properties
comment|// * PK
comment|// * FK used in relationships
comment|// * GROUP BY
comment|// * joined prefetch PK
name|ClassDescriptor
name|descriptor
init|=
name|query
operator|.
name|getMetaData
argument_list|(
name|getEntityResolver
argument_list|()
argument_list|)
operator|.
name|getClassDescriptor
argument_list|()
decl_stmt|;
name|ObjEntity
name|oe
init|=
name|descriptor
operator|.
name|getEntity
argument_list|()
decl_stmt|;
name|PropertyVisitor
name|visitor
init|=
operator|new
name|PropertyVisitor
argument_list|()
block|{
specifier|public
name|boolean
name|visitAttribute
parameter_list|(
name|AttributeProperty
name|property
parameter_list|)
block|{
name|ObjAttribute
name|oa
init|=
name|property
operator|.
name|getAttribute
argument_list|()
decl_stmt|;
name|resetJoinStack
argument_list|()
expr_stmt|;
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|dbPathIterator
init|=
name|oa
operator|.
name|getDbPathIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|dbPathIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|pathPart
init|=
name|dbPathIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathPart
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"ObjAttribute has no component: "
operator|+
name|oa
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
if|else if
condition|(
name|pathPart
operator|instanceof
name|DbRelationship
condition|)
block|{
name|DbRelationship
name|rel
init|=
operator|(
name|DbRelationship
operator|)
name|pathPart
decl_stmt|;
name|dbRelationshipAdded
argument_list|(
name|rel
argument_list|,
name|JoinType
operator|.
name|INNER
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|pathPart
operator|instanceof
name|DbAttribute
condition|)
block|{
name|DbAttribute
name|dbAttr
init|=
operator|(
name|DbAttribute
operator|)
name|pathPart
decl_stmt|;
name|appendColumn
argument_list|(
name|columns
argument_list|,
name|oa
argument_list|,
name|dbAttr
argument_list|,
name|attributes
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitToMany
parameter_list|(
name|ToManyProperty
name|property
parameter_list|)
block|{
name|visitRelationship
argument_list|(
name|property
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitToOne
parameter_list|(
name|ToOneProperty
name|property
parameter_list|)
block|{
name|visitRelationship
argument_list|(
name|property
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|visitRelationship
parameter_list|(
name|ArcProperty
name|property
parameter_list|)
block|{
name|ObjRelationship
name|rel
init|=
name|property
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
name|DbRelationship
name|dbRel
init|=
name|rel
operator|.
name|getDbRelationships
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbJoin
argument_list|>
name|joins
init|=
name|dbRel
operator|.
name|getJoins
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|joins
operator|.
name|size
argument_list|()
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
name|len
condition|;
name|i
operator|++
control|)
block|{
name|DbJoin
name|join
init|=
name|joins
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|DbAttribute
name|src
init|=
name|join
operator|.
name|getSource
argument_list|()
decl_stmt|;
name|appendColumn
argument_list|(
name|columns
argument_list|,
literal|null
argument_list|,
name|src
argument_list|,
name|attributes
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
if|if
condition|(
name|query
operator|.
name|isResolvingInherited
argument_list|()
condition|)
block|{
name|descriptor
operator|.
name|visitAllProperties
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|descriptor
operator|.
name|visitProperties
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
block|}
comment|// add remaining needed attrs from DbEntity
name|DbEntity
name|table
init|=
name|getRootDbEntity
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|DbAttribute
name|dba
range|:
name|table
operator|.
name|getPrimaryKeys
argument_list|()
control|)
block|{
name|appendColumn
argument_list|(
name|columns
argument_list|,
literal|null
argument_list|,
name|dba
argument_list|,
name|attributes
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// special handling of a disjoint query...
comment|// TODO, Andrus 11/17/2005 - resultPath mechanism is generic and should probably
comment|// be moved in the superclass (SelectQuery), replacing customDbAttributes.
if|if
condition|(
name|query
operator|instanceof
name|PrefetchSelectQuery
condition|)
block|{
comment|// for each relationship path add closest FK or PK, for each attribute path,
comment|// add specified column
for|for
control|(
name|String
name|path
range|:
operator|(
operator|(
name|PrefetchSelectQuery
operator|)
name|query
operator|)
operator|.
name|getResultPaths
argument_list|()
control|)
block|{
name|Expression
name|pathExp
init|=
name|oe
operator|.
name|translateToDbPath
argument_list|(
name|Expression
operator|.
name|fromString
argument_list|(
name|path
argument_list|)
argument_list|)
decl_stmt|;
comment|// add joins and find terminating element
name|resetJoinStack
argument_list|()
expr_stmt|;
name|PathComponent
argument_list|<
name|DbAttribute
argument_list|,
name|DbRelationship
argument_list|>
name|lastComponent
init|=
literal|null
decl_stmt|;
for|for
control|(
name|PathComponent
argument_list|<
name|DbAttribute
argument_list|,
name|DbRelationship
argument_list|>
name|component
range|:
name|table
operator|.
name|resolvePath
argument_list|(
name|pathExp
argument_list|,
name|getPathAliases
argument_list|()
argument_list|)
control|)
block|{
comment|// do not add join for the last DB Rel
if|if
condition|(
name|component
operator|.
name|getRelationship
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|component
operator|.
name|isLast
argument_list|()
condition|)
block|{
name|dbRelationshipAdded
argument_list|(
name|component
operator|.
name|getRelationship
argument_list|()
argument_list|,
name|component
operator|.
name|getJoinType
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|lastComponent
operator|=
name|component
expr_stmt|;
block|}
name|String
name|labelPrefix
init|=
name|pathExp
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
literal|"db:"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
comment|// process terminating element
if|if
condition|(
name|lastComponent
operator|!=
literal|null
condition|)
block|{
name|DbRelationship
name|relationship
init|=
name|lastComponent
operator|.
name|getRelationship
argument_list|()
decl_stmt|;
if|if
condition|(
name|relationship
operator|!=
literal|null
condition|)
block|{
comment|// add last join
if|if
condition|(
name|relationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|dbRelationshipAdded
argument_list|(
name|relationship
argument_list|,
name|JoinType
operator|.
name|INNER
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|DbJoin
name|j
range|:
name|relationship
operator|.
name|getJoins
argument_list|()
control|)
block|{
name|DbAttribute
name|attribute
init|=
name|relationship
operator|.
name|isToMany
argument_list|()
condition|?
name|j
operator|.
name|getTarget
argument_list|()
else|:
name|j
operator|.
name|getSource
argument_list|()
decl_stmt|;
comment|// note that we my select a source attribute, but label it as
comment|// target for simplified snapshot processing
name|appendColumn
argument_list|(
name|columns
argument_list|,
literal|null
argument_list|,
name|attribute
argument_list|,
name|attributes
argument_list|,
name|labelPrefix
operator|+
literal|'.'
operator|+
name|j
operator|.
name|getTargetName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// label prefix already includes relationship name
name|appendColumn
argument_list|(
name|columns
argument_list|,
literal|null
argument_list|,
name|lastComponent
operator|.
name|getAttribute
argument_list|()
argument_list|,
name|attributes
argument_list|,
name|labelPrefix
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// handle joint prefetches directly attached to this query...
if|if
condition|(
name|query
operator|.
name|getPrefetchTree
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|PrefetchTreeNode
name|prefetch
range|:
name|query
operator|.
name|getPrefetchTree
argument_list|()
operator|.
name|adjacentJointNodes
argument_list|()
control|)
block|{
comment|// for each prefetch add all joins plus columns from the target entity
name|Expression
name|prefetchExp
init|=
name|Expression
operator|.
name|fromString
argument_list|(
name|prefetch
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|Expression
name|dbPrefetch
init|=
name|oe
operator|.
name|translateToDbPath
argument_list|(
name|prefetchExp
argument_list|)
decl_stmt|;
name|resetJoinStack
argument_list|()
expr_stmt|;
name|DbRelationship
name|r
init|=
literal|null
decl_stmt|;
for|for
control|(
name|PathComponent
argument_list|<
name|DbAttribute
argument_list|,
name|DbRelationship
argument_list|>
name|component
range|:
name|table
operator|.
name|resolvePath
argument_list|(
name|dbPrefetch
argument_list|,
name|getPathAliases
argument_list|()
argument_list|)
control|)
block|{
name|r
operator|=
name|component
operator|.
name|getRelationship
argument_list|()
expr_stmt|;
name|dbRelationshipAdded
argument_list|(
name|r
argument_list|,
name|JoinType
operator|.
name|LEFT_OUTER
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid joint prefetch '"
operator|+
name|prefetch
operator|+
literal|"' for entity: "
operator|+
name|oe
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
comment|// add columns from the target entity, including those that are matched
comment|// against the FK of the source entity. This is needed to determine
comment|// whether optional relationships are null
comment|// go via target OE to make sure that Java types are mapped correctly...
name|ObjRelationship
name|targetRel
init|=
operator|(
name|ObjRelationship
operator|)
name|prefetchExp
operator|.
name|evaluate
argument_list|(
name|oe
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ObjAttribute
argument_list|>
name|targetObjAttrs
init|=
operator|(
name|Iterator
argument_list|<
name|ObjAttribute
argument_list|>
operator|)
name|targetRel
operator|.
name|getTargetEntity
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|String
name|labelPrefix
init|=
name|dbPrefetch
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
literal|"db:"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|targetObjAttrs
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ObjAttribute
name|oa
init|=
name|targetObjAttrs
operator|.
name|next
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|CayenneMapEntry
argument_list|>
name|dbPathIterator
init|=
name|oa
operator|.
name|getDbPathIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|dbPathIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|pathPart
init|=
name|dbPathIterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|pathPart
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"ObjAttribute has no component: "
operator|+
name|oa
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
if|else if
condition|(
name|pathPart
operator|instanceof
name|DbRelationship
condition|)
block|{
name|DbRelationship
name|rel
init|=
operator|(
name|DbRelationship
operator|)
name|pathPart
decl_stmt|;
name|dbRelationshipAdded
argument_list|(
name|rel
argument_list|,
name|JoinType
operator|.
name|INNER
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|pathPart
operator|instanceof
name|DbAttribute
condition|)
block|{
name|DbAttribute
name|attribute
init|=
operator|(
name|DbAttribute
operator|)
name|pathPart
decl_stmt|;
name|appendColumn
argument_list|(
name|columns
argument_list|,
name|oa
argument_list|,
name|attribute
argument_list|,
name|attributes
argument_list|,
name|labelPrefix
operator|+
literal|'.'
operator|+
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// append remaining target attributes such as keys
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|targetAttributes
init|=
operator|(
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
operator|)
name|r
operator|.
name|getTargetEntity
argument_list|()
operator|.
name|getAttributes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|targetAttributes
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbAttribute
name|attribute
init|=
name|targetAttributes
operator|.
name|next
argument_list|()
decl_stmt|;
name|appendColumn
argument_list|(
name|columns
argument_list|,
literal|null
argument_list|,
name|attribute
argument_list|,
name|attributes
argument_list|,
name|labelPrefix
operator|+
literal|'.'
operator|+
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|columns
return|;
block|}
comment|/**      * Appends custom columns from SelectQuery to the provided list.      */
name|List
argument_list|<
name|ColumnDescriptor
argument_list|>
name|appendCustomColumns
parameter_list|(
name|List
argument_list|<
name|ColumnDescriptor
argument_list|>
name|columns
parameter_list|,
name|SelectQuery
name|query
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|customAttributes
init|=
name|query
operator|.
name|getCustomDbAttributes
argument_list|()
decl_stmt|;
name|DbEntity
name|table
init|=
name|getRootDbEntity
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|customAttributes
operator|.
name|size
argument_list|()
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
name|len
condition|;
name|i
operator|++
control|)
block|{
name|DbAttribute
name|attribute
init|=
operator|(
name|DbAttribute
operator|)
name|table
operator|.
name|getAttribute
argument_list|(
name|customAttributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Attribute does not exist: "
operator|+
name|customAttributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
throw|;
block|}
name|columns
operator|.
name|add
argument_list|(
operator|new
name|ColumnDescriptor
argument_list|(
name|attribute
argument_list|,
name|getCurrentAlias
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|columns
return|;
block|}
specifier|private
name|void
name|appendColumn
parameter_list|(
name|List
argument_list|<
name|ColumnDescriptor
argument_list|>
name|columns
parameter_list|,
name|ObjAttribute
name|objAttribute
parameter_list|,
name|DbAttribute
name|attribute
parameter_list|,
name|Set
argument_list|<
name|DbAttribute
argument_list|>
name|skipSet
parameter_list|,
name|String
name|label
parameter_list|)
block|{
if|if
condition|(
name|skipSet
operator|.
name|add
argument_list|(
name|attribute
argument_list|)
condition|)
block|{
name|String
name|alias
init|=
name|getCurrentAlias
argument_list|()
decl_stmt|;
name|ColumnDescriptor
name|column
init|=
operator|(
name|objAttribute
operator|!=
literal|null
operator|)
condition|?
operator|new
name|ColumnDescriptor
argument_list|(
name|objAttribute
argument_list|,
name|attribute
argument_list|,
name|alias
argument_list|)
else|:
operator|new
name|ColumnDescriptor
argument_list|(
name|attribute
argument_list|,
name|alias
argument_list|)
decl_stmt|;
if|if
condition|(
name|label
operator|!=
literal|null
condition|)
block|{
name|column
operator|.
name|setLabel
argument_list|(
name|label
argument_list|)
expr_stmt|;
block|}
name|columns
operator|.
name|add
argument_list|(
name|column
argument_list|)
expr_stmt|;
comment|// TODO: andrus, 5/7/2006 - replace 'columns' collection with this map, as it
comment|// is redundant
name|defaultAttributesByColumn
operator|.
name|put
argument_list|(
name|column
argument_list|,
name|objAttribute
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|objAttribute
operator|!=
literal|null
condition|)
block|{
comment|// record ObjAttribute override
for|for
control|(
name|ColumnDescriptor
name|column
range|:
name|columns
control|)
block|{
if|if
condition|(
name|attribute
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|column
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|// kick out the original attribute
name|ObjAttribute
name|original
init|=
name|defaultAttributesByColumn
operator|.
name|remove
argument_list|(
name|column
argument_list|)
decl_stmt|;
if|if
condition|(
name|original
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|attributeOverrides
operator|==
literal|null
condition|)
block|{
name|attributeOverrides
operator|=
operator|new
name|HashMap
argument_list|<
name|ObjAttribute
argument_list|,
name|ColumnDescriptor
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|attributeOverrides
operator|.
name|put
argument_list|(
name|original
argument_list|,
name|column
argument_list|)
expr_stmt|;
name|column
operator|.
name|setJavaClass
argument_list|(
name|Void
operator|.
name|TYPE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
block|}
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|public
name|void
name|resetJoinStack
parameter_list|()
block|{
name|joinStack
operator|.
name|resetStack
argument_list|()
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|public
name|void
name|dbRelationshipAdded
parameter_list|(
name|DbRelationship
name|relationship
parameter_list|,
name|JoinType
name|joinType
parameter_list|,
name|String
name|joinSplitAlias
parameter_list|)
block|{
if|if
condition|(
name|relationship
operator|.
name|isToMany
argument_list|()
condition|)
block|{
name|forcingDistinct
operator|=
literal|true
expr_stmt|;
block|}
name|joinStack
operator|.
name|pushJoin
argument_list|(
name|relationship
argument_list|,
name|joinType
argument_list|,
name|joinSplitAlias
argument_list|)
expr_stmt|;
block|}
comment|/**      * Always returns true.      */
annotation|@
name|Override
specifier|public
name|boolean
name|supportsTableAliases
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

