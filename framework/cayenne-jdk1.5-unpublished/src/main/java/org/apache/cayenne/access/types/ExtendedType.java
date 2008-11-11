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
name|types
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|CallableStatement
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
name|ResultSet
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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_comment
comment|/**  * Defines methods to read Java objects from JDBC ResultSets and write as parameters of  * PreparedStatements.  *   */
end_comment

begin_interface
specifier|public
interface|interface
name|ExtendedType
block|{
comment|/**      * Returns a full name of Java class that this ExtendedType supports.      */
name|String
name|getClassName
parameter_list|()
function_decl|;
comment|/**      * Performs validation of an object property. Property is considered valid if this it      * satisfies the database constraints known to this ExtendedType. In case of      * validation failure, failures are appended to the ValidationResult object and      *<code>false</code> is returned.      *       * @since 1.1      * @deprecated since 3.0 as validation should not be done at the DataNode level.      */
name|boolean
name|validateProperty
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|value
parameter_list|,
name|DbAttribute
name|dbAttribute
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
function_decl|;
comment|/**      * Initializes a single parameter of a PreparedStatement with object value.      */
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|Object
name|value
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|scale
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Reads an object from JDBC ResultSet column, converting it to class returned by      * 'getClassName' method.      *       * @throws Exception if read error ocurred, or an object can't be converted to a      *             target Java class.      */
name|Object
name|materializeObject
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Reads an object from a stored procedure OUT parameter, converting it to class      * returned by 'getClassName' method.      *       * @throws Exception if read error ocurred, or an object can't be converted to a      *             target Java class.      */
name|Object
name|materializeObject
parameter_list|(
name|CallableStatement
name|rs
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

