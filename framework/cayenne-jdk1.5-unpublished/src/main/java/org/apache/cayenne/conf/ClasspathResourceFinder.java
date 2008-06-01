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
name|conf
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
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|CayenneRuntimeException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A ResourceFinder that looks up resources in the classpath.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ClasspathResourceFinder
implements|implements
name|ResourceFinder
block|{
specifier|static
specifier|final
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ClasspathResourceFinder
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|ClassLoader
name|classLoader
decl_stmt|;
specifier|protected
name|Collection
argument_list|<
name|String
argument_list|>
name|rootPaths
decl_stmt|;
specifier|public
name|ClasspathResourceFinder
parameter_list|()
block|{
name|rootPaths
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|rootPaths
operator|.
name|add
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
specifier|public
name|URL
name|getResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null resource name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|ClassLoader
name|loader
init|=
name|getResourceClassLoader
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|root
range|:
name|rootPaths
control|)
block|{
name|String
name|fullName
init|=
name|root
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|root
operator|+
literal|"/"
operator|+
name|name
else|:
name|name
decl_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"searching for resource under: "
operator|+
name|fullName
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
name|loader
operator|.
name|getResource
argument_list|(
name|fullName
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
return|return
name|url
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|URL
argument_list|>
name|getResources
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null resource name"
argument_list|)
throw|;
block|}
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|ClassLoader
name|loader
init|=
name|getResourceClassLoader
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|URL
argument_list|>
name|urls
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|URL
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|root
range|:
name|rootPaths
control|)
block|{
name|String
name|fullName
init|=
name|root
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|root
operator|+
literal|"/"
operator|+
name|name
else|:
name|name
decl_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"searching for resources under: "
operator|+
name|fullName
argument_list|)
expr_stmt|;
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|urlsEn
decl_stmt|;
try|try
block|{
name|urlsEn
operator|=
name|loader
operator|.
name|getResources
argument_list|(
name|fullName
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
name|CayenneRuntimeException
argument_list|(
literal|"Error reading URL resources from "
operator|+
name|fullName
argument_list|)
throw|;
block|}
while|while
condition|(
name|urlsEn
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|urls
operator|.
name|add
argument_list|(
name|urlsEn
operator|.
name|nextElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|urls
return|;
block|}
comment|/**      * Adds a base path to be used for resource lookup. In the context of      * ClasspathResourceFinder, a "path" corresponds to a package name, only separated by      * "/" instead of ".". Default root path is empty String. This method allows to add      * more lookup roots.      */
specifier|public
name|void
name|addRootPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null path"
argument_list|)
throw|;
block|}
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|rootPaths
operator|.
name|add
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns ClassLoader override initialized via {@link #setClassLoader(ClassLoader)}.      * Null by default.      */
specifier|public
name|ClassLoader
name|getClassLoader
parameter_list|()
block|{
return|return
name|classLoader
return|;
block|}
comment|/**      * Sets an overriding ClassLoader for this resource finder. Setting it is only needed      * if the default thread class loader is not appropriate for looking up the resources.      */
specifier|public
name|void
name|setClassLoader
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|)
block|{
name|this
operator|.
name|classLoader
operator|=
name|classLoader
expr_stmt|;
block|}
comment|/**      * Returns a non-null ClassLoader that should be used to locate resources. The lookup      * following order is used to find it: explicitly set class loader, current thread      * class loader, this class class loader, system class loader.      */
specifier|protected
name|ClassLoader
name|getResourceClassLoader
parameter_list|()
block|{
name|ClassLoader
name|loader
init|=
name|this
operator|.
name|classLoader
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
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
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
literal|"Can't detect ClassLoader to use for resouyrce location"
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

