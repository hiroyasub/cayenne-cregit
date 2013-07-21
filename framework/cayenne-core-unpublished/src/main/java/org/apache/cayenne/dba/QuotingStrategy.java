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
name|dba
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
name|DataMap
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
name|DbJoin
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
name|Entity
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_interface
specifier|public
interface|interface
name|QuotingStrategy
block|{
comment|/**      * Returns a properly quoted identifier.      *       * @deprecated since 3.2      */
annotation|@
name|Deprecated
name|String
name|quoteString
parameter_list|(
name|String
name|identifier
parameter_list|)
function_decl|;
comment|/**      * @deprecated since 3.2 renamed to      *             {@link #quotedFullyQualifiedName(DbEntity)}.      */
annotation|@
name|Deprecated
name|String
name|quoteFullyQualifiedName
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
function_decl|;
comment|/**      * Builds a fully qualified name from catalog, schema, name parts of      * DbEntity, inclosing them in quotations according to this strategy      * algorithm. Analog of "quotedIdentifier(entity.getCatalog(),      * entity.getSchema(), entity.getName())".      *       * @since 3.2      */
name|String
name|quotedFullyQualifiedName
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
function_decl|;
comment|/**      *       * @since 3.2      */
name|String
name|quotedName
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
function_decl|;
comment|/**      * @since 3.2      */
name|String
name|quotedSourceName
parameter_list|(
name|DbJoin
name|join
parameter_list|)
function_decl|;
comment|/**      * @since 3.2      */
name|String
name|quotedTargetName
parameter_list|(
name|DbJoin
name|join
parameter_list|)
function_decl|;
comment|/**      * @since 3.2      */
name|String
name|quotedIdentifier
parameter_list|(
name|Entity
name|entity
parameter_list|,
name|String
modifier|...
name|identifierParts
parameter_list|)
function_decl|;
comment|/**      * @since 3.2      */
name|String
name|quotedIdentifier
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|String
modifier|...
name|identifierParts
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

