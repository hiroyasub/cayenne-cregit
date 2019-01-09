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
name|mysql
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
name|Node
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
name|ByteArrayType
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
name|DefaultQuotingStrategy
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Comparator
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
name|function
operator|.
name|Function
import|;
end_import

begin_comment
comment|/**  * DbAdapter implementation for<a href="http://www.mysql.com">MySQL RDBMS</a>.  *<h3>  * Foreign Key Constraint Handling</h3>  *<p>  * Foreign key constraints are supported by InnoDB engine and NOT supported by  * MyISAM engine. This adapter by default assumes MyISAM, so  *<code>supportsFkConstraints</code> will be false. Users can manually change  * this by calling<em>setSupportsFkConstraints(true)</em> or better by using an  * {@link org.apache.cayenne.dba.AutoAdapter}, i.e. not entering the adapter  * name at all for the DataNode, letting Cayenne guess it in runtime. In the  * later case Cayenne will check the<em>table_type</em> MySQL variable to  * detect whether InnoDB is the default, and configure the adapter accordingly.  *<h3>Sample Connection Settings</h3>  *<ul>  *<li>Adapter name: org.apache.cayenne.dba.mysql.MySQLAdapter</li>  *<li>DB URL: jdbc:mysql://serverhostname/dbname</li>  *<li>Driver Class: com.mysql.jdbc.Driver</li>  *</ul>  */
end_comment

