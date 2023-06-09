begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *<p/>  * https://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  ****************************************************************/
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
comment|/**  * A Cayenne extension point that abstracts the differences between specifics of  * JDBC interfaces to various databases. Cayenne already ships with a number of  * built-in adapters for most common databases and users can provide their own  * custom adapters.  */
end_comment

begin_interface
specifier|public
interface|interface
name|DbAdapter
block|{
comment|/** 	 * Returns a String used to terminate a batch in command-line tools. E.g. 	 * ";" on Oracle or "go" on Sybase. 	 * 	 * @since 1.0.4 	 */
name|String
name|getBatchTerminator
parameter_list|()
function_decl|;
comment|/** 	 * @since 4.2 	 */
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
function_decl|;
comment|/** 	 * @since 4.2 	 * @return {@link SQLTreeProcessor} that can adjust SQL tree to specific database flavour 	 */
name|SQLTreeProcessor
name|getSqlTreeProcessor
parameter_list|()
function_decl|;
comment|/** 	 * Returns an instance of SQLAction that should handle the query. 	 * 	 * @since 1.2 	 */
name|SQLAction
name|getAction
parameter_list|(
name|Query
name|query
parameter_list|,
name|DataNode
name|node
parameter_list|)
function_decl|;
comment|/** 	 * Returns true if a target database supports UNIQUE constraints. 	 * 	 * @since 1.1 	 */
name|boolean
name|supportsUniqueConstraints
parameter_list|()
function_decl|;
comment|/** 	 * Returns true if a target database supports catalogs on reverse 	 * engineering. 	 * 	 * @since 4.0 	 */
name|boolean
name|supportsCatalogsOnReverseEngineering
parameter_list|()
function_decl|;
comment|/** 	 * Returns true if a target database supports key autogeneration. This 	 * feature also requires JDBC3-compliant driver. 	 * 	 * @since 1.2 	 */
name|boolean
name|supportsGeneratedKeys
parameter_list|()
function_decl|;
comment|/** 	 * Returns true if a target database supports key autogeneration in a batch insert. 	 * @see #supportsGeneratedKeys()      * @since 4.2      */
specifier|default
name|boolean
name|supportsGeneratedKeysForBatchInserts
parameter_list|()
block|{
return|return
name|supportsGeneratedKeys
argument_list|()
return|;
block|}
comment|/** 	 * Returns<code>true</code> if the target database supports batch updates. 	 */
name|boolean
name|supportsBatchUpdates
parameter_list|()
function_decl|;
name|boolean
name|typeSupportsLength
parameter_list|(
name|int
name|type
parameter_list|)
function_decl|;
comment|/** 	 * Returns a collection of SQL statements needed to drop a database table. 	 * 	 * @since 3.0 	 */
name|Collection
argument_list|<
name|String
argument_list|>
name|dropTableStatements
parameter_list|(
name|DbEntity
name|table
parameter_list|)
function_decl|;
comment|/** 	 * Returns a SQL string that can be used to create database table 	 * corresponding to<code>entity</code> parameter. 	 */
name|String
name|createTable
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
function_decl|;
comment|/** 	 * Returns a DDL string to create a unique constraint over a set of columns, 	 * or null if the unique constraints are not supported. 	 * 	 * @since 1.1 	 */
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
function_decl|;
comment|/** 	 * Returns a SQL string that can be used to create a foreign key constraint 	 * for the relationship, or null if foreign keys are not supported. 	 */
name|String
name|createFkConstraint
parameter_list|(
name|DbRelationship
name|rel
parameter_list|)
function_decl|;
comment|/** 	 * Returns an array of RDBMS types that can be used with JDBC 	 *<code>type</code>. Valid JDBC types are defined in java.sql.Types. 	 */
name|String
index|[]
name|externalTypesForJdbcType
parameter_list|(
name|int
name|type
parameter_list|)
function_decl|;
comment|/** 	 * Returns a map of ExtendedTypes that is used to translate values between 	 * Java and JDBC layer. 	 */
name|ExtendedTypeMap
name|getExtendedTypes
parameter_list|()
function_decl|;
comment|/** 	 * Returns primary key generator associated with this DbAdapter. 	 */
name|PkGenerator
name|getPkGenerator
parameter_list|()
function_decl|;
comment|/** 	 * Set custom PK generator  associated with this DbAdapter. 	 * @param pkGenerator to set 	 * @since 4.1 	 */
name|void
name|setPkGenerator
parameter_list|(
name|PkGenerator
name|pkGenerator
parameter_list|)
function_decl|;
comment|/** 	 * Creates and returns a DbAttribute based on supplied parameters (usually 	 * obtained from database meta data). 	 * 	 * @param name 	 *            database column name 	 * @param typeName 	 *            database specific type name, may be used as a hint to 	 *            determine the right JDBC type. 	 * @param type 	 *            JDBC column type 	 * @param size 	 *            database column size (ignored if less than zero) 	 * @param scale 	 *            database column scale, i.e. the number of decimal digits 	 *            (ignored if less than zero) 	 * @param allowNulls 	 *            database column nullable parameter 	 */
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
function_decl|;
comment|/** 	 * Binds an object value to PreparedStatement's parameter. 	 */
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
function_decl|;
comment|/** 	 * Returns the name of the table type (as returned by 	 *<code>DatabaseMetaData.getTableTypes</code>) for a simple user table. 	 */
name|String
name|tableTypeForTable
parameter_list|()
function_decl|;
comment|/** 	 * Returns the name of the table type (as returned by 	 *<code>DatabaseMetaData.getTableTypes</code>) for a view table. 	 */
name|String
name|tableTypeForView
parameter_list|()
function_decl|;
comment|/** 	 * Append the column type part of a "create table" to the given 	 * {@link StringBuffer} 	 * 	 * @param sqlBuffer 	 *            the {@link StringBuffer} to append the column type to 	 * @param column 	 *            the {@link DbAttribute} defining the column to append type for 	 * @since 3.0 	 */
name|void
name|createTableAppendColumn
parameter_list|(
name|StringBuffer
name|sqlBuffer
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
function_decl|;
comment|/** 	 * Returns SQL identifier quoting strategy object 	 * 	 * @since 4.0 	 */
name|QuotingStrategy
name|getQuotingStrategy
parameter_list|()
function_decl|;
comment|/** 	 * Allows the users to get access to the adapter decorated by a given 	 * adapter. 	 * 	 * @since 4.0 	 */
name|DbAdapter
name|unwrap
parameter_list|()
function_decl|;
comment|/** 	 * Returns a translator factory for EJBQL to SQL translation. 	 * 	 * @since 4.0 	 */
name|EJBQLTranslatorFactory
name|getEjbqlTranslatorFactory
parameter_list|()
function_decl|;
comment|/** 	 * @since 4.1 	 * @return list of system catalogs 	 */
name|List
argument_list|<
name|String
argument_list|>
name|getSystemCatalogs
parameter_list|()
function_decl|;
comment|/** 	 * @since 4.1 	 * @return list of system schemas 	 */
name|List
argument_list|<
name|String
argument_list|>
name|getSystemSchemas
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

