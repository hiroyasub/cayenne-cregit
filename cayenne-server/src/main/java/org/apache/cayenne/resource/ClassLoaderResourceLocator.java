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
name|resource
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Enumeration
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
name|di
operator|.
name|ClassLoaderManager
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

begin_comment
comment|/**  * A {@link ResourceLocator} that looks up resources is the application  * classpath based on the current thread ClassLoader.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|ClassLoaderResourceLocator
implements|implements
name|ResourceLocator
block|{
specifier|private
name|ClassLoaderManager
name|classLoaderManager
decl_stmt|;
specifier|public
name|ClassLoaderResourceLocator
parameter_list|(
annotation|@
name|Inject
name|ClassLoaderManager
name|classLoaderManager
parameter_list|)
block|{
name|this
operator|.
name|classLoaderManager
operator|=
name|classLoaderManager
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|Resource
argument_list|>
name|findResources
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Collection
argument_list|<
name|Resource
argument_list|>
name|resources
init|=
operator|new
name|ArrayList
argument_list|<
name|Resource
argument_list|>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|urls
decl_stmt|;
try|try
block|{
name|urls
operator|=
name|classLoaderManager
operator|.
name|getClassLoader
argument_list|(
name|name
argument_list|)
operator|.
name|getResources
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConfigurationException
argument_list|(
literal|"Error getting resources for "
argument_list|)
throw|;
block|}
while|while
condition|(
name|urls
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
comment|// TODO: andrus 11/30/2009 - replace URLResource that resolves
comment|// relative URL's as truly relative with some kind of
comment|// ClasspathResource that creates a relative *path* and then
comment|// resolves it against the entire classpath space.
name|resources
operator|.
name|add
argument_list|(
operator|new
name|URLResource
argument_list|(
name|urls
operator|.
name|nextElement
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|resources
return|;
block|}
comment|/**      * @deprecated since 4.0 unused, as AdhocObjectFactory.getClassLoader() is      *             used instead.      */
annotation|@
name|Deprecated
specifier|protected
name|ClassLoader
name|getClassLoader
parameter_list|()
block|{
name|ClassLoader
name|loader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
name|loader
operator|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
name|loader
operator|=
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|loader
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Can't detect ClassLoader to use for resource location"
argument_list|)
throw|;
block|}
return|return
name|loader
return|;
block|}
block|}
end_class

end_unit

