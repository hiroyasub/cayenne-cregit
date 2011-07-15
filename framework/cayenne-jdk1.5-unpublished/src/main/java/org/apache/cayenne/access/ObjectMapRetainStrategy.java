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
name|access
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
comment|/**  * A strategy for retaining objects in {@link ObjectStore}. May be used  * weak, soft or hard references.   *   * @since 3.1  */
end_comment

begin_interface
specifier|public
interface|interface
name|ObjectMapRetainStrategy
block|{
specifier|static
specifier|final
name|String
name|MAP_RETAIN_STRATEGY_PROPERTY
init|=
literal|"org.apache.cayenne.context_object_retain_strategy"
decl_stmt|;
specifier|static
specifier|final
name|String
name|WEAK_RETAIN_STRATEGY
init|=
literal|"weak"
decl_stmt|;
specifier|static
specifier|final
name|String
name|SOFT_RETAIN_STRATEGY
init|=
literal|"soft"
decl_stmt|;
specifier|static
specifier|final
name|String
name|HARD_RETAIN_STRATEGY
init|=
literal|"hard"
decl_stmt|;
name|Map
argument_list|<
name|Object
argument_list|,
name|Persistent
argument_list|>
name|createObjectMap
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

