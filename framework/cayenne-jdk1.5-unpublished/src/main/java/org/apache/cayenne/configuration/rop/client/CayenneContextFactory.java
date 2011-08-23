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
name|configuration
operator|.
name|rop
operator|.
name|client
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
name|CayenneContext
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
name|DataChannel
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
name|ObjectContext
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
name|cache
operator|.
name|NestedQueryCache
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
name|cache
operator|.
name|QueryCache
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
name|configuration
operator|.
name|ObjectContextFactory
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
name|configuration
operator|.
name|RuntimeProperties
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|Injector
import|;
end_import

begin_class
specifier|public
class|class
name|CayenneContextFactory
implements|implements
name|ObjectContextFactory
block|{
annotation|@
name|Inject
specifier|protected
name|DataChannel
name|dataChannel
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|RuntimeProperties
name|properties
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|QueryCache
name|queryCache
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|Injector
name|injector
decl_stmt|;
specifier|public
name|ObjectContext
name|createContext
parameter_list|()
block|{
return|return
name|createContext
argument_list|(
name|dataChannel
argument_list|)
return|;
block|}
specifier|public
name|ObjectContext
name|createContext
parameter_list|(
name|DataChannel
name|parent
parameter_list|)
block|{
name|boolean
name|changeEvents
init|=
name|properties
operator|.
name|getBoolean
argument_list|(
name|ClientModule
operator|.
name|CONTEXT_CHANGE_EVENTS
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|boolean
name|lifecycleEvents
init|=
name|properties
operator|.
name|getBoolean
argument_list|(
name|ClientModule
operator|.
name|CONTEXT_LIFECYCLE_EVENTS
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|CayenneContext
name|context
init|=
operator|new
name|CayenneContext
argument_list|(
name|parent
argument_list|,
name|changeEvents
argument_list|,
name|lifecycleEvents
argument_list|)
decl_stmt|;
name|context
operator|.
name|setQueryCache
argument_list|(
operator|new
name|NestedQueryCache
argument_list|(
name|queryCache
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

