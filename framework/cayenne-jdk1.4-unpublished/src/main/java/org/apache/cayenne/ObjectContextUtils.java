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
name|conf
operator|.
name|Configuration
import|;
end_import

begin_comment
comment|/**  * Provides static methods for {@link ObjectContext} creation. Context settings are  * extracted behind the scenes from the shared {@link Configuration} instance. Some can be  * overridden with custom properties on the fly.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_comment
comment|// note - this is sort of like javax.persistence.Persistence class with Cayenne flavor,
end_comment

begin_comment
comment|// with support for explicit thread binding of context, nested contexts, etc.
end_comment

begin_class
specifier|public
class|class
name|ObjectContextUtils
block|{
specifier|private
specifier|static
specifier|final
name|ThreadLocal
name|threadContext
init|=
operator|new
name|ThreadLocal
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|ObjectContext
name|createContext
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|ObjectContext
name|getThreadContext
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Binds an ObjectContext to the current thread. ObjectContext can later be retrieved      * within the same thread by calling {@link ObjectContextUtils#getThreadContext()}.      * Using null parameter will unbind currently bound context and should be used to      * clean up the thread state.      */
specifier|public
specifier|static
name|void
name|setThreadContext
parameter_list|(
name|ObjectContext
name|context
parameter_list|)
block|{
name|threadContext
operator|.
name|set
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

