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
name|java
operator|.
name|sql
operator|.
name|Driver
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
comment|/**  * A builder of a DI module that helps to bootstrap Cayenne in OSGi environment.  *   * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|OsgiModuleBuilder
block|{
specifier|private
name|OsgiModule
name|module
decl_stmt|;
specifier|public
specifier|static
name|OsgiModuleBuilder
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
return|return
operator|new
name|OsgiModuleBuilder
argument_list|(
name|typeFromProjectBundle
argument_list|)
return|;
block|}
specifier|private
name|OsgiModuleBuilder
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|typeFromProjectBundle
parameter_list|)
block|{
name|this
operator|.
name|module
operator|=
operator|new
name|OsgiModule
argument_list|()
expr_stmt|;
name|module
operator|.
name|setTypeFromProjectBundle
argument_list|(
name|typeFromProjectBundle
argument_list|)
expr_stmt|;
block|}
comment|/**      * Registers a JDBC driver class used by Cayenne. This is an optional piece      * of information used by Cayenne to find JDBC driver in case of Cayenne      * managed DataSource. E.g. don't use this when you are using a JNDI      * DataSource.      */
specifier|public
name|OsgiModuleBuilder
name|withDriver
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Driver
argument_list|>
name|driverType
parameter_list|)
block|{
return|return
name|withType
argument_list|(
name|driverType
argument_list|)
return|;
block|}
comment|/**      * Registers an arbitrary Java class. If Cayenne tries to load it via      * reflection later on, it will be using ClassLoader attached to the class      * passed to this method.      */
specifier|public
name|OsgiModuleBuilder
name|withType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|module
operator|.
name|putClassLoader
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|,
name|type
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Registers an arbitrary Java class. If Cayenne tries to load it via      * reflection later on, it will be using ClassLoader attached to the class      * passed to this method.      */
specifier|public
name|OsgiModuleBuilder
name|withTypes
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
modifier|...
name|types
parameter_list|)
block|{
if|if
condition|(
name|types
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
range|:
name|types
control|)
block|{
name|module
operator|.
name|putClassLoader
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|,
name|type
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Module
name|module
parameter_list|()
block|{
return|return
name|module
return|;
block|}
block|}
end_class

end_unit

