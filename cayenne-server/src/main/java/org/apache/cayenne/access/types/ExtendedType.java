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
name|java
operator|.
name|util
operator|.
name|Optional
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
name|ChildProcessor
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

begin_comment
comment|/**  * Defines methods to read Java objects from JDBC ResultSets and write as parameters of PreparedStatements.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ExtendedType
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Defines trimming constant for toString method that helps to limit logging of large values.      */
name|int
name|TRIM_VALUES_THRESHOLD
init|=
literal|30
decl_stmt|;
comment|/**      * Returns a full name of Java class that this ExtendedType supports.      */
name|String
name|getClassName
parameter_list|()
function_decl|;
comment|/**      * Initializes a single parameter of a PreparedStatement with object value.      */
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|T
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
comment|/**      * Reads an object from JDBC ResultSet column, converting it to class returned by      * 'getClassName' method.      *      * @throws Exception if read error occurred, or an object can't be converted to a      *                   target Java class.      */
name|T
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
comment|/**      * Reads an object from a stored procedure OUT parameter, converting it to class      * returned by 'getClassName' method.      *      * @throws Exception if read error occurred, or an object can't be converted to a      *                   target Java class.      */
name|T
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
comment|/**      * Converts value of the supported type to a human-readable String representation.      *      * @param value a value to convert to String.      * @since 4.0      */
name|String
name|toString
parameter_list|(
name|T
name|value
parameter_list|)
function_decl|;
comment|/**      * @since 4.2      * @return      */
specifier|default
name|ChildProcessor
argument_list|<
name|?
argument_list|>
name|readProcessor
parameter_list|()
block|{
return|return
name|ChildProcessor
operator|.
name|EMPTY
return|;
block|}
comment|/**      * @since 4.2      * @return      */
specifier|default
name|ChildProcessor
argument_list|<
name|?
argument_list|>
name|writeProcessor
parameter_list|()
block|{
return|return
name|ChildProcessor
operator|.
name|EMPTY
return|;
block|}
block|}
end_interface

end_unit

