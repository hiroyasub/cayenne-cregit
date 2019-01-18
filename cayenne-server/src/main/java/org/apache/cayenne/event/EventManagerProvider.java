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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ConfigurationException
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
name|Constants
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
name|Provider
import|;
end_import

begin_comment
comment|/**  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|EventManagerProvider
implements|implements
name|Provider
argument_list|<
name|EventManager
argument_list|>
block|{
specifier|protected
name|RuntimeProperties
name|properties
decl_stmt|;
specifier|public
name|EventManagerProvider
parameter_list|(
annotation|@
name|Inject
name|RuntimeProperties
name|properties
parameter_list|)
block|{
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
name|EventManager
name|get
parameter_list|()
throws|throws
name|ConfigurationException
block|{
name|boolean
name|sync
init|=
name|properties
operator|.
name|getBoolean
argument_list|(
name|Constants
operator|.
name|SERVER_CONTEXTS_SYNC_PROPERTY
argument_list|,
literal|false
argument_list|)
decl_stmt|;
return|return
name|sync
condition|?
operator|new
name|DefaultEventManager
argument_list|()
else|:
operator|new
name|NoopEventManager
argument_list|()
return|;
block|}
block|}
end_class

end_unit

