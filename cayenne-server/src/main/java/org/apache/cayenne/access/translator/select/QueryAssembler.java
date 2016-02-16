begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *<p/>  * http://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  ****************************************************************/
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
name|translator
operator|.
name|select
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
name|access
operator|.
name|translator
operator|.
name|DbAttributeBinding
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
name|types
operator|.
name|ExtendedType
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
name|dba
operator|.
name|TypesMapping
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
name|boolean
name|translated
decl_stmt|;
specifier|protected
name|String
name|sql
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
name|List
argument_list|<
name|DbAttributeBinding
argument_list|>
name|bindings
decl_stmt|;
comment|/** 	 * @since 4.0 	 */
specifier|public
name|QueryAssembler
parameter_list|(
name|Query
name|query
parameter_list|,
name|DbAdapter
name|adapter
parameter_list|,
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
name|adapter
operator|=
name|adapter
expr_stmt|;
name|this
operator|.
name|query
operator|=
name|query
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
name|this
operator|.
name|bindings
operator|=
operator|new
name|ArrayList
argument_list|<
name|DbAttributeBinding
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * Returns aliases for the path splits defined in the query. 	 * 	 * @since 3.0 	 */
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
name|queryMetadata
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
comment|/** 	 * Returns query object being processed. 	 */
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
comment|/** 	 * A callback invoked by a child qualifier or ordering processor allowing 	 * query assembler to reset its join stack. 	 * 	 * @since 3.0 	 */
specifier|public
specifier|abstract
name|void
name|resetJoinStack
parameter_list|()
function_decl|;
comment|/** 	 * Returns an alias of the table which is currently at the top of the join 	 * stack. 	 * 	 * @since 3.0 	 */
specifier|public
specifier|abstract
name|String
name|getCurrentAlias
parameter_list|()
function_decl|;
comment|/** 	 * Appends a join with given semantics to the query. 	 * 	 * @since 3.0 	 */
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
comment|/** 	 * Translates query into an SQL string formatted to use in a 	 * PreparedStatement. 	 */
specifier|public
name|String
name|getSql
parameter_list|()
block|{
name|ensureTranslated
argument_list|()
expr_stmt|;
return|return
name|sql
return|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|protected
name|void
name|ensureTranslated
parameter_list|()
block|{
if|if
condition|(
operator|!
name|translated
condition|)
block|{
name|doTranslate
argument_list|()
expr_stmt|;
name|translated
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|/** 	 * @since 4.0 	 */
specifier|protected
specifier|abstract
name|void
name|doTranslate
parameter_list|()
function_decl|;
comment|/** 	 * Returns<code>true</code> if table aliases are supported. Default 	 * implementation returns false. 	 */
specifier|public
name|boolean
name|supportsTableAliases
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/** 	 * Registers<code>anObject</code> as a PreparedStatement parameter. 	 * 	 * @param anObject 	 *            object that represents a value of DbAttribute 	 * @param dbAttr 	 *            DbAttribute being processed. 	 */
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
name|String
name|typeName
init|=
name|TypesMapping
operator|.
name|SQL_NULL
decl_stmt|;
if|if
condition|(
name|dbAttr
operator|!=
literal|null
condition|)
name|typeName
operator|=
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|dbAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|ExtendedType
name|extendedType
init|=
name|adapter
operator|.
name|getExtendedTypes
argument_list|()
operator|.
name|getRegisteredType
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
name|DbAttributeBinding
name|binding
init|=
operator|new
name|DbAttributeBinding
argument_list|(
name|dbAttr
argument_list|,
name|extendedType
argument_list|)
decl_stmt|;
name|binding
operator|.
name|setValue
argument_list|(
name|anObject
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setStatementPosition
argument_list|(
name|bindings
operator|.
name|size
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|bindings
operator|.
name|add
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * @since 4.0 	 */
specifier|public
name|DbAttributeBinding
index|[]
name|getBindings
parameter_list|()
block|{
return|return
name|bindings
operator|.
name|toArray
argument_list|(
operator|new
name|DbAttributeBinding
index|[
name|bindings
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

