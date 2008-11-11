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
name|dba
operator|.
name|hsqldb
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
name|access
operator|.
name|trans
operator|.
name|ProcedureTranslator
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

begin_comment
comment|/**  * Works around HSQLDB's pickiness about stored procedure syntax.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|HSQLDBProcedureTranslator
extends|extends
name|ProcedureTranslator
block|{
comment|/**      * Creates HSQLDB-compliant SQL to execute a stored procedure.      */
annotation|@
name|Override
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
comment|// HSQL won't accept "? =". The parser only recognizes "?="
comment|// TODO: Andrus, 12/12/2005 - this is kind of how it is in the
comment|// CallableStatement javadocs, so we may need to make "?=" a default ... this
comment|// requires testing on Oracle/PostgreSQL/Sybase/SQLServer.
name|buf
operator|.
name|append
argument_list|(
literal|"{?= call "
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
comment|// HSQLDB requires that procedures with periods (referring to Java packages)
comment|// be enclosed in quotes. It is not clear that quotes can always be used, though
if|if
condition|(
name|procedure
operator|.
name|getFullyQualifiedName
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
operator|.
name|append
argument_list|(
name|procedure
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
block|}
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
block|}
end_class

end_unit

