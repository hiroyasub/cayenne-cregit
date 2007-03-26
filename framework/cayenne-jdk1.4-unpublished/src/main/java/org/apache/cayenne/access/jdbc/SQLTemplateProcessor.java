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
name|access
operator|.
name|jdbc
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|velocity
operator|.
name|VelocityContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|context
operator|.
name|InternalContextAdapterImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|RuntimeConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|RuntimeInstance
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|log
operator|.
name|NullLogSystem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|parser
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|velocity
operator|.
name|runtime
operator|.
name|parser
operator|.
name|node
operator|.
name|SimpleNode
import|;
end_import

begin_comment
comment|/**  * Processor for SQL velocity templates.  *   * @see org.apache.cayenne.query.SQLTemplate  * @since 1.1  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|SQLTemplateProcessor
block|{
specifier|private
specifier|static
name|RuntimeInstance
name|sharedRuntime
decl_stmt|;
specifier|static
specifier|final
name|String
name|BINDINGS_LIST_KEY
init|=
literal|"bindings"
decl_stmt|;
specifier|static
specifier|final
name|String
name|RESULT_COLUMNS_LIST_KEY
init|=
literal|"resultColumns"
decl_stmt|;
specifier|static
specifier|final
name|String
name|HELPER_KEY
init|=
literal|"helper"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|SQLTemplateRenderingUtils
name|sharedUtils
init|=
operator|new
name|SQLTemplateRenderingUtils
argument_list|()
decl_stmt|;
name|RuntimeInstance
name|velocityRuntime
decl_stmt|;
name|SQLTemplateRenderingUtils
name|renderingUtils
decl_stmt|;
static|static
block|{
name|initVelocityRuntime
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|initVelocityRuntime
parameter_list|()
block|{
comment|// init static velocity engine
name|sharedRuntime
operator|=
operator|new
name|RuntimeInstance
argument_list|()
expr_stmt|;
comment|// set null logger
name|sharedRuntime
operator|.
name|addProperty
argument_list|(
name|RuntimeConstants
operator|.
name|RUNTIME_LOG_LOGSYSTEM
argument_list|,
operator|new
name|NullLogSystem
argument_list|()
argument_list|)
expr_stmt|;
name|sharedRuntime
operator|.
name|addProperty
argument_list|(
name|RuntimeConstants
operator|.
name|RESOURCE_MANAGER_CLASS
argument_list|,
name|SQLTemplateResourceManager
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sharedRuntime
operator|.
name|addProperty
argument_list|(
literal|"userdirective"
argument_list|,
name|BindDirective
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sharedRuntime
operator|.
name|addProperty
argument_list|(
literal|"userdirective"
argument_list|,
name|BindEqualDirective
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sharedRuntime
operator|.
name|addProperty
argument_list|(
literal|"userdirective"
argument_list|,
name|BindNotEqualDirective
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sharedRuntime
operator|.
name|addProperty
argument_list|(
literal|"userdirective"
argument_list|,
name|ResultDirective
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sharedRuntime
operator|.
name|addProperty
argument_list|(
literal|"userdirective"
argument_list|,
name|ChainDirective
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sharedRuntime
operator|.
name|addProperty
argument_list|(
literal|"userdirective"
argument_list|,
name|ChunkDirective
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|sharedRuntime
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error setting up Velocity RuntimeInstance."
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
name|SQLTemplateProcessor
parameter_list|()
block|{
name|this
operator|.
name|velocityRuntime
operator|=
name|sharedRuntime
expr_stmt|;
name|this
operator|.
name|renderingUtils
operator|=
name|sharedUtils
expr_stmt|;
block|}
name|SQLTemplateProcessor
parameter_list|(
name|RuntimeInstance
name|velocityRuntime
parameter_list|,
name|SQLTemplateRenderingUtils
name|renderingUtils
parameter_list|)
block|{
name|this
operator|.
name|velocityRuntime
operator|=
name|velocityRuntime
expr_stmt|;
name|this
operator|.
name|renderingUtils
operator|=
name|renderingUtils
expr_stmt|;
block|}
comment|/**      * Builds and returns a SQLStatement based on SQL template and a set of parameters.      * During rendering, VelocityContext exposes the following as variables: all      * parameters in the map, {@link SQLTemplateRenderingUtils} as a "helper" variable and      * SQLStatement object as "statement" variable.      */
name|SQLStatement
name|processTemplate
parameter_list|(
name|String
name|template
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// have to make a copy of parameter map since we are gonna modify it..
name|Map
name|internalParameters
init|=
operator|(
name|parameters
operator|!=
literal|null
operator|&&
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
operator|)
condition|?
operator|new
name|HashMap
argument_list|(
name|parameters
argument_list|)
else|:
operator|new
name|HashMap
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|List
name|bindings
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|List
name|results
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|internalParameters
operator|.
name|put
argument_list|(
name|BINDINGS_LIST_KEY
argument_list|,
name|bindings
argument_list|)
expr_stmt|;
name|internalParameters
operator|.
name|put
argument_list|(
name|RESULT_COLUMNS_LIST_KEY
argument_list|,
name|results
argument_list|)
expr_stmt|;
name|internalParameters
operator|.
name|put
argument_list|(
name|HELPER_KEY
argument_list|,
name|renderingUtils
argument_list|)
expr_stmt|;
name|String
name|sql
init|=
name|buildStatement
argument_list|(
operator|new
name|VelocityContext
argument_list|(
name|internalParameters
argument_list|)
argument_list|,
name|template
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|ParameterBinding
index|[]
name|bindingsArray
init|=
operator|new
name|ParameterBinding
index|[
name|bindings
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|bindings
operator|.
name|toArray
argument_list|(
name|bindingsArray
argument_list|)
expr_stmt|;
name|ColumnDescriptor
index|[]
name|resultsArray
init|=
operator|new
name|ColumnDescriptor
index|[
name|results
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|results
operator|.
name|toArray
argument_list|(
name|resultsArray
argument_list|)
expr_stmt|;
return|return
operator|new
name|SQLStatement
argument_list|(
name|sql
argument_list|,
name|resultsArray
argument_list|,
name|bindingsArray
argument_list|)
return|;
block|}
name|String
name|buildStatement
parameter_list|(
name|VelocityContext
name|context
parameter_list|,
name|String
name|template
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Note: this method is a reworked version of
comment|// org.apache.velocity.app.Velocity.evaluate(..)
comment|// cleaned up to avoid using any Velocity singletons
name|StringWriter
name|out
init|=
operator|new
name|StringWriter
argument_list|(
name|template
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|SimpleNode
name|nodeTree
init|=
literal|null
decl_stmt|;
try|try
block|{
name|nodeTree
operator|=
name|velocityRuntime
operator|.
name|parse
argument_list|(
operator|new
name|StringReader
argument_list|(
name|template
argument_list|)
argument_list|,
name|template
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|pex
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error parsing template '"
operator|+
name|template
operator|+
literal|"' : "
operator|+
name|pex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|nodeTree
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error parsing template "
operator|+
name|template
argument_list|)
throw|;
block|}
comment|// ... not sure what InternalContextAdapter is for...
name|InternalContextAdapterImpl
name|ica
init|=
operator|new
name|InternalContextAdapterImpl
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ica
operator|.
name|pushCurrentTemplateName
argument_list|(
name|template
argument_list|)
expr_stmt|;
try|try
block|{
name|nodeTree
operator|.
name|init
argument_list|(
name|ica
argument_list|,
name|velocityRuntime
argument_list|)
expr_stmt|;
name|nodeTree
operator|.
name|render
argument_list|(
name|ica
argument_list|,
name|out
argument_list|)
expr_stmt|;
return|return
name|out
operator|.
name|toString
argument_list|()
return|;
block|}
finally|finally
block|{
name|ica
operator|.
name|popCurrentTemplateName
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

