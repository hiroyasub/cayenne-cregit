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
name|event
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
name|DataRowStore
import|;
end_import

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|NoopEventBridge
extends|extends
name|EventBridge
block|{
name|NoopEventBridge
parameter_list|()
block|{
name|super
argument_list|(
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|DataRowStore
operator|.
name|class
argument_list|,
literal|"noop-subject"
argument_list|)
argument_list|,
literal|"noop-subject"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|startupExternal
parameter_list|()
throws|throws
name|Exception
block|{
block|}
annotation|@
name|Override
specifier|protected
name|void
name|shutdownExternal
parameter_list|()
throws|throws
name|Exception
block|{
block|}
annotation|@
name|Override
specifier|protected
name|void
name|sendExternalEvent
parameter_list|(
name|CayenneEvent
name|localEvent
parameter_list|)
throws|throws
name|Exception
block|{
block|}
block|}
end_class

end_unit

