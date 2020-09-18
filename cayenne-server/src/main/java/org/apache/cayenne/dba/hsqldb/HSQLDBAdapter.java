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
operator|.
name|hsqldb
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
name|ejbql
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
name|access
operator|.
name|types
operator|.
name|ValueObjectTypeRegistry
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
comment|/**  * DbAdapter implementation for the<a href="http://hsqldb.sourceforge.net/">  * HSQLDB RDBMS</a>. Sample connection settings to use with HSQLDB are shown  * below:  *   *<pre>  *        test-hsqldb.jdbc.username = test  *        test-hsqldb.jdbc.password = secret  *        test-hsqldb.jdbc.url = jdbc:hsqldb:hsql://serverhostname  *        test-hsqldb.jdbc.driver = org.hsqldb.jdbcDriver  *</pre>  */
end_comment

begin_class
specifier|public
class|class
name|HSQLDBAdapter
extends|extends
name|JdbcAdapter
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TRIM_FUNCTION
init|=
literal|"RTRIM"
decl_stmt|;
specifier|public
name|HSQLDBAdapter
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
parameter_list|,
annotation|@
name|Inject
name|ValueObjectTypeRegistry
name|valueObjectTypeRegistry
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
argument_list|,
name|valueObjectTypeRegistry
argument_list|)
expr_stmt|;
name|setSupportsGeneratedKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * @since 4.0 	 */
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
comment|// create specially configured CharType handler
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|CharType
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * @since 4.2 	 */
annotation|@
name|Override
specifier|public
name|SQLTreeProcessor
name|getSqlTreeProcessor
parameter_list|()
block|{
return|return
operator|new
name|HSQLTreeProcessor
argument_list|()
return|;
block|}
comment|/** 	 * @since 4.0 	 */
annotation|@
name|Override
specifier|protected
name|EJBQLTranslatorFactory
name|createEJBQLTranslatorFactory
parameter_list|()
block|{
name|JdbcEJBQLTranslatorFactory
name|translatorFactory
init|=
operator|new
name|HSQLEJBQLTranslatorFactory
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
comment|/** 	 * Generate fully-qualified name for 1.8 and on. Subclass generates 	 * unqualified name. 	 *  	 * @since 1.2 	 */
specifier|protected
name|String
name|getTableName
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
name|quotingStrategy
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|entity
argument_list|)
return|;
block|}
comment|/** 	 * Returns DbEntity schema name for 1.8 and on. Subclass generates 	 * unqualified name. 	 *  	 * @since 1.2 	 */
specifier|protected
name|String
name|getSchemaName
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
return|return
name|entity
operator|.
name|getSchema
argument_list|()
return|;
block|}
comment|/** 	 * Uses special action builder to create the right action. 	 *  	 * @since 1.2 	 */
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
name|HSQLActionBuilder
argument_list|(
name|node
argument_list|)
argument_list|)
return|;
block|}
comment|/** 	 * Returns a DDL string to create a unique constraint over a set of columns. 	 *  	 * @since 1.1 	 */
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
name|String
name|srcName
init|=
name|getTableName
argument_list|(
name|source
argument_list|)
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
literal|"ALTER TABLE "
argument_list|)
operator|.
name|append
argument_list|(
name|srcName
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" ADD CONSTRAINT "
argument_list|)
expr_stmt|;
name|String
name|name
init|=
literal|"U_"
operator|+
name|source
operator|.
name|getName
argument_list|()
operator|+
literal|"_"
operator|+
operator|(
name|long
operator|)
operator|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
operator|(
name|Math
operator|.
name|random
argument_list|()
operator|*
literal|100000
operator|)
operator|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|quotingStrategy
operator|.
name|quotedIdentifier
argument_list|(
name|source
argument_list|,
name|source
operator|.
name|getSchema
argument_list|()
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" UNIQUE ("
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
name|quotingStrategy
operator|.
name|quotedName
argument_list|(
name|first
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
name|quotingStrategy
operator|.
name|quotedName
argument_list|(
name|next
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
comment|/** 	 * Adds an ADD CONSTRAINT clause to a relationship constraint. 	 *  	 * @see JdbcAdapter#createFkConstraint(DbRelationship) 	 */
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
name|String
name|srcName
init|=
name|getTableName
argument_list|(
name|rel
operator|.
name|getSourceEntity
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|dstName
init|=
name|getTableName
argument_list|(
name|rel
operator|.
name|getTargetEntity
argument_list|()
argument_list|)
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
name|srcName
argument_list|)
expr_stmt|;
comment|// hsqldb requires the ADD CONSTRAINT statement
name|buf
operator|.
name|append
argument_list|(
literal|" ADD CONSTRAINT "
argument_list|)
expr_stmt|;
name|String
name|name
init|=
literal|"U_"
operator|+
name|rel
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"_"
operator|+
operator|(
name|long
operator|)
operator|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
operator|(
name|Math
operator|.
name|random
argument_list|()
operator|*
literal|100000
operator|)
operator|)
decl_stmt|;
name|DbEntity
name|sourceEntity
init|=
name|rel
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|quotingStrategy
operator|.
name|quotedIdentifier
argument_list|(
name|sourceEntity
argument_list|,
name|sourceEntity
operator|.
name|getSchema
argument_list|()
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" FOREIGN KEY ("
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
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
name|quotingStrategy
operator|.
name|quotedSourceName
argument_list|(
name|join
argument_list|)
argument_list|)
expr_stmt|;
name|refBuf
operator|.
name|append
argument_list|(
name|quotingStrategy
operator|.
name|quotedTargetName
argument_list|(
name|join
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
name|dstName
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|refBuf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
comment|// also make sure we delete dependent FKs
name|buf
operator|.
name|append
argument_list|(
literal|" ON DELETE CASCADE"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/** 	 * Uses "CREATE CACHED TABLE" instead of "CREATE TABLE". 	 *  	 * @since 1.2 	 */
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
comment|// SET SCHEMA<schemaname>
name|String
name|sql
init|=
name|super
operator|.
name|createTable
argument_list|(
name|ent
argument_list|)
decl_stmt|;
if|if
condition|(
name|sql
operator|!=
literal|null
operator|&&
name|sql
operator|.
name|toUpperCase
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"CREATE TABLE "
argument_list|)
condition|)
block|{
name|sql
operator|=
literal|"CREATE CACHED TABLE "
operator|+
name|sql
operator|.
name|substring
argument_list|(
literal|"CREATE TABLE "
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sql
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
comment|// CAY-1095: if the column is type double, temporarily set the max
comment|// length to 0 to
comment|// avoid adding precision information.
if|if
condition|(
name|column
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|DOUBLE
operator|&&
name|column
operator|.
name|getMaxLength
argument_list|()
operator|>
literal|0
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
name|column
operator|.
name|setMaxLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|super
operator|.
name|createTableAppendColumn
argument_list|(
name|sqlBuffer
argument_list|,
name|column
argument_list|)
expr_stmt|;
name|column
operator|.
name|setMaxLength
argument_list|(
name|len
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|createTableAppendColumn
argument_list|(
name|sqlBuffer
argument_list|,
name|column
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|column
operator|.
name|isGenerated
argument_list|()
condition|)
block|{
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" GENERATED BY DEFAULT AS IDENTITY (START WITH 1)"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

