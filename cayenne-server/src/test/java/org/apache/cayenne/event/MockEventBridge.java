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
name|Map
import|;
end_import

begin_class
specifier|public
class|class
name|MockEventBridge
extends|extends
name|EventBridge
block|{
specifier|protected
name|Map
name|properties
decl_stmt|;
specifier|public
name|MockEventBridge
parameter_list|(
name|EventSubject
name|localSubject
parameter_list|,
name|String
name|externalSubject
parameter_list|,
name|Map
name|properties
parameter_list|)
block|{
name|super
argument_list|(
name|localSubject
argument_list|,
name|externalSubject
argument_list|)
expr_stmt|;
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
specifier|public
name|MockEventBridge
parameter_list|(
name|Collection
name|localSubjects
parameter_list|,
name|String
name|externalSubject
parameter_list|,
name|Map
name|properties
parameter_list|)
block|{
name|super
argument_list|(
name|localSubjects
argument_list|,
name|externalSubject
argument_list|)
expr_stmt|;
name|this
operator|.
name|properties
operator|=
name|properties
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

