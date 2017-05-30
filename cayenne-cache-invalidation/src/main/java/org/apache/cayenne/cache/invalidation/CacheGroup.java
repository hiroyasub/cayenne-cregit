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
name|cache
operator|.
name|invalidation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Documented
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Inherited
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_comment
comment|/**  * Annotation for defining cache group in case different cache types are targeted.  *  * @see CacheGroups  * @see CacheGroupsHandler  *  * @since 4.0  */
end_comment

begin_annotation_defn
annotation|@
name|Target
argument_list|(
name|ElementType
operator|.
name|TYPE
argument_list|)
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Documented
annotation|@
name|Inherited
specifier|public
annotation_defn|@interface
name|CacheGroup
block|{
comment|/**      * @return cache group name      */
name|String
name|value
parameter_list|()
function_decl|;
comment|/**      * Defines key type of the cache.      * Could be used for managing  external caches that is strictly typed (e.g. JCache).      */
name|Class
argument_list|<
name|?
argument_list|>
name|keyType
parameter_list|()
default|default
name|Void
operator|.
name|class
function_decl|;
comment|/**      * Defines value type of the cache.      * Could be used for managing external caches that is strictly typed (e.g. JCache).      */
name|Class
argument_list|<
name|?
argument_list|>
name|valueType
parameter_list|()
default|default
name|Void
operator|.
name|class
function_decl|;
block|}
end_annotation_defn

end_unit

