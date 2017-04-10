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
name|lifecycle
operator|.
name|audit
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|lifecycle
operator|.
name|postcommit
operator|.
name|Confidential
import|;
end_import

begin_comment
comment|/**  * An annotation that adds auditing behavior to DataObjects.  *   * @since 3.1  */
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
name|Auditable
block|{
comment|/** 	 * Returns an array of entity properties that should be excluded from audit. 	 */
name|String
index|[]
name|ignoredProperties
argument_list|()
expr|default
block|{}
expr_stmt|;
comment|/** 	 * Returns whether all attributes should be excluded from audit. 	 *  	 * @since 4.0 	 */
name|boolean
name|ignoreAttributes
parameter_list|()
default|default
literal|false
function_decl|;
comment|/** 	 * Returns whether all to-one relationships should be excluded from audit. 	 *  	 * @since 4.0 	 */
name|boolean
name|ignoreToOneRelationships
parameter_list|()
default|default
literal|false
function_decl|;
comment|/** 	 * Returns whether all to-many relationships should be excluded from audit. 	 *  	 * @since 4.0 	 */
name|boolean
name|ignoreToManyRelationships
parameter_list|()
default|default
literal|false
function_decl|;
comment|/** 	 * Returns an array of properties that should be treated as confidential. 	 * I.e. their change should be recorded, but their values should be hidden 	 * from listeners. In practice both old and new values will be set to an 	 * instance of {@link Confidential}. 	 *  	 * @since 4.0 	 */
name|String
index|[]
name|confidential
argument_list|()
expr|default
block|{}
expr_stmt|;
block|}
end_annotation_defn

end_unit

