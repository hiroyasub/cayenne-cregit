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

begin_comment
comment|/**  * Defines API needed to handle differences between various databases accessed via JDBC.  * Implementing classed are intended to be pluggable database-specific adapters.  * DbAdapter-based architecture is introduced to solve the following problems:  *<ul>  *<li>Make Cayenne code independent from SQL syntax differences between different RDBMS.  *<li>Allow for vendor-specific tuning of JDBC access.  *</ul>  *   * @author Andrus Adamchik  */
end_comment

begin_interface
specifier|public
interface|interface
name|DbAdapter
block|{
comment|/**      * Returns a String used to terminate a batch in command-line tools. E.g. ";" on      * Oracle or "go" on Sybase.      *       * @since 1.0.4      */
specifier|public
name|String
name|getBatchTerminator
parameter_list|()
function_decl|;
comment|// TODO: deprecate and move into SQLAction implementation
specifier|public
name|QualifierTranslator
name|getQualifierTranslator
parameter_list|(
name|QueryAssembler
name|queryAssembler
parameter_list|)
function_decl|;
comment|/**      * Returns an instance of SQLAction that should handle the query.      *       * @since 1.2      */
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
function_decl|;
comment|/**      * Returns true if a target database supports FK constraints.      *       * @deprecated since 3.0 - almost all DB's support FK's now and also this flag is less      *             relevant for Cayenne now.      */
specifier|public
name|boolean
name|supportsFkConstraints
parameter_list|()
function_decl|;
comment|/**      * Returns true if a target database supports UNIQUE constraints.      *       * @since 1.1      */
specifier|public
name|boolean
name|supportsUniqueConstraints
parameter_list|()
function_decl|;
comment|/**      * Returns true if a target database supports key autogeneration. This feature also      * requires JDBC3-compliant driver.      *       * @since 1.2      */
specifier|public
name|boolean
name|supportsGeneratedKeys
parameter_list|()
function_decl|;
comment|/**      * Returns<code>true</code> if the target database supports batch updates.      */
specifier|public
name|boolean
name|supportsBatchUpdates
parameter_list|()
function_decl|;
comment|/**      * Returns a SQL string that can be used to drop a database table corresponding to      *<code>ent</code> parameter.      */
specifier|public
name|String
name|dropTable
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
function_decl|;
comment|/**      * Returns a SQL string that can be used to create database table corresponding to      *<code>entity</code> parameter.      */
specifier|public
name|String
name|createTable
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
function_decl|;
comment|/**      * Returns a DDL string to create a unique constraint over a set of columns, or null       * if the unique constraints are not supported.      *       * @since 1.1      */
specifier|public
name|String
name|createUniqueConstraint
parameter_list|(
name|DbEntity
name|source
parameter_list|,
name|Collection
name|columns
parameter_list|)
function_decl|;
comment|/**      * Returns a SQL string that can be used to create a foreign key constraint for the      * relationship, or null if foreign keys are not supported.      */
specifier|public
name|String
name|createFkConstraint
parameter_list|(
name|DbRelationship
name|rel
parameter_list|)
function_decl|;
comment|/**      * Returns an array of RDBMS types that can be used with JDBC<code>type</code>.      * Valid JDBC types are defined in java.sql.Types.      */
specifier|public
name|String
index|[]
name|externalTypesForJdbcType
parameter_list|(
name|int
name|type
parameter_list|)
function_decl|;
comment|/**      * Returns a map of ExtendedTypes that is used to translate values between Java and      * JDBC layer.      *       * @see org.apache.cayenne.access.types.ExtendedType      */
specifier|public
name|ExtendedTypeMap
name|getExtendedTypes
parameter_list|()
function_decl|;
comment|/**      * Returns primary key generator associated with this DbAdapter.      */
specifier|public
name|PkGenerator
name|getPkGenerator
parameter_list|()
function_decl|;
comment|/**      * Creates and returns a DbAttribute based on supplied parameters (usually obtained      * from database meta data).      *       * @param name database column name      * @param typeName database specific type name, may be used as a hint to determine the      *            right JDBC type.      * @param type JDBC column type      * @param size database column size (ignored if less than zero)      * @param scale database column scale, i.e. the number of decimal digits (ignored if      *            less than zero)      * @param allowNulls database column nullable parameter      */
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
function_decl|;
comment|/**      * Binds an object value to PreparedStatement's numbered parameter.      */
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
function_decl|;
comment|/**      * Returns the name of the table type (as returned by      *<code>DatabaseMetaData.getTableTypes</code>) for a simple user table.      */
specifier|public
name|String
name|tableTypeForTable
parameter_list|()
function_decl|;
comment|/**      * Returns the name of the table type (as returned by      *<code>DatabaseMetaData.getTableTypes</code>) for a view table.      */
specifier|public
name|String
name|tableTypeForView
parameter_list|()
function_decl|;
specifier|public
name|MergerFactory
name|mergerFactory
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

