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
name|conf
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
name|access
operator|.
name|DataDomain
import|;
end_import

begin_comment
comment|/**  * Configuration object used for tests that does not require "cayenne.xml".  */
end_comment

begin_class
specifier|public
class|class
name|EmptyConfiguration
extends|extends
name|DefaultConfiguration
block|{
specifier|public
name|EmptyConfiguration
parameter_list|()
block|{
comment|// ignore any loading failures
name|this
operator|.
name|setIgnoringLoadFailures
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|setLoaderDelegate
argument_list|(
operator|new
name|RuntimeLoadDelegate
argument_list|(
name|this
argument_list|,
name|this
operator|.
name|getLoadStatus
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addDomain
parameter_list|(
name|DataDomain
name|domain
parameter_list|)
block|{
comment|// noop
block|}
block|}
end_class

end_unit