begin_class
specifier|public
class|class
name|MySQLAdapter
extends|extends
name|JdbcAdapter
block|{
specifier|static
specifier|final
name|String
name|DEFAULT_STORAGE_ENGINE
init|=
literal|"InnoDB"
decl_stmt|;
specifier|static
specifier|final
name|String
name|MYSQL_QUOTE_SQL_IDENTIFIERS_CHAR_START
init|=
literal|"`"
decl_stmt|;
specifier|static
specifier|final
name|String
name|MYSQL_QUOTE_SQL_IDENTIFIERS_CHAR_END
init|=
literal|"`"
decl_stmt|;
specifier|protected
name|String
name|storageEngine
decl_stmt|;
specifier|public
name|MySQLAdapter
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
comment|// init defaults
name|this
operator|.
name|storageEngine
operator|=
name|DEFAULT_STORAGE_ENGINE
expr_stmt|;
name|setSupportsBatchUpdates
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setSupportsUniqueConstraints
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setSupportsGeneratedKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|QuotingStrategy
name|createQuotingStrategy
parameter_list|()
block|{
return|return
operator|new
name|DefaultQuotingStrategy
argument_list|(
literal|"`"
argument_list|,
literal|"`"
argument_list|)
return|;
block|}
comment|/**      * @since 4.2      */
annotation|@
name|Override
specifier|public
name|Function
argument_list|<
name|Node
argument_list|,
name|Node
argument_list|>
name|getSqlTreeProcessor
parameter_list|()
block|{
return|return
name|MySQLTreeProcessor
operator|.
name|getInstance
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
name|MySQLActionBuilder
argument_list|(
name|node
argument_list|)
argument_list|)
return|;
block|}
comment|/** 	 * @since 3.0 	 */
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
comment|// note that CASCADE is a noop as of MySQL 5.0, so we have to use FK
comment|// checks
comment|// statement
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|QuotingStrategy
name|context
init|=
name|getQuotingStrategy
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|table
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
literal|"SET FOREIGN_KEY_CHECKS=0"
argument_list|,
literal|"DROP TABLE IF EXISTS "
operator|+
name|buf
operator|.
name|toString
argument_list|()
operator|+
literal|" CASCADE"
argument_list|,
literal|"SET FOREIGN_KEY_CHECKS=1"
argument_list|)
return|;
block|}
comment|/** 	 * Installs appropriate ExtendedTypes used as converters for passing values 	 * between JDBC and Java layers. 	 */
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
comment|// must handle CLOBs as strings, otherwise there
comment|// are problems with NULL clobs that are treated
comment|// as empty strings... somehow this doesn't happen
comment|// for BLOBs (ConnectorJ v. 3.0.9)
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|CharType
argument_list|(
literal|false
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
name|ByteArrayType
argument_list|(
literal|false
argument_list|,
literal|false
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
name|precision
parameter_list|,
name|boolean
name|allowNulls
parameter_list|)
block|{
if|if
condition|(
name|typeName
operator|!=
literal|null
condition|)
block|{
name|typeName
operator|=
name|typeName
operator|.
name|toLowerCase
argument_list|()
expr_stmt|;
block|}
comment|// all LOB types are returned by the driver as OTHER... must remap them
comment|// manually
comment|// (at least on MySQL 3.23)
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|OTHER
condition|)
block|{
if|if
condition|(
literal|"longblob"
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
if|else if
condition|(
literal|"mediumblob"
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
if|else if
condition|(
literal|"blob"
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
if|else if
condition|(
literal|"tinyblob"
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
name|VARBINARY
expr_stmt|;
block|}
if|else if
condition|(
literal|"longtext"
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
name|CLOB
expr_stmt|;
block|}
if|else if
condition|(
literal|"mediumtext"
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
name|CLOB
expr_stmt|;
block|}
if|else if
condition|(
literal|"text"
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
name|CLOB
expr_stmt|;
block|}
if|else if
condition|(
literal|"tinytext"
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
name|VARCHAR
expr_stmt|;
block|}
block|}
comment|// types like "int unsigned" map to Long
if|else if
condition|(
name|typeName
operator|!=
literal|null
operator|&&
name|typeName
operator|.
name|endsWith
argument_list|(
literal|" unsigned"
argument_list|)
condition|)
block|{
comment|// per
comment|// http://dev.mysql.com/doc/refman/5.0/en/connector-j-reference-type-conversions.html
if|if
condition|(
literal|"int unsigned"
operator|.
name|equals
argument_list|(
name|typeName
argument_list|)
operator|||
literal|"integer unsigned"
operator|.
name|equals
argument_list|(
name|typeName
argument_list|)
operator|||
literal|"mediumint unsigned"
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
name|BIGINT
expr_stmt|;
block|}
comment|// BIGINT UNSIGNED maps to BigInteger according to MySQL docs, but
comment|// there is no
comment|// JDBC mapping for BigInteger
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
name|binding
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
name|binding
operator|.
name|setJdbcType
argument_list|(
name|mapNTypes
argument_list|(
name|binding
operator|.
name|getJdbcType
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
comment|/** 	 * Creates and returns a primary key generator. Overrides superclass 	 * implementation to return an instance of MySQLPkGenerator that does the 	 * correct table locking. 	 */
annotation|@
name|Override
specifier|protected
name|PkGenerator
name|createPkGenerator
parameter_list|()
block|{
return|return
operator|new
name|MySQLPkGenerator
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/** 	 * @since 3.0 	 */
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
name|MySQLEJBQLTranslatorFactory
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
comment|/** 	 * Overrides super implementation to explicitly set table engine to InnoDB 	 * if FK constraints are supported by this adapter. 	 */
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
name|String
name|ddlSQL
init|=
name|super
operator|.
name|createTable
argument_list|(
name|entity
argument_list|)
decl_stmt|;
if|if
condition|(
name|storageEngine
operator|!=
literal|null
condition|)
block|{
name|ddlSQL
operator|+=
literal|" ENGINE="
operator|+
name|storageEngine
expr_stmt|;
block|}
return|return
name|ddlSQL
return|;
block|}
comment|/** 	 * Customizes PK clause semantics to ensure that generated columns are in 	 * the beginning of the PK definition, as this seems to be a requirement for 	 * InnoDB tables. 	 *  	 * @since 1.2 	 */
comment|// See CAY-358 for details of the InnoDB problem
annotation|@
name|Override
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
comment|// must move generated to the front...
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|pkList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|entity
operator|.
name|getPrimaryKeys
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|pkList
argument_list|,
operator|new
name|PKComparator
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|pkit
init|=
name|pkList
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
block|{
name|firstPk
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
name|quotingStrategy
operator|.
name|quotedName
argument_list|(
name|at
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
comment|/** 	 * Appends AUTO_INCREMENT clause to the column definition for generated 	 * columns. 	 */
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
name|column
operator|.
name|getEntity
argument_list|()
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
literal|"Undefined type for attribute '%s.%s': %s"
argument_list|,
name|entityName
argument_list|,
name|column
operator|.
name|getName
argument_list|()
argument_list|,
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
name|quotingStrategy
operator|.
name|quotedName
argument_list|(
name|column
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
name|typeSupportsLength
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
name|TypesMapping
operator|.
name|isDecimal
argument_list|(
name|column
operator|.
name|getType
argument_list|()
argument_list|)
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
literal|" AUTO_INCREMENT"
argument_list|)
expr_stmt|;
block|}
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
comment|// As of MySQL 5.6.4 the "TIMESTAMP" and "TIME" types support length,
comment|// which is the number of decimal places for fractional seconds
comment|// http://dev.mysql.com/doc/refman/5.6/en/fractional-seconds.html
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|Types
operator|.
name|TIMESTAMP
case|:
case|case
name|Types
operator|.
name|TIME
case|:
return|return
literal|true
return|;
default|default:
return|return
name|super
operator|.
name|typeSupportsLength
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
specifier|final
class|class
name|PKComparator
implements|implements
name|Comparator
argument_list|<
name|DbAttribute
argument_list|>
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|DbAttribute
name|a1
parameter_list|,
name|DbAttribute
name|a2
parameter_list|)
block|{
if|if
condition|(
name|a1
operator|.
name|isGenerated
argument_list|()
operator|!=
name|a2
operator|.
name|isGenerated
argument_list|()
condition|)
block|{
return|return
name|a1
operator|.
name|isGenerated
argument_list|()
condition|?
operator|-
literal|1
else|:
literal|1
return|;
block|}
else|else
block|{
return|return
name|a1
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|a2
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
comment|/** 	 * @since 3.0 	 */
specifier|public
name|String
name|getStorageEngine
parameter_list|()
block|{
return|return
name|storageEngine
return|;
block|}
comment|/** 	 * @since 3.0 	 */
specifier|public
name|void
name|setStorageEngine
parameter_list|(
name|String
name|engine
parameter_list|)
block|{
name|this
operator|.
name|storageEngine
operator|=
name|engine
expr_stmt|;
block|}
block|}
end_class

end_unit

