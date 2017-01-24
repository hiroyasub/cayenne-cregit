begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *<p/>  * http://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  ****************************************************************/
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
name|translator
operator|.
name|procedure
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
name|Connection
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
name|translator
operator|.
name|ProcedureParameterBinding
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
name|types
operator|.
name|ExtendedType
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
name|DbAdapter
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
name|log
operator|.
name|JdbcEventLogger
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
name|log
operator|.
name|NoopJdbcEventLogger
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
name|EntityResolver
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
comment|/**  * Stored procedure query translator.  */
end_comment

begin_class
specifier|public
class|class
name|ProcedureTranslator
block|{
comment|/** 	 * Helper class to make OUT and VOID parameters logger-friendly. 	 */
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
name|ProcedureQuery
name|query
decl_stmt|;
specifier|protected
name|Connection
name|connection
decl_stmt|;
specifier|protected
name|DbAdapter
name|adapter
decl_stmt|;
specifier|protected
name|EntityResolver
name|entityResolver
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
name|Object
argument_list|>
name|values
decl_stmt|;
specifier|protected
name|JdbcEventLogger
name|logger
decl_stmt|;
specifier|public
name|ProcedureTranslator
parameter_list|()
block|{
name|this
operator|.
name|logger
operator|=
name|NoopJdbcEventLogger
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setQuery
parameter_list|(
name|ProcedureQuery
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
specifier|public
name|void
name|setConnection
parameter_list|(
name|Connection
name|connection
parameter_list|)
block|{
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
block|}
specifier|public
name|void
name|setAdapter
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
block|}
comment|/** 	 * @since 3.1 	 */
specifier|public
name|void
name|setJdbcEventLogger
parameter_list|(
name|JdbcEventLogger
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
comment|/** 	 * @since 3.1 	 */
specifier|public
name|JdbcEventLogger
name|getJdbcEventLogger
parameter_list|()
block|{
return|return
name|logger
return|;
block|}
comment|/** 	 * @since 1.2 	 */
specifier|public
name|void
name|setEntityResolver
parameter_list|(
name|EntityResolver
name|entityResolver
parameter_list|)
block|{
name|this
operator|.
name|entityResolver
operator|=
name|entityResolver
expr_stmt|;
block|}
comment|/** 	 * Creates an SQL String for the stored procedure call. 	 */
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
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
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
comment|/** 	 * Creates and binds a PreparedStatement to execute query SQL via JDBC. 	 */
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
argument_list|<>
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
name|logger
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
argument_list|<
name|Object
argument_list|>
name|loggableParameters
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|values
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|val
range|:
name|values
control|)
block|{
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
name|logger
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
name|query
operator|.
name|getMetaData
argument_list|(
name|entityResolver
argument_list|)
operator|.
name|getProcedure
argument_list|()
return|;
block|}
specifier|public
name|ProcedureQuery
name|getProcedureQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
comment|/** 	 * Set IN and OUT parameters. 	 */
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
name|?
argument_list|>
name|queryValues
init|=
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
comment|/** 	 * Sets a single IN parameter of the CallableStatement. 	 */
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
name|ExtendedType
name|extendedType
init|=
name|val
operator|!=
literal|null
condition|?
name|adapter
operator|.
name|getExtendedTypes
argument_list|()
operator|.
name|getRegisteredType
argument_list|(
name|val
operator|.
name|getClass
argument_list|()
argument_list|)
else|:
name|adapter
operator|.
name|getExtendedTypes
argument_list|()
operator|.
name|getDefaultType
argument_list|()
decl_stmt|;
name|ProcedureParameterBinding
name|binding
init|=
operator|new
name|ProcedureParameterBinding
argument_list|(
name|param
argument_list|)
decl_stmt|;
name|binding
operator|.
name|setStatementPosition
argument_list|(
name|pos
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setValue
argument_list|(
name|val
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setExtendedType
argument_list|(
name|extendedType
argument_list|)
expr_stmt|;
name|adapter
operator|.
name|bindParameter
argument_list|(
name|stmt
argument_list|,
name|binding
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Sets a single OUT parameter of the CallableStatement. 	 */
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

