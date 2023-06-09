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
name|velocity
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
name|CayenneRuntimeException
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
name|jdbc
operator|.
name|ColumnDescriptor
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
name|jdbc
operator|.
name|SQLStatement
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
name|jdbc
operator|.
name|SQLTemplateProcessor
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
name|translator
operator|.
name|ParameterBinding
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
name|exp
operator|.
name|ExpressionException
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
name|template
operator|.
name|SQLTemplateRenderingUtils
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
name|Template
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
name|ASTReference
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
name|visitor
operator|.
name|BaseVisitor
import|;
end_import

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

begin_comment
comment|/**  * Processor for SQL velocity templates.  *   * @see org.apache.cayenne.query.SQLTemplate  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|VelocitySQLTemplateProcessor
implements|implements
name|SQLTemplateProcessor
block|{
specifier|private
specifier|final
class|class
name|PositionalParamMapper
extends|extends
name|BaseVisitor
block|{
specifier|private
name|int
name|i
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|positionalParams
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
decl_stmt|;
name|PositionalParamMapper
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|positionalParams
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
parameter_list|)
block|{
name|this
operator|.
name|positionalParams
operator|=
name|positionalParams
expr_stmt|;
name|this
operator|.
name|params
operator|=
name|params
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|visit
parameter_list|(
name|ASTReference
name|node
parameter_list|,
name|Object
name|data
parameter_list|)
block|{
comment|// strip off leading "$"
name|String
name|paramName
init|=
name|node
operator|.
name|getFirstToken
argument_list|()
operator|.
name|image
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// only consider the first instance of each named parameter
if|if
condition|(
operator|!
name|params
operator|.
name|containsKey
argument_list|(
name|paramName
argument_list|)
condition|)
block|{
if|if
condition|(
name|i
operator|>=
name|positionalParams
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ExpressionException
argument_list|(
literal|"Too few parameters to bind template: "
operator|+
name|positionalParams
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
name|params
operator|.
name|put
argument_list|(
name|paramName
argument_list|,
name|positionalParams
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
return|return
name|data
return|;
block|}
name|void
name|onFinish
parameter_list|()
block|{
if|if
condition|(
name|i
operator|<
name|positionalParams
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ExpressionException
argument_list|(
literal|"Too many parameters to bind template. Expected: "
operator|+
name|i
operator|+
literal|", actual: "
operator|+
name|positionalParams
operator|.
name|size
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
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
specifier|protected
name|RuntimeInstance
name|velocityRuntime
decl_stmt|;
specifier|protected
name|SQLTemplateRenderingUtils
name|renderingUtils
decl_stmt|;
specifier|public
name|VelocitySQLTemplateProcessor
parameter_list|()
block|{
name|this
operator|.
name|renderingUtils
operator|=
operator|new
name|SQLTemplateRenderingUtils
argument_list|()
expr_stmt|;
name|this
operator|.
name|velocityRuntime
operator|=
operator|new
name|RuntimeInstance
argument_list|()
expr_stmt|;
name|velocityRuntime
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
name|velocityRuntime
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
name|velocityRuntime
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
name|velocityRuntime
operator|.
name|addProperty
argument_list|(
literal|"userdirective"
argument_list|,
name|BindObjectEqualDirective
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|velocityRuntime
operator|.
name|addProperty
argument_list|(
literal|"userdirective"
argument_list|,
name|BindObjectNotEqualDirective
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|velocityRuntime
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
name|velocityRuntime
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
name|velocityRuntime
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
name|velocityRuntime
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
comment|/** 	 * Builds and returns a SQLStatement based on SQL template and a set of 	 * parameters. During rendering, VelocityContext exposes the following as 	 * variables: all parameters in the map, {@link SQLTemplateRenderingUtils} 	 * as a "helper" variable and SQLStatement object as "statement" variable. 	 */
annotation|@
name|Override
specifier|public
name|SQLStatement
name|processTemplate
parameter_list|(
name|String
name|template
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|parameters
parameter_list|)
block|{
comment|// have to make a copy of parameter map since we are gonna modify it..
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
argument_list|<>
argument_list|(
name|parameters
argument_list|)
else|:
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|SimpleNode
name|parsedTemplate
init|=
name|parse
argument_list|(
name|template
argument_list|)
decl_stmt|;
return|return
name|processTemplate
argument_list|(
name|template
argument_list|,
name|parsedTemplate
argument_list|,
name|internalParameters
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|SQLStatement
name|processTemplate
parameter_list|(
name|String
name|template
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|positionalParameters
parameter_list|)
block|{
name|SimpleNode
name|parsedTemplate
init|=
name|parse
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|internalParameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|PositionalParamMapper
name|visitor
init|=
operator|new
name|PositionalParamMapper
argument_list|(
name|positionalParameters
argument_list|,
name|internalParameters
argument_list|)
decl_stmt|;
name|parsedTemplate
operator|.
name|jjtAccept
argument_list|(
name|visitor
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|visitor
operator|.
name|onFinish
argument_list|()
expr_stmt|;
return|return
name|processTemplate
argument_list|(
name|template
argument_list|,
name|parsedTemplate
argument_list|,
name|internalParameters
argument_list|)
return|;
block|}
name|SQLStatement
name|processTemplate
parameter_list|(
name|String
name|template
parameter_list|,
name|SimpleNode
name|parsedTemplate
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|List
argument_list|<
name|ParameterBinding
argument_list|>
name|bindings
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ColumnDescriptor
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|put
argument_list|(
name|BINDINGS_LIST_KEY
argument_list|,
name|bindings
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|put
argument_list|(
name|RESULT_COLUMNS_LIST_KEY
argument_list|,
name|results
argument_list|)
expr_stmt|;
name|parameters
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
decl_stmt|;
try|try
block|{
name|sql
operator|=
name|buildStatement
argument_list|(
operator|new
name|VelocityContext
argument_list|(
name|parameters
argument_list|)
argument_list|,
name|template
argument_list|,
name|parsedTemplate
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error processing Velocity template"
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
name|SimpleNode
name|parsedTemplate
parameter_list|)
throws|throws
name|Exception
block|{
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
try|try
block|{
name|parsedTemplate
operator|.
name|init
argument_list|(
name|ica
argument_list|,
name|velocityRuntime
argument_list|)
expr_stmt|;
name|parsedTemplate
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
specifier|private
name|SimpleNode
name|parse
parameter_list|(
name|String
name|template
parameter_list|)
block|{
name|SimpleNode
name|nodeTree
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
operator|new
name|Template
argument_list|()
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
literal|"Error parsing template '%s' : %s"
argument_list|,
name|template
argument_list|,
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
literal|"Error parsing template %s"
argument_list|,
name|template
argument_list|)
throw|;
block|}
return|return
name|nodeTree
return|;
block|}
block|}
end_class

end_unit

