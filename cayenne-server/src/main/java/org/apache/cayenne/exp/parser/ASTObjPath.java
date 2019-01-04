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
name|exp
operator|.
name|parser
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
name|util
operator|.
name|List
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
name|cayenne
operator|.
name|DataObject
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
name|Expression
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
name|map
operator|.
name|Entity
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
name|map
operator|.
name|ObjEntity
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
name|reflect
operator|.
name|PropertyUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
specifier|public
class|class
name|ASTObjPath
extends|extends
name|ASTPath
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|3574281576491705706L
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ASTObjPath
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|OBJ_PREFIX
init|=
literal|"obj:"
decl_stmt|;
comment|/** 	 * Constructor used by expression parser. Do not invoke directly. 	 */
name|ASTObjPath
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTObjPath
parameter_list|()
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTOBJPATH
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTObjPath
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTOBJPATH
argument_list|)
expr_stmt|;
name|setPath
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|evaluateNode
parameter_list|(
name|Object
name|o
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|(
name|o
operator|instanceof
name|DataObject
operator|)
condition|?
operator|(
operator|(
name|DataObject
operator|)
name|o
operator|)
operator|.
name|readNestedProperty
argument_list|(
name|path
argument_list|)
else|:
operator|(
name|o
operator|instanceof
name|Entity
operator|)
condition|?
name|evaluateEntityNode
argument_list|(
operator|(
name|Entity
operator|)
name|o
argument_list|)
else|:
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|o
argument_list|,
name|path
argument_list|)
return|;
block|}
comment|/** 	 * Creates a copy of this expression node, without copying children. 	 */
annotation|@
name|Override
specifier|public
name|Expression
name|shallowCopy
parameter_list|()
block|{
name|ASTObjPath
name|copy
init|=
operator|new
name|ASTObjPath
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|copy
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|copy
operator|.
name|setPathAliases
argument_list|(
name|pathAliases
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
comment|/** 	 * @since 4.0 	 */
annotation|@
name|Override
specifier|public
name|void
name|appendAsEJBQL
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|parameterAccumulator
parameter_list|,
name|Appendable
name|out
parameter_list|,
name|String
name|rootId
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|append
argument_list|(
name|rootId
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
name|out
operator|.
name|append
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * @since 4.0 	 */
annotation|@
name|Override
specifier|public
name|void
name|appendAsString
parameter_list|(
name|Appendable
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|out
operator|.
name|append
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|Expression
operator|.
name|OBJ_PATH
return|;
block|}
name|void
name|injectValue
parameter_list|(
name|Object
name|source
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|getPath
argument_list|()
operator|.
name|contains
argument_list|(
name|ObjEntity
operator|.
name|PATH_SEPARATOR
argument_list|)
condition|)
block|{
try|try
block|{
if|if
condition|(
name|source
operator|instanceof
name|DataObject
condition|)
block|{
operator|(
operator|(
name|DataObject
operator|)
name|source
operator|)
operator|.
name|writeProperty
argument_list|(
name|getPath
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PropertyUtils
operator|.
name|setProperty
argument_list|(
name|source
argument_list|,
name|getPath
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|CayenneRuntimeException
name|ex
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Failed to inject value "
operator|+
name|value
operator|+
literal|" on path "
operator|+
name|getPath
argument_list|()
operator|+
literal|" to "
operator|+
name|source
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

