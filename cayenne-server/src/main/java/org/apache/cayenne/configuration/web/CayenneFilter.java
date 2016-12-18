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
name|web
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
name|configuration
operator|.
name|CayenneRuntime
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
name|server
operator|.
name|ServerModule
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
name|server
operator|.
name|ServerRuntime
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

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterChain
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterConfig
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
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
import|;
end_import

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

begin_comment
comment|/**  * A filter that creates a Cayenne server runtime, possibly including custom modules. By  * default runtime includes {@link ServerModule} and {@link WebModule}. Any custom modules  * are loaded after the two standard ones to allow custom service overrides. Filter  * initialization parameters:  *<ul>  *<li>configuration-location - (optional) a name of Cayenne configuration XML file that  * will be used to load Cayenne stack. If missing, the filter name will be used to derive  * the location. ".xml" extension will be appended to the filter name to get the location,  * so a filter named "cayenne-foo" will result in location "cayenne-foo.xml".  *<li>extra-modules - (optional) a comma or space-separated list of class names, with  * each class implementing {@link Module} interface. These are the custom modules loaded  * after the two standard ones that allow users to override any Cayenne runtime aspects,  * e.g. {@link RequestHandler}. Each custom module must have a no-arg constructor.  *</ul>  *<p>  * CayenneFilter is a great utility to quickly start a Cayenne application. More advanced  * apps most likely will not use it, relying on their own configuration mechanism (such as  * Guice, Spring, etc.)  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|CayenneFilter
implements|implements
name|Filter
block|{
specifier|protected
name|ServletContext
name|servletContext
decl_stmt|;
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|config
parameter_list|)
throws|throws
name|ServletException
block|{
name|checkAlreadyConfigured
argument_list|(
name|config
operator|.
name|getServletContext
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|servletContext
operator|=
name|config
operator|.
name|getServletContext
argument_list|()
expr_stmt|;
name|WebConfiguration
name|configAdapter
init|=
operator|new
name|WebConfiguration
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|String
name|configurationLocation
init|=
name|configAdapter
operator|.
name|getConfigurationLocation
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Module
argument_list|>
name|modules
init|=
name|configAdapter
operator|.
name|createModules
argument_list|(
operator|new
name|WebModule
argument_list|()
argument_list|)
decl_stmt|;
name|modules
operator|.
name|addAll
argument_list|(
name|getAdditionalModules
argument_list|()
argument_list|)
expr_stmt|;
name|ServerRuntime
name|runtime
init|=
operator|new
name|ServerRuntime
argument_list|(
name|configurationLocation
argument_list|,
name|modules
operator|.
name|toArray
argument_list|(
operator|new
name|Module
index|[
name|modules
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
decl_stmt|;
name|WebUtil
operator|.
name|setCayenneRuntime
argument_list|(
name|config
operator|.
name|getServletContext
argument_list|()
argument_list|,
name|runtime
argument_list|)
expr_stmt|;
block|}
comment|/**      * Subclasses may override this to specify additional modules that should be included when creating the CayenneRuntime (in addition to those specified in the web.xml file).      *       * @since 4.0      */
specifier|protected
name|Collection
argument_list|<
name|Module
argument_list|>
name|getAdditionalModules
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|()
return|;
block|}
specifier|protected
name|void
name|checkAlreadyConfigured
parameter_list|(
name|ServletContext
name|context
parameter_list|)
throws|throws
name|ServletException
block|{
comment|// sanity check
if|if
condition|(
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|context
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"CayenneRuntime is already configured in the servlet environment"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|CayenneRuntime
name|runtime
init|=
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|servletContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|runtime
operator|!=
literal|null
condition|)
block|{
name|runtime
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|CayenneRuntime
name|runtime
init|=
name|WebUtil
operator|.
name|getCayenneRuntime
argument_list|(
name|servletContext
argument_list|)
decl_stmt|;
name|RequestHandler
name|handler
init|=
name|runtime
operator|.
name|getInjector
argument_list|()
operator|.
name|getInstance
argument_list|(
name|RequestHandler
operator|.
name|class
argument_list|)
decl_stmt|;
name|handler
operator|.
name|requestStart
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
try|try
block|{
name|chain
operator|.
name|doFilter
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|handler
operator|.
name|requestEnd
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

