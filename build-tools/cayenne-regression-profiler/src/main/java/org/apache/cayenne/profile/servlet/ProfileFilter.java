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
name|ServletException
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
name|conf
operator|.
name|WebApplicationContextFilter
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
name|TestDataSourceFactory
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
name|util
operator|.
name|LocalizedStringsHandler
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
comment|/**  * A filter that sets up DB schema.  */
end_comment

begin_class
specifier|public
class|class
name|ProfileFilter
extends|extends
name|WebApplicationContextFilter
block|{
specifier|protected
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|public
specifier|synchronized
name|void
name|init
parameter_list|(
name|FilterConfig
name|config
parameter_list|)
throws|throws
name|ServletException
block|{
comment|// start Cayenne stack
name|super
operator|.
name|init
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|String
name|cayenneVersion
init|=
name|LocalizedStringsHandler
operator|.
name|getString
argument_list|(
literal|"cayenne.version"
argument_list|)
decl_stmt|;
if|if
condition|(
name|cayenneVersion
operator|==
literal|null
condition|)
block|{
name|cayenneVersion
operator|=
literal|"unknown"
expr_stmt|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"Started Cayenne... Version - '"
operator|+
name|cayenneVersion
operator|+
literal|"'; connection: '"
operator|+
name|TestDataSourceFactory
operator|.
name|getDataSourceName
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

