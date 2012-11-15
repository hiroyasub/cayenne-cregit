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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|jdbc
operator|.
name|BatchQueryBuilderFactory
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
name|jdbc
operator|.
name|JdbcEJBQLTranslatorFactory
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
name|trans
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
name|trans
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
name|resource
operator|.
name|ClassLoaderResourceLocator
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
name|Resource
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * A generic DbAdapter implementation. Can be used as a default adapter or as a  * superclass of a concrete adapter implementation.  */
end_comment

begin_class
specifier|public
class|class
name|JdbcAdapter
implements|implements
name|DbAdapter
block|{
comment|// defines if database uses case-insensitive collation
specifier|public
specifier|final
specifier|static
name|String
name|CI_PROPERTY
init|=
literal|"cayenne.runtime.db.collation.assume.ci"
decl_stmt|;
specifier|final
specifier|static
name|String
name|DEFAULT_IDENTIFIERS_START_QUOTE
init|=
literal|"\""
decl_stmt|;
specifier|final
specifier|static
name|String
name|DEFAULT_IDENTIFIERS_END_QUOTE
init|=
literal|"\""
decl_stmt|;
specifier|private
name|PkGenerator
name|pkGenerator
decl_stmt|;
specifier|private
specifier|final
name|QuotingStrategy
name|noQuotingStrategy
decl_stmt|;
specifier|private
name|QuotingStrategy
name|quotingStrategy
decl_stmt|;
specifier|protected
name|TypesHandler
name|typesHandler
decl_stmt|;
specifier|protected
name|ExtendedTypeMap
name|extendedTypes
decl_stmt|;
specifier|protected
name|boolean
name|supportsBatchUpdates
decl_stmt|;
specifier|protected
name|boolean
name|supportsUniqueConstraints
decl_stmt|;
specifier|protected
name|boolean
name|supportsGeneratedKeys
decl_stmt|;
specifier|protected
name|EJBQLTranslatorFactory
name|ejbqlTranslatorFactory
decl_stmt|;
specifier|protected
name|String
name|identifiersStartQuote
decl_stmt|;
specifier|protected
name|String
name|identifiersEndQuote
decl_stmt|;
specifier|protected
name|ResourceLocator
name|resourceLocator
decl_stmt|;
specifier|protected
name|boolean
name|caseInsensitiveCollations
decl_stmt|;
comment|/**      * @since 3.1      */
annotation|@
name|Inject
specifier|protected
name|BatchQueryBuilderFactory
name|batchQueryBuilderFactory
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|JdbcEventLogger
name|logger
decl_stmt|;
comment|/**      * @since 3.0      */
specifier|public
name|String
name|getIdentifiersStartQuote
parameter_list|()
block|{
return|return
name|identifiersStartQuote
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|String
name|getIdentifiersEndQuote
parameter_list|()
block|{
return|return
name|identifiersEndQuote
return|;
block|}
comment|/**      * Creates new JdbcAdapter with a set of default parameters.      */
specifier|public
name|JdbcAdapter
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
parameter_list|)
block|{
name|this
operator|.
name|noQuotingStrategy
operator|=
operator|new
name|NoQuotingStrategy
argument_list|()
expr_stmt|;
comment|// init defaults
name|this
operator|.
name|setSupportsBatchUpdates
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|setSupportsUniqueConstraints
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|caseInsensitiveCollations
operator|=
name|runtimeProperties
operator|.
name|getBoolean
argument_list|(
name|CI_PROPERTY
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// TODO: andrus 05.02.2010 - ideally this should be injected
name|this
operator|.
name|resourceLocator
operator|=
operator|new
name|ClassLoaderResourceLocator
argument_list|()
expr_stmt|;
name|this
operator|.
name|pkGenerator
operator|=
name|createPkGenerator
argument_list|()
expr_stmt|;
name|this
operator|.
name|ejbqlTranslatorFactory
operator|=
name|createEJBQLTranslatorFactory
argument_list|()
expr_stmt|;
name|this
operator|.
name|typesHandler
operator|=
name|TypesHandler
operator|.
name|getHandler
argument_list|(
name|findResource
argument_list|(
literal|"/types.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|extendedTypes
operator|=
operator|new
name|ExtendedTypeMap
argument_list|()
expr_stmt|;
name|initExtendedTypes
argument_list|(
name|defaultExtendedTypes
argument_list|,
name|userExtendedTypes
argument_list|,
name|extendedTypeFactories
argument_list|)
expr_stmt|;
name|initIdentifiersQuotes
argument_list|()
expr_stmt|;
name|this
operator|.
name|quotingStrategy
operator|=
operator|new
name|DefaultQuotingStrategy
argument_list|(
name|identifiersStartQuote
argument_list|,
name|identifiersEndQuote
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns default separator - a semicolon.      *       * @since 1.0.4      */
specifier|public
name|String
name|getBatchTerminator
parameter_list|()
block|{
return|return
literal|";"
return|;
block|}
comment|/**      * @since 3.1      */
specifier|public
name|JdbcEventLogger
name|getJdbcEventLogger
parameter_list|()
block|{
return|return
name|this
operator|.
name|logger
return|;
block|}
comment|/**      * Locates and returns a named adapter resource. A resource can be an XML      * file, etc.      *<p>      * This implementation is based on the premise that each adapter is located      * in its own Java package and all resources are in the same package as      * well. Resource lookup is recursive, so that if DbAdapter is a subclass of      * another adapter, parent adapter package is searched as a failover.      *</p>      *       * @since 3.0      */
specifier|protected
name|URL
name|findResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|adapterClass
init|=
name|getClass
argument_list|()
decl_stmt|;
while|while
condition|(
name|adapterClass
operator|!=
literal|null
operator|&&
name|JdbcAdapter
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|adapterClass
argument_list|)
condition|)
block|{
name|String
name|path
init|=
name|Util
operator|.
name|getPackagePath
argument_list|(
name|adapterClass
operator|.
name|getName
argument_list|()
argument_list|)
operator|+
name|name
decl_stmt|;
name|Collection
argument_list|<
name|Resource
argument_list|>
name|resources
init|=
name|resourceLocator
operator|.
name|findResources
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|resources
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|resources
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getURL
argument_list|()
return|;
block|}
name|adapterClass
operator|=
name|adapterClass
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Called from {@link #initExtendedTypes(List, List, List)} to load      * adapter-specific types into the ExtendedTypeMap right after the default      * types are loaded, but before the DI overrides are. This method has      * specific implementations in JdbcAdapter subclasses.      */
specifier|protected
name|void
name|configureExtendedTypes
parameter_list|(
name|ExtendedTypeMap
name|map
parameter_list|)
block|{
comment|// noop... subclasses may override to install custom types
block|}
comment|/**      * @since 3.1      */
specifier|protected
name|void
name|initExtendedTypes
parameter_list|(
name|List
argument_list|<
name|ExtendedType
argument_list|>
name|defaultExtendedTypes
parameter_list|,
name|List
argument_list|<
name|ExtendedType
argument_list|>
name|userExtendedTypes
parameter_list|,
name|List
argument_list|<
name|ExtendedTypeFactory
argument_list|>
name|extendedTypeFactories
parameter_list|)
block|{
for|for
control|(
name|ExtendedType
name|type
range|:
name|defaultExtendedTypes
control|)
block|{
name|extendedTypes
operator|.
name|registerType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
comment|// loading adapter specific extended types
name|configureExtendedTypes
argument_list|(
name|extendedTypes
argument_list|)
expr_stmt|;
for|for
control|(
name|ExtendedType
name|type
range|:
name|userExtendedTypes
control|)
block|{
name|extendedTypes
operator|.
name|registerType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|ExtendedTypeFactory
name|typeFactory
range|:
name|extendedTypeFactories
control|)
block|{
name|extendedTypes
operator|.
name|addFactory
argument_list|(
name|typeFactory
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates and returns a primary key generator. This factory method should      * be overriden by JdbcAdapter subclasses to provide custom implementations      * of PKGenerator.      */
specifier|protected
name|PkGenerator
name|createPkGenerator
parameter_list|()
block|{
return|return
operator|new
name|JdbcPkGenerator
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Creates and returns an {@link EJBQLTranslatorFactory} used to generate      * visitors for EJBQL to SQL translations. This method should be overriden      * by subclasses that need to customize EJBQL generation.      *       * @since 3.0      */
specifier|protected
name|EJBQLTranslatorFactory
name|createEJBQLTranslatorFactory
parameter_list|()
block|{
name|JdbcEJBQLTranslatorFactory
name|translatorFactory
init|=
operator|new
name|JdbcEJBQLTranslatorFactory
argument_list|()
decl_stmt|;
name|translatorFactory
operator|.
name|setCaseInsensitive
argument_list|(
name|caseInsensitiveCollations
argument_list|)
expr_stmt|;
return|return
name|translatorFactory
return|;
block|}
comment|/**      * Returns primary key generator associated with this DbAdapter.      */
specifier|public
name|PkGenerator
name|getPkGenerator
parameter_list|()
block|{
return|return
name|pkGenerator
return|;
block|}
comment|/**      * Sets new primary key generator.      *       * @since 1.1      */
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
comment|/**      * Returns true.      *       * @since 1.1      */
specifier|public
name|boolean
name|supportsUniqueConstraints
parameter_list|()
block|{
return|return
name|supportsUniqueConstraints
return|;
block|}
comment|/**      * @since 1.1      */
specifier|public
name|void
name|setSupportsUniqueConstraints
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|this
operator|.
name|supportsUniqueConstraints
operator|=
name|flag
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
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
argument_list|(
name|table
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
argument_list|)
decl_stmt|;
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"DROP TABLE "
argument_list|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteFullyQualifiedName
argument_list|(
name|table
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns a SQL string that can be used to create database table      * corresponding to<code>ent</code> parameter.      */
specifier|public
name|String
name|createTable
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
name|boolean
name|status
init|=
operator|(
name|entity
operator|.
name|getDataMap
argument_list|()
operator|!=
literal|null
operator|&&
name|entity
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
operator|)
decl_stmt|;
name|QuotingStrategy
name|context
init|=
name|getQuotingStrategy
argument_list|(
name|status
argument_list|)
decl_stmt|;
name|StringBuffer
name|sqlBuffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|"CREATE TABLE "
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteFullyQualifiedName
argument_list|(
name|entity
argument_list|)
argument_list|)
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
expr_stmt|;
comment|// columns
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|entity
operator|.
name|getAttributes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
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
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|DbAttribute
name|column
init|=
operator|(
name|DbAttribute
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// attribute may not be fully valid, do a simple check
if|if
condition|(
name|column
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
name|entity
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
literal|"."
operator|+
name|column
operator|.
name|getName
argument_list|()
operator|+
literal|"'."
argument_list|)
throw|;
block|}
name|createTableAppendColumn
argument_list|(
name|sqlBuffer
argument_list|,
name|column
argument_list|)
expr_stmt|;
block|}
name|createTableAppendPKClause
argument_list|(
name|sqlBuffer
argument_list|,
name|entity
argument_list|)
expr_stmt|;
block|}
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
return|return
name|sqlBuffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * @since 1.2      */
specifier|protected
name|void
name|createTableAppendPKClause
parameter_list|(
name|StringBuffer
name|sqlBuffer
parameter_list|,
name|DbEntity
name|entity
parameter_list|)
block|{
name|boolean
name|status
init|=
operator|(
name|entity
operator|.
name|getDataMap
argument_list|()
operator|!=
literal|null
operator|&&
name|entity
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
operator|)
decl_stmt|;
name|QuotingStrategy
name|context
init|=
name|getQuotingStrategy
argument_list|(
name|status
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|pkit
init|=
name|entity
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
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|", PRIMARY KEY ("
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
name|firstPk
operator|=
literal|false
expr_stmt|;
else|else
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|DbAttribute
name|at
init|=
name|pkit
operator|.
name|next
argument_list|()
decl_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteString
argument_list|(
name|at
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Appends SQL for column creation to CREATE TABLE buffer.      *       * @since 1.2      */
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
name|boolean
name|status
init|=
operator|(
operator|(
name|column
operator|.
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
operator|!=
literal|null
operator|)
operator|&&
name|column
operator|.
name|getEntity
argument_list|()
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
operator|)
decl_stmt|;
name|QuotingStrategy
name|context
init|=
name|getQuotingStrategy
argument_list|(
name|status
argument_list|)
decl_stmt|;
name|String
index|[]
name|types
init|=
name|externalTypesForJdbcType
argument_list|(
name|column
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
name|String
name|entityName
init|=
name|column
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
condition|?
operator|(
operator|(
name|DbEntity
operator|)
name|column
operator|.
name|getEntity
argument_list|()
operator|)
operator|.
name|getFullyQualifiedName
argument_list|()
else|:
literal|"<null>"
decl_stmt|;
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Undefined type for attribute '"
operator|+
name|entityName
operator|+
literal|"."
operator|+
name|column
operator|.
name|getName
argument_list|()
operator|+
literal|"': "
operator|+
name|column
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
name|sqlBuffer
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteString
argument_list|(
name|column
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sqlBuffer
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
comment|// append size and precision (if applicable)s
if|if
condition|(
name|TypesMapping
operator|.
name|supportsLength
argument_list|(
name|column
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|int
name|len
init|=
name|column
operator|.
name|getMaxLength
argument_list|()
decl_stmt|;
name|int
name|scale
init|=
operator|(
name|TypesMapping
operator|.
name|isDecimal
argument_list|(
name|column
operator|.
name|getType
argument_list|()
argument_list|)
operator|&&
name|column
operator|.
name|getType
argument_list|()
operator|!=
name|Types
operator|.
name|FLOAT
operator|)
condition|?
name|column
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
name|sqlBuffer
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
name|sqlBuffer
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
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
block|}
name|sqlBuffer
operator|.
name|append
argument_list|(
name|column
operator|.
name|isMandatory
argument_list|()
condition|?
literal|" NOT NULL"
else|:
literal|" NULL"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a DDL string to create a unique constraint over a set of columns.      *       * @since 1.1      */
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
name|boolean
name|status
init|=
operator|(
name|source
operator|.
name|getDataMap
argument_list|()
operator|!=
literal|null
operator|&&
name|source
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
operator|)
decl_stmt|;
name|QuotingStrategy
name|context
init|=
name|getQuotingStrategy
argument_list|(
name|status
argument_list|)
decl_stmt|;
if|if
condition|(
name|columns
operator|==
literal|null
operator|||
name|columns
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't create UNIQUE constraint - no columns specified."
argument_list|)
throw|;
block|}
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
literal|"ALTER TABLE "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteFullyQualifiedName
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" ADD UNIQUE ("
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|it
init|=
name|columns
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|DbAttribute
name|first
init|=
name|it
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
name|quoteString
argument_list|(
name|first
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbAttribute
name|next
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteString
argument_list|(
name|next
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns a SQL string that can be used to create a foreign key constraint      * for the relationship.      */
specifier|public
name|String
name|createFkConstraint
parameter_list|(
name|DbRelationship
name|rel
parameter_list|)
block|{
name|DbEntity
name|source
init|=
operator|(
name|DbEntity
operator|)
name|rel
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
name|boolean
name|status
init|=
operator|(
name|source
operator|.
name|getDataMap
argument_list|()
operator|!=
literal|null
operator|&&
name|source
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
operator|)
decl_stmt|;
name|QuotingStrategy
name|context
init|=
name|getQuotingStrategy
argument_list|(
name|status
argument_list|)
decl_stmt|;
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|StringBuilder
name|refBuf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"ALTER TABLE "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteFullyQualifiedName
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" ADD FOREIGN KEY ("
argument_list|)
expr_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|DbJoin
name|join
range|:
name|rel
operator|.
name|getJoins
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|first
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
name|refBuf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
else|else
name|first
operator|=
literal|false
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteString
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|refBuf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteString
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|") REFERENCES "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteFullyQualifiedName
argument_list|(
operator|(
name|DbEntity
operator|)
name|rel
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
operator|.
name|append
argument_list|(
name|refBuf
operator|.
name|toString
argument_list|()
argument_list|)
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
name|typesHandler
operator|.
name|externalTypesForJdbcType
argument_list|(
name|type
argument_list|)
return|;
block|}
specifier|public
name|ExtendedTypeMap
name|getExtendedTypes
parameter_list|()
block|{
return|return
name|extendedTypes
return|;
block|}
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
name|DbAttribute
name|attr
init|=
operator|new
name|DbAttribute
argument_list|()
decl_stmt|;
name|attr
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|attr
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|attr
operator|.
name|setMandatory
argument_list|(
operator|!
name|allowNulls
argument_list|)
expr_stmt|;
if|if
condition|(
name|size
operator|>=
literal|0
condition|)
block|{
name|attr
operator|.
name|setMaxLength
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|scale
operator|>=
literal|0
condition|)
block|{
name|attr
operator|.
name|setScale
argument_list|(
name|scale
argument_list|)
expr_stmt|;
block|}
return|return
name|attr
return|;
block|}
specifier|public
name|String
name|tableTypeForTable
parameter_list|()
block|{
return|return
literal|"TABLE"
return|;
block|}
specifier|public
name|String
name|tableTypeForView
parameter_list|()
block|{
return|return
literal|"VIEW"
return|;
block|}
comment|/**      * Creates and returns a default implementation of a qualifier translator.      */
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
name|QualifierTranslator
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
comment|/**      * Uses JdbcActionBuilder to create the right action.      *       * @since 1.2      */
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
name|JdbcActionBuilder
argument_list|(
name|this
argument_list|,
name|node
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|bindParameter
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|Object
name|object
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|sqlType
parameter_list|,
name|int
name|scale
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
name|statement
operator|.
name|setNull
argument_list|(
name|pos
argument_list|,
name|sqlType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ExtendedType
name|typeProcessor
init|=
name|getExtendedTypes
argument_list|()
operator|.
name|getRegisteredType
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|typeProcessor
operator|.
name|setJdbcObject
argument_list|(
name|statement
argument_list|,
name|object
argument_list|,
name|pos
argument_list|,
name|sqlType
argument_list|,
name|scale
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|supportsBatchUpdates
parameter_list|()
block|{
return|return
name|this
operator|.
name|supportsBatchUpdates
return|;
block|}
specifier|public
name|void
name|setSupportsBatchUpdates
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|this
operator|.
name|supportsBatchUpdates
operator|=
name|flag
expr_stmt|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|boolean
name|supportsGeneratedKeys
parameter_list|()
block|{
return|return
name|supportsGeneratedKeys
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
name|void
name|setSupportsGeneratedKeys
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|this
operator|.
name|supportsGeneratedKeys
operator|=
name|flag
expr_stmt|;
block|}
comment|/**      * Returns a translator factory for EJBQL to SQL translation. This property      * is normally initialized in constructor by calling      * {@link #createEJBQLTranslatorFactory()}, and can be overridden by calling      * {@link #setEjbqlTranslatorFactory(EJBQLTranslatorFactory)}.      *       * @since 3.0      */
specifier|public
name|EJBQLTranslatorFactory
name|getEjbqlTranslatorFactory
parameter_list|()
block|{
return|return
name|ejbqlTranslatorFactory
return|;
block|}
comment|/**      * Sets a translator factory for EJBQL to SQL translation. This property is      * normally initialized in constructor by calling      * {@link #createEJBQLTranslatorFactory()}, so users would only override it      * if they need to customize EJBQL translation.      *       * @since 3.0      */
specifier|public
name|void
name|setEjbqlTranslatorFactory
parameter_list|(
name|EJBQLTranslatorFactory
name|ejbqlTranslatorFactory
parameter_list|)
block|{
name|this
operator|.
name|ejbqlTranslatorFactory
operator|=
name|ejbqlTranslatorFactory
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|MergerFactory
name|mergerFactory
parameter_list|()
block|{
return|return
operator|new
name|MergerFactory
argument_list|()
return|;
block|}
comment|/**      * @since 3.0      */
specifier|protected
name|void
name|initIdentifiersQuotes
parameter_list|()
block|{
name|this
operator|.
name|identifiersStartQuote
operator|=
name|DEFAULT_IDENTIFIERS_START_QUOTE
expr_stmt|;
name|this
operator|.
name|identifiersEndQuote
operator|=
name|DEFAULT_IDENTIFIERS_END_QUOTE
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|QuotingStrategy
name|getQuotingStrategy
parameter_list|(
name|boolean
name|needQuotes
parameter_list|)
block|{
return|return
operator|(
name|needQuotes
operator|)
condition|?
name|quotingStrategy
else|:
name|noQuotingStrategy
return|;
block|}
comment|/**      * @since 3.1      */
specifier|public
name|BatchQueryBuilderFactory
name|getBatchQueryBuilderFactory
parameter_list|()
block|{
return|return
name|batchQueryBuilderFactory
return|;
block|}
comment|/**      * @since 3.1      */
specifier|public
name|void
name|setBatchQueryBuilderFactory
parameter_list|(
name|BatchQueryBuilderFactory
name|batchQueryBuilderFactory
parameter_list|)
block|{
name|this
operator|.
name|batchQueryBuilderFactory
operator|=
name|batchQueryBuilderFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

