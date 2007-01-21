begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Copyright 2006 The Apache Software Foundation  *  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|profile
operator|.
name|servlet
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
name|http
operator|.
name|HttpServlet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|access
operator|.
name|DataContext
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
name|profile
operator|.
name|AbstractCase
import|;
end_import

begin_comment
comment|/**  * A main servlet of the profiler web app. Accepts URLs like  * "/servlet-path/nosession/CaseClass" and "/servlet-path/session/CaseClass".  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ProfileServlet
extends|extends
name|HttpServlet
block|{
specifier|public
specifier|static
specifier|final
name|String
name|CASE_PACKAGE
init|=
literal|"org.apache.cayenne.profile.cases."
decl_stmt|;
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|ServletException
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|doGet
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|DataContext
name|context
init|=
name|contextForRequest
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|caseForRequest
argument_list|(
name|request
argument_list|)
operator|.
name|doGet
argument_list|(
name|context
argument_list|,
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|doPost
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|DataContext
name|context
init|=
name|contextForRequest
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|caseForRequest
argument_list|(
name|request
argument_list|)
operator|.
name|doPost
argument_list|(
name|context
argument_list|,
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|DataContext
name|contextForRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
if|if
condition|(
name|request
operator|.
name|getPathInfo
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"/session/"
argument_list|)
condition|)
block|{
return|return
name|DataContext
operator|.
name|getThreadDataContext
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|DataContext
operator|.
name|createDataContext
argument_list|()
return|;
block|}
block|}
specifier|protected
name|AbstractCase
name|caseForRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
throws|throws
name|ServletException
block|{
comment|// everything after the first path component is the case class...
name|String
name|path
init|=
name|request
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
name|int
name|slash
init|=
name|path
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|,
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|slash
operator|<
literal|0
operator|||
name|slash
operator|+
literal|1
operator|==
name|path
operator|.
name|length
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Invalid case path: "
operator|+
name|path
argument_list|)
throw|;
block|}
name|String
name|caseName
init|=
name|CASE_PACKAGE
operator|+
name|path
operator|.
name|substring
argument_list|(
name|slash
operator|+
literal|1
argument_list|)
decl_stmt|;
try|try
block|{
return|return
operator|(
name|AbstractCase
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|caseName
argument_list|,
literal|true
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Error instantiating case '"
operator|+
name|caseName
operator|+
literal|"'"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

