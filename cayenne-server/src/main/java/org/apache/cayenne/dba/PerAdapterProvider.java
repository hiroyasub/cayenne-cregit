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
name|dba
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
name|di
operator|.
name|DIRuntimeException
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_comment
comment|/**  * An injectable provider that returns a given service in a context of a specific {@link DbAdapter}.  * This allows modules to create adapter-specific extensions without altering DbAdapter API.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|PerAdapterProvider
parameter_list|<
name|T
parameter_list|>
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|perAdapterValues
decl_stmt|;
specifier|private
name|T
name|defaultValue
decl_stmt|;
specifier|public
name|PerAdapterProvider
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|perAdapterValues
parameter_list|,
name|T
name|defaultValue
parameter_list|)
block|{
name|this
operator|.
name|perAdapterValues
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|perAdapterValues
argument_list|)
expr_stmt|;
name|this
operator|.
name|defaultValue
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|defaultValue
argument_list|)
expr_stmt|;
block|}
specifier|public
name|T
name|get
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
throws|throws
name|DIRuntimeException
block|{
name|T
name|t
init|=
name|perAdapterValues
operator|.
name|get
argument_list|(
name|adapter
operator|.
name|unwrap
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|t
operator|!=
literal|null
condition|?
name|t
else|:
name|defaultValue
return|;
block|}
block|}
end_class

end_unit

