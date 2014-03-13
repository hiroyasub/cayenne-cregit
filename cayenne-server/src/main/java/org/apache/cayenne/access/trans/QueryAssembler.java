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
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
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
name|DataNode
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
name|dba
operator|.
name|DbAdapter
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
name|log
operator|.
name|JdbcEventLogger
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

begin_comment
comment|/**  * Abstract superclass of Query translators.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|QueryAssembler
block|{
specifier|protected
name|Query
name|query
decl_stmt|;
specifier|protected
name|QueryMetadata
name|queryMetadata
decl_stmt|;
specifier|protected
name|String
name|cachedSqlString
decl_stmt|;
specifier|protected
name|Connection
name|connection
decl_stmt|;
specifier|protected
name|DbAdapter
name|adapter
decl_stmt|;
specifier|protected
name|EntityResolver
name|entityResolver
decl_stmt|;
specifier|protected
name|JdbcEventLogger
name|logger
decl_stmt|;
comment|/**      * Holds PreparedStatement values.      */
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * PreparedStatement attributes matching entries in<code>values</code>      * list.      */
specifier|protected
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|attributes
init|=
operator|new
name|ArrayList
argument_list|<
name|DbAttribute
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * The index parameter will be inserted at in parameter list      */
specifier|protected
name|int
name|parameterIndex
decl_stmt|;
comment|/**      * @since 3.2      */
specifier|public
name|QueryAssembler
parameter_list|(
name|Query
name|query
parameter_list|,
name|DataNode
name|dataNode
parameter_list|,
name|Connection
name|connection
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|dataNode
operator|.
name|getJdbcEventLogger
argument_list|()
expr_stmt|;
name|this
operator|.
name|entityResolver
operator|=
name|dataNode
operator|.
name|getEntityResolver
argument_list|()
expr_stmt|;
name|this
operator|.
name|adapter
operator|=
name|dataNode
operator|.
name|getAdapter
argument_list|()
expr_stmt|;
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
name|this
operator|.
name|queryMetadata
operator|=
name|query
operator|.
name|getMetaData
argument_list|(
name|entityResolver
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns aliases for the path splits defined in the query.      *       * @since 3.0      */
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getPathAliases
parameter_list|()
block|{
return|return
name|query
operator|.
name|getMetaData
argument_list|(
name|entityResolver
argument_list|)
operator|.
name|getPathSplitAliases
argument_list|()
return|;
block|}
specifier|public
name|EntityResolver
name|getEntityResolver
parameter_list|()
block|{
return|return
name|entityResolver
return|;
block|}
specifier|public
name|DbAdapter
name|getAdapter
parameter_list|()
block|{
return|return
name|adapter
return|;
block|}
comment|/**      * Returns query object being processed.      */
specifier|public
name|Query
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
specifier|public
name|QueryMetadata
name|getQueryMetadata
parameter_list|()
block|{
return|return
name|queryMetadata
return|;
block|}
comment|/**      * @since 3.1      */
specifier|public
name|JdbcEventLogger
name|getJdbcEventLogger
parameter_list|()
block|{
return|return
name|logger
return|;
block|}
specifier|public
name|DbEntity
name|getRootDbEntity
parameter_list|()
block|{
return|return
name|queryMetadata
operator|.
name|getDbEntity
argument_list|()
return|;
block|}
specifier|public
name|ObjEntity
name|getRootEntity
parameter_list|()
block|{
return|return
name|queryMetadata
operator|.
name|getObjEntity
argument_list|()
return|;
block|}
comment|/**      * A callback invoked by a child qualifier or ordering processor allowing      * query assembler to reset its join stack.      *       * @since 3.0      */
specifier|public
specifier|abstract
name|void
name|resetJoinStack
parameter_list|()
function_decl|;
comment|/**      * Returns an alias of the table which is currently at the top of the join      * stack.      *       * @since 3.0      */
specifier|public
specifier|abstract
name|String
name|getCurrentAlias
parameter_list|()
function_decl|;
comment|/**      * Appends a join with given semantics to the query.      *       * @since 3.0      */
specifier|public
specifier|abstract
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
function_decl|;
comment|/**      * Translates query into sql string. This is a workhorse method of      * QueryAssembler. It is called internally from<code>createStatement</code>      * . Usually there is no need to invoke it explicitly.      */
specifier|public
specifier|abstract
name|String
name|createSqlString
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns<code>true</code> if table aliases are supported. Default      * implementation returns false.      */
specifier|public
name|boolean
name|supportsTableAliases
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**      * Registers<code>anObject</code> as a PreparedStatement parameter.      *       * @param anObject      *            object that represents a value of DbAttribute      * @param dbAttr      *            DbAttribute being processed.      */
specifier|public
name|void
name|addToParamList
parameter_list|(
name|DbAttribute
name|dbAttr
parameter_list|,
name|Object
name|anObject
parameter_list|)
block|{
name|attributes
operator|.
name|add
argument_list|(
name|parameterIndex
argument_list|,
name|dbAttr
argument_list|)
expr_stmt|;
name|values
operator|.
name|add
argument_list|(
name|parameterIndex
operator|++
argument_list|,
name|anObject
argument_list|)
expr_stmt|;
block|}
comment|/**      * Translates internal query into PreparedStatement.      */
specifier|public
name|PreparedStatement
name|createStatement
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|t1
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|String
name|sqlStr
init|=
name|createSqlString
argument_list|()
decl_stmt|;
name|logger
operator|.
name|logQuery
argument_list|(
name|sqlStr
argument_list|,
name|attributes
argument_list|,
name|values
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|t1
argument_list|)
expr_stmt|;
name|PreparedStatement
name|stmt
init|=
name|connection
operator|.
name|prepareStatement
argument_list|(
name|sqlStr
argument_list|)
decl_stmt|;
name|initStatement
argument_list|(
name|stmt
argument_list|)
expr_stmt|;
return|return
name|stmt
return|;
block|}
comment|/**      * Initializes prepared statements with collected parameters. Called      * internally from "createStatement". Cayenne users shouldn't normally call      * it directly.      */
specifier|protected
name|void
name|initStatement
parameter_list|(
name|PreparedStatement
name|stmt
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|values
operator|!=
literal|null
operator|&&
name|values
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|int
name|len
init|=
name|values
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
name|Object
name|val
init|=
name|values
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|DbAttribute
name|attr
init|=
name|attributes
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// null DbAttributes are a result of inferior qualifier
comment|// processing
comment|// (qualifier can't map parameters to DbAttributes and therefore
comment|// only supports standard java types now)
comment|// hence, a special moronic case here:
if|if
condition|(
name|attr
operator|==
literal|null
condition|)
block|{
name|stmt
operator|.
name|setObject
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|adapter
operator|.
name|bindParameter
argument_list|(
name|stmt
argument_list|,
name|val
argument_list|,
name|i
operator|+
literal|1
argument_list|,
name|attr
operator|.
name|getType
argument_list|()
argument_list|,
name|attr
operator|.
name|getScale
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|queryMetadata
operator|.
name|getStatementFetchSize
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|stmt
operator|.
name|setFetchSize
argument_list|(
name|queryMetadata
operator|.
name|getStatementFetchSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

