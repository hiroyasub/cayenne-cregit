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
name|access
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
name|CayenneRuntimeException
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
name|DIRuntimeException
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
name|Provider
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
name|EventBridge
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
name|EventManager
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
name|NoopEventBridge
import|;
end_import

begin_comment
comment|/**  * A default implementation of {@link DataRowStoreFactory}  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DefaultDataRowStoreFactory
implements|implements
name|DataRowStoreFactory
block|{
name|Provider
argument_list|<
name|EventBridge
argument_list|>
name|eventBridgeProvider
decl_stmt|;
name|EventManager
name|eventManager
decl_stmt|;
name|RuntimeProperties
name|properties
decl_stmt|;
specifier|public
name|DefaultDataRowStoreFactory
parameter_list|(
annotation|@
name|Inject
name|Provider
argument_list|<
name|EventBridge
argument_list|>
name|eventBridgeProvider
parameter_list|,
annotation|@
name|Inject
name|EventManager
name|eventManager
parameter_list|,
annotation|@
name|Inject
name|RuntimeProperties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|eventBridgeProvider
operator|=
name|eventBridgeProvider
expr_stmt|;
name|this
operator|.
name|eventManager
operator|=
name|eventManager
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
specifier|public
name|DataRowStore
name|createDataRowStore
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|DIRuntimeException
block|{
name|DataRowStore
name|store
init|=
operator|new
name|DataRowStore
argument_list|(
name|name
argument_list|,
name|properties
argument_list|,
name|eventManager
argument_list|)
decl_stmt|;
name|setUpEventBridge
argument_list|(
name|store
argument_list|)
expr_stmt|;
return|return
name|store
return|;
block|}
specifier|private
name|void
name|setUpEventBridge
parameter_list|(
name|DataRowStore
name|store
parameter_list|)
block|{
try|try
block|{
name|EventBridge
name|eventBridge
init|=
name|eventBridgeProvider
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|eventBridge
operator|instanceof
name|NoopEventBridge
condition|)
block|{
return|return;
block|}
name|store
operator|.
name|setEventBridge
argument_list|(
name|eventBridge
argument_list|)
expr_stmt|;
name|store
operator|.
name|startListeners
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error initializing DataRowStore."
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

