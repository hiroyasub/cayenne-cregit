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
name|map
package|;
end_package

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
name|query
operator|.
name|Query
import|;
end_import

begin_comment
comment|/**  * Defines API of a container of DbEntities, ObjEntities, Procedures, Queries and other  * mapping objects.  *   * @since 1.1  * @author Andrus Adamchik  */
end_comment

begin_interface
specifier|public
interface|interface
name|MappingNamespace
block|{
comment|/**      * Returns an {@link Embeddable} matching class name or null if such Embeddable is not      * mapped.      */
name|Embeddable
name|getEmbeddable
parameter_list|(
name|String
name|className
parameter_list|)
function_decl|;
comment|/**      * Returns DbEntity for a given name, or null if no such DbEntity is found in the      * MappingNamespace.      */
name|DbEntity
name|getDbEntity
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns ObjEntity for a given name, or null if no such ObjEntity is found in the      * MappingNamespace.      */
name|ObjEntity
name|getObjEntity
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns Procedure for a given name, or null if no such Procedure is found in the      * MappingNamespace.      */
name|Procedure
name|getProcedure
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns Query for a given name, or null if no such Query is found in the      * MappingNamespace.      */
name|Query
name|getQuery
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns all DbEntities in the namespace.      */
name|Collection
name|getDbEntities
parameter_list|()
function_decl|;
comment|/**      * Returns all ObjEntities in the namespace.      */
name|Collection
name|getObjEntities
parameter_list|()
function_decl|;
comment|/**      * Returns all Procedures in the namespace.      */
name|Collection
name|getProcedures
parameter_list|()
function_decl|;
comment|/**      * Returns all Queries in the namespace.      */
name|Collection
name|getQueries
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

