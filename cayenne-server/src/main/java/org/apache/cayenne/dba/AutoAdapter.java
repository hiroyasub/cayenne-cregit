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
name|dba
package|;
end_package

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
name|sql
operator|.
name|SQLException
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
name|access
operator|.
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|SQLTreeProcessor
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
name|translator
operator|.
name|ParameterBinding
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
name|translator
operator|.
name|ejbql
operator|.
name|EJBQLTranslatorFactory
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
name|translator
operator|.
name|select
operator|.
name|SelectTranslator
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
name|ExtendedTypeMap
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
name|di
operator|.
name|Provider
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
name|query
operator|.
name|FluentSelect
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
name|SQLAction
import|;
end_import

begin_comment
comment|/**  * A DbAdapter that automatically detects the kind of database it is running on  * and instantiates an appropriate DB-specific adapter, delegating all  * subsequent method calls to this adapter.  *  * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|AutoAdapter
implements|implements
name|DbAdapter
block|{
specifier|protected
name|Provider
argument_list|<
name|DbAdapter
argument_list|>
name|adapterProvider
decl_stmt|;
specifier|protected
name|PkGenerator
name|pkGenerator
decl_stmt|;
specifier|protected
name|JdbcEventLogger
name|logger
decl_stmt|;
comment|/** 	 * The actual adapter that is delegated methods execution. 	 */
specifier|volatile
name|DbAdapter
name|adapter
decl_stmt|;
comment|/** 	 * Creates an {@link AutoAdapter} based on a delegate adapter obtained via 	 * "adapterProvider". 	 * 	 * @since 3.1 	 */
specifier|public
name|AutoAdapter
parameter_list|(
name|Provider
argument_list|<
name|DbAdapter
argument_list|>
name|adapterProvider
parameter_list|,
name|JdbcEventLogger
name|logger
parameter_list|)
block|{
if|if
condition|(
name|adapterProvider
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Null adapterProvider"
argument_list|)
throw|;
block|}
name|this
operator|.
name|adapterProvider
operator|=
name|adapterProvider
expr_stmt|;
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
comment|/** 	 * Returns a proxied DbAdapter, lazily creating it on first invocation. 	 */
specifier|protected
name|DbAdapter
name|getAdapter
parameter_list|()
block|{
if|if
condition|(
name|adapter
operator|==
literal|null
condition|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|adapter
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|adapter
operator|=
name|loadAdapter
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|adapter
return|;
block|}
comment|/** 	 * Loads underlying DbAdapter delegate. 	 */
specifier|protected
name|DbAdapter
name|loadAdapter
parameter_list|()
block|{
return|return
name|adapterProvider
operator|.
name|get
argument_list|()
return|;
block|}
comment|/** 	 * @since 4.2 	 */
annotation|@
name|Override
specifier|public
name|SelectTranslator
name|getSelectTranslator
parameter_list|(
name|FluentSelect
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|query
parameter_list|,
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|getSelectTranslator
argument_list|(
name|query
argument_list|,
name|entityResolver
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getBatchTerminator
parameter_list|()
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|getBatchTerminator
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|SQLTreeProcessor
name|getSqlTreeProcessor
parameter_list|()
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|getSqlTreeProcessor
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|SQLAction
name|getAction
parameter_list|(
name|Query
name|query
parameter_list|,
name|DataNode
name|node
parameter_list|)
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|getAction
argument_list|(
name|query
argument_list|,
name|node
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsUniqueConstraints
parameter_list|()
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|supportsUniqueConstraints
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsCatalogsOnReverseEngineering
parameter_list|()
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|supportsCatalogsOnReverseEngineering
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsGeneratedKeys
parameter_list|()
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|supportsGeneratedKeys
argument_list|()
return|;
block|}
comment|/** 	 * @since 4.2 	 */
annotation|@
name|Override
specifier|public
name|boolean
name|supportsGeneratedKeysForBatchInserts
parameter_list|()
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|supportsGeneratedKeysForBatchInserts
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsBatchUpdates
parameter_list|()
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|supportsBatchUpdates
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|typeSupportsLength
parameter_list|(
name|int
name|type
parameter_list|)
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|typeSupportsLength
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|dropTableStatements
parameter_list|(
name|DbEntity
name|table
parameter_list|)
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|dropTableStatements
argument_list|(
name|table
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|createTable
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|createTable
argument_list|(
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|createUniqueConstraint
parameter_list|(
name|DbEntity
name|source
parameter_list|,
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|columns
parameter_list|)
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|createUniqueConstraint
argument_list|(
name|source
argument_list|,
name|columns
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|createFkConstraint
parameter_list|(
name|DbRelationship
name|rel
parameter_list|)
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|createFkConstraint
argument_list|(
name|rel
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
index|[]
name|externalTypesForJdbcType
parameter_list|(
name|int
name|type
parameter_list|)
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|externalTypesForJdbcType
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ExtendedTypeMap
name|getExtendedTypes
parameter_list|()
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
return|;
block|}
comment|/** 	 * Returns a primary key generator. 	 */
annotation|@
name|Override
specifier|public
name|PkGenerator
name|getPkGenerator
parameter_list|()
block|{
return|return
operator|(
name|pkGenerator
operator|!=
literal|null
operator|)
condition|?
name|pkGenerator
else|:
name|getAdapter
argument_list|()
operator|.
name|getPkGenerator
argument_list|()
return|;
block|}
comment|/** 	 * Sets a PK generator override. If set to non-null value, such PK generator 	 * will be used instead of the one provided by wrapped adapter. 	 */
specifier|public
name|void
name|setPkGenerator
parameter_list|(
name|PkGenerator
name|pkGenerator
parameter_list|)
block|{
name|this
operator|.
name|pkGenerator
operator|=
name|pkGenerator
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|DbAttribute
name|buildAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|typeName
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|size
parameter_list|,
name|int
name|precision
parameter_list|,
name|boolean
name|allowNulls
parameter_list|)
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|buildAttribute
argument_list|(
name|name
argument_list|,
name|typeName
argument_list|,
name|type
argument_list|,
name|size
argument_list|,
name|precision
argument_list|,
name|allowNulls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|bindParameter
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|ParameterBinding
name|parameterBinding
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
name|getAdapter
argument_list|()
operator|.
name|bindParameter
argument_list|(
name|statement
argument_list|,
name|parameterBinding
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|tableTypeForTable
parameter_list|()
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|tableTypeForTable
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|tableTypeForView
parameter_list|()
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|tableTypeForView
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|createTableAppendColumn
parameter_list|(
name|StringBuffer
name|sqlBuffer
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
name|getAdapter
argument_list|()
operator|.
name|createTableAppendColumn
argument_list|(
name|sqlBuffer
argument_list|,
name|column
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * @since 4.0 	 */
annotation|@
name|Override
specifier|public
name|QuotingStrategy
name|getQuotingStrategy
parameter_list|()
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|getQuotingStrategy
argument_list|()
return|;
block|}
comment|/** 	 * @since 4.0 	 */
annotation|@
name|Override
specifier|public
name|DbAdapter
name|unwrap
parameter_list|()
block|{
return|return
name|getAdapter
argument_list|()
return|;
block|}
comment|/** 	 * @since 4.0 	 */
annotation|@
name|Override
specifier|public
name|EJBQLTranslatorFactory
name|getEjbqlTranslatorFactory
parameter_list|()
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|getEjbqlTranslatorFactory
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSystemCatalogs
parameter_list|()
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|getSystemCatalogs
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSystemSchemas
parameter_list|()
block|{
return|return
name|getAdapter
argument_list|()
operator|.
name|getSystemSchemas
argument_list|()
return|;
block|}
block|}
end_class

end_unit

