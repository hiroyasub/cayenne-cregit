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
name|configuration
operator|.
name|server
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
name|dba
operator|.
name|PerAdapterProvider
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
name|dba
operator|.
name|PkGenerator
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
name|di
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Per-adapter provider of PkGenerators  *  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|PkGeneratorFactoryProvider
extends|extends
name|PerAdapterProvider
argument_list|<
name|PkGenerator
argument_list|>
block|{
specifier|public
name|PkGeneratorFactoryProvider
parameter_list|(
annotation|@
name|Inject
name|Map
argument_list|<
name|String
argument_list|,
name|PkGenerator
argument_list|>
name|perAdapterValues
parameter_list|,
annotation|@
name|Inject
name|PkGenerator
name|defaultValue
parameter_list|)
block|{
name|super
argument_list|(
name|perAdapterValues
argument_list|,
name|defaultValue
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

