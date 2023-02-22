begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************   *   Licensed to the Apache Software Foundation (ASF) under one   *  or more contributor license agreements.  See the NOTICE file   *  distributed with this work for additional information   *  regarding copyright ownership.  The ASF licenses this file   *  to you under the Apache License, Version 2.0 (the   *  "License"); you may not use this file except in compliance   *  with the License.  You may obtain a copy of the License at   *   *    https://www.apache.org/licenses/LICENSE-2.0   *   *  Unless required by applicable law or agreed to in writing,   *  software distributed under the License is distributed on an   *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY   *  KIND, either express or implied.  See the License for the   *  specific language governing permissions and limitations   *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|unit
operator|.
name|di
operator|.
name|server
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
name|Target
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
operator|.
name|TYPE
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
operator|.
name|RUNTIME
import|;
end_import

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RUNTIME
argument_list|)
annotation|@
name|Target
argument_list|(
name|TYPE
argument_list|)
annotation|@
name|Documented
annotation|@
name|Inherited
comment|/**  * Annotation provides the ability to add additional modules in declarative way  *  * @since 4.3  */
specifier|public
annotation_defn|@interface
name|InjectExtraModules
block|{
name|Class
index|[]
name|extraModules
argument_list|()
expr|default
block|{}
expr_stmt|;
block|}
end_annotation_defn

end_unit

