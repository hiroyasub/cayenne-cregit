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
name|dbsync
operator|.
name|naming
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
name|dbsync
operator|.
name|reverse
operator|.
name|db
operator|.
name|ExportedKey
import|;
end_import

begin_comment
comment|/**  * A strategy for creating names for object layer metadata artifacts based on their DB counterpart naming. Generated  * names should normally be further cleaned by passing them through {@link org.apache.cayenne.dbsync.naming.NameBuilder},  * that will resolve duplication conflicts.  *  * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|ObjectNameGenerator
block|{
comment|/**      * Generates a name for ObjEntity derived from DbEntity name.      */
name|String
name|objEntityName
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|)
function_decl|;
comment|/**      * Generates a name for ObjAttribute derived from DbAttribute name.      */
name|String
name|objAttributeName
parameter_list|(
name|DbAttribute
name|dbAttribute
parameter_list|)
function_decl|;
comment|/**      * Generates a name for DbRelationship derived from the DB foreign key name.      */
comment|// TODO: the class is called Object* , but here it is generating a DB-layer name... Better naming?
name|String
name|dbRelationshipName
parameter_list|(
name|ExportedKey
name|key
parameter_list|,
name|boolean
name|toMany
parameter_list|)
function_decl|;
comment|/**      * Generates a name for ObjRelationship derived from DbRelationship name.      */
name|String
name|objRelationshipName
parameter_list|(
name|DbRelationship
name|dbRelationship
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

