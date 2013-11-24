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
name|osgi
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
name|AdhocObjectFactory
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
name|Binder
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
name|Module
import|;
end_import

begin_comment
comment|/**  * A DI module that helps to bootstrap Cayenne in OSGi environment.  *   * @since 3.2  */
end_comment

begin_class
specifier|public
class|class
name|OsgiModule
implements|implements
name|Module
block|{
comment|/**      * A factory method that creates a new OsgiModule, initialized with any      * class from the OSGi bundle that contains Cayenne mapping and persistent      * classes. This is likely the the bundle that is calling this method.      */
specifier|public
specifier|static
name|OsgiModule
name|forProject
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|typeFromProjectBundle
parameter_list|)
block|{
if|if
condition|(
name|typeFromProjectBundle
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null 'typeFromProjectBundle'"
argument_list|)
throw|;
block|}
name|OsgiModule
name|module
init|=
operator|new
name|OsgiModule
argument_list|()
decl_stmt|;
name|module
operator|.
name|typeFromProjectBundle
operator|=
name|typeFromProjectBundle
expr_stmt|;
return|return
name|module
return|;
block|}
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|typeFromProjectBundle
decl_stmt|;
specifier|private
name|OsgiModule
parameter_list|()
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|(
name|Binder
name|binder
parameter_list|)
block|{
name|binder
operator|.
name|bind
argument_list|(
name|AdhocObjectFactory
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|configureObjectFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|AdhocObjectFactory
name|configureObjectFactory
parameter_list|()
block|{
return|return
operator|new
name|SplitClassLoaderAdhocObjectFactory
argument_list|(
name|typeFromProjectBundle
operator|.
name|getClassLoader
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

