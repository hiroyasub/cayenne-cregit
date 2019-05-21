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

begin_comment
comment|/**  * Defines methods to support automatic primary key generation.  */
end_comment

begin_interface
specifier|public
interface|interface
name|PkGenerator
block|{
comment|/**      * Generates necessary database objects to provide automatic primary key support.      *      * @param node       node that provides access to a DataSource.      * @param dbEntities a list of entities that require primary key auto-generation      *                   support      */
name|void
name|createAutoPk
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns a list of SQL strings needed to generates database objects to provide      * automatic primary support for the list of entities. No actual database operations      * are performed.      */
name|List
argument_list|<
name|String
argument_list|>
name|createAutoPkStatements
parameter_list|(
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|)
function_decl|;
comment|/**      * Drops any common database objects associated with automatic primary key generation      * process. This may be lookup tables, special stored procedures or sequences.      *      * @param node       node that provides access to a DataSource.      * @param dbEntities a list of entities whose primary key auto-generation support      *                   should be dropped.      */
name|void
name|dropAutoPk
parameter_list|(
name|DataNode
name|node
parameter_list|,
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns SQL string needed to drop database objects associated with automatic      * primary key generation. No actual database operations are performed.      */
name|List
argument_list|<
name|String
argument_list|>
name|dropAutoPkStatements
parameter_list|(
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|)
function_decl|;
comment|/**      * Generates a unique and non-repeating primary key for specified PK attribute.      *      * @since 3.0      */
name|Object
name|generatePk
parameter_list|(
name|DataNode
name|dataNode
parameter_list|,
name|DbAttribute
name|pk
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Install the adapter associated with current PkGenerator      *      * @since 4.1      */
name|void
name|setAdapter
parameter_list|(
name|DbAdapter
name|q
parameter_list|)
function_decl|;
comment|/**      * Get an adapter associated with current PkGenerator      *      * @since 4.1      */
name|DbAdapter
name|getAdapter
parameter_list|()
function_decl|;
comment|/**      * Resets any cached primary keys forcing generator to go to the database next time id      * generation is requested. May not be applicable for all generator implementations.      */
name|void
name|reset
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

