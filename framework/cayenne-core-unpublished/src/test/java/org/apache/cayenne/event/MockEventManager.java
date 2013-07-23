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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|event
operator|.
name|DefaultEventManager
operator|.
name|Dispatch
import|;
end_import

begin_class
specifier|public
class|class
name|MockEventManager
implements|implements
name|EventManager
block|{
specifier|public
name|void
name|addListener
parameter_list|(
name|Object
name|listener
parameter_list|,
name|String
name|methodName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|eventParameterClass
parameter_list|,
name|EventSubject
name|subject
parameter_list|)
block|{
block|}
specifier|public
name|void
name|addListener
parameter_list|(
name|Object
name|listener
parameter_list|,
name|String
name|methodName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|eventParameterClass
parameter_list|,
name|EventSubject
name|subject
parameter_list|,
name|Object
name|sender
parameter_list|)
block|{
block|}
specifier|public
name|void
name|addNonBlockingListener
parameter_list|(
name|Object
name|listener
parameter_list|,
name|String
name|methodName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|eventParameterClass
parameter_list|,
name|EventSubject
name|subject
parameter_list|)
block|{
block|}
specifier|public
name|void
name|addNonBlockingListener
parameter_list|(
name|Object
name|listener
parameter_list|,
name|String
name|methodName
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|eventParameterClass
parameter_list|,
name|EventSubject
name|subject
parameter_list|,
name|Object
name|sender
parameter_list|)
block|{
block|}
specifier|public
name|List
argument_list|<
name|Dispatch
argument_list|>
name|getEventQueue
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isSingleThreaded
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|postEvent
parameter_list|(
name|EventObject
name|event
parameter_list|,
name|EventSubject
name|subject
parameter_list|)
block|{
block|}
specifier|public
name|void
name|postNonBlockingEvent
parameter_list|(
name|EventObject
name|event
parameter_list|,
name|EventSubject
name|subject
parameter_list|)
block|{
block|}
specifier|public
name|boolean
name|removeAllListeners
parameter_list|(
name|EventSubject
name|subject
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|removeListener
parameter_list|(
name|Object
name|listener
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|removeListener
parameter_list|(
name|Object
name|listener
parameter_list|,
name|EventSubject
name|subject
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|removeListener
parameter_list|(
name|Object
name|listener
parameter_list|,
name|EventSubject
name|subject
parameter_list|,
name|Object
name|sender
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
block|}
block|}
end_class

end_unit
