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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
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
name|ObjEntity
import|;
end_import

begin_comment
comment|/**  * Defines API for progress tracking and altering the flow of reverse-engineering.  */
end_comment

begin_interface
specifier|public
interface|interface
name|DbLoaderDelegate
block|{
name|void
name|dbEntityAdded
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
function_decl|;
name|void
name|dbEntityRemoved
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
function_decl|;
comment|/**      * Called before relationship loading for a {@link DbEntity}.      *      * @param entity DbEntity for which {@link DbRelationship} is about to be loaded.      * @return true in case you want process relationships for this entity, false otherwise.      */
name|boolean
name|dbRelationship
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
function_decl|;
comment|/**      * Called before relationship will be added into db-entity but after it was loaded from db      *      * @param entity      * @return true in case you want add this relationship into entity      * false otherwise      */
name|boolean
name|dbRelationshipLoaded
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbRelationship
name|relationship
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

