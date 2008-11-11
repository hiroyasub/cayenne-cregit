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

begin_comment
comment|/**  * Interface for callback and delegate methods allowing implementing classes to control  * various aspects of data porting via DataPort. DataPort instance will invoke appropriate  * delegate methods during different stages of porting process.  *   * @since 1.2: Prior to 1.2 DataPort classes were a part of cayenne-examples package.  */
end_comment

begin_interface
specifier|public
interface|interface
name|DataPortDelegate
block|{
comment|/**      * Allows delegate to sort or otherwise alter a list of DbEntities right before the      * port starts.      */
name|List
name|willPortEntities
parameter_list|(
name|DataPort
name|portTool
parameter_list|,
name|List
name|entities
parameter_list|)
function_decl|;
comment|/**      * Invoked by DataPort right before the start of data port for a given entity. Allows      * delegate to handle such things like logging, etc. Also makes it possible to      * substitute or alter the select query used to fecth the source data, e.g. set a      * limiting qualifier.      */
name|Query
name|willPortEntity
parameter_list|(
name|DataPort
name|portTool
parameter_list|,
name|DbEntity
name|entity
parameter_list|,
name|Query
name|query
parameter_list|)
function_decl|;
comment|/**      * Invoked by DataPort right after the end of data port for a given entity. Allows      * delegate to handle such things like logging, etc.      */
name|void
name|didPortEntity
parameter_list|(
name|DataPort
name|portTool
parameter_list|,
name|DbEntity
name|entity
parameter_list|,
name|int
name|rowCount
parameter_list|)
function_decl|;
comment|/**      * Allows delegate to sort or otherwise alter a list of DbEntities right before data      * cleanup starts.      */
name|List
name|willCleanData
parameter_list|(
name|DataPort
name|portTool
parameter_list|,
name|List
name|entities
parameter_list|)
function_decl|;
comment|/**      * Invoked by DataPort right before the start of data cleanup for a given entity.      * Allows delegate to handle such things like logging, etc. Also makes it possible to      * substitute or alter the delete query used to cleanup the data, e.g. set a limiting      * qualifier.      */
name|Query
name|willCleanData
parameter_list|(
name|DataPort
name|portTool
parameter_list|,
name|DbEntity
name|entity
parameter_list|,
name|Query
name|query
parameter_list|)
function_decl|;
comment|/**      * Invoked by DataPort right after the end of data cleanup for a given entity. Allows      * delegate to handle such things like logging, etc.      */
name|void
name|didCleanData
parameter_list|(
name|DataPort
name|portTool
parameter_list|,
name|DbEntity
name|entity
parameter_list|,
name|int
name|rowCount
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

