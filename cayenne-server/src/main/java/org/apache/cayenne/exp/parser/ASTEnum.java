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
name|java
operator|.
name|util
operator|.
name|Objects
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * Scalar node that represents constant enumeration value.  * It resolves actual value at a late stage to be parsable in environment where  * final Enum class is not known, i.e. in Modeler.  *  * @since 4.1  */
end_comment

begin_class
specifier|public
class|class
name|ASTEnum
extends|extends
name|ASTScalar
block|{
name|ASTEnum
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
name|ASTEnum
parameter_list|()
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTENUM
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ASTEnum
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|ExpressionParserTreeConstants
operator|.
name|JJTENUM
argument_list|)
expr_stmt|;
name|setValue
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
block|{
return|return
name|getValueAsEnum
argument_list|()
operator|.
name|resolve
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Expression
name|shallowCopy
parameter_list|()
block|{
name|ASTScalar
name|copy
init|=
operator|new
name|ASTEnum
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|copy
operator|.
name|value
operator|=
name|value
expr_stmt|;
return|return
name|copy
return|;
block|}
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
name|Object
name|scalar
init|=
name|getValueAsEnum
argument_list|()
operator|.
name|resolve
argument_list|()
decl_stmt|;
name|SimpleNode
operator|.
name|encodeScalarAsEJBQL
argument_list|(
name|parameterAccumulator
argument_list|,
name|out
argument_list|,
name|scalar
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getValue
parameter_list|()
block|{
return|return
name|getValueAsEnum
argument_list|()
operator|.
name|resolve
argument_list|()
return|;
block|}
specifier|public
name|void
name|setEnumValue
parameter_list|(
name|String
name|enumPath
parameter_list|)
throws|throws
name|ParseException
block|{
if|if
condition|(
name|enumPath
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Null 'enumPath'"
argument_list|)
throw|;
block|}
name|int
name|dot
init|=
name|enumPath
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|dot
operator|<=
literal|0
operator|||
name|dot
operator|==
name|enumPath
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|ParseException
argument_list|(
literal|"Invalid enum path: "
operator|+
name|enumPath
argument_list|)
throw|;
block|}
name|String
name|className
init|=
name|enumPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|dot
argument_list|)
decl_stmt|;
name|String
name|enumName
init|=
name|enumPath
operator|.
name|substring
argument_list|(
name|dot
operator|+
literal|1
argument_list|)
decl_stmt|;
name|setValue
argument_list|(
operator|new
name|ASTEnum
operator|.
name|EnumValue
argument_list|(
name|className
argument_list|,
name|enumName
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|EnumValue
name|getValueAsEnum
parameter_list|()
block|{
return|return
operator|(
name|EnumValue
operator|)
name|value
return|;
block|}
specifier|static
specifier|final
class|class
name|EnumValue
block|{
name|String
name|className
decl_stmt|;
name|String
name|enumName
decl_stmt|;
name|EnumValue
parameter_list|(
name|String
name|className
parameter_list|,
name|String
name|enumName
parameter_list|)
block|{
name|this
operator|.
name|className
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|className
argument_list|)
expr_stmt|;
name|this
operator|.
name|enumName
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|enumName
argument_list|)
expr_stmt|;
block|}
name|Enum
argument_list|<
name|?
argument_list|>
name|resolve
parameter_list|()
block|{
name|Class
name|enumClass
decl_stmt|;
try|try
block|{
name|enumClass
operator|=
name|Util
operator|.
name|getJavaClass
argument_list|(
name|className
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Enum class not found: "
operator|+
name|className
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|enumClass
operator|.
name|isEnum
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Specified class is not an enum: "
operator|+
name|className
argument_list|)
throw|;
block|}
try|try
block|{
return|return
name|Enum
operator|.
name|valueOf
argument_list|(
name|enumClass
argument_list|,
name|enumName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Invalid enum path: "
operator|+
name|className
operator|+
literal|"."
operator|+
name|enumName
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|EnumValue
operator|.
name|class
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
return|return
literal|false
return|;
name|EnumValue
name|enumValue
init|=
operator|(
name|EnumValue
operator|)
name|o
decl_stmt|;
return|return
name|className
operator|.
name|equals
argument_list|(
name|enumValue
operator|.
name|className
argument_list|)
operator|&&
name|enumName
operator|.
name|equals
argument_list|(
name|enumValue
operator|.
name|enumName
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|className
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|enumName
operator|.
name|hashCode
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"enum:"
operator|+
name|className
operator|+
literal|"."
operator|+
name|enumName
return|;
block|}
block|}
block|}
end_class

end_unit

