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
name|dba
operator|.
name|frontbase
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
name|access
operator|.
name|translator
operator|.
name|select
operator|.
name|QualifierTranslator
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
name|QueryAssembler
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
name|access
operator|.
name|types
operator|.
name|ExtendedTypeFactory
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
name|configuration
operator|.
name|Constants
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
name|configuration
operator|.
name|RuntimeProperties
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
name|JdbcAdapter
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
name|PkGenerator
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
name|QuotingStrategy
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
name|di
operator|.
name|Inject
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
name|resource
operator|.
name|ResourceLocator
import|;
end_import

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

begin_comment
comment|/**  * DbAdapter implementation for<a href="http://www.frontbase.com/">FrontBase  * RDBMS</a>. Sample connection settings to use with FrontBase are shown below:  *   *<pre>  *          fb.jdbc.username = _system  *          fb.jdbc.password = secret  *          fb.jdbc.url = jdbc:FrontBase://localhost/cayenne/  *          fb.jdbc.driver = jdbc.FrontBase.FBJDriver  *</pre>  *   * @since 1.2  */
end_comment

begin_comment
comment|// TODO, Andrus 11/8/2005:
end_comment

begin_comment
comment|// Limitations (also see FrontBaseStackAdapter in unit tests):
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// 1. Case insensitive ordering (i.e. UPPER in the ORDER BY clause) is supported
end_comment

begin_comment
comment|// by
end_comment

begin_comment
comment|// FrontBase, however aliases don't work ( ORDER BY UPPER(t0.ARTIST_NAME)) ...
end_comment

begin_comment
comment|// not sure
end_comment

begin_comment
comment|// what to do about it.
end_comment

