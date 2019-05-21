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
name|Persistent
import|;
end_import

begin_comment
comment|/**  * Defines API of a container of DbEntities, ObjEntities, Procedures, Queries  * and other mapping objects.  *   * @since 1.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|MappingNamespace
block|{
comment|/**      * Returns an {@link Embeddable} matching class name or null if such      * Embeddable is not mapped.      *       * @since 3.0      */
name|Embeddable
name|getEmbeddable
parameter_list|(
name|String
name|className
parameter_list|)
function_decl|;
comment|/**      * @since 4.0      */
name|Collection
argument_list|<
name|Embeddable
argument_list|>
name|getEmbeddables
parameter_list|()
function_decl|;
comment|/**      * Returns a named result set mapping.      *       * @since 3.0      */
name|SQLResult
name|getResult
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * @since 4.0      */
name|Collection
argument_list|<
name|SQLResult
argument_list|>
name|getResults
parameter_list|()
function_decl|;
comment|/**      * Returns DbEntity for a given name, or null if no such DbEntity is found      * in the MappingNamespace.      */
name|DbEntity
name|getDbEntity
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns ObjEntity for a given name, or null if no such ObjEntity is found      * in the MappingNamespace.      */
name|ObjEntity
name|getObjEntity
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns Procedure for a given name, or null if no such Procedure is found      * in the MappingNamespace.      */
name|Procedure
name|getProcedure
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns Query for a given name, or null if no such Query is found in the      * MappingNamespace.      */
name|QueryDescriptor
name|getQueryDescriptor
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns all DbEntities in the namespace.      */
name|Collection
argument_list|<
name|DbEntity
argument_list|>
name|getDbEntities
parameter_list|()
function_decl|;
comment|/**      * Returns all ObjEntities in the namespace.      */
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|getObjEntities
parameter_list|()
function_decl|;
comment|/**      * Returns all Procedures in the namespace.      */
name|Collection
argument_list|<
name|Procedure
argument_list|>
name|getProcedures
parameter_list|()
function_decl|;
comment|/**      * Returns all Queries in the namespace.      */
name|Collection
argument_list|<
name|QueryDescriptor
argument_list|>
name|getQueryDescriptors
parameter_list|()
function_decl|;
comment|/**      * @since 4.0      */
name|EntityInheritanceTree
name|getInheritanceTree
parameter_list|(
name|String
name|entityName
parameter_list|)
function_decl|;
comment|/**      * @since 4.0      */
name|ObjEntity
name|getObjEntity
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityClass
parameter_list|)
function_decl|;
comment|/**      * @since 4.0      */
name|ObjEntity
name|getObjEntity
parameter_list|(
name|Persistent
name|object
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

