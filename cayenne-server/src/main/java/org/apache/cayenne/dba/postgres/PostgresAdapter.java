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
name|postgres
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
name|translator
operator|.
name|Binding
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
name|CharType
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
name|merge
operator|.
name|MergerFactory
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
comment|/**  * DbAdapter implementation for<a href="http://www.postgresql.org">PostgreSQL  * RDBMS</a>. Sample connection settings to use with PostgreSQL are shown  * below:  *   *<pre>  *      postgres.jdbc.username = test  *      postgres.jdbc.password = secret  *      postgres.jdbc.url = jdbc:postgresql://serverhostname/cayenne  *      postgres.jdbc.driver = org.postgresql.Driver  *</pre>  */
end_comment

begin_class
specifier|public
class|class
name|PostgresAdapter
extends|extends
name|JdbcAdapter
block|{
specifier|public
specifier|static
specifier|final
name|String
name|BYTEA
init|=
literal|"bytea"
decl_stmt|;
specifier|public
name|PostgresAdapter
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
name|PostgresSelectTranslator
argument_list|(
name|query
argument_list|,
name|this
argument_list|,
name|entityResolver
argument_list|)
return|;
block|}
comment|/** 	 * Uses PostgresActionBuilder to create the right action. 	 *  	 * @since 1.2 	 */
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
name|query
operator|.
name|createSQLAction
argument_list|(
operator|new
name|PostgresActionBuilder
argument_list|(
name|node
argument_list|)
argument_list|)
return|;
block|}
comment|/** 	 * Installs appropriate ExtendedTypes as converters for passing values 	 * between JDBC and Java layers. 	 */
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
name|CharType
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|PostgresByteArrayType
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
argument_list|)
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
name|scale
parameter_list|,
name|boolean
name|allowNulls
parameter_list|)
block|{
comment|// "bytea" maps to pretty much any binary type, so
comment|// it is up to us to select the most sensible default.
comment|// And the winner is LONGVARBINARY
if|if
condition|(
name|BYTEA
operator|.
name|equalsIgnoreCase
argument_list|(
name|typeName
argument_list|)
condition|)
block|{
name|type
operator|=
name|Types
operator|.
name|LONGVARBINARY
expr_stmt|;
block|}
comment|// oid is returned as INTEGER, need to make it BLOB
if|else if
condition|(
literal|"oid"
operator|.
name|equals
argument_list|(
name|typeName
argument_list|)
condition|)
block|{
name|type
operator|=
name|Types
operator|.
name|BLOB
expr_stmt|;
block|}
comment|// somehow the driver reverse-engineers "text" as VARCHAR, must be CLOB
if|else if
condition|(
literal|"text"
operator|.
name|equalsIgnoreCase
argument_list|(
name|typeName
argument_list|)
condition|)
block|{
name|type
operator|=
name|Types
operator|.
name|CLOB
expr_stmt|;
block|}
return|return
name|super
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
name|scale
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
name|Binding
name|binding
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
name|binding
operator|.
name|setType
argument_list|(
name|mapNTypes
argument_list|(
name|binding
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|bindParameter
argument_list|(
name|statement
argument_list|,
name|binding
argument_list|)
expr_stmt|;
block|}
specifier|private
name|int
name|mapNTypes
parameter_list|(
name|int
name|sqlType
parameter_list|)
block|{
switch|switch
condition|(
name|sqlType
condition|)
block|{
case|case
name|Types
operator|.
name|NCHAR
case|:
return|return
name|Types
operator|.
name|CHAR
return|;
case|case
name|Types
operator|.
name|NCLOB
case|:
return|return
name|Types
operator|.
name|CLOB
return|;
case|case
name|Types
operator|.
name|NVARCHAR
case|:
return|return
name|Types
operator|.
name|VARCHAR
return|;
case|case
name|Types
operator|.
name|LONGNVARCHAR
case|:
return|return
name|Types
operator|.
name|LONGVARCHAR
return|;
default|default:
return|return
name|sqlType
return|;
block|}
block|}
comment|/** 	 * Customizes table creating procedure for PostgreSQL. One difference with 	 * generic implementation is that "bytea" type has no explicit length unlike 	 * similar binary types in other databases. 	 *  	 * @since 1.0.2 	 */
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
name|createAttribute
argument_list|(
name|ent
argument_list|,
name|context
argument_list|,
name|buf
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
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
name|context
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
specifier|private
name|void
name|createAttribute
parameter_list|(
name|DbEntity
name|ent
parameter_list|,
name|QuotingStrategy
name|context
parameter_list|,
name|StringBuilder
name|buf
parameter_list|,
name|DbAttribute
name|at
parameter_list|)
block|{
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
literal|"Undefined type for attribute '"
operator|+
name|ent
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
literal|"."
operator|+
name|at
operator|.
name|getName
argument_list|()
operator|+
literal|"'."
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
literal|"Undefined type for attribute '"
operator|+
name|ent
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
literal|"."
operator|+
name|at
operator|.
name|getName
argument_list|()
operator|+
literal|"': "
operator|+
name|at
operator|.
name|getType
argument_list|()
argument_list|)
throw|;
block|}
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
name|types
index|[
literal|0
index|]
argument_list|)
operator|.
name|append
argument_list|(
name|sizeAndPrecision
argument_list|(
name|this
argument_list|,
name|at
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|at
operator|.
name|isMandatory
argument_list|()
condition|?
literal|" NOT"
else|:
literal|""
argument_list|)
operator|.
name|append
argument_list|(
literal|" NULL"
argument_list|)
expr_stmt|;
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
comment|// "bytea" type does not support length
name|String
index|[]
name|externalTypes
init|=
name|externalTypesForJdbcType
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|externalTypes
operator|!=
literal|null
operator|&&
name|externalTypes
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|String
name|externalType
range|:
name|externalTypes
control|)
block|{
if|if
condition|(
name|BYTEA
operator|.
name|equalsIgnoreCase
argument_list|(
name|externalType
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
name|super
operator|.
name|typeSupportsLength
argument_list|(
name|type
argument_list|)
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
name|QuotingStrategy
name|context
init|=
name|getQuotingStrategy
argument_list|()
decl_stmt|;
return|return
name|Collections
operator|.
name|singleton
argument_list|(
literal|"DROP TABLE "
operator|+
name|context
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
comment|/** 	 * Returns a trimming translator. 	 */
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
name|QualifierTranslator
name|translator
init|=
operator|new
name|PostgresQualifierTranslator
argument_list|(
name|queryAssembler
argument_list|)
decl_stmt|;
name|translator
operator|.
name|setCaseInsensitive
argument_list|(
name|caseInsensitiveCollations
argument_list|)
expr_stmt|;
return|return
name|translator
return|;
block|}
comment|/** 	 * @see JdbcAdapter#createPkGenerator() 	 */
annotation|@
name|Override
specifier|protected
name|PkGenerator
name|createPkGenerator
parameter_list|()
block|{
return|return
operator|new
name|PostgresPkGenerator
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|MergerFactory
name|mergerFactory
parameter_list|()
block|{
return|return
operator|new
name|PostgresMergerFactory
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
literal|false
return|;
block|}
block|}
end_class

end_unit

