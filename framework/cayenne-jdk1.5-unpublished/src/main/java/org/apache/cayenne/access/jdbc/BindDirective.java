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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|Iterator
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
name|dba
operator|.
name|TypesMapping
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
name|ConversionUtil
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
name|InternalContextAdapter
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
name|exception
operator|.
name|MethodInvocationException
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
name|exception
operator|.
name|ParseErrorException
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
name|exception
operator|.
name|ResourceNotFoundException
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
name|directive
operator|.
name|Directive
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
name|Node
import|;
end_import

begin_comment
comment|/**  * A custom Velocity directive to create a PreparedStatement parameter text. There are the  * following possible invocation formats inside the template:  *   *<pre>  * #bind(value) - e.g. #bind($xyz)  * #bind(value jdbc_type_name) - e.g. #bind($xyz 'VARCHAR'). This is the most common and useful form.  * #bind(value jdbc_type_name, precision) - e.g. #bind($xyz 'VARCHAR' 2)  *</pre>  *<p>  * Other examples:  *</p>  *<p>  *<strong>Binding literal parameter value:</strong>  *</p>  *<p>  *<code>"WHERE SOME_COLUMN> #bind($xyz)"</code> produces  *<code>"WHERE SOME_COLUMN> ?"</code> and also places the value of the "xyz" parameter  * in the context "bindings" collection.  *</p>  *<p>  *<strong>Binding ID column of a DataObject value:</strong>  *</p>  *<p>  *<code>"WHERE ID_COL1 = #bind($helper.cayenneExp($xyz, 'db:ID_COL2'))   * AND ID_COL2 = #bind($helper.cayenneExp($xyz, 'db:ID_COL2'))"</code> produces<code>"WHERE ID_COL1 = ? AND ID_COL2 = ?"</code> and also places the  * values of id columns of the DataObject parameter "xyz" in the context "bindings"  * collection.  *</p>  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|BindDirective
extends|extends
name|Directive
block|{
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"bind"
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|LINE
return|;
block|}
comment|/**      * Extracts the value of the object property to render and passes control to      * {@link #render(InternalContextAdapter, Writer, ParameterBinding)} to do the actual      * rendering.      */
annotation|@
name|Override
specifier|public
name|boolean
name|render
parameter_list|(
name|InternalContextAdapter
name|context
parameter_list|,
name|Writer
name|writer
parameter_list|,
name|Node
name|node
parameter_list|)
throws|throws
name|IOException
throws|,
name|ResourceNotFoundException
throws|,
name|ParseErrorException
throws|,
name|MethodInvocationException
block|{
name|Object
name|value
init|=
name|getChild
argument_list|(
name|context
argument_list|,
name|node
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|Object
name|type
init|=
name|getChild
argument_list|(
name|context
argument_list|,
name|node
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|int
name|precision
init|=
name|ConversionUtil
operator|.
name|toInt
argument_list|(
name|getChild
argument_list|(
name|context
argument_list|,
name|node
argument_list|,
literal|2
argument_list|)
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
name|String
name|typeString
init|=
name|type
operator|!=
literal|null
condition|?
name|type
operator|.
name|toString
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Collection
condition|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
operator|(
operator|(
name|Collection
operator|)
name|value
operator|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|render
argument_list|(
name|context
argument_list|,
name|writer
argument_list|,
name|node
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|,
name|typeString
argument_list|,
name|precision
argument_list|)
expr_stmt|;
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|writer
operator|.
name|write
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|render
argument_list|(
name|context
argument_list|,
name|writer
argument_list|,
name|node
argument_list|,
name|value
argument_list|,
name|typeString
argument_list|,
name|precision
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * @since 3.0      */
specifier|protected
name|void
name|render
parameter_list|(
name|InternalContextAdapter
name|context
parameter_list|,
name|Writer
name|writer
parameter_list|,
name|Node
name|node
parameter_list|,
name|Object
name|value
parameter_list|,
name|String
name|typeString
parameter_list|,
name|int
name|precision
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParseErrorException
block|{
name|int
name|jdbcType
init|=
name|TypesMapping
operator|.
name|NOT_DEFINED
decl_stmt|;
if|if
condition|(
name|typeString
operator|!=
literal|null
condition|)
block|{
name|jdbcType
operator|=
name|TypesMapping
operator|.
name|getSqlTypeByName
argument_list|(
name|typeString
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|jdbcType
operator|=
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|jdbcType
operator|==
name|TypesMapping
operator|.
name|NOT_DEFINED
condition|)
block|{
throw|throw
operator|new
name|ParseErrorException
argument_list|(
literal|"Can't determine JDBC type of binding ("
operator|+
name|value
operator|+
literal|", "
operator|+
name|typeString
operator|+
literal|") at line "
operator|+
name|node
operator|.
name|getLine
argument_list|()
operator|+
literal|", column "
operator|+
name|node
operator|.
name|getColumn
argument_list|()
argument_list|)
throw|;
block|}
name|render
argument_list|(
name|context
argument_list|,
name|writer
argument_list|,
operator|new
name|ParameterBinding
argument_list|(
name|value
argument_list|,
name|jdbcType
argument_list|,
name|precision
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|render
parameter_list|(
name|InternalContextAdapter
name|context
parameter_list|,
name|Writer
name|writer
parameter_list|,
name|ParameterBinding
name|binding
parameter_list|)
throws|throws
name|IOException
block|{
name|bind
argument_list|(
name|context
argument_list|,
name|binding
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Object
name|getChild
parameter_list|(
name|InternalContextAdapter
name|context
parameter_list|,
name|Node
name|node
parameter_list|,
name|int
name|i
parameter_list|)
throws|throws
name|MethodInvocationException
block|{
return|return
operator|(
name|i
operator|>=
literal|0
operator|&&
name|i
operator|<
name|node
operator|.
name|jjtGetNumChildren
argument_list|()
operator|)
condition|?
name|node
operator|.
name|jjtGetChild
argument_list|(
name|i
argument_list|)
operator|.
name|value
argument_list|(
name|context
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**      * Adds value to the list of bindings in the context.      */
specifier|protected
name|void
name|bind
parameter_list|(
name|InternalContextAdapter
name|context
parameter_list|,
name|ParameterBinding
name|binding
parameter_list|)
block|{
name|Collection
name|bindings
init|=
operator|(
name|Collection
operator|)
name|context
operator|.
name|getInternalUserContext
argument_list|()
operator|.
name|get
argument_list|(
name|SQLTemplateProcessor
operator|.
name|BINDINGS_LIST_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|bindings
operator|!=
literal|null
condition|)
block|{
name|bindings
operator|.
name|add
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

