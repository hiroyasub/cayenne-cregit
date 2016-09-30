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
name|reverse
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
name|map
operator|.
name|naming
operator|.
name|ExportedKey
import|;
end_import

begin_comment
comment|/**  * ObjectNameGenerator is a strategy for creating names for entities,  * attributes, relationships from DB metadata.  *   * @since 4.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|ObjectNameGenerator
block|{
comment|/** 	 * Creates new name for ObjEntity 	 */
name|String
name|createObjEntityName
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
function_decl|;
comment|/** 	 * Creates new name for ObjAttribute 	 */
name|String
name|createObjAttributeName
parameter_list|(
name|DbAttribute
name|attr
parameter_list|)
function_decl|;
comment|/** 	 * Creates new name for DbRelationship 	 */
name|String
name|createDbRelationshipName
parameter_list|(
name|ExportedKey
name|key
parameter_list|,
name|boolean
name|toMany
parameter_list|)
function_decl|;
comment|/** 	 * Creates new name for ObjRelationship 	 */
name|String
name|createObjRelationshipName
parameter_list|(
name|DbRelationship
name|dbRel
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

