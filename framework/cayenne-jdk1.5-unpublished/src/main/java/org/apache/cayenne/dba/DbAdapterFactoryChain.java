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
name|dba
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|DatabaseMetaData
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|List
import|;
end_import

begin_comment
comment|/**  * A facade for a collection of DbAdapterFactories. Can be configured to auto-detect all  * adapters known to Cayenne or can work with custom factories.  *   * @since 1.2  */
end_comment

begin_comment
comment|// TODO, Andrus 11/01/2005, how can custom adapters be auto-detected? I.e. is there a way
end_comment

begin_comment
comment|// to plug a custom factory into configuration loading process? Of course users can simply
end_comment

begin_comment
comment|// specify the adapter class in the modeler, so this may be a non-issue.
end_comment

begin_class
class|class
name|DbAdapterFactoryChain
implements|implements
name|DbAdapterFactory
block|{
name|List
argument_list|<
name|DbAdapterFactory
argument_list|>
name|factories
decl_stmt|;
name|DbAdapterFactoryChain
parameter_list|(
name|Collection
argument_list|<
name|DbAdapterFactory
argument_list|>
name|factories
parameter_list|)
block|{
name|this
operator|.
name|factories
operator|=
operator|new
name|ArrayList
argument_list|<
name|DbAdapterFactory
argument_list|>
argument_list|(
name|factories
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|factories
operator|.
name|addAll
argument_list|(
name|factories
argument_list|)
expr_stmt|;
block|}
comment|/**      * Iterates through predicated factories, stopping when the first one returns non-null      * DbAdapter. If none of the factories match the database, returns null.      */
specifier|public
name|DbAdapter
name|createAdapter
parameter_list|(
name|DatabaseMetaData
name|md
parameter_list|)
throws|throws
name|SQLException
block|{
comment|// match against configured predicated factories
comment|// iterate in reverse order to allow custom factories to take precedence over the
comment|// default ones configured in constructor
for|for
control|(
name|int
name|i
init|=
name|factories
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|DbAdapterFactory
name|factory
init|=
name|factories
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|DbAdapter
name|adapter
init|=
name|factory
operator|.
name|createAdapter
argument_list|(
name|md
argument_list|)
decl_stmt|;
if|if
condition|(
name|adapter
operator|!=
literal|null
condition|)
block|{
return|return
name|adapter
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Removes all configured factories.      */
name|void
name|clearFactories
parameter_list|()
block|{
name|this
operator|.
name|factories
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Adds a new DbAdapterFactory to the factory chain.      */
name|void
name|addFactory
parameter_list|(
name|DbAdapterFactory
name|factory
parameter_list|)
block|{
name|this
operator|.
name|factories
operator|.
name|add
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

