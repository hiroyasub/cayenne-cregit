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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|Iterator
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

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
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
comment|/**  * A ResourceLocator that can find resources relative to web application context.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|WebApplicationResourceLocator
extends|extends
name|ResourceLocator
block|{
specifier|private
specifier|static
name|Log
name|logObj
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|WebApplicationResourceLocator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|ServletContext
name|context
decl_stmt|;
specifier|protected
name|List
name|additionalContextPaths
decl_stmt|;
comment|/**      * @since 1.2      */
specifier|public
name|WebApplicationResourceLocator
parameter_list|()
block|{
name|this
operator|.
name|additionalContextPaths
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|this
operator|.
name|addFilesystemPath
argument_list|(
literal|"/WEB-INF/"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates new WebApplicationResourceLocator with default lookup policy including user      * home directory, current directory and CLASSPATH.      */
specifier|public
name|WebApplicationResourceLocator
parameter_list|(
name|ServletContext
name|context
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setServletContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the ServletContext used to locate resources.      */
specifier|public
name|void
name|setServletContext
parameter_list|(
name|ServletContext
name|servletContext
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|servletContext
expr_stmt|;
block|}
comment|/**      * Gets the ServletContext used to locate resources.      */
specifier|public
name|ServletContext
name|getServletContext
parameter_list|()
block|{
return|return
name|this
operator|.
name|context
return|;
block|}
comment|/**      * Looks for resources relative to /WEB-INF/ directory or any extra context paths      * configured. Internal ServletContext is used to find resources.      */
annotation|@
name|Override
specifier|public
name|URL
name|findResource
parameter_list|(
name|String
name|location
parameter_list|)
block|{
if|if
condition|(
operator|!
name|additionalContextPaths
operator|.
name|isEmpty
argument_list|()
operator|&&
name|getServletContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|suffix
init|=
name|location
operator|!=
literal|null
condition|?
name|location
else|:
literal|""
decl_stmt|;
if|if
condition|(
name|suffix
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|suffix
operator|=
name|suffix
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|Iterator
name|cpi
init|=
name|this
operator|.
name|additionalContextPaths
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|cpi
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|prefix
init|=
operator|(
name|String
operator|)
name|cpi
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|prefix
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|prefix
operator|+=
literal|"/"
expr_stmt|;
block|}
name|String
name|fullName
init|=
name|prefix
operator|+
name|suffix
decl_stmt|;
name|logObj
operator|.
name|debug
argument_list|(
literal|"searching for: "
operator|+
name|fullName
argument_list|)
expr_stmt|;
try|try
block|{
name|URL
name|url
init|=
name|getServletContext
argument_list|()
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
catch|catch
parameter_list|(
name|MalformedURLException
name|ex
parameter_list|)
block|{
comment|// ignoring
name|logObj
operator|.
name|debug
argument_list|(
literal|"Malformed URL, ignoring."
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|super
operator|.
name|findResource
argument_list|(
name|location
argument_list|)
return|;
block|}
comment|/**      * Override ResourceLocator.addFilesystemPath(String) to intercept context paths      * starting with "/WEB-INF/" to place in additionalContextPaths.      */
annotation|@
name|Override
specifier|public
name|void
name|addFilesystemPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"/WEB-INF/"
argument_list|)
condition|)
block|{
name|this
operator|.
name|additionalContextPaths
operator|.
name|add
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|addFilesystemPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Path must not be null."
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

