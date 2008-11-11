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
name|event
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
import|;
end_import

begin_comment
comment|/**  * A test event listener that reacts to an event by registering another listener.  *   */
end_comment

begin_class
specifier|public
class|class
name|MockListener
block|{
specifier|public
specifier|static
specifier|final
name|EventSubject
name|mockSubject
init|=
name|EventSubject
operator|.
name|getSubject
argument_list|(
name|MockListener
operator|.
name|class
argument_list|,
literal|"mock"
argument_list|)
decl_stmt|;
specifier|protected
name|EventManager
name|manager
decl_stmt|;
specifier|protected
name|Object
name|sender
decl_stmt|;
specifier|public
name|MockListener
parameter_list|(
name|EventManager
name|manager
parameter_list|)
block|{
name|this
argument_list|(
name|manager
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MockListener
parameter_list|(
name|EventManager
name|manager
parameter_list|,
name|Object
name|sender
parameter_list|)
block|{
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
name|this
operator|.
name|sender
operator|=
name|sender
expr_stmt|;
block|}
specifier|public
name|void
name|processEvent
parameter_list|(
name|EventObject
name|object
parameter_list|)
block|{
name|manager
operator|.
name|addListener
argument_list|(
operator|new
name|MockListener
argument_list|(
name|manager
argument_list|)
argument_list|,
literal|"processEvent"
argument_list|,
name|EventObject
operator|.
name|class
argument_list|,
name|mockSubject
argument_list|,
name|sender
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

