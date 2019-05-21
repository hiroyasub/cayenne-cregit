begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|ServletContext
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

begin_comment
comment|/**  * A helper class to retrieve and store {@link CayenneRuntime} in the  * {@link ServletContext}. All Cayenne web configuration objects, such as  * {@link CayenneFilter} and {@link org.apache.cayenne.rop.ROPServlet}, are using this class to access  * runtime.  *   * @since 3.1  */
end_comment

begin_class
specifier|public
class|class
name|WebUtil
block|{
specifier|static
specifier|final
name|String
name|CAYENNE_RUNTIME_KEY
init|=
name|WebUtil
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".CAYENNE_RUNTIME"
decl_stmt|;
comment|/**      * Retrieves CayenneRuntime previously stored in provided context via      * {@link #setCayenneRuntime(ServletContext, CayenneRuntime)}. May return null if no      * runtime was stored.      */
specifier|public
specifier|static
name|CayenneRuntime
name|getCayenneRuntime
parameter_list|(
name|ServletContext
name|context
parameter_list|)
block|{
return|return
operator|(
name|CayenneRuntime
operator|)
name|context
operator|.
name|getAttribute
argument_list|(
name|CAYENNE_RUNTIME_KEY
argument_list|)
return|;
block|}
comment|/**      * Stores {@link CayenneRuntime} in the servlet context. It can be later retrieve via      * {@link #getCayenneRuntime(ServletContext)}.      */
specifier|public
specifier|static
name|void
name|setCayenneRuntime
parameter_list|(
name|ServletContext
name|context
parameter_list|,
name|CayenneRuntime
name|runtime
parameter_list|)
block|{
name|context
operator|.
name|setAttribute
argument_list|(
name|CAYENNE_RUNTIME_KEY
argument_list|,
name|runtime
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

