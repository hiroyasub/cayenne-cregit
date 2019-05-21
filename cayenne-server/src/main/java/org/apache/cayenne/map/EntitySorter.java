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
name|Comparator
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
comment|/**  * Defines API for sorting of Cayenne entities based on their mutual dependencies.  *   * @since 1.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|EntitySorter
block|{
comment|/**      * Sets EntityResolver for this sorter. All entities present in the resolver will be      * used to determine sort ordering.      *       * @since 3.1      */
name|void
name|setEntityResolver
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
function_decl|;
comment|/**      * Sorts a list of DbEntities.      */
name|void
name|sortDbEntities
parameter_list|(
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
parameter_list|,
name|boolean
name|deleteOrder
parameter_list|)
function_decl|;
comment|/**      * Sorts a list of ObjEntities.      */
name|void
name|sortObjEntities
parameter_list|(
name|List
argument_list|<
name|ObjEntity
argument_list|>
name|objEntities
parameter_list|,
name|boolean
name|deleteOrder
parameter_list|)
function_decl|;
comment|/**      * Sorts a list of objects belonging to the ObjEntity.      */
name|void
name|sortObjectsForEntity
parameter_list|(
name|ObjEntity
name|entity
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|objects
parameter_list|,
name|boolean
name|deleteOrder
parameter_list|)
function_decl|;
comment|/**      * @return comparator for {@link DbEntity}      * @since 4.2      */
name|Comparator
argument_list|<
name|DbEntity
argument_list|>
name|getDbEntityComparator
parameter_list|()
function_decl|;
comment|/**      * @return comparator for {@link ObjEntity}      * @since 4.2      */
name|Comparator
argument_list|<
name|ObjEntity
argument_list|>
name|getObjEntityComparator
parameter_list|()
function_decl|;
comment|/**      * @param entity to check      * @return is entity has reflexive relationships      *      * @since 4.2      */
name|boolean
name|isReflexive
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

