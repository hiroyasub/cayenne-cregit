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
name|HttpSession
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
name|BaseContext
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
name|ObjectContext
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
name|ObjectContextFactory
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
name|Injector
import|;
end_import

begin_comment
comment|/**  * Default implementation of the {@link RequestHandler} that stores per-user  * {@link ObjectContext} in a web session and binds it to request thread. Note that using  * this handler would force {@link HttpSession} creation, that may not be desirable in  * many cases. Also session-bound context may result in a race condition with two user  * requests updating the same persistent objects in parallel.  *<p>  * User applications in most cases should provide a custom RequestHandler that implements  * a smarter app-specific strategy for providing ObjectContext.  *<p>  * For stateless (per request) context creation use {@link StatelessContextRequestHandler}.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|SessionContextRequestHandler
implements|implements
name|RequestHandler
block|{
specifier|static
specifier|final
name|String
name|SESSION_CONTEXT_KEY
init|=
name|SessionContextRequestHandler
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".SESSION_CONTEXT"
decl_stmt|;
comment|// using injector to lookup services instead of injecting them directly for lazy
comment|// startup and "late binding"
annotation|@
name|Inject
specifier|private
name|Injector
name|injector
decl_stmt|;
specifier|public
name|void
name|requestStart
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|)
block|{
name|CayenneRuntime
operator|.
name|bindThreadInjector
argument_list|(
name|injector
argument_list|)
expr_stmt|;
if|if
condition|(
name|request
operator|instanceof
name|HttpServletRequest
condition|)
block|{
comment|// this forces session creation if it does not exist yet
name|HttpSession
name|session
init|=
operator|(
operator|(
name|HttpServletRequest
operator|)
name|request
operator|)
operator|.
name|getSession
argument_list|()
decl_stmt|;
name|ObjectContext
name|context
decl_stmt|;
synchronized|synchronized
init|(
name|session
init|)
block|{
name|context
operator|=
operator|(
name|ObjectContext
operator|)
name|session
operator|.
name|getAttribute
argument_list|(
name|SESSION_CONTEXT_KEY
argument_list|)
expr_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
name|injector
operator|.
name|getInstance
argument_list|(
name|ObjectContextFactory
operator|.
name|class
argument_list|)
operator|.
name|createContext
argument_list|()
expr_stmt|;
name|session
operator|.
name|setAttribute
argument_list|(
name|SESSION_CONTEXT_KEY
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
block|}
name|BaseContext
operator|.
name|bindThreadObjectContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|requestEnd
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|)
block|{
name|CayenneRuntime
operator|.
name|bindThreadInjector
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|BaseContext
operator|.
name|bindThreadObjectContext
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

