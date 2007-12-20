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
name|jpa
operator|.
name|conf
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
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Basic
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Id
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|ManyToOne
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|OneToMany
import|;
end_import

begin_class
annotation|@
name|Entity
specifier|public
class|class
name|MockAnnotatedBean3
block|{
annotation|@
name|Id
specifier|protected
name|int
name|pk
decl_stmt|;
annotation|@
name|Basic
specifier|protected
name|String
name|attribute1
decl_stmt|;
comment|// no annotation here should result in a conflict.
specifier|protected
name|MockAnnotatedBean1
name|attribute2
decl_stmt|;
annotation|@
name|ManyToOne
specifier|protected
name|MockAnnotatedBean1
name|toBean2
decl_stmt|;
annotation|@
name|OneToMany
specifier|protected
name|Collection
argument_list|<
name|MockAnnotatedBean1
argument_list|>
name|toBean2s1
decl_stmt|;
annotation|@
name|OneToMany
comment|// no collection type - must result in a failure
specifier|protected
name|Collection
argument_list|<
name|?
argument_list|>
name|toBean2s2
decl_stmt|;
comment|// date w/o Temporal annotation must resolve to TIMESTAMP
specifier|protected
name|Date
name|date
decl_stmt|;
block|}
end_class

end_unit

