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
name|trans
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|CallableStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
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
name|Iterator
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
name|access
operator|.
name|QueryLogger
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
name|QueryTranslator
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
name|Procedure
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
name|ProcedureParameter
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
name|query
operator|.
name|ProcedureQuery
import|;
end_import

begin_comment
comment|/**  * Stored procedure query translator.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ProcedureTranslator
extends|extends
name|QueryTranslator
block|{
comment|/**      * Helper class to make OUT and VOID parameters logger-friendly.      */
specifier|static
class|class
name|NotInParam
block|{
specifier|protected
name|String
name|type
decl_stmt|;
specifier|public
name|NotInParam
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|type
return|;
block|}
block|}
specifier|private
specifier|static
name|NotInParam
name|OUT_PARAM
init|=
operator|new
name|NotInParam
argument_list|(
literal|"[OUT]"
argument_list|)
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|ProcedureParameter
argument_list|>
name|callParams
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|NotInParam
argument_list|>
name|values
decl_stmt|;
comment|/**      * Creates an SQL String for the stored procedure call.      */
specifier|protected
name|String
name|createSqlString
parameter_list|()
block|{
name|Procedure
name|procedure
init|=
name|getProcedure
argument_list|()
decl_stmt|;
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|int
name|totalParams
init|=
name|callParams
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// check if procedure returns values
if|if
condition|(
name|procedure
operator|.
name|isReturningValue
argument_list|()
condition|)
block|{
name|totalParams
operator|--
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"{? = call "
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"{call "
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
name|procedure
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|totalParams
operator|>
literal|0
condition|)
block|{
comment|// unroll the loop
name|buf
operator|.
name|append
argument_list|(
literal|"(?"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|totalParams
condition|;
name|i
operator|++
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|", ?"
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|PreparedStatement
name|createStatement
parameter_list|()
throws|throws
name|Exception
block|{
name|long
name|t1
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|this
operator|.
name|callParams
operator|=
name|getProcedure
argument_list|()
operator|.
name|getCallParameters
argument_list|()
expr_stmt|;
name|this
operator|.
name|values
operator|=
operator|new
name|ArrayList
argument_list|<
name|NotInParam
argument_list|>
argument_list|(
name|callParams
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|initValues
argument_list|()
expr_stmt|;
name|String
name|sqlStr
init|=
name|createSqlString
argument_list|()
decl_stmt|;
if|if
condition|(
name|QueryLogger
operator|.
name|isLoggable
argument_list|()
condition|)
block|{
comment|// need to convert OUT/VOID parameters to loggable strings
name|long
name|time
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|t1
decl_stmt|;
name|List
name|loggableParameters
init|=
operator|new
name|ArrayList
argument_list|(
name|values
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|NotInParam
argument_list|>
name|it
init|=
name|values
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
name|Object
name|val
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|val
operator|instanceof
name|NotInParam
condition|)
block|{
name|val
operator|=
name|val
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|loggableParameters
operator|.
name|add
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
comment|// FIXME: compute proper attributes via callParams
name|QueryLogger
operator|.
name|logQuery
argument_list|(
name|sqlStr
argument_list|,
literal|null
argument_list|,
name|loggableParameters
argument_list|,
name|time
argument_list|)
expr_stmt|;
block|}
name|CallableStatement
name|stmt
init|=
name|connection
operator|.
name|prepareCall
argument_list|(
name|sqlStr
argument_list|)
decl_stmt|;
name|initStatement
argument_list|(
name|stmt
argument_list|)
expr_stmt|;
return|return
name|stmt
return|;
block|}
specifier|public
name|Procedure
name|getProcedure
parameter_list|()
block|{
return|return
name|getEntityResolver
argument_list|()
operator|.
name|lookupProcedure
argument_list|(
name|query
argument_list|)
return|;
block|}
specifier|public
name|ProcedureQuery
name|getProcedureQuery
parameter_list|()
block|{
return|return
operator|(
name|ProcedureQuery
operator|)
name|query
return|;
block|}
comment|/**      * Set IN and OUT parameters.      */
specifier|protected
name|void
name|initStatement
parameter_list|(
name|CallableStatement
name|stmt
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|values
operator|!=
literal|null
operator|&&
name|values
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|List
argument_list|<
name|ProcedureParameter
argument_list|>
name|params
init|=
name|getProcedure
argument_list|()
operator|.
name|getCallParameters
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|values
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|ProcedureParameter
name|param
init|=
name|params
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
comment|// !Stored procedure parameter can be both in and out
comment|// at the same time
if|if
condition|(
name|param
operator|.
name|isOutParam
argument_list|()
condition|)
block|{
name|setOutParam
argument_list|(
name|stmt
argument_list|,
name|param
argument_list|,
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|param
operator|.
name|isInParameter
argument_list|()
condition|)
block|{
name|setInParam
argument_list|(
name|stmt
argument_list|,
name|param
argument_list|,
name|values
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|initValues
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|NotInParam
argument_list|>
name|queryValues
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|NotInParam
argument_list|>
operator|)
name|getProcedureQuery
argument_list|()
operator|.
name|getParameters
argument_list|()
decl_stmt|;
comment|// match values with parameters in the correct order.
comment|// make an assumption that a missing value is NULL
comment|// Any reason why this is bad?
for|for
control|(
name|ProcedureParameter
name|param
range|:
name|callParams
control|)
block|{
if|if
condition|(
name|param
operator|.
name|getDirection
argument_list|()
operator|==
name|ProcedureParameter
operator|.
name|OUT_PARAMETER
condition|)
block|{
name|values
operator|.
name|add
argument_list|(
name|OUT_PARAM
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|values
operator|.
name|add
argument_list|(
name|queryValues
operator|.
name|get
argument_list|(
name|param
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Sets a single IN parameter of the CallableStatement.      */
specifier|protected
name|void
name|setInParam
parameter_list|(
name|CallableStatement
name|stmt
parameter_list|,
name|ProcedureParameter
name|param
parameter_list|,
name|Object
name|val
parameter_list|,
name|int
name|pos
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|type
init|=
name|param
operator|.
name|getType
argument_list|()
decl_stmt|;
name|adapter
operator|.
name|bindParameter
argument_list|(
name|stmt
argument_list|,
name|val
argument_list|,
name|pos
argument_list|,
name|type
argument_list|,
name|param
operator|.
name|getPrecision
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets a single OUT parameter of the CallableStatement.      */
specifier|protected
name|void
name|setOutParam
parameter_list|(
name|CallableStatement
name|stmt
parameter_list|,
name|ProcedureParameter
name|param
parameter_list|,
name|int
name|pos
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|precision
init|=
name|param
operator|.
name|getPrecision
argument_list|()
decl_stmt|;
if|if
condition|(
name|precision
operator|>=
literal|0
condition|)
block|{
name|stmt
operator|.
name|registerOutParameter
argument_list|(
name|pos
argument_list|,
name|param
operator|.
name|getType
argument_list|()
argument_list|,
name|precision
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stmt
operator|.
name|registerOutParameter
argument_list|(
name|pos
argument_list|,
name|param
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