begin_class
specifier|public
class|class
name|FrontBaseAdapter
extends|extends
name|JdbcAdapter
block|{
specifier|public
name|FrontBaseAdapter
parameter_list|(
annotation|@
name|Inject
name|RuntimeProperties
name|runtimeProperties
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_DEFAULT_TYPES_LIST
argument_list|)
name|List
argument_list|<
name|ExtendedType
argument_list|>
name|defaultExtendedTypes
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_USER_TYPES_LIST
argument_list|)
name|List
argument_list|<
name|ExtendedType
argument_list|>
name|userExtendedTypes
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_TYPE_FACTORIES_LIST
argument_list|)
name|List
argument_list|<
name|ExtendedTypeFactory
argument_list|>
name|extendedTypeFactories
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_RESOURCE_LOCATOR
argument_list|)
name|ResourceLocator
name|resourceLocator
parameter_list|)
block|{
name|super
argument_list|(
name|runtimeProperties
argument_list|,
name|defaultExtendedTypes
argument_list|,
name|userExtendedTypes
argument_list|,
name|extendedTypeFactories
argument_list|,
name|resourceLocator
argument_list|)
expr_stmt|;
name|setSupportsBatchUpdates
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * @since 4.0 	 */
annotation|@
name|Override
specifier|public
name|SelectTranslator
name|getSelectTranslator
parameter_list|(
name|SelectQuery
argument_list|<
name|?
argument_list|>
name|query
parameter_list|,
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
return|return
operator|new
name|FrontBaseSelectTranslator
argument_list|(
name|query
argument_list|,
name|this
argument_list|,
name|entityResolver
argument_list|)
return|;
block|}
comment|/** 	 * @since 4.0 	 */
annotation|@
name|Override
specifier|public
name|QualifierTranslator
name|getQualifierTranslator
parameter_list|(
name|QueryAssembler
name|queryAssembler
parameter_list|)
block|{
return|return
operator|new
name|FrontBaseQualifierTranslator
argument_list|(
name|queryAssembler
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|tableTypeForTable
parameter_list|()
block|{
return|return
literal|"BASE TABLE"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|configureExtendedTypes
parameter_list|(
name|ExtendedTypeMap
name|map
parameter_list|)
block|{
name|super
operator|.
name|configureExtendedTypes
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|FrontBaseByteArrayType
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|FrontBaseBooleanType
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|FrontBaseCharType
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Customizes table creating procedure for FrontBase. 	 */
annotation|@
name|Override
specifier|public
name|String
name|createTable
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
name|QuotingStrategy
name|context
init|=
name|getQuotingStrategy
argument_list|()
decl_stmt|;
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"CREATE TABLE "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|ent
argument_list|)
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
expr_stmt|;
comment|// columns
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|it
init|=
name|ent
operator|.
name|getAttributes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|boolean
name|first
init|=
literal|true
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
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|DbAttribute
name|at
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// attribute may not be fully valid, do a simple check
if|if
condition|(
name|at
operator|.
name|getType
argument_list|()
operator|==
name|TypesMapping
operator|.
name|NOT_DEFINED
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Undefined type for attribute '%s.%s'."
argument_list|,
name|ent
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|,
name|at
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|String
index|[]
name|types
init|=
name|externalTypesForJdbcType
argument_list|(
name|at
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|types
operator|==
literal|null
operator|||
name|types
operator|.
name|length
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Undefined type for attribute '%s.%s': %s"
argument_list|,
name|ent
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|,
name|at
operator|.
name|getName
argument_list|()
argument_list|,
name|at
operator|.
name|getType
argument_list|()
argument_list|)
throw|;
block|}
name|String
name|type
init|=
name|types
index|[
literal|0
index|]
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedName
argument_list|(
name|at
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
expr_stmt|;
comment|// Mapping LONGVARCHAR without length creates a column with length
comment|// "1" which
comment|// is definitely not what we want...so just use something very large
comment|// (1Gb seems
comment|// to be the limit for FB)
if|if
condition|(
name|at
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|LONGVARCHAR
condition|)
block|{
name|int
name|len
init|=
name|at
operator|.
name|getMaxLength
argument_list|()
operator|>
literal|0
condition|?
name|at
operator|.
name|getMaxLength
argument_list|()
else|:
literal|1073741824
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|len
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|at
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|VARBINARY
operator|||
name|at
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|BINARY
condition|)
block|{
comment|// use a BIT column with size * 8
name|int
name|len
init|=
name|at
operator|.
name|getMaxLength
argument_list|()
operator|>
literal|0
condition|?
name|at
operator|.
name|getMaxLength
argument_list|()
else|:
literal|1073741824
decl_stmt|;
name|len
operator|*=
literal|8
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|len
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|typeSupportsLength
argument_list|(
name|at
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|int
name|len
init|=
name|at
operator|.
name|getMaxLength
argument_list|()
decl_stmt|;
name|int
name|scale
init|=
name|TypesMapping
operator|.
name|isDecimal
argument_list|(
name|at
operator|.
name|getType
argument_list|()
argument_list|)
condition|?
name|at
operator|.
name|getScale
argument_list|()
else|:
operator|-
literal|1
decl_stmt|;
comment|// sanity check
if|if
condition|(
name|scale
operator|>
name|len
condition|)
block|{
name|scale
operator|=
operator|-
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|len
operator|>
literal|0
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
operator|.
name|append
argument_list|(
name|len
argument_list|)
expr_stmt|;
if|if
condition|(
name|scale
operator|>=
literal|0
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|scale
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|at
operator|.
name|isMandatory
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" NOT NULL"
argument_list|)
expr_stmt|;
block|}
comment|// else: don't appen NULL for FrontBase:
block|}
comment|// primary key clause
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|pkit
init|=
name|ent
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|pkit
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"PRIMARY KEY ("
argument_list|)
expr_stmt|;
name|boolean
name|firstPk
init|=
literal|true
decl_stmt|;
while|while
condition|(
name|pkit
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
name|firstPk
condition|)
block|{
name|firstPk
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|DbAttribute
name|at
init|=
name|pkit
operator|.
name|next
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|quotingStrategy
operator|.
name|quotedName
argument_list|(
name|at
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/** 	 * Adds the CASCADE option to the DROP TABLE clause. 	 */
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
name|Collections
operator|.
name|singleton
argument_list|(
literal|"DROP TABLE "
operator|+
name|getQuotingStrategy
argument_list|()
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|table
argument_list|)
operator|+
literal|" CASCADE"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|PkGenerator
name|createPkGenerator
parameter_list|()
block|{
return|return
operator|new
name|FrontBasePkGenerator
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

